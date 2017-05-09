package com.skilrock.sle.gameDataMgmt.javaBeans;

public class MappedEventMasterBean extends EventMasterBean{
	private int leagueId;
	private int venueId;
	private int homeTeamId;
	private int awayTeamId;
	private boolean isSaleStarted;
	
	public int getLeagueId() {
		return leagueId;
	}
	public void setLeagueId(int leagueId) {
		this.leagueId = leagueId;
	}
	public int getVenueId() {
		return venueId;
	}
	public void setVenueId(int venueId) {
		this.venueId = venueId;
	}
	public int getHomeTeamId() {
		return homeTeamId;
	}
	public void setHomeTeamId(int homeTeamId) {
		this.homeTeamId = homeTeamId;
	}
	public int getAwayTeamId() {
		return awayTeamId;
	}
	public void setAwayTeamId(int awayTeamId) {
		this.awayTeamId = awayTeamId;
	}
	public boolean isSaleStarted() {
		return isSaleStarted;
	}
	public void setSaleStarted(boolean isSaleStarted) {
		this.isSaleStarted = isSaleStarted;
	}
	
}
