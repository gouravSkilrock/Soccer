package com.skilrock.sle.embedded.playMgmt.common.javaBeans;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public class CancelTicketBean {
	private String isAutoCancel;
	private String cancelType;
	private String lastTxnNbr;
	private boolean isLastTktCancel;
	private String tktNbr;
	private String interfaceType;
	private Timestamp cancelDate;
	private double cancelAmount;
	private long saleTxnId;
	private long cancelTxnId;
	private long txnIdToCancel;
	private String tktToCancel;
	private int gameId;
	private int gameTypeId;
	private int drawId;
	private String gameTypeName;
	private int reprintCount;
	private int barcodeCount;
	private long tktMerchantUserId;
	private Map<String, List<String>> advMessageMap;

	public String getIsAutoCancel() {
		return isAutoCancel;
	}

	public void setIsAutoCancel(String isAutoCancel) {
		this.isAutoCancel = isAutoCancel;
	}

	public String getCancelType() {
		return cancelType;
	}

	public void setCancelType(String cancelType) {
		this.cancelType = cancelType;
	}

	public String getLastTxnNbr() {
		return lastTxnNbr;
	}

	public void setLastTxnNbr(String lastTxnNbr) {
		this.lastTxnNbr = lastTxnNbr;
	}

	public boolean isLastTktCancel() {
		return isLastTktCancel;
	}

	public void setLastTktCancel(boolean isLastTktCancel) {
		this.isLastTktCancel = isLastTktCancel;
	}

	public String getTktNbr() {
		return tktNbr;
	}

	public void setTktNbr(String tktNbr) {
		this.tktNbr = tktNbr;
	}

	public String getInterfaceType() {
		return interfaceType;
	}

	public void setInterfaceType(String interfaceType) {
		this.interfaceType = interfaceType;
	}

	public Timestamp getCancelDate() {
		return cancelDate;
	}

	public void setCancelDate(Timestamp cancelDate) {
		this.cancelDate = cancelDate;
	}

	public double getCancelAmount() {
		return cancelAmount;
	}

	public void setCancelAmount(double cancelAmount) {
		this.cancelAmount = cancelAmount;
	}

	public long getSaleTxnId() {
		return saleTxnId;
	}

	public void setSaleTxnId(long saleTxnId) {
		this.saleTxnId = saleTxnId;
	}

	public long getCancelTxnId() {
		return cancelTxnId;
	}

	public void setCancelTxnId(long cancelTxnId) {
		this.cancelTxnId = cancelTxnId;
	}

	public long getTxnIdToCancel() {
		return txnIdToCancel;
	}

	public void setTxnIdToCancel(long txnIdToCancel) {
		this.txnIdToCancel = txnIdToCancel;
	}

	public String getTktToCancel() {
		return tktToCancel;
	}

	public void setTktToCancel(String tktToCancel) {
		this.tktToCancel = tktToCancel;
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

	public String getGameTypeName() {
		return gameTypeName;
	}

	public void setGameTypeName(String gameTypeName) {
		this.gameTypeName = gameTypeName;
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

	public long getTktMerchantUserId() {
		return tktMerchantUserId;
	}

	public void setTktMerchantUserId(long tktMerchantUserId) {
		this.tktMerchantUserId = tktMerchantUserId;
	}

	public Map<String, List<String>> getAdvMessageMap() {
		return advMessageMap;
	}

	public void setAdvMessageMap(Map<String, List<String>> advMessageMap) {
		this.advMessageMap = advMessageMap;
	}

	@Override
	public String toString() {
		return "CancelTicketBean [isAutoCancel=" + isAutoCancel
				+ ", cancelType=" + cancelType + ", lastTxnNbr=" + lastTxnNbr
				+ ", isLastTktCancel=" + isLastTktCancel + ", tktNbr=" + tktNbr
				+ ", interfaceType=" + interfaceType + ", cancelDate="
				+ cancelDate + ", cancelAmount=" + cancelAmount
				+ ", saleTxnId=" + saleTxnId + ", cancelTxnId=" + cancelTxnId
				+ ", txnIdToCancel=" + txnIdToCancel + ", tktToCancel="
				+ tktToCancel + ", gameId=" + gameId + ", gameTypeId="
				+ gameTypeId + ", drawId=" + drawId + ", gameTypeName="
				+ gameTypeName + ", reprintCount=" + reprintCount
				+ ", barcodeCount=" + barcodeCount + ", tktMerchantUserId="
				+ tktMerchantUserId + ", advMessageMap=" + advMessageMap + "]";
	}


}
