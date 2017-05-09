package com.skilrock.sle.pwtMgmt.javaBeans;

import java.util.List;
import java.util.Map;

public class PwtVerifyTicketBean {

	private String merchantName;
	private String ticketNumber;
	private String purchaseDateTime;
	private double totalPurchaseAmt;
	private double totalWinAmt;
	private int noOfDraws;
	private PwtVerifyTicketDrawDataBean[] verifyTicketDrawDataBeanArray;
	private String gameName;
	private String gameTypename;
	private int gameId;
	private int gameTypeId;
	private String ticketNumInDB;
	private Map<String, List<String>> advMessageMap;
	
	

	public String getTicketNumInDB() {
		return ticketNumInDB;
	}

	public void setTicketNumInDB(String ticketNumInDB) {
		this.ticketNumInDB = ticketNumInDB;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getTicketNumber() {
		return ticketNumber;
	}

	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}

	public String getPurchaseDateTime() {
		return purchaseDateTime;
	}

	public void setPurchaseDateTime(String purchaseDateTime) {
		this.purchaseDateTime = purchaseDateTime;
	}

	public double getTotalPurchaseAmt() {
		return totalPurchaseAmt;
	}

	public void setTotalPurchaseAmt(double totalPurchaseAmt) {
		this.totalPurchaseAmt = totalPurchaseAmt;
	}

	public double getTotalWinAmt() {
		return totalWinAmt;
	}

	public void setTotalWinAmt(double totalWinAmt) {
		this.totalWinAmt = totalWinAmt;
	}

	public PwtVerifyTicketDrawDataBean[] getVerifyTicketDrawDataBeanArray() {
		return verifyTicketDrawDataBeanArray;
	}

	public void setVerifyTicketDrawDataBeanArray(
			PwtVerifyTicketDrawDataBean[] verifyTicketDrawDataBeanArray) {
		this.verifyTicketDrawDataBeanArray = verifyTicketDrawDataBeanArray;
	}

	public int getNoOfDraws() {
		return noOfDraws;
	}

	public void setNoOfDraws(int noOfDraws) {
		this.noOfDraws = noOfDraws;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public String getGameTypename() {
		return gameTypename;
	}

	public void setGameTypename(String gameTypename) {
		this.gameTypename = gameTypename;
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

	public Map<String, List<String>> getAdvMessageMap() {
		return advMessageMap;
	}

	public void setAdvMessageMap(Map<String, List<String>> advMessageMap) {
		this.advMessageMap = advMessageMap;
	}
	
}