package com.skilrock.sle.gameDataMgmt.javaBeans;

import java.util.List;

public class EventMasterBean {

	private int eventId;
	private int gameId;
	private String eventDisplay;
	private String eventDescription;
	private String homeTeamName;
	private String awayTeamName;
	private String venueName;
	private String leagueName;
	private String startTime;
	private String endTime;
	private String entryTime;
	private List<String> eventOptionsList;
	private int noOfEvents;
	private String homeTeamCode;
	private String awayTeamCode;
	private String homeTeamOdds;
	private String awayTeamOdds;
	private String favTeam;
	private String drawOdds;
	private String winningOption;
	private String winninOptionCode;

	public EventMasterBean() {
	}

	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public String getEventDisplay() {
		return eventDisplay;
	}

	public void setEventDisplay(String eventDisplay) {
		this.eventDisplay = eventDisplay;
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

	public String getVenueName() {
		return venueName;
	}

	public void setVenueName(String venueName) {
		this.venueName = venueName;
	}

	public String getLeagueName() {
		return leagueName;
	}

	public void setLeagueName(String leagueName) {
		this.leagueName = leagueName;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getEntryTime() {
		return entryTime;
	}

	public void setEntryTime(String entryTime) {
		this.entryTime = entryTime;
	}

	public List<String> getEventOptionsList() {
		return eventOptionsList;
	}

	public void setEventOptionsList(List<String> eventOptionsList) {
		this.eventOptionsList = eventOptionsList;
	}

	public int getNoOfEvents() {
		return noOfEvents;
	}

	public void setNoOfEvents(int noOfEvents) {
		this.noOfEvents = noOfEvents;
	}

	public void setAwayTeamCode(String awayTeamCode) {
		this.awayTeamCode = awayTeamCode;
	}

	public String getAwayTeamCode() {
		return awayTeamCode;
	}

	public void setHomeTeamCode(String homeTeamCode) {
		this.homeTeamCode = homeTeamCode;
	}

	public String getHomeTeamCode() {
		return homeTeamCode;
	}
	
	public void setWinningOption(String winningOption) {
		this.winningOption = winningOption;
	}

	public String getWinningOption() {
		return winningOption;
	}

	public String getWinninOptionCode() {
		return winninOptionCode;
	}

	public void setWinninOptionCode(String winninOptionCode) {
		this.winninOptionCode = winninOptionCode;
	}

	public String getHomeTeamOdds() {
		return homeTeamOdds;
	}

	public void setHomeTeamOdds(String homeTeamOdds) {
		this.homeTeamOdds = homeTeamOdds;
	}

	public String getAwayTeamOdds() {
		return awayTeamOdds;
	}

	public void setAwayTeamOdds(String awayTeamOdds) {
		this.awayTeamOdds = awayTeamOdds;
	}

	public String getDrawOdds() {
		return drawOdds;
	}

	public void setDrawOdds(String drawOdds) {
		this.drawOdds = drawOdds;
	}

	public String getFavTeam() {
		return favTeam;
	}

	public void setFavTeam(String favTeam) {
		this.favTeam = favTeam;
	}

	@Override
	public String toString() {
		return "EventMasterBean [eventId=" + eventId + ", gameId=" + gameId
				+ ", eventDisplay=" + eventDisplay + ", eventDescription="
				+ eventDescription + ", homeTeamName=" + homeTeamName
				+ ", awayTeamName=" + awayTeamName + ", venueName=" + venueName
				+ ", leagueName=" + leagueName + ", startTime=" + startTime
				+ ", endTime=" + endTime + ", entryTime=" + entryTime
				+ ", eventOptionsList=" + eventOptionsList + ", noOfEvents="
				+ noOfEvents + ", homeTeamCode=" + homeTeamCode
				+ ", awayTeamCode=" + awayTeamCode + ", homeTeamOdds="
				+ homeTeamOdds + ", awayTeamOdds=" + awayTeamOdds
				+ ", favTeam=" + favTeam + ", drawOdds=" + drawOdds
				+ ", winningOption=" + winningOption + ", winninOptionCode="
				+ winninOptionCode + "]";
	}

	

	
}