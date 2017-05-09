package com.skilrock.sle.common.javaBeans;

public class UserInfoBean {
	private long sleUserId;
	private int merchantUserId;
	private int merchantUserMappingId;
	private int merchantId;
	private String merchantDevName;
	private String dbSessionId;
	private String userSessionId;
	private String userName;
	private String userType;
	private String firstName;
	private String lastName;
	private String address;
	private String mobileNbr;
	private String gender;
	private String emailId;
	private String city;
	private String state;
	private String country;
	private double balance;
	private String orgName;
	private String userId;
	private String tokenId;
	private String parentOrgName;
	private String channelName;
	private String userAgent;
	
	// For TonyBet
	private String callerId;
	private String callerPassword;

	public String getCallerId() {
		return callerId;
	}

	public void setCallerId(String callerId) {
		this.callerId = callerId;
	}

	public String getCallerPassword() {
		return callerPassword;
	}

	public void setCallerPassword(String callerPassword) {
		this.callerPassword = callerPassword;
	}

	public String getParentOrgName() {
		return parentOrgName;
	}

	public void setParentOrgName(String parentOrgName) {
		this.parentOrgName = parentOrgName;
	}

	public long getSleUserId() {
		return sleUserId;
	}

	public void setSleUserId(long sleUserId) {
		this.sleUserId = sleUserId;
	}

	public int getMerchantUserId() {
		return merchantUserId;
	}

	public void setMerchantUserId(int merchantUserId) {
		this.merchantUserId = merchantUserId;
	}

	public int getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(int merchantId) {
		this.merchantId = merchantId;
	}

	public int getMerchantUserMappingId() {
		return merchantUserMappingId;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public void setMerchantUserMappingId(int merchantUserMappingId) {
		this.merchantUserMappingId = merchantUserMappingId;
	}

	public String getMerchantDevName() {
		return merchantDevName;
	}

	public void setMerchantDevName(String merchantDevName) {
		this.merchantDevName = merchantDevName;
	}

	public String getDbSessionId() {
		return dbSessionId;
	}

	public void setDbSessionId(String dbSessionId) {
		this.dbSessionId = dbSessionId;
	}

	public String getUserSessionId() {
		return userSessionId;
	}

	public void setUserSessionId(String userSessionId) {
		this.userSessionId = userSessionId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getMobileNbr() {
		return mobileNbr;
	}

	public void setMobileNbr(String mobileNbr) {
		this.mobileNbr = mobileNbr;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
	this.tokenId = tokenId;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	@Override
	public String toString() {
		return "UserInfoBean [sleUserId=" + sleUserId + ", merchantUserId=" + merchantUserId
				+ ", merchantUserMappingId=" + merchantUserMappingId + ", merchantId=" + merchantId
				+ ", merchantDevName=" + merchantDevName + ", dbSessionId=" + dbSessionId + ", userSessionId="
				+ userSessionId + ", userName=" + userName + ", userType=" + userType + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", address=" + address + ", mobileNbr=" + mobileNbr + ", gender=" + gender
				+ ", emailId=" + emailId + ", city=" + city + ", state=" + state + ", country=" + country + ", balance="
				+ balance + ", orgName=" + orgName + ", userId=" + userId + ", tokenId=" + tokenId + ", parentOrgName="
				+ parentOrgName + ", callerId=" + callerId + ",callerPassword=" + callerPassword + "]";
	}


	

	

	

}
