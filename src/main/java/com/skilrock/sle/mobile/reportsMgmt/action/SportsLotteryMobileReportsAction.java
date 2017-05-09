package com.skilrock.sle.mobile.reportsMgmt.action;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.skilrock.sle.common.SportsLotteryUtils;
import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.common.javaBeans.ReportBean;
import com.skilrock.sle.common.javaBeans.TicketInfoBean;
import com.skilrock.sle.common.javaBeans.UserInfoBean;
import com.skilrock.sle.commonMethod.serviceImpl.CommonMethodsServiceImpl;
import com.skilrock.sle.gameDataMgmt.javaBeans.GameMasterBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.GameTypeMasterBean;
import com.skilrock.sle.gamePlayMgmt.javaBeans.ReprintTicketBean;
import com.skilrock.sle.gamePlayMgmt.javaBeans.SportsLotteryGameEventDataBean;
import com.skilrock.sle.merchant.common.javaBeans.MerchantInfoBean;
import com.skilrock.sle.mobile.common.BaseActionMobile;
import com.skilrock.sle.mobile.common.SportsLotteryResponseData;
import com.skilrock.sle.mobile.reportsMgmt.controllerImpl.SportsLotteryMobileReportsControllerImpl;
import com.skilrock.sle.reportsMgmt.controllerImpl.WinningResultReportControllerImpl;
import com.skilrock.sle.reportsMgmt.javaBeans.WinningResultReportBean;

public class SportsLotteryMobileReportsAction extends BaseActionMobile {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public SportsLotteryMobileReportsAction() {
		super(SportsLotteryMobileReportsAction.class.getName());
	}
	
	public void fetchPurchaseTicketReport() {
		PrintWriter out = null;
		JSONObject jsonObject = null;
		String merchantCode = null;
		SportsLotteryMobileReportsControllerImpl mobileReportsControllerImpl = null;
		Map<Long, TicketInfoBean> tktInfoMap = null;
		ReportBean reportBean = null;
		UserInfoBean userInfoBean = null;
		try {
			jsonObject = new JSONObject();
			out = response.getWriter();

			logger.info("Sports Lottery fetchPurchaseTicketReport Request Data: "+getRequestData());
			JsonObject requestObj = new JsonParser().parse(getRequestData()).getAsJsonObject();
			response.setContentType("application/json");
			
			merchantCode = requestObj.get("merchantCode").getAsString();
			
			userInfoBean = new UserInfoBean();
			userInfoBean.setUserName(requestObj.get("playerName").getAsString());
			userInfoBean.setMerchantId((Integer)request.getAttribute("merchantId"));
			userInfoBean.setUserSessionId(requestObj.get("sessionId").getAsString());
			userInfoBean.setMerchantDevName(merchantCode);
			
			CommonMethodsServiceImpl.getInstance().fetchAndCheckUserData(userInfoBean);
			logger.debug("Merchant User Info Bean is "+userInfoBean);
			
			mobileReportsControllerImpl = new SportsLotteryMobileReportsControllerImpl();
			reportBean = new ReportBean();
			reportBean.setReportChannel("MOBILE");
			tktInfoMap = mobileReportsControllerImpl.fetchPurchaseTicketReport(userInfoBean,reportBean);
			
			jsonObject = SportsLotteryResponseData.prepareTrackTktData(tktInfoMap);
			
		} catch (SLEException pe) {
			pe.printStackTrace();
			jsonObject.put("errorCode", pe.getErrorCode());
			jsonObject.put("errorMsg", pe.getErrorMessage());
			jsonObject.put("isSuccess", false);
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("errorCode", SLEErrors.GENERAL_EXCEPTION_ERROR_CODE);
			jsonObject.put("errorMsg", SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			jsonObject.put("isSuccess", false);
		} finally {
			if (jsonObject.isEmpty()) {
				jsonObject.put("errorCode", SLEErrors.GENERAL_EXCEPTION_ERROR_CODE);
				jsonObject.put("errorMsg", SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
				jsonObject.put("isSuccess", false);
			}
			logger.debug("***** Mobile Game Play Response "+jsonObject);
			out.print(jsonObject);
			out.flush();
			out.close();
		}
	}
	
	public void fetchPurchaseTicketData() {
		ReprintTicketBean tktBean = null;
		TicketInfoBean tktInfoBean = null;
		PrintWriter out = null;
		JSONObject jsonObject = null;
		String merchantCode = null;
		SportsLotteryMobileReportsControllerImpl mobileReportsControllerImpl = null;
		List<String> eventOptionsList = null;

		UserInfoBean userInfoBean = null;
		try {
			jsonObject = new JSONObject();
			out = response.getWriter();

			logger.info("Sports Lottery fetchPurchaseTicketReport Request Data: "+getRequestData());
			JsonObject requestObj = new JsonParser().parse(getRequestData()).getAsJsonObject();
			response.setContentType("application/json");

			merchantCode = requestObj.get("merchantCode").getAsString();

			userInfoBean = new UserInfoBean();
			userInfoBean.setUserName(requestObj.get("playerName").getAsString());
			userInfoBean.setMerchantId((Integer)request.getAttribute("merchantId"));
			userInfoBean.setUserSessionId(requestObj.get("sessionId").getAsString());
			userInfoBean.setMerchantDevName(merchantCode);
			userInfoBean.setUserType("PLAYER");

			CommonMethodsServiceImpl.getInstance().fetchAndCheckUserData(userInfoBean);
			logger.debug("Merchant User Info Bean is "+userInfoBean);

			tktInfoBean = CommonMethodsServiceImpl.getInstance().fetchTicketInfoUsingMerchantTxnId(requestObj.get("txnId").getAsLong());
			if(tktInfoBean.getTxnId()==null){
				throw new SLEException(SLEErrors.FAILED_TRANSACTION_ERROR_CODE,SLEErrors.FAILED_TRANSACTION_ERROR_MESSAGE);
			}
			tktBean = new ReprintTicketBean();
			tktBean.setGameId(tktInfoBean.getGameId());
			tktBean.setGameTypeId(tktInfoBean.getGameTypeId());
			tktBean.setTicketNumber(String.valueOf(tktInfoBean.getTktNbr()));
			mobileReportsControllerImpl = new SportsLotteryMobileReportsControllerImpl();
			eventOptionsList = mobileReportsControllerImpl.fetchPurchaseTicketData(tktBean, Util.fetchMerchantInfoBean(userInfoBean.getMerchantId()),requestObj.get("txnId").getAsLong());
	
			jsonObject = SportsLotteryResponseData.generateSportsLotteryTrackTicketResponseData(tktBean.getGamePlayBean(), userInfoBean.getMerchantDevName(), 0, eventOptionsList);
		} catch (SLEException pe) {
			pe.printStackTrace();
			if(pe.getErrorCode()==10012){
				jsonObject.put("responseCode", SLEErrors.INVALID_SESSION_MOBILE_ERROR_CODE);
				jsonObject.put("responseMsg", SLEErrors.INVALID_SESSION_MOBILE_ERROR_MESSAGE);
				jsonObject.put("isSuccess", false);
			}else{
				jsonObject.put("responseCode", pe.getErrorCode());
				jsonObject.put("responseMsg", pe.getErrorMessage());
				jsonObject.put("isSuccess", false);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("responseCode", SLEErrors.GENERAL_EXCEPTION_ERROR_CODE);
			jsonObject.put("responseMsg", SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			jsonObject.put("isSuccess", false);
		} finally {
			if (jsonObject.isEmpty()) {
				jsonObject.put("responseCode", SLEErrors.GENERAL_EXCEPTION_ERROR_CODE);
				jsonObject.put("responseMsg", SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
				jsonObject.put("isSuccess", false);
			}
			logger.debug("***** Mobile Game Play Response "+jsonObject);
			out.print(jsonObject);
			out.flush();
			out.close();
		}
	}
	
	public void getActiveGames() {
		response.setContentType("application/json");
		JSONObject responseObject = new JSONObject();
		JSONArray gameArray = new JSONArray();
		JSONArray gameTypeArray = null;
		JSONObject gameData = null;
		PrintWriter out = null;
		MerchantInfoBean merchantInfoBean = null;
		String merchantCode = null;
		UserInfoBean userInfoBean = null;
		try {
			logger.info("Sports Lottery Game List Request Data: "+getRequestData());
			JsonObject sportsLotteryPlayData = new JsonParser().parse(
					getRequestData()).getAsJsonObject(); // JSONObject
			response.setContentType("application/json");

			merchantCode = sportsLotteryPlayData.get("merchantCode")
					.getAsString();

			merchantInfoBean = CommonMethodsServiceImpl.getInstance()
					.fetchMerchantInfo().get(merchantCode);

			userInfoBean = new UserInfoBean();
			userInfoBean.setUserName(sportsLotteryPlayData.get("playerName")
					.getAsString());
			userInfoBean.setMerchantId(merchantInfoBean.getMerchantId());
			userInfoBean.setUserSessionId(sportsLotteryPlayData
					.get("sessionId").getAsString());
			userInfoBean.setMerchantDevName(merchantCode);

			CommonMethodsServiceImpl.getInstance().fetchAndCheckUserData(
					userInfoBean);

			logger.debug("Merchant User Info Bean is "+userInfoBean);

			out = response.getWriter();
			Map<Integer, GameMasterBean> gameList = CommonMethodsServiceImpl
					.getInstance().getGameMap(merchantInfoBean);
			for (Map.Entry<Integer, GameMasterBean> bean : gameList.entrySet()) {
				gameTypeArray = new JSONArray();
				for (GameTypeMasterBean masterBean : bean.getValue()
						.getGameTypeMasterList()) {
					gameData = new JSONObject();
					gameData.put("gameTypeId", masterBean.getGameId());
					gameData.put("gameTypeName", masterBean
							.getGameTypeDispName());
					gameTypeArray.add(gameData);
				}
				gameData = new JSONObject();
				gameData.put("gameId", bean.getValue().getGameId());
				gameData.put("gameName", bean.getValue().getGameDispName());
				gameData.put("gameTypeData", gameTypeArray);
				gameArray.add(gameData);
			}
			responseObject.put("isSuccess", true);
			responseObject.put("gameList", gameArray);
		} catch (Exception e) {
			responseObject.put("errorCode",
					SLEErrors.GENERAL_EXCEPTION_ERROR_CODE);
			responseObject.put("errorMsg", getText(String
					.valueOf(SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE)));
			responseObject.put("isSuccess", false);
			return;
		} finally {
			if (responseObject.isEmpty()) {
				responseObject.put("errorCode",
						SLEErrors.GENERAL_EXCEPTION_ERROR_CODE); //
				responseObject.put("errorMsg",
						SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
				responseObject.put("isSuccess", false);
			}
			logger.info("Response Data: " + responseObject);
			out.print(responseObject);
			out.flush();
			out.close();
		}
	}
	
	public void fetchWinningResultReport() {

		response.setContentType("application/json");
		JSONObject jsonObject = new JSONObject();
		PrintWriter out = null;

		WinningResultReportControllerImpl controllerImpl;

		JSONArray winningArray = new JSONArray();
		JSONObject winningData = null;
		String merchantCode = null;
		int gameId = 0, gameTypeId = 0;
		MerchantInfoBean merchantInfoBean = null;
		UserInfoBean userInfoBean = null;
		List<WinningResultReportBean> winResultBeanList = null;
		try {
			logger.info("Sports Lottery Winning Report Request Data: "+getRequestData());
			JsonObject slWinRepData = new JsonParser().parse(getRequestData())
					.getAsJsonObject();
			merchantCode = slWinRepData.get("merchantCode").getAsString();
			gameId = slWinRepData.get("gameId").getAsInt();
			gameTypeId = slWinRepData.get("gameTypeId").getAsInt();

			merchantInfoBean = CommonMethodsServiceImpl.getInstance()
			.fetchMerchantInfo().get(merchantCode);
//			merchantInfoBean = Util.fetchMerchantInfoBean(Integer
//					.parseInt(merchantCode));

			userInfoBean = new UserInfoBean();
			userInfoBean.setUserName(slWinRepData.get("playerName")
					.getAsString());
			userInfoBean.setMerchantId(merchantInfoBean.getMerchantId());
			userInfoBean.setUserSessionId(slWinRepData.get("sessionId")
					.getAsString());
			userInfoBean.setMerchantDevName(merchantInfoBean
					.getMerchantDevName());

			CommonMethodsServiceImpl.getInstance().fetchAndCheckUserData(
					userInfoBean);

			logger.debug("Merchant User Info Bean is "+userInfoBean);

			out = response.getWriter();

			jsonObject.put("isSuccess", false);
			controllerImpl = new WinningResultReportControllerImpl();
			winResultBeanList = controllerImpl.winningResultReportSearch(
					gameId, gameTypeId, merchantCode);
			if (winResultBeanList != null) {

				if (winResultBeanList.size() > 0) {
				for (int i = 0; i < winResultBeanList.size(); i++) {
					winningData = new JSONObject();
					WinningResultReportBean winResultBean = (WinningResultReportBean) winResultBeanList
							.get(i);
					// String date = dateFormat
					// .format(winResultBean.getDrawDate());
					// String time = timeFormat
					// .format(winResultBean.getDrawTime());
					// Add Winning Data Here...

					winningData.put("drawDate", winResultBean.getDrawDate());
					winningData.put("drawTime", winResultBean.getDrawTime());
					winningData.put("drawName", winResultBean.getDrawName());

					JSONArray eventArray = new JSONArray();
					JSONObject eventData = null;
					// Map<String, String> eventMap = winResultBean
					// .getEventOptionMap();
					// for (Map.Entry<String, String> entry :
					// eventMap.entrySet()) {
					// eventData = new JSONObject();
					// // eventData.put("homeTeamName",
					// // sleEventDataBean.getHomeTeamName());
					// // eventData.put("awayTeamName",
					// // sleEventDataBean.getAwayTeamName());
					// // eventData.put("eventDateTime",
					// // sleEventDataBean.getEventDateTime());
					// // eventData.put("leagueName",
					// // sleEventDataBean.getLeagueName());
					// eventData.put("eventDisplay", entry.getKey());
					// eventData.put("winningEvent", entry.getValue());
					// eventArray.add(eventData);
					// }
					// winningData.put("eventData", eventArray);
					if(winResultBean.getDrawEventList().size() > 0){
						for (SportsLotteryGameEventDataBean eventDataBean : winResultBean
								.getDrawEventList()) {
							eventData = new JSONObject();
							eventData.put("eventDisplayHome", eventDataBean
									.getHomeTeamName());
							eventData.put("eventDisplayAway", eventDataBean
									.getAwayTeamName());
							eventData.put("winningEvent", eventDataBean
									.getWinningEvent());
							eventArray.add(eventData);
						}
						winningData.put("eventData", eventArray);
						winningArray.add(winningData);
					}else{
						throw new SLEException(SLEErrors.NO_RESULT_AVL_ERROR_CODE, SLEErrors.NO_RESULT_AVL_ERROR_MESSGE);
					}
					
				}

				// Map<String, Map<Integer, GameTypeMasterBean>> map = //
				// SportsLotteryUtils.gameTypeInfoMerchantMap.get("").get(gameId).;
				GameTypeMasterBean gameTypeBean = SportsLotteryUtils.gameTypeInfoMerchantMap
						.get(merchantInfoBean.getMerchantDevName()).get(
								gameTypeId);

				jsonObject.put("isSuccess", true);
				jsonObject.put("winningResult", winningArray);
				jsonObject
						.put("gameDevName", gameTypeBean.getGameTypeDevName());
				jsonObject.put("gameDispName", gameTypeBean
						.getGameTypeDispName());
			}
			}else{
				throw new SLEException(SLEErrors.NO_RESULT_AVL_ERROR_CODE, SLEErrors.NO_RESULT_AVL_ERROR_MESSGE);
			}

			return;
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("errorCode", SLEErrors.GENERAL_EXCEPTION_ERROR_CODE);
			jsonObject.put("errorMsg",
					SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			jsonObject.put("isSuccess", false);
			return;
		} finally {
			if (jsonObject.isEmpty()) {
				jsonObject.put("errorCode",
						SLEErrors.GENERAL_EXCEPTION_ERROR_CODE);
				jsonObject.put("errorMsg",
						SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
				jsonObject.put("isSuccess", false);
			}
			logger.info("Response Data: " + jsonObject);
			out.print(jsonObject);
			out.flush();
			out.close();
		}
	}


	
}
