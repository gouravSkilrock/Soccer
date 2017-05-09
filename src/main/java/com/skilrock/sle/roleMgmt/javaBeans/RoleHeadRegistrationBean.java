package com.skilrock.sle.roleMgmt.javaBeans;

public class RoleHeadRegistrationBean {

	private int roleId;
	private int userId;
	private int creatorUserId;
	private String userName;
	private String userType;
	private String firstName;
	private String lastName;
	private String emailId;
	private String mobileNbr;
	private int userMappingId;
	private String mobileNubmer;
	private String requestIp;

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getCreatorUserId() {
		return creatorUserId;
	}

	public void setCreatorUserId(int creatorUserId) {
		this.creatorUserId = creatorUserId;
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

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getMobileNbr() {
		return mobileNbr;
	}

	public void setMobileNbr(String mobileNbr) {
		this.mobileNbr = mobileNbr;
	}

	public void setUserMappingId(int userMappingId) {
		this.userMappingId = userMappingId;
	}

	public int getUserMappingId() {
		return userMappingId;
	}

	public void setMobileNubmer(String mobileNubmer) {
		this.mobileNubmer = mobileNubmer;
	}

	public String getMobileNubmer() {
		return mobileNubmer;
	}

	public String getRequestIp() {
		return requestIp;
	}

	public void setRequestIp(String requestIp) {
		this.requestIp = requestIp;
	}
}