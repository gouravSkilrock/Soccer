package com.skilrock.sle.gamePlayMgmt.controllerImpl;

import java.sql.Connection;

import javax.transaction.UserTransaction;

import com.skilrock.sle.common.DBConnect;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.common.javaBeans.UserInfoBean;
import com.skilrock.sle.commonMethod.daoImpl.CommonMethodsDaoImpl;
import com.skilrock.sle.dataMgmt.javaBeans.CancelTransactionAPIBean;
import com.skilrock.sle.embedded.playMgmt.common.javaBeans.CancelTicketBean;
import com.skilrock.sle.gamePlayMgmt.daoImpl.CancelTicketDaoImpl;

public class CancelTicketControllerImpl {
	
	public void cancelTicket(CancelTicketBean cancelTicketBean, UserInfoBean userInfoBean,boolean isTxnAutoCancel) throws SLEException{
//		logger.debug("***** Inside cancelTicket Method");

		Connection con = null;
		CancelTicketDaoImpl cancelTicketDaoImpl = null;
		UserTransaction userTxn = null;
		try { 
			con = DBConnect.getConnection();
			userTxn = DBConnect.startTransaction();
			
			cancelTicketDaoImpl = new CancelTicketDaoImpl();
			cancelTicketDaoImpl.cancelTicket(cancelTicketBean, userInfoBean, con,isTxnAutoCancel);
			userTxn.commit();
		} catch (SLEException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(con);
		}
	}

	public void updateSportsLotteryCancelTicket(Boolean isCanceled, String merchantTranId, Long saleTxnId, Long cancelTxnId) throws SLEException {
		CancelTicketDaoImpl daoImpl = null;
		Connection con = null;
		UserTransaction userTxn = null;
		try {
			con = DBConnect.getConnection();
			userTxn = DBConnect.startTransaction();

			daoImpl = new CancelTicketDaoImpl();

			daoImpl.updateSportsLotteryCancelTicket(isCanceled, merchantTranId, saleTxnId, cancelTxnId,con);
			userTxn.commit();
		} catch (SLEException e) {
			DBConnect.rollBackUserTransaction(userTxn);
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			DBConnect.rollBackUserTransaction(userTxn);
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(con);
		}
	}
	
	public boolean checkForManualCancel(String txnId, UserInfoBean userInfoBean) throws SLEException {
		boolean isCancel = false;
		Connection con = null;
		try {
			con = DBConnect.getConnection();

			isCancel = new CancelTicketDaoImpl().checkForManualCancel(txnId, userInfoBean, con);
		} catch (SLEException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(con);
		}
		return isCancel;
	}
	
	public long checkForManualCancelWeb(UserInfoBean userInfoBean) throws SLEException {
		long lastTxnId=0;
		Connection con = null;
		try {
			con = DBConnect.getConnection();
			lastTxnId = new CancelTicketDaoImpl().checkForManualCancelWeb(userInfoBean, con);
		} catch (SLEException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(con);
		}
		return lastTxnId;
	}

	public boolean cancelTransactionControllerAPI(UserInfoBean userBean, CancelTransactionAPIBean cancelBean) throws SLEException{
		Connection con = null;
		boolean isSuccess = false;
		UserTransaction userTxn = null;
		long trxId = 0;
		try{
			con = DBConnect.getConnection();
			CommonMethodsDaoImpl.getInstance().fetchAndVerifyTpUser(userBean, con);
			CommonMethodsDaoImpl.getInstance().fetchAndVerifyTpTransaction(userBean, cancelBean, con);			
			userTxn = DBConnect.startTransaction();
			trxId = CommonMethodsDaoImpl.getInstance().generateSLETransaction(userBean.getMerchantId(), "SALE_REFUND", con);
			cancelBean.setCancelTxnId(trxId);
			isSuccess = new CancelTicketDaoImpl().cancelTransactionDaoAPI(userBean, cancelBean, con);
			if(isSuccess){
				userTxn.commit();
			}else{
				throw new SLEException(SLEErrors.FAILED_TRANSACTION_ERROR_CODE, SLEErrors.FAILED_TRANSACTION_ERROR_MESSAGE);
			}
			
		} catch(SLEException sl){
			DBConnect.rollBackUserTransaction(userTxn);
			throw sl;
		} 
		catch (Exception e){
			e.printStackTrace();
			DBConnect.rollBackUserTransaction(userTxn);
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(con);
		}
		return isSuccess;	
	}

}
