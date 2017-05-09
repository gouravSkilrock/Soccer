package com.skilrock.sle.drawMgmt.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.skilrock.sle.common.DBConnect;
import com.skilrock.sle.common.SLELogger;
import com.skilrock.sle.common.SportsLotteryUtils;
import com.skilrock.sle.common.TransactionManager;
import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.common.javaBeans.UserInfoBean;
import com.skilrock.sle.drawMgmt.javaBeans.DrawInfoBean;
import com.skilrock.sle.drawMgmt.javaBeans.DrawInfoMerchantWiseBean;
import com.skilrock.sle.drawMgmt.javaBeans.SlePwtBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.MappedEventMasterBean;
import com.skilrock.sle.gamePlayMgmt.javaBeans.EventDetailBean;
import com.skilrock.sle.pwtMgmt.javaBeans.TicketInfoBean;

public class DrawMgmtDaoImpl {
	private static final SLELogger logger = SLELogger.getLogger(DrawMgmtDaoImpl.class.getName());

	private static volatile DrawMgmtDaoImpl classInstance;

	public static DrawMgmtDaoImpl getInstance() {
		if (classInstance == null) {
			synchronized (DrawMgmtDaoImpl.class) {
				if (classInstance == null) {
					classInstance = new DrawMgmtDaoImpl();
				}
			}
		}
		return classInstance;
	}

	public SlePwtBean getSportsLotteryDrawData(SlePwtBean slePwtBean,Connection con){
		int drawId;
		int gameId;
		int merchantId;
		String pwtQuery = null;
		Statement stmt = null;
		EventDetailBean eventDetailBean = null;
		ArrayList<EventDetailBean> eventDetailList = null;
		StringBuilder sb = new StringBuilder();
		try {
			drawId = slePwtBean.getDrawId();
			gameId = slePwtBean.getGameId();
			slePwtBean.setMerchantId(TransactionManager.getMerchantId());
			merchantId = slePwtBean.getMerchantId();
			pwtQuery = "select srs.draw_id,sdm.game_type_id,sdmm.draw_name,sdm.draw_datetime,DATE(sdm.draw_datetime)draw_date,TIME(sdm.draw_datetime)draw_time,DATE(sdm.draw_freeze_time)draw_freeze_date,TIME(sdm.draw_freeze_time)draw_freeze_time,sdm.draw_status,srs.user1_result,srs.bo_result,srs.status from st_sl_draw_master_"	+ gameId+ " sdm INNER JOIN st_sl_result_submission_user_mapping srs on sdm.draw_id=srs.draw_id INNER JOIN st_sl_draw_merchant_mapping_"	+ gameId+ " sdmm on sdmm.draw_id=srs.draw_id and merchant_id="	+ merchantId+ " where sdm.draw_id="+ drawId	+ " and srs.status IN ('MATCHED','RESOLVED');";
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(pwtQuery.toString());

			if (rs.next()) {
				slePwtBean.setDrawName(rs.getString("draw_name"));
				slePwtBean.setDrawDate(rs.getString("draw_date"));
				slePwtBean.setDrawTime(rs.getString("draw_time"));
				slePwtBean.setDrawDateTime(Util.convertDateTimeToResponseFormat2(rs.getString("draw_datetime")));
				slePwtBean.setDrawFreezeDate(rs.getString("draw_freeze_date"));
				slePwtBean.setDrawFreezeTime(rs.getString("draw_freeze_time"));
				slePwtBean.setDrawStatus(rs.getString("draw_status"));
				if ("MATCHED".equalsIgnoreCase(rs.getString("status"))) {
					slePwtBean.setDrawResult(rs.getString("user1_result"));
				} else {
					slePwtBean.setDrawResult(rs.getString("bo_result"));
				}
				slePwtBean.setGameTypeId(rs.getInt("game_type_id"));
			}

			String eventArr[] = slePwtBean.getDrawResult().split(",");
			eventDetailList = new ArrayList<EventDetailBean>();
			for (int i = 0; i <= eventArr.length - 1; i++) {
				eventDetailBean = new EventDetailBean();
				String eventData[] = eventArr[i].split("-");
				eventDetailBean.setEventId(Integer.parseInt(eventData[0].trim()));
				eventDetailBean.setOptionName(eventData[1].trim());
				eventDetailBean.setEventDisplay(getEventNameFromEventId(Integer.parseInt(eventData[0].trim()), con));
				eventDetailList.add(eventDetailBean);
			}
			String gameTypeName = SportsLotteryUtils.gameTypeInfoMerchantMap.get("PMS").get(slePwtBean.getGameTypeId()).getGameTypeDevName();
			for (EventDetailBean evtBean : eventDetailList) {
				if ("soccer4".equalsIgnoreCase(gameTypeName) || "soccer6".equalsIgnoreCase(gameTypeName)) {
					if ("HOME(+2)".equalsIgnoreCase(evtBean.getOptionName().trim())) {
						evtBean.setOptionName("H+");
					} else if ("HOME".equalsIgnoreCase(evtBean.getOptionName().trim())) {
						evtBean.setOptionName("H");
					} else if ("DRAW".equalsIgnoreCase(evtBean.getOptionName().trim())) {
						evtBean.setOptionName("D");
					} else if ("AWAY".equalsIgnoreCase(evtBean.getOptionName().trim())) {
						evtBean.setOptionName("A");
					} else if ("AWAY(+2)".equalsIgnoreCase(evtBean.getOptionName().trim())) {
						evtBean.setOptionName("A+");
					} else if ("CANCEL".equalsIgnoreCase(evtBean.getOptionName().trim())) {
						evtBean.setOptionName("C");
					}
				}
				sb.append(evtBean.getEventDisplay()).append(" --- ").append(evtBean.getOptionName()).append("\n");
			}
			if (!sb.toString().isEmpty())
				slePwtBean.setWinningResult(sb.substring(0, sb.toString().length() - 1));
			slePwtBean.setEventDetailList(eventDetailList);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		finally {

		}
		return slePwtBean;
	}

	public String getEventNameFromEventId(int eventId, Connection con) {
		String eventQuery = null;
		Statement stmt = null;
		String eventName = null;
		eventQuery = "select event_display from st_sl_event_master where event_id="	+ eventId + "";
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(eventQuery.toString());
			if (rs.next()) {
				eventName = rs.getString("event_display");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return eventName;
	}

	public void fetchWinningAmountDateAndDrawWise(List<DrawInfoMerchantWiseBean> drawInfoMerchantWiseBeans,String startDate, String endDate, int gameId, int gameTypeId,String merchantCode, Connection con) throws SLEException {
		logger.info("***** Inside fetchWinningAmountDateAndDrawWise Method");
		Statement drawResultStmt = null;
		ResultSet drawResultSet = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuilder query = new StringBuilder();
		int merchantId = 0;
		DrawInfoMerchantWiseBean drawInfoMerchantWiseBean = null;
		try {
			stmt = con.createStatement();
			query.append("SELECT merchant_id FROM st_sl_merchant_master WHERE merchant_dev_name = '").append(merchantCode).append("';");
			rs = stmt.executeQuery(query.toString());
			if (rs.next()) {
				merchantId = rs.getInt("merchant_id");
			} else {
				throw new SLEException(SLEErrors.MERCHANT_NOT_AVAILABLE_ERROR_CODE,SLEErrors.MERCHANT_NOT_AVAILABLE_ERROR_MESSAGE);
			}

			query.setLength(0);

			query.append(
				"SELECT dm.draw_id, dm.purchase_table_name, dm.draw_datetime, dm.draw_freeze_time, dmm.draw_name, dm.draw_status FROM st_sl_draw_master_")
				.append(gameId)
				.append(" dm INNER JOIN st_sl_draw_merchant_mapping_")
				.append(gameId)
				.append(" dmm ON dm.draw_id = dmm.draw_id WHERE dm.game_type_id  = ")
				.append(gameTypeId)
				.append(" AND dm.draw_status = 'CLAIM ALLOW' AND dm.draw_datetime >= ' ")
				.append(startDate).append("' AND dm.draw_datetime <= '")
				.append(endDate).append("' AND dmm.merchant_id = ")
				.append(merchantId).append(";");
			// query.append("SELECT draw_id, purchase_table_name FROM st_sl_draw_master_").append(gameId).append("WHERE game_type_id = ").append(gameTypeId).append(" AND draw_status = 'CLAIM ALLOW' AND draw_datetime >= '").append(startDate).append("' AND draw_datetime <= '").append(endDate).append("';");
			drawResultStmt = con.createStatement();
			drawResultSet = drawResultStmt.executeQuery(query.toString());

			while (drawResultSet.next()) {
				query.setLength(0);

				query.append(
					"select ticket_number,sum(drawTkt.unit_price*drawTkt.bet_amount_multiple*przDetail.prize_amount)/przDetail.unit_price winAmt from st_sl_draw_ticket_")
					.append(gameId)
					.append("_")
					.append(gameTypeId)
					.append("_")
					.append(drawResultSet.getInt("purchase_table_name"))
					.append(" drawTkt inner join st_sl_prize_details_")
					.append(gameId)
					.append("_")
					.append(gameTypeId)
					.append(" przDetail on drawTkt.rank_id = przDetail.prize_rank and drawTkt.merchant_id=przDetail.merchant_id where przDetail.draw_id = ")
					.append(drawResultSet.getInt("draw_id"))
					.append(" and status != 'CANCELLED' and drawTkt.rank_id != 0 and przDetail.merchant_id = ").append(merchantId).append(" GROUP BY ticket_number;");
				rs = stmt.executeQuery(query.toString());
				drawInfoMerchantWiseBean = new DrawInfoMerchantWiseBean();

				drawInfoMerchantWiseBean.setDrawId(drawResultSet.getInt("draw_id"));
				drawInfoMerchantWiseBean.setGameId(gameId);
				drawInfoMerchantWiseBean.setGameTypeId(gameTypeId);
				drawInfoMerchantWiseBean.setDrawDateTime(drawResultSet.getString("draw_datetime"));
				if (drawResultSet.getString("draw_name") == null)
					drawInfoMerchantWiseBean.setDrawName("NA");
				else
					drawInfoMerchantWiseBean.setDrawName(drawResultSet.getString("draw_name"));
				drawInfoMerchantWiseBean.setDrawStatus(drawResultSet.getString("draw_status"));
				drawInfoMerchantWiseBean.setFreezeTime(drawResultSet.getString("draw_freeze_time"));
				List<TicketInfoBean> ticketPwtBeanList =new ArrayList<TicketInfoBean>();
				while (rs.next()) {
					TicketInfoBean bean=new TicketInfoBean();
					bean.setTicketNo(rs.getLong("ticket_number"));
					bean.setTotalWinningAmt(rs.getDouble("winAmt"));
					ticketPwtBeanList.add(bean);
				}
				drawInfoMerchantWiseBean.setTicketPwtBeanList(ticketPwtBeanList);
				drawInfoMerchantWiseBeans.add(drawInfoMerchantWiseBean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE,SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
	}

	public List<DrawInfoBean> fetchDrawMgmtDrawInfo(int gameId, int gameTypeId,	String startDate, String endDate, int merchantId, Connection con)throws SLEException {
		Statement stmt = null;
		List<DrawInfoBean> drawInfoList = null;
		ResultSet rs = null;
		String drawInfoQuery = null;
		try {
			logger.info("Inside fetchDrawMgmtDrawInfo method");
			drawInfoList = new LinkedList<DrawInfoBean>();
			drawInfoQuery = "SELECT  mas.draw_id,mas.draw_no ,draw_name, draw_datetime, draw_freeze_time, draw_status, sale_start_time  FROM st_sl_draw_master_"+ gameId+ " mas INNER JOIN st_sl_draw_merchant_mapping_"+ gameId+ " map ON mas.draw_id=map.draw_id WHERE game_type_id="	+ gameTypeId+ " AND merchant_id="+ merchantId+ " AND draw_datetime>='"+ startDate	+ "' AND draw_datetime<='"+ endDate+ "' AND draw_status IN('ACTIVE','INACTIVE') ORDER BY draw_datetime ASC;";
			stmt = con.createStatement();
			logger.info("fetchDrawMgmtDrawInfo Query" + drawInfoQuery);
			rs = stmt.executeQuery(drawInfoQuery);
			while (rs.next()) {
				DrawInfoBean drawInfoBean = new DrawInfoBean();
				drawInfoBean.setDrawId(rs.getInt("draw_id"));
				drawInfoBean.setDrawNo(rs.getInt("draw_no"));
				drawInfoBean.setDrawName(rs.getString("draw_name"));
				drawInfoBean.setDrawStatus(rs.getString("draw_status"));
				drawInfoBean.setDrawFreezeTime(rs.getString("draw_freeze_time").replace(".0", ""));
				drawInfoBean.setDrawDateTime(rs.getString("draw_datetime").replace(".0", ""));
				drawInfoBean.setSaleStartTime(rs.getString("sale_start_time").replace(".0", ""));
				drawInfoList.add(drawInfoBean);
			}
			if (drawInfoList.isEmpty()) {
				throw new SLEException(SLEErrors.NO_RECORD_FOUND_ERROR_CODE,SLEErrors.NO_RECORD_FOUND_ERROR_MESSAGE);
			}
		} catch (SLEException e) {
			throw e;
		} catch (Exception e) {
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(stmt, rs);
		}
		return drawInfoList;
	}

	public DrawInfoBean fetchDrawInfo(int gameId, int gameTypeId, int drawId,int merchantId, Connection con) throws SLEException {
		Statement stmt = null;
		DrawInfoBean drawInfoBean = new DrawInfoBean();
		ResultSet rs = null;
		String drawInfoQuery = null;
		try {
			logger.info("Inside fetchDrawInfo method");
			drawInfoQuery = "SELECT  mas.draw_id,mas.draw_no ,draw_name, draw_datetime, draw_freeze_time, draw_status, sale_start_time  FROM st_sl_draw_master_"+ gameId+ " mas INNER JOIN st_sl_draw_merchant_mapping_"+ gameId+ " map ON mas.draw_id=map.draw_id WHERE game_type_id="	+ gameTypeId+ " AND merchant_id="+ merchantId+ " AND mas.draw_id=" + drawId + ";";
			stmt = con.createStatement();
			logger.info("fetchDrawInfo Query" + drawInfoQuery);
			rs = stmt.executeQuery(drawInfoQuery);
			if (rs.next()) {
				drawInfoBean.setDrawId(rs.getInt("draw_id"));
				drawInfoBean.setDrawNo(rs.getInt("draw_no"));
				drawInfoBean.setDrawName(rs.getString("draw_name"));
				drawInfoBean.setDrawStatus(rs.getString("draw_status"));
				drawInfoBean.setDrawFreezeTime(rs.getString("draw_freeze_time").replace(".0", ""));
				drawInfoBean.setDrawDateTime(rs.getString("draw_datetime").replace(".0", ""));
				drawInfoBean.setSaleStartTime(rs.getString("sale_start_time").replace(".0", ""));
			}
		} catch (Exception e) {
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(stmt, rs);
		}
		return drawInfoBean;
	}

	public DrawInfoBean fetchDrawInfoForValidation(int gameId, int gameTypeId,int drawId, int merchantId, Connection con) throws SLEException {
		Statement stmt = null;
		DrawInfoBean drawInfoBean = new DrawInfoBean();
		ResultSet rs = null;
		String drawInfoQuery = null;
		try {
			logger.info("Inside fetchDrawInfoForValidation method");
			drawInfoQuery = "SELECT  drawInfo.draw_id,draw_name, draw_datetime, draw_freeze_time, draw_status, sale_start_time,min_evt_start_time   FROM (SELECT  mas.draw_id,mas.draw_no ,draw_name, draw_datetime, draw_freeze_time, draw_status, sale_start_time  FROM st_sl_draw_master_"+ gameId+ " mas INNER JOIN st_sl_draw_merchant_mapping_"+ gameId+ " map ON mas.draw_id=map.draw_id WHERE game_type_id="+ gameTypeId+ " AND merchant_id="+ merchantId+ " AND mas.draw_id="+ drawId+ ") drawInfo INNER JOIN (SELECT  "+ drawId+ " draw_id, min(start_time) min_evt_start_time from st_sl_draw_event_mapping_"+ gameId+ " dem INNER JOIN st_sl_event_master em on em.event_id=dem.event_id where draw_id="+ drawId + ") evtInfo ON drawInfo.draw_id=evtInfo.draw_id;";
			stmt = con.createStatement();
			rs = stmt.executeQuery(drawInfoQuery);
			if (rs.next()) {
				drawInfoBean.setDrawId(rs.getInt("draw_id"));
				drawInfoBean.setDrawName(rs.getString("draw_name"));
				drawInfoBean.setDrawStatus(rs.getString("draw_status"));
				drawInfoBean.setDrawFreezeTime(rs.getString("draw_freeze_time").replace(".0", ""));
				drawInfoBean.setDrawDateTime(rs.getString("draw_datetime").replace(".0", ""));
				drawInfoBean.setSaleStartTime(rs.getString("sale_start_time").replace(".0", ""));
				drawInfoBean.setMinEventStartTime(rs.getString("min_evt_start_time") == null ? null : rs.getString("min_evt_start_time").replace(".0", ""));
			}
		} catch (Exception e) {
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(stmt, rs);
		}
		return drawInfoBean;
	}
	public List<String> getOtherDrawFreezeDataForValidation(int gameId,int gameTypeId,DrawInfoBean drawInfoBean,Connection con) throws SLEException{
		PreparedStatement pstmt = null;
		List<String> drawFreezeTimeListToValidate=null;
		ResultSet rs=null;
		
		try {
			logger.info("Inside getOtherDrawDataForValidation method");
			pstmt = con.prepareStatement("SELECT draw_freeze_time FROM (SELECT draw_freeze_time FROM st_sl_draw_master_? WHERE draw_freeze_time < ? AND game_type_id=?  ORDER BY draw_freeze_time DESC LIMIT 1) preTB UNION ALL SELECT draw_freeze_time FROM (select draw_freeze_time FROM st_sl_draw_master_? WHERE draw_freeze_time > ? AND game_type_id=? AND draw_status IN ('ACTIVE','INACTIVE') ORDER BY draw_freeze_time  LIMIT 1) aftTB");
			pstmt.setInt(1, gameId);
			pstmt.setString(2, drawInfoBean.getDrawFreezeTime());
			pstmt.setInt(3, gameTypeId);
			pstmt.setInt(4, gameId);
			pstmt.setString(5, drawInfoBean.getDrawFreezeTime());
			pstmt.setInt(6, gameTypeId);

			rs=pstmt.executeQuery();
			drawFreezeTimeListToValidate=new LinkedList<String>();
			while (rs.next()) {
				drawFreezeTimeListToValidate.add(rs.getString("draw_freeze_time").replace(".0", ""));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}finally{
			DBConnect.closePstmt(pstmt);
		}
		return drawFreezeTimeListToValidate;

	}
	
	
	public List<String> getOtherDrawSaleDataForValidation(int gameId,int gameTypeId,DrawInfoBean drawInfoBean,Connection con) throws SLEException{
		PreparedStatement pstmt = null;
		List<String> drawSaleTimeListToValidate=null;
		ResultSet rs=null;
		
		try {
			logger.info("Inside getOtherDrawDataForValidation method");
			pstmt = con.prepareStatement("SELECT sale_start_time FROM (SELECT sale_start_time FROM st_sl_draw_master_? WHERE sale_start_time < ? AND game_type_id=?  ORDER BY sale_start_time DESC LIMIT 1) preTB UNION ALL SELECT sale_start_time FROM (select sale_start_time FROM st_sl_draw_master_? WHERE sale_start_time > ? AND game_type_id=? AND draw_status IN ('ACTIVE','INACTIVE') ORDER BY sale_start_time  LIMIT 1) aftTB");
			pstmt.setInt(1, gameId);
			pstmt.setString(2, drawInfoBean.getSaleStartTime());
			pstmt.setInt(3, gameTypeId);
			pstmt.setInt(4, gameId);
			pstmt.setString(5, drawInfoBean.getSaleStartTime());
			pstmt.setInt(6, gameTypeId);

			rs=pstmt.executeQuery();
			drawSaleTimeListToValidate=new LinkedList<String>();
			while (rs.next()) {
				drawSaleTimeListToValidate.add(rs.getString("sale_start_time").replace(".0", ""));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}finally{
			DBConnect.closePstmt(pstmt);
		}
		return drawSaleTimeListToValidate;

	}
	

	public void updateDrawFreezeTimeAndHistory(int gameId,int gameTypeId,DrawInfoBean drawInfoBean,int merchantId,int merchantUserId,Connection con) throws SLEException{
		PreparedStatement pstmt = null;
		int isUpdated = 0;
		try {
			logger.info("Inside updateDrawInfoAndHistory method");
			con.setAutoCommit(false);
			pstmt = con.prepareStatement("UPDATE st_sl_draw_master_? SET draw_freeze_time=? WHERE draw_id=?");
			pstmt.setInt(1, gameId);
			pstmt.setString(2, drawInfoBean.getUpdatedDrawFreezeTime());
			pstmt.setInt(3, drawInfoBean.getDrawId());

			logger.info("update draw freeze Time Query" + pstmt);
			isUpdated = pstmt.executeUpdate();
			if (isUpdated == 0) {
				throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			}

			//update Draw History table
			insertDrawDetailsChangeHistory(gameId,gameTypeId,drawInfoBean,merchantId,merchantUserId,"CHANGE FREEZE TIME",con);
			con.commit();
		} catch (SLEException e) {
			throw e;
		}catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}finally{
			DBConnect.closePstmt(pstmt);
		}

	}

	public void  updateDrawSaleTimeAndHistory(int gameId,int gameTypeId,DrawInfoBean drawInfoBean,int merchantId,int merchantUserId,Connection con) throws SLEException{
		PreparedStatement pstmt=null;
		int isUpdated=0;
		try{
			logger.info("Inside updateDrawSaleTimeAndHistory method");			
			con.setAutoCommit(false);
			pstmt=con.prepareStatement("UPDATE st_sl_draw_master_? SET sale_start_time=? WHERE draw_id=?");
			pstmt.setInt(1, gameId);
			pstmt.setString(2, drawInfoBean.getUpdatedSaleStartTime());
			pstmt.setInt(3,drawInfoBean.getDrawId());

			logger.info("update draw sale Time Query"+pstmt);
			isUpdated = pstmt.executeUpdate();
			if (isUpdated == 0) {
				throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			}
			
			//update Draw History table
			insertDrawDetailsChangeHistory(gameId,gameTypeId,drawInfoBean,merchantId,merchantUserId,"CHANGE SALE TIME",con);
			con.commit();
		} catch (SLEException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closePstmt(pstmt);
		}

	}

	
	public void insertDrawDetailsChangeHistory(int gameId,int gameTypeId,DrawInfoBean drawInfoBean,int merchantId,int metchantUserId,String action,Connection con) throws SLEException{
		PreparedStatement pstmt=null;
		int isUpdated=0;
		try{
			pstmt=con.prepareStatement("INSERT INTO st_sl_draw_details_change_history(draw_id,game_id,game_type_id,merchant_id,user_id,action,date,remarks) VALUES (?,?,?,?,?,?,?,?);");
			
			pstmt.setInt(1, drawInfoBean.getDrawId());
			pstmt.setInt(2, gameId);
			pstmt.setInt(3,gameTypeId);
			pstmt.setInt(4, merchantId);
			pstmt.setInt(5, metchantUserId);
			pstmt.setString(6,action);
			pstmt.setString(7, Util.getCurrentTimeString());
			if("CHANGE SALE TIME".equals(action)){
				pstmt.setString(8, action+" "+drawInfoBean.getSaleStartTime()+" to "+drawInfoBean.getUpdatedSaleStartTime()+"");
			}else{
				pstmt.setString(8, action+" "+drawInfoBean.getDrawFreezeTime()+" to "+drawInfoBean.getUpdatedDrawFreezeTime()+"");
			}
			
			
			logger.info("update "+action+" Time History Query"+pstmt);
			isUpdated=pstmt.executeUpdate();
			if(isUpdated==0){
				throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			}
		}catch (Exception e) {
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
	}
	
	
	public List<String> getNextDrawData(int gameId, int gameTypeId, int drawId,Connection con) throws SLEException {
		List<String> returnList = new ArrayList<String>(2);
		Timestamp dateTime = null;
		String cronExp = null;
		ResultSet rs = null;
		Statement stmt = null;
		try {
			logger.info("Inside getNextDrawData method ");
			stmt = con.createStatement();
			rs = stmt.executeQuery("select draw_freeze_time, draw_id from st_sl_draw_master_" + gameId + " where game_type_id = " + gameTypeId + " AND draw_id = " + drawId);
			if (rs.next()) {
				dateTime = rs.getTimestamp("draw_freeze_time");
				logger.info("Next Draw Freeze Time for GameId " + gameId + " , gameTypeId " + gameTypeId + " and drawId is " + rs.getInt("draw_id"));

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
			logger.info("Cron Expression is " + cronExp);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE,SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(stmt, rs);
		}
		return returnList;
	}

	public void addLeagues(Connection con, int gameId, String leagueName) throws SLEException {
		int isUpdated = 0;
		Statement stm = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String leagueType = null;
		try {
			stm = con.createStatement();
			rs = stm.executeQuery("Select game_dev_name from st_sl_game_master where game_id="+gameId);
			while (rs.next()) {
				leagueType = rs.getString("game_dev_name");
			}
			pstmt = con.prepareStatement("INSERT INTO st_sl_league_master(league_type,league_dev_name,league_display_name,status) VALUES (?,?,?,?);");
			pstmt.setString(1, leagueType);
			pstmt.setString(2, leagueName);
			pstmt.setString(3, leagueName);
			pstmt.setString(4, "ACTIVE");
			isUpdated=pstmt.executeUpdate();
			if (isUpdated == 0) {
				throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			}
		}catch (SLEException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			DBConnect.closeConnection(pstmt, stm, rs);
		}
	}

	public void addTeams(Connection con, int gameId, String teamName,String teamCode) throws SLEException {
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
		int cityId=0;
		int isUpdated1=0;
		int isUpdated2=0;
		try {
			pstmt1 = con.prepareStatement("INSERT INTO st_sl_game_team_master(game_id,team_name,team_code,status) VALUES (?,?,?,?);");
			pstmt2 = con.prepareStatement("INSERT INTO st_sl_venue_master(venue_display_name,venue_code,city_id,status) VALUES (?,?,?,?);");
			pstmt1.setInt(1, gameId);
			pstmt1.setString(2, teamName);
			pstmt1.setString(3, teamCode);
			pstmt1.setString(4, "ACTIVE");
			isUpdated1=pstmt1.executeUpdate();
			
			pstmt2.setString(1, teamName);
			pstmt2.setString(2, teamCode);
			pstmt2.setInt(3, cityId);
			pstmt2.setString(4, "ACTIVE");
			isUpdated2=pstmt2.executeUpdate();
			if (isUpdated1 == 0 || isUpdated2 == 0) {
				throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			}
		}catch (SLEException e) {
			throw e;
		}catch(Exception e){
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally{
			DBConnect.closePstmt(pstmt1,pstmt2);
		}
	}
	
	public void updateTeamData(Connection con,int teamId,String teamName,String teamCode) throws SLEException{
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs = null;
		Statement stmt = null;
		int isUpdated1 = 0;
		int isUpdated2 = 0;
		try{
			pstmt1=con.prepareStatement("Update st_sl_game_team_master set team_name = ? , team_code = ? where team_id ="+teamId);
			pstmt2=con.prepareStatement("Update st_sl_venue_master set venue_display_name = ? , venue_code = ? where venue_id ="+teamId);
			pstmt1.setString(1, teamName);
			pstmt1.setString(2, teamCode);
			pstmt2.setString(1, teamName);
			pstmt2.setString(2, teamCode);
			isUpdated1 = pstmt1.executeUpdate();
			isUpdated2 = pstmt2.executeUpdate();
			if (isUpdated1 == 0) {
				throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			}else{
				stmt = con.createStatement();
				rs = stmt.executeQuery("Select event_id,home_team_id,away_team_id from st_sl_event_master where home_team_id ="+teamId+" OR away_team_id ="+teamId);
				while(rs.next()){
					updateDescriptionForEventMaster(con,rs.getInt("event_id"),rs.getInt("home_team_id"),rs.getInt("away_team_id"));
				}
			}
		}catch (SLEException e) {
			throw e;
		}catch(SQLException e){
			e.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE,SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} finally{
			DBConnect.closePstmt(pstmt1,pstmt2);
			DBConnect.closeConnection(stmt,rs);
		}
	}
	public void updateDescriptionForEventMaster(Connection conn,int eventId,int homeTeamId,int awayTeamId) throws SLEException{
		PreparedStatement pstmt = null;
		int isUpdated = 0;
		String eventDescription = null;
		try{
			eventDescription = getTeamCodeForUpdate(conn,homeTeamId) +"-vs-"+getTeamCodeForUpdate(conn,awayTeamId);
			pstmt = conn.prepareStatement("Update st_sl_event_master set event_display = ?,event_description = ? where event_id ="+eventId);
			pstmt.setString(1,eventDescription);
			pstmt.setString(2,eventDescription);
			isUpdated = pstmt.executeUpdate();
			if (isUpdated == 0) {
				throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			}
		}catch (SLEException e) {
			throw e;
		}catch(SQLException e){
			e.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE,SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} finally{
			DBConnect.closePstmt(pstmt);
		}
	}
	public void updateTeamMappingData(Connection con, int leagueId, String teamId, List<Integer> newTeams, List<Integer> updateOld, Map<Integer,String> inActiveTeams) throws SLEException{
		List<Integer> alreadyMapped=null;
		Statement stmt1=null;
		Statement stmt2=null;
		ResultSet rs1=null;
		ResultSet rs2=null;
		int mapTeamId=0;
		List<Integer> incomingTeams = null;
		try{
			stmt1 = con.createStatement();
			alreadyMapped = new ArrayList<Integer>();
			incomingTeams = new ArrayList<Integer>();
			String query1="Select team_id,league_id,status from st_sl_league_team_mapping Where league_id="+leagueId+" And status='ACTIVE'";
			rs1 = stmt1.executeQuery(query1);
			while(rs1.next()){
				alreadyMapped.add(rs1.getInt("team_id"));
				
			}
			stmt2 = con.createStatement();
			String query2="Select team_id,league_id,status from st_sl_league_team_mapping Where league_id="+leagueId;
			rs2 = stmt2.executeQuery(query2);
			while(rs2.next()){
				inActiveTeams.put(rs2.getInt("team_id"), rs2.getString("status"));
			}
			if(teamId.equals("")){
				updateOld.addAll(alreadyMapped);
			} 
			else{
				int noOfRecords=teamId.split(",").length;
				for(int i=0;i<noOfRecords;i++){
					mapTeamId = Integer.parseInt(teamId.split(",")[i]);
					incomingTeams.add(mapTeamId);
				}
				newTeams.addAll(incomingTeams);
				newTeams.removeAll(alreadyMapped);
				updateOld.addAll(alreadyMapped);
				updateOld.removeAll(incomingTeams);
			}
		}
		catch(Exception e){
			e.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE,SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} finally{
			DBConnect.closeConnection(stmt1, rs1);
			DBConnect.closeConnection(stmt2, rs2);
			
		}
	}
	
	
	public void mapLeagueTeams(Connection con,int gameId,int leagueId,String teamId) throws SLEException{
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
		PreparedStatement pstmt3 = null;
		int isUpdated1=0;
		int isUpdated2=0;
		int isUpdated3=0;
		StringBuilder query = null;
		List<Integer> addNew = null;
		List<Integer> updateOld = null;
		Map<Integer,String> inActiveTeams = null;
		List<Integer> activeStatus = null;
		List<Integer> inActiveStatus = null;
		try{	
			addNew = new ArrayList<Integer>();
			updateOld = new ArrayList<Integer>();
			inActiveTeams = new HashMap<Integer, String>();
			activeStatus = new ArrayList<Integer>();
			inActiveStatus = new ArrayList<Integer>();
			updateTeamMappingData(con, leagueId, teamId, addNew, updateOld, inActiveTeams);
			
			for(Integer i1 : addNew){
				if(inActiveTeams.get(i1) != null && inActiveTeams.get(i1).equals("INACTIVE")){
					inActiveStatus.add(i1);
				}
					
			}
			activeStatus.addAll(addNew);
			activeStatus.removeAll(inActiveStatus);
			if(inActiveStatus.size() > 0){
				pstmt1=con.prepareStatement("Update st_sl_league_team_mapping set status = 'ACTIVE' Where team_id IN ("+inActiveStatus.toString().replace("[", "").replace("]", "")+") and league_id= "+leagueId+"");
				isUpdated1 = pstmt1.executeUpdate();		
				if (isUpdated1 == 0) {
					throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
				}
			}
			if(activeStatus.size() > 0){
				query = new StringBuilder("INSERT INTO st_sl_league_team_mapping(league_id,team_id,status) VALUES ");
				for(Integer i2: activeStatus){
					query.append("(").append(leagueId).append(",").append(i2).append(",").append("'ACTIVE'").append(")").append(",");
				}
				
				query.deleteCharAt(query.length()-1);
				
				pstmt2 = con.prepareStatement(query.toString());
				isUpdated2 = pstmt2.executeUpdate();
				if (isUpdated2 == 0) {
					throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
				}
			}
			if(updateOld.size() > 0) {
				pstmt3 = con.prepareStatement("UPDATE st_sl_league_team_mapping set status='INACTIVE' Where team_id IN ("+updateOld.toString().replace("[", "").replace("]", "")+") AND league_id="+leagueId+"");
				isUpdated3 = pstmt3.executeUpdate();	
				if (isUpdated3 == 0) {
					throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
				}
			}
			
		} catch(Exception e){
			e.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE,SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		}finally{
			DBConnect.closePstmt(pstmt1,pstmt2,pstmt3);
		}
	}
	
	/**
	 * This method updates the information for the event based on the eventId.
	 * @param conn
	 * @param MappedEventMasterBean
	 *  @param UserInfoBean
	 * @throws SLEException
	 * @since 02-Oct-2015
	 * @author Rishi
	 */
	
	public void updateEventData(MappedEventMasterBean eventDataBean,Connection conn,UserInfoBean userInfoBean) throws SLEException{
		PreparedStatement pstmt = null;
		int isUpdated = 0;
		String homeTeamCode = getTeamCodeForUpdate(conn,eventDataBean.getHomeTeamId());
		String awayTeamCode = getTeamCodeForUpdate(conn,eventDataBean.getAwayTeamId());
		String eventDisplay = homeTeamCode +"-VS-" +awayTeamCode;
		eventDataBean.setEventDisplay(eventDisplay);
		try{
			conn.setAutoCommit(false);
			
			updateEventDetailsHistory(eventDataBean.getEventId(), conn, userInfoBean);
			
			pstmt = conn.prepareStatement("UPDATE st_sl_event_master set event_display = ?, event_description = ?, league_id = ?,home_team_id = ?,away_team_id = ?,venue_id = ?,home_team_odds = ?,away_team_odds = ?,draw_odds = ?,start_time = ?,end_time = ? Where event_id="+eventDataBean.getEventId());
			pstmt.setString(1,eventDataBean.getEventDisplay());
			pstmt.setString(2,eventDataBean.getEventDisplay());
			pstmt.setInt(3,eventDataBean.getLeagueId());
			pstmt.setInt(4,eventDataBean.getHomeTeamId());
			pstmt.setInt(5,eventDataBean.getAwayTeamId());
			pstmt.setInt(6,eventDataBean.getVenueId());
			pstmt.setString(7,eventDataBean.getHomeTeamOdds());
			pstmt.setString(8,eventDataBean.getAwayTeamOdds());
			pstmt.setString(9,eventDataBean.getDrawOdds());
			pstmt.setString(10,eventDataBean.getStartTime());
			pstmt.setString(11,eventDataBean.getEndTime());
			isUpdated = pstmt.executeUpdate();
			if(isUpdated == 0){
				throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			}
			conn.commit();
			
		}catch (SLEException e) {
			throw e;
		}catch(Exception e){
			e.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE,SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		}finally{
			DBConnect.closePstmt(pstmt);
		}
	}
	
	
	
	
	/**
	 * This method  maintain the history for eventId.
	 * @param conn
	 * @param eventId
	 *  @param UserInfoBean
	 * @throws SLEException
	 * @since 19-Oct-2015
	 * @author Nikhil K. Bansal
	 */
	
	public void updateEventDetailsHistory(int eventId,Connection conn,UserInfoBean userInfoBean) throws SLEException{
		PreparedStatement pstmt = null;
		int isUpdated = 0;
		Statement stmt=null;
		ResultSet rs=null;
		try{
			conn.setAutoCommit(false);
			stmt=conn.createStatement();
			rs= stmt.executeQuery("SELECT * FROM st_sl_event_master where event_id="+eventId); 
			
			pstmt = conn.prepareStatement("INSERT INTO st_sl_event_details_change_history (event_id,game_id,event_display,event_description,league_id,home_team_id,away_team_id,home_team_odds,away_team_odds,draw_odds,venue_id,start_time,end_time,user_id,date)  VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);");
			
			if(rs.next()){
				pstmt.setInt(1,eventId);
				pstmt.setInt(2,rs.getInt("game_id"));
				pstmt.setString(3,rs.getString("event_display"));
				pstmt.setString(4,rs.getString("event_description"));
				pstmt.setInt(5,rs.getInt("league_id"));
				pstmt.setInt(6,rs.getInt("home_team_id"));
				pstmt.setInt(7,rs.getInt("away_team_id"));
				pstmt.setString(8,rs.getString("home_team_odds"));
				pstmt.setString(9,rs.getString("away_team_odds"));
				pstmt.setString(10,rs.getString("draw_odds"));
				pstmt.setInt(11,rs.getInt("venue_id"));
				pstmt.setString(12,rs.getString("start_time"));
				pstmt.setString(13,rs.getString("end_time"));
				pstmt.setInt(14,userInfoBean.getMerchantUserId());
				pstmt.setString(15, Util.getCurrentTimeString());
				
			}
			isUpdated=pstmt.executeUpdate();
			if(isUpdated == 0){
				throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			}
			
		}catch(Exception e){
			e.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE,SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		}finally{
			DBConnect.closeConnection(stmt, rs);
			DBConnect.closePstmt(pstmt);
		}
	}
	
	/**
	 * This method fetches the team code for home team and away team
	 * <p>
	 * </p> 
	 * @param conn
	 * @param teamId
	 * @return teamName
	 * @since 02-Oct-2015
	 * @author Rishi
	 */
	
	public String getTeamCodeForUpdate(Connection conn,int teamId){
		String teamCode = null;
		String query = null;
		ResultSet rs = null;
		Statement stmt = null;
		try{
			stmt = conn.createStatement();
			query = "Select team_code from st_sl_game_team_master Where team_id="+teamId;
			rs = stmt.executeQuery(query);
			if(rs.next()){
				teamCode = rs.getString("team_code");
			}
		}catch(Exception e){
			e.printStackTrace();
		} finally{
			DBConnect.closeConnection(stmt,rs);
		}
		return teamCode;
	}
}