package com.skilrock.sle.gamePlayMgmt.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.skilrock.sle.common.DBConnect;
import com.skilrock.sle.common.SLELogger;
import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.common.javaBeans.UserInfoBean;
import com.skilrock.sle.commonMethod.daoImpl.CommonMethodsDaoImpl;
import com.skilrock.sle.dataMgmt.javaBeans.CancelTransactionAPIBean;
import com.skilrock.sle.embedded.playMgmt.common.javaBeans.CancelTicketBean;

public class CancelTicketDaoImpl {
	private static final SLELogger logger = SLELogger.getLogger(CancelTicketDaoImpl.class.getName());
	
	public void cancelTicket(CancelTicketBean cancelTicketBean, UserInfoBean userInfoBean, Connection con,boolean isTktAutoCancel) throws SLEException{
//		logger.debug("***** Inside cancelTicket Method");

		long cancelTnxId = 0;

		Statement stmt = null;
		int isUpdated = 0;
		StringBuilder query = null;
	    String drawStatus=null ;
		try {
			query = new StringBuilder();

			fetchTransactionStatus(cancelTicketBean, userInfoBean, con); 
			drawStatus = CommonMethodsDaoImpl.getInstance().fetchTicketWiseDrawStatus(cancelTicketBean.getGameId(), cancelTicketBean.getGameTypeId(), cancelTicketBean.getTktToCancel(), con);
			if(!"ACTIVE".equalsIgnoreCase(drawStatus) && !isTktAutoCancel){
				throw new SLEException(SLEErrors.CANCELLED_TICKET_INITIATE_ERROR_CODE,SLEErrors.CANCELLED_TICKET_INITIATE_ERROR_MESSAGE);
			}
			
			cancelTnxId = CommonMethodsDaoImpl.getInstance().generateSLETransaction(userInfoBean.getMerchantId(), "SALE_REFUND", con);
			cancelTicketBean.setCancelTxnId(cancelTnxId);

			query.setLength(0);

			query.append(
					"INSERT INTO st_sl_sale_refund_txn_master(trans_id, merchant_id, channel_type, game_id, game_type_id, ticket_nbr, user_id, merchant_user_id, is_auto_cancel, cancel_type, trans_date, amount, STATUS, sale_trans_id) ")
					.append("VALUES(").append(cancelTnxId).append(", ")
					.append(userInfoBean.getMerchantId()).append(", '")
					.append(cancelTicketBean.getInterfaceType()).append("', ")
					.append(cancelTicketBean.getGameId()).append(", ").append(cancelTicketBean.getGameTypeId())
					.append(", ").append(cancelTicketBean.getTktToCancel())
					.append(", ").append(userInfoBean.getSleUserId())
					.append(", ").append(userInfoBean.getMerchantUserId())
					.append(", '").append(cancelTicketBean.getIsAutoCancel())
					.append("', '").append(cancelTicketBean.getCancelType())
					.append("', '").append(cancelTicketBean.getCancelDate())
					.append("', '").append(cancelTicketBean.getCancelAmount()).append("', 'INITIATED'")
					.append(", ").append(cancelTicketBean.getSaleTxnId()).append(")");

			stmt = con.createStatement();
			isUpdated = stmt.executeUpdate(query.toString());
			if (isUpdated == 0)
				throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
			
			updateCancelStatusInPurchaseTable(cancelTicketBean.getSaleTxnId(), "CANCELLED", "Y", con);
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
		DBConnect.closeStmt(stmt);
		}
	}
	
	public void updateCancelStatusInPurchaseTable(long saleTransId, String status, String isCancel, Connection con) throws SLEException {
		int gameId = 0;
		int gameTypeId = 0;
		int drawId = 0;
		int purchaseTableName = 0;
		String ticketNumber = null;
		PreparedStatement pStmt = null;
		int isUpdated = 0;
		Statement stmt = null;
		ResultSet rs=null;
		try {
			String query = "SELECT game_id, game_type_id, ticket_nbr FROM st_sl_sale_txn_master WHERE trans_id=" + saleTransId + ";";
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
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
			}

			query = "UPDATE st_sl_draw_ticket_" + gameId + "_" + gameTypeId
					+ "_" + purchaseTableName + " SET STATUS = '" + status
					+ "' WHERE ticket_number='" + ticketNumber + "';";
			stmt.executeUpdate(query);

			pStmt = con.prepareStatement("update st_sl_sale_txn_master set is_cancel = '" + isCancel + "' where trans_id = ?");
			pStmt.setLong(1, saleTransId);
			isUpdated = pStmt.executeUpdate();
			if (isUpdated == 0)
				throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		DBConnect.closeConnection(pStmt, rs);
		DBConnect.closeStmt(stmt);
	}

	public void updateSportsLotteryCancelTicket(Boolean isCanceled, String merchantTranId, Long saleTransId, Long cancelTxnId, Connection con) throws SLEException {
		PreparedStatement pStmt = null;
		int isUpdated = 0;
		try {
			if(isCanceled) {
				pStmt = con.prepareStatement("update st_sl_sale_refund_txn_master set status = 'DONE', merchant_trans_id = ?, sale_trans_id = ? where trans_id = ?");
				pStmt.setString(1, merchantTranId);
				pStmt.setLong(2, saleTransId);
				pStmt.setLong(3, cancelTxnId);
			}
			else {
				pStmt = con.prepareStatement("update st_sl_sale_refund_txn_master set status = 'FAILED' where trans_id = ?");
				pStmt.setLong(1, cancelTxnId);
			}
			isUpdated = pStmt.executeUpdate();

			if (isUpdated == 0)
				throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);

			if(!isCanceled) {
				updateCancelStatusInPurchaseTable(saleTransId, "UNCLAIMED", "N", con);
				/*int gameId = 0;
				int gameTypeId = 0;
				int drawId = 0;
				int purchaseTableName = 0;
				String ticketNumber = null;
				String query = "SELECT game_id, game_type_id, ticket_nbr FROM st_sl_sale_txn_master WHERE trans_id="+saleTransId+";";
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				if(rs.next()) {
					gameId = rs.getInt("game_id");
					gameTypeId = rs.getInt("game_type_id");
					ticketNumber = rs.getString("ticket_nbr");
				}

				if(ticketNumber != null) {
					query = "SELECT draw_id FROM st_sl_game_tickets_"+gameId+"_"+gameTypeId+" WHERE ticket_number='"+ticketNumber+"';";
					rs = stmt.executeQuery(query);
					if(rs.next()) {
						drawId = rs.getInt("draw_id");
					}
				}

				if(drawId>0) {
					query = "SELECT purchase_table_name FROM st_sl_draw_master_"+gameId+" WHERE draw_id="+drawId+";";
					rs = stmt.executeQuery(query);
					if(rs.next()) {
						purchaseTableName = rs.getInt("purchase_table_name");
					}
				}

				query = "UPDATE st_sl_draw_ticket_"+gameId+"_"+gameTypeId+"_"+purchaseTableName+" SET STATUS='CANCELLED' WHERE ticket_number='"+ticketNumber+"';";
				stmt.executeUpdate(query);

				pStmt = con.prepareStatement("update st_sl_sale_txn_master set is_cancel = ? where trans_id = ?");
				pStmt.setString(1, "Y");
				pStmt.setLong(2, saleTransId);
				isUpdated = pStmt.executeUpdate();
				if (isUpdated == 0)
					throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);*/
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
	}
	
	public void fetchTransactionStatus(CancelTicketBean cancelTicketBean, UserInfoBean userInfoBean, Connection con) throws SLEException {
		logger.debug("***** Inside fetchTransactionStatus Method");

		Statement stmt = null;
		StringBuilder query = null;
		ResultSet rs = null;
		long saleTnxId = 0;
		int gameId = 0;
		int gameTypeId = 0;
		double amount = 0;
		try {
			query = new StringBuilder();
			query.append("SELECT trans_id, game_id, game_type_id, amount FROM st_sl_sale_txn_master WHERE trans_id = ").append(cancelTicketBean.getTxnIdToCancel())
			.append(" AND is_cancel = 'N'");
			stmt = con.createStatement();
			rs = stmt.executeQuery(query.toString());

			if (rs.next()) {
				saleTnxId = rs.getInt("trans_id");
				gameId = rs.getInt("game_id");
				gameTypeId = rs.getInt("game_type_id");
				amount = rs.getDouble("amount");
				cancelTicketBean.setGameId(gameId);
				cancelTicketBean.setGameTypeId(gameTypeId);
				cancelTicketBean.setCancelAmount(amount);
				cancelTicketBean.setSaleTxnId(saleTnxId);
			} else {
				throw new SLEException(SLEErrors.CANCELLED_TICKET_ERROR_CODE, SLEErrors.CANCELLED_TICKET_ERROR_MESSAGE);
			}

			query.setLength(0);

			query.append("SELECT rpc_total, draw_id, ticket_number, barcode_count FROM st_sl_game_tickets_").append(gameId).append("_").append(gameTypeId)
					.append(" WHERE trans_id = ").append(cancelTicketBean.getSaleTxnId());
			stmt = con.createStatement();
			rs = stmt.executeQuery(query.toString());

			if (rs.next()) {
				cancelTicketBean.setTktToCancel(rs.getString("ticket_number"));
				cancelTicketBean.setReprintCount(rs.getInt("rpc_total"));
				cancelTicketBean.setBarcodeCount(rs.getInt("barcode_count"));
				cancelTicketBean.setDrawId(rs.getInt("draw_id"));
			} else {
				throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
			}
			
			query.setLength(0);
			
			query.append("select type_disp_name from st_sl_game_type_merchant_mapping where game_type_id = ").append(cancelTicketBean.getGameTypeId())
				.append(" AND merchant_id = ").append(userInfoBean.getMerchantId());
			stmt = con.createStatement();
			rs = stmt.executeQuery(query.toString());

			if (rs.next()) {
				cancelTicketBean.setGameTypeName(rs.getString("type_disp_name"));
			} else {
				throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
			}
			
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
	
	public boolean checkForManualCancel(String txnId, UserInfoBean userInfoBean, Connection con) throws SLEException {
		boolean isCancel = true;
		StringBuilder query = new StringBuilder();
		Statement stmt = null;
		ResultSet rs = null;
		try {
//			query.append("SELECT trans_id FROM st_sl_sale_refund_txn_master WHERE merchant_id = ")
//					.append(userInfoBean.getMerchantId())
//					.append(" AND merchant_user_id = ").append(userInfoBean.getMerchantUserId()).append(" AND trans_id > ").append(txnId);
//			stmt = con.createStatement();
//			rs = stmt.executeQuery(query.toString());
//			if(rs.next()) {
//				isCancel = false;
//				return isCancel;
//			}
			
			query.setLength(0);
			
			query.append("SELECT trans_id FROM st_sl_winning_txn_master WHERE merchant_id = ").append(userInfoBean.getMerchantId())
					.append(" AND merchant_user_id = ").append(userInfoBean.getMerchantUserId())
					.append(" AND trans_id > ").append(txnId);
			stmt = con.createStatement();
			rs = stmt.executeQuery(query.toString());
			if (rs.next()) {
				isCancel = false;
				return isCancel;
			}

			query.setLength(0);

			query.append("SELECT trans_id FROM st_sl_sale_txn_master WHERE merchant_id = ").append(userInfoBean.getMerchantId())
					.append(" AND merchant_user_id = ").append(userInfoBean.getMerchantUserId())
					.append(" AND channel_type='TERMINAL' AND trans_id > ").append(txnId);
			stmt = con.createStatement();
			rs = stmt.executeQuery(query.toString());
			if (rs.next()) {
				isCancel = false;
				return isCancel;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return isCancel;
	}
	
	public long checkForManualCancelWeb(UserInfoBean userInfoBean, Connection con) throws SLEException {
		StringBuilder query = new StringBuilder();
		Statement stmt = null;
		ResultSet rs = null;
		long lastTxnId=0;
		String channelType=null;
		try {
			query.append("select trans_id,channel_type from st_sl_sale_txn_master where status='DONE' and channel_type='WEB' and merchant_user_id=").append(userInfoBean.getMerchantUserId()).append(" and merchant_id=").append(userInfoBean.getMerchantId()).append(" order by trans_id desc limit 1");
			stmt = con.createStatement();
			rs = stmt.executeQuery(query.toString());
			if (rs.next()) {
				lastTxnId=rs.getLong("trans_id");
				channelType=rs.getString("channel_type");
				if(!channelType.equalsIgnoreCase("WEB")){
					return 0;
				}
			}else{
			    return lastTxnId; 
			}
			query.setLength(0);
			query.append("SELECT trans_id FROM st_sl_winning_txn_master WHERE merchant_id = ").append(userInfoBean.getMerchantId())
					.append(" AND merchant_user_id = ").append(userInfoBean.getMerchantUserId())
					.append(" AND trans_id > ").append(lastTxnId);
			rs = stmt.executeQuery(query.toString());
			if (rs.next()) {
				return 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}finally{
			DBConnect.closeConnection(stmt, rs);
		}
		return lastTxnId;
	}

	public void updateSetteledCancelTicketStatus(Boolean isCanceled, String merchantTranId, Long saleTransId, Long cancelTxnId, Connection con) throws SLEException {
		PreparedStatement pStmt = null;
		int isUpdated = 0;
		try {
			if(isCanceled) {
				pStmt = con.prepareStatement("update st_sl_sale_refund_txn_master set status = 'DONE', merchant_trans_id = ?, sale_trans_id = ?, settlement_status = ?, settlement_date = ? where trans_id = ?");
				pStmt.setString(1, merchantTranId);
				pStmt.setLong(2, saleTransId);
				pStmt.setString(3, "DONE");
				pStmt.setTimestamp(4, Util.getCurrentTimeStamp());
				pStmt.setLong(5, cancelTxnId);
			}
			else {
				pStmt = con.prepareStatement("update st_sl_sale_refund_txn_master set status = 'FAILED', settlement_status = ?, settlement_date = ? where trans_id = ?");
				pStmt.setString(1, "DONE");
				pStmt.setTimestamp(2, Util.getCurrentTimeStamp());
				pStmt.setLong(3, cancelTxnId);
			}
			isUpdated = pStmt.executeUpdate();

			if (isUpdated == 0)
				throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}finally{
			DBConnect.closePstmt(pStmt);
		}
	}

	public boolean cancelTransactionDaoAPI(UserInfoBean userBean, CancelTransactionAPIBean cancelBean, Connection con) throws SLEException{
		boolean isSuccess = false;
		PreparedStatement pstmt = null;
		String query = null;
		int isUpated = 0;
		try{
			query = "Insert Into st_sl_sale_refund_txn_master(trans_id,merchant_id,channel_type,game_id,game_type_id,ticket_nbr,user_id,merchant_user_id,is_auto_cancel,cancel_type,trans_date,amount,status,merchant_trans_id,sale_trans_id) Values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			pstmt = con.prepareStatement(query);
			pstmt.setLong(1, cancelBean.getCancelTxnId());
			pstmt.setInt(2, userBean.getMerchantId());
			pstmt.setString(3, cancelBean.getChannelType());
			pstmt.setInt(4, cancelBean.getGameId());
			pstmt.setInt(5, cancelBean.getGameTypeId());
			pstmt.setString(6, cancelBean.getTktNbr());
			pstmt.setLong(7, cancelBean.getUserId());
			pstmt.setInt(8, userBean.getMerchantUserId());
			pstmt.setString(9, cancelBean.getIsAutoCancel());
			pstmt.setString(10, cancelBean.getCancelType());
			pstmt.setString(11, Util.getCurrentTimeString());
			pstmt.setDouble(12, cancelBean.getCancelAmount());
			pstmt.setString(13, "DONE");
			pstmt.setString(14, cancelBean.getRefTransId());
			pstmt.setLong(15, cancelBean.getSaleTxnId());
			
			isUpated = pstmt.executeUpdate();
			if(isUpated!=0){
				pstmt.clearParameters();
				query = "Update st_sl_sale_txn_master set is_cancel = 'Y' Where ticket_nbr =? and trans_id =?";
				pstmt = con.prepareStatement(query);
				pstmt.setString(1, cancelBean.getTktNbr());
				pstmt.setLong(2, cancelBean.getSaleTxnId());
				
				isUpated = pstmt.executeUpdate();
				if(isUpated!=0){
					pstmt.clearParameters();
					query = "Update st_sl_draw_ticket_"+cancelBean.getGameId()+"_"+cancelBean.getGameTypeId()+"_"+cancelBean.getPurchaseTableName()+" set status = 'CANCELLED' Where ticket_number =?";
					pstmt = con.prepareStatement(query);
					pstmt.setString(1, cancelBean.getTktNbr());
					isUpated = pstmt.executeUpdate();
					if(isUpated !=0){
						isSuccess = true;
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			if(e.getErrorCode()==1){
				
			}else{
				throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}finally{
			DBConnect.closePstmt(pstmt);
		}
		return isSuccess;
		
	}
	
}
