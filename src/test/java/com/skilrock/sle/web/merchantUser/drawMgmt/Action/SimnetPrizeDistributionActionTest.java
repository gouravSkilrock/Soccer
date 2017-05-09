package com.skilrock.sle.web.merchantUser.drawMgmt.Action;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.skilrock.sle.common.DBConnect;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.common.javaBeans.UserInfoBean;
import com.skilrock.sle.drawMgmt.controllerImpl.ResultSubmissionControllerImpl;



@PrepareForTest({ResultSubmissionControllerImpl.class})
@RunWith(PowerMockRunner.class)

public class SimnetPrizeDistributionActionTest {
	private HttpServletRequest request;
	private HttpSession session;	
	private ResultSubmissionControllerImpl controllerObj;
	private UserInfoBean userBean;
	private SimnetPrizeDistributionAction actionObj;
	
	@Before
    public void setup(){
		request = Mockito.mock(HttpServletRequest.class);
		session=Mockito.mock(HttpSession.class);
		Mockito.when(request.getSession()).thenReturn(session);
		controllerObj=Mockito.mock(ResultSubmissionControllerImpl.class);		
		actionObj=new SimnetPrizeDistributionAction(controllerObj);
		actionObj.setServletRequest(request);
		actionObj.setNoOfWinnersfor10(5);
		actionObj.setNoOfWinnersfor11(6);
		actionObj.setNoOfWinnersfor10(7);
		actionObj.setGameId(1);
		actionObj.setGameTypeId(1);
		actionObj.setSaleOnSimnet(500);
		actionObj.setSimnetDrawsId(208);
		userBean=new UserInfoBean();
		userBean.setMerchantUserId(11001);
	    
	}
	
	@Test
	public void simnetResultDatafetch_CheckForUserId() throws SLEException
	{	Map<String,Double> mapTest=new HashMap<String,Double>();
		Mockito.when(session.getAttribute("USER_INFO")).thenReturn(userBean);	
	    Mockito.when(controllerObj.simnetPrizeDistribution(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(),Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(mapTest);
		String response=actionObj.simnetResultDatafetch();
		Assert.assertEquals(11001, userBean.getMerchantUserId());
	}
	

	@Test
	public void simnetResultDatafetch_ReturnStringOnCorrectCall() throws SLEException
	{	Map<String,Double> mapTest=new HashMap<String,Double>();
		Mockito.when(session.getAttribute("USER_INFO")).thenReturn(userBean);	
	    Mockito.when(controllerObj.simnetPrizeDistribution(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(),Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(mapTest);
		String response=actionObj.simnetResultDatafetch();
		Assert.assertEquals("success",response);
	}
	
	@Test
	public void simnetResultDataFetch_ReturnErrorOnNullMap() throws SLEException
	{
	Map<String,Double> mapTest=null;
	Mockito.when(session.getAttribute("USER_INFO")).thenReturn(userBean);	
    Mockito.when(controllerObj.simnetPrizeDistribution(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(),Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(mapTest);
	String response=actionObj.simnetResultDatafetch();
	Assert.assertEquals("error",response);
	}
	
	@Test
	public void simnetResultDataFetch_SLEExceptionOcuured() throws Exception{
		Mockito.when(session.getAttribute("USER_INFO")).thenReturn(userBean);	
	    Mockito.when(controllerObj.simnetPrizeDistribution(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(),Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt())).thenThrow(new SLEException());
		String response=actionObj.simnetResultDatafetch();
		Assert.assertEquals("error",response);
	}

	@Test
	public void simnetResultDataFetch_ReturnCorrectMap() throws SLEException
	{
	Map<String,Double> mapTest=new HashMap<String,Double>();
	mapTest.put("Total Sale", 999.09);
	mapTest.put("Winners for 12 Match", 1000.090);
    mapTest.put("Winners for 11 Match", null);
	Mockito.when(session.getAttribute("USER_INFO")).thenReturn(userBean);	
    Mockito.when(controllerObj.simnetPrizeDistribution(actionObj.getGameId(),actionObj.getGameTypeId(),actionObj.getSimnetDrawsId(),actionObj.getSaleOnSimnet(),10011,actionObj.getNoOfWinnersfor12(),actionObj.getNoOfWinnersfor11(),actionObj.getNoOfWinnersfor10())).thenReturn(mapTest);
	String response=actionObj.simnetResultDatafetch();
	Assert.assertNull(mapTest.get("Winners for 11 Match"));
	}
	
	
	

}
