package com.skilrock.sle.merchant.lms;

import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.skilrock.sle.common.SLELogger;
import com.skilrock.sle.common.exception.GenericException;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.common.javaBeans.UserInfoBean;
import com.skilrock.sle.common.javaBeans.ValidateTicketBean;
import com.skilrock.sle.dataMgmt.javaBeans.ReconciliationBean;
import com.skilrock.sle.embedded.playMgmt.common.javaBeans.CancelTicketBean;
import com.skilrock.sle.gamePlayMgmt.javaBeans.ReprintTicketBean;
import com.skilrock.sle.gamePlayMgmt.javaBeans.SportsLotteryGamePlayBean;
import com.skilrock.sle.merchant.TpIntegrationImpl;
import com.skilrock.sle.merchant.common.javaBeans.TPPwtRequestBean;
import com.skilrock.sle.merchant.common.javaBeans.TPPwtResponseBean;
import com.skilrock.sle.merchant.common.javaBeans.TPRequestBean;
import com.skilrock.sle.merchant.lms.common.javaBeans.LMSCancelTicketResponseBean;
import com.skilrock.sle.merchant.lms.common.javaBeans.LMSGamePlayResponseBean;
import com.skilrock.sle.tp.rest.common.javaBeans.TpUserRegistrationBean;

public class LMSIntegrationImpl {
	private static final SLELogger logger = SLELogger.getLogger(LMSIntegrationImpl.class.getName());

	public static LMSGamePlayResponseBean purchaseTicketAtLMS(SportsLotteryGamePlayBean gamePlayBean, UserInfoBean userInfoBean) throws GenericException{
		LMSGamePlayResponseBean gameResponseBean = null;
		JsonObject reqObj = null;

		String respString = null;
		JsonObject respObj = null;

//		logger.debug("***** Inside purchaseTicket Method");

		try {
			reqObj = new JsonObject();
			reqObj.addProperty("userName", userInfoBean.getUserName());
			reqObj.addProperty("sessionId", userInfoBean.getUserSessionId());
			reqObj.addProperty("txType", "WAGER");
			reqObj.addProperty("gameId", gamePlayBean.getGameId());
			reqObj.addProperty("ticketNumber", gamePlayBean.getTicketNumber());
			reqObj.addProperty("plrMobileNumber", gamePlayBean.getPlrMobileNumber());
			reqObj.addProperty("engineTxId", gamePlayBean.getTransId());
			reqObj.addProperty("userMappingId", userInfoBean.getMerchantUserMappingId());
			reqObj.addProperty("txAmount", gamePlayBean.getTotalPurchaseAmt());
			reqObj.addProperty("gameTypeId", gamePlayBean.getGameTypeId());
			reqObj.addProperty("interfaceType", gamePlayBean.getInterfaceType());
			reqObj.addProperty("tokenId", userInfoBean.getTokenId());
			reqObj.addProperty("serviceCode", "SLE");

//			logger.debug("Req Obj For LMS Sale "+reqObj);

			respString = TpIntegrationImpl.getLMSResponseString(ServiceName.ACCOUNT_MGMT, ServiceMethodName.MANAGE_ACCOUNTS, reqObj);
//			respString = "{\"responseCode\":0,\"responseData\":{\"gameId\":1,\"gameTypeId\":1,\"userMappingId\":50486,\"merTxId\":33584113,\"txAmount\":0.0,\"availBal\":10016.1,\"txType\":\"WAGER\",\"ticketNumber\":\"26306233050486036\",\"responseMessage\":\"SUCCESS\"}}";
			respObj = new JsonParser().parse(respString).getAsJsonObject();
			
			if(respObj.get("responseCode").getAsInt() == 0) {
				gameResponseBean = new Gson().fromJson(respObj.get("responseData").getAsJsonObject(), LMSGamePlayResponseBean.class);
				gameResponseBean.setResponseCode(respObj.get("responseCode").getAsInt());
				//gamePlayBean.setAdvMessageMap(gameResponseBean.getAdvMessageMap());
			} else {
				if(respObj.get("responseData")!=null){
					gameResponseBean = new Gson().fromJson(respObj.get("responseData").getAsJsonObject(), LMSGamePlayResponseBean.class);
					gameResponseBean.setResponseCode(respObj.get("responseCode").getAsInt());
					
				}else{
					gameResponseBean=new LMSGamePlayResponseBean();
					gameResponseBean.setResponseCode(respObj.get("responseCode").getAsInt());
					gameResponseBean.setResponseMessage(respObj.get("responseMessage").getAsString());
				}
				
				
			}
			
//			logger.debug("Response Obj For LMS Sale "+gameResponseBean);
		} catch (Exception e) {
			throw new GenericException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return gameResponseBean;
	}

	public static LMSCancelTicketResponseBean cancelTicketAtLMS(CancelTicketBean cancelTicketBean, UserInfoBean userInfoBean) throws GenericException{
		LMSCancelTicketResponseBean cancelResponseBean = null;
		JsonObject reqObj = null;

		String respString = null;
		JsonObject respObj = null;

//		logger.debug("***** Inside purchaseTicket Method");
		
		try {
			reqObj = new JsonObject();
			reqObj.addProperty("userName", userInfoBean.getUserName());
			reqObj.addProperty("sessionId", userInfoBean.getUserSessionId());
			if("Y".equals(cancelTicketBean.getIsAutoCancel()))
				reqObj.addProperty("txType", "AUTO_WAGER_REFUND");
			else if("WEB".equals(cancelTicketBean.getInterfaceType()))
				reqObj.addProperty("txType", "WAGER_REFUND_BO");
			else
				reqObj.addProperty("txType", "WAGER_REFUND");
			reqObj.addProperty("gameId", cancelTicketBean.getGameId());
			reqObj.addProperty("ticketNumber", String.valueOf(cancelTicketBean.getTktToCancel()) + cancelTicketBean.getReprintCount());
			reqObj.addProperty("engineTxId", cancelTicketBean.getCancelTxnId());
			reqObj.addProperty("engineSaleTxId", cancelTicketBean.getSaleTxnId());
			reqObj.addProperty("userMappingId", userInfoBean.getMerchantUserMappingId());
			reqObj.addProperty("txAmount", cancelTicketBean.getCancelAmount());
			reqObj.addProperty("gameTypeId", cancelTicketBean.getGameTypeId());
			reqObj.addProperty("interfaceType", cancelTicketBean.getInterfaceType());
			reqObj.addProperty("serviceCode", "SLE");

			logger.info("Req Obj For LMS Sale "+reqObj);
			
			respString = TpIntegrationImpl.getLMSResponseString(ServiceName.ACCOUNT_MGMT, ServiceMethodName.MANAGE_ACCOUNTS, reqObj);
//			respString = "{\"responseCode\":0,\"responseData\":{\"gameId\":1,\"gameTypeId\":1,\"userMappingId\":50486,\"merTxId\":33583899,\"txAmount\":10.0,\"availBal\":10038.3,\"txType\":\"WAGER_REFUND\",\"ticketNumber\":\"110032362523212560\",\"responseMessage\":\"SUCCESS\"}}";
			respObj = new JsonParser().parse(respString).getAsJsonObject();
			
			if(respObj.get("responseCode").getAsInt() == 0) {
				cancelResponseBean = new Gson().fromJson(respObj.get("responseData").getAsJsonObject(), LMSCancelTicketResponseBean.class);
				cancelResponseBean.setResponseCode(respObj.get("responseCode").getAsInt());
			} else {
				cancelResponseBean = new LMSCancelTicketResponseBean();
				cancelResponseBean.setResponseCode(respObj.get("responseCode").getAsInt());
				if (respObj.get("responseData")!=null) {
					cancelResponseBean.setResponseMessage(respObj.get("responseData").getAsJsonObject().get("responseMessage").getAsString());
				}
			}

			logger.info("Response Obj For LMS Refund "+cancelResponseBean);
		} catch (GenericException e) {
			throw e;
		}catch (Exception e) {
			throw new GenericException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return cancelResponseBean;
	}
	
	public static TpUserRegistrationBean fetchUserData(String userName) throws SLEException{
		TpUserRegistrationBean regBean = null;
		JsonObject reqObj = null;

		String respString = null;
		JsonObject respObj = null;

//		logger.debug("***** Inside purchaseTicket Method");

		try {
			reqObj = new JsonObject();
			reqObj.addProperty("userName", userName);

//			logger.debug("Req Obj For LMS User Fetch Data "+reqObj);

			respString = TpIntegrationImpl.getLMSResponseString(ServiceName.USER_MGMT, ServiceMethodName.FETCH_USER_INFO, reqObj);
//			respString = "{\"responseCode\":0,\"responseData\":{\"gameId\":1,\"gameTypeId\":1,\"userMappingId\":50486,\"merTxId\":33583898,\"txAmount\":10.0,\"availBal\":10028.5,\"txType\":\"WAGER\",\"ticketNumber\":\"110032362523212560\",\"responseMessage\":\"SUCCESS\"}}";
			respObj = new JsonParser().parse(respString).getAsJsonObject();
			
			if(respObj.get("responseCode").getAsInt() == 0) {
				regBean = new Gson().fromJson(respObj.get("responseData").getAsJsonObject(), TpUserRegistrationBean.class);
			} 
			
//			logger.debug("Response Obj For LMS Sale "+regBean);
		} /*catch (GenericException e) {
			throw e;
		}*/ catch (Exception e) {
				throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return regBean;
	}
	
	public static LMSGamePlayResponseBean reprintTicketAtLMS(ReprintTicketBean tktBean, UserInfoBean userInfoBean) throws GenericException{
		LMSGamePlayResponseBean gameResponseBean = null;
		JsonObject reqObj = null;

		String respString = null;
		JsonObject respObj = null;

		logger.debug("***** Inside reprintTicket Method");

		try {
			reqObj = new JsonObject();
			reqObj.addProperty("userName", userInfoBean.getUserName());
			reqObj.addProperty("sessionId", userInfoBean.getUserSessionId());
			reqObj.addProperty("txType", "WAGER_REPRINT");
			reqObj.addProperty("gameId", tktBean.getGameId());
			reqObj.addProperty("ticketNumber", tktBean.getTicketNumber());
			reqObj.addProperty("userMappingId", userInfoBean.getMerchantUserMappingId());
			reqObj.addProperty("gameTypeId", tktBean.getGameTypeId());
			reqObj.addProperty("interfaceType", tktBean.getInterfaceType());
			reqObj.addProperty("serviceCode", "SLE");

			logger.info("Req Obj For LMS Reprint "+reqObj);

			respString = TpIntegrationImpl.getLMSResponseString(ServiceName.ACCOUNT_MGMT, ServiceMethodName.MANAGE_ACCOUNTS, reqObj);
			//respString = "{\"responseCode\":0,\"responseData\":{\"gameId\":1,\"gameTypeId\":1,\"userMappingId\":50486,\"merTxId\":33583898,\"txAmount\":10.0,\"availBal\":10028.5,\"txType\":\"WAGER\",\"ticketNumber\":\"110032362523212560\",\"responseMessage\":\"SUCCESS\"}}";
			respObj = new JsonParser().parse(respString).getAsJsonObject();
			
			if(respObj.get("responseCode").getAsInt() == 0) {
				gameResponseBean = new LMSGamePlayResponseBean();
				gameResponseBean.setResponseCode(respObj.get("responseCode").getAsInt());
				gameResponseBean.setAvailBal(respObj.get("responseData").getAsDouble());
			} else {
				gameResponseBean = new LMSGamePlayResponseBean();
				gameResponseBean.setResponseCode(respObj.get("responseCode").getAsInt());
			}
			
			//logger.info("Response Obj For LMS Sale "+gameResponseBean);
		} /*catch (GenericException e) {
			throw e;
		}*/ catch (Exception e) {
			throw new GenericException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return gameResponseBean;
	}

	public static String checkTicketPWTStatus(String userName, String userSessionId, double winningAmount) throws SLEException, GenericException {
		logger.info("-- checkTicketPWTStatus --");

		String pwtStatus = null;
		try {
			JsonObject requestObject = new JsonObject();
			requestObject.addProperty("userName", userName);
			requestObject.addProperty("userSession", userSessionId);
			requestObject.addProperty("serviceCode", "SLE");
			requestObject.addProperty("winningAmount", winningAmount);

			logger.info("Request String - "+requestObject);
			String responseString = TpIntegrationImpl.getLMSResponseString(ServiceName.ACCOUNT_MGMT, ServiceMethodName.CHECK_TICKET_PWT_STATUS, requestObject);
			logger.info("Response String - "+responseString);

			JsonObject responseObject = new JsonParser().parse(responseString).getAsJsonObject();
			int responseCode = responseObject.get("responseCode").getAsInt();
			if(responseCode == 0) {
				pwtStatus = responseObject.get("pwtStatus").getAsString();
			} else {
				throw new SLEException(responseCode);
			}
		} catch (SLEException se) {
			throw se;
		} catch (Exception e) {
			throw new GenericException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}

		return pwtStatus;
	}

	public static String checkRetailerClaimStatus(String userName, String userSessionId, double winningAmount) throws SLEException, GenericException {
		logger.info("-- Inside checkTicketPWTStatus method --");

		//boolean claimStatus = false;
		String statusMsg = null;
		try {
			JsonObject requestObject = new JsonObject();
			requestObject.addProperty("userName", userName);
			requestObject.addProperty("userSession", userSessionId);
			requestObject.addProperty("serviceCode", "SLE");
			requestObject.addProperty("winningAmount", winningAmount);

			logger.info("Request String - "+requestObject);
			String responseString = TpIntegrationImpl.getLMSResponseString(ServiceName.ACCOUNT_MGMT, ServiceMethodName.CHECK_RETAILER_CLAIM_STATUS, requestObject);
			logger.info("Response String - "+responseString);

			JsonObject responseObject = new JsonParser().parse(responseString).getAsJsonObject();
			int responseCode = responseObject.get("responseCode").getAsInt();
			if(responseCode == 0) {
				//claimStatus = responseObject.get("claimStatus").getAsBoolean();
				statusMsg = responseObject.get("statusMsg").getAsString();
			} else {
				throw new SLEException(responseCode);
			}
		} catch (SLEException se) {
			throw se;
		} catch (Exception e) {
			throw new GenericException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}

		return statusMsg;
	}

	public static TPPwtResponseBean winningSLETicket(TPPwtRequestBean pwtRequestBean, UserInfoBean userBean, String interfaceType) throws GenericException, SLEException {
		logger.info("--Inside winningSLETicket method --");
		JsonObject respObj = null;

		TPPwtResponseBean pwtResponseBean = null;
		TPRequestBean requestBean = new TPRequestBean();
		requestBean.setUserName(userBean.getUserName());
		requestBean.setUserSession(userBean.getUserSessionId());
		requestBean.setRequestData(pwtRequestBean);
		requestBean.setServiceCode("SLE");
		requestBean.setInterfaceType(interfaceType);

		JsonObject requestObject = new JsonParser().parse(new Gson().toJson(requestBean)).getAsJsonObject();

		logger.info("Request String - "+requestObject);
		String responseString = TpIntegrationImpl.getLMSResponseString(ServiceName.ACCOUNT_MGMT, ServiceMethodName.MANAGE_WINNING, requestObject);
		logger.info("Response String - "+responseString);
		if(responseString==null || responseString.isEmpty()){
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}

		respObj = new JsonParser().parse(responseString).getAsJsonObject();
		
		if(respObj.get("responseCode").getAsInt() != 0) {
			throw new SLEException(respObj.get("responseCode").getAsInt(), respObj.get("responseMessage").getAsString());
		}
		//pwtResponseBean = (TPPwtResponseBean) responseBean.getResponseData();
		pwtResponseBean = new Gson().fromJson(respObj.get("responseData").getAsJsonObject(), TPPwtResponseBean.class);

		return pwtResponseBean;
	}

public static LMSGamePlayResponseBean updateRGAtLMS(UserInfoBean userInfoBean, ValidateTicketBean validateTktBean) throws GenericException{
	LMSGamePlayResponseBean gameResponseBean = null;
	JsonObject reqObj = null;
	String respString = null;
	JsonObject respObj = null;

	logger.debug("***** Inside reprintTicket Method");

	try {
		reqObj = new JsonObject();
		reqObj.addProperty("userName", userInfoBean.getUserName());
		reqObj.addProperty("sessionId", userInfoBean.getUserSessionId());
		reqObj.addProperty("txType", "SLE_INVALID_PWT");
		reqObj.addProperty("gameId", validateTktBean.getGameid());
		reqObj.addProperty("ticketNumber", validateTktBean.getTicketNo());
		reqObj.addProperty("userMappingId", userInfoBean.getMerchantUserMappingId());
		reqObj.addProperty("gameTypeId", validateTktBean.getGameTypeId());
		reqObj.addProperty("interfaceType", "TERMINAL");
		reqObj.addProperty("serviceCode", "SLE");

		logger.debug("Req Obj For LMS Reprint "+reqObj);

		respString=TpIntegrationImpl.getLMSResponseString(ServiceName.ACCOUNT_MGMT, ServiceMethodName.MANAGE_ACCOUNTS, reqObj);
		//respString = "{\"responseCode\":0,\"responseData\":{\"gameId\":1,\"gameTypeId\":1,\"userMappingId\":50486,\"merTxId\":33583898,\"txAmount\":10.0,\"availBal\":10028.5,\"txType\":\"WAGER\",\"ticketNumber\":\"110032362523212560\",\"responseMessage\":\"SUCCESS\"}}";
		respObj = new JsonParser().parse(respString).getAsJsonObject();
		gameResponseBean = new LMSGamePlayResponseBean();
		gameResponseBean.setResponseCode(respObj.get("responseCode").getAsInt());
		
		if(respObj.get("responseCode").getAsInt() == 118){
			throw new SLEException(SLEErrors.INVALID_SESSION_ERROR_CODE, SLEErrors.INVALID_SESSION_ERROR_MESSAGE);
		}

		logger.debug("Response Obj For LMS Sale "+gameResponseBean);
	} /*catch (GenericException e) {
		throw e;
	}*/ catch (Exception e) {
		throw new GenericException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
	}
	return gameResponseBean;
}

	public static int fetchMerchantUserBalance(UserInfoBean userInfoBean) throws GenericException {
		JsonObject reqObj = null;
		String respString = null;
		JsonObject respObj = null;
		
		int responseCode = 0;
		try {
			reqObj = new JsonObject();
			reqObj.addProperty("userId", userInfoBean.getMerchantUserId());
	
			logger.debug("Req Obj For fetchMerchantUserBalance {}"+ reqObj);
	
			respString = TpIntegrationImpl.getLMSResponseString(ServiceName.ACCOUNT_MGMT, ServiceMethodName.FETCH_MERCHANT_USER_BALANCE, reqObj);
	//		respString = "{\"responseCode\":0,\"responseData\":{\"gameId\":1,\"gameTypeId\":1,\"userMappingId\":50486,\"merTxId\":33584113,\"txAmount\":1.0,\"availBal\":10016.1,\"txType\":\"WAGER\",\"ticketNumber\":\"26306233050486036\",\"responseMessage\":\"SUCCESS\"}}";
			respObj = new JsonParser().parse(respString).getAsJsonObject();
			
			if(respObj.get("responseCode").getAsInt() == 0) {
				userInfoBean.setBalance(respObj.get("balance").getAsDouble());
			} else {
				responseCode = respObj.get("responseCode").getAsInt();
			}
		} catch (Exception e) {
			throw new GenericException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		
		return responseCode;
	}

	public static Map<String, List<ReconciliationBean>> reconcileTxns(Map<String, List<ReconciliationBean>> reconciliationRequestMap) throws GenericException {
		Map<String, List<ReconciliationBean>> reconciliationResponseMap = null;
		JsonObject reqObj = null;
		String respString = null;
		JsonObject respObj = null;
		logger.debug("***** Inside reconcileTxns Method");
		try {
			reqObj = new JsonObject();
			reqObj.addProperty("requestData", new Gson().toJson(reconciliationRequestMap));
			reqObj.addProperty("serviceCode", "SLE");
			logger.debug("Req Obj For LMS reconcileTxns {}" + reqObj);
			respString = TpIntegrationImpl.getLMSResponseString(ServiceName.DATA_MGMT, ServiceMethodName.RECONCILE_SLE_TRANSACTIONS, reqObj);

			respObj = new JsonParser().parse(respString).getAsJsonObject();

			if (respObj.get("responseCode").getAsInt() == 0) {
				reconciliationResponseMap = new Gson().fromJson(respObj.get("reconcile").getAsString(), new TypeToken<Map<String, List<ReconciliationBean>>>() {}.getType());
			}
			logger.debug("Response Obj For LMS reconcileTxns {}" + reconciliationResponseMap);
		} catch (Exception e) {
			throw new GenericException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return reconciliationResponseMap;
	}
	
	//for FPFCC
	
	public static double getPwtTaxDetails(int gameId,int gameTypeId,String ticketNbr,double winningAmount) throws SLEException, GenericException {
		logger.info("-- checkTicketPWTStatus --");

		//boolean claimStatus = false;
		double govTaxOnPwt = 0.0;
		try {
			JsonObject requestObject = new JsonObject();
			requestObject.addProperty("gameId", gameId);
			requestObject.addProperty("gameTypeId", gameTypeId);
			requestObject.addProperty("ticketNbr", ticketNbr);
			requestObject.addProperty("serviceCode", "SLE");
			requestObject.addProperty("winningAmount", winningAmount);

			logger.info("Request String - "+requestObject);
			String responseString = TpIntegrationImpl.getLMSResponseString(ServiceName.ACCOUNT_MGMT, ServiceMethodName.GET_TAX_DETALS, requestObject);
			logger.info("Response String - "+responseString);

			JsonObject responseObject = new JsonParser().parse(responseString).getAsJsonObject();
			int responseCode = responseObject.get("responseCode").getAsInt();
			if(responseCode == 0) {
				govTaxOnPwt = responseObject.get("govTaxOnPwt").getAsDouble();
			} else {
				throw new SLEException(responseCode);
			}
		} catch (SLEException se) {
			throw se;
		} catch (Exception e) {
			throw new GenericException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}

		return govTaxOnPwt;
	}	
	
}
