package com.skilrock.sle.pwtMgmt.javaBeans;

public class PwtVerifyTicketDrawDataBean {

	private String drawName;
	private String drawDateTime;
	private String drawResult;
	private BoardTicketDataBean[] boardTicketBeanArray;
	private String drawStatus;
	private double drawWinAmt;
	private int boardCount;
	private int drawId;
	private int drawNo;
	private int noOfMatches;
	private String message;
	private String status;
	private String actuatDrawStatus;
	private String rankId ;
	
	

	public String getActuatDrawStatus() {
		return actuatDrawStatus;
	}

	public void setActuatDrawStatus(String actuatDrawStatus) {
		this.actuatDrawStatus = actuatDrawStatus;
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

	public String getDrawResult() {
		return drawResult;
	}

	public void setDrawResult(String drawResult) {
		this.drawResult = drawResult;
	}

	public BoardTicketDataBean[] getBoardTicketBeanArray() {
		return boardTicketBeanArray;
	}

	public void setBoardTicketBeanArray(
			BoardTicketDataBean[] boardTicketBeanArray) {
		this.boardTicketBeanArray = boardTicketBeanArray;
	}

	public String getDrawStatus() {
		return drawStatus;
	}

	public void setDrawStatus(String drawStatus) {
		this.drawStatus = drawStatus;
	}

	public double getDrawWinAmt() {
		return drawWinAmt;
	}

	public void setDrawWinAmt(double drawWinAmt) {
		this.drawWinAmt = drawWinAmt;
	}

	public int getBoardCount() {
		return boardCount;
	}

	public void setBoardCount(int boardCount) {
		this.boardCount = boardCount;
	}

	public int getDrawId() {
		return drawId;
	}

	public void setDrawId(int drawId) {
		this.drawId = drawId;
	}

	public int getDrawNo() {
		return drawNo;
	}

	public void setDrawNo(int drawNo) {
		this.drawNo = drawNo;
	}

	public int getNoOfMatches() {
		return noOfMatches;
	}

	public void setNoOfMatches(int noOfMatches) {
		this.noOfMatches = noOfMatches;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRankId() {
		return rankId;
	}

	public void setRankId(String rankId) {
		this.rankId = rankId;
	}
	
	
}