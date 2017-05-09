package com.skilrock.sle.drawMgmt.jackpotDaoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import com.skilrock.sle.common.DBConnect;
import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.drawMgmt.jackpotDao.JackPotCalculationDao;
import com.skilrock.sle.drawMgmt.javaBeans.SLPrizeRankDistributionBean;

public class Soccer13JackPotDaoImpl implements JackPotCalculationDao {

	private static Soccer13JackPotDaoImpl instance;

	private Soccer13JackPotDaoImpl(){}

	public static Soccer13JackPotDaoImpl getInstance() {
		if (instance == null) {
			synchronized (Soccer13JackPotDaoImpl.class) {
				if (instance == null) {
					instance = new Soccer13JackPotDaoImpl();
				}
			}
		}
		return instance;
	}
	private double prizePayoutRatio;
	private double jackpotSalePercent;

	@Override
	public void setJackpotSalePercent(double percent) {
		jackpotSalePercent = percent;
	}

	@Override
	public void setPrizePayoutRatio(double ppr) {
		prizePayoutRatio = ppr;
	}
	
	@Override
	public void calculationForJackpot(int gameId, int gameTypeId, int drawId, Map<Integer, SLPrizeRankDistributionBean> rankMap, double totalSaleAmt, Connection connection) throws SLEException {
		Statement stmt = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		double prizeFund = 0.0;
		double jackpotToBeRolledOver = 0.0;
		double totalAvailableJackpotAmt = 0.0;
		double remainingPoolFund = 0.0;
		int rollOverCount = 0;
		double cariedOverJackpot = 0.00;
		double jackpotForThisDraw = 0.00;
		double fundTobeDistributedEachplayerRank = 0.00;
		double fundTobeDistributedEachplayerRank1 = 0.00;
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery("select carried_over_jackpot from st_sl_miscellaneous_"+gameId+"_"+gameTypeId+" order by id desc limit 1");

			if (rs.next()) {
				cariedOverJackpot = rs.getDouble("carried_over_jackpot");
			}
			
			jackpotToBeRolledOver = cariedOverJackpot;
			totalAvailableJackpotAmt = cariedOverJackpot;
			pstmt = connection.prepareStatement("UPDATE st_sl_prize_details_"+gameId+"_"+gameTypeId+" SET prize_amount=? WHERE prize_rank=? AND draw_id="+drawId+";");
			rs = stmt.executeQuery("SELECT SUM(no_of_winners) no_of_winners, prize_rank FROM st_sl_prize_details_"+gameId+"_"+gameTypeId+" WHERE draw_id="+drawId+" GROUP BY prize_rank ORDER BY prize_rank ASC;");
			if (totalSaleAmt != 0.0) {
				prizeFund = totalSaleAmt*prizePayoutRatio/100;
				jackpotForThisDraw = totalSaleAmt * jackpotSalePercent * .01; 
				jackpotToBeRolledOver = jackpotToBeRolledOver + jackpotForThisDraw;
				totalAvailableJackpotAmt = totalAvailableJackpotAmt + jackpotForThisDraw;
			
				Map<Integer, Integer> winnerMap = new HashMap<Integer, Integer>();
				while (rs.next()) {
					int numberOfWinner = rs.getInt("no_of_winners");
					int prizeRank = rs.getInt("prize_rank");
					winnerMap.put(prizeRank, numberOfWinner);
					int noOfWinnerHavingRank1 = winnerMap.get(prizeRank);
					if (prizeRank == 1 && noOfWinnerHavingRank1 != 0) {
						double fundTobeDistributedRank1 =rankMap.get(prizeRank).getPrizeValue()* prizeFund/100 + cariedOverJackpot;
						jackpotToBeRolledOver = 0.0;
						//totalAvailableJackpotAmt = 0.0;
						jackpotForThisDraw = rankMap.get(prizeRank).getPrizeValue()* prizeFund/100;
						totalAvailableJackpotAmt = cariedOverJackpot + jackpotForThisDraw;
						fundTobeDistributedEachplayerRank1 = fundTobeDistributedRank1 / noOfWinnerHavingRank1;
						if(fundTobeDistributedRank1 < rankMap.get(prizeRank).getMinPrizeValue()){
							fundTobeDistributedEachplayerRank1 = rankMap.get(prizeRank).getMinPrizeValue() / noOfWinnerHavingRank1;
							jackpotForThisDraw = rankMap.get(prizeRank).getMinPrizeValue();
							totalAvailableJackpotAmt = jackpotForThisDraw; 
						}							
						rankMap.get(prizeRank).setPrizeValue(fundTobeDistributedEachplayerRank1);
						pstmt.setDouble(1, fundTobeDistributedEachplayerRank1);
						pstmt.setInt(2, prizeRank);
						pstmt.addBatch();
					 } else{
							double fundTobeDistributedRank = rankMap.get(prizeRank).getPrizeValue()*prizeFund/100;		
							if(numberOfWinner != 0){
								fundTobeDistributedEachplayerRank = fundTobeDistributedRank / numberOfWinner;
							} else{
								fundTobeDistributedEachplayerRank = fundTobeDistributedRank;
							}
							if(fundTobeDistributedEachplayerRank < rankMap.get(prizeRank).getMinPrizeValue()){
								fundTobeDistributedEachplayerRank = rankMap.get(prizeRank).getMinPrizeValue();
							}
							rankMap.get(prizeRank).setPrizeValue(fundTobeDistributedEachplayerRank);
							pstmt.setDouble(1, fundTobeDistributedEachplayerRank);
							pstmt.setInt(2, prizeRank);
							pstmt.addBatch();
					 	}
					} 
				
				}else{
					while (rs.next()) {
						int prizeRank = rs.getInt("prize_rank");
						pstmt.setDouble(1, rankMap.get(prizeRank).getMinPrizeValue());
						pstmt.setInt(2, prizeRank);
						pstmt.addBatch();
					}
				}
				
			pstmt.executeBatch();
			double rsrToBeRolledOver = 0.0;
			double rsrForThisDraw = 0.0;
			double totalAvailableRSR = 0.0;
			double utilizedRSR = 0.0;
			double fixedPrizesFund = 0.0;
			stmt.executeUpdate("INSERT INTO st_sl_miscellaneous_"+gameId+"_"+gameTypeId+" (draw_id, roll_over_count, carried_over_RSR, carried_over_jackpot,prize_fund, RSR_for_this_draw, total_available_RSR, RSR_utilized_in_this_draw, jackpot_for_this_draw, total_available_jackpot, fixed_prizes_fund, remaining_prize_fund) VALUES ("+drawId+","+rollOverCount+","+rsrToBeRolledOver+","+jackpotToBeRolledOver+","+prizeFund+","+rsrForThisDraw+","+totalAvailableRSR+","+utilizedRSR+","+jackpotForThisDraw+","+totalAvailableJackpotAmt+","+fixedPrizesFund+","+remainingPoolFund+");");

			/*	Jackpot Update in Context Start	*/
			Map<Integer, Double> gameTypeJackpotMap = Util.jackpotMap.get(gameId);
			if(gameTypeJackpotMap != null) {
				gameTypeJackpotMap.put(gameTypeId, jackpotToBeRolledOver);
			}
			/*	Jackpot Update in Context End	*/
		} catch (SQLException se) {
			se.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally{
			DBConnect.closeConnection(pstmt, stmt, rs);
		}
	}
	
	public void calculationForJackpotForSimnet(int gameId, int gameTypeId, int drawId, Map<Integer, SLPrizeRankDistributionBean> rankMap, double totalSaleAmt, Connection connection) throws SLEException {
	//not applicable for soccer 13 as of now
	}
	
}