package com.skilrock.sle.gameDataMgmt.controllerImpl;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.skilrock.sle.common.DBConnect;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.gameDataMgmt.daoImpl.TeamMasterDataDaoImpl;
import com.skilrock.sle.gameDataMgmt.javaBeans.MappedTeamMasterBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.TeamMasterBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.VenueMasterBean;

public class TeamMasterDataControllerImpl {

	public List<TeamMasterBean> getTeamMasterData(int gameId, int leagueId) throws SLEException {
		Connection connection = null;
		TeamMasterDataDaoImpl daoImpl = new TeamMasterDataDaoImpl();
		List<TeamMasterBean> teamMasterList = null;
		try {
			connection = DBConnect.getConnection();
			teamMasterList = daoImpl.getTeamMasterData(gameId, leagueId, connection);
		} catch (SLEException se) {
			throw se;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(connection);
		}

		return teamMasterList;
	}
	
	public List<VenueMasterBean> getVenueMasterData() throws SLEException {
		Connection connection = null;
		TeamMasterDataDaoImpl daoImpl = new TeamMasterDataDaoImpl();
		List<VenueMasterBean> venueMasterList = null;
		try {
			connection = DBConnect.getConnection();
			venueMasterList = daoImpl.getVenueMasterData(connection);
		} catch (SLEException se) {
			throw se;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(connection);
		}

		return venueMasterList;
	}

	public Map<Integer, MappedTeamMasterBean> getMappedTeamData(int gameId,int leagueId) throws SLEException{
		Connection connection = null;
		TeamMasterDataDaoImpl daoImpl = new TeamMasterDataDaoImpl();
		Map<Integer,MappedTeamMasterBean> mappedTeamData=null;
		try {
			connection = DBConnect.getConnection();
			mappedTeamData = daoImpl.getMappedTeamMasterData(connection,gameId,leagueId);
		} catch (SLEException se) {
			throw se;
		} finally {
			DBConnect.closeConnection(connection);
		}
		return mappedTeamData;	
		}
	
	public Map<Integer,TeamMasterBean> getUnMappedTeamData(int gameId,int leagueId) throws SLEException{
		Connection connection = null;
		TeamMasterDataDaoImpl daoImpl = new TeamMasterDataDaoImpl();
		Map<Integer,TeamMasterBean> allTeamData=null;
		try {
			connection = DBConnect.getConnection();
			allTeamData = daoImpl.getUnMappedTeamMasterData(connection,gameId,leagueId);
		} catch (SLEException se) {
			throw se;
		}finally {
			DBConnect.closeConnection(connection);
		}
		return allTeamData;	
	}
	
	
	public String eventInsertionSubmit(int gameId, String homeTeam, String awayTeam, String homeTeamOdds,String awayTeamOdds,String drawOdds, String[] optionSet, long startTime, long endTime, int venue, int leagueId, String venue_Name) throws SLEException {
		Connection connection = null;
		TeamMasterDataDaoImpl daoImpl = new TeamMasterDataDaoImpl();
		int eventId = 0;
		try {
			String eventDisplay = homeTeam.split("_")[0]+"-vs-"+awayTeam.split("_")[0];
			String eventDiscription = homeTeam.split("_")[0]+"-vs-"+awayTeam.split("_")[0];
			Timestamp startTimeStamp = new Timestamp(startTime);
			Timestamp endTimeStamp = new Timestamp(endTime);

			connection = DBConnect.getConnection();
			connection.setAutoCommit(false);

			eventId = daoImpl.eventInsertionSubmit(gameId, eventDisplay, eventDiscription, startTimeStamp, endTimeStamp, venue, homeTeam, awayTeam,homeTeamOdds,awayTeamOdds,drawOdds,leagueId,venue_Name, connection);
			if(eventId == 0)
				throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			/*else
				daoImpl.eventOptionInsertion(eventId, optionSet, connection);*/

			connection.commit();
		} catch (SLEException se) {
			throw se;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(connection);
		}

		return "success";
	}
}