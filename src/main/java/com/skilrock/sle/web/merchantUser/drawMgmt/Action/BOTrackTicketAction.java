package com.skilrock.sle.web.merchantUser.drawMgmt.Action;

import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.commonMethod.serviceImpl.CommonMethodsServiceImpl;
import com.skilrock.sle.drawMgmt.controllerImpl.TrackTicketControllerImpl;
import com.skilrock.sle.drawMgmt.javaBeans.TrackSLETicketBean;
import com.skilrock.sle.embedded.common.BaseActionWeb;
import com.skilrock.sle.merchant.common.javaBeans.MerchantInfoBean;

public class BOTrackTicketAction extends BaseActionWeb  {
	private static final long serialVersionUID = 1L;

	public BOTrackTicketAction() {
		super(BOTrackTicketAction.class.getName());
	}

	private String ticketNumber;
	private TrackSLETicketBean trackTicketBean;
	private String countryDeployed;

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

	public String getCountryDeployed() {
		return countryDeployed;
	}

	public void setCountryDeployed(String countryDeployed) {
		this.countryDeployed = countryDeployed;
	}

	public String trackTicket() {
		TrackTicketControllerImpl controllerImpl = null;
		HttpSession session = null;
		MerchantInfoBean merchantInfoBean=null;
		String merCode=null;
		try {
			merchantInfoBean=CommonMethodsServiceImpl.getInstance().fetchMerchantDetailFromTicket(ticketNumber);
			if(merchantInfoBean!=null && ("OKPOS".equalsIgnoreCase(merchantInfoBean.getMerchantDevName()) || "Asoft".equalsIgnoreCase(merchantInfoBean.getMerchantDevName()))){
				merCode=merchantInfoBean.getMerchantDevName();
			}else{
				session = request.getSession();
				merCode = session.getAttribute("MER_CODE").toString();
					
			}
			controllerImpl = TrackTicketControllerImpl.getInstance();
			
			//ticketNumber = "64776233150486030";
			trackTicketBean = controllerImpl.trackSLETicket(merCode,ticketNumber);
			countryDeployed=Util.getPropertyValue("COUNTRY_DEPLOYED");
			trackTicketBean.setMerchantName(Util.merchantInfoMap.get(merCode).getMerchantName());
			logger.info(new Gson().toJson(trackTicketBean));
		} catch (SLEException se) {
			logger.info("ErrorCode - " + se.getErrorCode() + " | ErrorMessage - " + se.getErrorMessage());
			request.setAttribute("SLE_EXCEPTION", se.getErrorMessage());
			return "applicationAjaxException";
		}

		return SUCCESS;
	}
}