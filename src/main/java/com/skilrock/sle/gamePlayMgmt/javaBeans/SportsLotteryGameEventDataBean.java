package com.skilrock.sle.gamePlayMgmt.javaBeans;

import java.util.Arrays;

public class SportsLotteryGameEventDataBean {

	private int eventId;
	private String[] selectedOption;
	private String eventDescription;
	private String homeTeamName;
	private String awayTeamName;
	private String leagueName;
	private String venueName;
	private String eventDateTime;
	private String winningEvent;
	private String homeTeamCode;
	private String awayTeamCode;

	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	public String[] getSelectedOption() {
		return selectedOption;
	}

	public void setSelectedOption(String[] selectedOption) {
		this.selectedOption = selectedOption;
	}

	public String getEventDescription() {
		return eventDescription;
	}

	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
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

	public String getWinningEvent() {
		return winningEvent;
	}

	public void setWinningEvent(String winningEvent) {
		this.winningEvent = winningEvent;
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
		return "SportsLotteryGameEventDataBean [eventId=" + eventId
				+ ", selectedOption=" + Arrays.toString(selectedOption)
				+ ", eventDescription=" + eventDescription + ", homeTeamName="
				+ homeTeamName + ", awayTeamName=" + awayTeamName
				+ ", leagueName=" + leagueName + ", venueName=" + venueName
				+ ", eventDateTime=" + eventDateTime + ", winningEvent="
				+ winningEvent + ", homeTeamCode=" + homeTeamCode
				+ ", awayTeamCode=" + awayTeamCode + "]";
	}

}
