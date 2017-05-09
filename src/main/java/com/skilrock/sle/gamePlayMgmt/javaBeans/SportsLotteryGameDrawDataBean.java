package com.skilrock.sle.gamePlayMgmt.javaBeans;

import java.util.Arrays;
import java.util.Map;

public class SportsLotteryGameDrawDataBean {
	private int drawId;
	private int eventId;
	private SportsLotteryGameEventDataBean[] gameEventDataBeanArray = null;
	private int noOfLines;
	private int betAmountMultiple;
	private double boardPurchaseAmount;
	private String drawDateTime;
	private String drawDisplayname;
	private String drawClaimStartTime;
	private String drawClaimEndTime;
	private String drawVerificationStartTime;
	private Map<Integer, PrizeRankDrawWinningBean> drawPrizeRankMap;

	public int getDrawId() {
		return drawId;
	}

	public void setDrawId(int drawId) {
		this.drawId = drawId;
	}

	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	public SportsLotteryGameEventDataBean[] getGameEventDataBeanArray() {
		return gameEventDataBeanArray;
	}

	public void setGameEventDataBeanArray(
			SportsLotteryGameEventDataBean[] gameEventDataBeanArray) {
		this.gameEventDataBeanArray = gameEventDataBeanArray;
	}

	public int getNoOfLines() {
		return noOfLines;
	}

	public void setNoOfLines(int noOfLines) {
		this.noOfLines = noOfLines;
	}

	public int getBetAmountMultiple() {
		return betAmountMultiple;
	}

	public void setBetAmountMultiple(int betAmountMultiple) {
		this.betAmountMultiple = betAmountMultiple;
	}

	public double getBoardPurchaseAmount() {
		return boardPurchaseAmount;
	}

	public void setBoardPurchaseAmount(double boardPurchaseAmount) {
		this.boardPurchaseAmount = boardPurchaseAmount;
	}

	public String getDrawDateTime() {
		return drawDateTime;
	}

	public void setDrawDateTime(String drawDateTime) {
		this.drawDateTime = drawDateTime;
	}

	public String getDrawDisplayname() {
		return drawDisplayname;
	}

	public void setDrawDisplayname(String drawDisplayname) {
		this.drawDisplayname = drawDisplayname;
	}

	public String getDrawClaimStartTime() {
		return drawClaimStartTime;
	}

	public void setDrawClaimStartTime(String drawClaimStartTime) {
		this.drawClaimStartTime = drawClaimStartTime;
	}

	public String getDrawClaimEndTime() {
		return drawClaimEndTime;
	}

	public void setDrawClaimEndTime(String drawClaimEndTime) {
		this.drawClaimEndTime = drawClaimEndTime;
	}

	public String getDrawVerificationStartTime() {
		return drawVerificationStartTime;
	}

	public void setDrawVerificationStartTime(String drawVerificationStartTime) {
		this.drawVerificationStartTime = drawVerificationStartTime;
	}

	public Map<Integer, PrizeRankDrawWinningBean> getDrawPrizeRankMap() {
		return drawPrizeRankMap;
	}

	public void setDrawPrizeRankMap(
			Map<Integer, PrizeRankDrawWinningBean> drawPrizeRankMap) {
		this.drawPrizeRankMap = drawPrizeRankMap;
	}

	@Override
	public String toString() {
		return "SportsLotteryGameDrawDataBean [drawId=" + drawId + ", eventId="
				+ eventId + ", gameEventDataBeanArray="
				+ Arrays.toString(gameEventDataBeanArray) + ", noOfLines="
				+ noOfLines + ", betAmountMultiple=" + betAmountMultiple
				+ ", boardPurchaseAmount=" + boardPurchaseAmount
				+ ", drawDateTime=" + drawDateTime + ", drawDisplayname="
				+ drawDisplayname + ", drawClaimStartTime="
				+ drawClaimStartTime + ", drawClaimEndTime=" + drawClaimEndTime
				+ ", drawVerificationStartTime=" + drawVerificationStartTime
				+ ", drawPrizeRankMap=" + drawPrizeRankMap + "]";
	}

}
