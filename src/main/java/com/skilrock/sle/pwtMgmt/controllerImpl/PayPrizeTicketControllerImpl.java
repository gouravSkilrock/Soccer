package com.skilrock.sle.pwtMgmt.controllerImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import com.skilrock.sle.common.DBConnect;
import com.skilrock.sle.common.SLELogger;
import com.skilrock.sle.common.SportsLotteryUtils;
import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.common.javaBeans.TicketInfoBean;
import com.skilrock.sle.common.javaBeans.UserInfoBean;
import com.skilrock.sle.commonMethod.daoImpl.CommonMethodsDaoImpl;
import com.skilrock.sle.merchant.common.javaBeans.TPPwtRequestBean;
import com.skilrock.sle.merchant.common.javaBeans.TPPwtResponseBean;
import com.skilrock.sle.merchant.lms.LMSIntegrationImpl;
import com.skilrock.sle.pwtMgmt.daoImpl.PayPrizeTicketDaoImpl;
import com.skilrock.sle.pwtMgmt.javaBeans.PlayerBean;

public class PayPrizeTicketControllerImpl {
	private static final SLELogger logger = SLELogger.getLogger(PayPrizeTicketControllerImpl.class.getName());
	
	private static volatile PayPrizeTicketControllerImpl classInstance;

	public static PayPrizeTicketControllerImpl getInstance() {
		if (classInstance == null) {
			synchronized (PayPrizeTicketControllerImpl.class) {
				if (classInstance == null) {
					classInstance = new PayPrizeTicketControllerImpl();
				}
			}
		}
		return classInstance;
	}

	public String checkTicketPWTStatus(String userName, String userSessionId, double winningAmount) throws SLEException {
		String pwtStatus = null;
		try {
			pwtStatus = LMSIntegrationImpl.checkTicketPWTStatus(userName, userSessionId, winningAmount);
		} catch (SLEException se) {
			if(se.getErrorCode() == 118){
				throw new SLEException(SLEErrors.INVALID_SESSION_ERROR_CODE, SLEErrors.INVALID_SESSION_ERROR_MESSAGE);
			}
			throw se;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}

		return pwtStatus;
	}

	public TPPwtResponseBean normalPayWinning(String merchantName, String ticketNumber, String winningChannel, UserInfoBean userBean, String saleMerCode, String verificationCode) throws SLEException, SQLException {
		Connection connection = null;
		TPPwtResponseBean pwtResponseBean = null;
		TPPwtRequestBean pwtRequestBean=null;
		TicketInfoBean tktInfoBean = null;
		try {
			connection = DBConnect.getConnection();
			if("Asoft".equalsIgnoreCase(saleMerCode)){
				tktInfoBean = new TicketInfoBean();
				SportsLotteryUtils.fetchTicketInfo(tktInfoBean, ticketNumber, connection);
				if(!verificationCode.equalsIgnoreCase(tktInfoBean.getVerificationCode())){
					throw new SLEException(SLEErrors.INVALID_VERIFICATION_CODE_ERROR_CODE,SLEErrors.INVALID_VERIFICATION_CODE_ERROR_MESSAGE);
				}
			}
			connection.setAutoCommit(false);
			pwtRequestBean = PayPrizeTicketDaoImpl.getSingleInstance().claimWinningRequest(merchantName, ticketNumber, userBean.getSleUserId(), userBean.getMerchantUserId(), winningChannel, "NORMAL_PAY", connection);
			connection.commit();

			if(pwtRequestBean.getTotalAmount() > 0.0) {
				pwtResponseBean = LMSIntegrationImpl.winningSLETicket(pwtRequestBean, userBean, winningChannel);

				connection.setAutoCommit(false);
				PayPrizeTicketDaoImpl.getSingleInstance().updateWinningRequest(pwtResponseBean, null, "DONE", connection,pwtRequestBean.getDrawDataList().get(0).getTransactionId());
				connection.commit();
			}
		} catch (SLEException se) {
			if(pwtRequestBean!=null){
				if(se.getErrorCode()!=20001){
					connection.setAutoCommit(false);
					PayPrizeTicketDaoImpl.getSingleInstance().updateWinningStatus(pwtRequestBean.getTicketNumber(), "FAILED",pwtRequestBean.getDrawDataList().get(0).getTransactionId(), connection);
					PayPrizeTicketDaoImpl.getSingleInstance().updatePurTableStatus(pwtRequestBean.getGameId(), pwtRequestBean.getGameTypeId(), pwtRequestBean.getDrawDataList().get(0).getDrawId(), pwtRequestBean.getDrawDataList().get(0).getPurTbleName(),pwtRequestBean.getTicketNumber(),"UNCLAIMED", connection);
					connection.commit();	
				}
							
			}
			if(se.getErrorCode() == 118){
				throw new SLEException(SLEErrors.INVALID_SESSION_ERROR_CODE, SLEErrors.INVALID_SESSION_ERROR_MESSAGE);
			}
			throw se;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(connection);
		}
		
		return pwtResponseBean;
	}

	public String highPrizeOrMasApprovalWinning(String payType, String merchantName, String ticketNumber, String winningChannel, UserInfoBean userBean, PlayerBean playerBean, String remarks,String saleMerCode,String verificationCode) throws SLEException {
		Connection connection = null;
		String requestId = null;
		TPPwtRequestBean pwtRequestBean=null;
		TicketInfoBean tktInfoBean = null;
		try {
			connection = DBConnect.getConnection();
			connection.setAutoCommit(false);
			if("Asoft".equalsIgnoreCase(saleMerCode)){
				tktInfoBean=new TicketInfoBean();
				SportsLotteryUtils.fetchTicketInfo(tktInfoBean, ticketNumber, connection);
				if(!verificationCode.equalsIgnoreCase(tktInfoBean.getVerificationCode())){
					throw new SLEException(SLEErrors.INVALID_VERIFICATION_CODE_ERROR_CODE,SLEErrors.INVALID_VERIFICATION_CODE_ERROR_MESSAGE);
				}
			}
			pwtRequestBean = PayPrizeTicketDaoImpl.getSingleInstance().claimWinningRequest(merchantName, ticketNumber, userBean.getSleUserId(), userBean.getMerchantUserId(), winningChannel, payType, connection);
			pwtRequestBean.setPlayerBean(playerBean);
			pwtRequestBean.setRemarks(remarks);
			connection.commit();

			TPPwtResponseBean pwtResponseBean = LMSIntegrationImpl.winningSLETicket(pwtRequestBean, userBean, "WEB");
			requestId = pwtResponseBean.getRequestId();

			connection.setAutoCommit(false);
			PayPrizeTicketDaoImpl.getSingleInstance().updateWinningRequest(pwtResponseBean, requestId, "DONE", connection,pwtRequestBean.getDrawDataList().get(0).getTransactionId());
			connection.commit();
		} catch (SLEException se) {
			if(se.getErrorCode() == 118){
				throw new SLEException(SLEErrors.INVALID_SESSION_ERROR_CODE, SLEErrors.INVALID_SESSION_ERROR_MESSAGE);
			}
			throw se;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(connection);
		}

		return requestId;
	}

	public TPPwtRequestBean apiPayWinning(String merchantName, String ticketNumber, String refTransactionId) throws SLEException {
		logger.info("-- Inside apiPayWinning (Controller) --");
		Connection connection = null;
		TPPwtRequestBean pwtRequestBean=null;
		try {
			connection = DBConnect.getConnection();
			connection.setAutoCommit(false);

			UserInfoBean userBean = CommonMethodsDaoImpl.getInstance().getMerchantMasterUser(Util.merchantInfoMap.get(merchantName).getMerchantId(), connection);
			pwtRequestBean = PayPrizeTicketDaoImpl.getSingleInstance().claimWinningByAPI(merchantName, userBean, ticketNumber, refTransactionId,"NORMAL_PAY", connection);
			connection.commit();
		} catch (SLEException se) {
			throw se;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(connection);
		}

		return pwtRequestBean;
	}
	
	public void updateWinningRequest(long sleTransId, String merchantTxnId, String status) throws SLEException {
		Connection connection=null;
		try {
			PayPrizeTicketDaoImpl.getSingleInstance().updateWinningRequest(sleTransId, merchantTxnId, status, connection);
		} catch (SLEException ex) {
			throw ex;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}finally{
			DBConnect.closeConnection(connection);
		}
	}
}