package com.skilrock.sle.gameDataMgmt.controllerImpl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.skilrock.sle.common.DBConnect;
import com.skilrock.sle.common.UtilityFunctions;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.commonMethod.daoImpl.CommonMethodsDaoImpl;
import com.skilrock.sle.dataMgmt.javaBeans.SimnetResultDrawsBean;
import com.skilrock.sle.gameDataMgmt.daoImpl.DrawEventMappingDaoImpl;
import com.skilrock.sle.gameDataMgmt.daoImpl.TeamMasterDataDaoImpl;
import com.skilrock.sle.gameDataMgmt.javaBeans.DrawMasterBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.EventMasterBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.LeagueMasterBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.MappedEventMasterBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.TeamMasterBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.VenueMasterBean;

public class DrawEventMappingControllerImpl {

	public List<DrawMasterBean> getDrawMappingDrawMasterList(int gameId, int gameTypeId, String merchantName) throws SLEException {
		Connection connection = null;
		DrawEventMappingDaoImpl daoImpl = null;
		List<DrawMasterBean> drawMasterList = null;
		try {
			connection = DBConnect.getConnection();
			daoImpl = new DrawEventMappingDaoImpl();
			int merchantId = UtilityFunctions.getMerchantIdFromMerchantName(merchantName, connection);
			drawMasterList = daoImpl.getDrawMappingDrawMasterList(gameId, gameTypeId, merchantId, connection);
		} catch (SLEException se) {
			throw se;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(connection);
		}

		return drawMasterList;
	}

	public List<EventMasterBean> getDrawMappingEventMasterList(int gameId, int gameTypeId, int drawId) throws SLEException {
		Connection connection = null;
		DrawEventMappingDaoImpl daoImpl = null;
		List<EventMasterBean> eventMasterList = null;
		try {
			connection = DBConnect.getConnection();
			daoImpl = new DrawEventMappingDaoImpl();
			eventMasterList = daoImpl.getDrawMappingEventMasterList(gameId, gameTypeId, drawId, connection);
		} catch (SLEException se) {
			throw se;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(connection);
		}

		return eventMasterList;
	}

	public String drawEventMappingSubmit(int gameId, int gameTypeId, int drawId, int[] eventSelected) throws SLEException {
		Connection connection = null;
		DrawEventMappingDaoImpl daoImpl = null;
		String retValue = null;
		Statement stmt = null;
		String query = null;
		ResultSet rs = null;
		try {
			connection = DBConnect.getConnection();
			connection.setAutoCommit(false);
			daoImpl = new DrawEventMappingDaoImpl();
			stmt = connection.createStatement();
			query = "SELECT event_draw_id FROM st_sl_draw_event_mapping_"+gameId+" WHERE draw_id = "+drawId+"";
			rs = stmt.executeQuery(query);
			if(rs.next()){
				throw new SLEException(SLEErrors.EVENT_MAPPING_FAILED_ERROR_CODE, SLEErrors.EVENT_MAPPING_FAILED_ERROR_MESSAGE);
			}
			daoImpl.eventOptionInsertion(gameId, gameTypeId, eventSelected, connection);
			retValue = daoImpl.drawEventMappingSubmit(gameId, gameTypeId, drawId, eventSelected, connection);
			connection.commit();
		} catch (SLEException se) {
			throw se;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(stmt,rs);
			DBConnect.closeConnection(connection);
		}
		return retValue;
	}
	
	public List<EventMasterBean> fetchSelectedEventDetails(String eventIds) throws SLEException {
		Connection connection = null;
		DrawEventMappingDaoImpl daoImpl = null;
		List<EventMasterBean> eventMasterList = null;
		try {
			connection = DBConnect.getConnection();
			daoImpl = new DrawEventMappingDaoImpl();
			eventMasterList = daoImpl.fetchSelectedEventDetails(eventIds, connection);
		} catch (SLEException se) {
			throw se;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(connection);
		}

		return eventMasterList;
	}
	
	/**
	 * <p>This method creates the drawMasterList by calling the function getDrawMasterListInfo and passing the gameId, gameTypeId,
	 * merchantId and connection as parameters.</p>
	 * @param gameId
	 * @param gameTypeId
	 * @param merchantName
	 * @return drawMasterList
	 * @throws SLEException
	 * @since 02-Oct-2015
	 * @author Rishi
	 */
	
	public List<DrawMasterBean> getGameTypeDrawMasterList(int gameId, int gameTypeId, String merchantName) throws SLEException {
		Connection connection = null;
		DrawEventMappingDaoImpl daoImpl = null;
		List<DrawMasterBean> drawMasterList = null;	
		try {
			connection = DBConnect.getConnection();
			daoImpl = new DrawEventMappingDaoImpl();
			int merchantId = UtilityFunctions.getMerchantIdFromMerchantName(merchantName, connection);
			drawMasterList = daoImpl.getGameTypeDrawMasterListInfo(gameId, gameTypeId, merchantId, connection);
		} catch (SLEException se) {
			throw se;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(connection);
		}

		return drawMasterList;
	}
	

	/**
	 * <p>This method calls getEventsList method to create arraylist - mappedEventMasterList that contains information regarding the mapped 
	 * events for a particular draw, getLeagueMaInfo method to create map - leagueMap that contains the information 
	 * regarding all the leagues, getAllTeam method to create arraylist - teamMasterList that contains information regarding all the 
	 * teams and getVenueData method to create arraylist - venueMasterList that contains the information for all the venue.</p> 
	 * @param gameId
	 * @param drawId
	 * @param mappedEventMasterList
	 * @param leagueMap
	 * @param teamMasterList
	 * @param venueMasterList
	 * @throws SLEException
	 * @since 07-Oct-2015
	 * @author Rishi
	 */
	
	public void getInformationForEventManagement(int gameId,int drawId,List<MappedEventMasterBean> mappedEventMasterList,List<TeamMasterBean> teamMasterList, Map<Integer, LeagueMasterBean> leagueMap, List<VenueMasterBean> venueMasterList) throws SLEException{
		Connection connection = null;
		DrawEventMappingDaoImpl daoImpl = null;
		TeamMasterDataDaoImpl teamDao = null;
		Set<Integer> mappedLeagueSet=null;
		try{
			connection = DBConnect.getConnection();
			daoImpl = new DrawEventMappingDaoImpl();
			teamDao = new TeamMasterDataDaoImpl();
			mappedLeagueSet=new HashSet<Integer>();
			daoImpl.getMappedEventsList(connection,drawId,mappedEventMasterList,gameId,mappedLeagueSet);
			daoImpl.getLeagueTeamMasterData(gameId, mappedLeagueSet,teamMasterList, connection);
			teamDao.getVenueMasterData(connection,venueMasterList);
			CommonMethodsDaoImpl.getLeagueMap(connection,leagueMap);
		}catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(connection);
		}
	}
	
	/**
	 * <p>This method gets draw information for particular event.</p>
	 * @param gameId
	 * @param eventId
	 * @param merchantName
	 * @return drawListForEvent
	 * @throws SLEException
	 * @since 16-Oct-2015
	 * @author Rishi
	 * @param venueMasterList 
	 * @param leagueMap 
	 */
	
	public List<DrawMasterBean> drawInfoForParticularEvent(int gameId,int eventId,String merchantName) throws SLEException{
		Connection conn = null;
		DrawEventMappingDaoImpl daoImpl = null;
		List<DrawMasterBean> drawListForEvent = null;
		try{
			conn = DBConnect.getConnection();
			daoImpl = new DrawEventMappingDaoImpl();
			int merchantId = UtilityFunctions.getMerchantIdFromMerchantName(merchantName, conn);
			drawListForEvent = daoImpl.getDrawMasterListInfoForEvent(gameId, eventId, conn, merchantId);
		} catch (SLEException se) {
			throw se;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(conn);
		}
		return drawListForEvent;
			
		}
	
	
	
	public List<SimnetResultDrawsBean> getSimnetResultDraws(int gameId,int gameTypeId,int merchantId) throws Exception
	{   
		Connection conn = null;
		DrawEventMappingDaoImpl daoImpl = null;
		List<SimnetResultDrawsBean> simnetResultDrawsList = null;
		try{
			conn = DBConnect.getConnection();
			daoImpl = new DrawEventMappingDaoImpl();
			simnetResultDrawsList = daoImpl.getSimnetResultDraws(gameId,gameTypeId,merchantId,conn);
		} catch (SLEException se) {
			throw se;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(conn);
		}
		return simnetResultDrawsList;
	}
	
	
	
	
	
}