package com.skilrock.sle.embedded.playMgmt.action;

import java.io.IOException;

import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.common.javaBeans.UserInfoBean;
import com.skilrock.sle.commonMethod.serviceImpl.CommonMethodsServiceImpl;
import com.skilrock.sle.embedded.common.BaseActionTerminal;
import com.skilrock.sle.embedded.common.SportsLotteryResponseData;
import com.skilrock.sle.gamePlayMgmt.controllerImpl.ReprintTicketControllerImpl;
import com.skilrock.sle.gamePlayMgmt.javaBeans.ReprintTicketBean;
import com.skilrock.sle.merchant.common.javaBeans.MerchantInfoBean;

public class SportsLotteryTerminalGameTicketReprintAction extends BaseActionTerminal {
	
	public SportsLotteryTerminalGameTicketReprintAction() {
		super(SportsLotteryTerminalGameTicketReprintAction.class.getName());
	}
	private static final long serialVersionUID = 1L;
	

	
	private String userName;
	
	public void reprintTicket() {
	//	logger.info("***** Inside Reprint Ticket Method**************************");
		String responseString = null;
		ReprintTicketControllerImpl controller = null;
		MerchantInfoBean merchantInfoBean = null;
		UserInfoBean userInfoBean = null;
		ReprintTicketBean reprintTktBean = null;
		try {
				merchantInfoBean =Util.merchantInfoMap.get(getMerCode());

				userInfoBean = new UserInfoBean();
				userInfoBean.setUserName(getUserName());
				userInfoBean.setMerchantId(merchantInfoBean.getMerchantId());
				userInfoBean.setUserSessionId(getSessId());
				userInfoBean.setMerchantDevName(getMerCode());
			
				CommonMethodsServiceImpl.getInstance().fetchAndCheckUserData(userInfoBean);
				
				logger.debug("Merchant User Info Bean is "+userInfoBean);
				
				CommonMethodsServiceImpl.getInstance().checkAndAutoCancel(getSlLstTxnId(), "CANCEL_MISMATCH", userInfoBean);

				controller = new ReprintTicketControllerImpl();			
				reprintTktBean = controller.reprintSportsLotteryGameTicket(userInfoBean,merchantInfoBean,"TERMINAL");
				responseString = SportsLotteryResponseData.generateSportsLotterySaleResponseData(reprintTktBean.getGamePlayBean(), userInfoBean.getMerchantDevName(), String.valueOf(reprintTktBean.getAvlBal()), reprintTktBean.getGamePlayBean().getTransId());
				response.getOutputStream().write(responseString.getBytes());
			}catch (SLEException e) {
				try {
						if(e.getErrorCode() == 10012){
							response.getOutputStream().write(("ErrorMsg:" + e.getErrorMessage()+"|ErrorCode:01|").getBytes());
						}else if(e.getErrorCode() == 2016){
							response.getOutputStream().write(("ErrorMsg:" + e.getErrorMessage()+"|ErrorCode:"+e.getErrorCode()+"|").getBytes());
						}else{
							response.getOutputStream().write(("ErrorMsg:" + e.getErrorMessage()).getBytes());
						}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				return;
			}catch (IOException e) {
				e.printStackTrace();
				try {
					response.getOutputStream().write("ErrorMsg:Error!Reprint Failed".getBytes());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				return;
			} catch (Exception e) {
				e.printStackTrace();
				try {
					response.getOutputStream().write("ErrorMsg:Error!Reprint Failed".getBytes());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				return;
		}
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
}
