package com.skilrock.sle.common;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.javaBeans.UserInfoBean;
import com.skilrock.sle.merchant.common.javaBeans.MerchantInfoBean;

public class AuditLogInterceptor extends AbstractInterceptor {
	private static final SLELogger logger = SLELogger.getLogger(AbstractInterceptor.class.getName());

	private static final long serialVersionUID = 1L;

	private String serviceName;
	private String interfaceType;

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getInterfaceType() {
		return interfaceType;
	}

	public void setInterfaceType(String interfaceType) {
		this.interfaceType = interfaceType;
	}

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();

		request.setAttribute("AUDIT_ID", Util.getAuditTrailId());
		MerchantInfoBean merchantInfoBean = null;
		JsonObject respObj = null;
		String userName = null;

		PrintWriter out = null;
		if ("MOBILE".equals(interfaceType)) {
			JsonObject reqObject = new JsonParser().parse(request.getParameter("requestData")).getAsJsonObject();
			merchantInfoBean = Util.merchantInfoMap.get(reqObject.get("merchantCode").getAsString());
			if(merchantInfoBean == null) {
				out = response.getWriter();
				respObj = new JsonObject();
				respObj.addProperty("errorCode", SLEErrors.MERCHANT_NOT_AVAILABLE_ERROR_CODE);
				respObj.addProperty("errorMsg", SLEErrors.MERCHANT_NOT_AVAILABLE_ERROR_MESSAGE);
				respObj.addProperty("isSuccess", false);
				out.print(respObj);
				out.flush();
				return null;
			}
			userName = reqObject.get("playerName").getAsString();
//			merchantId = MerchantAuthorizationHelper.merchantAuthorization(reqObject.get("merchantUserName").getAsString(), reqObject.get("merchantPwd").getAsString());
			request.setAttribute("merchantId", merchantInfoBean.getMerchantId());
			request.setAttribute("serviceId", merchantInfoBean.getServiceId());
			StringBuilder requestStr = new StringBuilder("REQUEST_AUDIT_TRAIL-" + request.getAttribute("AUDIT_ID"));
			requestStr.append("#userName=").append(userName);
			requestStr.append("#IPAddress=").append(request.getRemoteAddr());
			requestStr.append("#Entry_Time=").append(Util.getCurrentTimeString());
			requestStr.append("#ActionName=").append(invocation.getInvocationContext().getName());
			requestStr.append("#serviceName=").append(serviceName);
			requestStr.append("#interfaceType=").append(interfaceType);
			logger.info(requestStr.toString());
			invocation.invoke();
			StringBuilder responseStr = new StringBuilder("RESPONSE_AUDIT_TRAIL-" + request.getAttribute("AUDIT_ID"));
			responseStr.append("#userName=").append(userName);
			responseStr.append("#Exit_Time=").append(Util.getCurrentTimeString());
			responseStr.append("#ActionName=").append(invocation.getInvocationContext().getName());
			responseStr.append("#serviceName=").append(serviceName);
			logger.info(responseStr.toString());
		} else if ("TERMINAL".equals(interfaceType)) {
//			merchantId = MerchantAuthorizationHelper.merchantAuthorization((String) request.getParameter("merchantUserName"), 
//					(String) request.getParameter("merchantPwd"));
			merchantInfoBean = Util.merchantInfoMap.get(request.getParameter("merCode"));
			if(merchantInfoBean == null) {
				out = response.getWriter();
				out.print("Error! Your are Not Authorized To Access Sports Lottery");
				out.flush();
				return null;
			}
			
			request.setAttribute("merchantId", merchantInfoBean.getMerchantId());
			request.setAttribute("serviceId", merchantInfoBean.getServiceId());
			StringBuilder requestStr = new StringBuilder("REQUEST_AUDIT_TRAIL-" + request.getAttribute("AUDIT_ID"));
			requestStr.append("#userName=").append(userName);
			requestStr.append("#IPAddress=").append(request.getRemoteAddr());
			requestStr.append("#Entry_Time=").append(Util.getCurrentTimeString());
			requestStr.append("#ActionName=").append(invocation.getInvocationContext().getName());
			requestStr.append("#serviceName=").append(serviceName);
			requestStr.append("#interfaceType=").append(interfaceType);
			requestStr.append("#SystemDetail=").append((String) request.getHeader("User-Agent"));
			logger.info(requestStr.toString());
			invocation.invoke();
			StringBuilder responseStr = new StringBuilder("RESPONSE_AUDIT_TRAIL-" + request.getAttribute("AUDIT_ID"));
			responseStr.append("#userName=").append(userName);
			responseStr.append("#Exit_Time=").append(Util.getCurrentTimeString());
			responseStr.append("#ActionName=").append(invocation.getInvocationContext().getName());
			responseStr.append("#serviceName=").append(serviceName);
			logger.info(responseStr.toString());
		} else if ("WEB".equals(interfaceType)) {
			UserInfoBean userInfoBean = (UserInfoBean)request.getSession().getAttribute("USER_INFO");
			if (userInfoBean != null)
				userName = userInfoBean.getUserName();
			else
				userName = request.getParameter("userName");

			StringBuilder requestStr = new StringBuilder("REQUEST_AUDIT_TRAIL-" + request.getAttribute("AUDIT_ID"));
			requestStr.append("#userName=").append(userName);
			requestStr.append("#IPAddress=").append(request.getRemoteAddr());
			requestStr.append("#Entry_Time=").append(Util.getCurrentTimeString());
			requestStr.append("#ActionName=").append(invocation.getInvocationContext().getName());
			requestStr.append("#serviceName=").append(serviceName);
			requestStr.append("#interfaceType=").append(interfaceType);
			requestStr.append("#SystemDetail=").append((String) request.getHeader("User-Agent"));
			logger.info(requestStr.toString());
			invocation.invoke();
			StringBuilder responseStr = new StringBuilder("RESPONSE_AUDIT_TRAIL-" + request.getAttribute("AUDIT_ID"));
			responseStr.append("#userName=").append(userName);
			responseStr.append("#Exit_Time=").append(Util.getCurrentTimeString());
			responseStr.append("#ActionName=").append(invocation.getInvocationContext().getName());
			responseStr.append("#serviceName=").append(serviceName);
			logger.info(responseStr.toString());
		}
		return null;
	}

}
