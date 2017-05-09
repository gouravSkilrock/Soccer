package com.skilrock.sle.web.merchantUser.drawMgmt.Action;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.common.javaBeans.UserInfoBean;
import com.skilrock.sle.commonMethod.serviceImpl.CommonMethodsServiceImpl;
import com.skilrock.sle.dataMgmt.javaBeans.SimnetResultDrawsBean;
import com.skilrock.sle.drawMgmt.controllerImpl.ResultSubmissionControllerImpl;
import com.skilrock.sle.embedded.common.BaseActionWeb;
import com.skilrock.sle.gameDataMgmt.controllerImpl.DrawEventMappingControllerImpl;
import com.skilrock.sle.gameDataMgmt.javaBeans.DrawEventResultBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.DrawMasterBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.GameMasterBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.ResultApprovalBean;
import com.skilrock.sle.merchant.common.javaBeans.MerchantInfoBean;

public class SLEResultSubmissionAction extends BaseActionWeb {
	private static final long serialVersionUID = 1L;

	public SLEResultSubmissionAction() {
		super(SLEResultSubmissionAction.class.getName());
	}

	private Map<Integer, GameMasterBean> gameMap;
	private int gameId;
	private int gameTypeId;
	private int drawId;
	private String startDate;
	private String endDate;
	private List<ResultApprovalBean> approvalList;
	private ResultApprovalBean approvalBean;
	private String eventResult;
	private List<DrawMasterBean> drawMasterList;
	private String status;

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

	public List<ResultApprovalBean> getApprovalList() {
		return approvalList;
	}

	public void setApprovalList(List<ResultApprovalBean> approvalList) {
		this.approvalList = approvalList;
	}

	public ResultApprovalBean getApprovalBean() {
		return approvalBean;
	}

	public void setApprovalBean(ResultApprovalBean approvalBean) {
		this.approvalBean = approvalBean;
	}

	public String getEventResult() {
		return eventResult;
	}

	public void setEventResult(String eventResult) {
		this.eventResult = eventResult;
	}

	public List<DrawMasterBean> getDrawMasterList() {
		return drawMasterList;
	}

	public void setDrawMasterList(List<DrawMasterBean> drawMasterList) {
		this.drawMasterList = drawMasterList;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String resultSubmissionMenu() {
		HttpSession session = null;
		try {
			session = request.getSession();
			String merCode = session.getAttribute("MER_CODE").toString();
			MerchantInfoBean merchantBean = Util.merchantInfoMap.get(merCode);

			gameMap = CommonMethodsServiceImpl.getInstance().getGameMap(merchantBean);
		} catch (SLEException se) {
			logger.info("ErrorCode:" + se.getErrorCode() + " ErrorMessage:"+ se.getErrorMessage());
			request.setAttribute("SLE_EXCEPTION", se.getErrorMessage());
			return "applicationException";
		}
		if("GHANA".equalsIgnoreCase(Util.getPropertyValue("COUNTRY_DEPLOYED"))){
			return "GHANARESULTSUBMIT";
		}

		return SUCCESS;
	}

	public String resultSubmissionDrawEventSearch() throws Exception {
		HttpSession session = null;
		ResultSubmissionControllerImpl controllerImpl = new ResultSubmissionControllerImpl();
		try {
			session = request.getSession();
			String merCode = session.getAttribute("MER_CODE").toString();
			MerchantInfoBean merchantBean =  Util.merchantInfoMap.get(merCode);
			UserInfoBean userBean = (UserInfoBean) session.getAttribute("USER_INFO");

			drawMasterList = controllerImpl.resultSubmissionDrawData(gameId, gameTypeId, merchantBean.getMerchantId(), userBean.getMerchantUserId());
		} catch (SLEException se) {
			logger.info("ErrorCode:" + se.getErrorCode() + " ErrorMessage:"+ se.getErrorMessage());
			request.setAttribute("SLE_EXCEPTION", se.getErrorMessage());
			return "applicationAjaxException";
		}
		if("GHANA".equalsIgnoreCase(Util.getPropertyValue("COUNTRY_DEPLOYED"))){
			return "GHANARESULTSUBMIT";
		}
        
		return SUCCESS;
	}

	public String resultSubmissionSubmit() throws Exception {
		HttpSession session = null;
		Map<Integer, String> eventOptionResult = new TreeMap<Integer, String>();
		DrawEventResultBean drawEventResultBean = new DrawEventResultBean();
		ResultSubmissionControllerImpl controllerImpl = new ResultSubmissionControllerImpl();
		try {
			session = request.getSession();
			UserInfoBean userBean = (UserInfoBean) session.getAttribute("USER_INFO");

			drawEventResultBean.setUserId(userBean.getMerchantUserId());
			drawEventResultBean.setGameId(gameId);
			drawEventResultBean.setGameTypeId(gameTypeId);
			drawEventResultBean.setDrawId(drawId);
			for(String result : eventResult.split(",")) {
				eventOptionResult.put(Integer.parseInt(result.split("_")[0]), result.split("_")[1]);
			}
			drawEventResultBean.setEventOptionResult(eventOptionResult);

			status = controllerImpl.sportsLotteryResultSubmission(drawEventResultBean);
			logger.info("Result Status - "+status);
		} catch (SLEException se) {
			logger.info("ErrorCode:" + se.getErrorCode() + " ErrorMessage:"+ se.getErrorMessage());
			request.setAttribute("SLE_EXCEPTION", se.getErrorMessage());
			if("GHANA".equalsIgnoreCase(Util.getPropertyValue("COUNTRY_DEPLOYED"))){
				return "applicationExceptionGhana";
			}
			else{
				return "applicationException";
			}
		}
		if("GHANA".equalsIgnoreCase(Util.getPropertyValue("COUNTRY_DEPLOYED"))){
			return "GHANARESULTSUBMIT";
		}

		return SUCCESS;
	}
	
	public void SimnetResultDraws()
	{	
		String resp=null; 
		HttpSession session = null;
		List<SimnetResultDrawsBean> simnetResultDrawsList=null;
		PrintWriter out=null;
		try {
			session=request.getSession();
			String merCode = session.getAttribute("MER_CODE").toString();
			MerchantInfoBean merchantBean =  Util.merchantInfoMap.get(merCode);
			DrawEventMappingControllerImpl controllerImpl = new DrawEventMappingControllerImpl();
			simnetResultDrawsList=controllerImpl.getSimnetResultDraws(gameId, gameTypeId, merchantBean.getMerchantId());
			Gson gson =new Gson();
			resp=gson.toJson(simnetResultDrawsList);
			out = response.getWriter();
			out.print(resp);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			out.flush();
			out.close();
		}
	}
}