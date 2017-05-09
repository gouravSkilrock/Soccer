package com.skilrock.sle.gamePlayMgmt.javaBeans;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SportsLotteryGamePlayBean {
	private int gameId;
	private int gameTypeId;
	private int noOfBoard;
	private double unitPrice;
	private double totalPurchaseAmt;
	private SportsLotteryGameDrawDataBean[] gameDrawDataBeanArray;
	private int serviceId;
	private String interfaceType;
	private String refTransId;
	private Integer[] drawIdArray;
	private long ticketNumber;
	private String purchaseTime;
	private int reprintCount;
	private int barcodeCount;
	private boolean isPromoTicket;
	private long transId;
	private String winStatus;
	private double winAmt;
	private Map<String, List<String>> advMessageMap;
	private String plrMobileNumber;
	private String gameDevName;
	
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

	public double getTotalPurchaseAmt() {
		return totalPurchaseAmt;
	}

	public void setTotalPurchaseAmt(double totalPurchaseAmt) {
		this.totalPurchaseAmt = totalPurchaseAmt;
	}

	public SportsLotteryGameDrawDataBean[] getGameDrawDataBeanArray() {
		return gameDrawDataBeanArray;
	}

	public void setGameDrawDataBeanArray(
			SportsLotteryGameDrawDataBean[] gameDrawDataBeanArray) {
		this.gameDrawDataBeanArray = gameDrawDataBeanArray;
	}

	public int getServiceId() {
		return serviceId;
	}

	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}

	public String getInterfaceType() {
		return interfaceType;
	}

	public void setInterfaceType(String interfaceType) {
		this.interfaceType = interfaceType;
	}

	public String getRefTransId() {
		return refTransId;
	}

	public void setRefTransId(String refTransId) {
		this.refTransId = refTransId;
	}

	public Integer[] getDrawIdArray() {
		return drawIdArray;
	}

	public void setDrawIdArray(Integer[] drawIdArray) {
		this.drawIdArray = drawIdArray;
	}

	public long getTicketNumber() {
		return ticketNumber;
	}

	public void setTicketNumber(long ticketNumber) {
		this.ticketNumber = ticketNumber;
	}

	public String getPurchaseTime() {
		return purchaseTime;
	}

	public void setPurchaseTime(String purchaseTime) {
		this.purchaseTime = purchaseTime;
	}

	public int getReprintCount() {
		return reprintCount;
	}

	public void setReprintCount(int reprintCount) {
		this.reprintCount = reprintCount;
	}

	public int getBarcodeCount() {
		return barcodeCount;
	}

	public void setBarcodeCount(int barcodeCount) {
		this.barcodeCount = barcodeCount;
	}

	public boolean isPromoTicket() {
		return isPromoTicket;
	}

	public void setPromoTicket(boolean isPromoTicket) {
		this.isPromoTicket = isPromoTicket;
	}

	public long getTransId() {
		return transId;
	}

	public void setTransId(long transId) {
		this.transId = transId;
	}

	public Map<String, List<String>> getAdvMessageMap() {
		return advMessageMap;
	}

	public void setAdvMessageMap(Map<String, List<String>> advMessageMap) {
		this.advMessageMap = advMessageMap;
	}
	public void setWinStatus(String winStatus) {
		this.winStatus = winStatus;
	}

	public String getWinStatus() {
		return winStatus;
	}

	public void setWinAmt(double winAmt) {
		this.winAmt = winAmt;
	}

	public double getWinAmt() {
		return winAmt;
	}

	public String getPlrMobileNumber() {
		return plrMobileNumber;
	}

	public void setPlrMobileNumber(String plrMobileNumber) {
		this.plrMobileNumber = plrMobileNumber;
	}
	
	public String getGameDevName() {
		return gameDevName;
	}

	public void setGameDevName(String gameDevName) {
		this.gameDevName = gameDevName;
	}

	@Override
	public String toString() {
		return "SportsLotteryGamePlayBean [gameId=" + gameId + ", gameTypeId="
				+ gameTypeId + ", noOfBoard=" + noOfBoard + ", unitPrice="
				+ unitPrice + ", totalPurchaseAmt=" + totalPurchaseAmt
				+ ", gameDrawDataBeanArray=" + Arrays.toString(gameDrawDataBeanArray) + ", serviceId="
				+ serviceId + ", interfaceType=" + interfaceType
				+ ", refTransId=" + refTransId + ", drawIdArray="
				+ Arrays.toString(drawIdArray) + ", ticketNumber="
				+ ticketNumber + ", purchaseTime=" + purchaseTime
				+ ", reprintCount=" + reprintCount + ", barcodeCount="
				+ barcodeCount + ", isPromoTicket=" + isPromoTicket
				+ ", transId=" + transId + ", winStatus=" + winStatus
				+ ", winAmt=" + winAmt + ", advMessageMap=" + advMessageMap
				+ ", plrMobileNumber=" + plrMobileNumber + "]";
	}

	

}