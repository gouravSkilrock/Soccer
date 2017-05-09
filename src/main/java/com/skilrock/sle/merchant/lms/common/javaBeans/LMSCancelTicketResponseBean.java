package com.skilrock.sle.merchant.lms.common.javaBeans;

import java.util.List;
import java.util.Map;

public class LMSCancelTicketResponseBean {

	private int responseCode;
	private int gameId;
	private int gameTypeId;
	private int userMappingId;
	private String merTxId;
	private double txAmount;
	private double availBal;
	private String txType;
	private String ticketNumber;
	private String responseMessage;
	private Map<String, List<String>> advMessageMap;

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

	public String getMerTxId() {
		return merTxId;
	}

	public void setMerTxId(String merTxId) {
		this.merTxId = merTxId;
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

	public Map<String, List<String>> getAdvMessageMap() {
		return advMessageMap;
	}

	public void setAdvMessageMap(Map<String, List<String>> advMessageMap) {
		this.advMessageMap = advMessageMap;
	}

	@Override
	public String toString() {
		return "LMSCancelTicketResponseBean [responseCode=" + responseCode
				+ ", gameId=" + gameId + ", gameTypeId=" + gameTypeId
				+ ", userMappingId=" + userMappingId + ", merTxId=" + merTxId
				+ ", txAmount=" + txAmount + ", availBal=" + availBal
				+ ", txType=" + txType + ", ticketNumber=" + ticketNumber
				+ ", responseMessage=" + responseMessage + ", advMessageMap="
				+ advMessageMap + "]";
	}

	

}
