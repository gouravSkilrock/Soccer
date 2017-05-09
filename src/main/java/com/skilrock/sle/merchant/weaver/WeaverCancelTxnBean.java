package com.skilrock.sle.merchant.weaver;

public class WeaverCancelTxnBean {

	
	private int errorCode;
	private long playerId;
	private String txnId;
	private String refWagerTxnId;
	private double totalBal;
	private double realBal;
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	public long getPlayerId() {
		return playerId;
	}
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public String getTxnId() {
		return txnId;
	}
	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}
	public String getRefWagerTxnId() {
		return refWagerTxnId;
	}
	public void setRefWagerTxnId(String refWagerTxnId) {
		this.refWagerTxnId = refWagerTxnId;
	}
	public double getTotalBal() {
		return totalBal;
	}
	public void setTotalBal(double totalBal) {
		this.totalBal = totalBal;
	}
	public double getRealBal() {
		return realBal;
	}
	public void setRealBal(double realBal) {
		this.realBal = realBal;
	}
}
