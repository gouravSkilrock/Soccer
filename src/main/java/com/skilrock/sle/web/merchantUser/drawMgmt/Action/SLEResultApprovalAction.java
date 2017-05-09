package com.skilrock.sle.web.merchantUser.drawMgmt.Action;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpSession;

import com.skilrock.sle.common.SportsLotteryUtils;
import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.commonMethod.serviceImpl.CommonMethodsServiceImpl;
import com.skilrock.sle.drawMgmt.controllerImpl.ResultSubmissionControllerImpl;
import com.skilrock.sle.embedded.common.BaseActionWeb;
import com.skilrock.sle.gameDataMgmt.javaBeans.DrawEventResultBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.GameMasterBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.ResultApprovalBean;
import com.skilrock.sle.merchant.common.javaBeans.MerchantInfoBean;

public class SLEResultApprovalAction extends BaseActionWeb {
	private static final long serialVersionUID = 1L;

	public SLEResultApprovalAction() {
		super(SLEResultApprovalAction.class.getName());
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
	private int userResult;

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

	public int getUserResult() {
		return userResult;
	}

	public void setUserResult(int userResult) {
		this.userResult = userResult;
	}

	public String resultApprovalMenu() {
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

	public String getUnmatchedDraws() throws Exception {
		HttpSession session = null;
		ResultSubmissionControllerImpl controllerImpl = new ResultSubmissionControllerImpl();
		ResultApprovalBean approvalBean = new ResultApprovalBean();
		try {
			session = request.getSession();
			String merCode = session.getAttribute("MER_CODE").toString();
			MerchantInfoBean merchantInfoBean = Util.merchantInfoMap.get(merCode);

			approvalBean.setMerchantId(merchantInfoBean.getMerchantId());
			approvalBean.setGameId(gameId);
			approvalBean.setGameTypeId(gameTypeId);
			approvalBean.setDrawId(drawId);
			approvalBean.setStartDate(startDate);
			approvalBean.setEndDate(endDate);
			approvalList = controllerImpl.getUnmatchedDraws(approvalBean);
			if(approvalList.isEmpty()){
				request.setAttribute("SLE_EXCEPTION", "No Records Found !!");
				return "applicationAjaxException";
			}
		} catch (SLEException se) {
			logger.info("ErrorCode:" + se.getErrorCode() + " ErrorMessage:"+ se.getErrorMessage());
			request.setAttribute("SLE_EXCEPTION", se.getErrorMessage());
			return "applicationAjaxException";
		}

		return SUCCESS;
	}

	public String getUnmatchedDrawDetails() throws Exception {
		ResultSubmissionControllerImpl controllerImpl = new ResultSubmissionControllerImpl();
		HttpSession session = null;
		try {
			session = request.getSession();
			String merCode = session.getAttribute("MER_CODE").toString();
			MerchantInfoBean merchantInfoBean =  Util.merchantInfoMap.get(merCode);

			approvalBean = new ResultApprovalBean();
			approvalBean.setMerchantId(merchantInfoBean.getMerchantId());
			approvalBean.setGameId(gameId);
			approvalBean.setGameTypeId(gameTypeId);
			approvalBean.setDrawId(drawId);
			approvalBean = controllerImpl.getUnmatchedDrawDetails(approvalBean);
			approvalBean.setGameTypeDispName(SportsLotteryUtils.gameTypeInfoMerchantMap.get(merCode).get(gameTypeId).getGameTypeDispName());
			approvalBean.setGameDispName(SportsLotteryUtils.gameInfoMerchantMap.get(merCode).get(gameId).getGameDispName());
		} catch (SLEException se) {
			logger.info("ErrorCode:" + se.getErrorCode() + " ErrorMessage:"+ se.getErrorMessage());
			request.setAttribute("SLE_EXCEPTION", se.getErrorMessage());
			return "applicationAjaxException";
		}

		return SUCCESS;
	}

	public String resultApprovalSubmit() throws Exception {
		ResultSubmissionControllerImpl controllerImpl = new ResultSubmissionControllerImpl();
		try {
			DrawEventResultBean resultBean = new DrawEventResultBean();
			int userId = 1001;
			resultBean.setUserId(userId);
			resultBean.setGameId(gameId);
			resultBean.setGameTypeId(gameTypeId);
			resultBean.setDrawId(drawId);
			resultBean.setUserResult(userResult);

			if(userResult == 3) {
				Map<Integer, String> eventOptionResult = new TreeMap<Integer, String>();
				for(String result : eventResult.split(",")) {
					eventOptionResult.put(Integer.parseInt(result.split("_")[0]), result.split("_")[1]);
				}
				resultBean.setEventOptionResult(eventOptionResult);
			}

			controllerImpl.resultApproval(resultBean);
		} catch (SLEException se) {
			logger.info("ErrorCode:" + se.getErrorCode() + " ErrorMessage:"+ se.getErrorMessage());
			request.setAttribute("SLE_EXCEPTION", se.getErrorMessage());
			return "applicationAjaxException";
		}

		System.out.println(userResult);
		return SUCCESS;
	}
}