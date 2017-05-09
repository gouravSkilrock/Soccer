package com.skilrock.sle.gameDataMgmt.javaBeans;

import java.io.Serializable;
import java.util.Map;

public class ResultApprovalBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private int merchantId;
	private int gameId;
	private int gameTypeId;
	private int drawId;
	private String gameTypeDispName;
	private String gameDispName;
	private String drawName;
	private String drawDate;
	private String startDate;
	private String endDate;
	private int userId1;
	private String userName1;
	private String userUpdateTime1;
	private int userId2;
	private String userName2;
	private String userUpdateTime2;
	private Map<Integer, ResultApprovalEventBean> userResult;
	private Map<String,String> optionCodeMap;

	public ResultApprovalBean() {
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

	public int getDrawId() {
		return drawId;
	}

	public void setDrawId(int drawId) {
		this.drawId = drawId;
	}

	public String getDrawName() {
		return drawName;
	}

	public void setDrawName(String drawName) {
		this.drawName = drawName;
	}

	public String getDrawDate() {
		return drawDate;
	}

	public void setDrawDate(String drawDate) {
		this.drawDate = drawDate;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public int getUserId1() {
		return userId1;
	}

	public void setUserId1(int userId1) {
		this.userId1 = userId1;
	}

	public String getUserName1() {
		return userName1;
	}

	public void setUserName1(String userName1) {
		this.userName1 = userName1;
	}

	public String getUserUpdateTime1() {
		return userUpdateTime1;
	}

	public void setUserUpdateTime1(String userUpdateTime1) {
		this.userUpdateTime1 = userUpdateTime1;
	}

	public int getUserId2() {
		return userId2;
	}

	public void setUserId2(int userId2) {
		this.userId2 = userId2;
	}

	public String getUserName2() {
		return userName2;
	}

	public void setUserName2(String userName2) {
		this.userName2 = userName2;
	}

	public String getUserUpdateTime2() {
		return userUpdateTime2;
	}

	public void setUserUpdateTime2(String userUpdateTime2) {
		this.userUpdateTime2 = userUpdateTime2;
	}

	public Map<Integer, ResultApprovalEventBean> getUserResult() {
		return userResult;
	}

	public void setUserResult(Map<Integer, ResultApprovalEventBean> userResult) {
		this.userResult = userResult;
	}

	public Map<String, String> getOptionCodeMap() {
		return optionCodeMap;
	}

	public void setOptionCodeMap(Map<String, String> optionCodeMap) {
		this.optionCodeMap = optionCodeMap;
	}

	public String getGameTypeDispName() {
		return gameTypeDispName;
	}

	public void setGameTypeDispName(String gameTypeDispName) {
		this.gameTypeDispName = gameTypeDispName;
	}

	public String getGameDispName() {
		return gameDispName;
	}

	public void setGameDispName(String gameDispName) {
		this.gameDispName = gameDispName;
	}
}