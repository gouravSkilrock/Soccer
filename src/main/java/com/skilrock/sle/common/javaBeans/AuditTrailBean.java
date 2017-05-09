package com.skilrock.sle.common.javaBeans;

public class AuditTrailBean {

	private String loginName;
	private String userName;
	private String accessIp;
	private String accessTime;
	private String activity;

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAccessIp() {
		return accessIp;
	}

	public void setAccessIp(String accessIp) {
		this.accessIp = accessIp;
	}

	public String getAccessTime() {
		return accessTime;
	}

	public void setAccessTime(String accessTime) {
		this.accessTime = accessTime;
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	@Override
	public String toString() {
		return "AuditTrailBean [loginName=" + loginName + ", userName="
				+ userName + ", accessIp=" + accessIp + ", accessTime="
				+ accessTime + ", activity=" + activity + "]";
	}

}
