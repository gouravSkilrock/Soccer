package com.skilrock.sle.common.javaBeans;

import java.io.Serializable;

public class ValidateTicketBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private int gameid;
	private int gameTypeId;
	private int gameNo;
	private String gameName;
	private String gameTypeName;
	private String ticketNo;
	private String ticketNumInDB;
	private int reprintCount;
	private boolean isValid;
	private boolean isTicketExpired;
	private int ticketExpiryPeriod;
	private String status;
	private int dayOfTicket;
	private String merchantCode;
	private long transId;
	private long merchantUserId;
	private boolean isDateBypass;
	private int barCodeCount;
	private String isCancel;
	private String drawClaimEndDate;

	public String getIsCancel() {
		return isCancel;
	}

	public void setIsCancel(String isCancel) {
		this.isCancel = isCancel;
	}

	public int getBarCodeCount() {
		return barCodeCount;
	}

	public void setBarCodeCount(int barCodeCount) {
		this.barCodeCount = barCodeCount;
	}

	public ValidateTicketBean() {
	}

	public ValidateTicketBean(String ticketNo) {
		this.ticketNo = ticketNo;
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

	public int getGameid() {
		return gameid;
	}

	public void setGameid(int gameid) {
		this.gameid = gameid;
	}

	public int getGameTypeId() {
		return gameTypeId;
	}

	public void setGameTypeId(int gameTypeId) {
		this.gameTypeId = gameTypeId;
	}

	public int getGameNo() {
		return gameNo;
	}

	public void setGameNo(int gameNo) {
		this.gameNo = gameNo;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public String getGameTypeName() {
		return gameTypeName;
	}

	public void setGameTypeName(String gameTypeName) {
		this.gameTypeName = gameTypeName;
	}

	public String getTicketNo() {
		return ticketNo;
	}

	public void setTicketNo(String ticketNo) {
		this.ticketNo = ticketNo;
	}

	public String getTicketNumInDB() {
		return ticketNumInDB;
	}

	public void setTicketNumInDB(String ticketNumInDB) {
		this.ticketNumInDB = ticketNumInDB;
	}

	public int getReprintCount() {
		return reprintCount;
	}

	public void setReprintCount(int reprintCount) {
		this.reprintCount = reprintCount;
	}

	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	public boolean isTicketExpired() {
		return isTicketExpired;
	}

	public void setTicketExpired(boolean isTicketExpired) {
		this.isTicketExpired = isTicketExpired;
	}

	public int getTicketExpiryPeriod() {
		return ticketExpiryPeriod;
	}

	public void setTicketExpiryPeriod(int ticketExpiryPeriod) {
		this.ticketExpiryPeriod = ticketExpiryPeriod;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getDayOfTicket() {
		return dayOfTicket;
	}

	public void setDayOfTicket(int dayOfTicket) {
		this.dayOfTicket = dayOfTicket;
	}

	public String getMerchantCode() {
		return merchantCode;
	}

	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}

	public boolean isDateBypass() {
		return isDateBypass;
	}

	public void setDateBypass(boolean isDateBypass) {
		this.isDateBypass = isDateBypass;
	}

	public String getDrawClaimEndDate() {
		return drawClaimEndDate;
	}

	public void setDrawClaimEndDate(String drawClaimEndDate) {
		this.drawClaimEndDate = drawClaimEndDate;
	}

	@Override
	public String toString() {
		return "ValidateTicketBean [gameid=" + gameid + ", gameTypeId="
				+ gameTypeId + ", gameNo=" + gameNo + ", gameName=" + gameName
				+ ", gameTypeName=" + gameTypeName + ", ticketNo=" + ticketNo
				+ ", ticketNumInDB=" + ticketNumInDB + ", reprintCount="
				+ reprintCount + ", isValid=" + isValid + ", isTicketExpired="
				+ isTicketExpired + ", ticketExpiryPeriod="
				+ ticketExpiryPeriod + ", status=" + status + ", dayOfTicket="
				+ dayOfTicket + ", merchantCode=" + merchantCode + ", transId="
				+ transId + ", merchantUserId=" + merchantUserId
				+ ", isDateBypass=" + isDateBypass + ", barCodeCount="
				+ barCodeCount + ", isCancel=" + isCancel
				+ ", drawClaimEndDate=" + drawClaimEndDate + "]";
	}

	

}