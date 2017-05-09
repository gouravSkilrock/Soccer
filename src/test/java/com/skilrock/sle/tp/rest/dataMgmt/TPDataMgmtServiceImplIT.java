package com.skilrock.sle.tp.rest.dataMgmt;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.skilrock.sle.common.TransactionManager;
import com.skilrock.sle.common.Util;
import com.skilrock.sle.merchant.common.javaBeans.MerchantInfoBean;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MerchantInfoBean.class, Util.class, TransactionManager.class })
public class TPDataMgmtServiceImplIT {

	private static final String GET_MERCHANT_ID_METHOD = "getMerchantId";
	private static final String FETCH_MERCHANT_INFO_BEAN_METHOD = "fetchMerchantInfoBean";
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
	private static final String INVALID_JSON_FOR_FETCH_DRAW_EVENTS = "{\"gameId\"}";
	private static final String REQUEST_DATA_NOT_PROVIDED_MESSAGE = "{\"responseCode\":112,\"responseMessage\":\"No request data provided\"}";
	private static final String ERROR_RESPONSE = "{\"responseCode\":20000,\"responseMessage\":\"Internal System Error\"}";
	private static final String FETCH_DRAW_EVENTS_SUCCESS_REQUEST = "{\"gameId\":1,\"gameName\":\"Soccer\",\"gameTypeId\":1,\"gameTypeName\":\"Soccer12\",\"drawId\":181,\"drawName\":\"Thursday\",\"drawDateTime\":\"2017-03-05 18:00:00\"}";
	private static final String FETCH_DRAW_EVENTS_SUCCESS_RESPONSE = "{\"responseCode\":200,\"responseMessage\":\"SUCCESS\",\"gameId\":1,\"gameName\":\"Soccer\",\"gameTypeId\":1,\"gameTypeName\":\"Soccer12\",\"drawId\":1223,\"drawName\":\"Thursday\",\"drawDateTime\":\"2017-03-04 18:00:00\",\"eventsInfo\":[{\"awayTeamCode\":\"EA\",\"awayTeamName\":\"Al Ahly\",\"awayTeamOdds\":\"\",\"drawOdds\":\"\",\"endTime\":\"\",\"entryTime\":\"\",\"eventDescription\":\"\",\"eventDisplay\":\"ZAM-vs-EA\",\"eventId\":1395,\"eventOptionsList\":[\"HOME\",\"DRAW\",\"AWAY\",\"CANCEL\"],\"favTeam\":\"\",\"gameId\":0,\"homeTeamCode\":\"ZAM\",\"homeTeamName\":\"Zamalek\",\"homeTeamOdds\":\"\",\"leagueName\":\"Egyptian League\",\"noOfEvents\":0,\"startTime\":\"2016-07-09 18:30:00\",\"venueName\":\"Zamalek\",\"winninOptionCode\":\"\",\"winningOption\":\"\"},{\"awayTeamCode\":\"LAG\",\"awayTeamName\":\"LA Galaxy\",\"awayTeamOdds\":\"\",\"drawOdds\":\"\",\"endTime\":\"\",\"entryTime\":\"\",\"eventDescription\":\"\",\"eventDisplay\":\"SEAT-vs-LAG\",\"eventId\":1396,\"eventOptionsList\":[\"HOME\",\"DRAW\",\"AWAY\",\"CANCEL\"],\"favTeam\":\"\",\"gameId\":0,\"homeTeamCode\":\"SEAT\",\"homeTeamName\":\"Seattle Sounders\",\"homeTeamOdds\":\"\",\"leagueName\":\"Major League Soccer\",\"noOfEvents\":0,\"startTime\":\"2016-07-09 19:00:00\",\"venueName\":\"Seattle Sounders\",\"winninOptionCode\":\"\",\"winningOption\":\"\"},{\"awayTeamCode\":\"CRTN\",\"awayTeamName\":\"Corinthians\",\"awayTeamOdds\":\"\",\"drawOdds\":\"\",\"endTime\":\"\",\"entryTime\":\"\",\"eventDescription\":\"\",\"eventDisplay\":\"CHAP-vs-CRTN\",\"eventId\":1397,\"eventOptionsList\":[\"HOME\",\"DRAW\",\"AWAY\",\"CANCEL\"],\"favTeam\":\"\",\"gameId\":0,\"homeTeamCode\":\"CHAP\",\"homeTeamName\":\"Chapeconese \",\"homeTeamOdds\":\"\",\"leagueName\":\"Brazil Serie A\",\"noOfEvents\":0,\"startTime\":\"2016-07-09 19:30:00\",\"venueName\":\"Chapeconese \",\"winninOptionCode\":\"\",\"winningOption\":\"\"},{\"awayTeamCode\":\"SAN\",\"awayTeamName\":\"Santos\",\"awayTeamOdds\":\"\",\"drawOdds\":\"\",\"endTime\":\"\",\"entryTime\":\"\",\"eventDescription\":\"\",\"eventDisplay\":\"PLM-vs-SAN\",\"eventId\":1398,\"eventOptionsList\":[\"HOME\",\"DRAW\",\"AWAY\",\"CANCEL\"],\"favTeam\":\"\",\"gameId\":0,\"homeTeamCode\":\"PLM\",\"homeTeamName\":\"Palmeiras\",\"homeTeamOdds\":\"\",\"leagueName\":\"Brazil Serie A\",\"noOfEvents\":0,\"startTime\":\"2016-07-09 21:30:00\",\"venueName\":\"Palmeiras\",\"winninOptionCode\":\"\",\"winningOption\":\"\"},{\"awayTeamCode\":\"COLB\",\"awayTeamName\":\"Columbus Crew \",\"awayTeamOdds\":\"\",\"drawOdds\":\"\",\"endTime\":\"\",\"entryTime\":\"\",\"eventDescription\":\"\",\"eventDisplay\":\"NER-vs-COLB\",\"eventId\":1399,\"eventOptionsList\":[\"HOME\",\"DRAW\",\"AWAY\",\"CANCEL\"],\"favTeam\":\"\",\"gameId\":0,\"homeTeamCode\":\"NER\",\"homeTeamName\":\"New England Revolution \",\"homeTeamOdds\":\"\",\"leagueName\":\"Major League Soccer\",\"noOfEvents\":0,\"startTime\":\"2016-07-09 23:00:00\",\"venueName\":\"New England Revoluti\",\"winninOptionCode\":\"\",\"winningOption\":\"\"},{\"awayTeamCode\":\"BTFG\",\"awayTeamName\":\"Botafogo \",\"awayTeamOdds\":\"\",\"drawOdds\":\"\",\"endTime\":\"\",\"entryTime\":\"\",\"eventDescription\":\"\",\"eventDisplay\":\"CORI-vs-BTFG\",\"eventId\":1400,\"eventOptionsList\":[\"HOME\",\"DRAW\",\"AWAY\",\"CANCEL\"],\"favTeam\":\"\",\"gameId\":0,\"homeTeamCode\":\"CORI\",\"homeTeamName\":\"Coritiba\",\"homeTeamOdds\":\"\",\"leagueName\":\"Brazil Serie A\",\"noOfEvents\":0,\"startTime\":\"2016-07-10 14:00:00\",\"venueName\":\"Coritiba\",\"winninOptionCode\":\"\",\"winningOption\":\"\"},{\"awayTeamCode\":\"BCH\",\"awayTeamName\":\"Berekum Chelsea\",\"awayTeamOdds\":\"\",\"drawOdds\":\"\",\"endTime\":\"\",\"entryTime\":\"\",\"eventDescription\":\"\",\"eventDisplay\":\"BUT-vs-BCH\",\"eventId\":1401,\"eventOptionsList\":[\"HOME\",\"DRAW\",\"AWAY\",\"CANCEL\"],\"favTeam\":\"\",\"gameId\":0,\"homeTeamCode\":\"BUT\",\"homeTeamName\":\"Bechem United\",\"homeTeamOdds\":\"\",\"leagueName\":\"Ghana Premier League\",\"noOfEvents\":0,\"startTime\":\"2016-07-10 15:00:00\",\"venueName\":\"Bechem United\",\"winninOptionCode\":\"\",\"winningOption\":\"\"},{\"awayTeamCode\":\"KOT\",\"awayTeamName\":\"Kotoko\",\"awayTeamOdds\":\"\",\"drawOdds\":\"\",\"endTime\":\"\",\"entryTime\":\"\",\"eventDescription\":\"\",\"eventDisplay\":\"IA-vs-KOT\",\"eventId\":1402,\"eventOptionsList\":[\"HOME\",\"DRAW\",\"AWAY\",\"CANCEL\"],\"favTeam\":\"\",\"gameId\":0,\"homeTeamCode\":\"IA\",\"homeTeamName\":\"Inter Allies\",\"homeTeamOdds\":\"\",\"leagueName\":\"Ghana Premier League\",\"noOfEvents\":0,\"startTime\":\"2016-07-10 15:05:00\",\"venueName\":\"Inter Allies\",\"winninOptionCode\":\"\",\"winningOption\":\"\"},{\"awayTeamCode\":\"ADS\",\"awayTeamName\":\"Aduana Stars\",\"awayTeamOdds\":\"\",\"drawOdds\":\"\",\"endTime\":\"\",\"entryTime\":\"\",\"eventDescription\":\"\",\"eventDisplay\":\"HOK-vs-ADS\",\"eventId\":1403,\"eventOptionsList\":[\"HOME\",\"DRAW\",\"AWAY\",\"CANCEL\"],\"favTeam\":\"\",\"gameId\":0,\"homeTeamCode\":\"HOK\",\"homeTeamName\":\"Hearts of Oak\",\"homeTeamOdds\":\"\",\"leagueName\":\"Ghana Premier League\",\"noOfEvents\":0,\"startTime\":\"2016-07-10 15:10:00\",\"venueName\":\"Hearts of Oak\",\"winninOptionCode\":\"\",\"winningOption\":\"\"},{\"awayTeamCode\":\"LIB\",\"awayTeamName\":\"Liberty\",\"awayTeamOdds\":\"\",\"drawOdds\":\"\",\"endTime\":\"\",\"entryTime\":\"\",\"eventDescription\":\"\",\"eventDisplay\":\"EDU-vs-LIB\",\"eventId\":1404,\"eventOptionsList\":[\"HOME\",\"DRAW\",\"AWAY\",\"CANCEL\"],\"favTeam\":\"\",\"gameId\":0,\"homeTeamCode\":\"EDU\",\"homeTeamName\":\"Edubiase\",\"homeTeamOdds\":\"\",\"leagueName\":\"Ghana Premier League\",\"noOfEvents\":0,\"startTime\":\"2016-07-10 15:15:00\",\"venueName\":\"Edubiase\",\"winninOptionCode\":\"\",\"winningOption\":\"\"},{\"awayTeamCode\":\"HAS\",\"awayTeamName\":\"Hasaacas\",\"awayTeamOdds\":\"\",\"drawOdds\":\"\",\"endTime\":\"\",\"entryTime\":\"\",\"eventDescription\":\"\",\"eventDisplay\":\"DWAF-vs-HAS\",\"eventId\":1405,\"eventOptionsList\":[\"HOME\",\"DRAW\",\"AWAY\",\"CANCEL\"],\"favTeam\":\"\",\"gameId\":0,\"homeTeamCode\":\"DWAF\",\"homeTeamName\":\"Dwarfs\",\"homeTeamOdds\":\"\",\"leagueName\":\"Ghana Premier League\",\"noOfEvents\":0,\"startTime\":\"2016-07-10 15:20:00\",\"venueName\":\"Dwarfs\",\"winninOptionCode\":\"\",\"winningOption\":\"\"},{\"awayTeamCode\":\"STBK\",\"awayTeamName\":\"Stabaek\",\"awayTeamOdds\":\"\",\"drawOdds\":\"\",\"endTime\":\"\",\"entryTime\":\"\",\"eventDescription\":\"\",\"eventDisplay\":\"LLS-vs-STBK\",\"eventId\":1406,\"eventOptionsList\":[\"HOME\",\"DRAW\",\"AWAY\",\"CANCEL\"],\"favTeam\":\"\",\"gameId\":0,\"homeTeamCode\":\"LLS\",\"homeTeamName\":\"Lillstrom\",\"homeTeamOdds\":\"\",\"leagueName\":\"Norway Tippeligaen\",\"noOfEvents\":0,\"startTime\":\"2016-07-10 16:00:00\",\"venueName\":\"Lillstrom\",\"winninOptionCode\":\"\",\"winningOption\":\"\"}]}";
	private TPDataMgmtServiceImpl tpDataMgmtServiceImpl = new TPDataMgmtServiceImpl();
	
	
	@Test
	public void fetchDrawEvents_ReturnSuccessResponseForValidRequest(){
		Assert.assertEquals(FETCH_DRAW_EVENTS_SUCCESS_RESPONSE, tpDataMgmtServiceImpl.fetchDrawEvents(FETCH_DRAW_EVENTS_SUCCESS_REQUEST));
	}
	
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
	
	private void powermockclasses() throws Exception {
		PowerMockito.mockStatic(Util.class);
		PowerMockito.mockStatic(TransactionManager.class);
		PowerMockito.when(TransactionManager.class, GET_MERCHANT_ID_METHOD).thenReturn(1);
	}
}
