package com.skilrock.sle.merchant.weaver;

import java.io.Serializable;
import java.util.List;

public class WeaverGamePlayBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private String sessionId;
	private long playerId;
	private String aliasName;
	private int gameId;
	private String gameType;
	private String gameName;
	private String device;
	private String userAgent;
	private String particular;
	private double rake;
	private double wrContriAmount;
	private List<WeaverTxnBean> transactionInfoList;

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public String getAliasName() {
		return aliasName;
	}

	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public String getGameType() {
		return gameType;
	}

	public void setGameType(String gameType) {
		this.gameType = gameType;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getParticular() {
		return particular;
	}

	public void setParticular(String particular) {
		this.particular = particular;
	}

	public double getRake() {
		return rake;
	}

	public void setRake(double rake) {
		this.rake = rake;
	}

	public double getWrContriAmount() {
		return wrContriAmount;
	}

	public void setWrContriAmount(double wrContriAmount) {
		this.wrContriAmount = wrContriAmount;
	}

	public List<WeaverTxnBean> getTransactionInfoList() {
		return transactionInfoList;
	}

	public void setTransactionInfoList(List<WeaverTxnBean> transactionInfoList) {
		this.transactionInfoList = transactionInfoList;
	}

}
