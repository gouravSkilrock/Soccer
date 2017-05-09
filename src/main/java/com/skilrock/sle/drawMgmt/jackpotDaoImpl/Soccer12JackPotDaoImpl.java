package com.skilrock.sle.drawMgmt.jackpotDaoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.drawMgmt.jackpotDao.JackPotCalculationDao;
import com.skilrock.sle.drawMgmt.javaBeans.SLPrizeRankDistributionBean;

public class Soccer12JackPotDaoImpl implements JackPotCalculationDao {

	private static Soccer12JackPotDaoImpl instance;

	private Soccer12JackPotDaoImpl(){}

	public static Soccer12JackPotDaoImpl getInstance() {
		if (instance == null) {
			synchronized (Soccer12JackPotDaoImpl.class) {
				if (instance == null) {
					instance = new Soccer12JackPotDaoImpl();
				}
			}
		}

		return instance;
	}

	private static class Constants {
		private static final int PPR = 45;
		private static final int ROLL_DOWN_UPTO_RANK = 3;
		//private static final int ROLL_OVER_UPTO_DRAW = 3;
		private static final int ROLL_OVER_UPTO_DRAW = Integer.parseInt(Util.getPropertyValue("ROLL_OVER_UPTO_DRAW"));
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
		try {
			stmt = connection.createStatement();

			int rollOverCount = 0;
			//double rolledOverJackpotAmt = 0.0;
			/*rs = stmt.executeQuery("SELECT roll_over_count, carried_over_RSR, carried_over_jackpot FROM st_sl_miscellaneous_"+gameId+"_"+gameTypeId+" ORDER BY id DESC LIMIT 1;");
			if (rs.next()) {
				rollOverCount = rs.getInt("roll_over_count");
				rolledOverJackpotAmt = rs.getDouble("carried_over_jackpot");
			}*/
			double rolledOverJackpotAmt = Double.parseDouble(Util.getPropertyValue("HARDCODED_JACKPOT_AMOUNT"));

			double jackpotForThisDraw = 0.00;
			if (totalSaleAmt != 0.0) {
				rs = stmt.executeQuery("SELECT SUM(no_of_winners) no_of_winners, prize_rank FROM st_sl_prize_details_"+gameId+"_"+gameTypeId+" WHERE draw_id="+drawId+" GROUP BY prize_rank ORDER BY prize_rank ASC;");
				Map<Integer, Integer> winnerMap = new HashMap<Integer, Integer>();
				while (rs.next()) {
					winnerMap.put(rs.getInt("prize_rank"), rs.getInt("no_of_winners"));
				}

				prizeFund = totalSaleAmt*Constants.PPR/100;
				totalAvailableJackpotAmt = prizeFund;

				int noOfWinnerHavingRank1 = winnerMap.get(1);
				if (noOfWinnerHavingRank1 != 0) {
					double fundTobeDistributedRank1 = rankMap.get(1).getPrizeValue()*prizeFund/100 + rolledOverJackpotAmt;
					jackpotForThisDraw = fundTobeDistributedRank1;
					double fundTobeDistributedEachplayerRank1 = 0.00;

					fundTobeDistributedEachplayerRank1 = fundTobeDistributedRank1 / noOfWinnerHavingRank1;
					rankMap.get(1).setPrizeValue(fundTobeDistributedEachplayerRank1);
					pstmt = connection.prepareStatement("UPDATE st_sl_prize_details_"+gameId+"_"+gameTypeId+" SET prize_amount="+fundTobeDistributedEachplayerRank1+" WHERE prize_rank=1 AND draw_id="+drawId+";");
					pstmt.executeUpdate();
				} else {
					jackpotToBeRolledOver = rankMap.get(1).getPrizeValue()*prizeFund/100 + rolledOverJackpotAmt;
				}

				for(int rank=2; rank<=Constants.ROLL_DOWN_UPTO_RANK; rank++) {
					int noOfWinners = winnerMap.get(rank);

					if(noOfWinners != 0) {
						double fundTobeDistributed = 0.00;
						if(rollOverCount == Constants.ROLL_OVER_UPTO_DRAW-1) {
							fundTobeDistributed = rankMap.get(rank).getPrizeValue()*prizeFund/100 + jackpotToBeRolledOver;
							jackpotToBeRolledOver = 0.00;
						} else {
							fundTobeDistributed = rankMap.get(rank).getPrizeValue()*prizeFund/100;
						}
						double fundTobeDistributedEachPlayer = 0.00;

						fundTobeDistributedEachPlayer = fundTobeDistributed/ noOfWinners;
						rankMap.get(rank).setPrizeValue(fundTobeDistributedEachPlayer);
						pstmt = connection.prepareStatement("UPDATE st_sl_prize_details_"+gameId+"_"+gameTypeId+" SET prize_amount="+fundTobeDistributedEachPlayer+" WHERE prize_rank="+rank+" AND draw_id="+drawId+";");
						pstmt.executeUpdate();
					}
				}
			}

			if(rollOverCount == Constants.ROLL_OVER_UPTO_DRAW && jackpotToBeRolledOver != 0) {
				jackpotToBeRolledOver = 0.00;
			}

			if(jackpotToBeRolledOver == 0) {
				rollOverCount = 0;
			} else {
				rollOverCount += 1;
			}

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
		}
	}

	//Note the below Method is for calculation for SIMNET and not to be used it elsewhere
	public void calculationForJackpotForSimnet(int gameId, int gameTypeId, int drawId, Map<Integer, SLPrizeRankDistributionBean> rankMap, double totalSaleAmt, Connection connection) throws SLEException {
		Statement stmt = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		double prizeFund = 0.0;
		double jackpotToBeRolledOver = 0.0;
		double totalAvailableJackpotAmt = 0.0;
		double remainingPoolFund = 0.0;
		try {
			stmt = connection.createStatement();

			int rollOverCount = 0;
			//double rolledOverJackpotAmt = 0.0;
			/*rs = stmt.executeQuery("SELECT roll_over_count, carried_over_RSR, carried_over_jackpot FROM st_sl_miscellaneous_"+gameId+"_"+gameTypeId+" ORDER BY id DESC LIMIT 1;");
			if (rs.next()) {
				rollOverCount = rs.getInt("roll_over_count");
				rolledOverJackpotAmt = rs.getDouble("carried_over_jackpot");
			}*/
			double rolledOverJackpotAmt = Double.parseDouble(Util.getPropertyValue("HARDCODED_JACKPOT_AMOUNT"));

			double jackpotForThisDraw = 0.00;
			if (totalSaleAmt != 0.0) {
				rs = stmt.executeQuery("SELECT SUM(no_of_winners) no_of_winners, prize_rank FROM st_sl_prize_details_"+gameId+"_"+gameTypeId+" WHERE draw_id="+drawId+" GROUP BY prize_rank ORDER BY prize_rank ASC;");
				Map<Integer, Integer> winnerMap = new HashMap<Integer, Integer>();
				while (rs.next()) {
					winnerMap.put(rs.getInt("prize_rank"), rs.getInt("no_of_winners"));
				}

				prizeFund = totalSaleAmt*Constants.PPR/100;
				totalAvailableJackpotAmt = prizeFund;

				int noOfWinnerHavingRank1 = winnerMap.get(1);
				if (noOfWinnerHavingRank1 != 0) {
					double fundTobeDistributedRank1 = rankMap.get(1).getPrizeValue()*prizeFund/100 + rolledOverJackpotAmt;
					jackpotForThisDraw = fundTobeDistributedRank1;
					double fundTobeDistributedEachplayerRank1 = 0.00;

					fundTobeDistributedEachplayerRank1 = fundTobeDistributedRank1 / noOfWinnerHavingRank1;
					rankMap.get(1).setPrizeValue(fundTobeDistributedEachplayerRank1);
					pstmt = connection.prepareStatement("UPDATE st_sl_prize_details_"+gameId+"_"+gameTypeId+" SET prize_amount="+fundTobeDistributedEachplayerRank1+" WHERE prize_rank=1 AND draw_id="+drawId+";");
					pstmt.executeUpdate();
				} else {
					jackpotToBeRolledOver = rankMap.get(1).getPrizeValue()*prizeFund/100 + rolledOverJackpotAmt;
				}

				for(int rank=2; rank<=Constants.ROLL_DOWN_UPTO_RANK; rank++) {
					int noOfWinners = winnerMap.get(rank);

					if(noOfWinners != 0) {
						double fundTobeDistributed = 0.00;
						if(rollOverCount == Constants.ROLL_OVER_UPTO_DRAW-1) {
							fundTobeDistributed = rankMap.get(rank).getPrizeValue()*prizeFund/100 + jackpotToBeRolledOver;
							jackpotToBeRolledOver = 0.00;
						} else {
							fundTobeDistributed = rankMap.get(rank).getPrizeValue()*prizeFund/100;
						}
						double fundTobeDistributedEachPlayer = 0.00;

						fundTobeDistributedEachPlayer = fundTobeDistributed/ noOfWinners;
						rankMap.get(rank).setPrizeValue(fundTobeDistributedEachPlayer);
						pstmt = connection.prepareStatement("UPDATE st_sl_prize_details_"+gameId+"_"+gameTypeId+" SET prize_amount="+fundTobeDistributedEachPlayer+" WHERE prize_rank="+rank+" AND draw_id="+drawId+";");
						pstmt.executeUpdate();
					}
				}
			}

			if(rollOverCount == Constants.ROLL_OVER_UPTO_DRAW && jackpotToBeRolledOver != 0) {
				jackpotToBeRolledOver = 0.00;
			}

			if(jackpotToBeRolledOver == 0) {
				rollOverCount = 0;
			} else {
				rollOverCount += 1;
			}

			double rsrToBeRolledOver = 0.0;
			double rsrForThisDraw = 0.0;
			double totalAvailableRSR = 0.0;
			double utilizedRSR = 0.0;
			double fixedPrizesFund = 0.0;
			stmt.executeUpdate("INSERT INTO st_sl_miscellaneous_simnet_"+gameId+"_"+gameTypeId+" (draw_id, roll_over_count, carried_over_RSR, carried_over_jackpot,prize_fund, RSR_for_this_draw, total_available_RSR, RSR_utilized_in_this_draw, jackpot_for_this_draw, total_available_jackpot, fixed_prizes_fund, remaining_prize_fund) VALUES ("+drawId+","+rollOverCount+","+rsrToBeRolledOver+","+jackpotToBeRolledOver+","+prizeFund+","+rsrForThisDraw+","+totalAvailableRSR+","+utilizedRSR+","+jackpotForThisDraw+","+totalAvailableJackpotAmt+","+fixedPrizesFund+","+remainingPoolFund+");");
            
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
		}
	}
	@Override
	public void setJackpotSalePercent(double percent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPrizePayoutRatio(double ppr) {
		// TODO Auto-generated method stub
		
	}
}