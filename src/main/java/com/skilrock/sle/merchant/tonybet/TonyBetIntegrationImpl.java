package com.skilrock.sle.merchant.tonybet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.skilrock.sle.common.SportsLotteryUtils;
import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.UtilityFunctions;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.common.javaBeans.UserInfoBean;
import com.skilrock.sle.gamePlayMgmt.javaBeans.SportsLotteryGamePlayBean;
import com.skilrock.sle.merchant.TpIntegrationImpl;
import com.skilrock.sle.merchant.tonybet.TonyBetUtils.ServiceMethod;
import com.skilrock.sle.tp.rest.common.javaBeans.TpUserRegistrationBean;

public class TonyBetIntegrationImpl {
	private static final int TONY_BET_INVALID_CREDENTIAL_ERROR_CODE = 2;
	private static final int TONY_BET_SUCCESS_CODE = 1;
	private static final Logger logger = LoggerFactory.getLogger(TonyBetIntegrationImpl.class);
			
	public static TpUserRegistrationBean fetchUserData(UserInfoBean userInfoBean) throws SLEException {
		logger.info("*****Inside Fetch User Data Method*****");
		TpUserRegistrationBean registrationBean = new TpUserRegistrationBean();
		JsonObject requestObject = null;
		JsonObject responseObject = null;
		String responseString = null;
		try {
			requestObject = new JsonObject();
			requestObject.addProperty("CallerId", TonyBetUtils.CALLER_ID);
			requestObject.addProperty("CallerPassword", TonyBetUtils.CALLER_PASSWORD);
			requestObject.addProperty("PlayerId",userInfoBean.getMerchantUserId());
			requestObject.addProperty("TokenId", userInfoBean.getUserSessionId());
			logger.info("Request Object For TonyBet User Fetch Data {}", requestObject);
			responseString = TpIntegrationImpl.getTonyBetResponse(TonyBetUtils.BASE_SERVICE_API, ServiceMethod.account_details, requestObject);
			responseObject = new JsonParser().parse(responseString).getAsJsonObject();
			logger.info("Response Object For TonyBet User Fetch Data {}", responseObject);
			if (responseObject.get("ResultCode").getAsInt() == TONY_BET_SUCCESS_CODE) {
				registrationBean.setSessionId(userInfoBean.getUserSessionId());
				if (!responseObject.get("Currency").isJsonNull()) {
					registrationBean.setCurrency(responseObject.get("Currency").getAsString());
				}else {
					registrationBean.setCurrency("");
				}
				if (!responseObject.get("Country").isJsonNull()) {
					registrationBean.setCountry(responseObject.get("Country").getAsString());
				}else {
					registrationBean.setCountry("");
				}
				if (!responseObject.get("City").isJsonNull()) {
					registrationBean.setCity(responseObject.get("City").getAsString());
				}else {
					registrationBean.setCity("");
				}
				registrationBean.setUserName(responseObject.get("LoginName").getAsString());
				registrationBean.setUserType("PLAYER");
			}else {
				if (responseObject.get("ResultCode").getAsInt() == TONY_BET_INVALID_CREDENTIAL_ERROR_CODE) {
					throw new SLEException(SLEErrors.INVALID_CREDENTIALS_ERROR_CODE, SLEErrors.INVALID_CREDENTIALS_ERROR_MESSAGE);
				}
				throw new SLEException(responseObject.get("ResultCode").getAsInt());
			}
		}catch (SLEException e) {
			throw e;
		}catch (Exception e) {
			logger.error("Error in TonyBet Fetch User Data", e);
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return registrationBean;
	}

	public static TonyBetGamePlayResponseBean purchaseTicketAtTonyBet(SportsLotteryGamePlayBean gamePlayBean,
			UserInfoBean userInfoBean) throws SLEException {
		logger.info("*****Inside Purchase ticket at TonyBet Method*****");
		JsonObject requestObject = null;
		String responseString = null;
		JsonObject responseObject = null;
		TonyBetGamePlayResponseBean tonyBetResponseBean = null;
		try {
			requestObject = new JsonObject();
			requestObject.addProperty("CallerId", TonyBetUtils.CALLER_ID);
			requestObject.addProperty("CallerPassword", TonyBetUtils.CALLER_PASSWORD);
			requestObject.addProperty("PlayerId",userInfoBean.getMerchantUserId());
			requestObject.addProperty("TokenId", "");
			requestObject.addProperty("Amount",gamePlayBean.getTotalPurchaseAmt());
			requestObject.addProperty("Currency", TonyBetUtils.CURRENCY);
			requestObject.addProperty("TransactionRef", gamePlayBean.getTransId());
			requestObject.addProperty("GameRoundRef", gamePlayBean.getGameDevName());
			requestObject.addProperty("GameRef",gamePlayBean.getGameDevName()+"_"+gamePlayBean.getGameId());
			logger.info("Req Object For TonyBet Sale "+requestObject);
			responseString = TpIntegrationImpl.getTonyBetResponse(TonyBetUtils.BASE_SERVICE_API, ServiceMethod.withdraw, requestObject);
			responseObject = new JsonParser().parse(responseString).getAsJsonObject();
			tonyBetResponseBean = new Gson().fromJson(responseObject, TonyBetGamePlayResponseBean.class);
			logger.info("Response Obj For TonyBet Sale {}", tonyBetResponseBean);
		}catch (Exception e) {
			logger.error("Error in TonyBet Sale", e);
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return tonyBetResponseBean;
	}

	public static TonyBetGamePlayResponseBean cancelTransaction(SportsLotteryGamePlayBean gamePlayBean,
			UserInfoBean userInfoBean) throws SLEException {
		logger.info("*****Inside Cancel Transaction at TonyBet Method*****");
		JsonObject requestObject = null;
		String responseString = null;
		JsonObject responseObject = null;
		TonyBetGamePlayResponseBean tonyBetResponseBean = null;
		try {
			requestObject = new JsonObject();
			requestObject.addProperty("CallerId", TonyBetUtils.CALLER_ID);
			requestObject.addProperty("CallerPassword", TonyBetUtils.CALLER_PASSWORD);
			requestObject.addProperty("PlayerId",userInfoBean.getMerchantUserId());
			requestObject.addProperty("TokenId", "");
			requestObject.addProperty("TransactionRef", gamePlayBean.getTransId());
			requestObject.addProperty("GameRef","Soccer_"+gamePlayBean.getGameId());
			logger.info("Request Object For TonyBet Sale {}",requestObject);
			responseString = TpIntegrationImpl.getTonyBetResponse(TonyBetUtils.BASE_SERVICE_API, ServiceMethod.rollback, requestObject);
			responseObject = new JsonParser().parse(responseString).getAsJsonObject();
			tonyBetResponseBean = new Gson().fromJson(responseObject, TonyBetGamePlayResponseBean.class);
			logger.info("Response Obj For TonyBet cancel {}", tonyBetResponseBean);
		}catch (Exception e) {
			logger.error("Error in TonyBet cancel", e);
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return tonyBetResponseBean;
	}

	public static TonyBetGamePlayResponseBean depositTranaction(SportsLotteryGamePlayBean gamePlayBean,
			UserInfoBean userInfoBean) throws SLEException {
		TonyBetGamePlayResponseBean  tonyBetResponseBean =null;
		JsonObject requestObject = null;
		String responseString = null;
		JsonObject responseObject = null;
		try{
			requestObject = new JsonObject();
			requestObject.addProperty("CallerId", TonyBetUtils.CALLER_ID);
			requestObject.addProperty("CallerPassword", TonyBetUtils.CALLER_PASSWORD);
			requestObject.addProperty("PlayerId",userInfoBean.getMerchantUserId());
			requestObject.addProperty("TokenId", "");
			requestObject.addProperty("Currency", TonyBetUtils.CURRENCY);
			requestObject.addProperty("Amount", gamePlayBean.getWinAmt());
			requestObject.addProperty("TransactionRef", gamePlayBean.getTransId());
			requestObject.addProperty("GameRoundRef", gamePlayBean.getGameDevName());
			requestObject.addProperty("GameRef","Soccer_"+gamePlayBean.getGameId());
			logger.info("Request Object For TonyBet Sale {}",requestObject);
			responseString = TpIntegrationImpl.getTonyBetResponse(TonyBetUtils.BASE_SERVICE_API, ServiceMethod.deposit, requestObject);
			responseObject = new JsonParser().parse(responseString).getAsJsonObject();
			tonyBetResponseBean = new Gson().fromJson(responseObject, TonyBetGamePlayResponseBean.class);
			return tonyBetResponseBean;
		}catch (Exception e) {
			logger.error("Error in TonyBet deposit", e);
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		
		
		
	}

}
