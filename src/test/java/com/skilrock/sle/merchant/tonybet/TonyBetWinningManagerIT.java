package com.skilrock.sle.merchant.tonybet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.transaction.UserTransaction;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.skilrock.sle.common.DBConnect;
import com.skilrock.sle.common.SportsLotteryUtils;
import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.common.javaBeans.PropertyMasterBean;
import com.skilrock.sle.common.javaBeans.UserInfoBean;
import com.skilrock.sle.commonMethod.daoImpl.CommonMethodsDaoImpl;
import com.skilrock.sle.commonMethod.serviceImpl.CommonMethodsServiceImpl;
import com.skilrock.sle.filterDispatcher.SLEMgmtFilterDispatcher;
import com.skilrock.sle.gamePlayMgmt.javaBeans.SportsLotteryGamePlayBean;
import com.skilrock.sle.pwtMgmt.controllerImpl.WinningMgmtControllerImpl;
import com.skilrock.sle.pwtMgmt.javaBeans.DrawWiseTicketInfoBean;
import com.skilrock.sle.pwtMgmt.javaBeans.TicketInfoBean;
@RunWith(PowerMockRunner.class)
@PrepareForTest({DBConnect.class,Util.class})
public class TonyBetWinningManagerIT {
	Connection connection = null;
	private static final String DATABASE_NAME = "sle_zim_qa_2017";
	private static final String HOST_ADDRESS = "192.168.124.80:3306";
	private static final String USERNAME = "root";
	private static final String PASSWORD = "root";
	UserTransaction userTxn = null;
	Calendar cal = Calendar.getInstance();
	DrawWiseTicketInfoBean  infoBean ;
	private Connection getConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://" + HOST_ADDRESS + "/" + DATABASE_NAME;
			connection = DriverManager.getConnection(url, USERNAME, PASSWORD);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException se) {
			se.printStackTrace();
		}
		return connection;
	}
	@Before
	public void setStaticMaps() throws Exception {
		connection = getConnection();
		userTxn = Mockito.mock(UserTransaction.class);
		PowerMockito.mockStatic(DBConnect.class);
		PowerMockito.when(DBConnect.class,"getConnection").thenReturn(connection);
		PowerMockito.when(DBConnect.startTransaction()).thenReturn(userTxn);
	/*	PowerMockito.mockStatic(Util.class);
		PowerMockito.when(Util.convertDateTimeToResponseFormat2(Mockito.anyString())).thenReturn("dd-MM-yyyy HH:mm:ss");*/
		setUpPropertyMap();
		connection = getConnection();
		PowerMockito.when(DBConnect.class,"getConnection").thenReturn(connection);
  		Util.merchantInfoMap = CommonMethodsServiceImpl.getInstance().fetchMerchantInfo();
		connection = getConnection();
		PowerMockito.when(DBConnect.class,"getConnection").thenReturn(connection);
        CommonMethodsServiceImpl.getInstance().setGameAndGameTypeInfoMerchantMap();
		connection = getConnection();
		PowerMockito.when(DBConnect.class,"getConnection").thenReturn(connection);
    	infoBean  = new DrawWiseTicketInfoBean();
		infoBean.setDrawId(113);
		infoBean.setGameId(1);
		infoBean.setGameTypeId(1);
		infoBean.setMerchantId(1);
		updateTicketStatusToUnclaimed();
	}
	
	@Test
	public void run_checkIfwinningTransferSuccessfully() throws Exception{
		WinningMgmtControllerImpl.getSingleInstance().fetchWinningTickets(infoBean);
		List<TicketInfoBean> ticketInfoBeanList = new ArrayList<TicketInfoBean>(infoBean.getTicketMap().values());
		callTonyBetAPIForWinningTransfer(ticketInfoBeanList);
	}
	private void callTonyBetAPIForWinningTransfer(List<TicketInfoBean> ticketInfoBeanList) throws Exception{
		if (ticketInfoBeanList != null && !ticketInfoBeanList.isEmpty()) {
			List<TonyBetGamePlayResponseBean> responseBeanList= new ArrayList<TonyBetGamePlayResponseBean>();
			getResponseList(ticketInfoBeanList, responseBeanList);
		 //	Take action for transactions
			updateWinningTxnStatus(responseBeanList);
		}
	}
	private void setUpPropertyMap() throws SLEException{
		List<PropertyMasterBean> propertyDataList = CommonMethodsServiceImpl.getInstance().getPropertyDetail();
		Map<String, String> propertiesMap = new HashMap<String, String>();
		for (PropertyMasterBean propertyMasterBean : propertyDataList) {
			propertiesMap.put(propertyMasterBean.getPropertyDevName(), propertyMasterBean.getValue());
		}
		Util.sysPropertiesMap = propertiesMap;
	}
	private void updateWinningTxnStatus(List<TonyBetGamePlayResponseBean> responseBeanList) {
		Connection connection = null;
		List<String> failedTransactionList = null;
		try {
			synchronized (this) {
				connection = getConnection();
				connection.setAutoCommit(false);
				failedTransactionList = new ArrayList<String>();
				for (TonyBetGamePlayResponseBean txnBean : responseBeanList) {
					if (txnBean.getResultCode() == 1) {
						CommonMethodsDaoImpl.getInstance().updateWinningStatus("DONE", txnBean.getTransactionId(),txnBean.getSleTranactionId(), connection);
						connection.commit(); 
					} else {
						//What to do with these tickets??
						failedTransactionList.add(String.valueOf(txnBean.getTransactionId()));
					}
				}			
		
				
				
			}
		} catch (Exception e) {
			e.printStackTrace();			
		} finally {
			DBConnect.closeConnection(connection);
		}
	}
	private void getResponseList(List<TicketInfoBean> ticketInfoBeanList,
			List<TonyBetGamePlayResponseBean> responseBeanList) throws SLEException {
		for (TicketInfoBean bean : ticketInfoBeanList) {
			SportsLotteryGamePlayBean gamePlayBean  = new SportsLotteryGamePlayBean();
			UserInfoBean userInfoBean  = new 		UserInfoBean();
			gamePlayBean.setTransId(Long.parseLong(bean.getEnginewinTxnId()));
			gamePlayBean.setGameId(infoBean.getGameId());
			gamePlayBean.setGameTypeId(infoBean.getGameTypeId());
			gamePlayBean.setGameDevName( SportsLotteryUtils.gameTypeInfoMerchantMap.get("TonyBet").get(gamePlayBean.getGameTypeId()).getGameTypeDevName());
			userInfoBean.setMerchantUserId(bean.getPartyId());
			gamePlayBean.setWinAmt(bean.getTotalWinningAmt());
			TonyBetGamePlayResponseBean depositResponse = TonyBetIntegrationImpl.depositTranaction(gamePlayBean, userInfoBean);
			depositResponse.setSleTranactionId(Long.parseLong(bean.getEnginewinTxnId()));
			responseBeanList.add(depositResponse);
		}
	}
	private void updateTicketStatusToUnclaimed() throws SQLException{
		String update ="UPDATE `st_sl_draw_ticket_1_1_113` SET `status` = 'UNCLAIMED' WHERE  `ticket_number` = '2331285100152901' "; 
		Statement stmt =connection.createStatement();
		stmt.executeUpdate(update);
		
	}
}
