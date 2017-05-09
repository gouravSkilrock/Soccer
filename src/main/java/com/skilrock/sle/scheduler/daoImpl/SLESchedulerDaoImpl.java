package com.skilrock.sle.scheduler.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.skilrock.sle.common.DateTimeFormat;
import com.skilrock.sle.common.SLELogger;
import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;

public class SLESchedulerDaoImpl {
	private static final SLELogger logger = SLELogger.getLogger(SLESchedulerDaoImpl.class.getName());
	
	private static volatile SLESchedulerDaoImpl classInstance;
	public static SLESchedulerDaoImpl getInstance() {
		if(classInstance == null){
			synchronized (SLESchedulerDaoImpl.class) {
				if(classInstance == null){
					classInstance = new SLESchedulerDaoImpl();					
				}
			}
		}
		return classInstance;
	}
	public void updateFreezeTimeStatus(Connection con) throws SLEException {
		Statement stmt = null;
		ResultSet gameRs = null;
		logger.info("***** Inside updateFreezeTimeStatus Method");
		
		try {
			stmt = con.createStatement();
			String gameQuery = "select SQL_CACHE game_id from st_sl_game_master where game_status='SALE_OPEN'";
			logger.info("Query For Fetching Active Game Ids "+gameQuery);
			gameRs = stmt.executeQuery(gameQuery);
			while (gameRs.next()) {
				int gameId = gameRs.getInt("game_id");

				String updateFreezeQry = "update st_sl_draw_master_" + gameId
						+ " set draw_status='FREEZE' where draw_freeze_time<='"
						+ DateTimeFormat.getCurrentTimeString()
						+ "' and draw_status='ACTIVE'";
				stmt = con.createStatement();
				logger.info("Query For Changing Draw Status To Freeze "+updateFreezeQry);
				int updateCount = stmt.executeUpdate(updateFreezeQry);
				logger.info("Count of Updated Draw Status To Freeze "+updateCount);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
	}
	
	public void freezeDraw(int gameId, int gameTypeId, int drawId, Connection con) throws SLEException {
		Statement stmt = null;
		logger.info("***** Inside freezeDraw Method");

		try {
			stmt = con.createStatement();
			String updateFreezeQry = "update st_sl_draw_master_" + gameId
					+ " set draw_status='FREEZE' where game_type_id = " + gameTypeId + " AND draw_id = " + drawId;
			stmt = con.createStatement();
			logger.info("Query For Changing Draw Status To Freeze "+updateFreezeQry);
			int updateCount = stmt.executeUpdate(updateFreezeQry);
			logger.info("Count of Updated Draw Status To Freeze "+updateCount);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
	}
	
	public List<String> getNextDrawData(int gameId, int gameTypeId, Connection con) throws SLEException {
		List<String> returnList = new ArrayList<String>(2);
		Timestamp dateTime = null;
		String cronExp = null;
		ResultSet rs = null;
		
		try {
			Statement stmt = con.createStatement();
			rs = stmt.executeQuery("select draw_freeze_time, draw_id from st_sl_draw_master_" + gameId + " where game_type_id = " + gameTypeId + " AND draw_status='ACTIVE' and draw_freeze_time > '" + Util.getCurrentTimeString() + "' order by draw_freeze_time limit 1");
			if (rs.next()) {
				dateTime = rs.getTimestamp("draw_freeze_time");
				logger.debug("Next Draw Freeze Time for GameId "+gameId+" , gameTypeId "+gameTypeId+" and drawId is "+rs.getInt("draw_id"));

				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(dateTime.getTime());
				int sec = cal.get(Calendar.SECOND);
				int mm = cal.get(Calendar.MINUTE);
				int hh = cal.get(Calendar.HOUR_OF_DAY);
				int day = cal.get(Calendar.DAY_OF_MONTH);
				int month = cal.get(Calendar.MONTH);
				int year = cal.get(Calendar.YEAR);

				cronExp = "" + sec + " " + mm + " " + hh + " " + day + " " + (month + 1) + " ? " + year;
				returnList.add(cronExp);
				returnList.add(rs.getString("draw_id"));
			}
			logger.debug("Cron Expression is "+cronExp);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return returnList;
	}
	
	
	public List<String> getNextDrawDataForSendDrawNotification(int gameId, int gameTypeId, Connection con) throws SLEException {
		List<String> returnList = new ArrayList<String>(2);
		Timestamp dateTime = null;
		String cronExp = null;
		ResultSet rs = null;
		
		try {
			Statement stmt = con.createStatement();
			rs = stmt.executeQuery("select sale_start_time, draw_id from st_sl_draw_master_" + gameId + " where game_type_id = " + gameTypeId + " AND draw_status IN('ACTIVE','INACTIVE') and sale_start_time > '" + Util.getCurrentTimeString() + "' order by sale_start_time limit 1");
			if (rs.next()) {
				dateTime = rs.getTimestamp("sale_start_time");
				logger.debug("Next Draw  Time for GameId "+gameId+" , gameTypeId "+gameTypeId+" and drawId is "+rs.getInt("draw_id"));

				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(dateTime.getTime());
				int sec = cal.get(Calendar.SECOND);
				int mm = cal.get(Calendar.MINUTE);
				int hh = cal.get(Calendar.HOUR_OF_DAY);
				int day = cal.get(Calendar.DAY_OF_MONTH);
				int month = cal.get(Calendar.MONTH);
				int year = cal.get(Calendar.YEAR);

				cronExp = "" + sec + " " + mm + " " + hh + " " + day + " " + (month + 1) + " ? " + year;
				returnList.add(cronExp);
				returnList.add(rs.getString("draw_id"));
			}
			logger.info("Cron Expression is "+cronExp);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return returnList;
	}
	
	
	//Original
//	public void insertDraws(Connection con) throws SLEException {
//		Statement gameStmt = null;
//		ResultSet gameRs = null;
//
//		Statement drawStmt = null;
//		ResultSet drawRs = null;
//		int maxAdvanceDaysDraw = 60;
//		Timestamp lastDrawTime = null;
//		try {
//			gameStmt = con.createStatement();
//			String gameQuery = "select SQL_CACHE gm.game_id,gtm.game_type_id,max_advance_draws,additional_draws,scheduler_draw_data,entry_per_table,draw_frequency,table_query from st_sl_game_master gm inner join st_sl_game_type_master gtm inner join st_sl_scheduler_master gsm on gm.game_id=gtm.game_id and gtm.game_type_id=gsm.game_type_id where gm.game_status='SALE_OPEN' and gtm.type_status='SALE_OPEN' and scheduler_status='ACTIVE'";
//			gameRs = gameStmt.executeQuery(gameQuery);
//			while (gameRs.next()) {
//				int gameId = gameRs.getInt("game_id");
//				int gameTypeId = gameRs.getInt("game_type_id");
//				int maxAdvanceDraws = gameRs.getInt("max_advance_draws");
//				int additionalDraws = gameRs.getInt("additional_draws");
//	
//				String drawQuery = "select draw_datetime,draw_status from st_sl_draw_master_"
//						+ gameId
//						+ " where draw_datetime> '"
//						+ DateTimeFormat.getCurrentTimeString()
//						+ "' and game_type_id="
//						+ gameTypeId
//						+ " order by draw_datetime asc";
//				drawStmt = con.createStatement();
//				drawRs = drawStmt.executeQuery(drawQuery);
//				int activeDraws = 0;
//				int inactiveDraws = 0;
//				while (drawRs.next()) {
//					if ("ACTIVE".equals(drawRs.getString("draw_status"))) {
//						activeDraws++;
//					} else if ("INACTIVE".equals(drawRs.getString("draw_status"))) {
//						inactiveDraws++;
//					}
//					lastDrawTime = drawRs.getTimestamp("draw_datetime");
//				}
//				if (lastDrawTime == null || lastDrawTime.before(new Timestamp(new Date().getTime()))) {
//					lastDrawTime = DateTimeFormat.getCurrentTimeStamp();
//				}
//	
//				if (maxAdvanceDraws > activeDraws || additionalDraws > inactiveDraws) {
//					int insertDrawCount = maxAdvanceDraws + additionalDraws - activeDraws - inactiveDraws;
//					int drawCount = 0;
//					String drawFrequency = gameRs.getString("draw_frequency");
//					String drawScheduleData = gameRs.getString("scheduler_draw_data");
//					String tableQuery = gameRs.getString("table_query");
//					int entryPerTable = gameRs.getInt("entry_per_table");
//					if ("WEEKLY".equals(drawFrequency)) {
//						JsonParser parser = new JsonParser();
//						JsonObject drawScheduleObj = (JsonObject) parser.parse(drawScheduleData);
//	
//						JsonObject weeklyDrawObj = drawScheduleObj.get("WEEKLY").getAsJsonObject();
//	
//						Calendar cal = Calendar.getInstance();
//						cal.setTimeInMillis(lastDrawTime.getTime());
//						cal.add(Calendar.MINUTE, 2);
//						cal.set(Calendar.SECOND, 0);
//						int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
//	
//						// String currentDayName =new
//						// SimpleDateFormat("EEE").format(cal.getTime());
//						Map<Integer, String> dayMap = new HashMap<Integer, String>();
//						dayMap.put(1, "SUN");
//						dayMap.put(2, "MON");
//						dayMap.put(3, "TUE");
//						dayMap.put(4, "WED");
//						dayMap.put(5, "THU");
//						dayMap.put(6, "FRI");
//						dayMap.put(7, "SAT");
//	
//						Calendar drawCal = Calendar.getInstance();
//						drawCal.setTimeInMillis(lastDrawTime.getTime());
//						boolean isBreak = false;
//						for (int i = dayOfWeek; i < dayOfWeek + maxAdvanceDaysDraw; i++) {
//							if (isBreak) {
//								break;
//							}
//							if (i != dayOfWeek) {
//								drawCal.add(Calendar.DAY_OF_YEAR, 1);
//							}
//	
//							int dayWeek = i % 7;
//							String dayName = dayMap.get(dayWeek);
//	
//							JsonElement dayDrawElement = weeklyDrawObj.get(dayName);
//	
//							if (dayDrawElement != null) {
//								JsonArray dayDrawArr = dayDrawElement.getAsJsonArray();
//								for (JsonElement drawElement : dayDrawArr) {
//									if (drawCount == insertDrawCount) {
//										PreparedStatement updateDrawPstmt = null;
//										updateDrawPstmt = con.prepareStatement("update st_sl_draw_master_? aa inner join (select draw_id from st_sl_draw_master_? where draw_status in ('INACTIVE','ACTIVE') and draw_freeze_time> ? and game_type_id = ? order by draw_datetime asc limit ?,?) bb on aa.draw_id=bb.draw_id set draw_status='ACTIVE'");
//										updateDrawPstmt.setInt(1, gameId);
//										updateDrawPstmt.setInt(2, gameId);
//										updateDrawPstmt.setTimestamp(3, DateTimeFormat.getCurrentTimeStamp());
//										updateDrawPstmt.setInt(4, gameTypeId);
//										updateDrawPstmt.setInt(5, 0);
//										updateDrawPstmt.setInt(6, maxAdvanceDraws);
//										updateDrawPstmt.executeUpdate();
//	
//										updateDrawPstmt = con.prepareStatement("update st_sl_draw_master_? aa inner join (select draw_id from st_sl_draw_master_? where draw_status in ('INACTIVE','ACTIVE') and draw_freeze_time> ? and game_type_id = ? order by draw_datetime asc limit ?,?) bb on aa.draw_id=bb.draw_id set draw_status='INACTIVE'");
//										updateDrawPstmt.setInt(1, gameId);
//										updateDrawPstmt.setInt(2, gameId);
//										updateDrawPstmt.setTimestamp(3, DateTimeFormat.getCurrentTimeStamp());
//										updateDrawPstmt.setInt(4, gameTypeId);
//										updateDrawPstmt.setInt(5, maxAdvanceDraws);
//										updateDrawPstmt.setInt(6, additionalDraws);
//										updateDrawPstmt.executeUpdate();
//										isBreak = true;
//										break;
//									}
//	
//									JsonObject drawObj = drawElement.getAsJsonObject();
//	
//									drawCal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(drawObj.get("drawTime").getAsString().substring(0, 2)));
//									drawCal.set(Calendar.MINUTE, Integer.parseInt(drawObj.get("drawTime").getAsString().substring(2, 4)));
//	
//									Timestamp drawDateTime = new Timestamp(drawCal.getTimeInMillis());
//									if (drawDateTime.compareTo(lastDrawTime) > 0) {
//										drawCount++;
//	
//										int mapId = drawObj.get("mapId").getAsInt();
//										int freezeTime = drawObj.get("freezeTime").getAsInt();
//										int saleStartTime = drawObj.get("saleStartTime").getAsInt();
//	
//										drawCal.add(Calendar.SECOND, -freezeTime);
//										Timestamp freezeDateTime = new Timestamp(drawCal.getTimeInMillis());
//										drawCal.add(Calendar.SECOND, freezeTime);
//	
//										drawCal.add(Calendar.SECOND, -saleStartTime);
//										Timestamp saleDateTime = new Timestamp(drawCal.getTimeInMillis());
//										drawCal.add(Calendar.SECOND, saleStartTime);
//	
//										int eventId = 1;
//										int purchaseTableName = getTableName(entryPerTable, gameId, gameTypeId, con);
//	
//										PreparedStatement insertSchedulePstmt = con.prepareStatement(
//														"insert into st_sl_draw_master_?(game_type_id,draw_no,draw_datetime,draw_freeze_time,sale_start_time,draw_status,purchase_table_name)values(?,?,?,?,?,?,?);",
//														PreparedStatement.RETURN_GENERATED_KEYS);
//										insertSchedulePstmt.setInt(1, gameId);
//										insertSchedulePstmt.setInt(2, gameTypeId);
//										insertSchedulePstmt.setInt(3, eventId);
//										insertSchedulePstmt.setTimestamp(4, drawDateTime);
//										insertSchedulePstmt.setTimestamp(5, freezeDateTime);
//										insertSchedulePstmt.setTimestamp(6, saleDateTime);
//										insertSchedulePstmt.setString(7, "INACTIVE");
//										insertSchedulePstmt.setInt(8, purchaseTableName);
//	
//										insertSchedulePstmt.executeUpdate();
//										ResultSet rsDraw = insertSchedulePstmt.getGeneratedKeys();
//										if (rsDraw.next()) {
//											int drawId = rsDraw.getInt(1);
//	
//											String merchantScheduleQuery = "select mm.merchant_id,draw_data from st_sl_scheduler_master sm inner join st_sl_schedular_merchant_mapping smm inner join st_sl_merchant_master mm inner join st_sl_game_type_merchant_mapping gtmm on sm.scheduler_id=smm.schedular_id and smm.merchant_id=mm.merchant_id and gtmm.merchant_id=smm.merchant_id and gtmm.game_type_id=sm.game_type_id where sm.game_id ="
//													+ gameId
//													+ " and sm.game_type_id="
//													+ gameTypeId
//													+ " and status='ACTIVE' and type_status='SALE_OPEN'";
//											Statement merchantScheduleStmt = con.createStatement();
//											ResultSet merchantSchduleRs = merchantScheduleStmt.executeQuery(merchantScheduleQuery);
//											while (merchantSchduleRs.next()) {
//												int merchantId = merchantSchduleRs.getInt("merchant_id");
//												String merchantDrawString = merchantSchduleRs.getString("draw_data");
//												JsonParser merchantScheduleparser = new JsonParser();
//												JsonObject merchantScheduleObj = (JsonObject) merchantScheduleparser.parse(merchantDrawString);
//	
//												JsonObject weeklyMerchantObj = merchantScheduleObj.get("WEEKLY").getAsJsonObject();
//												JsonObject merchantObj = weeklyMerchantObj.get(dayName).getAsJsonObject().get(mapId + "").getAsJsonObject();
//	
//												int claimStartDays = merchantObj.get("claimStartDays").getAsInt();
//												int clamiEndDays = merchantObj.get("claimEndDays").getAsInt();
//												int verificationDays = merchantObj.get("verificationDays").getAsInt();
//	
//												drawCal.add(Calendar.DAY_OF_YEAR, -claimStartDays);
//												Timestamp claimStartDateTime = new Timestamp(drawCal.getTimeInMillis());
//												drawCal.add(Calendar.DAY_OF_YEAR, claimStartDays);
//	
//												drawCal.add(Calendar.DAY_OF_YEAR, -clamiEndDays);
//												Timestamp claimEndDateTime = new Timestamp(drawCal.getTimeInMillis());
//												drawCal.add(Calendar.DAY_OF_YEAR, clamiEndDays);
//	
//												drawCal.add(Calendar.DAY_OF_YEAR, -verificationDays);
//												Timestamp verficationDateTime = new Timestamp(drawCal.getTimeInMillis());
//												drawCal.add(Calendar.DAY_OF_YEAR, verificationDays);
//	
//												PreparedStatement insertMerchantSchedule = con.prepareStatement("insert into st_sl_draw_merchant_mapping_?(draw_id,merchant_id,draw_name,draw_display_type,claim_start_date,claim_end_date,verification_date)values(?,?,?,?,?,?,?)");
//												insertMerchantSchedule.setInt(1, gameId);
//												insertMerchantSchedule.setInt(2, drawId);
//												insertMerchantSchedule.setInt(3, merchantId);
//												insertMerchantSchedule.setString(4, merchantObj.get("drawName").getAsString());
//												insertMerchantSchedule.setString(5, merchantObj.get("drawDisplayType").getAsString());
//												insertMerchantSchedule.setTimestamp(6, claimStartDateTime);
//												insertMerchantSchedule.setTimestamp(7, claimEndDateTime);
//												insertMerchantSchedule.setTimestamp(8, verficationDateTime);
//												int updateCount = insertMerchantSchedule.executeUpdate();
//	
//												String createTableNameQuery = "CREATE TABLE IF not EXISTS st_sl_draw_ticket_"
//														+ gameId
//														+ "_"
//														+ gameTypeId
//														+ "_"
//														+ purchaseTableName
//														+ tableQuery;
//												Statement createTableStmt = con.createStatement();
//												createTableStmt.executeUpdate(createTableNameQuery);
//											}
//										}
//									}
//								}
//							}
//						}
//					}
//				}
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
//		}
//	}
	
//	public void insertDraws(Connection con) throws SLEException {
//		Map<Integer, List<Integer>> map = null;
//		List<Integer> gameTypeList = null;
//		int gameTypeListSize = 0;
//		try {
//			map = new LinkedHashMap<Integer, List<Integer>>();
//			CommonMethodsDaoImpl.getInstance().fetchActiveGameAndGameType(map, con);
//
//			for (Entry<Integer, List<Integer>> entrySet : map.entrySet()) {
//				gameTypeList = entrySet.getValue();
//				gameTypeListSize = gameTypeList.size();
//				for (int iLoop = 0; iLoop < gameTypeListSize; iLoop++) {
//					insertDrawsGameAndGameTypeWise(entrySet.getKey(), gameTypeList.get(iLoop), con);
//				}
//			}
//		} catch (SLEException e) {
//			throw e;
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
//		}
//	}
	
	public synchronized void insertDrawsGameAndGameTypeWise(int gameId, int gameTypeId, Connection con) throws SLEException {
		Statement gameStmt = null;
		ResultSet gameRs = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		Statement drawStmt = null;
		ResultSet drawRs = null;
		int maxAdvanceDaysDraw = 60;
		int maxAdvanceDraws = 0;
		Timestamp lastDrawTime = null;
		try {
			gameStmt = con.createStatement();
			String gameQuery = "select SQL_CACHE gm.game_id, gtm.game_type_id, max_advance_draws, additional_draws, scheduler_draw_data, entry_per_table, draw_frequency, table_query from st_sl_game_master gm inner join st_sl_game_type_master gtm inner join st_sl_scheduler_master gsm on gm.game_id = gtm.game_id and gtm.game_type_id = gsm.game_type_id where gtm.game_id = "+gameId+" AND gtm.game_type_id = "+gameTypeId+" AND gm.game_status = 'SALE_OPEN' and gtm.type_status = 'SALE_OPEN' and scheduler_status = 'ACTIVE'";
			gameRs = gameStmt.executeQuery(gameQuery);
			if(gameRs.next())
				maxAdvanceDraws = gameRs.getInt("max_advance_draws");
				int additionalDraws = gameRs.getInt("additional_draws");
	
				String drawQuery = "select draw_datetime,draw_status from st_sl_draw_master_"
						+ gameId
						+ " where draw_datetime> '"
						+ DateTimeFormat.getCurrentTimeString()
						+ "' and game_type_id="
						+ gameTypeId
						+ " order by draw_datetime asc";
				drawStmt = con.createStatement();
				drawRs = drawStmt.executeQuery(drawQuery);
				int activeDraws = 0;
				int inactiveDraws = 0;
				while (drawRs.next()) {
					if ("ACTIVE".equals(drawRs.getString("draw_status"))) {
						activeDraws++;
					} else if ("INACTIVE".equals(drawRs.getString("draw_status"))) {
						inactiveDraws++;
					}
					lastDrawTime = drawRs.getTimestamp("draw_datetime");
				}
				if (lastDrawTime == null || lastDrawTime.before(new Timestamp(new Date().getTime()))) {
					lastDrawTime = DateTimeFormat.getCurrentTimeStamp();
				}
	
				if (maxAdvanceDraws > activeDraws || additionalDraws > inactiveDraws) {
					int insertDrawCount = maxAdvanceDraws + additionalDraws - activeDraws - inactiveDraws;
					int drawCount = 0;
					String drawFrequency = gameRs.getString("draw_frequency");
					String drawScheduleData = gameRs.getString("scheduler_draw_data");
					String tableQuery = gameRs.getString("table_query");
					int entryPerTable = gameRs.getInt("entry_per_table");
					if ("WEEKLY".equals(drawFrequency)) {
						JsonParser parser = new JsonParser();
						JsonObject drawScheduleObj = (JsonObject) parser.parse(drawScheduleData);
	
						JsonObject weeklyDrawObj = drawScheduleObj.get("WEEKLY").getAsJsonObject();
	
						Calendar cal = Calendar.getInstance();
						cal.setTimeInMillis(lastDrawTime.getTime());
						cal.add(Calendar.MINUTE, 2);
						cal.set(Calendar.SECOND, 0);
						int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
	
						// String currentDayName =new
						// SimpleDateFormat("EEE").format(cal.getTime());
						Map<Integer, String> dayMap = new HashMap<Integer, String>();
						dayMap.put(1, "SUN");
						dayMap.put(2, "MON");
						dayMap.put(3, "TUE");
						dayMap.put(4, "WED");
						dayMap.put(5, "THU");
						dayMap.put(6, "FRI");
						dayMap.put(0, "SAT");
	
						Calendar drawCal = Calendar.getInstance();
						drawCal.setTimeInMillis(lastDrawTime.getTime());
						boolean isBreak = false;
						for (int i = dayOfWeek; i < dayOfWeek + maxAdvanceDaysDraw; i++) {
							if (isBreak) {
								break;
							}
							if (i != dayOfWeek) {
								drawCal.add(Calendar.DAY_OF_YEAR, 1);
							}
	
							int dayWeek = i % 7;
							String dayName = dayMap.get(dayWeek);
	
							JsonElement dayDrawElement = weeklyDrawObj.get(dayName);
	
							if (dayDrawElement != null) {
								JsonArray dayDrawArr = dayDrawElement.getAsJsonArray();
								for (JsonElement drawElement : dayDrawArr) {
									if (drawCount == insertDrawCount) {
										PreparedStatement updateDrawPstmt = null;
										updateDrawPstmt = con.prepareStatement("update st_sl_draw_master_? aa inner join (select draw_id from st_sl_draw_master_? where draw_status in ('INACTIVE','ACTIVE') and draw_freeze_time> ? and game_type_id = ? order by draw_datetime asc limit ?,?) bb on aa.draw_id=bb.draw_id set draw_status='ACTIVE'");
										updateDrawPstmt.setInt(1, gameId);
										updateDrawPstmt.setInt(2, gameId);
										updateDrawPstmt.setTimestamp(3, DateTimeFormat.getCurrentTimeStamp());
										updateDrawPstmt.setInt(4, gameTypeId);
										updateDrawPstmt.setInt(5, 0);
										updateDrawPstmt.setInt(6, maxAdvanceDraws);
										updateDrawPstmt.executeUpdate();
	
										updateDrawPstmt = con.prepareStatement("update st_sl_draw_master_? aa inner join (select draw_id from st_sl_draw_master_? where draw_status in ('INACTIVE','ACTIVE') and draw_freeze_time> ? and game_type_id = ? order by draw_datetime asc limit ?,?) bb on aa.draw_id=bb.draw_id set draw_status='INACTIVE'");
										updateDrawPstmt.setInt(1, gameId);
										updateDrawPstmt.setInt(2, gameId);
										updateDrawPstmt.setTimestamp(3, DateTimeFormat.getCurrentTimeStamp());
										updateDrawPstmt.setInt(4, gameTypeId);
										updateDrawPstmt.setInt(5, maxAdvanceDraws);
										updateDrawPstmt.setInt(6, additionalDraws);
										updateDrawPstmt.executeUpdate();
										isBreak = true;
										break;
									}
	
									JsonObject drawObj = drawElement.getAsJsonObject();
	
									drawCal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(drawObj.get("drawTime").getAsString().substring(0, 2)));
									drawCal.set(Calendar.MINUTE, Integer.parseInt(drawObj.get("drawTime").getAsString().substring(2, 4)));
	
									Timestamp drawDateTime = new Timestamp(drawCal.getTimeInMillis());
									if (drawDateTime.compareTo(lastDrawTime) > 0) {
										drawCount++;
	
										int mapId = drawObj.get("mapId").getAsInt();
										int freezeTime = drawObj.get("freezeTime").getAsInt();
										int saleStartTime = drawObj.get("saleStartTime").getAsInt();
	
										drawCal.add(Calendar.SECOND, -freezeTime);
										Timestamp freezeDateTime = new Timestamp(drawCal.getTimeInMillis());
										drawCal.add(Calendar.SECOND, freezeTime);
	
										drawCal.add(Calendar.SECOND, -saleStartTime);
										Timestamp saleDateTime = new Timestamp(drawCal.getTimeInMillis());
										drawCal.add(Calendar.SECOND, saleStartTime);
	
										int eventId = 0;
										
										stmt = con.createStatement();
										rs = stmt.executeQuery("SELECT IFNULL(MAX(draw_no), 0) event_no FROM st_sl_draw_master_" + gameId);
										if(rs.next()) {
											eventId = rs.getInt("event_no");
										}
										eventId++;
										
										int purchaseTableName = getTableName(entryPerTable, gameId, gameTypeId, con);
	
										PreparedStatement insertSchedulePstmt = con.prepareStatement(
														"insert into st_sl_draw_master_?(game_type_id,draw_no,draw_datetime,draw_freeze_time,sale_start_time,draw_status,purchase_table_name)values(?,?,?,?,?,?,?);",
														PreparedStatement.RETURN_GENERATED_KEYS);
										insertSchedulePstmt.setInt(1, gameId);
										insertSchedulePstmt.setInt(2, gameTypeId);
										insertSchedulePstmt.setInt(3, eventId);
										insertSchedulePstmt.setTimestamp(4, drawDateTime);
										insertSchedulePstmt.setTimestamp(5, freezeDateTime);
										insertSchedulePstmt.setTimestamp(6, saleDateTime);
										insertSchedulePstmt.setString(7, "INACTIVE");
										insertSchedulePstmt.setInt(8, purchaseTableName);
	
										insertSchedulePstmt.executeUpdate();
										ResultSet rsDraw = insertSchedulePstmt.getGeneratedKeys();
										if (rsDraw.next()) {
											int drawId = rsDraw.getInt(1);
	
											String merchantScheduleQuery = "select mm.merchant_id,draw_data from st_sl_scheduler_master sm inner join st_sl_schedular_merchant_mapping smm inner join st_sl_merchant_master mm inner join st_sl_game_type_merchant_mapping gtmm on sm.scheduler_id=smm.schedular_id and smm.merchant_id=mm.merchant_id and gtmm.merchant_id=smm.merchant_id and gtmm.game_type_id=sm.game_type_id where sm.game_id ="
													+ gameId
													+ " and sm.game_type_id="
													+ gameTypeId
													+ " and status='ACTIVE' and type_status='SALE_OPEN'";
											Statement merchantScheduleStmt = con.createStatement();
											ResultSet merchantSchduleRs = merchantScheduleStmt.executeQuery(merchantScheduleQuery);
											while (merchantSchduleRs.next()) {
												int merchantId = merchantSchduleRs.getInt("merchant_id");
												String merchantDrawString = merchantSchduleRs.getString("draw_data");
												JsonParser merchantScheduleparser = new JsonParser();
												JsonObject merchantScheduleObj = (JsonObject) merchantScheduleparser.parse(merchantDrawString);
	
												JsonObject weeklyMerchantObj = merchantScheduleObj.get("WEEKLY").getAsJsonObject();
												JsonObject merchantObj = weeklyMerchantObj.get(dayName).getAsJsonObject().get(mapId + "").getAsJsonObject();
	
												int claimStartDays = merchantObj.get("claimStartDays").getAsInt();
												int clamiEndDays = merchantObj.get("claimEndDays").getAsInt();
												int verificationDays = merchantObj.get("verificationDays").getAsInt();
	
												drawCal.add(Calendar.DAY_OF_YEAR, -claimStartDays);
												Timestamp claimStartDateTime = new Timestamp(drawCal.getTimeInMillis());
												drawCal.add(Calendar.DAY_OF_YEAR, claimStartDays);
	
												drawCal.add(Calendar.DAY_OF_YEAR, -clamiEndDays);
												Timestamp claimEndDateTime = new Timestamp(drawCal.getTimeInMillis());
												drawCal.add(Calendar.DAY_OF_YEAR, clamiEndDays);
	
												drawCal.add(Calendar.DAY_OF_YEAR, -verificationDays);
												Timestamp verficationDateTime = new Timestamp(drawCal.getTimeInMillis());
												drawCal.add(Calendar.DAY_OF_YEAR, verificationDays);
	
												PreparedStatement insertMerchantSchedule = con.prepareStatement("insert into st_sl_draw_merchant_mapping_?(draw_id,merchant_id,draw_name,draw_display_type,claim_start_date,claim_end_date,verification_date)values(?,?,?,?,?,?,?)");
												insertMerchantSchedule.setInt(1, gameId);
												insertMerchantSchedule.setInt(2, drawId);
												insertMerchantSchedule.setInt(3, merchantId);
												insertMerchantSchedule.setString(4, merchantObj.get("drawName").getAsString());
												insertMerchantSchedule.setString(5, merchantObj.get("drawDisplayType").getAsString());
												insertMerchantSchedule.setTimestamp(6, claimStartDateTime);
												insertMerchantSchedule.setTimestamp(7, claimEndDateTime);
												insertMerchantSchedule.setTimestamp(8, verficationDateTime);
												int updateCount = insertMerchantSchedule.executeUpdate();
	
												String createTableNameQuery = "CREATE TABLE IF not EXISTS st_sl_draw_ticket_"
														+ gameId
														+ "_"
														+ gameTypeId
														+ "_"
														+ purchaseTableName
														+ tableQuery;
												Statement createTableStmt = con.createStatement();
												createTableStmt.executeUpdate(createTableNameQuery);
											}
										}
									}
								}
							}
						}
					}
				}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
	}
	
	private int getTableName(int noOfEntryPerTable, int gameId, int gameTypeId, Connection con) throws SQLException {
		Statement stmt = con.createStatement();
		int tableName = 0;
		String checkTableNameQuery = "select count(1)noOfTable,purchase_table_name from st_sl_draw_master_"
				+ gameId
				+ " where purchase_table_name !=0 and game_type_id="
				+ gameTypeId
				+ "  group by purchase_table_name order by purchase_table_name desc limit 1";
		ResultSet rs = stmt.executeQuery(checkTableNameQuery);
		if (rs.next()) {
			int noOfTable = rs.getInt("noOfTable");
			int purchaseTableName = rs.getInt("purchase_table_name");
			if (noOfTable < noOfEntryPerTable) {
				tableName = purchaseTableName;
			} else {
				tableName = purchaseTableName + 1;
			}
		} else {
			tableName = 1;
		}
		return tableName;
	}
	
	//Working And Used
	public void updateTicketCounter(Connection con) throws SLEException {
		Statement stmt = null;
		try {
			stmt = con.createStatement();
			stmt.executeUpdate("UPDATE st_sl_generate_ticketno SET ticket_count = 0");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
	}
	
//	public void fetchActiveGames(Map<Integer, Map<Integer, Integer>> tobeFreezedDrawData, Connection con) throws SLEException {
//		Statement stmt = null;
//		ResultSet rs = null;
//		Map<Integer, Integer> gameTypeIdDrawMap = null;
//		try {
//			stmt = con.createStatement();
//			rs = stmt.executeQuery("SELECT gm.game_id, gtm.game_type_id FROM st_sl_game_master gm INNER JOIN st_sl_game_type_master gtm ON gm.game_id = gtm.game_id ORDER BY gm.game_id, gtm.game_type_id;");
//			while (rs.next()) {
//				if(tobeFreezedDrawData.containsKey(rs.getInt("game_id"))) {
//					gameTypeIdDrawMap = tobeFreezedDrawData.get(rs.getInt("game_id"));
//				} else {
//					gameTypeIdDrawMap = new HashMap<Integer, Integer>();
//					tobeFreezedDrawData.put(rs.getInt("game_id"), gameTypeIdDrawMap);
//				}
//				gameTypeIdDrawMap.put(rs.getInt("game_type_id"), 0);
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
//		}
//	}
	
	public void fetchActiveGames(Map<Integer, List<Integer>> gameAndGameTypeData, Connection con) throws SLEException {
		Statement stmt = null;
		ResultSet rs = null;
		List<Integer> gameTypeIdList = null;
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT gm.game_id, gtm.game_type_id FROM st_sl_game_master gm INNER JOIN st_sl_game_type_master gtm ON gm.game_id = gtm.game_id ORDER BY gm.game_id, gtm.game_type_id;");
			while (rs.next()) {
				if(gameAndGameTypeData.containsKey(rs.getInt("game_id"))) {
					gameTypeIdList = gameAndGameTypeData.get(rs.getInt("game_id"));
				} else {
					gameTypeIdList = new ArrayList<Integer>();
					gameAndGameTypeData.put(rs.getInt("game_id"), gameTypeIdList);
				}
				gameTypeIdList.add(rs.getInt("game_type_id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
	}
	
	
//	public void fetchToBeFreezedDraws(Map<Integer, Map<Integer, Integer>> tobeFreezedDrawData, Connection con) throws SLEException {
//		Statement stmt = null;
//		ResultSet rs = null;
//		
//		Map<Integer, Integer> gameTypeIdDrawMap = null;
//		int gameId = 0;
//		try {
//			stmt = con.createStatement();
//			for(Entry<Integer, Map<Integer, Integer>> entrySet : tobeFreezedDrawData.entrySet()) {
//				gameId = entrySet.getKey();
//				gameTypeIdDrawMap = entrySet.getValue();
//				
//				
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//	}
	
	public void createDrawFreezeJob(List<Integer> activeGameIds, Connection con) throws SLEException {
		
	}
	
}
