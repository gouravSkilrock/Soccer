package com.skilrock.sle.pwtMgmt.daoImpl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import com.skilrock.sle.common.ConfigurationVariables;
import com.skilrock.sle.common.DBConnect;
import com.skilrock.sle.common.SLELogger;
import com.skilrock.sle.common.SportsLotteryUtils;
import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.UtilityFunctions;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.common.javaBeans.UserInfoBean;
import com.skilrock.sle.common.javaBeans.ValidateTicketBean;
import com.skilrock.sle.commonMethod.daoImpl.CommonMethodsDaoImpl;
import com.skilrock.sle.drawMgmt.controllerImpl.TrackTicketControllerImpl;
import com.skilrock.sle.merchant.common.javaBeans.MerchantInfoBean;
import com.skilrock.sle.merchant.lms.LMSIntegrationImpl;
import com.skilrock.sle.pwtMgmt.javaBeans.BoardTicketDataBean;
import com.skilrock.sle.pwtMgmt.javaBeans.PwtVerifyTicketBean;
import com.skilrock.sle.pwtMgmt.javaBeans.PwtVerifyTicketDrawDataBean;

public class PrizeTicketVerificationDaoImpl {
	private static final SLELogger logger = SLELogger.getLogger(PrizeTicketVerificationDaoImpl.class.getName());

	private static PrizeTicketVerificationDaoImpl singleInstance;

	private PrizeTicketVerificationDaoImpl(){}

	public static PrizeTicketVerificationDaoImpl getSingleInstance() {
		if (singleInstance == null) {
			synchronized (PrizeTicketVerificationDaoImpl.class) {
				if (singleInstance == null) {
					singleInstance = new PrizeTicketVerificationDaoImpl();
				}
			}
		}

		return singleInstance;
	}

	public PwtVerifyTicketBean prizeWinningVerifyTicket(UserInfoBean userBean, String winningMerchantName, String ticketNumber, Connection connection) throws SLEException {
		PwtVerifyTicketBean verifyTicketBean = null;
		
		PwtVerifyTicketDrawDataBean pwtVerifyDrawDataBean = null;
		PwtVerifyTicketDrawDataBean[] pwtVerifyTicketDrawDataArr = null;
		BoardTicketDataBean boardTicketDataBean = null;
		BoardTicketDataBean[] boardTicketdataBeanArr = null;

		String query = null;
		Statement drawStmt = null;
		ResultSet drawRs = null;
		Statement boardStmt = null;
		ResultSet boardRs = null;
		Statement cancelTktStmt = null;
		ResultSet cancelTktRs = null;
		String drawStatus = null;
		String purchaseDateTime = null;
		double totalPurchaseAmt = 0.0;
		double totalWinAmt = 0.0;
		boolean isTicketAvailable = false;
		boolean isBoardAvailable = false;
		String tktNum = null;
		int barCodeCount = 0;
		try {
			drawStmt = connection.createStatement();
			boardStmt = connection.createStatement();
			
			tktNum = ticketNumber;
			MerchantInfoBean merchantInfoBean=CommonMethodsDaoImpl.getInstance().fetchMerchantDetailFromTicket(ticketNumber, connection);
			if("Asoft".equalsIgnoreCase(merchantInfoBean.getMerchantDevName()) || "OKPOS".equalsIgnoreCase(merchantInfoBean.getMerchantDevName())){
				winningMerchantName=merchantInfoBean.getMerchantDevName();
			}else if("RMS".equalsIgnoreCase(winningMerchantName)){
				tktNum = TrackTicketControllerImpl.getTicketNumber(ticketNumber, 3);
			}
			ValidateTicketBean validateTktBean = new ValidateTicketBean(tktNum);
			SportsLotteryUtils.validateTicketPWT(validateTktBean, winningMerchantName, connection);
			if (validateTktBean.isValid()) {
				if("RMS".equalsIgnoreCase(winningMerchantName)){
					if(ticketNumber.length() == ConfigurationVariables.barCodeCountRMS){
						barCodeCount = SportsLotteryUtils.getBarCodeCountFromTicketNumber(ticketNumber);
						if(barCodeCount==0){
							throw new SLEException(SLEErrors.INVALID_TICKET_ERROR_CODE, SLEErrors.INVALID_TICKET_ERROR_MESSAGE);
						}
					}
				}
				validateTktBean.setBarCodeCount(barCodeCount);
				String saleMerchantName = validateTktBean.getMerchantCode();
				int saleMerchantId = UtilityFunctions.getMerchantIdFromMerchantName(saleMerchantName, connection);
				int gameId = validateTktBean.getGameid();
				int gameTypeId = validateTktBean.getGameTypeId();
				String barCodeAppender = " ";
				
					if(barCodeCount > 0){
						barCodeAppender = " and barcode_count = "+barCodeCount+" ";
					}
						

				query = "SELECT draw.draw_id, draw_no, draw_datetime, draw_status, purchase_table_name, draw_name, claim_start_date, claim_end_date, verification_date FROM st_sl_draw_master_"+gameId+" draw INNER JOIN st_sl_draw_merchant_mapping_"+gameId+" merchant ON draw.draw_id=merchant.draw_id INNER JOIN  st_sl_game_tickets_"+gameId+"_"+gameTypeId+"  gt ON gt.draw_id=draw.draw_id  INNER JOIN st_sl_sale_txn_master stm on stm.trans_id=gt.trans_id WHERE stm.merchant_id="+saleMerchantId+" AND merchant.merchant_id="+saleMerchantId+" AND stm.ticket_nbr="+validateTktBean.getTicketNumInDB()+barCodeAppender;
				logger.info("Draw Query - "+query);
				drawRs = drawStmt.executeQuery(query);
				drawRs.last();
				int size = drawRs.getRow();
				drawRs.beforeFirst();
				int drawNo = 0;
				pwtVerifyTicketDrawDataArr = new PwtVerifyTicketDrawDataBean[size];
				while (drawRs.next()) {
					isTicketAvailable = true;
					double drawWinAmt = 0.0;
					pwtVerifyDrawDataBean = new PwtVerifyTicketDrawDataBean();
					pwtVerifyDrawDataBean.setDrawDateTime(Util.getDateTimeFormat(drawRs.getTimestamp("draw_datetime")));
					pwtVerifyDrawDataBean.setDrawName(drawRs.getString("draw_name"));
					pwtVerifyDrawDataBean.setDrawId(drawRs.getInt("draw_id"));
					int purchaseTableName = drawRs.getInt("purchase_table_name");
					pwtVerifyDrawDataBean.setActuatDrawStatus(drawRs.getString("draw_status"));

					Timestamp currentDate = Util.getCurrentTimeStamp();
					Timestamp claimStartDate = drawRs.getTimestamp("claim_start_date");
					Timestamp claimEndDate = drawRs.getTimestamp("claim_end_date");
					Timestamp verificationDate = drawRs.getTimestamp("verification_date");
					drawStatus = null;
					if(currentDate.before(verificationDate))
						drawStatus="VERIFICATION_PENDING";
					else if(currentDate.before(claimStartDate))
						drawStatus="CLAIM_PENDING";
					else if(currentDate.after(claimEndDate))
						drawStatus="DRAW_EXPIRED";
					else if(currentDate.after(verificationDate) && currentDate.after(claimStartDate) && currentDate.before(claimEndDate))
						if ("CLAIM ALLOW".equals(drawRs.getString("draw_status")))
							drawStatus = "CLAIM ALLOW";
						else if ("CANCEL".equals(drawRs.getString("draw_status")))
							drawStatus = "DRAW CANCELLED";
						else
							drawStatus = "RESULT AWAITED";
					pwtVerifyDrawDataBean.setDrawStatus(drawStatus);

					if ("CLAIM ALLOW".equals(drawStatus) || "VERIFICATION_PENDING".equals(drawStatus) || "DRAW_EXPIRED".equals(drawStatus) || "CLAIM_PENDING".equals(drawStatus)) {
						//query = "SELECT board_id, ticket_number, purchase_date, SUM(purchaseAmt) purchaseAmt, SUM(winAmt) winAmt, STATUS FROM (SELECT board_id, ticket_number, purchase_date, bet_amount_multiple*draw_tlb.unit_price purchaseAmt, (bet_amount_multiple*draw_tlb.unit_price*prize_amount)/prize.unit_price winAmt, rank_id, draw_tlb.merchant_id, STATUS FROM st_sl_draw_ticket_"+gameId+"_"+gameTypeId+"_"+purchaseTableName+" draw_tlb INNER JOIN st_sl_prize_details_"+gameId+"_"+gameTypeId+" prize ON draw_tlb.merchant_id=prize.merchant_id AND draw_tlb.rank_id=prize.prize_rank AND draw_tlb.draw_id=prize.draw_id WHERE ticket_number="+validateTktBean.getTicketNumInDB()+" AND rank_id>0 AND draw_tlb.merchant_id="+saleMerchantId+")winTlb GROUP BY board_id;";
						query = "SELECT board_id, ticket_number, purchase_date, SUM(purchaseAmt) purchaseAmt, IFNULL(SUM(winAmt), 0.00) winAmt, IFNULL(rank_id, 0) rank_id, status FROM (SELECT board_id, ticket_number, purchase_date, bet_amount_multiple*draw_tlb.unit_price purchaseAmt, (bet_amount_multiple*draw_tlb.unit_price*prize_amount)/prize.unit_price winAmt, rank_id, draw_tlb.merchant_id, STATUS FROM st_sl_draw_ticket_"+gameId+"_"+gameTypeId+"_"+purchaseTableName+" draw_tlb LEFT JOIN st_sl_prize_details_"+gameId+"_"+gameTypeId+" prize ON draw_tlb.merchant_id=prize.merchant_id AND draw_tlb.rank_id=prize.prize_rank AND draw_tlb.draw_id=prize.draw_id WHERE ticket_number="+validateTktBean.getTicketNumInDB()+" AND draw_tlb.merchant_id="+saleMerchantId+" order by rank_id desc)winTlb GROUP BY board_id;";
						logger.debug("Board Query - "+query);
						boardRs = boardStmt.executeQuery(query);
						boardRs.last();
						int boardSize = boardRs.getRow();
						boardRs.beforeFirst();
						int boardNo = 0;
						pwtVerifyDrawDataBean.setBoardCount(boardSize);
						boardTicketdataBeanArr = new BoardTicketDataBean[boardSize+1];
						while (boardRs.next()) {
							isBoardAvailable = true;
							boardTicketDataBean = new BoardTicketDataBean();
							if ("CLAIMED".equals(boardRs.getString("status"))) {
								pwtVerifyDrawDataBean.setMessage("Already Claimed");
							}

							boardTicketDataBean.setTicketStatus(boardRs.getString("status"));
							purchaseDateTime = Util.getDateTimeFormat(boardRs.getTimestamp("purchase_date"));
							totalPurchaseAmt += boardRs.getDouble("purchaseAmt");
							totalWinAmt = totalWinAmt + boardRs.getDouble("winAmt");
							drawWinAmt = drawWinAmt + boardRs.getDouble("winAmt");
							boardTicketDataBean.setWinningAmt(boardRs.getDouble("winAmt"));
							boardTicketDataBean.setBoardId(boardRs.getInt("board_id"));
							boardTicketdataBeanArr[boardNo] = boardTicketDataBean;
							boardNo++;
							pwtVerifyDrawDataBean.setStatus(boardRs.getString("status"));
							pwtVerifyDrawDataBean.setRankId(boardRs.getString("rank_id")) ;
						}
						if(!isBoardAvailable){
							query = "SELECT ticket_nbr from st_sl_sale_refund_txn_master where ticket_nbr = '"+validateTktBean.getTicketNumInDB()+"'";
							logger.info("Cancel Tkt Query - "+query);
							cancelTktStmt = connection.createStatement();
							cancelTktRs = cancelTktStmt.executeQuery(query);
							if(cancelTktRs.next()){
								boardTicketDataBean = new BoardTicketDataBean();
								boardTicketDataBean.setTicketStatus("CANCELLED");
								boardTicketDataBean.setWinningAmt(0.0);
								boardTicketDataBean.setBoardId(1);
								boardTicketdataBeanArr[0] = boardTicketDataBean;
								pwtVerifyDrawDataBean.setMessage("Ticket is Cancelled");
								pwtVerifyDrawDataBean.setStatus("CANCELLED");
							}
						}
						pwtVerifyDrawDataBean.setBoardTicketBeanArray(boardTicketdataBeanArr);
						if (drawWinAmt > 0.0) {
							//pwtVerifyDrawDataBean.setMessage("panel Winning:" + boardTicketdataBeanArr.length);
							pwtVerifyDrawDataBean.setMessage("Winning Amt:");
						}
					} else {
						pwtVerifyDrawDataBean.setBoardCount(1);
						pwtVerifyDrawDataBean.setMessage("Result Awaited");
					}

					pwtVerifyDrawDataBean.setDrawWinAmt(drawWinAmt);
					pwtVerifyTicketDrawDataArr[drawNo] = pwtVerifyDrawDataBean;
				}

				if (!isTicketAvailable) {
					throw new SLEException(SLEErrors.INVALID_TICKET_NUMBER_ERROR_CODE, SLEErrors.INVALID_TICKET_NUMBER_ERROR_MESSAGE);
				}

				verifyTicketBean = new PwtVerifyTicketBean();
				verifyTicketBean.setGameId(gameId);
				verifyTicketBean.setGameTypeId(gameTypeId);
				verifyTicketBean.setGameName(SportsLotteryUtils.gameInfoMerchantMap.get(saleMerchantName).get(gameId).getGameDispName());
				verifyTicketBean.setGameTypename(SportsLotteryUtils.gameTypeInfoMerchantMap.get(saleMerchantName).get(gameTypeId).getGameTypeDispName());
				verifyTicketBean.setTicketNumber(ticketNumber);
				verifyTicketBean.setVerifyTicketDrawDataBeanArray(pwtVerifyTicketDrawDataArr);
				verifyTicketBean.setPurchaseDateTime(purchaseDateTime);
				verifyTicketBean.setTotalPurchaseAmt(totalPurchaseAmt);
				verifyTicketBean.setTotalWinAmt(totalWinAmt);
				verifyTicketBean.setTicketNumInDB(validateTktBean.getTicketNumInDB());
			} else {
				if(userBean != null) {
					LMSIntegrationImpl.updateRGAtLMS(userBean, validateTktBean);
				}
				throw new SLEException(SLEErrors.INVALID_TICKET_NUMBER_ERROR_CODE, SLEErrors.INVALID_TICKET_NUMBER_ERROR_MESSAGE);
			}
		} catch (SLEException se) {
			throw se;
		} catch (SQLException se) {
			se.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}finally{
			DBConnect.closeConnection(cancelTktStmt, cancelTktRs);
			DBConnect.closeConnection(boardStmt, boardRs);
			DBConnect.closeConnection(drawStmt, drawRs);
		}

		return verifyTicketBean;
	}

	public PwtVerifyTicketBean verifyTicketForAPI(String winningMerchantName, String ticketNumber, Connection connection) throws SLEException {
		PwtVerifyTicketBean verifyTicketBean = null;
		
		PwtVerifyTicketDrawDataBean pwtVerifyDrawDataBean = null;
		PwtVerifyTicketDrawDataBean[] pwtVerifyTicketDrawDataArr = null;
		BoardTicketDataBean boardTicketDataBean = null;
		BoardTicketDataBean[] boardTicketdataBeanArr = null;

		String query = null;
		Statement drawStmt = null;
		ResultSet drawRs = null;
		Statement winResultStmt = null;
		ResultSet winResultRs = null;
		Statement boardStmt = null;
		ResultSet boardRs = null;
		//Statement cancelTktStmt = null;
		//ResultSet cancelTktRs = null;
		String drawStatus = null;
		int noOfMatches = 0;
		String purchaseDateTime = null;
		double totalPurchaseAmt = 0.0;
		double totalWinAmt = 0.0;
		boolean isTicketAvailable = false;
		//boolean isBoardAvailable = false;
		try {
			drawStmt = connection.createStatement();
			winResultStmt = connection.createStatement();
			boardStmt = connection.createStatement();

			ValidateTicketBean validateTktBean = new ValidateTicketBean(ticketNumber);
			SportsLotteryUtils.validateTicketPWT(validateTktBean, winningMerchantName, connection);
			if (validateTktBean.isValid()) {
				String saleMerchantName = validateTktBean.getMerchantCode();
				int saleMerchantId = UtilityFunctions.getMerchantIdFromMerchantName(saleMerchantName, connection);
				int gameId = validateTktBean.getGameid();
				int gameTypeId = validateTktBean.getGameTypeId();

				query = "SELECT draw.draw_id, draw_no, draw_datetime, draw_status, purchase_table_name, draw_name, claim_start_date, claim_end_date, verification_date FROM st_sl_draw_master_"+gameId+" draw INNER JOIN st_sl_draw_merchant_mapping_"+gameId+" merchant ON draw.draw_id=merchant.draw_id INNER JOIN  st_sl_game_tickets_"+gameId+"_"+gameTypeId+"  gt ON gt.draw_id=draw.draw_id  INNER JOIN st_sl_sale_txn_master stm on stm.trans_id=gt.trans_id WHERE stm.merchant_id="+saleMerchantId+" AND merchant.merchant_id="+saleMerchantId+" AND stm.ticket_nbr="+validateTktBean.getTicketNumInDB();
				logger.info("Draw Query - "+query);
				drawRs = drawStmt.executeQuery(query);
				drawRs.last();
				int size = drawRs.getRow();
				drawRs.beforeFirst();
				int drawNo = 0;
				pwtVerifyTicketDrawDataArr = new PwtVerifyTicketDrawDataBean[size];
				while (drawRs.next()) {
					isTicketAvailable = true;
					double drawWinAmt = 0.0;
					String drawResult = "";

					query = "SELECT IFNULL(GROUP_CONCAT(CONCAT(event_description,':',option_name) SEPARATOR ';'), 'NA') draw_result FROM st_sl_draw_event_mapping_"+gameId+" dem INNER JOIN st_sl_event_master em ON dem.event_id=em.event_id INNER JOIN st_sl_event_option_mapping eom ON dem.evt_opt_id=eom.evt_opt_id WHERE draw_id="+drawRs.getInt("draw_id")+" and eom.game_id="+gameId+" and eom.game_type_id="+gameTypeId+" ORDER BY event_order;";
					logger.info("Draw Result Query - "+query);
					winResultRs = winResultStmt.executeQuery(query);
					if (winResultRs.next()) {
						drawResult = winResultRs.getString("draw_result");
					}

					pwtVerifyDrawDataBean = new PwtVerifyTicketDrawDataBean();
					pwtVerifyDrawDataBean.setDrawDateTime(Util.getDateTimeFormat(drawRs.getTimestamp("draw_datetime")));
					pwtVerifyDrawDataBean.setDrawName(drawRs.getString("draw_name"));
					pwtVerifyDrawDataBean.setDrawId(drawRs.getInt("draw_id"));
					pwtVerifyDrawDataBean.setDrawNo(drawRs.getInt("draw_no"));
					pwtVerifyDrawDataBean.setActuatDrawStatus(drawRs.getString("draw_status"));
					pwtVerifyDrawDataBean.setDrawResult(drawResult);

					int purchaseTableName = drawRs.getInt("purchase_table_name");

					Timestamp currentDate = Util.getCurrentTimeStamp();
					Timestamp claimStartDate = drawRs.getTimestamp("claim_start_date");
					Timestamp claimEndDate = drawRs.getTimestamp("claim_end_date");
					Timestamp verificationDate = drawRs.getTimestamp("verification_date");
					drawStatus = null;
					if(currentDate.before(verificationDate))
						drawStatus="VERIFICATION_PENDING";
					else if(currentDate.before(claimStartDate))
						drawStatus="CLAIM_PENDING";
					else if(currentDate.after(claimEndDate))
						drawStatus="DRAW_EXPIRED";
					else if(currentDate.after(verificationDate) && currentDate.after(claimStartDate) && currentDate.before(claimEndDate))
						if ("CLAIM ALLOW".equals(drawRs.getString("draw_status")))
							drawStatus = "CLAIM ALLOW";
						else if ("CANCEL".equals(drawRs.getString("draw_status")))
							drawStatus = "DRAW CANCELLED";
						else
							drawStatus = "RESULT AWAITED";
					pwtVerifyDrawDataBean.setDrawStatus(drawStatus);

					query = "SELECT board_id, ticket_number, purchase_date, SUM(purchaseAmt) purchaseAmt, IFNULL(SUM(winAmt), 0.00) winAmt, IFNULL(rank_id, 0) rank_id, IFNULL(no_of_match, 0) no_of_match, status FROM (SELECT board_id, ticket_number, purchase_date, bet_amount_multiple*draw_tlb.unit_price purchaseAmt, (bet_amount_multiple*draw_tlb.unit_price*prize_amount)/prize.unit_price winAmt, rank_id, draw_tlb.merchant_id, no_of_match, status FROM st_sl_draw_ticket_"+gameId+"_"+gameTypeId+"_"+purchaseTableName+" draw_tlb LEFT JOIN st_sl_prize_details_"+gameId+"_"+gameTypeId+" prize ON draw_tlb.merchant_id=prize.merchant_id AND draw_tlb.rank_id=prize.prize_rank AND draw_tlb.draw_id=prize.draw_id WHERE ticket_number="+validateTktBean.getTicketNumInDB()+" AND draw_tlb.merchant_id="+saleMerchantId+")winTlb GROUP BY board_id;";
					logger.info("Board Query - "+query);
					boardRs = boardStmt.executeQuery(query);
					boardRs.last();
					int boardSize = boardRs.getRow();
					boardRs.beforeFirst();
					int boardNo = 0;
					pwtVerifyDrawDataBean.setBoardCount(boardSize);
					boardTicketdataBeanArr = new BoardTicketDataBean[boardSize+1];
					while (boardRs.next()) {
						//isBoardAvailable = true;
						boardTicketDataBean = new BoardTicketDataBean();
						boardTicketDataBean.setTicketStatus(boardRs.getString("status"));
						boardTicketDataBean.setWinningAmt(boardRs.getDouble("winAmt"));
						boardTicketDataBean.setBoardId(boardRs.getInt("board_id"));
						boardTicketdataBeanArr[boardNo] = boardTicketDataBean;
						pwtVerifyDrawDataBean.setStatus(boardRs.getString("status"));
						boardNo++;

						noOfMatches = boardRs.getInt("no_of_match");
						purchaseDateTime = Util.getDateTimeFormat(boardRs.getTimestamp("purchase_date"));
						totalPurchaseAmt += boardRs.getDouble("purchaseAmt");
						totalWinAmt = totalWinAmt + boardRs.getDouble("winAmt");
						drawWinAmt = drawWinAmt + boardRs.getDouble("winAmt");
					}
					/*if(!isBoardAvailable){
						query = "SELECT ticket_nbr from st_sl_sale_refund_txn_master where ticket_nbr = '"+validateTktBean.getTicketNumInDB()+"'";
						logger.info("Cancel Tkt Query - "+query);
						cancelTktStmt = connection.createStatement();
						cancelTktRs = cancelTktStmt.executeQuery(query);
						if(cancelTktRs.next()){
							boardTicketDataBean = new BoardTicketDataBean();
							boardTicketDataBean.setTicketStatus("CANCELLED");
							boardTicketDataBean.setWinningAmt(0.0);
							boardTicketDataBean.setBoardId(1);
							boardTicketdataBeanArr[0] = boardTicketDataBean;
							pwtVerifyDrawDataBean.setStatus("CANCELLED");
						}
					}*/
					pwtVerifyDrawDataBean.setBoardTicketBeanArray(boardTicketdataBeanArr);
					pwtVerifyDrawDataBean.setNoOfMatches(noOfMatches);
					pwtVerifyDrawDataBean.setDrawWinAmt(drawWinAmt);
					pwtVerifyTicketDrawDataArr[drawNo] = pwtVerifyDrawDataBean;
				}

				if (!isTicketAvailable) {
					throw new SLEException(SLEErrors.INVALID_TICKET_NUMBER_ERROR_CODE, SLEErrors.INVALID_TICKET_NUMBER_ERROR_MESSAGE);
				}
				if("PMS".equalsIgnoreCase(saleMerchantName) && pwtVerifyDrawDataBean.getDrawWinAmt() < Double.parseDouble(Util.getPropertyValue("AUTO_APPROVED_WINNING_AMT_LIMIT"))){
					throw new SLEException(SLEErrors.INVALID_MERCHANT_FOR_PWT_ERROR_CODE, SLEErrors.INVALID_MERCHANT_FOR_PWT_ERROR_MESSAGE);
				}
				

				verifyTicketBean = new PwtVerifyTicketBean();
				verifyTicketBean.setGameId(gameId);
				verifyTicketBean.setGameTypeId(gameTypeId);
				verifyTicketBean.setGameName(SportsLotteryUtils.gameInfoMerchantMap.get(saleMerchantName).get(gameId).getGameDispName());
				verifyTicketBean.setGameTypename(SportsLotteryUtils.gameTypeInfoMerchantMap.get(saleMerchantName).get(gameTypeId).getGameTypeDispName());
				verifyTicketBean.setTicketNumber(ticketNumber);
				verifyTicketBean.setVerifyTicketDrawDataBeanArray(pwtVerifyTicketDrawDataArr);
				verifyTicketBean.setPurchaseDateTime(purchaseDateTime);
				verifyTicketBean.setTotalPurchaseAmt(totalPurchaseAmt);
				verifyTicketBean.setTotalWinAmt(totalWinAmt);
				verifyTicketBean.setTicketNumInDB(validateTktBean.getTicketNumInDB());
			} else {
				throw new SLEException(SLEErrors.INVALID_TICKET_NUMBER_ERROR_CODE, SLEErrors.INVALID_TICKET_NUMBER_ERROR_MESSAGE);
			}
		} catch (SLEException se) {
			throw se;
		} catch (SQLException se) {
			se.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}

		return verifyTicketBean;
	}
}