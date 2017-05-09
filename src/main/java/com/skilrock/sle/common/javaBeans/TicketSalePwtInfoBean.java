package com.skilrock.sle.common.javaBeans;

import java.io.Serializable;

public class TicketSalePwtInfoBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private int eventId;
	private int drawId;
	private String drawName;
	private String userId;
	private long ticketNbr;
	private String partyId;
	private String userType;
	private String saleDateTime;
	private double saleAmt;
	private double winAmt;
	private String datePrizePaid;

	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	public String getRefTxnId() {
		return refTxnId;
	}

	public void setRefTxnId(String refTxnId) {
		this.refTxnId = refTxnId;
	}

	private String refTxnId;

	public String getDatePrizePaid() {
		return datePrizePaid;
	}

	public void setDatePrizePaid(String datePrizePaid) {
		this.datePrizePaid = datePrizePaid;
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public long getTicketNbr() {
		return ticketNbr;
	}

	public void setTicketNbr(long ticketNbr) {
		this.ticketNbr = ticketNbr;
	}

	public String getPartyId() {
		return partyId;
	}

	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getSaleDateTime() {
		return saleDateTime;
	}

	public void setSaleDateTime(String saleDateTime) {
		this.saleDateTime = saleDateTime;
	}

	public double getSaleAmt() {
		return saleAmt;
	}

	public void setSaleAmt(double saleAmt) {
		this.saleAmt = saleAmt;
	}

	public double getWinAmt() {
		return winAmt;
	}

	public void setWinAmt(double winAmt) {
		this.winAmt = winAmt;
	}

}
