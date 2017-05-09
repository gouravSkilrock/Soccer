package com.skilrock.sle.tp.rest.common.javaBeans;

import java.io.Serializable;
import java.util.Map;

public class TransactionBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int gameId;
	private int gameTypeId;
	private int merchantId;
	private String merchantPlayerId;
	private long transactionId;
	private double transactionAmt;
	private String ticketNumber;
	private String reprintCount;
	private String playerSessionId;
	private String transactionType;
	private Map<Integer, Map<Integer, String>> drawIdTableMap;
	private boolean isPromoTkt;
	private String merchantTxnId;
	private String gameDevName;
	private Object gameRequestBean;
	
	public long getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}

	public int getGameId() {
		return gameId;
	}
	public void setGameId(int gameId) {
		this.gameId = gameId;
	}
	
	public String getMerchantPlayerId() {
		return merchantPlayerId;
	}
	public void setMerchantPlayerId(String merchantPlayerId) {
		this.merchantPlayerId = merchantPlayerId;
	}
	
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public int getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(int merchantId) {
		this.merchantId = merchantId;
	}
	public String getTicketNumber() {
		return ticketNumber;
	}
	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}
	public String getReprintCount() {
		return reprintCount;
	}
	public void setReprintCount(String reprintCount) {
		this.reprintCount = reprintCount;
	}
	public String getPlayerSessionId() {
		return playerSessionId;
	}
	public void setPlayerSessionId(String playerSessionId) {
		this.playerSessionId = playerSessionId;
	}
	public double getTransactionAmt() {
		return transactionAmt;
	}
	public void setTransactionAmt(double transactionAmt) {
		this.transactionAmt = transactionAmt;
	}
	public Map<Integer, Map<Integer, String>> getDrawIdTableMap() {
		return drawIdTableMap;
	}
	public void setDrawIdTableMap(Map<Integer, Map<Integer, String>> drawIdTableMap) {
		this.drawIdTableMap = drawIdTableMap;
	}
	public boolean isPromoTkt() {
		return isPromoTkt;
	}
	public void setPromoTkt(boolean isPromoTkt) {
		this.isPromoTkt = isPromoTkt;
	}
	public String getMerchantTxnId() {
		return merchantTxnId;
	}
	public void setMerchantTxnId(String merchantTxnId) {
		this.merchantTxnId = merchantTxnId;
	}
	public String getGameDevName() {
		return gameDevName;
	}
	public void setGameDevName(String gameDevName) {
		this.gameDevName = gameDevName;
	}
	public Object getGameRequestBean() {
		return gameRequestBean;
	}
	public void setGameRequestBean(Object gameRequestBean) {
		this.gameRequestBean = gameRequestBean;
	}
	public int getGameTypeId() {
		return gameTypeId;
	}
	public void setGameTypeId(int gameTypeId) {
		this.gameTypeId = gameTypeId;
	}
	
	
	
}
