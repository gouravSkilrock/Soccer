package com.skilrock.sle.common.javaBeans;

import java.io.Serializable;

public class MerchantBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private int merchantId;
	private String merchantName;
	private String registrationDate;
	private String merchantIP;
	private String merchantUsername;
	private String merchantPassword;
	private String status;

	public MerchantBean() {
	}

	public int getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(int merchantId) {
		this.merchantId = merchantId;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(String registrationDate) {
		this.registrationDate = registrationDate;
	}

	public String getMerchantIP() {
		return merchantIP;
	}

	public void setMerchantIP(String merchantIP) {
		this.merchantIP = merchantIP;
	}

	public String getMerchantUsername() {
		return merchantUsername;
	}

	public void setMerchantUsername(String merchantUsername) {
		this.merchantUsername = merchantUsername;
	}

	public String getMerchantPassword() {
		return merchantPassword;
	}

	public void setMerchantPassword(String merchantPassword) {
		this.merchantPassword = merchantPassword;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "MerchantBean [merchantIP=" + merchantIP + ", merchantId="
				+ merchantId + ", merchantName=" + merchantName
				+ ", merchantPassword=" + merchantPassword
				+ ", merchantUsername=" + merchantUsername
				+ ", registrationDate=" + registrationDate + ", status="
				+ status + "]";
	}
}