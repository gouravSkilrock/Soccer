package com.skilrock.sle.tp.rest.playMgmt;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.skilrock.sle.common.SLELogger;
import com.skilrock.sle.common.SportsLotteryUtils;
import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.common.javaBeans.UserInfoBean;
import com.skilrock.sle.commonMethod.daoImpl.CommonMethodsDaoImpl;
import com.skilrock.sle.commonMethod.serviceImpl.CommonMethodsServiceImpl;
import com.skilrock.sle.dataMgmt.javaBeans.CancelTransactionAPIBean;
import com.skilrock.sle.gamePlayMgmt.controllerImpl.CancelTicketControllerImpl;
import com.skilrock.sle.gamePlayMgmt.controllerImpl.PurchaseTicketControllerImpl;
import com.skilrock.sle.gamePlayMgmt.javaBeans.SportsLotteryGameDrawDataBean;
import com.skilrock.sle.gamePlayMgmt.javaBeans.SportsLotteryGameEventDataBean;
import com.skilrock.sle.gamePlayMgmt.javaBeans.SportsLotteryGamePlayBean;

import net.sf.json.JSONObject;

@Path("/playMgmt")
public class TPPlayMgmtServiceImpl {
	private static final SLELogger logger = SLELogger.getLogger(TPPlayMgmtServiceImpl.class.getName());

	@Path("/purchaseTicket")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response purchaseTicket(String requestData,@Context HttpServletRequest request) {
		JSONObject resObj = null;
		int gameId = 0;
		int gameTypeId = 0;
		int eventId = 0;
		UserInfoBean userInfoBean = null;
		String merchantCode = null;
		String reqChannel=null;
		SportsLotteryGameEventDataBean eventDataBean = null;
		SportsLotteryGameDrawDataBean gameDrawDataBean = null;
		String refTransId=null;
		try {
			resObj = new JSONObject();
			logger.info("TP-Sports Lottery Sale Request Data: "+requestData);
			if (requestData == null || requestData.trim().length() < 1){
				throw new SLEException(SLEErrors.NO_REQUEST_DATA_PROVIDED_ERROR_CODE,SLEErrors.NO_REQUEST_DATA_PROVIDED_ERROR_MESSAGE);
			}
			JsonObject sportsLotteryPlayData = new JsonParser().parse(requestData).getAsJsonObject();
			
			userInfoBean = new UserInfoBean();
			merchantCode = sportsLotteryPlayData.get("merchantCode").getAsString();
			if("Asoft".equalsIgnoreCase(merchantCode) || "OKPOS".equalsIgnoreCase(merchantCode)){
				if(sportsLotteryPlayData.get("userId")==null || sportsLotteryPlayData.get("userId").getAsString().trim().length()<=0 || sportsLotteryPlayData.get("refTransId")==null || sportsLotteryPlayData.get("refTransId").getAsString().trim().length()<0){
					throw new SLEException(SLEErrors.PROPER_REQUEST_DATA_INVALID_ERROR_CODE,SLEErrors.PROPER_REQUEST_DATA_INVALID_ERROR_MESSAGE);
				}else{
					if("Asoft".equalsIgnoreCase(merchantCode)){
						if(sportsLotteryPlayData.get("mobileNo")==null || sportsLotteryPlayData.get("mobileNo").getAsString().trim().isEmpty()){
							throw new SLEException(SLEErrors.PROPER_REQUEST_DATA_INVALID_ERROR_CODE,SLEErrors.PROPER_REQUEST_DATA_INVALID_ERROR_MESSAGE);
						}
					}	
					userInfoBean.setUserId(sportsLotteryPlayData.get("userId").getAsString());
					userInfoBean.setMobileNbr(sportsLotteryPlayData.get("mobileNo")!=null?sportsLotteryPlayData.get("mobileNo").getAsString():null);
					refTransId=sportsLotteryPlayData.get("refTransId").getAsString();
					
				}
			}
				
			if("PMS".equals(merchantCode)){
				reqChannel=request.getHeader("reqChannel");
				userInfoBean.setUserName(sportsLotteryPlayData.get("playerName").getAsString());
				userInfoBean.setUserSessionId(sportsLotteryPlayData.get("sessionId").getAsString());
				if(!"PORTAL".equalsIgnoreCase(reqChannel)){
					reqChannel="MOBILE";
				}
				userInfoBean.setUserType("PLAYER");
			}else if ("Asoft".equals(merchantCode)) {
				reqChannel="MOBILE";
				userInfoBean.setUserType("PLAYER");
			}else if("OKPOS".equals(merchantCode)){
				reqChannel="TERMINAL";
				userInfoBean.setUserType("RETAILER");
			}else if ("Weaver".equals(merchantCode)) {
				reqChannel=request.getHeader("reqChannel");
				userInfoBean.setUserName(sportsLotteryPlayData.get("playerName").getAsString());
				userInfoBean.setUserSessionId(sportsLotteryPlayData.get("sessionId").getAsString());
				if(!"PORTAL".equalsIgnoreCase(reqChannel)){
					reqChannel="MOBILE";
					userInfoBean.setChannelName(reqChannel);
				}
				userInfoBean.setUserType("PLAYER");
				userInfoBean.setMerchantUserId(sportsLotteryPlayData.get("userId").getAsInt());
				
			}else if ("TonyBet".equals(merchantCode)) {
				reqChannel = request.getHeader("reqChannel");
				userInfoBean.setUserName(sportsLotteryPlayData.get("playerName").getAsString());
				userInfoBean.setUserSessionId(sportsLotteryPlayData.get("sessionId").getAsString());
				if(!"PORTAL".equalsIgnoreCase(reqChannel)){
					reqChannel="MOBILE";
					userInfoBean.setChannelName(reqChannel);
				}
				userInfoBean.setUserType("PLAYER");
				userInfoBean.setMerchantUserId(sportsLotteryPlayData.get("userId").getAsInt());
			}
			else{
				throw new SLEException(SLEErrors.INVALID_MERCHANT_NAME_ERROR_CODE,SLEErrors.INVALID_MERCHANT_NAME_ERROR_MESSAGE);
			}
			userInfoBean.setMerchantId(Util.merchantInfoMap.get(merchantCode).getMerchantId());
			userInfoBean.setMerchantDevName(merchantCode);
			


			SportsLotteryGamePlayBean gamePlayBean = new SportsLotteryGamePlayBean();
			gameId = sportsLotteryPlayData.get("gameId").getAsInt();
			gameTypeId = sportsLotteryPlayData.get("gameTypeId").getAsInt();
			gamePlayBean.setGameId(gameId);
			gamePlayBean.setGameTypeId(gameTypeId);
			gamePlayBean.setGameDevName(SportsLotteryUtils.gameInfoMerchantMap.get(merchantCode).get(gameId).getGameDevName());
			int noOfBoard = sportsLotteryPlayData.get("noOfBoard").getAsInt();
			int noOfEvents = SportsLotteryUtils.gameTypeInfoMerchantMap.get(merchantCode).get(gameTypeId).getNoOfEvents();
			String gameTypeDevName = SportsLotteryUtils.gameTypeInfoMerchantMap.get(merchantCode).get(gameTypeId).getGameTypeDevName();
			double unitPrice = SportsLotteryUtils.gameTypeInfoMerchantMap.get(merchantCode).get(gameTypeId).getUnitPrice();
			double maxBetAmtMul = SportsLotteryUtils.gameTypeInfoMerchantMap.get(merchantCode).get(gameTypeId).getMaxBetAmtMultiple();
			double maxTktAmt=SportsLotteryUtils.gameInfoMerchantMap.get(merchantCode).get(gameId).getMaxTicketAmt();
			double totalPurchaseAmt = 0.0;
			Set<Integer> drawIsSet = new HashSet<Integer>();

			gamePlayBean.setNoOfBoard(noOfBoard);
			SportsLotteryGameEventDataBean[] eventDataBeanArray = null;
			SportsLotteryGameDrawDataBean[] gameDrawDataBeanArray = new SportsLotteryGameDrawDataBean[noOfBoard];

			JsonArray drawInfoArr = sportsLotteryPlayData.get("drawInfo").getAsJsonArray();
			JsonObject drawDataObject = null;
			JsonArray eventInfoArr = null;
			JsonObject eventDataObject = null;

			for (int i = 0; i < noOfBoard; i++) {
				drawDataObject = drawInfoArr.get(i).getAsJsonObject();

				int drawId = drawDataObject.get("drawId").getAsInt();
				int betAmtMultiple = drawDataObject.get("betAmtMul").getAsInt();
				if(betAmtMultiple>maxBetAmtMul){
					throw new SLEException(SLEErrors.MAXIMUM_BET_AMOUNT_MULTIPLE_LIMIT_EXCEED_ERROR_CODE,SLEErrors.MAXIMUM_BET_AMOUNT_MULTIPLE_LIMIT_EXCEED_ERROR_MESSAGE);
				}
				eventInfoArr = drawDataObject.get("eventData").getAsJsonArray();

				int noOfLines = 1;

				eventDataBeanArray = new SportsLotteryGameEventDataBean[noOfEvents];
				drawIsSet.add(drawId);
				for (int j = 0; j < noOfEvents; j++) {
					eventDataObject = eventInfoArr.get(j).getAsJsonObject();
					eventDataBean = new SportsLotteryGameEventDataBean();
					eventId = eventDataObject.get("eventId").getAsInt();
					eventDataBean.setEventId(eventDataObject.get("eventId").getAsInt());
					String[] selectedOption = eventDataObject.get("eventSelected").getAsString().split(",");
					if ("soccer13".equalsIgnoreCase(gameTypeDevName) || "soccer10".equalsIgnoreCase(gameTypeDevName)) {
						if (selectedOption.length > CommonMethodsServiceImpl.getInstance().fetchGameTypeOptionList(gameId, gameTypeId).size() 
								|| selectedOption.length < 1) {
							throw new SLEException(SLEErrors.INVALID_NUMBER_OF_EVENTS_ENTERED_ERROR_CODE,SLEErrors.INVALID_NUMBER_OF_EVENTS_ENTERED_ERROR_MESSAGE);
						}
					}
					if ("soccer4".equalsIgnoreCase(gameTypeDevName) || "soccer6".equalsIgnoreCase(gameTypeDevName)) {
						if (selectedOption.length > CommonMethodsServiceImpl.getInstance().fetchGameTypeOptionList(gameId, gameTypeId).size() 
								|| selectedOption.length < 1) {
							throw new SLEException(SLEErrors.INVALID_NUMBER_OF_EVENTS_ENTERED_ERROR_CODE,SLEErrors.INVALID_NUMBER_OF_EVENTS_ENTERED_ERROR_MESSAGE);
						}
					}
					Set<String> containsEvent  = new HashSet<String>();
					for (String event : selectedOption) {
						if (!containsEvent.add(event)) {
							throw new SLEException(SLEErrors.SAME_EVENT_SELECTED_ERROR_CODE,SLEErrors.SAME_EVENT_SELECTED_ERROR_MESSAGE);
						}
					}
					noOfLines *= selectedOption.length;

					eventDataBean.setSelectedOption(selectedOption);
					eventDataBeanArray[j] = eventDataBean;
				}
				
				gameDrawDataBean = new SportsLotteryGameDrawDataBean();
				gameDrawDataBean.setBetAmountMultiple(betAmtMultiple);
				gameDrawDataBean.setNoOfLines(noOfLines);

				gameDrawDataBean.setBoardPurchaseAmount(noOfLines * unitPrice * betAmtMultiple);
				totalPurchaseAmt += noOfLines * unitPrice * betAmtMultiple;
				gameDrawDataBean.setDrawId(drawId);
				gameDrawDataBean.setGameEventDataBeanArray(eventDataBeanArray);
				gameDrawDataBeanArray[i] = gameDrawDataBean;
			}
            if(totalPurchaseAmt>maxTktAmt){
            	throw new SLEException(SLEErrors.MAXIMUM_TICKET_PRICE_LIMIT_EXCEED_ERROR_CODE,SLEErrors.MAXIMUM_TICKET_PRICE_LIMIT_EXCEED_ERROR_MESSAGE);
            	
            }
			gamePlayBean.setGameDrawDataBeanArray(gameDrawDataBeanArray);
			gamePlayBean.setServiceId(Util.merchantInfoMap.get(merchantCode).getServiceId());
			gamePlayBean.setInterfaceType(reqChannel);
			gamePlayBean.setTotalPurchaseAmt(Util.fmtToTwoDecimal(totalPurchaseAmt));
			gamePlayBean.setUnitPrice(Util.fmtToTwoDecimal(unitPrice));
			Integer[] drawIdArray = (Integer[]) drawIsSet.toArray(new Integer[drawIsSet.size()]);
			gamePlayBean.setDrawIdArray(drawIdArray);
			
			resObj = PurchaseTicketControllerImpl.getInstance().purchaseTicket(userInfoBean, gamePlayBean,eventId,refTransId);
		} catch (SLEException pe) {
			pe.printStackTrace();
			if(pe.getErrorCode()==10012){
				resObj.put("responseCode", SLEErrors.INVALID_SESSION_MOBILE_ERROR_CODE);
				resObj.put("responseMsg",SLEErrors.INVALID_SESSION_MOBILE_ERROR_MESSAGE);
			}else{
				resObj.put("responseCode", pe.getErrorCode());
				resObj.put("responseMsg", pe.getErrorMessage());
			}
		} catch (Exception e) {
			e.printStackTrace();
			resObj.put("responseCode", SLEErrors.GENERAL_EXCEPTION_ERROR_CODE);
			resObj.put("responseMsg", SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			if (resObj.isEmpty()) {
				resObj.put("responseCode", SLEErrors.GENERAL_EXCEPTION_ERROR_CODE);
				resObj.put("responseMsg", SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			}
			logger.debug("TP-Sports Lottery Sale Response Data: "+resObj);
		}
		return Response.ok().entity(resObj).build();
	}
	
	
	@Path("/cancelTransaction")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response cancelTransaction(String requestData,@Context HttpServletRequest request) {
		JSONObject resObj = null;
		String merchantCode = null;
		String refSaleTransId = null;
		String refTransId = null;
		String userId = null;
		UserInfoBean userBean = null;
		CancelTransactionAPIBean cancelBean = null;
		try{
			resObj = new JSONObject();
			logger.info("TP-Sports Lottery Cancel Txn Request Data"+requestData);
			if(requestData == null || requestData.trim().isEmpty()){
				throw new SLEException(SLEErrors.NO_REQUEST_DATA_PROVIDED_ERROR_CODE,SLEErrors.NO_REQUEST_DATA_PROVIDED_ERROR_MESSAGE);
			}
			
			JsonObject sportsLotteryData = new JsonParser().parse(requestData).getAsJsonObject();
			if(sportsLotteryData.get("merchantCode") == null || sportsLotteryData.get("merchantCode").getAsString().trim().isEmpty() || sportsLotteryData.get("refSaleTransId") == null || sportsLotteryData.get("refSaleTransId").getAsString().trim().isEmpty() || sportsLotteryData.get("refTransId") == null || sportsLotteryData.get("refTransId").getAsString().trim().isEmpty() || sportsLotteryData.get("userId") == null || sportsLotteryData.get("userId").getAsString().trim().isEmpty()){
				throw new SLEException(SLEErrors.PROPER_REQUEST_DATA_INVALID_ERROR_CODE, SLEErrors.PROPER_REQUEST_DATA_INVALID_ERROR_MESSAGE);
			}else{
				merchantCode = sportsLotteryData.get("merchantCode").getAsString();
				refSaleTransId = sportsLotteryData.get("refSaleTransId").getAsString();
				refTransId = sportsLotteryData.get("refTransId").getAsString();
				userId = sportsLotteryData.get("userId").getAsString();
			}
			if(Util.merchantInfoMap.containsKey(merchantCode)){
				if("Asoft".equalsIgnoreCase(merchantCode) || "OKPOS".equalsIgnoreCase(merchantCode)){
					userBean = new UserInfoBean();
					cancelBean = new CancelTransactionAPIBean();
					cancelBean.setRefTransId(refTransId);
					cancelBean.setRefSaleTransId(refSaleTransId);
					userBean.setUserId(userId);
					userBean.setMerchantDevName(merchantCode);
					userBean.setMerchantId(Util.merchantInfoMap.get(merchantCode).getMerchantId());
					
					new CancelTicketControllerImpl().cancelTransactionControllerAPI(userBean, cancelBean);
					
					resObj.put("responseCode", 0);
					resObj.put("responseMessage", "Transaction is cancelled successfully!");
					resObj.put("cancelTransId", cancelBean.getCancelTxnId());
					resObj.put("amount", cancelBean.getCancelAmount());
				}else{
					throw new SLEException(SLEErrors.INVALID_MERCHANT_NAME_ERROR_CODE, SLEErrors.INVALID_MERCHANT_NAME_ERROR_MESSAGE);
				}
			}else{
				throw new SLEException(SLEErrors.INVALID_MERCHANT_NAME_ERROR_CODE, SLEErrors.INVALID_MERCHANT_NAME_ERROR_MESSAGE);
			}
		}catch(SLEException sl){
			resObj.put("responseCode", sl.getErrorCode());
			resObj.put("responseMsg", sl.getErrorMessage());
		}
		catch(Exception e){
			e.printStackTrace();
			resObj.put("responseCode", SLEErrors.GENERAL_EXCEPTION_ERROR_CODE);
			resObj.put("responseMsg", SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			if (resObj.isEmpty()) {
				resObj.put("responseCode", SLEErrors.GENERAL_EXCEPTION_ERROR_CODE);
				resObj.put("responseMsg", SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			}
			logger.debug("TP-Sports Lottery Cancel Txn Response Data: "+resObj);
		}
		return Response.ok().entity(resObj).build();
	}
}