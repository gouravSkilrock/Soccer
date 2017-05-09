package com.skilrock.sle.drawMgmt.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.skilrock.sle.common.CallUrl;
import com.skilrock.sle.common.DBConnect;
import com.skilrock.sle.common.SLELogger;
import com.skilrock.sle.common.SendSMSToWinners;
import com.skilrock.sle.common.SportsLotteryUtils;
import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.commonMethod.daoImpl.CommonMethodsDaoImpl;
import com.skilrock.sle.commonMethod.serviceImpl.CommonMethodsServiceImpl;
import com.skilrock.sle.drawMgmt.jackpotDao.JackPotCalculationDao;
import com.skilrock.sle.drawMgmt.jackpotDaoImpl.Soccer10JackPotDaoImpl;
import com.skilrock.sle.drawMgmt.jackpotDaoImpl.Soccer12JackPotDaoImpl;
import com.skilrock.sle.drawMgmt.jackpotDaoImpl.Soccer13JackPotDaoImpl;
import com.skilrock.sle.drawMgmt.jackpotDaoImpl.Soccer4JackPotDaoImpl;
import com.skilrock.sle.drawMgmt.jackpotDaoImpl.Soccer6JackPotDaoImpl;
import com.skilrock.sle.drawMgmt.javaBeans.SLPrizeRankDistributionBean;
import com.skilrock.sle.filterDispatcher.SLEMgmtFilterDispatcher;
import com.skilrock.sle.gameDataMgmt.javaBeans.DrawEventResultBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.DrawMasterBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.FreezeDrawBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.EventMasterBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.ResultApprovalBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.ResultApprovalEventBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.ResultUserAssignBean;
import com.skilrock.sle.merchant.tonybet.TonyBetWinningManager;
import com.skilrock.sle.merchant.weaver.WeaverWinningTransferThread;
import com.skilrock.sle.pwtMgmt.javaBeans.DrawWiseTicketInfoBean;

public class ResultSubmissionDaoImpl {
	private static final SLELogger logger = SLELogger.getLogger(ResultSubmissionDaoImpl.class.getName());

	public Map<Integer, String> userAssignMenu(int merchantId, Connection connection) throws SLEException {
		String query = null;
		Statement stmt = null;
		ResultSet rs = null;
		Map<Integer, String> userMap = new HashMap<Integer, String>();
		try {
			query = "SELECT merchant_user_id, CONCAT(first_name,' ',last_name) user_name FROM st_sl_merchant_user_master WHERE merchant_id="+merchantId+" AND user_type='BO';";
			stmt = connection.createStatement();
			logger.info("resultSubmissionUserAssign - "+query);
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				userMap.put(rs.getInt("merchant_user_id"), rs.getString("user_name"));
			}
		} catch (SQLException se) {
			se.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}

		return userMap;
	}

	public List<Integer> getAssignedUser(int merchantId, int gameId, int gameTypeId, Connection connection) throws SLEException {
        String query = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<Integer> userList = new ArrayList<Integer>();
        try {
            query = "SELECT user1_id, user2_id, user3_id, user4_id, user5_id FROM st_sl_result_sub_master WHERE merchant_id="+merchantId+" AND game_id="+gameId+" AND game_type_id="+gameTypeId+";";
            stmt = connection.createStatement();
            logger.info("getAssignedUser - "+query);
            rs = stmt.executeQuery(query);
            if(rs.next()) {
                userList.add(rs.getInt("user1_id"));
                userList.add(rs.getInt("user2_id"));
                userList.add(rs.getInt("user3_id"));
                userList.add(rs.getInt("user4_id"));
                userList.add(rs.getInt("user5_id"));
            } else {
                userList.add(-1);
                userList.add(-1);
                userList.add(-1);
                userList.add(-1);
                userList.add(-1);
            }
        } catch (SQLException se) {
            se.printStackTrace();
            throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
        }

        return userList;
    }

	public void resultSubmissionUserAssign(ResultUserAssignBean assignBean, Connection connection) throws SLEException {
		String query = null;
		Statement stmt = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			stmt = connection.createStatement();
			query = "SELECT id FROM st_sl_result_sub_master WHERE merchant_id="+assignBean.getMerchantId()+" AND game_id="+assignBean.getGameId()+" AND game_type_id="+assignBean.getGameTypeId()+";";
			logger.info("resultSubmissionUserAssign - "+query);
			rs = stmt.executeQuery(query);
			if(rs.next()) {
				int id = rs.getInt("id");
				List<Integer> userIds = assignBean.getUserIds();

				query = "UPDATE st_sl_result_sub_master SET user1_id=?, user2_id=?, user3_id=?, user4_id=?, user5_id=? WHERE id=?;";
				pstmt = connection.prepareStatement(query);
				pstmt.setInt(1, userIds.get(0));
				pstmt.setInt(2, userIds.get(1));
				pstmt.setInt(3, userIds.get(2));
				pstmt.setInt(4, userIds.get(3));
				pstmt.setInt(5, userIds.get(4));
				pstmt.setInt(6, id);
				pstmt.executeUpdate();
			} else {
				List<Integer> userIds = assignBean.getUserIds();

				query = "INSERT INTO st_sl_result_sub_master (merchant_id, game_id, game_type_id, user1_id, user2_id, user3_id, user4_id, user5_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
				pstmt = connection.prepareStatement(query);
				pstmt.setInt(1, assignBean.getMerchantId());
				pstmt.setInt(2, assignBean.getGameId());
				pstmt.setInt(3, assignBean.getGameTypeId());
				pstmt.setInt(4, userIds.get(0));
				pstmt.setInt(5, userIds.get(1));
				pstmt.setInt(6, userIds.get(2));
				pstmt.setInt(7, userIds.get(3));
				pstmt.setInt(8, userIds.get(4));
				pstmt.executeUpdate();
			}
		} catch (SQLException se) {
			se.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
	}

	public boolean getUserResultAuthorization(int gameId, int gameTypeId, int merchantId, int userId, Connection connection) throws SLEException {
		boolean isValid = false;
		Statement stmt = null;
		ResultSet rs = null;
		String query = null;
		try {
			query = "SELECT id FROM st_sl_result_sub_master WHERE game_id="+gameId+" AND game_type_id="+gameTypeId+" AND merchant_id="+merchantId+" AND (user1_id="+userId+" OR user2_id="+userId+" OR user3_id="+userId+" OR user4_id="+userId+" OR user5_id="+userId+");";
			stmt = connection.createStatement();
			logger.info("getUserResultAuthorization - "+query);
			rs = stmt.executeQuery(query);
			if(rs.next()) {
				isValid = true;
			} else {
				throw new SLEException(SLEErrors.USER_AUTHORIZATION_FAILED_ERROR_CODE, SLEErrors.USER_AUTHORIZATION_FAILED_ERROR_MESSAGE);
			}
		} catch (SLEException se) {
			throw se;
		} catch (SQLException se) {
			se.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}

		return isValid;
	}

	public List<DrawMasterBean> getDrawMasterDetails(int gameId,int gameTypeId,int userId,int merchantId ,Connection connection) throws SLEException {
		List<DrawMasterBean> drawMasterList = null;
		DrawMasterBean drawMasterBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		String query = null;
		try {
			query = "SELECT mas.draw_id, draw_name, draw_datetime, (SELECT IF(user1_id="+userId+", 'SUBMIT', status) FROM st_sl_result_submission_user_mapping WHERE game_id="+gameId+" AND game_type_id="+gameTypeId+" AND draw_id=mas.draw_id) status FROM st_sl_draw_master_"+gameId+" mas INNER JOIN st_sl_draw_merchant_mapping_"+gameId+" map ON mas.draw_id=map.draw_id WHERE mas.game_type_id="+gameTypeId+" AND merchant_id="+merchantId+" AND draw_status='FREEZE' AND draw_datetime<'"+Util.getCurrentTimeString()+"';";
			drawMasterList = new ArrayList<DrawMasterBean>();
			stmt = connection.createStatement();
			logger.info("DrawMasterDetails - "+query);
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				String status = rs.getString("status");
 
				if(status == null || "PENDING".equals(status)) {
					drawMasterBean = new DrawMasterBean();
					int drawId = rs.getInt("draw_id");
					drawMasterBean.setDrawId(drawId);
					drawMasterBean.setDrawName(rs.getString("draw_name"));
					drawMasterBean.setDrawDateTime(Util.getDateTimeFormat(rs.getTimestamp("draw_datetime")));
					drawMasterBean.setEventMasterList(getEventMasterDetails(gameId, gameTypeId, drawId,merchantId ,connection));
					drawMasterBean.setEventOptionMap(CommonMethodsDaoImpl.getInstance().fetchGameTypeOptionMap(gameId, gameTypeId, connection));
					drawMasterList.add(drawMasterBean);
				}
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

	public List<FreezeDrawBean> getFreezedDrawsList(int gameId, int gameTypeId, int userId, int merchantId,
			Connection connection) throws SLEException {
		List<FreezeDrawBean> freezedDrawsList = null;
		FreezeDrawBean freezeDrawBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		String query = null;
		try {
			query = "SELECT mas.draw_id, draw_name, draw_datetime, (SELECT IF((user1_id=" + userId + " OR user2_id="
					+ userId + "), 'SUBMIT', status) FROM st_sl_result_submission_user_mapping WHERE game_id=" + gameId
					+ " AND game_type_id=" + gameTypeId + " AND draw_id=mas.draw_id) status FROM st_sl_draw_master_"
					+ gameId + " mas INNER JOIN st_sl_draw_merchant_mapping_" + gameId
					+ " map ON mas.draw_id=map.draw_id WHERE mas.game_type_id=" + gameTypeId + " AND merchant_id="
					+ merchantId + " AND draw_status='FREEZE' AND draw_datetime<'" + Util.getCurrentTimeString() + "';";
			stmt = connection.createStatement();
			rs = stmt.executeQuery(query);
			freezedDrawsList = new ArrayList<FreezeDrawBean>();
			while (rs.next()) {
				String status = rs.getString("status");
				if (status == null || "PENDING".equalsIgnoreCase(status)) {
					freezeDrawBean = new FreezeDrawBean();
					int drawId = rs.getInt("draw_id");
					String drawName = rs.getString("draw_name");
					String drawDateTime = Util.getDateTimeFormat(rs.getTimestamp("draw_datetime"));
					freezeDrawBean.setDrawId(drawId);
					freezeDrawBean.setDrawName(drawName);
					freezeDrawBean.setDrawDateTime(drawDateTime);
					freezedDrawsList.add(freezeDrawBean);
				}
			}
			
		} catch (SQLException se) {
			se.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return freezedDrawsList;
	}
	
	
	public List<EventMasterBean> getEventMasterDetails(int gameId, int gameTypeId,int drawId,int merchantId ,Connection connection) throws SLEException {
		List<EventMasterBean> eventMasterList = null;
		EventMasterBean eventMasterBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		String query = null;
		try {
			query = "SELECT game_id,  gameName,a.event_id, eventDisp,  eventDesc,  leagueName,  homeTeam, homeTeamCode, awayTeam,awayTeamCode,  venuName, start_time, end_time FROM (select a.event_id, a.game_id, game_disp_name gameName, event_display eventDisp, event_description eventDesc, " +
			"league_display_name leagueName, d.team_name homeTeam,d.team_code homeTeamCode, f.team_name awayTeam,f.team_code awayTeamCode, venue_display_name venuName, start_time, " +
			"end_time from st_sl_event_master a, st_sl_game_merchant_mapping b, st_sl_league_master c, st_sl_game_team_master d, " +
			"st_sl_venue_master e,  st_sl_game_team_master f where a.game_id = b.game_id and a.game_id  ="+gameId+" and " +
			"b.merchant_id = "+merchantId+" and a.league_id = c.league_id and a.home_team_id = d.team_id and " +
			"a.venue_id = e.venue_id and a.away_team_id = f.team_id ) a INNER JOIN (" +
			 "select event_id, event_order from st_sl_draw_event_mapping_"+gameId+" where draw_id="+drawId+") b on a.event_id = b.event_id order by b.event_order";
			eventMasterList = new ArrayList<EventMasterBean>();
			stmt = connection.createStatement();
			logger.info("EventMasterDetails - "+query);
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				eventMasterBean = new EventMasterBean();
				int eventId = rs.getInt("event_id");
				eventMasterBean.setEventId(eventId);
				eventMasterBean.setEventDisplay(rs.getString("eventDisp"));
				eventMasterBean.setAwayTeamCode(rs.getString("awayTeamCode"));
				eventMasterBean.setAwayTeamName(rs.getString("awayTeam"));
				eventMasterBean.setHomeTeamCode(rs.getString("homeTeamCode"));
				eventMasterBean.setHomeTeamName(rs.getString("homeTeam"));
				eventMasterBean.setLeagueName(rs.getString("leagueName"));
				eventMasterBean.setVenueName(rs.getString("venuName"));
				eventMasterBean.setStartTime(rs.getString("start_time").replace(".0", ""));
				eventMasterBean.setEventOptionsList(getEventOptionsList(eventId,gameId,gameTypeId, connection));
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

	public List<String> getEventOptionsList(int eventId,int gameId, int gameTypeId, Connection connection) throws SLEException {
		List<String> eventOptionsList = null;
		Statement stmt = null;
		ResultSet rs = null;
		String query = null;
		try {
			query = "SELECT option_disp_name FROM st_sl_event_option_mapping eom inner join st_sl_option_master om on eom.option_name=om.option_disp_name and eom.game_id="+gameId+" and eom.game_type_id="+gameTypeId+" inner join st_sl_game_type_option_mapping som on som.option_id=om.option_id WHERE event_id="+eventId+" and som.game_id="+gameId+" and som.game_type_id="+gameTypeId+" order by display_order";
			eventOptionsList = new ArrayList<String>();
			stmt = connection.createStatement();
			logger.info("EventOptionsList - "+query);
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				eventOptionsList.add(rs.getString("option_disp_name"));
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

		return eventOptionsList;
	}

	public String insertUpdateResultStatus(DrawEventResultBean resultBean, Connection connection) throws SLEException {
		String result = null;
		String userResult = null;
		int userId1 = 0;
		String userResult1 = null;
		PreparedStatement pstmt = null;
		Statement stmt = null;
		ResultSet rs = null;
		String query = null;
		try {
			StringBuilder optionBuilder = new StringBuilder();
			for(Integer eventId : resultBean.getEventOptionResult().keySet()) {
				optionBuilder.append(eventId+"-"+resultBean.getEventOptionResult().get(eventId)+", ");
			}
			optionBuilder.delete(optionBuilder.lastIndexOf(", "), optionBuilder.length());
			userResult = optionBuilder.toString();
			logger.info("User Result - "+userResult);

			query = "SELECT status, user1_id, user1_result, user2_id, user2_result FROM st_sl_result_submission_user_mapping WHERE game_id="+resultBean.getGameId()+" AND game_type_id="+resultBean.getGameTypeId()+" AND draw_id="+resultBean.getDrawId()+";";
			stmt = connection.createStatement();
			rs = stmt.executeQuery(query);
			if(rs.next()) {
				result = rs.getString("status").toUpperCase();
				userId1 = rs.getInt("user1_id");
				userResult1 = rs.getString("user1_result");
			} else {
				result = "FIRST";
			}

			if("FIRST".equals(result)) {
				logger.info("--First Result--");
				pstmt = connection.prepareStatement("INSERT INTO st_sl_result_submission_user_mapping (game_id, game_type_id, draw_id, user1_id, user1_result, user1_update_time, status) VALUES (?,?,?,?,?,?,?);");
				pstmt.setInt(1, resultBean.getGameId());
				pstmt.setInt(2, resultBean.getGameTypeId());
				pstmt.setInt(3, resultBean.getDrawId());
				pstmt.setInt(4, resultBean.getUserId());
				pstmt.setString(5, userResult);
				pstmt.setTimestamp(6, Util.getCurrentTimeStamp());
				pstmt.setString(7, "PENDING");
				logger.info("Insert First DrawResult - "+pstmt);
				pstmt.executeUpdate();
			} else if ("PENDING".equals(result)) {
				logger.info("--Pending Result--");

				if(userId1 == resultBean.getUserId()) {
					throw new SLEException(SLEErrors.USER_ALREADY_SUBMITTED_RESULT_ERROR_CODE, SLEErrors.USER_ALREADY_SUBMITTED_RESULT_ERROR_MESSAGE);
				}

				result = (userResult.equals(userResult1)) ? "MATCHED" : "UNMATCHED";
				logger.info("resultStatus ("+userResult+" | "+userResult1+") - "+result);

				pstmt = connection.prepareStatement("UPDATE st_sl_result_submission_user_mapping SET user2_id=?, user2_result=?, user2_update_time=?, status=? WHERE game_id=? AND game_type_id=? AND draw_id=?;");
				pstmt.setInt(1, resultBean.getUserId());
				pstmt.setString(2, userResult);
				pstmt.setTimestamp(3, Util.getCurrentTimeStamp());
				pstmt.setString(4, result);
				pstmt.setInt(5, resultBean.getGameId());
				pstmt.setInt(6, resultBean.getGameTypeId());
				pstmt.setInt(7, resultBean.getDrawId());
				logger.info("Update Draw Result By Second User - "+pstmt);
				pstmt.executeUpdate();
			} else if ("UNMATCHED".equals(result)) {
				logger.info("--Unmatched Result--");

				result = "RESOLVED";

				pstmt = connection.prepareStatement("UPDATE st_sl_result_submission_user_mapping SET bo_user_id=?, bo_result=?, bo_update_time=?, status=? WHERE game_id=? AND game_type_id=? AND draw_id=?;");
				pstmt.setInt(1, resultBean.getUserId());
				pstmt.setString(2, userResult);
				pstmt.setTimestamp(3, Util.getCurrentTimeStamp());
				pstmt.setString(4, result);
				pstmt.setInt(5, resultBean.getGameId());
				pstmt.setInt(6, resultBean.getGameTypeId());
				pstmt.setInt(7, resultBean.getDrawId());
				logger.info("Resolve Draw Result - "+pstmt);
				pstmt.executeUpdate();
			}
		} catch (SLEException se) {
			throw se;
		} catch (SQLException se) {
			se.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}

		return result;
	}

	public List<ResultApprovalBean> getUnmatchedDraws(ResultApprovalBean requestBean, Connection connection) throws SLEException {
		SimpleDateFormat dateFormat = null;
		Statement stmt = null;
		ResultSet rs = null;
		List<ResultApprovalBean> approvalList = new ArrayList<ResultApprovalBean>();
		ResultApprovalBean approvalBean = null;
		try {
			dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			StringBuilder queryBuilder = new StringBuilder("SELECT rsm.draw_id, draw_name, dd.draw_datetime FROM st_sl_result_submission_user_mapping rsm INNER JOIN st_sl_draw_master_").append(requestBean.getGameId()).append(" dd ON rsm.draw_id=dd.draw_id INNER JOIN st_sl_draw_merchant_mapping_").append(requestBean.getGameId()).append(" dmm ON rsm.draw_id=dmm.draw_id WHERE rsm.game_id=").append(requestBean.getGameId()).append(" AND rsm.game_type_id=").append(requestBean.getGameTypeId()).append(" AND rsm.status='UNMATCHED' AND dmm.merchant_id=").append(requestBean.getMerchantId());
			if(requestBean.getDrawId() != 0) {
				queryBuilder.append(" AND rsm.draw_id=").append(requestBean.getDrawId());
			}

			SimpleDateFormat format = null;
			if(requestBean.getStartDate() != null && requestBean.getStartDate().length() != 0) {
				format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				String startDate = dateFormat.format(format.parse(requestBean.getStartDate()+" 00:00:00"));
				queryBuilder.append(" AND dd.draw_datetime>='").append(startDate).append("'");
			}
			if(requestBean.getEndDate() != null && requestBean.getEndDate().length() != 0) {
				if(format == null) {
					format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				}
				String endDate = dateFormat.format(format.parse(requestBean.getEndDate()+" 23:59:59"));
				queryBuilder.append(" AND dd.draw_datetime<='").append(endDate).append("'");
			}

			stmt = connection.createStatement();
			logger.info("getUnmatchedDraws - "+queryBuilder.toString());
			rs = stmt.executeQuery(queryBuilder.toString());
			while(rs.next()) {
				approvalBean = new ResultApprovalBean();
				approvalBean.setDrawId(rs.getInt("draw_id"));
				approvalBean.setDrawName(rs.getString("draw_name"));
				approvalBean.setDrawDate(dateFormat.format(rs.getTimestamp("draw_datetime")));

				approvalList.add(approvalBean);
			}
		} catch (SQLException se) {
			se.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}

		return approvalList;
	}

	public ResultApprovalBean getUnmatchedDrawDetails(ResultApprovalBean requestBean, Connection connection) throws SLEException {
		SimpleDateFormat dateFormat = null;
		Statement stmt = null;
		ResultSet rs = null;
		ResultApprovalBean approvalBean = null;
		Map<String, String> optionMap=null;
		try {
			dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			StringBuilder queryBuilder = new StringBuilder("SELECT rsm.draw_id, draw_name, dd.draw_datetime, (SELECT GROUP_CONCAT(CONCAT(em.event_id,'_', event_display,'_', event_description) ORDER BY em.event_id ASC SEPARATOR ':') FROM st_sl_event_master em INNER JOIN st_sl_draw_event_mapping_").append(requestBean.getGameId()).append(" dem ON em.event_id=dem.event_id WHERE draw_id=").append(requestBean.getDrawId()).append(") event_detail, user1_id, (SELECT user_name FROM st_sl_merchant_user_master WHERE merchant_user_id=user1_id AND merchant_id=").append(requestBean.getMerchantId()).append(") user1_name, user1_result, user1_update_time, user2_id, (SELECT user_name FROM st_sl_merchant_user_master WHERE merchant_user_id=user2_id AND merchant_id=").append(requestBean.getMerchantId()).append(") user2_name, user2_result, user2_update_time FROM st_sl_result_submission_user_mapping rsm INNER JOIN st_sl_draw_master_").append(requestBean.getGameId()).append(" dd ON rsm.draw_id=dd.draw_id INNER JOIN st_sl_draw_merchant_mapping_").append(requestBean.getGameId()).append(" dmm ON rsm.draw_id=dmm.draw_id WHERE rsm.game_id=").append(requestBean.getGameId()).append(" AND rsm.game_type_id=").append(requestBean.getGameTypeId()).append(" AND rsm.status='UNMATCHED' AND dmm.merchant_id=").append(requestBean.getMerchantId()).append(" AND rsm.draw_id=").append(requestBean.getDrawId()).append(";");

			String eventDetails = null;
			String userResult = null;
			Map<Integer, ResultApprovalEventBean> resultMap = null;
			ResultApprovalEventBean eventBean = null;
			optionMap = CommonMethodsDaoImpl.getInstance().fetchGameTypeOptionMap(requestBean.getGameId(), requestBean.getGameTypeId(), connection);
			stmt = connection.createStatement();
			logger.info("getUnmatchedDrawDetails - "+queryBuilder.toString());
			rs = stmt.executeQuery(queryBuilder.toString());
			if(rs.next()) {
				approvalBean = new ResultApprovalBean();
				approvalBean.setDrawId(rs.getInt("draw_id"));
				approvalBean.setDrawName(rs.getString("draw_name"));
				approvalBean.setDrawDate(dateFormat.format(rs.getTimestamp("draw_datetime")));
				approvalBean.setUserId1(rs.getInt("user1_id"));
				approvalBean.setUserName1(rs.getString("user1_name"));
				approvalBean.setUserUpdateTime1(dateFormat.format(rs.getTimestamp("user1_update_time")));
				approvalBean.setUserId2(rs.getInt("user2_id"));
				approvalBean.setUserName2(rs.getString("user2_name"));
				approvalBean.setUserUpdateTime2(dateFormat.format(rs.getTimestamp("user2_update_time")));

				eventDetails = rs.getString("event_detail");
				resultMap = new TreeMap<Integer, ResultApprovalEventBean>();
				for(String eventDetailString : eventDetails.split(":")) {
					String[] eventDetail = eventDetailString.split("_");

					eventBean = new ResultApprovalEventBean();
					eventBean.setEventId(Integer.parseInt(eventDetail[0]));
					eventBean.setEventDisplay(eventDetail[1]);
					eventBean.setEventDescription(eventDetail[2]);
					resultMap.put(eventBean.getEventId(), eventBean);
				}
				approvalBean.setUserResult(resultMap);
				approvalBean.setOptionCodeMap(optionMap);

				userResult = rs.getString("user1_result");
				for(String eventResult : userResult.split(",")) {
					resultMap.get(Integer.parseInt(eventResult.split("-")[0].trim())).setOptionSelected1(eventResult.split("-")[1].trim());
				}

				userResult = rs.getString("user2_result");
				for(String eventResult : userResult.split(",")) {
					resultMap.get(Integer.parseInt(eventResult.split("-")[0].trim())).setOptionSelected2(eventResult.split("-")[1].trim());
				}
			}
		} catch (SQLException se) {
			se.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}

		return approvalBean;
	}

	public void resolveResultStatus(DrawEventResultBean resultBean, Connection connection) throws SLEException {
		String userResult = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		//Map<String, String> optionMap=null;
		try {
			//optionMap = CommonMethodsDaoImpl.getInstance().fetchGameTypeOptionMap(resultBean.getGameId(), resultBean.getGameTypeId(), connection);
			if(resultBean.getUserResult() == 1 || resultBean.getUserResult() == 2 ) {
				pstmt = connection.prepareStatement("SELECT user?_result AS result FROM st_sl_result_submission_user_mapping WHERE game_id=? AND game_type_id=? AND draw_id=?;");
				pstmt.setInt(1, resultBean.getUserResult());
				pstmt.setInt(2, resultBean.getGameId());
				pstmt.setInt(3, resultBean.getGameTypeId());
				pstmt.setInt(4, resultBean.getDrawId());
				rs = pstmt.executeQuery();
				if(rs.next()) {
					userResult = rs.getString("result");
				}
				Map<Integer, String> eventMap = new TreeMap<Integer, String>();
				for(String eventResult : userResult.split(",")) {
					eventMap.put(Integer.parseInt(eventResult.split("-")[0].trim()), eventResult.split("-")[1].trim());
				}
				resultBean.setEventOptionResult(eventMap);
			} else {
				StringBuilder optionBuilder = new StringBuilder();
				for(Integer eventId : resultBean.getEventOptionResult().keySet()) {
					optionBuilder.append(eventId+"-"+resultBean.getEventOptionResult().get(eventId)+", ");
				}
				optionBuilder.delete(optionBuilder.lastIndexOf(", "), optionBuilder.length());
				userResult = optionBuilder.toString();
			}
			logger.info("User Result - "+userResult);

			pstmt = connection.prepareStatement("UPDATE st_sl_result_submission_user_mapping SET bo_user_id=?, bo_result=?, bo_update_time=?, status=? WHERE game_id=? AND game_type_id=? AND draw_id=?;");
			pstmt.setInt(1, resultBean.getUserId());
			pstmt.setString(2, userResult);
			pstmt.setTimestamp(3, Util.getCurrentTimeStamp());
			pstmt.setString(4, "RESOLVED");
			pstmt.setInt(5, resultBean.getGameId());
			pstmt.setInt(6, resultBean.getGameTypeId());
			pstmt.setInt(7, resultBean.getDrawId());
			logger.info("Resolve Draw Result - "+pstmt);
			pstmt.executeUpdate();
		} catch (SQLException se) {
			se.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}finally{
			DBConnect.closePstmt(pstmt);
		}
	}

	public String sportsLotteryResultSubmission(DrawEventResultBean drawEventResultBean, Connection connection) throws SLEException {
		Statement stmt = null;
		Statement merchantStmt = null;
		PreparedStatement pstmt = null;
		PreparedStatement salePstmt = null;
		ResultSet rs = null;
		ResultSet merchantRs = null;
		ResultSet saleRs = null;
		
		Map<Integer, String> eventOptionResult = null;
		int noOfEvents = 0;
		String winMatchQuery = null;
		int purTblName = 0;
		int cancelEvents = 0;
		double jackpotSalePercent = 0.0;
		String eventOptionQuery = "";
		int merchantId = 0;
		String merchantName = null;
		String query = null;
		String updateDrawTicketQuery = null;
		Map<Integer, SLPrizeRankDistributionBean> prizeDistributionMap = null;
		Map<Integer, Integer> rankWinnerMap = null;
		double totalSaleAmt = 0.0;
		//double winningAmount = 0.0;
		PrizeRankDistributionDaoImpl daoImpl = new PrizeRankDistributionDaoImpl();
		try {
			int gameId = drawEventResultBean.getGameId();
			int gameTypeId = drawEventResultBean.getGameTypeId();
			int drawId = drawEventResultBean.getDrawId();

			//	UPDATE DRAW RESULT IN st_sl_draw_event_mapping.
			pstmt = connection.prepareStatement("UPDATE st_sl_draw_event_mapping_? SET evt_opt_id=(SELECT evt_opt_id FROM st_sl_event_option_mapping WHERE event_id=? AND option_name=? and game_id=? and game_type_id=?) WHERE draw_id=? AND event_id=?;");
			eventOptionResult = drawEventResultBean.getEventOptionResult();
			Set<Integer> keySet = eventOptionResult.keySet();
			for(Integer eventId : keySet) {
				String optionName = eventOptionResult.get(eventId);
				pstmt.setInt(1, gameId);
				pstmt.setInt(2, eventId);
				pstmt.setString(3, optionName);
				pstmt.setInt(4, gameId);
				pstmt.setInt(5, gameTypeId);
				pstmt.setInt(6, drawId);
				pstmt.setInt(7, eventId);
				pstmt.addBatch();
			}
			logger.info("EventOptionsList - "+pstmt);
			pstmt.executeBatch();

			//	GET noOfEvents, Winning Result, purchaseTableName, cancelEvents.
			stmt = connection.createStatement();
			query = "SELECT no_of_events, option_list, purchase_table_name, cancelEvents,jackpot_sale_percent FROM ((SELECT no_of_events,jackpot_sale_percent FROM st_sl_game_type_master WHERE game_id="+gameId+" AND game_type_id="+gameTypeId+")aa, (SELECT GROUP_CONCAT(evt_opt_id SEPARATOR ',') option_list FROM st_sl_draw_event_mapping_"+gameId+" WHERE draw_id="+drawId+")bb, (SELECT purchase_table_name FROM st_sl_draw_master_"+gameId+" WHERE game_type_id="+gameTypeId+" AND draw_id="+drawId+")cc, (SELECT COUNT(*) cancelEvents FROM st_sl_draw_event_mapping_"+gameId+" dem INNER JOIN st_sl_event_option_mapping eom ON dem.evt_opt_id=eom.evt_opt_id AND draw_id="+drawId+" AND option_code='C' and eom.game_id="+gameId+" and eom.game_type_id="+gameTypeId+")dd);";
			logger.info("NoOfEvents, WinMatch, PurchaseTableName Query - "+query);
			rs = stmt.executeQuery(query);
			if(rs.next()) {
				noOfEvents = rs.getInt("no_of_events");
				winMatchQuery = rs.getString("option_list");
				purTblName = rs.getInt("purchase_table_name");
				cancelEvents = rs.getInt("cancelEvents");
				jackpotSalePercent = rs.getDouble("jackpot_sale_percent");
			}

			for(int i=1; i<=keySet.size(); i++) {
				eventOptionQuery += "IF(event_"+i+" IN("+winMatchQuery+"),1,0)+";
			}
			eventOptionQuery = eventOptionQuery.substring(0, eventOptionQuery.lastIndexOf("+"));

			double overAllSale = 0.00;
			//	APPLY MERCHANT WISE LOOP.
			merchantStmt = connection.createStatement();
			merchantRs = merchantStmt.executeQuery("SELECT merchant_id, merchant_dev_name FROM st_sl_merchant_master WHERE status='ACTIVE';");
			while(merchantRs.next()) {
				merchantId = merchantRs.getInt("merchant_id");
				merchantName = merchantRs.getString("merchant_dev_name");

				//	Check that Game is Open for Merchant
				if(SportsLotteryUtils.gameTypeInfoMerchantMap.get(merchantName).get(gameTypeId) == null) {
					continue;
				}

				//	GET PrizeRankDistribution Map.
				prizeDistributionMap = daoImpl.getPrizeRankMap(gameTypeId, merchantId, connection);
				String rankString = prizeDistributionMap.keySet().toString().replace("[", "(").replace("]", ")");

				//	UPDATE st_sl_draw_ticket TABLE FOR rank_id FOR ALL TICKETS.
				/*updateDrawTicketQuery = "UPDATE (SELECT draw_id, board_id, line_id, ticket_number, IF("+noOfEvents+"-matches+1-"+cancelEvents+" IN "+rankString+","+noOfEvents+"-matches+1-"+cancelEvents+",0) matches FROM (SELECT draw_id, board_id, line_id, ticket_number, ("+eventOptionQuery+
					") matches FROM st_sl_draw_ticket_"+gameId+"_"+gameTypeId+"_"+purTblName+" WHERE draw_id="+drawId+" AND STATUS<>'CANCELLED' AND merchant_id="+merchantId+")aa)temp " +
					"INNER JOIN st_sl_draw_ticket_"+gameId+"_"+gameTypeId+"_"+purTblName+" sdt ON sdt.ticket_number=temp.ticket_number AND " +
					"sdt.board_id=temp.board_id AND sdt.line_id=temp.line_id AND sdt.draw_id=temp.draw_id SET sdt.rank_id=matches " +
					"WHERE sdt.merchant_id="+merchantId+";";*/

				StringBuilder updateQueryBuilder = new StringBuilder();
				//updateQueryBuilder.append("UPDATE (SELECT draw_id, board_id, line_id, ticket_number, IF(").append(noOfEvents).append("-matches+1-").append(cancelEvents).append(" IN ").append(rankString).append(",").append(noOfEvents).append("-matches+1-").append(cancelEvents).append(",0) matches FROM (SELECT draw_id, board_id, line_id, ticket_number, (").append(eventOptionQuery).append(") matches FROM st_sl_draw_ticket_").append(gameId).append("_").append(gameTypeId).append("_").append(purTblName).append(" dt INNER JOIN (SELECT ticket_nbr FROM st_sl_sale_txn_master WHERE game_id=").append(gameId).append(" AND game_type_id=").append(gameTypeId).append(" AND merchant_id=").append(merchantId).append(" AND STATUS='DONE' AND is_cancel='N') stm ON dt.ticket_number=stm.ticket_nbr WHERE draw_id=").append(drawId).append(" AND STATUS NOT IN ('CANCELLED','FAILED') AND merchant_id=").append(merchantId).append(")aa)temp ").append("INNER JOIN st_sl_draw_ticket_").append(gameId).append("_").append(gameTypeId).append("_").append(purTblName).append(" sdt ON sdt.ticket_number=temp.ticket_number AND ").append("sdt.board_id=temp.board_id AND sdt.line_id=temp.line_id AND sdt.draw_id=temp.draw_id SET sdt.rank_id=matches ").append("WHERE sdt.merchant_id=").append(merchantId).append(";");
				updateQueryBuilder.append("UPDATE (SELECT draw_id, board_id, line_id, ticket_number, IF(").append(noOfEvents).append("-matches+1-").append(cancelEvents).append(" IN ").append(rankString).append(",").append(noOfEvents).append("-matches+1-").append(cancelEvents).append(",0) matches FROM (SELECT draw_id, board_id, line_id, ticket_number, (").append(eventOptionQuery).append(") matches FROM st_sl_draw_ticket_").append(gameId).append("_").append(gameTypeId).append("_").append(purTblName).append(" dt WHERE draw_id=").append(drawId).append(" AND STATUS NOT IN ('CANCELLED','FAILED') AND merchant_id=").append(merchantId).append(")aa)temp ").append("INNER JOIN st_sl_draw_ticket_").append(gameId).append("_").append(gameTypeId).append("_").append(purTblName).append(" sdt ON sdt.ticket_number=temp.ticket_number AND ").append("sdt.board_id=temp.board_id AND sdt.line_id=temp.line_id AND sdt.draw_id=temp.draw_id SET sdt.rank_id=matches ").append("WHERE sdt.merchant_id=").append(merchantId).append(";");
				updateDrawTicketQuery = updateQueryBuilder.toString();
				stmt = connection.createStatement();
				logger.info("updateDrawTicketQuery - "+updateDrawTicketQuery);
				stmt.executeUpdate(updateDrawTicketQuery);

				//salePstmt = connection.prepareStatement("select sum(bet_amount_multiple*unit_price) total_sale from st_sl_draw_ticket_?_?_? where status <> 'CANCELLED' and draw_id=? AND STATUS<>'CANCELLED' AND merchant_id=?");
				//salePstmt = connection.prepareStatement("SELECT SUM(bet_amount_multiple*unit_price) total_sale FROM st_sl_draw_ticket_?_?_? dt INNER JOIN (SELECT ticket_nbr FROM st_sl_sale_txn_master WHERE game_id=? AND game_type_id=? AND merchant_id=? AND STATUS='DONE' AND is_cancel='N') stm ON dt.ticket_number=stm.ticket_nbr WHERE STATUS NOT IN ('CANCELLED','FAILED') AND draw_id=? AND merchant_id=?;");
				salePstmt = connection.prepareStatement("SELECT SUM(bet_amount_multiple*unit_price) total_sale FROM st_sl_draw_ticket_?_?_? dt  WHERE STATUS NOT IN ('CANCELLED','FAILED') AND draw_id=? AND merchant_id=?;");
				salePstmt.setInt(1, gameId);
				salePstmt.setInt(2, gameTypeId);
				salePstmt.setInt(3, purTblName);
				//salePstmt.setInt(4, gameId);
				//salePstmt.setInt(5, gameTypeId);
				//salePstmt.setInt(6, merchantId);
				salePstmt.setInt(4, drawId);
				salePstmt.setInt(5, merchantId);
				logger.info("Get Total Sale Query - "+salePstmt);
				saleRs = salePstmt.executeQuery();
				if(saleRs.next()) {
					totalSaleAmt = saleRs.getDouble("total_sale");
					overAllSale += totalSaleAmt;
				}

				logger.info("Total Sale Value - "+totalSaleAmt);
				/*double winningPercentage = SportsLotteryUt(NULL)ils.gameTypeInfoMerchantMap.get(merchantName).get(gameTypeId).getPrizeAmtPercentage();
				winningAmount = totalSaleAmt*.01*winningPercentage;
				logger.info("Winning Amount Value - "+winningAmount);*/

				//salePstmt = connection.prepareStatement("select rank_id,sum(bet_amount_multiple) noOfWinners from st_sl_draw_ticket_?_?_? where status <> 'CANCELLED' and draw_id=? AND rank_id >0 AND merchant_id=?  group by rank_id");
				//salePstmt = connection.prepareStatement("SELECT rank_id, SUM(bet_amount_multiple) noOfWinners FROM st_sl_draw_ticket_?_?_? dt INNER JOIN (SELECT ticket_nbr FROM st_sl_sale_txn_master WHERE game_id=? AND game_type_id=? AND merchant_id=? AND STATUS='DONE' AND is_cancel='N') stm ON dt.ticket_number=stm.ticket_nbr WHERE STATUS NOT IN ('CANCELLED','FAILED') AND draw_id=? AND rank_id>0 AND merchant_id=? GROUP BY rank_id;");
				salePstmt = connection.prepareStatement("SELECT rank_id, SUM(bet_amount_multiple) noOfWinners FROM st_sl_draw_ticket_?_?_? dt WHERE STATUS NOT IN ('CANCELLED','FAILED') AND draw_id=? AND rank_id>0 AND merchant_id=? GROUP BY rank_id;");
				salePstmt.setInt(1, gameId);
				salePstmt.setInt(2, gameTypeId);
				salePstmt.setInt(3, purTblName);
				//salePstmt.setInt(4, gameId);
				//salePstmt.setInt(5, gameTypeId);
				//salePstmt.setInt(6, merchantId);
				salePstmt.setInt(4, drawId);
				salePstmt.setInt(5, merchantId);
				logger.info("Get Rank And Winner Query - "+salePstmt);
				saleRs = salePstmt.executeQuery();
				rankWinnerMap = new HashMap<Integer, Integer>();
				while(saleRs.next()) {
					rankWinnerMap.put(saleRs.getInt("rank_id"), saleRs.getInt("noOfWinners"));
				}

				//	INSERT IN st_sl_prize_details TABLE.
				Set<Integer> prizeRankSet = prizeDistributionMap.keySet();
				//pstmt = connection.prepareStatement("INSERT INTO st_sl_prize_details_?_? (draw_id, merchant_id, prize_rank, prize_amount, unit_price, no_of_winners, no_of_match) values(?,?,?,?,?,?,?);");
				pstmt = connection.prepareStatement("INSERT INTO st_sl_prize_details_?_? (draw_id, merchant_id, prize_rank, prize_amount, unit_price, no_of_winners, no_of_match) VALUES (?,?,?,?,?,?,?);");
				for(Integer prizeRank : prizeRankSet) {
					SLPrizeRankDistributionBean bean = prizeDistributionMap.get(prizeRank);

					pstmt.setInt(1, gameId);
					pstmt.setInt(2, gameTypeId);
					pstmt.setInt(3, drawId);
					pstmt.setInt(4, merchantId);
					pstmt.setInt(5, prizeRank);
					pstmt.setDouble(6, 0);
					/*if("FIXED".equalsIgnoreCase(bean.getPrizeType())) {
						pstmt.setDouble(6, bean.getPrizeValue());
					} else {
						if(rankWinnerMap.containsKey(prizeRank)) {
							double netWinAmt = winningAmount*.01*bean.getPrizeValue()/rankWinnerMap.get(prizeRank);
							if(bean.getMinPrizeValue() > netWinAmt ) {
								netWinAmt = bean.getMinPrizeValue();
							}
							pstmt.setDouble(6, netWinAmt);
						} else {
							pstmt.setDouble(6, winningAmount*.01*bean.getPrizeValue());
						}
					}*/
					pstmt.setDouble(7, bean.getUnitPrice());
					if(rankWinnerMap.containsKey(prizeRank)) {
						pstmt.setInt(8, rankWinnerMap.get(prizeRank));
					} else {
						pstmt.setInt(8, 0);
					}
					pstmt.setInt(9, bean.getMatchId());
					logger.info("prizeDetailsInsertQuery : "+pstmt);
					pstmt.addBatch();
				}
				pstmt.executeBatch();
			}

			//	Calculation for Jackpot Start
			String gameTypeDevName = SportsLotteryUtils.gameTypeInfoMerchantMap.get("RMS").get(gameTypeId).getGameTypeDevName();
			double ppr = SportsLotteryUtils.gameTypeInfoMerchantMap.get("RMS").get(gameTypeId).getPrizeAmtPercentage();
			JackPotCalculationDao jackPotDao = null;
			if("soccer12".equals(gameTypeDevName)) {
				jackPotDao = Soccer12JackPotDaoImpl.getInstance();
			} else if("soccer13".equals(gameTypeDevName)) {
				jackPotDao = Soccer13JackPotDaoImpl.getInstance();
				jackPotDao.setJackpotSalePercent(jackpotSalePercent);
				jackPotDao.setPrizePayoutRatio(ppr);
			} else if("soccer10".equals(gameTypeDevName)) {
				jackPotDao = Soccer10JackPotDaoImpl.getInstance();
				jackPotDao.setJackpotSalePercent(jackpotSalePercent);
				jackPotDao.setPrizePayoutRatio(ppr);
			} else if("soccer6".equals(gameTypeDevName)) {
				jackPotDao = Soccer6JackPotDaoImpl.getInstance();
				jackPotDao.setPrizePayoutRatio(ppr);
			} else if("soccer4".equals(gameTypeDevName)) {
				jackPotDao = Soccer4JackPotDaoImpl.getInstance();
				jackPotDao.setPrizePayoutRatio(ppr);
			}
			jackPotDao.calculationForJackpot(gameId, gameTypeId, drawId, prizeDistributionMap, overAllSale, connection);
			//	Calculation for Jackpot End

			//	Update Draw Status from FREEZE to CLAIM_ALLOW
			stmt = connection.createStatement();
			int rows = stmt.executeUpdate("UPDATE st_sl_draw_master_"+gameId+" SET draw_status='CLAIM ALLOW' WHERE draw_id="+drawId+" AND game_type_id="+gameTypeId+" AND draw_status='FREEZE';");
			
			//Update verification code for all Winners
			String merchantList=(String)Util.getPropertyValue("MERCHANT_LIST_FOR_PWT_VERIFICATION");
			if(merchantList != null && merchantList.length() > 0 && !merchantList.equals("")){
				SportsLotteryUtils.updateVerificationCode(gameId,gameTypeId,purTblName,connection,drawId,merchantList);  
			}
			connection.commit();
			//Send VerificationCode to Winners
			if(merchantList != null && merchantList.length() > 0){
				SendSMSToWinners sendSms = new SendSMSToWinners(gameId,gameTypeId, merchantList, drawId,purTblName);
				sendSms.setDaemon(true);
				sendSms.start();
			}
			//In case of weaver run a thread from here for winning transfer.
			if (Util.merchantInfoMap.get("Weaver") != null) {
				WeaverWinningTransferThread transferThread = new WeaverWinningTransferThread(gameId, gameTypeId, drawId,new DrawWiseTicketInfoBean());
				transferThread.setDaemon(true);
				transferThread.start();
			}
			if(Util.merchantInfoMap.get("TonyBet")!=null){
				DrawWiseTicketInfoBean  infoBean  = new DrawWiseTicketInfoBean();
				infoBean.setDrawId(drawId);
				infoBean.setGameId(gameId);
				infoBean.setGameTypeId(gameTypeId);
				infoBean.setMerchantId(Util.merchantInfoMap.get("TonyBet").getMerchantId());
				TonyBetWinningManager   winningManager = new TonyBetWinningManager(infoBean);
				winningManager.start();
				
			}
			if(rows > 0) {
				query = "SELECT client_access_url, on_activity FROM st_sl_client_info_master WHERE on_activity='RESULT_SUBMISSION' AND STATUS = 'ACTIVE';";
				rs = stmt.executeQuery(query);
				while(rs.next()) {
					CallUrl callUrl = new CallUrl(rs.getString("client_access_url"), gameId, gameTypeId, drawId, rs.getString("on_activity"));
					callUrl.setDaemon(false);
					callUrl.start();
				}
			}
		} catch (SQLException se) {
			se.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}

		return "success";
	}
	/*public static void main(String[] args) throws SLEException {
		DrawWiseTicketInfoBean  infoBean  = new DrawWiseTicketInfoBean();
		infoBean.setDrawId(113);
		infoBean.setGameId(1);
		infoBean.setGameTypeId(1);
		infoBean.setMerchantId(1);
		SLEMgmtFilterDispatcher  obj = new SLEMgmtFilterDispatcher();
		obj.onStartServer();
  
		Util.merchantInfoMap = CommonMethodsServiceImpl.getInstance().fetchMerchantInfo();
        CommonMethodsServiceImpl.getInstance().setGameAndGameTypeInfoMerchantMap();
		TonyBetWinningManager   winningManager = new TonyBetWinningManager(infoBean);
		winningManager.run();
	}*/
	
	/**
	 * @author Vatsal
	 * @throws SQLException 
	 * @since 10-April-2017
	 * This Method is for SIMNET. 
	 */
	public Map<String,Double> simnetPrizeDistributionDao(int gameId,int gameTypeId,int drawId,int simnetTotalSale,int userId,int noOfWinnersFor12,int noOfWinnersFor11,int noOfWinnersFor10,Connection conn) throws SLEException, SQLException{
		double totalSaleAmt = 0.0;
		Map<String,Double> SuccessJspDataMap=null;
		insertIntoSimnetHistoryTable(gameId,gameTypeId,drawId,simnetTotalSale,userId,noOfWinnersFor12,noOfWinnersFor11,noOfWinnersFor10,conn);
		updateWinnersInSleTableAndMaintaingHostoryInNewTable(gameId,gameTypeId,drawId,simnetTotalSale,userId,noOfWinnersFor12,noOfWinnersFor11,noOfWinnersFor10,conn);
		totalSaleAmt=simnetTotalSale+ getSleTotalSaleForDraw(gameId,gameTypeId,drawId,conn);
		String status=simnetCalculationForJackpot(gameId, gameTypeId, drawId,totalSaleAmt, conn);
		if(status=="Success")
		{
			SuccessJspDataMap=OnSuccessJspData(gameId,gameTypeId,drawId,conn);
			if(SuccessJspDataMap!=null)
			{
				SuccessJspDataMap.put("Total Combined Sale",totalSaleAmt);
				return SuccessJspDataMap;
			}else{
				throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			}
		}
		else
		{
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		
	}
	
	/**
	 * @author Vatsal
	 * @throws SLEException 
	 * @throws SQLException 
	 * @since 10-April-2017
	 * This method returns combined sale for SIMNET and SLE for particular drawId
	 */
	public double getSleTotalSaleForDraw(int gameId,int gameTypeId,int drawId,Connection conn) throws SLEException, SQLException{
		String query=null;
		Statement stmt1=null;
		ResultSet rs1=null;
		double TotalSaleAmount = 0.0;
		int purchaseTableNameForDraw=-1;
		
		try{
			query="SELECT purchase_table_name FROM st_sl_draw_master_"+gameId+" WHERE draw_id="+drawId+";";
			stmt1=conn.createStatement();
			rs1=stmt1.executeQuery(query);
			if(rs1.next()){
				purchaseTableNameForDraw=rs1.getInt("purchase_table_name");
			}
			rs1 = stmt1.executeQuery("SELECT merchant_id, merchant_dev_name FROM st_sl_merchant_master WHERE status='ACTIVE';");
			while(rs1.next())
			{
				int merchantId=rs1.getInt("merchant_id");
				PreparedStatement merchantStmt=conn.prepareStatement("SELECT SUM(bet_amount_multiple*unit_price) total_sale FROM st_sl_draw_ticket_?_?_? dt  WHERE STATUS NOT IN ('CANCELLED','FAILED') AND draw_id=? AND merchant_id=?;");
				merchantStmt.setInt(1, gameId);
				merchantStmt.setInt(2, gameTypeId);
				merchantStmt.setInt(3, purchaseTableNameForDraw);
				merchantStmt.setInt(4, drawId);
				merchantStmt.setInt(5, merchantId);
				ResultSet rs2=merchantStmt.executeQuery();
				double saleForMerchant=0.0;
				if(rs2.next()){
				 saleForMerchant=rs2.getInt("total_sale");
				}
				if(!rs2.wasNull())//To Handle if NULL value of rs2(to execute only when not null)
				{
					TotalSaleAmount=TotalSaleAmount+saleForMerchant;
				}
				else//if rs2 was null was null value don't do anything(don't add)
				{
					continue;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return	TotalSaleAmount;	
	}
	   
	
	/**
	 * @author Vatsal
	 * @throws SQLException 
	 * @since 10-April-2017
	 * This method updates for SIMNET and SLE for particular drawId
	 */
	public void insertIntoSimnetHistoryTable(int gameId, int gameTypeId, int drawId, int simnetTotalSale,
			int userId, int noOfWinnersFor12, int noOfWinnersFor11, int noOfWinnersFor10, Connection conn) throws SLEException, SQLException {
		String query = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn.setAutoCommit(false);
			query="INSERT INTO st_sl_simnet_history_prize_distribution_"+gameId+" (game_type_Id,draw_id,total_sale,10_match_winners,11_match_winners,12_match_winners,update_time,user_id)"+ " VALUES ('"+gameTypeId+"', '"+drawId+"', '"+simnetTotalSale+"', '"+noOfWinnersFor10+"', '"+noOfWinnersFor11+"', '"+noOfWinnersFor12+"', '"+new java.sql.Timestamp(new java.util.Date().getTime())+"', '"+userId+"')"+";";
			stmt=conn.prepareStatement(query);
			int r=stmt.executeUpdate();
			conn.commit();
		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}finally{
			conn.setAutoCommit(true);
		}

	}
	
	
	public void updateWinnersInSleTableAndMaintaingHostoryInNewTable(int gameId,int gameTypeId,int drawId,int simnetTotalSale,int userId,int noOfWinnersFor12,int noOfWinnersFor11,int noOfWinnersFor10,Connection conn) throws SLEException, SQLException{
		
		String query1,query2=null;
		PreparedStatement stmt=null;
		int a[]={noOfWinnersFor12,noOfWinnersFor11,noOfWinnersFor10};
		try{
			conn.setAutoCommit(false);
			for(int i=1;i<=3;i++){
				query1="insert into st_sl_simnet_history_prize_detail_"+gameId+"_"+gameTypeId+"(draw_id ,merchant_id ,prize_rank ,prize_amount,unit_price ,no_of_winners ,no_of_match,user_id,update_time)(select draw_id ,merchant_id ,prize_rank ,prize_amount,unit_price ,no_of_winners ,no_of_match,"+userId+",'"+new java.sql.Timestamp(new java.util.Date().getTime())+"'from st_sl_prize_details_"+gameId+"_"+gameTypeId+" WHERE draw_id ="+drawId+" and prize_rank ="+i+" and merchant_id = 2 )";
				stmt=conn.prepareStatement(query1);
				logger.info("insert into simnet History Table query - "+stmt);
				stmt.executeUpdate();
				query2="update st_sl_prize_details_"+gameId+"_"+gameTypeId+" set no_of_winners = (no_of_winners +"+a[i-1]+" ) WHERE draw_id ="+drawId+" and prize_rank ="+i+" and merchant_id = 2";
				stmt=conn.prepareStatement(query2);
				logger.info("updating no of winners(adding simnet winners) in original sle table query - "+stmt);
				stmt.executeUpdate();
			}
			conn.commit();
		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}finally{
			conn.setAutoCommit(true);
		}
		
	}
	
	public String simnetCalculationForJackpot(int gameId, int gameTypeId, int drawId, double totalSaleAmt, Connection conn) throws SLEException{
		Map<Integer, SLPrizeRankDistributionBean> prizeDistributionMap = null;
		try {
			PrizeRankDistributionDaoImpl daoImpl = new PrizeRankDistributionDaoImpl();
			prizeDistributionMap = daoImpl.getPrizeRankMap(gameTypeId,2, conn);
			JackPotCalculationDao jackPotDao = Soccer12JackPotDaoImpl.getInstance();
			jackPotDao.calculationForJackpotForSimnet(gameId, gameTypeId, drawId, prizeDistributionMap, totalSaleAmt, conn);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return "Success";
	}
	
	public Map<String,Double> OnSuccessJspData(int gameId,int gameTypeId,int drawId,Connection conn) throws SLEException, SQLException{
		String query=null;
		Statement stmt=null;
		ResultSet rs=null;
		Map<String,Double> combinedPrizeAmtMap=null;
		try{
			combinedPrizeAmtMap=new LinkedHashMap<String,Double>();
			query="select prize_rank,prize_amount from st_sl_prize_details_"+gameId+"_"+gameTypeId+" where draw_id="+drawId+" AND merchant_id=2"+";";
			stmt=conn.createStatement();
			rs=stmt.executeQuery(query);
			while(rs.next())
			{   if(rs.getInt("prize_rank")==1)
				{combinedPrizeAmtMap.put("Prize Amount For 12 Match",rs.getDouble("prize_amount"));}
			else if(rs.getInt("prize_rank")==2){
				combinedPrizeAmtMap.put("Prize Amount For 11 Match",rs.getDouble("prize_amount"));
			}else if(rs.getInt("prize_rank")==3){
				combinedPrizeAmtMap.put("Prize Amount For 10 Match",rs.getDouble("prize_amount"));
			}
		 }
		}catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return combinedPrizeAmtMap;
	}
	
}