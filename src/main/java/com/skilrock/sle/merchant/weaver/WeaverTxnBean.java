package com.skilrock.sle.merchant.weaver;

public class WeaverTxnBean {

	private double amount;
	private long txnId;
	private String refTxnNo;
	private String balanceType;
	private String walletType;
	private String gameDevName;
	private int gameId;
	private double wrContriAmount;
	
	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public long getTxnId() {
		return txnId;
	}

	public void setTxnId(long txnId) {
		this.txnId = txnId;
	}

	public String getRefTxnNo() {
		return refTxnNo;
	}

	public void setRefTxnNo(String refTxnNo) {
		this.refTxnNo = refTxnNo;
	}

	public String getBalanceType() {
		return balanceType;
	}

	public void setBalanceType(String balanceType) {
		this.balanceType = balanceType;
	}

	public String getWalletType() {
		return walletType;
	}

	public void setWalletType(String walletType) {
		this.walletType = walletType;
	}

	public String getGameDevName() {
		return gameDevName;
	}

	public void setGameDevName(String gameDevName) {
		this.gameDevName = gameDevName;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public double getWrContriAmount() {
		return wrContriAmount;
	}

	public void setWrContriAmount(double wrContriAmount) {
		this.wrContriAmount = wrContriAmount;
	}
  
}
