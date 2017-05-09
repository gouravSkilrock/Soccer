package com.skilrock.sle.drawMgmt.daoImpl;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.drawMgmt.javaBeans.SLPrizeRankDistributionBean;

public class PrizeRankDistributionDaoImpl {

	public static Map<Integer, Map<Integer, Map<Integer, SLPrizeRankDistributionBean>>> getMerchantGameTypeRankMap(int merchantId, Connection connection) throws SLEException {
		Statement stmt = null;
		ResultSet rs = null;
		Map<Integer, SLPrizeRankDistributionBean> prizeRankMap = null;
		Map<Integer, Map<Integer, SLPrizeRankDistributionBean>> gamePrizeRankMap = null;
		Map<Integer, Map<Integer, Map<Integer, SLPrizeRankDistributionBean>>> merchantGamePrizeRankMap = new HashMap<Integer, Map<Integer, Map<Integer, SLPrizeRankDistributionBean>>>();
		List<SLPrizeRankDistributionBean> prizeRankDistributionList = null;
		Type type = null;
		try {
			type = new TypeToken<List<SLPrizeRankDistributionBean>>() {}.getType();
			stmt = connection.createStatement();
			rs = stmt.executeQuery("SELECT SQL_CACHE merchant.merchant_id,game_type_id,prize_distribution_xml FROM st_sl_game_type_merchant_mapping game inner join st_sl_merchant_master merchant on game.merchant_id=merchant.merchant_id WHERE merchant.merchant_id = "+merchantId+" AND merchant.status='ACTIVE';");
			while (rs.next()) {
				if (!merchantGamePrizeRankMap.containsKey(rs.getInt("merchant_id"))) {
					gamePrizeRankMap = new HashMap<Integer, Map<Integer, SLPrizeRankDistributionBean>>();
					merchantGamePrizeRankMap.put(rs.getInt("merchant_id"), gamePrizeRankMap);
				}

				prizeRankDistributionList = new Gson().fromJson(rs.getString("prize_distribution_xml"), type);

				prizeRankMap = new HashMap<Integer, SLPrizeRankDistributionBean>();
				if (prizeRankDistributionList != null) {
					for (SLPrizeRankDistributionBean bean : prizeRankDistributionList) {
						prizeRankMap.put(bean.getPrizeRank(), bean);
					}
				}
				gamePrizeRankMap.put(rs.getInt("game_type_id"), prizeRankMap);
			}
		} catch (SQLException se) {
			se.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return merchantGamePrizeRankMap;
	}
	
	
	public Map<Integer, SLPrizeRankDistributionBean> getPrizeRankMap(int gameTypeId, int merchantId, Connection connection) throws SLEException {
		Statement stmt = null;
		ResultSet rs = null;
		Map<Integer, SLPrizeRankDistributionBean> prizeRankMap = null;
		List<SLPrizeRankDistributionBean> prizeRankDistributionList = null;
		Type type = null;
		try {
			type = new TypeToken<List<SLPrizeRankDistributionBean>>(){}.getType();
			stmt = connection.createStatement();
			rs = stmt.executeQuery("SELECT prize_distribution_xml FROM st_sl_game_type_merchant_mapping WHERE game_type_id="+gameTypeId+" AND merchant_id="+merchantId+";");
			if(rs.next()) {
				prizeRankDistributionList = new Gson().fromJson(rs.getString("prize_distribution_xml"), type);
			}
			
		} catch (SQLException se) {
			se.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}

		prizeRankMap = new HashMap<Integer, SLPrizeRankDistributionBean>();
		if(prizeRankDistributionList != null) {
			for(SLPrizeRankDistributionBean bean : prizeRankDistributionList) {
				prizeRankMap.put(bean.getPrizeRank(), bean);
			}
		}

		return prizeRankMap;
	}
	
	public static void main(String[] args) {
		new PrizeRankDistributionDaoImpl();
	}
}