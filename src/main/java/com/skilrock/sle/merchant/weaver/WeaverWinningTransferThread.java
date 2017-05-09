package com.skilrock.sle.merchant.weaver;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.skilrock.sle.common.DBConnect;
import com.skilrock.sle.common.SLELogger;
import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.commonMethod.daoImpl.CommonMethodsDaoImpl;
import com.skilrock.sle.merchant.weaver.WeaverUtils.BALANCE_TYPE;
import com.skilrock.sle.pwtMgmt.controllerImpl.WinningMgmtControllerImpl;
import com.skilrock.sle.pwtMgmt.javaBeans.DrawWiseTicketInfoBean;
import com.skilrock.sle.pwtMgmt.javaBeans.TicketInfoBean;

public class WeaverWinningTransferThread extends Thread{
	private static final SLELogger logger = SLELogger.getLogger(WeaverWinningTransferThread.class.getName());
	
	private int drawId;
	private int gameId;
	private int gameTypeId;
	DrawWiseTicketInfoBean infoBean;
	private static int count = 0;
	
	public WeaverWinningTransferThread(int gameId,int gameTypeId, int drawId) {
		this.drawId = drawId;
		this.gameId = gameId;
		this.gameTypeId = gameTypeId;
	}
	public WeaverWinningTransferThread(int gameId,int gameTypeId, int drawId,DrawWiseTicketInfoBean infoBean) {
		this.drawId = drawId;
		this.gameId = gameId;
		this.gameTypeId = gameTypeId;
		this.infoBean = infoBean;
	}
	public int getDrawId() {
		return drawId;
	}
	public void setDrawId(int drawId) {
		this.drawId = drawId;
	}
	public int getGameId() {
		return gameId;
	}
	public void setGameId(int gameId) {
		this.gameId = gameId;
	}
	public int getGameTypeId() {
		return gameTypeId;
	}
	public void setGameTypeId(int gameTypeId) {
		this.gameTypeId = gameTypeId;
	}

	public void run() {
		//count = 0;
		try {
			//DrawWiseTicketInfoBean infoBean = new DrawWiseTicketInfoBean();
			infoBean.setMerchantId(Util.merchantInfoMap.get("Weaver").getMerchantId());
			infoBean.setDrawId(drawId);
			infoBean.setGameId(gameId);
			infoBean.setGameTypeId(gameTypeId);
			WinningMgmtControllerImpl.getSingleInstance().fetchWinningTickets(infoBean);
			List<TicketInfoBean> ticketInfoBeanList = new ArrayList<TicketInfoBean>(infoBean.getTicketMap().values());
			callWeaverAPIForWinningTransfer(ticketInfoBeanList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void callWeaverAPIForWinningTransfer(List<TicketInfoBean> ticketInfoBeanList) throws Exception{
		if (ticketInfoBeanList != null && !ticketInfoBeanList.isEmpty()) {
			List<WeaverPlayerTxnDataBean> dataBeanList = new ArrayList<WeaverPlayerTxnDataBean>();
			for (TicketInfoBean bean : ticketInfoBeanList) {
				WeaverPlayerTxnDataBean dataBean = new WeaverPlayerTxnDataBean();
				dataBean.setWalletType(WeaverUtils.WALLET_TYPE);
				dataBean.setBalanceType(BALANCE_TYPE.MAIN.toString());
				dataBean.setAmount(String.valueOf(bean.getTotalWinningAmt()));
				dataBean.setPlayerId(String.valueOf(bean.getPartyId()));
				dataBean.setAliasName(WeaverUtils.getAliasName());
				dataBean.setRefTxnNo(String.valueOf(bean.getEnginewinTxnId()));
				dataBean.setParticular("WINNING");
				dataBean.setGameId(String.valueOf(bean.getGameId()));
				dataBean.setGameName(bean.getGameName());
				dataBean.setDevice(WeaverUtils.DEVICE);
				dataBean.setIsWithdrawable("Y");
				
				dataBeanList.add(dataBean);
			}
			List<WeaverPlayerTxnResponseDataBean> responseBeanList = WeaverIntegrationImpl.doWinningTxnAtWeaver(dataBeanList);
			logger.debug("Response List from Weaver :" + responseBeanList);
//			Take action for transactions
			updateWinningTxnStatus(responseBeanList);
		}
	}
	public void updateWinningTxnStatus(List<WeaverPlayerTxnResponseDataBean> responseBeanList) {
		Connection connection = null;
		List<String> failedTransactionList = null;
		try {
			synchronized (this) {
				connection = DBConnect.getConnection();
				connection.setAutoCommit(false);
				failedTransactionList = new ArrayList<String>();
				for (WeaverPlayerTxnResponseDataBean txnBean : responseBeanList) {
					if (txnBean.getErrorCode() == 0) {
						CommonMethodsDaoImpl.getInstance().updateWinningStatus("DONE", txnBean.getRefTxnNo(), connection);
						connection.commit(); 
					} else {
						//What to do with these tickets??
						failedTransactionList.add(String.valueOf(txnBean.getRefTxnNo()));
					}
				}			
				
				//call weaver again for failed txns if any..
				if (failedTransactionList.size() > 0 && count == 0) {
					List<TicketInfoBean> ticketList = CommonMethodsDaoImpl.getInstance().fetchInitiatedWinningTktDetails(Util.merchantInfoMap.get("Weaver").getMerchantId(), failedTransactionList, connection);
					if(ticketList != null && ticketList.size() > 0) {
					    callWeaverAPIForWinningTransfer(ticketList);
					}
					count++;
				}
				logger.info("Winning done for Weaver tickets. Request Count:"+count+" Failed Transactions:"+failedTransactionList);
			}
		} catch (SLEException e) {
			e.printStackTrace();			
		} catch (Exception e) {
			e.printStackTrace();			
		} finally {
			DBConnect.closeConnection(connection);
		}
	}
}
