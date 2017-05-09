package com.skilrock.sle.drawMgmt.jackpotDaoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import com.skilrock.sle.common.DBConnect;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.drawMgmt.jackpotDao.JackPotCalculationDao;
import com.skilrock.sle.drawMgmt.javaBeans.SLPrizeRankDistributionBean;

public class Soccer4JackPotDaoImpl implements JackPotCalculationDao {

	private static Soccer4JackPotDaoImpl instance;

	private Soccer4JackPotDaoImpl(){}

	public static Soccer4JackPotDaoImpl getInstance() {
		if (instance == null) {
			synchronized (Soccer4JackPotDaoImpl.class) {
				if (instance == null) {
					instance = new Soccer4JackPotDaoImpl();
				}
			}
		}

		return instance;
	}

	
	private double prizePayoutRatio;
	
	@Override
	public void setJackpotSalePercent(double percent) {
		// TODO Auto-generated method stub
		
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
		double fundTobeDistributedEachplayerRank = 0.00;
		try {
			stmt = connection.createStatement();
			pstmt = connection.prepareStatement("UPDATE st_sl_prize_details_"+gameId+"_"+gameTypeId+" SET prize_amount=? WHERE prize_rank=? AND draw_id="+drawId+";");
			rs = stmt.executeQuery("SELECT SUM(no_of_winners) no_of_winners, prize_rank FROM st_sl_prize_details_"+gameId+"_"+gameTypeId+" WHERE draw_id="+drawId+" GROUP BY prize_rank ORDER BY prize_rank ASC;");
			if (totalSaleAmt != 0.0) {
				
				prizeFund = totalSaleAmt*prizePayoutRatio/100;
				
				while (rs.next()) {
					int numberOfWinner = rs.getInt("no_of_winners");
					int prizeRank = rs.getInt("prize_rank");
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
				
			}else{
				while (rs.next()) {
					int prizeRank = rs.getInt("prize_rank");
					pstmt.setDouble(1, rankMap.get(prizeRank).getMinPrizeValue());
					pstmt.setInt(2, prizeRank);
					pstmt.addBatch();
				}
			}
			pstmt.executeBatch();
		} catch (SQLException se) {
			se.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally{
			DBConnect.closeConnection(pstmt, rs);
			DBConnect.closeStatement(stmt);
		}
	}
	
	public void calculationForJackpotForSimnet(int gameId, int gameTypeId, int drawId, Map<Integer, SLPrizeRankDistributionBean> rankMap, double totalSaleAmt, Connection connection) throws SLEException {
		//not applicable for soccer 4 as of now
		}
	
}