package com.skilrock.sle.merchant.tonybet;


import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.google.gson.JsonObject;
import com.skilrock.sle.common.exception.GenericException;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.common.javaBeans.UserInfoBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.GameMasterBean;
import com.skilrock.sle.gamePlayMgmt.javaBeans.SportsLotteryGamePlayBean;
import com.skilrock.sle.merchant.TpIntegrationImpl;
import com.skilrock.sle.merchant.tonybet.TonyBetUtils.ServiceMethod;

import com.skilrock.sle.tp.rest.common.javaBeans.TpUserRegistrationBean;

/**
 * @author Rachit Bhandari
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({TpIntegrationImpl.class})
public class TonyBetIntegrationImplTest {
	UserInfoBean userInfoBean = new UserInfoBean();
	SportsLotteryGamePlayBean gamePlayBean = new SportsLotteryGamePlayBean();
	public static Map<String, Map<Integer,GameMasterBean>> gameInfoMerchantMap=null;
	@Before
	public void SetUpUserInfoBean() {
		userInfoBean.setCallerId("demo");
		userInfoBean.setCallerPassword("test");
		userInfoBean.setMerchantUserId(5);
		userInfoBean.setMerchantId(7);
		userInfoBean.setMerchantDevName("TonyBet");
		userInfoBean.setUserSessionId("");
		userInfoBean.setUserType("PLAYER");
	}
	@Before
	public void SetSportsLotteryGamePlayBean() {
		gamePlayBean.setGameId(1);
		gamePlayBean.setGameTypeId(4);
		gamePlayBean.setTotalPurchaseAmt(0.5);
		gamePlayBean.setTransId(78456L);
	}
	@Before
	public void SetGameInfoMerchantMap() {
		
	}
	private void powerMockTPIntegrationImpl(String responseString) throws GenericException, SLEException {
		
		PowerMockito.mockStatic(TpIntegrationImpl.class);
		PowerMockito.when(TpIntegrationImpl.getTonyBetResponse(Mockito.anyString(), 
				Mockito.any(ServiceMethod.class), Mockito.any(JsonObject.class)))
		.thenReturn(responseString);
	}
	
	// Fetch User data Test cases
	
	@Test(expected = SLEException.class)
	public void fetchUserData_throwSLEExceptionForAllOtherResultCode() throws GenericException, SLEException  {
		String responseString = "{\"ResultCode\":3,\"LoginName\":\"Android\",\"Currency\":\"EUR\",\"Country\":\"Dominica\",\"City\":\"dsfdssd\"}";
		powerMockTPIntegrationImpl(responseString);
		TpUserRegistrationBean registrationBean = TonyBetIntegrationImpl.fetchUserData(userInfoBean);
	}
	@Test
	public void fetchUserData_UserNameIsSetIfReceivedInResponse() throws GenericException, SLEException  {
		String responseString = "{\"ResultCode\":1,\"LoginName\":\"Android\",\"Currency\":\"EUR\",\"Country\":\"Dominica\",\"City\":\"dsfdssd\"}";
		powerMockTPIntegrationImpl(responseString);
		TpUserRegistrationBean registrationBean = TonyBetIntegrationImpl.fetchUserData(userInfoBean);
		Assert.assertEquals("Android",registrationBean.getUserName());
	}
	@Test
	public void fetchUserData_CurrencyIsSetIfReceivedInResponse() throws GenericException, SLEException {
		String responseString = "{\"ResultCode\":1,\"LoginName\":\"Android\",\"Currency\":\"EUR\",\"Country\":\"Dominica\",\"City\":\"dsfdssd\"}";
		powerMockTPIntegrationImpl(responseString);
		TpUserRegistrationBean registrationBean = TonyBetIntegrationImpl.fetchUserData(userInfoBean);
		Assert.assertEquals("EUR",registrationBean.getCurrency());
	}
	@Test
	public void fetchUserData_CountryIsSetIfReceivedInResponse() throws GenericException, SLEException {
		String responseString = "{\"ResultCode\":1,\"LoginName\":\"Android\",\"Currency\":\"EUR\",\"Country\":\"Dominica\",\"City\":\"dsfdssd\"}";
		powerMockTPIntegrationImpl(responseString);
		TpUserRegistrationBean registrationBean = TonyBetIntegrationImpl.fetchUserData(userInfoBean);
		Assert.assertEquals("Dominica",registrationBean.getCountry());
	}
	@Test
	public void fetchUserData_SetCurrencyBlankIfCurrencyIsNull() throws GenericException, SLEException  {
		String responseString = "{\"ResultCode\":1,\"LoginName\":\"Android\",\"Currency\":null,\"Country\":\"Dominica\",\"City\":\"dsfdssd\"}";
		powerMockTPIntegrationImpl(responseString);
		TpUserRegistrationBean registrationBean = TonyBetIntegrationImpl.fetchUserData(userInfoBean);
		Assert.assertEquals("",registrationBean.getCurrency());
	}
	@Test
	public void fetchUserData_SetCountryBlankIfCountryIsNull() throws GenericException, SLEException  {
		String responseString = "{\"ResultCode\":1,\"LoginName\":\"Android\",\"Currency\":\"EUR\",\"Country\":null,\"City\":\"dsfdssd\"}";
		powerMockTPIntegrationImpl(responseString);
		TpUserRegistrationBean registrationBean = TonyBetIntegrationImpl.fetchUserData(userInfoBean);
		Assert.assertEquals("",registrationBean.getCountry());
	}
	@Test
	public void fetchUserData_SetCityBlankIfCityIsNull() throws GenericException, SLEException  {
		String responseString = "{\"ResultCode\":1,\"LoginName\":\"Android\",\"Currency\":\"EUR\",\"Country\":null,\"City\":null}";
		powerMockTPIntegrationImpl(responseString);
		TpUserRegistrationBean registrationBean = TonyBetIntegrationImpl.fetchUserData(userInfoBean);
		Assert.assertEquals("",registrationBean.getCity());
	}
	@Test(expected = SLEException.class)
	public void fetchUserData_responseStringIsNull() throws GenericException, SLEException  {
		String responseString = null;
		powerMockTPIntegrationImpl(responseString);
		TpUserRegistrationBean registrationBean = TonyBetIntegrationImpl.fetchUserData(userInfoBean);
		Assert.assertEquals("",registrationBean.getCity());
	}
	
	// Purchase Method Test cases
	
	@Test
	public void purchaseTicketAtTonyBet_CheckIfSaleIsDoneSuccessfully() throws GenericException, SLEException {
		String responseString = "{\"ResultCode\":1,\"Balance\":1204.75,\"TransactionId\":3}";
		powerMockTPIntegrationImpl(responseString);
		TonyBetGamePlayResponseBean tonyBetResponseBean = TonyBetIntegrationImpl.purchaseTicketAtTonyBet(gamePlayBean,userInfoBean);
		Assert.assertEquals(1,tonyBetResponseBean.getResultCode());
	}
	
	// Cancel Method Test Cases
	
	@Test
	public void cancelTransaction_CheckIfRollbackHasHappenedSuccesfully() throws GenericException, SLEException {
		String responseString = "{\"ResultCode\":1}";
		powerMockTPIntegrationImpl(responseString);
		TonyBetGamePlayResponseBean tonyBetResponseBean = TonyBetIntegrationImpl.cancelTransaction(gamePlayBean, userInfoBean);
		Assert.assertEquals(1,tonyBetResponseBean.getResultCode());
	}
	
	@Test
	public void depositTranaction_CheckIfDepositSuccessfull() throws GenericException, SLEException{
		String responseString ="{\"ResultCode\":1}";
		powerMockTPIntegrationImpl(responseString);			
		TonyBetGamePlayResponseBean tonyBetResponseBean = TonyBetIntegrationImpl.depositTranaction(gamePlayBean, userInfoBean);
		Assert.assertEquals(1,tonyBetResponseBean.getResultCode());
	}  
	@Test(expected = SLEException.class)
	public void depositTranaction_CheckIfUserInfoBeanIsNull() throws GenericException, SLEException{
		String responseString ="{\"ResultCode\":1}";
		powerMockTPIntegrationImpl(responseString);			
		userInfoBean=null;
		TonyBetGamePlayResponseBean tonyBetResponseBean = TonyBetIntegrationImpl.depositTranaction(gamePlayBean, userInfoBean);
		Assert.assertEquals(1,tonyBetResponseBean.getResultCode());
	}
	@Test
	public void depositTranaction_CheckIfDepositMethodCalled() throws GenericException, SLEException{
		String responseString ="{\"ResultCode\":1}";
		powerMockTPIntegrationImpl(responseString);	
		TonyBetIntegrationImpl.depositTranaction(gamePlayBean, userInfoBean);
		PowerMockito.verifyStatic();
		TpIntegrationImpl.getTonyBetResponse(Mockito.eq(TonyBetUtils.BASE_SERVICE_API),Mockito.eq(TonyBetUtils.ServiceMethod.deposit) , Mockito.any(JsonObject.class));
	}   
}
