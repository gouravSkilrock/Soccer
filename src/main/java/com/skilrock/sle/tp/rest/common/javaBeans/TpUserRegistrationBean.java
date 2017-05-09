package com.skilrock.sle.tp.rest.common.javaBeans;

public class TpUserRegistrationBean {
	private String firstName;
	private String lastName;
	private String gender;
	private String address;
	private String mobileNbr;
	private String emailId;
	private String userName;
	private int userId;
	private int userMappingId;
	private String userType;
	private String sessionId;
	private String city;
	private String state;
	private String country;
	private long creatorUserId;
	private long slLastTxnId;
	private String orgName;
	// For Internal Use, Will not be Received by Merchant
	private int merchantId;
	private long sleUserId;
	// Ends
	private String currency;	// For TonyBet

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

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
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

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getUserMappingId() {
		return userMappingId;
	}

	public void setUserMappingId(int userMappingId) {
		this.userMappingId = userMappingId;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
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

	public int getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(int merchantId) {
		this.merchantId = merchantId;
	}

	public long getSleUserId() {
		return sleUserId;
	}

	public void setSleUserId(long sleUserId) {
		this.sleUserId = sleUserId;
	}

	public long getSlLastTxnId() {
		return slLastTxnId;
	}

	public void setSlLastTxnId(long slLastTxnId) {
		this.slLastTxnId = slLastTxnId;
	}

	
	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	@Override
	public String toString() {
		return "TpUserRegistrationBean [address=" + address + ", city=" + city
				+ ", country=" + country + ", creatorUserId=" + creatorUserId
				+ ", emailId=" + emailId + ", firstName=" + firstName
				+ ", gender=" + gender + ", lastName=" + lastName
				+ ", merchantId=" + merchantId + ", mobileNbr=" + mobileNbr
				+ ", orgName=" + orgName + ", sessionId=" + sessionId
				+ ", slLastTxnId=" + slLastTxnId + ", sleUserId=" + sleUserId
				+ ", state=" + state + ", userId=" + userId
				+ ", userMappingId=" + userMappingId + ", userName=" + userName
				+ ", userType=" + userType + ", currency=" + currency + "]";
	}

	public void setCreatorUserId(long creatorUserId) {
		this.creatorUserId = creatorUserId;
	}

	public long getCreatorUserId() {
		return creatorUserId;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	
}
