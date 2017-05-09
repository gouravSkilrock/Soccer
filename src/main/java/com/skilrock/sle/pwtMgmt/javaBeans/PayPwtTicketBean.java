package com.skilrock.sle.pwtMgmt.javaBeans;

public class PayPwtTicketBean {

	private long userId;
	private String merchantName;
	private int gameId;
	private int gameTypeId;
	private int drawId;
	private int taskId;
	private String ticketNumber;
	private double winningAmount;
	private String winningChannel;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
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

	public int getTaskId() {
		return taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public String getTicketNumber() {
		return ticketNumber;
	}

	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}

	public String getWinningChannel() {
		return winningChannel;
	}

	public double getWinningAmount() {
		return winningAmount;
	}

	public void setWinningAmount(double winningAmount) {
		this.winningAmount = winningAmount;
	}

	public void setWinningChannel(String winningChannel) {
		this.winningChannel = winningChannel;
	}
}