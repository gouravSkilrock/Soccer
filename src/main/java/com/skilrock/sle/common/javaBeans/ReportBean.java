package com.skilrock.sle.common.javaBeans;

import java.util.Map;


public class ReportBean {
	
	private String startDate;
	private String endDate;
	private String reportType;
	private String reportChannel;
	Map<Long, TicketInfoBean> tktInfoMap;	

	public Map<Long, TicketInfoBean> getTktInfoMap() {
		return tktInfoMap;
	}
	public void setTktInfoMap(Map<Long, TicketInfoBean> tktInfoMap) {
		this.tktInfoMap = tktInfoMap;
	}
	public String getReportType() {
		return reportType;
	}
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
	public String getReportChannel() {
		return reportChannel;
	}
	public void setReportChannel(String reportChannel) {
		this.reportChannel = reportChannel;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	

}
