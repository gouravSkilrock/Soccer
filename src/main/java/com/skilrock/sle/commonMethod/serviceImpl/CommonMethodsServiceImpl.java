package com.skilrock.sle.commonMethod.serviceImpl;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.UserTransaction;

import com.skilrock.sle.common.DBConnect;
import com.skilrock.sle.common.DateTimeFormat;
import com.skilrock.sle.common.SLELogger;
import com.skilrock.sle.common.SportsLotteryUtils;
import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.common.javaBeans.AuditTrailBean;
import com.skilrock.sle.common.javaBeans.CountryDataBean;
import com.skilrock.sle.common.javaBeans.PropertyMasterBean;
import com.skilrock.sle.common.javaBeans.TicketInfoBean;
import com.skilrock.sle.common.javaBeans.UserInfoBean;
import com.skilrock.sle.commonMethod.daoImpl.CommonMethodsDaoImpl;
import com.skilrock.sle.dataMgmt.javaBeans.CancelTransactionAPIBean;
import com.skilrock.sle.dataMgmt.javaBeans.FetchDrawEventsRequest;
import com.skilrock.sle.drawMgmt.javaBeans.SLPrizeRankDistributionBean;
import com.skilrock.sle.embedded.playMgmt.common.javaBeans.CancelTicketBean;
import com.skilrock.sle.gameDataMgmt.daoImpl.GameDataDaoImpl;
import com.skilrock.sle.gameDataMgmt.javaBeans.GameMasterBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.GameTypeMasterBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.LeagueMasterBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.TeamMasterBean;
import com.skilrock.sle.gamePlayMgmt.controllerImpl.CancelTicketControllerImpl;
import com.skilrock.sle.merchant.common.javaBeans.MerchantInfoBean;
import com.skilrock.sle.merchant.lms.LMSIntegrationImpl;
import com.skilrock.sle.merchant.lms.common.javaBeans.LMSCancelTicketResponseBean;
import com.skilrock.sle.merchant.pms.PMSIntegrationImpl;
import com.skilrock.sle.merchant.tonybet.TonyBetIntegrationImpl;
import com.skilrock.sle.merchant.weaver.WeaverIntegrationImpl;
import com.skilrock.sle.tp.rest.common.javaBeans.TpUserRegistrationBean;

public class CommonMethodsServiceImpl {

	private static final SLELogger logger = SLELogger.getLogger(CommonMethodsServiceImpl.class.getName());

	private CommonMethodsServiceImpl() {
	}

	private static volatile CommonMethodsServiceImpl classInstance;

	public static CommonMethodsServiceImpl getInstance() {
		if(classInstance == null){
			synchronized (CommonMethodsServiceImpl.class) {
				if(classInstance == null){
					classInstance = new CommonMethodsServiceImpl();					
				}
			}
		}
		return classInstance;
	}

	public List<PropertyMasterBean> getPropertyDetail() throws SLEException {
		return CommonMethodsDaoImpl.getInstance().getPropertyDetail();
	}

	public List<String> setServerUrl() throws SLEException {
		return CommonMethodsDaoImpl.getInstance().setServerUrl();
	}

	public void fetchUserInfo(UserInfoBean userInfoBean) throws SLEException {
		Connection con = null;
		try {
			con = DBConnect.getConnection();
			CommonMethodsDaoImpl.getInstance().fetchUserInfo(userInfoBean, con);
		} catch (SLEException e) {
			throw e;
		} catch (Exception e) {
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,
					SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(con);
		}
	}

	public void fetchAndCheckUserData(UserInfoBean userInfoBean) throws SLEException {
		Connection connection = null;
		try {
			connection = DBConnect.getConnection();
			fetchAndCheckUserInfo(userInfoBean, connection);
		} catch (SLEException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(connection);
		}
	}
	
	public void fetchAndCheckUserInfo(UserInfoBean userInfoBean, Connection connection) throws SLEException {
		TpUserRegistrationBean tpUserRegistrationBean = null;
		UserTransaction userTxn = null;
		try {
			CommonMethodsDaoImpl.getInstance().fetchUserInfo(userInfoBean, connection);
			if (userInfoBean.getDbSessionId() == null || userInfoBean.getUserSessionId() == null || !userInfoBean.getUserSessionId().equals(userInfoBean.getDbSessionId())) {

				if("PMS".equals(userInfoBean.getMerchantDevName())) {
					tpUserRegistrationBean = PMSIntegrationImpl.fetchUserData(userInfoBean);
				}else if ("Weaver".equals(userInfoBean.getMerchantDevName())) {
					tpUserRegistrationBean = WeaverIntegrationImpl.fetchUserDataAtWeaver(userInfoBean);
				} else if("RMS".equals(userInfoBean.getMerchantDevName())) {
					tpUserRegistrationBean = LMSIntegrationImpl.fetchUserData(userInfoBean.getUserName());
				} else if("TonyBet".equalsIgnoreCase(userInfoBean.getMerchantDevName())) {
					tpUserRegistrationBean = TonyBetIntegrationImpl.fetchUserData(userInfoBean);
					tpUserRegistrationBean.setUserId(userInfoBean.getMerchantUserId());
				}
				else {
					throw new SLEException(SLEErrors.INVALID_MERCHANT_NAME_ERROR_CODE, SLEErrors.INVALID_MERCHANT_NAME_ERROR_MESSAGE);
				}
				
				if(tpUserRegistrationBean == null)
					throw new SLEException(SLEErrors.CONNECTION_ERROR_CODE, SLEErrors.CONNECTION_ERROR_MESSAGE);

				if((tpUserRegistrationBean.getSessionId() == null || "".equalsIgnoreCase(tpUserRegistrationBean.getSessionId().trim()))){
					throw new SLEException(SLEErrors.INVALID_SESSION_ERROR_CODE, SLEErrors.INVALID_SESSION_ERROR_MESSAGE);
				}
				if(userInfoBean.getUserSessionId()!=null){
					if(!userInfoBean.getUserSessionId().equals(tpUserRegistrationBean.getSessionId())){
						throw new SLEException(SLEErrors.INVALID_SESSION_ERROR_CODE, SLEErrors.INVALID_SESSION_ERROR_MESSAGE);
					}
				}
				userTxn = DBConnect.startTransaction();

				tpUserRegistrationBean.setMerchantId(userInfoBean.getMerchantId());
				
				if(userInfoBean.getSleUserId() == 0) {
					CommonMethodsDaoImpl.getInstance().registerMerchantUser(tpUserRegistrationBean, connection);
					userInfoBean.setSleUserId(tpUserRegistrationBean.getSleUserId());
				}
				else
					CommonMethodsDaoImpl.getInstance().updateMerchantUserData(tpUserRegistrationBean, connection);

				userTxn.commit();
				userInfoBean.setMerchantUserId(tpUserRegistrationBean.getUserId());
				userInfoBean.setDbSessionId(tpUserRegistrationBean.getSessionId());
				userInfoBean.setUserSessionId(tpUserRegistrationBean.getSessionId());
				userInfoBean.setUserType(tpUserRegistrationBean.getUserType());
				userInfoBean.setMobileNbr(tpUserRegistrationBean.getMobileNbr());

			}
		} catch (SLEException e) {
			DBConnect.rollBackUserTransaction(userTxn);
			throw e;
		} catch (Exception e) {
			DBConnect.rollBackUserTransaction(userTxn);
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} 
	}
	
	public void userSessionValidation(UserInfoBean userInfoBean) throws SLEException {
		Connection connection = null;
		TpUserRegistrationBean tpUserRegistrationBean = null;
		UserTransaction userTxn = null;
		
		logger.debug("***** Inside fetchAndCheckUserData Method");
		try {
				connection = DBConnect.getConnection();	
				
				CommonMethodsDaoImpl.getInstance().fetchUserInfo(userInfoBean, connection);

				if("PMS".equals(userInfoBean.getMerchantDevName())) {
					tpUserRegistrationBean = PMSIntegrationImpl.fetchUserData(userInfoBean);
				} else if("RMS".equals(userInfoBean.getMerchantDevName())) {
					tpUserRegistrationBean = LMSIntegrationImpl.fetchUserData(userInfoBean.getUserName());
				} else {
					throw new SLEException(SLEErrors.INVALID_MERCHANT_NAME_ERROR_CODE, SLEErrors.INVALID_MERCHANT_NAME_ERROR_MESSAGE);
				}
				
				if(tpUserRegistrationBean == null)
					throw new SLEException(SLEErrors.CONNECTION_ERROR_CODE, SLEErrors.CONNECTION_ERROR_MESSAGE);

				if(tpUserRegistrationBean.getSessionId() == null || userInfoBean.getDbSessionId() == null){
					throw new SLEException(SLEErrors.INVALID_SESSION_ERROR_CODE, SLEErrors.INVALID_SESSION_ERROR_MESSAGE);
				}
				
				userTxn = DBConnect.startTransaction();
				
				tpUserRegistrationBean.setMerchantId(userInfoBean.getMerchantId());

				userTxn.commit();
				userInfoBean.setMerchantUserId(tpUserRegistrationBean.getUserId());
				userInfoBean.setDbSessionId(tpUserRegistrationBean.getSessionId());
				userInfoBean.setUserSessionId(tpUserRegistrationBean.getSessionId());
				userInfoBean.setUserType(tpUserRegistrationBean.getUserType());
				userInfoBean.setMobileNbr(tpUserRegistrationBean.getMobileNbr());

		} catch (SLEException e) {
			DBConnect.rollBackUserTransaction(userTxn);
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			DBConnect.rollBackUserTransaction(userTxn);
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(connection);
		}
	}
	
	
	
	
	public Map<String, MerchantInfoBean> fetchMerchantInfo() throws SLEException {
		Map<String, MerchantInfoBean> merchantInfoMap = null;
		
		Connection con = null;
		
		try {
			con = DBConnect.getConnection();
			merchantInfoMap = CommonMethodsDaoImpl.getInstance().fetchMerchantInfo(con);
		} catch (SLEException e) {
			throw e;
		} catch (Exception e) {
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(con);
		}
		return merchantInfoMap;
	}
	
	public void setGameAndGameTypeInfoMerchantMap() throws SLEException {
		Connection con = null;
		
		GameMasterBean gameMasterBean = null;
		Map<Integer, GameMasterBean> gameMap = null;
		Map<Integer, GameTypeMasterBean> gameTypeMap = null;
		GameTypeMasterBean gameTypeBean = null;
		
		MerchantInfoBean merchantInfoBean = null;
		List<GameMasterBean> gameMasterList = null;

		try {
			con = DBConnect.getConnection();
			
			SportsLotteryUtils.gameInfoMerchantMap = new HashMap<String, Map<Integer,GameMasterBean>>();
			SportsLotteryUtils.gameTypeInfoMerchantMap = new HashMap<String, Map<Integer,GameTypeMasterBean>>();
			for (Map.Entry<String, MerchantInfoBean> entry : Util.merchantInfoMap.entrySet()) {
				merchantInfoBean = Util.merchantInfoMap.get(entry.getKey());
				gameMasterList = GameDataDaoImpl.getInstance().getSportsLotteryGameData(merchantInfoBean, true, con);

				gameMap = new HashMap<Integer, GameMasterBean>();
				gameTypeMap = new HashMap<Integer, GameTypeMasterBean>();
				for (int i = 0; i < gameMasterList.size(); i++) {
					gameMasterBean = gameMasterList.get(i);
					gameMap.put(gameMasterBean.getGameId(), gameMasterBean);

					for (int j = 0; j < gameMasterBean.getGameTypeMasterList().size(); j++) {
						gameTypeBean = gameMasterBean.getGameTypeMasterList().get(j);
						gameTypeMap.put(gameTypeBean.getGameTypeId(), gameTypeBean);
					}
				}
				SportsLotteryUtils.gameInfoMerchantMap.put(entry.getKey(), gameMap);
				SportsLotteryUtils.gameTypeInfoMerchantMap.put(entry.getKey(), gameTypeMap);
			}
		} catch (SLEException e) {
			throw e;
		} catch (Exception e) {
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,
					SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(con);
		}
	}
	
	public List<Long> fetchAutoCancelTxn(String txnId, UserInfoBean userInfoBean) throws SLEException{
		List<Long> cancelTxnList = null;
		Connection con = null;
		UserTransaction userTxn = null;
		try {
			con = DBConnect.getConnection();
			userTxn = DBConnect.startTransaction();
			cancelTxnList = CommonMethodsDaoImpl.getInstance().fetchAutoCancelTxn(txnId, userInfoBean, con);
			userTxn.commit();
		} catch (SLEException e) {
			DBConnect.rollBackUserTransaction(userTxn);
			throw e;
		} catch (Exception e) {
			DBConnect.rollBackUserTransaction(userTxn);
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(con);
		}
		return cancelTxnList;
	}
	
	public List<Long> checkAndAutoCancel(String txnId, String cancelType, UserInfoBean userInfoBean) throws SLEException {
		List<Long> cancelTxnList = null;
		CancelTicketBean cancelTicketBean = null;

		LMSCancelTicketResponseBean cancelResponseBean = null;

		if(Long.parseLong(txnId) == 0)
			return cancelTxnList;
		
		CancelTicketControllerImpl cancelTicketControllerImpl = new CancelTicketControllerImpl();
		try {
			cancelTxnList = fetchAutoCancelTxn(txnId, userInfoBean);

			if(cancelTxnList.size() > 0) {
				for(Long txnToCancel : cancelTxnList) {
					cancelTicketBean = new CancelTicketBean();
					cancelTicketBean.setIsAutoCancel("Y");
					cancelTicketBean.setLastTxnNbr(txnId);
					cancelTicketBean.setCancelType(cancelType);
					cancelTicketBean.setInterfaceType("TERMINAL");
					cancelTicketBean.setCancelDate(DateTimeFormat.getCurrentTimeStamp());
					cancelTicketBean.setTxnIdToCancel(txnToCancel);

					cancelTicketControllerImpl.cancelTicket(cancelTicketBean, userInfoBean,true);
					
					String gameTypeDevName = SportsLotteryUtils.gameTypeInfoMerchantMap.get(userInfoBean.getMerchantDevName()).get(cancelTicketBean.getGameTypeId()).getGameTypeDevName();
					if(!"soccer12".equalsIgnoreCase(gameTypeDevName)){
					if ("YES".equals(SportsLotteryUtils.gameTypeInfoMerchantMap.get(userInfoBean.getMerchantDevName()).get(cancelTicketBean.getGameTypeId()).getJackPotMessageDisplay())) {
						Map<Integer, Double> drawSaleMap = SportsLotteryUtils.drawSaleMap.get(userInfoBean.getMerchantId() + "_" + cancelTicketBean.getGameId() + "_" + cancelTicketBean.getGameTypeId());
						if(drawSaleMap!=null && drawSaleMap.containsKey(cancelTicketBean.getDrawId())) {
							double totalSaleAmt = drawSaleMap.get(cancelTicketBean.getDrawId());
							drawSaleMap.put(cancelTicketBean.getDrawId(), totalSaleAmt - cancelTicketBean.getCancelAmount());
						}
					}
					}
					


					cancelResponseBean = LMSIntegrationImpl.cancelTicketAtLMS(cancelTicketBean, userInfoBean);

					if(cancelResponseBean.getResponseCode() == 0) {
						cancelTicketControllerImpl.updateSportsLotteryCancelTicket(true, cancelResponseBean.getMerTxId(), cancelTicketBean.getSaleTxnId(), cancelTicketBean.getCancelTxnId());
					} else {
						cancelTicketControllerImpl.updateSportsLotteryCancelTicket(false, "-1", cancelTicketBean.getSaleTxnId(), cancelTicketBean.getCancelTxnId());
						if(cancelResponseBean.getResponseCode() == 118){
							throw new SLEException(SLEErrors.INVALID_SESSION_ERROR_CODE, SLEErrors.INVALID_SESSION_ERROR_MESSAGE);
						}
					}
				}
			}
		} catch (SLEException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return cancelTxnList;
	}
	
	public String getGameNameFromGameId(int gameId,int merchantId) throws SLEException{
		String gameName = null;
		Connection con = null;
		try {
				con = DBConnect.getConnection();
				gameName = CommonMethodsDaoImpl.getInstance().getGameNameFromGameId(gameId, merchantId, con);
		} catch (SLEException e) {
			throw e;
		} catch (Exception e) {
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(con);
		}
		return gameName;
	}
	
	public TicketInfoBean fetchTicketInfoUsingTxnId(long txnId) throws SLEException {
		TicketInfoBean tktInfoBean = null;
		Connection con = null;
		try {
			con = DBConnect.getConnection();
			tktInfoBean = new TicketInfoBean();
			CommonMethodsDaoImpl.getInstance().fetchTicketInfoUsingTxnId(tktInfoBean, txnId, con);
		} catch (SLEException e) {
			throw e;
		} catch (Exception e) {
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(con);
		}
		return tktInfoBean;
	}
	
	public TicketInfoBean fetchTicketInfoUsingMerchantTxnId(long txnId) throws SLEException {
		TicketInfoBean tktInfoBean = null;
		Connection con = null;
		try {
			con = DBConnect.getConnection();
			tktInfoBean = new TicketInfoBean();
			CommonMethodsDaoImpl.getInstance().fetchTicketInfoUsingMerchantTxnId(tktInfoBean, txnId, con);
		} catch (SLEException e) {
			throw e;
		} catch (Exception e) {
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(con);
		}
		return tktInfoBean;
	}
	
	public Map<Integer, GameMasterBean> getGameMap(MerchantInfoBean merchantInfoBean) throws SLEException {
		Connection connection = DBConnect.getConnection();
		Map<Integer, GameMasterBean> gameMap = CommonMethodsDaoImpl.getGameMap(merchantInfoBean.getMerchantId(),connection);
		DBConnect.closeConnection(connection);
		return gameMap;
	}
	
	public Map<Integer, LeagueMasterBean> getLeagueMap() throws SLEException {
		Connection connection = DBConnect.getConnection();
		Map<Integer, LeagueMasterBean> leagueMap = CommonMethodsDaoImpl.getLeagueMap(connection);
		DBConnect.closeConnection(connection);
		return leagueMap;
	}
	
	public Map<Integer, TeamMasterBean> getTeamMap() throws SLEException {
		Connection connection = DBConnect.getConnection();
		Map<Integer, TeamMasterBean> teamMap = CommonMethodsDaoImpl.getTeamMap(connection);
		DBConnect.closeConnection(connection);
		return teamMap;
	}
	
	public List<CountryDataBean> getCountryListMap() throws SLEException {
		return CommonMethodsDaoImpl.getInstance().getCountryListMap();
	}

	public void fetchGameDataMerchantWise(Map<Integer, GameMasterBean> gameDataMap, String merchantCode) throws SLEException {
		Connection con = null;
		try {
			con = DBConnect.getConnection();
			CommonMethodsDaoImpl.getInstance().fetchGameDataMerchantWise(gameDataMap, merchantCode, con);
		} catch (SLEException e) {
			throw e;
		} catch (Exception e) {
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(con);
		}
	}

	public Map<Integer, Map<Integer, Double>> getJackpotMap() throws SLEException {
		Map<Integer, Map<Integer, Double>> jackpotMap = null;
		Connection connection = null;
		try {
			connection = DBConnect.getConnection();
			jackpotMap = CommonMethodsDaoImpl.getInstance().getJackpotMap(connection);
			connection.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return jackpotMap;
	}
	
	public double fetchMerchantUserBalance(UserInfoBean userInfoBean) throws SLEException {
		Connection con = null;
		double balance = 0.0;
		try {
			con = DBConnect.getConnection();
			if("PMS".equals(userInfoBean.getMerchantDevName())) {
				PMSIntegrationImpl.fetchUserData(userInfoBean);
			} else if("RMS".equals(userInfoBean.getMerchantDevName())) {
				LMSIntegrationImpl.fetchUserData(userInfoBean.getUserName());
			} else {
				throw new SLEException(SLEErrors.INVALID_MERCHANT_NAME_ERROR_CODE, SLEErrors.INVALID_MERCHANT_NAME_ERROR_MESSAGE);
			}
		} catch (SLEException e) {
			throw e;
		} catch (Exception e) {
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(con);
		}
		return balance;
	}
	
	public void checkForAutoCancel(UserInfoBean userInfoBean, String requestIds) throws SLEException {
		try {
			CommonMethodsServiceImpl.getInstance().fetchAndCheckUserData(userInfoBean);
			logger.debug("Merchant User Info Bean is {}"+userInfoBean);

			CommonMethodsServiceImpl.getInstance().checkAndAutoCancel(requestIds, "CANCEL_MISMATCH", userInfoBean);			
		} catch (SLEException e) {
			throw e;
		} catch (Exception e) {
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
	}
	
	public boolean isSessionAlreadyExist(String sessionId) throws SLEException{		
		Connection con = null;
		boolean isSessionExist = false;
		try {
				con = DBConnect.getConnection();
				isSessionExist = CommonMethodsDaoImpl.getInstance().isSessionAlreadyExist(sessionId, con);
		} catch (SLEException e) {
			throw e;
		} catch (Exception e) {
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(con);
		}
		return isSessionExist;
	}
	
	public void setPrizeDistributionDataGameTypeWise() throws SLEException {
		int merchantId = 0;
		int gameTypeId = 0;
		String key = null;
		Map<Integer, SLPrizeRankDistributionBean> prizeRankMap = null;
		for (Map.Entry<String, Map<Integer, GameMasterBean>> merchantEntrySet : SportsLotteryUtils.gameInfoMerchantMap.entrySet()) {
			merchantId = Util.merchantInfoMap.get(merchantEntrySet.getKey()).getMerchantId();
			for (Map.Entry<Integer, GameMasterBean> gameEntrySet : merchantEntrySet.getValue().entrySet()) {
				for (GameTypeMasterBean gameTypeMasterBean : gameEntrySet.getValue().getGameTypeMasterList()) {
					gameTypeId = gameTypeMasterBean.getGameTypeId();
					prizeRankMap = CommonMethodsDaoImpl.getInstance().fetchPrizeDistributionData(merchantId, gameTypeId);
					if(prizeRankMap == null)
						continue;
					key = merchantId + "_" + gameTypeId;
					
					SportsLotteryUtils.prizeRankDistributionMap.put(key, prizeRankMap);
				}
			}
		}
	}
	
	public void setDrawSaleMap() throws SLEException {
		int merchantId = 0;
		int gameId = 0;
		int gameTypeId = 0;
		String key = null;
		Connection con = null;
		SportsLotteryUtils.drawSaleMap = new HashMap<String, Map<Integer, Double>>();
		Map<Integer, Double> drawSaleMap = null;
		try {
			con = DBConnect.getConnection();
			for (Map.Entry<String, Map<Integer, GameMasterBean>> merchantEntrySet : SportsLotteryUtils.gameInfoMerchantMap.entrySet()) {
				merchantId = Util.merchantInfoMap.get(merchantEntrySet.getKey()).getMerchantId();
				for (Map.Entry<Integer, GameMasterBean> gameEntrySet : merchantEntrySet.getValue().entrySet()) {
					for (GameTypeMasterBean gameTypeMasterBean : gameEntrySet.getValue().getGameTypeMasterList()) {
						gameId = gameTypeMasterBean.getGameId();
						gameTypeId = gameTypeMasterBean.getGameTypeId();
						drawSaleMap = CommonMethodsDaoImpl.getInstance().getMerchantGameTypeDrawSale(merchantId, gameId, gameTypeId, con);
						key = merchantId + "_" + gameId + "_" + gameTypeId;
						SportsLotteryUtils.drawSaleMap.put(key, drawSaleMap);
					}
				}
			}
		} catch (SLEException e) {
			throw e;
		} catch (Exception e) {
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(con);
		}
	}

	public List<AuditTrailBean> fetchAuditTrailData(int userId, int merchantId, String startTime, String endTime) throws SLEException {
		Connection con = null;
		List<AuditTrailBean> auditTrailBeans = null;
		try {
			con = DBConnect.getConnection();
			auditTrailBeans = CommonMethodsDaoImpl.getInstance().fetchAuditTrailData(userId, merchantId, startTime, endTime, con);
		} catch (SLEException e) {
			throw e;
		} catch (Exception e) {
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(con);
		}
		return auditTrailBeans;
	}
	
	public List<String> fetchGameTypeOptionList(int gameId,int gameTypeId) throws SLEException{
		Connection con=null;
		List<String> optionList=null;
		try{
			con=DBConnect.getConnection();
			optionList=CommonMethodsDaoImpl.getInstance().fetchGameTypeOptionList(gameId, gameTypeId, con);
		}catch (SLEException e) {
			throw e;
		} catch (Exception e) {
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(con);
		}
		return optionList;
	}
	
	public void verifyAndRegisterTpMerchentUserData(UserInfoBean userBean,String refTransId) throws SLEException{
		Connection con=null;
		UserTransaction userTxn = null;
		try{
			con=DBConnect.getConnection();
			userTxn = DBConnect.startTransaction();
			CommonMethodsDaoImpl.getInstance().checkDuplicateMerchantTransId(refTransId, userBean.getMerchantId(), con);
			CommonMethodsDaoImpl.getInstance().registerTpMerchentUserData(userBean, con);
			userTxn.commit();
		}catch (SLEException e) {
			DBConnect.rollBackUserTransaction(userTxn);
			throw e;
		} catch (Exception e) {
			DBConnect.rollBackUserTransaction(userTxn);
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(con);
		}
	}
	public MerchantInfoBean fetchMerchantDetailFromTicket(String tktNo) throws SLEException{
		Connection con=null;
		MerchantInfoBean merchantInfoBean=null;
		try {
			con=DBConnect.getConnection();
			merchantInfoBean=CommonMethodsDaoImpl.getInstance().fetchMerchantDetailFromTicket(tktNo, con);
		} catch (SLEException e) {
			throw e;
		}catch (Exception e) {
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally{
			DBConnect.closeConnection(con);
		}
		return merchantInfoBean;
	}
	
	public void fetchAndVerifyTpUser(UserInfoBean userBean) throws SLEException{
		Connection con = null;
		try{
			con = DBConnect.getConnection();
			CommonMethodsDaoImpl.getInstance().fetchAndVerifyTpUser(userBean, con);
		} catch(SLEException e){
			throw e;
		} finally{
			DBConnect.closeConnection(con);
		}
	}
	
	public void fetchAndVerifyTpTransaction(UserInfoBean userBean,CancelTransactionAPIBean cancelBean,String merchantTransId) throws SLEException{
		Connection con = null;
		try{
			con = DBConnect.getConnection();
			CommonMethodsDaoImpl.getInstance().fetchAndVerifyTpTransaction(userBean, cancelBean, con);
		} catch(SLEException e){
			throw e;
		} finally{
			DBConnect.closeConnection(con);
		}
	}
	
	public Map<String, String> fetchGameTypeOptionMap(FetchDrawEventsRequest fetchDrawEventsRequest){
		Connection connection = null;
		Map<String, String> eventOptionMap = null;
		try{
			connection = DBConnect.getConnection();
			eventOptionMap = CommonMethodsDaoImpl.getInstance().fetchGameTypeOptionMap(fetchDrawEventsRequest.getGameId(), fetchDrawEventsRequest.getGameTypeId(), connection);
		}catch(SLEException e){
			logger.error("Exception occurred:"+e);
		}catch(Exception e){
			logger.error("Exception occurred:"+e);
		}
		return eventOptionMap;
	}
}
