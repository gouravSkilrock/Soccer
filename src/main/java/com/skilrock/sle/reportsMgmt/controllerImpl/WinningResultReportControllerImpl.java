package com.skilrock.sle.reportsMgmt.controllerImpl;

import java.sql.Connection;
import java.util.List;

import com.skilrock.sle.common.DBConnect;
import com.skilrock.sle.common.UtilityFunctions;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.reportsMgmt.daoImpl.WinningResultReportDaoImpl;
import com.skilrock.sle.reportsMgmt.javaBeans.WinningResultReportBean;

/**
 * @author Shobhit
 * @category Game data
 */
public class WinningResultReportControllerImpl {

	public List<WinningResultReportBean> winningResultReportSearch(int gameId, int gameTypeId, String merchantName) throws SLEException {
		Connection connection = null;
		int merchantId = 0;
		WinningResultReportDaoImpl daoImpl = new WinningResultReportDaoImpl();;
		List<WinningResultReportBean> winningResultReportList = null;
		try {
			connection = DBConnect.getConnection();
			merchantId = UtilityFunctions.getMerchantIdFromMerchantName(merchantName, connection);
			winningResultReportList = daoImpl.getWinningDataReport(gameId, gameTypeId, merchantId, connection);
		} catch (SLEException se) {
			throw se;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(connection);
		}

		return winningResultReportList;
	}
	
	public List<WinningResultReportBean> winningDrawResultReportSearch(int gameId, int gameTypeId, String merchantName) throws SLEException {
		Connection connection = null;
		int merchantId = 0;
		WinningResultReportDaoImpl daoImpl = new WinningResultReportDaoImpl();;
		List<WinningResultReportBean> winningResultReportList = null;
		try {
			connection = DBConnect.getConnection();
			merchantId = UtilityFunctions.getMerchantIdFromMerchantName(merchantName, connection);
			winningResultReportList = daoImpl.getDrawDataReport(gameId, gameTypeId, merchantId, connection);
		} catch (SLEException se) {
			throw se;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(connection);
		}

		return winningResultReportList;
	}
	
	public WinningResultReportBean winningResultForDraw(int gameId, int gameTypeId,int drawId, String merchantName) throws SLEException {
		Connection connection = null;
		int merchantId = 0;
		WinningResultReportDaoImpl daoImpl = new WinningResultReportDaoImpl();
		WinningResultReportBean resultReportBean = null;
		try {
			connection = DBConnect.getConnection();
			merchantId = UtilityFunctions.getMerchantIdFromMerchantName(merchantName, connection);
			resultReportBean = daoImpl.winningResultForDraw(gameId, gameTypeId,drawId, merchantId, connection);
		} catch (SLEException se) {
			throw se;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(connection);
		}

		return resultReportBean;
	}
}