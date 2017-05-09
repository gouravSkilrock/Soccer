package com.skilrock.sle.userMgmt.controllerImpl;

import java.sql.Connection;

import javax.transaction.UserTransaction;

import com.skilrock.sle.common.DBConnect;
import com.skilrock.sle.common.SLELogger;
import com.skilrock.sle.common.TransactionManager;
import com.skilrock.sle.common.exception.GenericException;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.commonMethod.daoImpl.CommonMethodsDaoImpl;
import com.skilrock.sle.tp.rest.common.javaBeans.TpUserRegistrationBean;

public class UserMgmtControllerImpl {
	private static final SLELogger logger = SLELogger.getLogger(UserMgmtControllerImpl.class.getName());

	public void registerMerchantUser(TpUserRegistrationBean userRegistrationRequestBean) throws GenericException {
		logger.debug("***** Inside registerUser Method");
		Connection conn = null;
		UserTransaction userTxn = null;
		try {
			conn = DBConnect.getConnection();
			userTxn = DBConnect.startTransaction();
			userRegistrationRequestBean.setMerchantId(TransactionManager.getMerchantId());
			CommonMethodsDaoImpl.getInstance().registerMerchantUser(userRegistrationRequestBean, conn);
			userTxn.commit();
		} catch (GenericException e) {
			DBConnect.rollBackUserTransaction(userTxn);
			throw e;
		} catch (Exception e) {
			DBConnect.rollBackUserTransaction(userTxn);
			e.printStackTrace();
			throw new GenericException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE);
		} finally {
			DBConnect.closeConnection(conn);
		}
	}
	
	public void userLoginNotify(TpUserRegistrationBean tpUserRegistrationRequestBean) throws GenericException
	{
		logger.debug("***** Inside Login Notify Method");
		Connection conn = null;
		boolean isUserExists = false;
		UserTransaction userTxn = null;
		CommonMethodsDaoImpl daoImpl = null;
		try {
			conn = DBConnect.getConnection();
			userTxn = DBConnect.startTransaction();

			isUserExists = CommonMethodsDaoImpl.getInstance().checkUserExistence(tpUserRegistrationRequestBean.getUserId(), tpUserRegistrationRequestBean.getMerchantId(), conn);
			daoImpl = CommonMethodsDaoImpl.getInstance();
			if(isUserExists) {
				daoImpl.updateMerchantUserData(tpUserRegistrationRequestBean, conn);
			} else {
				daoImpl.registerMerchantUser(tpUserRegistrationRequestBean, conn);
			}
			userTxn.commit();
//			daoImpl = new UserMgmtDaoImpl();
//			if(isUserExists == false) {
//				userInfoBean = new UserInfoBean();
//				reqObj = new JsonObject();
//				reqObj.addProperty("userName", userInfoBean.getUserName());
////				respString = TpIntegrationImpl.getLMSResponseString(ServiceName.USER_MGMT, ServiceMethodName.FETCH_USER_SESSION, reqObj);
//				respString = "{\"firstName\":\"Dushyant\", \"lastName\":\"sapra\", \"gender\":\"MALE\", \"address\":\"Gurgaon\", \"mobileNbr\":\"9999999999\", \"emailId\":\"abc@abc.com\", \"userName\":\"sapra\", \"userId\":\"12345\", \"userType\":\"PLAYER\", \"city\":\"gurgaon\", \"state\":\"haryana\", \"country\":\"India\"}";
//				respObj = new JsonParser().parse(respString).getAsJsonObject();
//				
//				tpUserRegistrationBean = new Gson().fromJson(respObj, TpUserRegistrationBean.class);
//				
//				tpUserRegistrationBean.setMerchantId(userLoginNotifyBean.getMerchantId());
//				
//				CommonMethodsDaoImpl.getInstance().registerMerchantUser(tpUserRegistrationBean, conn);
//			} else {
//				daoImpl.userLoginNotify(userLoginNotifyBean, conn);
//			}
//			userTxn.commit();
		} catch (GenericException e) {
			DBConnect.rollBackUserTransaction(userTxn);
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			DBConnect.rollBackUserTransaction(userTxn);
			throw new GenericException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE);
		} finally {
			DBConnect.closeConnection(conn);
		}
	}
	
	public void userLogoutNotify(TpUserRegistrationBean tpUserRegistrationRequestBean) throws GenericException
	{
		logger.debug("***** Inside Logout Notify Method");
		Connection conn = null;
		boolean isUserExists = false;
		UserTransaction userTxn = null;
		CommonMethodsDaoImpl daoImpl = null;
		try {
			conn = DBConnect.getConnection();
			userTxn = DBConnect.startTransaction();

			isUserExists = CommonMethodsDaoImpl.getInstance().checkUserExistence(tpUserRegistrationRequestBean.getUserId(), tpUserRegistrationRequestBean.getMerchantId(), conn);
			daoImpl = CommonMethodsDaoImpl.getInstance();
			if(isUserExists) {
				daoImpl.updateMerchantUserLogout(tpUserRegistrationRequestBean, conn);
			}
			userTxn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			DBConnect.rollBackUserTransaction(userTxn);
			throw new GenericException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE);
		} finally {
			DBConnect.closeConnection(conn);
		}
	}
	
	public void serverDownNotify(int merchantId, String sessionId) throws GenericException
	{
		logger.debug("***** Inside ServerDown Notify Method");
		Connection conn = null;
		UserTransaction userTxn = null;
		CommonMethodsDaoImpl daoImpl = null;
		try {
			conn = DBConnect.getConnection();
			userTxn = DBConnect.startTransaction();
			daoImpl = CommonMethodsDaoImpl.getInstance();
			daoImpl.updateMerchantServerDown(merchantId, sessionId, conn);
			userTxn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			DBConnect.rollBackUserTransaction(userTxn);
			throw new GenericException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE);
		} finally {
			DBConnect.closeConnection(conn);
		}
	}
}
