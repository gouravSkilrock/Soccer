package com.skilrock.sle.tp.rest.reportsMgmt;

import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.skilrock.sle.common.SLELogger;
import com.skilrock.sle.common.SportsLotteryUtils;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.javaBeans.UserInfoBean;
import com.skilrock.sle.commonMethod.serviceImpl.CommonMethodsServiceImpl;
import com.skilrock.sle.gameDataMgmt.javaBeans.GameMasterBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.GameTypeMasterBean;
import com.skilrock.sle.gamePlayMgmt.javaBeans.SportsLotteryGameEventDataBean;
import com.skilrock.sle.merchant.common.javaBeans.MerchantInfoBean;
import com.skilrock.sle.reportsMgmt.controllerImpl.WinningResultReportControllerImpl;
import com.skilrock.sle.reportsMgmt.javaBeans.WinningResultReportBean;

@Path("/sleWinResultRepMgmt")
public class TPResultReportServiceImpl {

	private static final SLELogger logger = SLELogger.getLogger(TPResultReportServiceImpl.class.getName());

	@Path("/getActiveGames")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response tpGetActiveGames(String requestData) {
		System.out.println(requestData);

		JSONObject responseObject = new JSONObject();
		JSONArray gameArray = new JSONArray();
		JSONArray gameTypeArray = null;
		JSONObject gameData = null;
		MerchantInfoBean merchantInfoBean = null;
		String merchantCode = null;
		UserInfoBean userInfoBean = null;
		try {
			JsonParser parser = new JsonParser();
			JsonObject requestObj = (JsonObject) parser.parse(requestData);

			merchantCode = requestObj.get("merchantCode").getAsString();

			merchantInfoBean = CommonMethodsServiceImpl.getInstance()
					.fetchMerchantInfo().get(merchantCode);

			userInfoBean = new UserInfoBean();
			userInfoBean
					.setUserName(requestObj.get("playerName").getAsString());
			userInfoBean.setMerchantId(merchantInfoBean.getMerchantId());
			userInfoBean.setUserSessionId(requestObj.get("sessionId")
					.getAsString());
			userInfoBean.setMerchantDevName(merchantCode);

			CommonMethodsServiceImpl.getInstance().fetchAndCheckUserData(
					userInfoBean);

			logger.debug("Merchant User Info Bean is "+userInfoBean);

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
			responseObject.put("errorMsg",
					SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			responseObject.put("isSuccess", false);
		} finally {
			if (responseObject.isEmpty()) {
				responseObject.put("errorCode",
						SLEErrors.GENERAL_EXCEPTION_ERROR_CODE);
				responseObject.put("errorMsg",
						SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
				responseObject.put("isSuccess", false);
			}
			logger.info("Response Data: " + responseObject);
		}
		return Response.ok().entity(responseObject).build();
	}

	@Path("/fetchWinningResultReport")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response tpFetchWinningResultReport(String requestData) {
		System.out.println(requestData);
		JSONObject jsonObject = new JSONObject();
		WinningResultReportControllerImpl controllerImpl;
		JSONArray winningArray = new JSONArray();
		JSONObject winningData = null;
		String merchantCode = null;
		int gameId = 0, gameTypeId = 0;
		MerchantInfoBean merchantInfoBean = null;
		UserInfoBean userInfoBean = null;
		List<WinningResultReportBean> winResultBeanList = null;
		try {
			JsonObject slWinRepData = new JsonParser().parse(requestData)
					.getAsJsonObject();
			merchantCode = slWinRepData.get("merchantCode").getAsString();
			gameId = slWinRepData.get("gameId").getAsInt();
			gameTypeId = slWinRepData.get("gameTypeId").getAsInt();

			merchantInfoBean = CommonMethodsServiceImpl.getInstance()
					.fetchMerchantInfo().get(merchantCode);

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
					}

					GameTypeMasterBean gameTypeBean = SportsLotteryUtils.gameTypeInfoMerchantMap
							.get(merchantInfoBean.getMerchantDevName()).get(
									gameTypeId);

					jsonObject.put("isSuccess", true);
					jsonObject.put("winningResult", winningArray);
					jsonObject
							.put("gameDevName", gameTypeBean.getGameTypeDevName());
					jsonObject.put("gameDispName", gameTypeBean
							.getGameTypeDispName());
				} else {
					jsonObject.put("isSuccess", false);
					jsonObject.put("errorMsg", "No Draw/Result Available!!");
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("errorCode", SLEErrors.GENERAL_EXCEPTION_ERROR_CODE);
			jsonObject.put("errorMsg",
					SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			jsonObject.put("isSuccess", false);
		} finally {
			if (jsonObject.isEmpty()) {
				jsonObject.put("errorCode",
						SLEErrors.GENERAL_EXCEPTION_ERROR_CODE);
				jsonObject.put("errorMsg",
						SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
				jsonObject.put("isSuccess", false);
			}
			logger.info("Response Data: " + jsonObject);
		}
		return Response.ok().entity(jsonObject).build();
	}

}
