package com.skilrock.sle.gameDataMgmt.javaBeans;

public class SLEDrawWinningBean {
	
	private int noOfMatches;
	private long noOfWInniners;
	private double prizeAmount;
	
	public int getNoOfMatches() {
		return noOfMatches;
	}
	public void setNoOfMatches(int noOfMatches) {
		this.noOfMatches = noOfMatches;
	}
	public long getNoOfWInniners() {
		return noOfWInniners;
	}
	public void setNoOfWInniners(long noOfWInniners) {
		this.noOfWInniners = noOfWInniners;
	}
	public void setPrizeAmount(double prizeAmount) {
		this.prizeAmount = prizeAmount;
	}
	public double getPrizeAmount() {
		return prizeAmount;
	}
	@Override
	public String toString() {
		return "SLEDrawWinningBean [noOfMatches=" + noOfMatches
				+ ", noOfWInniners=" + noOfWInniners + ", prizeAmount="
				+ prizeAmount + "]";
	}
	
}
