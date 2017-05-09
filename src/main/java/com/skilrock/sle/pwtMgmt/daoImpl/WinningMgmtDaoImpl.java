package com.skilrock.sle.pwtMgmt.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;

import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.skilrock.sle.common.SLELogger;
import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.commonMethod.daoImpl.CommonMethodsDaoImpl;
import com.skilrock.sle.pwtMgmt.javaBeans.DrawWiseTicketInfoBean;
import com.skilrock.sle.pwtMgmt.javaBeans.TicketInfoBean;

public class WinningMgmtDaoImpl {
	private static final SLELogger logger = SLELogger.getLogger(WinningMgmtDaoImpl.class.getName());

	private static WinningMgmtDaoImpl singleInstance;

	private WinningMgmtDaoImpl(){}

	public static WinningMgmtDaoImpl getSingleInstance() {
		if (singleInstance == null) {
			synchronized (WinningMgmtDaoImpl.class) {
				if (singleInstance == null) {
					singleInstance = new WinningMgmtDaoImpl();
				}
			}
		}

		return singleInstance;
	}

	public synchronized void fetchWinningTickets(DrawWiseTicketInfoBean infoBean, Connection connection) throws SLEException {
		Statement stmt = null;
		PreparedStatement winPstmt = null;
		String query = null;
		ResultSet rs = null;
		Map<Long, TicketInfoBean> ticketMap = new HashMap<Long, TicketInfoBean>();
		Set<Long> highPrizeTicketSet=new HashSet<Long>();
		TicketInfoBean ticketInfoBean = null;
		try {
			stmt = connection.createStatement();

			query = "SELECT draw_status, purchase_table_name FROM st_sl_draw_master_"+infoBean.getGameId()+" WHERE draw_id="+infoBean.getDrawId()+" AND game_type_id="+infoBean.getGameTypeId()+";";
			logger.info("Draw Status Query - "+query);
			rs = stmt.executeQuery(query);
			int purchaseTableName = 0;
			while (rs.next()) {
				if (!"CLAIM ALLOW".equals(rs.getString("draw_status"))) {
					logger.info("Draw Status - "+rs.getString("draw_status"));
					infoBean.setResponseCode(1);
					infoBean.setResponseMessage("PWT_HOLD");
					return;
				} else {
					purchaseTableName = rs.getInt("purchase_table_name");
				}
			}

			if(purchaseTableName == 0) {
				infoBean.setResponseCode(2);
				infoBean.setResponseMessage("INVALID_DRAW");
				return;
			}

			winPstmt = connection.prepareStatement("INSERT INTO st_sl_winning_txn_master (trans_id, merchant_id, channel_type, game_id, game_type_id, draw_id, ticket_nbr, user_id, merchant_user_id, trans_date, amount,tax_amt,net_amount,status, pay_type) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);");

			query = "SELECT ticket_number, merchant_user_id, rank_id,trans_id,SUM((pd.prize_amount*dt.unit_price*bet_amount_multiple)/dt.unit_price) winning_amt FROM st_sl_draw_ticket_"+infoBean.getGameId()+"_"+infoBean.getGameTypeId()+"_"+purchaseTableName+" dt INNER JOIN st_sl_prize_details_"+infoBean.getGameId()+"_"+infoBean.getGameTypeId()+" pd ON dt.draw_id=pd.draw_id and dt.merchant_id=pd.merchant_id and  dt.rank_id=pd.prize_rank  INNER JOIN st_sl_merchant_user_master mum ON mum.user_id=dt.party_id WHERE rank_id>0 AND dt.merchant_id="+infoBean.getMerchantId()+" AND STATUS ='UNCLAIMED' GROUP BY ticket_number, rank_id;";
			logger.info("Fetch Winning Tickets - "+query);
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				if(ticketMap.containsKey(rs.getLong("ticket_number"))) {
					ticketInfoBean = ticketMap.get(rs.getLong("ticket_number"));
					ticketInfoBean.setTotalWinningAmt(ticketInfoBean.getTotalWinningAmt()+rs.getDouble("winning_amt"));
				} else {
					ticketInfoBean = new TicketInfoBean();
					ticketInfoBean.setTicketNo(rs.getLong("ticket_number"));
					ticketInfoBean.setPartyId(rs.getInt("merchant_user_id"));
					ticketInfoBean.setTotalWinningAmt(rs.getDouble("winning_amt"));
					ticketInfoBean.setEnginesaleTxnId(rs.getString("trans_id"));
				}
				ticketMap.put(rs.getLong("ticket_number"), ticketInfoBean);
				
			}

			for(Entry<Long, TicketInfoBean> ticketEntry:ticketMap.entrySet()){
				if(ticketEntry.getValue().getTotalWinningAmt() > Double.parseDouble(Util.getPropertyValue("AUTO_APPROVED_WINNING_AMT_LIMIT"))){
					highPrizeTicketSet.add(ticketEntry.getKey());
					continue;
				}
				
				long transactionId = CommonMethodsDaoImpl.getInstance().generateSLETransaction(infoBean.getMerchantId(), "PWT", connection);
				winPstmt.setLong(1, transactionId);
				winPstmt.setInt(2, infoBean.getMerchantId());
				winPstmt.setString(3, "API");
				winPstmt.setInt(4, infoBean.getGameId());
				winPstmt.setInt(5, infoBean.getGameTypeId());
				winPstmt.setInt(6, infoBean.getDrawId());
			//	winPstmt.setInt(7, rs.getInt("rank_id"));
				winPstmt.setString(7, ticketEntry.getKey().toString());
				winPstmt.setLong(8, 0);
				winPstmt.setLong(9, ticketInfoBean.getPartyId());
				winPstmt.setTimestamp(10, Util.getCurrentTimeStamp());
				winPstmt.setDouble(11, ticketEntry.getValue().getTotalWinningAmt());
				winPstmt.setDouble(12, 0.0);
				winPstmt.setDouble(13, ticketEntry.getValue().getTotalWinningAmt());
				winPstmt.setString(14, "INITIATED");
				winPstmt.setString(15, "NORMAL_PAY");
				winPstmt.addBatch();
				ticketEntry.getValue().setEnginewinTxnId(String.valueOf(transactionId));
			}
							
			winPstmt.executeBatch();
			for(Long tickeNbr:highPrizeTicketSet){
				ticketMap.remove(tickeNbr);
			}

			Set<Long> tickSet = ticketMap.keySet();
			if(tickSet.size()>0){
				query = "UPDATE st_sl_draw_ticket_"+infoBean.getGameId()+"_"+infoBean.getGameTypeId()+"_"+purchaseTableName+" SET STATUS='CLAIMED' WHERE draw_id="+infoBean.getDrawId()+" AND ticket_number IN('"+tickSet.toString().substring(1, tickSet.toString().length() - 1).replaceAll(",", "','").replaceAll(" ", "")+"') AND rank_id>0;";
				logger.info("Update Ticket Status in Purchase Table to CLAIMED Query - "+query);
			  int isupdate=	stmt.executeUpdate(query);
			  if(isupdate==0){
				  infoBean.setResponseCode(3);
				  infoBean.setResponseMessage("INTERNAL_ERROR");
				return;
			  }
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception - " + e);
			infoBean.setResponseCode(3);
			infoBean.setResponseMessage("INTERNAL_ERROR");
			return;
		}

		infoBean.setTicketMap(ticketMap);
	}

	public void updateWinningTransaction(int merchantId, int gameId, int gameTypeId, int drawId, Map<Long, String> transMap, Connection connection) throws SLEException {
		PreparedStatement pstmt = null;
		try {
			pstmt = connection.prepareStatement("UPDATE st_sl_winning_txn_master SET STATUS=?, merchant_trans_id=? WHERE merchant_id="+merchantId+" AND game_id="+gameId+" AND game_type_id="+gameTypeId+" AND draw_id="+drawId+" AND ticket_nbr=?;");
			for(Map.Entry<Long, String> entrySet : transMap.entrySet()) {
				pstmt.setString(1, "DONE");
				pstmt.setLong(2, entrySet.getKey());
				pstmt.setString(3, entrySet.getValue());
				pstmt.addBatch();
			}
			pstmt.executeBatch();

		} catch (Exception e) {
			logger.error("Exception - " + e);
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
	}
}