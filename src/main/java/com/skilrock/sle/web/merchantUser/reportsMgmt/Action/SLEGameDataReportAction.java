package com.skilrock.sle.web.merchantUser.reportsMgmt.Action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.commonMethod.serviceImpl.CommonMethodsServiceImpl;
import com.skilrock.sle.embedded.common.BaseActionWeb;
import com.skilrock.sle.gameDataMgmt.javaBeans.GameMasterBean;
import com.skilrock.sle.merchant.common.javaBeans.MerchantInfoBean;
import com.skilrock.sle.reportsMgmt.controllerImpl.GameDataReportControllerImpl;
import com.skilrock.sle.reportsMgmt.javaBeans.GameDataReportBean;
import com.skilrock.sle.reportsMgmt.javaBeans.SaleTrendDataBean;

public class SLEGameDataReportAction extends BaseActionWeb {
	public SLEGameDataReportAction() {
		super(SLEGameDataReportAction.class.getName());
	}
	private static final long serialVersionUID = 1L;

	private Map<Integer, GameMasterBean> gameMap;
	private int gameId;
	private int gameTypeId;
	private int drawId;
	private String startDate;
	private String endDate;
	private String reportType;
	private List<GameDataReportBean> gameDataReportList;
	private List<GameDataReportBean> saleReportDataList;
	private List<SaleTrendDataBean> saleTrendDataList;
	private String reportData;


	/* Menu Data*/
	public String gameDataReportMenu() {
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
	
	/*Search Report Data*/
	
	public String gameDataReportSearch() {
		HttpSession session = null;
		GameDataReportControllerImpl controllerImpl = null;
		try {
			session = request.getSession();
			String merCode = session.getAttribute("MER_CODE").toString();
			controllerImpl = new GameDataReportControllerImpl();
			gameDataReportList = controllerImpl.getGameDataReport(gameId, gameTypeId, startDate, endDate, reportType, merCode);
		} catch (SLEException se) {
			logger.info("ErrorCode:" + se.getErrorCode() + " ErrorMessage:"+ se.getErrorMessage());
			request.setAttribute("SLE_EXCEPTION", se.getErrorMessage());
			return "applicationException";
		}catch(Exception e){
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public String fetchDrawWiseTicketInfo() {
		HttpSession session = null;
		try {
			session = request.getSession();
			String merCode = session.getAttribute("MER_CODE").toString();
			int merchantId = Util.merchantInfoMap.get(merCode).getMerchantId();
			gameDataReportList = GameDataReportControllerImpl.getInstance().fetchDrawWiseTicketInfo(merchantId, gameId, gameTypeId, drawId);
		} catch (SLEException se) {
			logger.info("ErrorCode:" + se.getErrorCode() + " ErrorMessage:"+ se.getErrorMessage());
			request.setAttribute("SLE_EXCEPTION", se.getErrorMessage());
			return "applicationException";
		}catch(Exception e){
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String getSaleTrendData() {
		HttpSession session = null;
		try {
			session = request.getSession();
			String merCode = session.getAttribute("MER_CODE").toString();
			saleTrendDataList = GameDataReportControllerImpl.getInstance().getDrawWiseSaleTrendData(gameId, gameTypeId, startDate, endDate, reportType, merCode); 
		} catch (SLEException se) {
			logger.info("ErrorCode:" + se.getErrorCode() + " ErrorMessage:"+ se.getErrorMessage());
			request.setAttribute("SLE_EXCEPTION", se.getErrorMessage());
			return "applicationException";
		}catch(Exception e){
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	
/*Search Sale Report Data*/
	
	public String saleReportSearch() {
		HttpSession session = null;
		try {
			session = request.getSession();
			String merCode = session.getAttribute("MER_CODE").toString();
			saleReportDataList = GameDataReportControllerImpl.getInstance().getSaleReportData(gameId, gameTypeId, startDate, endDate,merCode);
		} catch (SLEException se) {
			logger.info("ErrorCode:" + se.getErrorCode() + " ErrorMessage:"+ se.getErrorMessage());
			request.setAttribute("SLE_EXCEPTION", se.getErrorMessage());
			return "applicationException";
		}catch(Exception e){
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public void exportToExcel() throws IOException {
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment; filename=Sale_Trend_Report.xls");
		PrintWriter out = response.getWriter();
		reportData = reportData.replaceAll("<tbody>", "").replaceAll("</tbody>", "").trim();
		out.write(reportData);
	}
	
	public String saleTrendReportMenu() {
		return SUCCESS;
	}
	
	public String saleTrendReportSearch() {
		return SUCCESS;
	}

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

	public int getDrawId() {
		return drawId;
	}

	public void setDrawId(int drawId) {
		this.drawId = drawId;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public void setGameDataReportList(List<GameDataReportBean> gameDataReportList) {
		this.gameDataReportList = gameDataReportList;
	}

	public List<GameDataReportBean> getGameDataReportList() {
		return gameDataReportList;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setSaleTrendDataList(List<SaleTrendDataBean> saleTrendDataList) {
		this.saleTrendDataList = saleTrendDataList;
	}

	public List<SaleTrendDataBean> getSaleTrendDataList() {
		return saleTrendDataList;
	}
	

	public String getReportData() {
		return reportData;
	}

	public void setReportData(String reportData) {
		this.reportData = reportData;
	}

	public List<GameDataReportBean> getSaleReportDataList() {
		return saleReportDataList;
	}

	public void setSaleReportDataList(List<GameDataReportBean> saleReportDataList) {
		this.saleReportDataList = saleReportDataList;
	}
}