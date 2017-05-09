package com.skilrock.sle.tp.rest.dataMgmt;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import com.google.gson.Gson;
import com.skilrock.sle.common.TransactionManager;
import com.skilrock.sle.common.Util;
import com.skilrock.sle.drawMgmt.controllerImpl.ResultSubmissionControllerImpl;
import com.skilrock.sle.gameDataMgmt.javaBeans.DrawMasterBean;
import com.skilrock.sle.merchant.common.javaBeans.MerchantInfoBean;

import net.sf.json.JSONObject;



@RunWith(PowerMockRunner.class)
@SuppressStaticInitializationFor({ "com.skilrock.lms.web.drawGames.common.Util"})
@PrepareForTest({Util.class,TransactionManager.class})

public class TPDataMgmtServiceImplNewTest {

	TPDataMgmtServiceImpl serviceLayerObject ;
	private String requestData;
	DrawMasterBean drawmasterBean ;
	List<DrawMasterBean> drawMasterList ;
	ResultSubmissionControllerImpl controllerImpl;
	MerchantInfoBean merchantInfoBean ;
	
	
	@Before
	public void initializeVariables() throws Exception{
		merchantInfoBean = new MerchantInfoBean();
		merchantInfoBean.setMerchantId(2);
		PowerMockito.mockStatic(Util.class);
		PowerMockito.mockStatic(TransactionManager.class);
		PowerMockito.when(TransactionManager.class,"getMerchantId").thenReturn(merchantInfoBean.getMerchantId());
		PowerMockito.when(Util.class,"fetchMerchantInfoBean",Matchers.anyInt()).thenReturn(merchantInfoBean);
		drawMasterList = new ArrayList<DrawMasterBean>();
		mockedClass();
	}
	
	private void mockedClass() throws Exception{
		controllerImpl = Mockito.mock(ResultSubmissionControllerImpl.class);
		serviceLayerObject = new TPDataMgmtServiceImpl(controllerImpl);
		drawmasterBean = Mockito.mock(DrawMasterBean.class);
		drawMasterList.add(drawmasterBean);
		Mockito.when(controllerImpl.resultSubmissionDrawData(Matchers.anyInt(),Matchers.anyInt(),Matchers.anyInt(),Matchers.anyInt())).thenReturn(drawMasterList);
	}
	@Test
	public void fetchDrawsAndEventsTest_ForEmptyRequest() throws Exception{
		requestData = " ";
		String response = serviceLayerObject.fetchDrawsAndEvents(requestData);
		Gson g = new Gson();
		JSONObject js = g.fromJson(response,JSONObject.class);
		Assert.assertEquals("No request data provided", js.getString("responseMessage"));
	}
	@Test
	public void fetchDrawsAndEventsTest_ForNullRequest() throws Exception{
		requestData = null;
		String response = serviceLayerObject.fetchDrawsAndEvents(requestData);
		Gson g = new Gson();
		JSONObject js = g.fromJson(response,JSONObject.class);
		Assert.assertEquals("No request data provided", js.getString("responseMessage"));
	}
	@Test
	public void validateRequestTestForGameId(){
		requestData = "{\"gameTypeId\":1, \"userId\":17660} ";
		String response = serviceLayerObject.fetchDrawsAndEvents(requestData);
		Gson g = new Gson();
		JSONObject js = g.fromJson(response,JSONObject.class);
		Assert.assertEquals("No request data provided", js.getString("responseMessage"));
	}
	@Test
	public void validateIfRequestDataIsWrong(){
		requestData = "{\"gameId\":, \"gameTypeId\":1, \"userId\":17660} ";
		String response = serviceLayerObject.fetchDrawsAndEvents(requestData);
		Gson g = new Gson();
		JSONObject js = g.fromJson(response,JSONObject.class);
		Assert.assertEquals("Internal System Error", js.getString("responseMessage"));
	}
	@Test
	public void validateRequestTestforGameTypeId(){
		requestData = "{\"gameId\":1, \"userId\":17660} ";
		String response = serviceLayerObject.fetchDrawsAndEvents(requestData);
		Gson g = new Gson();
		JSONObject js = g.fromJson(response,JSONObject.class);
		Assert.assertEquals("No request data provided", js.getString("responseMessage"));
	}
	@Test
	public void validateRequestTestForuserId(){
		requestData = "{\"gameId\":1, \"gameTypeId\":1} ";
		String response = serviceLayerObject.fetchDrawsAndEvents(requestData);
		Gson g = new Gson();
		JSONObject js = g.fromJson(response,JSONObject.class);
		Assert.assertEquals("No request data provided", js.getString("responseMessage"));
	}
	
	@Test
	public void validateRequestTestForNegativeGameId(){
		requestData = "{\"gameId\":-1, \"gameTypeId\":1, \"userId\":17660} ";
		String response = serviceLayerObject.fetchDrawsAndEvents(requestData);
		Gson g = new Gson();
		JSONObject js = g.fromJson(response,JSONObject.class);
		Assert.assertEquals("No request data provided", js.getString("responseMessage"));
	}
	@Test
	public void validateRequestTestforNegativeGameTypeId(){
		requestData ="{\"gameId\":1, \"gameTypeId\":-1, \"userId\":17660} ";
		String response = serviceLayerObject.fetchDrawsAndEvents(requestData);
		Gson g = new Gson();
		JSONObject js = g.fromJson(response,JSONObject.class);
		Assert.assertEquals("No request data provided", js.getString("responseMessage"));
	}
	@Test
	public void validateRequestTestForNegativeuserId(){
		requestData = "{\"gameId\":1, \"gameTypeId\":1, \"userId\":-17660} ";
		String response = serviceLayerObject.fetchDrawsAndEvents(requestData);
		Gson g = new Gson();
		JSONObject js = g.fromJson(response,JSONObject.class);
		Assert.assertEquals("No request data provided", js.getString("responseMessage"));
	}
	
	@Test
	public void fetchDrawsAndEventsTest() throws Exception{
		requestData = "{\"gameId\":1, \"gameTypeId\":1, \"userId\":17660} ";
		String response = serviceLayerObject.fetchDrawsAndEvents(requestData);
		Gson g = new Gson();
		JSONObject js = g.fromJson(response,JSONObject.class);
		Assert.assertEquals("SUCCESS", js.getString("responseMessage"));
	}
}
