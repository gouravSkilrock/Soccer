package com.skilrock.sle.tp.rest.dataMgmt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.skilrock.sle.common.TransactionManager;
import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.commonMethod.serviceImpl.CommonMethodsServiceImpl;
import com.skilrock.sle.dataMgmt.javaBeans.FetchDrawEventsRequest;
import com.skilrock.sle.drawMgmt.controllerImpl.ResultSubmissionControllerImpl;
import com.skilrock.sle.gameDataMgmt.javaBeans.DrawEventResultBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.EventMasterBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.FreezeDrawBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.GameMasterBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.GameTypeMasterBean;
import com.skilrock.sle.merchant.common.javaBeans.MerchantInfoBean;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ CommonMethodsServiceImpl.class, MerchantInfoBean.class, Util.class, TransactionManager.class })
public class TPDataMgmtServiceImplTest {
	private static final String INVALID_MERCHANT_ID_MESSAGE = "{\"responseCode\":112,\"responseMessage\":\"No request data provided\"}";
	private static final String EMPTY_REQUEST= "{\"responseCode\":112,\"responseMessage\":\"No request data provided\"}";
	private static final String INVALID_REQUEST_MESSAGE = "{\"responseCode\":20000,\"responseMessage\":\"Internal System Error\"}";
	private static final String NULL_MERCHANT_BEAN_RESPONSE = "{\"responseCode\":20000,\"responseMessage\":\"Internal System Error\"}";
	private static final String NO_PROPER_REQUEST =  "{\"responseCode\":2005,\"responseMessage\":\"Proper request data not provided\"}";
	
	private static final String GET_INSTANCE_METHOD = "getInstance";
	private static final String FETCH_MERCHANT_INFO_BEAN_METHOD = "fetchMerchantInfoBean";
	private static final String INVALID_JSON_FOR_FETCH_DRAW_EVENTS = "{\"gameId\"}";
	private static final String REQUEST_DATA_NOT_PROVIDED_MESSAGE = "{\"responseCode\":112,\"responseMessage\":\"No request data provided\"}";
	private static final String ERROR_RESPONSE = "{\"responseCode\":20000,\"responseMessage\":\"Internal System Error\"}";
	private static final String FETCH_DRAW_EVENTS_REQUEST = "{\"gameId\":1,\"gameName\":\"Soccer\",\"gameTypeId\":1,\"gameTypeName\":\"Soccer12\",\"drawId\":181,\"drawName\":\"Thursday\",\"drawDateTime\":\"2017-03-05 18:00:00\"}";
	private static final String GAME_ID_NOT_PROVIDED_REQUEST_1 = "{\"gameName\":\"Soccer\",\"gameTypeId\":1,\"gameTypeName\":\"Soccer12\",\"drawId\":181,\"drawName\":\"Thursday\",\"drawDateTime\":\"2017-03-05 18:00:00\"}";
	private static final String GAME_ID_NOT_PROVIDED_REQUEST_2 = "{\"gameId\":0,\"gameName\":\"Soccer\",\"gameTypeId\":1,\"gameTypeName\":\"Soccer12\",\"drawId\":181,\"drawName\":\"Thursday\",\"drawDateTime\":\"2017-03-05 18:00:00\"}";
	private static final String GAME_TYPE_ID_NOT_PROVIDED_REQUEST_1 = "{\"gameId\":1,\"gameName\":\"Soccer\",\"gameTypeName\":\"Soccer12\",\"drawId\":181,\"drawName\":\"Thursday\",\"drawDateTime\":\"2017-03-05 18:00:00\"}";
	private static final String GAME_TYPE_ID_NOT_PROVIDED_REQUEST_2 = "{\"gameId\":1,\"gameName\":\"Soccer\",\"gameTypeId\":0,\"gameTypeName\":\"Soccer12\",\"drawId\":181,\"drawName\":\"Thursday\",\"drawDateTime\":\"2017-03-05 18:00:00\"}";
	private static final String DRAW_ID_NOT_PROVIDED_REQUEST_1 = "{\"gameId\":1,\"gameName\":\"Soccer\",\"gameTypeId\":1,\"gameTypeName\":\"Soccer12\",\"drawName\":\"Thursday\",\"drawDateTime\":\"2017-03-05 18:00:00\"}";
	private static final String DRAW_ID_NOT_PROVIDED_REQUEST_2 = "{\"gameId\":1,\"gameName\":\"Soccer\",\"gameTypeId\":1,\"gameTypeName\":\"Soccer12\",\"drawId\":0,\"drawName\":\"Thursday\",\"drawDateTime\":\"2017-03-05 18:00:00\"}";
	private static final String GAME_NAME_NOT_PROVIDED_REQUEST_1 = "{\"gameId\":1,\"gameTypeId\":1,\"gameTypeName\":\"Soccer12\",\"drawId\":181,\"drawName\":\"Thursday\",\"drawDateTime\":\"2017-03-05 18:00:00\"}";
	private static final String GAME_NAME_NOT_PROVIDED_REQUEST_2 = "{\"gameId\":1,\"gameName\":\"\",\"gameTypeId\":1,\"gameTypeName\":\"Soccer12\",\"drawId\":181,\"drawName\":\"Thursday\",\"drawDateTime\":\"2017-03-05 18:00:00\"}";
	private static final String GAME_TYPE_NAME_NOT_PROVIDED_REQUEST_1 = "{\"gameId\":1,\"gameName\":\"Soccer\",\"gameTypeId\":1,\"drawId\":181,\"drawName\":\"Thursday\",\"drawDateTime\":\"2017-03-05 18:00:00\"}";
	private static final String GAME_TYPE_NAME_NOT_PROVIDED_REQUEST_2 = "{\"gameId\":1,\"gameName\":\"Soccer\",\"gameTypeId\":1,\"gameTypeName\":\"\",\"drawId\":181,\"drawName\":\"Thursday\",\"drawDateTime\":\"2017-03-05 18:00:00\"}";
	private static final String DRAW_NAME_NOT_PROVIDED_REQUEST_1 = "{\"gameId\":1,\"gameName\":\"Soccer\",\"gameTypeId\":1,\"gameTypeName\":\"Soccer12\",\"drawId\":181,\"drawDateTime\":\"2017-03-05 18:00:00\"}";
	private static final String DRAW_NAME_NOT_PROVIDED_REQUEST_2 = "{\"gameId\":1,\"gameName\":\"Soccer\",\"gameTypeId\":1,\"gameTypeName\":\"Soccer12\",\"drawId\":181,\"drawName\":\"\",\"drawDateTime\":\"2017-03-05 18:00:00\"}";
	private static final String DRAW_DATE_TIME_NOT_PROVIDED_REQUEST_1 = "{\"gameId\":1,\"gameName\":\"Soccer\",\"gameTypeId\":1,\"gameTypeName\":\"Soccer12\",\"drawId\":181,\"drawName\":\"Thursday\"}";
	private static final String DRAW_DATE_TIME_NOT_PROVIDED_REQUEST_2 = "{\"gameId\":1,\"gameName\":\"Soccer\",\"gameTypeId\":1,\"gameTypeName\":\"Soccer12\",\"drawId\":181,\"drawName\":\"Thursday\",\"drawDateTime\":\"\"}";
	private static final String GAME_ID_NOT_PROVIDED_MESSAGE = "{\"responseCode\":112,\"responseMessage\":\"Game Id not provided\"}";
	private static final String GAME_TYPE_ID_NOT_PROVIDED_MESSAGE = "{\"responseCode\":112,\"responseMessage\":\"Game Type Id not provided\"}";
	private static final String DRAW_ID_NOT_PROVIDED_MESSAGE = "{\"responseCode\":112,\"responseMessage\":\"Draw Id not provided\"}";
	private static final String GAME_NAME_NOT_PROVIDED_MESSAGE = "{\"responseCode\":112,\"responseMessage\":\"Game Name not provided\"}";
	private static final String GAME_TYPE_NAME_NOT_PROVIDED_MESSAGE = "{\"responseCode\":112,\"responseMessage\":\"Game Type Name not provided\"}";
	private static final String DRAW_NAME_NOT_PROVIDED_MESSAGE = "{\"responseCode\":112,\"responseMessage\":\"Draw Name not provided\"}";
	private static final String DRAW_DATE_TIME_NOT_PROVIDED_MESSAGE = "{\"responseCode\":112,\"responseMessage\":\"Draw Date Time not provided\"}";
	private static final String INVALID_MERCHANT_MESSAGE = "{\"responseCode\":112,\"responseMessage\":\"Invalid Merchant Name !\"}";
	private static final String DRAW_EXPIRED_MESSAGE = "{\"responseCode\":10031,\"responseMessage\":\"Draw Expired !!\"}";
	private static final String SUCCESS_RESPONSE = "{\"responseCode\":100,\"responseMessage\":\"SUCCESS\",\"gameId\":1,\"gameName\":\"Soccer\",\"gameTypeId\":1,\"drawId\":181,\"drawName\":\"Thursday\",\"drawDateTime\":\"2017-03-05 18:00:00\",\"eventsInfo\":[{\"eventId\":0,\"gameId\":0,\"noOfEvents\":0}],\"eventOptionMap\":{}}";

	private MerchantInfoBean merchantInfoBeansTest = new MerchantInfoBean();
	private Map<Integer, GameMasterBean> gameMapTest = new LinkedHashMap<Integer, GameMasterBean>();
	private Map<Integer, GameMasterBean> gameMapTestOne = new LinkedHashMap<Integer, GameMasterBean>();
	private List<GameTypeMasterBean> gameTypeMasterListTest = new ArrayList<GameTypeMasterBean>();
	private GameMasterBean gameMasterBeanTest = new GameMasterBean();
	private TPDataMgmtServiceImpl tpDataMgmtServiceImpl;
	private ResultSubmissionControllerImpl resultSubmissionControllerImpl;
	DrawEventResultBean drawEventResultBean;
	private CommonMethodsServiceImpl commonMethodsServiceImpl = Mockito.mock(CommonMethodsServiceImpl.class);

	@Before
	public void setUp() throws SLEException, Exception {
		resultSubmissionControllerImpl = Mockito.mock(ResultSubmissionControllerImpl.class);
		tpDataMgmtServiceImpl = new TPDataMgmtServiceImpl(resultSubmissionControllerImpl);
	}

	@Test
	public void submitResult_isMethodWithRequestData() throws SLEException {
		String requestData = "";
		tpDataMgmtServiceImpl.submitResultWithUserAuthentication(requestData);
	}

	@Test
	public void submitResult_rejectNonJsonRequest() throws Exception {
		String requestData = "1";
		String responseData = tpDataMgmtServiceImpl.submitResultWithUserAuthentication(requestData);
		Assert.assertEquals("{\"responseCode\":2005,\"responseMsg\":\"Proper request data not provided\"}",
				responseData);
	}

	@Test
	public void submitResult_validateNullRequestData() throws Exception {
		String requestData = null;
		String responseData = tpDataMgmtServiceImpl.submitResultWithUserAuthentication(requestData);
		Assert.assertEquals("{\"responseCode\":112,\"responseMsg\":\"No request data provided\"}", responseData);
	}

	@Test
	public void submitResult_validateEmptyRequestData() throws Exception {
		String requestData = "";
		String responseData = tpDataMgmtServiceImpl.submitResultWithUserAuthentication(requestData);
		Assert.assertEquals("{\"responseCode\":112,\"responseMsg\":\"No request data provided\"}", responseData);
	}

	@Test
	public void submitResult_validateRequestData() throws Exception {
		String requestData = " ";
		String responseData = tpDataMgmtServiceImpl.submitResultWithUserAuthentication(requestData);
		Assert.assertEquals("{\"responseCode\":112,\"responseMsg\":\"No request data provided\"}", responseData);
	}

	@Test
	public void submitResult_validateRequestDataForEventResult() throws Exception {
		String requestData = "{\"userId\" : 17660,\"gameId\" : 1,\"gameTypeId\" : 1,\"drawId\" : 183}";
		String responseData = tpDataMgmtServiceImpl.submitResultWithUserAuthentication(requestData);
		Assert.assertEquals("{\"responseCode\":2005,\"responseMsg\":\"Event Result Cannot be Empty or Null\"}",
				responseData);
	}

	@Test
	public void submitResult_validateRequestDataForUserId() throws Exception {
		String requestData = "{\"gameId\" : 1,\"gameTypeId\" : 1,\"drawId\" : 183,\"eventResult\" : \"6469_HOME,6480_AWAY,6468_DRAW,6470_AWAY,6477_AWAY,6479_CANCEL,6481_DRAW,6478_AWAY,6473_AWAY,6474_DRAW,1_HOME,2_DRAW\"}";
		String responseData = tpDataMgmtServiceImpl.submitResultWithUserAuthentication(requestData);
		Assert.assertEquals("{\"responseCode\":2005,\"responseMsg\":\"User ID cannot be empty or null\"}",
				responseData);
	}

	@Test
	public void submitResult_validateRequestDataForEventResultNotProper() throws Exception {
		String requestData = "{\"userId\" : 17660,\"gameId\" : 1,\"gameTypeId\" : 1,\"drawId\" : 183,\"eventResult\" : \"64693285682376RAW\"}";
		String responseData = tpDataMgmtServiceImpl.submitResultWithUserAuthentication(requestData);
		Assert.assertEquals("{\"responseCode\":2005,\"responseMsg\":\"Proper request data not provided\"}",
				responseData);
	}

	@Test
	public void submitResult_MandatoryParamsValidation() {
		drawEventResultBean = new DrawEventResultBean();
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<DrawEventResultBean>> violations = validator.validate(drawEventResultBean);
		System.out.println(violations.isEmpty());
	}

	@Test
	public void submitResult_validateRequestDataForStatusSuccess() throws Exception {
		String requestData = "{\"userId\" : 17660,\"gameId\" : 1,\"gameTypeId\" : 1,\"drawId\" : 183,\"eventResult\" : \"6469_HOME,6480_AWAY,6468_DRAW,6470_AWAY,6477_AWAY,6479_CANCEL,6481_DRAW,6478_AWAY,6473_AWAY,6474_DRAW,1_HOME,2_DRAW\"}";
		String status = "success";
		Mockito.when(resultSubmissionControllerImpl.sportsLotteryResultSubmission(Mockito.any(DrawEventResultBean.class))).thenReturn(status);
		Mockito.when(resultSubmissionControllerImpl.validateUserAuthenticationForResultSubmit(Mockito.any(DrawEventResultBean.class))).thenReturn(true);
		String responseData = tpDataMgmtServiceImpl.submitResultWithUserAuthentication(requestData);
		Assert.assertEquals("{\"responseCode\":100,\"responseMsg\":\"SUCCESS\"}", responseData);
	}

	@Test
	public void submitResult_validateRequestDataForFirstResult() throws Exception {
		String requestData = "{\"userId\" : 17660,\"gameId\" : 1,\"gameTypeId\" : 1,\"drawId\" : 183,\"eventResult\" : \"6469_HOME,6480_AWAY,6468_DRAW,6470_AWAY,6477_AWAY,6479_CANCEL,6481_DRAW,6478_AWAY,6473_AWAY,6474_DRAW,1_HOME,2_DRAW\"}";
		String status = "FIRST";
		Mockito.when(resultSubmissionControllerImpl.validateUserAuthenticationForResultSubmit(Mockito.any(DrawEventResultBean.class))).thenReturn(true);
		Mockito.when(resultSubmissionControllerImpl.sportsLotteryResultSubmission(Mockito.any(DrawEventResultBean.class))).thenReturn(status);
		String responseData = tpDataMgmtServiceImpl.submitResultWithUserAuthentication(requestData);
		Assert.assertEquals("{\"responseCode\":100,\"responseMsg\":\"First Result Submitted Successfully\"}",responseData);
	}

	@Test
	public void submitResult_validateRequestDataForUnmatchedResult() throws Exception {
		String requestData = "{\"userId\" : 17660,\"gameId\" : 1,\"gameTypeId\" : 1,\"drawId\" : 183,\"eventResult\" : \"6469_HOME,6480_AWAY,6468_DRAW,6470_AWAY,6477_AWAY,6479_CANCEL,6481_DRAW,6478_AWAY,6473_AWAY,6474_DRAW,1_HOME,2_DRAW\"}";
		String status = "UNMATCHED";
		Mockito.when(resultSubmissionControllerImpl.validateUserAuthenticationForResultSubmit(Mockito.any(DrawEventResultBean.class))).thenReturn(true);
		Mockito.when(resultSubmissionControllerImpl.sportsLotteryResultSubmission(Mockito.any(DrawEventResultBean.class))).thenReturn(status);
		String responseData = tpDataMgmtServiceImpl.submitResultWithUserAuthentication(requestData);
		Assert.assertEquals("{\"responseCode\":10037,\"responseMsg\":\"Second User Result Not Matched, BO has to submit the result\"}",responseData);
	}

	@Test
	public void submitResult_validateRequestDataForResolvedStatus() throws Exception {
		String requestData = "{\"userId\" : 17660,\"gameId\" : 1,\"gameTypeId\" : 1,\"drawId\" : 183,\"eventResult\" : \"6469_HOME,6480_AWAY,6468_DRAW,6470_AWAY,6477_AWAY,6479_CANCEL,6481_DRAW,6478_AWAY,6473_AWAY,6474_DRAW,1_HOME,2_DRAW\"}";
		String status = "RESOLVED";
		Mockito.when(resultSubmissionControllerImpl.validateUserAuthenticationForResultSubmit(Mockito.any(DrawEventResultBean.class))).thenReturn(true);
		Mockito.when(resultSubmissionControllerImpl.sportsLotteryResultSubmission(Mockito.any(DrawEventResultBean.class))).thenReturn(status);
		String responseData = tpDataMgmtServiceImpl.submitResultWithUserAuthentication(requestData);
		Assert.assertEquals("{\"responseCode\":10038,\"responseMsg\":\"Both Users Result Not Matched, BO has to submit the result\"}",responseData);
	}

	@Test
	public void submitResult_validateRequestDataForNullStatus() throws Exception {
		String requestData = "{\"userId\" : 17660,\"gameId\" : 1,\"gameTypeId\" : 1,\"drawId\" : 183,\"eventResult\" : \"6469_HOME,6480_AWAY,6468_DRAW,6470_AWAY,6477_AWAY,6479_CANCEL,6481_DRAW,6478_AWAY,6473_AWAY,6474_DRAW,1_HOME,2_DRAW\"}";
		Mockito.when(resultSubmissionControllerImpl.validateUserAuthenticationForResultSubmit(Mockito.any(DrawEventResultBean.class))).thenReturn(true);
		Mockito.when(resultSubmissionControllerImpl.sportsLotteryResultSubmission(Mockito.any(DrawEventResultBean.class))).thenReturn(null);
		String responseData = tpDataMgmtServiceImpl.submitResultWithUserAuthentication(requestData);
		Assert.assertEquals("{\"responseCode\":20000,\"responseMsg\":\"Internal System Error\"}", responseData);
	}


	private void powermockclasses() throws Exception {
		PowerMockito.mockStatic(Util.class);
		PowerMockito.mockStatic(TransactionManager.class);
		PowerMockito.when(TransactionManager.class, "getMerchantId").thenReturn(merchantInfoBeansTest.getMerchantId());
	}

	@Test
	public void fetchGameAndGameType_checkNullMerchantBean() throws Exception {
		powermockclasses();
		mockMerchantInfoBeanAndGameMap(gameMapTest);
		MerchantInfoBean merchantInfoBean = new MerchantInfoBean();
		PowerMockito.when(Util.class, FETCH_MERCHANT_INFO_BEAN_METHOD, Matchers.anyInt()).thenReturn(merchantInfoBean);
		String response = tpDataMgmtServiceImpl.fetchGameAndGameType();
		Assert.assertEquals(INVALID_MERCHANT_ID_MESSAGE, response);
	}

	@Test
	public void fetchGameAndGameType_checkNegativeMerchantId() throws Exception {
		powermockclasses();
		mockMerchantInfoBeanAndGameMap(gameMapTest);
		MerchantInfoBean merchantInfoBean = new MerchantInfoBean();
		merchantInfoBean.setMerchantId(-1);
		PowerMockito.when(Util.class, FETCH_MERCHANT_INFO_BEAN_METHOD, Matchers.anyInt()).thenReturn(merchantInfoBean);
		String response = tpDataMgmtServiceImpl.fetchGameAndGameType();
		Assert.assertEquals(INVALID_MERCHANT_ID_MESSAGE, response);
	}

	@Test
	public void fetchGameAndGameType_CheckResponseMessage() throws Exception {
		powermockclasses();
		mockMerchantInfoBeanAndGameMap(gameMapTest);
		MerchantInfoBean merchantInfoBean = new MerchantInfoBean();
		merchantInfoBean.setMerchantId(2);
		PowerMockito.when(Util.class, FETCH_MERCHANT_INFO_BEAN_METHOD, Matchers.anyInt()).thenReturn(merchantInfoBean);
		String response = tpDataMgmtServiceImpl.fetchGameAndGameType();
		JsonObject jsonResp = new JsonParser().parse(response).getAsJsonObject();
		Assert.assertEquals("SUCCESS", jsonResp.get("responseMessage").getAsString());
	}

	@Test
	public void fetchGameAndGameType_CheckResponseCode() throws Exception {
		powermockclasses();
		mockMerchantInfoBeanAndGameMap(gameMapTest);
		MerchantInfoBean merchantInfoBean = new MerchantInfoBean();
		merchantInfoBean.setMerchantId(2);
		PowerMockito.when(Util.class, FETCH_MERCHANT_INFO_BEAN_METHOD, Matchers.anyInt()).thenReturn(merchantInfoBean);
		String response = tpDataMgmtServiceImpl.fetchGameAndGameType();
		JsonObject jsonResp = new JsonParser().parse(response).getAsJsonObject();
		Assert.assertEquals(100, jsonResp.get("responseCode").getAsInt());
	}

	@Test
	public void fetchGameAndGameType_CheckNullGameBeanList() throws Exception {
		powermockclasses();
		mockMerchantInfoBeanAndGameMap(gameMapTest);
		String gameListResponse = "{\"responseCode\":100,\"responseMessage\":\"SUCCESS\",\"gameBeanList\":[]}";
		MerchantInfoBean merchantInfoBean = new MerchantInfoBean();
		merchantInfoBean.setMerchantId(2);
		PowerMockito.when(Util.class, FETCH_MERCHANT_INFO_BEAN_METHOD, Matchers.anyInt()).thenReturn(merchantInfoBean);
		String response = tpDataMgmtServiceImpl.fetchGameAndGameType();
		Assert.assertEquals(gameListResponse, response);
	}

	@Test
	public void fetchGameAndGameType_CheckNullGameTypeBeanList() throws Exception {
		setMapwithEmptyGameType();
		powermockclasses();
		mockMerchantInfoBeanAndGameMap(gameMapTestOne);
		String gameListResponse = "{\"responseCode\":100,\"responseMessage\":\"SUCCESS\",\"gameBeanList\":[{\"gameId\":0,\"gameNo\":0,\"merchantId\":0,\"gameDevName\":\"SOCCER\",\"maxTicketAmt\":0.0,\"thersholdTickerAmt\":0.0,\"minBoardCount\":0,\"maxBoardCount\":0,\"displayOrder\":0,\"gameTypeMasterList\":[]}]}";
		MerchantInfoBean merchantInfoBean = new MerchantInfoBean();
		merchantInfoBean.setMerchantId(2);
		PowerMockito.when(Util.class, FETCH_MERCHANT_INFO_BEAN_METHOD, Matchers.anyInt()).thenReturn(merchantInfoBean);
		String response = tpDataMgmtServiceImpl.fetchGameAndGameType();
		Assert.assertEquals(gameListResponse, response);
	}

	/*
	 * @Test public void fetchGameAndGameType_toCheckIfGameListIsEmpty() throws
	 * Exception{ String gameListResponse=
	 * "{'responseCode':100,'responseMessage':'SUCCESS','gameBeanList':[]}";
	 * mockMerchantInfoBeanAndGameMap(); String response =
	 * tpDataMgmtServiceImpl.fetchGameAndGameType();
	 * 
	 * }
	 */
	

	
	
	
	private void mockMerchantInfoBeanAndGameMap(Map<Integer, GameMasterBean> map) throws Exception, SLEException {

		CommonMethodsServiceImpl mockedObj1 = Mockito.mock(CommonMethodsServiceImpl.class);
		PowerMockito.mockStatic(CommonMethodsServiceImpl.class);
		PowerMockito.when(CommonMethodsServiceImpl.class, GET_INSTANCE_METHOD).thenReturn(mockedObj1);
		PowerMockito.when(mockedObj1.getGameMap(Matchers.any(MerchantInfoBean.class))).thenReturn(map);
	}

	private void setMapwithEmptyGameType() {
		gameMasterBeanTest.setGameDevName("SOCCER");
		gameMasterBeanTest.setGameTypeMasterList(gameTypeMasterListTest);
		gameMapTestOne.put(1, gameMasterBeanTest);
	}

	//Test Cases For Fetch Draw Events
		
	@Test
	public void fetchDrawEvents_ReturnsRequestDataNotProvidedIfRequestDataIsNull() throws Exception{
		powermockclasses();
		MerchantInfoBean merchantInfoBean = new MerchantInfoBean();
		merchantInfoBean.setMerchantId(2);
		PowerMockito.when(Util.class, FETCH_MERCHANT_INFO_BEAN_METHOD,Matchers.anyInt()).thenReturn(merchantInfoBean);
		Assert.assertEquals(REQUEST_DATA_NOT_PROVIDED_MESSAGE, tpDataMgmtServiceImpl.fetchDrawEvents(null));
	}
	
	@Test
	public void fetchDrawEvents_ReturnsRequestDataNotProvidedIfRequestDataIsEmpty() throws Exception{
		powermockclasses();
		MerchantInfoBean merchantInfoBean = new MerchantInfoBean();
		merchantInfoBean.setMerchantId(2);
		PowerMockito.when(Util.class, FETCH_MERCHANT_INFO_BEAN_METHOD,Matchers.anyInt()).thenReturn(merchantInfoBean);
		Assert.assertEquals(REQUEST_DATA_NOT_PROVIDED_MESSAGE, tpDataMgmtServiceImpl.fetchDrawEvents(""));
	}
	
	@Test
	public void fetchDrawEvents_ReturnsErrorResponseForInvalidJson() throws Exception{
		powermockclasses();
		MerchantInfoBean merchantInfoBean = new MerchantInfoBean();
		merchantInfoBean.setMerchantId(2);
		PowerMockito.when(Util.class, FETCH_MERCHANT_INFO_BEAN_METHOD,Matchers.anyInt()).thenReturn(merchantInfoBean);
		Assert.assertEquals(ERROR_RESPONSE, tpDataMgmtServiceImpl.fetchDrawEvents(INVALID_JSON_FOR_FETCH_DRAW_EVENTS));
	}
	
	@Test
	public void fetchDrawEvents_ReturnsGameIdNotProvidedResponse() throws Exception{
		powermockclasses();
		MerchantInfoBean merchantInfoBean = new MerchantInfoBean();
		merchantInfoBean.setMerchantId(2);
		PowerMockito.when(Util.class, FETCH_MERCHANT_INFO_BEAN_METHOD,Matchers.anyInt()).thenReturn(merchantInfoBean);
		Assert.assertEquals(GAME_ID_NOT_PROVIDED_MESSAGE, tpDataMgmtServiceImpl.fetchDrawEvents(GAME_ID_NOT_PROVIDED_REQUEST_1));
	}
	
	@Test
	public void fetchDrawEvents_ReturnsGameTypeIdNotProvidedResponse() throws Exception{
		powermockclasses();
		MerchantInfoBean merchantInfoBean = new MerchantInfoBean();
		merchantInfoBean.setMerchantId(2);
		PowerMockito.when(Util.class, FETCH_MERCHANT_INFO_BEAN_METHOD,Matchers.anyInt()).thenReturn(merchantInfoBean);
		Assert.assertEquals(GAME_TYPE_ID_NOT_PROVIDED_MESSAGE, tpDataMgmtServiceImpl.fetchDrawEvents(GAME_TYPE_ID_NOT_PROVIDED_REQUEST_1));
	}
	
	@Test
	public void fetchDrawEvents_ReturnsDrawIdNotProvidedResponse() throws Exception{
		powermockclasses();
		MerchantInfoBean merchantInfoBean = new MerchantInfoBean();
		merchantInfoBean.setMerchantId(2);
		PowerMockito.when(Util.class, FETCH_MERCHANT_INFO_BEAN_METHOD,Matchers.anyInt()).thenReturn(merchantInfoBean);
		Assert.assertEquals(DRAW_ID_NOT_PROVIDED_MESSAGE, tpDataMgmtServiceImpl.fetchDrawEvents(DRAW_ID_NOT_PROVIDED_REQUEST_1));
	}
	
	@Test
	public void fetchDrawEvents_ReturnsGameNameProvidedResponse() throws Exception{
		powermockclasses();
		MerchantInfoBean merchantInfoBean = new MerchantInfoBean();
		merchantInfoBean.setMerchantId(2);
		PowerMockito.when(Util.class, FETCH_MERCHANT_INFO_BEAN_METHOD,Matchers.anyInt()).thenReturn(merchantInfoBean);
		Assert.assertEquals(GAME_NAME_NOT_PROVIDED_MESSAGE, tpDataMgmtServiceImpl.fetchDrawEvents(GAME_NAME_NOT_PROVIDED_REQUEST_1));
	}
	
	@Test
	public void fetchDrawEvents_ReturnsGameTypeNameProvidedResponse() throws Exception{
		powermockclasses();
		MerchantInfoBean merchantInfoBean = new MerchantInfoBean();
		merchantInfoBean.setMerchantId(2);
		PowerMockito.when(Util.class, FETCH_MERCHANT_INFO_BEAN_METHOD,Matchers.anyInt()).thenReturn(merchantInfoBean);
		Assert.assertEquals(GAME_TYPE_NAME_NOT_PROVIDED_MESSAGE, tpDataMgmtServiceImpl.fetchDrawEvents(GAME_TYPE_NAME_NOT_PROVIDED_REQUEST_1));
	}
	
	@Test
	public void fetchDrawEvents_ReturnsDrawNameNotProvidedResponse() throws Exception{
		powermockclasses();
		MerchantInfoBean merchantInfoBean = new MerchantInfoBean();
		merchantInfoBean.setMerchantId(2);
		PowerMockito.when(Util.class, FETCH_MERCHANT_INFO_BEAN_METHOD,Matchers.anyInt()).thenReturn(merchantInfoBean);
		Assert.assertEquals(DRAW_NAME_NOT_PROVIDED_MESSAGE, tpDataMgmtServiceImpl.fetchDrawEvents(DRAW_NAME_NOT_PROVIDED_REQUEST_1));
	}

	@Test
	public void fetchDrawEvents_ReturnsDrawDateTimeNotProvidedResponse() throws Exception{
		powermockclasses();
		MerchantInfoBean merchantInfoBean = new MerchantInfoBean();
		merchantInfoBean.setMerchantId(2);
		PowerMockito.when(Util.class, FETCH_MERCHANT_INFO_BEAN_METHOD,Matchers.anyInt()).thenReturn(merchantInfoBean);
		Assert.assertEquals(DRAW_DATE_TIME_NOT_PROVIDED_MESSAGE, tpDataMgmtServiceImpl.fetchDrawEvents(DRAW_DATE_TIME_NOT_PROVIDED_REQUEST_1));
	}
	
	@Test
	public void fetchDrawEvents_ReturnsGameIdNotProvidedResponseIfGameIdIsZero() throws Exception{
		powermockclasses();
		MerchantInfoBean merchantInfoBean = new MerchantInfoBean();
		merchantInfoBean.setMerchantId(2);
		PowerMockito.when(Util.class, FETCH_MERCHANT_INFO_BEAN_METHOD,Matchers.anyInt()).thenReturn(merchantInfoBean);
		Assert.assertEquals(GAME_ID_NOT_PROVIDED_MESSAGE, tpDataMgmtServiceImpl.fetchDrawEvents(GAME_ID_NOT_PROVIDED_REQUEST_2));
	}
	
	@Test
	public void fetchDrawEvents_ReturnsGameTypeIdNotProvidedResponseIfGameTypeIdIsZero() throws Exception{
		powermockclasses();
		MerchantInfoBean merchantInfoBean = new MerchantInfoBean();
		merchantInfoBean.setMerchantId(2);
		PowerMockito.when(Util.class, FETCH_MERCHANT_INFO_BEAN_METHOD,Matchers.anyInt()).thenReturn(merchantInfoBean);
		Assert.assertEquals(GAME_TYPE_ID_NOT_PROVIDED_MESSAGE, tpDataMgmtServiceImpl.fetchDrawEvents(GAME_TYPE_ID_NOT_PROVIDED_REQUEST_2));
	}
	
	@Test
	public void fetchDrawEvents_ReturnsDrawIdNotProvidedResponseIfDrawIdIsZero() throws Exception{
		powermockclasses();
		MerchantInfoBean merchantInfoBean = new MerchantInfoBean();
		merchantInfoBean.setMerchantId(2);
		PowerMockito.when(Util.class, FETCH_MERCHANT_INFO_BEAN_METHOD,Matchers.anyInt()).thenReturn(merchantInfoBean);
		Assert.assertEquals(DRAW_ID_NOT_PROVIDED_MESSAGE, tpDataMgmtServiceImpl.fetchDrawEvents(DRAW_ID_NOT_PROVIDED_REQUEST_2));
	}
	
	@Test
	public void fetchDrawEvents_ReturnsGameNameProvidedResponseIfGameNameIsBlank() throws Exception{
		powermockclasses();
		MerchantInfoBean merchantInfoBean = new MerchantInfoBean();
		merchantInfoBean.setMerchantId(2);
		PowerMockito.when(Util.class, FETCH_MERCHANT_INFO_BEAN_METHOD,Matchers.anyInt()).thenReturn(merchantInfoBean);
		Assert.assertEquals(GAME_NAME_NOT_PROVIDED_MESSAGE, tpDataMgmtServiceImpl.fetchDrawEvents(GAME_NAME_NOT_PROVIDED_REQUEST_2));
	}
	
	@Test
	public void fetchDrawEvents_ReturnsGameTypeNameProvidedResponseIfGameTypeNameIsBlank() throws Exception{
		powermockclasses();
		MerchantInfoBean merchantInfoBean = new MerchantInfoBean();
		merchantInfoBean.setMerchantId(2);
		PowerMockito.when(Util.class, FETCH_MERCHANT_INFO_BEAN_METHOD,Matchers.anyInt()).thenReturn(merchantInfoBean);
		Assert.assertEquals(GAME_TYPE_NAME_NOT_PROVIDED_MESSAGE, tpDataMgmtServiceImpl.fetchDrawEvents(GAME_TYPE_NAME_NOT_PROVIDED_REQUEST_2));
	}
	
	@Test
	public void fetchDrawEvents_ReturnsDrawNameNotProvidedResponseIfDrawNameIsBlank() throws Exception{
		powermockclasses();
		MerchantInfoBean merchantInfoBean = new MerchantInfoBean();
		merchantInfoBean.setMerchantId(2);
		PowerMockito.when(Util.class, FETCH_MERCHANT_INFO_BEAN_METHOD,Matchers.anyInt()).thenReturn(merchantInfoBean);
		Assert.assertEquals(DRAW_NAME_NOT_PROVIDED_MESSAGE, tpDataMgmtServiceImpl.fetchDrawEvents(DRAW_NAME_NOT_PROVIDED_REQUEST_2));
	}

	@Test
	public void fetchDrawEvents_ReturnsDrawDateTimeNotProvidedResponseIfDrawDateTimeIsBlank() throws Exception{
		powermockclasses();
		MerchantInfoBean merchantInfoBean = new MerchantInfoBean();
		merchantInfoBean.setMerchantId(2);
		PowerMockito.when(Util.class, FETCH_MERCHANT_INFO_BEAN_METHOD,Matchers.anyInt()).thenReturn(merchantInfoBean);
		Assert.assertEquals(DRAW_DATE_TIME_NOT_PROVIDED_MESSAGE, tpDataMgmtServiceImpl.fetchDrawEvents(DRAW_DATE_TIME_NOT_PROVIDED_REQUEST_2));
	}
	
	@Test
	public void fetchDrawEvents_ReturnsInvalidMerchantResponse() throws Exception{
		powermockclasses();
		PowerMockito.when(Util.class, FETCH_MERCHANT_INFO_BEAN_METHOD,Matchers.anyInt()).thenReturn(null);
		Assert.assertEquals(INVALID_MERCHANT_MESSAGE, tpDataMgmtServiceImpl.fetchDrawEvents(FETCH_DRAW_EVENTS_REQUEST));
	}
	
	 @Test
	 public void fetchfreezedDraws_checkNullMerchantBean() throws Exception
	 {
			powerMockMerchantBeanClasses();
			MerchantInfoBean merchantInfoBean = new MerchantInfoBean();
			PowerMockito.when(Util.class, FETCH_MERCHANT_INFO_BEAN_METHOD, Matchers.anyInt()).thenReturn(merchantInfoBean);
			String response = tpDataMgmtServiceImpl.fetchfreezedDraws(null);
			Assert.assertEquals(INVALID_MERCHANT_ID_MESSAGE, response);	
	 }
	 
	 @Test
	 public void fetchfreezedDraws_checkNegativeMerchantId() throws Exception
	 {
			powerMockMerchantBeanClasses();
			MerchantInfoBean merchantInfoBean = new MerchantInfoBean();
			merchantInfoBean.setMerchantId(-1);
			PowerMockito.when(Util.class, FETCH_MERCHANT_INFO_BEAN_METHOD, Matchers.anyInt()).thenReturn(merchantInfoBean);
			String response = tpDataMgmtServiceImpl.fetchfreezedDraws(null);
			Assert.assertEquals(INVALID_MERCHANT_ID_MESSAGE, response);	
	 }
	 
	 @Test
	 public void fetchfreezedDraws_checkNullRequest() throws Exception
	 {
			powerMockMerchantBeanClasses();
			MerchantInfoBean merchantInfoBean = new MerchantInfoBean();
			merchantInfoBean.setMerchantId(1);
			PowerMockito.when(Util.class, FETCH_MERCHANT_INFO_BEAN_METHOD, Matchers.anyInt()).thenReturn(merchantInfoBean);
			String response = tpDataMgmtServiceImpl.fetchfreezedDraws(null);
			Assert.assertEquals(EMPTY_REQUEST, response);	
	 }
	 
	 @Test
	 public void fetchfreezedDraws_checkEmptyRequest() throws Exception
	 {
			powerMockMerchantBeanClasses();
			MerchantInfoBean merchantInfoBean = new MerchantInfoBean();
			merchantInfoBean.setMerchantId(1);
			PowerMockito.when(Util.class, FETCH_MERCHANT_INFO_BEAN_METHOD, Matchers.anyInt()).thenReturn(merchantInfoBean);
			String response = tpDataMgmtServiceImpl.fetchfreezedDraws("");
			Assert.assertEquals(EMPTY_REQUEST, response);	
	 }
	 
	 @Test
	 public void fetchfreezedDraws_InvalidRequest() throws Exception
	 {     
			powerMockMerchantBeanClasses();
			MerchantInfoBean merchantInfoBean = new MerchantInfoBean();
			merchantInfoBean.setMerchantId(1);
			PowerMockito.when(Util.class, FETCH_MERCHANT_INFO_BEAN_METHOD, Matchers.anyInt()).thenReturn(merchantInfoBean);
			String response = tpDataMgmtServiceImpl.fetchfreezedDraws("{\"gameId\":, \"gameTypeId\":1} ");
			Assert.assertEquals(INVALID_REQUEST_MESSAGE, response);	
	 }
	 
	 @Test
	 public void fetchfreezedDraws_NoGameIdInRequest() throws Exception
	 {     
			powerMockMerchantBeanClasses();
			MerchantInfoBean merchantInfoBean = new MerchantInfoBean();
			merchantInfoBean.setMerchantId(1);
			PowerMockito.when(Util.class, FETCH_MERCHANT_INFO_BEAN_METHOD, Matchers.anyInt()).thenReturn(merchantInfoBean);
			String response = tpDataMgmtServiceImpl.fetchfreezedDraws("{\"gameTypeId\":1} ");
			Assert.assertEquals(NO_PROPER_REQUEST, response);	
	 }
	 
	 @Test
	 public void fetchfreezedDraws_NoGameTypeIdInRequest() throws Exception
	 {     
			powerMockMerchantBeanClasses();
			MerchantInfoBean merchantInfoBean = new MerchantInfoBean();
			merchantInfoBean.setMerchantId(1);
			PowerMockito.when(Util.class, FETCH_MERCHANT_INFO_BEAN_METHOD, Matchers.anyInt()).thenReturn(merchantInfoBean);
			String response = tpDataMgmtServiceImpl.fetchfreezedDraws("{\"gameId\":1} ");
			Assert.assertEquals(NO_PROPER_REQUEST, response);	
	 }
	 
	 @Test
	 public void fetchfreezedDraws_NegativeGameIdInRequest() throws Exception
	 {     
			powerMockMerchantBeanClasses();
			MerchantInfoBean merchantInfoBean = new MerchantInfoBean();
			merchantInfoBean.setMerchantId(1);
			PowerMockito.when(Util.class, FETCH_MERCHANT_INFO_BEAN_METHOD, Matchers.anyInt()).thenReturn(merchantInfoBean);
			String response = tpDataMgmtServiceImpl.fetchfreezedDraws("{\"gameId\":-1, \"gameTypeId\":1} ");
			Assert.assertEquals(NO_PROPER_REQUEST, response);	
	 }
	 
	 @Test
	 public void fetchfreezedDraws_NegativeGameTypeIdInRequest() throws Exception
	 {     
			powerMockMerchantBeanClasses();
			MerchantInfoBean merchantInfoBean = new MerchantInfoBean();
			merchantInfoBean.setMerchantId(1);
			PowerMockito.when(Util.class, FETCH_MERCHANT_INFO_BEAN_METHOD, Matchers.anyInt()).thenReturn(merchantInfoBean);
			String response = tpDataMgmtServiceImpl.fetchfreezedDraws("{\"gameId\":1, \"gameTypeId\":-1} ");
			Assert.assertEquals(NO_PROPER_REQUEST, response);	
	 }
	 
	 
	 @Test
	 public void fetchfreezedDraws_CheckForCorrectRequest() throws Exception
	 {     
			powerMockMerchantBeanClasses();
			mockedClassforFreezedDrawList();
			MerchantInfoBean merchantInfoBean = new MerchantInfoBean();
			merchantInfoBean.setMerchantId(1);
			PowerMockito.when(Util.class, FETCH_MERCHANT_INFO_BEAN_METHOD, Matchers.anyInt()).thenReturn(merchantInfoBean);
			String response = tpDataMgmtServiceImpl.fetchfreezedDraws("{\"gameId\":1, \"gameTypeId\":1, \"userId\":11002} ");
			String expectedResponse="{\"responseCode\":100,\"responseMessage\":\"SUCCESS\",\"freezeDrawList\":[{\"drawDateTime\":\"\",\"drawId\":7,\"drawName\":\"THURSDAY-S13\"}]}";
			Assert.assertEquals(expectedResponse, response);	
	 }
	
	 private void mockedClassforFreezedDrawList() throws Exception{
			List<FreezeDrawBean> drawListTest =new ArrayList<FreezeDrawBean>();
			FreezeDrawBean FreezeDrawBeanTest =new FreezeDrawBean(); 
			FreezeDrawBeanTest.setDrawId(7);
			FreezeDrawBeanTest.setDrawName("THURSDAY-S13");
			drawListTest.add(FreezeDrawBeanTest);	
			Mockito.when(resultSubmissionControllerImpl.freezedDrawResult(Matchers.anyInt(),Matchers.anyInt(),Matchers.anyInt(),Matchers.anyInt())).thenReturn(drawListTest);
			//Mockito.when(resltSubmissionControllerImpl.freezedDrawResult(Matchers.anyInt(),Matchers.anyInt(),Matchers.anyInt(),Matchers.anyInt())).thenReturn(drawListTest);;
		}
	
	private void powerMockMerchantBeanClasses() throws Exception
	{   
		PowerMockito.mockStatic(Util.class);
		PowerMockito.mockStatic(TransactionManager.class);
		PowerMockito.when(TransactionManager.class, "getMerchantId").thenReturn(1);
	}
	
	

	@Test
	public void fetchDrawEvents_ReturnsInvalidMerchantResponseIfMerchantIdsIsZero() throws Exception{
		powermockclasses();
		MerchantInfoBean merchantInfoBean = new MerchantInfoBean();
		merchantInfoBean.setMerchantId(0);
		PowerMockito.when(Util.class, FETCH_MERCHANT_INFO_BEAN_METHOD,Matchers.anyInt()).thenReturn(merchantInfoBean);
		Assert.assertEquals(INVALID_MERCHANT_MESSAGE, tpDataMgmtServiceImpl.fetchDrawEvents(FETCH_DRAW_EVENTS_REQUEST));
	}
	
	@Test
	public void fetchDrawEvents_ReturnsDrawExpiredResponse() throws Exception{
		powermockclasses();
		MerchantInfoBean merchantInfoBean = new MerchantInfoBean();
		merchantInfoBean.setMerchantId(2);
		PowerMockito.when(Util.class, FETCH_MERCHANT_INFO_BEAN_METHOD,Matchers.anyInt()).thenReturn(merchantInfoBean);
		Mockito.when(resultSubmissionControllerImpl.getEventMasterDetails(Mockito.any(FetchDrawEventsRequest.class))).thenReturn(null);
		Assert.assertEquals(DRAW_EXPIRED_MESSAGE, tpDataMgmtServiceImpl.fetchDrawEvents(FETCH_DRAW_EVENTS_REQUEST));
	}
	
	@Test
	public void fetchDrawEvents_ReturnsDrawExpiredResponseIfListSizeIsZero() throws Exception{
		List<EventMasterBean> eventMasterList = new ArrayList<>();
		powermockclasses();
		MerchantInfoBean merchantInfoBean = new MerchantInfoBean();
		merchantInfoBean.setMerchantId(2);
		PowerMockito.when(Util.class, FETCH_MERCHANT_INFO_BEAN_METHOD,Matchers.anyInt()).thenReturn(merchantInfoBean);
		Mockito.when(resultSubmissionControllerImpl.getEventMasterDetails(Mockito.any(FetchDrawEventsRequest.class))).thenReturn(eventMasterList);
		Assert.assertEquals(DRAW_EXPIRED_MESSAGE, tpDataMgmtServiceImpl.fetchDrawEvents(FETCH_DRAW_EVENTS_REQUEST));
	}
	
	@Test
	public void fetchDrawEvents_ReturnsSuccessResponse() throws Exception{
		List<EventMasterBean> eventMasterList = new ArrayList<>();
		Map<String,String> eventOptionMap = new HashMap<>();
		FetchDrawEventsRequest fetchDrawEventsRequest = new FetchDrawEventsRequest();
		EventMasterBean eventMasterBean = new EventMasterBean();
		eventMasterList.add(eventMasterBean);
		powermockclasses();
		MerchantInfoBean merchantInfoBean = new MerchantInfoBean();
		merchantInfoBean.setMerchantId(2);
		PowerMockito.when(Util.class, FETCH_MERCHANT_INFO_BEAN_METHOD,Matchers.anyInt()).thenReturn(merchantInfoBean);
		PowerMockito.mockStatic(CommonMethodsServiceImpl.class);
		PowerMockito.when(CommonMethodsServiceImpl.class, GET_INSTANCE_METHOD).thenReturn(commonMethodsServiceImpl);
		PowerMockito.when(commonMethodsServiceImpl.fetchGameTypeOptionMap(fetchDrawEventsRequest)).thenReturn(eventOptionMap);
		Mockito.when(resultSubmissionControllerImpl.getEventMasterDetails(Mockito.any(FetchDrawEventsRequest.class))).thenReturn(eventMasterList);
		Assert.assertEquals(SUCCESS_RESPONSE, tpDataMgmtServiceImpl.fetchDrawEvents(FETCH_DRAW_EVENTS_REQUEST));
	}
	
}
