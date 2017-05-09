package com.skilrock.sle.reportsMgmt.javaBeans;

import java.io.Serializable;

public class WinningDataBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private double rankOneWinningAmt;
	private int rankOneTotalPlayers;
	private double rankTwoWinningAmt;
	private int rankTwoTotalPlayers;
	private double rankThreeWinningAmt;
	private int rankThreeTotalPlayers;
	private double totalWinningAmt;
	private int totalPlayers;
	
	
	
	public int getTotalPlayers() {
		return totalPlayers;
	}



	public void setTotalPlayers(int totalPlayers) {
		this.totalPlayers = totalPlayers;
	}



	public double getRankOneWinningAmt() {
		return rankOneWinningAmt;
	}



	public void setRankOneWinningAmt(double rankOneWinningAmt) {
		this.rankOneWinningAmt = rankOneWinningAmt;
	}



	public int getRankOneTotalPlayers() {
		return rankOneTotalPlayers;
	}



	public void setRankOneTotalPlayers(int rankOneTotalPlayers) {
		this.rankOneTotalPlayers = rankOneTotalPlayers;
	}



	public double getRankTwoWinningAmt() {
		return rankTwoWinningAmt;
	}



	public void setRankTwoWinningAmt(double rankTwoWinningAmt) {
		this.rankTwoWinningAmt = rankTwoWinningAmt;
	}



	public int getRankTwoTotalPlayers() {
		return rankTwoTotalPlayers;
	}



	public void setRankTwoTotalPlayers(int rankTwoTotalPlayers) {
		this.rankTwoTotalPlayers = rankTwoTotalPlayers;
	}



	public double getRankThreeWinningAmt() {
		return rankThreeWinningAmt;
	}



	public void setRankThreeWinningAmt(double rankThreeWinningAmt) {
		this.rankThreeWinningAmt = rankThreeWinningAmt;
	}



	public int getRankThreeTotalPlayers() {
		return rankThreeTotalPlayers;
	}



	public void setRankThreeTotalPlayers(int rankThreeTotalPlayers) {
		this.rankThreeTotalPlayers = rankThreeTotalPlayers;
	}



	public WinningDataBean() {
	}



	public void setTotalWinningAmt(double totalWinningAmt) {
		this.totalWinningAmt = totalWinningAmt;
	}



	public double getTotalWinningAmt() {
		return totalWinningAmt;
	}
}