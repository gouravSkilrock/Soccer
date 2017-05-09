package com.skilrock.sle.mobile.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.sle.common.SLELogger;

public class BaseActionMobile extends ActionSupport implements ServletRequestAware, ServletResponseAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static SLELogger logger;

	protected HttpServletRequest request;
	protected HttpServletResponse response;

	private String requestData;
	
	public BaseActionMobile(Class<?> clazz) {
		logger = SLELogger.getLogger(clazz.getName());
	}

	public BaseActionMobile() {
		// TODO Auto-generated constructor stub
	}

	public BaseActionMobile(String className) {
		logger = SLELogger.getLogger(className);
	}

	public String getRequestData() {
		return requestData;
	}

	public void setRequestData(String requestData) {
		this.requestData = requestData;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

}
