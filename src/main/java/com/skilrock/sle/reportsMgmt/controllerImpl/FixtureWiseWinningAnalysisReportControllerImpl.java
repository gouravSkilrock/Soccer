package com.skilrock.sle.reportsMgmt.controllerImpl;

import java.sql.Connection;
import java.sql.Date;
import java.util.Map;

import com.skilrock.sle.common.DBConnect;
import com.skilrock.sle.common.SLELogger;
import com.skilrock.sle.common.SportsLotteryUtils;
import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.reportsMgmt.daoImpl.FixtureWiseWinningAnalysisReportDaoImpl;
import com.skilrock.sle.reportsMgmt.javaBeans.FixtureWiseWinningAnalysisReportBean;

public class FixtureWiseWinningAnalysisReportControllerImpl {
	private static final SLELogger logger = SLELogger.getLogger(FixtureWiseWinningAnalysisReportControllerImpl.class.getName());

	private static FixtureWiseWinningAnalysisReportControllerImpl instance;

	private FixtureWiseWinningAnalysisReportControllerImpl(){}

	public static FixtureWiseWinningAnalysisReportControllerImpl getInstance() {
		if (instance == null) {
			synchronized (FixtureWiseWinningAnalysisReportControllerImpl.class) {
				if (instance == null) {
					instance = new FixtureWiseWinningAnalysisReportControllerImpl();
				}
			}
		}

		return instance;
	}

	public Map<Integer, String> getDrawData(int merchantId, int gameId, int gameTypeId, String selectedDate) throws SLEException {
		logger.info("--- getDrawData (Controller) ---");

		Connection connection = null;
		Map<Integer, String> drawMap = null;
		try {
			connection = DBConnect.getConnection();
			String lastArchDate=SportsLotteryUtils.getLastArchDate(connection);
			if(lastArchDate!=null && !Util.StringToDateConversion(selectedDate.replace("/","-")+" 00:00:00").after(Util.StringToDateConversion(lastArchDate+" 00:00:00"))){
				throw new SLEException(SLEErrors.ARCH_DATE_LIMIT_ERROR_CODE,SLEErrors.ARCH_DATE_LIMIT_ERROR_MESSAGE+" "+lastArchDate.replace("-", "/"));
			}
			drawMap = FixtureWiseWinningAnalysisReportDaoImpl.getInstance().getDrawData(merchantId, gameId, gameTypeId, selectedDate, connection);
		} catch (SLEException se) {
			throw se;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(connection);
		}

		return drawMap;
	}

	public Map<Integer, FixtureWiseWinningAnalysisReportBean> getFixtureWinningData(int gameId, int gameTypeId, int drawId) throws SLEException {
		logger.info("--- getFixtureWinningData (Controller) ---");

		Connection connection = null;
		Map<Integer, FixtureWiseWinningAnalysisReportBean> reportMap = null;
		try {
			connection = DBConnect.getConnection();
			reportMap = FixtureWiseWinningAnalysisReportDaoImpl.getInstance().getFixtureWinningData(gameId, gameTypeId, drawId, connection);
		} catch (SLEException se) {
			throw se;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(connection);
		}

		return reportMap;
	}
}