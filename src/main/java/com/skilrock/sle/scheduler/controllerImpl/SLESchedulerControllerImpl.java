package com.skilrock.sle.scheduler.controllerImpl;

import java.sql.Connection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.transaction.UserTransaction;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

import com.skilrock.sle.common.DBConnect;
import com.skilrock.sle.common.SLELogger;
import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.scheduler.daoImpl.SLESchedulerDaoImpl;
import com.skilrock.sle.scheduler.job.SLESchedulerJob;
import com.skilrock.sle.scheduler.job.SLESendDrawNotificationSchedulerJob;

public class SLESchedulerControllerImpl {
	
	private static final SLELogger logger = SLELogger.getLogger(SLESchedulerControllerImpl.class.getName());

	public void updateFreezeTimeStatus() throws SLEException {
		SLESchedulerDaoImpl daoImpl =SLESchedulerDaoImpl.getInstance();
		Connection con = null;
		try {
//			con = DBConnect.getPropFileCon();
			con = DBConnect.getConnection();
			daoImpl.updateFreezeTimeStatus(con);
		} catch (SLEException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(con);
		}
	}
	
//	public void insertDraws() throws SLEException {
//		SLESchedulerDaoImpl daoImpl = new SLESchedulerDaoImpl();
//		Connection con = null;
//		try {
//			con = DBConnect.getPropFileCon();
//			con.setAutoCommit(false);
//			daoImpl.insertDraws(con);
//			con.commit();
//		} catch (SLEException e) {
//			throw e;
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
//		} finally {
//			DBConnect.closeConnection(con);
//		}
//	}
	
//	public void scheduleDraws() throws SLEException {
//		SLESchedulerDaoImpl daoImpl = new SLESchedulerDaoImpl();
//		Map<Integer, List<Integer>> map = null;
//		List<Integer> gameTypeList = null;
//		int gameTypeListSize = 0;
//		Connection con = null;
//		try {
//			con = DBConnect.getPropFileCon();
//			map = CommonMethodsServiceImpl.getInstance().fetchActiveGameAndGameType();
//			for (Entry<Integer, List<Integer>> entrySet : map.entrySet()) {
//				gameTypeList = entrySet.getValue();
//				gameTypeListSize = gameTypeList.size();
//				for (int iLoop = 0; iLoop < gameTypeListSize; iLoop++) {
//				}
//			}
//		} catch (SLEException e) {
//			throw e;
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
//		} finally {
//			DBConnect.closeConnection(con);
//		}
//	}

	public void drawFreezeActivity(int gameId, int gameTypeId, int drawId) throws SLEException {
		SLESchedulerDaoImpl daoImpl = SLESchedulerDaoImpl.getInstance();
		List<String> drawData = null;
		Connection con = null;
		UserTransaction userTxn = null;
		try {
//			con = DBConnect.getPropFileCon();
			con = DBConnect.getConnection();
			userTxn = DBConnect.startTransaction();
			daoImpl.freezeDraw(gameId, gameTypeId, drawId, con);
			daoImpl.insertDrawsGameAndGameTypeWise(gameId, gameTypeId, con);
			drawData = daoImpl.getNextDrawData(gameId, gameTypeId, con);
			userTxn.commit();
			createJob(gameId, gameTypeId, Integer.parseInt(drawData.get(1)), drawData.get(0));
		}  catch (SLEException e) {
			DBConnect.rollBackUserTransaction(userTxn);
			throw e;
		} catch (Exception e) {
			DBConnect.rollBackUserTransaction(userTxn);
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(con);
		}
	}

	//Working And Used
	public void updateTicketCounter() throws SLEException {
		Connection con = null;
		SLESchedulerDaoImpl daoImpl =SLESchedulerDaoImpl.getInstance();
		try {
//			con = DBConnect.getPropFileCon();
			con = DBConnect.getConnection();
			daoImpl.updateTicketCounter(con);
		} catch (SLEException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(con);
		}
	}
	
	public void createDrawFreezeJob() throws SLEException {
		Connection con = null;
		SLESchedulerDaoImpl daoImpl = SLESchedulerDaoImpl.getInstance();
		Map<Integer, List<Integer>> gameAndGameTypeData = new HashMap<Integer, List<Integer>>();
		List<String> drawData = null;
		List<Integer> gameIdList = null;
		int gameId = 0;
		int gameTypeId = 0;
		try {
//			con = DBConnect.getPropFileCon();
			con = DBConnect.getConnection();
			daoImpl.fetchActiveGames(gameAndGameTypeData, con);
			for(Entry<Integer, List<Integer>> entrySet : gameAndGameTypeData.entrySet()) {
				gameId = entrySet.getKey();
				gameIdList = entrySet.getValue();

				Iterator<Integer> itr = gameIdList.iterator();
				while(itr.hasNext()) {
					gameTypeId = itr.next();
					drawData = daoImpl.getNextDrawData(gameId, gameTypeId, con);
					createJob(gameId, gameTypeId, Integer.parseInt(drawData.get(1)), drawData.get(0));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeConnection(con);
		}
	}
	
	public void createJob(int gameId, int gameTypeId, int drawId, String cronExpression) {
		logger.debug("****** Creating Draw Freeze Job for GameID "+gameId+" , GameTpeId "+gameTypeId+" , drawId "+drawId+" and with Cron Expression "+cronExpression);
		Date date = null;
		try {
			JobDetail job1 = JobBuilder.newJob(SLESchedulerJob.class)
					.usingJobData("gameId", gameId).usingJobData("gameTypeId", gameTypeId)
					.usingJobData("drawId", drawId)
					.withIdentity(gameId + "_" + gameTypeId + "_" + drawId + "", "drawFreezeJob")
					.storeDurably(true)
					.build();

			Trigger drawFreezeTrigger = TriggerBuilder
					.newTrigger()
					.withIdentity("drawFreezeTrigger_" + gameId + "_" + gameTypeId + "_" + drawId, "drawFreezeTrigger")
					.withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
					.build();

			date = Util.scheduler.scheduleJob(job1, drawFreezeTrigger);
			logger.debug("Job Created for Date "+date);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void createSendDrawNotificationJob() throws SLEException {
		Connection con = null;
		SLESchedulerDaoImpl daoImpl = SLESchedulerDaoImpl.getInstance();
		Map<Integer, List<Integer>> gameAndGameTypeData = new HashMap<Integer, List<Integer>>();
		List<String> drawData = null;
		List<Integer> gameIdList = null;
		int gameId = 0;
		int gameTypeId = 0;
		try {
			con = DBConnect.getConnection();
			daoImpl.fetchActiveGames(gameAndGameTypeData, con);
			for(Entry<Integer, List<Integer>> entrySet : gameAndGameTypeData.entrySet()) {
				gameId = entrySet.getKey();
				gameIdList = entrySet.getValue();

				Iterator<Integer> itr = gameIdList.iterator();
				while(itr.hasNext()) {
					gameTypeId = itr.next();
					drawData = daoImpl.getNextDrawDataForSendDrawNotification(gameId, gameTypeId, con);
					createSendDrawNotificationJob(gameId, gameTypeId, Integer.parseInt(drawData.get(1)), drawData.get(0));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeConnection(con);
		}
	}
	
	public void createSendDrawNotificationJob(int gameId, int gameTypeId, int drawId, String cronExpression) {
		logger.debug("****** Creating  Send Draw Notification Job for GameID "+gameId+" , GameTpeId "+gameTypeId+" , drawId "+drawId+" and with Cron Expression "+cronExpression);
		Date date = null;
		try {
			JobDetail job1 = JobBuilder.newJob(SLESendDrawNotificationSchedulerJob.class)
					.usingJobData("gameId", gameId).usingJobData("gameTypeId", gameTypeId)
					.usingJobData("drawId", drawId)
					.withIdentity(gameId + "_" + gameTypeId + "_" + drawId + "", "sendDrawNotification")
					.storeDurably(true)
					.build();

			Trigger drawFreezeTrigger = TriggerBuilder
					.newTrigger()
					.withIdentity("sendDrawNotificatioTrigger_" + gameId + "_" + gameTypeId + "_" + drawId, "sendDrawNotificatioTrigger")
					.withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
					.build();

			date = Util.scheduler.scheduleJob(job1, drawFreezeTrigger);
			logger.debug("Job Created for Date "+date);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void sendDrawNotificationRescheduleActivity(int gameId, int gameTypeId, int drawId, Connection con) throws SLEException {
		SLESchedulerDaoImpl daoImpl = SLESchedulerDaoImpl.getInstance();
		List<String> drawData = null;
		try {
			drawData = daoImpl.getNextDrawDataForSendDrawNotification(gameId, gameTypeId, con);	
			createSendDrawNotificationJob(gameId, gameTypeId, Integer.parseInt(drawData.get(1)), drawData.get(0));
		}  catch (SLEException e) {
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} 
	}


}
