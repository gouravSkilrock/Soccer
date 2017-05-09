package com.skilrock.sle.gameDataMgmt.javaBeans;

import java.io.Serializable;
import java.util.List;

public class ResultUserAssignBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private int merchantId;
	private int gameId;
	private int gameTypeId;
	private List<Integer> userIds;

	public ResultUserAssignBean() {
	}

	public int getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(int merchantId) {
		this.merchantId = merchantId;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public int getGameTypeId() {
		return gameTypeId;
	}

	public void setGameTypeId(int gameTypeId) {
		this.gameTypeId = gameTypeId;
	}

	public List<Integer> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<Integer> userIds) {
		this.userIds = userIds;
	}
}