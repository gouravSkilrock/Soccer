package com.skilrock.sle.dataMgmt.javaBeans;

public class TicketTxnStatusBean {
	
	private String winStatus;
	private long ticketNbr;
	private double winAmt;
	private String  merchantTxnId;
	private int gameId;
	private int gameTypeId;
	private int drawId;
	
	
	public String getWinStatus() {
		return winStatus;
	}
	public void setWinStatus(String winStatus) {
		this.winStatus = winStatus;
	}
	public long getTicketNbr() {
		return ticketNbr;
	}
	public void setTicketNbr(long ticketNbr) {
		this.ticketNbr = ticketNbr;
	}
	public double getWinAmt() {
		return winAmt;
	}
	public void setWinAmt(double winAmt) {
		this.winAmt = winAmt;
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
	public int getDrawId() {
		return drawId;
	}
	public void setDrawId(int drawId) {
		this.drawId = drawId;
	}
	public String getMerchantTxnId() {
		return merchantTxnId;
	}
	public void setMerchantTxnId(String merchantTxnId) {
		this.merchantTxnId = merchantTxnId;
	}
	@Override
	public String toString() {
		return "TicketTxnStatusBean [drawId=" + drawId + ", gameId=" + gameId
				+ ", gameTypeId=" + gameTypeId + ", merchantTxnId="
				+ merchantTxnId + ", ticketNbr=" + ticketNbr + ", winAmt="
				+ winAmt + ", winStatus=" + winStatus + "]";
	}
	
}
