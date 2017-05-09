package com.skilrock.sle.embedded.playMgmt.action;

import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.json.JSONObject;

import com.skilrock.sle.common.SportsLotteryUtils;
import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.common.javaBeans.UserInfoBean;
import com.skilrock.sle.commonMethod.serviceImpl.CommonMethodsServiceImpl;
import com.skilrock.sle.embedded.common.BaseActionTerminal;
import com.skilrock.sle.gamePlayMgmt.controllerImpl.PurchaseTicketControllerImpl;
import com.skilrock.sle.gamePlayMgmt.javaBeans.SportsLotteryGameDrawDataBean;
import com.skilrock.sle.gamePlayMgmt.javaBeans.SportsLotteryGameEventDataBean;
import com.skilrock.sle.gamePlayMgmt.javaBeans.SportsLotteryGamePlayBean;
import com.skilrock.sle.merchant.lms.common.javaBeans.LMSGamePlayResponseBean;
import com.skilrock.sle.web.merchantUser.common.SportsLotteryWebResponseData;

public class SportsLotteryGamePlayActionNew extends BaseActionTerminal {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SportsLotteryGamePlayActionNew() {
		super(SportsLotteryGamePlayActionNew.class.getName());
	}

	private int gameId;
	private int gameTypeId;
	private String[] drawInfo;
	private int drawCount;
	private double ticketAmt;
	private String plrMobileNumber;
	private String tokenId;

	public void sportsLotteryPurchaseTicket() {
		int merchantId = 0;
		int noOfLines = 1;
		UserInfoBean userInfoBean = null;
		LMSGamePlayResponseBean gameResponseBean = null;
        JSONObject jsonObject = null;
        PrintWriter out = null;
        List<String> eventOptionsList = null;
		try {
			jsonObject = new JSONObject();
			out = response.getWriter();
			response.setContentType("application/json");
			merchantId = (Integer)request.getAttribute("merchantId");

			userInfoBean = new UserInfoBean();
			if(getUserName().split(",").length>1){
				userInfoBean.setUserName(getUserName().split(",")[1].trim());	
			}else{
				userInfoBean.setUserName(getUserName().trim());
			}
			if (getSessId().split(",").length >1) {
				userInfoBean.setUserSessionId(getSessId().split(",")[1].trim());
			} else {
				userInfoBean.setUserSessionId(getSessId().trim());

			}
			userInfoBean.setMerchantId(merchantId);
			userInfoBean.setMerchantDevName(getMerCode());
			
			if(tokenId!=null&&!tokenId.trim().isEmpty())
				userInfoBean.setTokenId(tokenId);

			CommonMethodsServiceImpl.getInstance().fetchAndCheckUserData(userInfoBean);

			logger.debug("Merchant User Info Bean is "+userInfoBean);
			String txnId =  null;
			if (getSlLstTxnId().split(",").length >1) {
				txnId = getSlLstTxnId().split(",")[1].trim();
			} else {
				txnId = getSlLstTxnId().trim();

			}
			CommonMethodsServiceImpl.getInstance().checkAndAutoCancel(txnId, "CANCEL_MISMATCH", userInfoBean);

			SportsLotteryGameEventDataBean eventDataBean = null;
			SportsLotteryGameDrawDataBean gameDrawDataBean = null;

			SportsLotteryGamePlayBean gamePlayBean = new SportsLotteryGamePlayBean();
			gamePlayBean.setGameId(gameId);
			gamePlayBean.setGameTypeId(gameTypeId);
			gamePlayBean.setPlrMobileNumber(plrMobileNumber);

			int noOfBoard = drawCount;
			int noOfEvents = SportsLotteryUtils.gameTypeInfoMerchantMap.get(getMerCode()).get(gameTypeId).getNoOfEvents();

			double unitPrice = SportsLotteryUtils.gameTypeInfoMerchantMap.get(getMerCode()).get(gameTypeId).getUnitPrice();
			double totalPurchaseAmt = 0.0;
			Set<Integer> drawIsSet = new HashSet<Integer>();

			gamePlayBean.setNoOfBoard(noOfBoard);
			SportsLotteryGameEventDataBean[] eventDataBeanArray = null;
			SportsLotteryGameDrawDataBean[] gameDrawDataBeanArray = new SportsLotteryGameDrawDataBean[noOfBoard];

			for (int i = 0; i < noOfBoard; i++) {
				String drawData = drawInfo[i];
				String[] drawDataArray = drawData.split("~");
				int drawId = Integer.parseInt(drawDataArray[0]);
				int betAmtMultiple = Integer.parseInt(drawDataArray[1]);

				String[] evntData = drawDataArray[2].split("\\$");

				noOfLines = 1;

				eventDataBeanArray = new SportsLotteryGameEventDataBean[noOfEvents];
				drawIsSet.add(drawId);
				for (int j = 0; j < noOfEvents; j++) {
					eventDataBean = new SportsLotteryGameEventDataBean();
					String[] eventArr = evntData[j].split("@");

					eventDataBean.setEventId(Integer.parseInt(eventArr[0]));
					String[] selectedOption = eventArr[1].split(",");

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
			gamePlayBean.setServiceId((Integer)request.getAttribute("serviceId"));
			gamePlayBean.setTotalPurchaseAmt(Util.fmtToTwoDecimal(totalPurchaseAmt));
			gamePlayBean.setUnitPrice(Util.fmtToTwoDecimal(unitPrice));
			Integer[] drawIdArray = (Integer[]) drawIsSet.toArray(new Integer[drawIsSet.size()]);
			gamePlayBean.setDrawIdArray(drawIdArray);
			gameResponseBean = PurchaseTicketControllerImpl.getInstance().purchaseTicketForTerminal(userInfoBean, gamePlayBean);
			if(gameResponseBean.getResponseCode() == 0) {
				DecimalFormat df = new DecimalFormat("###0.00");
				String balString = df.format(gameResponseBean.getAvailBal());
				gamePlayBean.setAdvMessageMap(gameResponseBean.getAdvMessageMap());
				jsonObject = SportsLotteryWebResponseData.generateSportsLotterySaleResponseData(gamePlayBean, userInfoBean.getMerchantDevName(), gameResponseBean.getAvailBal(),eventOptionsList,userInfoBean.getOrgName());
			} else {
				jsonObject.put("responseCode", gameResponseBean.getResponseCode());
				jsonObject.put("responseMsg", gameResponseBean.getResponseMessage());
			}
			
		} 	catch (SLEException pe) {
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

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public int getGameTypeId() {
		return gameTypeId;
	}

	public void setGameTypeId(int gameTypeId) {
		this.gameTypeId = gameTypeId;
	}

	public String[] getDrawInfo() {
		return drawInfo;
	}

	public void setDrawInfo(String[] drawInfo) {
		this.drawInfo = drawInfo;
	}

	public int getDrawCount() {
		return drawCount;
	}

	public void setDrawCount(int drawCount) {
		this.drawCount = drawCount;
	}

	public double getTicketAmt() {
		return ticketAmt;
	}

	public void setTicketAmt(double ticketAmt) {
		this.ticketAmt = ticketAmt;
	}
	public String getPlrMobileNumber() {
		return plrMobileNumber;
	}

	public void setPlrMobileNumber(String plrMobileNumber) {
		this.plrMobileNumber = plrMobileNumber;
	}
	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}
	
	public String getTokenId() {
		return tokenId;
	}

}