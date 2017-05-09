package com.skilrock.sle.pwtMgmt.javaBeans;

import java.io.Serializable;
import java.util.Map;

public class DrawWiseTicketInfoBean implements Serializable{
	private static final long serialVersionUID = 1L;

	private int responseCode;
	private String responseMessage;
	private int merchantId;
	private int gameId;
	private int gameTypeId;
	private int drawId;
	//private List<TicketInfoBean> ticketInfoList;
	private Map<Long, TicketInfoBean> ticketMap;

	public DrawWiseTicketInfoBean() {
	}

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
	/*public List<TicketInfoBean> getTicketInfoList() {
		return ticketInfoList;
	}

	public void setTicketInfoList(List<TicketInfoBean> ticketInfoList) {
		this.ticketInfoList = ticketInfoList;
	}*/

	public Map<Long, TicketInfoBean> getTicketMap() {
		return ticketMap;
	}

	public void setTicketMap(Map<Long, TicketInfoBean> ticketMap) {
		this.ticketMap = ticketMap;
	}
}