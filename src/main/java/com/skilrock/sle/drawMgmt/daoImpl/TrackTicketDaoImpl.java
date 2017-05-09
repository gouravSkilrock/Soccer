package com.skilrock.sle.drawMgmt.daoImpl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.skilrock.sle.common.SLELogger;
import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.common.javaBeans.ValidateTicketBean;
import com.skilrock.sle.commonMethod.daoImpl.CommonMethodsDaoImpl;
import com.skilrock.sle.drawMgmt.javaBeans.TrackSLETicketBean;
import com.skilrock.sle.gamePlayMgmt.javaBeans.EventDetailBean;

public class TrackTicketDaoImpl {
	private static final SLELogger logger = SLELogger.getLogger(TrackTicketDaoImpl.class.getName());

	private static TrackTicketDaoImpl singleInstance;

	private TrackTicketDaoImpl(){}

	public static TrackTicketDaoImpl getInstance() {
		if (singleInstance == null) {
			synchronized (TrackTicketDaoImpl.class) {
				if (singleInstance == null) {
					singleInstance = new TrackTicketDaoImpl();
				}
			}
		}

		return singleInstance;
	}

	public TrackSLETicketBean trackSLETicket(int merchantId, String ticketNumber, String tktNoWithRpc, int barCodeCount, ValidateTicketBean tktBean, Connection connection) throws SQLException, SLEException {
		TrackSLETicketBean trackTicketBean = new TrackSLETicketBean();

		int gameId;
		int gameTypeId;
		trackTicketBean.setTicketNumber(tktNoWithRpc);
		String barCodeAppender = " ";
		String purchaseTableName = " ";
		String drawStatus = " ";
		if(barCodeCount > 0){
			barCodeAppender = " INNER JOIN st_sl_game_tickets_"+tktBean.getGameid()+"_"+tktBean.getGameTypeId()+" gt ON ticket_number = '"+ticketNumber+"' and barcode_count = "+barCodeCount+" ";
		}
		String query = "SELECT stm.merchant_id, merchant_name, stm.game_id,sgm.game_disp_name,stm.game_type_id, type_disp_name, unit_price, DATE(trans_date) purchase_date, TIME(trans_date) purchase_time, no_of_events FROM st_sl_sale_txn_master stm INNER JOIN st_sl_game_type_master gtmas ON stm.game_id=gtmas.game_id AND stm.game_type_id=gtmas.game_type_id INNER JOIN st_sl_game_type_merchant_mapping gtmap ON stm.game_type_id=gtmap.game_type_id AND stm.merchant_id=gtmap.merchant_id INNER JOIN st_sl_merchant_master mm ON stm.merchant_id=mm.merchant_id INNER JOIN st_sl_game_merchant_mapping sgm on sgm.game_id=stm.game_id and sgm.merchant_id=stm.merchant_id "+barCodeAppender+"  WHERE ticket_nbr="+ticketNumber+" and  stm.merchant_id="+merchantId+";";
		Statement stmt = connection.createStatement();
		logger.info("Select Game Details - "+query);
		ResultSet rs = stmt.executeQuery(query);
		if(rs.next()) {
			/*merchantId = rs.getInt("merchant_id");*/
			gameId = rs.getInt("game_id");
			gameTypeId = rs.getInt("game_type_id");

			trackTicketBean.setMerchantName(rs.getString("merchant_name"));
			trackTicketBean.setGameTypeName(rs.getString("type_disp_name"));
			trackTicketBean.setGameName(rs.getString("game_disp_name"));
			trackTicketBean.setUnitPrice(rs.getDouble("unit_price"));
			trackTicketBean.setPurchaseDate(rs.getString("purchase_date"));
			trackTicketBean.setPurchaseTime(rs.getString("purchase_time"));
			trackTicketBean.setNoOfEvents(rs.getInt("no_of_events"));
		} else {
			throw new SLEException(SLEErrors.INVALID_TICKET_ERROR_CODE, SLEErrors.INVALID_TICKET_ERROR_MESSAGE);
		}

		StringBuilder eventBuilder = new StringBuilder("CONCAT(");
		for(int i=1; i<=trackTicketBean.getNoOfEvents(); i++) {
			eventBuilder.append("event_").append(i).append(",',', ");
		}
		eventBuilder.delete(eventBuilder.lastIndexOf(",',', "), eventBuilder.length());
		eventBuilder.append(")");

		String eventString = null;
		query = "SELECT gt.draw_id, draw_name,gmp.max_board_count,dmas.draw_status,dmas.purchase_table_name,DATE(draw_datetime) draw_date, TIME(draw_datetime) draw_time, board_id, no_of_lines, "+eventBuilder+" events, total_amount,verification_date,claim_start_date,claim_end_date,draw_datetime FROM st_sl_game_tickets_"+gameId+"_"+gameTypeId+" gt INNER JOIN st_sl_draw_master_"+gameId+" dmas ON gt.draw_id=dmas.draw_id INNER JOIN st_sl_draw_merchant_mapping_"+gameId+" dmap ON dmas.draw_id=dmap.draw_id AND dmap.merchant_id="+merchantId+"  inner join st_sl_game_merchant_mapping gmp on dmap.merchant_id=gmp.merchant_id and gmp.game_id="+gameId+" and gmp.merchant_id="+merchantId+" WHERE ticket_number="+ticketNumber+";";
		logger.info("Select Draw Details - "+query);
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			eventString = rs.getString("events");
			purchaseTableName = rs.getString("purchase_table_name");

			trackTicketBean.setDrawId(rs.getInt("draw_id"));
			trackTicketBean.setDrawName(rs.getString("draw_name"));
			trackTicketBean.setDrawDate(rs.getString("draw_date"));
			trackTicketBean.setDrawTime(rs.getString("draw_time"));
			trackTicketBean.setNoOfLines(rs.getInt("no_of_lines"));
			trackTicketBean.setNoOfBoard(rs.getInt("max_board_count"));
			trackTicketBean.setTotalAmount(rs.getDouble("total_amount"));
			Timestamp currentDate = Util.getCurrentTimeStamp();
			Timestamp claimStartDate = rs.getTimestamp("claim_start_date");
			Timestamp claimEndDate = rs.getTimestamp("claim_end_date");
			Timestamp verificationDate = rs.getTimestamp("verification_date");
			drawStatus = rs.getString("draw_status");
			
			if("RMS".equals(Util.fetchMerchantInfoBean(merchantId).getMerchantDevName()) || "OKPOS".equalsIgnoreCase(Util.fetchMerchantInfoBean(merchantId).getMerchantDevName()) || "Asoft".equalsIgnoreCase(Util.fetchMerchantInfoBean(merchantId).getMerchantDevName())){
				if("CLAIM ALLOW".equalsIgnoreCase(drawStatus)){
					if(currentDate.before(verificationDate)){
						 drawStatus = "VERIFICATION PENDING";
					}
					else if (currentDate.before(claimStartDate)) {
						drawStatus = "CLAIM PENDING";
					}
					else if (currentDate.after(claimEndDate)) {
						drawStatus = "DRAW EXPIRED";
					}
					
				 } else {
					 if("CANCEL".equalsIgnoreCase(drawStatus)){
						 drawStatus="DRAW CANCELLED";
					 }else if("ACTIVE".equalsIgnoreCase(drawStatus)){
						drawStatus="ACTIVE";
					 } else if (currentDate.after(claimEndDate)) {
						drawStatus = "DRAW EXPIRED";
					 }else{
						 drawStatus = "RESULT AWAITED";
					}
				 }
			}else {
				 if (currentDate.after(claimEndDate)) {
					drawStatus = "DRAW EXPIRED";
				 }else if("ACTIVE".equalsIgnoreCase(drawStatus)){
					drawStatus="ACTIVE";
				} else if("CANCEL".equalsIgnoreCase(drawStatus)){
					drawStatus="DRAW CANCELLED";
				 }else if("FREEZE".equalsIgnoreCase(drawStatus)){
	         		drawStatus = "RESULT AWAITED";
	         	}
			}
			trackTicketBean.setDrawStatus(drawStatus);	 
		}  
		
		
		/*win data*/
		if("CLAIM ALLOW".equals(trackTicketBean.getDrawStatus()) || "CLAIM PENDING".equalsIgnoreCase(trackTicketBean.getDrawStatus())){
			query = "SELECT SUM(dt.bet_amount_multiple*dt.unit_price*prize_amount)/pd.unit_price winAmt FROM st_sl_draw_ticket_"+gameId+"_"+gameTypeId+"_"+purchaseTableName+" dt INNER JOIN st_sl_prize_details_"+gameId+"_"+gameTypeId+" pd ON dt.merchant_id=pd.merchant_id AND dt.rank_id=pd.prize_rank AND dt.draw_id=pd.draw_id WHERE dt.draw_id=" +trackTicketBean.getDrawId()+ " AND rank_id >0 AND dt.merchant_id="+merchantId+"  AND ticket_number="+ticketNumber+" group by ticket_number";
				  rs = stmt.executeQuery(query);
				  if(rs.next()){
					  trackTicketBean.setTotalWinAmt((rs.getDouble("winAmt"))); 
				  }
			 if("PMS".equals(Util.fetchMerchantInfoBean(merchantId).getMerchantDevName())){
				double winAmt=CommonMethodsDaoImpl .getInstance().fetchWinningAmtForTicket(ticketNumber, connection);
				if(trackTicketBean.getTotalWinAmt()>0 && winAmt==0){
					trackTicketBean.setWinStatus("SETTLEMENT PENDING");
				}
			 }
		}
		
		
		List<EventDetailBean> eventDetails = new ArrayList<EventDetailBean>();
		EventDetailBean eventBean = null;
		query = "SELECT em.event_id, event_display, event_description, GROUP_CONCAT(option_code ORDER By eom.evt_opt_id SEPARATOR ',') opt_code, GROUP_CONCAT(option_name ORDER BY eom.evt_opt_id SEPARATOR ',') opt_name FROM st_sl_event_option_mapping eom INNER JOIN st_sl_event_master em ON eom.event_id=em.event_id WHERE evt_opt_id IN ("+eventString+") AND eom.game_id="+gameId+" and eom.game_type_id="+gameTypeId+" GROUP BY eom.event_id ORDER BY event_Id asc;";
		logger.info("Select Event Details - "+query);
		rs = stmt.executeQuery(query);
		while(rs.next()) {
			eventBean = new EventDetailBean();
			eventBean.setEventId(rs.getInt("event_id"));
			eventBean.setEventDisplay(rs.getString("event_display"));
			eventBean.setEventDescription(rs.getString("event_description"));
			eventBean.setOptionCode(rs.getString("opt_code"));
			eventBean.setOptionName(rs.getString("opt_name"));
			eventDetails.add(eventBean);
		}
		trackTicketBean.setEventDetails(eventDetails);

		return trackTicketBean;
	}
}