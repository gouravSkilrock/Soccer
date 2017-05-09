package com.skilrock.sle.mobile.gameDataMgmt.action;

import java.io.PrintWriter;
import java.util.List;

import net.sf.json.JSONObject;

import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.gameDataMgmt.controllerImpl.GameDataControllerImpl;
import com.skilrock.sle.gameDataMgmt.javaBeans.GameMasterBean;
import com.skilrock.sle.mobile.common.BaseActionMobile;
import com.skilrock.sle.mobile.common.SportsLotteryResponseData;

public class SportsLotteryGameDataAction extends BaseActionMobile {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//	private static final Logger logger = LoggerFactory.getLogger(SportsLotteryGameDataAction.class);
	
	public SportsLotteryGameDataAction() {
		super(SportsLotteryGameDataAction.class.getName());
	}

	public void fetchSLEDrawData() {
		List<GameMasterBean> gameMasterList = null;
		JSONObject finalSportsLotteryData = new JSONObject();
		PrintWriter out = null;
		int merchantId = 0;
		try {
			out = response.getWriter();
			merchantId = (Integer) request.getAttribute("merchantId");
			gameMasterList = GameDataControllerImpl.getInstance().getSportsLotteryGameData(merchantId);
			finalSportsLotteryData = SportsLotteryResponseData.generateDrawGameData(gameMasterList,0,"success");
			// response.getOutputStream().write("sdsds".getBytes());
//			response.getOutputStream().write(responseString.getBytes());
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
			logger.info("*****Final Sports Lottery Data For Mobile is : "
					+ finalSportsLotteryData);
			out.print(finalSportsLotteryData);
			out.flush();
			out.close();
		}
	}
	
	public void fetchSLEMatchListData() {
		/*List<GameMasterBean> gameMasterList = null;
		Map<Integer, List<EventMasterBean>> matchDataDayWise = null;
		JSONObject finalSportsLotteryData = new JSONObject();
		PrintWriter out = null;
		int merchantId = 0;
		try {
			out = response.getWriter();
			JsonObject reqData = new JsonParser().parse(getRequestData()).getAsJsonObject();
			String listType = reqData.get("listType").getAsString();
			merchantId = (Integer) request.getAttribute("merchantId");
			GameDataControllerImpl sportsLotteryDataControllerImpl = new GameDataControllerImpl();
			if("DRAW_WISE".equalsIgnoreCase(listType)){
				gameMasterList = sportsLotteryDataControllerImpl.getSportsLotteryGameData(merchantId);
				finalSportsLotteryData = SportsLotteryResponseData.prepareSLEMatchListData(gameMasterList);
			}else if("DAY_WISE".equalsIgnoreCase(listType)){
				String fromDate = reqData.get("startDate").getAsString().concat(" 00:00:00");
				String toDate = reqData.get("endDate").getAsString().concat(" 23:59:59");
				//matchDataDayWise = sportsLotteryDataControllerImpl.getMatchDataDayWise(merchantId, fromDate, toDate);
				finalSportsLotteryData = SportsLotteryResponseData.prepareSLEMatchListDataDayWise(matchDataDayWise, merchantId);
			}
			// response.getOutputStream().write("sdsds".getBytes());
			// response.getOutputStream().write(responseString.getBytes());
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
			logger.info("*****Final Sports Lottery Match Data For Mobile is : "
					+ finalSportsLotteryData);
			out.print(finalSportsLotteryData);
			out.flush();
			out.close();
		}*/
	}

}
