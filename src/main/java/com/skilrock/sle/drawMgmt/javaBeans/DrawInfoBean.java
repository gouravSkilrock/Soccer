package com.skilrock.sle.drawMgmt.javaBeans;

import java.io.Serializable;

import com.skilrock.sle.reportsMgmt.javaBeans.DrawDataBean;

public class DrawInfoBean extends DrawDataBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int drawNo;
	private String drawFreezeTime;
	private String minEventStartTime;
	private String updatedDrawFreezeTime;
	private String updatedSaleStartTime;
	private String message;
	private String eventTimeChangeReq;

	public int getDrawNo() {
		return drawNo;
	}
	public void setDrawNo(int drawNo) {
		this.drawNo = drawNo;
	}
	public String getDrawFreezeTime() {
		return drawFreezeTime;
	}
	public void setDrawFreezeTime(String drawFreezeTime) {
		this.drawFreezeTime = drawFreezeTime;
	}
	public String getMinEventStartTime() {
		return minEventStartTime;
	}
	public void setMinEventStartTime(String minEventStartTime) {
		this.minEventStartTime = minEventStartTime;
	}
	public String getUpdatedDrawFreezeTime() {
		return updatedDrawFreezeTime;
	}
	public void setUpdatedDrawFreezeTime(String updatedDrawFreezeTime) {
		this.updatedDrawFreezeTime = updatedDrawFreezeTime;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getUpdatedSaleStartTime() {
		return updatedSaleStartTime;
	}
	public void setUpdatedSaleStartTime(String updatedSaleStartTime) {
		this.updatedSaleStartTime = updatedSaleStartTime;
	}
	public String getEventTimeChangeReq() {
		return eventTimeChangeReq;
	}
	public void setEventTimeChangeReq(String eventTimeChangeReq) {
		this.eventTimeChangeReq = eventTimeChangeReq;
	}
	@Override
	public String toString() {
		return "DrawInfoBean [drawNo=" + drawNo + ", drawFreezeTime="
				+ drawFreezeTime + ", minEventStartTime=" + minEventStartTime
				+ ", updatedDrawFreezeTime=" + updatedDrawFreezeTime
				+ ", updatedSaleStartTime=" + updatedSaleStartTime
				+ ", message=" + message + ", eventTimeChangeReq="
				+ eventTimeChangeReq + "]";
	}
	
	
	
	
	
	
}