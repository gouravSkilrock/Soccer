package com.skilrock.sle.pwtMgmt.javaBeans;

public class BoardTicketDataBean {

	private int boardId;
	private double winningAmt;
	private String ticketStatus; 
	
	public double getWinningAmt() {
		return winningAmt;
	}
	public void setWinningAmt(double winningAmt) {
		this.winningAmt = winningAmt;
	}
	public String getTicketStatus() {
		return ticketStatus;
	}
	public void setTicketStatus(String ticketStatus) {
		this.ticketStatus = ticketStatus;
	}
	public int getBoardId() {
		return boardId;
	}
	public void setBoardId(int boardId) {
		this.boardId = boardId;
	}
	
}
