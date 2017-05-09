package com.skilrock.sle.embedded.pwtMgmt.action;

import java.io.IOException;

import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.common.javaBeans.UserInfoBean;
import com.skilrock.sle.commonMethod.serviceImpl.CommonMethodsServiceImpl;
import com.skilrock.sle.embedded.common.BaseActionTerminal;
import com.skilrock.sle.embedded.common.SportsLotteryResponseData;
import com.skilrock.sle.merchant.common.javaBeans.MerchantInfoBean;
import com.skilrock.sle.merchant.common.javaBeans.TPPwtResponseBean;
import com.skilrock.sle.pwtMgmt.controllerImpl.PayPrizeTicketControllerImpl;
import com.skilrock.sle.pwtMgmt.controllerImpl.PrizeTicketVerificationControllerImpl;
import com.skilrock.sle.pwtMgmt.javaBeans.PwtVerifyTicketBean;
import com.skilrock.sle.pwtMgmt.javaBeans.PwtVerifyTicketDrawDataBean;

public class RetailerPwtTicketAction extends BaseActionTerminal {
	private static final long serialVersionUID = 1L;

	public RetailerPwtTicketAction() {
		super(RetailerPwtTicketAction.class.getName());
	}

	private String userName;
	private String ticketNumber;
	private String saleMerCode;
	private String verificationCode;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getTicketNumber() {
		return ticketNumber;
	}

	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}
	public String getSaleMerCode() {
		return saleMerCode;
	}

	public void setSaleMerCode(String saleMerCode) {
		this.saleMerCode = saleMerCode;
	}
	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public void verifyTicket() {
		String responseString = null;
		UserInfoBean userBean = null;
		PwtVerifyTicketBean verifyTicketBean = null;
		String statusMsg = null;
		try {
			MerchantInfoBean merchantInfoBean = Util.merchantInfoMap.get(getMerCode());

			userBean = new UserInfoBean();
			userBean.setUserName(userName);
			userBean.setMerchantId(merchantInfoBean.getMerchantId());
			userBean.setUserSessionId(getSessId());
			userBean.setMerchantDevName(getMerCode());

			CommonMethodsServiceImpl.getInstance().fetchAndCheckUserData(userBean);
			CommonMethodsServiceImpl.getInstance().checkAndAutoCancel(getSlLstTxnId(), "CANCEL_MISMATCH", userBean);

			PrizeTicketVerificationControllerImpl controllerImpl = new PrizeTicketVerificationControllerImpl();
			verifyTicketBean = controllerImpl.prizeWinningVerifyTicket(userBean, getMerCode(), ticketNumber);
			
			MerchantInfoBean saleMerchantInfoBean = CommonMethodsServiceImpl.getInstance().fetchMerchantDetailFromTicket(ticketNumber);

			boolean claimStatus = true;
			for(PwtVerifyTicketDrawDataBean drawDataBean : verifyTicketBean.getVerifyTicketDrawDataBeanArray()) {
				if("CANCELLED".equals(drawDataBean.getStatus())){
					throw new SLEException(SLEErrors.INVALID_TICKET_ERROR_CODE,SLEErrors.INVALID_TICKET_ERROR_MESSAGE);
				}
				if(!"CLAIM ALLOW".equals(drawDataBean.getDrawStatus())) {
					claimStatus = false;
					String message = null;
					if("VERIFICATION_PENDING".equals(drawDataBean.getDrawStatus())) {
						message = "Verification Pending";
					} else if("CLAIM_PENDING".equals(drawDataBean.getDrawStatus())) {
						message = "Claim Pending";
					} else if("DRAW_EXPIRED".equals(drawDataBean.getDrawStatus())) {
						message = "Draw Expired";
					} else if("DRAW CANCELLED".equals(drawDataBean.getDrawStatus())) {
						message = "Draw Cancelled";
					} else if("RESULT AWAITED".equals(drawDataBean.getDrawStatus())) {
						message = "Result Awaited";
					}
					drawDataBean.setMessage(message);
					break;
				}

				if("CLAIMED".equals(drawDataBean.getStatus())) {
					claimStatus = false;
					drawDataBean.setMessage("Already Claimed");
					break;
				}
				if(drawDataBean.getDrawWinAmt()<=0.0) {
					claimStatus = false;
					drawDataBean.setMessage("Better Luck Next Time!");
					break;
				}
			}

			if(claimStatus) {
				statusMsg = controllerImpl.checkRetailerClaimStatus(userName, userBean.getUserSessionId(), verifyTicketBean.getTotalWinAmt());
				if(!"NORMAL_PAY".equalsIgnoreCase(statusMsg)){
					claimStatus = false;
				}

				if("ORG_LMT_EXCEED".equalsIgnoreCase(statusMsg)){
					statusMsg = "Pay Limit Exceeded";
				}else if("WINNING_EXCEED_HIGH_PRIZE".equalsIgnoreCase(statusMsg)){
					statusMsg = " High Prize Ticket";
				}
			}			

			responseString = SportsLotteryResponseData.generateSLEVerifyTicketResponseData(verifyTicketBean, claimStatus, statusMsg);
			StringBuilder res= new StringBuilder(responseString);
			res.append("verificationCode:").append("Asoft".equalsIgnoreCase(saleMerchantInfoBean.getMerchantDevName())?"Y":"N").append("|");
			response.getOutputStream().write(res.toString().getBytes());
		} catch (SLEException e) {
			try {
				if(e.getErrorCode() == 10012){
					response.getOutputStream().write(("ErrorMsg:" + e.getErrorMessage()+"|ErrorCode:01|").getBytes());
				}else{
					response.getOutputStream().write(("ErrorMsg:"+e.getErrorMessage()).getBytes());				
				}
				
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return;
		} catch (Exception e) {
			e.printStackTrace();
			try {
				response.getOutputStream().write(("ErrorMsg:Error!PWT Failed").getBytes());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return;
		}
	}

	public void payPwtTicket() {
		String responseString = null;
		UserInfoBean userBean = null;
		PwtVerifyTicketBean verifyTicketBean = null;
		TPPwtResponseBean pwtResponseBean = null;
		double balance = 0.0;
		try {
			MerchantInfoBean merchantInfoBean = Util.merchantInfoMap.get(getMerCode());

			userBean = new UserInfoBean();
			userBean.setUserName(userName);
			userBean.setMerchantId(merchantInfoBean.getMerchantId());
			userBean.setUserSessionId(getSessId());
			userBean.setMerchantDevName(getMerCode());

			CommonMethodsServiceImpl.getInstance().fetchAndCheckUserData(userBean);
			CommonMethodsServiceImpl.getInstance().checkAndAutoCancel(getSlLstTxnId(), "CANCEL_MISMATCH", userBean);

			verifyTicketBean = new PrizeTicketVerificationControllerImpl().prizeWinningVerifyTicket(userBean, getMerCode(), ticketNumber);
			MerchantInfoBean saleMerchantInfoBean = CommonMethodsServiceImpl.getInstance().fetchMerchantDetailFromTicket(ticketNumber);
			setSaleMerCode(saleMerchantInfoBean.getMerchantDevName());
			boolean claimStatus = true;
			for(PwtVerifyTicketDrawDataBean drawDataBean : verifyTicketBean.getVerifyTicketDrawDataBeanArray()) {
				if("CLAIMED".equals(drawDataBean.getStatus())) {
					claimStatus = false;
					drawDataBean.setMessage("Already Claimed");
					break;
				}
			}

			if(claimStatus) {
				pwtResponseBean = new PayPrizeTicketControllerImpl().normalPayWinning(getMerCode(), ticketNumber, "TERMINAL", userBean,saleMerCode,verificationCode);
				balance = pwtResponseBean.getBalance();
			}
			verifyTicketBean.setAdvMessageMap(pwtResponseBean.getAdvMsg());
			responseString = SportsLotteryResponseData.generateSLEWinningTicketResponseData(verifyTicketBean, balance,pwtResponseBean.getGovtTaxPwt());
			response.getOutputStream().write(responseString.getBytes());
		} catch (SLEException e) {
			try {
				
				if(e.getErrorCode() == 10012){
					response.getOutputStream().write(("ErrorMsg:" + e.getErrorMessage()+"|ErrorCode:01|").getBytes());
				}else{
					response.getOutputStream().write(("ErrorMsg:Error!PWT Failed "+e.getErrorMessage()).getBytes());
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return;
		} catch (Exception e) {
			e.printStackTrace();
			try {
				response.getOutputStream().write(("ErrorMsg:Error!PWT Failed").getBytes());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return;
		}
	}
}