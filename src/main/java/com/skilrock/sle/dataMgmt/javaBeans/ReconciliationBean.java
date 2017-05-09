package com.skilrock.sle.dataMgmt.javaBeans;

public class ReconciliationBean {
	private String engineTxnId;
	private String engineSaleTxnId;
	private String merchantTxnId;
	private String status;
	private long ticktNo;
	private double winAmt;
	private int playerId;
	private int gameId;
	private int gameTypeId;

	public String getEngineTxnId() {
		return engineTxnId;
	}

	public void setEngineTxnId(String engineTxnId) {
		this.engineTxnId = engineTxnId;
	}

	public String getEngineSaleTxnId() {
		return engineSaleTxnId;
	}

	public void setEngineSaleTxnId(String engineSaleTxnId) {
		this.engineSaleTxnId = engineSaleTxnId;
	}

	public String getMerchantTxnId() {
		return merchantTxnId;
	}

	public void setMerchantTxnId(String merchantTxnId) {
		this.merchantTxnId = merchantTxnId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	public long getTicktNo() {
		return ticktNo;
	}

	public void setTicktNo(long ticktNo) {
		this.ticktNo = ticktNo;
	}

	public double getWinAmt() {
		return winAmt;
	}

	public void setWinAmt(double winAmt) {
		this.winAmt = winAmt;
	}

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
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

	@Override
	public String toString() {
		return "ReconciliationBean [engineSaleTxnId=" + engineSaleTxnId
				+ ", engineTxnId=" + engineTxnId + ", gameId=" + gameId
				+ ", gameTypeId=" + gameTypeId + ", merchantTxnId="
				+ merchantTxnId + ", playerId=" + playerId + ", status="
				+ status + ", ticktNo=" + ticktNo + ", winAmt=" + winAmt + "]";
	}

	
	

}
