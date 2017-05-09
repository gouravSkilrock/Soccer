package com.skilrock.sle.gameDataMgmt.controllerImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonObject;
import com.skilrock.sle.common.DBConnect;
import com.skilrock.sle.common.SportsLotteryUtils;
import com.skilrock.sle.common.TransactionManager;
import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.common.javaBeans.UserInfoBean;
import com.skilrock.sle.commonMethod.serviceImpl.CommonMethodsServiceImpl;
import com.skilrock.sle.dataMgmt.javaBeans.TicketTxnStatusBean;
import com.skilrock.sle.gameDataMgmt.daoImpl.GameDataDaoImpl;
import com.skilrock.sle.gameDataMgmt.javaBeans.DrawMasterBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.EventMasterBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.GameMasterBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.GameTypeMasterBean;
import com.skilrock.sle.merchant.common.javaBeans.MerchantInfoBean;

public class GameDataControllerImpl {
	private GameDataControllerImpl(){
		
	}
	
	private static volatile GameDataControllerImpl classInstance;

	public static GameDataControllerImpl getInstance() {
		if(classInstance == null){
			synchronized (GameDataControllerImpl.class) {
				if(classInstance == null){
					classInstance = new GameDataControllerImpl();					
				}
			}
		}
		return classInstance;
	}
	
	public List<GameMasterBean> getSportsLotteryOnStartServerData(String merchantDevName) throws SLEException {
		Connection connection = null;
		List<GameMasterBean> gameMasterList = null;
		MerchantInfoBean merchantInfoBean = null;
		try {
			connection = DBConnect.getConnection();
			merchantInfoBean = Util.merchantInfoMap.get(merchantDevName);
			gameMasterList = GameDataDaoImpl.getInstance().getSportsLotteryGameData(merchantInfoBean, false, connection);
		} catch (SLEException se) {
			throw se;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,
					SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(connection);
		}

		return gameMasterList;
	}

	
	/**
	 * This method get list of GameMasterBean from DAO layer date_wise matchList for mobile.
	 * 
	 * @param merchantId
	 * @return ArrayList of GameMasterBean
	 * @throws SLEException
	 */
	public List<GameMasterBean> getSportsLotteryGameData(int merchantId,String fromDate, String toDate,boolean isForMatchList) throws SLEException {
		Connection connection = null;
		List<GameMasterBean> gameMasterList = null;
		try {
		
			connection = DBConnect.getConnection();
			MerchantInfoBean merchantInfoBean = Util.fetchMerchantInfoBean(merchantId);
			if(isForMatchList){
				gameMasterList = GameDataDaoImpl.getInstance().getSportsLotteryGameDataDateWise(merchantInfoBean,true, connection,fromDate,toDate);
			}else{
				gameMasterList = GameDataDaoImpl.getInstance().getSportsLotteryGameData(merchantInfoBean, true, connection);
			}
			
		} catch (SLEException se) {
			throw se;
		} catch (Exception e) {
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(connection);
		}
		return gameMasterList;
	}
	
	
	/**
	 * This method get list of GameMasterBean from DAO layer.
	 * 
	 * @param merchantId
	 * @return ArrayList of GameMasterBean
	 * @throws SLEException
	 */
	public List<GameMasterBean> getSportsLotteryGameData(int merchantId) throws SLEException {
		List<GameMasterBean> gameMasterList = null;
		try {
			gameMasterList=getSportsLotteryGameData(merchantId,null,null,false);
		} catch (SLEException se) {
			throw se;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} 
		return gameMasterList;
	}
	
	public List<GameMasterBean> getSportsLotteryGameDataGameTypeWise(MerchantInfoBean merchantinfoBean,int gameId,String [] gameTypeIdArr) throws SLEException {
		List<GameMasterBean> gameMasterList = null;
		List<GameTypeMasterBean> gameTypeMasterList=null;
		Connection con=null;
		GameMasterBean gameMasterBean=null;
		GameMasterBean newGameMasterBean=null;

		try {
			con=DBConnect.getConnection();
			newGameMasterBean =SportsLotteryUtils.gameInfoMerchantMap.get(merchantinfoBean.getMerchantDevName()).get(gameId);
			
			gameMasterBean = new GameMasterBean();
			
			gameMasterBean.setGameId(newGameMasterBean.getGameId());
			gameMasterBean.setGameDevName(newGameMasterBean.getGameDevName());
			gameMasterBean.setGameDispName(newGameMasterBean.getGameDispName());
			gameMasterBean.setGameNo(newGameMasterBean.getGameNo());
			gameMasterBean.setMaxBoardCount(newGameMasterBean.getMaxBoardCount());
			gameMasterBean.setMinBoardCount(newGameMasterBean.getMinBoardCount());
			gameMasterBean.setThersholdTickerAmt(newGameMasterBean.getThersholdTickerAmt());
			gameMasterBean.setMaxTicketAmt(newGameMasterBean.getMaxTicketAmt());
			
			gameMasterList=new ArrayList<GameMasterBean>();
			gameTypeMasterList=new ArrayList<GameTypeMasterBean>();
		
			for(String gameTypeId: gameTypeIdArr){
				GameTypeMasterBean gameTypeMasterbean=SportsLotteryUtils.gameTypeInfoMerchantMap.get(merchantinfoBean.getMerchantDevName()).get(Integer.parseInt(gameTypeId));
				gameTypeMasterbean.setDrawMasterList(GameDataDaoImpl.getInstance().getDrawMasterDetails(gameId, Integer.parseInt(gameTypeId), gameTypeMasterbean.getNoOfEvents(), merchantinfoBean, con));
				gameTypeMasterList.add(gameTypeMasterbean);
			}
			gameMasterBean.setGameTypeMasterList(gameTypeMasterList);
			gameMasterList.add(gameMasterBean);
			
		} catch (SLEException se) {
			throw se;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally{
			DBConnect.closeConnection(con);
		}
		return gameMasterList;
	}
	
	
	public Map<Integer, List<EventMasterBean>> getMatchDataDrawWise(int gameId,int gameTypeId ,int merchantId, String listType,StringBuilder drawInfo) throws SLEException {
		Connection connection = null;
		Map<Integer, List<EventMasterBean>> matchData = null;
		try {
			connection = DBConnect.getConnection();
			matchData = GameDataDaoImpl.getInstance().getMatchListDrawWise(gameId,gameTypeId ,merchantId, listType, connection,drawInfo);
		} catch (SLEException se) {
			throw se;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(connection);
		}
		return matchData;
	}
	public List<DrawMasterBean> getMatchDataDrawWiseForWeb(int gameId,int gameTypeId ,int merchantId) throws SLEException {
		Connection connection = null;
		List<DrawMasterBean>  matchDataList = null;
		try {
			connection = DBConnect.getConnection();
			matchDataList = GameDataDaoImpl.getInstance().getMatchListDrawWiseForWeb(gameId,gameTypeId ,merchantId, connection);
		} catch (SLEException se) {
			throw se;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(connection);
		}
		return matchDataList;
	}
	
	public List<DrawMasterBean> getSleResultDataDrawWiseForWeb(int gameId,int gameTypeId ,int merchantId) throws SLEException {
		Connection connection = null;
		List<DrawMasterBean>  matchDataList = null;
		try {
			connection = DBConnect.getConnection();
			matchDataList = GameDataDaoImpl.getInstance().getSleResultDataDrawWiseForWeb(gameId,gameTypeId ,merchantId, connection);
		} catch (SLEException se) {
			throw se;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(connection);
		}
		return matchDataList;
	}
	
	public int getMatchListCount(String userName) throws SLEException{
		int matchListCount;
		Connection con = null;
		try{	
				con = DBConnect.getConnection();
				matchListCount = GameDataDaoImpl.getInstance().getMatchListCount(userName, con);			
		}catch(SQLException se){
			se.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}catch(Exception e){
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}finally{
			DBConnect.closeConnection(con);
		}
		return matchListCount;
	}
	
	public void updateMatchListCount(String userName) throws SLEException{
		Connection con = null;
		try{	
				con = DBConnect.getConnection();
				GameDataDaoImpl.getInstance().updateMatchListCount(userName, con);	
		}catch(SQLException se){
			se.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}catch(Exception e){
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}finally{
			DBConnect.closeConnection(con);
		}
	}
	
	public List<GameMasterBean> getSportsLotteryWinningData(int merchantId) throws SLEException {
		Connection connection = null;
		List<GameMasterBean> gameMasterList = null;
		try {
			connection = DBConnect.getConnection();
			MerchantInfoBean merchantInfoBean = Util.fetchMerchantInfoBean(merchantId);
			gameMasterList = GameDataDaoImpl.getInstance().getSportsLotteryGameWiseWinningData(merchantInfoBean,connection);
			
		} catch (SLEException se) {
			throw se;
		} catch (Exception e) {
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(connection);
		}
		return gameMasterList;
	}
	
	public List<TicketTxnStatusBean> getSportsLotteryTxnStatusData(JsonObject SLETxnData) throws SLEException {
		Connection connection = null;
		String merchantCode = null;
		UserInfoBean userInfoBean = null;
		List<TicketTxnStatusBean> txnStatusList = null;
		try {
			connection = DBConnect.getConnection();
			userInfoBean = new UserInfoBean();
			merchantCode = SLETxnData.get("merchantCode").getAsString();
			userInfoBean.setUserName(SLETxnData.get("playerName").getAsString());
			userInfoBean.setMerchantId(Util.merchantInfoMap.get(merchantCode).getMerchantId());
			userInfoBean.setUserSessionId(SLETxnData.get("sessionId").getAsString());
			userInfoBean.setMerchantDevName(merchantCode);
			userInfoBean.setUserType("PLAYER");
			CommonMethodsServiceImpl.getInstance().fetchAndCheckUserInfo(userInfoBean,connection);
			
			MerchantInfoBean merchantInfoBean = Util.fetchMerchantInfoBean(TransactionManager.getMerchantId());
			txnStatusList = GameDataDaoImpl.getInstance().getSportsLotteryTxnStatus(merchantInfoBean,SLETxnData.get("merchantTxnIdList").toString(),connection);
			
		} catch (SLEException se) {
			throw se;
		} catch (Exception e) {
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(connection);
		}
		return txnStatusList;
	}
}