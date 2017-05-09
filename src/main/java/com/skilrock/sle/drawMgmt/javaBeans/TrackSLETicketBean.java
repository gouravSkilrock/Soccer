package com.skilrock.sle.drawMgmt.javaBeans;

import java.util.List;

import com.skilrock.sle.gamePlayMgmt.javaBeans.EventDetailBean;

public class TrackSLETicketBean {
	private String ticketNumber;
	private String merchantName;
	private String gameTypeName;
	private String gameName;
	private String purchaseDate;
	private String purchaseTime;
	private int drawId;
	private String drawName;
	private String drawDate;
	private String drawTime;
	private String drawStatus;
	private int noOfEvents;
	private int noOfLines;
	private int noOfBoard;
	private double unitPrice;
	private double totalAmount;
	private long transId;
	private long merchantUserId;
	private String tktStatus;
	private int rpcCount;
	private double totalWinAmt;
	private String winStatus;

	private List<EventDetailBean> eventDetails;

	public TrackSLETicketBean() {
	}

	public String getDrawStatus() {
		return drawStatus;
	}

	public void setDrawStatus(String drawStatus) {
		this.drawStatus = drawStatus;
	}

	public int getRpcCount() {
		return rpcCount;
	}

	public void setRpcCount(int rpcCount) {
		this.rpcCount = rpcCount;
	}

	public String getTktStatus() {
		return tktStatus;
	}

	public void setTktStatus(String tktStatus) {
		this.tktStatus = tktStatus;
	}

	public long getMerchantUserId() {
		return merchantUserId;
	}

	public void setMerchantUserId(long merchantUserId) {
		this.merchantUserId = merchantUserId;
	}

	public long getTransId() {
		return transId;
	}

	public void setTransId(long transId) {
		this.transId = transId;
	}

	public String getGameTypeName() {
		return gameTypeName;
	}

	public void setGameTypeName(String gameTypeName) {
		this.gameTypeName = gameTypeName;
	}

	public String getTicketNumber() {
		return ticketNumber;
	}

	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public String getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(String purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public String getPurchaseTime() {
		return purchaseTime;
	}

	public void setPurchaseTime(String purchaseTime) {
		this.purchaseTime = purchaseTime;
	}

	public int getDrawId() {
		return drawId;
	}

	public void setDrawId(int drawId) {
		this.drawId = drawId;
	}

	public String getDrawName() {
		return drawName;
	}

	public void setDrawName(String drawName) {
		this.drawName = drawName;
	}

	public String getDrawDate() {
		return drawDate;
	}

	public void setDrawDate(String drawDate) {
		this.drawDate = drawDate;
	}

	public String getDrawTime() {
		return drawTime;
	}

	public void setDrawTime(String drawTime) {
		this.drawTime = drawTime;
	}

	public int getNoOfEvents() {
		return noOfEvents;
	}

	public void setNoOfEvents(int noOfEvents) {
		this.noOfEvents = noOfEvents;
	}

	public int getNoOfLines() {
		return noOfLines;
	}

	public void setNoOfLines(int noOfLines) {
		this.noOfLines = noOfLines;
	}

	public int getNoOfBoard() {
		return noOfBoard;
	}

	public void setNoOfBoard(int noOfBoard) {
		this.noOfBoard = noOfBoard;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public List<EventDetailBean> getEventDetails() {
		return eventDetails;
	}

	public void setEventDetails(List<EventDetailBean> eventDetails) {
		this.eventDetails = eventDetails;
	}

	public double getTotalWinAmt() {
		return totalWinAmt;
	}

	public void setTotalWinAmt(double totalWinAmt) {
		this.totalWinAmt = totalWinAmt;
	}

	public String getWinStatus() {
		return winStatus;
	}

	public void setWinStatus(String winStatus) {
		this.winStatus = winStatus;
	}

}