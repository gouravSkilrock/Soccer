package com.skilrock.sle.drawMgmt.jackpotDao;

import java.sql.Connection;
import java.util.Map;

import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.drawMgmt.javaBeans.SLPrizeRankDistributionBean;

public interface JackPotCalculationDao {
	public void calculationForJackpot(int gameId, int gameTypeId, int drawId, Map<Integer, SLPrizeRankDistributionBean> rankMap, double totalSaleAmt, Connection connection) throws SLEException;
	public void calculationForJackpotForSimnet(int gameId, int gameTypeId, int drawId, Map<Integer, SLPrizeRankDistributionBean> rankMap, double totalSaleAmt, Connection connection) throws SLEException;
	public void setJackpotSalePercent(double percent);
	public void setPrizePayoutRatio(double ppr);
}