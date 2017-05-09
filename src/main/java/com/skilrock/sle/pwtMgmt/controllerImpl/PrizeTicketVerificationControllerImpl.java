package com.skilrock.sle.pwtMgmt.controllerImpl;

import java.sql.Connection;

import com.skilrock.sle.common.DBConnect;
import com.skilrock.sle.common.SLELogger;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.common.javaBeans.UserInfoBean;
import com.skilrock.sle.merchant.lms.LMSIntegrationImpl;
import com.skilrock.sle.pwtMgmt.daoImpl.PrizeTicketVerificationDaoImpl;
import com.skilrock.sle.pwtMgmt.javaBeans.PwtVerifyTicketBean;

public class PrizeTicketVerificationControllerImpl {
	private static final SLELogger logger = SLELogger.getLogger(PrizeTicketVerificationControllerImpl.class.getName());
	

	public PwtVerifyTicketBean prizeWinningVerifyTicket(UserInfoBean userBean, String merchantName, String ticketNumber) throws SLEException {
		Connection connection = null;
		PwtVerifyTicketBean verifyTicketBean = null;
		try {
			connection = DBConnect.getConnection();
			verifyTicketBean = PrizeTicketVerificationDaoImpl.getSingleInstance().prizeWinningVerifyTicket(userBean, merchantName, ticketNumber, connection);
		} catch (SLEException se) {
			throw se;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(connection);
		}

		return verifyTicketBean;
	}

	public PwtVerifyTicketBean verifyTicketForAPI(String merchantName, String ticketNumber) throws SLEException {
		logger.info("-- verifyTicketForAPI (Controller) --");

		Connection connection = null;
		PwtVerifyTicketBean verifyTicketBean = null;
		try {
			connection = DBConnect.getConnection();
			verifyTicketBean = PrizeTicketVerificationDaoImpl.getSingleInstance().verifyTicketForAPI(merchantName, ticketNumber, connection);
		} catch (SLEException se) {
			throw se;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(connection);
		}

		return verifyTicketBean;
	}

	public String checkRetailerClaimStatus(String userName, String userSessionId, double winningAmount) throws SLEException {
		//boolean claimStatus = false;
		String statusMsg = null;
		try {
			statusMsg = LMSIntegrationImpl.checkRetailerClaimStatus(userName, userSessionId, winningAmount);
		} catch(SLEException se){
			if(se.getErrorCode() == 118){
				throw new SLEException(SLEErrors.INVALID_SESSION_ERROR_CODE, SLEErrors.INVALID_SESSION_ERROR_MESSAGE);
			}else{
				throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			}
		}catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return statusMsg;
	}
	
	public double getPwtTaxDetails(int gameId,int gameTypeId,String ticketNbr,double winningAmount) throws SLEException {
		//boolean claimStatus = false;
		double govTaxOnPwt = 0.0;
		try {
			govTaxOnPwt = LMSIntegrationImpl.getPwtTaxDetails(gameId, gameTypeId, ticketNbr, winningAmount);
		} catch(SLEException se){
			if(se.getErrorCode() == 118){
				throw new SLEException(SLEErrors.INVALID_SESSION_ERROR_CODE, SLEErrors.INVALID_SESSION_ERROR_MESSAGE);
			}else{
				throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			}
		}catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return govTaxOnPwt;
	}
}