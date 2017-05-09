package com.skilrock.sle.drawMgmt.javaBeans;

import java.util.List;

import com.skilrock.sle.pwtMgmt.javaBeans.TicketInfoBean;

public class DrawInfoMerchantWiseBean {
	private int gameId;
	private String gameDispName;
	private String gameDevName;
	private int gameTypeId;
	private String gameTypeDispName;
	private String gameTypeDevName;

	private int drawId;
	private String drawName;
	private String drawDateTime;
	private String drawStatus;
	private String freezeTime;
	private double winningAmt;
	private List<TicketInfoBean> ticketPwtBeanList;

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public String getGameDispName() {
		return gameDispName;
	}

	public void setGameDispName(String gameDispName) {
		this.gameDispName = gameDispName;
	}

	public String getGameDevName() {
		return gameDevName;
	}

	public void setGameDevName(String gameDevName) {
		this.gameDevName = gameDevName;
	}

	public int getGameTypeId() {
		return gameTypeId;
	}

	public void setGameTypeId(int gameTypeId) {
		this.gameTypeId = gameTypeId;
	}

	public String getGameTypeDispName() {
		return gameTypeDispName;
	}

	public void setGameTypeDispName(String gameTypeDispName) {
		this.gameTypeDispName = gameTypeDispName;
	}

	public String getGameTypeDevName() {
		return gameTypeDevName;
	}

	public void setGameTypeDevName(String gameTypeDevName) {
		this.gameTypeDevName = gameTypeDevName;
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

	public String getDrawDateTime() {
		return drawDateTime;
	}

	public void setDrawDateTime(String drawDateTime) {
		this.drawDateTime = drawDateTime;
	}

	public String getDrawStatus() {
		return drawStatus;
	}

	public void setDrawStatus(String drawStatus) {
		this.drawStatus = drawStatus;
	}

	public String getFreezeTime() {
		return freezeTime;
	}

	public void setFreezeTime(String freezeTime) {
		this.freezeTime = freezeTime;
	}

	public double getWinningAmt() {
		return winningAmt;
	}

	public void setWinningAmt(double winningAmt) {
		this.winningAmt = winningAmt;
	}

	public List<TicketInfoBean> getTicketPwtBeanList() {
		return ticketPwtBeanList;
	}

	public void setTicketPwtBeanList(List<TicketInfoBean> ticketPwtBeanList) {
		this.ticketPwtBeanList = ticketPwtBeanList;
	}

	@Override
	public String toString() {
		return "DrawInfoMerchantWiseBean [drawDateTime=" + drawDateTime
				+ ", drawId=" + drawId + ", drawName=" + drawName
				+ ", drawStatus=" + drawStatus + ", freezeTime=" + freezeTime
				+ ", gameDevName=" + gameDevName + ", gameDispName="
				+ gameDispName + ", gameId=" + gameId + ", gameTypeDevName="
				+ gameTypeDevName + ", gameTypeDispName=" + gameTypeDispName
				+ ", gameTypeId=" + gameTypeId + ", ticketPwtBeanList="
				+ ticketPwtBeanList + ", winningAmt=" + winningAmt + "]";
	}


}
