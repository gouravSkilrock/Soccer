package com.skilrock.sle.web.merchantUser.pwtMgmt.Action;

import java.text.DecimalFormat;

import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.common.javaBeans.UserInfoBean;
import com.skilrock.sle.commonMethod.serviceImpl.CommonMethodsServiceImpl;
import com.skilrock.sle.embedded.common.BaseActionWeb;
import com.skilrock.sle.merchant.common.javaBeans.TPPwtResponseBean;
import com.skilrock.sle.merchant.common.javaBeans.MerchantInfoBean;
import com.skilrock.sle.pwtMgmt.controllerImpl.PayPrizeTicketControllerImpl;
import com.skilrock.sle.pwtMgmt.controllerImpl.PrizeTicketVerificationControllerImpl;
import com.skilrock.sle.pwtMgmt.javaBeans.PlayerBean;
import com.skilrock.sle.pwtMgmt.javaBeans.PwtVerifyTicketBean;
import com.skilrock.sle.pwtMgmt.javaBeans.PwtVerifyTicketDrawDataBean;

public class BOPwtTicketAction extends BaseActionWeb {
	private static final long serialVersionUID = 1L;

	private String ticketNumber;
	private double winningAmount;
	private String pwtStatus;
	private PwtVerifyTicketBean verifyTicketBean;
	private String netWinAmount;
	private String taxOnPwt;
	private String winAmt;

	/*	Information for Player Registration Start	*/
	private String countryData;
	private String firstName;
	private String lastName;
	private String emailId;
	private String phoneNumber;
	private String address1;
	private String address2;
	private String city;
	private String state;
	private String country;
	private String idType;
	private String idNumber;
	private String remarks;
	private String requestId;
	/*	Information for Player Registration End	*/

	private String saleMerCode;
	private String verificationCode;

	public BOPwtTicketAction() {
		super(BOPwtTicketAction.class.getName());
	}

	public String getTicketNumber() {
		return ticketNumber;
	}

	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}

	public double getWinningAmount() {
		return winningAmount;
	}

	public void setWinningAmount(double winningAmount) {
		this.winningAmount = winningAmount;
	}

	public String getNetWinAmount() {
		return netWinAmount;
	}

	public void setNetWinAmount(String netWinAmount) {
		this.netWinAmount = netWinAmount;
	}

	public String getTaxOnPwt() {
		return taxOnPwt;
	}

	public void setTaxOnPwt(String taxOnPwt) {
		this.taxOnPwt = taxOnPwt;
	}

	public String getWinAmt() {
		return winAmt;
	}

	public void setWinAmt(String winAmt) {
		this.winAmt = winAmt;
	}

	public String getPwtStatus() {
		return pwtStatus;
	}

	public void setPwtStatus(String pwtStatus) {
		this.pwtStatus = pwtStatus;
	}

	public PwtVerifyTicketBean getVerifyTicketBean() {
		return verifyTicketBean;
	}

	public void setVerifyTicketBean(PwtVerifyTicketBean verifyTicketBean) {
		this.verifyTicketBean = verifyTicketBean;
	}

	public String getCountryData() {
		return countryData;
	}

	public void setCountryData(String countryData) {
		this.countryData = countryData;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public String getRemarks() {
		return remarks.replaceAll("+", " ");
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks.replaceAll(" ", "+");
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
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

	public String verifyTicket() throws Exception {
		HttpSession session = null;
		PrizeTicketVerificationControllerImpl controllerImpl = new PrizeTicketVerificationControllerImpl();
		MerchantInfoBean merchantInfoBean=null;
		String merCode = null;
		try {
			merchantInfoBean = CommonMethodsServiceImpl.getInstance().fetchMerchantDetailFromTicket(ticketNumber);
			if(merchantInfoBean != null && ("OKPOS".equalsIgnoreCase(merchantInfoBean.getMerchantDevName()) || "Asoft".equalsIgnoreCase(merchantInfoBean.getMerchantDevName()))){
				merCode = merchantInfoBean.getMerchantDevName();
			}else{
			session = request.getSession();
				merCode = session.getAttribute("MER_CODE").toString();
			}

			verifyTicketBean = controllerImpl.prizeWinningVerifyTicket(null,merCode, ticketNumber);
			verifyTicketBean.setMerchantName(merCode);

			for(PwtVerifyTicketDrawDataBean drawDataBean : verifyTicketBean.getVerifyTicketDrawDataBeanArray()) {
				if("VERIFICATION_PENDING".equals(drawDataBean.getDrawStatus())) {
					drawDataBean.setDrawStatus("Verification Pending");
					//drawDataBean.setStatus("Unclaimed");
					if(drawDataBean.getStatus()==null || drawDataBean.getStatus().length()==0){
						drawDataBean.setStatus("Unclaimed");
					}
				} else if("CLAIM_PENDING".equals(drawDataBean.getDrawStatus())) {
					drawDataBean.setDrawStatus("Claim Pending");
					drawDataBean.setStatus("Unclaimed");
				} else if("DRAW_EXPIRED".equals(drawDataBean.getDrawStatus())) {
					drawDataBean.setDrawStatus("Draw Expired");
					if(drawDataBean.getStatus()==null || drawDataBean.getStatus().length()==0){
						drawDataBean.setStatus("Non Winning");
					}
					//drawDataBean.setStatus("Unclaimed");
				} else if("DRAW CANCELLED".equals(drawDataBean.getDrawStatus())) {
					drawDataBean.setDrawStatus("Draw Cancelled");
					drawDataBean.setStatus("Unclaimed");
				} else if("RESULT AWAITED".equals(drawDataBean.getDrawStatus())) {
					drawDataBean.setDrawStatus("Result Awaited");
					drawDataBean.setStatus("Unclaimed");
				}else if("CLAIM ALLOW".equals(drawDataBean.getDrawStatus())) {
					if(drawDataBean.getStatus()==null || drawDataBean.getStatus().length()==0){
						drawDataBean.setStatus("Non Winning");
					}
				}
			}
		} catch (SLEException se) {
			logger.error("ErrorCode:" + se.getErrorCode() + " ErrorMessage:"+ se.getErrorMessage());
			request.setAttribute("SLE_EXCEPTION", se.getErrorMessage());
			return "applicationAjaxException";
		} catch (Exception ex) {
			logger.error("ErrorCode:"+SLEErrors.GENERAL_EXCEPTION_ERROR_CODE+" ErrorMessage:"+SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			request.setAttribute("SLE_EXCEPTION", SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			return "applicationAjaxException";
		}

		return SUCCESS;
	}

	//	http://localhost:8081/SportsLottery/com/skilrock/sle/web/merchantUser/pwtMgmt/Action/payPwt.action?winningAmount=1650&userName=bomaster&userSession=&serviceCode=SLE
	public String payPwt() throws Exception {
		HttpSession session = null;
		UserInfoBean userBean = null;
		TPPwtResponseBean pwtResponseBean=null;
		 DecimalFormat df = new DecimalFormat("###0.00");
		PayPrizeTicketControllerImpl controllerImpl = new PayPrizeTicketControllerImpl();
		try {
			session = request.getSession();
			String merCode = session.getAttribute("MER_CODE").toString();
			userBean = (UserInfoBean) session.getAttribute("USER_INFO");

			pwtStatus = controllerImpl.checkTicketPWTStatus(userBean.getUserName(), userBean.getUserSessionId(), winningAmount);

			if("NORMAL_PAY".equals(pwtStatus)) {
				pwtResponseBean = controllerImpl.normalPayWinning(merCode, ticketNumber, "WEB", userBean,saleMerCode,verificationCode);
				winAmt=df.format(winningAmount)+" "+Util.getPropertyValue("CURRENCY_SYMBOL");
				taxOnPwt=df.format(winningAmount*pwtResponseBean.getGovtTaxPwt()*.01)+" "+Util.getPropertyValue("CURRENCY_SYMBOL");;
				netWinAmount=df.format(winningAmount-(winningAmount*pwtResponseBean.getGovtTaxPwt()*.01))+" "+Util.getPropertyValue("CURRENCY_SYMBOL");;
			} else if("HIGH_PRIZE".equals(pwtStatus) || "MAS_APPROVAL".equals(pwtStatus)) {
				countryData = new Gson().toJson(CommonMethodsServiceImpl.getInstance().getCountryListMap());
				return "REGISTRATION";
			}
		} catch (SLEException se) {
			logger.info("ErrorCode:" + se.getErrorCode() + " ErrorMessage:"+ se.getErrorMessage());
			request.setAttribute("SLE_EXCEPTION", se.getErrorMessage());
			return "applicationException";
		} catch (Exception ex) {
			logger.info("ErrorCode:"+SLEErrors.GENERAL_EXCEPTION_ERROR_CODE+" ErrorMessage:"+SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			request.setAttribute("SLE_EXCEPTION", SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			return "applicationAjaxException";
		}

		return SUCCESS;
	}

	//	http://localhost:8081/SportsLottery/com/skilrock/sle/web/merchantUser/pwtMgmt/Action/boHighPrizeORMasApproval.action?ticketNumber=655362322199760010&pwtStatus=HIGH_PRIZE&firstName=Shobhit&lastName=Bhardwaj&emailId=aaa%40bbb.com&phoneNumber=9876543210&address1=sdsdasd&address2=fdggdfgdfgd&city=Gurgaon&state=Haryana&country=India&idType=DrivingLicence&idNumber=DLC12345&remarks=ddfsdsadada
	//	http://localhost:8081/SportsLottery/com/skilrock/sle/web/merchantUser/pwtMgmt/Action/boHighPrizeORMasApproval.action?ticketNumber=655362322199760010&pwtStatus=MAS_APPROVAL&firstName=Shobhit&lastName=Bhardwaj&emailId=aaa%40bbb.com&phoneNumber=9876543210&address1=sdsdasd&address2=fdggdfgdfgd&city=Gurgaon&state=Haryana&country=India&idType=DrivingLicence&idNumber=DLC12345&remarks=ddfsdsadada
	public String payHighPrizeORMasApproval() throws Exception {
		HttpSession session = null;
		UserInfoBean userBean = null;
		PlayerBean playerBean = null;
		PayPrizeTicketControllerImpl controllerImpl = new PayPrizeTicketControllerImpl();
		try {
			session = request.getSession();
			String merCode = session.getAttribute("MER_CODE").toString();
			userBean = (UserInfoBean) session.getAttribute("USER_INFO");

			playerBean = new PlayerBean();
			playerBean.setFirstName(firstName.replaceAll(" ", "+"));
			playerBean.setLastName(lastName.replaceAll(" ", "+"));
			playerBean.setEmailId(emailId.replaceAll(" ", "+"));
			playerBean.setPhoneNumber(phoneNumber.replaceAll(" ", "+"));
			playerBean.setAddress1(address1.replaceAll(" ", "+"));
			if(address2.length()>0) {
				playerBean.setAddress2(address2.replaceAll(" ", "+"));
			}
			playerBean.setCity(city.replaceAll(" ", "+"));
			playerBean.setState(state.replaceAll(" ", "+"));
			playerBean.setCountry(country.replaceAll(" ", "+"));
			playerBean.setIdType(idType.replaceAll(" ", "+"));
			playerBean.setIdNumber(idNumber.replaceAll(" ", "+"));

			requestId = controllerImpl.highPrizeOrMasApprovalWinning(pwtStatus, merCode, ticketNumber, "WEB", userBean, playerBean, remarks,saleMerCode,verificationCode);
		} catch (SLEException se) {
			logger.error("ErrorCode:" + se.getErrorCode() + " ErrorMessage:"+ se.getErrorMessage());
			request.setAttribute("SLE_EXCEPTION", se.getErrorMessage());
			return "applicationException";
		} catch (Exception ex) {
			logger.error("ErrorCode:"+SLEErrors.GENERAL_EXCEPTION_ERROR_CODE+" ErrorMessage:"+SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			request.setAttribute("SLE_EXCEPTION", SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			return "applicationException";
		}

		return SUCCESS;
	}
}