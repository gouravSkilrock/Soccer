package com.skilrock.sle.pwtMgmt.javaBeans;

import java.io.Serializable;

public class TicketInfoBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private int partyId;
	private Long ticketNo;
	private double totalWinningAmt;
	private String enginewinTxnId;
	private String enginesaleTxnId;
	private int gameId;
	private String gameName;
	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	public TicketInfoBean() {
	}

	public int getPartyId() {
		return partyId;
	}

	public void setPartyId(int partyId) {
		this.partyId = partyId;
	}

	public Long getTicketNo() {
		return ticketNo;
	}

	public void setTicketNo(Long ticketNo) {
		this.ticketNo = ticketNo;
	}

	public double getTotalWinningAmt() {
		return totalWinningAmt;
	}

	public void setTotalWinningAmt(double totalWinningAmt) {
		this.totalWinningAmt = totalWinningAmt;
	}

	public void setEnginewinTxnId(String enginewinTxnId) {
		this.enginewinTxnId = enginewinTxnId;
	}

	public String getEnginewinTxnId() {
		return enginewinTxnId;
	}
	public String getEnginesaleTxnId() {
		return enginesaleTxnId;
	}

	public void setEnginesaleTxnId(String enginesaleTxnId) {
		this.enginesaleTxnId = enginesaleTxnId;
	}

	
}