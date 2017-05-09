package com.skilrock.sle.gameDataMgmt.javaBeans;

import java.io.Serializable;
import java.util.Map;

import javax.validation.constraints.Min;


public class DrawEventResultBean implements Serializable {
	private static final long serialVersionUID = 1L;

	@Min(message="User ID cannot be empty or null",value=1 )
	private int userId;
	@Min(message="Game ID cannot be empty or null",value=1 )
	private int gameId;
	@Min(message="Game Type ID cannot be empty or null",value=1 )
	private int gameTypeId;
	@Min(message="Draw ID cannot be empty or null",value=1 )
	private int drawId;
	private int userResult;
	private Map<Integer, String> eventOptionResult;

	public DrawEventResultBean() {
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public int getGameTypeId() {
		return gameTypeId;
	}

	public void setGameTypeId(int gameTypeId) {
		this.gameTypeId = gameTypeId;
	}

	public int getDrawId() {
		return drawId;
	}

	public void setDrawId(int drawId) {
		this.drawId = drawId;
	}

	public int getUserResult() {
		return userResult;
	}

	public void setUserResult(int userResult) {
		this.userResult = userResult;
	}

	public Map<Integer, String> getEventOptionResult() {
		return eventOptionResult;
	}

	public void setEventOptionResult(Map<Integer, String> eventOptionResult) {
		this.eventOptionResult = eventOptionResult;
	}
	
}