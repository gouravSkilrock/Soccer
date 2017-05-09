package com.skilrock.sle.merchant.weaver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.skilrock.sle.common.exception.GenericException;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.common.javaBeans.UserInfoBean;
import com.skilrock.sle.merchant.TpIntegrationImpl;
import com.skilrock.sle.merchant.weaver.WeaverUtils.BALANCE_TYPE;
import com.skilrock.sle.merchant.weaver.WeaverUtils.ServiceMethods;
import com.skilrock.sle.merchant.weaver.WeaverUtils.TxnTypes;
import com.skilrock.sle.tp.rest.common.javaBeans.TpUserRegistrationBean;
import com.skilrock.sle.tp.rest.common.javaBeans.TransactionBean;


public class WeaverIntegrationImpl {
	private static final Logger logger = LoggerFactory.getLogger(WeaverIntegrationImpl.class);

	public static WeaverGamePlayResponseBean purchaseTicketAtWeaver(Map<Long, TransactionBean> sleTxnsIdMap,String gameDevName,int gameId, UserInfoBean userInfoBean) throws SLEException{
		WeaverGamePlayResponseBean gameResponseBean = null;
		JsonObject reqObj = null;
		String respString = null;
		JsonObject respObj = null;
		try {
			reqObj = new JsonObject();
			reqObj.addProperty("playerId", userInfoBean.getMerchantUserId());
			reqObj.addProperty("sessionId", userInfoBean.getUserSessionId());
			reqObj.addProperty("device", userInfoBean.getChannelName());
			reqObj.addProperty("userAgent", userInfoBean.getUserAgent());
			reqObj.addProperty("wrContriAmount",WeaverUtils.WEAVER_CONTRI_AMOUNT);
			reqObj.addProperty("aliasName", WeaverUtils.getAliasName());
			JsonArray txnList = new JsonArray();
			for(Entry<Long, TransactionBean> entry : sleTxnsIdMap.entrySet()){
				TransactionBean gamePlayBean=entry.getValue();
				JsonObject txnObject = new JsonObject();
				txnObject.addProperty("refTxnNo", gamePlayBean.getTransactionId());
				txnObject.addProperty("walletType",WeaverUtils.WALLET_TYPE );
				txnObject.addProperty("balanceType",gamePlayBean.isPromoTkt()?BALANCE_TYPE.PROMO.toString():BALANCE_TYPE.MAIN.toString());
				txnObject.addProperty("currencyCode","");
				txnObject.addProperty("amount", gamePlayBean.getTransactionAmt());
				txnObject.addProperty("walletType", WeaverUtils.WALLET_TYPE);
				txnObject.addProperty("balanceType", BALANCE_TYPE.MAIN.toString());
				txnObject.addProperty("gameId", gamePlayBean.getGameId());
	     		txnObject.addProperty("gameName", gamePlayBean.getGameDevName());
	     		txnObject.addProperty("particular",String.format(TxnTypes.WAGER+"-"+ gameDevName+"-"+gameId));
	     		txnList.add(txnObject);
			}
			reqObj.add("transactionInfoList", txnList);
	  		//	 reqObj.addProperty("promoTicket", gamePlayBean.isPromoTkt());
			respString = TpIntegrationImpl.getWeaverResponseString(WeaverUtils.BASE_SERVICE_TXN, ServiceMethods.wager, TxnTypes.WAGER, reqObj);
			respObj = new JsonParser().parse(respString).getAsJsonObject();
			gameResponseBean = new Gson().fromJson(respObj, WeaverGamePlayResponseBean.class);
			logger.info("Response Obj For Weaver Sale {}", gameResponseBean);
		}  catch (Exception e) {
			logger.error("Weaver Wager Error", e);
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return gameResponseBean;
	}
	public static TpUserRegistrationBean fetchUserDataAtWeaver(UserInfoBean userInfoBean) throws SLEException {
		TpUserRegistrationBean regBean = new TpUserRegistrationBean();
		JsonObject reqObj = null;
		String respString = null;
		JsonObject respObj = null;
		logger.info("***** Inside fetch User Data at weaver method  *****");
		try {
			reqObj = new JsonObject();
			reqObj.addProperty("playerId", userInfoBean.getMerchantUserId());
			reqObj.addProperty("domainName",WeaverUtils.getAliasName());
			reqObj.addProperty("playerToken", userInfoBean.getUserSessionId());
			logger.info("Req Obj For Weaver User Fetch Data {}", reqObj);
			respString = TpIntegrationImpl.getWeaverResponseString(WeaverUtils.BASE_SERVICE_REST, ServiceMethods.playerProfile,null,reqObj);
			respObj = new JsonParser().parse(respString).getAsJsonObject();
			logger.info("Response Obj From Weaver User Fetch Data {}", respObj);
			if(respObj.get("errorCode").getAsInt() == 0) {
				JsonObject plrInfoData =respObj.get("playerInfoBean").getAsJsonObject();
				regBean.setSessionId(userInfoBean.getUserSessionId());
				if(plrInfoData.get("firstName")!=null){
					regBean.setFirstName(plrInfoData.get("firstName").getAsString());
				}else{
					regBean.setFirstName("");
				}
				if(plrInfoData.get("lastName")!=null){
					regBean.setLastName(plrInfoData.get("lastName").getAsString());
				}else{
					regBean.setLastName("");
				}
				if(plrInfoData.get("gender")!=null){
					regBean.setGender(plrInfoData.get("gender").getAsString());
				}else{
					regBean.setGender("");
				}
				if(plrInfoData.get("emailId")!=null){
					regBean.setEmailId(plrInfoData.get("emailId").getAsString());
				}else{
					regBean.setEmailId("");
				}
				if(plrInfoData.get("mobileNo")!=null){
					regBean.setMobileNbr(plrInfoData.get("mobileNo").getAsString());
				}else{
					regBean.setMobileNbr("");
				}
				if(plrInfoData.get("addressLine1")!=null){
					regBean.setAddress(plrInfoData.get("addressLine1").getAsString());
				}else{
					regBean.setAddress("");
				}
				if(plrInfoData.get("city")!=null){
					regBean.setCity(plrInfoData.get("city").getAsString());
				}else{
					regBean.setCity("");
				}
				if(plrInfoData.get("stateCode")!=null){
					regBean.setState(plrInfoData.get("stateCode").getAsString());
				}else{
					regBean.setState("");
				}
				if(plrInfoData.get("countryCode")!=null){
					regBean.setCountry(plrInfoData.get("countryCode").getAsString());
				}else{
					regBean.setCountry("");
				}
				
				
				regBean.setUserName(plrInfoData.get("userName").getAsString());
				regBean.setUserId(plrInfoData.get("playerId").getAsInt());
				regBean.setUserType("PLAYER");
				//regBean = new Gson().fromJson(respObj.get("responseData").getAsJsonObject(), TpUserRegistrationBean.class);
			} else {
				if (respObj.get("errorCode").getAsInt() == 203)
					throw new SLEException(SLEErrors.INVALID_SESSION_ERROR_CODE, SLEErrors.INVALID_SESSION_ERROR_MESSAGE);
				throw new SLEException(respObj.get("errorCode").getAsInt(), respObj.get("errorMsg").getAsString());
			}
			//logger.debug("Response Obj For User Fetch Data {}", respObj);
		}catch (SLEException e) {
			throw e;
		}catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return regBean;
	}
	
	public static WeaverGamePlayResponseBean cancelTicketTxnAtWeaver(Map<Long, TransactionBean> sleTransactionMap, UserInfoBean userInfoBean) throws GenericException, SLEException{
		WeaverGamePlayResponseBean gameResponseBean = null;
		if (sleTransactionMap.isEmpty()) {
			logger.error("SLE Transaction Map is Empty!!");
			throw new SLEException(SLEErrors.SLE_TRANSACTION_MAP_EMPTY_ERROR_CODE,SLEErrors.SLE_TRANSACTION_MAP_EMPTY_ERROR_MESSAGE);
		}
		if (userInfoBean == null) {
			logger.error("UserInfoBean is NULL!!");
			throw new SLEException(SLEErrors.USERINFOBEAN_NULL_ERROR_CODE, SLEErrors.USERINFOBEAN_NULL_ERROR_MESSAGE);
		}
		try {
			List<WeaverPlayerTxnDataBean> txnDataBeans = new ArrayList<WeaverPlayerTxnDataBean>();
			for(Map.Entry<Long,TransactionBean> txnBean :sleTransactionMap.entrySet()){
				txnDataBeans.add(prepareWeaverPlayerTxnDataBean(userInfoBean, txnBean));
			}
			gameResponseBean = prepareGameResponseBean(txnDataBeans);
		}catch (GenericException e) {
			throw e;
		}catch (Exception e) {
			e.printStackTrace();
			throw new GenericException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return gameResponseBean;
	}
	private static WeaverGamePlayResponseBean prepareGameResponseBean(List<WeaverPlayerTxnDataBean> txnDataBeans) throws GenericException {
		WeaverGamePlayResponseBean gameResponseBean;
		String responseString = null;
		JsonObject requestObj = new JsonObject();
		requestObj.add("playerTxnBean", new Gson().toJsonTree(txnDataBeans));
		responseString = TpIntegrationImpl.getWeaverResponseString(WeaverUtils.BASE_SERVICE_TXN, ServiceMethods.plrTxn, TxnTypes.WAGER_REFUND, requestObj);
		gameResponseBean = new Gson().fromJson(new JsonParser().parse(responseString).getAsJsonObject(), WeaverGamePlayResponseBean.class);
		logger.debug("Response Obj For Weaver cancel {}", gameResponseBean);
		return gameResponseBean;
	}
	private static WeaverPlayerTxnDataBean prepareWeaverPlayerTxnDataBean(UserInfoBean userInfoBean,
			Map.Entry<Long, TransactionBean> txnBean) {
		WeaverPlayerTxnDataBean weavertxnBean = new WeaverPlayerTxnDataBean();
		weavertxnBean.setPlayerId(String.valueOf(userInfoBean.getMerchantUserId()));
		weavertxnBean.setAliasName(WeaverUtils.getAliasName());
		weavertxnBean.setWalletType(WeaverUtils.WALLET_TYPE);
		weavertxnBean.setBalanceType(BALANCE_TYPE.MAIN.toString());
		weavertxnBean.setRefWagerTxnId(txnBean.getValue().getMerchantTxnId());
		weavertxnBean.setGameId(txnBean.getValue().getGameId()+"");
		weavertxnBean.setGameName(txnBean.getValue().getGameDevName());
		weavertxnBean.setParticular(String.format(TxnTypes.WAGER+"-"+txnBean.getValue().getTransactionId())+"-"+ txnBean.getValue().getGameId());
		return weavertxnBean;
	}
	
	public static List<WeaverPlayerTxnResponseDataBean> confirmTicketTxnAtWeaver(List<WeaverPlayerTxnDataBean> weaverPlrTxnBeanList) throws SLEException{
		List<WeaverPlayerTxnResponseDataBean> responseDataBeans = null;
		JsonObject reqObj = null;
		String respString = null;
		JsonObject respObj = null;
		try {
			if (weaverPlrTxnBeanList == null || weaverPlrTxnBeanList.isEmpty()) {
				logger.info("weaverPlrTxnBeanList in null or empty hence throwing exception");
				throw new SLEException();
			}
			reqObj = new JsonObject();
			reqObj.add("playerTxnBean", new Gson().toJsonTree(weaverPlrTxnBeanList));
			respString = TpIntegrationImpl.getWeaverResponseString(WeaverUtils.BASE_SERVICE_TXN, ServiceMethods.plrTxn, TxnTypes.WAGER_CONFIRMATION, reqObj);
			respObj = new JsonParser().parse(respString).getAsJsonObject();
			responseDataBeans = new Gson().fromJson(respObj.get("plrWiseRespBean"), new TypeToken<List<WeaverPlayerTxnResponseDataBean>>(){}.getType());
			logger.info("Response Obj For Weaver Confirm {}", responseDataBeans);
		}  catch (Exception e) {
			logger.error("An exception occurred while confirmation", e);
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return responseDataBeans;
	}
	
	public static List<WeaverPlayerTxnResponseDataBean> doWinningTxnAtWeaver(List<WeaverPlayerTxnDataBean> txnDataBeanList) throws SLEException{
		List<WeaverPlayerTxnResponseDataBean> responseDataBeans = null;
		JsonObject reqObj = null;
		String respString = null;
		JsonObject respObj = null;
		try {
			reqObj = new JsonObject();
			reqObj.add("playerTxnBean", new Gson().toJsonTree(txnDataBeanList));
			respString = TpIntegrationImpl.getWeaverResponseString(WeaverUtils.BASE_SERVICE_TXN, ServiceMethods.plrTxn, TxnTypes.WINNING, reqObj);
			respObj = new JsonParser().parse(respString).getAsJsonObject();
			responseDataBeans = new Gson().fromJson(respObj.get("plrWiseRespBean"), new TypeToken<List<WeaverPlayerTxnResponseDataBean>>(){}.getType());
			logger.debug("Response Obj For Weaver Winning {}", responseDataBeans);
		}  catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return responseDataBeans;
	}
	
}
