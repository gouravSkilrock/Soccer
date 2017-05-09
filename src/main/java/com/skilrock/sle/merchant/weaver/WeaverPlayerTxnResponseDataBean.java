package com.skilrock.sle.merchant.weaver;

import java.io.Serializable;

public class WeaverPlayerTxnResponseDataBean extends WeaverCommonResponseDataBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private long playerId;
	private String practiceBal;
	private double totalBal;
	private String pendingWinning;
	private double realBal;
	private String promoBal;
	private String refWagerTxnId;
	private String bonusToCash;
	private String txnId;
	private long refTxnNo;

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public String getPracticeBal() {
		return practiceBal;
	}

	public void setPracticeBal(String practiceBal) {
		this.practiceBal = practiceBal;
	}

	public double getTotalBal() {
		return totalBal;
	}

	public void setTotalBal(double totalBal) {
		this.totalBal = totalBal;
	}

	public String getPendingWinning() {
		return pendingWinning;
	}

	public void setPendingWinning(String pendingWinning) {
		this.pendingWinning = pendingWinning;
	}

	public double getRealBal() {
		return realBal;
	}

	public void setRealBal(double realBal) {
		this.realBal = realBal;
	}

	public String getPromoBal() {
		return promoBal;
	}

	public void setPromoBal(String promoBal) {
		this.promoBal = promoBal;
	}

	public String getRefWagerTxnId() {
		return refWagerTxnId;
	}

	public void setRefWagerTxnId(String refWagerTxnId) {
		this.refWagerTxnId = refWagerTxnId;
	}

	public String getBonusToCash() {
		return bonusToCash;
	}

	public void setBonusToCash(String bonusToCash) {
		this.bonusToCash = bonusToCash;
	}


	public String getTxnId() {
		return txnId;
	}

	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}

	public long getRefTxnNo() {
		return refTxnNo;
	}

	public void setRefTxnNo(long refTxnNo) {
		this.refTxnNo = refTxnNo;
	}

	@Override
	public String toString() {
		return "WeaverPlayerTxnResponseDataBean [playerId=" + playerId
				+ ", practiceBal=" + practiceBal + ", totalBal=" + totalBal
				+ ", pendingWinning=" + pendingWinning + ", realBal=" + realBal
				+ ", promoBal=" + promoBal + ", refWagerTxnId=" + refWagerTxnId
				+ ", bonusToCash=" + bonusToCash + ", txnId=" + txnId
				+ ", refTxnNo=" + refTxnNo + "]";
	}

}
