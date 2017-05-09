package com.skilrock.sle.web.merchantUser.common;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.skilrock.sle.common.SportsLotteryUtils;
import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.commonMethod.serviceImpl.CommonMethodsServiceImpl;
import com.skilrock.sle.gameDataMgmt.javaBeans.DrawMasterBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.EventMasterBean;
import com.skilrock.sle.gamePlayMgmt.javaBeans.PrizeRankDrawWinningBean;
import com.skilrock.sle.gamePlayMgmt.javaBeans.SportsLotteryGameDrawDataBean;
import com.skilrock.sle.gamePlayMgmt.javaBeans.SportsLotteryGameEventDataBean;
import com.skilrock.sle.gamePlayMgmt.javaBeans.SportsLotteryGamePlayBean;
import com.skilrock.sle.pwtMgmt.javaBeans.PwtVerifyTicketBean;
import com.skilrock.sle.pwtMgmt.javaBeans.PwtVerifyTicketDrawDataBean;

public class SportsLotteryWebResponseData {
	
	public static final JSONObject  generateSLEVerifyTicketResponseData(PwtVerifyTicketBean verifyTicketBean, boolean claimStatus, String statusMsg) throws SLEException {
		JSONObject winData = new JSONObject();
		try{
			 DecimalFormat df = new DecimalFormat("###0.00");
		        
		        String currentTime = Util.getCurrentTimeString().replace(" ", "|currTime:");
		       
		        winData.put("responseCode",0);
		        winData.put("responseMsg","SUCCESS");
		        winData.put("currDate",(currentTime));
		        winData.put("ticketNo",verifyTicketBean.getTicketNumber());
		        winData.put("gameName",verifyTicketBean.getGameName());
		        winData.put("gameTypeName",verifyTicketBean.getGameTypename());

		       // for(int i=0;i<verifyTicketBean.getVerifyTicketDrawDataBeanArray().length;i++){
		            PwtVerifyTicketDrawDataBean pwtVerifyDrawTicketBean=verifyTicketBean.getVerifyTicketDrawDataBeanArray()[0];
		            winData.put("drawTime",pwtVerifyDrawTicketBean.getDrawDateTime());
		            winData.put("drawName",pwtVerifyDrawTicketBean.getDrawName());
		            winData.put("boardCount",pwtVerifyDrawTicketBean.getBoardCount());
		            if(statusMsg != null){
		                winData.put("message",statusMsg);
		            }else{
		                winData.put("message",pwtVerifyDrawTicketBean.getMessage());
		            }

		             winData.put("prizeAmt",df.format(pwtVerifyDrawTicketBean.getDrawWinAmt()));
		        //}
		        winData.put("totalPay",df.format(verifyTicketBean.getTotalWinAmt())+" "+Util.getPropertyValue("CURRENCY_SYMBOL"));
		        winData.put("totalClmPend",0.0);
		        winData.put("claimStatus",claimStatus);
		}catch(Exception e){
			 e.printStackTrace();
	         throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		
        return winData;
     }
	
	 public static final JSONObject generateSLEWinningTicketResponseData(PwtVerifyTicketBean verifyTicketBean, double balance,String userName) throws SLEException {
		 JSONObject winData = new JSONObject();
	        DecimalFormat df = new DecimalFormat("###0.00");
	        try {
	            String balanceString = new DecimalFormat("#.00").format(balance);
                winData.put("retailerName", userName);
	            winData.put("ticketNo",verifyTicketBean.getTicketNumber());
	            winData.put("gameName",verifyTicketBean.getGameName());
	            winData.put("gameTypeName",verifyTicketBean.getGameTypename());

	            //for(int i=0;i<verifyTicketBean.getVerifyTicketDrawDataBeanArray().length;i++){
	                PwtVerifyTicketDrawDataBean pwtVerifyDrawTicketBean=verifyTicketBean.getVerifyTicketDrawDataBeanArray()[0];
	                winData.put("drawTime",pwtVerifyDrawTicketBean.getDrawDateTime());
	                winData.put("drawName",pwtVerifyDrawTicketBean.getDrawName());
	                winData.put("boardCount",pwtVerifyDrawTicketBean.getBoardCount());
	                winData.put("message",pwtVerifyDrawTicketBean.getMessage());
	                winData.put("totalWinAmt",df.format(pwtVerifyDrawTicketBean.getDrawWinAmt()));
	            //}

	                winData.put("totalPay",df.format(verifyTicketBean.getTotalWinAmt()));
	                winData.put("claimPndAmt",0.0);
	                winData.put("balance",balanceString);
	                winData.put("mode", "PWT");
	                winData.put("currSymbol",Util.getPropertyValue("CURRENCY_SYMBOL"));
	                winData.put("reprintCountPWT",0);
	                winData.put("orgName",Util.getPropertyValue("ORG_NAME"));
	                winData.put("pwtDateTime",Util.getCurrentTimeString());
	                winData.put("responseCode",0);
	                winData.put("responseMsg","SUCCESS");
	        }catch (Exception e) {
	            e.printStackTrace();
	            throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
	        }
	        return winData;
	    }
	 
	 
	 public static JSONObject generateSportsLotterySaleResponseData(SportsLotteryGamePlayBean gamePlayBeanResponse, String merchantDevName, double balance,List<String> eventOptionsList,String userName) throws SLEException {
			JSONObject finalPurchaseResponseData = new JSONObject();
			  StringBuilder jackpotMsg = null;
		        Map<Integer, PrizeRankDrawWinningBean> prizeDistributionMap = null;
		        NumberFormat nf = NumberFormat.getInstance();
		        DecimalFormat df = new DecimalFormat("###0.00");
		        nf.setMinimumFractionDigits(2);
			try {
				JSONObject tktData = new JSONObject();
				tktData.put("purchaseDate", gamePlayBeanResponse.getPurchaseTime().split(" ")[0]);
				tktData.put("reprintCount", gamePlayBeanResponse.getReprintCount());
				tktData.put("retailerName", userName);
				tktData.put("currSymbol", Util.getPropertyValue("CURRENCY_SYMBOL"));
				tktData.put("orgName", Util.getPropertyValue("ORG_NAME"));
				tktData.put("expiryPeriod","");
				
				tktData.put("purchaseTime", gamePlayBeanResponse.getPurchaseTime().split(" ")[1]);
				tktData.put("ticketNo", String.valueOf(gamePlayBeanResponse.getTicketNumber()) + gamePlayBeanResponse.getReprintCount());
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
				if(eventOptionsList != null){
					tktData.put("eventType", eventOptionsList.toString());
				}else{
					eventOptionsList=CommonMethodsServiceImpl.getInstance().fetchGameTypeOptionList(gamePlayBeanResponse.getGameId(), gamePlayBeanResponse.getGameTypeId());
					tktData.put("eventType", eventOptionsList.toString());
				}
				SportsLotteryGameDrawDataBean drawBean = null;
				JSONArray drawDataArray = new JSONArray();
				JSONObject drawData = null;
				for(int i=0; i<gamePlayBeanResponse.getNoOfBoard(); i++) {
					drawBean = gamePlayBeanResponse.getGameDrawDataBeanArray()[i];
					 jackpotMsg = new StringBuilder("");
                    Map<Integer, PrizeRankDrawWinningBean> rankDistributionmap = drawBean
                             .getDrawPrizeRankMap();
                     prizeDistributionMap = new TreeMap<Integer, PrizeRankDrawWinningBean>();
                     prizeDistributionMap.putAll(rankDistributionmap);
                     if (prizeDistributionMap != null && prizeDistributionMap.size() > 0) {
                        
                     	/*currenty only print in INGENICO TERMINALS*/
                     	String gameTypeDevName = SportsLotteryUtils.gameTypeInfoMerchantMap.get("RMS").get(gamePlayBeanResponse.getGameTypeId()).getGameTypeDevName();
                     	if("soccer13".equals(gameTypeDevName)) {
                     		jackpotMsg.append("Current Jackpot Fund@(All Prizes Parimutuel)@13 out Of 13 Match@");
                             for (Map.Entry<Integer, PrizeRankDrawWinningBean> entry : prizeDistributionMap.entrySet()) {
                                 String prizeVal = df.format(entry.getValue().getPrizeValue()).replaceAll(",", "");
                                 if(entry.getValue().getRankName().equalsIgnoreCase("Match 13")) {
                                     jackpotMsg.append(prizeVal).append(" USD").append("~");
                                 }      
                             }
                     	} else if("soccer10".equals(gameTypeDevName)) {
                     		jackpotMsg.append("Current Jackpot Fund@(All Prizes Parimutuel)@10 out Of 10 Match@");
                              for (Map.Entry<Integer, PrizeRankDrawWinningBean> entry : prizeDistributionMap.entrySet()) {
                                  String prizeVal = df.format(entry.getValue().getPrizeValue()).replaceAll(",", "");
                                  if(entry.getValue().getRankName().equalsIgnoreCase("Match 10")) {
                                      jackpotMsg.append(prizeVal).append(" USD").append("~");
                                  }      
                              }
                     	} else{
                     		jackpotMsg.append("");
                     	}
                     	if(jackpotMsg.length() > 0){
                     		jackpotMsg.deleteCharAt(jackpotMsg.length() - 1);
                     	}
                     }
					drawData = new JSONObject();
					drawData.put("drawId", drawBean.getDrawId());
					drawData.put("drawName", drawBean.getDrawDisplayname());
					drawData.put("drawDate", drawBean.getDrawDateTime());
					drawData.put("drawDate", drawBean.getDrawDateTime().split(" ")[0]);
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
						eventData.put("eventDate", eventBean.getEventDateTime());
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
				tktData.put("jackpotMsg", jackpotMsg.toString());
				finalPurchaseResponseData.put("responseCode", 0);
				finalPurchaseResponseData.put("responseMsg", "Success");
				finalPurchaseResponseData.put("tktData", tktData);
				finalPurchaseResponseData.put("mode", "Buy");
				finalPurchaseResponseData.put("sampleStatus", "NO");
				
				JSONObject advData = new JSONObject();
				if(gamePlayBeanResponse.getAdvMessageMap() !=null && gamePlayBeanResponse.getAdvMessageMap().get("TOP") !=null && gamePlayBeanResponse.getAdvMessageMap().get("BOTTOM")!=null){
	                advData.put("TOP", gamePlayBeanResponse.getAdvMessageMap().get("TOP"));
	                advData.put("BOTTOM", gamePlayBeanResponse.getAdvMessageMap().get("BOTTOM"));
				}
				finalPurchaseResponseData.put("advMessage", advData);
			} catch (Exception e) {
				throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			}
			return finalPurchaseResponseData;
		}

	 public static JSONObject generateMatchListDataDrawWiseWeb(List<DrawMasterBean> drawMasterList) throws SLEException{
		JSONObject drawData = new JSONObject();
		JSONArray currentDrawDataArray = new JSONArray();
		 try{
				for (DrawMasterBean drawMasterBean : drawMasterList) {
					JSONObject currentDrawData = new JSONObject();

					currentDrawData.put("drawId", drawMasterBean.getDrawId());
					currentDrawData.put("drawDateTime",  Util.convertDateTimeToResponseFormat(drawMasterBean.getDrawDateTime()));
					currentDrawData.put("drawDisplayString", drawMasterBean.getDrawDisplayType());
					currentDrawData.put("drawName", drawMasterBean.getDrawName());
					currentDrawData.put("gameDispName","Soccer");
					currentDrawData.put("gameTypeDispName", SportsLotteryUtils.gameTypeInfoMerchantMap.get("RMS").get(drawMasterBean.getGameTypeId()).getGameTypeDispName());
					currentDrawData.put("orgName", Util.getPropertyValue("ORG_NAME"));
					currentDrawData.put("mode", "MATCH_LIST");
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

						eventDataArray.add(eventData);
					}
					currentDrawData.put("eventData", eventDataArray);
					currentDrawDataArray.add(currentDrawData);
				}
				drawData.put("drawData", currentDrawDataArray);
		 }catch (Exception e) {
				throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			}
		 return drawData;

	 }

}
