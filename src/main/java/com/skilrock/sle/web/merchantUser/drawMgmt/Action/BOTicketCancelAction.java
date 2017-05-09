package com.skilrock.sle.web.merchantUser.drawMgmt.Action;

import java.util.Map;

import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.skilrock.sle.common.DateTimeFormat;
import com.skilrock.sle.common.SportsLotteryUtils;
import com.skilrock.sle.common.exception.GenericException;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.common.javaBeans.UserInfoBean;
import com.skilrock.sle.drawMgmt.controllerImpl.TicketCancelControllerImpl;
import com.skilrock.sle.drawMgmt.controllerImpl.TrackTicketControllerImpl;
import com.skilrock.sle.drawMgmt.javaBeans.TrackSLETicketBean;
import com.skilrock.sle.embedded.common.BaseActionWeb;
import com.skilrock.sle.embedded.playMgmt.common.javaBeans.CancelTicketBean;
import com.skilrock.sle.gamePlayMgmt.controllerImpl.CancelTicketControllerImpl;
import com.skilrock.sle.merchant.lms.LMSIntegrationImpl;
import com.skilrock.sle.merchant.lms.common.javaBeans.LMSCancelTicketResponseBean;
import com.skilrock.sle.merchant.pms.PMSIntegrationImpl;

public class BOTicketCancelAction extends BaseActionWeb {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public BOTicketCancelAction() {
		super(BOTicketCancelAction.class.getName());
	}

	private String ticketNumber;
	private TrackSLETicketBean trackTicketBean;
	private String reasonForCancel;
	private long transId;
	private long merchantUserId;
	
	
	
	public long getMerchantUserId() {
		return merchantUserId;
	}



	public void setMerchantUserId(long merchantUserId) {
		this.merchantUserId = merchantUserId;
	}



	public long getTransId() {
		return transId;
	}



	public void setTransId(long transId) {
		this.transId = transId;
	}



	public String getTicketNumber() {
		return ticketNumber;
	}



	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}



	public TrackSLETicketBean getTrackTicketBean() {
		return trackTicketBean;
	}



	public void setTrackTicketBean(TrackSLETicketBean trackTicketBean) {
		this.trackTicketBean = trackTicketBean;
	}



	public String getReasonForCancel() {
		return reasonForCancel;
	}



	public void setReasonForCancel(String reasonForCancel) {
		this.reasonForCancel = reasonForCancel;
	}



	public String cancelTicketDetails() {
		TrackTicketControllerImpl controllerImpl = null;
		TicketCancelControllerImpl tktControllerImpl=null;
		HttpSession session = null;
		boolean isValidTkt;
		try {
			controllerImpl = TrackTicketControllerImpl.getInstance();
			session = request.getSession();
			String merCode = session.getAttribute("MER_CODE").toString();
			tktControllerImpl=new TicketCancelControllerImpl();
			isValidTkt=tktControllerImpl.verifyTicket(merCode,ticketNumber);
			if(!isValidTkt)
			throw new SLEException(SLEErrors.INVALID_TICKET_ERROR_CODE, SLEErrors.INVALID_TICKET_ERROR_MESSAGE);	
			//ticketNumber = "64776233150486030";
			trackTicketBean = controllerImpl.trackSLETicket(merCode,ticketNumber);
			logger.info(new Gson().toJson(trackTicketBean));
		} catch (SLEException se) {
			logger.info("ErrorCode - " + se.getErrorCode() + " | ErrorMessage - " + se.getErrorMessage());
			request.setAttribute("SLE_EXCEPTION", se.getErrorMessage());
			return "applicationAjaxException";
		}

		return SUCCESS;
	}
	
	
	public String cancelTicketAtBo(){
		logger.info("***** Inside cancelTicket Method");
		CancelTicketBean cancelTicketBean = null;
		UserInfoBean userInfoBean = null;
		HttpSession session = null;
		CancelTicketControllerImpl cancelTicketControllerImpl=null;
		LMSCancelTicketResponseBean cancelTicketResponseBean = null;
		
		try{
			System.out.println("Ticket for Cancellation:"+ticketNumber);
			session = request.getSession();
			userInfoBean = (UserInfoBean)session.getAttribute("USER_INFO");
			logger.info(new Gson().toJson(userInfoBean));
			cancelTicketControllerImpl = new CancelTicketControllerImpl();
		
			cancelTicketBean = new CancelTicketBean();
			cancelTicketBean.setTktMerchantUserId(merchantUserId);
			cancelTicketBean.setIsAutoCancel("N");
			cancelTicketBean.setCancelType("CANCEL_MANUAL");
			cancelTicketBean.setTxnIdToCancel(transId);
			cancelTicketBean.setInterfaceType("WEB");
			cancelTicketBean.setCancelDate(DateTimeFormat.getCurrentTimeStamp());
		
			logger.info("Cancel Ticket Bean is Before Transaction "+cancelTicketBean);
			cancelTicketControllerImpl.cancelTicket(cancelTicketBean, userInfoBean,false);
			logger.info("Cancel Ticket Bean is After Transaction "+cancelTicketBean);
			
			String gameTypeDevName = SportsLotteryUtils.gameTypeInfoMerchantMap.get(userInfoBean.getMerchantDevName()).get(cancelTicketBean.getGameTypeId()).getGameTypeDevName();
			if(!"soccer12".equalsIgnoreCase(gameTypeDevName)){
			if ("YES".equals(SportsLotteryUtils.gameTypeInfoMerchantMap.get(userInfoBean.getMerchantDevName()).get(cancelTicketBean.getGameTypeId()).getJackPotMessageDisplay())) {
				Map<Integer, Double> drawSaleMap = SportsLotteryUtils.drawSaleMap.get(userInfoBean.getMerchantId() + "_" + cancelTicketBean.getGameId() + "_" + cancelTicketBean.getGameTypeId());
				if(drawSaleMap.containsKey(cancelTicketBean.getDrawId())) {
					double totalSaleAmt = drawSaleMap.get(cancelTicketBean.getDrawId());
					drawSaleMap.put(cancelTicketBean.getDrawId(), totalSaleAmt - cancelTicketBean.getCancelAmount());
				}
			}
			}
			
			
			if("RMS".equalsIgnoreCase(userInfoBean.getMerchantDevName()))
			cancelTicketResponseBean = LMSIntegrationImpl.cancelTicketAtLMS(cancelTicketBean, userInfoBean);
			else if("PMS".equalsIgnoreCase(userInfoBean.getMerchantDevName()))
			cancelTicketResponseBean = PMSIntegrationImpl.cancelTicketAtPMS(cancelTicketBean, userInfoBean);

			if(cancelTicketResponseBean.getResponseCode() == 0 && !"0".equals(cancelTicketResponseBean.getMerTxId())) {
				cancelTicketControllerImpl.updateSportsLotteryCancelTicket(true, cancelTicketResponseBean.getMerTxId(), cancelTicketBean.getSaleTxnId(), cancelTicketBean.getCancelTxnId());
			} else {
				cancelTicketControllerImpl.updateSportsLotteryCancelTicket(false, "-1", cancelTicketBean.getSaleTxnId(), cancelTicketBean.getCancelTxnId());
				if(cancelTicketResponseBean.getResponseCode() == 118){
					throw new SLEException(SLEErrors.INVALID_SESSION_ERROR_CODE, SLEErrors.INVALID_SESSION_ERROR_MESSAGE);
				}else if(cancelTicketResponseBean.getResponseCode() == 2016){
					throw new SLEException(SLEErrors.RG_LIMIT_EXCEPTION_ERROR_CODE,SLEErrors.CANCEL_LIMIT_EXCEPTION_ERROR_MESSAGE);
				}else{
					throw new  SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
				}
				
			}
		}catch (SLEException se) {
			logger.info("ErrorCode - " + se.getErrorCode() + " | ErrorMessage - " + se.getErrorMessage());
			request.setAttribute("SLE_EXCEPTION", se.getErrorMessage());
			return "applicationAjaxException";
		}catch (GenericException se) {
			logger.info("ErrorCode - " + se.getErrorCode() + " | ErrorMessage - " + se.getErrorMessage());
			request.setAttribute("SLE_EXCEPTION","Ticket Cancelled Successfully");
			return "applicationAjaxException";
		}
		
		return SUCCESS;
	}
	
}