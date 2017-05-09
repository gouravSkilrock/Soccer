package com.skilrock.sle.mobile.common;

import java.text.NumberFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.skilrock.sle.common.SportsLotteryUtils;
import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.common.javaBeans.TicketInfoBean;
import com.skilrock.sle.commonMethod.serviceImpl.CommonMethodsServiceImpl;
import com.skilrock.sle.dataMgmt.javaBeans.TicketTxnStatusBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.DrawMasterBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.EventMasterBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.GameMasterBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.GameTypeMasterBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.SLEDrawWinningBean;
import com.skilrock.sle.gamePlayMgmt.javaBeans.SportsLotteryGameDrawDataBean;
import com.skilrock.sle.gamePlayMgmt.javaBeans.SportsLotteryGameEventDataBean;
import com.skilrock.sle.gamePlayMgmt.javaBeans.SportsLotteryGamePlayBean;
import com.skilrock.sle.pwtMgmt.javaBeans.PwtVerifyTicketBean;
import com.skilrock.sle.pwtMgmt.javaBeans.PwtVerifyTicketDrawDataBean;
import com.skilrock.sle.reportsMgmt.javaBeans.WinningResultReportBean;

public class SportsLotteryResponseData {

	public static JSONObject generateDrawGameData(List<GameMasterBean> gameMasterList,int responseCode,String respMessage) throws Exception {
		JSONObject finalSportsLotteryData = new JSONObject();

		/*SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat format2 = new SimpleDateFormat("E dd MMM yyyy hh:mm a");*/
		finalSportsLotteryData.put("responseMsg",respMessage);
		finalSportsLotteryData.put("responseCode",responseCode);

		JSONObject finalGameData = new JSONObject();
		JSONArray gameDataArray = new JSONArray();
		for (GameMasterBean gameMasterBean : gameMasterList) {
			JSONObject gameData = new JSONObject();

			gameData.put("gameId", gameMasterBean.getGameId());
			gameData
					.put("gameDevname", gameMasterBean.getGameDevName());
			gameData.put("gameDisplayName", gameMasterBean
					.getGameDispName());
			gameData.put("tktThresholdAmt", gameMasterBean
					.getThersholdTickerAmt());
			gameData.put("tktMaxAmt", gameMasterBean.getMaxTicketAmt());
			gameData.put("minEventCount", gameMasterBean
					.getMinBoardCount());
			gameData.put("maxEventCount", gameMasterBean
					.getMaxBoardCount());
			gameData.put("maxEventCount", gameMasterBean
					.getMaxBoardCount());

			List<GameTypeMasterBean> gameTypeMasterList = gameMasterBean
					.getGameTypeMasterList();

			JSONArray gameTypeArray = new JSONArray();
			for (GameTypeMasterBean gameTypeMasterBean : gameTypeMasterList) {

				JSONObject gameTypeData = new JSONObject();

				String eventType = gameTypeMasterBean.getEventType();
				// List<String> eventOptionsList = null;
				String eventOptionsList = null;
				if ("SIMPLE".equals(eventType)) {
					List<DrawMasterBean> drawMasterList = gameTypeMasterBean
							.getDrawMasterList();
					if (drawMasterList != null && drawMasterList.size() > 0) {
						eventOptionsList = drawMasterList.get(0)
								.getEventMasterList().get(0)
								.getEventOptionsList().toString();
					} else {
						eventOptionsList = "";
					}
				}
				gameTypeData.put("gameTypeId", gameTypeMasterBean
						.getGameTypeId());
				gameTypeData.put("gameTypeDevName", gameTypeMasterBean
						.getGameTypeDevName());
				gameTypeData.put("gameTypeDisplayName",
						gameTypeMasterBean.getGameTypeDispName());
				gameTypeData.put("gameTypeUnitPrice",
						gameTypeMasterBean.getUnitPrice());
				gameTypeData.put("gameTypeMaxBetAmtMultiple",
						gameTypeMasterBean.getMaxBetAmtMultiple());
				gameTypeData.put("eventType", eventOptionsList);
				gameTypeData.put("eventSelectionType", gameTypeMasterBean.getEventSelectionType());
				gameTypeData.put("upcomingDrawStartTime", gameTypeMasterBean.getUpcomingDrawStartTime());
				gameTypeData.put("areEventsMappedForUpcomingDraw", gameTypeMasterBean.isAreEventsMappedForUpcomingDraw());
				List<DrawMasterBean> drawMasterList = gameTypeMasterBean
						.getDrawMasterList();

				// For Current Draw Now

				JSONArray currentDrawDataArray = new JSONArray();
				for (DrawMasterBean drawMasterBean : drawMasterList) {
					JSONObject currentDrawData = new JSONObject();

					currentDrawData.put("drawId", drawMasterBean.getDrawId());
					currentDrawData.put("drawNumber", drawMasterBean.getDrawNo());
					currentDrawData.put("drawDateTime",  Util.convertDateTimeToResponseFormat2(drawMasterBean.getDrawDateTime()));
					currentDrawData.put("ftg",  Util.convertDateTimeToResponseFormat2(drawMasterBean.getDrawFreezeTime()));
					currentDrawData.put("drawDisplayString", drawMasterBean
							.getDrawDisplayType());

					List<EventMasterBean> eventMasterList = drawMasterBean.getEventMasterList();

					JSONArray eventDataArray = new JSONArray();
					for (EventMasterBean eventMasterBean : eventMasterList) {
						JSONObject eventData = new JSONObject();
						eventData.put("eventId", eventMasterBean.getEventId());
						eventData.put("eventLeague", eventMasterBean.getLeagueName());
						eventData.put("eventVenue", eventMasterBean.getVenueName());
						eventData.put("eventDate",  Util.convertDateTimeToResponseFormat(eventMasterBean.getStartTime()));
						eventData.put("eventDisplayHome", eventMasterBean.getHomeTeamName());
						eventData.put("eventCodeHome", eventMasterBean.getHomeTeamCode());
						eventData.put("eventDisplayAway", eventMasterBean.getAwayTeamName());
						eventData.put("eventCodeAway", eventMasterBean.getAwayTeamCode());
						eventData.put("homeTeamOdds", eventMasterBean.getHomeTeamOdds()==null?"":eventMasterBean.getHomeTeamOdds());
						eventData.put("awayTeamOdds", eventMasterBean.getAwayTeamOdds()==null?"":eventMasterBean.getAwayTeamOdds());
						eventData.put("drawOdds", eventMasterBean.getDrawOdds()==null?"":eventMasterBean.getDrawOdds());
						eventData.put("favTeam", eventMasterBean.getFavTeam()==null?"":eventMasterBean.getFavTeam());

						eventDataArray.add(eventData);
					}
					currentDrawData.put("eventData", eventDataArray);
					currentDrawDataArray.add(currentDrawData);
				}
				gameTypeData.put("drawData", currentDrawDataArray);

				gameTypeArray.add(gameTypeData);
			}
			gameData.put("gameTypeData", gameTypeArray);
			gameDataArray.add(gameData);
		}
		finalGameData.put("gameData", gameDataArray);
		finalSportsLotteryData.put("sleData", finalGameData);
		return finalSportsLotteryData;
	}
	public static JSONObject generateSLEDrawData(List<GameMasterBean> gameMasterList, int responseCode, String responseMessage) throws Exception {
		JSONObject finalSportsLotteryData = new JSONObject();

		finalSportsLotteryData.put("responseMsg",responseMessage);
		finalSportsLotteryData.put("responseCode",responseCode);

		JSONObject finalGameData = new JSONObject();
		JSONArray gameDataArray = new JSONArray();
		for (GameMasterBean gameMasterBean : gameMasterList) {
			JSONObject gameData = new JSONObject();

			gameData.put("gameDisplayName", gameMasterBean.getGameDispName());
			
			List<GameTypeMasterBean> gameTypeMasterList = gameMasterBean.getGameTypeMasterList();

			JSONArray gameTypeArray = new JSONArray();
			for (GameTypeMasterBean gameTypeMasterBean : gameTypeMasterList) {

				JSONObject gameTypeData = new JSONObject();
				
				gameTypeData.put("gameTypeDisplayName",gameTypeMasterBean.getGameTypeDispName());
				List<DrawMasterBean> drawMasterList = gameTypeMasterBean.getDrawMasterList();

				// For Current Draw Now

				for (DrawMasterBean drawMasterBean : drawMasterList) {
					gameTypeData.put("drawId", drawMasterBean.getDrawId());
					gameTypeData.put("drawDisplayString", drawMasterBean.getDrawDisplayType());
					gameTypeData.put("drawDateTime",  Util.convertDateTimeToResponseFormat2(drawMasterBean.getDrawDateTime()));
					gameTypeData.put("drawFreezeTime", Util.convertDateTimeToResponseFormat2(drawMasterBean.getDrawFreezeTime()));
				}
				gameTypeArray.add(gameTypeData);
			}
			gameData.put("gameTypeData", gameTypeArray);
			gameDataArray.add(gameData);
		}
		finalGameData.put("gameData", gameDataArray);
		finalSportsLotteryData.put("sleData", finalGameData);
		return finalSportsLotteryData;
	}

	public static JSONObject generateSportsLotterySaleResponseData(SportsLotteryGamePlayBean gamePlayBeanResponse, String merchantDevName, double balance,List<String> eventOptionsList) throws SLEException {
		JSONObject finalPurchaseResponseData = new JSONObject();
		try {
			JSONObject tktData = new JSONObject();
			tktData.put("purchaseDate", Util.convertDateTimeToResponseFormat2(gamePlayBeanResponse.getPurchaseTime()).split(" ")[0]);
			tktData.put("purchaseTime", gamePlayBeanResponse.getPurchaseTime().split(" ")[1]);
			if("OKPOS".equalsIgnoreCase(merchantDevName)){
				tktData.put("ticketNo", String.valueOf(gamePlayBeanResponse.getTicketNumber())+String.valueOf(gamePlayBeanResponse.getReprintCount()));
				
			}else{
			tktData.put("ticketNo", gamePlayBeanResponse.getTicketNumber());
			}
			tktData.put("brCd",String.valueOf(gamePlayBeanResponse.getTicketNumber()) + String.valueOf(gamePlayBeanResponse.getReprintCount()) + String.valueOf(gamePlayBeanResponse.getBarcodeCount() > 0 ? gamePlayBeanResponse.getBarcodeCount() : ""));
			tktData.put("trxId", String.valueOf(gamePlayBeanResponse.getTransId()));
			NumberFormat numberFormat = NumberFormat.getInstance();
			numberFormat.setMinimumFractionDigits(2);
			String ticketAmt = numberFormat.format(gamePlayBeanResponse.getTotalPurchaseAmt()).replaceAll(",","");
			tktData.put("ticketAmt", ticketAmt);
			tktData.put("balance", Util.getCurrentAmountFormatForMobile(balance));
			tktData.put("gameId", gamePlayBeanResponse.getGameId());
			tktData.put("gameTypeId", gamePlayBeanResponse.getGameTypeId());
			tktData.put("gameName", SportsLotteryUtils.gameInfoMerchantMap.get(merchantDevName).get(gamePlayBeanResponse.getGameId()).getGameDispName());
			tktData.put("gameTypeName",SportsLotteryUtils.gameTypeInfoMerchantMap.get(merchantDevName).get(gamePlayBeanResponse.getGameTypeId()).getGameTypeDispName());
			tktData.put("eventType", eventOptionsList.toString());
			SportsLotteryGameDrawDataBean drawBean = null;
			JSONArray drawDataArray = new JSONArray();
			JSONObject drawData = null;
			for(int i=0; i<gamePlayBeanResponse.getNoOfBoard(); i++) {
				drawBean = gamePlayBeanResponse.getGameDrawDataBeanArray()[i];
				drawData = new JSONObject();
				drawData.put("drawId", drawBean.getDrawId());
				drawData.put("drawName", drawBean.getDrawDisplayname());
				drawData.put("drawDate", drawBean.getDrawDateTime());
				drawData.put("drawDate", Util.convertDateTimeToResponseFormat2(drawBean.getDrawDateTime()).split(" ")[0]);
				drawData.put("drawTime", drawBean.getDrawDateTime().split(" ")[1]);
				drawData.put("noOfLines", drawBean.getNoOfLines());
				drawData.put("boardPrice", drawBean.getBoardPurchaseAmount());
				drawData.put("unitPrice", gamePlayBeanResponse.getUnitPrice());
				SportsLotteryGameEventDataBean[] eventBeanArray = drawBean.getGameEventDataBeanArray();
				JSONArray eventDataArray = new JSONArray();
				JSONObject eventData = null;
				for(SportsLotteryGameEventDataBean eventBean : eventBeanArray) {
					eventData = new JSONObject();
					eventData.put("eventLeague", eventBean.getLeagueName());
					eventData.put("eventVenue", eventBean.getVenueName());
					eventData.put("eventDate", Util.convertDateTimeToResponseFormat2(eventBean.getEventDateTime()));
					eventData.put("eventDisplayHome", eventBean.getHomeTeamName());
					eventData.put("eventDisplayAway", eventBean.getAwayTeamName());
					eventData.put("eventCodeHome", eventBean.getHomeTeamCode());
					eventData.put("eventCodeAway", eventBean.getAwayTeamCode());
					
					String selection = "";
					for(String eventSelect : eventBean.getSelectedOption()) {
						selection += eventSelect+",";
					}
					selection = selection.substring(0, selection.length()-1);
					eventData.put("selection", selection);
					eventDataArray.add(eventData);
				}
				drawData.put("eventData", eventDataArray);
				drawDataArray.add(drawData);
			}
			tktData.put("boardData", drawDataArray);
			finalPurchaseResponseData.put("responseCode", 0);
			finalPurchaseResponseData.put("responseMsg", "Success");
			finalPurchaseResponseData.put("tktData", tktData);
		} catch (Exception e) {
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return finalPurchaseResponseData;
	}

	public static String generateSportsLotteryPwtResponseData(
			PwtVerifyTicketBean pwtVerifyTicketBean, String balance)
			throws SLEException {
		StringBuilder responseString = new StringBuilder("");
		/*
		 * winData:drawTime:2013-04-29
		 * 15:00:00,drawName:CONTINENTAL,No:1,message:CLAIMED,prizeAmt:0.0|
		 * drawTime:2013-04-29
		 * 15:00:00,drawName:CONTINENTAL,No:1,message:CLAIMED,prizeAmt:0.0|
		 * 
		 * totalPay:0.0| totalClmPend:1444.00| balance:0.0|
		 */
		try {
			responseString.append("winData:");
//			String time = Util.getCurrentTimeString()
//					.replace(" ", "|currTime:");
//			responseString.append("currDate:").append(time).append("|");
			responseString.append("ticketNo:").append(
					pwtVerifyTicketBean.getTicketNumber()).append("|");
			responseString.append("gameName:").append(
					pwtVerifyTicketBean.getGameName()).append("|");
			responseString.append("gameTypeName:").append(
					pwtVerifyTicketBean.getGameTypename()).append("|");

			for (int i = 0; i < pwtVerifyTicketBean
					.getVerifyTicketDrawDataBeanArray().length; i++) {
				PwtVerifyTicketDrawDataBean pwtVerifyDrawTicketBean = pwtVerifyTicketBean
						.getVerifyTicketDrawDataBeanArray()[i];
				responseString.append("drawTime:"
						+ pwtVerifyDrawTicketBean.getDrawDateTime() + ",");
				responseString.append("drawName:"
						+ pwtVerifyDrawTicketBean.getDrawName() + ",");
				responseString.append("No:"
						+ pwtVerifyDrawTicketBean.getBoardCount() + ",");
				responseString.append("message:"
						+ pwtVerifyDrawTicketBean.getMessage() + ",");
				responseString.append("prizeAmt:"
						+ pwtVerifyDrawTicketBean.getDrawWinAmt() + "|");

			}

			responseString.append("totalPay:"
					+ pwtVerifyTicketBean.getTotalWinAmt() + "|");
			responseString.append("totalClmPend:" + 0.0 + "|");
			responseString.append("balance:" + balance + "|");
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,
					SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return responseString.toString();
	}

	/*public static final String generateGameDataReportDate(String retailerName,
			Map<String, List<RetGameDataReportBean>> gameDataReportMap,
			String reportDate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		Date nowDate = new Date();
		String currentDate = dateFormat.format(nowDate);
		String currentTime = timeFormat.format(nowDate);

		NumberFormat numberFormat = NumberFormat.getInstance();
		numberFormat.setMinimumFractionDigits(2);

		double totalSaleAmt = 0;
		double totalPwtAmt = 0;

		StringBuilder responseDate = new StringBuilder("SlReportData:").append(
				"retName:").append(retailerName).append("|Date:").append(
				currentDate).append("|Time:").append(currentTime).append(
				"|ReportDate:").append(reportDate);
		for (String gameName : gameDataReportMap.keySet()) {
			responseDate.append("#gameName:").append(gameName);
			List<RetGameDataReportBean> gameDataReportList = gameDataReportMap
					.get(gameName);
			for (RetGameDataReportBean gameDataReportBean : gameDataReportList) {
				double saleAmt = gameDataReportBean.getSaleAmount();
				double pwtAmt = gameDataReportBean.getPwtAmount();
				totalSaleAmt += saleAmt;
				totalPwtAmt += pwtAmt;

				responseDate
						.append("$gameTypeName:")
						.append(gameDataReportBean.getGameTypeName())
						.append(",GameS:")
						.append(
								numberFormat.format(saleAmt)
										.replaceAll(",", ""))
						.append(",GamePwt:")
						.append(numberFormat.format(pwtAmt).replaceAll(",", ""));
			}
		}
		responseDate.append("|TotalSale:").append(
				numberFormat.format(totalSaleAmt).replaceAll(",", "")).append(
				"|TotalPWT:").append(
				numberFormat.format(totalPwtAmt).replaceAll(",", ""));

		return responseDate.toString();
	}*/

	public static String generateWinningResultReportData(String userName,
			List<WinningResultReportBean> winningResultReportList) {
		WinningResultReportBean winningResultReportBean = winningResultReportList
				.get(0);
		StringBuilder responseDate = new StringBuilder("gameName:").append(
				winningResultReportBean.getGameName()).append("|").append(
				"gameTypeName:").append(
				winningResultReportBean.getGameTypeName()).append("|").append(
				"drawDate:").append(winningResultReportBean.getDrawDate())
				.append("|").append("drawTime:").append(
						winningResultReportBean.getDrawTime()).append("|")
				.append("drawName:").append(
						winningResultReportBean.getDrawName()).append("|")
				.append("eventInfo:");
		Map<String, String> eventOptionMap = winningResultReportBean
				.getEventOptionMap();
		if (eventOptionMap.size() == 0) {
			responseDate.append("AWAITED");
		} else {
			Set<String> eventDisplaySet = eventOptionMap.keySet();
			for (String eventDisplay : eventDisplaySet) {
				String optionCode = eventOptionMap.get(eventDisplay);
				responseDate.append(eventDisplay).append("@")
						.append(optionCode).append("~");
			}
		}
		return responseDate.toString();
	}
	
	public static JSONObject generateDrawResultReportData(List<WinningResultReportBean> winningResultReportList) {
		
		
		
		JSONObject finaldrawResultResponseData = new JSONObject();
		JSONArray jsArr=new JSONArray();
		JSONObject js=null;
		for(int i=0;i<winningResultReportList.size();i++){
			WinningResultReportBean winningResultReportBean = winningResultReportList
					.get(i);
			js=new JSONObject();
			js.put("drawId", winningResultReportBean.getDrawId());
			js.put("drawName", winningResultReportBean.getDrawName());
			js.put("drawDate", winningResultReportBean.getDrawDate());
			js.put("drawTime", winningResultReportBean.getDrawTime());
			jsArr.add(js);
		}
		finaldrawResultResponseData.put("drawDataArr", jsArr);
		finaldrawResultResponseData.put("isSuccess", true);
		
		return finaldrawResultResponseData;
	}
	
	public static JSONObject getWinningResultDraw(WinningResultReportBean resultReportBean) {
		
		
		
		JSONObject finaldrawResultResponseData = new JSONObject();
		JSONObject js=null;
			
			js=new JSONObject();
			js.put("drawId", resultReportBean.getDrawId());
			js.put("drawName", resultReportBean.getDrawName());
			js.put("drawDate", resultReportBean.getDrawDate());
			js.put("drawTime", resultReportBean.getDrawTime());
			
			List<SportsLotteryGameEventDataBean> drawEventList = resultReportBean.getDrawEventList();

			JSONArray eventDataArray = new JSONArray();
			for (SportsLotteryGameEventDataBean eventMasterBean : drawEventList) {
				JSONObject eventData = new JSONObject();
				eventData.put("eventDescription", eventMasterBean.getEventDescription());
				//eventData.put("eventDisplayString", eventMasterBean.getEventDisplay());
				eventData.put("selection", eventMasterBean.getWinningEvent());

				/*
				 * List<String> eventOptionsList =
				 * eventMasterBean.getEventOptionsList();
				 * responseString.append(eventOptionsList);
				 */
				eventDataArray.add(eventData);
			}
			js.put("eventData", eventDataArray);
			
			if(drawEventList.size() == 0){
				finaldrawResultResponseData.put("message", "Result Awaited");
				finaldrawResultResponseData.put("responseCode",2);
			}else{
				finaldrawResultResponseData.put("responseCode",0);
			}
			
		finaldrawResultResponseData.put("winningData", js);
		finaldrawResultResponseData.put("isSuccess", true);
		
		return finaldrawResultResponseData;
	}
	
	public static JSONObject prepareSLEMatchListData(List<GameMasterBean> gameMasterList) throws Exception {
		
		JSONObject finalSportsLotteryData = new JSONObject();
/*
		String format1 = Util.convertDateTimeLocalToDB(Util.getCurrentTimeString());
		SimpleDateFormat format2 = new SimpleDateFormat("E dd MMM yyyy hh:mm a");*/
		finalSportsLotteryData.put("responseMsg", "success");
		finalSportsLotteryData.put("responseCode",0);

		JSONObject finalGameData = new JSONObject();
		JSONArray gameDataArray = new JSONArray();
		for (GameMasterBean gameMasterBean : gameMasterList) {
			
			JSONObject gameData = new JSONObject();
			gameData.put("gameId", gameMasterBean.getGameId());
			gameData.put("gameDevname", gameMasterBean.getGameDevName());
			gameData.put("gameDisplayName", gameMasterBean.getGameDispName());
			

			List<GameTypeMasterBean> gameTypeMasterList = gameMasterBean.getGameTypeMasterList();

			JSONArray gameTypeArray = new JSONArray();
			for (GameTypeMasterBean gameTypeMasterBean : gameTypeMasterList) {
				
				JSONObject gameTypeData = new JSONObject();
				String eventType = gameTypeMasterBean.getEventType();
				String eventOptionsList = null;
				if ("SIMPLE".equals(eventType)) {
					List<DrawMasterBean> drawMasterList = gameTypeMasterBean.getDrawMasterList();
					if (drawMasterList != null && drawMasterList.size() > 0) {
						eventOptionsList = drawMasterList.get(0).getEventMasterList().get(0).getEventOptionsList().toString();
					} else {
						eventOptionsList = "";
					}
				}
				gameTypeData.put("gameTypeId", gameTypeMasterBean.getGameTypeId());
				gameTypeData.put("gameTypeDevName", gameTypeMasterBean.getGameTypeDevName());
				gameTypeData.put("gameTypeDisplayName",	gameTypeMasterBean.getGameTypeDispName());
				gameTypeData.put("eventType", eventOptionsList);
				gameTypeData.put("upcomingDrawStartTime", gameTypeMasterBean.getUpcomingDrawStartTime());
				gameTypeData.put("areEventsMappedForUpcomingDraw",gameTypeMasterBean.isAreEventsMappedForUpcomingDraw());

				List<DrawMasterBean> drawMasterList = gameTypeMasterBean.getDrawMasterList();

				// For Current Draw Now

				JSONArray currentDrawDataArray = new JSONArray();
				for (DrawMasterBean drawMasterBean : drawMasterList) {
					JSONObject currentDrawData = new JSONObject();

					currentDrawData.put("drawId", drawMasterBean.getDrawId());
					currentDrawData.put("drawNumber", drawMasterBean.getDrawNo());
					currentDrawData.put("drawDateTime",  Util.convertDateTimeToResponseFormat2(drawMasterBean.getDrawDateTime()));
					currentDrawData.put("drawDisplayString", drawMasterBean.getDrawDisplayType());

					List<EventMasterBean> eventMasterList = drawMasterBean.getEventMasterList();

					JSONArray eventDataArray = new JSONArray();
					for (EventMasterBean eventMasterBean : eventMasterList) {
						JSONObject eventData = new JSONObject();
						eventData.put("eventId", eventMasterBean.getEventId());
						eventData.put("eventDisplay",eventMasterBean.getEventDisplay());
						eventData.put("eventDiscription",eventMasterBean.getEventDescription());
						eventData.put("eventLeague", eventMasterBean.getLeagueName());
						eventData.put("eventVenue", eventMasterBean.getVenueName());
						eventData.put("startTime",Util.convertDateTimeToResponseFormat(eventMasterBean.getStartTime()));
						eventData.put("endTime",Util.convertDateTimeToResponseFormat(eventMasterBean.getEndTime()));
						eventData.put("eventDisplayHome", eventMasterBean.getHomeTeamName());
						eventData.put("eventCodeHome", eventMasterBean.getHomeTeamCode());
						eventData.put("eventDisplayAway", eventMasterBean.getAwayTeamName());
						eventData.put("eventCodeAway", eventMasterBean.getAwayTeamCode());
						eventData.put("homeTeamOdds", eventMasterBean.getHomeTeamOdds()==null?"":eventMasterBean.getHomeTeamOdds());
						eventData.put("awayTeamOdds", eventMasterBean.getAwayTeamOdds()==null?"":eventMasterBean.getAwayTeamOdds());
						eventData.put("drawOdds", eventMasterBean.getDrawOdds()==null?"":eventMasterBean.getDrawOdds());
						eventData.put("favTeam", eventMasterBean.getFavTeam()==null?"":eventMasterBean.getFavTeam());
						

						
						eventDataArray.add(eventData);
					}
					currentDrawData.put("eventData", eventDataArray);
					currentDrawDataArray.add(currentDrawData);
				}
				gameTypeData.put("drawData", currentDrawDataArray);
				gameTypeArray.add(gameTypeData);
			}
			gameData.put("gameTypeData", gameTypeArray);
			gameDataArray.add(gameData);
		}
		finalGameData.put("gameData", gameDataArray);
		finalSportsLotteryData.put("matchListData", finalGameData);
		return finalSportsLotteryData;
	}
	
public static JSONObject prepareSLEMatchListDataDayWise(Map<Integer, List<EventMasterBean>> matchDataDayWise, int merchantId) throws Exception {
		
		JSONObject finalSportsLotteryData = new JSONObject();
		
		finalSportsLotteryData.put("isSuccess", true);
		finalSportsLotteryData.put("errorMsg", "");
		
		JSONArray matchData = new JSONArray();
		for(Map.Entry<Integer, List<EventMasterBean>> entry : matchDataDayWise.entrySet()){
			JSONObject gameWiseData = new JSONObject();
			
			gameWiseData.put("gameName", CommonMethodsServiceImpl.getInstance().getGameNameFromGameId(entry.getKey(), merchantId));
			
			JSONArray eventDataArray = new JSONArray();
			for (EventMasterBean eventMasterBean : entry.getValue()) {
				
				JSONObject eventData = new JSONObject();
				eventData.put("eventDisplayName", eventMasterBean.getEventDisplay());
				eventData.put("eventDescription", eventMasterBean.getEventDescription());
				eventData.put("leagueName", eventMasterBean.getLeagueName());
				eventData.put("homeTeam", eventMasterBean.getHomeTeamName());
				eventData.put("awayTeam", eventMasterBean.getAwayTeamName());
				eventData.put("venueName", eventMasterBean.getVenueName());
				eventData.put("startTime", Util.convertDateTimeToResponseFormat(eventMasterBean.getStartTime()));
				eventData.put("endTime",Util.convertDateTimeToResponseFormat(eventMasterBean.getEndTime()));
				eventDataArray.add(eventData);
			}
			gameWiseData.put("eventData",eventDataArray);
			matchData.add(gameWiseData);
		}
		finalSportsLotteryData.put("matchDataDayWise",matchData);
		return finalSportsLotteryData;
	}

	public static JSONObject prepareTrackTktData(Map<Long, TicketInfoBean> tktInfoMap) {
		JSONObject finalResponse = new JSONObject();
		JSONArray tktDataArray = new JSONArray();
		JSONObject tmpObj = null;
		TicketInfoBean tktInfoBean = null;

		finalResponse.put("isSuccess", true);
		finalResponse.put("errorMsg", "");

		for(Entry<Long, TicketInfoBean> entryset : tktInfoMap.entrySet()) {
			tmpObj = new JSONObject();
			tktInfoBean = entryset.getValue();
			tmpObj.put("txnId", entryset.getKey());
			tmpObj.put("ticketNumber", tktInfoBean.getTktNbr());
			tmpObj.put("ticketAmount", tktInfoBean.getAmount());
			tmpObj.put("ticketDate",Util.convertDateTimeToResponseFormat(tktInfoBean.getTxnDate()));
			tmpObj.put("ticketPwt", tktInfoBean.getStatus());
			tktDataArray.add(tmpObj);
		}
		finalResponse.put("ticketData", tktDataArray);
		return finalResponse;
	}
	
	
	public static JSONObject prepareSLEResultData(List<GameMasterBean> gameMasterList) throws Exception {
		
		JSONObject finalSportsLotteryData = new JSONObject();

		/*SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat format2 = new SimpleDateFormat("E dd MMM yyyy hh:mm a");*/
		finalSportsLotteryData.put("responseMsg", "success");
		finalSportsLotteryData.put("responseCode",0);

		JSONObject finalGameData = new JSONObject();
		JSONArray gameDataArray = new JSONArray();
		for (GameMasterBean gameMasterBean : gameMasterList) {
			
			JSONObject gameData = new JSONObject();
			gameData.put("gameId", gameMasterBean.getGameId());
			gameData.put("gameDevname", gameMasterBean.getGameDevName());
			gameData.put("gameDisplayName", gameMasterBean.getGameDispName());

			List<GameTypeMasterBean> gameTypeMasterList = gameMasterBean.getGameTypeMasterList();

			JSONArray gameTypeArray = new JSONArray();
			for (GameTypeMasterBean gameTypeMasterBean : gameTypeMasterList) {
				
				JSONObject gameTypeData = new JSONObject();
				String eventType = gameTypeMasterBean.getEventType();
				String eventOptionsList = null;
				if ("SIMPLE".equals(eventType)) {
					List<DrawMasterBean> drawMasterList = gameTypeMasterBean.getDrawMasterList();
					if (drawMasterList != null && drawMasterList.size() > 0) {
						eventOptionsList = drawMasterList.get(0).getEventMasterList().get(0).getEventOptionsList().toString();
					} else {
						eventOptionsList = "";
					}
				}
				gameTypeData.put("gameTypeId", gameTypeMasterBean.getGameTypeId());
				gameTypeData.put("gameTypeDevName", gameTypeMasterBean.getGameTypeDevName());
				gameTypeData.put("gameTypeDisplayName",	gameTypeMasterBean.getGameTypeDispName());
				gameTypeData.put("eventType", eventOptionsList);

				List<DrawMasterBean> drawMasterList = gameTypeMasterBean.getDrawMasterList();

				// For Current Draw Now
				JSONArray currentDrawDataArray = new JSONArray();
				for (DrawMasterBean drawMasterBean : drawMasterList) {
					JSONObject currentDrawData = new JSONObject();

					currentDrawData.put("drawId", drawMasterBean.getDrawId());
					currentDrawData.put("drawNumber", drawMasterBean.getDrawNo());
					currentDrawData.put("drawDateTime", Util.convertDateTimeToResponseFormat2(drawMasterBean.getDrawDateTime()));
					currentDrawData.put("drawDisplayString", drawMasterBean.getDrawDisplayType());

					List<EventMasterBean> eventMasterList = drawMasterBean.getEventMasterList();

					JSONArray eventDataArray = new JSONArray();
					for (EventMasterBean eventMasterBean : eventMasterList) {
						JSONObject eventData = new JSONObject();
						eventData.put("eventId", eventMasterBean.getEventId());
						eventData.put("eventDisplay",eventMasterBean.getEventDisplay());
						eventData.put("eventDiscription",eventMasterBean.getEventDescription());
						eventData.put("eventLeague", eventMasterBean.getLeagueName());
						eventData.put("eventVenue", eventMasterBean.getVenueName());
						eventData.put("startTime", Util.convertDateTimeToResponseFormat(eventMasterBean.getStartTime()));
						eventData.put("endTime",  Util.convertDateTimeToResponseFormat(eventMasterBean.getEndTime()));
						eventData.put("eventDisplayHome", eventMasterBean.getHomeTeamName());
						eventData.put("eventCodeHome", eventMasterBean.getHomeTeamCode());
						eventData.put("eventDisplayAway", eventMasterBean.getAwayTeamName());
						eventData.put("eventCodeAway", eventMasterBean.getAwayTeamCode());
						eventData.put("winningOption", eventMasterBean.getWinningOption());

						
						eventDataArray.add(eventData);
					}
					currentDrawData.put("eventData", eventDataArray);
					
					List<SLEDrawWinningBean> drawWinningList = drawMasterBean.getDrawWinningDetail();
					JSONArray WinningDataArray = new JSONArray();
					for (SLEDrawWinningBean drawWinningBean : drawWinningList) {
						JSONObject winningData = new JSONObject();
						winningData.put("noOfMatches", drawWinningBean.getNoOfMatches());
						winningData.put("noOfWinners",drawWinningBean.getNoOfWInniners());
						winningData.put("prizeAmount",drawWinningBean.getPrizeAmount());
		
						WinningDataArray.add(winningData);
					}
					
					currentDrawData.put("winningData", WinningDataArray);
					currentDrawDataArray.add(currentDrawData);
				}
				gameTypeData.put("drawData", currentDrawDataArray);
				gameTypeArray.add(gameTypeData);
			}
			gameData.put("gameTypeData", gameTypeArray);
			gameDataArray.add(gameData);
		}
		finalGameData.put("gameData", gameDataArray);
		finalSportsLotteryData.put("drawResultData", finalGameData);
		return finalSportsLotteryData;
	}
	
	
public static JSONObject prepareTxnStatusData(List<TicketTxnStatusBean> txnStatusList) throws Exception {
		
		JSONObject finalSportsLotteryData = new JSONObject();

		finalSportsLotteryData.put("responseMsg", "success");
		finalSportsLotteryData.put("responseCode",0);
		
		JSONArray txnDataArray = new JSONArray();
		for (TicketTxnStatusBean ticketTxnStatusBean : txnStatusList) {
			JSONObject txnData = new JSONObject();
			txnData.put("txnId", ticketTxnStatusBean.getMerchantTxnId());
			txnData.put("winStatus",ticketTxnStatusBean.getWinStatus());
			txnData.put("winAmt",ticketTxnStatusBean.getWinAmt());
			
			txnDataArray.add(txnData);
		}
		finalSportsLotteryData.put("txnStatus", txnDataArray);
		return finalSportsLotteryData;
	}


public static JSONObject generateSportsLotteryTrackTicketResponseData(SportsLotteryGamePlayBean gamePlayBeanResponse, String merchantDevName, double balance,List<String> eventOptionsList) throws SLEException {
	JSONObject finalPurchaseResponseData = new JSONObject();
	try {
		JSONObject tktData = new JSONObject();
		tktData.put("purchaseDate", Util.convertDateTimeToResponseFormat2(gamePlayBeanResponse.getPurchaseTime()).split(" ")[0]);
		tktData.put("purchaseTime", Util.convertDateTimeToResponseFormat2(gamePlayBeanResponse.getPurchaseTime()).split(" ")[1]);
		tktData.put("ticketNo", gamePlayBeanResponse.getTicketNumber() + gamePlayBeanResponse.getReprintCount());
		tktData.put("brCd",String.valueOf(gamePlayBeanResponse.getTicketNumber()) + String.valueOf(gamePlayBeanResponse.getReprintCount()) + String.valueOf(gamePlayBeanResponse.getBarcodeCount() > 0 ? gamePlayBeanResponse.getBarcodeCount() : ""));
		tktData.put("trxId", String.valueOf(gamePlayBeanResponse.getTransId()));
		NumberFormat numberFormat = NumberFormat.getInstance();
		numberFormat.setMinimumFractionDigits(2);
		String ticketAmt = numberFormat.format(gamePlayBeanResponse.getTotalPurchaseAmt()).replaceAll(",","");
		tktData.put("ticketAmt", ticketAmt);
		tktData.put("balance", Util.getCurrentAmountFormatForMobile(balance));
		tktData.put("gameId", gamePlayBeanResponse.getGameId());
		tktData.put("gameTypeId", gamePlayBeanResponse.getGameTypeId());
		tktData.put("gameName", SportsLotteryUtils.gameInfoMerchantMap.get(merchantDevName).get(gamePlayBeanResponse.getGameId()).getGameDispName());
		tktData.put("gameTypeName",SportsLotteryUtils.gameTypeInfoMerchantMap.get(merchantDevName).get(gamePlayBeanResponse.getGameTypeId()).getGameTypeDispName());
		tktData.put("eventType", eventOptionsList.toString());
		SportsLotteryGameDrawDataBean drawBean = null;
		JSONArray drawDataArray = new JSONArray();
		JSONObject drawData = null;
		for(int i=0; i<gamePlayBeanResponse.getNoOfBoard(); i++) {
			drawBean = gamePlayBeanResponse.getGameDrawDataBeanArray()[i];
			drawData = new JSONObject();
			drawData.put("drawId", drawBean.getDrawId());
			drawData.put("drawName", drawBean.getDrawDisplayname());
			drawData.put("drawDate", Util.convertDateTimeToResponseFormat2(drawBean.getDrawDateTime()));
			drawData.put("drawDate",  Util.convertDateTimeToResponseFormat2(drawBean.getDrawDateTime()).split(" ")[0]);
			drawData.put("drawTime",Util.convertDateTimeToResponseFormat2(drawBean.getDrawDateTime()).split(" ")[1]);
			drawData.put("noOfLines", drawBean.getNoOfLines());
			drawData.put("boardPrice", drawBean.getBoardPurchaseAmount());
			drawData.put("unitPrice", gamePlayBeanResponse.getUnitPrice());
			drawData.put("winAmt", gamePlayBeanResponse.getWinAmt());
			drawData.put("winStatus", gamePlayBeanResponse.getWinStatus());
			SportsLotteryGameEventDataBean[] eventBeanArray = drawBean.getGameEventDataBeanArray();
			JSONArray eventDataArray = new JSONArray();
			JSONObject eventData = null;
			for(SportsLotteryGameEventDataBean eventBean : eventBeanArray) {
				eventData = new JSONObject();
				eventData.put("eventLeague", eventBean.getLeagueName());
				eventData.put("eventVenue", eventBean.getVenueName());
				eventData.put("eventDate", Util.convertDateTimeToResponseFormat2(eventBean.getEventDateTime()));
				eventData.put("eventDisplayHome", eventBean.getHomeTeamName());
				eventData.put("eventDisplayAway", eventBean.getAwayTeamName());
				eventData.put("eventCodeHome", eventBean.getHomeTeamCode());
				eventData.put("eventCodeAway", eventBean.getAwayTeamCode());
				
				String selection = "";
				for(String eventSelect : eventBean.getSelectedOption()) {
					selection += eventSelect+",";
				}
				selection = selection.substring(0, selection.length()-1);
				eventData.put("selection", selection);
				eventDataArray.add(eventData);
			}
			drawData.put("eventData", eventDataArray);
			drawDataArray.add(drawData);
		}
		tktData.put("boardData", drawDataArray);
		finalPurchaseResponseData.put("responseCode", 0);
		finalPurchaseResponseData.put("responseMsg", "Success");
		finalPurchaseResponseData.put("tktData", tktData);
	} catch (Exception e) {
		throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
	}
	return finalPurchaseResponseData;
}


}