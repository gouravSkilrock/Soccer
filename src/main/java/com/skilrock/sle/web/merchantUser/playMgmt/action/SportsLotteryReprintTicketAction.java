package com.skilrock.sle.web.merchantUser.playMgmt.action;

import java.io.PrintWriter;

import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.common.javaBeans.UserInfoBean;
import com.skilrock.sle.commonMethod.serviceImpl.CommonMethodsServiceImpl;
import com.skilrock.sle.embedded.common.BaseActionTerminal;
import com.skilrock.sle.gamePlayMgmt.controllerImpl.ReprintTicketControllerImpl;
import com.skilrock.sle.gamePlayMgmt.javaBeans.ReprintTicketBean;
import com.skilrock.sle.merchant.common.javaBeans.MerchantInfoBean;
import com.skilrock.sle.web.merchantUser.common.SportsLotteryWebResponseData;

public class SportsLotteryReprintTicketAction extends BaseActionTerminal {

	private static final long serialVersionUID = 1L;
    
	public SportsLotteryReprintTicketAction(){
		super(SportsLotteryReprintTicketAction.class.getName());
	}
	public void reprintTicket(){
		ReprintTicketControllerImpl controller = null;
		MerchantInfoBean merchantInfoBean = null;
		UserInfoBean userInfoBean = null;
		ReprintTicketBean reprintTktBean = null;
		JSONObject responseJson=null;
		PrintWriter out=null;
		 try{
				response.setContentType("application/json");
				out=response.getWriter();
				merchantInfoBean =Util.merchantInfoMap.get("RMS");
			    HttpSession session=request.getSession();
			    userInfoBean=(UserInfoBean)session.getAttribute("USER_INFO");
				CommonMethodsServiceImpl.getInstance().fetchAndCheckUserData(userInfoBean);				
				logger.debug("Merchant User Info Bean is "+userInfoBean);	
				controller = new ReprintTicketControllerImpl();			
				reprintTktBean = controller.reprintSportsLotteryGameTicket(userInfoBean,merchantInfoBean,"WEB");							
				responseJson=SportsLotteryWebResponseData.generateSportsLotterySaleResponseData(reprintTktBean.getGamePlayBean(), merchantInfoBean.getMerchantDevName(), reprintTktBean.getAvlBal(), null, userInfoBean.getOrgName());
				responseJson.put("isSuccess", true);
				logger.info("Response data for reprint"+responseJson);	
			}catch(SLEException e){
		        e.printStackTrace();
			    if(responseJson==null){
			    	responseJson=new JSONObject();
			    }
			    responseJson.put("isSuccess", false);
				responseJson.put("errorMsg", e.getErrorMessage());
			}catch (Exception e) {
					e.printStackTrace();
					if(responseJson==null){
				    	responseJson=new JSONObject();
				    }
				    responseJson.put("isSuccess", false);
					responseJson.put("errorMsg","Error!Reprint Failed");
		   }
		out.print(responseJson);
		out.flush();
		out.close();
    }
}
