package com.skilrock.sle.reportsMgmt.javaBeans;

import java.io.Serializable;
import java.util.Map;

public class FixtureWiseWinningAnalysisReportBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private int eventId;
	private String eventDescription;
	private String optionIdCodeList;
	private int winningOptionId;
	private String winningOptionCode;
	Map<String, String> eventCountMap;
	Map<String,String>optionCodeMap;
	private double winPercentage;
	public int getEventId() {
		return eventId;
	}
	public void setEventId(int eventId) {
		this.eventId = eventId;
	}
	public String getEventDescription() {
		return eventDescription;
	}
	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}
	public String getOptionIdCodeList() {
		return optionIdCodeList;
	}
	public void setOptionIdCodeList(String optionIdCodeList) {
		this.optionIdCodeList = optionIdCodeList;
	}
	public int getWinningOptionId() {
		return winningOptionId;
	}
	public void setWinningOptionId(int winningOptionId) {
		this.winningOptionId = winningOptionId;
	}
	public String getWinningOptionCode() {
		return winningOptionCode;
	}
	public void setWinningOptionCode(String winningOptionCode) {
		this.winningOptionCode = winningOptionCode;
	}
	public Map<String, String> getEventCountMap() {
		return eventCountMap;
	}
	public void setEventCountMap(Map<String, String> eventCountMap) {
		this.eventCountMap = eventCountMap;
	}
	public double getWinPercentage() {
		return winPercentage;
	}
	public void setWinPercentage(double winPercentage) {
		this.winPercentage = winPercentage;
	}
	public Map<String, String> getOptionCodeMap() {
		return optionCodeMap;
	}
	public void setOptionCodeMap(Map<String, String> optionCodeMap) {
		this.optionCodeMap = optionCodeMap;
	}


}