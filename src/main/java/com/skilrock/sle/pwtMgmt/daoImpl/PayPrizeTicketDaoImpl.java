package com.skilrock.sle.pwtMgmt.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.skilrock.sle.merchant.common.javaBeans.MerchantInfoBean;
import com.skilrock.sle.merchant.common.javaBeans.TPPwtRequestBean;
import com.skilrock.sle.merchant.common.javaBeans.TPPwtResponseBean;
import com.skilrock.sle.pwtMgmt.javaBeans.DrawTicketDataBean;

public class PayPrizeTicketDaoImpl {
	private static final SLELogger logger = SLELogger.getLogger(PayPrizeTicketDaoImpl.class.getName());

	private static PayPrizeTicketDaoImpl singleInstance;

	private PayPrizeTicketDaoImpl() {
	}

	public static PayPrizeTicketDaoImpl getSingleInstance() {
		if (singleInstance == null) {
			synchronized (PayPrizeTicketDaoImpl.class) {
				if (singleInstance == null) {
					singleInstance = new PayPrizeTicketDaoImpl();
				}
			}
		}

		return singleInstance;
	}

	public TPPwtRequestBean claimWinningRequest(String merchantName, String ticketNumber, long sleUserId, long merchantUserId, String winningChannel, String payType, Connection connection) throws SLEException {
		String query = null;
		Statement drawStmt = null;
		ResultSet drawRs = null;
		Statement boardStmt = null;
		ResultSet boardRs = null;
		PreparedStatement updatePstmt = null;
		PreparedStatement insertWinning = null;

		TPPwtRequestBean pwtRequestBean = null;
		List<DrawTicketDataBean> drawTicketList = null;
		DrawTicketDataBean drawTicketBean = null;

		boolean isTicketAvailable = false;
		String saleMerchantName=null;
		int saleMerchantId=0;
		try {
			drawStmt = connection.createStatement();
			boardStmt = connection.createStatement();

			pwtRequestBean = new TPPwtRequestBean();
			drawTicketList = new ArrayList<DrawTicketDataBean>();
			pwtRequestBean.setDrawDataList(drawTicketList);

			ValidateTicketBean validateTktBean = new ValidateTicketBean(ticketNumber);
			MerchantInfoBean merchantInfoBean=CommonMethodsDaoImpl.getInstance().fetchMerchantDetailFromTicket(ticketNumber, connection);
			if("Asoft".equalsIgnoreCase(merchantInfoBean.getMerchantDevName()) || "OKPOS".equalsIgnoreCase(merchantInfoBean.getMerchantDevName())){
				saleMerchantName=merchantInfoBean.getMerchantDevName();
			}else{
				saleMerchantName=merchantName;
			}
			saleMerchantId=merchantInfoBean.getMerchantId();
			SportsLotteryUtils.validateTkt(validateTktBean, saleMerchantName);

			if (validateTktBean.isValid()) {
				int merchantId = UtilityFunctions.getMerchantIdFromMerchantName(merchantName, connection);
				int gameId = validateTktBean.getGameid();
				int gameTypeId = validateTktBean.getGameTypeId();

				query = "SELECT draw.draw_id, draw_status, purchase_table_name FROM st_sl_draw_master_"+gameId+" draw INNER JOIN st_sl_draw_merchant_mapping_"+gameId+" merchant ON draw.draw_id=merchant.draw_id INNER JOIN  st_sl_game_tickets_"+gameId+"_"+gameTypeId+"  gt ON gt.draw_id=draw.draw_id  INNER JOIN st_sl_sale_txn_master stm on stm.trans_id=gt.trans_id WHERE stm.merchant_id="+merchantId+" AND merchant.merchant_id="+merchantId+" AND stm.ticket_nbr="+validateTktBean.getTicketNumInDB();
				logger.info("Draw Query - "+query);
				drawRs = drawStmt.executeQuery(query);

				double totalWinAmount = 0.0;
				while (drawRs.next()) {
					isTicketAvailable = true;
					int purchaseTableName = drawRs.getInt("purchase_table_name");
					int drawId = drawRs.getInt("draw_id");
					String drawStatus = drawRs.getString("draw_status");

					if ("CLAIM ALLOW".equals(drawStatus)) {
						query = "SELECT ticket_number, rank_id, SUM((bet_amount_multiple*draw_tlb.unit_price*prize_amount)/pd.unit_price) winAmt, STATUS FROM st_sl_draw_ticket_"+gameId+"_"+gameTypeId+"_"+purchaseTableName+" draw_tlb INNER JOIN st_sl_prize_details_"+gameId+"_"+gameTypeId+" pd ON draw_tlb.draw_id=pd.draw_id AND draw_tlb.rank_id=pd.prize_rank AND draw_tlb.merchant_id=pd.merchant_id WHERE draw_tlb.merchant_id="+saleMerchantId+" AND ticket_number="+validateTktBean.getTicketNumInDB()+" AND rank_id>0 AND STATUS ='UNCLAIMED';";
						logger.info("Board Query - "+query);
						boardRs = boardStmt.executeQuery(query);
						if (boardRs.next()) {
							if(boardRs.getString("ticket_number") != null) {
								long transactionId = CommonMethodsDaoImpl.getInstance().generateSLETransaction(merchantId, "PWT", connection);

								updatePstmt = connection.prepareStatement("UPDATE st_sl_draw_ticket_?_?_? SET status=? WHERE draw_id=? AND ticket_number=? AND rank_id>0;");
								updatePstmt.setInt(1, gameId);
								updatePstmt.setInt(2, gameTypeId);
								updatePstmt.setInt(3, purchaseTableName);
								updatePstmt.setString(4, "CLAIMED");
								updatePstmt.setInt(5, drawId);
								updatePstmt.setString(6, validateTktBean.getTicketNumInDB());
								updatePstmt.executeUpdate();

								double winAmount = boardRs.getDouble("winAmt");
								insertWinning = connection.prepareStatement("INSERT INTO st_sl_winning_txn_master (trans_id, merchant_id, channel_type, game_id, game_type_id, draw_id, ticket_nbr, user_id, merchant_user_id, trans_date, amount,tax_amt,net_amount, status, pay_type) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);");
								insertWinning.setLong(1, transactionId);
								insertWinning.setInt(2, merchantId);
								insertWinning.setString(3, winningChannel);
								insertWinning.setInt(4, gameId);
								insertWinning.setInt(5, gameTypeId);
								insertWinning.setInt(6, drawId);
								//insertWinning.setInt(7, boardRs.getInt("rank_id"));
								insertWinning.setString(7, boardRs.getString("ticket_number"));
								insertWinning.setLong(8, sleUserId);
								insertWinning.setLong(9, merchantUserId);
								insertWinning.setTimestamp(10, Util.getCurrentTimeStamp());
								insertWinning.setDouble(11, winAmount);
								insertWinning.setDouble(12, 0.0);
								insertWinning.setDouble(13, winAmount);
								insertWinning.setString(14, "INITIATED");
								insertWinning.setString(15, payType);
								insertWinning.executeUpdate();

								totalWinAmount += winAmount;
								drawTicketBean = new DrawTicketDataBean();
								drawTicketBean.setDrawId(drawId);
								drawTicketBean.setTransactionId(transactionId);
								drawTicketBean.setWinningAmt(winAmount);
								drawTicketBean.setPurTbleName(purchaseTableName);
								drawTicketList.add(drawTicketBean);
							}
						}
					} else {
						throw new SLEException(SLEErrors.INVALID_TICKET_NUMBER_ERROR_CODE, SLEErrors.INVALID_TICKET_NUMBER_ERROR_MESSAGE);
					}
				}

				if (!isTicketAvailable) {
					throw new SLEException(SLEErrors.INVALID_TICKET_NUMBER_ERROR_CODE, SLEErrors.INVALID_TICKET_NUMBER_ERROR_MESSAGE);
				}

				pwtRequestBean.setPwtType(payType);
				pwtRequestBean.setGameId(gameId);
				pwtRequestBean.setGameTypeId(gameTypeId);
				pwtRequestBean.setTicketNumber(validateTktBean.getTicketNumInDB());
				pwtRequestBean.setTotalAmount(totalWinAmount);
				pwtRequestBean.setRemarks(null);
			} else {
				throw new SLEException(SLEErrors.INVALID_TICKET_NUMBER_ERROR_CODE, SLEErrors.INVALID_TICKET_NUMBER_ERROR_MESSAGE);
			}
		} catch (SLEException se) {
			throw se;
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}finally{
			DBConnect.closePrepareStatementStatements(insertWinning,updatePstmt);
			DBConnect.closeStatements(boardStmt,drawStmt);
			DBConnect.closeRs(boardRs);
			DBConnect.closeRs(drawRs);
		}

		return pwtRequestBean;
	}

	public void updateWinningRequest(TPPwtResponseBean pwtBean, String requestId, String status, Connection connection,long txnId) throws SLEException {
		PreparedStatement transPstmt = null;
		try {
			transPstmt = connection.prepareStatement("UPDATE st_sl_winning_txn_master SET status=?, merchant_trans_id=?, request_id=? WHERE game_id=? AND game_type_id=? AND draw_id=? AND ticket_nbr=? and trans_id=?;");
			for(int drawId : pwtBean.getDrawTransMap().keySet()) {
				String refTransId = pwtBean.getDrawTransMap().get(drawId);

				transPstmt.setString(1, status);
				transPstmt.setString(2, refTransId);
				transPstmt.setString(3, requestId);
				transPstmt.setInt(4, pwtBean.getGameId());
				transPstmt.setInt(5, pwtBean.getGameTypeId());
				transPstmt.setInt(6, drawId);
				transPstmt.setString(7,  pwtBean.getTicketNumber());
				transPstmt.setLong(8, txnId);
				transPstmt.addBatch();
			}
			transPstmt.executeBatch();
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(20001, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}finally{
			DBConnect.closePrepareStatementStatement(transPstmt);
		}
	}

	public void updateWinningRequest(long sleTransId, String merchantTxnId, String status, Connection connection) throws SLEException {
		try {
			connection.createStatement().executeUpdate("UPDATE st_sl_winning_txn_master SET merchant_trans_id = " + merchantTxnId + ", status='" + status + "', settlement_date = '"+ Util.getCurrentTimeString() +"', settlement_status = 'DONE' WHERE trans_id=" + sleTransId + ";");
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
	}

	public TPPwtRequestBean claimWinningByAPI(String winningMerchantName, UserInfoBean userBean, String ticketNumber, String refTransactionId,String payType, Connection connection) throws SLEException {
		logger.info("-- Inside claimWinningByAPI (Dao) --");
		String query = null;
		Statement stmt = null;
		ResultSet rs = null;
		Statement drawStmt = null;
		ResultSet drawRs = null;
		Statement boardStmt = null;
		ResultSet boardRs = null;
		PreparedStatement updatePstmt = null;
		PreparedStatement insertWinning = null;

		TPPwtRequestBean pwtRequestBean = null;
		List<DrawTicketDataBean> drawTicketList = null;
		DrawTicketDataBean drawTicketBean = null;

		Map<Integer, Long> transMap = null;
		boolean isTicketAvailable = false;
		double pwtTaxPercent=0.0;
		try {
			pwtRequestBean = new TPPwtRequestBean();
			drawTicketList = new ArrayList<DrawTicketDataBean>();
			pwtRequestBean.setDrawDataList(drawTicketList);

			stmt = connection.createStatement();
			query = "SELECT merchant_trans_id FROM st_sl_winning_txn_master WHERE merchant_trans_id='"+refTransactionId+"';";
			logger.info("Ref Trans Check Query - "+query);
			rs = stmt.executeQuery(query);
			if(rs.next()) {
				throw new SLEException(SLEErrors.DUPLICATE_TRANSACTION_ID_ERROR_CODE, SLEErrors.DUPLICATE_TRANSACTION_ID_ERROR_MESSAGE);
			}

			drawStmt = connection.createStatement();
			boardStmt = connection.createStatement();
			
			ValidateTicketBean validateTktBean = new ValidateTicketBean(ticketNumber);
			SportsLotteryUtils.validateTicketPWT(validateTktBean, winningMerchantName, connection);

			if (validateTktBean.isValid()) {
				pwtTaxPercent=Double.parseDouble(Util.getPropertyValue("PLAYER_WINNING_TAX_PERCENTAGE"));
				double pwtWinTaxApplcbleAmt=Double.parseDouble(Util.getPropertyValue("PLAYER_WINNING_TAX_APPLICABLE_AMOUNT"));
				String saleMerchantName = validateTktBean.getMerchantCode();
				int saleMerchantId = UtilityFunctions.getMerchantIdFromMerchantName(saleMerchantName, connection);
				int winningMerchantId = UtilityFunctions.getMerchantIdFromMerchantName(winningMerchantName, connection);
				int gameId = validateTktBean.getGameid();
				int gameTypeId = validateTktBean.getGameTypeId();

				query = "SELECT draw.draw_id, draw_status, purchase_table_name, claim_start_date, claim_end_date, verification_date FROM st_sl_draw_master_"+gameId+" draw INNER JOIN st_sl_draw_merchant_mapping_"+gameId+" merchant ON draw.draw_id=merchant.draw_id INNER JOIN  st_sl_game_tickets_"+gameId+"_"+gameTypeId+"  gt ON gt.draw_id=draw.draw_id  INNER JOIN st_sl_sale_txn_master stm on stm.trans_id=gt.trans_id WHERE stm.merchant_id="+saleMerchantId+" AND merchant.merchant_id="+saleMerchantId+" AND stm.ticket_nbr="+validateTktBean.getTicketNumInDB();
				logger.info("Draw Query - "+query);
				drawRs = drawStmt.executeQuery(query);
				double totalWinAmount = 0.0;
				while (drawRs.next()) {
					String drawStatus = drawRs.getString("draw_status");

					if("RMS".equalsIgnoreCase(saleMerchantName) && !validateTktBean.isDateBypass()) {
						Timestamp currentDate = Util.getCurrentTimeStamp();
						Timestamp claimStartDate = drawRs.getTimestamp("claim_start_date");
						Timestamp claimEndDate = drawRs.getTimestamp("claim_end_date");
						Timestamp verificationDate = drawRs.getTimestamp("verification_date");
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

						if(!"CLAIM ALLOW".equals(drawStatus)) {
							throw new SLEException(SLEErrors.TICKET_CANNOT_CLAIMED_ERROR_CODE, SLEErrors.TICKET_CANNOT_CLAIMED_ERROR_MESSAGE);
						}
					}

					isTicketAvailable = true;
					int purchaseTableName = drawRs.getInt("purchase_table_name");
					int drawId = drawRs.getInt("draw_id");
					

					if (validateTktBean.isDateBypass() || "CLAIM ALLOW".equals(drawStatus)) {
						transMap = new HashMap<Integer, Long>();
						query = "SELECT ticket_number, rank_id, SUM((bet_amount_multiple*draw_tlb.unit_price*prize_amount)/pd.unit_price) winAmt, STATUS FROM st_sl_draw_ticket_"+gameId+"_"+gameTypeId+"_"+purchaseTableName+" draw_tlb INNER JOIN st_sl_prize_details_"+gameId+"_"+gameTypeId+" pd ON draw_tlb.draw_id=pd.draw_id AND draw_tlb.rank_id=pd.prize_rank AND draw_tlb.merchant_id=pd.merchant_id WHERE draw_tlb.merchant_id="+saleMerchantId+" AND ticket_number="+validateTktBean.getTicketNumInDB()+" AND rank_id>0 AND STATUS ='UNCLAIMED';";
						logger.info("Board Query - "+query);
						boardRs = boardStmt.executeQuery(query);
						if (boardRs.next()) {
							if(boardRs.getString("ticket_number") != null) {
								long transactionId = CommonMethodsDaoImpl.getInstance().generateSLETransaction(winningMerchantId, "PWT", connection);

								updatePstmt = connection.prepareStatement("UPDATE st_sl_draw_ticket_?_?_? SET status=? WHERE draw_id=? AND ticket_number=? AND rank_id>0;");
								updatePstmt.setInt(1, gameId);
								updatePstmt.setInt(2, gameTypeId);
								updatePstmt.setInt(3, purchaseTableName);
								updatePstmt.setString(4, "CLAIMED");
								updatePstmt.setInt(5, drawId);
								updatePstmt.setString(6, validateTktBean.getTicketNumInDB());
								updatePstmt.executeUpdate();

								double winAmount = boardRs.getDouble("winAmt");
								double taxAmount=winAmount>pwtWinTaxApplcbleAmt?Double.parseDouble(Util.getCurrentAmountFormatForMobile(winAmount*pwtTaxPercent*.01)):0.0;
								
								insertWinning = connection.prepareStatement("INSERT INTO st_sl_winning_txn_master (trans_id, merchant_id, channel_type, game_id, game_type_id, draw_id, ticket_nbr, user_id, merchant_user_id, trans_date, amount,tax_amt,net_amount, status, pay_type, merchant_trans_id) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);");
								insertWinning.setLong(1, transactionId);
								insertWinning.setInt(2, winningMerchantId);
								insertWinning.setString(3, "API");
								insertWinning.setInt(4, gameId);
								insertWinning.setInt(5, gameTypeId);
								insertWinning.setInt(6, drawId);
								//insertWinning.setInt(7, boardRs.getInt("rank_id"));
								insertWinning.setString(7, boardRs.getString("ticket_number"));
								insertWinning.setLong(8, userBean.getSleUserId());
								insertWinning.setLong(9, userBean.getMerchantUserId());
								insertWinning.setTimestamp(10, Util.getCurrentTimeStamp());
								insertWinning.setDouble(11, winAmount);
								insertWinning.setDouble(12,taxAmount);
								insertWinning.setDouble(13, Double.parseDouble(Util.getCurrentAmountFormatForMobile(winAmount-taxAmount)));
								insertWinning.setString(14, "DONE");
								insertWinning.setString(15, "API_PAY");
								insertWinning.setString(16, refTransactionId);
								
								totalWinAmount += winAmount;
								insertWinning.executeUpdate();
								drawTicketBean = new DrawTicketDataBean();
								drawTicketBean.setDrawId(drawId);
								drawTicketBean.setTransactionId(transactionId);
								drawTicketBean.setWinningAmt(winAmount);
								drawTicketList.add(drawTicketBean);

								transMap.put(drawId, transactionId);
							} else {
								throw new SLEException(SLEErrors.TICKET_CANNOT_CLAIMED_ERROR_CODE, SLEErrors.TICKET_CANNOT_CLAIMED_ERROR_MESSAGE);
							}
						}
					} else {
						throw new SLEException(SLEErrors.INVALID_TICKET_NUMBER_ERROR_CODE, SLEErrors.INVALID_TICKET_NUMBER_ERROR_MESSAGE);
					}
				}

				if (!isTicketAvailable) {
					throw new SLEException(SLEErrors.INVALID_TICKET_NUMBER_ERROR_CODE, SLEErrors.INVALID_TICKET_NUMBER_ERROR_MESSAGE);
				}
				
				pwtRequestBean.setPwtType(payType);
				pwtRequestBean.setGameId(gameId);
				pwtRequestBean.setGameTypeId(gameTypeId);
				pwtRequestBean.setTicketNumber(validateTktBean.getTicketNumInDB());
				pwtRequestBean.setTotalAmount(totalWinAmount);
				pwtRequestBean.setRemarks(null);
				pwtRequestBean.setTransMap(transMap);
			} else {
				throw new SLEException(SLEErrors.INVALID_TICKET_NUMBER_ERROR_CODE, SLEErrors.INVALID_TICKET_NUMBER_ERROR_MESSAGE);
			}
		} catch (SLEException se) {
			throw se;
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closePstmt(updatePstmt, insertWinning);
			DBConnect.closeStatements(boardStmt,drawStmt);
			DBConnect.closeRs(boardRs);
			DBConnect.closeRs(drawRs);
		}

		return pwtRequestBean;
	}
	
	public void updatePurTableStatus(int gameId,int gameTypeId,int drawId,int purchaseTableName,String ticketNbr,String status,Connection con){
		PreparedStatement transPstmt = null;
		try {
			transPstmt = con.prepareStatement("UPDATE st_sl_draw_ticket_?_?_? SET status=? WHERE draw_id=? AND ticket_number=? AND rank_id>0;");
			transPstmt.setInt(1, gameId);
			transPstmt.setInt(2, gameTypeId);
			transPstmt.setInt(3,purchaseTableName);
			transPstmt.setString(4, status);
			transPstmt.setInt(5, drawId);
			transPstmt.setString(6, ticketNbr);
			
			transPstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			DBConnect.closePrepareStatementStatement(transPstmt);
		}
		
		
	}
	
	public void updateWinningStatus(String ticketNumber, String status,long transId, Connection connection) throws SLEException {
		PreparedStatement transPstmt = null;
		try {
			transPstmt = connection.prepareStatement("UPDATE st_sl_winning_txn_master SET status=? WHERE  ticket_nbr=? and trans_id=?;");
			transPstmt.setString(1, status);
			transPstmt.setString(2, ticketNumber);
			transPstmt.setLong(3, transId);
			transPstmt.executeUpdate();
		}  catch (Exception e) {
			e.printStackTrace();
		}finally{
			DBConnect.closePrepareStatementStatement(transPstmt);
		}
	}
}