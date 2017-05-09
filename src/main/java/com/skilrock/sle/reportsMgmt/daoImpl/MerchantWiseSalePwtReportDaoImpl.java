package com.skilrock.sle.reportsMgmt.daoImpl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.skilrock.sle.common.SLELogger;
import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.reportsMgmt.javaBeans.MerchantWiseSalePwtReportBean;
import com.skilrock.sle.reportsMgmt.javaBeans.MerchantWiseSalePwtReportBean.GameDetailsBean;

public class MerchantWiseSalePwtReportDaoImpl {
	private static final SLELogger logger = SLELogger.getLogger(MerchantWiseSalePwtReportDaoImpl.class.getName());

	private static MerchantWiseSalePwtReportDaoImpl instance;

	private MerchantWiseSalePwtReportDaoImpl(){}

	public static MerchantWiseSalePwtReportDaoImpl getInstance() {
		if (instance == null) {
			synchronized (MerchantWiseSalePwtReportDaoImpl.class) {
				if (instance == null) {
					instance = new MerchantWiseSalePwtReportDaoImpl();
				}
			}
		}

		return instance;
	}

	public List<GameDetailsBean> getGameList(List<GameDetailsBean> gameList, Connection connection) throws SLEException {
		logger.info("--- getGameList ---");

		Statement stmt = null;
		ResultSet rs = null;
		GameDetailsBean gameBean = null;
		try {
			stmt = connection.createStatement();
			String query = "SELECT SQL_CACHE gm.game_id, game_dev_name, game_type_id, type_dev_name FROM st_sl_game_master gm INNER JOIN st_sl_game_type_master gtm ON gm.game_id=gtm.game_id WHERE game_status='SALE_OPEN' AND type_status='SALE_OPEN' ORDER BY gm.game_id, game_type_id;";
			logger.info("getGameList Query - "+query);
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				gameBean = new GameDetailsBean();
				gameBean.setGameId(rs.getInt("game_id"));
				gameBean.setGameName(rs.getString("game_dev_name"));
				gameBean.setGameTypeId(rs.getInt("game_type_id"));
				gameBean.setGameTypeName(rs.getString("type_dev_name"));
				gameList.add(gameBean);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}

		return gameList;
	}

	/*
	public void saleInfo(Timestamp startTime, Timestamp endTime, MerchantWiseSalePwtReportBean reportBean, Connection connection) throws SLEException {
		logger.info("--- saleInfo ---");

		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = connection.createStatement();
			String query = "SELECT IFNULL(SUM(amount), 0.00) saleAmt FROM st_sl_sale_txn_master WHERE game_id="+reportBean.getGameId()+" AND game_type_id="+reportBean.getGameTypeId()+" AND merchant_id="+reportBean.getMerchantId()+" AND trans_date>='"+startTime+"' AND trans_date<='"+endTime+"' AND STATUS='DONE' AND is_cancel<>'Y';";
			logger.info("Sale Amount Query - "+query);
			rs = stmt.executeQuery(query);
			if(rs.next()) {
				reportBean.setSaleAmount(rs.getDouble("saleAmt"));
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
	}
	*/
	public void saleInfo(Timestamp startTime, Timestamp endTime, Map<String, List<MerchantWiseSalePwtReportBean>> reportMap, Connection connection) throws SLEException {
		logger.info("--- saleInfo ---");

		Statement stmt = null;
		ResultSet rs = null;

		MerchantWiseSalePwtReportBean tempBean = new MerchantWiseSalePwtReportBean();
		MerchantWiseSalePwtReportBean reportBean = null;
		try {
			stmt = connection.createStatement();
			String query = "SELECT merchant_id, game_id, game_type_id,IFNULL(SUM(saleAmt), 0.00) saleAmt  FROM(SELECT merchant_id, game_id, game_type_id, IFNULL(SUM(amount), 0.00) saleAmt FROM st_sl_sale_txn_master WHERE trans_date>='"+startTime+"' AND trans_date<='"+endTime+"' AND STATUS='DONE' AND is_cancel<>'Y' GROUP BY merchant_id, game_id, game_type_id  UNION ALL SELECT merchant_id,game_id,game_type_id,IFNULL(SUM(total_sale_amount-total_refund_amount),0.00) saleAmt FROM st_sl_merchant_wise_game_details WHERE rep_date >='"+startTime+"' AND rep_date<='"+endTime+"'  GROUP BY merchant_id, game_id, game_type_id)mainTB group by merchant_id, game_id, game_type_id;";
			logger.info("Sale Amount Query - "+query);
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				int merchantId = rs.getInt("merchant_id");
				String merchantName = Util.fetchMerchantInfoBean(merchantId).getMerchantDevName();;

				List<MerchantWiseSalePwtReportBean> reportList = reportMap.get(merchantName);
				if(reportList!=null){
				tempBean.setGameId(rs.getInt("game_id"));
				tempBean.setGameTypeId(rs.getInt("game_type_id"));

				reportBean = (MerchantWiseSalePwtReportBean) CollectionUtils.find(reportList, tempBean);
				reportBean.setSaleAmount(rs.getDouble("saleAmt"));
			}
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
	}

	/*public void winningInfo(Timestamp startTime, Timestamp endTime, Map<String, List<MerchantWiseSalePwtReportBean>> reportMap, Connection connection) throws SLEException {
		logger.info("--- winningInfo ---");

		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = connection.createStatement();

			String query = "SELECT IFNULL(SUM(amount), 0.00) winAmt FROM st_sl_winning_txn_master WHERE game_id="+reportBean.getGameId()+" AND game_type_id="+reportBean.getGameTypeId()+" AND merchant_id="+reportBean.getMerchantId()+" AND trans_date>='"+startTime+"' AND trans_date<='"+endTime+"' AND STATUS='DONE';";
			logger.info("Winning Amount Query - "+query);
			rs = stmt.executeQuery(query);
			if(rs.next()) {
				reportBean.setWinningAmount(rs.getDouble("winAmt"));
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
	}*/

	public void winningInfo(Timestamp startTime, Timestamp endTime, Map<String, List<MerchantWiseSalePwtReportBean>> reportMap, Connection connection) throws SLEException {
		logger.info("--- winningInfo ---");

		Statement stmt = null;
		ResultSet rs = null;

		MerchantWiseSalePwtReportBean tempBean = new MerchantWiseSalePwtReportBean();
		MerchantWiseSalePwtReportBean reportBean = null;
		try {
			stmt = connection.createStatement();

			String query = "SELECT merchant_id, game_id, game_type_id,IFNULL(SUM(winAmt), 0.00) winAmt  FROM(SELECT merchant_id, game_id, game_type_id, IFNULL(SUM(net_amount), 0.00) winAmt FROM st_sl_winning_txn_master WHERE trans_date>='"+startTime+"' AND trans_date<='"+endTime+"' AND STATUS='DONE' GROUP BY merchant_id, game_id, game_type_id  UNION ALL SELECT merchant_id,game_id,game_type_id,IFNULL(SUM(total_winning_amount),0.00) winAmt FROM st_sl_merchant_wise_game_details WHERE rep_date >='"+startTime+"' AND rep_date<='"+endTime+"'  GROUP BY merchant_id, game_id, game_type_id)mainTB group by merchant_id, game_id, game_type_id;";
			logger.info("Winning Amount Query - "+query);
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				int merchantId = rs.getInt("merchant_id");
				String merchantName = Util.fetchMerchantInfoBean(merchantId).getMerchantDevName();;

				List<MerchantWiseSalePwtReportBean> reportList = reportMap.get(merchantName);
				if(reportList!=null){
				tempBean.setGameId(rs.getInt("game_id"));
				tempBean.setGameTypeId(rs.getInt("game_type_id"));

				reportBean = (MerchantWiseSalePwtReportBean) CollectionUtils.find(reportList, tempBean);
				reportBean.setWinningAmount(rs.getDouble("winAmt"));
			}
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
	}
}