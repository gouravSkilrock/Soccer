package com.skilrock.sle.tp.rest.pwtMgmt;

import java.text.DecimalFormat;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.skilrock.sle.common.SLELogger;
import com.skilrock.sle.common.TransactionManager;
import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.exception.SLEErrorProperty;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.merchant.common.javaBeans.TPPwtRequestBean;
import com.skilrock.sle.pwtMgmt.controllerImpl.PayPrizeTicketControllerImpl;
import com.skilrock.sle.pwtMgmt.controllerImpl.PrizeTicketVerificationControllerImpl;
import com.skilrock.sle.pwtMgmt.controllerImpl.WinningMgmtControllerImpl;
import com.skilrock.sle.pwtMgmt.javaBeans.DrawWiseTicketInfoBean;
import com.skilrock.sle.pwtMgmt.javaBeans.PwtVerifyTicketBean;
import com.skilrock.sle.pwtMgmt.javaBeans.PwtVerifyTicketDrawDataBean;

@Path("/pwtMgmt")
public class TPPwtMgmtServiceImpl {
	private static final SLELogger logger = SLELogger.getLogger(TPPwtMgmtServiceImpl.class.getName());

	@Path("/getWinningTickets")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getWinningTickets(String reqData) {
		logger.info("--- getWinningTickets ---");
		JsonObject requestObject = null;
		DrawWiseTicketInfoBean infoBean = new DrawWiseTicketInfoBean();
		try {
			requestObject = new JsonParser().parse(reqData).getAsJsonObject();

			int merchantId = TransactionManager.getMerchantId();

			infoBean.setMerchantId(merchantId);
			infoBean.setGameId(requestObject.get("gameId").getAsInt());
			infoBean.setGameTypeId(requestObject.get("gameTypeId").getAsInt());
			infoBean.setDrawId(requestObject.get("drawId").getAsInt());

			WinningMgmtControllerImpl.getSingleInstance().fetchWinningTickets(infoBean);
		} catch (SLEException se) {
			return Response.status(se.getErrorCode()).entity(SLEErrorProperty.getPropertyValue(se.getErrorCode())).build();
		} catch (Exception e) {
			return Response.status(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE).entity(SLEErrorProperty.getPropertyValue(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE)).build();
		}

		return Response.ok(infoBean).build();
	}

	@Path("/claimedTicketStatus")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateWinningTransaction(String reqData) {
		logger.info("--- updateWinningTransaction ---");
		JsonObject requestObject = null;
		try {
			requestObject = new JsonParser().parse(reqData).getAsJsonObject();

			int merchantId = TransactionManager.getMerchantId();
			int gameId = requestObject.get("gameId").getAsInt();
			int gameTypeId = requestObject.get("gameTypeId").getAsInt();
			int drawId = requestObject.get("drawId").getAsInt();
			Map<Long, String> transMap = new Gson().fromJson(requestObject.get("ticketMap"), new TypeToken<Map<Long, String>>() {}.getType());
			WinningMgmtControllerImpl.getSingleInstance().updateWinningTransaction(merchantId, gameId, gameTypeId, drawId, transMap);
		} catch (SLEException se) {
			return Response.status(se.getErrorCode()).entity(SLEErrorProperty.getPropertyValue(se.getErrorCode())).build();
		} catch (Exception e) {
			return Response.status(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE).entity(SLEErrorProperty.getPropertyValue(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE)).build();
		}

		return Response.ok().build();
	}

	//	http://localhost:8081/SportsLottery/rest/pwtMgmt/verifyTicket
	//	{"ticketNumber":"664562055172310010"}
	@Path("/verifyTicket")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response verifyTicket(String reqData) {
		logger.info("--- verifyTicket ---"+reqData);
		JsonObject requestObject = null;
		JsonObject responseObject = new JsonObject();
		double pwtTaxPercent=0.0;
		try {
			requestObject = new JsonParser().parse(reqData).getAsJsonObject();

			int merchantId = TransactionManager.getMerchantId();
			String merchantName = Util.fetchMerchantInfoBean(merchantId).getMerchantDevName();
			String ticketNumber = requestObject.get("ticketNumber").getAsString();
			pwtTaxPercent=Double.parseDouble(Util.getPropertyValue("PLAYER_WINNING_TAX_PERCENTAGE"));
			double pwtWinTaxApplcbleAmt=Double.parseDouble(Util.getPropertyValue("PLAYER_WINNING_TAX_APPLICABLE_AMOUNT"));

			PrizeTicketVerificationControllerImpl controllerImpl = new PrizeTicketVerificationControllerImpl();
			PwtVerifyTicketBean verifyTicketBean = controllerImpl.verifyTicketForAPI(merchantName, ticketNumber);
			for(PwtVerifyTicketDrawDataBean drawDataBean : verifyTicketBean.getVerifyTicketDrawDataBeanArray()) {
				if("CLAIMED".equals(drawDataBean.getStatus())) {
					throw new SLEException(SLEErrors.TICKET_ALREADY_CLAIMED_ERROR_CODE, SLEErrors.TICKET_ALREADY_CLAIMED_ERROR_MESSAGE);
				}
				if("CANCELLED".equals(drawDataBean.getStatus())) {
					throw new SLEException(SLEErrors.TICKET_CANCELLED_ERROR_CODE, SLEErrors.TICKET_CANCELLED_ERROR_MESSAGE);
				}
			}
			//call LMS
			//double a=controllerImpl.getPwtTaxDetails(verifyTicketBean.getGameId(), verifyTicketBean.getGameTypeId(), verifyTicketBean.getTicketNumInDB(), verifyTicketBean.getTotalWinAmt());
			
			double winAmount = verifyTicketBean.getTotalWinAmt();
			double taxAmount=winAmount>pwtWinTaxApplcbleAmt?Double.parseDouble(Util.getCurrentAmountFormatForMobile(winAmount*pwtTaxPercent*.01)):0.0;
			
			DecimalFormat decimalFormat = new DecimalFormat("###0.00");

			responseObject.addProperty("responseCode", 0);
			responseObject.addProperty("responseMessage", "SUCCESS");
			responseObject.addProperty("gameName", verifyTicketBean.getGameName());
			responseObject.addProperty("gameTypeName", verifyTicketBean.getGameTypename());
			responseObject.addProperty("ticketNumber", ticketNumber);
			responseObject.addProperty("drawNo", verifyTicketBean.getVerifyTicketDrawDataBeanArray()[0].getDrawNo());
			responseObject.addProperty("drawName", verifyTicketBean.getVerifyTicketDrawDataBeanArray()[0].getDrawName());
			responseObject.addProperty("drawDateTime", verifyTicketBean.getVerifyTicketDrawDataBeanArray()[0].getDrawDateTime());
			responseObject.addProperty("drawStatus", verifyTicketBean.getVerifyTicketDrawDataBeanArray()[0].getDrawStatus());
			responseObject.addProperty("drawResult", verifyTicketBean.getVerifyTicketDrawDataBeanArray()[0].getDrawResult());
			responseObject.addProperty("purchaseAmount", decimalFormat.format(verifyTicketBean.getTotalPurchaseAmt()));
			responseObject.addProperty("purchaseDateTime", verifyTicketBean.getPurchaseDateTime());
			responseObject.addProperty("noOfMatches", verifyTicketBean.getVerifyTicketDrawDataBeanArray()[0].getNoOfMatches());
			responseObject.addProperty("winningAmount", decimalFormat.format(verifyTicketBean.getTotalWinAmt()-taxAmount));
			logger.info("verifyTicket response data--"+responseObject.toString());
		} catch (SLEException se) {
			responseObject.addProperty("responseCode", se.getErrorCode());
			responseObject.addProperty("responseMessage", se.getErrorMessage());
		} catch (Exception e) {
			e.printStackTrace();
			responseObject.addProperty("responseCode", SLEErrors.GENERAL_EXCEPTION_ERROR_CODE);
			responseObject.addProperty("responseMessage", SLEErrors.GENERAL_EXCEPTION_ERROR_CODE);
		}

		return Response.ok().entity(responseObject.toString() ).build();
	}

	//	http://localhost:8081/SportsLottery/rest/pwtMgmt/payPwtTicket
	//	{"ticketNumber":"512062048000000020", "transactionId":"12345ABCDE6789"}
	@Path("/payPwtTicket")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response payPwtTicket(String reqData) {
		logger.info("--- payPwtTicket ---"+reqData);
		JsonObject requestObject = null;
		JsonObject responseObject = new JsonObject();
		double pwtTaxPercent=0.0;
		try {
			requestObject = new JsonParser().parse(reqData).getAsJsonObject();

			int merchantId = TransactionManager.getMerchantId();
			String merchantName = Util.fetchMerchantInfoBean(merchantId).getMerchantDevName();
			String ticketNumber = requestObject.get("ticketNumber").getAsString();
			String transactionId = requestObject.get("transactionId").getAsString();
			pwtTaxPercent=Double.parseDouble(Util.getPropertyValue("PLAYER_WINNING_TAX_PERCENTAGE"));
			double pwtWinTaxApplcbleAmt=Double.parseDouble(Util.getPropertyValue("PLAYER_WINNING_TAX_APPLICABLE_AMOUNT"));

			PrizeTicketVerificationControllerImpl controllerImpl = new PrizeTicketVerificationControllerImpl();
			PwtVerifyTicketBean verifyTicketBean = controllerImpl.verifyTicketForAPI(merchantName, ticketNumber);
			for(PwtVerifyTicketDrawDataBean drawDataBean : verifyTicketBean.getVerifyTicketDrawDataBeanArray()) {
				if("CLAIMED".equals(drawDataBean.getStatus())) {
					throw new SLEException(SLEErrors.TICKET_ALREADY_CLAIMED_ERROR_CODE, SLEErrors.TICKET_ALREADY_CLAIMED_ERROR_MESSAGE);
				} else if("CANCELLED".equals(drawDataBean.getStatus())) {
					throw new SLEException(SLEErrors.TICKET_CANNOT_CLAIMED_ERROR_CODE, SLEErrors.TICKET_CANNOT_CLAIMED_ERROR_MESSAGE);
				}

				if(drawDataBean.getDrawWinAmt() == 0) {
					throw new SLEException(SLEErrors.TICKET_CANNOT_CLAIMED_ERROR_CODE, SLEErrors.TICKET_CANNOT_CLAIMED_ERROR_MESSAGE);
				}
			}

			TPPwtRequestBean pwtRequestBean = PayPrizeTicketControllerImpl.getInstance().apiPayWinning(merchantName, ticketNumber, transactionId);
			
	
			Map<Integer, Long> transMap=pwtRequestBean.getTransMap();
			
			double winAmount = verifyTicketBean.getTotalWinAmt();
			double taxAmount=winAmount>pwtWinTaxApplcbleAmt?Double.parseDouble(Util.getCurrentAmountFormatForMobile(winAmount*pwtTaxPercent*.01)):0.0;
			
			//call LMS for 5% pwt
			/*TPPwtResponseBean pwtResponseBean = LMSIntegrationImpl.winningSLETicket(pwtRequestBean, userBean, "API");
			
			PayPrizeTicketControllerImpl.getInstance().updateWinningRequest(transMap.get(verifyTicketBean.getVerifyTicketDrawDataBeanArray()[0].getDrawId()), 123+"", "DONE");*/
			
			DecimalFormat decimalFormat = new DecimalFormat("###0.00");

			responseObject.addProperty("responseCode", 0);
			responseObject.addProperty("responseMessage", "SUCCESS");
			responseObject.addProperty("gameName", verifyTicketBean.getGameName());
			responseObject.addProperty("gameTypeName", verifyTicketBean.getGameTypename());
			responseObject.addProperty("ticketNumber", ticketNumber);
			responseObject.addProperty("drawNo", verifyTicketBean.getVerifyTicketDrawDataBeanArray()[0].getDrawNo());
			responseObject.addProperty("drawName", verifyTicketBean.getVerifyTicketDrawDataBeanArray()[0].getDrawName());
			responseObject.addProperty("drawDateTime", verifyTicketBean.getVerifyTicketDrawDataBeanArray()[0].getDrawDateTime());
			responseObject.addProperty("drawStatus", verifyTicketBean.getVerifyTicketDrawDataBeanArray()[0].getDrawStatus());
			responseObject.addProperty("drawResult", verifyTicketBean.getVerifyTicketDrawDataBeanArray()[0].getDrawResult());
			responseObject.addProperty("purchaseAmount", decimalFormat.format(verifyTicketBean.getTotalPurchaseAmt()));
			responseObject.addProperty("purchaseDateTime", verifyTicketBean.getPurchaseDateTime());
			responseObject.addProperty("noOfMatches", verifyTicketBean.getVerifyTicketDrawDataBeanArray()[0].getNoOfMatches());
			responseObject.addProperty("winningAmount", decimalFormat.format(verifyTicketBean.getTotalWinAmt()-taxAmount));
			responseObject.addProperty("refTransactionId", transMap.get(verifyTicketBean.getVerifyTicketDrawDataBeanArray()[0].getDrawId()));
			logger.info("pay pwt ticket response data--"+responseObject.toString());
		} catch (SLEException se) {
			responseObject.addProperty("responseCode", se.getErrorCode());
			responseObject.addProperty("responseMessage", se.getErrorMessage());
		} catch (Exception e) {
			e.printStackTrace();
			responseObject.addProperty("responseCode", SLEErrors.GENERAL_EXCEPTION_ERROR_CODE);
			responseObject.addProperty("responseMessage", SLEErrors.GENERAL_EXCEPTION_ERROR_CODE);
		}

		return Response.ok().entity(responseObject.toString() ).build();
	}
}