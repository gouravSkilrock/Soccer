package com.skilrock.sle.scheduler.job;

import java.sql.Connection;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.google.gson.JsonObject;
import com.skilrock.sle.common.DBConnect;
import com.skilrock.sle.common.SLELogger;
import com.skilrock.sle.common.SportsLotteryUtils;
import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.commonMethod.daoImpl.CommonMethodsDaoImpl;
import com.skilrock.sle.gamePlayMgmt.javaBeans.DrawDetailBean;
import com.skilrock.sle.merchant.TpIntegrationImpl;
import com.skilrock.sle.merchant.common.javaBeans.MerchantInfoBean;
import com.skilrock.sle.merchant.pms.ServiceMethodName;
import com.skilrock.sle.merchant.pms.ServiceName;
import com.skilrock.sle.scheduler.controllerImpl.SLESchedulerControllerImpl;
public class SLESendDrawNotificationSchedulerJob implements Job {

	private static final SLELogger logger = SLELogger.getLogger(SLESendDrawNotificationSchedulerJob.class.getName());
	JsonObject reqObj;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		int gameId = 0;
		int gameTypeId = 0;
		int drawId = 0;
		String jobName = null;
		String jobGroupName = null;
		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		Connection con = null;
		gameId = dataMap.getInt("gameId");
		gameTypeId = dataMap.getInt("gameTypeId");
		drawId = dataMap.getInt("drawId");
		MerchantInfoBean merInfoBean=null;
		Map<Integer, DrawDetailBean> drawDetailMap=null;
		logger.info("SendDrawNotification Scheduer Called For GameId "+gameId+" , GameTypeId "+gameTypeId+" , drawId "+drawId+" , jobName "+jobName+" , jobGroupName "+jobGroupName);

		try {
			con = DBConnect.getConnection();
			new SLESchedulerControllerImpl().sendDrawNotificationRescheduleActivity(gameId, gameTypeId, drawId,con);
			merInfoBean=Util.merchantInfoMap.get("PMS");
			drawDetailMap=CommonMethodsDaoImpl.getInstance().setEventDataDrawWise(gameId, gameTypeId, merInfoBean.getMerchantId(), con);
			if(drawDetailMap.isEmpty()){
				return;
			}
			if(drawDetailMap.get(drawId)==null){
				return;
			}
			if(!drawDetailMap.get(drawId).getDrawEventDetail().isEmpty()){
				reqObj = new JsonObject();
				reqObj.addProperty("drawId", drawId);
				reqObj.addProperty("gameTypeDispName", SportsLotteryUtils.gameTypeInfoMerchantMap.get(merInfoBean.getMerchantDevName()).get(gameTypeId).getGameTypeDispName());
				reqObj.addProperty("drawSaleStartTime",Util.convertDateTimeToResponseFormat2(drawDetailMap.get(drawId).getDrawSaleStartTime()));
				reqObj.addProperty("drawDateTime", drawDetailMap.get(drawId).getDrawDateTime());
				reqObj.addProperty("drawName", drawDetailMap.get(drawId).getDrawDisplay());
				reqObj.addProperty("gameTypeId", gameTypeId);
				
				
				logger.debug("Req Obj For sle sendDrawNotification Scheduler "+reqObj);
				
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							TpIntegrationImpl.getPMSResponseString(ServiceName.DATA_MGMT, ServiceMethodName.SLE_SEND_DRAW_NOTIFY_PLAYER, reqObj);
						} catch (Exception e) {
							e.printStackTrace();
						}
						
					}
				}).start();
				
			}
	
		} catch (SLEException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBConnect.closeConnection(con);
		}
	}
}
