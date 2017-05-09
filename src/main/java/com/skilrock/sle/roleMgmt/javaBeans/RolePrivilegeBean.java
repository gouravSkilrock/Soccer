package com.skilrock.sle.roleMgmt.javaBeans;

import java.util.List;

public class RolePrivilegeBean {

	private int roleId;
	private String roleName;
	private int userId;
	private String roleDescription;
	private int creatorUserId;
	private String tierType;
	private String requestIp;
	private List<PrivilegeDataBean> privilegeList;

	public RolePrivilegeBean() {
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public List<PrivilegeDataBean> getPrivilegeList() {
		return privilegeList;
	}

	public void setPrivilegeList(List<PrivilegeDataBean> privilegeList) {
		this.privilegeList = privilegeList;
	}

	public String getRoleDescription() {
		return roleDescription;
	}

	public void setRoleDescription(String roleDescription) {
		this.roleDescription = roleDescription;
	}

	public String getTierType() {
		return tierType;
	}

	public void setTierType(String tierType) {
		this.tierType = tierType;
	}

	public int getCreatorUserId() {
		return creatorUserId;
	}

	public void setCreatorUserId(int creatorUserId) {
		this.creatorUserId = creatorUserId;
	}

	public String getRequestIp() {
		return requestIp;
	}

	public void setRequestIp(String requestIp) {
		this.requestIp = requestIp;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
}