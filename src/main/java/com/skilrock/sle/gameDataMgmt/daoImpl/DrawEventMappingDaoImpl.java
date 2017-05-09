package com.skilrock.sle.gameDataMgmt.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.skilrock.sle.common.DBConnect;
import com.skilrock.sle.common.SLELogger;
import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.UtilityFunctions;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.commonMethod.daoImpl.CommonMethodsDaoImpl;
import com.skilrock.sle.dataMgmt.javaBeans.SimnetResultDrawsBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.DrawMasterBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.EventMasterBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.MappedEventMasterBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.TeamMasterBean;

public class DrawEventMappingDaoImpl {
	private static final SLELogger logger = SLELogger.getLogger(DrawEventMappingDaoImpl.class.getName());

	public List<DrawMasterBean> getDrawMappingDrawMasterList(int gameId, int gameTypeId, int merchantId, Connection connection) throws SLEException {
		List<DrawMasterBean> drawMasterList = null;
		DrawMasterBean drawMasterBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		String query = null;
		try {
			query = "SELECT mas.draw_id, map.draw_name, mas.draw_freeze_time " +
					"FROM st_sl_draw_master_"+gameId+" mas INNER JOIN st_sl_draw_merchant_mapping_"+gameId+" map ON mas.draw_id=map.draw_id " +
					"AND game_type_id="+gameTypeId+" AND draw_status IN ('ACTIVE','INACTIVE') AND merchant_id="+merchantId+" AND sale_start_time>'"+Util.getCurrentTimeStamp()+"' " +
					"AND mas.draw_id NOT IN(SELECT DISTINCT draw_id FROM st_sl_draw_event_mapping_"+gameId+");";
			drawMasterList = new ArrayList<DrawMasterBean>();
			stmt = connection.createStatement();
			logger.info("DrawEventMapping Draw Query - "+query);
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				drawMasterBean = new DrawMasterBean();
				drawMasterBean.setDrawId(rs.getInt("draw_id"));
				drawMasterBean.setDrawName(rs.getString("draw_name"));
				drawMasterBean.setDrawFreezeTime(UtilityFunctions.timeFormat(rs.getString("draw_freeze_time")));
				drawMasterList.add(drawMasterBean);
			}
		} catch (SQLException se) {
			se.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}

		return drawMasterList;
	}

	public List<EventMasterBean> getDrawMappingEventMasterList(int gameId, int gameTypeId, int drawId, Connection connection) throws SLEException {
		SimpleDateFormat simpleDateFormat = null;
		List<EventMasterBean> eventMasterList = null;
		EventMasterBean eventMasterBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		String query = null;
		try {
			simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

			query = "SELECT event_id, game_id, event_display, event_description, start_time, end_time, " +
					"(SELECT no_of_events FROM st_sl_game_type_master WHERE game_type_id="+gameTypeId+") no_of_events " +
					"FROM st_sl_event_master WHERE start_time>(SELECT draw_freeze_time FROM st_sl_draw_master_"+gameId+" WHERE draw_id="+drawId+") " +
					"AND start_time>'"+Util.getCurrentTimeStamp()+"' AND end_time<(SELECT draw_datetime FROM st_sl_draw_master_"+gameId+" WHERE draw_id="+drawId+") " +
					"AND game_id="+gameId+" group by event_display,start_time,end_time order by start_time;";
			eventMasterList = new ArrayList<EventMasterBean>();
			stmt = connection.createStatement();
			logger.info("DrawEventMapping Event Query - "+query);
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				eventMasterBean = new EventMasterBean();
				eventMasterBean.setEventId(rs.getInt("event_id"));
				eventMasterBean.setGameId(rs.getInt("game_id"));
				eventMasterBean.setEventDisplay(rs.getString("event_display"));
				eventMasterBean.setEventDescription(rs.getString("event_description"));
				eventMasterBean.setStartTime(simpleDateFormat.format(rs.getTimestamp("start_time")));
				eventMasterBean.setEndTime(simpleDateFormat.format(rs.getTimestamp("end_time")));
				eventMasterBean.setNoOfEvents(rs.getInt("no_of_events"));
				eventMasterList.add(eventMasterBean);
			}
		} catch (SQLException se) {
			se.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return eventMasterList;
	}

	public String drawEventMappingSubmit(int gameId, int gameTypeId, int drawId, int[] eventSelected, Connection connection) throws SLEException {
		//Statement stmt = null;
		PreparedStatement pstmt = null;
		//String query = null;
		try {
			/*
			query = "DELETE FROM st_sl_draw_event_mapping_"+gameId+" WHERE draw_id="+drawId+";";
			stmt = connection.createStatement();
			logger.info("Delete Draw Event Query - "+query);
			stmt.executeUpdate(query);
			*/
			pstmt = connection.prepareStatement("INSERT INTO st_sl_draw_event_mapping_"+gameId+" (event_id, draw_id, event_order) VALUES (?,?,?);");
			int eventOrder=1;
			for(int event : eventSelected) {
				pstmt.setInt(1, event);
				pstmt.setInt(2, drawId);
				pstmt.setInt(3, eventOrder++);
				pstmt.addBatch();
			}
			logger.info("Insert Draw Event Query - "+pstmt);
			pstmt.executeBatch();
		} catch (SQLException se) {
			se.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}finally{
			DBConnect.closePstmt(pstmt);
		}

		return "SUCCESS";
	}
	
	public void eventOptionInsertion(int gameId, int gameTypeId,int[] eventSelected, Connection connection) throws SLEException {
		PreparedStatement pstmt = null;
		Statement stmt = null;
		Map<String,String> optionMap = null;
		ResultSet rs = null;
		String checkEventExistQuery=null;
		List<String> list = null;
		try { 
			checkEventExistQuery = "SELECT eom.evt_opt_id,em.event_display FROM st_sl_event_option_mapping eom INNER JOIN st_sl_event_master em ON eom.event_id = em.event_id WHERE game_type_id ="+gameTypeId+" AND eom.event_id IN ("+Arrays.toString(eventSelected).replace("[", "").replace("]", "")+") group by eom.event_id";
			stmt = connection.createStatement();
			rs = stmt.executeQuery(checkEventExistQuery);
			list=new ArrayList<String>();
			while(rs.next()){
				list.add(rs.getString("event_display"));
				}
			if(list.size()>0){
				throw new SLEException(SLEErrors.EVENT_ALREADY_EXIST_ERROR_CODE,"Events ("+list.toString().replace("[", "").replace("]", "")+") "+SLEErrors.EVENT_ALREADY_EXIST_ERROR_MESSAGE);
			}
			optionMap = CommonMethodsDaoImpl.getInstance().fetchGameTypeOptionMap(gameId, gameTypeId, connection);
			pstmt = connection.prepareStatement("INSERT INTO st_sl_event_option_mapping (game_id, game_type_id, event_id, option_name, option_code, is_displayable) VALUES (?,?,?,?,?,?);");
			for(int event : eventSelected) {
				Iterator<Entry<String, String>> it = optionMap.entrySet().iterator();
			    while (it.hasNext()) {
			    	 Map.Entry<String,String> me = it.next();
					 String optionCode = me.getKey();
					 String optionName = me.getValue();
					 pstmt.setInt(1, gameId);
					 pstmt.setInt(2, gameTypeId);
					 pstmt.setInt(3, event);
					 pstmt.setString(4, optionName);
					 pstmt.setString(5, optionCode);
				     pstmt.setString(6, ("C".equals(optionCode))?"NO":"YES");
					 pstmt.addBatch();
			    }
			}
			pstmt.executeBatch();
			logger.info("InsertEventOptions - "+pstmt);
		} catch (SQLException se) {
			se.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		}catch (SLEException se) {
			 throw se;
		}catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}finally{
			DBConnect.closePstmt(pstmt);
		}
	}
	
	public List<EventMasterBean> fetchSelectedEventDetails(String eventIds, Connection connection) throws SLEException {
		SimpleDateFormat simpleDateFormat = null;
		List<EventMasterBean> eventMasterList = null;
		EventMasterBean eventMasterBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		String query = null;
		try {
			simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

			query = "select event_display, start_time, end_time, game_id from st_sl_event_master where event_id in ("+eventIds+");";
			eventMasterList = new ArrayList<EventMasterBean>();
			stmt = connection.createStatement();
			logger.info("DrawEventMapping Event Query - "+query);
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				eventMasterBean = new EventMasterBean();
				eventMasterBean.setGameId(rs.getInt("game_id"));
				eventMasterBean.setEventDisplay(rs.getString("event_display"));
				eventMasterBean.setStartTime(simpleDateFormat.format(rs.getTimestamp("start_time")));
				eventMasterBean.setEndTime(simpleDateFormat.format(rs.getTimestamp("end_time")));
				eventMasterList.add(eventMasterBean);
			}
		} catch (SQLException se) {
			se.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}finally{
			DBConnect.closeConnection(stmt, rs);
		}
		return eventMasterList;
	}
	
	/**
	 * <p>This method creates the drawMasterList which contains the data fetched from database from table st_sl_draw_master_?
	 * and st_sl_draw_merchant_? by passing the parameters - gameId, gameTypeId, merchantId and connection.</p>
	 * <p>
	 * <b>NOTE:</b> ? is replaced by gameId.</p>
	 * @param gameId
	 * @param gameTypeId
	 * @param merchantId
	 * @param connection
	 * @exception SQLExecption
	 * @return drawMasterList
	 * @since 02-Oct-2015
	 * @author Rishi
	 */
	
	public List<DrawMasterBean> getGameTypeDrawMasterListInfo(int gameId, int gameTypeId, int merchantId, Connection connection) throws SLEException {
		List<DrawMasterBean> drawMasterList = null;
		DrawMasterBean drawMasterBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		String query = null;
		try {
			query = "SELECT mas.draw_id, map.draw_name, mas.draw_freeze_time, mas.draw_datetime FROM st_sl_draw_master_"+gameId+" mas INNER JOIN st_sl_draw_merchant_mapping_"+gameId+" map ON mas.draw_id=map.draw_id AND game_type_id="+gameTypeId+" AND draw_status IN ('ACTIVE','INACTIVE') AND merchant_id="+merchantId+" AND draw_freeze_time > '"+Util.getCurrentTimeString()+"'";
			drawMasterList = new ArrayList<DrawMasterBean>();
			stmt = connection.createStatement();

			rs = stmt.executeQuery(query);
			while(rs.next()) {
				drawMasterBean = new DrawMasterBean();
				drawMasterBean.setDrawId(rs.getInt("draw_id"));
				drawMasterBean.setDrawName(rs.getString("draw_name"));
				drawMasterBean.setDrawFreezeTime(UtilityFunctions.timeFormat(rs.getString("draw_freeze_time")).replace(".0",""));	
				drawMasterBean.setDrawDateTime(UtilityFunctions.timeFormat(rs.getString("draw_datetime")).replace(".0",""));
				drawMasterList.add(drawMasterBean);
			}
		} catch (SQLException se) {
			se.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return drawMasterList;
	}
	
	/**
	 * <p>This method fetches information for draws in whic common events are mapped.</p>
	 * @param gameId
	 * @param merchantId
	 * @param conn
	 * @return commonEventsDrawMasterList
	 * @throws SLEException
	 */
	
	public List<DrawMasterBean> getDrawMasterListInfoForEvent(int gameId, int eventId, Connection conn, int merchantId) throws SLEException{
		List<DrawMasterBean> commonEventsDrawMasterList = null;
		DrawMasterBean drawMasterBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		String query = null;
		try {
			query = "Select dm.draw_id,dm.game_type_id,dm.draw_no,dm.draw_datetime,dm.draw_freeze_time,dm.sale_start_time,dm.draw_status From st_sl_draw_master_"+gameId+" dm INNER JOIN st_sl_draw_event_mapping_"+gameId+" dem ON dm.draw_id=dem.draw_id Where dem.event_id = "+eventId+" AND dm.draw_status IN ('ACTIVE','INACTIVE') AND draw_freeze_time > '"+Util.getCurrentTimeString()+"'";
			commonEventsDrawMasterList = new ArrayList<DrawMasterBean>();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				drawMasterBean = new DrawMasterBean();
				drawMasterBean.setDrawId(rs.getInt("dm.draw_id"));
				drawMasterBean.setGameTypeName(getGameTypeName(conn,rs.getInt("dm.game_type_id"),merchantId));
				drawMasterBean.setDrawFreezeTime(UtilityFunctions.timeFormat(rs.getString("dm.draw_freeze_time")).replace(".0",""));
				drawMasterBean.setSaleStartTime(UtilityFunctions.timeFormat(rs.getString("dm.sale_start_time")).replace(".0",""));
				drawMasterBean.setDrawDateTime(UtilityFunctions.timeFormat(rs.getString("dm.draw_datetime")).replace(".0",""));
				drawMasterBean.setDrawStatus(rs.getString("dm.draw_status"));
				commonEventsDrawMasterList.add(drawMasterBean);
			}
		} catch (SQLException se) {
			se.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally{
			DBConnect.closeConnection(stmt, rs);
		}
		return commonEventsDrawMasterList;
	}
	
	public String getGameTypeName(Connection conn,int gameTypeId,int merchantId){
		String gameTypeName = null;
		Statement stmt = null;
		ResultSet rs = null;
		try{
			stmt = conn.createStatement();
			rs = stmt.executeQuery("Select type_disp_name From st_sl_game_type_merchant_mapping Where game_type_id="+gameTypeId+" AND merchant_id="+merchantId);
			if(rs.next()){
				gameTypeName = rs.getString("type_disp_name");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			DBConnect.closeConnection(stmt, rs);
		}
		return gameTypeName;
	}
	
	/**
	 * <p>This method populates mappedEventMasterList arraylist by fetching data from st_sl_draw_event_mapping_? and st_sl_event_master
	 * based on the gameId and drawId.</p>
	 * <p>
	 * <b>NOTE:</b> ? is replaced by gameId.</p>
	 * @param connection
	 * @param drawId
	 * @param mappedEventMasterList
	 * @param gameId
	 * @throws SLEException
	 * @since 02-Oct-2015
	 * @author Rishi
	 */

	
	public void getMappedEventsList(Connection connection,int drawId,List<MappedEventMasterBean> mappedEventMasterList,int gameId,Set<Integer> mappedLeagueSet) throws SLEException{
		MappedEventMasterBean mappedEventMasterBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		String query = null;
		try {
			query = "Select b.event_id,b.event_description,b.league_id,b.home_team_id,b.away_team_id,b.venue_id,b.start_time,b.end_time,b.home_team_odds,b.away_team_odds,b.draw_odds"+
				" From st_sl_draw_event_mapping_"+gameId+" a INNER JOIN st_sl_event_master b on a.event_id=b.event_id where a.draw_id="+drawId;
			stmt = connection.createStatement();
			logger.info("EventManagement Event Query - "+query);
			rs = stmt.executeQuery(query);
			while(rs.next()){
				mappedEventMasterBean = new MappedEventMasterBean();
				mappedEventMasterBean.setEventId(rs.getInt("b.event_id"));
				mappedEventMasterBean.setEventDescription(rs.getString("b.event_description"));
				mappedEventMasterBean.setLeagueId(rs.getInt("b.league_id"));
				mappedLeagueSet.add(mappedEventMasterBean.getLeagueId());
				mappedEventMasterBean.setHomeTeamId(rs.getInt("b.home_team_id"));
				mappedEventMasterBean.setAwayTeamId(rs.getInt("b.away_team_id"));
				mappedEventMasterBean.setVenueId(rs.getInt("b.venue_id"));
				mappedEventMasterBean.setHomeTeamOdds(rs.getString("b.home_team_odds"));
				mappedEventMasterBean.setAwayTeamOdds(rs.getString("b.away_team_odds"));
				mappedEventMasterBean.setDrawOdds(rs.getString("b.draw_odds"));
				mappedEventMasterBean.setLeagueName(getLeagueORTeamORVenueName(connection,rs.getInt("b.league_id"),1));
				mappedEventMasterBean.setHomeTeamName(getLeagueORTeamORVenueName(connection,rs.getInt("b.home_team_id"),2));
				mappedEventMasterBean.setAwayTeamName(getLeagueORTeamORVenueName(connection,rs.getInt("b.away_team_id"),2));
				mappedEventMasterBean.setVenueName(getLeagueORTeamORVenueName(connection,rs.getInt("b.venue_id"),3));
				mappedEventMasterBean.setStartTime((rs.getString("b.start_time").replace(".0", "").replace("-","/")).substring(0, 16));
				mappedEventMasterBean.setEndTime((rs.getString("b.end_time").replace(".0", "").replace("-", "/")).substring(0, 16));
				mappedEventMasterList.add(mappedEventMasterBean);
			}
		}
		catch (SQLException se) {
			se.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}finally{
			DBConnect.closeConnection(stmt, rs);
		}
	}
	
	/**
	 * <p>This method fetches the league name,team Name and venue name based on the id and type parameter.</p>
	 * <p>If it receives type = 1, it fetches league name.</p>
	 * <p>If it receives type = 2, it fetches team name.</p>
	 * <p>If it receives type = 3, it fetches venue name.</p>
	 * @param conn
	 * @param id
	 * @param type
	 * @return Name
	 * @since 02-Oct-2015
	 * @author Rishi
	 */
	
	public String getLeagueORTeamORVenueName(Connection conn, int id,int type){
		Statement stmt = null;
		ResultSet rs=null;
		String query = null;
		String name = null;
		if(type == 1){
			query = "Select league_display_name From st_sl_league_master Where league_id="+id;
		}
		else if(type == 2){
			query = "Select team_name From st_sl_game_team_master Where team_id="+id;
		}
		else{
			query = "Select venue_display_name From st_sl_venue_master Where venue_id="+id;
		}
		try{
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			if(rs.next()){
				name = rs.getString(1);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			DBConnect.closeConnection(stmt, rs);
		}
		return name;
	}
	
	/**
	 * <p>This method fetches all the active teams present in the sle_zim database in st_sl_game_team_master and stores
	 * it in arraylist - teamMasterList.</p>
	 * @param connection
	 * @param gameId
	 * @param teamMasterList
	 * @throws SLEException
	 * @since 02-Oct-2015
	 * @author Rishi
	 */
	
	public void getAllTeam(Connection connection,int gameId,List<TeamMasterBean> teamMasterList) throws SLEException{
		TeamMasterBean teamMasterBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		String query = null;
		try {
			query = "SELECT team_id, team_name FROM st_sl_game_team_master where status='ACTIVE' order by team_name";
			stmt = connection.createStatement();
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				teamMasterBean = new TeamMasterBean();
				teamMasterBean.setTeamId(rs.getInt("team_id"));
				teamMasterBean.setTeamName(rs.getString("team_name"));
				teamMasterList.add(teamMasterBean);
			}
		} catch (SQLException se) {
			se.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally{
			DBConnect.closeConnection(stmt, rs);
		}
	}
	
	
	public void getLeagueTeamMasterData(int gameId, Set<Integer> maapedLeagueSet, List<TeamMasterBean> teamMasterList,Connection connection) throws SLEException {
		
		TeamMasterBean teamMasterBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		String query = null;
		String leagueQuery = null;
		try {
				if(maapedLeagueSet.size() > 0){
					leagueQuery = " and league_id IN ("+maapedLeagueSet.toString().replace("[", "").replace("]", "")+")";
				}else{
					leagueQuery = "";
				}
			query = "SELECT a.team_id, game_id, team_name, team_code, a.status FROM st_sl_game_team_master a, st_sl_league_team_mapping b WHERE game_id="+gameId+" AND a.status='ACTIVE' and a.team_id = b.team_id "+leagueQuery+" order by team_name";
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
		}finally{
			DBConnect.closeConnection(stmt,rs);
		}
	}
	
	/**
	 * @author Vatsal
	 * @since 10-April-2017
	 * This Method is for SIMNET PARTY. it returns the draws with status 'claim allow' for the gameId and gameTypeId and also must not present in simnet history table.
	 */
	
	public List<SimnetResultDrawsBean> getSimnetResultDraws(int gameId,int gameTypeId,int merchantId,Connection conn) throws SLEException{
		List<SimnetResultDrawsBean> simnetResultDrawsList=null;
		String query=null;
		Statement stmt=null;
		ResultSet rs=null;
		SimnetResultDrawsBean claimHoldDrawsBean=null;
		try {
			query = "SELECT mas.draw_id, draw_name, draw_datetime FROM st_sl_draw_master_"
					+ gameId + " mas INNER JOIN st_sl_draw_merchant_mapping_" + gameId
					+ " map ON mas.draw_id=map.draw_id WHERE mas.game_type_id=" + gameTypeId + " AND merchant_id="
					+ merchantId + " AND draw_status='CLAIM ALLOW' AND map.verification_date <= DATE_ADD( mas.draw_datetime , INTERVAL 7 DAY) "+"and mas.draw_id not in (select draw_id from st_sl_simnet_history_prize_distribution_"+gameId+")"+";";
			stmt=conn.createStatement();
			rs=stmt.executeQuery(query);
			simnetResultDrawsList=new ArrayList<SimnetResultDrawsBean>();
			while(rs.next())
			{
				claimHoldDrawsBean=new SimnetResultDrawsBean();
				claimHoldDrawsBean.setDrawId(rs.getInt("draw_id"));
				claimHoldDrawsBean.setDrawName(rs.getString("draw_name"));
				claimHoldDrawsBean.setDrawDateTime(Util.getDateTimeFormat(rs.getTimestamp("draw_datetime")));
				simnetResultDrawsList.add(claimHoldDrawsBean);
			}
		}catch (SQLException se) {
			se.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return simnetResultDrawsList;
	}
}