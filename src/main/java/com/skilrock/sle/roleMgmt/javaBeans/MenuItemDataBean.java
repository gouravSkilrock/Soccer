package com.skilrock.sle.roleMgmt.javaBeans;

import java.io.Serializable;
import java.util.List;

public class MenuItemDataBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private int menuItemId;
	private String menuItemName;
	private Boolean isAssign;
	private List<MenuItemActionDataBean> actionItems;
	
	public MenuItemDataBean() {
	}

	public int getMenuItemId() {
		return menuItemId;
	}

	public void setMenuItemId(int menuItemId) {
		this.menuItemId = menuItemId;
	}

	public String getMenuItemName() {
		return menuItemName;
	}

	public void setMenuItemName(String menuItemName) {
		this.menuItemName = menuItemName;
	}

	

	public Boolean getIsAssign() {
		return isAssign;
	}

	public void setIsAssign(Boolean isAssign) {
		this.isAssign = isAssign;
	}

	public List<MenuItemActionDataBean> getActionItems() {
		return actionItems;
	}

	public void setActionItems(List<MenuItemActionDataBean> actionItems) {
		this.actionItems = actionItems;
	}
}