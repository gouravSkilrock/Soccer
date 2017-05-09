package com.skilrock.sle.merchant.weaver;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WeaverTxnConfirmationThread implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(WeaverTxnConfirmationThread.class);
	private long playerId;
	private String txnId;
    private List<WeaverPlayerTxnDataBean> weaverPlrTxnBeanList;
	public WeaverTxnConfirmationThread(long playerId, String txnId) {
		this.playerId = playerId;
		this.txnId = txnId;
	}

	public WeaverTxnConfirmationThread(	List<WeaverPlayerTxnDataBean> weaverPlrTxnBeanList) {
		this.weaverPlrTxnBeanList=weaverPlrTxnBeanList;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public String getTxnId() {
		return txnId;
	}

	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}

	public void run() {
		try {
			logger.info("WeaverTxnConfirmation Thread Starts"+weaverPlrTxnBeanList);
			WeaverIntegrationImpl.confirmTicketTxnAtWeaver(weaverPlrTxnBeanList);
			logger.info("WeaverTxnConfirmation Thread ends"+weaverPlrTxnBeanList);
		} catch (Exception e) {
			logger.error("Error",e);
		}
	}
	
	

}
