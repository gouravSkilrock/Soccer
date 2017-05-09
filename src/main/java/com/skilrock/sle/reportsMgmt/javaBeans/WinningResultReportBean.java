package com.skilrock.sle.reportsMgmt.javaBeans;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.skilrock.sle.gamePlayMgmt.javaBeans.SportsLotteryGameEventDataBean;

public class WinningResultReportBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private String gameName;
	private String gameTypeName;
	private int drawId;
	private String drawName;
	private String drawDate;
	private String drawTime;
	private Map<String, String> eventOptionMap;
	private List<SportsLotteryGameEventDataBean> drawEventList;
	public WinningResultReportBean() {
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
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

	public String getDrawDate() {
		return drawDate;
	}

	public void setDrawDate(String drawDate) {
		this.drawDate = drawDate;
	}

	public String getDrawTime() {
		return drawTime;
	}

	public void setDrawTime(String drawTime) {
		this.drawTime = drawTime;
	}

	public Map<String, String> getEventOptionMap() {
		return eventOptionMap;
	}

	public void setEventOptionMap(Map<String, String> eventOptionMap) {
		this.eventOptionMap = eventOptionMap;
	}

	public List<SportsLotteryGameEventDataBean> getDrawEventList() {
		return drawEventList;
	}

	public void setDrawEventList(List<SportsLotteryGameEventDataBean> drawEventList) {
		this.drawEventList = drawEventList;
	}
}