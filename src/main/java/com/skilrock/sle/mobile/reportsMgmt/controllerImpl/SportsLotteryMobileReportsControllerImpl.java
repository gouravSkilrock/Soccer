package com.skilrock.sle.mobile.reportsMgmt.controllerImpl;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.skilrock.sle.common.DBConnect;
import com.skilrock.sle.common.SLELogger;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.common.javaBeans.ReportBean;
import com.skilrock.sle.common.javaBeans.TicketInfoBean;
import com.skilrock.sle.common.javaBeans.TicketSalePwtInfoBean;
import com.skilrock.sle.common.javaBeans.UserInfoBean;
import com.skilrock.sle.dataMgmt.javaBeans.TicketTxnStatusBean;
import com.skilrock.sle.gameDataMgmt.daoImpl.GameDataDaoImpl;
import com.skilrock.sle.gamePlayMgmt.daoImpl.ReprintTicketDaoImpl;
import com.skilrock.sle.gamePlayMgmt.javaBeans.ReprintTicketBean;
import com.skilrock.sle.gamePlayMgmt.javaBeans.SportsLotteryGamePlayBean;
import com.skilrock.sle.merchant.common.javaBeans.MerchantInfoBean;
import com.skilrock.sle.mobile.reportsMgmt.daoImpl.SportsLotteryMobileReportsDaompl;

public class SportsLotteryMobileReportsControllerImpl {
	private static final SLELogger logger = SLELogger.getLogger(SportsLotteryMobileReportsControllerImpl.class.getName());
	
	public Map<Long, TicketInfoBean> fetchPurchaseTicketReport(UserInfoBean userInfoBean, ReportBean reportBean) throws SLEException {
		logger.info("***** Inside fetchPurchaseTicketReport Method");
		Connection con = null;
		SportsLotteryMobileReportsDaompl mobileReportsDaompl = new SportsLotteryMobileReportsDaompl();
		Map<Long, TicketInfoBean> tktInfoMap = null;
		try {
			con = DBConnect.getConnection();
			tktInfoMap = mobileReportsDaompl.fetchPurchaseTicketReport(userInfoBean, reportBean, con);
		} catch (SLEException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(con);
		}
		return tktInfoMap;
	}
	
	public List<String> fetchPurchaseTicketData(ReprintTicketBean tktBean, MerchantInfoBean merchantInfoBean,long txnId) throws SLEException{
		Connection con = null;
		int rpc_total = 0;
		SportsLotteryGamePlayBean gamePlayBean = null;
		ReprintTicketDaoImpl daoImpl = null;
		List<String> eventTypeList=new ArrayList<String>();
		List<TicketTxnStatusBean> ticketwinBean=new ArrayList<TicketTxnStatusBean>();
		List<String> txnIdlIst=new ArrayList<String>();
		try {
			txnIdlIst.add(txnId+"");
			con = DBConnect.getConnection();
			//rpc_total = CommonMethodsDaoImpl.getInstance().fetchRPCOfTicket(tktBean, con);
			tktBean.setRpc(rpc_total);
			tktBean.setAvlBal(0.0);
			daoImpl = new ReprintTicketDaoImpl();
			ticketwinBean = GameDataDaoImpl.getInstance().getSportsLotteryTxnStatus(merchantInfoBean,txnIdlIst.toString(),con);
			gamePlayBean = daoImpl.reprintSportsLotteryGameTicket(tktBean, merchantInfoBean, con);			
			gamePlayBean.setWinStatus(ticketwinBean.get(0).getWinStatus());
			gamePlayBean.setWinAmt(ticketwinBean.get(0).getWinAmt());
			tktBean.setGamePlayBean(gamePlayBean);
			eventTypeList=GameDataDaoImpl.getInstance().getEventOptionsList(tktBean.getEventTypeId(),tktBean.getGameId(),tktBean.getGameTypeId(),con);
		} catch (SLEException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(con);
		}
		return eventTypeList;
	}
	
	public List<TicketSalePwtInfoBean> fetchDetailedSmallWinningPaymentReport(int merchantId,int gameId,int gameTypeId,String sDate,String eDate,String detailType,String retList) throws SLEException{
		List<TicketSalePwtInfoBean> ticketDataList = null;
		Connection con = null;
		try {
		     con = DBConnect.getConnection();
		     ticketDataList = GameDataDaoImpl.getInstance().fetchDetailedSmallWinningPaymentReport(merchantId, gameId, gameTypeId, sDate, eDate, detailType, retList,con);
		    
		} catch (SLEException e) {
			e.printStackTrace();
			throw e;
			
		}catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}finally{
			DBConnect.closeConnection(con);
		}
		return ticketDataList;
	}
	
}
