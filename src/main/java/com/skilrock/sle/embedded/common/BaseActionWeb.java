package com.skilrock.sle.embedded.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.sle.common.SLELogger;

public class BaseActionWeb extends ActionSupport implements ServletRequestAware, ServletResponseAware {
	private static final long serialVersionUID = 1L;

	public static SLELogger logger;

	private String userName;
	private String sessId;
	private String slLstTxnId;
	private int merchantId;
	private String merCode;

	protected HttpServletRequest request;
	protected HttpServletResponse response;

	public BaseActionWeb() {
	}

	public BaseActionWeb(String className) {
		logger = SLELogger.getLogger(className);
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSessId() {
		return sessId;
	}

	public void setSessId(String sessId) {
		this.sessId = sessId;
	}

	public String getSlLstTxnId() {
		return slLstTxnId;
	}

	public void setSlLstTxnId(String slLstTxnId) {
		this.slLstTxnId = slLstTxnId;
	}

	public int getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(int merchantId) {
		this.merchantId = merchantId;
	}

	public String getMerCode() {
		return merCode;
	}

	public void setMerCode(String merCode) {
		this.merCode = merCode;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	@Override
	public String toString() {
		return "BaseActionTerminal [merCode=" + merCode + ", merchantId="
				+ merchantId + ", request=" + request + ", response="
				+ response + ", sessId=" + sessId + ", slLstTxnId="
				+ slLstTxnId + ", userName=" + userName + "]";
	}

}
