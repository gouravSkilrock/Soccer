package com.skilrock.sle.merchant.common.javaBeans;

import java.io.Serializable;

public class TPRequestBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private String userName;
	private String userSession;
	private Object requestData;
	private String serviceCode;
	private String interfaceType;

	public TPRequestBean() {
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserSession() {
		return userSession;
	}

	public void setUserSession(String userSession) {
		this.userSession = userSession;
	}

	public Object getRequestData() {
		return requestData;
	}

	public void setRequestData(Object requestData) {
		this.requestData = requestData;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getInterfaceType() {
		return interfaceType;
	}

	public void setInterfaceType(String interfaceType) {
		this.interfaceType = interfaceType;
	}
}