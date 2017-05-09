package com.skilrock.sle.mobile.reportsMgmt.daoImpl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.skilrock.sle.common.SLELogger;
import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.UtilityFunctions;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.common.javaBeans.ReportBean;
import com.skilrock.sle.common.javaBeans.TicketInfoBean;
import com.skilrock.sle.common.javaBeans.UserInfoBean;
import com.skilrock.sle.gamePlayMgmt.javaBeans.ReprintTicketBean;

public class SportsLotteryMobileReportsDaompl {
	private static final SLELogger logger = SLELogger.getLogger(SportsLotteryMobileReportsDaompl.class.getName());

	public Map<Long, TicketInfoBean> fetchPurchaseTicketReport(UserInfoBean userInfoBean, ReportBean reportBean, Connection con) throws SLEException {
		logger.info("***** Inside fetchPurchaseTicketReport Method");
		Statement stmt = null;
		ResultSet rs = null;
		TicketInfoBean tktInfoBean = null;
		Statement boardStmt = null;
		ResultSet boardRs = null;
		Map<Long, TicketInfoBean> tktInfoMap = new LinkedHashMap<Long, TicketInfoBean>();
		StringBuilder query = new StringBuilder();
		String winQuery;
		String criteriaAppender = null;
		try {
				if("MOBILE".equalsIgnoreCase(reportBean.getReportChannel())){
					criteriaAppender = " ORDER BY trans_id DESC LIMIT 10";
				}else{
					criteriaAppender = " AND trans_date >'"+reportBean.getStartDate()+"' and trans_date<'"+reportBean.getEndDate()+"' ";
				}
			
			query.append("SELECT trans_id, game_id, game_type_id, ticket_nbr, amount, trans_date FROM st_sl_sale_txn_master WHERE merchant_user_id = ")
					.append(userInfoBean.getMerchantUserId())
					.append(" AND  merchant_id = ")
					.append(userInfoBean.getMerchantId())
					.append(" AND STATUS = 'DONE' AND is_cancel = 'N' ")
					.append(criteriaAppender);
			logger.info("Query For Fetching Sale Txn for "+reportBean.getReportChannel()+" - "+query.toString());
			stmt = con.createStatement();
			
			rs = stmt.executeQuery(query.toString());
			while(rs.next()) {
				tktInfoBean = new TicketInfoBean();
				tktInfoBean.setTxnId(rs.getLong("trans_id"));
				tktInfoBean.setGameId(rs.getInt("game_id"));
				tktInfoBean.setGameTypeId(rs.getInt("game_type_id"));
				tktInfoBean.setTktNbr(rs.getLong("ticket_nbr"));
				tktInfoBean.setAmount(rs.getDouble("amount"));
				tktInfoBean.setTxnDate(UtilityFunctions.timeFormat(rs.getString("trans_date")));
				tktInfoBean.setStatus("TRY_AGAIN");
				
				tktInfoMap.put(tktInfoBean.getTxnId(), tktInfoBean);
			}
			
			for(Entry<Long, TicketInfoBean> entrySet : tktInfoMap.entrySet()) {
				query.setLength(0);
				tktInfoBean = entrySet.getValue();
				query.append("SELECT draw_id FROM st_sl_game_tickets_").append(tktInfoBean.getGameId())
				.append("_").append(tktInfoBean.getGameTypeId()).append(" WHERE trans_id = ").append(tktInfoBean.getTxnId());

				rs = stmt.executeQuery(query.toString());
				if(rs.next()) {
					tktInfoBean.setDrawId(rs.getInt("draw_id"));
				} else {
					throw new SLEException(SLEErrors.SQL_DATA_ERROR_CODE, SLEErrors.SQL_DATA_ERROR_MESSAGE);
				}
			}
			
			for(Entry<Long, TicketInfoBean> entrySet : tktInfoMap.entrySet()) {
				query.setLength(0);
				tktInfoBean = entrySet.getValue();
				query.append("SELECT draw_status,purchase_table_name FROM st_sl_draw_master_").append(tktInfoBean.getGameId())
				.append(" WHERE draw_id = ").append(tktInfoBean.getDrawId());

				rs = stmt.executeQuery(query.toString());
				String drawStatus=null;
				if(rs.next()) {
					drawStatus = rs.getString("draw_status");
					if ("FREEZE".equals(drawStatus) || "ACTIVE".equals(drawStatus)
							|| "CLAIM HOLD".equals(drawStatus)) {
						tktInfoBean.setStatus("RESULT_AWAITED");
					}
					tktInfoBean.setPurchaseTable(rs.getInt("purchase_table_name"));
				} else {
					throw new SLEException(SLEErrors.SQL_DATA_ERROR_CODE, SLEErrors.SQL_DATA_ERROR_MESSAGE);
				}
			}
			
			/*for(Entry<Long, TicketInfoBean> entrySet : tktInfoMap.entrySet()) {
				query.setLength(0);
				tktInfoBean = entrySet.getValue();
				query.append("SELECT STATUS,rank_id FROM st_sl_draw_ticket_").append(tktInfoBean.getGameId())
				.append("_").append(tktInfoBean.getGameTypeId()).append("_").append(tktInfoBean.getPurchaseTable())
				.append(" WHERE trans_id = ").append(tktInfoBean.getTxnId());

				rs = stmt.executeQuery(query.toString());
				if(rs.next()) {
					tktInfoBean.setStatus(rs.getString("STATUS"));
					tktInfoBean.setPrizerank(rs.getInt("rank_id"));
				} else {
					throw new SLEException(SLEErrors.SQL_DATA_ERROR_CODE, SLEErrors.SQL_DATA_ERROR_MESSAGE);
				}
			}*/
			
			boardStmt=con.createStatement();
			for(Entry<Long, TicketInfoBean> entrySet : tktInfoMap.entrySet()) {
				tktInfoBean = entrySet.getValue();
				winQuery = "SELECT board_id, ticket_number,status, SUM(winAmt) winAmt, STATUS FROM (SELECT board_id, ticket_number, (bet_amount_multiple*draw_tlb.unit_price*prize_amount)/prize.unit_price winAmt, rank_id, draw_tlb.merchant_id, STATUS FROM st_sl_draw_ticket_"+tktInfoBean.getGameId()+"_"+tktInfoBean.getGameTypeId()+"_"+tktInfoBean.getPurchaseTable()+" draw_tlb INNER JOIN st_sl_prize_details_"+tktInfoBean.getGameId()+"_"+tktInfoBean.getGameTypeId()+" prize ON draw_tlb.merchant_id=prize.merchant_id AND draw_tlb.rank_id=prize.prize_rank AND draw_tlb.draw_id=prize.draw_id WHERE ticket_number="+tktInfoBean.getTktNbr()+" AND rank_id>0 AND STATUS<>'CANCELLED' AND draw_tlb.merchant_id="+userInfoBean.getMerchantId()+")winTlb GROUP BY ticket_number;";
				logger.info("Board Query - "+query);
				boardRs = boardStmt.executeQuery(winQuery.toString());
				if(boardRs.next()) {
					tktInfoBean.setTicketStatus(boardRs.getString("status"));
					if(boardRs.getDouble("winAmt")>0.0)
						tktInfoBean.setStatus("Winning Amt: "+Util.fmtToTwoDecimal(boardRs.getDouble("winAmt")));
				} 
				else if("RESULT_AWAITED".equals(tktInfoBean.getStatus())){
					tktInfoBean.setStatus("RESULT_AWAITED");
				}
			}
		} catch (SLEException e) {
			throw e;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return tktInfoMap;
	}
	
	public void fetchPurchaseTicketData(ReprintTicketBean tktBean, UserInfoBean userInfoBean, Connection con) {
		
	}

}
