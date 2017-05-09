package com.skilrock.sle.roleMgmt.javaBeans;

import java.util.List;
import java.util.Map;

public class PriviledgeModificationMasterBean {
	private String username;
	private String registerDate;
	private String registerBy;
	private String emailId;
	private List<PriviledgeModificationHeaderBean> headerList;
	private Map<String, Map<String, List<PriviledgeModificationDataBean>>> serviceMap;

	public PriviledgeModificationMasterBean() {
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(String registerDate) {
		this.registerDate = registerDate;
	}

	public String getRegisterBy() {
		return registerBy;
	}

	public void setRegisterBy(String registerBy) {
		this.registerBy = registerBy;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public List<PriviledgeModificationHeaderBean> getHeaderList() {
		return headerList;
	}

	public void setHeaderList(List<PriviledgeModificationHeaderBean> headerList) {
		this.headerList = headerList;
	}

	public Map<String, Map<String, List<PriviledgeModificationDataBean>>> getServiceMap() {
		return serviceMap;
	}

	public void setServiceMap(Map<String, Map<String, List<PriviledgeModificationDataBean>>> serviceMap) {
		this.serviceMap = serviceMap;
	}
}