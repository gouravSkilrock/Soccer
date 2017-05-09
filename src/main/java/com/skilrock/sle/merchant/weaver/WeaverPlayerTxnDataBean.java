package com.skilrock.sle.merchant.weaver;

import java.io.Serializable;

public class WeaverPlayerTxnDataBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private String playerId;
	private String walletType;
	private String balanceType;
	private String currencyCode;
	private String amount;
	private String aliasName;
	private String gameId;
	private String gameName;
	private String device;
	private String isWithdrawable;
	private String particular;
	private String refWagerTxnId;
	private String refTxnNo;

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public String getWalletType() {
		return walletType;
	}

	public void setWalletType(String walletType) {
		this.walletType = walletType;
	}

	public String getBalanceType() {
		return balanceType;
	}

	public void setBalanceType(String balanceType) {
		this.balanceType = balanceType;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getAliasName() {
		return aliasName;
	}

	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
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

	public String getIsWithdrawable() {
		return isWithdrawable;
	}

	public void setIsWithdrawable(String isWithdrawable) {
		this.isWithdrawable = isWithdrawable;
	}

	public String getParticular() {
		return particular;
	}

	public void setParticular(String particular) {
		this.particular = particular;
	}

	public String getRefWagerTxnId() {
		return refWagerTxnId;
	}

	public void setRefWagerTxnId(String refWagerTxnId) {
		this.refWagerTxnId = refWagerTxnId;
	}

	public String getRefTxnNo() {
		return refTxnNo;
	}

	public void setRefTxnNo(String refTxnNo) {
		this.refTxnNo = refTxnNo;
	}
    @Override
    public String toString() {
    	
    	return String.format("refWagerTxnId:"+refWagerTxnId+"playerId:"+playerId);
    }
}
