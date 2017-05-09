package com.skilrock.sle.embedded.playMgmt.action;

import java.io.IOException;
import java.util.Map;

import com.skilrock.sle.common.ConfigurationVariables;
import com.skilrock.sle.common.DateTimeFormat;
import com.skilrock.sle.common.SportsLotteryUtils;
import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.exception.GenericException;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.common.javaBeans.UserInfoBean;
import com.skilrock.sle.common.javaBeans.ValidateTicketBean;
import com.skilrock.sle.commonMethod.serviceImpl.CommonMethodsServiceImpl;
import com.skilrock.sle.drawMgmt.controllerImpl.TrackTicketControllerImpl;
import com.skilrock.sle.embedded.common.BaseActionTerminal;
import com.skilrock.sle.embedded.common.SportsLotteryResponseData;
import com.skilrock.sle.embedded.playMgmt.common.javaBeans.CancelTicketBean;
import com.skilrock.sle.gamePlayMgmt.controllerImpl.CancelTicketControllerImpl;
import com.skilrock.sle.merchant.lms.LMSIntegrationImpl;
import com.skilrock.sle.merchant.lms.common.javaBeans.LMSCancelTicketResponseBean;

public class SportsLotteryCancelTicketAction extends BaseActionTerminal {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String cancelType;
	private String autoCancel;
	private String ticketNo;
	private String isLstTktCancel;

	public SportsLotteryCancelTicketAction() {
		super(SportsLotteryCancelTicketAction.class.getName());
	}

	public String getCancelType() {
		return cancelType;
	}

	public void setCancelType(String cancelType) {
		this.cancelType = cancelType;
	}

	public String getAutoCancel() {
		return autoCancel;
	}

	public void setAutoCancel(String autoCancel) {
		this.autoCancel = autoCancel;
	}

	public String getTicketNo() {
		return ticketNo;
	}

	public void setTicketNo(String ticketNo) {
		this.ticketNo = ticketNo;
	}

	public String getIsLstTktCancel() {
		return isLstTktCancel;
	}

	public void setIsLstTktCancel(String isLstTktCancel) {
		this.isLstTktCancel = isLstTktCancel;
	}

	public void cancelTicket() {
		logger.info("***** Inside cancelTicket Method");
		CancelTicketBean cancelTicketBean = null;
		UserInfoBean userInfoBean = null;
		CancelTicketControllerImpl cancelTicketControllerImpl = new CancelTicketControllerImpl();
		LMSCancelTicketResponseBean cancelTicketResponseBean = null;
		StringBuilder successMsg=null;
		
		String cancelTypeStatus = cancelType;
		
		boolean isCancel = true;
		String responseString = null;
		try {
			userInfoBean = new UserInfoBean();
			userInfoBean.setUserName(getUserName());
			userInfoBean.setMerchantId((Integer)request.getAttribute("merchantId"));
			userInfoBean.setUserSessionId(getSessId());
			userInfoBean.setMerchantDevName(getMerCode());

			CommonMethodsServiceImpl.getInstance().fetchAndCheckUserData(userInfoBean);
			logger.debug("Merchant User Info Bean is "+userInfoBean);
			
			if("paperOut".equals(cancelType))
				cancelTypeStatus = "PAPER_OUT";
			else if("responseTimeOut".equals(cancelType))
				cancelTypeStatus = "RESPONSE_TIME_OUT";
			else if("manual".equals(cancelType))
				cancelTypeStatus = "CANCEL_MANUAL";
			else if("dataError".equals(cancelType))
				cancelTypeStatus = "DATA_ERROR";
			
			if("manual".equals(cancelTypeStatus))
				CommonMethodsServiceImpl.getInstance().checkAndAutoCancel(getSlLstTxnId(), "CANCEL_MISMATCH", userInfoBean);
			else 
				CommonMethodsServiceImpl.getInstance().checkAndAutoCancel(getSlLstTxnId(), cancelTypeStatus, userInfoBean);
			
			if("true".equals(autoCancel)) {
				response.getOutputStream().write("ErrorMsg:Sale Failed, Check Your Balance!".getBytes());
				return;
			}
			if(isLstTktCancel==null || "".equalsIgnoreCase(isLstTktCancel)|| "TRUE".equalsIgnoreCase(isLstTktCancel)){
				isCancel = cancelTicketControllerImpl.checkForManualCancel(getSlLstTxnId(), userInfoBean);
				if(isCancel == false) {
					response.getOutputStream().write("ErrorMsg:Ticket Can't Be Cancelled".getBytes());
					return;
				}
			}else{
				if("RMS".equalsIgnoreCase(userInfoBean.getMerchantDevName())){
					if(ticketNo==null || "".equals(ticketNo)){
						throw new SLEException(SLEErrors.INVALID_TICKET_ERROR_CODE,SLEErrors.INVALID_TICKET_ERROR_MESSAGE);
					}
					String ticketNum = TrackTicketControllerImpl.getTicketNumber(ticketNo, 3);

					ValidateTicketBean validateTktBean = new ValidateTicketBean(ticketNum);
					SportsLotteryUtils.validateTkt(validateTktBean, userInfoBean.getMerchantDevName());
					if(!validateTktBean.isValid())
						throw new SLEException(SLEErrors.INVALID_TICKET_ERROR_CODE,SLEErrors.INVALID_TICKET_ERROR_MESSAGE);
					int barCodeCount=0;
					if(ticketNo.length() == ConfigurationVariables.barCodeCountRMS){
						barCodeCount = SportsLotteryUtils.getBarCodeCountFromTicketNumber(ticketNo);
						if(barCodeCount==0){
							throw new SLEException(SLEErrors.INVALID_TICKET_ERROR_CODE, SLEErrors.INVALID_TICKET_ERROR_MESSAGE);
						}
						validateTktBean.setBarCodeCount(barCodeCount);
						if(!SportsLotteryUtils.isBarCodeCorrectFotTicket(validateTktBean.getTicketNumInDB(), validateTktBean.getGameid(),validateTktBean.getGameTypeId(), validateTktBean.getBarCodeCount())){
							throw new SLEException(SLEErrors.INVALID_TICKET_ERROR_CODE, SLEErrors.INVALID_TICKET_ERROR_MESSAGE);
						}
					}
			
					setSlLstTxnId(validateTktBean.getTransId()+"");
				}else{
				response.getOutputStream().write("ErrorMsg:Ticket Can't Be Cancelled".getBytes());
				return;
			}
			
			}
			
			cancelTicketBean = new CancelTicketBean();
			
			if("true".equals(autoCancel))
				cancelTicketBean.setIsAutoCancel("Y");
			else if("false".equals(autoCancel))
				cancelTicketBean.setIsAutoCancel("N");

			cancelTicketBean.setCancelType(cancelTypeStatus);

			cancelTicketBean.setTxnIdToCancel(Long.parseLong(getSlLstTxnId()));
			cancelTicketBean.setInterfaceType("TERMINAL");
			cancelTicketBean.setCancelDate(DateTimeFormat.getCurrentTimeStamp());
			
			//Cancel Ticket at SLE
			
			logger.debug("Cancel Ticket Bean is Before Transaction "+cancelTicketBean);
			cancelTicketControllerImpl.cancelTicket(cancelTicketBean, userInfoBean,false);
			logger.debug("Cancel Ticket Bean is After Transaction "+cancelTicketBean);
			
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
			try{
			cancelTicketResponseBean = LMSIntegrationImpl.cancelTicketAtLMS(cancelTicketBean, userInfoBean);
				cancelTicketBean.setAdvMessageMap(cancelTicketResponseBean.getAdvMessageMap());
			}
			catch (GenericException e) {
				successMsg=new StringBuilder();
				successMsg.append("ErrorMsg:Ticket Cancelled Successfully! Please refund ").append(cancelTicketBean.getCancelAmount()).append(" ").append(Util.getPropertyValue("CURRENCY_SYMBOL"));
				response.getOutputStream().write(successMsg.toString().getBytes());
				return;
			}

			if(cancelTicketResponseBean.getResponseCode() == 0) {
				cancelTicketControllerImpl.updateSportsLotteryCancelTicket(true, cancelTicketResponseBean.getMerTxId(), cancelTicketBean.getSaleTxnId(), cancelTicketBean.getCancelTxnId());
				responseString = SportsLotteryResponseData.generateSportsLotterySaleRefundResponseData(cancelTicketBean, cancelTicketResponseBean.getTxAmount(), cancelTicketResponseBean.getAvailBal());
			} else {
				cancelTicketControllerImpl.updateSportsLotteryCancelTicket(false, "-1", cancelTicketBean.getSaleTxnId(), cancelTicketBean.getCancelTxnId());
				responseString = "ErrorMsg:Error! Cancel Failed";
				if(cancelTicketResponseBean.getResponseCode() == 118){
					throw new SLEException(SLEErrors.INVALID_SESSION_ERROR_CODE, SLEErrors.INVALID_SESSION_ERROR_MESSAGE);
				}else if(cancelTicketResponseBean.getResponseCode() ==2016){
					throw new SLEException(SLEErrors.RG_LIMIT_EXCEPTION_ERROR_CODE,SLEErrors.CANCEL_LIMIT_TERMINAL_EXCEPTION_ERROR_MESSAGE);
				}else if (cancelTicketResponseBean.getResponseMessage()!=null) {
					responseString="ErrorMsg:"+cancelTicketResponseBean.getResponseMessage();
				}
			}
			logger.debug("***** Terminal Game Refund Response "+responseString);
			response.getOutputStream().write(responseString.getBytes());
		} catch (SLEException e) {
			try {
				if(e.getErrorCode() == 10012){
					response.getOutputStream().write(("ErrorMsg:" +SLEErrors.INVALID_SESSION_MOBILE_ERROR_MESSAGE+"|ErrorCode:01|").getBytes());
				} else if (e.getErrorCode() == 2016) {
					response.getOutputStream().write(("ErrorMsg:" + e.getErrorMessage()+"|ErrorCode:"+e.getErrorCode()+"|").getBytes());
				}else{
					response.getOutputStream().write(("ErrorMsg:" + e.getErrorMessage()).getBytes());
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return;
		} catch (IOException e) {
			e.printStackTrace();
			try {
				response.getOutputStream().write("ErrorMsg:Error!Try Again".getBytes());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return;
		} catch (Exception e) {
			e.printStackTrace();
			try {
				response.getOutputStream().write("ErrorMsg:Error!Try Again".getBytes());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return;
		}
	}

}
