package com.skilrock.sle.drawMgmt.controllerImpl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.transaction.UserTransaction;

import org.quartz.CronScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;

import com.google.gson.JsonObject;
import com.skilrock.sle.common.DBConnect;
import com.skilrock.sle.common.SLELogger;
import com.skilrock.sle.common.TransactionManager;
import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.exception.GenericException;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.common.javaBeans.UserInfoBean;
import com.skilrock.sle.commonMethod.daoImpl.CommonMethodsDaoImpl;
import com.skilrock.sle.drawMgmt.daoImpl.DrawMgmtDaoImpl;
import com.skilrock.sle.drawMgmt.javaBeans.DrawInfoBean;
import com.skilrock.sle.drawMgmt.javaBeans.DrawInfoMerchantWiseBean;
import com.skilrock.sle.drawMgmt.javaBeans.SlePwtBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.MappedEventMasterBean;
import com.skilrock.sle.merchant.TpIntegrationImpl;
import com.skilrock.sle.merchant.common.javaBeans.MerchantInfoBean;

import com.skilrock.sle.merchant.lms.ServiceName;
import com.skilrock.sle.merchant.pms.ServiceMethodName;
import com.skilrock.sle.scheduler.daoImpl.SLESchedulerDaoImpl;
import com.skilrock.sle.tp.rest.common.javaBeans.TpUserRegistrationBean;

public class DrawMgmtControllerImpl {
	private static final SLELogger logger = SLELogger.getLogger(DrawMgmtControllerImpl.class.getName());

	private static volatile DrawMgmtControllerImpl classInstance;

	public static DrawMgmtControllerImpl getInstance() {
		if (classInstance == null) {
			synchronized (DrawMgmtControllerImpl.class) {
				if (classInstance == null) {
					classInstance = new DrawMgmtControllerImpl();
				}
			}
		}
		return classInstance;
	}

	public void registerMerchantUser(TpUserRegistrationBean userRegistrationRequestBean)throws GenericException {
		logger.debug("***** Inside registerUser Method");
		Connection conn = null;
		UserTransaction userTxn = null;
		try {
			conn = DBConnect.getConnection();
			userTxn = DBConnect.startTransaction();
			userRegistrationRequestBean.setMerchantId(TransactionManager.getMerchantId());
			CommonMethodsDaoImpl.getInstance().registerMerchantUser(userRegistrationRequestBean, conn);
			userTxn.commit();
		} catch (GenericException e) {
			DBConnect.rollBackUserTransaction(userTxn);
			throw e;
		} catch (Exception e) {
			DBConnect.rollBackUserTransaction(userTxn);
			e.printStackTrace();
			throw new GenericException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE);
		} finally {
			DBConnect.closeConnection(conn);
		}
	}

	public SlePwtBean getSportsLotteryDrawData(SlePwtBean slePwtBean) {
		logger.debug("***** Inside getSportsLotteryDrawData Method");
		Connection conn = null;
		try {
			conn = DBConnect.getConnection();
			slePwtBean = DrawMgmtDaoImpl.getInstance().getSportsLotteryDrawData(slePwtBean, conn);
		} catch (Exception e) {
			e.printStackTrace();
			slePwtBean.setRespnseCode(1);
			slePwtBean.setResponseMsg(SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(conn);
		}
		return slePwtBean;
	}

	public void fetchWinningAmountDateAndDrawWise(List<DrawInfoMerchantWiseBean> drawInfoMerchantWiseBeans,	String startDate, String endDate, int gameId, int gameTypeId, String merchantCode) throws SLEException {
		logger.info("***** Inside fetchWinningAmountDateAndDrawWise Method");
		Connection con = null;

		try {
			con = DBConnect.getConnection();
			DrawMgmtDaoImpl.getInstance().fetchWinningAmountDateAndDrawWise(drawInfoMerchantWiseBeans, startDate, endDate, gameId,gameTypeId, merchantCode, con);
		} catch (SLEException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(con);
		}
	}

	public List<DrawInfoBean> getSportsLotteryDrawDetails(int gameId,int gameTypeId, String startDate, String endDate, int merchantId)throws SLEException {
		logger.info("***** Inside getSportsLotteryDrawDetails Method");
		Connection conn = null;
		List<DrawInfoBean> drawInfoList = null;
		SimpleDateFormat dateFormat = null;
		try {
			conn = DBConnect.getConnection();
			dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			SimpleDateFormat format = null;
			if (startDate != null && endDate != null) {
				format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				startDate = dateFormat.format(format.parse(startDate+ " 00:00:00"));
				endDate = dateFormat.format(format.parse(endDate + " 23:59:59"));

			}
			drawInfoList = DrawMgmtDaoImpl.getInstance().fetchDrawMgmtDrawInfo(gameId, gameTypeId, startDate, endDate, merchantId, conn);
		} catch (SLEException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(conn);
		}
		return drawInfoList;
	}

	public DrawInfoBean fetchDrawInfo(int gameId, int gameTypeId, int drawId,int merchantId) throws SLEException {
		logger.info("***** Inside fetchDrawInfo Method");
		Connection conn = null;
		DrawInfoBean drawInfo = null;
		try {
			conn = DBConnect.getConnection();
			drawInfo = DrawMgmtDaoImpl.getInstance().fetchDrawInfo(gameId, gameTypeId, drawId, merchantId, conn);
		} catch (SLEException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(conn);
		}
		return drawInfo;
	}

	public DrawInfoBean updateDrawInfo(int gameId, int gameTypeId, int drawId,String drawStatus, MerchantInfoBean merchantInfoBean,boolean isLogoutAllRet, int seconds, UserInfoBean 
			userInfoBean)throws SLEException {
		logger.info("***** Inside updateDrawInfo Method");
		Connection conn = null;
		Timestamp currentDrawFreezeTime = null;
		Timestamp updatedDrawFreeTime = null;
		DrawInfoBean drawInfo = null;
		try {
			conn = DBConnect.getConnection();
			drawInfo = DrawMgmtDaoImpl.getInstance().fetchDrawInfoForValidation(gameId, gameTypeId, drawId,merchantInfoBean.getMerchantId(), conn);
			logger.info("Draw Info for change freeze Time" + drawInfo);

			currentDrawFreezeTime = Util.DateToTimeStampConversion(Util.StringToDateConversion(drawInfo.getDrawFreezeTime()));
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(currentDrawFreezeTime.getTime());
			cal.add(Calendar.SECOND, seconds);
			updatedDrawFreeTime = new Timestamp(cal.getTime().getTime());
			if (drawStatus.equalsIgnoreCase(drawInfo.getDrawStatus())) {

				if (drawInfo.getMinEventStartTime() != null	&& updatedDrawFreeTime.after(Util.StringToDateConversion(drawInfo.getMinEventStartTime()))) {
					drawInfo.setUpdatedDrawFreezeTime(currentDrawFreezeTime.toString().replace(".0", ""));
					drawInfo.setMessage("Unsuccessful!! Draw Freeze Time should be less than "+ drawInfo.getMinEventStartTime());
					drawInfo.setEventTimeChangeReq("YES");
				} else if (updatedDrawFreeTime.compareTo(new Timestamp(new Date().getTime())) <= 0) {
					drawInfo.setUpdatedDrawFreezeTime(currentDrawFreezeTime.toString().replace(".0", ""));
					drawInfo.setMessage("Unsuccessful!! Draw Freeze Time should be greater than current time");
				} else if (updatedDrawFreeTime.before(Util.StringToDateConversion(drawInfo.getSaleStartTime()))) {
					drawInfo.setUpdatedDrawFreezeTime(currentDrawFreezeTime.toString().replace(".0", ""));
					drawInfo.setMessage("Unsuccessful!! Draw Freeze Time should be greater than "+ drawInfo.getSaleStartTime());
				} else if (updatedDrawFreeTime.after(Util.StringToDateConversion(drawInfo.getDrawDateTime()))) {
					drawInfo.setUpdatedDrawFreezeTime(currentDrawFreezeTime.toString().replace(".0", ""));
					drawInfo.setMessage("Unsuccessful!! Draw Freeze Time should be less than "+ drawInfo.getDrawDateTime());
				} else {
					drawInfo.setUpdatedDrawFreezeTime(updatedDrawFreeTime.toString().replace(".0", ""));

					
					// validation to check  valid draw freeze time
					List<String> drawFreezeTimeTovalidateList=DrawMgmtDaoImpl.getInstance().getOtherDrawFreezeDataForValidation(gameId, gameTypeId, drawInfo, conn);
					if(drawFreezeTimeTovalidateList.size()>0){
						int i=0;
						for(String drawFreezeTimeTovalidate:drawFreezeTimeTovalidateList){
							i++;
							if(i==1){
								if(updatedDrawFreeTime.compareTo(Util.StringToDateConversion(drawFreezeTimeTovalidate))<=0){
									drawInfo.setUpdatedDrawFreezeTime(currentDrawFreezeTime.toString().replace(".0", ""));
									drawInfo.setMessage("Unsuccessful!! Draw Freeze Time should be greater than "+ drawFreezeTimeTovalidate);
									return drawInfo;
								}
							}else{
								if(updatedDrawFreeTime.compareTo(Util.StringToDateConversion(drawFreezeTimeTovalidate))>=0){
									drawInfo.setUpdatedDrawFreezeTime(currentDrawFreezeTime.toString().replace(".0", ""));
									drawInfo.setMessage("Unsuccessful!! Draw Freeze Time should be less "+ drawFreezeTimeTovalidate);
									return drawInfo;
								}
							}
							
						}
						
					}
					// update draw Freeze data
					DrawMgmtDaoImpl.getInstance().updateDrawFreezeTimeAndHistory(gameId, gameTypeId, drawInfo, merchantInfoBean.getMerchantId(), userInfoBean.getMerchantUserId(),conn);

					// rescheduling and logout code
					rescheduleJob(gameId, gameTypeId, conn, drawId);
					drawInfo.setMessage("Successful!! Draw Freeze Time Changed from "+ drawInfo.getDrawFreezeTime()+ " to "+ drawInfo.getUpdatedDrawFreezeTime() + "");
					drawInfo.setDrawFreezeTime(drawInfo.getUpdatedDrawFreezeTime());
				}

			} else {
				drawInfo.setMessage("Unsuccessful!! Current Status is "+ drawInfo.getDrawStatus());
				drawInfo.setUpdatedDrawFreezeTime(currentDrawFreezeTime.toString().replace(".0", ""));
			}

		} catch (SLEException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(conn);
		}
		return drawInfo;
	}

	public static void rescheduleJob(int gameId, int gameTypeId, Connection con, int drawId) throws SLEException {
		List<String> drawData = null;
		String cronExpression = "";
		Date date = null;

		try {
			drawData = DrawMgmtDaoImpl.getInstance().getNextDrawData(gameId, gameTypeId, drawId, con);
			cronExpression = drawData.get(0);

			if (Util.scheduler.checkExists(new TriggerKey("drawFreezeTrigger_"+ gameId + "_" + gameTypeId + "_" + drawId,"drawFreezeTrigger"))) {
				logOutAllRets();
				Trigger drawFreezeTrigger = TriggerBuilder
					.newTrigger()
					.withIdentity("drawFreezeTrigger_" + gameId + "_" + gameTypeId + "_" + drawId,"drawFreezeTrigger")
					.withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
					.build();

				date = Util.scheduler.rescheduleJob(new TriggerKey("drawFreezeTrigger_" + gameId + "_" + gameTypeId + "_" + drawId, "drawFreezeTrigger"),drawFreezeTrigger);
				logger.info("Job rescheduled for Date: " + date + " ,for draw Id: " + drawId + " , for game Type Id "+ gameTypeId);
			} else {
				logger.info("Job isn't scheduled for draw Id: " + drawId + " , for game Type Id " + gameTypeId+ " hence no need to reschedule this draw");
			}

			// comment this code from here
		/*	System.out.println("Existance" + Util.scheduler.checkExists(new TriggerKey("drawFreezeTrigger_" + gameId + "_" + gameTypeId + "_" + drawId,"drawFreezeTrigger")));
			System.out.println("Size "+Util.scheduler.getCurrentlyExecutingJobs().size());
			
  		    for (String groupName : Util.scheduler.getJobGroupNames()) {
			     for (JobKey jobKey : Util.scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
					  String jobName = jobKey.getName();
					  String jobGroup = jobKey.getGroup();
					  List<Trigger> triggers = (List<Trigger>) Util.scheduler.getTriggersOfJob(jobKey);
					  if(triggers.size()>0){
						  nextFireTime = triggers.get(0).getNextFireTime();
					  } 
					
					  System.out.println("[jobName] : " + jobName + " [groupName] : "+ jobGroup + " - " + nextFireTime);
			     }
			}*/
// end	
			
		} catch (Exception e) {
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} 
	}
	
	public DrawInfoBean updateDrawSaleTime(int gameId,int gameTypeId,int drawId,String drawStatus,MerchantInfoBean merchantInfoBean,boolean isLogoutAllRet,int seconds,UserInfoBean
			userInfoBean) throws SLEException{
		logger.info("***** Inside updateDrawSaleTime Method");
		Connection conn = null;
		Timestamp currentDrawSaleTime=null;
		Timestamp updatedDrawSaleTime=null;
		DrawInfoBean drawInfo=null;
		try {
			conn = DBConnect.getConnection();
			drawInfo=DrawMgmtDaoImpl.getInstance().fetchDrawInfoForValidation(gameId, gameTypeId, drawId, merchantInfoBean.getMerchantId(),conn);
			logger.info("Draw Info for change draw sale  Time"+drawInfo);
			
			currentDrawSaleTime=Util.DateToTimeStampConversion(Util.StringToDateConversion(drawInfo.getSaleStartTime()));
			Calendar cal=Calendar.getInstance();
			cal.setTimeInMillis(currentDrawSaleTime.getTime());
			cal.add(Calendar.SECOND, seconds);
			updatedDrawSaleTime = new Timestamp(cal.getTime().getTime());
			if(drawStatus.equalsIgnoreCase(drawInfo.getDrawStatus())){
				
				if(updatedDrawSaleTime.after(Util.StringToDateConversion(drawInfo.getDrawFreezeTime()))){
					drawInfo.setUpdatedSaleStartTime(currentDrawSaleTime.toString().replace(".0", ""));
					drawInfo.setMessage("Unsuccessful!! Draw Sale Start Time should be less than "+drawInfo.getDrawFreezeTime());
				} else if(updatedDrawSaleTime.compareTo(new Timestamp(new Date().getTime())) <= 0){
					drawInfo.setUpdatedSaleStartTime(currentDrawSaleTime.toString().replace(".0", ""));
					drawInfo.setMessage("Unsuccessful!! Draw Sale Start Time should be greater than current time");
				} else if(currentDrawSaleTime.before(Util.StringToDateConversion(Util.getCurrentTimeString()))){
					drawInfo.setUpdatedSaleStartTime(currentDrawSaleTime.toString().replace(".0", ""));
					drawInfo.setMessage("Unsuccessful!! Draw Sale Already Started");
				}else{
					drawInfo.setUpdatedSaleStartTime(updatedDrawSaleTime.toString().replace(".0", ""));
					
					
					
					// validation to check  valid draw sale time
					List<String> drawSaleTimeTovalidateList=DrawMgmtDaoImpl.getInstance().getOtherDrawSaleDataForValidation(gameId, gameTypeId, drawInfo, conn);
					if(drawSaleTimeTovalidateList.size()>0){
						int i=0;
						for(String drawSaleTimeTovalidate:drawSaleTimeTovalidateList){
							i++;
							if(i==1){
								if(updatedDrawSaleTime.compareTo(Util.StringToDateConversion(drawSaleTimeTovalidate))<=0){
									drawInfo.setUpdatedDrawFreezeTime(currentDrawSaleTime.toString().replace(".0", ""));
									drawInfo.setMessage("Unsuccessful!! Draw sale start Time should be greater than "+ drawSaleTimeTovalidate);
									return drawInfo;
								}
							}else{
								if(updatedDrawSaleTime.compareTo(Util.StringToDateConversion(drawSaleTimeTovalidate))>=0){
									drawInfo.setUpdatedDrawFreezeTime(currentDrawSaleTime.toString().replace(".0", ""));
									drawInfo.setMessage("Unsuccessful!! Draw sale start Time should be less than "+ drawSaleTimeTovalidate);
									return drawInfo;
								}
							}
							
						}
						
					}
					
					
					//update draw data 
					DrawMgmtDaoImpl.getInstance().updateDrawSaleTimeAndHistory(gameId, gameTypeId, drawInfo, merchantInfoBean.getMerchantId(), userInfoBean.getMerchantUserId(),conn);
					logOutAllRets();
					if(currentDrawSaleTime.after(Util.StringToDateConversion(Util.getCurrentTimeString()))){
						rescheduleSendSaleNotificationJob(gameId, gameTypeId, conn, drawId);
					}
					
					drawInfo.setMessage("Successful!! Draw Sale Start Time Changed from "+drawInfo.getSaleStartTime()+" to "+drawInfo.getUpdatedSaleStartTime()+"");
					drawInfo.setSaleStartTime(drawInfo.getUpdatedSaleStartTime());
				}
				
				
			} else{
				drawInfo.setMessage("Unsuccessful!! Current Status is "+ drawInfo.getDrawStatus());
				drawInfo.setUpdatedSaleStartTime(currentDrawSaleTime.toString().replace(".0", ""));
			}
			
		}catch (SLEException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(conn);
		}
		return drawInfo;
	}
	
	
	public static void rescheduleSendSaleNotificationJob(int gameId, int gameTypeId, Connection con, int drawId) throws SLEException {
		List<String> drawData = null;
		String cronExpression = "";
		Date date = null;

		try {
		/*	System.out.println("Existance" + Util.scheduler.checkExists(new TriggerKey("sendDrawNotificatioTrigger_" + gameId + "_" + gameTypeId + "_" + drawId,"sendDrawNotificatioTrigger")));
			System.out.println("Size "+Util.scheduler.getCurrentlyExecutingJobs().size());
			
  		    for (String groupName : Util.scheduler.getJobGroupNames()) {
			     for (JobKey jobKey : Util.scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
					  String jobName = jobKey.getName();
					  String jobGroup = jobKey.getGroup();
					  List<Trigger> triggers = (List<Trigger>) Util.scheduler.getTriggersOfJob(jobKey);
					  if(triggers.size()>0){
						  nextFireTime = triggers.get(0).getNextFireTime();
					  } 
					
					  System.out.println("[jobName] : " + jobName + " [groupName] : "+ jobGroup + " - " + nextFireTime);
			     }
			}
  		    
  		    System.out.println("Nextttttttttttttttttttttt");*/
			SLESchedulerDaoImpl daoImpl = SLESchedulerDaoImpl.getInstance();
			drawData = daoImpl.getNextDrawDataForSendDrawNotification(gameId, gameTypeId, con);		
			cronExpression = drawData.get(0);
		
  		    if(Util.scheduler.checkExists(new TriggerKey("sendDrawNotificatioTrigger_" + gameId + "_" + gameTypeId + "_" + drawId,"sendDrawNotificatioTrigger"))){
					Trigger drawSaleNotificationTrigger = TriggerBuilder
							.newTrigger()
							.withIdentity("sendDrawNotificatioTrigger_" + gameId + "_" + gameTypeId + "_" + drawId, "sendDrawNotificatioTrigger")
							.withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
							.build();
		
					date = Util.scheduler.rescheduleJob(new TriggerKey("sendDrawNotificatioTrigger_" + gameId + "_" + gameTypeId + "_" + drawId,"sendDrawNotificatioTrigger"), drawSaleNotificationTrigger);
					logger.info("Job rescheduled for Date: "+date+" ,for draw Id: "+drawId+" , for game Type Id "+gameTypeId);
  		    } else{
  		    		logger.info("Job isn't scheduled for draw Id: "+drawId+" , for game Type Id "+gameTypeId+" hence no need to reschedule this draw");
  		    }
  		    
  		//comment this code from here
		/*	System.out.println("Existance" + Util.scheduler.checkExists(new TriggerKey("sendDrawNotificatioTrigger_" + gameId + "_" + gameTypeId + "_" + drawId,"sendDrawNotificatioTrigger")));
			System.out.println("Size "+Util.scheduler.getCurrentlyExecutingJobs().size());
			
  		    for (String groupName : Util.scheduler.getJobGroupNames()) {
			     for (JobKey jobKey : Util.scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
					  String jobName = jobKey.getName();
					  String jobGroup = jobKey.getGroup();
					  List<Trigger> triggers = (List<Trigger>) Util.scheduler.getTriggersOfJob(jobKey);
					  if(triggers.size()>0){
						  nextFireTime = triggers.get(0).getNextFireTime();
					  } 
					
					  System.out.println("[jobName] : " + jobName + " [groupName] : "+ jobGroup + " - " + nextFireTime);
			     }
			}*/
			// end

		} catch (Exception e) {
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
	}

	public static void logOutAllRets() throws SLEException {
		JsonObject reqObj = null;
		try {
			reqObj = new JsonObject();
			TpIntegrationImpl.getLMSResponseString(ServiceName.USER_MGMT,ServiceMethodName.LOGOUT_ALL_RETAILERS, reqObj);
		} catch (Exception e) {
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
	}

	public void insertLeague(int gameId, String leagueName) throws SLEException {
		Connection con = null;
		try {
			con = DBConnect.getConnection();
			DrawMgmtDaoImpl.getInstance().addLeagues(con, gameId, leagueName);
		}catch (SLEException e) {
			throw e;
		} finally {
			DBConnect.closeConnection(con);
		}
	}

	public void insertTeam(int gameId, String teamName, String teamCode) throws SLEException {
		Connection con = null;
		try {
			con = DBConnect.getConnection();
			DrawMgmtDaoImpl.getInstance().addTeams(con, gameId, teamName, teamCode);
		}catch (SLEException e) {
			throw e;
		}finally {
			DBConnect.closeConnection(con);
		}
	}
	
	public void mapLeagueTeam(int gameId,int leagueId,String teamId) throws SLEException{
		Connection con = null;
		try {
			con = DBConnect.getConnection();
			DrawMgmtDaoImpl.getInstance().mapLeagueTeams(con, gameId, leagueId, teamId);
		}catch (SLEException e) {
			throw e;
		}finally {
			DBConnect.closeConnection(con);
		}
	}
	
	public void updateTeamInfo(int teamId,String teamName,String teamCode) throws SLEException{
		Connection con = null;
		try {
			con = DBConnect.getConnection();
			DrawMgmtDaoImpl.getInstance().updateTeamData(con, teamId, teamName, teamCode);
		}catch (SLEException e) {
			throw e;
		} finally {
			DBConnect.closeConnection(con);
		}
	}
	
	public void updateMappedEvents(int eventId,int leagueId,int venueId,int homeTeamId,int awayTeamId,String startTime,String endTime,String homeTeamOdds,String awayTeamOdds,String drawOdds,String venue_Name,UserInfoBean userInfoBean) throws SLEException{
		Connection con = null;
		PreparedStatement insertPstmt = null;
		ResultSet insertRs =null;
		int cityId=0;
		try {
			con = DBConnect.getConnection();
			startTime=(startTime+":00").replaceAll("/", "-");
			endTime=(endTime+":00").replaceAll("/", "-");
			if(venueId == -1){
				insertPstmt = con.prepareStatement("INSERT INTO st_sl_venue_master (venue_display_name, venue_code, city_id, status) VALUES(?,?,?,?);", Statement.RETURN_GENERATED_KEYS);
				insertPstmt.setString(1,venue_Name);
				insertPstmt.setString(2,venue_Name.substring(0, 3));
				insertPstmt.setInt(3, cityId);
				insertPstmt.setString(4, "ACTIVE");
				insertPstmt.executeUpdate();
				insertRs = insertPstmt.getGeneratedKeys();
				if(insertRs.next()){
					venueId = insertRs.getInt(1);
				}
			}
			MappedEventMasterBean eventDataBean=new MappedEventMasterBean();
			eventDataBean.setEventId(eventId);
			eventDataBean.setLeagueId(leagueId);
			eventDataBean.setVenueId(venueId);
			eventDataBean.setHomeTeamId(homeTeamId);
			eventDataBean.setAwayTeamId(awayTeamId);
			eventDataBean.setStartTime(startTime);
			eventDataBean.setEndTime(endTime);
			eventDataBean.setHomeTeamOdds(homeTeamOdds);
			eventDataBean.setAwayTeamOdds(awayTeamOdds);
			eventDataBean.setDrawOdds(drawOdds);
			
			DrawMgmtDaoImpl.getInstance().updateEventData(eventDataBean,con,userInfoBean);
			
		} catch (SLEException e) {
			throw e;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeConnection(con);
			DBConnect.closeConnection(insertPstmt, insertRs);
		}

	}
}
