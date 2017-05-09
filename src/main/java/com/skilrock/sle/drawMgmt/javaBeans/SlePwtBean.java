package com.skilrock.sle.drawMgmt.javaBeans;

import java.util.ArrayList;

import com.skilrock.sle.gamePlayMgmt.javaBeans.EventDetailBean;

public class SlePwtBean{
	
	private int gameId;
	private int gameTypeId;
	private int drawId;
	private int merchantId;
	private String drawName;
	private String drawDate;
	private String drawTime;
	private String drawFreezeDate;
	private String drawFreezeTime;
	private String drawDateTime;
	private String drawResult;
	private String drawStatus;
	private String winningResult;
	ArrayList<EventDetailBean> eventDetailList;
	private int respnseCode;
	private String responseMsg;
	
	
	public String getResponseMsg() {
		return responseMsg;
	}
	public void setResponseMsg(String responseMsg) {
		this.responseMsg = responseMsg;
	}
	public int getRespnseCode() {
		return respnseCode;
	}
	public void setRespnseCode(int respnseCode) {
		this.respnseCode = respnseCode;
	}
	public ArrayList<EventDetailBean> getEventDetailList() {
		return eventDetailList;
	}
	public void setEventDetailList(ArrayList<EventDetailBean> eventDetailList) {
		this.eventDetailList = eventDetailList;
	}
	public String getDrawStatus() {
		return drawStatus;
	}
	public void setDrawStatus(String drawStatus) {
		this.drawStatus = drawStatus;
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
	public String getDrawFreezeDate() {
		return drawFreezeDate;
	}
	public void setDrawFreezeDate(String drawFreezeDate) {
		this.drawFreezeDate = drawFreezeDate;
	}
	public String getDrawFreezeTime() {
		return drawFreezeTime;
	}
	public void setDrawFreezeTime(String drawFreezeTime) {
		this.drawFreezeTime = drawFreezeTime;
	}
	public String getDrawResult() {
		return drawResult;
	}
	public void setDrawResult(String drawResult) {
		this.drawResult = drawResult;
	}
	public int getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(int merchantId) {
		this.merchantId = merchantId;
	}
	public int getGameId() {
		return gameId;
	}
	public void setGameId(int gameId) {
		this.gameId = gameId;
	}
	public int getDrawId() {
		return drawId;
	}
	public void setDrawId(int drawId) {
		this.drawId = drawId;
	}
	public String getWinningResult() {
		return winningResult;
	}
	public void setWinningResult(String winningResult) {
		this.winningResult = winningResult;
	}
	public String getDrawDateTime() {
		return drawDateTime;
	}
	public void setDrawDateTime(String drawDateTime) {
		this.drawDateTime = drawDateTime;
	}
	public int getGameTypeId() {
		return gameTypeId;
	}
	public void setGameTypeId(int gameTypeId) {
		this.gameTypeId = gameTypeId;
	}
	
	
}