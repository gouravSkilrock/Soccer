package com.skilrock.sle.merchant.weaver;

import java.io.Serializable;

public class WeaverCommonResponseDataBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private int errorCode;
	private String errorMsg;
	private String response;

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	@Override
	public String toString() {
		return "WeaverCommonResponseDataBean [errorCode=" + errorCode
				+ ", errorMsg=" + errorMsg + ", response=" + response + "]";
	}
	
}
