package com.skilrock.sle.merchant.weaver;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import com.google.gson.JsonObject;
import com.skilrock.sle.common.DBConnect;
import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.exception.GenericException;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.commonMethod.daoImpl.CommonMethodsDaoImpl;
import com.skilrock.sle.merchant.TpIntegrationImpl;
import com.skilrock.sle.merchant.common.javaBeans.MerchantInfoBean;
import com.skilrock.sle.merchant.weaver.WeaverUtils.ServiceMethods;
import com.skilrock.sle.merchant.weaver.WeaverUtils.TxnTypes;
import com.skilrock.sle.pwtMgmt.controllerImpl.WinningMgmtControllerImpl;
import com.skilrock.sle.pwtMgmt.javaBeans.DrawWiseTicketInfoBean;
import com.skilrock.sle.pwtMgmt.javaBeans.TicketInfoBean;

@RunWith(PowerMockRunner.class)
@PrepareForTest({TpIntegrationImpl.class,WinningMgmtControllerImpl.class,DBConnect.class,WeaverUtils.class,CommonMethodsDaoImpl.class})
@SuppressStaticInitializationFor("com.skilrock.sle.common.DBConnect")
public class WeaverWinningTransferThreadTest {
	Connection connection=null;
	DrawWiseTicketInfoBean infoBean = new DrawWiseTicketInfoBean();
	@Before
	public void setMerchantInfoMap() {
		Map<String, MerchantInfoBean> merchantInfoMap = new HashMap<String, MerchantInfoBean>();
		MerchantInfoBean merchantBean = new MerchantInfoBean();
		merchantBean.setMerchantId(5);
		merchantBean.setMerchantName("Weaver");
		merchantBean.setMerchantDevName("Weaver");
		merchantBean.setServiceId(5);
		merchantBean.setMerchantIp("ala.winweaver.com");
		merchantBean.setProtocol("http");
		merchantBean.setProjectName("Weaver");
		merchantBean.setPort("80");
		merchantBean.setPassword("p@55w0rd");
		merchantBean.setUserName("E49B4EF3-1511-4B8B-86D2-CE78DA0F74D6");
		merchantInfoMap.put("Weaver", merchantBean);
		
		Util.merchantInfoMap = merchantInfoMap;
	}
/*	@Before
	public void mockConnectionObject() throws Exception {
		connection = Mockito.mock(Connection.class);
		PowerMockito.mockStatic(DBConnect.class);
		PowerMockito.when(DBConnect.class,"getConnection").thenReturn(connection);
	}*/
	private void powerrMockTPIntegrationImpl(String responseString) throws GenericException, SLEException {
		PowerMockito.mockStatic(WeaverUtils.class);
		PowerMockito.when(WeaverUtils.getAliasName()).thenReturn(WeaverUtils.ALIAS_NAME);
		PowerMockito.mockStatic(TpIntegrationImpl.class);
		PowerMockito.when(TpIntegrationImpl.getWeaverResponseString(Mockito.anyString(), 
				Mockito.any(ServiceMethods.class), Mockito.any(TxnTypes.class), Mockito.any(JsonObject.class)))
		.thenReturn(responseString);
	}
	@Before
	public void setTicketMap() throws Exception {
		Map<Long, TicketInfoBean> ticketMap= new HashMap<>(); 
		TicketInfoBean ticketInfoBean = new TicketInfoBean();
		ticketInfoBean.setTicketNo(2655061000058301L);
		ticketInfoBean.setPartyId(11001);
		ticketInfoBean.setTotalWinningAmt(5.00);
		ticketInfoBean.setEnginesaleTxnId("78035");
		
		ticketMap.put(2655061000058301L, ticketInfoBean);
		infoBean.setTicketMap(ticketMap);
		
		connection = Mockito.mock(Connection.class);
		PowerMockito.mockStatic(DBConnect.class);
		PowerMockito.when(DBConnect.class,"getConnection").thenReturn(connection);
	}
	@Test
	public void checkMerchantID() {
		Assert.assertEquals(5, Util.merchantInfoMap.get("Weaver").getMerchantId());
	}
	@Test /*(expected = SLEException.class)*/
	public void run_responseStringIsNotNull() throws Exception {
		String responseString = "{\"errorCode\":0,\"txnType\":\"WINNING\",\"serviceCode\":\"SPORTS_LOTTERY\",\"plrWiseRespBean\":[{\"errorCode\":0,\"playerId\":76,\"totalBal\":559.5,\"realBal\":559.5,\"txnId\":10546924}]}";
		powerrMockTPIntegrationImpl(responseString);
		mockStaticMethods();
		WeaverWinningTransferThread weaverThread = new WeaverWinningTransferThread(1, 4, 415,infoBean);
		weaverThread.run();
		
	}
	/*@Test (expected = SLEException.class)
	public void run_responseStringIsNull() throws Exception {
		String responseString = null;
		powerrMockTPIntegrationImpl(responseString);
		mockStaticMethods();
		WeaverWinningTransferThread weaverThread = new WeaverWinningTransferThread(1, 4, 415,infoBean);
		weaverThread.run();
		//Assert.fail("Null pointer Exception expected");
		//Assert.fail("SLE Exception expected");
		
	}*/
	private void mockStaticMethods() throws Exception, SLEException {
		PowerMockito.mockStatic(WinningMgmtControllerImpl.class);
		WinningMgmtControllerImpl winningObj = Mockito.mock(WinningMgmtControllerImpl.class);
		PowerMockito.when(WinningMgmtControllerImpl.class,"getSingleInstance").thenReturn(winningObj);
		Mockito.doNothing().when(winningObj).fetchWinningTickets(Mockito.any(DrawWiseTicketInfoBean.class));
		PowerMockito.mockStatic(CommonMethodsDaoImpl.class);
		CommonMethodsDaoImpl commonObj = Mockito.mock(CommonMethodsDaoImpl.class);
		PowerMockito.when(CommonMethodsDaoImpl.class,"getInstance").thenReturn(commonObj);
		Mockito.doNothing().when(commonObj).updateWinningStatus(Mockito.anyString(), Mockito.anyLong(), Mockito.any(Connection.class));
	}
}
