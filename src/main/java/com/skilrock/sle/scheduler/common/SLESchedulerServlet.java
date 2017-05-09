package com.skilrock.sle.scheduler.common;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.GenericServlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

import com.skilrock.sle.common.DBConnect;
import com.skilrock.sle.common.SLELogger;
import com.skilrock.sle.common.Util;
import com.skilrock.sle.scheduler.job.SettlementJob;
import com.skilrock.sle.scheduler.job.TicketCountUpdateJob;

public class SLESchedulerServlet extends GenericServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final SLELogger logger = SLELogger.getLogger(SLESchedulerServlet.class.getName());

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			con = DBConnect.getConnection();
			stmt = con.createStatement();
			String qry = "select jobGroup, scheduled_Time from st_sl_generic_scheduler_master where status ='ACTIVE' group by jobGroup";
			rs = stmt.executeQuery(qry);

			while (rs.next()) {
				String jobGroup = rs.getString("jobGroup");
				String scheduledTime = rs.getString("scheduled_Time");

				if ("ticketCountCronExpr".equals(jobGroup)) {
					logger.info("****** Creating Ticket Counter Update Job");
					JobDetail job1 = JobBuilder.newJob(TicketCountUpdateJob.class).withIdentity(jobGroup)
							.build();
					logger.info("ticketCountCronExpr Cron Explression - " + scheduledTime);

					Trigger ticketCountUpdateJobTrigger = TriggerBuilder.newTrigger()
							.withIdentity("TicketCountUpdateJobName", "TicketCountUpdateJobTrigger")
							.withSchedule(CronScheduleBuilder.cronSchedule(scheduledTime)).build();
					Util.scheduler.scheduleJob(job1, ticketCountUpdateJobTrigger);
					logger.info("****** Ticket Counter Update Created");
				} else if ("settlementJobCronExpr".equals(jobGroup)) {
					logger.info("****** Creating Settlement Job");
					JobDetail job2 = JobBuilder.newJob(SettlementJob.class).withIdentity(jobGroup).build();
					logger.info("settlementJobCronExpr Cron Explression - " + scheduledTime);

					Trigger settlementJobTrigger = TriggerBuilder.newTrigger()
							.withIdentity("SettlementJobName", "SettlementJobTrigger")
							.withSchedule(CronScheduleBuilder.cronSchedule(scheduledTime)).build();
					logger.info("****** Creating Settlement Job");
					Util.scheduler.scheduleJob(job2, settlementJobTrigger);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
	}

}
