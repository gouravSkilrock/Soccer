package com.skilrock.sle.gamePlayMgmt.controllerImpl;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.UserTransaction;

import org.apache.struts2.components.UIBean;

import com.skilrock.sle.common.DBConnect;
import com.skilrock.sle.common.SportsLotteryUtils;
import com.skilrock.sle.common.TransactionManager;
import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.common.javaBeans.UserInfoBean;
import com.skilrock.sle.commonMethod.daoImpl.CommonMethodsDaoImpl;
import com.skilrock.sle.commonMethod.serviceImpl.CommonMethodsServiceImpl;
import com.skilrock.sle.embedded.playMgmt.common.javaBeans.CancelTicketBean;
import com.skilrock.sle.gameDataMgmt.controllerImpl.GameDataControllerImpl;
import com.skilrock.sle.gameDataMgmt.daoImpl.GameDataDaoImpl;
import com.skilrock.sle.gameDataMgmt.javaBeans.GameMasterBean;
import com.skilrock.sle.gamePlayMgmt.daoImpl.PurchaseTicketDaoImpl;
import com.skilrock.sle.gamePlayMgmt.javaBeans.DrawDetailBean;
import com.skilrock.sle.gamePlayMgmt.javaBeans.EventDetailBean;
import com.skilrock.sle.gamePlayMgmt.javaBeans.PrizeRankDrawWinningBean;
import com.skilrock.sle.gamePlayMgmt.javaBeans.SportsLotteryGameDrawDataBean;
import com.skilrock.sle.gamePlayMgmt.javaBeans.SportsLotteryGameEventDataBean;
import com.skilrock.sle.gamePlayMgmt.javaBeans.SportsLotteryGamePlayBean;
import com.skilrock.sle.merchant.lms.LMSIntegrationImpl;
import com.skilrock.sle.merchant.lms.common.javaBeans.LMSCancelTicketResponseBean;
import com.skilrock.sle.merchant.lms.common.javaBeans.LMSGamePlayResponseBean;
import com.skilrock.sle.merchant.pms.PMSIntegrationImpl;
import com.skilrock.sle.merchant.pms.common.javaBeans.PMSGamePlayResponseBean;
import com.skilrock.sle.merchant.tonybet.TonyBetGamePlayResponseBean;
import com.skilrock.sle.merchant.tonybet.TonyBetIntegrationImpl;
import com.skilrock.sle.merchant.weaver.WeaverCancelTxnBean;
import com.skilrock.sle.merchant.weaver.WeaverGamePlayResponseBean;
import com.skilrock.sle.merchant.weaver.WeaverIntegrationImpl;
import com.skilrock.sle.merchant.weaver.WeaverPlayerTxnDataBean;
import com.skilrock.sle.merchant.weaver.WeaverTxnBean;
import com.skilrock.sle.merchant.weaver.WeaverTxnConfirmationThread;
import com.skilrock.sle.merchant.weaver.WeaverUtils;
import com.skilrock.sle.merchant.weaver.WeaverUtils.BALANCE_TYPE;
import com.skilrock.sle.merchant.weaver.WeaverUtils.TxnTypes;
import com.skilrock.sle.mobile.common.SportsLotteryResponseData;
import com.skilrock.sle.tp.rest.common.javaBeans.TransactionBean;

import net.sf.json.JSONObject;
import rng.RNGUtilities;

public class PurchaseTicketControllerImpl {
	private PurchaseTicketControllerImpl() {
	}

	private static volatile PurchaseTicketControllerImpl classInstance;

	public static PurchaseTicketControllerImpl getInstance() {
		if(classInstance == null){
			synchronized (PurchaseTicketControllerImpl.class) {
				if(classInstance == null){
					classInstance = new PurchaseTicketControllerImpl();					
				}
			}
		}
		return classInstance;
	}
	
	public void purchaseSportsLotteryGameTicket(SportsLotteryGamePlayBean gamePlayBean, UserInfoBean userInfoBean, long transId,Connection connection,Map<Integer, DrawDetailBean> drawDetailMap) throws SLEException {
		PurchaseTicketDaoImpl daoImpl = null;
		long ticketNumber = 0;
		try {
//			validateSaleRequestData(gamePlayBean, userInfoBean);		
			daoImpl = PurchaseTicketDaoImpl.getInstance();
			ticketNumber = daoImpl.generateTicketNumber(userInfoBean, gamePlayBean.getServiceId(), connection);
			gamePlayBean.setTicketNumber(ticketNumber);
			gamePlayBean.setReprintCount(0);
			if("RMS".equalsIgnoreCase(userInfoBean.getMerchantDevName())){
				if("TRUE".equalsIgnoreCase(Util.getPropertyValue("IS_BARCODE_ENABLED"))){
					gamePlayBean.setBarcodeCount(RNGUtilities.generateRandomNumber(10, 99));
				}
			}
//			gamePlayBean.setPurchaseTime(Util.getCurrentTimeString());
			transId = daoImpl.purchaseTicket(gamePlayBean, userInfoBean, connection,transId,drawDetailMap);
			gamePlayBean.setTransId(transId);
		} catch (SLEException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
	}
	
	public void updateSportsLotteryGameTicket(String status, String merchantTranId, String isCancel, Long transId,Connection con,long ticketNumber, boolean isUpdatePurchaseTable) throws SLEException {
		PurchaseTicketDaoImpl daoImpl = null;
		try {
			daoImpl = PurchaseTicketDaoImpl.getInstance();
			daoImpl.updateSportsLotteryGameTicket(status, merchantTranId, isCancel, transId, con,ticketNumber,isUpdatePurchaseTable);
		} catch (SLEException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
	}
	
	public void updatePromoTktMap(SportsLotteryGamePlayBean gamePlayBean, UserInfoBean userInfoBean) throws SLEException{
		SportsLotteryGameDrawDataBean gameDrawDataBean = null;
		try {
			if ("YES".equals(SportsLotteryUtils.gameTypeInfoMerchantMap.get(userInfoBean.getMerchantDevName()).get(gamePlayBean.getGameTypeId()).getJackPotMessageDisplay())) {
				for(int iLoop = 0; iLoop < gamePlayBean.getNoOfBoard(); iLoop++) {
					gameDrawDataBean = gamePlayBean.getGameDrawDataBeanArray()[iLoop];
					Map<Integer, PrizeRankDrawWinningBean> drawPrizeRankMap = PurchaseTicketDaoImpl.getInstance().getPrizeRankDrawSaleDistriButionNew(userInfoBean.getMerchantId(), userInfoBean.getMerchantDevName(), gamePlayBean.getGameId(), gamePlayBean.getGameTypeId(), gameDrawDataBean.getDrawId(), gameDrawDataBean.getBoardPurchaseAmount());
					gameDrawDataBean.setDrawPrizeRankMap(drawPrizeRankMap);
				}
			}
		} catch (SLEException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
	}
	
	public JSONObject purchaseTicket(UserInfoBean userInfoBean, SportsLotteryGamePlayBean gamePlayBean, int eventId,String refTransId) throws Exception{
		JSONObject jsonObject = null;
		Connection con = null;
		UserTransaction userTxn = null;
		long trxId = 0;
		List<String> eventOptionsList = null;
		PMSGamePlayResponseBean gameResponseBean = null;
		WeaverGamePlayResponseBean weaverGameResponseBean = null;
		LMSCancelTicketResponseBean cancelTicketResponseBean=null;
		CancelTicketBean cancelTicketBean = null;
		Map<Long,TransactionBean> sleTxnsIdMap = null;
		TonyBetGamePlayResponseBean tonyBetResponseBean = null;
		try {
			jsonObject = new JSONObject();
			if("PMS".equalsIgnoreCase(userInfoBean.getMerchantDevName()) || "Weaver".equalsIgnoreCase(userInfoBean.getMerchantDevName()) 
					|| "TonyBet".equalsIgnoreCase(userInfoBean.getMerchantDevName())){
				CommonMethodsServiceImpl.getInstance().fetchAndCheckUserData(userInfoBean);
			}  else{
				CommonMethodsServiceImpl.getInstance().verifyAndRegisterTpMerchentUserData(userInfoBean,refTransId);
			}
			con = DBConnect.getConnection();
			Map<Integer, DrawDetailBean> drawDetailMap = PurchaseTicketDaoImpl.getInstance().getDrawDetail(gamePlayBean.getDrawIdArray(), gamePlayBean.getGameId(),
					gamePlayBean.getGameTypeId(), userInfoBean.getMerchantId(), con);
			//Validate Event Details for current Draw
			validateEventDetails(drawDetailMap.get(gamePlayBean.getDrawIdArray()[0]), gamePlayBean.getGameDrawDataBeanArray());			
			userTxn = DBConnect.startTransaction();
			gamePlayBean.setPurchaseTime(Util.getCurrentTimeString());
			trxId = CommonMethodsDaoImpl.getInstance().getNewSaleTransactionId(userInfoBean, gamePlayBean, con);
			userTxn.commit();
			gamePlayBean.setTransId(trxId);
			DBConnect.closeConnection(con);
			int responseCode = -1;
			int tonyBetResultCode = -1;
			if("PMS".equalsIgnoreCase(userInfoBean.getMerchantDevName())){
				gameResponseBean = PMSIntegrationImpl.purchaseTicketAtPMS(gamePlayBean, userInfoBean);
				responseCode = gameResponseBean.getResponseCode();
			} else if ("Weaver".equalsIgnoreCase(userInfoBean.getMerchantDevName())) {
				sleTxnsIdMap = new LinkedHashMap<Long, TransactionBean>();
				TransactionBean txnBean = new TransactionBean();
				txnBean.setTransactionId(gamePlayBean.getTransId());
				txnBean.setTransactionAmt(gamePlayBean.getTotalPurchaseAmt());
				txnBean.setGameId(gamePlayBean.getGameId());
				txnBean.setGameTypeId(gamePlayBean.getGameTypeId());
				txnBean.setGameDevName(SportsLotteryUtils.gameInfoMerchantMap.get(userInfoBean.getMerchantDevName()).get(gamePlayBean.getGameId()).getGameDevName());
				sleTxnsIdMap.put(trxId, txnBean);
				weaverGameResponseBean = WeaverIntegrationImpl.purchaseTicketAtWeaver(sleTxnsIdMap, txnBean.getGameDevName(), gamePlayBean.getGameId(), userInfoBean);
				responseCode = weaverGameResponseBean.getErrorCode();
			} else if ("TonyBet".equalsIgnoreCase(userInfoBean.getMerchantDevName())) {
				tonyBetResponseBean = TonyBetIntegrationImpl.purchaseTicketAtTonyBet(gamePlayBean,userInfoBean);
				tonyBetResultCode = tonyBetResponseBean.getResultCode();
			}
			else{
				gameResponseBean=new PMSGamePlayResponseBean();
				gameResponseBean.setResponseCode(0);
				gameResponseBean.setMerTxnId(refTransId);				
			}
			con = DBConnect.getConnection();
			if(responseCode == 0 || tonyBetResultCode == 1) {
				List<WeaverPlayerTxnDataBean> weaverPlrTxnBeanList = new ArrayList<WeaverPlayerTxnDataBean>();
				try{
					userTxn = DBConnect.startTransaction();
					if ("PMS".equalsIgnoreCase(userInfoBean.getMerchantDevName())) {
						purchaseSportsLotteryGameTicket(gamePlayBean, userInfoBean,trxId,con,drawDetailMap);
						updateSportsLotteryGameTicket("DONE", gameResponseBean.getMerTxnId(), "N", gamePlayBean.getTransId(),con,gamePlayBean.getTicketNumber(),false);
					} else if ("Weaver".equalsIgnoreCase(userInfoBean.getMerchantDevName())) {
						for (WeaverTxnBean txnBean : weaverGameResponseBean.getTransactionInfoList()) {
							TransactionBean transactionBean = sleTxnsIdMap.get(Long.parseLong(txnBean.getRefTxnNo()));
							transactionBean.setMerchantTxnId(String.valueOf(txnBean.getTxnId()));
						}
						purchaseSportsLotteryGameTicket(gamePlayBean, userInfoBean,trxId,con,drawDetailMap);
						for (WeaverTxnBean txnBean : weaverGameResponseBean.getTransactionInfoList()) {
							updateSportsLotteryGameTicket("DONE", txnBean.getRefTxnNo(), "N", gamePlayBean.getTransId(),con,gamePlayBean.getTicketNumber(),false);
							 WeaverPlayerTxnDataBean txnDataBean = new WeaverPlayerTxnDataBean();
			    			    txnDataBean.setAliasName(WeaverUtils.getAliasName());
			    			    txnDataBean.setBalanceType(BALANCE_TYPE.MAIN.toString());
			    			    txnDataBean.setParticular(TxnTypes.WAGER_CONFIRMATION.toString());
			    			    txnDataBean.setPlayerId(String.valueOf(userInfoBean.getMerchantId()));
			    			    txnDataBean.setRefWagerTxnId(txnBean.getRefTxnNo());
			    			    txnDataBean.setWalletType(WeaverUtils.WALLET_TYPE);
			    			    weaverPlrTxnBeanList.add(txnDataBean);
						}
					} else if ("TonyBet".equalsIgnoreCase(userInfoBean.getMerchantDevName())) {
						purchaseSportsLotteryGameTicket(gamePlayBean, userInfoBean,trxId,con,drawDetailMap);
						updateSportsLotteryGameTicket("DONE", String.valueOf(tonyBetResponseBean.getTransactionId()), "N", gamePlayBean.getTransId(),con,gamePlayBean.getTicketNumber(),false);
					}
					userTxn.commit();
					if ("Weaver".equalsIgnoreCase(userInfoBean.getMerchantDevName())) {
						Thread weaverTxnConfirmationThread = new Thread(new WeaverTxnConfirmationThread(weaverPlrTxnBeanList));
						weaverTxnConfirmationThread.setDaemon(true);
			    		weaverTxnConfirmationThread.start();
					}
					
				}catch (Exception e) {
					DBConnect.rollBackUserTransaction(userTxn);
					userTxn = DBConnect.startTransaction();
					if("PMS".equalsIgnoreCase(userInfoBean.getMerchantDevName())){
						cancelTicketBean=new CancelTicketBean();
						cancelTicketBean.setGameId(gamePlayBean.getGameId());
						cancelTicketBean.setGameTypeId(gamePlayBean.getGameTypeId());
						cancelTicketBean.setTktToCancel(gamePlayBean.getTicketNumber()+"");
						cancelTicketBean.setSaleTxnId(trxId);
						cancelTicketBean.setCancelAmount(gamePlayBean.getTotalPurchaseAmt());
						cancelTicketBean.setIsAutoCancel("Y");
						cancelTicketBean.setInterfaceType("API");
						cancelTicketBean.setTktMerchantUserId(userInfoBean.getMerchantUserId());
						cancelTicketResponseBean = PMSIntegrationImpl.cancelTicketAtPMS(cancelTicketBean, userInfoBean);
						updateSportsLotteryGameTicket("FAILED", cancelTicketResponseBean.getMerTxId(), "N", gamePlayBean.getTransId(),con,0,false);
						
					} else if ("Weaver".equalsIgnoreCase(userInfoBean.getMerchantDevName())) {
						weaverGameResponseBean = WeaverIntegrationImpl.cancelTicketTxnAtWeaver(sleTxnsIdMap, userInfoBean);
						if (weaverGameResponseBean.getErrorCode() == 0) {
							for (WeaverCancelTxnBean weaverCancelBean : weaverGameResponseBean.getPlrWiseRespBean()) {
								updateSportsLotteryGameTicket("FAILED", weaverCancelBean.getTxnId(), "N", gamePlayBean.getTransId(), con, 0, false);
							}
						}
						else {
							throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
						}
					} else if ("TonyBet".equalsIgnoreCase(userInfoBean.getMerchantDevName())) {
						tonyBetResponseBean = TonyBetIntegrationImpl.cancelTransaction(gamePlayBean,userInfoBean);
						updateSportsLotteryGameTicket("FAILED", String.valueOf(tonyBetResponseBean.getTransactionId()), "N", gamePlayBean.getTransId(),con,0,false);
					}
					else{
						cancelTicketResponseBean = new LMSCancelTicketResponseBean();
						cancelTicketResponseBean.setMerTxId(refTransId);
						updateSportsLotteryGameTicket("FAILED", cancelTicketResponseBean.getMerTxId(), "N", gamePlayBean.getTransId(),con,0,false);
					}
				    userTxn.commit();
					throw new SLEException(SLEErrors.FAILED_TRANSACTION_ERROR_CODE,SLEErrors.FAILED_TRANSACTION_ERROR_MESSAGE);
				}
				
				String gameTypeDevName = SportsLotteryUtils.gameTypeInfoMerchantMap.get(userInfoBean.getMerchantDevName()).get(gamePlayBean.getGameTypeId()).getGameTypeDevName();
				if(!"soccer12".equalsIgnoreCase(gameTypeDevName)){
					if ("YES".equals(SportsLotteryUtils.gameTypeInfoMerchantMap.get(userInfoBean.getMerchantDevName()).get(gamePlayBean.getGameTypeId()).getJackPotMessageDisplay())) {
						Map<Integer, Double> drawSaleMap = SportsLotteryUtils.drawSaleMap.get(userInfoBean.getMerchantId() + "_" + gamePlayBean.getGameId() + "_" + gamePlayBean.getGameTypeId());
						if(drawSaleMap.containsKey(gamePlayBean.getDrawIdArray()[0])) {
							double totalSaleAmt = drawSaleMap.get(gamePlayBean.getDrawIdArray()[0]);
							drawSaleMap.put(gamePlayBean.getDrawIdArray()[0], totalSaleAmt+gamePlayBean.getTotalPurchaseAmt());
						}
					}
				}
				
				eventOptionsList = GameDataDaoImpl.getInstance().getEventOptionsList(eventId,gamePlayBean.getGameId(),gamePlayBean.getGameTypeId(),con);
				if ("PMS".equalsIgnoreCase(userInfoBean.getMerchantDevName())) {
					jsonObject = SportsLotteryResponseData.generateSportsLotterySaleResponseData(gamePlayBean, userInfoBean.getMerchantDevName(), gameResponseBean.getAvailBal(),eventOptionsList);
				} else if("Weaver".equalsIgnoreCase(userInfoBean.getMerchantDevName())) {
					jsonObject = SportsLotteryResponseData.generateSportsLotterySaleResponseData(gamePlayBean, userInfoBean.getMerchantDevName(),weaverGameResponseBean.getRealBal(),eventOptionsList);
				} else if("TonyBet".equalsIgnoreCase(userInfoBean.getMerchantDevName())) {
					jsonObject = SportsLotteryResponseData.generateSportsLotterySaleResponseData(gamePlayBean, userInfoBean.getMerchantDevName(),tonyBetResponseBean.getBalance(),eventOptionsList);
				} else {
					jsonObject = SportsLotteryResponseData.generateSportsLotterySaleResponseData(gamePlayBean, userInfoBean.getMerchantDevName(), gameResponseBean.getAvailBal(),eventOptionsList);
				}
				
			} else {
				if ("PMS".equalsIgnoreCase(userInfoBean.getMerchantDevName())) {
					userTxn = DBConnect.startTransaction();
					updateSportsLotteryGameTicket("FAILED", gameResponseBean.getMerTxnId(), "N", gamePlayBean.getTransId(),con,0,false);
					userTxn.commit();
					jsonObject.put("responseCode", gameResponseBean.getResponseCode());
					jsonObject.put("responseMsg", gameResponseBean.getResponseMessage());		
				} else if ("Weaver".equalsIgnoreCase(userInfoBean.getMerchantDevName())) {
					userTxn = DBConnect.startTransaction();
					for (WeaverTxnBean txnBean : weaverGameResponseBean.getTransactionInfoList() ) {
						updateSportsLotteryGameTicket("FAILED", txnBean.getRefTxnNo(), "N", gamePlayBean.getTransId(),con,0,false);
					}
					userTxn.commit();
					jsonObject.put("responseCode", weaverGameResponseBean.getErrorCode());
					jsonObject.put("responseMsg", weaverGameResponseBean.getErrorMsg());
				} else if ("TonyBet".equalsIgnoreCase(userInfoBean.getMerchantDevName())) {
					userTxn = DBConnect.startTransaction();
					updateSportsLotteryGameTicket("FAILED", String.valueOf(tonyBetResponseBean.getTransactionId()), "N", gamePlayBean.getTransId(),con,0,false);
					userTxn.commit();
					jsonObject.put("responseCode", tonyBetResponseBean.getResultCode());
				} else {
					userTxn = DBConnect.startTransaction();
					updateSportsLotteryGameTicket("FAILED", gameResponseBean.getMerTxnId(), "N", gamePlayBean.getTransId(),con,0,false);
					userTxn.commit();
					jsonObject.put("responseCode", gameResponseBean.getResponseCode());
					jsonObject.put("responseMsg", gameResponseBean.getResponseMessage());		
				}
				
				
			} 
		} catch (SLEException pe) {
			pe.printStackTrace();
			DBConnect.rollBackUserTransaction(userTxn);
			if(pe.getErrorCode()==10012){
				jsonObject.put("responseCode", SLEErrors.INVALID_SESSION_MOBILE_ERROR_CODE);
				jsonObject.put("responseMsg",SLEErrors.INVALID_SESSION_MOBILE_ERROR_MESSAGE);
			}else if(pe.getErrorCode()==10003){
				if(CommonMethodsDaoImpl.getInstance().checkDrawSaleStatus(con,gamePlayBean.getGameId(),gamePlayBean.getGameTypeId())){// verify Draw Info
					List<GameMasterBean> gameMasterList = null;
        			gameMasterList = GameDataControllerImpl.getInstance().getSportsLotteryGameData(TransactionManager.getMerchantId());
        			jsonObject = SportsLotteryResponseData.generateDrawGameData(gameMasterList,SLEErrors.DRAW_FREEZED_ERROR_CODE,SLEErrors.DRAW_FREEZED_ERROR_MESSAGE);
				} else{
					jsonObject.put("responseCode", pe.getErrorCode());
					jsonObject.put("responseMsg", pe.getErrorMessage());
					 String upcomingDrawDetails = CommonMethodsDaoImpl.getInstance().getUpcomingDrawDetail(gamePlayBean.getGameId(),gamePlayBean.getGameTypeId(),con);
					 if(upcomingDrawDetails.split("~").length > 1){
							jsonObject.put("upcomingDrawStartTime", upcomingDrawDetails.split("~")[1]);
							int drawId = Integer.parseInt(upcomingDrawDetails.split("~")[0]);
							jsonObject.put("areEventsMappedForUpcomingDraw", CommonMethodsDaoImpl.getInstance().isEventAvailable(gamePlayBean.getGameId(), gamePlayBean.getGameTypeId(), drawId, con));
					 }
        		}
				
			} else{
				jsonObject.put("responseCode", pe.getErrorCode());
				jsonObject.put("responseMsg", pe.getErrorMessage());
			}
		} catch (Exception e) {
			e.printStackTrace();
			DBConnect.rollBackUserTransaction(userTxn);
			jsonObject.put("responseCode", SLEErrors.GENERAL_EXCEPTION_ERROR_CODE);
			jsonObject.put("responseMsg", SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(con);
			if (jsonObject.isEmpty()) {
				jsonObject.put("responseCode", SLEErrors.GENERAL_EXCEPTION_ERROR_CODE);
				jsonObject.put("responseMsg", SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			}
		}
		return jsonObject;
	}
	public void validateEventDetails(DrawDetailBean bean, SportsLotteryGameDrawDataBean[] gameDrawDataBeanArray) throws SLEException{
		try{
			for (int i = 0; i < gameDrawDataBeanArray.length; i++) {
				SportsLotteryGameEventDataBean[] eventDrawDataBeanArray = gameDrawDataBeanArray[i].getGameEventDataBeanArray();
				for (int j = 0; j < eventDrawDataBeanArray.length; j++) {
					SportsLotteryGameEventDataBean eventDrawDataBean = eventDrawDataBeanArray[j];
					for (int k = 0; k < eventDrawDataBean.getSelectedOption().length; k++) {
							if(bean.getDrawEventDetail().size() < 1){
								throw new SLEException(SLEErrors.NO_ACTIVE_DRAW_AVAILABLE_ERROR_CODE,SLEErrors.NO_ACTIVE_DRAW_AVAILABLE_ERROR_MESSAGE);
							}
							EventDetailBean eventDetailBean = bean.getDrawEventDetail().get(eventDrawDataBean.getEventId()).get(eventDrawDataBean.getSelectedOption()[k]);
							if(eventDetailBean == null){
								throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
							}
					}
				}
			}		
		} catch(SLEException e){
			e.printStackTrace();
			throw e;
		}catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
	}
	
	public LMSGamePlayResponseBean purchaseTicketForTerminal(UserInfoBean userInfoBean, SportsLotteryGamePlayBean gamePlayBean) throws Exception{
		LMSGamePlayResponseBean gameResponseBean = null;
		Connection con = null;
		UserTransaction userTxn = null;
		long trxId = 0;
		try{
			con = DBConnect.getConnection();
			Map<Integer, DrawDetailBean> drawDetailMap = PurchaseTicketDaoImpl.getInstance().getDrawDetail(gamePlayBean.getDrawIdArray(), gamePlayBean.getGameId(), gamePlayBean.getGameTypeId(), userInfoBean.getMerchantId(), con);

			validateEventDetails(drawDetailMap.get(gamePlayBean.getDrawIdArray()[0]), gamePlayBean.getGameDrawDataBeanArray());
			gamePlayBean.setPurchaseTime(Util.getCurrentTimeString());
			userTxn = DBConnect.startTransaction();
			trxId = CommonMethodsDaoImpl.getInstance().getNewSaleTransactionId(userInfoBean, gamePlayBean, con);
			userTxn.commit();
			gamePlayBean.setTransId(trxId);
			DBConnect.closeConnection(con);
			
			gameResponseBean = LMSIntegrationImpl.purchaseTicketAtLMS(gamePlayBean, userInfoBean);
			con = DBConnect.getConnection();
			userTxn = DBConnect.startTransaction();
			if(gameResponseBean.getResponseCode() == 0) {
				purchaseSportsLotteryGameTicket(gamePlayBean,userInfoBean,trxId,con,drawDetailMap);
				updateSportsLotteryGameTicket("DONE", gameResponseBean.getMerTxId(), "N", gamePlayBean.getTransId(), con,gamePlayBean.getTicketNumber(),false);
				String gameTypeDevName = SportsLotteryUtils.gameTypeInfoMerchantMap.get(userInfoBean.getMerchantDevName()).get(gamePlayBean.getGameTypeId()).getGameTypeDevName();
				if(!"soccer12".equalsIgnoreCase(gameTypeDevName)){
				updatePromoTktMap(gamePlayBean, userInfoBean);
				}
			} else{
				updateSportsLotteryGameTicket("FAILED", gameResponseBean.getMerTxId(), "N", gamePlayBean.getTransId(), con,0,false);
			}
			userTxn.commit();
		} catch(SLEException e){
			e.printStackTrace();
			DBConnect.rollBackUserTransaction(userTxn);
			throw e;
		}catch (Exception e) {
			e.printStackTrace();
			DBConnect.rollBackUserTransaction(userTxn);
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally{
			DBConnect.closeConnection(con);
		}
		return gameResponseBean;
	}
	
	public LMSGamePlayResponseBean purchaseTicketForPcpos(UserInfoBean userInfoBean, SportsLotteryGamePlayBean gamePlayBean, int eventId, List<String> eventOptionsList) throws Exception{
		LMSGamePlayResponseBean gameResponseBean = null;
		Connection con = null;
		UserTransaction userTxn = null;
		long trxId = 0;
		try{
			con = DBConnect.getConnection();
			Map<Integer, DrawDetailBean> drawDetailMap = PurchaseTicketDaoImpl.getInstance().getDrawDetail(gamePlayBean.getDrawIdArray(), gamePlayBean.getGameId(), gamePlayBean.getGameTypeId(), userInfoBean.getMerchantId(), con);

			validateEventDetails(drawDetailMap.get(gamePlayBean.getDrawIdArray()[0]), gamePlayBean.getGameDrawDataBeanArray());
			gamePlayBean.setPurchaseTime(Util.getCurrentTimeString());
			userTxn = DBConnect.startTransaction();
			trxId = CommonMethodsDaoImpl.getInstance().getNewSaleTransactionId(userInfoBean, gamePlayBean, con);
			userTxn.commit();
			gamePlayBean.setTransId(trxId);
			DBConnect.closeConnection(con);
			
			gameResponseBean = LMSIntegrationImpl.purchaseTicketAtLMS(gamePlayBean, userInfoBean);
			con = DBConnect.getConnection();
			userTxn = DBConnect.startTransaction();
			if(gameResponseBean.getResponseCode() == 0) {
				purchaseSportsLotteryGameTicket(gamePlayBean,userInfoBean,trxId,con,drawDetailMap);
				updateSportsLotteryGameTicket("DONE", gameResponseBean.getMerTxId(), "N", gamePlayBean.getTransId(), con,gamePlayBean.getTicketNumber(),false);
				String gameTypeDevName = SportsLotteryUtils.gameTypeInfoMerchantMap.get(userInfoBean.getMerchantDevName()).get(gamePlayBean.getGameTypeId()).getGameTypeDevName();
				if(!"soccer12".equalsIgnoreCase(gameTypeDevName)){
				updatePromoTktMap(gamePlayBean, userInfoBean);
				}
				eventOptionsList = GameDataDaoImpl.getInstance().getEventOptionsList(eventId,gamePlayBean.getGameId(),gamePlayBean.getGameTypeId(),con);
			} else{
				updateSportsLotteryGameTicket("FAILED", gameResponseBean.getMerTxId(), "N", gamePlayBean.getTransId(), con,0,false);
			}
			userTxn.commit();
		} catch(SLEException e){
			DBConnect.rollBackUserTransaction(userTxn);
			throw e;
		}catch (Exception e) {
			DBConnect.rollBackUserTransaction(userTxn);
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally{
			DBConnect.closeConnection(con);
		}
		return gameResponseBean;
	}
}