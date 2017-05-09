package com.skilrock.sle.web.merchantUser.reportsMgmt.Action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.commonMethod.serviceImpl.CommonMethodsServiceImpl;
import com.skilrock.sle.embedded.common.BaseActionWeb;
import com.skilrock.sle.gameDataMgmt.javaBeans.GameMasterBean;
import com.skilrock.sle.merchant.common.javaBeans.MerchantInfoBean;
import com.skilrock.sle.reportsMgmt.controllerImpl.FixtureWiseWinningAnalysisReportControllerImpl;
import com.skilrock.sle.reportsMgmt.javaBeans.FixtureWiseWinningAnalysisReportBean;

public class FixtureWiseWinningAnalysisReportAction extends BaseActionWeb {
	private static final long serialVersionUID = 1L;

	public FixtureWiseWinningAnalysisReportAction() {
		super(FixtureWiseWinningAnalysisReportAction.class.getName());
	}

	private Map<Integer, GameMasterBean> gameMap;
	private int gameId;
	private int gameTypeId;
	private String selectedDate;
	private int drawId;
	private Map<Integer, FixtureWiseWinningAnalysisReportBean> reportMap;

	public Map<Integer, GameMasterBean> getGameMap() {
		return gameMap;
	}

	public void setGameMap(Map<Integer, GameMasterBean> gameMap) {
		this.gameMap = gameMap;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public int getGameTypeId() {
		return gameTypeId;
	}

	public void setGameTypeId(int gameTypeId) {
		this.gameTypeId = gameTypeId;
	}

	public String getSelectedDate() {
		return selectedDate;
	}

	public void setSelectedDate(String selectedDate) {
		this.selectedDate = selectedDate;
	}

	public int getDrawId() {
		return drawId;
	}

	public void setDrawId(int drawId) {
		this.drawId = drawId;
	}

	public Map<Integer, FixtureWiseWinningAnalysisReportBean> getReportMap() {
		return reportMap;
	}

	public void setReportMap(Map<Integer, FixtureWiseWinningAnalysisReportBean> reportMap) {
		this.reportMap = reportMap;
	}

	public String reportMenu() {
		HttpSession session = null;
		MerchantInfoBean merchantInfoBean = null;
		try {
			session = request.getSession();
			String merCode = session.getAttribute("MER_CODE").toString();
			merchantInfoBean =  Util.merchantInfoMap.get(merCode);
			gameMap = CommonMethodsServiceImpl.getInstance().getGameMap(merchantInfoBean);
		} catch (SLEException se) {
			logger.info("ErrorCode:" + se.getErrorCode() + " ErrorMessage:"+ se.getErrorMessage());
			request.setAttribute("SLE_EXCEPTION", se.getErrorMessage());
			return "applicationException";
		}

		return SUCCESS;
	}

	public void drawSearch() throws IOException {
		HttpSession session = null;
		MerchantInfoBean merchantInfoBean = null;
		PrintWriter out = response.getWriter();
		try {
			session = request.getSession();
			String merCode = session.getAttribute("MER_CODE").toString();
			merchantInfoBean =  Util.merchantInfoMap.get(merCode);
			Map<Integer, String> drawMap = FixtureWiseWinningAnalysisReportControllerImpl.getInstance().getDrawData(merchantInfoBean.getMerchantId(), gameId, gameTypeId, selectedDate);
			out.print(new Gson().toJson(drawMap));
			return;
		} catch (SLEException se) {
			logger.info("ErrorCode:"+se.getErrorCode()+" ErrorMessage:"+se.getErrorMessage());
			out.print(new Gson().toJson(se.getErrorCode() +"#"+ se.getErrorMessage()));
			return;
		} catch (Exception se) {
			logger.info("ErrorCode:"+SLEErrors.GENERAL_EXCEPTION_ERROR_CODE+" ErrorMessage:"+SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			return;
		} finally {
			out.flush();
			out.close();
		}
	}

	public String reportSearch() {
		try {
			reportMap = FixtureWiseWinningAnalysisReportControllerImpl.getInstance().getFixtureWinningData(gameId, gameTypeId, drawId);
		} catch (SLEException se) {
			logger.info("ErrorCode:" + se.getErrorCode() + " ErrorMessage:"+ se.getErrorMessage());
			request.setAttribute("SLE_EXCEPTION", se.getErrorMessage());
			return "applicationAjaxException";
		} catch (Exception ex) {
			logger.info("ErrorCode:"+SLEErrors.GENERAL_EXCEPTION_ERROR_CODE+" ErrorMessage:"+SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			request.setAttribute("SLE_EXCEPTION", SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			return "applicationAjaxException";
		}

		return SUCCESS;
	}
}