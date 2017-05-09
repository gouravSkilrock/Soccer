package com.skilrock.sle.gamePlayMgmt.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import rng.RNGUtilities;

import com.skilrock.sle.common.DBConnect;
import com.skilrock.sle.common.SportsLotteryUtils;
import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.common.javaBeans.UserInfoBean;
import com.skilrock.sle.commonMethod.daoImpl.CommonMethodsDaoImpl;
import com.skilrock.sle.drawMgmt.javaBeans.SLPrizeRankDistributionBean;
import com.skilrock.sle.gamePlayMgmt.javaBeans.DrawDetailBean;
import com.skilrock.sle.gamePlayMgmt.javaBeans.EventDetailBean;
import com.skilrock.sle.gamePlayMgmt.javaBeans.PrizeRankDrawWinningBean;
import com.skilrock.sle.gamePlayMgmt.javaBeans.SportsLotteryGameDrawDataBean;
import com.skilrock.sle.gamePlayMgmt.javaBeans.SportsLotteryGameEventDataBean;
import com.skilrock.sle.gamePlayMgmt.javaBeans.SportsLotteryGamePlayBean;
import com.skilrock.sle.merchant.common.javaBeans.MerchantInfoBean;


public class PurchaseTicketDaoImpl {
	
	private PurchaseTicketDaoImpl() {
	}

	private static volatile PurchaseTicketDaoImpl classInstance;

	public static PurchaseTicketDaoImpl getInstance() {
		if(classInstance == null){
			synchronized (PurchaseTicketDaoImpl.class) {
				if(classInstance == null){
					classInstance = new PurchaseTicketDaoImpl();					
				}
			}
		}
		return classInstance;
	}
		//Original
		public Map<Integer, PrizeRankDrawWinningBean> getPrizeRankDrawSaleDistriBution(int merchantId, String merchantDevName, int gameId,int gameTypeId,int drawId,double saleAmt, Connection con) throws SLEException{
			double winningAmount=0.0;
			double totalSaleAmt = 0;
			double winningPercentage = 0;
			Map<Integer, PrizeRankDrawWinningBean> drawPrizeRankMap=new TreeMap<Integer, PrizeRankDrawWinningBean>();
			PrizeRankDrawWinningBean drawPrizeRankBean=null;
			Map<Integer, Double> drawSaleMap = null;
			try{
				//drawSaleMap = SportsLotteryUtils.drawSaleMap.get(merchantId + "_" + gameId + "_" + gameTypeId);
				//totalSaleAmt=drawSaleMap.get(drawId);
				//drawSaleMap.put(drawId, totalSaleAmt+saleAmt);

				winningPercentage=SportsLotteryUtils.gameTypeInfoMerchantMap.get(merchantDevName).get(gameTypeId).getPrizeAmtPercentage();
				
				totalSaleAmt = 0;
				for(Map.Entry<String, MerchantInfoBean> entrySet : Util.merchantInfoMap.entrySet()) {
					if(SportsLotteryUtils.drawSaleMap.containsKey(entrySet.getValue().getMerchantId() + "_" + gameId + "_" + gameTypeId)) {
						drawSaleMap = SportsLotteryUtils.drawSaleMap.get(entrySet.getValue().getMerchantId() + "_" + gameId + "_" + gameTypeId);
						if(drawSaleMap.containsKey(drawId)){
							totalSaleAmt += drawSaleMap.get(drawId);
						}
					}
				}
				winningAmount=totalSaleAmt*.01*winningPercentage;
				Map<Integer, SLPrizeRankDistributionBean> prizeDistributionMap = SportsLotteryUtils.prizeRankDistributionMap.get(merchantId + "_" + gameTypeId);
				Set<Integer> prizeRankSet = prizeDistributionMap.keySet();
			
				for(Integer prizeRank : prizeRankSet) {
					drawPrizeRankBean=new PrizeRankDrawWinningBean();
					SLPrizeRankDistributionBean bean = prizeDistributionMap.get(prizeRank);
	
					drawPrizeRankBean.setMatchId(bean.getMatchId());
					drawPrizeRankBean.setPrizeRank(prizeRank);
					drawPrizeRankBean.setRankName(bean.getRankName());
					drawPrizeRankBean.setSaleValue(totalSaleAmt);
					
					if("FIXED".equalsIgnoreCase(bean.getPrizeType())){
						drawPrizeRankBean.setPrizeValue(bean.getPrizeValue());
						continue;
						
					}else{
						double netWinAmt=Util.jackpotMap.get(gameId).get(gameTypeId) + winningAmount*.01*bean.getPrizeValue();
							if(bean.getMinPrizeValue()>netWinAmt ){
								netWinAmt=bean.getMinPrizeValue();
							}
							
							drawPrizeRankBean.setPrizeValue(netWinAmt);
					}
					drawPrizeRankMap.put(prizeRank,drawPrizeRankBean);		
				}
			}catch (Exception e) {
				throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);

			}
			return drawPrizeRankMap;
		}
		
	public Map<Integer, PrizeRankDrawWinningBean> getPrizeRankDrawSaleDistriButionNew(int merchantId, String merchantDevName, int gameId, int gameTypeId, int drawId, double saleAmt) throws SLEException {
		double winningAmount = 0.0;
		Map<Integer, PrizeRankDrawWinningBean> drawPrizeRankMap = new TreeMap<Integer, PrizeRankDrawWinningBean>();
		PrizeRankDrawWinningBean drawPrizeRankBean = null;
		Map<Integer, Double> drawSaleMap = null;
		double totalSaleAmt = 0;
		double winningPercentage = 0;

		try {

			drawSaleMap = SportsLotteryUtils.drawSaleMap.get(merchantId + "_" + gameId + "_" + gameTypeId);
			if(drawSaleMap.containsKey(drawId))
				totalSaleAmt = drawSaleMap.get(drawId);
				drawSaleMap.put(drawId, totalSaleAmt + saleAmt);
				winningPercentage = SportsLotteryUtils.gameTypeInfoMerchantMap.get(merchantDevName).get(gameTypeId).getPrizeAmtPercentage();
				totalSaleAmt = 0;
				for(Map.Entry<String, MerchantInfoBean> entrySet : Util.merchantInfoMap.entrySet()) {
					if(SportsLotteryUtils.drawSaleMap.containsKey(entrySet.getValue().getMerchantId() + "_" + gameId + "_" + gameTypeId)) {
						drawSaleMap = SportsLotteryUtils.drawSaleMap.get(entrySet.getValue().getMerchantId() + "_" + gameId + "_" + gameTypeId);
						if(drawSaleMap.containsKey(drawId))
							totalSaleAmt += drawSaleMap.get(drawId);
					}
				}
			winningAmount = totalSaleAmt * .01 * winningPercentage;

			Map<Integer, SLPrizeRankDistributionBean> prizeDistributionMap = SportsLotteryUtils.prizeRankDistributionMap.get(merchantId + "_" + gameTypeId);
			Set<Integer> prizeRankSet = prizeDistributionMap.keySet();

			for (Integer prizeRank : prizeRankSet) {
				drawPrizeRankBean = new PrizeRankDrawWinningBean();
				SLPrizeRankDistributionBean bean = prizeDistributionMap.get(prizeRank);

				drawPrizeRankBean.setMatchId(bean.getMatchId());
				drawPrizeRankBean.setPrizeRank(prizeRank);
				drawPrizeRankBean.setRankName(bean.getRankName());
				drawPrizeRankBean.setSaleValue(totalSaleAmt);

				if ("FIXED".equalsIgnoreCase(bean.getPrizeType())) {
					drawPrizeRankBean.setPrizeValue(bean.getPrizeValue());
					continue;

				} else {
					double netWinAmt = Util.jackpotMap.get(gameId).get(gameTypeId) + winningAmount * .01 * bean.getPrizeValue();
					if (bean.getMinPrizeValue() > netWinAmt) {
						netWinAmt = bean.getMinPrizeValue();
					}
					drawPrizeRankBean.setPrizeValue(netWinAmt);
				}
				drawPrizeRankMap.put(prizeRank, drawPrizeRankBean);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return drawPrizeRankMap;
	}
				
	public long generateTicketNumber(UserInfoBean userInfoBean, int serviceId, Connection con) throws SLEException {
		long tickNum = 0;
		int randomNo = 0;
		int newTickNum = 0;
		PreparedStatement fetchPstm = null;
		PreparedStatement updatePstm = null;
		int merchantServiceId;
		int isUpdated = 0;
		try {
			fetchPstm = con.prepareStatement("select ticket_count, service_id from st_sl_generate_ticketno where merchant_id = ? and merchant_user_ticket_id = ?");
			fetchPstm.setInt(1, userInfoBean.getMerchantId());
			fetchPstm.setInt(2, userInfoBean.getMerchantUserId());
			ResultSet rs = fetchPstm.executeQuery();
			if (rs.next()) {
				newTickNum = rs.getInt("ticket_count") + 1;
				if("PMS".equals(userInfoBean.getMerchantDevName())) {
					if (newTickNum > 99)
						throw new SLEException(SLEErrors.TICKET_LIMIT_REACHED_ERROR_CODE, SLEErrors.TICKET_LIMIT_REACHED_ERROR_MESSAGE);
				} else if("RMS".equals(userInfoBean.getMerchantDevName())) {
					if (newTickNum > 999)
						throw new SLEException(SLEErrors.TICKET_LIMIT_REACHED_ERROR_CODE, SLEErrors.TICKET_LIMIT_REACHED_ERROR_MESSAGE);
				}

				merchantServiceId = rs.getInt("service_id");

				updatePstm = con.prepareStatement("update st_sl_generate_ticketno set ticket_count = ? where merchant_id = ? and merchant_user_ticket_id = ?");
				updatePstm.setInt(1, newTickNum);
				updatePstm.setInt(2, userInfoBean.getMerchantId());
				updatePstm.setInt(3, userInfoBean.getMerchantUserId());

				isUpdated = updatePstm.executeUpdate();
				if(isUpdated == 0)
					throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);

			} else {
				newTickNum = 1;
				merchantServiceId = serviceId;
				updatePstm = con.prepareStatement("insert into st_sl_generate_ticketno(user_id, ticket_count, service_id, merchant_user_ticket_id, merchant_id) values(?, ?, ?, ?, ?)");
				updatePstm.setLong(1, userInfoBean.getSleUserId());
				updatePstm.setInt(2, newTickNum);
				updatePstm.setInt(3, merchantServiceId);
				updatePstm.setInt(4, userInfoBean.getMerchantUserId());
				updatePstm.setInt(5, userInfoBean.getMerchantId());
				updatePstm.executeUpdate();
			}

			if("PMS".equals(userInfoBean.getMerchantDevName()) || "Asoft".equals(userInfoBean.getMerchantDevName()) || "Weaver".equals(userInfoBean.getMerchantDevName()) || "TonyBet".equals(userInfoBean.getMerchantDevName())) {
				randomNo = RNGUtilities.generateRandomNumber(10, 99);
				tickNum = (((((long)((((randomNo * 10 + merchantServiceId) * 10) + userInfoBean.getMerchantId()) * 1000) + Calendar.getInstance().get(Calendar.DAY_OF_YEAR)) * 10000000) + userInfoBean.getMerchantUserId()) * 100) + newTickNum; 
			} else if("RMS".equals(userInfoBean.getMerchantDevName()) || "OKPOS".equals(userInfoBean.getMerchantDevName())) {
				randomNo = RNGUtilities.generateRandomNumber(1000, 9999);
				tickNum = (((((long)((((randomNo * 10 + merchantServiceId) * 10) + userInfoBean.getMerchantId()) * 1000) + Calendar.getInstance().get(Calendar.DAY_OF_YEAR)) * 100000) + userInfoBean.getMerchantUserMappingId()) * 1000) + newTickNum;
			} else {
				throw new SLEException(SLEErrors.INVALID_MERCHANT_NAME_ERROR_CODE, SLEErrors.INVALID_MERCHANT_NAME_ERROR_MESSAGE);
			}
		} catch (SLEException e) {
			e.printStackTrace();
			throw e;
		} catch (SQLException se) {
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE,SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally{
			DBConnect.closePrepareStatementStatements(updatePstm,fetchPstm);
		}
		return tickNum;
	}
	
	public long purchaseTicket(SportsLotteryGamePlayBean gamePlayBean, UserInfoBean userInfoBean, Connection conn,long trxId,Map<Integer, DrawDetailBean> drawDetailMap) throws SLEException {
		int noOfBoard = 0;
		int noOfEvents = 0;
		int drawId = 0;
		StringBuilder eventString = null;

		Statement stmt = null;
		int isUpdated = 0;
		StringBuffer insertDrawTickets = null;
		PreparedStatement pStmt = null;
		int tempCount = 0;
		int linesCount = 0;
		try {
			if(trxId == 0){
				gamePlayBean.setPurchaseTime(Util.getCurrentTimeString());
				trxId = CommonMethodsDaoImpl.getInstance().getNewSaleTransactionId(userInfoBean, gamePlayBean, conn);
			}
			
			if(drawDetailMap == null){
				drawDetailMap = PurchaseTicketDaoImpl.getInstance().getDrawDetail(gamePlayBean.getDrawIdArray(), gamePlayBean.getGameId(),
						gamePlayBean.getGameTypeId(), userInfoBean.getMerchantId(), conn);
			}
			if(drawDetailMap.isEmpty()){
				throw new SLEException(SLEErrors.NO_ACTIVE_DRAW_AVAILABLE_ERROR_CODE,SLEErrors.NO_ACTIVE_DRAW_AVAILABLE_ERROR_MESSAGE);
			}
			DrawDetailBean drawDetailBean = null;
			noOfBoard = gamePlayBean.getNoOfBoard();
			noOfEvents = SportsLotteryUtils.gameTypeInfoMerchantMap.get(userInfoBean.getMerchantDevName()).get(gamePlayBean.getGameTypeId()).getNoOfEvents();

			eventString = new StringBuilder();

			for (int iLoop = 1; iLoop <= noOfEvents; iLoop++) {
				eventString.append("event_").append(iLoop).append(",");
			}
			
			for (int i = 0; i < noOfBoard; i++) {
				int purchaseTableName = 0;
				StringBuilder gameTicketBuilder = new StringBuilder();

				SportsLotteryGameDrawDataBean gameDrawDataBean = gamePlayBean.getGameDrawDataBeanArray()[i];

				int[][] selectedEventArray = new int[noOfEvents][];
				int noOfLines = gameDrawDataBean.getNoOfLines();
				drawId = gameDrawDataBean.getDrawId();

				if(!drawDetailMap.containsKey(drawId)) 
					throw new SLEException(SLEErrors.NO_ACTIVE_DRAW_AVAILABLE_ERROR_CODE, SLEErrors.NO_ACTIVE_DRAW_AVAILABLE_ERROR_MESSAGE);

				drawDetailBean = drawDetailMap.get(drawId);
				gameDrawDataBean.setDrawDateTime(drawDetailBean.getDrawDateTime());
				gameDrawDataBean.setDrawDisplayname(drawDetailBean.getDrawDisplay());
				gameDrawDataBean.setDrawClaimStartTime(drawDetailBean.getDrawClaimEndTime());
				gameDrawDataBean.setDrawClaimEndTime(drawDetailBean.getDrawClaimEndTime());
				gameDrawDataBean.setDrawVerificationStartTime(drawDetailBean.getVerificationStartTime());
				gameDrawDataBean.setEventId(drawDetailBean.getDrawNo());
				
				gameTicketBuilder.append("(");
				gameTicketBuilder.append(trxId).append(",");
				gameTicketBuilder.append(drawId).append(",");
				gameTicketBuilder.append(gamePlayBean.getGameTypeId()).append(",");
				gameTicketBuilder.append(i + 1).append(",");
				gameTicketBuilder.append(gamePlayBean.getTicketNumber()).append(",");
				gameTicketBuilder.append(noOfLines).append(",");

				SportsLotteryGameEventDataBean[] eventDrawDataBeanArray = gameDrawDataBean.getGameEventDataBeanArray();
				for (int j = 0; j < eventDrawDataBeanArray.length; j++) {
					SportsLotteryGameEventDataBean eventDrawDataBean = eventDrawDataBeanArray[j];
					int[] options = new int[eventDrawDataBean.getSelectedOption().length];
					gameTicketBuilder.append("'");
					for (int k = 0; k < eventDrawDataBean.getSelectedOption().length; k++) {
						EventDetailBean eventDetailBean = drawDetailMap.get(drawId).getDrawEventDetail().get(eventDrawDataBean.getEventId()).get(eventDrawDataBean.getSelectedOption()[k]);
						int evntOptionId = eventDetailBean.getEventOptionId();

						eventDrawDataBean.setEventDescription(eventDetailBean.getEventDescription());
						eventDrawDataBean.setHomeTeamName(eventDetailBean.getHomeTeamName());
						eventDrawDataBean.setAwayTeamName(eventDetailBean.getAwayTeamName());
						eventDrawDataBean.setLeagueName(eventDetailBean.getLeagueName());
						eventDrawDataBean.setVenueName(eventDetailBean.getVenueName());
						eventDrawDataBean.setEventDateTime(eventDetailBean.getEventDateTime());
						eventDrawDataBean.setHomeTeamCode(eventDetailBean.getHomeTeamCode());
						eventDrawDataBean.setAwayTeamCode(eventDetailBean.getAwayTeamCode());
						options[k] = evntOptionId;
						gameTicketBuilder.append(evntOptionId).append(",");
					}
					gameTicketBuilder.deleteCharAt(gameTicketBuilder.length() - 1);
					gameTicketBuilder.append("',");
					selectedEventArray[j] = options;
				}
				gameTicketBuilder.append(eventDrawDataBeanArray.length).append(",");
				gameTicketBuilder.append(gameDrawDataBean.getBetAmountMultiple()).append(",");
				gameTicketBuilder.append(gamePlayBean.getReprintCount()).append(",");
				gameTicketBuilder.append(gamePlayBean.getBarcodeCount()).append(",");
				gameTicketBuilder.append(gameDrawDataBean.getBoardPurchaseAmount()).append(",");
				if (gamePlayBean.isPromoTicket()) {
					gameTicketBuilder.append("'PROMO'");
				} else {
					gameTicketBuilder.append("'SALE'");
				}

				gameTicketBuilder.append("),");
				gameTicketBuilder.deleteCharAt(gameTicketBuilder.length() - 1);
				
				String insertGameTickets = "insert into st_sl_game_tickets_"
						+ gamePlayBean.getGameId()
						+ "_"
						+ gamePlayBean.getGameTypeId()
						+ "(trans_id, draw_id, game_type_id, board_id,ticket_number, no_of_lines,"
						+ eventString.toString()
						+ "no_fixture, bet_amount_multiple, rpc_total, barcode_count, total_amount, sale_type) VALUES";
				
				stmt = conn.createStatement();
				isUpdated = stmt.executeUpdate(insertGameTickets + gameTicketBuilder.toString());
				if (isUpdated == 0)
					throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);

				isUpdated = 0;

				insertDrawTickets = new StringBuffer();
				purchaseTableName = drawDetailMap.get(drawId).getPurchaseTableName();
				insertDrawTickets.append("insert into st_sl_draw_ticket_").append(gamePlayBean.getGameId()).append("_").append(gamePlayBean.getGameTypeId())
								.append("_").append(purchaseTableName).append("(trans_id, draw_id, board_id, line_id, ticket_number, purchase_channel, merchant_id, purchase_date, party_id,")
								.append(eventString.toString()).append("no_events, bet_amount_multiple, unit_price, status)values(?, ?, ?, ?, ?, ?, ?, ?, ?,");

				for (int iLoop = 1; iLoop <= noOfEvents; iLoop++) {
					insertDrawTickets.append(" ?, ");
				}

				insertDrawTickets.append("?, ?, ?, ?)");

				pStmt = conn.prepareStatement(insertDrawTickets.toString());

				linesCount = 0;
				int[][] allCombinations = SportsLotteryUtils.getAllCombinationsBoardWise(selectedEventArray);
				for (int k = 0; k < noOfLines; k++) {
					tempCount = 1;
					pStmt.setLong(tempCount++, trxId);
					pStmt.setInt(tempCount++, drawId);
					pStmt.setInt(tempCount++, (i + 1));
					pStmt.setInt(tempCount++, (k + 1));
					pStmt.setLong(tempCount++, gamePlayBean.getTicketNumber());
					pStmt.setString(tempCount++, gamePlayBean.getInterfaceType());
					pStmt.setInt(tempCount++, userInfoBean.getMerchantId());
					pStmt.setString(tempCount++, gamePlayBean.getPurchaseTime());
					pStmt.setLong(tempCount++, userInfoBean.getSleUserId());
					
					int[] eventDataArray = allCombinations[k];
					for (int l = 0; l < noOfEvents; l++) {
						pStmt.setInt(tempCount++, eventDataArray[l]);
					}
					pStmt.setInt(tempCount++, noOfEvents);
					pStmt.setInt(tempCount++, gameDrawDataBean.getBetAmountMultiple());
					pStmt.setDouble(tempCount++, gamePlayBean.getUnitPrice());
					pStmt.setString(tempCount++, "UNCLAIMED");

					pStmt.addBatch();

					linesCount++;

					if(linesCount > 10000) {
						pStmt.executeBatch();
						linesCount = 0;
					}
				}
				if(linesCount > 0)
					pStmt.executeBatch();
			}
		} catch (SLEException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally{
			DBConnect.closePstmt(pStmt);
			DBConnect.closeStatement(stmt);
		}
		return trxId;
	}
	
	public void updateSportsLotteryGameTicket(String status, String merchantTranId, String isCancel, Long transId, Connection con,long ticketNumber,boolean isUpdatePurchaseTable) throws SLEException {
		PreparedStatement pStmt = null;
		int isUpdated = 0;
		StringBuilder query = null;
		try {
			query = new StringBuilder();
			query.append("update st_sl_sale_txn_master set status = ?, merchant_trans_id = ?, is_cancel = ?");	
			if(ticketNumber != 0)
			query.append(" ,ticket_nbr = ?");
			
			query.append(" where trans_id = ?");
			
			pStmt = con.prepareStatement(query.toString());
			pStmt.setString(1, status);
			pStmt.setString(2, merchantTranId);
			pStmt.setString(3, isCancel);
			if(ticketNumber != 0){
				pStmt.setLong(4, ticketNumber);
				pStmt.setLong(5, transId);
			}else{
				pStmt.setLong(4, transId);
			}
			
			isUpdated = pStmt.executeUpdate();
			
			if(isUpdated == 0)
				throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
			
			if(("FAILED".equals(status) || "CANCELLED".equalsIgnoreCase(status)) && isUpdatePurchaseTable)
				updatePurchaseTicketStatus(status, transId, con);
		} catch (SLEException e) {
			e.printStackTrace();
			throw e;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally{ 
			DBConnect.closePstmt(pStmt);
		}
	}
	

	public Map<Integer, DrawDetailBean> getDrawDetail(Integer[] drawIdArr,int gameId,int gameTypeId,int merchantId,Connection con) throws SLEException {
		Statement drawStmt=null;
		ResultSet drawRs=null;
		PreparedStatement eventPstmt = null;
		ResultSet eventRs = null;
		Map<Integer, DrawDetailBean> drawDetailMap=null;
		DrawDetailBean drawDetailBean=null;
		EventDetailBean eventDetailBean = null;
		Map<String, EventDetailBean> eventDetailMap = null;
		Map<Integer, Map<String, EventDetailBean>> drawEventDetailMap = null;
		
		try{
			StringBuilder drawString=new StringBuilder();
			for(int i=0;i<drawIdArr.length;i++){
				drawString.append(drawIdArr[i]).append(",");
			}
			drawString.deleteCharAt(drawString.length()-1);
			String query = "select aa.draw_id, draw_no, draw_datetime, draw_freeze_time, sale_start_time, purchase_table_name, draw_name, draw_display_type, claim_start_date, claim_end_date, verification_date from st_sl_draw_master_"+gameId+" aa inner join st_sl_draw_merchant_mapping_"+gameId+" bb on aa.draw_id=bb.draw_id where aa.draw_id in("+drawString+") and aa.game_type_id="+gameTypeId+" AND merchant_id="+merchantId+" AND draw_freeze_time>'"+Util.getCurrentTimeStamp()+"' AND sale_start_time<'"+Util.getCurrentTimeStamp()+"' AND draw_status='ACTIVE'";
			drawStmt=con.createStatement();
			drawRs=drawStmt.executeQuery(query);
			
			drawRs.last();
		    int size = drawRs.getRow();
		    drawRs.beforeFirst();
		    drawDetailMap=new LinkedHashMap<Integer, DrawDetailBean>();
			if(size == drawIdArr.length){
				while(drawRs.next()){
					drawDetailBean=new DrawDetailBean();
					drawDetailBean.setDrawId(drawRs.getInt("draw_id"));
					drawDetailBean.setDrawNo(drawRs.getInt("draw_no"));
					drawDetailBean.setVerificationStartTime(drawRs.getString("verification_date"));
					drawDetailBean.setDrawClaimStartTime(drawRs.getString("claim_start_date"));
					drawDetailBean.setDrawClaimEndTime(drawRs.getString("claim_end_date"));
					String drawDisplay=null;
					drawDisplay=drawRs.getString("draw_name");
					drawDetailBean.setDrawDisplay(drawDisplay);
					drawDetailBean.setDrawDateTime(Util.getDateTimeFormat(drawRs.getTimestamp("draw_datetime")));
					drawDetailBean.setPurchaseTableName(drawRs.getInt("purchase_table_name"));
					
					query = "select distinct aa.event_id,event_option_id,draw_id,event_display,event_description, home_team_name, away_team_name, league_display_name, venue_display_name, start_time,end_time,option_name,option_code,event_draw_id,home_team_code,away_team_code from(select event.event_id,evt_opt_id event_option_id,event_display,event_description,(SELECT team_code FROM st_sl_game_team_master WHERE team_id = home_team_id) home_team_code, (SELECT team_code FROM st_sl_game_team_master WHERE team_id = away_team_id) away_team_code, (SELECT team_name FROM st_sl_game_team_master WHERE team_id = home_team_id) home_team_name, (SELECT team_name FROM st_sl_game_team_master WHERE team_id = away_team_id) away_team_name, league_display_name, venue_display_name, start_time,end_time,option_name,option_code from st_sl_event_master event inner join st_sl_league_master lm on lm.league_id = event.league_id inner join st_sl_venue_master vm on event.venue_id = vm.venue_id inner join st_sl_event_option_mapping op on event.event_id=op.event_id where op.game_id="
							+ gameId
							+ " and op.game_type_id="
							+ gameTypeId
							+ " and is_displayable='YES')aa inner join st_sl_draw_event_mapping_"
							+ gameId
							+ " bb on aa.event_id=bb.event_id and draw_id = "
							+ drawDetailBean.getDrawId()
							+ " order by event_draw_id,event_order";

					eventPstmt = con.prepareStatement(query);
					eventRs = eventPstmt.executeQuery();
					drawEventDetailMap = new HashMap<Integer, Map<String, EventDetailBean>>();
					
					while (eventRs.next()) {
						if (!drawEventDetailMap.containsKey(eventRs
								.getInt("event_id"))) {
							eventDetailMap = new HashMap<String, EventDetailBean>();
							drawEventDetailMap.put(eventRs.getInt("event_id"),
									eventDetailMap);
						}
						eventDetailBean = new EventDetailBean();
						eventDetailBean.setEventDescription(eventRs
								.getString("event_description"));
						eventDetailBean.setEventDisplay(eventRs
								.getString("event_display"));
						eventDetailBean.setEventId(eventRs.getInt("event_id"));
						eventDetailBean.setEventOptionId(eventRs
								.getInt("event_option_id"));
						eventDetailBean.setHomeTeamName(eventRs
								.getString("home_team_name"));
						eventDetailBean.setAwayTeamName(eventRs
								.getString("away_team_name"));
						eventDetailBean.setLeagueName(eventRs
								.getString("league_display_name"));
						eventDetailBean.setVenueName(eventRs
								.getString("venue_display_name"));
						eventDetailBean.setEventDateTime(eventRs
								.getString("start_time"));
						eventDetailBean.setOptionName(eventRs
								.getString("option_name"));
						eventDetailBean.setAwayTeamCode(eventRs
								.getString("away_team_code"));
						eventDetailBean.setHomeTeamCode(eventRs
								.getString("home_team_code"));
						eventDetailMap.put(eventRs.getString("option_code"),
								eventDetailBean);
					}
					drawDetailBean.setDrawEventDetail(drawEventDetailMap);
					drawDetailMap.put(drawRs.getInt("draw_id"), drawDetailBean);
				}
			}else{
				throw new SLEException(SLEErrors.NO_ACTIVE_DRAW_AVAILABLE_ERROR_CODE,SLEErrors.NO_ACTIVE_DRAW_AVAILABLE_ERROR_MESSAGE);
			}
			
		}catch (SQLException se) {
			se.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE,SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (SLEException e) {
			e.printStackTrace();
			throw e;
		}catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally{
			DBConnect.closeConnection(drawStmt, drawRs);
			DBConnect.closeConnection(eventPstmt, eventRs);
		}
		return drawDetailMap;
	}
	public boolean contains(final int[] array, final int key) {
	    for (final int i : array) {
	        if (i == key) {
	            return true;
	        }
	    }
	    return false;
	}
	
	
	public void updatePurchaseTicketStatus(String status, Long transId, Connection con) throws SLEException {
		int gameId = 0;
		int gameTypeId = 0;
		int drawId = 0;
		int purchaseTableName = 0;
		String ticketNumber = null;

		try {
			String query = "SELECT game_id, game_type_id, ticket_nbr FROM st_sl_sale_txn_master WHERE trans_id = " + transId + ";";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			if (rs.next()) {
				gameId = rs.getInt("game_id");
				gameTypeId = rs.getInt("game_type_id");
				ticketNumber = rs.getString("ticket_nbr");
			}

			if (ticketNumber != null) {
				query = "SELECT draw_id FROM st_sl_game_tickets_" + gameId
						+ "_" + gameTypeId + " WHERE ticket_number='"
						+ ticketNumber + "';";
				rs = stmt.executeQuery(query);
				if (rs.next()) {
					drawId = rs.getInt("draw_id");
				}
			}

			if (drawId > 0) {
				query = "SELECT purchase_table_name FROM st_sl_draw_master_"
						+ gameId + " WHERE draw_id=" + drawId + ";";
				rs = stmt.executeQuery(query);
				if (rs.next()) {
					purchaseTableName = rs.getInt("purchase_table_name");
				}
			} else {
				throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE,
						SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
			}

			query = "UPDATE st_sl_draw_ticket_" + gameId + "_" + gameTypeId
					+ "_" + purchaseTableName + " SET STATUS='" + status
					+ "' WHERE ticket_number='" + ticketNumber
					+ "' AND trans_id=" + transId + ";";
			stmt.executeUpdate(query);
		} catch (SLEException e) {
			e.printStackTrace();
			throw e;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
	}
	
	public void updateSettledSaleStatus(String status, String merchantTranId, String isCancel, Long transId, Connection con,MerchantInfoBean merchantInfoBean) throws SLEException {
		PreparedStatement pStmt = null;
		int isUpdated = 0;
		
		try {
			pStmt = con.prepareStatement("update st_sl_sale_txn_master set status = ?, merchant_trans_id = ?, is_cancel = ?, settlement_date = ?, settlement_status = ? where trans_id = ?");
			pStmt.setString(1, status);
			pStmt.setString(2, merchantTranId);
			pStmt.setString(3, isCancel);
			pStmt.setTimestamp(4, Util.getCurrentTimeStamp());
			pStmt.setString(5, "DONE");
			pStmt.setLong(6, transId);
			isUpdated = pStmt.executeUpdate();
			
			if(isUpdated == 0)
				throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
			
		/*	if("RMS".equals(merchantInfoBean.getMerchantDevName()))
				updatePurchaseTicketStatus(status, transId, con);*/
						
		} catch (SLEException e) {
			e.printStackTrace();
			throw e;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}finally{
			DBConnect.closePrepareStatementStatement(pStmt);
		}
	}
	
}
