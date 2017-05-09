package com.skilrock.sle.scheduler.job;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.skilrock.sle.scheduler.common.SchedulerCommonFuntionsHelper;
import com.skilrock.sle.scheduler.common.SchedulerDetailsBean;
import com.skilrock.sle.scheduler.controllerImpl.SLESchedulerControllerImpl;

public class TicketCountUpdateJob implements Job {

	private static Log logger = LogFactory.getLog(TicketCountUpdateJob.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			Map<String, SchedulerDetailsBean> scheBeanMap = SchedulerCommonFuntionsHelper
					.getSchedulerBeanMap(context.getTrigger().getJobKey().getName());
			if (scheBeanMap.size() > 0) {
				if (scheBeanMap.get("ticketCountCronExpr").isActive()) {
					String errorMsg = null;
					try {
						SchedulerCommonFuntionsHelper
								.updateSchedulerStart(scheBeanMap.get("ticketCountCronExpr").getJobId());
						logger.info("ticketCountCronExpr Started");
						SLESchedulerControllerImpl controllerImpl = new SLESchedulerControllerImpl();
						controllerImpl.updateTicketCounter();
						logger.info("ticketCountCronExpr Ends");
						SchedulerCommonFuntionsHelper
								.updateSchedulerEnd(scheBeanMap.get("ticketCountCronExpr").getJobId());
					} catch (Exception e) {
						logger.error("Exception in ticketCountCronExpr ", e);
						if (e.getMessage() != null) {
							errorMsg = e.getMessage();
						} else {
							errorMsg = "Error Occurred Msg Is Null ";
						}
					}
					if (errorMsg != null) {
						SchedulerCommonFuntionsHelper
								.updateSchedulerError(scheBeanMap.get("ticketCountCronExpr").getJobId(), errorMsg);
					}

				}

			}
		} catch (Exception e) {
			logger.error("LMSException in Weekly Job Scheduler  ", e);
		}
	}

}
