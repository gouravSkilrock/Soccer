package com.skilrock.sle.embedded.reportsMgmt.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.skilrock.sle.common.SportsLotteryUtils;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.common.javaBeans.UserInfoBean;
import com.skilrock.sle.commonMethod.serviceImpl.CommonMethodsServiceImpl;
import com.skilrock.sle.embedded.common.BaseActionTerminal;
import com.skilrock.sle.gameDataMgmt.javaBeans.GameMasterBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.GameTypeMasterBean;
import com.skilrock.sle.merchant.common.javaBeans.MerchantInfoBean;
import com.skilrock.sle.reportsMgmt.controllerImpl.WinningResultReportControllerImpl;
import com.skilrock.sle.reportsMgmt.javaBeans.WinningResultReportBean;

public class SLEWinningResultReportAction extends BaseActionTerminal {
	private static final long serialVersionUID = 1L;

	private String gameId;
	private String gameTypeId;

	public SLEWinningResultReportAction() {
		super(SLEWinningResultReportAction.class.getName());
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
			logger.info("Sports Lottery Game List Request Data: "+getRequest());
			// JSONObject sportsLotteryPlayData = (JSONObject)
			// JSONSerializer.toJSON(getRequestData());
			response.setContentType("application/json");

			merchantCode = getMerCode();

			merchantInfoBean = CommonMethodsServiceImpl.getInstance().fetchMerchantInfo().get(merchantCode);

			userInfoBean = new UserInfoBean();
			userInfoBean.setUserName(getUserName());
			userInfoBean.setMerchantId(merchantInfoBean.getMerchantId());
			userInfoBean.setUserSessionId(getSessId());
			userInfoBean.setMerchantDevName(merchantCode);

			CommonMethodsServiceImpl.getInstance().fetchAndCheckUserData(userInfoBean);

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
			// responseObject.put("errorMsg",
			// MobileErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			responseObject.put("errorMsg", getText(String
					.valueOf(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE)));
			responseObject.put("isSuccess", false);
			return;
		} finally {
			if (responseObject.isEmpty()) {
				responseObject.put("errorCode",
						SLEErrors.GENERAL_EXCEPTION_ERROR_CODE);
				// responseObject.put("errorMsg",
				// MobileErrors.COMPILE_TIME_ERROR_MESSAGE);
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
		WinningResultReportControllerImpl controllerImpl;
		String merchantCode = null;
		int gameId = 0, gameTypeId = 0;
		MerchantInfoBean merchantInfoBean = null;
		UserInfoBean userInfoBean = null;
		List<WinningResultReportBean> winResultBeanList = null;
		String finalData=null;
		try {
			merchantCode = getMerCode();
			gameId = Integer.parseInt(request.getParameter("gameId"));
			gameTypeId = Integer.parseInt(request.getParameter("gameTypeId"));

			merchantInfoBean = CommonMethodsServiceImpl.getInstance()
			.fetchMerchantInfo().get(merchantCode);

			userInfoBean = new UserInfoBean();
			userInfoBean.setUserName(getUserName());
			userInfoBean.setMerchantId(merchantInfoBean.getMerchantId());
			userInfoBean.setUserSessionId(getSessId());
			userInfoBean.setMerchantDevName(merchantInfoBean
					.getMerchantDevName());

			CommonMethodsServiceImpl.getInstance().fetchAndCheckUserData(userInfoBean);

			logger.debug("Merchant User Info Bean is "+userInfoBean);

			controllerImpl = new WinningResultReportControllerImpl();
			winResultBeanList = controllerImpl.winningResultReportSearch(
					gameId, gameTypeId, merchantCode);

			finalData=generateWinningResultReport(winResultBeanList, merchantInfoBean);
			response.getOutputStream().write(finalData.getBytes());
		} catch(SLEException e){
			try{
				if(e.getErrorCode()==10012 || e.getErrorCode()==118 ){
					response.getOutputStream().write(("ErrorMsg:" + SLEErrors.INVALID_SESSION_MOBILE_ERROR_MESSAGE+"|ErrorCode:01|").getBytes());
				}else{
					response.getOutputStream().write(("ErrorMsg:" + e.getErrorMessage()).getBytes());
				}
			
			}catch (IOException e1) {
				e.printStackTrace();
				
			}	
		}
		catch (Exception e) {
			e.printStackTrace();
			try{
				response.getOutputStream().write(("ErrorMsg:Error try again").getBytes());
			}catch (IOException e1) {
				e.printStackTrace();
			}
			
		}
	}

	private String generateWinningResultReport(List<WinningResultReportBean> winResultBeanList,MerchantInfoBean merchantInfoBean) throws SLEException {
		String finalData = "";
		try{
			if (winResultBeanList != null) {
				GameTypeMasterBean gameTypeBean = SportsLotteryUtils.gameTypeInfoMerchantMap
						.get(merchantInfoBean.getMerchantDevName()).get(Integer.parseInt(request.getParameter("gameTypeId")));
				GameMasterBean gameBean = SportsLotteryUtils.gameInfoMerchantMap
						.get(merchantInfoBean.getMerchantDevName()).get(Integer.parseInt(request.getParameter("gameId")));
	
				finalData += "gameName:" + gameBean.getGameDispName();
				finalData += "|gameTypeName:" + gameTypeBean.getGameTypeDispName();
				
				if (winResultBeanList.size() > 0) {
					for (int i = 0; i < winResultBeanList.size(); i++) {
						WinningResultReportBean winResultBean = (WinningResultReportBean) winResultBeanList.get(i);
	
						finalData += "|drawDate:" + winResultBean.getDrawDate() + "|drawTime:" + winResultBean.getDrawTime()+ "|drawName:" + winResultBean.getDrawName();
						finalData += "|eventInfo:";
	
						Map<String, String> eventMap = winResultBean.getEventOptionMap();
						if(eventMap.size() > 0){
							for (Map.Entry<String, String> entry : eventMap.entrySet()) {
								finalData += entry.getKey() + "@" + entry.getValue() + "~";
							}
						}else{
							throw new SLEException(SLEErrors.NO_RESULT_AVL_ERROR_CODE, SLEErrors.NO_RESULT_AVL_ERROR_MESSGE);
						}
						finalData = finalData.substring(0, finalData.length() - 1);
						finalData += "#";
					}
					finalData = finalData.substring(0, finalData.length() - 1);
					finalData += "|";
					
					
				} else {
					throw new SLEException(SLEErrors.NO_RESULT_AVL_ERROR_CODE, SLEErrors.NO_RESULT_AVL_ERROR_MESSGE);
				}
			}else{
				throw new SLEException(SLEErrors.NO_RESULT_AVL_ERROR_CODE, SLEErrors.NO_RESULT_AVL_ERROR_MESSGE);
			}
		
		} catch (SLEException e) {
			throw e;
		}catch (Exception e) {
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return finalData;
	}

	public static void main(String[] args) {
		MerchantInfoBean bean = new MerchantInfoBean();
		bean.setMerchantId(1);

		// Class.forName("com.mysql.jdbc.Driver");
		// Connection connection =
		// DriverManager.getConnection("jdbc:mysql://192.168.124.84/sle_blank_db",
		// "root", "root");

		// System.out.println("Game Map:"+new
		// SLEWinningResultReportAction().fetchWinningResultReport());
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public String getGameTypeId() {
		return gameTypeId;
	}

	public void setGameTypeId(String gameTypeId) {
		this.gameTypeId = gameTypeId;
	}

}
