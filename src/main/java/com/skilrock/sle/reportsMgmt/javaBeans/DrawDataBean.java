package com.skilrock.sle.reportsMgmt.javaBeans;

import java.io.Serializable;

public class DrawDataBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int drawId;
	private String drawName;
	private String drawDateTime;
	private String drawStatus;
	private String saleStartTime;
	private int eventId;
	
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
	public void setDrawStatus(String drawStatus) {
		this.drawStatus = drawStatus;
	}
	public String getDrawStatus() {
		return drawStatus;
	}
	public void setSaleStartTime(String saleStartTime) {
		this.saleStartTime = saleStartTime;
	}
	public String getSaleStartTime() {
		return saleStartTime;
	}
	public void setEventId(int eventId) {
		this.eventId = eventId;
	}
	public int getEventId() {
		return eventId;
	}
	@Override
	public String toString() {
		return "DrawDataBean [drawDateTime=" + drawDateTime + ", drawId="
				+ drawId + ", drawName=" + drawName + ", drawStatus="
				+ drawStatus + ", eventId=" + eventId + ", saleStartTime="
				+ saleStartTime + "]";
	}
	
	
	
	
}