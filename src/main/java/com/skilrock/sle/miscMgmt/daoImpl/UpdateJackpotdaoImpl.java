package com.skilrock.sle.miscMgmt.daoImpl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;

import com.skilrock.sle.common.DBConnect;
import com.skilrock.sle.common.SLELogger;
import com.skilrock.sle.common.Util;
import com.skilrock.sle.miscMgmt.javabeans.jackpotBean;

public class UpdateJackpotdaoImpl {

	private static final SLELogger logger = SLELogger
			.getLogger(UpdateJackpotdaoImpl.class.getName());

	private static UpdateJackpotdaoImpl instance;

	private UpdateJackpotdaoImpl() {
	}

	public static UpdateJackpotdaoImpl getInstance() {
		if (instance == null) {
			synchronized (UpdateJackpotdaoImpl.class) {
				if (instance == null) {
					instance = new UpdateJackpotdaoImpl();
				}
			}
		}

		return instance;
	}

	public  jackpotBean fetchJackpotMenu(Connection connection) {
		Statement stmt = null;
		ResultSet rs=null;
		String query=null;
		double carryForward=0.0;
		int tableId=0;
		double saleAmt=0.0;
		int activeDraw=0;
		jackpotBean bean=new jackpotBean();
		
		try{
			int drawId = 0;
			stmt = connection.createStatement();
			query="select draw_id,draw_status from st_sl_draw_master_1 where draw_id=(select max(draw_id) from st_sl_draw_master_1 where draw_datetime<'"+Util.getCurrentTimeString()+"');";
			rs=stmt.executeQuery(query);
			if(rs.next()){
				drawId = rs.getInt("draw_id");
				bean.setStatus(rs.getString("draw_status"));
			}
			query="select carried_over_jackpot from st_sl_miscellaneous_1_1 where draw_id="+drawId+";";
				rs=stmt.executeQuery(query);
			if(rs.next()){
				carryForward=rs.getDouble("carried_over_jackpot");
				bean.setCarryForward(carryForward);
			} 
		query="select purchase_table_name,draw_id from st_sl_draw_master_1 where draw_id =(select draw_id from st_sl_draw_master_1 where draw_status='ACTIVE' order by draw_datetime asc limit 1);";
		rs=stmt.executeQuery(query);
		if(rs.next()){
		tableId=rs.getInt("purchase_table_name");
		activeDraw=rs.getInt("draw_id");
		}
		query="SELECT COUNT(1) noOfsale, SUM(unit_price*bet_amount_multiple) saleAmount FROM st_sl_draw_ticket_1_1_"+tableId+" a left join st_sl_sale_txn_master b on a.ticket_number=b.ticket_nbr where b.status!='INITIATED' AND a.draw_id=("+activeDraw+") and  a.status NOT IN ('CANCELLED','FAILED');";
		rs=stmt.executeQuery(query);
		if(rs.next()){
			saleAmt=rs.getDouble("saleAmount");
			bean.setSaleAmt(saleAmt);
			}
		}catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();

		} finally {
			DBConnect.closeStatement(stmt);
		}return bean;
		
	}
	
	public void jackpotDetailsUpdation(long userId, double amount,String messageAmount,Connection connection) {
		
		DecimalFormat df = new DecimalFormat("###0.00");
		String bal = df.format(amount);

		Statement stmt = null;
		try {
			if (bal !=Util.getPropertyValue("HARDCODED_JACKPOT_AMOUNT")) {
				stmt = connection.createStatement();
				String query = "insert into st_sl_property_master_history (property_code,property_display_name,status,value,updated_date,updated_by_user_id) select property_code,property_display_name,status,value,'"
						+ Util.getCurrentTimeStamp()
						+ "','"
						+ userId
						+ "' from st_sl_property_master where property_dev_name='HARDCODED_JACKPOT_AMOUNT'";
				logger.info("entry in property history Query - " + query);
				stmt.executeUpdate(query);
				
				query = " update `st_sl_property_master` set value='"
						+ bal
						+ "' where property_dev_name='HARDCODED_JACKPOT_AMOUNT'";
				logger.info("amount updation Query - " + query);
				stmt.executeUpdate(query);
				Util.sysPropertiesMap.put("HARDCODED_JACKPOT_AMOUNT", bal);
			}

			if (messageAmount !=Util.getPropertyValue("JACKPOT_DISPLAY_AMOUNT")) {
				stmt = connection.createStatement();
				String query = "insert into st_sl_property_master_history (property_code,property_display_name,status,value,updated_date,updated_by_user_id) select property_code,property_display_name,status,value,'"
						+ Util.getCurrentTimeStamp()
						+ "','"
						+ userId
						+ "' from st_sl_property_master where property_dev_name='JACKPOT_DISPLAY_AMOUNT'";
				logger.info("entry in property history Query - " + query);
				stmt.executeUpdate(query);
				
				query = " update `st_sl_property_master` set value='"
						+ messageAmount
						+ "' where property_dev_name='JACKPOT_DISPLAY_AMOUNT'";
				logger.info("amount updation Query - " + query);
				stmt.executeUpdate(query);
				Util.sysPropertiesMap.put("JACKPOT_DISPLAY_AMOUNT", messageAmount);
			}

		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();

		} finally {
			DBConnect.closeStatement(stmt);
		}
	}

	

}
