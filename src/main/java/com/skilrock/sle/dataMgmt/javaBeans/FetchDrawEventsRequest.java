package com.skilrock.sle.dataMgmt.javaBeans;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class FetchDrawEventsRequest {

	private int gameId;
	private int gameTypeId;
	private int drawId;
	private int merchantId;
	@NotNull(message="Game Name not provided")
	@Size(min=1,message="Game Name not provided")
	private String gameName;
	@NotNull(message="Game Type Name not provided")
	@Size(min=1,message="Game Type Name not provided")
	private String gameTypeName;
	@NotNull(message="Draw Name not provided")
	@Size(min=1,message="Draw Name not provided")
	private String drawName;
	@NotNull(message="Draw Date Time not provided")
	@Size(min=1,message="Draw Date Time not provided")
	private String drawDateTime;

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

	public int getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(int merchantId) {
		this.merchantId = merchantId;
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

}
