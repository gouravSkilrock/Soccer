/**
 * 
 */
package com.skilrock.sle.merchant.weaver;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import com.skilrock.sle.common.exception.GenericException;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.common.javaBeans.UserInfoBean;
import com.skilrock.sle.merchant.TpIntegrationImpl;
import com.skilrock.sle.merchant.weaver.WeaverUtils.ServiceMethods;
import com.skilrock.sle.merchant.weaver.WeaverUtils.TxnTypes;
import com.skilrock.sle.tp.rest.common.javaBeans.TpUserRegistrationBean;
import com.skilrock.sle.tp.rest.common.javaBeans.TransactionBean;

/**
 * @author Rachit Bhandari
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({TpIntegrationImpl.class,DBConnect.class,WeaverUtils.class})
@SuppressStaticInitializationFor("com.skilrock.sle.common.DBConnect")
public class WeaverIntegrationImplTest {
	
	Connection connection=null;
	UserInfoBean userInfoBean = new UserInfoBean();
	TransactionBean txnBean = new TransactionBean();
	Map<Long,TransactionBean> sleTxnsIdMap = new HashMap<Long,TransactionBean>();
	@Before
	public void cancelTicketTxnAtWeaver_SetUpUserInfoBean() {
		userInfoBean.setMerchantUserId(583);
		userInfoBean.setMerchantId(5);
		userInfoBean.setMerchantDevName("Weaver");
		userInfoBean.setUserSessionId("WKtp3WAOrPkcDb3HkLYbun0FfZCAO3jhz1Kjt98AW18");
		userInfoBean.setUserType("PLAYER");
	}
	@Before
	public void cancelTicketTxnAtWeaver_SetUpSLETxnsIdMap() {
		txnBean.setTransactionId(11111);
		txnBean.setTransactionAmt(0.5);
		txnBean.setGameId(1);
		txnBean.setGameTypeId(4);
		txnBean.setGameDevName("SOCCER");
		sleTxnsIdMap.put(11111L, txnBean);
	}
	@Before
	public void mockConnectionObject() throws Exception {
		connection = Mockito.mock(Connection.class);
		PowerMockito.mockStatic(DBConnect.class);
		PowerMockito.when(DBConnect.class,"getConnection").thenReturn(connection);
	}
	private void powerrMockTPIntegrationImpl(String responseString) throws GenericException, SLEException {
		PowerMockito.mockStatic(WeaverUtils.class);
		PowerMockito.when(WeaverUtils.getAliasName()).thenReturn(WeaverUtils.ALIAS_NAME);
		PowerMockito.mockStatic(TpIntegrationImpl.class);
		PowerMockito.when(TpIntegrationImpl.getWeaverResponseString(Mockito.anyString(), 
				Mockito.any(ServiceMethods.class), Mockito.any(TxnTypes.class), Mockito.any(JsonObject.class)))
		.thenReturn(responseString);
	}
	@Test
	public void cancelTicketTxnAtWeaver_WeaverGamePlayResponseBeanIsNotNull() throws GenericException, SLEException {
		powerrMockTPIntegrationImpl("{\"serviceCode\":\"SPORTS_LOTTERY\",\"txnType\":\"WAGER_REFUND\",\"errorCode\":0,\"plrWiseRespBean\":[{\"playerId\":583,\"txnId\":231272,\"totalBal\":8773.4,\"refWagerTxnId\":231271,\"errorCode\":0,\"realBal\":8773.4}]}");
		WeaverGamePlayResponseBean weaverGamePlayResponseBean = WeaverIntegrationImpl.cancelTicketTxnAtWeaver(sleTxnsIdMap, userInfoBean);
		Assert.assertNotNull("WeaverGamePlayResponseBean is null", weaverGamePlayResponseBean);
	}
	
	@Test
	public void fetchUserDataAtWeaver_toCheckIfUserTypeIsPlayer() throws Exception {
		String responseString = "{\"domainName\":\"192.168.124.32\",\"playerInfoBean\":{\"userName\":\"jackbond10\",\"pinCode\":\"122001\",\"firstName\":\"F1wheekaaa\",\"lastName\":\"Guptafd123\",\"emailId\":\"bingobunny06@gmail.com\",\"mobileNo\":9845142321,\"addressLine1\":\"Skilrock Technologies 1243213\",\"city\":\"Gurgaonsda\",\"stateCode\":\"IN-HR\",\"countryCode\":\"IN\",\"commonContentPath\":\"http://192.168.132.32:8180/WeaverDoc/commonContent/192.168.132.32\",\"gender\":\"M\",\"emailVerified\":\"Y\",\"phoneVerified\":\"Y\",\"walletBean\":{\"withdrawableBal\":178039.52,\"depositBalance\":30300,\"winningBalance\":0,\"cashBalance\":181201.2,\"totalBalance\":203550.9,\"bonusBalance\":22349.7},\"playerId\":45,\"playerStatus\":\"FULL\",\"dob\":\"15/06/1991\",\"avatarPath\":\"/playerImages/45_image.png\",\"state\":\"Haryana\",\"country\":\"INDIA\"},\"mapping\":{\"516\":[202],\"551\":[186,216]},\"docUploadStatus\":\"UPLOADED\",\"profileUpdate\":true,\"fistDeposited\":true,\"errorCode\":0}";
		powerrMockTPIntegrationImpl(responseString);
		TpUserRegistrationBean registrationBean = WeaverIntegrationImpl.fetchUserDataAtWeaver(userInfoBean);
		Assert.assertEquals("PLAYER",registrationBean.getUserType());
	}
	@Test(expected = SLEException.class)
	public void fetchUserDataAtWeaver_throwsSLEExceptionForInvalidSession() throws Exception {
		String responseString = "{\"domainName\":\"192.168.124.32\",\"playerInfoBean\":{\"userName\":\"jackbond10\",\"pinCode\":\"122001\",\"firstName\":\"F1wheekaaa\",\"lastName\":\"Guptafd123\",\"emailId\":\"bingobunny06@gmail.com\",\"mobileNo\":9845142321,\"addressLine1\":\"Skilrock Technologies 1243213\",\"city\":\"Gurgaonsda\",\"stateCode\":\"IN-HR\",\"countryCode\":\"IN\",\"commonContentPath\":\"http://192.168.132.32:8180/WeaverDoc/commonContent/192.168.132.32\",\"gender\":\"M\",\"emailVerified\":\"Y\",\"phoneVerified\":\"Y\",\"walletBean\":{\"withdrawableBal\":178039.52,\"depositBalance\":30300,\"winningBalance\":0,\"cashBalance\":181201.2,\"totalBalance\":203550.9,\"bonusBalance\":22349.7},\"playerId\":45,\"playerStatus\":\"FULL\",\"dob\":\"15/06/1991\",\"avatarPath\":\"/playerImages/45_image.png\",\"state\":\"Haryana\",\"country\":\"INDIA\"},\"mapping\":{\"516\":[202],\"551\":[186,216]},\"docUploadStatus\":\"UPLOADED\",\"profileUpdate\":true,\"fistDeposited\":true,\"errorCode\":203}";
		powerrMockTPIntegrationImpl(responseString);
		WeaverIntegrationImpl.fetchUserDataAtWeaver(userInfoBean);
		
	}
	@Test(expected = SLEException.class)
	public void fetchUserDataAtWeaver_throwsGenericSLEExceptionForAllOtherErrorCode() throws Exception {
		String responseString = "{\"domainName\":\"192.168.124.32\",\"playerInfoBean\":{\"userName\":\"jackbond10\",\"pinCode\":\"122001\",\"firstName\":\"F1wheekaaa\",\"lastName\":\"Guptafd123\",\"emailId\":\"bingobunny06@gmail.com\",\"mobileNo\":9845142321,\"addressLine1\":\"Skilrock Technologies 1243213\",\"city\":\"Gurgaonsda\",\"stateCode\":\"IN-HR\",\"countryCode\":\"IN\",\"commonContentPath\":\"http://192.168.132.32:8180/WeaverDoc/commonContent/192.168.132.32\",\"gender\":\"M\",\"emailVerified\":\"Y\",\"phoneVerified\":\"Y\",\"walletBean\":{\"withdrawableBal\":178039.52,\"depositBalance\":30300,\"winningBalance\":0,\"cashBalance\":181201.2,\"totalBalance\":203550.9,\"bonusBalance\":22349.7},\"playerId\":45,\"playerStatus\":\"FULL\",\"dob\":\"15/06/1991\",\"avatarPath\":\"/playerImages/45_image.png\",\"state\":\"Haryana\",\"country\":\"INDIA\"},\"mapping\":{\"516\":[202],\"551\":[186,216]},\"docUploadStatus\":\"UPLOADED\",\"profileUpdate\":true,\"fistDeposited\":true,\"errorCode\":100}";
		powerrMockTPIntegrationImpl(responseString);
		WeaverIntegrationImpl.fetchUserDataAtWeaver(userInfoBean);
		
	}
	@Test
	public void fetchUserDataAtWeaver_toCheckIfFirstNameisSetIfNotNull() throws Exception {
		String responseString = "{\"domainName\":\"192.168.124.32\",\"playerInfoBean\":{\"userName\":\"jackbond10\",\"pinCode\":\"122001\",\"firstName\":\"testplayer\",\"lastName\":\"Guptafd123\",\"emailId\":\"bingobunny06@gmail.com\",\"mobileNo\":9845142321,\"addressLine1\":\"Skilrock Technologies 1243213\",\"city\":\"Gurgaonsda\",\"stateCode\":\"IN-HR\",\"countryCode\":\"IN\",\"commonContentPath\":\"http://192.168.132.32:8180/WeaverDoc/commonContent/192.168.132.32\",\"gender\":\"M\",\"emailVerified\":\"Y\",\"phoneVerified\":\"Y\",\"walletBean\":{\"withdrawableBal\":178039.52,\"depositBalance\":30300,\"winningBalance\":0,\"cashBalance\":181201.2,\"totalBalance\":203550.9,\"bonusBalance\":22349.7},\"playerId\":45,\"playerStatus\":\"FULL\",\"dob\":\"15/06/1991\",\"avatarPath\":\"/playerImages/45_image.png\",\"state\":\"Haryana\",\"country\":\"INDIA\"},\"mapping\":{\"516\":[202],\"551\":[186,216]},\"docUploadStatus\":\"UPLOADED\",\"profileUpdate\":true,\"fistDeposited\":true,\"errorCode\":0}";
		powerrMockTPIntegrationImpl(responseString);
		TpUserRegistrationBean registrationBean = WeaverIntegrationImpl.fetchUserDataAtWeaver(userInfoBean);
		Assert.assertEquals("testplayer",registrationBean.getFirstName());
	}
	@Test
	public void fetchUserDataAtWeaver_setFirstNameAsBlankIfFirstNameIsNull() throws Exception {
		String responseString = "{\"domainName\":\"192.168.124.32\",\"playerInfoBean\":{\"userName\":\"testplayer\",\"pinCode\":\"122001\",\"lastName\":\"Guptafd123\",\"emailId\":\"bingobunny06@gmail.com\",\"mobileNo\":9845142321,\"addressLine1\":\"Skilrock Technologies 1243213\",\"city\":\"Gurgaonsda\",\"stateCode\":\"IN-HR\",\"countryCode\":\"IN\",\"commonContentPath\":\"http://192.168.132.32:8180/WeaverDoc/commonContent/192.168.132.32\",\"gender\":\"M\",\"emailVerified\":\"Y\",\"phoneVerified\":\"Y\",\"walletBean\":{\"withdrawableBal\":178039.52,\"depositBalance\":30300,\"winningBalance\":0,\"cashBalance\":181201.2,\"totalBalance\":203550.9,\"bonusBalance\":22349.7},\"playerId\":45,\"playerStatus\":\"FULL\",\"dob\":\"15/06/1991\",\"avatarPath\":\"/playerImages/45_image.png\",\"state\":\"Haryana\",\"country\":\"INDIA\"},\"mapping\":{\"516\":[202],\"551\":[186,216]},\"docUploadStatus\":\"UPLOADED\",\"profileUpdate\":true,\"fistDeposited\":true,\"errorCode\":0}";
		powerrMockTPIntegrationImpl(responseString);
		TpUserRegistrationBean registrationBean = WeaverIntegrationImpl.fetchUserDataAtWeaver(userInfoBean);
		Assert.assertEquals("",registrationBean.getFirstName());
	}
	@Test
	public void fetchUserDataAtWeaver_setLastNameAsBlankIfLastNameIsNull() throws Exception {
		String responseString = "{\"domainName\":\"192.168.124.32\",\"playerInfoBean\":{\"userName\":\"testplayer\",\"pinCode\":\"122001\",\"firstName\":\"testplayer\",\"emailId\":\"bingobunny06@gmail.com\",\"mobileNo\":9845142321,\"addressLine1\":\"Skilrock Technologies 1243213\",\"city\":\"Gurgaonsda\",\"stateCode\":\"IN-HR\",\"countryCode\":\"IN\",\"commonContentPath\":\"http://192.168.132.32:8180/WeaverDoc/commonContent/192.168.132.32\",\"gender\":\"M\",\"emailVerified\":\"Y\",\"phoneVerified\":\"Y\",\"walletBean\":{\"withdrawableBal\":178039.52,\"depositBalance\":30300,\"winningBalance\":0,\"cashBalance\":181201.2,\"totalBalance\":203550.9,\"bonusBalance\":22349.7},\"playerId\":45,\"playerStatus\":\"FULL\",\"dob\":\"15/06/1991\",\"avatarPath\":\"/playerImages/45_image.png\",\"state\":\"Haryana\",\"country\":\"INDIA\"},\"mapping\":{\"516\":[202],\"551\":[186,216]},\"docUploadStatus\":\"UPLOADED\",\"profileUpdate\":true,\"fistDeposited\":true,\"errorCode\":0}";
		powerrMockTPIntegrationImpl(responseString);
		TpUserRegistrationBean registrationBean = WeaverIntegrationImpl.fetchUserDataAtWeaver(userInfoBean);
		Assert.assertEquals("",registrationBean.getLastName());
	}
	@Test
	public void fetchUserDataAtWeaver_toCheckIfLastNameisSetIfNotNull() throws Exception {
		String responseString = "{\"domainName\":\"192.168.124.32\",\"playerInfoBean\":{\"userName\":\"jackbond10\",\"pinCode\":\"122001\",\"firstName\":\"testplayer\",\"lastName\":\"xyz\",\"emailId\":\"bingobunny06@gmail.com\",\"mobileNo\":9845142321,\"addressLine1\":\"Skilrock Technologies 1243213\",\"city\":\"Gurgaonsda\",\"stateCode\":\"IN-HR\",\"countryCode\":\"IN\",\"commonContentPath\":\"http://192.168.132.32:8180/WeaverDoc/commonContent/192.168.132.32\",\"gender\":\"M\",\"emailVerified\":\"Y\",\"phoneVerified\":\"Y\",\"walletBean\":{\"withdrawableBal\":178039.52,\"depositBalance\":30300,\"winningBalance\":0,\"cashBalance\":181201.2,\"totalBalance\":203550.9,\"bonusBalance\":22349.7},\"playerId\":45,\"playerStatus\":\"FULL\",\"dob\":\"15/06/1991\",\"avatarPath\":\"/playerImages/45_image.png\",\"state\":\"Haryana\",\"country\":\"INDIA\"},\"mapping\":{\"516\":[202],\"551\":[186,216]},\"docUploadStatus\":\"UPLOADED\",\"profileUpdate\":true,\"fistDeposited\":true,\"errorCode\":0}";
		powerrMockTPIntegrationImpl(responseString);
		TpUserRegistrationBean registrationBean = WeaverIntegrationImpl.fetchUserDataAtWeaver(userInfoBean);
		Assert.assertEquals("xyz",registrationBean.getLastName());
	}
	
	@Test
	public void purchaseTicketAtWeaver_toCheckIfresponseBeanIsSetOrNot() throws Exception {
		String responseString = "{\"sessionId\":\"wLx9pfAacyCY0TAi0AOKdQ**\",\"errorCode\":0,\"playerId\":76,\"totalBal\":509.5,\"txnType\":\"WAGER\",\"realBal\":509.5,\"transactionInfoList\":[{\"amount\":10,\"gameName\":\"Soccer\",\"txnId\":10546898,\"refTxnNo\":\"1235\",\"gameId\":123,\"particular\":\"FreeTicket-BONUS-LOTO-8:50\",\"balanceType\":\"MAIN\",\"walletType\":\"CASH\",\"wrContriAmount\":100},{\"amount\":50,\"gameName\":\"BONUS-LOTO\",\"txnId\":10546899,\"refTxnNo\":\"1236\",\"gameId\":123,\"particular\":\"Wager-BONUS-LOTO-8:30\",\"balanceType\":\"MAIN\",\"walletType\":\"CASH\",\"wrContriAmount\":100}]}";
		powerrMockTPIntegrationImpl(responseString);
		WeaverGamePlayResponseBean gameResponseBean = WeaverIntegrationImpl.purchaseTicketAtWeaver(sleTxnsIdMap, "soccer13", 1, userInfoBean);
		Assert.assertEquals(0,gameResponseBean.getErrorCode());
	}
	@Test(expected = SLEException.class)
	public void confirmTicketTxnAtWeaver_throwsSLEExceptionIfWeaverTXnBeanListIsNullOrEmpty () throws SLEException{
		List<WeaverPlayerTxnDataBean> weaverPlrTxnBeanList = null;
		WeaverIntegrationImpl.confirmTicketTxnAtWeaver(weaverPlrTxnBeanList);
		
	}
	@Test
	public void confirmTicketTxnAtWeaver_tocCheckIfResponseDataListIsSetOrNot() throws Exception {
		WeaverPlayerTxnDataBean bean = new WeaverPlayerTxnDataBean();
		List<WeaverPlayerTxnDataBean> list = new ArrayList<>();
		list.add(bean);
		String responseString = "{\"plrWiseRespBean\":[{\"errorCode\":0,\"playerId\":76,\"totalBal\":409.5,\"refWagerTxnId\":10546894,\"realBal\":409.5,\"bonusToCash\":0},{\"errorCode\":201,\"playerId\":165663,\"refWagerTxnId\":10546894,\"errorMsg\":\"Invalid request\",\"bonusToCash\":0}],\"errorCode\":0,\"txnType\":\"WAGER_CONFIRMATION\",\"serviceCode\":\"CASINO\"}";
		powerrMockTPIntegrationImpl(responseString);
		List<WeaverPlayerTxnResponseDataBean> responSeList = WeaverIntegrationImpl.confirmTicketTxnAtWeaver(list);
		Assert.assertTrue(!responSeList.isEmpty());
	}
	@Test(expected = SLEException.class)
	public void cancelTicketTxnAtWeaver_SLETxnsIdMapIsEmpty() throws GenericException, SLEException {
		powerrMockTPIntegrationImpl("{\"serviceCode\":\"SPORTS_LOTTERY\",\"txnType\":\"WAGER_REFUND\",\"errorCode\":0,\"plrWiseRespBean\":[{\"playerId\":583,\"txnId\":231272,\"totalBal\":8773.4,\"refWagerTxnId\":231271,\"errorCode\":0,\"realBal\":8773.4}]}");
		WeaverIntegrationImpl.cancelTicketTxnAtWeaver(new HashMap<Long,TransactionBean >(), userInfoBean);
	}
	@Test(expected = SLEException.class)
	public void cancelTicketTxnAtWeaver_UserInfoBeanIsNull() throws GenericException, SLEException {
		powerrMockTPIntegrationImpl("{\"serviceCode\":\"SPORTS_LOTTERY\",\"txnType\":\"WAGER_REFUND\",\"errorCode\":0,\"plrWiseRespBean\":[{\"playerId\":583,\"txnId\":231272,\"totalBal\":8773.4,\"refWagerTxnId\":231271,\"errorCode\":0,\"realBal\":8773.4}]}");
		WeaverIntegrationImpl.cancelTicketTxnAtWeaver(sleTxnsIdMap, null);
	}
	@Test(expected = GenericException.class)
	public void cancelTicketTxnAtWeaver_ResponseFromWeaverIsNull() throws GenericException, SLEException {
		powerrMockTPIntegrationImpl(null);
		WeaverIntegrationImpl.cancelTicketTxnAtWeaver(sleTxnsIdMap, userInfoBean);
	}
}
 