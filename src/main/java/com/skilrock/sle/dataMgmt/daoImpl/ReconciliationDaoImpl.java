package com.skilrock.sle.dataMgmt.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skilrock.sle.common.DBConnect;
import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.dataMgmt.javaBeans.ReconciliationBean;

public class ReconciliationDaoImpl {
	private static final Logger logger = LoggerFactory.getLogger(ReconciliationDaoImpl.class);
	
	private ReconciliationDaoImpl() {
	}

	public enum Single {
		INSTANCE;
		ReconciliationDaoImpl instance = new ReconciliationDaoImpl();

		public ReconciliationDaoImpl getInstance() {
			if (instance == null)
				return new ReconciliationDaoImpl();
			else
				return instance;
		}
	}

	public void updateSettlementTxnStatus(int settlementId, Connection con) throws SLEException {
		Statement stmt = null;
		logger.info("***** Inside updateTxnStatus Method");
		try {
			stmt = con.createStatement();
			stmt.executeUpdate("UPDATE st_sl_settlement SET response_time = '"+Util.getCurrentTimeStamp()+"' WHERE id = " + settlementId);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}finally{
			DBConnect.closeStatement(stmt);
		}
	}
	
	public long fetchLastSettleTxnId(int merchantId, Connection con) throws SLEException {
		long txnId = 0;
		Statement stmt = null;
		ResultSet rs = null;
		String query = null;
		try {
			query = "SELECT IFNULL(MAX(last_txn_id), 0) last_txn_id FROM st_sl_settlement WHERE merchant_id = " + merchantId + ";";
			logger.info("Query For Fetching Last Settled Txn Id " + query);
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			if(rs.next()) {
				txnId = rs.getLong("last_txn_id");
			} else {
				throw new SLEException(SLEErrors.INVALID_MERCHANT_NAME_ERROR_CODE, SLEErrors.INVALID_MERCHANT_NAME_ERROR_MESSAGE);
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
		return txnId;
	}
	
	public void createLastSettlementId(long maxTxnId, int merchantId, Timestamp reqTime, Connection con) throws SLEException {
		PreparedStatement pStmt = null;
		try {
			pStmt = con.prepareStatement("INSERT INTO st_sl_settlement(merchant_id, last_txn_id, request_time, response_time) VALUES(?, ?, ?, ?)");
			pStmt.setInt(1, merchantId);
			pStmt.setLong(2, maxTxnId);
			pStmt.setTimestamp(3, reqTime);
			pStmt.setTimestamp(4, Util.getCurrentTimeStamp());
			pStmt.executeUpdate();
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
	
	public void fetchInitiatedSaleTxns(List<ReconciliationBean> reconciliationBeans, int merchantId, long lastSettledTxnId, long maxTxnId, Connection con) throws SLEException {
		Statement stmt = null;
		ResultSet rs = null;
		ReconciliationBean reconciliationBean = null;
		String query = null;
		logger.info("***** Inside fetchInitiatedSaleTxns Method");
		try {
			stmt = con.createStatement();
			query = "SELECT trans_id,ticket_nbr FROM st_sl_sale_txn_master WHERE STATUS = 'INITIATED' AND merchant_id = " + merchantId + " AND trans_id > " + lastSettledTxnId + ";";

			logger.info("Query For Fetching Sale Reconciliation Data {}", query);
			
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				reconciliationBean = new ReconciliationBean();
				reconciliationBean.setEngineTxnId(rs.getString("trans_id"));
				reconciliationBean.setEngineSaleTxnId(reconciliationBean.getEngineTxnId());
				reconciliationBean.setTicktNo(rs.getLong("ticket_nbr"));
				reconciliationBeans.add(reconciliationBean);
			}
			logger.debug("Sale Txn To Reconcile {}", reconciliationBeans);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}finally{
			DBConnect.closeConnection(stmt, rs);
		}
	}
	
	public void fetchInitiatedRefundTxns(List<ReconciliationBean> reconciliationBeans, int merchantId, long lastSettledTxnId, long maxTxnId, Connection con) throws SLEException {
		Statement stmt = null;
		ResultSet rs = null;
		ReconciliationBean reconciliationBean = null;
		String query = null;
		logger.info("***** Inside fetchInitiatedRefundTxns Method");

		try {
			stmt = con.createStatement();
			query = "SELECT trans_id, sale_trans_id,ticket_nbr FROM st_sl_sale_refund_txn_master WHERE STATUS = 'INITIATED' AND merchant_id = " + merchantId + " AND trans_id > " + lastSettledTxnId + ";";

			logger.info("Query For Fetching REFUND Reconciliation Data {}", query);

			rs = stmt.executeQuery(query);
			while(rs.next()) {
				reconciliationBean = new ReconciliationBean();
				reconciliationBean.setEngineTxnId(rs.getString("trans_id"));
				reconciliationBean.setEngineSaleTxnId(rs.getString("sale_trans_id"));
				reconciliationBean.setTicktNo(rs.getLong("ticket_nbr"));
				reconciliationBeans.add(reconciliationBean);
			}
			logger.debug("REFUND Txn To Reconcile {}", reconciliationBeans);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally{
			DBConnect.closeConnection(stmt, rs);
		}
	}

	public void fetchInitiatedPWTTxns(List<ReconciliationBean> reconciliationBeans, int merchantId, long lastSettledTxnId, long maxTxnId, Connection con,String merchantDevName) throws SLEException {
		Statement stmt = null;
		ResultSet rs = null;
		ReconciliationBean reconciliationBean = null;
		String query = null;
		logger.info("***** Inside fetchInitiatedPWTTxns Method");

		try {
			stmt = con.createStatement();
			if("PMS".equalsIgnoreCase(merchantDevName)){
				query="SELECT wtm.trans_id trans_id ,stm.trans_id saleTransId ,wtm.game_id,wtm.game_type_id,wtm.ticket_nbr ticket_nbr, stm.merchant_user_id player_id,wtm.net_amount win_amt FROM st_sl_winning_txn_master  wtm INNER JOIN st_sl_sale_txn_master stm ON  stm.ticket_nbr=wtm.ticket_nbr   WHERE wtm.STATUS = 'INITIATED' AND wtm.merchant_id =" + merchantId + "  AND wtm.trans_id >" + lastSettledTxnId + ";";
				logger.info("Query For Fetching PWT Reconciliation Data  {}", query);
				rs = stmt.executeQuery(query);
				while(rs.next()) {
					reconciliationBean = new ReconciliationBean();
					reconciliationBean.setEngineTxnId(rs.getString("trans_id"));
					reconciliationBean.setEngineSaleTxnId(rs.getString("saleTransId"));
					reconciliationBean.setPlayerId(rs.getInt("player_id"));
					reconciliationBean.setWinAmt(rs.getDouble("win_amt"));
					reconciliationBean.setTicktNo(rs.getLong("ticket_nbr"));
					reconciliationBean.setGameId(rs.getInt("game_id"));
					reconciliationBean.setGameTypeId(rs.getInt("game_type_id"));
					reconciliationBeans.add(reconciliationBean);
				}
				
			} else{
				query = "SELECT trans_id,ticket_nbr FROM st_sl_winning_txn_master WHERE STATUS = 'INITIATED' AND merchant_id = " + merchantId + " AND trans_id > " + lastSettledTxnId + ";";

				logger.info("Query For Fetching PWT Reconciliation Data {}", query);

				rs = stmt.executeQuery(query);
				while(rs.next()) {
					reconciliationBean = new ReconciliationBean();
					reconciliationBean.setEngineTxnId(rs.getString("trans_id"));
					reconciliationBean.setEngineSaleTxnId(rs.getString("trans_id"));
					reconciliationBean.setTicktNo(rs.getLong("ticket_nbr"));
					reconciliationBeans.add(reconciliationBean);
				}
			}
			logger.debug("PWT Txn To Reconcile {}", reconciliationBeans);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally{
			DBConnect.closeConnection(stmt, rs);
		}
	
	}
	
	public void settletTpSaleAndRefundTxn(Map<String, List<ReconciliationBean>> recMap,Connection con){
		PreparedStatement pstmt;
		try {
			for (Map.Entry<String, List<ReconciliationBean>> entry : recMap.entrySet()) {
				if("SALE".equals(entry.getKey())){
					pstmt=con.prepareStatement("UPDATE st_sl_sale_txn_master SET status ='FAILED',settlement_date='"+Util.getCurrentTimeString()+"',settlement_status='DONE' Where trans_id=?");
					for(ReconciliationBean recBean:entry.getValue()){
						pstmt.setString(1, recBean.getEngineTxnId());
						pstmt.addBatch();
						
					}
					pstmt.executeBatch();
				}else if("REFUND".equals(entry.getKey())){
					pstmt=con.prepareStatement("UPDATE st_sl_sale_refund_txn_master SET status ='FAILED',settlement_date='"+Util.getCurrentTimeString()+"',settlement_status='DONE' Where trans_id=?");
					for(ReconciliationBean recBean:entry.getValue()){
						pstmt.setString(1, recBean.getEngineTxnId());
						pstmt.addBatch();
					}
					pstmt.executeBatch();
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
}
