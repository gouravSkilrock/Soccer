package com.skilrock.sle.commonMethod.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skilrock.sle.common.ConfigurationVariables;
import com.skilrock.sle.common.DBConnect;
import com.skilrock.sle.common.SLELogger;
import com.skilrock.sle.common.SLEMySqlQueries;
import com.skilrock.sle.common.SportsLotteryUtils;
import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.exception.GenericException;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.common.javaBeans.AuditTrailBean;
import com.skilrock.sle.common.javaBeans.CityDataBean;
import com.skilrock.sle.common.javaBeans.CountryDataBean;
import com.skilrock.sle.common.javaBeans.PropertyMasterBean;
import com.skilrock.sle.common.javaBeans.StateDataBean;
import com.skilrock.sle.common.javaBeans.TicketInfoBean;
import com.skilrock.sle.common.javaBeans.UserInfoBean;
import com.skilrock.sle.common.javaBeans.ValidateTicketBean;
import com.skilrock.sle.dataMgmt.javaBeans.CancelTransactionAPIBean;
import com.skilrock.sle.drawMgmt.javaBeans.SLPrizeRankDistributionBean;
import com.skilrock.sle.gameDataMgmt.controllerImpl.GameDataControllerImpl;
import com.skilrock.sle.gameDataMgmt.javaBeans.GameMasterBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.GameTypeMasterBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.LeagueMasterBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.TeamMasterBean;
import com.skilrock.sle.gamePlayMgmt.javaBeans.DrawDetailBean;
import com.skilrock.sle.gamePlayMgmt.javaBeans.EventDetailBean;
import com.skilrock.sle.gamePlayMgmt.javaBeans.ReprintTicketBean;
import com.skilrock.sle.gamePlayMgmt.javaBeans.SportsLotteryGamePlayBean;
import com.skilrock.sle.merchant.common.javaBeans.MerchantInfoBean;
import com.skilrock.sle.tp.rest.common.javaBeans.TpUserRegistrationBean;

public class CommonMethodsDaoImpl {
	private static final SLELogger logger = SLELogger.getLogger(CommonMethodsDaoImpl.class.getName());

	private CommonMethodsDaoImpl() {
	}

	private static volatile CommonMethodsDaoImpl classInstance;

	public static CommonMethodsDaoImpl getInstance() {
		if (classInstance == null) {
			synchronized (CommonMethodsDaoImpl.class) {
				if (classInstance == null) {
					classInstance = new CommonMethodsDaoImpl();
				}
			}
		}
		return classInstance;
	}

	public void registerMerchantUser(TpUserRegistrationBean registrationRequestBean, Connection conn)throws GenericException {
		Statement stmt = null;
		PreparedStatement pStmt = null;
		ResultSet rs = null;
		int isUpdated = 0;
		long userId = 0;
		long parentUserId = 0;
		int roleId = 0;
		try {
			stmt = conn.createStatement();
			ResultSet userRs = stmt
					.executeQuery("select user_id,role_id from st_sl_merchant_user_master where merchant_user_id="
							+ registrationRequestBean.getCreatorUserId()
							+ " and merchant_id="
							+ registrationRequestBean.getMerchantId());
			if (userRs.next()) {
				parentUserId = userRs.getInt("user_id");
			} else {
				parentUserId = 0;

			}
			roleId = getRoleIdFromRoleName(registrationRequestBean
					.getUserType(), conn);
			pStmt = conn
					.prepareStatement("INSERT INTO st_sl_merchant_user_master(parent_user_id, role_id, merchant_id, merchant_user_id, merchant_user_mapping_id, user_name, user_type, is_role_head, first_name, last_name, address, mobile_nbr, gender, email_id, city, state, country,org_name) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);",Statement.RETURN_GENERATED_KEYS);
			pStmt.setLong(1, parentUserId);
			pStmt.setInt(2, roleId);
			pStmt.setInt(3, registrationRequestBean.getMerchantId());
			pStmt.setInt(4, registrationRequestBean.getUserId());
			pStmt.setInt(5, registrationRequestBean.getUserMappingId());
			pStmt.setString(6, registrationRequestBean.getUserName());
			pStmt.setString(7, registrationRequestBean.getUserType());
			if("RETAILER".equalsIgnoreCase(registrationRequestBean.getUserType()) || "AGENT".equalsIgnoreCase(registrationRequestBean.getUserType())){
				pStmt.setString(8, "Y");
			}else{
				pStmt.setString(8, "N");
			}
			pStmt.setString(9, registrationRequestBean.getFirstName());
			pStmt.setString(10, registrationRequestBean.getLastName());
			pStmt.setString(11, registrationRequestBean.getAddress());
			pStmt.setString(12, registrationRequestBean.getMobileNbr());
			pStmt.setString(13, registrationRequestBean.getGender());
			pStmt.setString(14, registrationRequestBean.getEmailId());
			pStmt.setString(15, registrationRequestBean.getCity());
			pStmt.setString(16, registrationRequestBean.getState());
			pStmt.setString(17, registrationRequestBean.getCountry());
			pStmt.setString(18, registrationRequestBean.getOrgName());

			isUpdated = pStmt.executeUpdate();
			if (isUpdated == 0)
				throw new GenericException(SLEErrors.SQL_EXCEPTION_ERROR_CODE,
						SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);

			rs = pStmt.getGeneratedKeys();
			if (rs.next()) {
				userId = rs.getLong(1);
				pStmt = conn
						.prepareStatement("INSERT INTO st_sl_merchant_user_authentication_mapping(user_id, merchant_user_id, session_id, merchant_id) VALUES(?, ?, ?, ?)");
				pStmt.setLong(1, userId);
				pStmt.setInt(2, registrationRequestBean.getUserId());
				pStmt.setString(3, registrationRequestBean.getSessionId());
				pStmt.setInt(4, registrationRequestBean.getMerchantId());
				logger.debug("query" + pStmt);
				isUpdated = pStmt.executeUpdate();

				if (isUpdated == 0)
					throw new GenericException(
							SLEErrors.SQL_EXCEPTION_ERROR_CODE,
							SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
			}
			
			if("RETAILER".equalsIgnoreCase(registrationRequestBean.getUserType())){
				int mctId=0;
				userRs = stmt.executeQuery("select mct_id,channel_type,channel_name from st_rm_merchant_channel_mapping aa inner join st_rm_merchant_channel_tier_mapping bb inner join st_rm_tier_master cc on aa.merchant_channel_id=bb.merchant_channel_id and bb.tier_id=cc.tier_id where merchant_id="+registrationRequestBean.getMerchantId()+" and tier_code='"+registrationRequestBean.getUserType()+"' and aa.status='ACTIVE' and bb.status='ACTIVE' and cc.tier_status='ACTIVE' and channel_name='Terminal'");
				if(userRs.next()){
					mctId=userRs.getInt("mct_id");					
				}
				pStmt = conn.prepareStatement("insert into st_rm_user_action_mapping(user_id,rpm_id,status)select ? user_id,rpm_id,status from st_rm_role_action_mapping where mct_id=? and rpm_id not in (select rpm_id from st_rm_user_action_mapping where user_id=?)");
				pStmt.setLong(1, userId);
				pStmt.setInt(2, mctId);
				pStmt.setLong(3, userId);
				logger.debug("query" + pStmt);
				pStmt.executeUpdate();
				
			}
				

			registrationRequestBean.setSleUserId(userId);
		} catch (GenericException e) {
			throw e;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new GenericException(SLEErrors.SQL_EXCEPTION_ERROR_CODE);
			
		} catch (Exception e) {
			throw new GenericException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE);
		} finally {
			DBConnect.closeConnection(pStmt, stmt, rs);
		}
	}

	public void updateMerchantUserData(
			TpUserRegistrationBean registrationRequestBean, Connection conn)
			throws SLEException {
		Statement stmt = null;
		StringBuilder query = new StringBuilder();
		int isUpdated = 0;

		try {
			stmt = conn.createStatement();
			query
					.append(
							"UPDATE st_sl_merchant_user_master SET merchant_user_mapping_id = ")
					.append(registrationRequestBean.getUserMappingId()).append(
							", first_name = '").append(
							registrationRequestBean.getFirstName()).append(
							"', last_name = '").append(
							registrationRequestBean.getLastName()).append(
							"', address = '").append(
							registrationRequestBean.getAddress()).append(
							"', mobile_nbr = '").append(
							registrationRequestBean.getMobileNbr()).append(
							"', email_id ='").append(
							registrationRequestBean.getEmailId()).append(
							"', city = '").append(
							registrationRequestBean.getCity()).append(
							"', state = '").append(
							registrationRequestBean.getState()).append(
							"', country = '").append(
							registrationRequestBean.getCountry()).append(
							"' WHERE merchant_user_id =").append(
							registrationRequestBean.getUserId()).append(
							" AND merchant_id = ").append(
							registrationRequestBean.getMerchantId());
			isUpdated = stmt.executeUpdate(query.toString());
			if (isUpdated == 0)
				throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE,
						SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);

			query.setLength(0);
			String sessionId = null;
			if (registrationRequestBean.getSessionId() != null) {
				sessionId = "'" + registrationRequestBean.getSessionId() + "'";
			}
			query
					.append(
							"update st_sl_merchant_user_authentication_mapping set session_id = ")
					.append(sessionId).append(" WHERE merchant_user_id = ")
					.append(registrationRequestBean.getUserId()).append(
							" AND merchant_id = ").append(
							registrationRequestBean.getMerchantId());

			isUpdated = stmt.executeUpdate(query.toString());
			if (isUpdated == 0)
				throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE,
						SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (SLEException e) {
			throw e;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE,
					SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,
					SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeStatement(stmt);
		}
	}

	public void updateMerchantUserLogout(
			TpUserRegistrationBean registrationRequestBean, Connection conn)
			throws SLEException {
		Statement stmt = null;
		StringBuilder query = new StringBuilder();
		int isUpdated = 0;
		String sessionId = null;

		logger.debug("***** Inside updateMerchantLogoutData Method");
		try {
			if (registrationRequestBean.getSessionId() != null) {
				sessionId = "'" + registrationRequestBean.getSessionId() + "'";
			}
			stmt = conn.createStatement();
			query
					.append(
							"update st_sl_merchant_user_authentication_mapping set session_id = ")
					.append(sessionId).append(" WHERE merchant_user_id = ")
					.append(registrationRequestBean.getUserId());
			isUpdated = stmt.executeUpdate(query.toString());
			if (isUpdated == 0)
				throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE,
						SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (SLEException e) {
			throw e;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE,
					SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,
					SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeStmt(stmt);
		}
	}

	public void updateMerchantServerDown(int merchantId, String sessionId,
			Connection conn) throws SLEException {
		PreparedStatement pstmt = null;
		StringBuilder query = new StringBuilder();
		int isUpdated = 0;
		logger.debug("***** Inside updateMerchantServerDown Method");
		try {
			query
					.append("UPDATE st_sl_merchant_user_authentication_mapping SET session_id = NULL where merchant_id = ? and sessionId = ?");
			pstmt = conn.prepareStatement(query.toString());
			pstmt.setInt(1, merchantId);
			pstmt.setString(2, sessionId);
			pstmt.executeUpdate();
			if (isUpdated == 0)
				throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE,
						SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (SLEException e) {
			throw e;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE,
					SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,
					SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closePstmt(pstmt);
		}
	}

	public boolean checkUserExistence(int userId, int merchantId, Connection con)
			throws SLEException {
		boolean isUserExists = true;
		StringBuilder query = null;
		Statement stmt = null;
		ResultSet rs = null;

		logger.debug("***** Inside checkUserExistence Function");

		try {
			query = new StringBuilder();
			query.append(
					"SELECT user_id FROM st_sl_merchant_user_master WHERE ")
					.append("merchant_user_id = ").append(userId).append(
							" AND merchant_id = ").append(merchantId);
			logger.info("Query For Fetching checkUserExistence "
					+ query.toString());
			stmt = con.createStatement();
			rs = stmt.executeQuery(query.toString());
			if (!rs.next())
				isUserExists = false;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE,
					SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,
					SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(stmt, rs);
		}
		return isUserExists;
	}

	public List<PropertyMasterBean> getPropertyDetail() throws SLEException {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		List<PropertyMasterBean> propertyDataList = null;
		PropertyMasterBean propertyMasterBean = null;
		try {
			con = DBConnect.getConnection();
			pstmt = con.prepareStatement(SLEMySqlQueries.GET_PROPERTY_DETAILS);
			logger.debug("Get Property Details: "
					+ SLEMySqlQueries.GET_PROPERTY_DETAILS);
			rs = pstmt.executeQuery();
			propertyDataList = new ArrayList<PropertyMasterBean>();
			while (rs.next()) {
				propertyMasterBean = new PropertyMasterBean();
				propertyMasterBean.setId(rs.getInt("id"));
				propertyMasterBean.setPropertyCode(rs
						.getString("property_code"));
				propertyMasterBean.setPropertyDevName(rs
						.getString("property_dev_name"));
				propertyMasterBean.setPropertyDisplayName(rs
						.getString("property_display_name"));
				propertyMasterBean.setEditable(rs.getString("editable"));
				propertyMasterBean.setValue(rs.getString("value"));
				propertyMasterBean.setValueType(rs.getString("value_type"));
				propertyMasterBean.setDescription(rs.getString("description"));
				propertyDataList.add(propertyMasterBean);
			}
		} catch (SQLException se) {
			se.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE,
					SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,
					SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(con);
			DBConnect.closeConnection(pstmt, rs);
		}
		return propertyDataList;
	}

	public void fetchUserInfo(UserInfoBean userInfoBean, Connection conn)
			throws SLEException {
		StringBuilder query = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuilder userDetaiCheck=null;
		try {
			query = new StringBuilder();
			userDetaiCheck=new StringBuilder();
			if(userInfoBean.getUserType()==null || userInfoBean.getUserType().equalsIgnoreCase("")){
				userDetaiCheck.append(" user_name = '").append(userInfoBean.getUserName()).append("' OR mobile_nbr='").append(userInfoBean.getUserName()).append("'");
			}else{
				userDetaiCheck.append("( user_name = '").append(userInfoBean.getUserName()).append("' OR mobile_nbr='").append(userInfoBean.getUserName()).append("') AND user_type='").append(userInfoBean.getUserType()).append("'");
			}
			query.append("SELECT mum.merchant_user_id, mum.merchant_user_mapping_id, mum.user_id, session_id, user_type,org_name ,mobile_nbr FROM st_sl_merchant_user_master mum INNER JOIN st_sl_merchant_user_authentication_mapping uam ON mum.merchant_user_id = uam.merchant_user_id")
					.append(" WHERE mum.merchant_id = ").append(userInfoBean.getMerchantId()).append(" AND ").append(userDetaiCheck.toString());
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query.toString());

			if (rs.next()) {
				userInfoBean.setMerchantUserId(rs.getInt("merchant_user_id"));
				userInfoBean.setMerchantUserMappingId(rs
						.getInt("merchant_user_mapping_id"));
				userInfoBean.setSleUserId(rs.getInt("user_id"));
				userInfoBean.setDbSessionId(rs.getString("session_id"));
				userInfoBean.setUserType(rs.getString("user_type"));
				userInfoBean.setMobileNbr(rs.getString("mobile_nbr"));
				userInfoBean.setOrgName(rs.getString("org_name"));
			}
		} catch (SQLException e) {
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE,
					SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,
					SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(stmt, rs);
		}
	}

	public List<String> setServerUrl() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		String query = null;
		List<String> serverUrl = null;
		try {
			con = DBConnect.getConnection();
			query = "select merchant_dev_name, host_address, protocol, project_name, port from st_sl_merchant_master where status='ACTIVE'";
			pstm = con.prepareStatement(query);
			rs = pstm.executeQuery();

			serverUrl = new ArrayList<String>();
			while (rs.next()) {
				if (rs.getString("merchant_dev_name").equals("PMS")) {
					String url = rs.getString("protocol") + "://"
							+ rs.getString("host_address") + ":"
							+ rs.getString("port") + "/"
							+ rs.getString("project_name") + "/";
					serverUrl.add(url);
				} else if (rs.getString("merchant_dev_name").equals("LMS")) {
					String url = rs.getString("protocol") + "://"
							+ rs.getString("host_address") + ":"
							+ rs.getString("port") + "/"
							+ rs.getString("project_name") + "/";
					serverUrl.add(url);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeConnection(con);
			DBConnect.closeConnection(pstm, rs);
		}
		return serverUrl;
	}

	public Map<String, MerchantInfoBean> fetchMerchantInfo(Connection con)
			throws SLEException {
		Map<String, MerchantInfoBean> merchantInfoMap = null;

		Statement stmt = null;
		StringBuilder query = null;
		ResultSet rs = null;

		MerchantInfoBean merchantInfoBean = null;
		try {
			stmt = con.createStatement();
			query = new StringBuilder();
			query
					.append("SELECT merchant_id, merchant_name, merchant_dev_name, service_id, merchant_ip, protocol, project_name, PORT, merchant_user_name, merchant_password FROM st_sl_merchant_master");
			logger
					.debug("Query For Fetching Merchant Info "
							+ query.toString());
			rs = stmt.executeQuery(query.toString());
			merchantInfoMap = new HashMap<String, MerchantInfoBean>();
			while (rs.next()) {
				merchantInfoBean = new MerchantInfoBean();
				merchantInfoBean.setMerchantId(rs.getInt("merchant_id"));
				merchantInfoBean.setMerchantName(rs.getString("merchant_name"));
				merchantInfoBean.setMerchantDevName(rs
						.getString("merchant_dev_name"));
				merchantInfoBean.setServiceId(rs.getInt("service_id"));
				merchantInfoBean.setMerchantIp(rs.getString("merchant_ip"));
				merchantInfoBean.setProtocol(rs.getString("protocol"));
				merchantInfoBean.setProjectName(rs.getString("project_name"));
				merchantInfoBean.setPort(rs.getString("port"));
				merchantInfoBean.setPassword(rs.getString("merchant_password"));
				merchantInfoBean
						.setUserName(rs.getString("merchant_user_name"));

				merchantInfoMap.put(rs.getString("merchant_dev_name"),
						merchantInfoBean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE,
					SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,
					SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(stmt, rs);
		}
		return merchantInfoMap;
	}

	public void setGameAndGameTypeInfoMerchantMap(Connection con)
			throws SLEException {
		GameMasterBean gameMasterBean = null;
		Map<Integer, GameMasterBean> gameMap = null;
		Map<Integer, GameTypeMasterBean> gameTypeMap = null;
		GameTypeMasterBean gameTypeBean = null;
		try {
			for (Map.Entry<String, MerchantInfoBean> entry : Util.merchantInfoMap
					.entrySet()) {
				List<GameMasterBean> gameMasterList = GameDataControllerImpl
						.getInstance().getSportsLotteryOnStartServerData(
								entry.getKey());
				gameMap = new HashMap<Integer, GameMasterBean>();
				gameTypeMap = new HashMap<Integer, GameTypeMasterBean>();
				for (int i = 0; i < gameMasterList.size(); i++) {
					gameMasterBean = gameMasterList.get(i);
					gameMap.put(gameMasterBean.getGameId(), gameMasterBean);

					for (int j = 0; j < gameMasterBean.getGameTypeMasterList()
							.size(); j++) {
						gameTypeBean = gameMasterBean.getGameTypeMasterList()
								.get(j);
						gameTypeMap.put(gameTypeBean.getGameTypeId(),
								gameTypeBean);
					}
				}
				SportsLotteryUtils.gameInfoMerchantMap.put(entry.getKey(),
						gameMap);
				SportsLotteryUtils.gameTypeInfoMerchantMap.put(entry.getKey(),
						gameTypeMap);
			}
		} catch (SLEException e) {
			e.printStackTrace();
		}
	}

	public long generateSLETransaction(int merchantId, String transactioType,
			Connection con) throws SLEException {
		StringBuilder query = null;
		query = new StringBuilder();
		Statement stmt = null;
		ResultSet insertRs = null;
		int isUpdated = 0;
		long trxId = 0;
		try {
			query = new StringBuilder();
			query.append(
					"INSERT INTO st_sl_txn_master(merchant_id, trans_type) ")
					.append("VALUES(").append(merchantId).append(", '").append(
							transactioType).append("')");

			stmt = con.createStatement();
			isUpdated = stmt.executeUpdate(query.toString(),
					Statement.RETURN_GENERATED_KEYS);
			if (isUpdated == 0)
				throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE,
						SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);

			insertRs = stmt.getGeneratedKeys();
			if (insertRs.next())
				trxId = insertRs.getLong(1);
			else
				throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,
						SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} catch (SLEException e) {
			throw e;
		} catch (Exception e) {
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,
					SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(stmt, insertRs);
		}
		return trxId;
	}

	public long getNewSaleTransactionId(UserInfoBean userInfoBean,
			SportsLotteryGamePlayBean gamePlayBean, Connection con)
			throws SLEException {
		StringBuilder query = null;
		Statement stmt = null;
		int isUpdated = 0;
		long trxId = 0;
		try {
			trxId = generateSLETransaction(userInfoBean.getMerchantId(),
					"SALE", con);
			query = new StringBuilder();
			query
					.append(
							"INSERT INTO st_sl_sale_txn_master(trans_id, merchant_id, channel_type, game_id, game_type_id, ticket_nbr, user_id, merchant_user_id, trans_date, amount, STATUS, is_cancel,player_mob_number) ")
					.append("VALUES(").append(trxId).append(", ").append(
							userInfoBean.getMerchantId()).append(", '").append(
							gamePlayBean.getInterfaceType()).append("', ")
					.append(gamePlayBean.getGameId()).append(", ").append(
							gamePlayBean.getGameTypeId()).append(", ").append(
							gamePlayBean.getTicketNumber()).append(", ")
					.append(userInfoBean.getSleUserId()).append(", ").append(
							userInfoBean.getMerchantUserId()).append(", '")
					.append(gamePlayBean.getPurchaseTime()).append("', '")
					.append(gamePlayBean.getTotalPurchaseAmt()).append(
							"', 'INITIATED', 'N','").append(gamePlayBean.getPlrMobileNumber()).append("')");

			stmt = con.createStatement();
			isUpdated = stmt.executeUpdate(query.toString());
			if (isUpdated == 0)
				throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE,
						SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);

		} catch (SLEException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,
					SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeStatement(stmt);
		}
		return trxId;
	}

	public String getGameNameFromGameId(int gameId, int merchantId,
			Connection con) throws SLEException {
		StringBuilder query = null;
		query = new StringBuilder();
		Statement stmt = null;
		ResultSet rs = null;
		String gameName = null;

		try {
			query = new StringBuilder();
			query
					.append(
							"select game_disp_name from st_sl_game_merchant_mapping where game_id = ")
					.append(gameId).append(" and merchant_id = ").append(
							merchantId);
			stmt = con.createStatement();
			rs = stmt.executeQuery(query.toString());
			if (rs.next()) {
				gameName = rs.getString("game_disp_name");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,
					SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(stmt, rs);
		}
		return gameName;
	}

	public boolean isTicketCancel(UserInfoBean userBean,
			ReprintTicketBean tktBean, Connection con) throws SLEException {
		boolean isTktCancel = true;
		StringBuilder query = null;
		query = new StringBuilder();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			query = new StringBuilder();
			query
					.append("select * from st_sl_sale_txn_master where ticket_nbr = '"
							+ tktBean.getTicketNumber()
							+ "' and merchant_id = "
							+ userBean.getMerchantId()
							+ " and game_id = "
							+ tktBean.getGameId()
							+ " and game_type_id = "
							+ tktBean.getGameTypeId()
							+ " and status = 'DONE' and is_cancel = 'N'");
			stmt = con.createStatement();
			rs = stmt.executeQuery(query.toString());
			if (rs.next()) {
				isTktCancel = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,
					SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(stmt, rs);
		}
		return isTktCancel;
	}

	public boolean isTicketCancelrequested(UserInfoBean userBean,
			ReprintTicketBean tktBean, Connection con) throws SLEException {
		boolean isTktCancel = false;
		StringBuilder query = null;
		query = new StringBuilder();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			query = new StringBuilder();
			query
					.append("select * from st_sl_sale_refund_txn_master where  ticket_nbr = '"
							+ tktBean.getTicketNumber()
							+ "' and merchant_id = "
							+ userBean.getMerchantId()
							+ " and game_id = "
							+ tktBean.getGameId()
							+ " and game_type_id = "
							+ tktBean.getGameTypeId()
							+ " and status IN('DONE','INITIATED')");
			stmt = con.createStatement();
			rs = stmt.executeQuery(query.toString());
			if (rs.next()) {
				isTktCancel = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,
					SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(stmt, rs);
		}
		return isTktCancel;
	}

	public int getRoleIdFromRoleName(String roleName, Connection con)
			throws SLEException {
		int roleId = 0;
		Statement stmt = null;
		ResultSet rs = null;
		String qry = null;
		try {
			qry = "select role_id from st_rm_role_master where role_name = '"
					+ roleName + "'";
			stmt = con.createStatement();
			rs = stmt.executeQuery(qry.toString());
			if (rs.next()) {
				roleId = rs.getInt("role_id");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,
					SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(stmt, rs);
		}
		return roleId;
	}

	public List<Long> fetchAutoCancelTxn(String txnId,
			UserInfoBean userInfoBean, Connection con) throws SLEException {
		Statement stmt = null;
		ResultSet rs = null;
		List<Long> cancelTxnList = new ArrayList<Long>();
		StringBuilder query = new StringBuilder();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String txnDateString = null;
		long cancelTxnId = 0;
		PreparedStatement pStmt = null;
		try {
			stmt = con.createStatement();
			query
					.append(
							"SELECT main.trans_id, main.ticket_nbr, main.amount, main.game_id, main.game_type_id, main.trans_date FROM (SELECT sale.trans_id, sale.ticket_nbr, sale.amount, sale.game_id, sale.game_type_id, sale.trans_date FROM st_sl_sale_txn_master sale LEFT JOIN st_sl_sale_refund_txn_master refund ON sale.trans_id = refund.sale_trans_id WHERE refund.sale_trans_id IS NULL AND sale.merchant_id = ")
					.append(userInfoBean.getMerchantId())
					.append(" AND sale.merchant_user_id = ")
					.append(userInfoBean.getMerchantUserId())
					.append(" AND sale.STATUS = 'DONE' AND sale.trans_id > ")
					.append(Long.parseLong(txnId))
					.append(
							" AND sale.is_cancel = 'N' and sale.channel_type='TERMINAL' ORDER BY sale.trans_id DESC LIMIT 1) main LEFT JOIN st_sl_pending_cancel pending ON pending.sale_ref_transaction_id = main.trans_id WHERE pending.sale_ref_transaction_id IS NULL;");

			rs = stmt.executeQuery(query.toString());

			while (rs.next()) {
				txnDateString = rs.getString("trans_date");
				cancelTxnId = rs.getLong("trans_id");
				if ((new Date().getTime() - df.parse(txnDateString).getTime()) <= Integer
						.parseInt(Util
								.getPropertyValue("AUTO_CANCEL_CLOSER_DAYS"))
						* 24 * 60 * 60 * 1000) {
					cancelTxnList.add(cancelTxnId);
				} else {
					pStmt = con
							.prepareStatement("INSERT INTO st_sl_pending_cancel(sale_ref_transaction_id, ticket_nbr, mrp_amt, game_id, game_type_id, cancel_attempt_time, txn_date, reason) VALUES(?, ?, ?, ?, ?, ?, ?, ?)");
					pStmt.setLong(1, cancelTxnId);
					pStmt.setLong(2, rs.getLong("ticket_nbr"));
					pStmt.setDouble(3, rs.getDouble("amount"));
					pStmt.setInt(4, rs.getInt("game_id"));
					pStmt.setInt(5, rs.getInt("game_type_id"));
					pStmt.setTimestamp(6, Util.getCurrentTimeStamp());
					pStmt.setString(7, txnDateString);
					pStmt.setString(8, "CANCEL_EXPIRED");

					pStmt.executeUpdate();
					logger
							.info("***** Txn Id Not Added Because of Auto Closer Days "
									+ cancelTxnId);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE,
					SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,
					SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closePrepareStatementStatement(pStmt);
		}
		return cancelTxnList;
	}

	public String getCurrentDrawId(int gameId, int gameTypeId, int merchantId,
			Connection con, StringBuilder drawInfo, boolean isDrawInforeq)
			throws SLEException {
		String drawId = null;
		Statement stmt = null;
		ResultSet rs = null;
		String qry = null;
		try {
			qry = "select a.draw_id,a.draw_datetime,b.draw_name from st_sl_draw_master_"
					+ gameId
					+ " a INNER JOIN  st_sl_draw_merchant_mapping_"
					+ gameId
					+ " b ON a.draw_id = b.draw_id INNER JOIN st_sl_draw_event_mapping_"+gameId+" c on a.draw_id=c.draw_id and b.merchant_id = "
					+ merchantId
					+ " and a.game_type_id ="
					+ gameTypeId
					+ " and draw_status = 'ACTIVE' order by draw_datetime asc limit 1;";
			stmt = con.createStatement();
			rs = stmt.executeQuery(qry.toString());
			if (rs.next()) {
				drawId = rs.getString("draw_id");
				if (isDrawInforeq) {
					String draw_datetime = rs.getString("draw_datetime");
					drawInfo.append(rs.getString("draw_name")).append(",")
							.append(
									draw_datetime.substring(0, draw_datetime
											.indexOf("."))).append(",");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,
					SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(stmt, rs);
		}
		return drawId;
	}

	public String getAdvanceDrawIds(int gameId, int gameTypeId, int merchantId,
			int limit, Connection con, StringBuilder drawInfo)
			throws SLEException {
		Statement stmt = null;
		ResultSet rs = null;
		String qry = null;
		StringBuilder drawId = null;
		try {
			qry = "select a.draw_id ,a.draw_datetime,b.draw_name from st_sl_draw_master_"
					+ gameId
					+ " a, st_sl_draw_merchant_mapping_"
					+ gameId
					+ " b where a.draw_id = b.draw_id and b.merchant_id = "
					+ merchantId
					+ " and a.game_type_id ="
					+ gameTypeId
					+ " and draw_status IN ('ACTIVE','INACTIVE') and a.draw_id not in ("
					+ getCurrentDrawId(gameId, gameTypeId, merchantId, con,
							drawInfo, false)
					+ ")  and draw_freeze_time >'"+Util.getCurrentTimeStamp()+"' order by draw_datetime  limit " + limit + ";";
			stmt = con.createStatement();
			rs = stmt.executeQuery(qry.toString());
			drawId = new StringBuilder();
			while (rs.next()) {
				String draw_datetime = rs.getString("draw_datetime");
				drawId.append(rs.getString("draw_id"));
				drawId.append(",");
				drawInfo.append(rs.getString("draw_name")).append(",").append(
						draw_datetime.substring(0, draw_datetime.indexOf(".")))
						.append(",");
			}
			drawId.setLength(drawId.length()==0 ? drawId.length() : drawId.length() - 1);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,
					SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(stmt, rs);
		}
		return drawId.toString();
	}

	public int fetchRPCOfTicket(ReprintTicketBean tktBean, Connection con)
			throws SLEException {

		Statement stmt = null;
		ResultSet rs = null;
		String qry = null;
		int rpcCount = 0;
		try {
			qry = "select rpc_total from st_sl_game_tickets_"
					+ tktBean.getGameId() + "_" + tktBean.getGameTypeId()
					+ " where ticket_number = " + tktBean.getTicketNumber()
					+ ";";
			stmt = con.createStatement();
			rs = stmt.executeQuery(qry.toString());
			if (rs.next()) {
				rpcCount = rs.getInt("rpc_total");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,
					SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(stmt, rs);
		}
		return rpcCount;

	}

	public void fetchTicketInfoUsingTxnId(TicketInfoBean tktInfoBean,
			long txnId, Connection con) throws SLEException {
		StringBuilder query = new StringBuilder();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			query
					.append(
							"SELECT trans_id, game_id, game_type_id, ticket_nbr, amount, trans_date FROM st_sl_sale_txn_master WHERE trans_id = ")
					.append(txnId);
			stmt = con.createStatement();
			rs = stmt.executeQuery(query.toString());
			if (rs.next()) {
				tktInfoBean.setTxnId(rs.getLong("trans_id"));
				tktInfoBean.setGameId(rs.getInt("game_id"));
				tktInfoBean.setGameTypeId(rs.getInt("game_type_id"));
				tktInfoBean.setTktNbr(rs.getLong("ticket_nbr"));
				tktInfoBean.setAmount(rs.getDouble("amount"));
				tktInfoBean.setTxnDate(rs.getString("trans_date"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE,
					SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,
					SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(stmt, rs);
		}
	}

	public void fetchTicketInfoUsingMerchantTxnId(TicketInfoBean tktInfoBean,
			long txnId, Connection con) throws SLEException {
		StringBuilder query = new StringBuilder();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			query
					.append(
							"SELECT trans_id, game_id, game_type_id, ticket_nbr, amount, trans_date FROM st_sl_sale_txn_master WHERE merchant_trans_id = ")
					.append(txnId);
			stmt = con.createStatement();
			rs = stmt.executeQuery(query.toString());
			if (rs.next()) {
				tktInfoBean.setTxnId(rs.getLong("trans_id"));
				tktInfoBean.setGameId(rs.getInt("game_id"));
				tktInfoBean.setGameTypeId(rs.getInt("game_type_id"));
				tktInfoBean.setTktNbr(rs.getLong("ticket_nbr"));
				tktInfoBean.setAmount(rs.getDouble("amount"));
				tktInfoBean.setTxnDate(rs.getString("trans_date"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE,
					SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,
					SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(stmt, rs);
		}
	}

	public int getEngineUserIdFromUserName(UserInfoBean userInfoBean,
			Connection con) throws SLEException {

		Statement stmt = null;
		ResultSet rs = null;
		String qry = null;
		int userId = 0;
		try {
			qry = "select user_id from st_sl_merchant_user_master where user_name = '"
					+ userInfoBean.getUserName()
					+ "' and merchant_id = "
					+ userInfoBean.getMerchantId();
			stmt = con.createStatement();
			rs = stmt.executeQuery(qry.toString());
			if (rs.next()) {
				userId = rs.getInt("user_id");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,
					SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(stmt, rs);
		}
		return userId;

	}

	public static Map<Integer, GameMasterBean> getGameMap(int merchantId,
			Connection connection) throws SLEException {
		Statement stmt = null;
		ResultSet rs = null;
		String query = null;
		Map<Integer, GameMasterBean> gameMasterMap = null;
		GameMasterBean gameMasterBean = null;
		try {
			gameMasterMap = new LinkedHashMap<Integer, GameMasterBean>();
			stmt = connection.createStatement();
			query = "select a.game_id, a.game_dev_name, b.game_disp_name, b.display_order, b.game_status from st_sl_game_master a, st_sl_game_merchant_mapping b WHERE a.game_id=b.game_id AND a.game_status='SALE_OPEN' AND b.game_status='SALE_OPEN' and merchant_id = "
					+ merchantId + " ORDER BY display_order;";
			logger.info("GameMasterQuery - " + query);
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				gameMasterBean = new GameMasterBean();
				int gameId = rs.getInt("game_id");
				gameMasterBean.setGameId(gameId);
				gameMasterBean.setGameDevName(rs.getString("game_dev_name"));
				gameMasterBean.setGameDispName(rs.getString("game_disp_name"));
				gameMasterBean.setDisplayOrder(rs.getInt("display_order"));
				gameMasterBean.setGameStatus(rs.getString("game_status"));
				gameMasterBean.setGameTypeMasterList(getGameTypeList(gameId,
						merchantId, connection));
				gameMasterMap.put(gameId, gameMasterBean);
			}
		} catch (SQLException se) {
			se.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,
					SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} catch (SLEException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,
					SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(stmt, rs);
		}
		return gameMasterMap;
	}

	public static List<GameTypeMasterBean> getGameTypeList(int gameId,
			int merchantId, Connection connection) throws SLEException {
		Statement stmt = null;
		ResultSet rs = null;
		String query = null;
		List<GameTypeMasterBean> gameTypeMasterList = null;
		GameTypeMasterBean gameTypeMasterBean = null;
		try {
			gameTypeMasterList = new ArrayList<GameTypeMasterBean>();
			stmt = connection.createStatement();
			query = "select a.game_type_id, a.game_id, a.type_dev_name, b.type_disp_name, b.unit_price, sale_start_time, sale_end_time, display_order, b.type_status   from st_sl_game_type_master a, st_sl_game_type_merchant_mapping b where a.game_id = "
					+ gameId
					+ " and a.game_type_id = b.game_type_id and b.merchant_id = "
					+ merchantId
					+ " and a.type_status = b.type_status and a.type_status = 'SALE_OPEN'";
			logger.info("GameMasterQuery - " + query);
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				gameTypeMasterBean = new GameTypeMasterBean();
				int gameTypeId = rs.getInt("game_type_id");
				gameTypeMasterBean.setGameTypeId(gameTypeId);
				gameTypeMasterBean.setGameId(gameId);
				gameTypeMasterBean.setGameTypeDevName(rs
						.getString("type_dev_name"));
				gameTypeMasterBean.setGameTypeDispName(rs
						.getString("type_disp_name"));
				gameTypeMasterBean.setDisplayOrder(rs.getInt("display_order"));
				gameTypeMasterBean.setGameTypeStatus(rs
						.getString("type_status"));
				gameTypeMasterList.add(gameTypeMasterBean);
			}
		} catch (SQLException se) {
			se.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,
					SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,
					SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(stmt, rs);
		}

		return gameTypeMasterList;
	}

	/**
	 * <p>This method fetches information of all the leagues stored in st_sl_league_master table that are
	 * active & stores it in a Hashmap and finally returns the map.</p>
	 * @param connection
	 * @return leagueMasterMap
	 * @throws SLEException
	 * @author Nikhil
	 */
	
	public static Map<Integer, LeagueMasterBean> getLeagueMap(Connection connection) throws SLEException {
		Statement stmt = null;
		ResultSet rs = null;
		String query = null;
		Map<Integer, LeagueMasterBean> leagueMasterMap = null;
		LeagueMasterBean leagueMasterBean = null;
		try {
			leagueMasterMap = new LinkedHashMap<Integer, LeagueMasterBean>();
			stmt = connection.createStatement();
			query = "select league_id, league_type, league_dev_name, league_display_name from st_sl_league_master where status = 'ACTIVE' order by league_display_name;";
			logger.info("LeagueMasterQuery - " + query);
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				leagueMasterBean = new LeagueMasterBean();
				int leagueId = rs.getInt("league_id");
				leagueMasterBean.setLeagueId(leagueId);
				leagueMasterBean.setLeagueType(rs.getString("league_type"));
				leagueMasterBean.setLeagueDevName(rs.getString("league_dev_name"));
				leagueMasterBean.setLeagueDispName(rs.getString("league_display_name"));
				leagueMasterMap.put(leagueId, leagueMasterBean);
			}
		} catch (SQLException se) {
			se.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(stmt, rs);
		}
		return leagueMasterMap;
	}
	/**
	 * This method overloads getLeagueMap(Connection connection).This method fetches data for all the leagues present in the
	 * database and stores it in leagueMap hashmap.
	 * @param connection
	 * @param leagueMap
	 * @throws SLEException
	 * @since 15-Oct-2015
	 * @author Rishi
	 */
	public static void getLeagueMap(Connection connection,Map<Integer, LeagueMasterBean> leagueMap) throws SLEException {
		Statement stmt = null;
		ResultSet rs = null;
		String query = null;
		LeagueMasterBean leagueMasterBean = null;
		try {
			stmt = connection.createStatement();
			query = "select league_id, league_type, league_dev_name, league_display_name from st_sl_league_master where status = 'ACTIVE' order by league_display_name;";
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				leagueMasterBean = new LeagueMasterBean();
				int leagueId = rs.getInt("league_id");
				leagueMasterBean.setLeagueId(leagueId);
				leagueMasterBean.setLeagueType(rs.getString("league_type"));
				leagueMasterBean.setLeagueDevName(rs.getString("league_dev_name"));
				leagueMasterBean.setLeagueDispName(rs.getString("league_display_name"));
				leagueMap.put(leagueId, leagueMasterBean);
			}
		} catch (SQLException se) {
			se.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(stmt, rs);
		}
	}
	public static Map<Integer, TeamMasterBean> getTeamMap(Connection connection) throws SLEException {
		Statement stmt = null;
		ResultSet rs = null;
		String query = null;
		Map<Integer, TeamMasterBean> teamMasterMap = null;
		TeamMasterBean teamMasterBean = null;
		try {
			teamMasterMap = new LinkedHashMap<Integer, TeamMasterBean>();
			stmt = connection.createStatement();
			query = "select team_id, game_id, team_name, team_code, status from st_sl_game_team_master where status = 'ACTIVE';";
			logger.info("TeamMasterQuery - " + query);
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				teamMasterBean = new TeamMasterBean();
				int teamId = rs.getInt("team_id");
				teamMasterBean.setTeamId(teamId);
				teamMasterBean.setGameId(rs.getInt("game_id"));
				teamMasterBean.setTeamName(rs.getString("team_name"));
				teamMasterBean.setTeamCode(rs.getString("team_code"));
				teamMasterBean.setStatus(rs.getString("status"));
				teamMasterMap.put(teamId, teamMasterBean);
			}
		} catch (SQLException se) {
			se.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,
					SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,
					SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(stmt, rs);
		}
		return teamMasterMap;
	}
	

	public Map<Integer, GameMasterBean> getGameMap(
			MerchantInfoBean merchantInfoBean) throws SLEException {
		Connection connection = DBConnect.getConnection();
		Map<Integer, GameMasterBean> gameMap = CommonMethodsDaoImpl.getGameMap(
				merchantInfoBean.getMerchantId(), connection);
		DBConnect.closeConnection(connection);
		return gameMap;
	}

	public Map<Integer, LeagueMasterBean> getLeagueMap() throws SLEException {
		Connection connection = DBConnect.getConnection();
		Map<Integer, LeagueMasterBean> leagueMap = CommonMethodsDaoImpl
				.getLeagueMap(connection);
		DBConnect.closeConnection(connection);
		return leagueMap;
	}
	
	public List<CountryDataBean> getCountryListMap() throws SLEException {

		PreparedStatement statePstmt = null;
		ResultSet stateRs = null;
		PreparedStatement cityPstmt = null;
		ResultSet cityRs = null;
		PreparedStatement countryPstmt = null;
		ResultSet countryRs = null;
		Connection con = null;
		StateDataBean stateBean = null;
		CountryDataBean countryBean = null;
		CityDataBean cityBean = null;
		Map<String, List<CityDataBean>> cityBeanListMap = null;
		Map<String, List<StateDataBean>> stateBeanListMap = null;
		List<CountryDataBean> countryBeanList = null;
		try {
			con = DBConnect.getConnection();
			countryBeanList = new ArrayList<CountryDataBean>();
			cityPstmt = con.prepareStatement(SLEMySqlQueries.GET_CITY_DETAILS);
			logger.info("CityPstmt: " + cityPstmt);
			cityRs = cityPstmt.executeQuery();
			cityBeanListMap = new HashMap<String, List<CityDataBean>>();
			List<CityDataBean> cityBeanList = null;
			while (cityRs.next()) {
				if (!cityBeanListMap
						.containsKey(cityRs.getString("state_code")))
					cityBeanList = new ArrayList<CityDataBean>();
				cityBean = new CityDataBean();
				cityBean.setCityCode(cityRs.getString("city_code"));
				cityBean.setCityName(cityRs.getString("city_name"));
				cityBeanList.add(cityBean);
				cityBeanListMap.put(cityRs.getString("state_code"),
						cityBeanList);
			}

			statePstmt = con
					.prepareStatement(SLEMySqlQueries.GET_STATE_DETAILS);
			logger.info("StatePstmt: " + statePstmt);
			stateRs = statePstmt.executeQuery();
			stateBeanListMap = new HashMap<String, List<StateDataBean>>();
			List<StateDataBean> stateBeanList = null;
			while (stateRs.next()) {
				if (!stateBeanListMap.containsKey(stateRs
						.getString("country_code")))
					stateBeanList = new ArrayList<StateDataBean>();
				stateBean = new StateDataBean();
				stateBean.setStateCode(stateRs.getString("state_code"));
				stateBean.setStateName(stateRs.getString("state_name"));
				stateBean.setCityBeanList(cityBeanListMap.get(stateRs
						.getString("state_code")));
				stateBeanList.add(stateBean);
				stateBeanListMap.put(stateRs.getString("country_code"),
						stateBeanList);
			}

			countryPstmt = con
					.prepareStatement(SLEMySqlQueries.GET_COUNTRY_DETAILS);
			logger.info("CountryPstmt: " + countryPstmt);
			countryRs = countryPstmt.executeQuery();
			while (countryRs.next()) {
				countryBean = new CountryDataBean();
				countryBean.setCountryCode(countryRs.getString("country_code"));
				countryBean.setCountryName(countryRs.getString("country_name"));
				countryBean.setStateBeanList(stateBeanListMap.get(countryRs
						.getString("country_code")));
				countryBeanList.add(countryBean);
			}
		} catch (SQLException se) {
			se.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,
					SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,
					SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(con);
			DBConnect.closePrepareStatementStatements(statePstmt, cityPstmt,
					countryPstmt);
			DBConnect.closeRs(stateRs);
			DBConnect.closeRs(countryRs);
			DBConnect.closeRs(cityRs);
		}

		return countryBeanList;
	}

	public boolean checkMerchantUserExistence(int userId, String merchantCode,
			Connection con) throws SLEException {
		Statement stmt = null;
		ResultSet rs = null;
		boolean isUserExists = false;

		try {
			stmt = con.createStatement();
			if ("RMS".equals(merchantCode))
				rs = stmt
						.executeQuery("select merchant_user_mapping_id from st_sl_merchant_user_master where merchant_user_mapping_id = "
								+ userId);
			else if ("PMS".equals(merchantCode))
				rs = stmt
						.executeQuery("select merchant_user_id from st_sl_merchant_user_master where merchant_user_id = "
								+ userId);
			else if ("Asoft".equals(merchantCode))
				rs = stmt
						.executeQuery("select user_id from st_sl_merchant_player_master where user_id = "
								+ userId);
			else if ("OKPOS".equals(merchantCode))
				rs = stmt
						.executeQuery("select user_id from st_sl_merchant_retailer_master where user_id = "
								+ userId);
			if (rs.next())
				isUserExists = true;
		} catch (SQLException e) {
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE,
					SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,
					SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(stmt, rs);
		}
		return isUserExists;
	}

	public void fetchTktinfo(String tktNmb, ValidateTicketBean tktBean,
			Connection con) throws SLEException {
		Statement stmt = null;
		ResultSet rs = null;
		String query = "SELECT stm.trans_id,stm.merchant_user_id,stm.game_id, stm.game_type_id, gmm.game_disp_name, gtmm.type_disp_name, ticket_nbr,stm.is_cancel FROM st_sl_sale_txn_master stm INNER JOIN st_sl_game_merchant_mapping gmm ON stm.game_id = gmm.game_id AND stm.merchant_id=gmm.merchant_id INNER JOIN st_sl_game_type_merchant_mapping gtmm ON stm.game_type_id = gtmm.game_type_id AND stm.merchant_id=gtmm.merchant_id WHERE ticket_nbr = '"
				+ tktNmb
				+ "' AND stm.merchant_id = "
				+ Util.merchantInfoMap.get(tktBean.getMerchantCode())
						.getMerchantId() + " and stm.status='DONE'";

		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			if (rs.next()) {
				tktBean.setMerchantUserId(rs.getInt("merchant_user_id"));
				tktBean.setTransId(rs.getLong("trans_id"));
				tktBean.setGameid(rs.getInt("game_id"));
				tktBean.setGameTypeId(rs.getInt("game_type_id"));
				tktBean.setGameName(rs.getString("game_disp_name"));
				tktBean.setGameTypeName(rs.getString("type_disp_name"));
				tktBean.setTicketNumInDB(rs.getString("ticket_nbr"));
				tktBean.setIsCancel(rs.getString("is_cancel"));
			} else {
				throw new SLEException(SLEErrors.INVALID_TICKET_ERROR_CODE,
						SLEErrors.INVALID_TICKET_ERROR_MESSAGE);
			}

			rs = stmt
					.executeQuery("SELECT IFNULL(rpc_total, 0) rpc,claim_end_date FROM st_sl_game_tickets_"
							+ tktBean.getGameid()
							+ "_"
							+ tktBean.getGameTypeId()+" gt "
							+ "INNER JOIN st_sl_draw_merchant_mapping_"+tktBean.getGameid()+" dmm ON gt.draw_id = dmm.draw_id WHERE ticket_number = '" + tktNmb + "' AND trans_id="+tktBean.getTransId()+" AND merchant_id ="+Util.merchantInfoMap.get(tktBean.getMerchantCode()).getMerchantId());
			if (rs.next()) {
				tktBean.setReprintCount(rs.getInt("rpc"));
				tktBean.setDrawClaimEndDate(rs.getString("claim_end_date"));
			} else {
				throw new SLEException(SLEErrors.INVALID_TICKET_ERROR_CODE,
						SLEErrors.INVALID_TICKET_ERROR_MESSAGE);
			}
		} catch (SQLException e) {
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE,
					SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (SLEException se) {
			throw se;
		} catch (Exception e) {
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,
					SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(stmt, rs);
		}
	}

	public void fetchGameDataMerchantWise(
			Map<Integer, GameMasterBean> gameDataMap, String merchantCode,
			Connection con) throws SLEException {
		Statement stmt = null;
		ResultSet rs = null;
		String query = "SELECT table1.game_disp_name, table1.game_id, gtm.game_type_id, gtmm.type_disp_name FROM (SELECT gmm.game_disp_name, gmm.game_id, mm.merchant_id FROM st_sl_game_merchant_mapping gmm INNER JOIN st_sl_merchant_master mm ON mm.merchant_id = gmm.merchant_id WHERE mm.merchant_dev_name = '"
				+ merchantCode
				+ "') table1 INNER JOIN st_sl_game_type_master gtm ON table1.game_id = gtm.game_id INNER JOIN st_sl_game_type_merchant_mapping gtmm ON gtm.game_type_id = gtmm.game_type_id  AND table1.merchant_id = gtmm.merchant_id;";

		GameMasterBean gameMasterBean = null;

		List<GameTypeMasterBean> gameTypeMasterBeans = null;
		GameTypeMasterBean gameTypeMasterBean = null;

		int gameId = 0;
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				gameId = rs.getInt("game_id");
				if (gameDataMap.containsKey(gameId)) {
					gameMasterBean = gameDataMap.get(gameId);
					gameTypeMasterBeans = gameMasterBean.getGameTypeMasterList();
				} else {
					gameMasterBean = new GameMasterBean();
					gameDataMap.put(gameId, gameMasterBean);

					gameMasterBean.setGameDispName(rs
							.getString("game_disp_name"));
					gameMasterBean.setGameId(rs.getInt("game_id"));

					gameTypeMasterBeans = new ArrayList<GameTypeMasterBean>();
					gameMasterBean.setGameTypeMasterList(gameTypeMasterBeans);
				}

				gameTypeMasterBean = new GameTypeMasterBean();
				gameTypeMasterBean.setGameTypeDispName(rs
						.getString("type_disp_name"));
				gameTypeMasterBean.setGameTypeId(rs.getInt("game_type_id"));

				gameTypeMasterBeans.add(gameTypeMasterBean);

			}
		} catch (SQLException e) {
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE,
					SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,
					SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
	}

	public Map<Integer, Map<Integer, Double>> getJackpotMap(
			Connection connection) throws SLEException {
		Statement stmt = null;
		ResultSet rs = null;

		Map<Integer, Map<Integer, Double>> jackpotMap = null;
		Map<Integer, Double> gameTypeMap = null;
		try {
			jackpotMap = new TreeMap<Integer, Map<Integer, Double>>();

			stmt = connection.createStatement();
			String gameQuery = "SELECT gtm.game_id, game_type_id FROM st_sl_game_master gm INNER JOIN st_sl_game_type_master gtm ON gm.game_id=gtm.game_id WHERE gm.game_status='SALE_OPEN' AND gtm.type_status='SALE_OPEN';";
			logger.debug("gameQuery" + gameQuery);
			rs = stmt.executeQuery(gameQuery);
			while (rs.next()) {
				gameTypeMap = jackpotMap.get(rs.getInt("game_id"));
				if (gameTypeMap == null) {
					gameTypeMap = new TreeMap<Integer, Double>();
				}
				gameTypeMap.put(rs.getInt("game_type_id"), 0.00);
				jackpotMap.put(rs.getInt("game_id"), gameTypeMap);
			}

			for (Map.Entry<Integer, Map<Integer, Double>> entrySet : jackpotMap
					.entrySet()) {
				int gameId = entrySet.getKey();
				gameTypeMap = entrySet.getValue();
				for (int gameTypeId : gameTypeMap.keySet()) {
					rs = stmt
							.executeQuery("SELECT carried_over_jackpot FROM st_sl_miscellaneous_"
									+ gameId
									+ "_"
									+ gameTypeId
									+ " ORDER BY id DESC LIMIT 1;");
					if (rs.next()) {
						gameTypeMap.put(gameTypeId, rs
								.getDouble("carried_over_jackpot"));
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return jackpotMap;
	}

	public boolean isSessionAlreadyExist(String sessionId, Connection con)
			throws SLEException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String query = "SELECT * from st_sl_merchant_user_authentication_mapping WHERE session_id = ?";
		boolean isSessionExist = false;
		try {
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, sessionId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				isSessionExist = true;
			}
		} catch (SQLException e) {
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE,
					SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,
					SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}

		return isSessionExist;
	}

	public long fetchMaxTxnId(int merchantId, Connection con)
			throws SLEException {
		long lastTxnId = 0;
		Statement stmt = null;
		ResultSet rs = null;
		String query = null;
		try {
			query = "SELECT IFNULL(MAX(trans_id), 0) max_txn_id FROM st_sl_txn_master WHERE merchant_id = "
					+ merchantId + ";";
			logger.info("Query For Fetching Max Txn Id " + query);
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			if (rs.next())
				lastTxnId = rs.getLong("max_txn_id");
			else
				throw new SLEException(
						SLEErrors.INVALID_MERCHANT_NAME_ERROR_CODE,
						SLEErrors.INVALID_MERCHANT_NAME_ERROR_MESSAGE);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE,
					SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,
					SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return lastTxnId;
	}

	public UserInfoBean getMerchantMasterUser(int merchantId,
			Connection connection) throws SLEException {
		UserInfoBean userBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			String query = "SELECT user_id, merchant_user_id FROM st_sl_merchant_user_master WHERE merchant_id="
					+ merchantId + " AND is_role_head='Y' LIMIT 1;";
			stmt = connection.createStatement();
			rs = stmt.executeQuery(query);
			if (rs.next()) {
				userBean = new UserInfoBean();
				userBean.setSleUserId(rs.getInt("user_id"));
				userBean.setMerchantUserId(rs.getInt("merchant_user_id"));
			}
		} catch (SQLException e) {
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE,
					SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,
					SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}

		return userBean;
	}

	public String checkSaleWinningClaimCondition(int saleMerchantId,
			int winningMerchantId, Connection connection) throws SLEException {
		Statement stmt = null;
		ResultSet rs = null;
		String isDateBypass = null;
		try {
			String query = "SELECT is_date_bypass FROM st_sl_sale_winning_claim_condition WHERE sale_merchant_id="
					+ saleMerchantId
					+ " AND winning_merchant_id="
					+ winningMerchantId + " AND STATUS='ACTIVE';";
			stmt = connection.createStatement();
			rs = stmt.executeQuery(query);
			if (rs.next()) {
				isDateBypass = rs.getString("is_date_bypass");
			}
		} catch (SQLException e) {
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE,
					SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,
					SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}

		return isDateBypass;
	}

	public Map<Integer, DrawDetailBean> setEventDataDrawWise(int gameId, int gameTypeId, int merchantId, Connection con) throws SLEException {
		Statement drawStmt = null;
		ResultSet drawRs = null;
		PreparedStatement eventPstmt = null;
		ResultSet eventRs = null;
		Map<Integer, DrawDetailBean> drawDetailMap = null;
		DrawDetailBean drawDetailBean = null;

		Map<Integer, Map<String, EventDetailBean>> drawEventDetailMap = null;
		Map<String, EventDetailBean> eventDetailMap = null;
		EventDetailBean eventDetailBean = null;
		String query = null;
		try {
			drawStmt = con.createStatement();

			query = "select aa.draw_id, draw_no, draw_datetime, draw_freeze_time, sale_start_time, purchase_table_name, draw_name, draw_display_type, claim_start_date, claim_end_date, verification_date from st_sl_draw_master_"
					+ gameId
					+ " aa inner join st_sl_draw_merchant_mapping_"
					+ gameId
					+ " bb on aa.draw_id=bb.draw_id where aa.game_type_id="
					+ gameTypeId + " AND draw_status='ACTIVE' and merchant_id="+merchantId+"";

			logger.info("Query For Fetching Active Draw Data " + query);

			drawRs = drawStmt.executeQuery(query);

			drawDetailMap = new LinkedHashMap<Integer, DrawDetailBean>();
			while (drawRs.next()) {
				drawDetailBean = new DrawDetailBean();
				drawDetailBean.setDrawId(drawRs.getInt("draw_id"));
				drawDetailBean.setDrawNo(drawRs.getInt("draw_no"));
				drawDetailBean.setVerificationStartTime(drawRs
						.getString("verification_date"));
				drawDetailBean.setDrawClaimStartTime(drawRs
						.getString("claim_start_date"));
				drawDetailBean.setDrawClaimEndTime(drawRs
						.getString("claim_end_date"));
				drawDetailBean.setDrawSaleStartTime(drawRs.getString("sale_start_time"));
				String drawDisplay = null;
				drawDisplay = drawRs.getString("draw_name");
	
				drawDetailBean.setDrawDisplay(drawDisplay);
				drawDetailBean.setDrawDateTime(Util.getDateTimeFormat(drawRs
						.getTimestamp("draw_datetime")));
				drawDetailBean.setPurchaseTableName(drawRs
						.getInt("purchase_table_name"));

				query = "select distinct aa.event_id,event_option_id,draw_id,event_display,event_description, home_team_name, away_team_name, league_display_name, venue_display_name, start_time,end_time,option_name,option_code,event_draw_id,home_team_code,away_team_code from(select event.event_id,evt_opt_id event_option_id,event_display,event_description,(SELECT team_code FROM st_sl_game_team_master WHERE team_id = home_team_id) home_team_code, (SELECT team_code FROM st_sl_game_team_master WHERE team_id = away_team_id) away_team_code, (SELECT team_name FROM st_sl_game_team_master WHERE team_id = home_team_id) home_team_name, (SELECT team_name FROM st_sl_game_team_master WHERE team_id = away_team_id) away_team_name, league_display_name, venue_display_name, start_time,end_time,option_name,option_code from st_sl_event_master event inner join st_sl_league_master lm on lm.league_id = event.league_id inner join st_sl_venue_master vm on event.venue_id = vm.venue_id inner join st_sl_event_option_mapping op on event.event_id=op.event_id where op.game_id="
						+ gameId
						+ " and op.game_type_id="
						+ gameTypeId
						+ " and is_displayable='YES')aa inner join st_sl_draw_event_mapping_"
						+ gameId
						+ " bb on aa.event_id=bb.event_id and draw_id = "
						+ drawRs.getInt("draw_id")
						+ " order by event_draw_id,event_order";

				logger.info("Query For Fetching Event Data Draw Wise " + query);
				
				eventPstmt = con.prepareStatement(query);

				eventRs = eventPstmt.executeQuery();
				drawEventDetailMap = new HashMap<Integer, Map<String, EventDetailBean>>();

				while (eventRs.next()) {
					if (!drawEventDetailMap.containsKey(eventRs
							.getInt("event_id"))) {
						eventDetailMap = new HashMap<String, EventDetailBean>();
						drawEventDetailMap.put(eventRs.getInt("event_id"),
								eventDetailMap);
					}
					eventDetailBean = new EventDetailBean();
					eventDetailBean.setEventDescription(eventRs
							.getString("event_description"));
					eventDetailBean.setEventDisplay(eventRs
							.getString("event_display"));
					eventDetailBean.setEventId(eventRs.getInt("event_id"));
					eventDetailBean.setEventOptionId(eventRs
							.getInt("event_option_id"));
					eventDetailBean.setHomeTeamName(eventRs
							.getString("home_team_name"));
					eventDetailBean.setAwayTeamName(eventRs
							.getString("away_team_name"));
					eventDetailBean.setLeagueName(eventRs
							.getString("league_display_name"));
					eventDetailBean.setVenueName(eventRs
							.getString("venue_display_name"));
					eventDetailBean.setEventDateTime(eventRs
							.getString("start_time"));
					eventDetailBean.setOptionName(eventRs
							.getString("option_name"));
					eventDetailBean.setAwayTeamCode(eventRs
							.getString("away_team_code"));
					eventDetailBean.setHomeTeamCode(eventRs
							.getString("home_team_code"));
					eventDetailMap.put(eventRs.getString("option_code"),
							eventDetailBean);
				}
				drawDetailBean.setDrawEventDetail(drawEventDetailMap);
				drawDetailMap.put(drawRs.getInt("draw_id"), drawDetailBean);
			}
		} catch (SQLException se) {
			se.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE,
					SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,
					SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return drawDetailMap;
	}

	public Map<Integer, SLPrizeRankDistributionBean> fetchPrizeDistributionData(
			int merchantId, int gameTypeId) throws SLEException {
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		Map<Integer, SLPrizeRankDistributionBean> prizeRankMap = null;
		List<SLPrizeRankDistributionBean> prizeRankDistributionList = null;
		try {
			con = DBConnect.getConnection();
			stmt = con.createStatement();
			rs = stmt
					.executeQuery("SELECT SQL_CACHE game_type_id, prize_distribution_xml FROM st_sl_game_type_merchant_mapping game inner join st_sl_merchant_master merchant on game.merchant_id = merchant.merchant_id WHERE merchant.merchant_id = "
							+ merchantId
							+ " and game_type_id="
							+ gameTypeId
							+ " AND merchant.status = 'ACTIVE';");
			while (rs.next()) {
				prizeRankDistributionList = new Gson().fromJson(rs
						.getString("prize_distribution_xml"),
						new TypeToken<List<SLPrizeRankDistributionBean>>() {
						}.getType());
				if (prizeRankDistributionList != null) {
					prizeRankMap = new HashMap<Integer, SLPrizeRankDistributionBean>();
					for (SLPrizeRankDistributionBean bean : prizeRankDistributionList) {
						prizeRankMap.put(bean.getPrizeRank(), bean);
					}
				}
			}
		} catch (SQLException se) {
			se.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE,
					SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,
					SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(con);
		}
		return prizeRankMap;
	}

	public Map<Integer, Double> getMerchantGameTypeDrawSale(int merchantId,
			int gameId, int gameTypeId, Connection connection)
			throws SLEException {
		Statement stmt = null;
		ResultSet rs = null;
		ResultSet saleResultSet = null;
		PreparedStatement salePstmt = null;
		Map<Integer, Double> drawSaleMap = new HashMap<Integer, Double>();
		String query = null;
		try {
			stmt = connection.createStatement();
			query = "SELECT dm.draw_id, dm.purchase_table_name FROM st_sl_draw_master_"
					+ gameId
					+ " dm INNER JOIN st_sl_draw_merchant_mapping_"
					+ gameId
					+ " dmm ON dm.draw_id = dmm.draw_id WHERE draw_status = 'ACTIVE' AND merchant_id = "
					+ merchantId;
			logger.debug("Query For Fetching Active Draws Merchant Wise "
					+ query);
			query = "SELECT dm.draw_id, dm.purchase_table_name FROM st_sl_draw_master_"+gameId+" dm INNER JOIN st_sl_draw_merchant_mapping_" + gameId + " dmm ON dm.draw_id = dmm.draw_id WHERE draw_status = 'ACTIVE' AND game_type_id="+gameTypeId+" and merchant_id = " + merchantId;
			logger.debug("Query For Fetching Active Draws Merchant Wise " + query);
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				query = "select ifnull(sum(bet_amount_multiple*unit_price),0.0) sale from st_sl_draw_ticket_"
						+ gameId
						+ "_"
						+ gameTypeId
						+ "_"
						+ rs.getInt("purchase_table_name")
						+ " where draw_id="
						+ rs.getInt("draw_id")
						+ " and merchant_id="
						+ merchantId + " and status<> 'CANCELLED'";
				logger
						.debug("Query For Fetching Pool Sale Amount Merchant wise and Draw Wise "
								+ query);
				salePstmt = connection.prepareStatement(query);

				saleResultSet = salePstmt.executeQuery();
				if (saleResultSet.next()) {
					drawSaleMap.put(rs.getInt("draw_id"), saleResultSet
							.getDouble("sale"));
				}
			}
		} catch (SQLException se) {
			se.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE,
					SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,
					SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return drawSaleMap;
	}

	public List<AuditTrailBean> fetchAuditTrailData(int userId, int merchantId,
			String startTime, String endTime, Connection con)
			throws SLEException {
		List<AuditTrailBean> auditTrailBeans = new ArrayList<AuditTrailBean>();
		AuditTrailBean auditTrailBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		StringBuilder query = new StringBuilder();
		try {
			stmt = con.createStatement();

			query
					.append(
							"SELECT mapping.priv_disp_name, audit.user_name login_name, audit.access_ip, audit.request_time, CONCAT(um.first_name, ' ', um.last_name) NAME FROM st_rm_priv_action_mapping priv INNER JOIN st_lms_audit_user_access_history audit ON audit.priv_id = priv.priv_id INNER JOIN st_rm_merchant_priv_mapping mapping ON mapping.priv_id = priv.priv_id INNER JOIN st_sl_merchant_user_master um ON audit.user_id = um.merchant_user_id WHERE mapping.merchant_id = ")
					.append(merchantId)
					.append(
							" AND priv.is_audit_trail_display = 'Y' AND audit.request_time >= '")
					.append(startTime).append("' AND audit.request_time <= '")
					.append(endTime).append("' AND um.merchant_id = ").append(
							merchantId).append(" AND um.merchant_user_id = ")
					.append(userId).append(";");

			rs = stmt.executeQuery(query.toString());
		
			while (rs.next()) {
				auditTrailBean = new AuditTrailBean();
				auditTrailBean.setActivity(rs.getString("priv_disp_name"));
				auditTrailBean.setLoginName(rs.getString("login_name"));
				auditTrailBean.setAccessIp(rs.getString("access_ip"));
				auditTrailBean.setUserName(rs.getString("name"));
				auditTrailBean.setAccessTime(df.format(df.parse(rs
						.getString("request_time"))));
				auditTrailBeans.add(auditTrailBean);
			}
		} catch (SQLException se) {
			se.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE,
					SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,
					SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return auditTrailBeans;
	}

	public Map<String, String> fetchGameTypeOptionMap(int gameId,
			int gameTypeId, Connection con) throws SLEException {
		Statement stmt = null;
		ResultSet rs = null;
		Map<String, String> optionMap = null;
		try {
			optionMap = new LinkedHashMap<String, String>();
			String query = "select option_dev_name,option_disp_name from st_sl_option_master om inner join st_sl_game_type_option_mapping gm on om.option_id=gm.option_id where game_id="
					+ gameId
					+ " and game_type_id="
					+ gameTypeId
					+ " and status='ACTIVE' ORDER BY display_order";
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				optionMap.put(rs.getString("option_dev_name").trim(), rs
						.getString("option_disp_name"));
			}
		} catch (SQLException e) {
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE,
					SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,
					SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeStatement(stmt);
		}

		return optionMap;
	}

	public List<String> fetchGameTypeOptionList(int gameId, int gameTypeId,
			Connection con) throws SLEException {
		Statement stmt = null;
		ResultSet rs = null;
		List<String> optionList = null;
		try {
			optionList = new ArrayList<String>();
			String query = "select option_dev_name from st_sl_game_type_option_mapping gtom  INNER JOIN st_sl_option_master om on gtom.option_id =om.option_id where game_id="
					+ gameId
					+ " and game_type_id="
					+ gameTypeId
					+ " and option_dev_name!='C' order by display_order";
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				optionList.add(rs.getString("option_dev_name"));
			}
		} catch (SQLException e) {
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE,
					SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,
					SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeStatement(stmt);
		}

		return optionList;
	}
	
	public  String fetchTicketWiseDrawStatus(int gameId, int gameTypeId,String tktNo, Connection con) throws SLEException {
		Statement stmt = null;
		ResultSet rs = null;
		String drawStatus = null;

		try {
			String query = " select gt.draw_id,draw_status from st_sl_game_tickets_"+ gameId+ "_"+ gameTypeId+ "  gt INNER JOIN st_sl_draw_master_"+ gameId+ "  dm on dm.draw_id=gt.draw_id where ticket_number="+ tktNo;
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				drawStatus = rs.getString("draw_status");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeConnection(stmt, rs);
		}

		return drawStatus;
	}
	public double fetchWinningAmtForTicket(String ticketNbrInDB ,Connection con) throws SLEException {
		Statement stmt = null;
		ResultSet rs = null;
		double winAmt = 0.0;
		
		try {
			String query ="select net_amount as totalWinAmt from st_sl_winning_txn_master where ticket_nbr="+ticketNbrInDB;
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			while(rs.next()){
				winAmt = rs.getDouble("totalWinAmt");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			DBConnect.closeConnection(stmt, rs);
			}
		return winAmt;
		
	}
	public String getUpcomingDrawDetail(int gameId, int gameTypeId ,Connection con){
		Statement stmt = null;
		ResultSet rs = null;
		StringBuilder upcomingDrawDetails = new StringBuilder();
		try{
			String query = "SELECT sale_start_time, draw_datetime, draw_id FROM st_sl_draw_master_"+gameId+" WHERE  game_type_id =" +gameTypeId+ " AND sale_start_time >now() AND draw_status IN('ACTIVE','INACTIVE') ORDER BY sale_start_time  LIMIT 1";
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);
            if(rs.next()) {
            	upcomingDrawDetails.append(rs.getInt("draw_id")).append("~").append(Util.convertDateTimeToResponseFormat2(rs.getString("sale_start_time")));
			}
           
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			DBConnect.closeConnection(stmt, rs);
		}
		return upcomingDrawDetails.toString();
	}
	
	public String getUpcomingDrawDetailForTerminal(int gameId, int gameTypeId){
		Statement stmt = null;
		Connection con = null;
		ResultSet rs = null;
		StringBuilder upcomingDrawDetails = new StringBuilder();
		try{
			con = DBConnect.getConnection();
			String query = "SELECT sale_start_time, draw_datetime, draw_id FROM st_sl_draw_master_"+gameId+" WHERE  game_type_id =" +gameTypeId+ " AND sale_start_time > '"+Util.getCurrentTimeString()+"' AND draw_status IN('ACTIVE','INACTIVE') ORDER BY sale_start_time  LIMIT 1";
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);
            if(rs.next()) {
            	upcomingDrawDetails.append(rs.getString("sale_start_time").replace(".0", ""));
			}
           
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			DBConnect.closeConnection(con);
			DBConnect.closeConnection(stmt, rs);
		}
		return upcomingDrawDetails.toString();
	}
	
	public boolean isEventAvailable(int gameId,int gameTypeId,int drawId,Connection con){
		Statement stmt = null;
		ResultSet rs = null;
		try {
			String query = "SELECT event_id FROM st_sl_draw_event_mapping_"+gameId+"  WHERE draw_id ="+drawId;
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			if(rs.next()){
				return true;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			DBConnect.closeConnection(stmt, rs);
		}
		return false;
	}
	
	public boolean checkDrawSaleStatus(Connection con, int gameId,int gameTypeId){
		Statement stmt = null;
		ResultSet rs = null;
		try {
			String query = "SELECT dm.draw_id FROM st_sl_draw_master_"+gameId+" dm inner join st_sl_draw_event_mapping_"+gameId+" dem on dm.draw_id=dem.draw_id WHERE game_type_id = "+gameTypeId+" AND draw_freeze_time > '"+Util.getCurrentTimeStamp()+"' AND sale_start_time <= '"+Util.getCurrentTimeStamp()+"' AND draw_status='ACTIVE'";
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			if(rs.next()){
				return true;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			DBConnect.closeConnection(stmt, rs);
		}
		return false;
	}
	
	public void registerTpMerchentUserData(UserInfoBean userBean,Connection con) throws SLEException{
		Statement stmt=null;
		ResultSet rs=null;
		PreparedStatement pstmt=null;
		String query=null;
		String insQuery=null;
		int isUpdated=0;
		ResultSet rs1=null;
		long sleUserId=0;
		try{
			stmt=con.createStatement();			
			
			if("Asoft".equalsIgnoreCase(userBean.getMerchantDevName())){
				query="SELECT mpm.user_id,mum.user_id sleUserId FROM st_sl_merchant_user_master  mum INNER JOIN  st_sl_merchant_player_master  mpm  ON mum.merchant_user_id=mpm.merchant_user_id  AND mum.merchant_id=mpm.merchant_id  WHERE mum.merchant_user_id='"+userBean.getUserId()+"'";
				rs=stmt.executeQuery(query);
				if(rs.next()){
					userBean.setMerchantUserId(rs.getInt("user_id"));
					userBean.setSleUserId(rs.getLong("sleUserId"));
				}else {
					sleUserId=registerMerchantUserMaster(userBean, con);
					
					insQuery="INSERT INTO st_sl_merchant_player_master (merchant_id,merchant_user_id) VALUES (?,?);";
					pstmt=con.prepareStatement(insQuery);
					pstmt.setInt(1, userBean.getMerchantId());
					pstmt.setString(2, userBean.getUserId());
					isUpdated=pstmt.executeUpdate();
					if(isUpdated<=0){
						throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
					}
					rs1=pstmt.getGeneratedKeys();
					rs1.next();
					userBean.setMerchantUserId(rs1.getInt(1));
					userBean.setSleUserId(sleUserId);
				}
			}else if ("OKPOS".equalsIgnoreCase(userBean.getMerchantDevName())) {
				query="SELECT mrm.user_id,mum.user_id sleUserId FROM st_sl_merchant_user_master  mum INNER JOIN  st_sl_merchant_retailer_master  mrm  ON mum.merchant_user_id=mrm.merchant_user_id  AND mum.merchant_id=mrm.merchant_id  WHERE mum.merchant_user_id='"+userBean.getUserId()+"'";
				rs=stmt.executeQuery(query);
				if(rs.next()){
					userBean.setMerchantUserId(rs.getInt("user_id"));
					userBean.setSleUserId(rs.getInt("sleUserId"));
					userBean.setMerchantUserMappingId(userBean.getMerchantUserId());
				}else {
					sleUserId=registerMerchantUserMaster(userBean, con);
					
					insQuery="INSERT INTO st_sl_merchant_retailer_master (merchant_id,merchant_user_id) VALUES (?,?);";
					pstmt=con.prepareStatement(insQuery);
					pstmt.setInt(1, userBean.getMerchantId());
					pstmt.setString(2, userBean.getUserId());
					isUpdated=pstmt.executeUpdate();
					if(isUpdated<=0){
						throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
					}
					rs1=pstmt.getGeneratedKeys();
					rs1.next();
					userBean.setMerchantUserId(rs1.getInt(1));
					userBean.setMerchantUserMappingId(userBean.getMerchantUserId());
					userBean.setSleUserId(sleUserId);
					
				}
			}

			
		}catch (SLEException e) {
			throw e;
		} catch (Exception e) {
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(pstmt, pstmt, rs1);
		}
	}
	
	public long registerMerchantUserMaster(UserInfoBean userbBean,Connection con) throws SLEException{
		String query=null;
		PreparedStatement pstmt=null;
		int isUpdated=0;
		ResultSet rs=null;
		long sleUserId=0;
		try {
			query="INSERT INTO st_sl_merchant_user_master(parent_user_id, role_id, merchant_id, merchant_user_id, merchant_user_mapping_id, user_name, user_type, is_role_head,  mobile_nbr) VALUES (?,?,?,?,?,?,?,?,?);";
			pstmt=con.prepareStatement(query);
			pstmt.setLong(1, 0);
			pstmt.setInt(2, 0);
			pstmt.setInt(3, userbBean.getMerchantId());
			pstmt.setString(4, userbBean.getUserId());
			pstmt.setInt(5, 0);
			pstmt.setString(6, userbBean.getUserName());
			pstmt.setString(7, userbBean.getUserType());
			pstmt.setString(8, "N");
			pstmt.setString(9, userbBean.getMobileNbr());
			
			isUpdated=pstmt.executeUpdate();
			if(isUpdated<=0){
				throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			}
			rs=pstmt.getGeneratedKeys();
			rs.next();
			sleUserId=rs.getLong(1);
		}  catch (SLEException e) {
			throw e;
		}catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}finally{
			DBConnect.closeConnection(pstmt, rs);
		}
		return sleUserId;
		
	}
	
	public void checkDuplicateMerchantTransId(String refTransId,int merchantId,Connection con) throws SLEException{
		Statement stmt=null;
		String query=null;
		ResultSet rs=null;
		try {
			query="SELECT trans_id FROM st_sl_sale_txn_master WHERE merchant_trans_id='"+refTransId+"' AND merchant_id="+merchantId;
			stmt=con.createStatement();
			rs=stmt.executeQuery(query);
			if(rs.next()){
				throw new SLEException(SLEErrors.DUPLICATE_TRANSACTION_ID_ERROR_CODE,SLEErrors.DUPLICATE_TRANSACTION_ID_ERROR_MESSAGE);
			}
			
		}catch (SLEException e){
			throw e;
		}catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}finally{
			DBConnect.closeConnection(stmt, rs);
		}
	}
	public MerchantInfoBean fetchMerchantDetailFromTicket(String tktNo,Connection con) throws SLEException{
		Statement stmt=null;
		String query=null;
		ResultSet rs=null;
		MerchantInfoBean merchantInfoBean=null;
		try {
			if(ConfigurationVariables.tktLenPMS < tktNo.length()){
				tktNo=tktNo.substring(0, 17);
			}
			query="SELECT stm.merchant_id,merchant_dev_name FROM st_sl_sale_txn_master stm INNER JOIN st_sl_merchant_master mm ON stm.merchant_id=mm.merchant_id WHERE ticket_nbr="+tktNo;
			stmt=con.createStatement();
			rs=stmt.executeQuery(query);
			if(rs.next()){
				merchantInfoBean=new MerchantInfoBean();
				merchantInfoBean.setMerchantId(rs.getInt("merchant_id"));
				merchantInfoBean.setMerchantDevName(rs.getString("merchant_dev_name"));
			}else{
				throw new SLEException(SLEErrors.INVALID_TICKET_ERROR_CODE,SLEErrors.INVALID_TICKET_ERROR_MESSAGE);
			}
		}catch (SLEException e) {
			throw e;
		}catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}finally{
			DBConnect.closeConnection(stmt, rs);
		}
		return merchantInfoBean;
	}
	
	public void fetchAndVerifyTpUser(UserInfoBean userBean,Connection con) throws SLEException{
		Statement stmt = null;
		String query = null;
		ResultSet rs = null;
		try{
			if("OKPOS".equalsIgnoreCase(userBean.getMerchantDevName())){
				query = "Select ssmrm.user_id From st_sl_merchant_user_master ssmum INNER JOIN st_sl_merchant_retailer_master ssmrm on ssmum.merchant_user_id = ssmrm.merchant_user_id Where ssmum.merchant_user_id ='"+userBean.getUserId()+"' and ssmum.merchant_id ="+userBean.getMerchantId();
			}else if("Asoft".equalsIgnoreCase(userBean.getMerchantDevName())){
				query = "Select ssmpm.user_id From st_sl_merchant_user_master ssmum INNER JOIN st_sl_merchant_player_master ssmpm on ssmum.merchant_user_id = ssmpm.merchant_user_id Where ssmum.merchant_user_id ='"+userBean.getUserId()+"' and ssmum.merchant_id ="+userBean.getMerchantId();
			}
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			if(rs.next()){
				userBean.setMerchantUserId(rs.getInt("user_id"));
			} else{
				throw new SLEException(SLEErrors.MERCHANT_USER_ERROR_CODE, SLEErrors.MERCHANT_USER_ERROR);
				
			}
		} catch (SLEException e){
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally{
			DBConnect.closeConnection(stmt,rs);
		}
	}
	
	public void fetchAndVerifyTpTransaction(UserInfoBean userBean,CancelTransactionAPIBean cancelBean,Connection con) throws SLEException{
		Statement stmt1 = null;
		Statement stmt2 = null;
		String query = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		try{
			query = "Select trans_id,game_type_id,game_id,ticket_nbr,is_cancel From st_sl_sale_txn_master Where merchant_trans_id ='"+cancelBean.getRefSaleTransId()+"' and merchant_id ="+userBean.getMerchantId()+" and merchant_user_id ="+userBean.getMerchantUserId();
			stmt1 = con.createStatement();
			rs1 = stmt1.executeQuery(query);
			if(rs1.next()){
				if("N".equalsIgnoreCase(rs1.getString("is_cancel"))){
					query = "Select ssdm.draw_status,ssdm.purchase_table_name,ssdm.draw_id,ssstm.amount,ssstm.channel_type,ssstm.user_id,ssstm.trans_date From st_sl_sale_txn_master ssstm INNER JOIN st_sl_game_tickets_"+rs1.getString("game_id")+"_"+rs1.getString("game_type_id")+" ssgt ON ssstm.trans_id = ssgt.trans_id INNER JOIN st_sl_draw_master_"+rs1.getString("game_id")+" ssdm ON ssgt.draw_id = ssdm.draw_id WHERE ssstm.ticket_nbr ="+rs1.getLong("ticket_nbr");
					stmt2 = con.createStatement();
					rs2 = stmt2.executeQuery(query);
					if(rs2.next()){
						if("ACTIVE".equalsIgnoreCase(rs2.getString("draw_status"))){
							cancelBean.setChannelType(rs2.getString("channel_type"));
							cancelBean.setCancelAmount(rs2.getDouble("amount"));
							cancelBean.setGameId(rs1.getInt("game_id"));
							cancelBean.setGameTypeId(rs1.getInt("game_type_id"));
							cancelBean.setTktNbr(rs1.getString("ticket_nbr"));
							cancelBean.setUserId(rs2.getInt("user_id"));
							cancelBean.setIsAutoCancel("N");
							cancelBean.setCancelType("CANCEL_MANUAL");
							cancelBean.setPurchaseTableName(rs2.getInt("purchase_table_name"));
							cancelBean.setSaleTxnId(rs1.getLong("trans_id"));
						} else{
							throw new SLEException(SLEErrors.TICKET_CANCELLATION_ERROR_CODE,SLEErrors.TICKET_CANCELLATION_ERROR_MESSAGE);
						}
					} else{
						throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
					}
				} else{
					throw new SLEException(SLEErrors.CANCELLED_TICKET_ERROR_CODE, SLEErrors.CANCELLED_TICKET_ERROR_MESSAGE);
				}
			} else{
				throw new SLEException(SLEErrors.INVALID_TICKET_ERROR_CODE, SLEErrors.INVALID_TICKET_ERROR_MESSAGE);
			}
		} catch (SLEException e){
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally{
			DBConnect.closeConnection(stmt1,rs1);
			DBConnect.closeConnection(stmt2,rs2);
		}
		
	}
	public void updateWinningStatus(String status, long transId, Connection con) {
		Statement stmt = null;
		try {
			stmt = con.createStatement();
			String query = "update st_sl_winning_txn_master set status='"+ status + "' where trans_id=" + transId + ";";
			stmt.execute(query);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public void updateWinningStatus(String status, long merchantTxnId,long sleTxnId, Connection con) {
		Statement stmt = null;
		try {
			stmt = con.createStatement();
			String query = "update st_sl_winning_txn_master set status='"+ status + "' ,merchant_trans_id="+merchantTxnId+" where trans_id=" + sleTxnId + ";";
			stmt.execute(query);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public List<com.skilrock.sle.pwtMgmt.javaBeans.TicketInfoBean> fetchInitiatedWinningTktDetails (int merchantId,
			List<String> failedTransactionList, Connection connection) throws SLEException{
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		com.skilrock.sle.pwtMgmt.javaBeans.TicketInfoBean ticketInfoBean= null;
		List<com.skilrock.sle.pwtMgmt.javaBeans.TicketInfoBean> ticketInfoBeanList = new ArrayList<com.skilrock.sle.pwtMgmt.javaBeans.TicketInfoBean>();
		try {
			connection = DBConnect.getConnection();
				
			pstmt = connection.prepareStatement("SELECT wtm.trans_id,stm.trans_id AS sale_txn_id,wtm.merchant_id,wtm.channel_type,wtm.game_id,wtm.draw_id,wtm.ticket_nbr,wtm.user_id,wtm.merchant_user_id,wtm.trans_date,wtm.amount,wtm.status FROM st_sl_winning_txn_master wtm INNER JOIN st_sl_sale_txn_master stm on wtm.ticket_nbr=stm.ticket_nbr WHERE wtm.trans_id IN ('"+ failedTransactionList.toString().substring(1,failedTransactionList.toString().length() - 1).replaceAll(",", "','").replaceAll(" ", "")+ "')");
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ticketInfoBean = new com.skilrock.sle.pwtMgmt.javaBeans.TicketInfoBean();
				ticketInfoBean.setTicketNo(rs.getLong("ticket_nbr"));
				ticketInfoBean.setPartyId(rs.getInt("merchant_user_id"));
				ticketInfoBean.setTotalWinningAmt(rs.getDouble("amount"));
				ticketInfoBean.setEnginesaleTxnId(rs.getString("trans_id"));
				
				ticketInfoBeanList.add(ticketInfoBean);
			}
			
		} catch (Exception e) {
			logger.error("Exception :" + e);
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(pstmt, rs);
		}

		return ticketInfoBeanList;
	}
}
