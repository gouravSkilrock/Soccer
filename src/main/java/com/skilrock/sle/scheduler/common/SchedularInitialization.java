package com.skilrock.sle.scheduler.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.transaction.UserTransaction;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.skilrock.sle.common.DBConnect;
import com.skilrock.sle.common.DateTimeFormat;
import com.skilrock.sle.common.SLELogger;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.scheduler.controllerImpl.SLESchedulerControllerImpl;

public class SchedularInitialization {
	private static final SLELogger logger = SLELogger.getLogger(SchedularInitialization.class.getName()); 

	public void startDrawMgmtScheduler() {
		Connection con = null;
		Statement gameStmt = null;
		Statement stmt = null;
		ResultSet rs = null;
		ResultSet gameRs = null;

		Statement drawStmt = null;
		ResultSet drawRs = null;
		int maxAdvanceDaysDraw = 60;
		
		UserTransaction userTxn = null;

		try {
			updateFreezeTimeStatus();
			Timestamp lastDrawTime = null;
			con = DBConnect.getConnection();
			userTxn = DBConnect.startTransaction();
//			con = DBConnect.getPropFileCon();
//			con.setAutoCommit(false);
			gameStmt = con.createStatement();
			String gameQuery = "select SQL_CACHE gm.game_id, gtm.game_type_id, max_advance_draws, additional_draws, scheduler_draw_data, entry_per_table, draw_frequency, table_query from st_sl_game_master gm inner join st_sl_game_type_master gtm inner join st_sl_scheduler_master gsm on gm.game_id = gtm.game_id and gtm.game_type_id = gsm.game_type_id where gm.game_status = 'SALE_OPEN' and gtm.type_status = 'SALE_OPEN' and scheduler_status = 'ACTIVE'";
			gameRs = gameStmt.executeQuery(gameQuery);
			while (gameRs.next()) {
				lastDrawTime = null;
				int gameId = gameRs.getInt("game_id");
				int gameTypeId = gameRs.getInt("game_type_id");
				int maxAdvanceDraws = gameRs.getInt("max_advance_draws");
				int additionalDraws = gameRs.getInt("additional_draws");

				String drawQuery = "select draw_datetime, draw_status from st_sl_draw_master_"
						+ gameId
						+ " where draw_datetime > '"
						+ DateTimeFormat.getCurrentTimeString()
						+ "' and game_type_id = "
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
										updateDrawPstmt = con.prepareStatement("update st_sl_draw_master_? aa inner join (select draw_id from st_sl_draw_master_? where draw_status in ('INACTIVE','ACTIVE') and draw_freeze_time> ? and game_type_id = ? order by draw_datetime asc limit ?,?) bb on aa.draw_id = bb.draw_id set draw_status = 'ACTIVE'");
										updateDrawPstmt.setInt(1, gameId);
										updateDrawPstmt.setInt(2, gameId);
										updateDrawPstmt.setTimestamp(3, DateTimeFormat.getCurrentTimeStamp());
										updateDrawPstmt.setInt(4, gameTypeId);
										updateDrawPstmt.setInt(5, 0);
										updateDrawPstmt.setInt(6, maxAdvanceDraws);
										updateDrawPstmt.executeUpdate();

										updateDrawPstmt = con.prepareStatement("update st_sl_draw_master_? aa inner join (select draw_id from st_sl_draw_master_? where draw_status in ('INACTIVE','ACTIVE') and draw_freeze_time> ? and game_type_id = ? order by draw_datetime asc limit ?,?) bb on aa.draw_id = bb.draw_id set draw_status = 'INACTIVE'");
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
									drawCal.set(Calendar.SECOND, 0);

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
														"insert into st_sl_draw_master_?(game_type_id, draw_no, draw_datetime, draw_freeze_time, sale_start_time, draw_status, purchase_table_name)values(?,?,?,?,?,?,?);",
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

											String merchantScheduleQuery = "SELECT mm.merchant_id, draw_data FROM st_sl_scheduler_master sm INNER JOIN st_sl_schedular_merchant_mapping smm inner join st_sl_merchant_master mm inner join st_sl_game_type_merchant_mapping gtmm on sm.scheduler_id = smm.schedular_id and smm.merchant_id = mm.merchant_id and gtmm.merchant_id = smm.merchant_id and gtmm.game_type_id = sm.game_type_id where sm.game_id = "
													+ gameId
													+ " AND sm.game_type_id = "
													+ gameTypeId
													+ " AND status='ACTIVE' AND type_status='SALE_OPEN'";
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

												PreparedStatement insertMerchantSchedule = con.prepareStatement("insert into st_sl_draw_merchant_mapping_?(draw_id, merchant_id, draw_name, draw_display_type, claim_start_date, claim_end_date, verification_date)values(?,?,?,?,?,?,?)");
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
			}
//			con.commit();
			userTxn.commit();
			DBConnect.closeConnection(con);
			new SLESchedulerControllerImpl().createDrawFreezeJob();
//			updateFreezeTimeStatus();
		} catch (Exception e) {
			e.printStackTrace();
			DBConnect.rollBackUserTransaction(userTxn);
		} 
	}

	private int getTableName(int noOfEntryPerTable, int gameId, int gameTypeId, Connection con) throws SQLException {
		Statement stmt = con.createStatement();
		int tableName = 0;
		String checkTableNameQuery = "select count(1)noOfTable, purchase_table_name from st_sl_draw_master_"
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

	public void updateFreezeTimeStatus() throws SLEException {
		Statement gameStmt = null;
		Connection con = null;
		ResultSet gameRs = null;
		Statement updateStatus = null;
		try {
			con = DBConnect.getConnection();
//			con = DBConnect.getPropFileCon();
			gameStmt = con.createStatement();
			String gameQuery = "select SQL_CACHE game_id from st_sl_game_master where game_status='SALE_OPEN'";
			gameRs = gameStmt.executeQuery(gameQuery);
			while (gameRs.next()) {
				int gameId = gameRs.getInt("game_id");

				updateStatus = con.createStatement();
				String updateFreezeQry = "update st_sl_draw_master_" + gameId
						+ " set draw_status='FREEZE' where draw_freeze_time<='"
						+ DateTimeFormat.getCurrentTimeString()
						+ "' and draw_status='ACTIVE'";
				int updateCount = updateStatus.executeUpdate(updateFreezeQry);
				logger.info("draw status Change "+updateCount);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(con);
		}

	}

	public void updateFreezeTimeStatus(int gameId) throws SLEException{
		Connection con = null;
		Statement updateStatus = null;
		logger.info("***** Inside updateFreezeTimeStatus Method");
		try {
			con = DBConnect.getConnection();
//			con = DBConnect.getPropFileCon();
			updateStatus = con.createStatement();
			String updateFreezeQry = "UPDATE st_sl_draw_master_" + gameId
					+ " set draw_status = 'FREEZE' WHERE draw_freeze_time <= '"
					+ DateTimeFormat.getCurrentTimeString()
					+ "' AND draw_status='ACTIVE'";
			logger.info("Query For Updating Draw Status "+updateFreezeQry);
			int updateCount = updateStatus.executeUpdate(updateFreezeQry);
			logger.info("Freezed Draw Count "+updateCount);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(con);
		}
	}
	
	public void sendDrawNotficationToUser(){
		try{
			new SLESchedulerControllerImpl().createSendDrawNotificationJob();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new SchedularInitialization().sendDrawNotficationToUser();
	}

}
