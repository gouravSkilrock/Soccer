package com.skilrock.sle.merchant.common.javaBeans;

import java.util.List;
import java.util.Map;

public class TPPwtResponseBean {
	private int gameId;
	private int gameTypeId;
	private String ticketNumber;
	private Map<Integer, String> drawTransMap;
	private int doneByUserId;
	private String status;
	private String requestId;
	private double balance;
	private Map<String, List<String>> advMsg;
	private double govtTaxPwt;

	public TPPwtResponseBean() {
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

	public String getTicketNumber() {
		return ticketNumber;
	}

	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}

	public Map<Integer, String> getDrawTransMap() {
		return drawTransMap;
	}

	public void setDrawTransMap(Map<Integer, String> drawTransMap) {
		this.drawTransMap = drawTransMap;
	}

	public int getDoneByUserId() {
		return doneByUserId;
	}

	public void setDoneByUserId(int doneByUserId) {
		this.doneByUserId = doneByUserId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public Map<String, List<String>> getAdvMsg() {
		return advMsg;
	}

	public void setAdvMsg(Map<String, List<String>> advMsg) {
		this.advMsg = advMsg;
	}

	public double getGovtTaxPwt() {
		return govtTaxPwt;
	}

	public void setGovtTaxPwt(double govtTaxPwt) {
		this.govtTaxPwt = govtTaxPwt;
	}
}