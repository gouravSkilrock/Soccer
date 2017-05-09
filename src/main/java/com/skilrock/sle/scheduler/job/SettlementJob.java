package com.skilrock.sle.scheduler.job;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.skilrock.sle.common.Util;
import com.skilrock.sle.dataMgmt.controllerImpl.ReconciliationControllerImpl;
import com.skilrock.sle.merchant.common.javaBeans.MerchantInfoBean;
import com.skilrock.sle.scheduler.common.SchedulerCommonFuntionsHelper;
import com.skilrock.sle.scheduler.common.SchedulerDetailsBean;
import com.skilrock.sle.scheduler.controllerImpl.SLESchedulerControllerImpl;

public class SettlementJob implements Job {
	
	private static Log logger = LogFactory.getLog(SettlementJob.class);
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {

			Map<String, SchedulerDetailsBean> scheBeanMap = SchedulerCommonFuntionsHelper
					.getSchedulerBeanMap(context.getTrigger().getJobKey().getName());
			if (scheBeanMap.size() > 0) {
				if (scheBeanMap.get("settlementJobCronExpr").isActive()) {
					String errorMsg = null;
					try {
						SchedulerCommonFuntionsHelper
								.updateSchedulerStart(scheBeanMap.get("settlementJobCronExpr").getJobId());
						logger.info("settlementJobCronExpr Started");
						for (Entry<String, MerchantInfoBean> entrySet : Util.merchantInfoMap.entrySet()){
							ReconciliationControllerImpl.Single.INSTANCE.getInstance().startReconciliation(entrySet.getValue());
						}
						logger.info("settlementJobCronExpr Ends");
						SchedulerCommonFuntionsHelper
								.updateSchedulerEnd(scheBeanMap.get("settlementJobCronExpr").getJobId());
					} catch (Exception e) {
						logger.error("Exception in settlementJobCronExpr ", e);
						if (e.getMessage() != null) {
							errorMsg = e.getMessage();
						} else {
							errorMsg = "Error Occurred Msg Is Null ";
						}
					}
					if (errorMsg != null) {
						SchedulerCommonFuntionsHelper
								.updateSchedulerError(scheBeanMap.get("settlementJobCronExpr").getJobId(), errorMsg);
					}

				}

			}
		

			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
