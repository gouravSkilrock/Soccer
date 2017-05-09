package com.skilrock.sle.web.merchantUser.reportsMgmt.Action;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.embedded.common.BaseActionWeb;
import com.skilrock.sle.reportsMgmt.controllerImpl.MerchantWiseSalePwtReportControllerImpl;
import com.skilrock.sle.reportsMgmt.javaBeans.MerchantWiseSalePwtReportBean;
import com.skilrock.sle.reportsMgmt.javaBeans.MerchantWiseSalePwtReportBean.GameDetailsBean;

public class MerchantWiseSalePwtReportAction extends BaseActionWeb {
	private static final long serialVersionUID = 1L;

	public MerchantWiseSalePwtReportAction() {
		super(MerchantWiseSalePwtReportAction.class.getName());
	}

	private List<GameDetailsBean> gameList;
	private String startDate;
	private String endDate;
	private Map<String, List<MerchantWiseSalePwtReportBean>> reportMap;

	public List<GameDetailsBean> getGameList() {
		return gameList;
	}

	public void setGameList(List<GameDetailsBean> gameList) {
		this.gameList = gameList;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public Map<String, List<MerchantWiseSalePwtReportBean>> getReportMap() {
		return reportMap;
	}

	public void setReportMap(Map<String, List<MerchantWiseSalePwtReportBean>> reportMap) {
		this.reportMap = reportMap;
	}

	public String reportMenu() {
		return SUCCESS;
	}

	public String reportSearch() {
		SimpleDateFormat simpleDateFormat = null;
		try {
			gameList = new ArrayList<GameDetailsBean>();

			simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
			Timestamp startTime = ("".equals(startDate)) ? null : new Timestamp(simpleDateFormat.parse(startDate).getTime());
			Timestamp endTime = ("".equals(endDate)) ? null : new Timestamp(simpleDateFormat.parse(endDate).getTime()+(24*60*60*1000-1000));
			reportMap = MerchantWiseSalePwtReportControllerImpl.getInstance().getSalePwtData(gameList, startTime, endTime);
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