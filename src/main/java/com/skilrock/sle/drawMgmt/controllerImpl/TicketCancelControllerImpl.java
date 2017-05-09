package com.skilrock.sle.drawMgmt.controllerImpl;

import java.sql.Connection;

import com.skilrock.sle.common.DBConnect;
import com.skilrock.sle.common.UtilityFunctions;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.common.javaBeans.UserInfoBean;
import com.skilrock.sle.commonMethod.daoImpl.CommonMethodsDaoImpl;
import com.skilrock.sle.gamePlayMgmt.javaBeans.ReprintTicketBean;
import com.skilrock.sle.pwtMgmt.daoImpl.PrizeTicketVerificationDaoImpl;
import com.skilrock.sle.pwtMgmt.javaBeans.PwtVerifyTicketBean;
import com.skilrock.sle.pwtMgmt.javaBeans.PwtVerifyTicketDrawDataBean;

public class TicketCancelControllerImpl{
	
	
	public boolean verifyTicket(String merchantName,String ticketNumber) throws SLEException{
		boolean isValidTicket;
		boolean isTktCancel;
		PwtVerifyTicketBean verifyTicketBean = null;
		Connection con=null;
		UserInfoBean userBean=null;
		CommonMethodsDaoImpl commonDao = null;
		ReprintTicketBean tktBean=null;
		PwtVerifyTicketDrawDataBean[] pwtVerifyTicketDrawDataArr = null;
		PwtVerifyTicketDrawDataBean pwtVerifyDrawDataBean = null;
		
		//for check ticket is valid or not
		
		try{
		con=DBConnect.getConnection();
		//check ticket is valid for cancellation
		verifyTicketBean = PrizeTicketVerificationDaoImpl.getSingleInstance().prizeWinningVerifyTicket(userBean,merchantName, ticketNumber, con);
		pwtVerifyTicketDrawDataArr=verifyTicketBean.getVerifyTicketDrawDataBeanArray();
		pwtVerifyDrawDataBean=pwtVerifyTicketDrawDataArr[0];
		
		if("ACTIVE".equals(pwtVerifyDrawDataBean.getActuatDrawStatus()))
			isValidTicket=true;		
		else
			throw new SLEException(SLEErrors.TICKET_CANCELLATION_ERROR_CODE, SLEErrors.TICKET_CANCELLATION_ERROR_MESSAGE);
		
		commonDao = CommonMethodsDaoImpl.getInstance();
		userBean=new UserInfoBean();
		tktBean=new ReprintTicketBean();
	
		
		//for check Ticket is already cancel or not
		userBean.setMerchantId(UtilityFunctions.getMerchantIdFromMerchantName(merchantName, con));
		tktBean.setGameId(verifyTicketBean.getGameId());
		tktBean.setGameTypeId(verifyTicketBean.getGameTypeId());
		tktBean.setTicketNumber(verifyTicketBean.getTicketNumInDB());
		isTktCancel=commonDao.isTicketCancel(userBean, tktBean, con);
		
		if(isTktCancel)
			throw new SLEException(SLEErrors.CANCELLED_TICKET_ERROR_CODE, SLEErrors.CANCELLED_TICKET_ERROR_MESSAGE);
		isTktCancel=commonDao.isTicketCancelrequested(userBean, tktBean, con);
		if(isTktCancel)
			throw new SLEException(SLEErrors.CANCELLED_TICKET_INITIATE_ERROR_CODE, SLEErrors.CANCELLED_TICKET_INITIATE_ERROR_MESSAGE);
		}catch(SLEException se) {
				throw se;
			} catch(Exception e) {
				e.printStackTrace();
				throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			} 
			
		finally{
			DBConnect.closeConnection(con);
		}
		
		
		return isValidTicket;
	
	}
}

