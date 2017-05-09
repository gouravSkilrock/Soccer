package com.skilrock.sle.merchant.tonybet;

import com.google.gson.annotations.SerializedName;

public class TonyBetGamePlayResponseBean {
	@SerializedName("ResultCode") 
	private int resultCode;
	@SerializedName("Balance")
	private double balance;
	@SerializedName("TransactionId")
	private int transactionId;
	
	private long sleTranactionId;
	
	public int getResultCode() {
		return resultCode;
	}
	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public int getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}
	public long getSleTranactionId() {
		return sleTranactionId;
	}
	public void setSleTranactionId(long sleTranactionId) {
		this.sleTranactionId = sleTranactionId;
	}
	
	
}
