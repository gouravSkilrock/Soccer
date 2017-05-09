package com.skilrock.sle.tp.rest.common.javaBeans;

import java.util.List;

import com.skilrock.sle.gameDataMgmt.javaBeans.GameMasterBean;

public class GameResponseBean {

	private int responseCode;
	private String responseMessage;
	private List<GameMasterBean> gameBeanList;

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public List<GameMasterBean> getGameBeanList() {
		return gameBeanList;
	}

	public void setGameBeanList(List<GameMasterBean> gameBeanList) {
		this.gameBeanList = gameBeanList;
	}

}
