package com.skilrock.sle.embedded.common;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.skilrock.sle.common.SportsLotteryUtils;
import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.UtilityFunctions;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.commonMethod.daoImpl.CommonMethodsDaoImpl;
import com.skilrock.sle.commonMethod.serviceImpl.CommonMethodsServiceImpl;
import com.skilrock.sle.embedded.playMgmt.common.javaBeans.CancelTicketBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.DrawMasterBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.EventMasterBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.GameMasterBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.GameTypeMasterBean;
import com.skilrock.sle.gamePlayMgmt.javaBeans.PrizeRankDrawWinningBean;
import com.skilrock.sle.gamePlayMgmt.javaBeans.SportsLotteryGameDrawDataBean;
import com.skilrock.sle.gamePlayMgmt.javaBeans.SportsLotteryGameEventDataBean;
import com.skilrock.sle.gamePlayMgmt.javaBeans.SportsLotteryGamePlayBean;
import com.skilrock.sle.pwtMgmt.javaBeans.PwtVerifyTicketBean;
import com.skilrock.sle.pwtMgmt.javaBeans.PwtVerifyTicketDrawDataBean;
import com.skilrock.sle.roleMgmt.controllerImpl.GetPriviledgeControllerImpl;

public class SportsLotteryResponseData {

   public static String generateDrawGameData(List<GameMasterBean> gameMasterList,int merchantUserId,int merchantId) throws SLEException {
    	  StringBuilder responseString = new StringBuilder("");
    	try{
  
    		responseString.append("sportsData:SLPriv:");

            responseString.append(new GetPriviledgeControllerImpl().fetchRetailerPrivList(merchantUserId,merchantId)).append("|");
           
            for (GameMasterBean gameMasterBean : gameMasterList) {
                responseString.append(gameMasterBean.getGameId()).append(",")
                        .append(gameMasterBean.getGameDevName()).append(",")
                        .append(gameMasterBean.getGameDispName()).append(",")
                        .append(gameMasterBean.getThersholdTickerAmt()).append(",")
                        .append(gameMasterBean.getMaxTicketAmt()).append(",")
                        .append(gameMasterBean.getMinBoardCount()).append(",")
                        .append(gameMasterBean.getMaxBoardCount());
                List<GameTypeMasterBean> gameTypeMasterList = gameMasterBean
                        .getGameTypeMasterList();
                for (GameTypeMasterBean gameTypeMasterBean : gameTypeMasterList) {
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
                        eventOptionsList = CommonMethodsServiceImpl.getInstance().fetchGameTypeOptionList(gameMasterBean.getGameId(), gameTypeMasterBean.getGameTypeId()).toString();
                    }
                }
                    responseString.append("$")
                            .append(gameTypeMasterBean.getGameTypeId()).append(",")
                            .append(gameTypeMasterBean.getGameTypeDevName())
                            .append(",")
                            .append(gameTypeMasterBean.getGameTypeDispName())
                            .append(",").append(gameTypeMasterBean.getUnitPrice())
                            .append(",")
                            .append(gameTypeMasterBean.getMaxBetAmtMultiple())
                            .append(",")
                    		.append(gameTypeMasterBean.getEventSelectionType())
                    		.append(",")
                    		.append(CommonMethodsDaoImpl.getInstance().getUpcomingDrawDetailForTerminal(gameMasterBean.getGameId(),gameTypeMasterBean.getGameTypeId()))
                            .append(",")
                            .append(eventOptionsList);
                    // .append("&CD:");
                    List<DrawMasterBean> drawMasterList = gameTypeMasterBean
                            .getDrawMasterList();
                    if (drawMasterList.size() > 0) {
                        int counter=0;
                        for (DrawMasterBean drawMasterBean : drawMasterList) {
                        	responseString.append(counter >0 ?"&AD:":"&CD:");
                        	counter++;
                            responseString.append(drawMasterBean.getDrawId())
                                    .append(",").append(drawMasterBean.getDrawNo())
                                    .append(",")
                                    .append(drawMasterBean.getDrawDateTime())
                                    .append(",")
                                     .append(drawMasterBean.getDrawFreezeTime()).append(",")
                                   // .append("50,")
                                    .append(drawMasterBean.getDrawDisplayType());
                            List<EventMasterBean> eventMasterList = drawMasterBean
                                    .getEventMasterList();
                            for (EventMasterBean eventMasterBean : eventMasterList) {
                                responseString.append("%")
                                        .append(eventMasterBean.getEventId())
                                        .append(",")
                                        .append(eventMasterBean.getEventDisplay())
                                        .append(",");
                                /*
                                 * List<String> eventOptionsList =
                                 * eventMasterBean.getEventOptionsList();
                                 * responseString.append(eventOptionsList);
                                 */
                            }

                            responseString
                                    .deleteCharAt(responseString.length() - 1);
                        //  if(counter == drawMasterList.size())
                        //responseString.append("|");
                        }
                        //responseString.deleteCharAt(responseString.length() - 1);
                    }
                }

                responseString.append("#");
            }
            responseString.deleteCharAt(responseString.length() - 1);
    	}catch (Exception e) {
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE); 
		}
    	
        return responseString.toString();
    }
   
    public static String prepareSLEMatchListData(List<GameMasterBean> gameMasterList) {
        StringBuilder responseString = new StringBuilder("");
        responseString.append("sportsData:SLPriv:11440114301145011420|");
        for (GameMasterBean gameMasterBean : gameMasterList) {
            responseString.append(gameMasterBean.getGameId()).append(",")
                    .append(gameMasterBean.getGameDevName()).append(",")
                    .append(gameMasterBean.getGameDispName()).append(",")
                    .append(gameMasterBean.getThersholdTickerAmt()).append(",")
                    .append(gameMasterBean.getMaxTicketAmt()).append(",")
                    .append(gameMasterBean.getMinBoardCount()).append(",")
                    .append(gameMasterBean.getMaxBoardCount());
            List<GameTypeMasterBean> gameTypeMasterList = gameMasterBean
                    .getGameTypeMasterList();
            for (GameTypeMasterBean gameTypeMasterBean : gameTypeMasterList) {
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
                responseString.append("$")
                        .append(gameTypeMasterBean.getGameTypeId()).append(",")
                        .append(gameTypeMasterBean.getGameTypeDevName())
                        .append(",")
                        .append(gameTypeMasterBean.getGameTypeDispName())
                        .append(",").append(gameTypeMasterBean.getUnitPrice())
                        .append(",")
                        .append(gameTypeMasterBean.getMaxBetAmtMultiple())
                        .append(",")
                        // .append(gameTypeMasterBean.getEventType());
                        .append(eventOptionsList);
                // .append("&CD:");
                List<DrawMasterBean> drawMasterList = gameTypeMasterBean
                        .getDrawMasterList();
                if (drawMasterList.size() > 0) {
                    responseString.append("&CD:");
                    for (DrawMasterBean drawMasterBean : drawMasterList) {
                        responseString.append(drawMasterBean.getDrawId())
                                .append(",").append(drawMasterBean.getDrawNo())
                                .append(",")
                                .append(drawMasterBean.getDrawDateTime())
                                .append(",")
                                // .append(drawMasterBean.getDrawFreezeTime()).append(",")
                                .append("50,")
                                .append(drawMasterBean.getDrawDisplayType());
                        List<EventMasterBean> eventMasterList = drawMasterBean
                                .getEventMasterList();
                        for (EventMasterBean eventMasterBean : eventMasterList) {
                            responseString.append("%")
                                    .append(eventMasterBean.getEventId())
                                    .append(",")
                                    .append(eventMasterBean.getEventDisplay())
                                    .append(",");
                            /*
                             * List<String> eventOptionsList =
                             * eventMasterBean.getEventOptionsList();
                             * responseString.append(eventOptionsList);
                             */
                        }

                        responseString
                                .deleteCharAt(responseString.length() - 1);
                        responseString.append("|");
                    }
                    responseString.deleteCharAt(responseString.length() - 1);
                }
            }

            responseString.append("#");
        }
        responseString.deleteCharAt(responseString.length() - 1);
        return responseString.toString();
    }

    public static String generateSportsLotterySaleResponseData(SportsLotteryGamePlayBean gamePlayBeanResponse, String merchantDevname, String balance, long transId)
            throws SLEException {
        StringBuilder responseString = new StringBuilder("");
        NumberFormat nf = NumberFormat.getInstance();
        DecimalFormat df = new DecimalFormat("###0.00");
        nf.setMinimumFractionDigits(2);
        StringBuilder jackpotMsg = null;
        Map<Integer, PrizeRankDrawWinningBean> prizeDistributionMap = null;
//        String newString = "currDate:2013:11:11|currTime:12:12:12|ticketNo=12453458584|brCd=54565465464612|ticketAmt=124.45|balance=456.12|gameId=124|gameTypeID=25|gameName=SOCCER|gameTypeName=Soccer8|drawId=<drawDate>,<drawTime>,<drawDisplay>,<drawId>,<unitPrice*betAmtMultiple>,<boardPrice>,<noOfLines>~<eventDescription@1,X>#<eventDescription@1,X>&drawId=<drawDate>,<drawTime>,<drawDisplay>,<drawId>,<unitPrice*betAmtMultiple>,<boardPrice>,<noOfLines>~<eventDescription@1,X>#<eventDescription@1,X>&";
        try {

            String time = gamePlayBeanResponse.getPurchaseTime().replace(" ", "|currTime:");
            responseString.append("currDate:").append(time);
            responseString.append("|ticketNo:" + gamePlayBeanResponse.getTicketNumber() + gamePlayBeanResponse.getReprintCount());
            responseString.append("|brCd:" + gamePlayBeanResponse.getTicketNumber()    + gamePlayBeanResponse.getReprintCount());
//                    + (gamePlayBeanResponse.getBarcodeCount() > 0 ? gamePlayBeanResponse.getBarcodeCount() : ""));
            if("TRUE".equals(Util.getPropertyValue("IS_BARCODE_ENABLED")))
                responseString.append(gamePlayBeanResponse.getBarcodeCount());

            responseString.append("|trxId:" + transId);
            String ticketAmt = nf.format(gamePlayBeanResponse.getTotalPurchaseAmt()).replaceAll(",", "");

            responseString.append("|ticketAmt:" + ticketAmt);
            responseString.append("|balance:" + balance);
            responseString
                    .append("|gameId:" + gamePlayBeanResponse.getGameId());
            responseString.append("|gameTypeId:"
                    + gamePlayBeanResponse.getGameTypeId());
            responseString
                    .append("|gameName:"
                            + SportsLotteryUtils.gameInfoMerchantMap.get(merchantDevname).get(gamePlayBeanResponse.getGameId()).getGameDispName());
            responseString.append("|gameTypeName:"+ SportsLotteryUtils.gameTypeInfoMerchantMap.get(merchantDevname).get(gamePlayBeanResponse.getGameTypeId()).getGameTypeDispName());

            Map<Integer, String> drawDataMap = new LinkedHashMap<Integer, String>();
            Map<Integer, String> jackpotDataMap = new HashMap<Integer, String>();

            String displayType = Util.getPropertyValue("SPORTS_LOTTERY_TICKET_TYPE");

            if (displayType.equals("DRAW_WISE")) {

                for (int i = 0; i < gamePlayBeanResponse.getNoOfBoard(); i++) {
                    SportsLotteryGameDrawDataBean gameDrawDataBean = gamePlayBeanResponse
                            .getGameDrawDataBeanArray()[i];

                    if (drawDataMap.containsKey(gameDrawDataBean.getDrawId())) {
                        StringBuilder drawStringBuild = new StringBuilder(drawDataMap.get(gameDrawDataBean.getDrawId()));
                        drawStringBuild.append("$");
                        String betAmt = nf.format(gameDrawDataBean.getBetAmountMultiple() * SportsLotteryUtils.gameTypeInfoMerchantMap.get(merchantDevname).get(gamePlayBeanResponse.getGameTypeId())
                                                .getUnitPrice()).replaceAll(",", "");

                        drawStringBuild.append(betAmt + ",");

                        String boardPurchaseAmt = nf.format(
                                gameDrawDataBean.getBoardPurchaseAmount())
                                .replaceAll(",", "");

                        drawStringBuild.append(boardPurchaseAmt + ",");

                        drawStringBuild.append(gameDrawDataBean.getNoOfLines()
                                + "~");

                        for (int j = 0; j < gameDrawDataBean.getGameEventDataBeanArray().length; j++) {
                            SportsLotteryGameEventDataBean eventDataBean = gameDrawDataBean
                                    .getGameEventDataBeanArray()[j];

                            drawStringBuild.append(eventDataBean
                                    .getEventDescription() + "@");
                            for (int k = 0; k < eventDataBean
                                    .getSelectedOption().length; k++) {
                                drawStringBuild.append(eventDataBean
                                        .getSelectedOption()[k] + ",");
                            }

                            drawStringBuild.deleteCharAt(drawStringBuild
                                    .length() - 1);

                            drawStringBuild.append("#");

                        }
                        drawStringBuild
                                .deleteCharAt(drawStringBuild.length() - 1);
                        drawDataMap.put(gameDrawDataBean.getDrawId(),
                                drawStringBuild.toString());

                    } else {
                        StringBuilder drawStringBuild = new StringBuilder();
                        drawStringBuild.append("|drawInfo:");
                        drawStringBuild.append(gameDrawDataBean
                                .getDrawDateTime().replace(" ", ",") + ",");
                        drawStringBuild.append(gameDrawDataBean
                                .getDrawDisplayname() + ",");
                        drawStringBuild.append(gameDrawDataBean.getEventId()
                                + ",");

                        // set jackpot message string
                        // jackpotMsg:current winning amt@still groving$Match
                        // 10@10000.00&Match 9@10000.00&match 8@10000.00|
                        jackpotMsg = new StringBuilder("");
                        String gameTypeDevName = SportsLotteryUtils.gameTypeInfoMerchantMap.get("RMS").get(gamePlayBeanResponse.getGameTypeId()).getGameTypeDevName();
                    	if("soccer12".equals(gameTypeDevName)) {
                    		jackpotMsg.append("jackpotMsg:  12 out of 12 Winners'         Jackpot Fund @ ").append(Util.getPropertyValue("JACKPOT_DISPLAY_AMOUNT")).append(" Ghs").append("~@~");
                    	} else{
	                        Map<Integer, PrizeRankDrawWinningBean> rankDistributionmap = gameDrawDataBean
	                                .getDrawPrizeRankMap();
	                        prizeDistributionMap = new TreeMap<Integer, PrizeRankDrawWinningBean>();
	                        prizeDistributionMap.putAll(rankDistributionmap);
	                        if (prizeDistributionMap != null && prizeDistributionMap.size() > 0) {
	                           
	                        	/*currenty only print in INGENICO TERMINALS*/
	                        	 if("soccer13".equals(gameTypeDevName)) {
	                        		jackpotMsg.append("jackpotMsg:Current Jackpot Fund@(All Prizes Parimutuel)~13 out Of 13 Match@");
	                                for (Map.Entry<Integer, PrizeRankDrawWinningBean> entry : prizeDistributionMap.entrySet()) {
	                                    String prizeVal = df.format(entry.getValue().getPrizeValue()).replaceAll(",", "");
	                                    if(entry.getValue().getRankName().equalsIgnoreCase("Match 13")) {
	                                        jackpotMsg.append(prizeVal).append(" USD").append("~");
	                                    }      
	                                }
	                        	} else if("soccer10".equals(gameTypeDevName)) {
	                        		jackpotMsg.append("jackpotMsg:Current Jackpot Fund@(All Prizes Parimutuel)~10 out Of 10 Match@");
	                                 for (Map.Entry<Integer, PrizeRankDrawWinningBean> entry : prizeDistributionMap.entrySet()) {
	                                     String prizeVal = df.format(entry.getValue().getPrizeValue()).replaceAll(",", "");
	                                     if(entry.getValue().getRankName().equalsIgnoreCase("Match 10")) {
	                                         jackpotMsg.append(prizeVal).append(" USD").append("~");
	                                     }      
	                                 }
	                        	} else{
	                        		jackpotMsg.append("jackpotMsg: ");
	                        	}        	
	                        }
                    	}
                    	if(jackpotMsg.length() > 0){
                    		jackpotMsg.deleteCharAt(jackpotMsg.length() - 1);
                    		jackpotMsg.append("|");
                    	}
                    	
                        jackpotDataMap.put(gameDrawDataBean.getDrawId(),
                                jackpotMsg.toString());

                        String betAmt = nf.format(
                                gameDrawDataBean.getBetAmountMultiple()
                                        * SportsLotteryUtils.gameTypeInfoMerchantMap.get(merchantDevname)
                                                .get(gamePlayBeanResponse
                                                        .getGameTypeId())
                                                .getUnitPrice()).replaceAll(
                                ",", "");

                        drawStringBuild.append(betAmt + ",");

                        String boardPurchaseAmt = nf.format(
                                gameDrawDataBean.getBoardPurchaseAmount())
                                .replaceAll(",", "");

                        drawStringBuild.append(boardPurchaseAmt + ",");
                        drawStringBuild.append(gameDrawDataBean.getNoOfLines()
                                + "~");

                        for (int j = 0; j < gameDrawDataBean
                                .getGameEventDataBeanArray().length; j++) {
                            SportsLotteryGameEventDataBean eventDataBean = gameDrawDataBean
                                    .getGameEventDataBeanArray()[j];

                            drawStringBuild.append(eventDataBean
                                    .getEventDescription() + "@");
                            for (int k = 0; k < eventDataBean
                                    .getSelectedOption().length; k++) {
                                drawStringBuild.append(eventDataBean
                                        .getSelectedOption()[k] + ",");
                            }

                            drawStringBuild.deleteCharAt(drawStringBuild
                                    .length() - 1);

                            drawStringBuild.append("#");

                        }
                        drawStringBuild
                                .deleteCharAt(drawStringBuild.length() - 1);
                        drawDataMap.put(gameDrawDataBean.getDrawId(),
                                drawStringBuild.toString());
                    }

                }

                for (Map.Entry<Integer, String> drawData : drawDataMap
                        .entrySet()) {
                    responseString.append(drawData.getValue()
                            + jackpotDataMap.get(drawData.getKey()));
                }

            } else {
                for (int i = 0; i < gamePlayBeanResponse.getNoOfBoard(); i++) {
                    SportsLotteryGameDrawDataBean gameDrawDataBean = gamePlayBeanResponse
                            .getGameDrawDataBeanArray()[i];
                    StringBuilder drawStringBuild = new StringBuilder();
                    drawStringBuild.append("|drawInfo:");
                    drawStringBuild.append(gameDrawDataBean.getDrawDateTime()
                            .replace(" ", ",") + ",");
                    drawStringBuild.append(gameDrawDataBean
                            .getDrawDisplayname() + ",");
                    drawStringBuild.append(gameDrawDataBean.getEventId() + ",");
                   
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Calendar calendar1 = Calendar.getInstance();
                    calendar1.setTime(dateFormat.parse(gameDrawDataBean.getDrawClaimEndTime()));

                    Calendar calendar2 = Calendar.getInstance();
                    calendar2.setTime(dateFormat.parse(gameDrawDataBean.getDrawDateTime()));
                   
                    int tktValidity = (int)(calendar1.getTimeInMillis() - calendar2.getTimeInMillis())/(24*60*60*1000);
                   
                    drawStringBuild.append(tktValidity + ",");
                    drawStringBuild.append(SportsLotteryUtils.gameTypeInfoMerchantMap.get(merchantDevname).get(gamePlayBeanResponse.getGameTypeId()).getUnitPrice() + ",");

                    String betAmt = nf.format(
                            gameDrawDataBean.getBetAmountMultiple()
                                    * SportsLotteryUtils.gameTypeInfoMerchantMap.get(merchantDevname).get(gamePlayBeanResponse.getGameTypeId()).getUnitPrice())
                            .replaceAll(",", "");

                    drawStringBuild.append(betAmt + ",");

                    String boardPurchaseAmt = nf.format(
                            gameDrawDataBean.getBoardPurchaseAmount())
                            .replaceAll(",", "");

                    drawStringBuild.append(boardPurchaseAmt + ",");
                    drawStringBuild.append(gameDrawDataBean.getNoOfLines()
                            + "~");

                    for (int j = 0; j < gameDrawDataBean
                            .getGameEventDataBeanArray().length; j++) {
                        SportsLotteryGameEventDataBean eventDataBean = gameDrawDataBean
                                .getGameEventDataBeanArray()[j];

                        drawStringBuild.append(eventDataBean
                                .getEventDescription() + "@");
                        for (int k = 0; k < eventDataBean.getSelectedOption().length; k++) {
                            drawStringBuild.append(eventDataBean
                                    .getSelectedOption()[k] + ",");
                        }

                        drawStringBuild
                                .deleteCharAt(drawStringBuild.length() - 1);

                        drawStringBuild.append("#");

                    }
                    drawStringBuild.deleteCharAt(drawStringBuild.length() - 1);

                    // set jackpot message string
                    // jackpotMsg:current winning amt@still groving$Match
                    // 10@10000.00&Match 9@10000.00&match 8@10000.00|
                    jackpotMsg = new StringBuilder("");
                    String gameTypeDevName = SportsLotteryUtils.gameTypeInfoMerchantMap.get("RMS").get(gamePlayBeanResponse.getGameTypeId()).getGameTypeDevName();
                	if("soccer12".equals(gameTypeDevName)) {
                		jackpotMsg.append("jackpotMsg:   12 out of 12 Winners'        Jackpot Fund @ ").append(Util.getPropertyValue("JACKPOT_DISPLAY_AMOUNT")).append(" Ghs").append("~@~");
                	} else{
	                    Map<Integer, PrizeRankDrawWinningBean> rankDistributionmap = gameDrawDataBean
	                            .getDrawPrizeRankMap();
	                    prizeDistributionMap = new TreeMap<Integer, PrizeRankDrawWinningBean>();
	                    prizeDistributionMap.putAll(rankDistributionmap);
	                    if (prizeDistributionMap != null
	                            && prizeDistributionMap.size() > 0) {
	                    	
	            	/*currenty only print in INGENICO TERMINALS*/
	                    	  if("soccer13".equals(gameTypeDevName)) {
	                    		jackpotMsg.append("jackpotMsg:Current Jackpot Fund@(All Prizes Parimutuel)~13 out Of 13 Match@");
	                            for (Map.Entry<Integer, PrizeRankDrawWinningBean> entry : prizeDistributionMap.entrySet()) {
	                                String prizeVal = df.format(entry.getValue().getPrizeValue()).replaceAll(",", "");
	                                if(entry.getValue().getRankName().equalsIgnoreCase("Match 13")) {
	                                    jackpotMsg.append(prizeVal).append(" USD").append("~");
	                                }      
	                            }
	                    	} else if("soccer10".equals(gameTypeDevName)) {
	                    		jackpotMsg.append("jackpotMsg:Current Jackpot Fund@(All Prizes Parimutuel)~10 out Of 10 Match@");
	                             for (Map.Entry<Integer, PrizeRankDrawWinningBean> entry : prizeDistributionMap.entrySet()) {
	                                 String prizeVal = df.format(entry.getValue().getPrizeValue()).replaceAll(",", "");
	                                 if(entry.getValue().getRankName().equalsIgnoreCase("Match 10")) {
	                                     jackpotMsg.append(prizeVal).append(" USD").append("~");
	                                 }      
	                             }
	                    	} else{
	                     		jackpotMsg.append("jackpotMsg: ");
	                     	}	
	                    }
                	}
                	if(jackpotMsg.length() > 0){
                 		jackpotMsg.deleteCharAt(jackpotMsg.length() - 1);
                 		jackpotMsg.append("|");
                 	}

                    responseString.append(drawStringBuild.toString()
                            + jackpotMsg.toString());
                }

                StringBuilder topMessage = new StringBuilder("");
                StringBuilder bottomMessage = new StringBuilder("");
                String advtMsg = "";

                UtilApplet.getAdvMessages(gamePlayBeanResponse.getAdvMessageMap(), topMessage, bottomMessage, 10);

                if (topMessage.length() != 0) {
                    advtMsg = "topAdvMsg:" + topMessage + "|";
                }
                if (bottomMessage.length() != 0) {
                    advtMsg = advtMsg + "bottomAdvMsg:" + bottomMessage + "|";
                }

                responseString.append(advtMsg);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
        }
        return responseString.toString();
    }

    public static String generateSportsLotterySaleRefundResponseData(CancelTicketBean cancelTicketBean, double txnAmt, double balance) {
        StringBuilder respString = null;
       
        respString = new StringBuilder();
        respString.append("RfdA:").append(txnAmt)
        .append("|TktN:").append(cancelTicketBean.getTktToCancel()).append(cancelTicketBean.getReprintCount())
        .append("|gameTypeName:").append(cancelTicketBean.getGameTypeName())
        .append("|Balance:").append(UtilityFunctions.formatToTwoDecimal(balance)).append("|");
        
        StringBuilder topMessage = new StringBuilder("");
        StringBuilder bottomMessage = new StringBuilder("");
        String advtMsg = "";

        UtilApplet.getAdvMessages(cancelTicketBean.getAdvMessageMap(), topMessage, bottomMessage, 10);

        if (topMessage.length() != 0) {
            advtMsg = "topAdvMsg:" + topMessage + "|";
        }
        if (bottomMessage.length() != 0) {
            advtMsg = advtMsg + "bottomAdvMsg:" + bottomMessage + "|";
        }
        respString.append(advtMsg);
        
        
       
        return respString.toString();
    }
   
    /*
     * public static String
     * generateSportsLotteryPwtResponseData(PwtVerifyTicketBean
     * pwtVerifyTicketBean,String balance) throws SLEException { StringBuilder
     * responseString = new StringBuilder(""); winData:drawTime:2013-04-29
     * 15:00:00,drawName:CONTINENTAL,No:1,message:CLAIMED,prizeAmt:0.0|
     * drawTime:2013-04-29
     * 15:00:00,drawName:CONTINENTAL,No:1,message:CLAIMED,prizeAmt:0.0|
     *
     * totalPay:0.0| totalClmPend:1444.00| balance:0.0| try{
     * responseString.append("winData:"); String time =
     * Util.getCurrentTimeString().replace(" ", "|currTime:");
     * responseString.append("currDate:").append(time).append("|");
     * responseString
     * .append("ticketNo:").append(pwtVerifyTicketBean.getTicketNumber
     * ()).append("|");
     * responseString.append("gameName:").append(pwtVerifyTicketBean
     * .getGameName()).append("|");
     * responseString.append("gameTypeName:").append
     * (pwtVerifyTicketBean.getGameTypename()).append("|");
     *
     * for(int
     * i=0;i<pwtVerifyTicketBean.getVerifyTicketDrawDataBeanArray().length;i++){
     * PwtVerifyTicketDrawDataBean
     * pwtVerifyDrawTicketBean=pwtVerifyTicketBean.getVerifyTicketDrawDataBeanArray
     * ()[i];
     * responseString.append("drawTime:"+pwtVerifyDrawTicketBean.getDrawDateTime
     * ()+",");
     * responseString.append("drawName:"+pwtVerifyDrawTicketBean.getDrawName
     * ()+",");
     * responseString.append("No:"+pwtVerifyDrawTicketBean.getBoardCount()+",");
     * responseString
     * .append("message:"+pwtVerifyDrawTicketBean.getMessage()+",");
     * responseString
     * .append("prizeAmt:"+pwtVerifyDrawTicketBean.getDrawWinAmt()+"|");
     *
     *
     * }
     *
     * responseString.append("totalPay:"+pwtVerifyTicketBean.getTotalWinAmt()+"|"
     * ); responseString.append("totalClmPend:"+0.0+"|");
     * responseString.append("balance:"+balance+"|"); }catch (Exception e) {
     * e.printStackTrace(); throw new
     * SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE
     * ,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE); } return
     * responseString.toString(); }
     *
     * public static final String generateGameDataReportDate(String
     * retailerName, Map<String, List<RetGameDataReportBean>> gameDataReportMap,
     * String reportDate) { SimpleDateFormat dateFormat = new
     * SimpleDateFormat("yyyy-MM-dd"); SimpleDateFormat timeFormat = new
     * SimpleDateFormat("HH:mm:ss"); Date nowDate = new Date(); String
     * currentDate = dateFormat.format(nowDate); String currentTime =
     * timeFormat.format(nowDate);
     *
     * NumberFormat numberFormat = NumberFormat.getInstance();
     * numberFormat.setMinimumFractionDigits(2);
     *
     * double totalSaleAmt = 0; double totalPwtAmt = 0;
     *
     * StringBuilder responseDate = new StringBuilder("SlReportData:")
     * .append("retName:").append(retailerName)
     * .append("|Date:").append(currentDate)
     * .append("|Time:").append(currentTime)
     * .append("|ReportDate:").append(reportDate); for(String gameName :
     * gameDataReportMap.keySet()) {
     * responseDate.append("#gameName:").append(gameName);
     * List<RetGameDataReportBean> gameDataReportList =
     * gameDataReportMap.get(gameName); for(RetGameDataReportBean
     * gameDataReportBean : gameDataReportList) { double saleAmt =
     * gameDataReportBean.getSaleAmount(); double pwtAmt =
     * gameDataReportBean.getPwtAmount(); totalSaleAmt += saleAmt; totalPwtAmt
     * += pwtAmt;
     *
     * responseDate.append("$gameTypeName:").append(gameDataReportBean.
     * getGameTypeName())
     * .append(",GameS:").append(numberFormat.format(saleAmt).replaceAll(",",
     * ""))
     * .append(",GamePwt:").append(numberFormat.format(pwtAmt).replaceAll(",",
     * "")); } }
     * responseDate.append("|TotalSale:").append(numberFormat.format(totalSaleAmt
     * ).replaceAll(",", ""))
     * .append("|TotalPWT:").append(numberFormat.format(totalPwtAmt
     * ).replaceAll(",", ""));
     *
     * return responseDate.toString(); }
     *
     * public static String generateWinningResultReportData(String userName,
     * List<WinningResultReportBean> winningResultReportList) {
     * if(winningResultReportList.size()==0) return "ErrorMsg:Result Awaited.";
     *
     * WinningResultReportBean winningResultReportBean =
     * winningResultReportList.get(0); StringBuilder responseData = new
     * StringBuilder
     * ("gameName:").append(winningResultReportBean.getGameName()).append("|")
     * .append
     * ("gameTypeName:").append(winningResultReportBean.getGameTypeName())
     * .append("|")
     * .append("drawDate:").append(winningResultReportBean.getDrawDate
     * ()).append("|")
     * .append("drawTime:").append(winningResultReportBean.getDrawTime
     * ()).append("|")
     * .append("drawName:").append(winningResultReportBean.getDrawName
     * ()).append("|") .append("eventInfo:"); Map<String, String> eventOptionMap
     * = winningResultReportBean.getEventOptionMap(); if(eventOptionMap.size()
     * == 0) { return "ErrorMsg:Result Awaited.";
     * //responseDate.append("AWAITED"); } else { Set<String> eventDisplaySet =
     * eventOptionMap.keySet(); for(String eventDisplay : eventDisplaySet) {
     * String optionCode = eventOptionMap.get(eventDisplay);
     * responseData.append(
     * eventDisplay).append("@").append(optionCode).append("~"); }
     * responseData.deleteCharAt(responseData.length()-1); } return
     * responseData.toString(); }
     */
   
    public static String prepareSLEMatchListDataDayWise(Map<Integer, List<EventMasterBean>> matchDataDayWise, int merchantId,int gameTypeId,StringBuilder drawInfo) throws SLEException {
        StringBuilder responseString = new StringBuilder("");
        responseString.append("sportsMatchData:");
        for(Map.Entry<Integer, List<EventMasterBean>> mapEntry : matchDataDayWise.entrySet()){
           
            responseString.append(CommonMethodsServiceImpl.getInstance().getGameNameFromGameId(mapEntry.getKey(), merchantId)).append(",").append(SportsLotteryUtils.gameTypeInfoMerchantMap.get(Util.fetchMerchantInfoBean(merchantId).getMerchantDevName()).get(gameTypeId).getGameTypeDispName()).append(",").append(drawInfo.toString());
            if(mapEntry.getValue().size() >0) {
            for(EventMasterBean eventMasterBean : mapEntry.getValue()){
                  
                responseString.append(eventMasterBean.getEventDisplay()).append(",");
                responseString.append(eventMasterBean.getEventDescription()).append(",");
                responseString.append(eventMasterBean.getLeagueName()).append(",");
                responseString.append(eventMasterBean.getHomeTeamName()).append(",");
                responseString.append(eventMasterBean.getAwayTeamName()).append(",");
                responseString.append(eventMasterBean.getVenueName()).append(",");
                responseString.append(eventMasterBean.getStartTime()).append(",");
                responseString.append(eventMasterBean.getEndTime()).append("#");
            }
            responseString.deleteCharAt(responseString.length() - 1);
            responseString.append("%");   
           
        }
            else{
            	throw new SLEException(SLEErrors.MATCH_NOT_AVAILABLE_ERROR_CODE,SLEErrors.MATCH_NOT_AVAILABLE_ERROR_MESSAGE);
            }
        }

        responseString.deleteCharAt(responseString.length() - 1);       
        return responseString.toString();
    }

    public static final String generateSLEVerifyTicketResponseData(PwtVerifyTicketBean verifyTicketBean, boolean claimStatus, String statusMsg) throws SLEException {
        StringBuilder responseBuilder = new StringBuilder();
        DecimalFormat df = new DecimalFormat("###0.00");

        String currentTime = Util.getCurrentTimeString().replace(" ", "|currTime:");

        responseBuilder.append("winData:");
        responseBuilder.append("currDate:").append(currentTime).append("|");
        responseBuilder.append("ticketNo:").append(verifyTicketBean.getTicketNumber()).append("|");
        responseBuilder.append("gameName:").append(verifyTicketBean.getGameName()).append("|");
        responseBuilder.append("gameTypeName:").append(verifyTicketBean.getGameTypename()).append("|");

        for(int i=0;i<verifyTicketBean.getVerifyTicketDrawDataBeanArray().length;i++){
            PwtVerifyTicketDrawDataBean pwtVerifyDrawTicketBean=verifyTicketBean.getVerifyTicketDrawDataBeanArray()[i];
            responseBuilder.append("drawTime:"+pwtVerifyDrawTicketBean.getDrawDateTime()+",");
            responseBuilder.append("drawName:"+pwtVerifyDrawTicketBean.getDrawName()+",");
            responseBuilder.append("No:"+pwtVerifyDrawTicketBean.getBoardCount()+",");
            if(statusMsg != null){
                responseBuilder.append("message:"+statusMsg+" ");
            }else{
                responseBuilder.append("message:"+pwtVerifyDrawTicketBean.getMessage()+" ");
            }
            if("Verification Pending".equalsIgnoreCase(pwtVerifyDrawTicketBean.getMessage().trim()) ||"DRAW_EXPIRED".equalsIgnoreCase(pwtVerifyDrawTicketBean.getDrawStatus()) || " High Prize Ticket".equalsIgnoreCase(statusMsg)){
            	 responseBuilder.append("|");
            }else{
            	if(pwtVerifyDrawTicketBean.getDrawWinAmt()>0.0){
            		 responseBuilder.append("prizeAmt:"+df.format(pwtVerifyDrawTicketBean.getDrawWinAmt())+"|");
            	}else{
            		responseBuilder.append("|");
            	}
            	
            }
           
        }
        responseBuilder.append("totalPay:"+df.format(verifyTicketBean.getTotalWinAmt())+"|");
        responseBuilder.append("totalClmPend:"+0.0+"|");
        responseBuilder.append("claimStatus:"+claimStatus+"|");

        return responseBuilder.toString();
    }

    public static String generateSLEWinningTicketResponseData(PwtVerifyTicketBean verifyTicketBean, double balance,double govtTaxPwt) throws SLEException {
        StringBuilder responseBuilder = new StringBuilder("");
        DecimalFormat df = new DecimalFormat("###0.00");
        try {
            String time = Util.getCurrentTimeString().replace(" ", "|currTime:");
            String balanceString = new DecimalFormat("#.00").format(balance);
            double taxOnPwt=verifyTicketBean.getTotalWinAmt()*govtTaxPwt*.01;

            responseBuilder.append("winData:");
            responseBuilder.append("currDate:").append(time).append("|");
            responseBuilder.append("ticketNo:").append(verifyTicketBean.getTicketNumber()).append("|");
            responseBuilder.append("gameName:").append(verifyTicketBean.getGameName()).append("|");
            responseBuilder.append("gameTypeName:").append(verifyTicketBean.getGameTypename()).append("|");

            for(int i=0;i<verifyTicketBean.getVerifyTicketDrawDataBeanArray().length;i++){
                PwtVerifyTicketDrawDataBean pwtVerifyDrawTicketBean=verifyTicketBean.getVerifyTicketDrawDataBeanArray()[i];
                responseBuilder.append("drawTime:"+pwtVerifyDrawTicketBean.getDrawDateTime()+",");
                responseBuilder.append("drawName:"+pwtVerifyDrawTicketBean.getDrawName()+",");
                responseBuilder.append("No:"+pwtVerifyDrawTicketBean.getBoardCount()+",");
                responseBuilder.append("message:"+pwtVerifyDrawTicketBean.getMessage()+",");
                responseBuilder.append("prizeAmt:"+df.format(pwtVerifyDrawTicketBean.getDrawWinAmt())+"|");
            }
            
            responseBuilder.append("totalPay:"+df.format(verifyTicketBean.getTotalWinAmt()-taxOnPwt)+"|");
            responseBuilder.append("totalClmPend:"+0.0+"|");
            responseBuilder.append("balance:"+balanceString+"|");
            
            StringBuilder topMessage = new StringBuilder("");
            StringBuilder bottomMessage = new StringBuilder("");
            String advtMsg = "";
           
        	UtilApplet.getAdvMessages(verifyTicketBean.getAdvMessageMap(), topMessage, bottomMessage, 10);

            if (topMessage.length() != 0) {
                advtMsg = "topAdvMsg:" + topMessage + "|";
            }
            if (bottomMessage.length() != 0) {
                advtMsg = advtMsg + "bottomAdvMsg:" + bottomMessage + "|";
            }

            responseBuilder.append(advtMsg);
            responseBuilder.append("Tax:"+df.format(taxOnPwt)+"|");
            
        }catch (Exception e) {
            e.printStackTrace();
            throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
        }
        return responseBuilder.toString();
    }
}