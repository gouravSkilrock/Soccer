package com.skilrock.sle.reportsMgmt.daoImpl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.skilrock.sle.common.DBConnect;
import com.skilrock.sle.common.SLELogger;
import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.gamePlayMgmt.javaBeans.SportsLotteryGameEventDataBean;
import com.skilrock.sle.reportsMgmt.javaBeans.WinningResultReportBean;

/**
 * @author Shobhit
 * @category Game data
 */
public class WinningResultReportDaoImpl {
	private static final SLELogger logger = SLELogger.getLogger(WinningResultReportDaoImpl.class.getName());

	public List<WinningResultReportBean> getWinningDataReport(int gameId, int gameTypeId, int merchantId, Connection connection) throws SLEException {
		Statement stmt = null;
		ResultSet rs = null;
		String query = null;
		List<WinningResultReportBean> winningResultReportList = null;
		WinningResultReportBean winningResultReportBean = null;
		List<SportsLotteryGameEventDataBean> eventDataBeanList = null;
		SportsLotteryGameEventDataBean eventDataBean = null;
		Map<String, String> eventOptionMap = null;
		SimpleDateFormat dateFormat = null;
		SimpleDateFormat timeFormat = null;
		try {
			dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			timeFormat = new SimpleDateFormat("HH:mm:ss");
			winningResultReportList = new ArrayList<WinningResultReportBean>();
			stmt = connection.createStatement();
			query = "SELECT (SELECT game_disp_name FROM st_sl_game_merchant_mapping WHERE game_id="+gameId+" AND merchant_id="+merchantId+") gameName, " +
					"(SELECT type_disp_name FROM st_sl_game_type_merchant_mapping WHERE game_type_id="+gameTypeId+" AND merchant_id="+merchantId+") gameTypeName, " +
					"mas.draw_id, draw_name, draw_datetime FROM st_sl_draw_master_"+gameId+" mas INNER JOIN st_sl_draw_merchant_mapping_"+gameId+" map ON mas.draw_id=map.draw_id " +
					"WHERE game_type_id="+gameTypeId+" AND draw_freeze_time<'"+Util.getCurrentTimeStamp()+"' AND draw_status='CLAIM ALLOW'   ORDER BY draw_freeze_time DESC LIMIT 1;";
			logger.info("getWinningDataReportQuery - "+query);
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				winningResultReportBean = new WinningResultReportBean();
				winningResultReportBean.setGameName(rs.getString("gameName"));
				winningResultReportBean.setGameTypeName(rs.getString("gameTypeName"));
				winningResultReportBean.setDrawId(rs.getInt("draw_id"));
				winningResultReportBean.setDrawName(rs.getString("draw_name"));
				winningResultReportBean.setDrawDate(dateFormat.format(rs.getTimestamp("draw_datetime")));
				winningResultReportBean.setDrawTime(timeFormat.format(rs.getTimestamp("draw_datetime")));
				winningResultReportList.add(winningResultReportBean);
			}

			for(WinningResultReportBean bean : winningResultReportList) {
				eventOptionMap = new LinkedHashMap<String, String>();
				eventDataBeanList = new ArrayList<SportsLotteryGameEventDataBean>();
				query = "SELECT draw_id, event_display, (SELECT team_name FROM st_sl_game_team_master where team_id=master.home_team_id) home_team_name, (SELECT team_name FROM st_sl_game_team_master where team_id=master.away_team_id) away_team_name, option_code " +
						"FROM (SELECT draw_id, event_display, home_team_id, away_team_id, option_code FROM st_sl_draw_event_mapping_"+gameId+" dem " +
						"INNER JOIN st_sl_event_option_mapping eom ON dem.evt_opt_id=eom.evt_opt_id " +
						"INNER JOIN st_sl_event_master em ON em.event_id=dem.event_id " +
						"WHERE draw_id="+bean.getDrawId()+" and eom.game_id="+gameId+" and eom.game_type_id="+gameTypeId+") master;";
				logger.info("getWinningEventQuery - "+query);
				rs = stmt.executeQuery(query);
				while(rs.next()) {
					eventDataBean = new SportsLotteryGameEventDataBean();
					eventDataBean.setHomeTeamName(rs.getString("home_team_name"));
					eventDataBean.setAwayTeamName(rs.getString("away_team_name"));
					eventDataBean.setWinningEvent(rs.getString("option_code"));
					
					eventDataBeanList.add(eventDataBean);
					
					eventOptionMap.put(rs.getString("event_display"), rs.getString("option_code"));
				}
				bean.setEventOptionMap(eventOptionMap);
				bean.setDrawEventList(eventDataBeanList);
			}
		} catch (SQLException se) {
			se.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}

		return winningResultReportList;
	}

	
	public List<WinningResultReportBean> getDrawDataReport(int gameId, int gameTypeId, int merchantId, Connection connection) throws SLEException {
		Statement stmt = null;
		ResultSet rs = null;
		String query = null;
		List<WinningResultReportBean> winningResultReportList = null;
		WinningResultReportBean winningResultReportBean = null;
		SimpleDateFormat dateFormat = null;
		SimpleDateFormat timeFormat = null;
		try {
			dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			timeFormat = new SimpleDateFormat("HH:mm:ss");
			winningResultReportList = new ArrayList<WinningResultReportBean>();
			stmt = connection.createStatement();
			query = "SELECT (SELECT game_disp_name FROM st_sl_game_merchant_mapping WHERE game_id="+gameId+" AND merchant_id="+merchantId+") gameName, " +
					"(SELECT type_disp_name FROM st_sl_game_type_merchant_mapping WHERE game_type_id="+gameTypeId+" AND merchant_id="+merchantId+") gameTypeName, " +
					"mas.draw_id, draw_name, draw_datetime FROM st_sl_draw_master_"+gameId+" mas INNER JOIN st_sl_draw_merchant_mapping_"+gameId+" map ON mas.draw_id=map.draw_id " +
					"WHERE game_type_id="+gameTypeId+" AND draw_freeze_time<'"+Util.getCurrentTimeStamp()+"' ORDER BY draw_freeze_time DESC LIMIT 10;";
			logger.info("getWinningDataReportQuery - "+query);
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				winningResultReportBean = new WinningResultReportBean();
				winningResultReportBean.setGameName(rs.getString("gameName"));
				winningResultReportBean.setGameTypeName(rs.getString("gameTypeName"));
				winningResultReportBean.setDrawId(rs.getInt("draw_id"));
				winningResultReportBean.setDrawName(rs.getString("draw_name"));
				winningResultReportBean.setDrawDate(dateFormat.format(rs.getTimestamp("draw_datetime")));
				winningResultReportBean.setDrawTime(timeFormat.format(rs.getTimestamp("draw_datetime")));
				winningResultReportList.add(winningResultReportBean);
			}
		}catch (SQLException se) {
			se.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		
		return winningResultReportList;
		
	}
	
	public WinningResultReportBean winningResultForDraw(int gameId, int gameTypeId,int drawId, int merchantId, Connection connection) throws SLEException {
		Statement stmt = null;
		ResultSet rs = null;
		String query = null;
		WinningResultReportBean winningResultReportBean = null;
		SimpleDateFormat dateFormat = null;
		SimpleDateFormat timeFormat = null;
		try {
			dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			timeFormat = new SimpleDateFormat("HH:mm:ss");
			stmt = connection.createStatement();
			query = "SELECT (SELECT game_disp_name FROM st_sl_game_merchant_mapping WHERE game_id="+gameId+" AND merchant_id="+merchantId+") gameName, " +
					"(SELECT type_disp_name FROM st_sl_game_type_merchant_mapping WHERE game_type_id="+gameTypeId+" AND merchant_id="+merchantId+") gameTypeName, " +
					"mas.draw_id, draw_name, draw_datetime FROM st_sl_draw_master_"+gameId+" mas INNER JOIN st_sl_draw_merchant_mapping_"+gameId+" map ON mas.draw_id=map.draw_id " +
					"WHERE game_type_id="+gameTypeId+" AND mas.draw_id="+drawId+";";
			logger.info("getWinningDataReportQuery - "+query);
			rs = stmt.executeQuery(query);
			if(rs.next()) {
				winningResultReportBean = new WinningResultReportBean();
				winningResultReportBean.setGameName(rs.getString("gameName"));
				winningResultReportBean.setGameTypeName(rs.getString("gameTypeName"));
				winningResultReportBean.setDrawId(rs.getInt("draw_id"));
				winningResultReportBean.setDrawName(rs.getString("draw_name"));
				winningResultReportBean.setDrawDate(dateFormat.format(rs.getTimestamp("draw_datetime")));
				winningResultReportBean.setDrawTime(timeFormat.format(rs.getTimestamp("draw_datetime")));
			}else{
				
			}
			List<SportsLotteryGameEventDataBean> drawEventList = new ArrayList<SportsLotteryGameEventDataBean>();
			
			SportsLotteryGameEventDataBean eventDataBean=null;
			query = "SELECT draw_id, event_display, option_code FROM st_sl_draw_event_mapping_"+gameId+" dem " +
					"INNER JOIN st_sl_event_option_mapping eom ON dem.evt_opt_id=eom.evt_opt_id " +
					"INNER JOIN st_sl_event_master em ON em.event_id=dem.event_id " +
					"WHERE draw_id="+drawId+" and eom.game_id="+gameId+" and eom.game_type_id="+gameTypeId+";";
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				eventDataBean = new SportsLotteryGameEventDataBean();
				eventDataBean.setEventDescription(rs.getString("event_display"));
				eventDataBean.setWinningEvent(rs.getString("option_code"));
				drawEventList.add(eventDataBean);
			}
			winningResultReportBean.setDrawEventList(drawEventList);
			
			
		}catch (SQLException se) {
			se.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		
		return winningResultReportBean;
		
	}
	
	
	public static void main(String[] args) throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		Connection connection = DBConnect.getConnection();
		List<WinningResultReportBean> list = new WinningResultReportDaoImpl().getWinningDataReport(1, 2, 1, connection);
		System.out.println(list);
	}
}