package com.skilrock.sle.web.merchantUser.playMgmt.action;

import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import net.sf.json.JSONObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import com.skilrock.sle.common.SLELogger;
import com.skilrock.sle.common.SportsLotteryUtils;
import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.common.javaBeans.UserInfoBean;
import com.skilrock.sle.commonMethod.serviceImpl.CommonMethodsServiceImpl;

import com.skilrock.sle.gamePlayMgmt.controllerImpl.PurchaseTicketControllerImpl;
import com.skilrock.sle.gamePlayMgmt.javaBeans.SportsLotteryGameDrawDataBean;
import com.skilrock.sle.gamePlayMgmt.javaBeans.SportsLotteryGameEventDataBean;
import com.skilrock.sle.gamePlayMgmt.javaBeans.SportsLotteryGamePlayBean;
import com.skilrock.sle.merchant.common.javaBeans.MerchantInfoBean;
import com.skilrock.sle.merchant.lms.common.javaBeans.LMSGamePlayResponseBean;
import com.skilrock.sle.mobile.common.BaseActionMobile;
import com.skilrock.sle.web.merchantUser.common.SportsLotteryWebResponseData;

public class SportsLotteryGamePlayAction extends BaseActionMobile{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final SLELogger logger = SLELogger.getLogger(SportsLotteryGamePlayAction.class.getName());

	public void sportsLotteryPurchaseTicket(){
		JSONObject jsonObject = null;
		PrintWriter out = null;
		int gameId = 0;
		int eventId = 0;
		List<String> eventOptionsList = null;
		int gameTypeId = 0;
		String merchantCode = null;
		UserInfoBean userInfoBean = null;
		LMSGamePlayResponseBean gameResponseBean = null;
		MerchantInfoBean merBean = null;
		try {
			jsonObject = new JSONObject();
			JsonObject sportsLotteryPlayData = new JsonParser().parse(getRequestData()).getAsJsonObject();
			logger.info("PCPOS Sale - Request Data : "+sportsLotteryPlayData);
			out = response.getWriter();
			response.setContentType("application/json");
			
			merchantCode = sportsLotteryPlayData.get("merchantCode").getAsString();
			userInfoBean = new UserInfoBean();
			if(sportsLotteryPlayData.get("tokenId")!=null&&!sportsLotteryPlayData.get("tokenId").getAsString().trim().isEmpty()){
				userInfoBean.setTokenId(sportsLotteryPlayData.get("tokenId").getAsString());
			}
			userInfoBean.setUserName(sportsLotteryPlayData.get("userName").getAsString());
			merBean = Util.merchantInfoMap.get(merchantCode);
			userInfoBean.setMerchantId(merBean.getMerchantId());
			userInfoBean.setUserSessionId(sportsLotteryPlayData.get("sessionId").getAsString());
			userInfoBean.setMerchantDevName(merchantCode);

		
			CommonMethodsServiceImpl.getInstance().fetchAndCheckUserData(userInfoBean);

			SportsLotteryGameEventDataBean eventDataBean = null;
			SportsLotteryGameDrawDataBean gameDrawDataBean = null;

			SportsLotteryGamePlayBean gamePlayBean = new SportsLotteryGamePlayBean();
			gameId = sportsLotteryPlayData.get("gameId").getAsInt();
			gameTypeId = sportsLotteryPlayData.get("gameTypeId").getAsInt();
			gamePlayBean.setGameId(gameId);
			gamePlayBean.setGameTypeId(gameTypeId);

			int noOfBoard = sportsLotteryPlayData.get("noOfBoard").getAsInt();
			int noOfEvents = SportsLotteryUtils.gameTypeInfoMerchantMap.get(merchantCode).get(gameTypeId).getNoOfEvents();

			double unitPrice = SportsLotteryUtils.gameTypeInfoMerchantMap.get(merchantCode).get(gameTypeId).getUnitPrice();
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

			gamePlayBean.setGameDrawDataBeanArray(gameDrawDataBeanArray);
			
			gamePlayBean.setInterfaceType("WEB");
			gamePlayBean.setServiceId(merBean.getServiceId());
			gamePlayBean.setTotalPurchaseAmt(Util.fmtToTwoDecimal(totalPurchaseAmt));
			gamePlayBean.setUnitPrice(Util.fmtToTwoDecimal(unitPrice));
			Integer[] drawIdArray = (Integer[]) drawIsSet.toArray(new Integer[drawIsSet.size()]);
			gamePlayBean.setDrawIdArray(drawIdArray);
			gameResponseBean = PurchaseTicketControllerImpl.getInstance().purchaseTicketForPcpos(userInfoBean, gamePlayBean,eventId, eventOptionsList);
			gamePlayBean.setAdvMessageMap(gameResponseBean.getAdvMessageMap());

			if (gameResponseBean.getResponseCode() == 0) {
				jsonObject = SportsLotteryWebResponseData.generateSportsLotterySaleResponseData(gamePlayBean, userInfoBean.getMerchantDevName(), gameResponseBean.getAvailBal(),eventOptionsList,userInfoBean.getOrgName());
				String smsData = Util.prepareSMSData(gamePlayBean, userInfoBean.getMerchantDevName(), String.valueOf(gameResponseBean.getAvailBal()), gamePlayBean.getTransId());
				logger.info("SMS Data : " + smsData);
				Util.sendSMS(smsData);
			} else {
				jsonObject.put("responseCode", gameResponseBean.getResponseCode());
				jsonObject.put("responseMsg", gameResponseBean.getResponseMessage());			
			}

		} 
		catch (SLEException pe) {
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
			logger.info("PCPOS Sale - Response Data : "+jsonObject);
			out.print(jsonObject);
			out.flush();
			out.close();
		}
	}
}