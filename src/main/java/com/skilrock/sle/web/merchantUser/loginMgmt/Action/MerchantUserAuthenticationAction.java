package com.skilrock.sle.web.merchantUser.loginMgmt.Action;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.common.javaBeans.UserInfoBean;
import com.skilrock.sle.commonMethod.serviceImpl.CommonMethodsServiceImpl;
import com.skilrock.sle.embedded.common.BaseActionWeb;
import com.skilrock.sle.merchant.common.javaBeans.MerchantInfoBean;
import com.skilrock.sle.roleMgmt.controllerImpl.CreateMerchantUserMenuDataControllerImpl;
import com.skilrock.sle.roleMgmt.javaBeans.MenuDataBean;

public class MerchantUserAuthenticationAction extends BaseActionWeb{

	private static final long serialVersionUID = 1L;
	private String userType;
	private String parentOrgName;

	public MerchantUserAuthenticationAction() {
		super(MerchantUserAuthenticationAction.class.getName());
	}

	public String authenticateMerchantUser() throws SLEException {
		MerchantInfoBean merchantInfoBean = null;
		UserInfoBean userInfoBean = null;
		HttpSession session = null;
		try {
			session = request.getSession();
			if(userType != null && userType.equalsIgnoreCase("RETAILER")){
				session.setMaxInactiveInterval(28800);
			} else{
				session.setMaxInactiveInterval(3600);
			}
			
			/*If sessionId is not sent in requested SLE url.*/
			if(getSessId().equals("")){
				logger.info("Session Id is not sent in requested url !!");
				throw new SLEException(SLEErrors.INVALID_SESSION_ERROR_CODE, SLEErrors.INVALID_SESSION_ERROR_MESSAGE);
			}
			merchantInfoBean =  Util.merchantInfoMap.get(getMerCode());
			
			/* Creating UserInfo Bean */
			userInfoBean = new UserInfoBean();
			userInfoBean.setUserName(getUserName());			
			userInfoBean.setMerchantDevName(getMerCode());
			userInfoBean.setMerchantId(merchantInfoBean.getMerchantId());
			userInfoBean.setUserSessionId(getSessId());
			userInfoBean.setParentOrgName(getParentOrgName());
			//System.out.println(getSessId());
			
			/* Validating Sessions */
			CommonMethodsServiceImpl.getInstance().fetchAndCheckUserData(userInfoBean);			
			if(!(userInfoBean.getUserSessionId().equals(getSessId()) || userInfoBean.getDbSessionId().equals(getSessId()))){
				logger.info("session expired at merchant("+getMerCode()+") side !!");
				throw new SLEException(SLEErrors.INVALID_SESSION_ERROR_CODE, SLEErrors.INVALID_SESSION_ERROR_MESSAGE);
			}
			session.setAttribute("USER_INFO", userInfoBean);
			session.setAttribute("MER_CODE", getMerCode());
			if(userType != null && userType.equalsIgnoreCase("RETAILER")){
				return "play";
			}
			/* Fetching Privilege/Menu Data for requested user. */
			List<MenuDataBean> menuDataBeanList = new CreateMerchantUserMenuDataControllerImpl().getMenuDataForMerchantUser(merchantInfoBean, userInfoBean, "WEB");
			if(menuDataBeanList.isEmpty()){
				logger.info("No SLE priviledge assigned to this user !!");
				throw new SLEException(SLEErrors.NO_PRIV_ASSIGNED_ERROR_CODE, SLEErrors.NO_PRIV_ASSIGNED_ERROR_MESSAGE);
			}
			 
			/* Setting Privilege/Menu & User Data in session. */
			String menuData = new Gson().toJson(menuDataBeanList);				
			session.setAttribute("MENU_DATA", menuData);
			
			
		} catch (SLEException pe) {
			pe.printStackTrace();
			logger.info("ErrorCode:"+pe.getErrorCode()+" ErrorMessage:"+pe.getErrorMessage());
			if(pe.getErrorCode() == 10013){
				request.setAttribute("SLE_EXCEPTION",pe.getErrorMessage());
				return "msg";
			}
        	request.setAttribute("SLE_EXCEPTION",pe.getErrorMessage());
    		return "error";
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("ErrorCode:"+SLEErrors.GENERAL_EXCEPTION_ERROR_CODE+" ErrorMessage:"+SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			request.setAttribute("SLE_EXCEPTION",SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
	    	return "error";
		}
		return SUCCESS;	
	}
	public String getParentOrgName() {
		return parentOrgName;
	}

	public void setParentOrgName(String parentOrgName) {
		this.parentOrgName = parentOrgName;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}
	
}