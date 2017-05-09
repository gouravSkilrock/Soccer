package com.skilrock.sle.reportsMgmt.javaBeans;

import org.apache.commons.collections.Predicate;

public class MerchantWiseSalePwtReportBean implements Predicate {

	private int merchantId;
	private String merchantName;
	private int gameId;
	private String gameName;
	private int gameTypeId;
	private String gameTypeName;
	private double saleAmount;
	private double winningAmount;

	public static class GameDetailsBean {
		private int gameId;
		private String gameName;
		private int gameTypeId;
		private String gameTypeName;

		public GameDetailsBean() {
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
	}

	public MerchantWiseSalePwtReportBean() {
	}

	public int getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(int merchantId) {
		this.merchantId = merchantId;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
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

	public double getSaleAmount() {
		return saleAmount;
	}

	public void setSaleAmount(double saleAmount) {
		this.saleAmount = saleAmount;
	}

	public double getWinningAmount() {
		return winningAmount;
	}

	public void setWinningAmount(double winningAmount) {
		this.winningAmount = winningAmount;
	}

	@Override
	public boolean evaluate(Object object) {
		MerchantWiseSalePwtReportBean reportBean = (MerchantWiseSalePwtReportBean) object;
		return (this.gameId == reportBean.gameId) && (this.gameTypeId == reportBean.gameTypeId);
	}
}