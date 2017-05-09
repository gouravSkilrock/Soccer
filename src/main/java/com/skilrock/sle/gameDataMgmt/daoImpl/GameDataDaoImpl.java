package com.skilrock.sle.gameDataMgmt.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skilrock.sle.common.DBConnect;
import com.skilrock.sle.common.SLELogger;
import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.common.javaBeans.TicketSalePwtInfoBean;
import com.skilrock.sle.commonMethod.daoImpl.CommonMethodsDaoImpl;
import com.skilrock.sle.dataMgmt.javaBeans.TicketTxnStatusBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.DrawMasterBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.EventMasterBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.GameMasterBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.GameTypeMasterBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.SLEDrawWinningBean;
import com.skilrock.sle.merchant.common.javaBeans.MerchantInfoBean;

/**
 * @author Shobhit
 * @category Game data
 */
public class GameDataDaoImpl {
	private static final SLELogger logger = SLELogger.getLogger(GameDataDaoImpl.class.getName());
	
	private static volatile GameDataDaoImpl classInstance;
	private GameDataDaoImpl(){
	}
	public static GameDataDaoImpl getInstance() {
		if(classInstance == null){
			synchronized (GameDataDaoImpl.class) {
				if(classInstance == null){
					classInstance = new GameDataDaoImpl();					
				}
			}
		}
		return classInstance;
	}

	/**
	 * This method is use to
	 * get game details on the
	 * basis of merchant_id.
	 * 
	 * @param merchantId
	 * @param connection
	 * @return ArrayList of GameMasterBean
	 * @throws SLEException
	 */
	public List<GameMasterBean> getSportsLotteryGameData(MerchantInfoBean merchantInfoBean, boolean isDrawDataReq, Connection connection) throws SLEException {
		List<GameMasterBean> gameMasterList = null;
		GameMasterBean gameMasterBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		String query = null;
		try {
			query = "SELECT mas.game_id, merchant_id, game_dev_name, game_disp_name, max_ticket_amount, thersold_ticket_amount, min_board_count, max_board_count, display_order, map.game_status FROM st_sl_game_master mas INNER JOIN st_sl_game_merchant_mapping map ON mas.game_id=map.game_id WHERE mas.game_status='SALE_OPEN' and map.game_status='SALE_OPEN' and merchant_id="+merchantInfoBean.getMerchantId()+" ORDER BY map.display_order;";
			gameMasterList = new ArrayList<GameMasterBean>();
			stmt = connection.createStatement();
			logger.info("GameMasterDetails - "+query);
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				gameMasterBean = new GameMasterBean();
				int gameId = rs.getInt("game_id");
				gameMasterBean.setGameId(gameId);
				gameMasterBean.setMerchantId(rs.getInt("merchant_id"));
				gameMasterBean.setGameDevName(rs.getString("game_dev_name"));
				gameMasterBean.setGameDispName(rs.getString("game_disp_name"));
				gameMasterBean.setMaxTicketAmt(rs.getDouble("max_ticket_amount"));
				gameMasterBean.setThersholdTickerAmt(rs.getDouble("thersold_ticket_amount"));
				gameMasterBean.setMinBoardCount(rs.getInt("min_board_count"));
				gameMasterBean.setMaxBoardCount(rs.getInt("max_board_count"));
				gameMasterBean.setDisplayOrder(rs.getInt("display_order"));
				gameMasterBean.setGameStatus(rs.getString("game_status"));
				gameMasterBean.setGameTypeMasterList(getGameTypeMasterDetails(gameId, merchantInfoBean, isDrawDataReq, connection));
				gameMasterList.add(gameMasterBean);
			}
		} catch (SLEException e) {
			throw e;
		} catch (SQLException se) {
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}finally{
			DBConnect.closeConnection(stmt, rs);
		}

		return gameMasterList;
	}

	/**
	 * This method is use to
	 * get game type details on
	 * the basis of game_id and
	 * merchant_id.
	 * 
	 * @param gameId
	 * @param merchantId
	 * @param connection
	 * @return ArrayList of GameTypeMasterBean
	 * @throws SLEException
	 */
	public List<GameTypeMasterBean> getGameTypeMasterDetails(int gameId, MerchantInfoBean merchantInfoBean, boolean isDrawDataReq, Connection connection) throws SLEException {
		List<GameTypeMasterBean> gameTypeMasterList = null;
		GameTypeMasterBean gameTypeMasterBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		String query = null;
		try {
			query = "SELECT mas.game_type_id, game_id, merchant_id, event_selection_type, type_dev_name, type_disp_name, unit_price, max_bet_amount_multiple, sale_start_time, sale_end_time,prize_amt_percentage,jackpot_message_display, no_of_events, event_type, display_order, map.type_status, map.prize_distribution_xml FROM st_sl_game_type_master mas INNER JOIN st_sl_game_type_merchant_mapping map ON mas.game_type_id=map.game_type_id WHERE mas.type_status='SALE_OPEN' AND map.type_status='SALE_OPEN' AND mas.game_id="+gameId+" AND map.merchant_id="+merchantInfoBean.getMerchantId()+" ORDER BY display_order;";
			gameTypeMasterList = new ArrayList<GameTypeMasterBean>();
			stmt = connection.createStatement();
			logger.info("GameTypeMasterDetails - "+query);
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				gameTypeMasterBean = new GameTypeMasterBean();
				int gameTypeId = rs.getInt("game_type_id");
				int noOfEvents = rs.getInt("no_of_events");
				gameTypeMasterBean.setGameTypeId(gameTypeId);
				gameTypeMasterBean.setGameId(rs.getInt("game_id"));
				gameTypeMasterBean.setMerchantId(rs.getInt("merchant_id"));
				gameTypeMasterBean.setGameTypeDevName(rs.getString("type_dev_name"));
				gameTypeMasterBean.setGameTypeDispName(rs.getString("type_disp_name"));
				gameTypeMasterBean.setUnitPrice(rs.getDouble("unit_price"));
				gameTypeMasterBean.setMaxBetAmtMultiple(rs.getInt("max_bet_amount_multiple"));
				gameTypeMasterBean.setSaleStartTime(rs.getString("sale_start_time"));
				gameTypeMasterBean.setSaleEndTime(rs.getString("sale_end_time"));
				gameTypeMasterBean.setPrizeAmtPercentage(rs.getDouble("prize_amt_percentage"));
				gameTypeMasterBean.setJackPotMessageDisplay(rs.getString("jackpot_message_display"));
				gameTypeMasterBean.setNoOfEvents(noOfEvents);
				gameTypeMasterBean.setEventType(rs.getString("event_type"));
				gameTypeMasterBean.setDisplayOrder(rs.getInt("display_order"));
				gameTypeMasterBean.setGameTypeStatus(rs.getString("type_status"));
				gameTypeMasterBean.setEventSelectionType(rs.getString("event_selection_type"));
				gameTypeMasterBean.setUpcomingDrawStartTime("");
				gameTypeMasterBean.setAreEventsMappedForUpcomingDraw(false);
				if(isDrawDataReq) {
					gameTypeMasterBean.setDrawMasterList(getDrawMasterDetails(gameId, gameTypeId, noOfEvents, merchantInfoBean, connection));
					//if(gameTypeMasterBean.getDrawMasterList().size() < 1){
						 String upcomingDrawDetails = CommonMethodsDaoImpl.getInstance().getUpcomingDrawDetail(gameId,gameTypeId,connection);
						 if(upcomingDrawDetails.split("~").length > 1){
							 gameTypeMasterBean.setUpcomingDrawStartTime(upcomingDrawDetails.split("~")[1]);
							 int drawId = Integer.parseInt(upcomingDrawDetails.split("~")[0]);
							 gameTypeMasterBean.setAreEventsMappedForUpcomingDraw(CommonMethodsDaoImpl.getInstance().isEventAvailable(gameId, gameTypeId, drawId, connection));
						 }
							  
					//}
				}

				gameTypeMasterList.add(gameTypeMasterBean);
			}
		} catch (SLEException e) {
			e.printStackTrace();
			throw e;
		} catch (SQLException se) {
			se.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}finally{
			DBConnect.closeConnection(stmt, rs);
		}

		return gameTypeMasterList;
	}

	/**
	 * This method is use to
	 * get draw details on the
	 * basis of game_type_id
	 * and merchant_id.
	 * 
	 * @param gameTypeId
	 * @param merchantId
	 * @param connection
	 * @return ArrayList of DrawMasterBean
	 * @throws SLEException
	 */
	public List<DrawMasterBean> getDrawMasterDetails(int gameId, int gameTypeId, int noOfEvents, MerchantInfoBean merchantInfoBean, Connection connection) throws SLEException {
		List<DrawMasterBean> drawMasterList = null;
		DrawMasterBean drawMasterBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		String query = null;
		try {
			query = "SELECT mas.draw_id, game_type_id, merchant_id, draw_no, draw_name, draw_display_type, draw_datetime, draw_freeze_time, sale_start_time, claim_start_date, claim_end_date, verification_date, draw_status, purchase_table_name FROM st_sl_draw_master_"+gameId+" mas INNER JOIN st_sl_draw_merchant_mapping_"+gameId+" map ON mas.draw_id=map.draw_id WHERE mas.game_type_id="+gameTypeId+" AND merchant_id="+merchantInfoBean.getMerchantId()+" AND draw_freeze_time>'"+Util.getCurrentTimeStamp()+"' AND sale_start_time<'"+Util.getCurrentTimeStamp()+"' AND draw_status='ACTIVE';";
			drawMasterList = new ArrayList<DrawMasterBean>();
			stmt = connection.createStatement();
			logger.info("DrawMasterDetails - "+query);
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				int drawId = rs.getInt("draw_id");
				List<EventMasterBean> eventMasterList = getEventMasterDetails(gameId,gameTypeId,drawId, connection);
				if(eventMasterList.size() == noOfEvents) {
					drawMasterBean = new DrawMasterBean();
					drawMasterBean.setDrawId(drawId);
					drawMasterBean.setGameTypeId(rs.getInt("game_type_id"));
					drawMasterBean.setMerchantId(rs.getInt("merchant_id"));
					drawMasterBean.setDrawNo(rs.getInt("draw_no"));
					drawMasterBean.setDrawName(rs.getString("draw_name"));

					if("DRAW_NAME".equals(rs.getString("draw_display_type"))){
						drawMasterBean.setDrawDisplayType(rs.getString("draw_name"));
					}else if("DRAW_DATETIME".equals(rs.getString("draw_display_type"))){
						SimpleDateFormat ft = new SimpleDateFormat ("EEE,MMM-d"); 
						drawMasterBean.setDrawDisplayType(ft.format(rs.getTimestamp("draw_datetime")));
					}else if("NAME_DATETIME".equals(rs.getString("draw_display_type"))){
						SimpleDateFormat ft = new SimpleDateFormat ("EEE,MMM-d");
						drawMasterBean.setDrawDisplayType(rs.getString("draw_name")+"<"+ft.format(rs.getTimestamp("draw_datetime"))+">");
					}

					drawMasterBean.setDrawDateTime(Util.getDateTimeFormat(rs.getTimestamp("draw_datetime")));
					drawMasterBean.setDrawFreezeTime(Util.getDateTimeFormat(rs.getTimestamp("draw_freeze_time")));
					drawMasterBean.setSaleStartTime(rs.getString("sale_start_time"));
					drawMasterBean.setClaimStartDate(rs.getString("claim_start_date"));
					drawMasterBean.setClaimEndDate(rs.getString("claim_end_date"));
					drawMasterBean.setVerificationDate(rs.getString("verification_date"));
					drawMasterBean.setDrawStatus(rs.getString("draw_status"));
					drawMasterBean.setPurchaseTableName(rs.getInt("purchase_table_name"));
					drawMasterBean.setEventMasterList(eventMasterList);
					drawMasterList.add(drawMasterBean);
				}
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

		return drawMasterList;
	}

	/**
	 * This method is use to
	 * get event data on the
	 * basis of draw_id.
	 * 
	 * @param drawId
	 * @param connection
	 * @return ArrayList of EventMasterBean
	 * @throws SLEException
	 */
	public List<EventMasterBean> getEventMasterDetails(int gameId, int gameTypeId, int drawId, Connection connection) throws SLEException {
		List<EventMasterBean> eventMasterList = null;
		EventMasterBean eventMasterBean = null;
		SimpleDateFormat simpleDateFormat = null;
		Statement stmt = null;
		ResultSet rs = null;
		String query = null;
		try {
			simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
//			query = "SELECT map.event_id, event_display, event_description, start_time, end_time, entry_time FROM st_sl_event_master mas INNER JOIN st_sl_draw_event_mapping_"+gameId+" map ON mas.event_id=map.event_id AND map.draw_id="+drawId+" ORDER BY map.event_order;";
			query = "SELECT map.event_id, vm.venue_display_name, event_display, event_description, lm.league_display_name, (SELECT team_name FROM st_sl_game_team_master WHERE team_id = home_team_id) home_team_name,(SELECT team_code FROM st_sl_game_team_master WHERE team_id = home_team_id) home_team_code, (SELECT team_code FROM st_sl_game_team_master WHERE team_id = away_team_id) away_team_code, (SELECT team_name FROM st_sl_game_team_master WHERE team_id = away_team_id) away_team_name, home_team_odds,away_team_odds,draw_odds, start_time, end_time, entry_time, lm.league_display_name FROM st_sl_event_master mas INNER JOIN st_sl_venue_master vm ON vm.venue_id = mas.venue_id INNER JOIN st_sl_league_master lm ON lm.league_id = mas.league_id INNER JOIN st_sl_draw_event_mapping_"+gameId+" map ON mas.event_id=map.event_id AND map.draw_id="+drawId+" ORDER BY map.event_order";
			eventMasterList = new ArrayList<EventMasterBean>();
			stmt = connection.createStatement();
			logger.info("EventMasterDetails - "+query);
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				eventMasterBean = new EventMasterBean();
				int eventId = rs.getInt("event_id");
				eventMasterBean.setEventId(eventId);
				eventMasterBean.setEventDisplay(rs.getString("event_display"));
				eventMasterBean.setEventDescription(rs.getString("event_description"));
				eventMasterBean.setStartTime(simpleDateFormat.format(rs.getTimestamp("start_time")));
				eventMasterBean.setEndTime(simpleDateFormat.format(rs.getTimestamp("end_time")));
				eventMasterBean.setEntryTime(simpleDateFormat.format(rs.getTimestamp("entry_time")));
				eventMasterBean.setVenueName(rs.getString("venue_display_name"));
				eventMasterBean.setHomeTeamName(rs.getString("home_team_name"));
				eventMasterBean.setHomeTeamCode(rs.getString("home_team_code"));
				eventMasterBean.setAwayTeamName(rs.getString("away_team_name"));
				eventMasterBean.setAwayTeamCode(rs.getString("away_team_code"));
				eventMasterBean.setHomeTeamOdds(rs.getString("home_team_odds"));
				eventMasterBean.setAwayTeamOdds(rs.getString("away_team_odds"));
				eventMasterBean.setDrawOdds(rs.getString("draw_odds"));
				if(eventMasterBean.getHomeTeamOdds()!=null && !"".equals(eventMasterBean.getHomeTeamOdds().trim()) && eventMasterBean.getAwayTeamOdds()!=null && !"".equals(eventMasterBean.getAwayTeamOdds().trim())){
					if("TRUE".equalsIgnoreCase(Util.getPropertyValue("IS_TIPS_ODD_AGAINST").trim())){
						eventMasterBean.setFavTeam(Double.parseDouble(eventMasterBean.getHomeTeamOdds())<Double.parseDouble(eventMasterBean.getAwayTeamOdds())?"HOME":Double.parseDouble(eventMasterBean.getHomeTeamOdds())!=Double.parseDouble(eventMasterBean.getAwayTeamOdds())?"AWAY":"");
					}else{
						eventMasterBean.setFavTeam(Double.parseDouble(eventMasterBean.getHomeTeamOdds())>Double.parseDouble(eventMasterBean.getAwayTeamOdds())?"HOME":Double.parseDouble(eventMasterBean.getHomeTeamOdds())!=Double.parseDouble(eventMasterBean.getAwayTeamOdds())?"AWAY":"");
					}
				}
				eventMasterBean.setLeagueName(rs.getString("league_display_name"));
				eventMasterBean.setEventOptionsList(getEventOptionsList(eventId, gameId,gameTypeId, connection));
				eventMasterList.add(eventMasterBean);
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

		return eventMasterList;
	}

	public List<String> getEventOptionsList(int eventId, int gameId, int gameTypeId, Connection connection) throws SLEException {
		List<String> eventOptionsList = null;
		Statement stmt = null;
		ResultSet rs = null;
		String query = null;
		try {
			query = "SELECT option_dev_name FROM st_sl_event_option_mapping eom inner join st_sl_option_master om on eom.option_name=om.option_disp_name and eom.game_id="+gameId+" and eom.game_type_id="+gameTypeId+" inner join st_sl_game_type_option_mapping som on som.option_id=om.option_id WHERE event_id="+eventId+" AND is_displayable='YES' and som.game_id="+gameId+" and som.game_type_id="+gameTypeId+" order by display_order";
			eventOptionsList = new ArrayList<String>();
			stmt = connection.createStatement();
			logger.info("EventOptionsList - "+query);
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				eventOptionsList.add(rs.getString("option_dev_name"));
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

		return eventOptionsList;
	}
	

	
	/**
	 * This method is use to
	 * get game details on the
	 * basis of merchant_id for 
	 * mobile API date Wise MatchList Data.
	 * @param merchantInfoBean
	 * @param connection
	 * @param idDrawDataReq
	 * @param fromDate
	 * @param toDate
	 * @return ArrayList of GameMasterBean
	 * @throws SLEException
	 */
	public List<GameMasterBean> getSportsLotteryGameDataDateWise(MerchantInfoBean merchantInfoBean, boolean isDrawDataReq, Connection connection,String fromDate,String toDate) throws SLEException {
		List<GameMasterBean> gameMasterList = null;
		GameMasterBean gameMasterBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		String query = null;
		try {
			query = "SELECT mas.game_id, merchant_id, game_dev_name, game_disp_name, max_ticket_amount, thersold_ticket_amount, min_board_count, max_board_count, display_order, map.game_status FROM st_sl_game_master mas INNER JOIN st_sl_game_merchant_mapping map ON mas.game_id=map.game_id WHERE mas.game_status='SALE_OPEN' and map.game_status='SALE_OPEN' and merchant_id="+merchantInfoBean.getMerchantId()+" ORDER BY map.display_order;";
			gameMasterList = new ArrayList<GameMasterBean>();
			stmt = connection.createStatement();
			logger.info("GameMasterDetails - "+query);
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				gameMasterBean = new GameMasterBean();
				int gameId = rs.getInt("game_id");
				gameMasterBean.setGameId(gameId);
				gameMasterBean.setMerchantId(rs.getInt("merchant_id"));
				gameMasterBean.setGameDevName(rs.getString("game_dev_name"));
				gameMasterBean.setGameDispName(rs.getString("game_disp_name"));
				gameMasterBean.setMaxTicketAmt(rs.getDouble("max_ticket_amount"));
				gameMasterBean.setThersholdTickerAmt(rs.getDouble("thersold_ticket_amount"));
				gameMasterBean.setMinBoardCount(rs.getInt("min_board_count"));
				gameMasterBean.setMaxBoardCount(rs.getInt("max_board_count"));
				gameMasterBean.setDisplayOrder(rs.getInt("display_order"));
				gameMasterBean.setGameStatus(rs.getString("game_status"));
				gameMasterBean.setGameTypeMasterList(getGameTypeMasterDetailsDateWise(gameId, merchantInfoBean, isDrawDataReq, connection,fromDate,toDate));
				gameMasterList.add(gameMasterBean);
			}
		} catch (SLEException e) {
			throw e;
		} catch (SQLException se) {
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}finally{
			DBConnect.closeConnection(stmt, rs);
		}

		return gameMasterList;
	}
	
	
	/**
	 * This method is use to
	 * get game type details on
	 * the basis of game_id and
	 * merchant_id .
	 * 
	 * @param gameId
	 * @param merchantInfoBean
	 * @param isDrawDataReq
	 * @param connection
	 * @param fromDate
	 * @param toDate
	 * @return ArrayList of GameTypeMasterBean
	 * @throws SLEException
	 */
	public List<GameTypeMasterBean> getGameTypeMasterDetailsDateWise(int gameId, MerchantInfoBean merchantInfoBean, boolean isDrawDataReq, Connection connection,String fromDate,String toDate) throws SLEException {
		List<GameTypeMasterBean> gameTypeMasterList = null;
		GameTypeMasterBean gameTypeMasterBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		String query = null;
		try {
			query = "SELECT mas.game_type_id, game_id, merchant_id, type_dev_name, type_disp_name, unit_price, max_bet_amount_multiple, sale_start_time, sale_end_time,prize_amt_percentage,jackpot_message_display, no_of_events, event_type, display_order, map.type_status, map.prize_distribution_xml FROM st_sl_game_type_master mas INNER JOIN st_sl_game_type_merchant_mapping map ON mas.game_type_id=map.game_type_id WHERE mas.type_status='SALE_OPEN' AND map.type_status='SALE_OPEN' AND mas.game_id="+gameId+" AND map.merchant_id="+merchantInfoBean.getMerchantId()+" ORDER BY display_order;";
			gameTypeMasterList = new ArrayList<GameTypeMasterBean>();
			stmt = connection.createStatement();
			logger.info("GameTypeMasterDetails - "+query);
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				gameTypeMasterBean = new GameTypeMasterBean();
				int gameTypeId = rs.getInt("game_type_id");
				int noOfEvents = rs.getInt("no_of_events");
				gameTypeMasterBean.setGameTypeId(gameTypeId);
				gameTypeMasterBean.setGameId(rs.getInt("game_id"));
				gameTypeMasterBean.setMerchantId(rs.getInt("merchant_id"));
				gameTypeMasterBean.setGameTypeDevName(rs.getString("type_dev_name"));
				gameTypeMasterBean.setGameTypeDispName(rs.getString("type_disp_name"));
				gameTypeMasterBean.setUnitPrice(rs.getDouble("unit_price"));
				gameTypeMasterBean.setMaxBetAmtMultiple(rs.getInt("max_bet_amount_multiple"));
				gameTypeMasterBean.setSaleStartTime(rs.getString("sale_start_time"));
				gameTypeMasterBean.setSaleEndTime(rs.getString("sale_end_time"));
				gameTypeMasterBean.setPrizeAmtPercentage(rs.getDouble("prize_amt_percentage"));
				gameTypeMasterBean.setJackPotMessageDisplay(rs.getString("jackpot_message_display"));
				gameTypeMasterBean.setNoOfEvents(noOfEvents);
				gameTypeMasterBean.setEventType(rs.getString("event_type"));
				gameTypeMasterBean.setDisplayOrder(rs.getInt("display_order"));
				gameTypeMasterBean.setGameTypeStatus(rs.getString("type_status"));
				gameTypeMasterBean.setUpcomingDrawStartTime("");
				gameTypeMasterBean.setAreEventsMappedForUpcomingDraw(false);
				if(isDrawDataReq) {
					gameTypeMasterBean.setDrawMasterList(getDrawMasterDetailsDateWise(gameId, gameTypeId, noOfEvents, merchantInfoBean, connection,fromDate,toDate));
					if(gameTypeMasterBean.getDrawMasterList().size() < 1){
						 String upcomingDrawDetails = CommonMethodsDaoImpl.getInstance().getUpcomingDrawDetail(gameId,gameTypeId,connection);
						 if(upcomingDrawDetails.split("~").length > 1){
							 gameTypeMasterBean.setUpcomingDrawStartTime(upcomingDrawDetails.split("~")[1]);
							  int drawId = Integer.parseInt(upcomingDrawDetails.split("~")[0]);
							 gameTypeMasterBean.setAreEventsMappedForUpcomingDraw(CommonMethodsDaoImpl.getInstance().isEventAvailable(gameId, gameTypeId, drawId, connection));
						 }
					}
				}

				gameTypeMasterList.add(gameTypeMasterBean);
			}
		} catch (SLEException e) {
			e.printStackTrace();
			throw e;
		} catch (SQLException se) {
			se.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}finally{
			DBConnect.closeConnection(stmt, rs);
		}

		return gameTypeMasterList;
	}


	
	/**
	 * This method is use to
	 * get draw details on the
	 * basis of game_type_id
	 * and merchant_id btween date limits
	 * @param gameId
	 * @param gameTypeId
	 * @param merchantId
	 * @param merchaantInfoBean
	 * @param connection
	 * @param fromDate
	 * @param toDate
	 * @return ArrayList of DrawMasterBean
	 * @throws SLEException
	 */
	public List<DrawMasterBean> getDrawMasterDetailsDateWise(int gameId, int gameTypeId, int noOfEvents, MerchantInfoBean merchantInfoBean, Connection connection,String fromDate,String toDate) throws SLEException {
		List<DrawMasterBean> drawMasterList = null;
		DrawMasterBean drawMasterBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		String query = null;
		String dateWiseDrawquery="";
		try {
			if(fromDate != null && toDate != null && !fromDate.trim().equals("") && !toDate.trim().equals("")){
				dateWiseDrawquery= " AND draw_datetime >='"+fromDate+"' AND  draw_datetime <='"+toDate+"'";
			}
			query = "SELECT mas.draw_id, game_type_id, merchant_id, draw_no, draw_name, draw_display_type, draw_datetime, draw_freeze_time, sale_start_time, claim_start_date, claim_end_date, verification_date, draw_status, purchase_table_name FROM st_sl_draw_master_"+gameId+" mas INNER JOIN st_sl_draw_merchant_mapping_"+gameId+" map ON mas.draw_id=map.draw_id WHERE mas.game_type_id="+gameTypeId+" AND merchant_id="+merchantInfoBean.getMerchantId()+" AND draw_freeze_time>'"+Util.getCurrentTimeStamp()+"' "+dateWiseDrawquery+" AND draw_status IN('ACTIVE','INACTIVE');";
			drawMasterList = new ArrayList<DrawMasterBean>();
			stmt = connection.createStatement();
			logger.info("DrawMasterDetails - "+query);
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				int drawId = rs.getInt("draw_id");
				List<EventMasterBean> eventMasterList = getEventMasterDetails(gameId,gameTypeId,drawId, connection);
				if(eventMasterList.size() == noOfEvents) {
					drawMasterBean = new DrawMasterBean();
					drawMasterBean.setDrawId(drawId);
					drawMasterBean.setGameTypeId(rs.getInt("game_type_id"));
					drawMasterBean.setMerchantId(rs.getInt("merchant_id"));
					drawMasterBean.setDrawNo(rs.getInt("draw_no"));
					drawMasterBean.setDrawName(rs.getString("draw_name"));

					if("DRAW_NAME".equals(rs.getString("draw_display_type"))){
						drawMasterBean.setDrawDisplayType(rs.getString("draw_name"));
					}else if("DRAW_DATETIME".equals(rs.getString("draw_display_type"))){
						SimpleDateFormat ft = new SimpleDateFormat ("EEE,MMM-d"); 
						drawMasterBean.setDrawDisplayType(ft.format(rs.getTimestamp("draw_datetime")));
					}else if("NAME_DATETIME".equals(rs.getString("draw_display_type"))){
						SimpleDateFormat ft = new SimpleDateFormat ("EEE,MMM-d");
						drawMasterBean.setDrawDisplayType(rs.getString("draw_name")+"<"+ft.format(rs.getTimestamp("draw_datetime"))+">");
					}

					drawMasterBean.setDrawDateTime(Util.getDateTimeFormat(rs.getTimestamp("draw_datetime")));
					drawMasterBean.setDrawFreezeTime(Util.getDateTimeFormat(rs.getTimestamp("draw_freeze_time")));
					drawMasterBean.setSaleStartTime(rs.getString("sale_start_time"));
					drawMasterBean.setClaimStartDate(rs.getString("claim_start_date"));
					drawMasterBean.setClaimEndDate(rs.getString("claim_end_date"));
					drawMasterBean.setVerificationDate(rs.getString("verification_date"));
					drawMasterBean.setDrawStatus(rs.getString("draw_status"));
					drawMasterBean.setPurchaseTableName(rs.getInt("purchase_table_name"));
					drawMasterBean.setEventMasterList(eventMasterList);
					drawMasterList.add(drawMasterBean);
				}
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

		return drawMasterList;
	}

	
	/*public Map<Integer, List<EventMasterBean>> getSLEMatchDetailsDayWise(int merchantId, String fromDate, String toDate, Connection connection) throws SLEException {
		List<EventMasterBean> eventList = null;
		EventMasterBean eventBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		String query = null;
		Map<Integer, List<EventMasterBean>> matchData = null;
		try {
				matchData = new HashMap<Integer, List<EventMasterBean>>();
				
				query = "select game_id from st_sl_game_merchant_mapping where merchant_id = "+merchantId;
				stmt = connection.createStatement();
				rs = stmt.executeQuery(query);
				while(rs.next()){
					eventList = new ArrayList<EventMasterBean>();
					matchData.put(rs.getInt("game_id"), eventList);
				}
			
				query = "select a.game_id, game_disp_name gameName, event_display eventDisp, event_description eventDesc, league_display_name leagueName, d.team_name homeTeam, f.team_name awayTeam, venue_display_name venuName, start_time, end_time from st_sl_event_master a, st_sl_game_merchant_mapping b, st_sl_league_master c, st_sl_game_team_master d, st_sl_venue_master e,  st_sl_game_team_master f where a.game_id = b.game_id and a.game_id  in (select game_id from st_sl_game_merchant_mapping where merchant_id = "+merchantId+") and b.merchant_id = "+merchantId+" and a.start_time >= '"+fromDate+"' and a.start_time <= '"+toDate+"' and a.league_id = c.league_id and a.home_team_id = d.team_id and a.venue_id = e.venue_id and a.away_team_id = f.team_id";
				stmt = connection.createStatement();
				logger.info("Event List Query - "+query);
				rs = stmt.executeQuery(query);
				while(rs.next()) {
					eventBean = new EventMasterBean();
					eventBean.setGameId(rs.getInt("game_id"));
					eventBean.setEventDisplay(rs.getString("eventDisp"));
					eventBean.setEventDescription(rs.getString("eventDesc"));
					eventBean.setLeagueName(rs.getString("leagueName"));
					eventBean.setHomeTeamName(rs.getString("homeTeam"));
					eventBean.setAwayTeamName(rs.getString("awayTeam"));
					eventBean.setVenueName(rs.getString("venuName"));
					eventBean.setStartTime(rs.getString("start_time"));
					eventBean.setEndTime(rs.getString("end_time"));
					matchData.get(rs.getInt("game_id")).add(eventBean);
				}
		} catch (SQLException se) {
			se.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return matchData;
	}*/
	
	public Map<Integer, List<EventMasterBean>> getMatchListDrawWise(int gameId,int gameTypeId ,int merchantId, String drawType, Connection connection,StringBuilder drawInfo) throws SLEException {
		List<EventMasterBean> eventList = null;
		EventMasterBean eventBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		String query = null;
		Map<Integer, List<EventMasterBean>> matchData = null;
		String drawIds = null;
		try {
				matchData = new HashMap<Integer, List<EventMasterBean>>();
				eventList = new ArrayList<EventMasterBean>();
				matchData.put(gameId, eventList);
				
				if("ADVANCED".equalsIgnoreCase(drawType)){
					drawIds = CommonMethodsDaoImpl.getInstance().getAdvanceDrawIds(gameId,gameTypeId ,merchantId, 1, connection,drawInfo);
				}else{
					drawIds = CommonMethodsDaoImpl.getInstance().getCurrentDrawId(gameId,gameTypeId ,merchantId, connection,drawInfo,true);
				}
				//drawIds = "".equalsIgnoreCase(drawIds)?"0":drawIds ;
				logger.info("draw Ids : " + drawIds) ;
				//query = "select a.game_id, game_disp_name gameName, event_display eventDisp, event_description eventDesc, league_display_name leagueName, d.team_name homeTeam, f.team_name awayTeam, venue_display_name venuName, start_time, end_time from st_sl_event_master a, st_sl_game_merchant_mapping b, st_sl_league_master c, st_sl_game_team_master d, st_sl_venue_master e,  st_sl_game_team_master f, st_sl_draw_event_mapping_1 g where a.game_id = b.game_id and a.game_id  ="+gameId+" and b.merchant_id = "+merchantId+" and a.event_id in (select event_id from st_sl_draw_event_mapping_"+gameId+" where draw_id in ("+drawIds+")) and a.league_id = c.league_id and a.home_team_id = d.team_id and a.venue_id = e.venue_id and a.away_team_id = f.team_id and a.event_id = g.event_id order by g.event_order";
				if(drawIds!=null && !"".equalsIgnoreCase(drawIds))
				{
				query = "select game_id,  gameName,  eventDisp,  eventDesc,  leagueName,  homeTeam,  awayTeam,  venuName, start_time, end_time FROM" +
						" (select a.event_id, a.game_id, game_disp_name gameName, event_display eventDisp, event_description eventDesc, " +
						"league_display_name leagueName, d.team_name homeTeam, f.team_name awayTeam, venue_display_name venuName, start_time, " +
						"end_time from st_sl_event_master a, st_sl_game_merchant_mapping b, st_sl_league_master c, st_sl_game_team_master d, " +
						"st_sl_venue_master e,  st_sl_game_team_master f where a.game_id = b.game_id and a.game_id  ="+gameId+" and " +
								"b.merchant_id = "+merchantId+" and a.league_id = c.league_id and a.home_team_id = d.team_id and " +
										"a.venue_id = e.venue_id and a.away_team_id = f.team_id ) a INNER JOIN (" +
										"select event_id, event_order from st_sl_draw_event_mapping_"+gameId+" where draw_id in " +
												"("+drawIds+")) b on a.event_id = b.event_id order by b.event_order";
				stmt = connection.createStatement();
				logger.info("Event List Query - "+query);
				rs = stmt.executeQuery(query);
				while(rs.next()) {
					eventBean = new EventMasterBean();
					eventBean.setGameId(rs.getInt("game_id"));
					eventBean.setEventDisplay(rs.getString("eventDisp"));
					eventBean.setEventDescription(rs.getString("eventDesc"));
					eventBean.setLeagueName(rs.getString("leagueName"));
					eventBean.setHomeTeamName(rs.getString("homeTeam"));
					eventBean.setAwayTeamName(rs.getString("awayTeam"));
					eventBean.setVenueName(rs.getString("venuName"));
					eventBean.setStartTime(rs.getString("start_time"));
					eventBean.setEndTime(rs.getString("end_time"));
					matchData.get(rs.getInt("game_id")).add(eventBean);
				}}
		} catch (SQLException se) {
			se.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (SLEException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return matchData;
	}
	
	
	public List<DrawMasterBean> getMatchListDrawWiseForWeb(int gameId,int gameTypeId ,int merchantId,Connection connection) throws SLEException {
		List<EventMasterBean> eventList = null;
		EventMasterBean eventBean = null;
		Statement stmt = null;
		ResultSet drawRs = null;
		ResultSet rs = null;
		String query = null;
		String qry=null;
		int drawId=0;
		List<DrawMasterBean>drawmasterBeanList=null;
		DrawMasterBean drawMasterBean=null;
		try {
			drawmasterBeanList=new LinkedList<DrawMasterBean>();
			
			qry = "select a.draw_id ,a.draw_datetime,draw_name,b.draw_name from st_sl_draw_master_"+ gameId+ " a, st_sl_draw_merchant_mapping_"+ gameId+ " b where a.draw_id = b.draw_id and b.merchant_id = "+ merchantId+ " and a.game_type_id ="+ gameTypeId+" AND draw_status IN('ACTIVE','INACTIVE') and draw_freeze_time>'"+Util.getCurrentTimeString()+"'order by draw_datetime asc;";
			stmt = connection.createStatement();
			drawRs = stmt.executeQuery(qry.toString());
			while(drawRs.next()){
				drawMasterBean = new DrawMasterBean();
				drawId=drawRs.getInt("draw_id");
				drawMasterBean.setDrawId(drawId);
				drawMasterBean.setDrawDateTime(Util.convertDateTimeToResponseFormat2(drawRs.getString("draw_datetime").replace(".0", "")));
				drawMasterBean.setDrawName(drawRs.getString("draw_name"));
				drawMasterBean.setGameTypeId(gameTypeId);
				drawMasterBean.setDrawDisplayType(CommonMethodsDaoImpl.getInstance().fetchGameTypeOptionList(gameId, gameTypeId, connection).toString());
				
				query = "SELECT game_id,  gameName,a.event_id, eventDisp,  eventDesc,  leagueName,  homeTeam, homeTeamCode, awayTeam,awayTeamCode,  venuName, start_time, end_time FROM (select a.event_id, a.game_id, game_disp_name gameName, event_display eventDisp, event_description eventDesc, " +
				"league_display_name leagueName, d.team_name homeTeam,d.team_code homeTeamCode, f.team_name awayTeam,f.team_code awayTeamCode, venue_display_name venuName, start_time, " +
				"end_time from st_sl_event_master a, st_sl_game_merchant_mapping b, st_sl_league_master c, st_sl_game_team_master d, " +
				"st_sl_venue_master e,  st_sl_game_team_master f where a.game_id = b.game_id and a.game_id  ="+gameId+" and " +
				"b.merchant_id = "+merchantId+" and a.league_id = c.league_id and a.home_team_id = d.team_id and " +
				"a.venue_id = e.venue_id and a.away_team_id = f.team_id ) a INNER JOIN (" +
				 "select event_id, event_order from st_sl_draw_event_mapping_"+gameId+" where draw_id="+drawId+") b on a.event_id = b.event_id order by b.event_order";
				stmt = connection.createStatement();
				logger.info("Event List Query - "+query);
				rs = stmt.executeQuery(query);
				eventList = new ArrayList<EventMasterBean>();
				while(rs.next()) {
					eventBean = new EventMasterBean();
					eventBean.setGameId(rs.getInt("game_id"));
					eventBean.setEventId(rs.getInt("event_id"));
					eventBean.setEventDisplay(rs.getString("eventDisp"));
					eventBean.setEventDescription(rs.getString("eventDesc"));
					eventBean.setLeagueName(rs.getString("leagueName"));
					eventBean.setHomeTeamName(rs.getString("homeTeam"));
					eventBean.setHomeTeamCode(rs.getString("homeTeamCode"));
					eventBean.setAwayTeamName(rs.getString("awayTeam"));
					eventBean.setAwayTeamCode(rs.getString("awayTeamCode"));
					eventBean.setVenueName(rs.getString("venuName"));
					eventBean.setStartTime(Util.convertDateTimeToResponseFormat2(rs.getString("start_time").replace(".0", "")));
					eventBean.setEndTime(rs.getString("end_time"));
					eventList.add(eventBean);
				}
				drawMasterBean.setEventMasterList(eventList);
				if(!drawMasterBean.getEventMasterList().isEmpty())
					drawmasterBeanList.add(drawMasterBean);
				}
				
		} catch (SQLException se) {
			se.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}finally{
			DBConnect.closeConnection(stmt, rs);
			DBConnect.closeRs(drawRs);
		}
		return drawmasterBeanList;
	}
	
	
	public List<DrawMasterBean> getSleResultDataDrawWiseForWeb(int gameId,int gameTypeId ,int merchantId,Connection connection) throws SLEException {
		List<EventMasterBean> eventList = null;
		EventMasterBean eventBean = null;
		Statement stmt = null;
		ResultSet drawRs = null;
		ResultSet rs = null;
		String query = null;
		String qry=null;
		int drawId=0;
		List<DrawMasterBean>drawmasterBeanList=null;
		DrawMasterBean drawMasterBean=null;
		try {
			drawmasterBeanList=new LinkedList<DrawMasterBean>();
			
			qry = "SELECT a.draw_id ,a.draw_datetime,b.draw_name FROM st_sl_draw_master_"+ gameId+ " a, st_sl_draw_merchant_mapping_"+ gameId+ " b WHERE a.draw_id = b.draw_id AND b.merchant_id = "+ merchantId+ " AND a.game_type_id ="+ gameTypeId+ " AND draw_status = 'CLAIM ALLOW' ORDER BY draw_datetime desc LIMIT "+Util.getPropertyValue("MOBILE_DRAW_LIMIT_WINNING_RESULT");
			stmt = connection.createStatement();
			logger.info("draw info Query - "+query);
			drawRs = stmt.executeQuery(qry.toString());
			while(drawRs.next()){
				drawMasterBean = new DrawMasterBean();
				drawId=drawRs.getInt("draw_id");
				drawMasterBean.setDrawId(drawId);
				drawMasterBean.setDrawDateTime(Util.convertDateTimeToResponseFormat2(drawRs.getString("draw_datetime").replace(".0", "")));
				drawMasterBean.setDrawName(drawRs.getString("draw_name"));
				drawMasterBean.setDrawDisplayType(CommonMethodsDaoImpl.getInstance().fetchGameTypeOptionList(gameId, gameTypeId, connection).toString());
				
				query = "SELECT draw_id, event_id,event_display,start_time,end_time, leagueName,venuName,(SELECT team_name FROM st_sl_game_team_master where team_id=master.home_team_id) home_team_name,(SELECT team_code FROM st_sl_game_team_master where team_id=master.home_team_id) home_team_code ,(SELECT team_name FROM st_sl_game_team_master where team_id=master.away_team_id) away_team_name,(SELECT team_code FROM st_sl_game_team_master where team_id=master.away_team_id) away_team_code, option_code,option_disp_name FROM (SELECT draw_id, em.event_id,event_display,em.start_time,em.end_time,league_display_name leagueName,venue_display_name venuName, home_team_id, away_team_id, option_code,option_name option_disp_name FROM st_sl_draw_event_mapping_"+gameId+" dem INNER JOIN st_sl_event_option_mapping eom ON dem.evt_opt_id=eom.evt_opt_id INNER JOIN st_sl_event_master em ON em.event_id=dem.event_id   INNER JOIN st_sl_league_master slm ON slm.league_id=em.league_id  INNER JOIN  st_sl_venue_master svm on svm.venue_id=em.venue_id WHERE draw_id="+drawId+" and eom.game_id="+gameId+" and eom.game_type_id="+gameTypeId+" order by dem.event_order ) master";
				stmt = connection.createStatement();
				logger.info("Event List Query - "+query);
				rs = stmt.executeQuery(query);
				eventList = new ArrayList<EventMasterBean>();
				while(rs.next()) {
					eventBean = new EventMasterBean();
					eventBean.setEventId(rs.getInt("event_id"));
					eventBean.setEventDisplay(rs.getString("event_display"));
					eventBean.setLeagueName(rs.getString("leagueName"));
					eventBean.setVenueName(rs.getString("venuName"));
					eventBean.setStartTime(Util.convertDateTimeToResponseFormat2(rs.getString("start_time").replace(".0", "")));
					eventBean.setEndTime(rs.getString("end_time"));
					eventBean.setHomeTeamName(rs.getString("home_team_name"));
					eventBean.setHomeTeamCode(rs.getString("home_team_code"));
					eventBean.setAwayTeamName(rs.getString("away_team_name"));
					eventBean.setAwayTeamCode(rs.getString("away_team_code"));
					eventBean.setWinningOption(rs.getString("option_disp_name"));
					eventBean.setWinninOptionCode(rs.getString("option_code"));
					eventList.add(eventBean);
				}
				drawMasterBean.setEventMasterList(eventList);
				if(!drawMasterBean.getEventMasterList().isEmpty())
					drawmasterBeanList.add(drawMasterBean);
				}
				
		} catch (SQLException se) {
			se.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return drawmasterBeanList;
	}
	
	
	public int getMatchListCount(String userName, Connection con) throws SQLException {
		int matchListCount;
		String qry = "SELECT match_list_count FROM st_sl_retailer_match_list_count WHERE user_name = '"+userName+"'";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(qry);
		if(rs.next()){
			matchListCount = rs.getInt("match_list_count");
		}else{
			insertUser(userName, con);
			matchListCount = 0;
		}		
		return matchListCount;		
	}
	
	public void insertUser(String userName, Connection con) throws SQLException{
		Statement stmt = con.createStatement();
		stmt.executeUpdate("INSERT INTO st_sl_retailer_match_list_count (user_name) VALUES ('"+userName+"')");
	}
	
	public void updateMatchListCount(String userName, Connection con) throws SQLException{
		con.setAutoCommit(false);
		Statement stmt = con.createStatement();
		stmt.executeUpdate("UPDATE st_sl_retailer_match_list_count SET match_list_count = match_list_count+1 where user_name = '"+userName+"'");
		con.commit();
	}
	
	
	
	public List<GameMasterBean> getSportsLotteryGameWiseWinningData(MerchantInfoBean merchantInfoBean, Connection connection) throws SLEException {
		List<GameMasterBean> gameMasterList = null;
		GameMasterBean gameMasterBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		String query = null;
		try {
			query = "SELECT mas.game_id, merchant_id, game_dev_name, game_disp_name, max_ticket_amount, thersold_ticket_amount, min_board_count, max_board_count, display_order, map.game_status FROM st_sl_game_master mas INNER JOIN st_sl_game_merchant_mapping map ON mas.game_id=map.game_id WHERE mas.game_status='SALE_OPEN' and map.game_status='SALE_OPEN' and merchant_id="+merchantInfoBean.getMerchantId()+" ORDER BY map.display_order;";
			gameMasterList = new ArrayList<GameMasterBean>();
			stmt = connection.createStatement();
			logger.info("GameMasterDetails - "+query);
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				gameMasterBean = new GameMasterBean();
				int gameId = rs.getInt("game_id");
				gameMasterBean.setGameId(gameId);
				gameMasterBean.setMerchantId(rs.getInt("merchant_id"));
				gameMasterBean.setGameDevName(rs.getString("game_dev_name"));
				gameMasterBean.setGameDispName(rs.getString("game_disp_name"));
				gameMasterBean.setMaxTicketAmt(rs.getDouble("max_ticket_amount"));
				gameMasterBean.setThersholdTickerAmt(rs.getDouble("thersold_ticket_amount"));
				gameMasterBean.setMinBoardCount(rs.getInt("min_board_count"));
				gameMasterBean.setMaxBoardCount(rs.getInt("max_board_count"));
				gameMasterBean.setDisplayOrder(rs.getInt("display_order"));
				gameMasterBean.setGameStatus(rs.getString("game_status"));
				gameMasterBean.setGameTypeMasterList(getGameTypeWiseWinningData(gameId, merchantInfoBean,connection));
				gameMasterList.add(gameMasterBean);
			}
		} catch (SLEException e) {
			throw e;
		} catch (SQLException se) {
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}finally{
			DBConnect.closeConnection(stmt, rs);
		}

		return gameMasterList;
	}
	
	
	public List<GameTypeMasterBean> getGameTypeWiseWinningData(int gameId, MerchantInfoBean merchantInfoBean, Connection connection) throws SLEException {
		List<GameTypeMasterBean> gameTypeMasterList = null;
		GameTypeMasterBean gameTypeMasterBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		String query = null;
		try {
			query = "SELECT mas.game_type_id, game_id, merchant_id, type_dev_name, type_disp_name, unit_price, max_bet_amount_multiple, sale_start_time, sale_end_time,prize_amt_percentage,jackpot_message_display, no_of_events, event_type, display_order, map.type_status, map.prize_distribution_xml FROM st_sl_game_type_master mas INNER JOIN st_sl_game_type_merchant_mapping map ON mas.game_type_id=map.game_type_id WHERE mas.type_status='SALE_OPEN' AND map.type_status='SALE_OPEN' AND mas.game_id="+gameId+" AND map.merchant_id="+merchantInfoBean.getMerchantId()+" ORDER BY display_order;";
			gameTypeMasterList = new ArrayList<GameTypeMasterBean>();
			stmt = connection.createStatement();
			logger.info("GameTypeMasterDetails - "+query);
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				gameTypeMasterBean = new GameTypeMasterBean();
				int gameTypeId = rs.getInt("game_type_id");
				int noOfEvents = rs.getInt("no_of_events");
				gameTypeMasterBean.setGameTypeId(gameTypeId);
				gameTypeMasterBean.setGameId(rs.getInt("game_id"));
				gameTypeMasterBean.setMerchantId(rs.getInt("merchant_id"));
				gameTypeMasterBean.setGameTypeDevName(rs.getString("type_dev_name"));
				gameTypeMasterBean.setGameTypeDispName(rs.getString("type_disp_name"));
				gameTypeMasterBean.setUnitPrice(rs.getDouble("unit_price"));
				gameTypeMasterBean.setMaxBetAmtMultiple(rs.getInt("max_bet_amount_multiple"));
				gameTypeMasterBean.setSaleStartTime(rs.getString("sale_start_time"));
				gameTypeMasterBean.setSaleEndTime(rs.getString("sale_end_time"));
				gameTypeMasterBean.setPrizeAmtPercentage(rs.getDouble("prize_amt_percentage"));
				gameTypeMasterBean.setJackPotMessageDisplay(rs.getString("jackpot_message_display"));
				gameTypeMasterBean.setNoOfEvents(noOfEvents);
				gameTypeMasterBean.setEventType(rs.getString("event_type"));
				gameTypeMasterBean.setDisplayOrder(rs.getInt("display_order"));
				gameTypeMasterBean.setGameTypeStatus(rs.getString("type_status"));
				gameTypeMasterBean.setDrawMasterList(getDrawMasterWinningDetails(gameId, gameTypeId, noOfEvents, merchantInfoBean, connection));
			
				gameTypeMasterList.add(gameTypeMasterBean);
			}
		} catch (SLEException e) {
			throw e;
		} catch (SQLException se) {
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}finally{
			DBConnect.closeConnection(stmt, rs);
		}
		return gameTypeMasterList;
	}
	
	public List<DrawMasterBean> getDrawMasterWinningDetails(int gameId, int gameTypeId, int noOfEvents, MerchantInfoBean merchantInfoBean, Connection connection) throws SLEException {
		List<DrawMasterBean> drawMasterList = null;
		DrawMasterBean drawMasterBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		String query = null;
		try {
			query = "SELECT mas.draw_id, game_type_id, merchant_id, draw_no, draw_name, draw_display_type, draw_datetime, draw_freeze_time, sale_start_time, claim_start_date, claim_end_date, verification_date, draw_status, purchase_table_name FROM st_sl_draw_master_"+gameId+" mas INNER JOIN st_sl_draw_merchant_mapping_"+gameId+" map ON mas.draw_id=map.draw_id WHERE mas.game_type_id="+gameTypeId+" AND merchant_id="+merchantInfoBean.getMerchantId()+" AND draw_status= 'CLAIM ALLOW' order by draw_datetime desc LIMIT "+(String)Util.getPropertyValue("MOBILE_DRAW_LIMIT_WINNING_RESULT");		
			drawMasterList = new ArrayList<DrawMasterBean>();
			stmt = connection.createStatement();
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				int drawId = rs.getInt("draw_id");
				List<EventMasterBean> eventMasterList = getEventMasterWinningDetails(gameId,gameTypeId,drawId, connection);
				if(eventMasterList.size() == noOfEvents) {
					drawMasterBean = new DrawMasterBean();
					drawMasterBean.setDrawId(drawId);
					drawMasterBean.setGameTypeId(rs.getInt("game_type_id"));
					drawMasterBean.setMerchantId(rs.getInt("merchant_id"));
					drawMasterBean.setDrawNo(rs.getInt("draw_no"));
					drawMasterBean.setDrawName(rs.getString("draw_name"));

					if("DRAW_NAME".equals(rs.getString("draw_display_type"))){
						drawMasterBean.setDrawDisplayType(rs.getString("draw_name"));
					}else if("DRAW_DATETIME".equals(rs.getString("draw_display_type"))){
						SimpleDateFormat ft = new SimpleDateFormat ("EEE,MMM-d"); 
						drawMasterBean.setDrawDisplayType(ft.format(rs.getTimestamp("draw_datetime")));
					}else if("NAME_DATETIME".equals(rs.getString("draw_display_type"))){
						SimpleDateFormat ft = new SimpleDateFormat ("EEE,MMM-d");
						drawMasterBean.setDrawDisplayType(rs.getString("draw_name")+"<"+ft.format(rs.getTimestamp("draw_datetime"))+">");
					}

					drawMasterBean.setDrawDateTime(Util.getDateTimeFormat(rs.getTimestamp("draw_datetime")));
					drawMasterBean.setDrawFreezeTime(Util.getDateTimeFormat(rs.getTimestamp("draw_freeze_time")));
					drawMasterBean.setSaleStartTime(rs.getString("sale_start_time"));
					drawMasterBean.setClaimStartDate(rs.getString("claim_start_date"));
					drawMasterBean.setClaimEndDate(rs.getString("claim_end_date"));
					drawMasterBean.setVerificationDate(rs.getString("verification_date"));
					drawMasterBean.setDrawStatus(rs.getString("draw_status"));
					drawMasterBean.setPurchaseTableName(rs.getInt("purchase_table_name"));
					drawMasterBean.setDrawWinningDetail(getDrawWinningDetails(gameId,gameTypeId,merchantInfoBean,drawId,connection));
					drawMasterBean.setEventMasterList(eventMasterList);
					drawMasterList.add(drawMasterBean);
				}
			}
		} catch (SQLException se) {
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}finally{
			DBConnect.closeConnection(stmt, rs);
		}

		return drawMasterList;
	}
	
	
	public List<EventMasterBean> getEventMasterWinningDetails(int gameId, int gameTypeId,int drawId, Connection connection) throws SLEException {
		List<EventMasterBean> eventMasterList = null;
		EventMasterBean eventMasterBean = null;
		SimpleDateFormat simpleDateFormat = null;
		Statement stmt = null;
		ResultSet rs = null;
		String query = null;
		try {
			simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			query = "SELECT eventTB.event_id,option_code winning_option ,venue_display_name,event_display,event_description,league_display_name,home_team_name,home_team_code,away_team_code,away_team_name,start_time,end_time,entry_time,league_display_name FROM (SELECT map.event_id, vm.venue_display_name, event_display, event_description, lm.league_display_name, (SELECT team_name FROM st_sl_game_team_master WHERE team_id = home_team_id) home_team_name,(SELECT team_code FROM st_sl_game_team_master WHERE team_id = home_team_id) home_team_code, (SELECT team_code FROM st_sl_game_team_master WHERE team_id = away_team_id) away_team_code, (SELECT team_name FROM st_sl_game_team_master WHERE team_id = away_team_id) away_team_name, start_time, end_time, entry_time  FROM st_sl_event_master mas INNER JOIN st_sl_venue_master vm ON vm.venue_id = mas.venue_id INNER JOIN st_sl_league_master lm ON lm.league_id = mas.league_id INNER JOIN st_sl_draw_event_mapping_"+gameId+" map ON mas.event_id=map.event_id AND map.draw_id="+drawId+" ORDER BY map.event_order ) eventTB INNER JOIN (select  dem.event_id,option_code from st_sl_draw_event_mapping_"+gameId+" dem INNER JOIN st_sl_event_option_mapping eom on dem.evt_opt_id=eom.evt_opt_id where game_id="+gameId+" and game_type_id="+gameTypeId+")winTB  on eventTB.event_id=winTB.event_id";
			eventMasterList = new ArrayList<EventMasterBean>();
			stmt = connection.createStatement();
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				eventMasterBean = new EventMasterBean();
				int eventId = rs.getInt("event_id");
				eventMasterBean.setEventId(eventId);
				eventMasterBean.setEventDisplay(rs.getString("event_display"));
				eventMasterBean.setEventDescription(rs.getString("event_description"));
				eventMasterBean.setStartTime(simpleDateFormat.format(rs.getTimestamp("start_time")));
				eventMasterBean.setEndTime(simpleDateFormat.format(rs.getTimestamp("end_time")));
				eventMasterBean.setEntryTime(simpleDateFormat.format(rs.getTimestamp("entry_time")));
				eventMasterBean.setVenueName(rs.getString("venue_display_name"));
				eventMasterBean.setHomeTeamName(rs.getString("home_team_name"));
				eventMasterBean.setHomeTeamCode(rs.getString("home_team_code"));
				eventMasterBean.setAwayTeamName(rs.getString("away_team_name"));
				eventMasterBean.setAwayTeamCode(rs.getString("away_team_code"));
				eventMasterBean.setWinningOption(rs.getString("winning_option"));
				eventMasterBean.setLeagueName(rs.getString("league_display_name"));
				eventMasterBean.setEventOptionsList(getEventOptionsList(eventId, gameId,gameTypeId, connection));
				eventMasterList.add(eventMasterBean);
			}
		} catch (SQLException se) {
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally{
			DBConnect.closeConnection(stmt, rs);
		}

		return eventMasterList;
	}


	
	public List<SLEDrawWinningBean> getDrawWinningDetails(int gameId, int gameTypeId,MerchantInfoBean merchantInfoBean,int drawId, Connection connection) throws SLEException {
		List<SLEDrawWinningBean> drawWinningList = null;
		SLEDrawWinningBean drawWinningBean = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String query = null;
		try {
			drawWinningList = new LinkedList<SLEDrawWinningBean>();
			query="SELECT no_of_match,sum(no_of_winners) no_of_winners,prize_amount FROM  st_sl_prize_details_?_? WHERE draw_id=? group by no_of_match ORDER BY no_of_match DESC";
			pstmt=connection.prepareStatement(query);
			pstmt.setInt(1, gameId);
			pstmt.setInt(2, gameTypeId);
			pstmt.setInt(3, drawId);
			rs=pstmt.executeQuery();
			logger.info("DrawWinningDetails - "+pstmt.toString());
			while(rs.next()){
				drawWinningBean=new SLEDrawWinningBean();
				drawWinningBean.setNoOfMatches(rs.getInt("no_of_match"));
				drawWinningBean.setNoOfWInniners(rs.getLong("no_of_winners"));
				if(drawWinningBean.getNoOfWInniners()!=0){
					drawWinningBean.setPrizeAmount(Util.fmtToTwoDecimal(rs.getDouble("prize_amount")*drawWinningBean.getNoOfWInniners()));
				}else{
					drawWinningBean.setPrizeAmount(Util.fmtToTwoDecimal(rs.getDouble("prize_amount")));
				}
				
				drawWinningList.add(drawWinningBean);
			}
	
		} catch (SQLException se) {
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally{
			DBConnect.closeConnection(pstmt, rs);
		}

		return drawWinningList;
	}
	
	
	public List<TicketTxnStatusBean> getSportsLotteryTxnStatus(MerchantInfoBean merchantInfoBean,String TxnIdList, Connection connection) throws SLEException {
		List<TicketTxnStatusBean> txnStatusBeanList = null;
		StringBuilder salequery=null;
		String query=null;
		String winTicketCheckQuery=null;
		Statement stmt=null;
		ResultSet rs=null;
		ResultSet winRs=null;
		ResultSet boardRs=null;
		try{
			salequery=new StringBuilder();
			txnStatusBeanList=new ArrayList<TicketTxnStatusBean>();
			salequery.append("SELECT game_id,game_type_id,ticket_nbr,merchant_trans_id FROM st_sl_sale_txn_master WHERE merchant_id=").append(merchantInfoBean.getMerchantId()).append("  AND status='DONE' AND merchant_trans_id IN (").append(TxnIdList.replace("\"", "").replace("[","").replace("]", "")).append(")");
			stmt=connection.createStatement();
			rs=stmt.executeQuery(salequery.toString());
			while(rs.next()){
				TicketTxnStatusBean tktTxnStatusBean=new TicketTxnStatusBean();
				tktTxnStatusBean.setGameId(rs.getInt("game_id"));
				tktTxnStatusBean.setGameTypeId(rs.getInt("game_type_id"));
				tktTxnStatusBean.setTicketNbr(rs.getLong("ticket_nbr"));
				tktTxnStatusBean.setMerchantTxnId(rs.getString("merchant_trans_id"));
				tktTxnStatusBean.setWinStatus("");
				tktTxnStatusBean.setWinAmt(0.00);
				txnStatusBeanList.add(tktTxnStatusBean);
			}
			
			if(txnStatusBeanList.isEmpty())
				throw new SLEException(SLEErrors.NO_RECORD_FOUND_ERROR_CODE,SLEErrors.NO_RECORD_FOUND_ERROR_MESSAGE);
			
			for(TicketTxnStatusBean tktTxnStatusBean:txnStatusBeanList){
				int purchaseTable=0;
				int gameId=tktTxnStatusBean.getGameId();
				int gameTypeId=tktTxnStatusBean.getGameTypeId();
				long tktNo=tktTxnStatusBean.getTicketNbr();
				String drawStatus=null;
				
				query="select dm.draw_id,purchase_table_name,draw_datetime,draw_status FROM st_sl_draw_master_"+gameId+" dm inner join st_sl_game_tickets_"+gameId+"_"+gameTypeId+" gt on gt.draw_id=dm.draw_id INNER JOIN st_sl_sale_txn_master stm on stm.trans_id=gt.trans_id WHERE  ticket_nbr="+tktNo+"";
				rs=stmt.executeQuery(query);
				if(rs.next()){
					purchaseTable=rs.getInt("purchase_table_name");
					drawStatus=rs.getString("draw_status");
				}
				
				if("FREEZE".equalsIgnoreCase(drawStatus) || "CLAIM HOLD".equalsIgnoreCase(drawStatus) || "ACTIVE".equalsIgnoreCase(drawStatus)){
					tktTxnStatusBean.setWinStatus("RA");
				} else{
					query="SELECT board_id, ticket_number,  IFNULL(SUM(winAmt), 0.00) winAmt, SUM(IFNULL(rank_id, 0)) rank_id FROM (SELECT board_id, ticket_number, (bet_amount_multiple*prize_amount)winAmt, rank_id, draw_tlb.merchant_id FROM st_sl_draw_ticket_"+gameId+"_"+gameTypeId+"_"+purchaseTable+" draw_tlb LEFT JOIN st_sl_prize_details_"+gameId+"_"+gameTypeId+" prize ON draw_tlb.merchant_id=prize.merchant_id AND draw_tlb.rank_id=prize.prize_rank AND draw_tlb.draw_id=prize.draw_id WHERE ticket_number="+tktNo+" AND draw_tlb.merchant_id="+merchantInfoBean.getMerchantId()+")winTlb GROUP BY ticket_number;";	
					boardRs=stmt.executeQuery(query);
					
					if(boardRs.next()){
						double winAmt=boardRs.getDouble("winAmt");
						int rank=boardRs.getInt("rank_id");
						if(winAmt == 0.0 || rank == 0){
							tktTxnStatusBean.setWinStatus("NON_WIN");
						} else{
							winTicketCheckQuery="SELECT trans_id,status FROM st_sl_winning_txn_master WHERE ticket_nbr='"+tktNo+"'";
							winRs=stmt.executeQuery(winTicketCheckQuery);
							if(winRs.next()){
								if("INITIATED".equalsIgnoreCase(winRs.getString("status"))){
									tktTxnStatusBean.setWinStatus("SETTLEMENT PENDING");
								}else{
									tktTxnStatusBean.setWinStatus("WIN");
									tktTxnStatusBean.setWinAmt(winAmt);
								}
							}else{
								tktTxnStatusBean.setWinStatus("SETTLEMENT PENDING");
							}
							
						}
					}
				}
			}
		}catch (SLEException e) {
			throw e;
		}catch (Exception e) {
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}finally{
			DBConnect.closeRs(boardRs);
			DBConnect.closeConnection(stmt, boardRs);
			DBConnect.closeRs(winRs);
		}
		return txnStatusBeanList;
	}
	
	public List<TicketSalePwtInfoBean> fetchDetailedSmallWinningPaymentReport(int merchantId,int gameId,int gameTypeId,String sDate,String eDate,String detailType,String retList,Connection con)throws SLEException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = null;
		String retQueryAppender = null;
		List<TicketSalePwtInfoBean> ticketWiseSalePwtInfoList = null;
		try {
			retQueryAppender = "";
			ticketWiseSalePwtInfoList = new LinkedList<TicketSalePwtInfoBean>();
			if (!"DETAILED".equalsIgnoreCase(detailType)) {
				retQueryAppender = "AND stm.merchant_user_id IN ("+retList.replace("[", "").replace("]", "")+")"; 
			}
			query = "SELECT dm.draw_id, draw_name,IF(stm.merchant_id = ?,CONCAT(stm.ticket_nbr,rpc_total),stm.ticket_nbr) ticket_nbr ,stm.trans_date purchase_time,stm.amount sale_amount, wtm.amount win_amount ,wtm.trans_date date_prize_paid,stm.merchant_user_id party_id FROM st_sl_sale_txn_master stm INNER JOIN st_sl_winning_txn_master wtm ON stm.ticket_nbr = wtm.ticket_nbr AND stm.merchant_id = wtm.merchant_id INNER JOIN st_sl_draw_master_? dm ON dm.draw_id = wtm.draw_id INNER JOIN  st_sl_draw_merchant_mapping_? dmm ON dm.draw_id = dmm.draw_id INNER JOIN st_sl_game_tickets_?_? gt ON gt.trans_id = stm.trans_id WHERE stm.status = 'DONE' AND wtm.status ='DONE' AND stm.merchant_id = ? AND dmm.merchant_id = ? AND draw_datetime >= ? AND draw_datetime <= ?"+retQueryAppender;
			ps = con.prepareStatement(query);
			ps.setInt(1, merchantId);
			ps.setInt(2, gameId);
			ps.setInt(3, gameId);
			ps.setInt(4, gameId);
			ps.setInt(5, gameTypeId);
			ps.setInt(6, merchantId);
			ps.setInt(7, merchantId);
			ps.setString(8, sDate);
			ps.setString(9, eDate);
			logger.info("smallWinningDetailDataQuery" + ps);
			rs = ps.executeQuery();
			
			while (rs.next()) {
				if (rs.getDouble("win_amount") <= Double.parseDouble(Util.getPropertyValue("AUTO_APPROVED_WINNING_AMT_LIMIT"))) {
					TicketSalePwtInfoBean dataBean = new TicketSalePwtInfoBean();
					dataBean.setDrawId(rs.getInt("draw_id"));
					dataBean.setDrawName(rs.getString("draw_name"));
					dataBean.setTicketNbr(rs.getLong("ticket_nbr"));
					dataBean.setSaleDateTime(rs.getString("purchase_time").replace(".",""));
					dataBean.setSaleAmt(rs.getDouble("sale_amount"));
					dataBean.setWinAmt(rs.getDouble("win_amount"));
					dataBean.setDatePrizePaid(rs.getString("date_prize_paid").replace(".",""));
					dataBean.setPartyId(rs.getString("party_id"));
					ticketWiseSalePwtInfoList.add(dataBean);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}finally{
			DBConnect.closeConnection(ps,rs);
		}
		return ticketWiseSalePwtInfoList;
	}
	
	
	
	
	
	/*public TicketTxnStatusBean getSportsLotteryTxnStatusfromTxnId (MerchantInfoBean merchantInfoBean,String txnId, Connection connection) throws SLEException {
		TicketTxnStatusBean tktTxnStatusBean = null;
		StringBuilder salequery=null;
		String query=null;
		Statement stmt=null;
		ResultSet rs=null;
		ResultSet boardRs=null;
		try{
			salequery=new StringBuilder();
			tktTxnStatusBean=new TicketTxnStatusBean();
			salequery.append("SELECT game_id,game_type_id,ticket_nbr,merchant_trans_id FROM st_sl_sale_txn_master WHERE merchant_id=").append(merchantInfoBean.getMerchantId()).append(" AND channel_type='MOBILE' AND status='DONE' AND trans_id=").append(txnId);
			stmt=connection.createStatement();
			rs=stmt.executeQuery(salequery.toString());
			if(rs.next()){
				tktTxnStatusBean.setGameId(rs.getInt("game_id"));
				tktTxnStatusBean.setGameTypeId(rs.getInt("game_type_id"));
				tktTxnStatusBean.setTicketNbr(rs.getLong("ticket_nbr"));
				tktTxnStatusBean.setMerchantTxnId(rs.getString("merchant_trans_id"));
				tktTxnStatusBean.setWinStatus("");
				tktTxnStatusBean.setWinAmt(0.00);
			}
			
			int drawId=0;
			int purchaseTable=0;
			int gameId=tktTxnStatusBean.getGameId();
			int gameTypeId=tktTxnStatusBean.getGameTypeId();
			long tktNo=tktTxnStatusBean.getTicketNbr();
			Timestamp drawDate=null;
			String drawStatus=null;
				
			query="select dm.draw_id,purchase_table_name,draw_datetime,draw_status FROM st_sl_draw_master_"+gameId+" dm inner join st_sl_game_tickets_"+gameId+"_"+gameTypeId+" gt on gt.draw_id=dm.draw_id WHERE  ticket_number="+tktNo+"";
			rs=stmt.executeQuery(query);
			if(rs.next()){
				drawId=rs.getInt("draw_id");
				purchaseTable=rs.getInt("purchase_table_name");
				drawDate=rs.getTimestamp("draw_datetime");
				drawStatus=rs.getString("draw_status");
			}
				
			Timestamp currentDate = Util.getCurrentTimeStamp();
				
			if(currentDate.before(drawDate) || "CLAIM HOLD".equalsIgnoreCase(drawStatus)){
				tktTxnStatusBean.setWinStatus("RA");
			} else{
				query="SELECT board_id, ticket_number,  IFNULL(SUM(winAmt), 0.00) winAmt, IFNULL(rank_id, 0) rank_id FROM (SELECT board_id, ticket_number, (bet_amount_multiple*prize_amount)winAmt, rank_id, draw_tlb.merchant_id FROM st_sl_draw_ticket_"+gameId+"_"+gameTypeId+"_"+purchaseTable+" draw_tlb LEFT JOIN st_sl_prize_details_"+gameId+"_"+gameTypeId+" prize ON draw_tlb.merchant_id=prize.merchant_id AND draw_tlb.rank_id=prize.prize_rank AND draw_tlb.draw_id=prize.draw_id WHERE ticket_number="+tktNo+" AND draw_tlb.merchant_id="+merchantInfoBean.getMerchantId()+")winTlb GROUP BY ticket_number;";	
				boardRs=stmt.executeQuery(query);
				
				while(boardRs.next()){
					double winAmt=boardRs.getDouble("winAmt");
					int rank=boardRs.getInt("rank_id");
					if(winAmt == 0.0 || rank == 0){
						tktTxnStatusBean.setWinStatus("NON WIN");
					} else{
						tktTxnStatusBean.setWinStatus("WIN");
						tktTxnStatusBean.setWinAmt(winAmt);
					}
				}
			}
		}catch (Exception e) {
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}finally{
			DBConnect.closeRs(boardRs);
			DBConnect.closeConnection(stmt, boardRs);
		}
		return tktTxnStatusBean;
	}*/

}