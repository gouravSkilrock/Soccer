package com.skilrock.sle.merchant.pms.common.javaBeans;

public class PMSGamePlayResponseBean {
	private int responseCode;
	private int gameId;
	private int gameTypeId;
	private int userMappingId;
	private String merTxnId;
	private double txAmount;
	private double availBal;
	private String txType;
	private String ticketNumber;
	private String responseMessage;

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
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

	public int getUserMappingId() {
		return userMappingId;
	}

	public void setUserMappingId(int userMappingId) {
		this.userMappingId = userMappingId;
	}

	public String getMerTxnId() {
		return merTxnId;
	}

	public void setMerTxnId(String merTxnId) {
		this.merTxnId = merTxnId;
	}

	public double getTxAmount() {
		return txAmount;
	}

	public void setTxAmount(double txAmount) {
		this.txAmount = txAmount;
	}

	public double getAvailBal() {
		return availBal;
	}

	public void setAvailBal(double availBal) {
		this.availBal = availBal;
	}

	public String getTxType() {
		return txType;
	}

	public void setTxType(String txType) {
		this.txType = txType;
	}

	public String getTicketNumber() {
		return ticketNumber;
	}

	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	@Override
	public String toString() {
		return "PMSGamePlayResponseBean [responseCode=" + responseCode
				+ ", gameId=" + gameId + ", gameTypeId=" + gameTypeId
				+ ", userMappingId=" + userMappingId + ", merTxnId=" + merTxnId
				+ ", txAmount=" + txAmount + ", availBal=" + availBal
				+ ", txType=" + txType + ", ticketNumber=" + ticketNumber
				+ ", responseMessage=" + responseMessage + "]";
	}
}
