package com.skilrock.sle.gameDataMgmt.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.skilrock.sle.common.DBConnect;
import com.skilrock.sle.common.SLELogger;
import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.gameDataMgmt.javaBeans.MappedTeamMasterBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.TeamMasterBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.VenueMasterBean;

public class TeamMasterDataDaoImpl {
	private static final SLELogger logger = SLELogger.getLogger(TeamMasterDataDaoImpl.class.getName());

	public List<TeamMasterBean> getTeamMasterData(int gameId, int leagueId, Connection connection) throws SLEException {
		List<TeamMasterBean> teamMasterList = null;
		TeamMasterBean teamMasterBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		String query = null;
		String leagueQuery = null;
		try {
				if(leagueId > 0){
					leagueQuery = " and league_id = "+leagueId;
				}else{
					leagueQuery = "";
				}
			query = "SELECT a.team_id, game_id, team_name, team_code, a.status FROM st_sl_game_team_master a, st_sl_league_team_mapping b WHERE game_id="+gameId+" AND a.status='ACTIVE' and a.team_id = b.team_id "+leagueQuery+" order by team_name";
			teamMasterList = new ArrayList<TeamMasterBean>();
			stmt = connection.createStatement();
			logger.info("TeamMasterDetails - "+query);
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				teamMasterBean = new TeamMasterBean();
				teamMasterBean.setTeamId(rs.getInt("team_id"));
				teamMasterBean.setGameId(rs.getInt("game_id"));
				teamMasterBean.setTeamName(rs.getString("team_name"));
				teamMasterBean.setTeamCode(rs.getString("team_code"));
				teamMasterBean.setStatus(rs.getString("status"));
				teamMasterBean.setTeamCodeId(rs.getString("team_code")+"_"+rs.getInt("team_id"));
				teamMasterList.add(teamMasterBean);
			}
		} catch (SQLException se) {
			se.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}

		return teamMasterList;
	}
	
	/**
	 * This method fetches the information regarding all the venues available in database in 
	 * st_sl_venue_master table that are active.
	 * @param connection
	 * @return venueMasterList
	 * @throws SLEException
	 * @author Unknown
	 */
	
	public List<VenueMasterBean> getVenueMasterData(Connection connection) throws SLEException {
		List<VenueMasterBean> venueMasterList = null;
		VenueMasterBean venueMasterBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		String query = null;
		try {
				
			query = "select venue_id, venue_display_name, venue_code, status from st_sl_venue_master where status = 'ACTIVE' order by venue_display_name";
			venueMasterList = new ArrayList<VenueMasterBean>();
			stmt = connection.createStatement();
			logger.info("VenueMasterDetails - "+query);
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				venueMasterBean = new VenueMasterBean();
				venueMasterBean.setVenueId(rs.getInt("venue_id"));
				venueMasterBean.setVenueDispName(rs.getString("venue_display_name"));
				venueMasterBean.setVenueCode(rs.getString("venue_code"));
				venueMasterBean.setStatus(rs.getString("status"));				
				venueMasterList.add(venueMasterBean);
			}
		} catch (SQLException se) {
			se.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}

		return venueMasterList;
	}
	/**
	 * This method overloads getVenueMasterData(Connection connection).This method fetches venue information and stores
	 * it in venueMasterList
	 * @param connection
	 * @param venueMasterList
	 * @throws SLEException
	 * @since 15-Oct-2015
	 * @author Rishi
	 */
	public void getVenueMasterData(Connection connection, List<VenueMasterBean> venueMasterList) throws SLEException{
		VenueMasterBean venueMasterBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		String query = null;
		try {
			query = "select venue_id, venue_display_name, venue_code, status from st_sl_venue_master where status = 'ACTIVE' order by venue_display_name";
			stmt = connection.createStatement();
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				venueMasterBean = new VenueMasterBean();
				venueMasterBean.setVenueId(rs.getInt("venue_id"));
				venueMasterBean.setVenueDispName(rs.getString("venue_display_name"));
				venueMasterBean.setVenueCode(rs.getString("venue_code"));
				venueMasterBean.setStatus(rs.getString("status"));				
				venueMasterList.add(venueMasterBean);
			}
		} catch (SQLException se) {
			se.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
	}
	
	public Map<Integer, MappedTeamMasterBean> getMappedTeamMasterData(Connection connection,int gameIds,int leagueId) throws SLEException{
		HashMap<Integer, MappedTeamMasterBean> mappedTeamData=null;
		MappedTeamMasterBean mappedTeamMasterBean=null;
		Statement stmt = null;
		ResultSet rs = null;
		String query = null;
		try {
			query = "Select s1.team_name,s1.game_id,s1.team_id,s1.team_code,s1.status from st_sl_game_team_master As s1 INNER JOIN (Select team_id,status from st_sl_league_team_mapping where league_id="+leagueId+" and status='ACTIVE') As s2 on s1.team_id=s2.team_id where s1.status='ACTIVE'";
			mappedTeamData = new HashMap<Integer,MappedTeamMasterBean>();
			stmt = connection.createStatement();
			logger.info("MappedTeamsDetails - "+query);
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				mappedTeamMasterBean = new MappedTeamMasterBean();
				mappedTeamMasterBean.setGameId(rs.getInt("s1.game_id"));
				mappedTeamMasterBean.setTeamId(rs.getInt("s1.team_id"));
				mappedTeamMasterBean.setTeamName(rs.getString("s1.team_name"));
				mappedTeamMasterBean.setTeamCode(rs.getString("s1.team_code"));
				mappedTeamMasterBean.setStatus(rs.getString("s1.status"));				
				mappedTeamData.put(rs.getInt("s1.team_id"),mappedTeamMasterBean);
		   }
		}catch (SQLException se) {
			se.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally{
			DBConnect.closeConnection(stmt,rs);
		}
		return mappedTeamData;
	}

	public Map<Integer,TeamMasterBean> getUnMappedTeamMasterData(Connection connection,int gameIds,int leagueId) throws SLEException{
		Map<Integer, TeamMasterBean> allTeamsData=null;
		TeamMasterBean allTeamMasterBean=null;
		Statement stmt = null;
		ResultSet rs = null;
		String query = null;
		try {
			query = "Select s1.team_name,s1.game_id,s1.team_id,s1.team_code,s1.status from st_sl_game_team_master As s1 left JOIN (Select team_id,status from st_sl_league_team_mapping where league_id="+leagueId+" and status='ACTIVE') As s2 on s1.team_id=s2.team_id where s1.status='ACTIVE' and s2.team_id is null";
			allTeamsData = new HashMap<Integer,TeamMasterBean>();
			stmt = connection.createStatement();
			logger.info("UnMappedTeamsDetails - "+query);
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				allTeamMasterBean = new TeamMasterBean();
				allTeamMasterBean.setGameId(rs.getInt("game_id"));
				allTeamMasterBean.setTeamId(rs.getInt("team_id"));
				allTeamMasterBean.setTeamName(rs.getString("team_name"));
				allTeamMasterBean.setTeamCode(rs.getString("team_code"));
				allTeamMasterBean.setStatus(rs.getString("status"));				
				allTeamsData.put(rs.getInt("team_id"),allTeamMasterBean);
			}
		} catch (SQLException se) {
			se.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally{
			DBConnect.closeConnection(stmt,rs);
		}
		return allTeamsData;
	}
	
	public int eventInsertionSubmit(int gameId, String eventDisplay, String eventDiscription, Timestamp startTime, Timestamp endTime, int venue, String homeTeam, String awayTeam,String homeTeamodds,String awayTeamOdds,String drawOdds, int leagueId, String venue_Name, Connection connection) throws SLEException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		PreparedStatement insertPstmt = null;
		ResultSet insertRs = null;
		int eventId = 0;
		int cityId = 0;
		int venueId = 0;
		try {
			if(venue == -1){
				insertPstmt = connection.prepareStatement("INSERT INTO st_sl_venue_master (venue_display_name,venue_code, city_id, status) VALUES(?,?,?,?);", Statement.RETURN_GENERATED_KEYS);
				insertPstmt.setString(1,venue_Name);
				insertPstmt.setString(2,venue_Name.length()<=3?venue_Name.toUpperCase():venue_Name.substring(0, 3).toUpperCase());
				insertPstmt.setInt(3, cityId);
				insertPstmt.setString(4, "ACTIVE");
				insertPstmt.executeUpdate();
				insertRs = insertPstmt.getGeneratedKeys();
				if(insertRs.next()){
					venueId = insertRs.getInt(1);
				}
			}
			else{
				venueId = venue;
			}
			pstmt = connection.prepareStatement("INSERT INTO st_sl_event_master (game_id, event_display, event_description, start_time, end_time, entry_time, venue_id, home_team_id, away_team_id,home_team_odds,away_team_odds,draw_odds,league_id) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?);", Statement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1, gameId);
			pstmt.setString(2, eventDisplay);
			pstmt.setString(3, eventDiscription);
			pstmt.setTimestamp(4, startTime);
			pstmt.setTimestamp(5, endTime);
			pstmt.setTimestamp(6, Util.getCurrentTimeStamp());
			pstmt.setInt(7, venueId);
			pstmt.setInt(8, Integer.parseInt(homeTeam.split("_")[1]));
			pstmt.setInt(9, Integer.parseInt(awayTeam.split("_")[1]));
			pstmt.setString(10, homeTeamodds);
			pstmt.setString(11, awayTeamOdds);
			pstmt.setString(12, drawOdds);
			pstmt.setInt(13, leagueId);
			logger.info("InsertInEventMaster - "+pstmt);
			pstmt.executeUpdate();
			rs = pstmt.getGeneratedKeys();
			if(rs.next()) {
				eventId = rs.getInt(1);
			}
			logger.info("EventId - "+eventId);
		} catch (SQLException se) {
			se.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}

		return eventId;
	}
}