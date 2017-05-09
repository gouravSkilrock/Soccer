package com.skilrock.sle.filterDispatcher;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

import org.apache.struts2.StrutsStatics;
import org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter;
import org.quartz.impl.StdSchedulerFactory;

import com.skilrock.sle.common.SLELogger;
import com.skilrock.sle.common.SportsLotteryUtils;
import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.common.javaBeans.PropertyMasterBean;
import com.skilrock.sle.commonMethod.serviceImpl.CommonMethodsServiceImpl;
import com.skilrock.sle.drawMgmt.javaBeans.SLPrizeRankDistributionBean;
import com.skilrock.sle.scheduler.common.SchedularInitialization;

public class SLEMgmtFilterDispatcher extends StrutsPrepareAndExecuteFilter implements StrutsStatics, Filter {
	private static final SLELogger logger = SLELogger.getLogger(SLEMgmtFilterDispatcher.class.getName());

	public void init(FilterConfig filterConfig) throws ServletException {
		super.init(filterConfig);
		logger.debug("----SLE Filter Dispatcher Start----");
		logger.info("info");
		try {
			onStartServer();
			setMerchantInfoMap();
//			setMerchantGameTypeSale();
			setGameAndGameTypeInfoMerchantMap();
			startDrawMgmtScheduler();
			sendDrawNotficationToUser();
			//setEventDataDrawWise();
			setPrizeDistributionDataGameTypeWise();
			setDrawSaleMap();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		logger.debug("----SLE Filter Dispatcher End----");
	}

	public void onStartServer() throws SLEException {
		List<PropertyMasterBean> propertyDataList = CommonMethodsServiceImpl.getInstance().getPropertyDetail();
		Map<String, String> propertiesMap = new HashMap<String, String>();
		for (PropertyMasterBean propertyMasterBean : propertyDataList) {
			propertiesMap.put(propertyMasterBean.getPropertyDevName(), propertyMasterBean.getValue());
		}
		Util.sysPropertiesMap = propertiesMap;
		Util.jackpotMap = CommonMethodsServiceImpl.getInstance().getJackpotMap();
	}
	
	public void setMerchantInfoMap() throws SLEException {
		Util.merchantInfoMap = CommonMethodsServiceImpl.getInstance().fetchMerchantInfo();
	}
	
	/*public void setEventDataDrawWise() throws SLEException {
		SportsLotteryUtils.eventDataDrawWise = new HashMap<String, Map<Integer,DrawDetailBean>>();
		CommonMethodsServiceImpl.getInstance().setEventDataDrawWise();
	}*/
	
	public void setPrizeDistributionDataGameTypeWise() throws SLEException {
		SportsLotteryUtils.prizeRankDistributionMap = new HashMap<String, Map<Integer,SLPrizeRankDistributionBean>>();
		CommonMethodsServiceImpl.getInstance().setPrizeDistributionDataGameTypeWise();
	}
	
	public void setDrawSaleMap() throws SLEException {
		SportsLotteryUtils.drawSaleMap = new HashMap<String, Map<Integer,Double>>();
		SportsLotteryUtils.drawSaleMap = Collections.synchronizedMap(SportsLotteryUtils.drawSaleMap);
		CommonMethodsServiceImpl.getInstance().setDrawSaleMap();
	}
	
//	public void setMerchantGameTypeSale() throws SLEException {
//		int merchantId = 0;
//		Connection con = null;
//		PurchaseTicketDaoImpl.merchantGameTypeDrawSale = new HashMap<Integer, Map<Integer,Map<Integer,Double>>>();
//		try {
//			con = DBConnect.getConnection();
//			for(Entry<String, MerchantInfoBean> entrySet : Util.merchantInfoMap.entrySet()) {
//				if("PMS".equals(entrySet.getKey())) {
//					merchantId = entrySet.getValue().getMerchantId();
//					PurchaseTicketDaoImpl.merchantGameTypeDrawSale.put(merchantId, new PurchaseTicketDaoImpl().getMerchantGameTypeSale(merchantId, con));
//				} else if("RMS".equals(entrySet.getKey())) {
//					merchantId = entrySet.getValue().getMerchantId();
//					PurchaseTicketDaoImpl.merchantGameTypeDrawSale.put(merchantId, new PurchaseTicketDaoImpl().getMerchantGameTypeSale(merchantId, con));
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			DBConnect.closeConnection(con);
//		}
//	}
	
	public void setGameAndGameTypeInfoMerchantMap() throws SLEException {
		CommonMethodsServiceImpl.getInstance().setGameAndGameTypeInfoMerchantMap();
	}
	
	public void setServerUrl() throws SLEException{
		List<String> serverList = null;
		serverList = CommonMethodsServiceImpl.getInstance().setServerUrl();
		Util.serverPMSURL = serverList.get(0);
		Util.serverLMSURL = serverList.get(1);
	}
	
	public void startDrawMgmtScheduler() {
		try {
			Util.scheduler = new StdSchedulerFactory().getScheduler();
			Util.scheduler.start();
			new SchedularInitialization().startDrawMgmtScheduler();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void sendDrawNotficationToUser() {
		try {
			Util.scheduler = new StdSchedulerFactory().getScheduler();
			Util.scheduler.start();
			new SchedularInitialization().sendDrawNotficationToUser();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
//	static void setServerUrl() {
//		Connection con = null;
//		PreparedStatement pstm = null;
//		ResultSet rs = null;
//		String query = null;
//		try {
//			con = DBConnect.getConnection();
//			query = "select server_id,server_code,host_address,protocol,project_name,port from st_pms_server_info_master where status='ACTIVE'";
//			pstm = con.prepareStatement(query);
//			rs = pstm.executeQuery();
//
//			while (rs.next()) {
//					
//				if (rs.getString("server_code").equals("DGE")) {
//					String url = rs.getString("protocol") + "://"
//					+ rs.getString("host_address") + ":"
//					+ rs.getString("port") + "/"
//					+ rs.getString("project_name") + "/";
//					serverDGURL = url;
//				} else if (rs.getString("server_code").equals("LMS")) {
//					String url = rs.getString("protocol") + "://"
//							+ rs.getString("host_address") + ":"
//							+ rs.getString("port") + "/"
//							+ rs.getString("project_name") + "/";
//					serverURL =url;
//				}else if (rs.getString("server_code").equals("SLE")) {
//					String url = rs.getString("protocol") + "://"
//					+ rs.getString("host_address") + ":"
//					+ rs.getString("port") + "/"
//					+ rs.getString("project_name") + "/";
//					serverDrawGameURL = url;
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if (!con.isClosed() || con != null) {
//					DBConnect.closeConnection(con);
//				}
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
//	}
}
