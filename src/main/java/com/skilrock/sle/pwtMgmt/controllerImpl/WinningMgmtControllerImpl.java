package com.skilrock.sle.pwtMgmt.controllerImpl;

import java.sql.Connection;
import java.util.Map;

import com.skilrock.sle.common.DBConnect;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.pwtMgmt.daoImpl.WinningMgmtDaoImpl;
import com.skilrock.sle.pwtMgmt.javaBeans.DrawWiseTicketInfoBean;

public class WinningMgmtControllerImpl {
	private static WinningMgmtControllerImpl singleInstance;

	private WinningMgmtControllerImpl(){}

	public static WinningMgmtControllerImpl getSingleInstance() {
		if (singleInstance == null) {
			synchronized (WinningMgmtControllerImpl.class) {
				if (singleInstance == null) {
					singleInstance = new WinningMgmtControllerImpl();
				}
			}
		}

		return singleInstance;
	}

	public void fetchWinningTickets(DrawWiseTicketInfoBean infoBean) throws SLEException {
		Connection connection = null;
		try {
			connection = DBConnect.getConnection();
			connection.setAutoCommit(false);
			WinningMgmtDaoImpl.getSingleInstance().fetchWinningTickets(infoBean, connection);
			connection.commit();
		} catch (SLEException se) {
			throw se;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(connection);
		}
	}

	public void updateWinningTransaction(int merchantId, int gameId, int gameTypeId, int drawId, Map<Long, String> transMap) throws SLEException {
		Connection connection = null;
		try {
			connection = DBConnect.getConnection();
			connection.setAutoCommit(false);
			WinningMgmtDaoImpl.getSingleInstance().updateWinningTransaction(merchantId, gameId, gameTypeId, drawId, transMap, connection);
			connection.commit();
		} catch (SLEException se) {
			throw se;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(connection);
		}
	}
}