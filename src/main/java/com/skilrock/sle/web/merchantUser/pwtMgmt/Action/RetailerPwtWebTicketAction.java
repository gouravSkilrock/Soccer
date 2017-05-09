package com.skilrock.sle.web.merchantUser.pwtMgmt.Action;

import java.io.PrintWriter;
import net.sf.json.JSONObject;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.common.javaBeans.UserInfoBean;
import com.skilrock.sle.commonMethod.serviceImpl.CommonMethodsServiceImpl;
import com.skilrock.sle.merchant.common.javaBeans.MerchantInfoBean;
import com.skilrock.sle.merchant.common.javaBeans.TPPwtResponseBean;
import com.skilrock.sle.mobile.common.BaseActionMobile;
import com.skilrock.sle.pwtMgmt.controllerImpl.PayPrizeTicketControllerImpl;
import com.skilrock.sle.pwtMgmt.controllerImpl.PrizeTicketVerificationControllerImpl;
import com.skilrock.sle.pwtMgmt.javaBeans.PwtVerifyTicketBean;
import com.skilrock.sle.pwtMgmt.javaBeans.PwtVerifyTicketDrawDataBean;
import com.skilrock.sle.web.merchantUser.common.SportsLotteryWebResponseData;

public class RetailerPwtWebTicketAction extends BaseActionMobile {
	private static final long serialVersionUID = 1L;

	public RetailerPwtWebTicketAction() {
		super(RetailerPwtWebTicketAction.class.getName());
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
		JSONObject jsonObject = null;
		PrintWriter out = null;
		String merchantCode = null;
		UserInfoBean userBean = null;
		PwtVerifyTicketBean verifyTicketBean = null;
		String statusMsg = null;
		MerchantInfoBean merchantInfoBean=null;
		try {
			jsonObject = new JSONObject();

			JsonObject sportsLotteryPlayData = new JsonParser().parse(getRequestData()).getAsJsonObject();
	
			out = response.getWriter();
			response.setContentType("application/json");
			
			merchantCode = sportsLotteryPlayData.get("merchantCode").getAsString();
			
			userBean = new UserInfoBean();
			userBean.setUserName(sportsLotteryPlayData.get("userName").getAsString());
			merchantInfoBean = Util.merchantInfoMap.get(merchantCode);
			userBean.setMerchantId(merchantInfoBean.getMerchantId());
			userBean.setUserSessionId(sportsLotteryPlayData.get("sessionId").getAsString());
			userBean.setMerchantDevName(merchantCode);
			setTicketNumber(sportsLotteryPlayData.get("ticketNumber").getAsString());


			CommonMethodsServiceImpl.getInstance().fetchAndCheckUserData(userBean);
			
			PrizeTicketVerificationControllerImpl controllerImpl = new PrizeTicketVerificationControllerImpl();
			verifyTicketBean = controllerImpl.prizeWinningVerifyTicket(userBean, merchantCode, getTicketNumber());
			
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
					drawDataBean.setMessage("Better Luck Next Time !");
					break;
				}
			}

			if(claimStatus) {
					statusMsg = controllerImpl.checkRetailerClaimStatus(sportsLotteryPlayData.get("userName").getAsString(), userBean.getUserSessionId(), verifyTicketBean.getTotalWinAmt());
					if(!"NORMAL_PAY".equalsIgnoreCase(statusMsg)){
						claimStatus = false;
					}

					if("ORG_LMT_EXCEED".equalsIgnoreCase(statusMsg)){
						statusMsg = "Pay Limit Exceeded";
					}else if("WINNING_EXCEED_HIGH_PRIZE".equalsIgnoreCase(statusMsg)){
						statusMsg = " High Prize Ticket";
					}				
			}			

			jsonObject = SportsLotteryWebResponseData.generateSLEVerifyTicketResponseData(verifyTicketBean, claimStatus, statusMsg);
			jsonObject.put("saleMerCode",saleMerchantInfoBean.getMerchantDevName());
			
		} catch (SLEException pe) {
			pe.printStackTrace();

			if(pe.getErrorCode()==10012){
				jsonObject.put("responseCode", SLEErrors.INVALID_SESSION_MOBILE_ERROR_CODE);
				jsonObject.put("responseMsg",SLEErrors.INVALID_SESSION_MOBILE_ERROR_MESSAGE);
			}else{
				jsonObject.put("responseCode", pe.getErrorCode());
				jsonObject.put("responseMsg", pe.getErrorMessage());
			}
		} catch (Exception e) {
			e.printStackTrace();
			
			jsonObject.put("responseCode", SLEErrors.GENERAL_EXCEPTION_ERROR_CODE);
			jsonObject.put("responseMsg", SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			if (jsonObject.isEmpty()) {
				jsonObject.put("responseCode", SLEErrors.GENERAL_EXCEPTION_ERROR_CODE);
				jsonObject.put("responseMsg", SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			}
			
			out.print(jsonObject);
			out.flush();
			out.close();
		}
	}
	

	public void payPwtTicket() {
		
		JSONObject jsonObject = null;
		PrintWriter out = null;
		UserInfoBean userBean = null;
		PwtVerifyTicketBean verifyTicketBean = null;
		TPPwtResponseBean pwtResponseBean = null;
		double balance = 0.0;
		String merCode=null;
		try {
			
			jsonObject = new JSONObject();
			out = response.getWriter();
			response.setContentType("application/json");

			System.out.println("str"+getRequestData());
			JsonObject sportsLotteryPlayData = new JsonParser().parse(getRequestData()).getAsJsonObject();
			merCode=sportsLotteryPlayData.get("merchantCode").getAsString();
			
			MerchantInfoBean merchantInfoBean = Util.merchantInfoMap.get(merCode);

			userBean = new UserInfoBean();
			userBean.setUserName(sportsLotteryPlayData.get("userName").getAsString());
			userBean.setMerchantId(merchantInfoBean.getMerchantId());
			userBean.setUserSessionId(sportsLotteryPlayData.get("sessionId").getAsString());
			userBean.setMerchantDevName(merCode);
			setTicketNumber(sportsLotteryPlayData.get("ticketNumber").getAsString());
			setVerificationCode(sportsLotteryPlayData.get("verificationCode").getAsString());
			setSaleMerCode(sportsLotteryPlayData.get("saleMerCode").getAsString());

			CommonMethodsServiceImpl.getInstance().fetchAndCheckUserData(userBean);

			verifyTicketBean = new PrizeTicketVerificationControllerImpl().prizeWinningVerifyTicket(userBean,merCode, ticketNumber);
			boolean claimStatus = true;
			for(PwtVerifyTicketDrawDataBean drawDataBean : verifyTicketBean.getVerifyTicketDrawDataBeanArray()) {

			if("CLAIMED".equals(drawDataBean.getStatus())) {
					claimStatus = false;
					drawDataBean.setMessage("Already Claimed");
					break;
				}
			}

			if(claimStatus) {
				pwtResponseBean = new PayPrizeTicketControllerImpl().normalPayWinning(merCode, ticketNumber, "WEB", userBean,saleMerCode,verificationCode);
				balance = pwtResponseBean.getBalance();
			}
			jsonObject = SportsLotteryWebResponseData.generateSLEWinningTicketResponseData(verifyTicketBean, balance,userBean.getOrgName());
		}catch (SLEException pe) {
			pe.printStackTrace();
			if(pe.getErrorCode()==10012){
				jsonObject.put("responseCode", SLEErrors.INVALID_SESSION_MOBILE_ERROR_CODE);
				jsonObject.put("responseMsg",SLEErrors.INVALID_SESSION_MOBILE_ERROR_MESSAGE);
			}else{
				jsonObject.put("responseCode", pe.getErrorCode());
				jsonObject.put("responseMsg", pe.getErrorMessage());
			}
			if (jsonObject.isEmpty()) {
				jsonObject.put("responseCode", SLEErrors.GENERAL_EXCEPTION_ERROR_CODE);
				jsonObject.put("responseMsg", SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			}
			out.print(jsonObject);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			
			jsonObject.put("responseCode", SLEErrors.GENERAL_EXCEPTION_ERROR_CODE);
			jsonObject.put("responseMsg", SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			if (jsonObject.isEmpty()) {
				jsonObject.put("responseCode", SLEErrors.GENERAL_EXCEPTION_ERROR_CODE);
				jsonObject.put("responseMsg", SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			}
			
			out.print(jsonObject);
			out.flush();
			out.close();
		}
	}
	}
