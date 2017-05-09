package com.skilrock.sle.embedded.gameDataMgmt.action;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.common.javaBeans.UserInfoBean;
import com.skilrock.sle.commonMethod.serviceImpl.CommonMethodsServiceImpl;
import com.skilrock.sle.embedded.common.BaseActionTerminal;
import com.skilrock.sle.embedded.common.SportsLotteryResponseData;
import com.skilrock.sle.gameDataMgmt.controllerImpl.GameDataControllerImpl;
import com.skilrock.sle.gameDataMgmt.javaBeans.EventMasterBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.GameMasterBean;
import com.skilrock.sle.merchant.common.javaBeans.MerchantInfoBean;

public class SportsLotteryGameDataAction extends BaseActionTerminal {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public SportsLotteryGameDataAction() {
		super(SportsLotteryGameDataAction.class.getName());
	}

	private String listType;
	private String fromDate;
	private String toDate;
	private int gameId;
	private int gameTypeId;
	private String gameTypeIdList;
	


	public String getListType() {
		return listType;
	}

	public void setListType(String listType) {
		this.listType = listType;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	
	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public int getGameId() {
		return gameId;
	}
	
	public int getGameTypeId() {
		return gameTypeId;
	}

	public void setGameTypeId(int gameTypeId) {
		this.gameTypeId = gameTypeId;
	}
	
	public String getGameTypeIdList() {
		return gameTypeIdList;
	}

	public void setGameTypeIdList(String gameTypeIdList) {
		this.gameTypeIdList = gameTypeIdList;
	}

	public void fetchSLEDrawData() {
		List<GameMasterBean> gameMasterList = null;
		String responseString = null;
		MerchantInfoBean merchantInfoBean = null;
		UserInfoBean userInfoBean = null;
	
		try {
			// merchantId = (Integer) request.getAttribute("merchantId");

			merchantInfoBean = Util.merchantInfoMap.get(getMerCode());

			userInfoBean = new UserInfoBean();
			userInfoBean.setUserName(getUserName());
			userInfoBean.setMerchantId(merchantInfoBean.getMerchantId());
			userInfoBean.setUserSessionId(getSessId());
			userInfoBean.setMerchantDevName(getMerCode());

			//UnComment when authentication is needed with Merchant.
			CommonMethodsServiceImpl.getInstance().fetchAndCheckUserData(userInfoBean);

			logger.debug("Merchant User Info Bean is "+userInfoBean);
			
			CommonMethodsServiceImpl.getInstance().checkAndAutoCancel(getSlLstTxnId(), "CANCEL_MISMATCH", userInfoBean);
			if(gameTypeIdList == null || gameTypeIdList.trim().isEmpty()){
				gameMasterList = GameDataControllerImpl.getInstance().getSportsLotteryGameData(merchantInfoBean.getMerchantId());				
			}else{
				String[] gameTypeIdArr=gameTypeIdList.split(",");
				gameMasterList = GameDataControllerImpl.getInstance().getSportsLotteryGameDataGameTypeWise(merchantInfoBean,gameId, gameTypeIdArr);
			}
			responseString = SportsLotteryResponseData.generateDrawGameData(gameMasterList,userInfoBean.getMerchantUserId(),merchantInfoBean.getMerchantId());
			response.getOutputStream().write(responseString.getBytes());
		} catch (SLEException e) {
			try {
				if(e.getErrorCode()==10012 || e.getErrorCode()==118 ){
					response.getOutputStream().write(("ErrorMsg:" + SLEErrors.INVALID_SESSION_MOBILE_ERROR_MESSAGE+"|ErrorCode:01|").getBytes());
				}else{
					response.getOutputStream().write(("ErrorMsg:" + e.getErrorMessage()).getBytes());
				}
				
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return;
		} catch (IOException e) {
			e.printStackTrace();
			try {
				response.getOutputStream().write("ErrorMsg:Error!Try Again".getBytes());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return;
		} catch (Exception e) {
			e.printStackTrace();
			try {
				response.getOutputStream().write("ErrorMsg:Error!Try Again".getBytes());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return;
		}
	}
	
	public void fetchSLEMatchListData() {
		Map<Integer, List<EventMasterBean>> matchDataDayWise = null;
		String responseString = null;
		MerchantInfoBean merchantInfoBean = null;
		UserInfoBean userInfoBean = null;
		try {
			merchantInfoBean = Util.merchantInfoMap.get(getMerCode());
			userInfoBean = new UserInfoBean();
			String uName = null;
			if (getUserName() == null) {
				uName = getPlayerName();
			} else {
				uName = getUserName();
			}
			userInfoBean.setUserName(uName);
			userInfoBean.setMerchantId(merchantInfoBean.getMerchantId());
			userInfoBean.setUserSessionId(getSessId());
			userInfoBean.setMerchantDevName(getMerCode());
			
			CommonMethodsServiceImpl.getInstance().fetchAndCheckUserData(userInfoBean);

			logger.debug("Merchant User Info Bean is " + userInfoBean);

			CommonMethodsServiceImpl.getInstance().checkAndAutoCancel(getSlLstTxnId(), "CANCEL_MISMATCH", userInfoBean);

			if (gameId <= 0) {
				throw new SLEException(SLEErrors.MATCH_NOT_AVAILABLE_ERROR_CODE,SLEErrors.MATCH_NOT_AVAILABLE_ERROR_MESSAGE);
			}
            StringBuilder drawInfo=new StringBuilder();
			int matchListCount = GameDataControllerImpl.getInstance().getMatchListCount(uName);
			if (matchListCount < Integer.parseInt(Util.getPropertyValue("MATCH_LIST_PRINT_COUNT_LIMIT"))) {
				matchDataDayWise =GameDataControllerImpl.getInstance().getMatchDataDrawWise(gameId,gameTypeId,merchantInfoBean.getMerchantId(),listType,drawInfo);
				responseString = SportsLotteryResponseData.prepareSLEMatchListDataDayWise(matchDataDayWise,merchantInfoBean.getMerchantId(),gameTypeId,drawInfo);
				response.getOutputStream().write(responseString.getBytes());
				GameDataControllerImpl.getInstance().updateMatchListCount(uName);
			} else {
				throw new SLEException(SLEErrors.MATCH_LIST_PRINT_LIMIT_EXCEEDED_ERROR_CODE,SLEErrors.MATCH_LIST_PRINT_LIMIT_EXCEEDED_ERROR_MESSAGE);
			}
		} catch (SLEException e) {
			try {
				if(e.getErrorCode()!=null && (e.getErrorCode()==10012 || e.getErrorCode()==118 )){
					response.getOutputStream().write(("ErrorMsg:" + SLEErrors.INVALID_SESSION_MOBILE_ERROR_MESSAGE+"|ErrorCode:01|").getBytes());
				}else{
					response.getOutputStream().write(("ErrorMsg:" + e.getErrorMessage()).getBytes());
				}
				
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return;
		} catch (IOException e) {
			e.printStackTrace();
			try {
				response.getOutputStream().write("ErrorMsg:Error!Try Again".getBytes());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return;
		} catch (Exception e) {
			e.printStackTrace();
			try {
				response.getOutputStream().write("ErrorMsg:Error!Try Again".getBytes());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return;
		}
	}

}
