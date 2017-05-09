package com.skilrock.sle.merchant.weaver;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.skilrock.sle.common.DBConnect;
import com.skilrock.sle.common.SLELogger;

public class WeaverUtils {
	private static final SLELogger logger = SLELogger.getLogger(WeaverUtils.class.getName());
	private static final String MERCHANT_NAME = "Weaver";
	public enum TxnTypes {
		WAGER, WAGER_REFUND, REFUND_AFTER_CONFIRM, WAGER_CONFIRMATION, WINNING,
	}

	public enum ServiceMethods {
		wager, plrTxn, playerProfile
	}

	public enum BALANCE_TYPE {
		MAIN, PROMO
	}

	public static final String BASE_SERVICE_TXN = "/service/api/txn/";
	public static final String SERVICE_NAME = "SPORTS_LOTTERY";
	public static final String ALIAS_NAME = getAliasName(); // "www.africalotto.co.zw";
	public static final String WALLET_TYPE = "CASH";
	public static final String BASE_SERVICE_REST = "/service/rest/";
	public static final String DEVICE = "SERVER";
	public static final String WEAVER_CONTRI_AMOUNT = "100.00";

	public static String getAliasName() {
		Connection con = null;
		Statement statement = null;
		ResultSet rs = null;

		try {
			con = DBConnect.getConnection();
			statement = con.createStatement();
			rs = statement.executeQuery("SELECT alias_name FROM st_sl_merchant_master WHERE merchant_name = '"+ MERCHANT_NAME + "'");
			while (rs.next()) {
				return rs.getString("alias_name");
			}
		} catch (Exception e) {
			logger.error("Generic exception occurred while fetching alias_name" + e);

		} finally {
			DBConnect.closeConnection(con, statement, rs);
		}

		return null;
	}

}
