package com.skilrock.sle.scheduler.job;

import java.util.Map.Entry;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.skilrock.sle.common.SLELogger;
import com.skilrock.sle.common.SportsLotteryUtils;
import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.commonMethod.serviceImpl.CommonMethodsServiceImpl;
import com.skilrock.sle.dataMgmt.controllerImpl.ReconciliationControllerImpl;
import com.skilrock.sle.merchant.common.javaBeans.MerchantInfoBean;
import com.skilrock.sle.scheduler.controllerImpl.SLESchedulerControllerImpl;
public class SLESchedulerJob implements Job {

	private static final SLELogger logger = SLELogger.getLogger(SLESchedulerJob.class.getName());
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		int gameId = 0;
		int gameTypeId = 0;
		int drawId = 0;
		String jobName = null;
		String jobGroupName = null;
		JobDataMap dataMap = context.getJobDetail().getJobDataMap();

		gameId = dataMap.getInt("gameId");
		gameTypeId = dataMap.getInt("gameTypeId");
		drawId = dataMap.getInt("drawId");
//		jobName = dataMap.getString("jobName");
//		jobGroupName = dataMap.getString("jobGroupName");

		logger.info("Scheduer Called For GameId "+gameId+" , GameTypeId "+gameTypeId+" , drawId "+drawId+" , jobName "+jobName+" , jobGroupName "+jobGroupName);

		try {
			new SLESchedulerControllerImpl().drawFreezeActivity(gameId, gameTypeId, drawId);
			
			//SportsLotteryUtils.eventDataDrawWise.clear();
			//CommonMethodsServiceImpl.getInstance().setEventDataDrawWise();

			SportsLotteryUtils.drawSaleMap.clear();
			CommonMethodsServiceImpl.getInstance().setDrawSaleMap();

			for (Entry<String, MerchantInfoBean> entrySet : Util.merchantInfoMap.entrySet()) {
				if("RMS".equals(entrySet.getKey()) || "PMS".equals(entrySet.getKey())) {
					ReconciliationControllerImpl.Single.INSTANCE.getInstance().startReconciliation(entrySet.getValue());
				}
			}
		} catch (SLEException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
