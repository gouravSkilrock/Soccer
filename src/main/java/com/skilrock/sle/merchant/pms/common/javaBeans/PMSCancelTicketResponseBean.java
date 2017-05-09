package com.skilrock.sle.merchant.pms.common.javaBeans;

public class PMSCancelTicketResponseBean {
	private double balance;
	private boolean isSuccess;
	private String refTrxId;

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getRefTrxId() {
		return refTrxId;
	}

	public void setRefTrxId(String refTrxId) {
		this.refTrxId = refTrxId;
	}

	@Override
	public String toString() {
		return "LMSCancelTicketResponseBean [balance=" + balance
				+ ", isSuccess=" + isSuccess + ", refTrxId=" + refTrxId + "]";
	}
}
