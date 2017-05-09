package com.skilrock.sle.web.merchantUser.playMgmt.action;

import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;


import com.skilrock.sle.common.DateTimeFormat;
import com.skilrock.sle.common.SportsLotteryUtils;
import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.UtilityFunctions;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.common.javaBeans.UserInfoBean;
import com.skilrock.sle.commonMethod.serviceImpl.CommonMethodsServiceImpl;
import com.skilrock.sle.embedded.common.BaseActionTerminal;
import com.skilrock.sle.embedded.playMgmt.common.javaBeans.CancelTicketBean;
import com.skilrock.sle.gamePlayMgmt.controllerImpl.CancelTicketControllerImpl;
import com.skilrock.sle.merchant.lms.LMSIntegrationImpl;
import com.skilrock.sle.merchant.lms.common.javaBeans.LMSCancelTicketResponseBean;

public class SportsLotteryCancelTicketAction extends BaseActionTerminal{
	private static final long serialVersionUID = 1L;
    private String cancelType;
    public boolean autoCancel;
	public String getCancelType() {
		return cancelType;
	}

	public void setCancelType(String cancelType) {
		this.cancelType = cancelType;
	}

	public boolean isAutoCancel() {
		return autoCancel;
	}

	public void setAutoCancel(boolean autoCancel) {
		this.autoCancel = autoCancel;
	}

	public SportsLotteryCancelTicketAction() {
		super(SportsLotteryCancelTicketAction.class.getName());
	}

	public void cancelTicket() {
		logger.info("***** Inside cancelTicket Method");
		CancelTicketBean cancelTicketBean = null;
		UserInfoBean userInfoBean = null;
		CancelTicketControllerImpl cancelTicketControllerImpl = new CancelTicketControllerImpl();
		LMSCancelTicketResponseBean cancelTicketResponseBean = null;
		PrintWriter out=null;
		JSONObject responseJson=new JSONObject();
		try {
			userInfoBean = new UserInfoBean();
		    HttpSession session=request.getSession();
		    userInfoBean=(UserInfoBean)session.getAttribute("USER_INFO");
			out=response.getWriter();
			response.setContentType("application/json");

			CommonMethodsServiceImpl.getInstance().fetchAndCheckUserData(userInfoBean);
			//logger.debug("Merchant User Info Bean is "+userInfoBean);
			

			long lastTxnId = cancelTicketControllerImpl.checkForManualCancelWeb(userInfoBean);
			
			if(lastTxnId == 0) {
				throw new SLEException(SLEErrors.CANCELLED_TICKET_INITIATE_ERROR_CODE,SLEErrors.CANCELLED_TICKET_INITIATE_ERROR_MESSAGE);
			}
			cancelTicketBean = new CancelTicketBean();
			
			cancelTicketBean.setCancelType(cancelType);
			cancelTicketBean.setTxnIdToCancel(lastTxnId);
			cancelTicketBean.setInterfaceType("WEB");
			cancelTicketBean.setCancelDate(DateTimeFormat.getCurrentTimeStamp());
			cancelTicketBean.setIsAutoCancel(autoCancel?"Y":"N");
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
			

			cancelTicketResponseBean = LMSIntegrationImpl.cancelTicketAtLMS(cancelTicketBean, userInfoBean);

			if(cancelTicketResponseBean.getResponseCode() == 0) {
				cancelTicketControllerImpl.updateSportsLotteryCancelTicket(true, cancelTicketResponseBean.getMerTxId(), cancelTicketBean.getSaleTxnId(), cancelTicketBean.getCancelTxnId());   
				responseJson.put("isSuccess", true);
				responseJson.put("refundAmount",cancelTicketResponseBean.getTxAmount());
				responseJson.put("ticketNumber",cancelTicketBean.getTktToCancel()+cancelTicketBean.getReprintCount());
				responseJson.put("gameTypeName",cancelTicketBean.getGameTypeName());
				responseJson.put("Balance", UtilityFunctions.formatToTwoDecimal(cancelTicketResponseBean.getAvailBal()));
				responseJson.put("mode","Cancel");
				responseJson.put("cancelDateTime", cancelTicketBean.getCancelDate().toString());
				responseJson.put("orgName", Util.getPropertyValue("ORG_NAME"));
				responseJson.put("currSymbol",Util.getPropertyValue("CURRENCY_SYMBOL"));
			} else {
				cancelTicketControllerImpl.updateSportsLotteryCancelTicket(false, "-1", cancelTicketBean.getSaleTxnId(), cancelTicketBean.getCancelTxnId());
				responseJson.put("isSuccess", false);
				responseJson.put("errorMsg", "Error! Cancel Failed");
				if(cancelTicketResponseBean.getResponseCode() == 118){
					throw new SLEException(SLEErrors.INVALID_SESSION_ERROR_CODE, SLEErrors.INVALID_SESSION_ERROR_MESSAGE);
				}else if(cancelTicketResponseBean.getResponseCode() ==2016){
					throw new SLEException(SLEErrors.RG_LIMIT_EXCEPTION_ERROR_CODE,SLEErrors.CANCEL_LIMIT_TERMINAL_EXCEPTION_ERROR_MESSAGE);
				}
			}
			logger.debug("***** Web Game Refund Response "+responseJson);
		}
		catch (SLEException e) {
			try {
				responseJson.put("isSuccess", false);
				if(e.getErrorCode() == 10012){
					responseJson.put("errorMsg",e.getErrorMessage());
					responseJson.put("errorCode",01);
				} else{
					responseJson.put("errorMsg",e.getErrorMessage());
					responseJson.put("errorCode",e.getErrorCode());
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				responseJson.put("isSuccess", false);
				responseJson.put("errorMsg","Error!Try Again");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		out.print(responseJson);
		out.flush();
		out.close();
	}
}
