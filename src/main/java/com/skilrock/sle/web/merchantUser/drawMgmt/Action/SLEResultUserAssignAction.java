package com.skilrock.sle.web.merchantUser.drawMgmt.Action;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.commonMethod.serviceImpl.CommonMethodsServiceImpl;
import com.skilrock.sle.drawMgmt.controllerImpl.ResultSubmissionControllerImpl;
import com.skilrock.sle.embedded.common.BaseActionWeb;
import com.skilrock.sle.gameDataMgmt.javaBeans.GameMasterBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.ResultUserAssignBean;
import com.skilrock.sle.merchant.common.javaBeans.MerchantInfoBean;

public class SLEResultUserAssignAction extends BaseActionWeb {
	private static final long serialVersionUID = 1L;

	public SLEResultUserAssignAction() {
		super(SLEResultUserAssignAction.class.getName());
	}

	private int gameId;
	private int gameTypeId;
	private Map<Integer, GameMasterBean> gameMap;
	private Map<Integer, String> userMap;
	private String userListString;

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

	public Map<Integer, GameMasterBean> getGameMap() {
		return gameMap;
	}

	public void setGameMap(Map<Integer, GameMasterBean> gameMap) {
		this.gameMap = gameMap;
	}

	public Map<Integer, String> getUserMap() {
		return userMap;
	}

	public void setUserMap(Map<Integer, String> userMap) {
		this.userMap = userMap;
	}

	public String getUserListString() {
		return userListString;
	}

	public void setUserListString(String userListString) {
		this.userListString = userListString;
	}

	public String userAssignMenu() {
		HttpSession session = null;
		ResultSubmissionControllerImpl controllerImpl = new ResultSubmissionControllerImpl();
		try {
			session = request.getSession();
			String merCode = session.getAttribute("MER_CODE").toString();
			MerchantInfoBean merchantBean =  Util.merchantInfoMap.get(merCode);

			gameMap = CommonMethodsServiceImpl.getInstance().getGameMap(merchantBean);
			userMap = controllerImpl.userAssignMenu(merchantBean.getMerchantId());
		} catch (SLEException se) {
			logger.info("ErrorCode:" + se.getErrorCode() + " ErrorMessage:"+ se.getErrorMessage());
			request.setAttribute("SLE_EXCEPTION", se.getErrorMessage());
			return "applicationException";
		}

		return SUCCESS;
	}

	public void getAssignedUser() {
		HttpSession session = null;
		ResultSubmissionControllerImpl controllerImpl = new ResultSubmissionControllerImpl();
		PrintWriter out = null;
		try {
			session = request.getSession();
			String merCode = session.getAttribute("MER_CODE").toString();
			MerchantInfoBean merchantBean =  Util.merchantInfoMap.get(merCode);

			List<Integer> userList = controllerImpl.getAssignedUser(merchantBean.getMerchantId(), gameId, gameTypeId);

			out = response.getWriter();
			out.print(new Gson().toJson(userList));
			return;
		} catch (SLEException se) {
			logger.info("ErrorCode:"+se.getErrorCode()+" ErrorMessage:"+se.getErrorMessage());
			return;
		} catch (Exception se) {
			logger.info("ErrorCode:"+SLEErrors.GENERAL_EXCEPTION_ERROR_CODE+" ErrorMessage:"+SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			return;
		} finally {
			out.flush();
			out.close();
		}
	}

	public String userAssignSubmit() {
		HttpSession session = null;
		ResultSubmissionControllerImpl controllerImpl = new ResultSubmissionControllerImpl();
		
		try {
			session = request.getSession();
			String merCode = session.getAttribute("MER_CODE").toString();
			MerchantInfoBean merchantInfoBean =  Util.merchantInfoMap.get(merCode);

			ResultUserAssignBean assignBean = new ResultUserAssignBean();
			List<Integer> userIds = new ArrayList<Integer>();
			assignBean.setMerchantId(merchantInfoBean.getMerchantId());
			assignBean.setGameId(gameId);
			assignBean.setGameTypeId(gameTypeId);
			assignBean.setUserIds(userIds);
			for(String userList : userListString.split(",")) {
				userIds.add(Integer.parseInt(userList.trim()));
			}

			controllerImpl.resultSubmissionUserAssign(assignBean);
		} catch (SLEException se) {
			logger.info("ErrorCode:" + se.getErrorCode() + " ErrorMessage:"+ se.getErrorMessage());
			request.setAttribute("SLE_EXCEPTION", se.getErrorMessage());
			return "applicationException";
		}

		return SUCCESS;
	}
}