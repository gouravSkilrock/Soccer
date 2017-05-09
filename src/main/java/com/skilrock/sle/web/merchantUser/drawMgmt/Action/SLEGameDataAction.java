package com.skilrock.sle.web.merchantUser.drawMgmt.Action;

import java.io.PrintWriter;
import java.sql.Connection;
import java.util.List;

import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.skilrock.sle.common.DBConnect;
import com.skilrock.sle.common.SportsLotteryUtils;
import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.UtilityFunctions;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.embedded.common.BaseActionWeb;
import com.skilrock.sle.gameDataMgmt.controllerImpl.GameDataControllerImpl;
import com.skilrock.sle.gameDataMgmt.javaBeans.DrawMasterBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.GameMasterBean;
import com.skilrock.sle.merchant.common.javaBeans.MerchantInfoBean;
import com.skilrock.sle.mobile.common.SportsLotteryResponseData;

public class SLEGameDataAction extends BaseActionWeb {
	public SLEGameDataAction() {
		super(SLEGameDataAction.class.getName());
	}
	private static final long serialVersionUID = 1L;
	private String requestData;
	List<DrawMasterBean> matchListDataDrawWiseBean;
	List<DrawMasterBean> sleResultListDataDrawWiseBean;
	private String gameTypDispName;
	
	
	public void fetchSLEDrawData() {
		response.setContentType("application/json");
		PrintWriter out = null;
		HttpSession session = null;
		List<GameMasterBean> gameMasterList = null;
		Connection con = null;
		JSONObject finalSportsLotteryData = new JSONObject();
		int merchantId = 0;
		try {
			out = response.getWriter();
			session = request.getSession();
			con = DBConnect.getConnection();
			String merCode = session.getAttribute("MER_CODE").toString();
			merchantId = UtilityFunctions.getMerchantIdFromMerchantName(merCode, con);
			gameMasterList = GameDataControllerImpl.getInstance().getSportsLotteryGameData(merchantId);
			finalSportsLotteryData = SportsLotteryResponseData.generateDrawGameData(gameMasterList,0,"success");
		} catch (Exception ex) {
			ex.printStackTrace();
			finalSportsLotteryData.put("errorCode", SLEErrors.GENERAL_EXCEPTION_ERROR_CODE);
			finalSportsLotteryData.put("errorMsg", SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			finalSportsLotteryData.put("isSuccess", false);
		} finally {
			if (finalSportsLotteryData.isEmpty()) {
				finalSportsLotteryData.put("errorMsg", "Error Occurred");
				finalSportsLotteryData.put("isSuccess", false);				
			}
			logger.info("*****Final Sports Lottery Data For web is : "
					+ finalSportsLotteryData);
			out.print(finalSportsLotteryData);
			out.flush();
			out.close();
			DBConnect.closeConnection(con);
		}
	}
	
	public String fetchMatchlistData(){
		String merchantCode=null;
		MerchantInfoBean merchantInfoBean=null;
		try{
			JsonObject sportsLotteryMatchListreqData = new JsonParser().parse(getRequestData()).getAsJsonObject();
			merchantCode = sportsLotteryMatchListreqData.get("merchantCode").getAsString();
			merchantInfoBean = Util.merchantInfoMap.get(merchantCode);
			gameTypDispName=SportsLotteryUtils.gameTypeInfoMerchantMap.get(merchantCode).get(sportsLotteryMatchListreqData.get("gameTypeId").getAsInt()).getGameTypeDispName();
			matchListDataDrawWiseBean=GameDataControllerImpl.getInstance().getMatchDataDrawWiseForWeb(sportsLotteryMatchListreqData.get("gameId").getAsInt(), sportsLotteryMatchListreqData.get("gameTypeId").getAsInt(), merchantInfoBean.getMerchantId());
		}catch (SLEException pe) {
			request.setAttribute("SLE_EXCEPTION", pe.getErrorMessage());
			return "applicationAjaxException";
		} catch (Exception e) {
			request.setAttribute("SLE_EXCEPTION", SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			return "applicationAjaxException";
		}
		return SUCCESS;
	}
	
	
	public String fetchSLEDrawResultData(){
		String merchantCode=null;
		MerchantInfoBean merchantInfoBean=null;
		try{
			JsonObject sportsLotteryMatchListreqData = new JsonParser().parse(getRequestData()).getAsJsonObject();			
			merchantCode = sportsLotteryMatchListreqData.get("merchantCode").getAsString();
			merchantInfoBean = Util.merchantInfoMap.get(merchantCode);
			gameTypDispName=SportsLotteryUtils.gameTypeInfoMerchantMap.get(merchantCode).get(sportsLotteryMatchListreqData.get("gameTypeId").getAsInt()).getGameTypeDispName();
			sleResultListDataDrawWiseBean=GameDataControllerImpl.getInstance().getSleResultDataDrawWiseForWeb(sportsLotteryMatchListreqData.get("gameId").getAsInt(), sportsLotteryMatchListreqData.get("gameTypeId").getAsInt(), merchantInfoBean.getMerchantId());
		}catch (SLEException pe) {
			request.setAttribute("SLE_EXCEPTION", pe.getErrorMessage());
			return "applicationAjaxException";
		} catch (Exception e) {
			request.setAttribute("SLE_EXCEPTION", SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			return "applicationAjaxException";
		}
		return SUCCESS;
		
	}

	public String getRequestData() {
		return requestData;
	}

	public void setRequestData(String requestData) {
		this.requestData = requestData;
	}

	public List<DrawMasterBean> getMatchListDataDrawWiseBean() {
		return matchListDataDrawWiseBean;
	}

	public void setMatchListDataDrawWiseBean(
			List<DrawMasterBean> matchListDataDrawWiseBean) {
		this.matchListDataDrawWiseBean = matchListDataDrawWiseBean;
	}

	public List<DrawMasterBean> getSleResultListDataDrawWiseBean() {
		return sleResultListDataDrawWiseBean;
	}

	public void setSleResultListDataDrawWiseBean(
			List<DrawMasterBean> sleResultListDataDrawWiseBean) {
		this.sleResultListDataDrawWiseBean = sleResultListDataDrawWiseBean;
	}

	public String getGameTypDispName() {
		return gameTypDispName;
	}

	public void setGameTypDispName(String gameTypDispName) {
		this.gameTypDispName = gameTypDispName;
	}
	
}