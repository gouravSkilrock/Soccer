package com.skilrock.sle.scheduler.common;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.sle.common.DBConnect;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;



public class SchedulerCommonFuntionsHelper {
	private static Log logger = LogFactory.getLog(SchedulerCommonFuntionsHelper.class);


public static void updateSchedulerStart(int jobId) throws SLEException{
	Connection con =null;
	try{
		con =DBConnect.getConnection();
		con.setAutoCommit(false);
		updateSchedulerStart(jobId, con);
		con.commit();
	}catch (Exception e) {
		logger.info("Exception ",e);
		throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
	}finally{
		DBConnect.closeConnection(con);
	}
}
public static void updateSchedulerStart(int jobId, Connection con) throws SLEException{
	Statement stmt =null;
	try{

		
		String qry = "update st_sl_generic_scheduler_master set last_start_time = now(), last_status='RUNNING' where id="+jobId;
		logger.info("update Scheduler  "+qry);
		stmt = con.createStatement();
		int updated =stmt.executeUpdate(qry);
		logger.info("Row Updated: "+updated);
		
	}catch (SQLException e) {
		logger.error("SQL Exception ",e);
		throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE,SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
	}catch (Exception e) {
		logger.error("Exception ",e);
		throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
	}finally{
		DBConnect.closeStmt(stmt);
	}
	
}

public static void updateSchedulerEnd(int jobId) throws SLEException{
	Connection con =null;
	try{
		con =DBConnect.getConnection();
		con.setAutoCommit(false);
		updateSchedulerEnd(jobId, con);
		con.commit();
	}catch (Exception e) {
		logger.info("Exception ",e);
		throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
	}finally{
		DBConnect.closeConnection(con);
	}
}
public static void updateSchedulerEnd(int jobId, Connection con) throws SLEException{
	Statement stmt =null;
	try{
		String qry = "update st_sl_generic_scheduler_master set last_end_time = now() , status_msg = 'Success',last_status='DONE',last_success_time=now()  where id="+jobId;
		logger.info("update Scheduler  "+qry);
		stmt = con.createStatement();
		int updated =stmt.executeUpdate(qry);
	
		logger.info("Row Updated: "+updated);
		
	}catch (SQLException e) {
		logger.error("SQL Exception ",e);
		throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE,SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
	}catch (Exception e) {
		logger.error("Exception ",e);
		throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
	}finally{
		DBConnect.closeStmt(stmt);
	}
	
}

public static void updateSchedulerError(int jobId,String errorMsg) throws SLEException{
	Connection con =null;
	Statement stmt =null;
	try{
		con =DBConnect.getConnection();
		con.setAutoCommit(false);
		String qry = "update st_sl_generic_scheduler_master set last_end_time = now() , status_msg = '"+errorMsg+"' , last_status='ERROR' where id="+jobId;
		logger.info("update Scheduler  "+qry);
		stmt = con.createStatement();
		int updated =stmt.executeUpdate(qry);
		con.commit();
		logger.info("Row Updated: "+updated);
		
	}catch (SQLException e) {
		logger.error("SQL Exception ",e);
		throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE,SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
	}catch (Exception e) {
		logger.error("Exception ",e);
		throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
	}finally{
		DBConnect.closeConnection(con);
		DBConnect.closeStmt(stmt);
	}
	
}

public static 	Map<String,SchedulerDetailsBean> getSchedulerBeanMap(String jobGroup) throws SLEException{
	Connection con =null;
	Statement stmt =null;
	ResultSet rs =null;
	Map<String,SchedulerDetailsBean> scheBeanMap = new HashMap<String ,SchedulerDetailsBean>();
	try{
		con =DBConnect.getConnection();
		stmt = con.createStatement();
		
		String qry = "select id,scheduler_dev_name,status from st_sl_generic_scheduler_master where jobGroup ='"+jobGroup+"'";
		rs = stmt.executeQuery(qry);
		
		while (rs.next()) {
			SchedulerDetailsBean scheDetailsBean = new SchedulerDetailsBean();
			scheDetailsBean.setActive(rs.getString("status").equalsIgnoreCase("ACTIVE"));
			scheDetailsBean.setJobId(rs.getInt("id"));
			scheDetailsBean.setJobDevName(rs.getString("scheduler_dev_name"));
			scheBeanMap.put(rs.getString("scheduler_dev_name"),scheDetailsBean );
		}
		
	}catch (SQLException e) {
		logger.error("SQL Exception ",e);
		throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE,SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
	}catch (Exception e) {
		logger.error("Exception ",e);
		throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
	}finally{
		DBConnect.closeConnection(con, stmt, rs);
	}
	return scheBeanMap ;
	
}	
}
