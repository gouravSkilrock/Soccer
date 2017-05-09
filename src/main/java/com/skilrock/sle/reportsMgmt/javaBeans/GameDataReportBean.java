package com.skilrock.sle.reportsMgmt.javaBeans;

import java.io.Serializable;

public class GameDataReportBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private int gameId;
	private String gameName;
	private int gameTypeId;
	private String gameTypeName;
	private int drawId;
	private String drawName;
	private String drawTime;
	private String drawFreezeTime;
	private String drawStatus;
	private int noOfSale;
	private double saleAmount;
	private int noOfWinning;
	private double winningAmount;
	private String diplayWinningAmt;
	private String merchantOrgName;
	private String ticketNumber;
	private String purchaseTime;
	private String ticketStatus;
	private Double netAmount;
	private int totalClaimedTkts;
	private int totalUnclaimedTkts;
	private double totalClaimedAmt;
	private double totalUnclaimedAmt;
	private double avgSalePerSeller;
	private String isArchData;

	public GameDataReportBean() {
	}

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

	public int getGameTypeId() {
		return gameTypeId;
	}

	public void setGameTypeId(int gameTypeId) {
		this.gameTypeId = gameTypeId;
	}

	public String getGameTypeName() {
		return gameTypeName;
	}

	public void setGameTypeName(String gameTypeName) {
		this.gameTypeName = gameTypeName;
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

	public String getDrawTime() {
		return drawTime;
	}

	public void setDrawTime(String drawTime) {
		this.drawTime = drawTime;
	}

	public String getDrawFreezeTime() {
		return drawFreezeTime;
	}

	public void setDrawFreezeTime(String drawFreezeTime) {
		this.drawFreezeTime = drawFreezeTime;
	}

	public String getDrawStatus() {
		return drawStatus;
	}

	public void setDrawStatus(String drawStatus) {
		this.drawStatus = drawStatus;
	}

	public int getNoOfSale() {
		return noOfSale;
	}

	public void setNoOfSale(int noOfSale) {
		this.noOfSale = noOfSale;
	}

	public double getSaleAmount() {
		return saleAmount;
	}

	public void setSaleAmount(double saleAmount) {
		this.saleAmount = saleAmount;
	}

	public int getNoOfWinning() {
		return noOfWinning;
	}

	public void setNoOfWinning(int noOfWinning) {
		this.noOfWinning = noOfWinning;
	}

	public double getWinningAmount() {
		return winningAmount;
	}

	public void setWinningAmount(double winningAmount) {
		this.winningAmount = winningAmount;
	}

	public String getDiplayWinningAmt() {
		return diplayWinningAmt;
	}

	public void setDiplayWinningAmt(String diplayWinningAmt) {
		this.diplayWinningAmt = diplayWinningAmt;
	}

	public String getMerchantOrgName() {
		return merchantOrgName;
	}

	public void setMerchantOrgName(String merchantOrgName) {
		this.merchantOrgName = merchantOrgName;
	}

	public String getTicketNumber() {
		return ticketNumber;
	}

	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}

	public String getPurchaseTime() {
		return purchaseTime;
	}

	public void setPurchaseTime(String purchaseTime) {
		this.purchaseTime = purchaseTime;
	}

	public String getTicketStatus() {
		return ticketStatus;
	}

	public void setTicketStatus(String ticketStatus) {
		this.ticketStatus = ticketStatus;
	}
	
	

	public int getTotalClaimedTkts() {
		return totalClaimedTkts;
	}

	public void setTotalClaimedTkts(int totalClaimedTkts) {
		this.totalClaimedTkts = totalClaimedTkts;
	}

	public int getTotalUnclaimedTkts() {
		return totalUnclaimedTkts;
	}

	public void setTotalUnclaimedTkts(int totalUnclaimedTkts) {
		this.totalUnclaimedTkts = totalUnclaimedTkts;
	}

	public double getTotalClaimedAmt() {
		return totalClaimedAmt;
	}

	public void setTotalClaimedAmt(double totalClaimedAmt) {
		this.totalClaimedAmt = totalClaimedAmt;
	}

	public double getTotalUnclaimedAmt() {
		return totalUnclaimedAmt;
	}

	public void setTotalUnclaimedAmt(double totalUnclaimedAmt) {
		this.totalUnclaimedAmt = totalUnclaimedAmt;
	}

	public double getAvgSalePerSeller() {
		return avgSalePerSeller;
	}

	public void setAvgSalePerSeller(double avgSalePerSeller) {
		this.avgSalePerSeller = avgSalePerSeller;
	}

	public Double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}

	public String getIsArchData() {
		return isArchData;
	}

	public void setIsArchData(String isArchData) {
		this.isArchData = isArchData;
	}

	@Override
	public String toString() {
		return "GameDataReportBean [gameId=" + gameId + ", gameName="
				+ gameName + ", gameTypeId=" + gameTypeId + ", gameTypeName="
				+ gameTypeName + ", drawId=" + drawId + ", drawName="
				+ drawName + ", drawTime=" + drawTime + ", drawFreezeTime="
				+ drawFreezeTime + ", drawStatus=" + drawStatus + ", noOfSale="
				+ noOfSale + ", saleAmount=" + saleAmount + ", noOfWinning="
				+ noOfWinning + ", winningAmount=" + winningAmount
				+ ", diplayWinningAmt=" + diplayWinningAmt
				+ ", merchantOrgName=" + merchantOrgName + ", ticketNumber="
				+ ticketNumber + ", purchaseTime=" + purchaseTime
				+ ", ticketStatus=" + ticketStatus + ", netAmount=" + netAmount
				+ ", totalClaimedTkts=" + totalClaimedTkts
				+ ", totalUnclaimedTkts=" + totalUnclaimedTkts
				+ ", totalClaimedAmt=" + totalClaimedAmt
				+ ", totalUnclaimedAmt=" + totalUnclaimedAmt
				+ ", avgSalePerSeller=" + avgSalePerSeller + ", isArchData="
				+ isArchData + "]";
	}

	

}