package com.skilrock.sle.drawMgmt.controllerImpl;

import static org.junit.Assert.*;

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

import com.skilrock.sle.common.DBConnect;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.dataMgmt.javaBeans.FetchDrawEventsRequest;
import com.skilrock.sle.drawMgmt.daoImpl.ResultSubmissionDaoImpl;
import com.skilrock.sle.gameDataMgmt.javaBeans.EventMasterBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.FreezeDrawBean;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DBConnect.class})
@SuppressStaticInitializationFor({"com.skilrock.sle.common.DBConnect"})
public class ResultSubmissionControllerImplTest {

	private ResultSubmissionDaoImpl resultSubmissionDaoImpl = Mockito.mock(ResultSubmissionDaoImpl.class);
	private ResultSubmissionControllerImpl resultSubmissionControllerImpl = new ResultSubmissionControllerImpl(resultSubmissionDaoImpl);
	private List<EventMasterBean> eventMasterList = null;
	private List<FreezeDrawBean> freezedDrawList = null;
    private Connection connection = null;
    Map<String,Double> TestsimnetMap=new HashMap<String,Double>();
	
    
    @Before
	public void mockConnectionObject() throws Exception {
		connection = Mockito.mock(Connection.class);
		PowerMockito.mockStatic(DBConnect.class);
		PowerMockito.when(DBConnect.class,"getConnection").thenReturn(connection);
		eventMasterList = new ArrayList<>();
		EventMasterBean eventMasterBean = new EventMasterBean();
		eventMasterList.add(eventMasterBean);
		freezedDrawList = new ArrayList<>();
		FreezeDrawBean freezeDrawBean = new FreezeDrawBean();
		freezedDrawList.add(freezeDrawBean);
		TestsimnetMap.put("Total Sale", 1000.00);
		TestsimnetMap.put("12 Match Winners", 1000.01);
		TestsimnetMap.put("11 Match Winners", 500.91);
		TestsimnetMap.put("10 Match Winners",null);
	}
    
	
	@Test
	public void getEventMasterDetails_ReturnNullEventMasterListIfExceptionOccurred() throws Exception{
		List<EventMasterBean> eventsList = null;
		FetchDrawEventsRequest fetchDrawEventsRequest = new FetchDrawEventsRequest();
		PowerMockito.mockStatic(DBConnect.class);
		PowerMockito.when(DBConnect.class,"getConnection").thenThrow(new ArrayIndexOutOfBoundsException());
		eventsList = resultSubmissionControllerImpl.getEventMasterDetails(fetchDrawEventsRequest);
		Assert.assertNull(eventsList);
	}
	
	@Test(expected=SLEException.class)
	public void freezedDrawResult_NullPointerExceptionOccurred() throws Exception{
		PowerMockito.mockStatic(DBConnect.class);
		PowerMockito.when(DBConnect.class,"getConnection").thenThrow(new NullPointerException());
		resultSubmissionControllerImpl.freezedDrawResult(1,1,11002,2);
	}
	
	@Test
	public void getEventMasterDetails_ReturnNullEventMasterListIfSLEExceptionOccurred() throws Exception{
		List<EventMasterBean> eventsList = null;
		FetchDrawEventsRequest fetchDrawEventsRequest = new FetchDrawEventsRequest();
		mockConnectionObject();
		Mockito.when(resultSubmissionDaoImpl.getEventMasterDetails(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.any(Connection.class))).thenThrow(new SLEException());
		eventsList = resultSubmissionControllerImpl.getEventMasterDetails(fetchDrawEventsRequest);
		Assert.assertNull(eventsList);
	}
	

	@Test(expected=SLEException.class)
	public void freezedDrawResult_IfSLEExceptionOccurred() throws Exception{
		mockConnectionObject();
		Mockito.when(resultSubmissionDaoImpl.getUserResultAuthorization(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt(), Mockito.any(Connection.class))).thenReturn(true);
		Mockito.when(resultSubmissionDaoImpl.getFreezedDrawsList(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt(), Mockito.any(Connection.class))).thenThrow(new SLEException());
		resultSubmissionControllerImpl.freezedDrawResult(1,1,11002,2);
	}
	
	
	
	@Test
	public void getEventMasterDetails_ReturnNonNullListEventMasterList() throws Exception{
		List<EventMasterBean> eventsList = null;
		FetchDrawEventsRequest fetchDrawEventsRequest = new FetchDrawEventsRequest();
		mockConnectionObject();
		Mockito.when(resultSubmissionDaoImpl.getEventMasterDetails(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.any(Connection.class))).thenReturn(eventMasterList);
		eventsList = resultSubmissionControllerImpl.getEventMasterDetails(fetchDrawEventsRequest);
		Assert.assertEquals(eventMasterList,eventsList);
	}
	
	@Test
	public void freezedDrawResult_ReturnNonNullListEventMasterList() throws Exception{
		List<FreezeDrawBean> freezedDrawListTest = null;
		mockConnectionObject();
		Mockito.when(resultSubmissionDaoImpl.getUserResultAuthorization(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt(), Mockito.any(Connection.class))).thenReturn(true);
		Mockito.when(resultSubmissionDaoImpl.getFreezedDrawsList(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt(),Mockito.any(Connection.class))).thenReturn(freezedDrawList);
		freezedDrawListTest = resultSubmissionControllerImpl.freezedDrawResult(1,1,11002,2);
		Assert.assertEquals(freezedDrawList,freezedDrawListTest);
	}
	
	@Test(expected=SLEException.class)
	public void simnetPrizeDistribution_IfSLEExceptionOccurred() throws Exception{
		Mockito.when(resultSubmissionDaoImpl.simnetPrizeDistributionDao(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt(),Mockito.any(Connection.class))).thenThrow(new SLEException());
		resultSubmissionControllerImpl.simnetPrizeDistribution(1,1,208,1000,5,5,5,5);
	}
	
	@Test(expected=SLEException.class)
	public void simnetPrizeDistribution_IfNullPointerExceptionOccurred() throws Exception{
		Mockito.when(resultSubmissionDaoImpl.simnetPrizeDistributionDao(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt(),Mockito.any(Connection.class))).thenThrow(new NullPointerException());
		resultSubmissionControllerImpl.simnetPrizeDistribution(1,1,208,1000,5,5,5,5);
	}
	
	@Test
	public void simnetPrizeDistribution_CorrectSimnetMap() throws Exception{
		Mockito.when(resultSubmissionDaoImpl.simnetPrizeDistributionDao(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt(),Mockito.any(Connection.class))).thenReturn(TestsimnetMap);
		Map<String,Double>testMap2=resultSubmissionControllerImpl.simnetPrizeDistribution(1,1,208,1000,5,5,5,5);
		assertEquals(Double.valueOf(1000.00), testMap2.get("Total Sale"));
	}
	
	@Test
	public void simnetPrizeDistribution_CorrectSimnetMapWithNullparameter() throws Exception{
		Mockito.when(resultSubmissionDaoImpl.simnetPrizeDistributionDao(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt(),Mockito.any(Connection.class))).thenReturn(TestsimnetMap);
		Map<String,Double>testMap2=resultSubmissionControllerImpl.simnetPrizeDistribution(1,1,208,1000,5,5,5,5);
		Assert.assertNull(testMap2.get("10 Match Winners"));
	}
	
	@Test
	public void simnetPrizeDistribution_NullSimnetMap() throws Exception{
		Map<String,Double>SimnetTestNullMap=new HashMap<String,Double>();
		SimnetTestNullMap=null;
		Mockito.when(resultSubmissionDaoImpl.simnetPrizeDistributionDao(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt(),Mockito.any(Connection.class))).thenReturn(SimnetTestNullMap);
		Map<String,Double>testMap2=resultSubmissionControllerImpl.simnetPrizeDistribution(1,1,208,1000,5,5,5,5);
		Assert.assertNull(testMap2);
	}
	
	
}
