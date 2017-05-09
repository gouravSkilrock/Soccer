package com.skilrock.sle.web.merchantUser.drawMgmt.Action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.common.javaBeans.UserInfoBean;
import com.skilrock.sle.commonMethod.serviceImpl.CommonMethodsServiceImpl;
import com.skilrock.sle.drawMgmt.controllerImpl.DrawMgmtControllerImpl;
import com.skilrock.sle.drawMgmt.javaBeans.DrawInfoBean;
import com.skilrock.sle.embedded.common.BaseActionWeb;
import com.skilrock.sle.gameDataMgmt.javaBeans.GameMasterBean;
import com.skilrock.sle.merchant.common.javaBeans.MerchantInfoBean;

public class SLEDrawMgmtAction extends BaseActionWeb {
	private static final long serialVersionUID = 1L;

	public SLEDrawMgmtAction() {
		super(SLEDrawMgmtAction.class.getName());
	}

	private Map<Integer, GameMasterBean> gameMap;
	private int gameId;
	private int gameTypeId;
	private int drawId;
	private String startDate;
	private String endDate;
	List<DrawInfoBean> drawInfoList;
	DrawInfoBean drawInfo;
	private boolean logoutAllRet;
	private int seconds;
	private String drawStatus;
	

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

	public Map<Integer, GameMasterBean> getGameMap() {
		return gameMap;
	}

	public void setGameMap(Map<Integer, GameMasterBean> gameMap) {
		this.gameMap = gameMap;
	}


	public List<DrawInfoBean> getDrawInfoList() {
		return drawInfoList;
	}

	public void setDrawInfoList(List<DrawInfoBean> drawInfoList) {
		this.drawInfoList = drawInfoList;
	}
	

	public DrawInfoBean getDrawInfo() {
		return drawInfo;
	}

	public void setDrawInfo(DrawInfoBean drawInfo) {
		this.drawInfo = drawInfo;
	}


	public boolean isLogoutAllRet() {
		return logoutAllRet;
	}

	public void setLogoutAllRet(boolean logoutAllRet) {
		this.logoutAllRet = logoutAllRet;
	}
	public int getSeconds() {
		return seconds;
	}
	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}

	public void setDrawStatus(String drawStatus) {
		this.drawStatus = drawStatus;
	}

	public String getDrawStatus() {
		return drawStatus;
	}

	public String fetchGameDataMenu() {
		HttpSession session = null;
		MerchantInfoBean merchantInfoBean = null;
		try {
			session = request.getSession();
			String merCode = session.getAttribute("MER_CODE").toString();
			merchantInfoBean =  Util.merchantInfoMap.get(merCode);
			gameMap = CommonMethodsServiceImpl.getInstance().getGameMap(merchantInfoBean);
			logger.info("game Map"+gameMap.toString());
		} catch (SLEException se) {
			logger.info("ErrorCode:" + se.getErrorCode() + " ErrorMessage:"+ se.getErrorMessage());
			request.setAttribute("SLE_EXCEPTION", se.getErrorMessage());
			return "applicationException";
		}

		return SUCCESS;
	}
	
	public String fetchDrawList() {
		HttpSession session = null;
		MerchantInfoBean merchantInfoBean = null;
		try {
			session = request.getSession();
			String merCode = session.getAttribute("MER_CODE").toString();
			merchantInfoBean =  Util.merchantInfoMap.get(merCode);
			drawInfoList=DrawMgmtControllerImpl.getInstance().getSportsLotteryDrawDetails(gameId, gameTypeId, startDate, endDate, merchantInfoBean.getMerchantId());
			logger.info("draw Info List"+drawInfoList.toString());
		} catch (SLEException se) {
			logger.info("ErrorCode:" + se.getErrorCode() + " ErrorMessage:"+ se.getErrorMessage());
			request.setAttribute("SLE_EXCEPTION", se.getErrorMessage());
			return "applicationAjaxException";
		}

		return SUCCESS;
	}
	
	public String fetchDrawDetail() {
		HttpSession session = null;
		MerchantInfoBean merchantInfoBean = null;
		try {
			session = request.getSession();
			String merCode = session.getAttribute("MER_CODE").toString();
			merchantInfoBean =  Util.merchantInfoMap.get(merCode);
			drawInfo=DrawMgmtControllerImpl.getInstance().fetchDrawInfo(gameId, gameTypeId,drawId,merchantInfoBean.getMerchantId());
			logger.info("draw Info "+drawInfo.toString());
		} catch (SLEException se) {
			logger.info("ErrorCode:" + se.getErrorCode() + " ErrorMessage:"+ se.getErrorMessage());
			request.setAttribute("SLE_EXCEPTION", se.getErrorMessage());
			return "applicationException";
		}

		return SUCCESS;
	}
	
	public String updateDrawInfo(){
		HttpSession session = null;
		MerchantInfoBean merchantInfoBean = null;
		UserInfoBean userInfoBean=null;
		try {
			session = request.getSession();
			String merCode = session.getAttribute("MER_CODE").toString();
			merchantInfoBean =  Util.merchantInfoMap.get(merCode);
			userInfoBean = (UserInfoBean)session.getAttribute("USER_INFO");
			drawInfo=DrawMgmtControllerImpl.getInstance().updateDrawInfo(gameId, gameTypeId,drawId,drawStatus,merchantInfoBean,logoutAllRet,seconds,userInfoBean);
			logger.info("Updated draw Info "+drawInfo.toString());
		} catch (SLEException se) {
			logger.info("ErrorCode:" + se.getErrorCode() + " ErrorMessage:"+ se.getErrorMessage());
			request.setAttribute("SLE_EXCEPTION", se.getErrorMessage());
			return "applicationException";
		}

		return SUCCESS;
	}
	
	public String updateDrawSaleTime(){
		HttpSession session = null;
		MerchantInfoBean merchantInfoBean = null;
		UserInfoBean userInfoBean=null;
		try {
			session = request.getSession();
			String merCode = session.getAttribute("MER_CODE").toString();
			merchantInfoBean =  Util.merchantInfoMap.get(merCode);
			userInfoBean = (UserInfoBean)session.getAttribute("USER_INFO");
			drawInfo=DrawMgmtControllerImpl.getInstance().updateDrawSaleTime(gameId, gameTypeId,drawId,drawStatus,merchantInfoBean,logoutAllRet,seconds,userInfoBean);
			logger.info("Updated draw Info "+drawInfo.toString());
		} catch (SLEException se) {
			logger.info("ErrorCode:" + se.getErrorCode() + " ErrorMessage:"+ se.getErrorMessage());
			request.setAttribute("SLE_EXCEPTION", se.getErrorMessage());
			return "applicationException";
		}

		return SUCCESS;
	}
}