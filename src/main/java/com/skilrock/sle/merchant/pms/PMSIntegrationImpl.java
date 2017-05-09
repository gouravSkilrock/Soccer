package com.skilrock.sle.merchant.pms;

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
import com.skilrock.sle.dataMgmt.javaBeans.ReconciliationBean;
import com.skilrock.sle.embedded.playMgmt.common.javaBeans.CancelTicketBean;
import com.skilrock.sle.gamePlayMgmt.controllerImpl.CancelTicketControllerImpl;
import com.skilrock.sle.gamePlayMgmt.javaBeans.SportsLotteryGamePlayBean;
import com.skilrock.sle.merchant.TpIntegrationImpl;
import com.skilrock.sle.merchant.pms.ServiceMethodName;
import com.skilrock.sle.merchant.pms.ServiceName;
import com.skilrock.sle.merchant.lms.common.javaBeans.LMSCancelTicketResponseBean;
import com.skilrock.sle.merchant.pms.common.javaBeans.PMSGamePlayResponseBean;
import com.skilrock.sle.tp.rest.common.javaBeans.TpUserRegistrationBean;

public class PMSIntegrationImpl {
	private static final SLELogger logger = SLELogger.getLogger(PMSIntegrationImpl.class.getName());

	public static PMSGamePlayResponseBean purchaseTicketAtPMS(SportsLotteryGamePlayBean gamePlayBean, UserInfoBean userInfoBean) throws GenericException{
		PMSGamePlayResponseBean gameResponseBean = null;
		JsonObject reqObj = null;
		String respString = null;
		JsonObject respObj = null;

		logger.debug("***** Inside purchaseTicket Method");
		try {
			reqObj = new JsonObject();
			reqObj.addProperty("userName", userInfoBean.getUserName());
			reqObj.addProperty("playerId", userInfoBean.getMerchantUserId());
			reqObj.addProperty("sessionId", userInfoBean.getUserSessionId());
			reqObj.addProperty("txType", "WAGER");
			reqObj.addProperty("gameId", gamePlayBean.getGameId());
			reqObj.addProperty("ticketNumber", gamePlayBean.getTicketNumber());
			reqObj.addProperty("engineTxId", gamePlayBean.getTransId());
			reqObj.addProperty("userMappingId", userInfoBean.getMerchantUserMappingId());
			reqObj.addProperty("txAmount", gamePlayBean.getTotalPurchaseAmt());
			reqObj.addProperty("gameTypeId", gamePlayBean.getGameTypeId());
			reqObj.addProperty("interfaceType", gamePlayBean.getInterfaceType());
			reqObj.addProperty("serviceCode", "SLE");

			logger.info("Req Obj For PMS Sale "+reqObj);
			
			respString = TpIntegrationImpl.getPMSResponseString(ServiceName.PLAY_MGMT, ServiceMethodName.SLE_PURCHASE_TICKET, reqObj);
			respObj = new JsonParser().parse(respString).getAsJsonObject();

			if(respObj.get("responseCode").getAsInt() == 0){
				gameResponseBean = new Gson().fromJson(respObj.get("responseData").getAsJsonObject(), PMSGamePlayResponseBean.class);
				gameResponseBean.setResponseCode(respObj.get("responseCode").getAsInt());
				gameResponseBean.setResponseMessage(respObj.get("responseMsg").getAsString());
			}else{
				gameResponseBean = new PMSGamePlayResponseBean();
				gameResponseBean.setResponseCode(respObj.get("responseCode").getAsInt());
				gameResponseBean.setResponseMessage(respObj.get("responseMsg").getAsString());
			}
			
			logger.info("Response Obj For PMS Sale "+gameResponseBean);
		}  catch (Exception e) {
			e.printStackTrace();
			throw new GenericException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return gameResponseBean;
	}
	
	public static TpUserRegistrationBean fetchUserData(UserInfoBean userInfo) throws SLEException{
		TpUserRegistrationBean regBean = null;
		JsonObject reqObj = null;

		String respString = null;
		JsonObject respObj = null;

		logger.debug("***** Inside fetch User Data Method");

		try {
			reqObj = new JsonObject();
			reqObj.addProperty("userName", userInfo.getUserName());
			reqObj.addProperty("userType", userInfo.getUserType());
			reqObj.addProperty("sessionId", userInfo.getUserSessionId());

			logger.debug("Req Obj For PMS User Fetch Data "+reqObj);

			respString = TpIntegrationImpl.getPMSResponseString(ServiceName.DATA_MGMT, ServiceMethodName.FETCH_USER_INFO, reqObj);
			respObj = new JsonParser().parse(respString).getAsJsonObject();
			
			if(respObj.get("responseCode").getAsInt() == 0) {
				regBean = new Gson().fromJson(respObj.get("responseData").getAsJsonObject(), TpUserRegistrationBean.class);
			} 
			if(respObj.get("responseCode").getAsInt() == 118) {
				throw new SLEException(SLEErrors.INVALID_SESSION_MOBILE_ERROR_CODE,SLEErrors.INVALID_SESSION_MOBILE_ERROR_MESSAGE);
			} 
			
			logger.debug("Response Obj For PMS Sale "+regBean);
		} catch(SLEException e){
			throw e;
		}catch (Exception e) {
				throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return regBean;
	}
	
	public static LMSCancelTicketResponseBean cancelTicketAtPMS(CancelTicketBean cancelTicketBean, UserInfoBean userInfoBean) throws GenericException{
		LMSCancelTicketResponseBean cancelResponseBean = null;
		JsonObject reqObj = null;

		String respString = null;
		JsonObject respObj = null;

		logger.info("***** Inside cancel ticket at pms Method");
		
		try {
			reqObj = new JsonObject();
			reqObj.addProperty("userName", userInfoBean.getUserName());
			reqObj.addProperty("sessionId", userInfoBean.getUserSessionId());
			if("Y".equals(cancelTicketBean.getIsAutoCancel()))
				reqObj.addProperty("txType", "AUTO_WAGER_REFUND");
			else
				if("WEB".equals(cancelTicketBean.getInterfaceType()))
					reqObj.addProperty("txType", "WAGER_REFUND_BO");
					else
					reqObj.addProperty("txType", "WAGER_REFUND");	
			reqObj.addProperty("gameId", cancelTicketBean.getGameId());
			reqObj.addProperty("ticketNumber", String.valueOf(cancelTicketBean.getTktToCancel()));
			reqObj.addProperty("engineTxId", cancelTicketBean.getCancelTxnId());
			reqObj.addProperty("userMappingId", userInfoBean.getMerchantUserMappingId());
			reqObj.addProperty("txAmount", cancelTicketBean.getCancelAmount());
			reqObj.addProperty("gameTypeId", cancelTicketBean.getGameTypeId());
			reqObj.addProperty("interfaceType", cancelTicketBean.getInterfaceType());
			reqObj.addProperty("tktMerchantUserId", cancelTicketBean.getTktMerchantUserId());
			reqObj.addProperty("engineSaleTxId", cancelTicketBean.getSaleTxnId());
			reqObj.addProperty("serviceCode", "SLE");

			logger.info("Req Obj For PMS Cancel "+reqObj);
			
			respString = TpIntegrationImpl.getPMSResponseString(ServiceName.PLAY_MGMT, ServiceMethodName.CANCEL_TICKET_AT_PMS, reqObj);
//			respString = "{\"responseCode\":0,\"responseData\":{\"gameId\":1,\"gameTypeId\":1,\"userMappingId\":50486,\"merTxId\":33583899,\"txAmount\":10.0,\"availBal\":10038.3,\"txType\":\"WAGER_REFUND\",\"ticketNumber\":\"110032362523212560\",\"responseMessage\":\"SUCCESS\"}}";
			if (respString==null) {
				new CancelTicketControllerImpl().updateSportsLotteryCancelTicket(false, "-1", cancelTicketBean.getSaleTxnId(), cancelTicketBean.getCancelTxnId());
				throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			}
			respObj = new JsonParser().parse(respString).getAsJsonObject();
			
			if(respObj.get("responseCode").getAsInt() == 0) {
				cancelResponseBean = new Gson().fromJson(respObj.get("responseData").getAsJsonObject(), LMSCancelTicketResponseBean.class);
				cancelResponseBean.setResponseCode(respObj.get("responseCode").getAsInt());
			} else {
				cancelResponseBean = new LMSCancelTicketResponseBean();
				cancelResponseBean.setResponseCode(respObj.get("responseCode").getAsInt());
			}

			logger.info("Response Obj For PMS Refund "+cancelResponseBean);
		} catch (GenericException e) {
			throw e;
		}catch (Exception e) {
			throw new GenericException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return cancelResponseBean;
	}
	
	public static Map<String, List<ReconciliationBean>> reconcileTxns(Map<String, List<ReconciliationBean>> reconciliationRequestMap) throws GenericException {
		Map<String, List<ReconciliationBean>> reconciliationResponseMap = null;
		JsonObject reqObj = null;
		String respString = null;
		JsonObject respObj = null;
		logger.debug("***** Inside reconcileTxns Method");
		try {
			reqObj = new JsonObject();
			reqObj.addProperty("reconcile", new Gson().toJson(reconciliationRequestMap));
			logger.debug("Req Obj For PMS reconcileTxns {}" + reqObj);
			respString = TpIntegrationImpl.getPMSResponseString(ServiceName.RECONCILE_MGMT, ServiceMethodName.SLE_RECONCILIATION, reqObj);

			respObj = new JsonParser().parse(respString).getAsJsonObject();

			if (respObj.get("responseCode").getAsInt() == 0) {
				reconciliationResponseMap = new Gson().fromJson(respObj.get("reconcile").getAsString(), new TypeToken<Map<String, List<ReconciliationBean>>>() {}.getType());
			}
			logger.debug("Response Obj For PMS reconcileTxns {}" + reconciliationResponseMap);
		} catch (Exception e) {
			throw new GenericException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return reconciliationResponseMap;
	}
	
}
