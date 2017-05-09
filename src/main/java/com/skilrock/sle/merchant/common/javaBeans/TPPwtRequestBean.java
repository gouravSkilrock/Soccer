package com.skilrock.sle.merchant.common.javaBeans;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.skilrock.sle.pwtMgmt.javaBeans.DrawTicketDataBean;
import com.skilrock.sle.pwtMgmt.javaBeans.PlayerBean;

public class TPPwtRequestBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private String pwtType;
	private int gameId;
	private int gameTypeId;
	private String ticketNumber;
	private List<DrawTicketDataBean> drawDataList;
	private double totalAmount;
	private String remarks;
	private PlayerBean playerBean;
	Map<Integer, Long> transMap;

	public TPPwtRequestBean() {
	}

	public String getPwtType() {
		return pwtType;
	}

	public void setPwtType(String pwtType) {
		this.pwtType = pwtType;
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

	public String getTicketNumber() {
		return ticketNumber;
	}

	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}

	public List<DrawTicketDataBean> getDrawDataList() {
		return drawDataList;
	}

	public void setDrawDataList(List<DrawTicketDataBean> drawDataList) {
		this.drawDataList = drawDataList;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public PlayerBean getPlayerBean() {
		return playerBean;
	}

	public void setPlayerBean(PlayerBean playerBean) {
		this.playerBean = playerBean;
	}

	public Map<Integer, Long> getTransMap() {
		return transMap;
	}

	public void setTransMap(Map<Integer, Long> transMap) {
		this.transMap = transMap;
	}
}