package com.skilrock.sle.drawMgmt.javaBeans;

import java.io.Serializable;

public class SLPrizeRankDistributionBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private int matchId;
	private double prizeValue;
	private double unitPrice;
	private String prizeType;
	private int prizeRank;
	private double minPrizeValue;
	private String rankName;

	public SLPrizeRankDistributionBean() {
	}

	public SLPrizeRankDistributionBean(int matchId, double prizeValue, double unitPrice, String prizeType, int prizeRank, double minPrizeValue, String rankName) {
		this.matchId = matchId;
		this.prizeValue = prizeValue;
		this.unitPrice = unitPrice;
		this.prizeType = prizeType;
		this.prizeRank = prizeRank;
		this.minPrizeValue = minPrizeValue;
		this.rankName = rankName;
	}

	public int getMatchId() {
		return matchId;
	}

	public void setMatchId(int matchId) {
		this.matchId = matchId;
	}

	public double getPrizeValue() {
		return prizeValue;
	}

	public void setPrizeValue(double prizeValue) {
		this.prizeValue = prizeValue;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public String getPrizeType() {
		return prizeType;
	}

	public void setPrizeType(String prizeType) {
		this.prizeType = prizeType;
	}

	public int getPrizeRank() {
		return prizeRank;
	}

	public void setPrizeRank(int prizeRank) {
		this.prizeRank = prizeRank;
	}

	public double getMinPrizeValue() {
		return minPrizeValue;
	}

	public void setMinPrizeValue(double minPrizeValue) {
		this.minPrizeValue = minPrizeValue;
	}

	public String getRankName() {
		return rankName;
	}

	public void setRankName(String rankName) {
		this.rankName = rankName;
	}

	@Override
	public String toString() {
		return "SLPrizeRankDistributionBean [matchId=" + matchId
				+ ", minPrizeValue=" + minPrizeValue + ", prizeRank="
				+ prizeRank + ", prizeType=" + prizeType + ", prizeValue="
				+ prizeValue + ", rankName=" + rankName + ", unitPrice="
				+ unitPrice + "]";
	}
}