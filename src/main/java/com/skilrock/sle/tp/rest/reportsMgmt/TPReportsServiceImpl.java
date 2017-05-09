package com.skilrock.sle.tp.rest.reportsMgmt;


import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import net.sf.json.JSONObject;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.skilrock.sle.common.SLELogger;
import com.skilrock.sle.common.TransactionManager;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.common.javaBeans.ReportBean;
import com.skilrock.sle.common.javaBeans.TicketInfoBean;
import com.skilrock.sle.common.javaBeans.TicketSalePwtInfoBean;
import com.skilrock.sle.common.javaBeans.UserInfoBean;

import com.skilrock.sle.mobile.reportsMgmt.controllerImpl.SportsLotteryMobileReportsControllerImpl;

@Path("/reportsMgmt")
public class TPReportsServiceImpl {

	private static final SLELogger logger = SLELogger.getLogger(TPReportsServiceImpl.class.getName());

	@Path("/getPurchaseTicketResponse")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response fetchPurchaseTicketReport(String requestData) {
		logger.info("--- fetchPurchaseTickets ---");
		SportsLotteryMobileReportsControllerImpl mobileReportsControllerImpl = null;
		Map<Long, TicketInfoBean> tktInfoMap = null;
		UserInfoBean userInfoBean = null;
		ReportBean reportBean = null;
		JSONObject js=new JSONObject();
		try {
			JsonParser parser = new JsonParser();
			JsonObject reqData = (JsonObject) parser.parse(requestData);
			logger.info("Sports Lottery fetchPurchaseTicketReport Request Data: "+requestData);
			
			int merchantId = TransactionManager.getMerchantId();
			
			userInfoBean = new UserInfoBean();
			userInfoBean.setMerchantUserId(reqData.get("playerId").getAsInt());
			userInfoBean.setMerchantId(merchantId);
			
			mobileReportsControllerImpl = new SportsLotteryMobileReportsControllerImpl();
			reportBean = new ReportBean();
			reportBean.setReportChannel("WEB");
			reportBean.setStartDate(reqData.get("startDate").getAsString());
			reportBean.setEndDate(reqData.get("endDate").getAsString());
			tktInfoMap = mobileReportsControllerImpl.fetchPurchaseTicketReport(userInfoBean, reportBean);
			reportBean.setTktInfoMap(tktInfoMap);
			js.put("responseCode", 0);
			js.put("ticketInfo", reportBean);			
		} catch (SLEException pe) {
			pe.printStackTrace();
			js.put("responseCode", pe.getErrorCode());
			js.put("responseMessage", pe.getErrorMessage());
			js.put("isSuccess", false);
		} catch (Exception e) {
			e.printStackTrace();
			js.put("responseCode", SLEErrors.GENERAL_EXCEPTION_ERROR_CODE);
			js.put("responseMessage", SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			js.put("isSuccess", false);
		} finally {
			if (js.isEmpty()) {
				js.put("responseCode", SLEErrors.GENERAL_EXCEPTION_ERROR_CODE);
				js.put("responseMessage", SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
				js.put("isSuccess", false);
			}
			logger.debug("***** purchase Ticket Report Response "+js);
		}
		return Response.ok(js).build();
	}
	
	/**
	 * For fetching small winning report for Ghana.
	 * @param- gameId,gameTypeId ,startDate, endDate, reportType, retailers list
	 * @return- TicketSalePwtInfoBean List.
	 * @author Mayank.
	 */
	@Path("/getDetailedWinningPaymentReport")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDetailedWinningPlayerReport(String requestData){
		logger.info("--- fetchDetailedWinningPaymentReport ---");
		JSONObject js = new JSONObject();
		List<TicketSalePwtInfoBean> ticketWiseSalePwtInfoList = null; 
		
		try {
			if (requestData == null || requestData.length() == 0){
				throw new SLEException(SLEErrors.NO_REQUEST_DATA_PROVIDED_ERROR_CODE, SLEErrors.NO_REQUEST_DATA_PROVIDED_ERROR_MESSAGE);
			}
			int merchantId = TransactionManager.getMerchantId();
			JsonParser parser = new JsonParser();
			JsonObject reqData = (JsonObject)parser.parse(requestData);
			logger.info("Sports Lottery fetchDetailedWinningPaymentReport Request Data: "+requestData);
			int gameId = reqData.get("gameId").getAsInt();
			int gameTypeId = reqData.get("gameTypeId").getAsInt();
			String detailType = reqData.get("detailType").getAsString();
			String startDate = reqData.get("startTime").getAsString();
			String endDate = reqData.get("endTime").getAsString();
			String retList = null;
			if (!"DETAILED".equalsIgnoreCase(detailType)) {
				retList = reqData.get("retList").getAsJsonArray().toString();
			 }
			if (detailType.isEmpty() || startDate.isEmpty() || endDate.isEmpty()){
				throw new SLEException(SLEErrors.INVALID_DATA_ERROR_CODE, SLEErrors.INVALID_DATA_ERROR_MESSAGE);
			}
			ticketWiseSalePwtInfoList = new SportsLotteryMobileReportsControllerImpl().fetchDetailedSmallWinningPaymentReport(merchantId, gameId, gameTypeId, startDate, endDate, detailType, retList);
		    js.put("responseCode", 0);
			js.put("responseMessage", "success");
			js.put("isSuccess", true);
			js.put("responseData", ticketWiseSalePwtInfoList);
			
			
		}catch (SLEException se) {
		     se.printStackTrace();
		     js.put("responseCode", se.getErrorCode());
		     js.put("responseMessage", se.getErrorMessage());
			 js.put("isSuccess", false);
		   
		     
		}catch (Exception e) {
			 e.printStackTrace();
			 js.put("responseCode", SLEErrors.GENERAL_EXCEPTION_ERROR_CODE);
			 js.put("responseMessage", SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			 js.put("isSuccess", false);
			
			 
		}finally{
			if (js.isEmpty()) {
				js.put("responseCode", SLEErrors.GENERAL_EXCEPTION_ERROR_CODE);
				js.put("responseMessage", SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
				js.put("isSuccess", false);
				 
				
			}
			logger.debug("***** Detailed Winning Payment report Info" +js);
		}
		return Response.ok(js).build();
	}
}
