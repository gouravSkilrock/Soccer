package com.skilrock.sle.gamePlayMgmt.controllerImpl;

import java.sql.Connection;

import javax.transaction.UserTransaction;

import com.skilrock.sle.common.ConfigurationVariables;
import com.skilrock.sle.common.DBConnect;
import com.skilrock.sle.common.SLELogger;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.common.javaBeans.UserInfoBean;
import com.skilrock.sle.commonMethod.daoImpl.CommonMethodsDaoImpl;
import com.skilrock.sle.gamePlayMgmt.daoImpl.ReprintTicketDaoImpl;
import com.skilrock.sle.gamePlayMgmt.javaBeans.ReprintTicketBean;
import com.skilrock.sle.gamePlayMgmt.javaBeans.SportsLotteryGamePlayBean;
import com.skilrock.sle.merchant.common.javaBeans.MerchantInfoBean;
import com.skilrock.sle.merchant.lms.LMSIntegrationImpl;
import com.skilrock.sle.merchant.lms.common.javaBeans.LMSGamePlayResponseBean;


public class ReprintTicketControllerImpl {
	
	private static final SLELogger logger = SLELogger.getLogger(ReprintTicketControllerImpl.class.getName());

	public ReprintTicketBean reprintSportsLotteryGameTicket(UserInfoBean userInfoBean, MerchantInfoBean merchantInfoBean,String channelType) throws SLEException {
		Connection con = null;
		CommonMethodsDaoImpl commonDao = null;
		ReprintTicketDaoImpl daoImpl = null;
		ReprintTicketBean tktBean = null;
		boolean isTicketCancel = false;
		boolean reprintTktStatus = true;
		String drawStatus=null;
		SportsLotteryGamePlayBean tktReprintDataBean = null;
		UserTransaction userTxn = null;

		try{
				con = DBConnect.getConnection();
				userTxn = DBConnect.startTransaction();
				daoImpl = new ReprintTicketDaoImpl();
				commonDao = CommonMethodsDaoImpl.getInstance();
				tktBean = daoImpl.fetchLastSoldTicket(userInfoBean,channelType, con) ;
				drawStatus=commonDao.fetchTicketWiseDrawStatus(tktBean.getGameId(),tktBean.getGameTypeId(),tktBean.getTicketNumber(),con);
				/*if(!"ACTIVE".equalsIgnoreCase(drawStatus)){
					throw new SLEException(SLEErrors.TICKET_REPRINT_FAILED_ERROR_CODE,SLEErrors.TICKET_REPRINT_FAILED_ERROR_MESSAGE);
				}*/
				isTicketCancel = commonDao.isTicketCancel(userInfoBean, tktBean, con);
				if(isTicketCancel){
					throw new SLEException(SLEErrors.CANCELLED_TICKET_ERROR_CODE, SLEErrors.CANCELLED_TICKET_ERROR_MESSAGE);						
				}else{
					if("RMS".equalsIgnoreCase(merchantInfoBean.getMerchantDevName())){
						tktBean.setInterfaceType(channelType);
						LMSGamePlayResponseBean resp = LMSIntegrationImpl.reprintTicketAtLMS(tktBean, userInfoBean);
						if(resp.getResponseCode() == 0){							
							int rpc_total = commonDao.fetchRPCOfTicket(tktBean, con);
							tktBean.setRpc(rpc_total);
							tktBean.setAvlBal(resp.getAvailBal());
							if(rpc_total > ConfigurationVariables.rpcLimitAtRMS){
								reprintTktStatus = false;
								throw new SLEException(SLEErrors.RPC_LIMIT_EXCEED_ERROR_CODE, SLEErrors.RPC_LIMIT_EXCEED_ERROR_MESSAGE);	
							}
						}else{
							if(resp.getResponseCode() == 118){
								throw new SLEException(SLEErrors.INVALID_SESSION_ERROR_CODE, SLEErrors.INVALID_SESSION_ERROR_MESSAGE);
							}else if(resp.getResponseCode() == 2016){
								throw new SLEException(SLEErrors.RG_LIMIT_EXCEPTION_ERROR_CODE,SLEErrors.Reprint_LIMIT_EXCEPTION_ERROR_MESSAGE);
							}else{
								throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);	
							}
						}
					}else{
						reprintTktStatus = false; //For anyother merchant like pms (not implemented yet)
					}
				}				
				if(reprintTktStatus){
					logger.debug("updating rpc... (tkt is ready to reprint !!)");
					daoImpl.updateTicketsTable(tktBean, con);
				}				
				tktReprintDataBean = daoImpl.reprintSportsLotteryGameTicket(tktBean, merchantInfoBean, con);
				tktBean.setGamePlayBean(tktReprintDataBean);
				userTxn.commit();
			}catch(SLEException se){
				se.printStackTrace();
				DBConnect.rollBackUserTransaction(userTxn);
				throw se;
			}catch(Exception e){
				e.printStackTrace();
				DBConnect.rollBackUserTransaction(userTxn);
				throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);						
			}finally{
				DBConnect.closeConnection(con);
			}
			return tktBean;
		}
}
