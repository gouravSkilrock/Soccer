package com.skilrock.sle.web.merchantUser.miscMgmt.Action;

import java.text.DecimalFormat;

import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.common.javaBeans.UserInfoBean;
import com.skilrock.sle.embedded.common.BaseActionWeb;
import com.skilrock.sle.miscMgmt.controllerImpl.UpdateJackpotControllerImpl;
import com.skilrock.sle.miscMgmt.javabeans.jackpotBean;

public class UpdateJackpotAction extends BaseActionWeb {
	private static final long serialVersionUID = 1L;
	private double amount;
	private String messageAmount;
	private String status;
	private double carryForward;
	private double saleAmt;
	private double jackpotAmt;

	public double getJackpotAmt() {
		return jackpotAmt;
	}

	public void setJackpotAmt(double jackpotAmt) {
		this.jackpotAmt = jackpotAmt;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public double getCarryForward() {
		return carryForward;
	}

	public void setCarryForward(double carryForward) {
		this.carryForward = carryForward;
	}

	public double getSaleAmt() {
		return saleAmt;
	}

	public void setSaleAmt(double saleAmt) {
		this.saleAmt = saleAmt;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getMessageAmount() {
		return messageAmount;
	}

	public void setMessageAmount(String messageAmount) {
		this.messageAmount = messageAmount;
	}

public String jackpotMenu() throws SLEException{
	jackpotBean bean=new jackpotBean();
	double amt=0.0;
	
	try{
		bean=UpdateJackpotControllerImpl.getInstance().jackpotAmount();
		status=bean.getStatus();
		saleAmt=bean.getSaleAmt();
		carryForward=bean.getCarryForward();
		amt=saleAmt*0.1575;
		DecimalFormat df = new DecimalFormat("###0.00");
		String balString = df.format(amt);
		jackpotAmt= Double.parseDouble(balString);
	}catch (Exception e) {
		e.printStackTrace();
		logger.info("ErrorCode:" + SLEErrors.GENERAL_EXCEPTION_ERROR_CODE
				+ " ErrorMessage:"
				+ SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		request.setAttribute("SLE_EXCEPTION",
				SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		return ERROR;
	}
	return SUCCESS;
}
	public String updateJackpotDetails() throws SLEException {
		UserInfoBean userBean = null;
		try {
			userBean = (UserInfoBean) request.getSession().getAttribute("USER_INFO");
			UpdateJackpotControllerImpl.getInstance().updateData(amount,messageAmount, userBean.getSleUserId());
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("ErrorCode:" + SLEErrors.GENERAL_EXCEPTION_ERROR_CODE
					+ " ErrorMessage:"
					+ SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			request.setAttribute("SLE_EXCEPTION",
					SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			return ERROR;
		}
		return SUCCESS;
	}
}
