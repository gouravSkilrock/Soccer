package com.skilrock.sle.merchant.common.javaBeans;

public class MerchantInfoBean {
	private int merchantId;
	private String merchantName;
	private String merchantDevName;
	private int serviceId;
	private String merchantIp;
	private String merchantUserName;
	private String merchantPwd;
	private String protocol;
	private String projectName;
	private String port;
	private String password;
	private String userName;

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

	public String getMerchantDevName() {
		return merchantDevName;
	}

	public void setMerchantDevName(String merchantDevName) {
		this.merchantDevName = merchantDevName;
	}

	public int getServiceId() {
		return serviceId;
	}

	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}

	public String getMerchantIp() {
		return merchantIp;
	}

	public void setMerchantIp(String merchantIp) {
		this.merchantIp = merchantIp;
	}

	public String getMerchantUserName() {
		return merchantUserName;
	}

	public void setMerchantUserName(String merchantUserName) {
		this.merchantUserName = merchantUserName;
	}

	public String getMerchantPwd() {
		return merchantPwd;
	}

	public void setMerchantPwd(String merchantPwd) {
		this.merchantPwd = merchantPwd;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	
	
	@Override
	public String toString() {
		return "MerchantInfoBean [merchantId=" + merchantId + ", merchantName="
				+ merchantName + ", merchantDevName=" + merchantDevName
				+ ", serviceId=" + serviceId + ", merchantIp=" + merchantIp
				+ ", merchantUserName=" + merchantUserName + ", merchantPwd="
				+ merchantPwd + ", protocol=" + protocol + ", projectName="
				+ projectName + ", port=" + port + ", password=" + password
				+ ", userName=" + userName + "]";
	}

}
