package com.skilrock.sle.tp.rest.playMgmt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.UserTransaction;
import javax.ws.rs.core.Response;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.skilrock.sle.common.DBConnect;
import com.skilrock.sle.common.Util;
import com.skilrock.sle.filterDispatcher.SLEMgmtFilterDispatcher;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DBConnect.class,Util.class})
public class TPPlayMgmtServiceImplIT {
	Connection connection = null;
	private static final String DATABASE_NAME = "sle_zim_qa_2017";
	private static final String HOST_ADDRESS = "localhost:3306";
	private static final String USERNAME = "root";
	private static final String PASSWORD = "password";
	UserTransaction userTxn = null;
	Calendar cal = Calendar.getInstance();
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
		PowerMockito.mockStatic(Util.class);
		PowerMockito.when(Util.convertDateTimeToResponseFormat2(Mockito.anyString())).thenReturn("dd-MM-yyyy HH:mm:ss");
		SLEMgmtFilterDispatcher mapObject = new SLEMgmtFilterDispatcher();
		mapObject.setMerchantInfoMap();
		mapObject.setGameAndGameTypeInfoMerchantMap();
		mapObject.setDrawSaleMap();
	}
	private void powerMockUtilMethods() {
		PowerMockito.mockStatic(Util.class);
		PowerMockito.when(Util.fmtToTwoDecimal(Mockito.anyDouble())).thenReturn(0.5);
		PowerMockito.when(Util.convertDateTimeToResponseFormat2(Mockito.anyString())).thenReturn("dd-MM-yyyy HH:mm:ss");
		PowerMockito.when(Util.getCurrentTimeStamp()).thenReturn(new Timestamp(cal.getTimeInMillis())); //2017-04-06 10:56:23.43
		PowerMockito.when(Util.getCurrentTimeString()).thenReturn(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime()));//2017-04-06 10:56:23
		PowerMockito.when(Util.getDateTimeFormat(Mockito.any(Timestamp.class))).thenReturn(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTimeInMillis()+10000000));
	}
	
	@Test
	public void purchaseTicket_TicketNumberFoundInResponse() {
		HttpServletRequest  mockedRequest = Mockito.mock(HttpServletRequest.class);
		String purchaseRequestData = "{\"gameId\":1,\"gameTypeId\":4,\"noOfBoard\":1,\"userId\":\"5\",\"merchantCode\":\"TonyBet\",\"mobileNo\":\"7877512353\",\"refTransId\":\"U897\",\"sessionId\":\"\",\"playerName\":\"Android\",\"callerId\": \"demo\",\"callerPassword\": \"test\",\"currency\": \"eur\",\"drawInfo\":[{\"drawId\":1538,\"betAmtMul\":1,\"eventData\":[{\"eventId\":1,\"eventSelected\":\"H\"},{\"eventId\":2,\"eventSelected\":\"H\"},{\"eventId\":3,\"eventSelected\":\"H\"},{\"eventId\":4,\"eventSelected\":\"H\"}]}]}";
		String purchaseResponseData = "";
		powerMockUtilMethods(); 
		Response response = new TPPlayMgmtServiceImpl().purchaseTicket(purchaseRequestData, mockedRequest);
		purchaseResponseData = response.getEntity().toString();
		JsonObject jsonObject = new JsonParser().parse(purchaseResponseData).getAsJsonObject();
		Assert.assertTrue(jsonObject.getAsJsonObject("tktData").has("ticketNo"));
	}
	
	@Test
	public void purchaseTicket_TransactionIDFoundInResponse() {
		HttpServletRequest  mockedRequest = Mockito.mock(HttpServletRequest.class);
		String purchaseRequestData = "{\"gameId\":1,\"gameTypeId\":4,\"noOfBoard\":1,\"userId\":\"5\",\"merchantCode\":\"TonyBet\",\"mobileNo\":\"7877512353\",\"refTransId\":\"U897\",\"sessionId\":\"\",\"playerName\":\"Android\",\"callerId\": \"demo\",\"callerPassword\": \"test\",\"currency\": \"eur\",\"drawInfo\":[{\"drawId\":1538,\"betAmtMul\":1,\"eventData\":[{\"eventId\":1,\"eventSelected\":\"H\"},{\"eventId\":2,\"eventSelected\":\"H\"},{\"eventId\":3,\"eventSelected\":\"H\"},{\"eventId\":4,\"eventSelected\":\"H\"}]}]}";
		String purchaseResponseData = "";
		powerMockUtilMethods(); 
		Response response = new TPPlayMgmtServiceImpl().purchaseTicket(purchaseRequestData, mockedRequest);
		purchaseResponseData = response.getEntity().toString();
		JsonObject jsonObject = new JsonParser().parse(purchaseResponseData).getAsJsonObject();
		Assert.assertTrue(jsonObject.getAsJsonObject("tktData").has("trxId"));
	}
}
