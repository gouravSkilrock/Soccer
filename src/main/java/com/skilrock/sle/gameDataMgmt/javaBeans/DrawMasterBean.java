package com.skilrock.sle.gameDataMgmt.javaBeans;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


public class DrawMasterBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private int drawId;
	private int gameTypeId;
	private int merchantId;
	private int drawNo;
	private String gameTypeName;
	private String drawName;
	private String drawDisplayType;
	private String drawDateTime;
	private String drawFreezeTime;
	private String saleStartTime;
	private String claimStartDate;
	private String claimEndDate;
	private String verificationDate;
	private String drawStatus;
	private int purchaseTableName;
	private List<EventMasterBean> eventMasterList;
	private List<SLEDrawWinningBean> drawWinningDetail;
	private Map<String,String> eventOptionMap;
	

	public DrawMasterBean() {
	}

	public int getDrawId() {
		return drawId;
	}

	public void setDrawId(int drawId) {
		this.drawId = drawId;
	}

	public int getGameTypeId() {
		return gameTypeId;
	}

	public void setGameTypeId(int gameTypeId) {
		this.gameTypeId = gameTypeId;
	}

	public int getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(int merchantId) {
		this.merchantId = merchantId;
	}

	public int getDrawNo() {
		return drawNo;
	}

	public void setDrawNo(int drawNo) {
		this.drawNo = drawNo;
	}

	public String getGameTypeName() {
		return gameTypeName;
	}

	public void setGameTypeName(String gameTypeName) {
		this.gameTypeName = gameTypeName;
	}

	public String getDrawName() {
		return drawName;
	}

	public void setDrawName(String drawName) {
		this.drawName = drawName;
	}

	public String getDrawDisplayType() {
		return drawDisplayType;
	}

	public void setDrawDisplayType(String drawDisplayType) {
		this.drawDisplayType = drawDisplayType;
	}

	public String getDrawDateTime() {
		return drawDateTime;
	}

	public void setDrawDateTime(String drawDateTime) {
		this.drawDateTime = drawDateTime;
	}

	public String getDrawFreezeTime() {
		return drawFreezeTime;
	}

	public void setDrawFreezeTime(String drawFreezeTime) {
		this.drawFreezeTime = drawFreezeTime;
	}

	public String getSaleStartTime() {
		return saleStartTime;
	}

	public void setSaleStartTime(String saleStartTime) {
		this.saleStartTime = saleStartTime;
	}

	public String getClaimStartDate() {
		return claimStartDate;
	}

	public void setClaimStartDate(String claimStartDate) {
		this.claimStartDate = claimStartDate;
	}

	public String getClaimEndDate() {
		return claimEndDate;
	}

	public void setClaimEndDate(String claimEndDate) {
		this.claimEndDate = claimEndDate;
	}

	public String getVerificationDate() {
		return verificationDate;
	}

	public void setVerificationDate(String verificationDate) {
		this.verificationDate = verificationDate;
	}

	public String getDrawStatus() {
		return drawStatus;
	}

	public void setDrawStatus(String drawStatus) {
		this.drawStatus = drawStatus;
	}

	public int getPurchaseTableName() {
		return purchaseTableName;
	}

	public void setPurchaseTableName(int purchaseTableName) {
		this.purchaseTableName = purchaseTableName;
	}

	public List<EventMasterBean> getEventMasterList() {
		return eventMasterList;
	}

	public void setEventMasterList(List<EventMasterBean> eventMasterList) {
		this.eventMasterList = eventMasterList;
	}

	public void setDrawWinningDetail(List<SLEDrawWinningBean> drawWinningDetail) {
		this.drawWinningDetail = drawWinningDetail;
	}

	public Map<String, String> getEventOptionMap() {
		return eventOptionMap;
	}

	public void setEventOptionMap(Map<String, String> eventOptionMap) {
		this.eventOptionMap = eventOptionMap;
	}

	public List<SLEDrawWinningBean> getDrawWinningDetail() {
		return drawWinningDetail;
	}

	@Override
	public String toString() {
		return "DrawMasterBean [drawId=" + drawId + ", gameTypeId="
				+ gameTypeId + ", merchantId=" + merchantId + ", drawNo="
				+ drawNo + ", gameTypeName=" + gameTypeName + ", drawName="
				+ drawName + ", drawDisplayType=" + drawDisplayType
				+ ", drawDateTime=" + drawDateTime + ", drawFreezeTime="
				+ drawFreezeTime + ", saleStartTime=" + saleStartTime
				+ ", claimStartDate=" + claimStartDate + ", claimEndDate="
				+ claimEndDate + ", verificationDate=" + verificationDate
				+ ", drawStatus=" + drawStatus + ", purchaseTableName="
				+ purchaseTableName + ", eventMasterList=" + eventMasterList
				+ ", drawWinningDetail=" + drawWinningDetail + "]";
	}
	
}