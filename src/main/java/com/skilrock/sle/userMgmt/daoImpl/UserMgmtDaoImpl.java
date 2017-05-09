package com.skilrock.sle.userMgmt.daoImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.skilrock.sle.common.SLELogger;
import com.skilrock.sle.common.exception.GenericException;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.tp.rest.common.javaBeans.TpUserLoginNotifyBean;

public class UserMgmtDaoImpl {
	private static final SLELogger logger = SLELogger.getLogger(UserMgmtDaoImpl.class.getName());

	public void userLoginNotify(TpUserLoginNotifyBean userLoginNotifyBean, Connection con) throws GenericException {
		Statement stmt = null;
		StringBuilder query = new StringBuilder();
		
		logger.debug("***** Inside userLoginNotify Method");
		try {
			stmt = con.createStatement();
			query.append("update st_sl_merchant_user_authentication_mapping set session_id = '")
				.append(userLoginNotifyBean.getSessionId()).append("'").append(" WHERE merchant_user_id = ").append(userLoginNotifyBean.getUserId())
				.append(" AND merchant_id = ").append(userLoginNotifyBean.getMerchantId());
			
			stmt.executeUpdate(query.toString());
		} catch (SQLException e) {
			e.printStackTrace();
			throw new GenericException(SLEErrors.SQL_EXCEPTION_ERROR_CODE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new GenericException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE);
		}
	}
	
}
