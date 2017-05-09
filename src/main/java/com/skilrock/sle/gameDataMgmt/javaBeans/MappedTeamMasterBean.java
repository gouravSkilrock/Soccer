package com.skilrock.sle.gameDataMgmt.javaBeans;

public class MappedTeamMasterBean extends TeamMasterBean{
	private static final long serialVersionUID = 1L;
	private int leagueId;
	
	public MappedTeamMasterBean() {
	}

	public int getLeagueId() {
		return leagueId;
	}

	public void setLeagueId(int leagueId) {
		this.leagueId = leagueId;
	}
	
	
}