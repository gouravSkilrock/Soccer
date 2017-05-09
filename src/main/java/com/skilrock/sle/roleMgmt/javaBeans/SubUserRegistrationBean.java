package com.skilrock.sle.roleMgmt.javaBeans;

import java.util.List;

public class SubUserRegistrationBean {

	private int roleId;
	private int creatorUserId;
	private int userId;
	private String firstName;
	private String lastName;
	private String userName;
	private String userType;
	private String mobileNbr;
	private String emailId;
	private String requestIp;
	private List<PrivilegeDataBean> privilegeList;
	private int userMappingId;

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public int getCreatorUserId() {
		return creatorUserId;
	}

	public void setCreatorUserId(int creatorUserId) {
		this.creatorUserId = creatorUserId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
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

	public String getRequestIp() {
		return requestIp;
	}

	public void setRequestIp(String requestIp) {
		this.requestIp = requestIp;
	}

	public List<PrivilegeDataBean> getPrivilegeList() {
		return privilegeList;
	}

	public void setPrivilegeList(List<PrivilegeDataBean> privilegeList) {
		this.privilegeList = privilegeList;
	}

	public void setUserMappingId(int userMappingId) {
		this.userMappingId = userMappingId;
	}

	public int getUserMappingId() {
		return userMappingId;
	}
}