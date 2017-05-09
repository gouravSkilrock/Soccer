package com.skilrock.sle.dataMgmt.javaBeans;

public class SimnetResultDrawsBean {

	
	private int drawId;
	private String drawName;
	private String drawDateTime;
	
	public String getDrawDateTime() {
		return drawDateTime;
	}
	public void setDrawDateTime(String drawDateTime) {
		this.drawDateTime = drawDateTime;
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
	
	@Override
	public String toString() {
		return "SimnetResultDrawsBean [drawId=" + drawId + ", drawName=" + drawName + ", drawDateTime=" + drawDateTime
				+ "]";
	}
}
