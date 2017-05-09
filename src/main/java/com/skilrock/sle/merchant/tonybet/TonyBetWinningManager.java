package com.skilrock.sle.merchant.tonybet;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.skilrock.sle.common.DBConnect;
import com.skilrock.sle.common.SLELogger;
import com.skilrock.sle.common.SportsLotteryUtils;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.common.javaBeans.UserInfoBean;
import com.skilrock.sle.commonMethod.daoImpl.CommonMethodsDaoImpl;
import com.skilrock.sle.gamePlayMgmt.javaBeans.SportsLotteryGamePlayBean;
import com.skilrock.sle.merchant.weaver.WeaverWinningTransferThread;
import com.skilrock.sle.pwtMgmt.controllerImpl.WinningMgmtControllerImpl;
import com.skilrock.sle.pwtMgmt.javaBeans.DrawWiseTicketInfoBean;
import com.skilrock.sle.pwtMgmt.javaBeans.TicketInfoBean;

public class TonyBetWinningManager extends Thread{
	private static final SLELogger logger = SLELogger.getLogger(WeaverWinningTransferThread.class.getName());

	DrawWiseTicketInfoBean infoBean;
	private static int count = 0;
	public TonyBetWinningManager(DrawWiseTicketInfoBean infoBean) {
	    this.infoBean=infoBean;
	}
	public void run() {
		
			try {
				WinningMgmtControllerImpl.getSingleInstance().fetchWinningTickets(infoBean);
				List<TicketInfoBean> ticketInfoBeanList = new ArrayList<TicketInfoBean>(infoBean.getTicketMap().values());
				callTonyBetAPIForWinningTransfer(ticketInfoBeanList);
			} catch (Exception e) {
				logger.error("Error", e);
			}
			
	
	}

	private void callTonyBetAPIForWinningTransfer(List<TicketInfoBean> ticketInfoBeanList) throws Exception{
		if (ticketInfoBeanList != null && !ticketInfoBeanList.isEmpty()) {
			List<TonyBetGamePlayResponseBean> responseBeanList= new ArrayList<TonyBetGamePlayResponseBean>();
			getResponseList(ticketInfoBeanList, responseBeanList);
		 //	Take action for transactions
			updateWinningTxnStatus(responseBeanList);
		}
	}
	private void getResponseList(List<TicketInfoBean> ticketInfoBeanList,
			List<TonyBetGamePlayResponseBean> responseBeanList) throws SLEException {
		for (TicketInfoBean bean : ticketInfoBeanList) {
			SportsLotteryGamePlayBean gamePlayBean  = new SportsLotteryGamePlayBean();
			UserInfoBean userInfoBean  = new 		UserInfoBean();
			gamePlayBean.setTransId(Long.parseLong(bean.getEnginewinTxnId()));
			gamePlayBean.setGameId(infoBean.getGameId());
			gamePlayBean.setGameTypeId(infoBean.getGameTypeId());
			gamePlayBean.setGameDevName( SportsLotteryUtils.gameTypeInfoMerchantMap.get("TonyBet").get(gamePlayBean.getGameTypeId()).getGameTypeDevName());
			userInfoBean.setMerchantUserId(bean.getPartyId());
			gamePlayBean.setWinAmt(bean.getTotalWinningAmt());
			TonyBetGamePlayResponseBean depositResponse = TonyBetIntegrationImpl.depositTranaction(gamePlayBean, userInfoBean);
			depositResponse.setSleTranactionId(Long.parseLong(bean.getEnginewinTxnId()));
			responseBeanList.add(depositResponse);
		}
	}
	public void updateWinningTxnStatus(List<TonyBetGamePlayResponseBean> responseBeanList) {
		Connection connection = null;
		List<String> failedTransactionList = null;
		try {
			synchronized (this) {
				connection = DBConnect.getConnection();
				connection.setAutoCommit(false);
				failedTransactionList = new ArrayList<String>();
				for (TonyBetGamePlayResponseBean txnBean : responseBeanList) {
					if (txnBean.getResultCode() == 1) {
						CommonMethodsDaoImpl.getInstance().updateWinningStatus("DONE", txnBean.getTransactionId(),txnBean.getSleTranactionId(), connection);
						connection.commit(); 
					} else {
						//What to do with these tickets??
						failedTransactionList.add(String.valueOf(txnBean.getTransactionId()));
					}
				}			
		
				doDepositForFailedTxns(connection, failedTransactionList);
				logger.info("Winning done for Tony Bet tickets. Request Count:"+count+" Failed Transactions:"+failedTransactionList);
			}
		} catch (SLEException e) {
			e.printStackTrace();			
		} catch (Exception e) {
			e.printStackTrace();			
		} finally {
			DBConnect.closeConnection(connection);
		}
	}
	private void doDepositForFailedTxns(Connection connection, List<String> failedTransactionList)
			throws SLEException, Exception {
		//call weaver again for failed txns if any..
		if (failedTransactionList.size() > 0 && count == 0) {
			List<TicketInfoBean> ticketList = CommonMethodsDaoImpl.getInstance().fetchInitiatedWinningTktDetails(infoBean.getMerchantId(), failedTransactionList, connection);
			if(ticketList != null && ticketList.size() > 0) {
				callTonyBetAPIForWinningTransfer(ticketList);
			}
			count++;
		}
	}
	

}
