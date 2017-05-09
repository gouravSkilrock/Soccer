package com.skilrock.sle.gameDataMgmt.javaBeans;

import java.util.List;

public class GameTypeMasterBean {
	private int gameTypeId;
	private int gameId;
	private int merchantId;
	private String gameTypeDevName;
	private String gameTypeDispName;
	private double unitPrice;
	private int maxBetAmtMultiple;
	private double prizeAmtPercentage;
	private String jackPotMessageDisplay;
	private String saleStartTime;
	private String saleEndTime;
	private int noOfEvents;
	private String eventType;
	private int displayOrder;
	private String gameTypeStatus;
	private List<DrawMasterBean> drawMasterList;
	private String eventSelectionType;
	private String upcomingDrawStartTime;
	private boolean areEventsMappedForUpcomingDraw;
	

	public GameTypeMasterBean() {
	}

	public int getGameTypeId() {
		return gameTypeId;
	}

	public void setGameTypeId(int gameTypeId) {
		this.gameTypeId = gameTypeId;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public int getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(int merchantId) {
		this.merchantId = merchantId;
	}

	public String getGameTypeDevName() {
		return gameTypeDevName;
	}

	public void setGameTypeDevName(String gameTypeDevName) {
		this.gameTypeDevName = gameTypeDevName;
	}

	public String getGameTypeDispName() {
		return gameTypeDispName;
	}

	public void setGameTypeDispName(String gameTypeDispName) {
		this.gameTypeDispName = gameTypeDispName;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public int getMaxBetAmtMultiple() {
		return maxBetAmtMultiple;
	}

	public void setMaxBetAmtMultiple(int maxBetAmtMultiple) {
		this.maxBetAmtMultiple = maxBetAmtMultiple;
	}

	public double getPrizeAmtPercentage() {
		return prizeAmtPercentage;
	}

	public void setPrizeAmtPercentage(double prizeAmtPercentage) {
		this.prizeAmtPercentage = prizeAmtPercentage;
	}

	public String getJackPotMessageDisplay() {
		return jackPotMessageDisplay;
	}

	public void setJackPotMessageDisplay(String jackPotMessageDisplay) {
		this.jackPotMessageDisplay = jackPotMessageDisplay;
	}

	public String getSaleStartTime() {
		return saleStartTime;
	}

	public void setSaleStartTime(String saleStartTime) {
		this.saleStartTime = saleStartTime;
	}

	public String getSaleEndTime() {
		return saleEndTime;
	}

	public void setSaleEndTime(String saleEndTime) {
		this.saleEndTime = saleEndTime;
	}

	public int getNoOfEvents() {
		return noOfEvents;
	}

	public void setNoOfEvents(int noOfEvents) {
		this.noOfEvents = noOfEvents;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public int getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}

	public String getGameTypeStatus() {
		return gameTypeStatus;
	}

	public void setGameTypeStatus(String gameTypeStatus) {
		this.gameTypeStatus = gameTypeStatus;
	}

	public List<DrawMasterBean> getDrawMasterList() {
		return drawMasterList;
	}

	public void setDrawMasterList(List<DrawMasterBean> drawMasterList) {
		this.drawMasterList = drawMasterList;
	}

	public String getEventSelectionType() {
		return eventSelectionType;
	}

	public void setEventSelectionType(String eventSelectionType) {
		this.eventSelectionType = eventSelectionType;
	}

	public String getUpcomingDrawStartTime() {
		return upcomingDrawStartTime;
	}

	public void setUpcomingDrawStartTime(String upcomingDrawStartTime) {
		this.upcomingDrawStartTime = upcomingDrawStartTime;
	}

	public boolean isAreEventsMappedForUpcomingDraw() {
		return areEventsMappedForUpcomingDraw;
	}

	public void setAreEventsMappedForUpcomingDraw(
			boolean areEventsMappedForUpcomingDraw) {
		this.areEventsMappedForUpcomingDraw = areEventsMappedForUpcomingDraw;
	}

	@Override
	public String toString() {
		return "GameTypeMasterBean [areEventsMappedForUpcomingDraw="
				+ areEventsMappedForUpcomingDraw + ", displayOrder="
				+ displayOrder + ", drawMasterList=" + drawMasterList
				+ ", eventSelectionType=" + eventSelectionType + ", eventType="
				+ eventType + ", gameId=" + gameId + ", gameTypeDevName="
				+ gameTypeDevName + ", gameTypeDispName=" + gameTypeDispName
				+ ", gameTypeId=" + gameTypeId + ", gameTypeStatus="
				+ gameTypeStatus + ", jackPotMessageDisplay="
				+ jackPotMessageDisplay + ", maxBetAmtMultiple="
				+ maxBetAmtMultiple + ", merchantId=" + merchantId
				+ ", noOfEvents=" + noOfEvents + ", prizeAmtPercentage="
				+ prizeAmtPercentage + ", saleEndTime=" + saleEndTime
				+ ", saleStartTime=" + saleStartTime + ", unitPrice="
				+ unitPrice + ", upcomingDrawStartTime="
				+ upcomingDrawStartTime + "]";
	}

	


}