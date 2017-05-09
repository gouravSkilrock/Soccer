package com.skilrock.sle.tp.rest.common.javaBeans;

public class TpUserLoginNotifyBean {
	private String sessionId;
	private int userId;
	private String userName;

	// For SLE Internal User Only
	private int merchantId;

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(int merchantId) {
		this.merchantId = merchantId;
	}

	@Override
	public String toString() {
		return "TpUserLoginNotifyBean [sessionId=" + sessionId + ", userId="
				+ userId + ", userName=" + userName + ", merchantId="
				+ merchantId + "]";
	}

}
