package com.skilrock.sle.miscMgmt.controllerImpl;

import java.sql.Connection;

import com.skilrock.sle.common.DBConnect;
import com.skilrock.sle.common.exception.GenericException;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.miscMgmt.daoImpl.UpdateJackpotdaoImpl;
import com.skilrock.sle.miscMgmt.javabeans.jackpotBean;

public class UpdateJackpotControllerImpl {
	private static UpdateJackpotControllerImpl singleInstance;

	private UpdateJackpotControllerImpl() {
	}

	public static UpdateJackpotControllerImpl getInstance() {
		if (singleInstance == null) {
			synchronized (UpdateJackpotControllerImpl.class) {
				if (singleInstance == null) {
					singleInstance = new UpdateJackpotControllerImpl();
				}
			}
		}

		return singleInstance;
	}
	
	public jackpotBean jackpotAmount() throws GenericException {
		Connection conn = null;
		jackpotBean bean=new jackpotBean();
		try {
			conn = DBConnect.getConnection();
			conn.setAutoCommit(false);
			bean=UpdateJackpotdaoImpl.getInstance().fetchJackpotMenu(conn);
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw new GenericException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE);
		} finally {
			DBConnect.closeConnection(conn);
		}return bean;
	}

	public void updateData(double amount,String messageAmount, long userId) throws GenericException {
		Connection conn = null;
		try {
			conn = DBConnect.getConnection();
			conn.setAutoCommit(false);
			UpdateJackpotdaoImpl.getInstance().jackpotDetailsUpdation(userId, amount,messageAmount,  conn);
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw new GenericException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE);
		} finally {
			DBConnect.closeConnection(conn);
		}
	}
}
