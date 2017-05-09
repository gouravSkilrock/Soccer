package com.skilrock.sle.roleMgmt.javaBeans;

import java.io.Serializable;

public class MenuItemActionDataBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private int actionId;
	private String actionName;
	private Boolean isAssign;

	public MenuItemActionDataBean() {
	}

	public int getActionId() {
		return actionId;
	}

	public void setActionId(int actionId) {
		this.actionId = actionId;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public Boolean getIsAssign() {
		return isAssign;
	}

	public void setIsAssign(Boolean isAssign) {
		this.isAssign = isAssign;
	}

	

	
}