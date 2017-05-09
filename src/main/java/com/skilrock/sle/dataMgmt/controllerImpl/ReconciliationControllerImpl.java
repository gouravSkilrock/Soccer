package com.skilrock.sle.dataMgmt.controllerImpl;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.transaction.UserTransaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skilrock.sle.common.DBConnect;
import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.commonMethod.daoImpl.CommonMethodsDaoImpl;
import com.skilrock.sle.dataMgmt.daoImpl.ReconciliationDaoImpl;
import com.skilrock.sle.dataMgmt.javaBeans.ReconciliationBean;
import com.skilrock.sle.gamePlayMgmt.daoImpl.CancelTicketDaoImpl;
import com.skilrock.sle.gamePlayMgmt.daoImpl.PurchaseTicketDaoImpl;
import com.skilrock.sle.merchant.common.javaBeans.MerchantInfoBean;
import com.skilrock.sle.merchant.lms.LMSIntegrationImpl;
import com.skilrock.sle.merchant.pms.PMSIntegrationImpl;
import com.skilrock.sle.pwtMgmt.daoImpl.PayPrizeTicketDaoImpl;

public class ReconciliationControllerImpl {
	private static final Logger logger = LoggerFactory.getLogger(ReconciliationControllerImpl.class);
	
	private ReconciliationControllerImpl() {
	}

	public enum Single {
		INSTANCE;
		ReconciliationControllerImpl instance = new ReconciliationControllerImpl();

		public ReconciliationControllerImpl getInstance() {
			if (instance == null)
				return new ReconciliationControllerImpl();
			else
				return instance;
		}
	}
	
	public void startReconciliation(MerchantInfoBean merchantInfoBean) throws SLEException {
		Connection con = null;
		List<ReconciliationBean> reconciliationBeans = null;
		Map<String, List<ReconciliationBean>> recMap = new HashMap<String, List<ReconciliationBean>>();
		Map<String, List<ReconciliationBean>> respMap = null;
		Set<Entry<String, List<ReconciliationBean>>> set = null;
		UserTransaction userTxn = null;
		PurchaseTicketDaoImpl purchaseTktDaoImpl = PurchaseTicketDaoImpl.getInstance();
		CancelTicketDaoImpl cancelTicketDaoImpl = new CancelTicketDaoImpl();
		long lastSettledTxnId = 0;
		long maxTxnId = 0;
		Timestamp reqTimeStamp = null;
		logger.info("Inside startReconciliation Method");

		ReconciliationDaoImpl reconciliationDaoImpl = ReconciliationDaoImpl.Single.INSTANCE.getInstance();
		try {
			con = DBConnect.getConnection();
			lastSettledTxnId = reconciliationDaoImpl.fetchLastSettleTxnId(merchantInfoBean.getMerchantId(), con);
			maxTxnId = CommonMethodsDaoImpl.getInstance().fetchMaxTxnId(merchantInfoBean.getMerchantId(), con);

			reconciliationBeans = new ArrayList<ReconciliationBean>();
			ReconciliationDaoImpl.Single.INSTANCE.getInstance().fetchInitiatedSaleTxns(reconciliationBeans, merchantInfoBean.getMerchantId(), lastSettledTxnId, maxTxnId, con);

			recMap.put("SALE", reconciliationBeans);

			reconciliationBeans = new ArrayList<ReconciliationBean>();
			reconciliationDaoImpl.fetchInitiatedRefundTxns(reconciliationBeans, merchantInfoBean.getMerchantId(), lastSettledTxnId, maxTxnId, con);

			recMap.put("REFUND", reconciliationBeans);

			reconciliationBeans = new ArrayList<ReconciliationBean>();
			reconciliationDaoImpl.fetchInitiatedPWTTxns(reconciliationBeans, merchantInfoBean.getMerchantId(), lastSettledTxnId, maxTxnId, con,merchantInfoBean.getMerchantDevName());

			recMap.put("PWT", reconciliationBeans);

			
			if(recMap.get("SALE").isEmpty() && recMap.get("PWT").isEmpty() && recMap.get("REFUND").isEmpty()){
				return;
			}
			reqTimeStamp = Util.getCurrentTimeStamp();
			if ("PMS".equals(merchantInfoBean.getMerchantDevName())) {
				DBConnect.closeConnection(con);
				respMap = PMSIntegrationImpl.reconcileTxns(recMap);
			} else if ("RMS".equals(merchantInfoBean.getMerchantDevName())) {
				DBConnect.closeConnection(con);
				respMap = LMSIntegrationImpl.reconcileTxns(recMap);
			}else if("OKPOS".equalsIgnoreCase(merchantInfoBean.getMerchantDevName()) ||"Asoft".equalsIgnoreCase(merchantInfoBean.getMerchantDevName())){
				reconciliationDaoImpl.settletTpSaleAndRefundTxn(recMap,con);
				reconciliationDaoImpl.createLastSettlementId(maxTxnId, merchantInfoBean.getMerchantId(), reqTimeStamp, con);
				DBConnect.closeConnection(con);
				return;
			}
			
			if(respMap == null)
				return;

			con = DBConnect.getConnection();
			userTxn = DBConnect.startTransaction();
			set = respMap.entrySet();
			for(Map.Entry<String, List<ReconciliationBean>> entry : set) {
				if("SALE".equals(entry.getKey())) {
					reconciliationBeans = entry.getValue();
					for(ReconciliationBean reconciliationBean : reconciliationBeans) {
//						userTxn = DBConnect.startTransaction();
						if("FAILED".equals(reconciliationBean.getStatus()))
							purchaseTktDaoImpl.updateSettledSaleStatus("FAILED", reconciliationBean.getMerchantTxnId(), "N", Long.parseLong(reconciliationBean.getEngineTxnId()), con,merchantInfoBean);
/*						else if("CANCELLED".equals(reconciliationBean.getStatus())) {
							cancelTicketBean = new CancelTicketBean();

							cancelTicketBean.setIsAutoCancel("Y");
							cancelTicketBean.setCancelType("CANCEL_MISMATCH");
							cancelTicketBean.setInterfaceType("TERMINAL");
							cancelTicketBean.setCancelDate(DateTimeFormat.getCurrentTimeStamp());
							cancelTicketBean.setTxnIdToCancel(Long.parseLong(reconciliationBean.getEngineTxnId()));

							userInfoBean = new UserInfoBean();
							CommonMethodsDaoImpl.getInstance().fetchUserInfoUsingTxnId(Long.valueOf(reconciliationBean.getEngineTxnId()), userInfoBean, con);

							cancelTicketDaoImpl.cancelTicket(cancelTicketBean, userInfoBean, con);
							cancelTicketDaoImpl.updateSetteledCancelTicketStatus(true, reconciliationBean.getMerchantTxnId(), Long.parseLong(reconciliationBean.getEngineTxnId()), cancelTicketBean.getCancelTxnId(), con);
							new PurchaseTicketDaoImpl().updateSportsLotteryGameTicket("DONE", reconciliationBean.getMerchantTxnId(), "Y", Long.parseLong(reconciliationBean.getEngineTxnId()), con);
						} else if("DONE".equals(reconciliationBean.getStatus()))
							new PurchaseTicketControllerImpl().updateSportsLotteryGameTicket("DONE", reconciliationBean.getMerchantTxnId(), "N", Long.valueOf(reconciliationBean.getEngineTxnId()));*/
//						userTxn.commit();
					}
//					DBConnect.closeConnection(con);
				} else if("REFUND".equals(entry.getKey())) {
					reconciliationBeans = entry.getValue();
//					con = DBConnect.getConnection();
					for(ReconciliationBean reconciliationBean : reconciliationBeans) {
						if("CANCELLED".equals(reconciliationBean.getStatus()))
							cancelTicketDaoImpl.updateSetteledCancelTicketStatus(true, reconciliationBean.getMerchantTxnId(), Long.parseLong(reconciliationBean.getEngineSaleTxnId()), Long.parseLong(reconciliationBean.getEngineTxnId()), con);
					}
//					DBConnect.closeConnection(con);
				} else if("PWT".equals(entry.getKey())) {
					reconciliationBeans = entry.getValue();
//					con = DBConnect.getConnection();
					for(ReconciliationBean reconciliationBean : reconciliationBeans) {
						if("DONE".equals(reconciliationBean.getStatus()))
							PayPrizeTicketDaoImpl.getSingleInstance().updateWinningRequest(Long.parseLong(reconciliationBean.getEngineTxnId()), reconciliationBean.getMerchantTxnId(), "DONE", con);
					}
//					DBConnect.closeConnection(con);
				}
			}
//			con = DBConnect.getConnection();
			reconciliationDaoImpl.createLastSettlementId(maxTxnId, merchantInfoBean.getMerchantId(), reqTimeStamp, con);
			userTxn.commit();
//			DBConnect.closeConnection(con);
		} catch (SLEException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(con);
		}
	}
}
