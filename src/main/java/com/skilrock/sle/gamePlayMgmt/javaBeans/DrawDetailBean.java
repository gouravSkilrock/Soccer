package com.skilrock.sle.gamePlayMgmt.javaBeans;

import java.util.Map;

public class DrawDetailBean {

	private int drawId;
	private int drawNo;
	private int purchaseTableName;
	private String drawDisplay;
	private String drawDateTime;
	private String drawClaimStartTime;
	private String drawSaleStartTime;
	private String drawClaimEndTime;
	private String verificationStartTime;
	private Map<Integer, Map<String, EventDetailBean>> drawEventDetail;

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

	public int getPurchaseTableName() {
		return purchaseTableName;
	}

	public void setPurchaseTableName(int purchaseTableName) {
		this.purchaseTableName = purchaseTableName;
	}

	public String getDrawDisplay() {
		return drawDisplay;
	}

	public void setDrawDisplay(String drawDisplay) {
		this.drawDisplay = drawDisplay;
	}

	public String getDrawDateTime() {
		return drawDateTime;
	}

	public void setDrawDateTime(String drawDateTime) {
		this.drawDateTime = drawDateTime;
	}

	public String getDrawClaimStartTime() {
		return drawClaimStartTime;
	}

	public void setDrawClaimStartTime(String drawClaimStartTime) {
		this.drawClaimStartTime = drawClaimStartTime;
	}

	public String getDrawClaimEndTime() {
		return drawClaimEndTime;
	}

	public void setDrawClaimEndTime(String drawClaimEndTime) {
		this.drawClaimEndTime = drawClaimEndTime;
	}

	public String getVerificationStartTime() {
		return verificationStartTime;
	}

	public void setVerificationStartTime(String verificationStartTime) {
		this.verificationStartTime = verificationStartTime;
	}

	public Map<Integer, Map<String, EventDetailBean>> getDrawEventDetail() {
		return drawEventDetail;
	}

	public void setDrawEventDetail(
			Map<Integer, Map<String, EventDetailBean>> drawEventDetail) {
		this.drawEventDetail = drawEventDetail;
	}

	public String getDrawSaleStartTime() {
		return drawSaleStartTime;
	}

	public void setDrawSaleStartTime(String drawSaleStartTime) {
		this.drawSaleStartTime = drawSaleStartTime;
	}

	@Override
	public String toString() {
		return "DrawDetailBean [drawClaimEndTime=" + drawClaimEndTime
				+ ", drawClaimStartTime=" + drawClaimStartTime
				+ ", drawDateTime=" + drawDateTime + ", drawDisplay="
				+ drawDisplay + ", drawEventDetail=" + drawEventDetail
				+ ", drawId=" + drawId + ", drawNo=" + drawNo
				+ ", drawSaleStartTime=" + drawSaleStartTime
				+ ", purchaseTableName=" + purchaseTableName
				+ ", verificationStartTime=" + verificationStartTime + "]";
	}



}
