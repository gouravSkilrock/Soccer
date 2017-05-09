package com.skilrock.sle.reportsMgmt.controllerImpl;

import java.sql.Connection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.skilrock.sle.common.DBConnect;
import com.skilrock.sle.common.SLELogger;
import com.skilrock.sle.common.SportsLotteryUtils;
import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.UtilityFunctions;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.reportsMgmt.daoImpl.GameDataReportDaoImpl;
import com.skilrock.sle.reportsMgmt.javaBeans.GameDataReportBean;
import com.skilrock.sle.reportsMgmt.javaBeans.SaleTrendDataBean;

/**
 * @author Shobhit
 * @category Game data
 */
public class GameDataReportControllerImpl {
	private static final SLELogger logger = SLELogger.getLogger(GameDataReportControllerImpl.class.getName());

	private static volatile GameDataReportControllerImpl classInstance;
	public static GameDataReportControllerImpl getInstance() {
		if(classInstance == null){
			synchronized (GameDataReportControllerImpl.class) {
				if(classInstance == null){
					classInstance = new GameDataReportControllerImpl();					
				}
			}
		}
		return classInstance;
	}
	
	public List<GameDataReportBean> getGameDataReport(int gameId, int gameTypeId, String startDate, String endDate, String reportType, String merchantName) throws SLEException {
		Connection connection = null;
		SimpleDateFormat simpleDateFormat = null;
		int merchantId = 0;
		Timestamp startTimeStamp = null;
		Timestamp endTimeStamp = null;
		Map<String, GameDataReportBean> gameDataReportMap = null;
		List<GameDataReportBean> gameDataReportList = null;
		try {
			connection = DBConnect.getConnection();
			simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
			gameDataReportList = new ArrayList<GameDataReportBean>();

			merchantId = UtilityFunctions.getMerchantIdFromMerchantName(merchantName, connection);

			/*if(gameTypeId==-1) {
				gameTypeArray = daoImpl.getGameTypeList(gameId, merchantId, connection);
			} else {
				gameTypeArray = new int[][]{{gameId, gameTypeId}};
			}*/

			/*if(gameTypeArray != null) {*/
				startTimeStamp = ("".equals(startDate))?null:new Timestamp(simpleDateFormat.parse(startDate).getTime());
				endTimeStamp = ("".equals(endDate))?null:new Timestamp(simpleDateFormat.parse(endDate).getTime()+(24*60*60*1000-1000));

				gameDataReportMap = GameDataReportDaoImpl.getInstance().getSaleDataReport(gameId, gameTypeId, startTimeStamp, endTimeStamp, reportType, merchantId, connection);
				GameDataReportDaoImpl.getInstance().getWinningDataReport(gameId, gameTypeId, gameDataReportMap, startTimeStamp, endTimeStamp, reportType, merchantId, connection);
			/*} else {
				throw new SLEException(SLEErrors.NO_GAME_AVAILABLE_ERROR_CODE, SLEErrors.NO_GAME_AVAILABLE_ERROR_MESSAGE);
			}*/

			for(String key : gameDataReportMap.keySet()) {
				gameDataReportList.add(gameDataReportMap.get(key));
			}
		} catch (SLEException se) {
			throw se;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(connection);
		}

		return gameDataReportList;
	}

	public List<GameDataReportBean> fetchDrawWiseTicketInfo(int merchantId, int gameId, int gameTypeId, int drawId) throws SLEException {
		logger.info("-- Inside fetchDrawWiseTicketInfo (Controller) --");
		Connection connection = null;
		List<GameDataReportBean> reportList = null;
		try {
			connection = DBConnect.getConnection();
			reportList = GameDataReportDaoImpl.getInstance().fetchDrawWiseTicketInfo(merchantId, gameId, gameTypeId, drawId, connection);
		} catch (SLEException se) {
			throw se;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(connection);
		}

		return reportList;
	}
	
	public List<SaleTrendDataBean> getDrawWiseSaleTrendData(int gameId, int gameTypeId, String startDate, String endDate, String reportType, String merchantName) throws SLEException {
		Connection connection = null;
		SimpleDateFormat simpleDateFormat = null;
		int merchantId = 0;
		Timestamp startTimeStamp = null;
		Timestamp endTimeStamp = null;
		List<SaleTrendDataBean> saleTrendDataBeans = null;
		String lastArchDate=null;
		try {
			connection = DBConnect.getConnection();
			simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
			merchantId = UtilityFunctions.getMerchantIdFromMerchantName(merchantName, connection);
			
			startTimeStamp = ("".equals(startDate))?null:new Timestamp(simpleDateFormat.parse(startDate).getTime());
			endTimeStamp = ("".equals(endDate))?null:new Timestamp(simpleDateFormat.parse(endDate).getTime()+(24*60*60*1000-1000));
			lastArchDate=SportsLotteryUtils.getLastArchDate(connection);
			if(lastArchDate!=null && !startTimeStamp.after(Util.StringToDateConversion(lastArchDate+" 00:00:00"))){
				throw new SLEException(SLEErrors.ARCH_DATE_LIMIT_ERROR_CODE,SLEErrors.ARCH_DATE_LIMIT_ERROR_MESSAGE+" "+lastArchDate.replace("-", "/"));
			}
			saleTrendDataBeans = GameDataReportDaoImpl.getInstance().getSaleTrendDataDrawWise(gameId, gameTypeId, startTimeStamp, endTimeStamp, reportType, merchantId, connection);
		} catch (SLEException se) {
			throw se;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(connection);
		}
		return saleTrendDataBeans;
	}
	
	
	public List<GameDataReportBean> getSaleReportData(int gameId, int gameTypeId, String startDate, String endDate, String merchantName) throws SLEException {
		Connection connection = null;
		SimpleDateFormat simpleDateFormat = null;
		int merchantId = 0;
		Timestamp startTimeStamp = null;
		Timestamp endTimeStamp = null;
		List<GameDataReportBean> sleSaleReportDataList = null;
		try {
			connection = DBConnect.getConnection();
			simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
			merchantId = UtilityFunctions.getMerchantIdFromMerchantName(merchantName, connection);
			startTimeStamp = ("".equals(startDate))?null:new Timestamp(simpleDateFormat.parse(startDate).getTime());
			endTimeStamp = ("".equals(endDate))?null:new Timestamp(simpleDateFormat.parse(endDate).getTime()+(24*60*60*1000-1000));

			sleSaleReportDataList = GameDataReportDaoImpl.getInstance().getSaleReportData(gameId, gameTypeId, merchantId, startTimeStamp, endTimeStamp, connection);


		} catch (SLEException se) {
			throw se;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(connection);
		}

		return sleSaleReportDataList;
	}

	
}