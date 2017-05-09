package com.skilrock.sle.dataMgmt.javaBeans;

import java.util.List;
import java.util.Map;

import com.skilrock.sle.gameDataMgmt.javaBeans.EventMasterBean;

public class FetchDrawEventsResponse {

	private int responseCode;
	private String responseMessage;
	private int gameId;
	private String gameName;
	private int gameTypeId;
	private String gameTypeName;
	private int drawId;
	private String drawName;
	private String drawDateTime;
	private List<EventMasterBean> eventsInfo;
	private Map<String, String> eventOptionMap;

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public int getGameTypeId() {
		return gameTypeId;
	}

	public void setGameTypeId(int gameTypeId) {
		this.gameTypeId = gameTypeId;
	}

	public String getGameTypeName() {
		return gameTypeName;
	}

	public void setGameTypeName(String gameTypeName) {
		this.gameTypeName = gameTypeName;
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

	public String getDrawDateTime() {
		return drawDateTime;
	}

	public void setDrawDateTime(String drawDateTime) {
		this.drawDateTime = drawDateTime;
	}

	public List<EventMasterBean> getEventsInfo() {
		return eventsInfo;
	}

	public void setEventsInfo(List<EventMasterBean> eventsInfo) {
		this.eventsInfo = eventsInfo;
	}

	public Map<String, String> getEventOptionMap() {
		return eventOptionMap;
	}

	public void setEventOptionMap(Map<String, String> eventOptionMap) {
		this.eventOptionMap = eventOptionMap;
	}

}
