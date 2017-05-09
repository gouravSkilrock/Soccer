package com.skilrock.sle.common.javaBeans;

public class TicketInfoBean {
	private Long txnId;
	private int gameId;
	private int gameTypeId;
	private long tktNbr;
	private double amount;
	private String txnDate;
	private int drawId;
	private int purchaseTable;
	private String status;
    private String ticketStatus;
    private String verificationCode;
    
	public Long getTxnId() {
		return txnId;
	}

	public void setTxnId(Long txnId) {
		this.txnId = txnId;
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

	public long getTktNbr() {
		return tktNbr;
	}

	public void setTktNbr(long tktNbr) {
		this.tktNbr = tktNbr;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getTxnDate() {
		return txnDate;
	}

	public void setTxnDate(String txnDate) {
		this.txnDate = txnDate;
	}

	public int getDrawId() {
		return drawId;
	}

	public void setDrawId(int drawId) {
		this.drawId = drawId;
	}

	public int getPurchaseTable() {
		return purchaseTable;
	}

	public void setPurchaseTable(int purchaseTable) {
		this.purchaseTable = purchaseTable;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setTicketStatus(String ticketStatus) {
		this.ticketStatus = ticketStatus;
	}

	public String getTicketStatus() {
		return ticketStatus;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

}
