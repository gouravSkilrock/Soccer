package com.skilrock.sle.merchant.weaver;

import java.io.Serializable;
import java.util.List;

public class WeaverGamePlayResponseBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private String sessionId;
	private long playerId;
	private String txnType;
	private int errorCode;
	private String errorMsg;
	private int gameId;
	private double practiceBal;
	private double totalBal;
	private double pendingWinning;
	private double realBal;
	private double promoBal;
	private List<WeaverTxnBean> transactionInfoList;
	///Response For Cancel
	private List<WeaverCancelTxnBean> plrWiseRespBean;
	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	

	public String getTxnType() {
		return txnType;
	}

	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public double getPracticeBal() {
		return practiceBal;
	}

	public void setPracticeBal(double practiceBal) {
		this.practiceBal = practiceBal;
	}

	public double getTotalBal() {
		return totalBal;
	}

	public void setTotalBal(double totalBal) {
		this.totalBal = totalBal;
	}

	public double getPendingWinning() {
		return pendingWinning;
	}

	public void setPendingWinning(double pendingWinning) {
		this.pendingWinning = pendingWinning;
	}

	public double getRealBal() {
		return realBal;
	}

	public void setRealBal(double realBal) {
		this.realBal = realBal;
	}

	public double getPromoBal() {
		return promoBal;
	}

	public void setPromoBal(double promoBal) {
		this.promoBal = promoBal;
	}

	public List<WeaverTxnBean> getTransactionInfoList() {
		return transactionInfoList;
	}

	public void setTransactionInfoList(List<WeaverTxnBean> transactionInfoList) {
		this.transactionInfoList = transactionInfoList;
	}

	public List<WeaverCancelTxnBean> getPlrWiseRespBean() {
		return plrWiseRespBean;
	}

	public void setPlrWiseRespBean(List<WeaverCancelTxnBean> plrWiseRespBean) {
		this.plrWiseRespBean = plrWiseRespBean;
	}

}
