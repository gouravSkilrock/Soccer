package com.skilrock.sle.common;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.skilrock.sle.common.exception.SLEException;

public class MerchantAuthorizationHelper {
	static int merchantAuthorization(String userName, String password)
			throws SLEException {
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		int merchantId = 0;
		try {
			con = DBConnect.getConnection();
			stmt = con.createStatement();

			rs = stmt
					.executeQuery("select SQL_CACHE merchant_id from st_sl_merchant_master where merchant_user_name='"
							+ userName
							+ "' and merchant_password='"
							+ password
							+ "' and status='ACTIVE'");

			if (rs.next()) {
				merchantId = rs.getInt("merchant_id");
			} else {
				throw new SLEException();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SLEException();
		} finally {
			DBConnect.closeConnection(con);
		}
		return merchantId;
	}
}
