package com.skilrock.sle.gamePlayMgmt.javaBeans;

public class EventDetailBean {

	private int eventId;
	private int eventOptionId;
	private String eventDescription;
	private String eventDisplay;
	private String homeTeamName;
	private String awayTeamName;
	private String leagueName;
	private String venueName;
	private String eventDateTime;
	private String optionName;
	private String optionCode;
	private String homeTeamCode;
	private String awayTeamCode;
	

	public String getOptionCode() {
		return optionCode;
	}

	public void setOptionCode(String optionCode) {
		this.optionCode = optionCode;
	}

	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	public int getEventOptionId() {
		return eventOptionId;
	}

	public void setEventOptionId(int eventOptionId) {
		this.eventOptionId = eventOptionId;
	}

	public String getEventDescription() {
		return eventDescription;
	}

	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}

	public String getEventDisplay() {
		return eventDisplay;
	}

	public void setEventDisplay(String eventDisplay) {
		this.eventDisplay = eventDisplay;
	}

	public String getHomeTeamName() {
		return homeTeamName;
	}

	public void setHomeTeamName(String homeTeamName) {
		this.homeTeamName = homeTeamName;
	}

	public String getAwayTeamName() {
		return awayTeamName;
	}

	public void setAwayTeamName(String awayTeamName) {
		this.awayTeamName = awayTeamName;
	}

	public String getLeagueName() {
		return leagueName;
	}

	public void setLeagueName(String leagueName) {
		this.leagueName = leagueName;
	}

	public String getVenueName() {
		return venueName;
	}

	public void setVenueName(String venueName) {
		this.venueName = venueName;
	}

	public String getEventDateTime() {
		return eventDateTime;
	}

	public void setEventDateTime(String eventDateTime) {
		this.eventDateTime = eventDateTime;
	}

	public String getOptionName() {
		return optionName;
	}

	public void setOptionName(String optionName) {
		this.optionName = optionName;
	}

	public String getHomeTeamCode() {
		return homeTeamCode;
	}

	public void setHomeTeamCode(String homeTeamCode) {
		this.homeTeamCode = homeTeamCode;
	}

	public String getAwayTeamCode() {
		return awayTeamCode;
	}

	public void setAwayTeamCode(String awayTeamCode) {
		this.awayTeamCode = awayTeamCode;
	}

	@Override
	public String toString() {
		return "EventDetailBean [eventId=" + eventId + ", eventOptionId="
				+ eventOptionId + ", eventDescription=" + eventDescription
				+ ", eventDisplay=" + eventDisplay + ", homeTeamName="
				+ homeTeamName + ", awayTeamName=" + awayTeamName
				+ ", leagueName=" + leagueName + ", venueName=" + venueName
				+ ", eventDateTime=" + eventDateTime + ", optionName="
				+ optionName + ", optionCode=" + optionCode + ", homeTeamCode="
				+ homeTeamCode + ", awayTeamCode=" + awayTeamCode + "]";
	}

	

}
