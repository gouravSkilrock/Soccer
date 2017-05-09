package com.skilrock.sle.interceptor.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.skilrock.sle.common.SLELogger;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.common.javaBeans.UserInfoBean;
import com.skilrock.sle.commonMethod.serviceImpl.CommonMethodsServiceImpl;

public class SessionValidateInterceptor extends AbstractInterceptor{

	private static final long serialVersionUID = 1L;
	private static final SLELogger logger = SLELogger.getLogger(SessionValidateInterceptor.class.getName());

	public String intercept(ActionInvocation invocation){
	
		String actionName = ActionContext.getContext().getName();
		logger.info("Starting session interceptor:" + actionName);
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		UserInfoBean userInfoBean = (UserInfoBean) session.getAttribute("USER_INFO");
		//if(!SportsLotteryUtils.isUserExist(userInfoBean.getMerchantId()+"_"+userInfoBean.getUserName())){
		try{
			if(userInfoBean!= null){
				CommonMethodsServiceImpl.getInstance().userSessionValidation(userInfoBean);
				invocation.invoke();
			}else{
				throw new SLEException(SLEErrors.INVALID_SESSION_ERROR_CODE,SLEErrors.INVALID_SESSION_ERROR_MESSAGE);
			}
			
		}catch(SLEException se){
			request.setAttribute("SLE_EXCEPTION", se.getErrorMessage());
			return "invalid";
		}catch(Exception e){
			request.setAttribute("SLE_EXCEPTION", e.getMessage());
			return "invalid";
		}
		return null;
	}
}