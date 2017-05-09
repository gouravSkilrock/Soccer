package com.skilrock.sle.reportsMgmt.controllerImpl;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.skilrock.sle.common.DBConnect;
import com.skilrock.sle.common.SLELogger;
import com.skilrock.sle.common.SportsLotteryUtils;
import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.merchant.common.javaBeans.MerchantInfoBean;
import com.skilrock.sle.reportsMgmt.daoImpl.MerchantWiseSalePwtReportDaoImpl;
import com.skilrock.sle.reportsMgmt.javaBeans.MerchantWiseSalePwtReportBean;
import com.skilrock.sle.reportsMgmt.javaBeans.MerchantWiseSalePwtReportBean.GameDetailsBean;

public class MerchantWiseSalePwtReportControllerImpl {
	private static final SLELogger logger = SLELogger.getLogger(MerchantWiseSalePwtReportControllerImpl.class.getName());

	private static MerchantWiseSalePwtReportControllerImpl instance;

	private MerchantWiseSalePwtReportControllerImpl(){}

	public static MerchantWiseSalePwtReportControllerImpl getInstance() {
		if (instance == null) {
			synchronized (MerchantWiseSalePwtReportControllerImpl.class) {
				if (instance == null) {
					instance = new MerchantWiseSalePwtReportControllerImpl();
				}
			}
		}

		return instance;
	}

	public Map<String, List<MerchantWiseSalePwtReportBean>> getSalePwtData(List<GameDetailsBean> gameList, Timestamp startTime, Timestamp endTime) throws SLEException {
		logger.info("--- getSalePwtData (Controller) ---");

		Connection connection = null;
		Map<String, List<MerchantWiseSalePwtReportBean>> reportMap = new TreeMap<String, List<MerchantWiseSalePwtReportBean>>();
		List<MerchantWiseSalePwtReportBean> reportList = null;
		MerchantWiseSalePwtReportBean reportBean = null;
		try {
			connection = DBConnect.getConnection();
			String countryDep=Util.getPropertyValue("COUNTRY_DEPLOYED");
			MerchantWiseSalePwtReportDaoImpl.getInstance().getGameList(gameList, connection);

			for(Map.Entry<String, MerchantInfoBean> entry : Util.merchantInfoMap.entrySet()) {
				if("GHANA".equalsIgnoreCase(countryDep) &&("RMS".equalsIgnoreCase(entry.getValue().getMerchantDevName()) || "PMS".equalsIgnoreCase(entry.getValue().getMerchantDevName())))
					continue;
				reportList = new ArrayList<MerchantWiseSalePwtReportBean>();

				for(GameDetailsBean gameBean : gameList) {
					reportBean = new MerchantWiseSalePwtReportBean();
					reportBean.setMerchantId(entry.getValue().getMerchantId());
					reportBean.setMerchantName(entry.getValue().getMerchantName());
					reportBean.setGameId(gameBean.getGameId());
					reportBean.setGameName(gameBean.getGameName());
					reportBean.setGameTypeId(gameBean.getGameTypeId());
					reportBean.setGameTypeName(gameBean.getGameTypeName());
					reportList.add(reportBean);
				}

				reportMap.put(entry.getValue().getMerchantName(), reportList);
			}

			MerchantWiseSalePwtReportDaoImpl.getInstance().saleInfo(startTime, endTime, reportMap, connection);
			MerchantWiseSalePwtReportDaoImpl.getInstance().winningInfo(startTime, endTime, reportMap, connection);
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