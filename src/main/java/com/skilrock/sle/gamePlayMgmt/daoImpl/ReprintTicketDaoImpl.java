package com.skilrock.sle.gamePlayMgmt.daoImpl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import com.skilrock.sle.common.SLELogger;
import com.skilrock.sle.common.SportsLotteryUtils;
import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.UtilityFunctions;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.common.javaBeans.UserInfoBean;
import com.skilrock.sle.gamePlayMgmt.javaBeans.PrizeRankDrawWinningBean;
import com.skilrock.sle.gamePlayMgmt.javaBeans.ReprintTicketBean;
import com.skilrock.sle.gamePlayMgmt.javaBeans.SportsLotteryGameDrawDataBean;
import com.skilrock.sle.gamePlayMgmt.javaBeans.SportsLotteryGameEventDataBean;
import com.skilrock.sle.gamePlayMgmt.javaBeans.SportsLotteryGamePlayBean;
import com.skilrock.sle.merchant.common.javaBeans.MerchantInfoBean;

public class ReprintTicketDaoImpl {
	
	private static final SLELogger logger = SLELogger.getLogger(ReprintTicketDaoImpl.class.getName());
	
	public SportsLotteryGamePlayBean reprintSportsLotteryGameTicket(ReprintTicketBean tktBean, MerchantInfoBean merchantInfoBean,  Connection con) throws SLEException{
		Statement stmt = null;
		Statement stmt1 = null;
		Statement stmt2 = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		ResultSet rs3 = null;
		ResultSet rs4 = null;
		ResultSet rs5 = null;
		SportsLotteryGameEventDataBean eventDataBean = null;
		SportsLotteryGameDrawDataBean drawDataBean = null;
		SportsLotteryGamePlayBean gamePlayBean = new SportsLotteryGamePlayBean();
		int drawId = 0;

		try {
				
				gamePlayBean.setReprintCount(tktBean.getRpc());
				gamePlayBean.setServiceId(merchantInfoBean.getServiceId());
				gamePlayBean.setInterfaceType(tktBean.getInterfaceType());
				String txnMasterQry = "select * from st_sl_sale_txn_master where ticket_nbr = '"+tktBean.getTicketNumber()+"' and status = 'DONE' and is_cancel = 'N'";
				stmt = con.createStatement();
				rs = stmt.executeQuery(txnMasterQry);
				if(rs.next()){
					gamePlayBean.setGameId(rs.getInt("game_id"));
					gamePlayBean.setGameTypeId(rs.getInt("game_type_id"));
					gamePlayBean.setTotalPurchaseAmt(rs.getDouble("amount"));
					gamePlayBean.setTicketNumber(rs.getLong("ticket_nbr"));
					gamePlayBean.setTransId(rs.getLong("trans_id"));
					//String tmpTime = Util.getCurrentTimeString();
					gamePlayBean.setPurchaseTime(UtilityFunctions.timeFormat(rs.getString("trans_date")));
					gamePlayBean.setUnitPrice(SportsLotteryUtils.gameTypeInfoMerchantMap.get(merchantInfoBean.getMerchantDevName()).get(rs.getInt("game_type_id")).getUnitPrice());
				}
				
				drawDataBean = new SportsLotteryGameDrawDataBean();
				String eventQry = "select * from st_sl_game_tickets_"+gamePlayBean.getGameId()+"_"+gamePlayBean.getGameTypeId()+" a, st_sl_draw_master_"+gamePlayBean.getGameId()+" b, st_sl_draw_merchant_mapping_"+gamePlayBean.getGameId()+" c where ticket_number = '"+gamePlayBean.getTicketNumber()+"' and a.draw_id = b.draw_id and a.draw_id = c.draw_id and merchant_id = "+merchantInfoBean.getMerchantId();
				stmt1 = con.createStatement();
				rs = stmt1.executeQuery(eventQry);
				StringBuilder drawIdArr = new StringBuilder();
				int sCount = 0;
				rs.last();
				int noOfBords = rs.getRow();
				SportsLotteryGameDrawDataBean[] gameDrawArr = new SportsLotteryGameDrawDataBean[noOfBords];
				rs.beforeFirst();
				while(rs.next()){
					gamePlayBean.setBarcodeCount(rs.getInt("barcode_count"));
					drawId = rs.getInt("draw_id");
					drawDataBean.setDrawId(drawId);
					String tmpTime = rs.getString("draw_datetime");
					drawDataBean.setDrawDateTime(tmpTime.substring(0, tmpTime.indexOf('.')));
					drawDataBean.setDrawDisplayname(rs.getString("draw_name"));
					drawDataBean.setDrawClaimEndTime(rs.getString("claim_end_date"));
					drawIdArr.append(rs.getString("draw_id")).append(",");
					
					String eventOrderQry = "select event_order from st_sl_draw_event_mapping_"+gamePlayBean.getGameId()+" where draw_id = "+rs.getInt("draw_id");
					rs2 = stmt.executeQuery(eventOrderQry);
					StringBuilder eventCols = new StringBuilder();
					while(rs2.next()){
						eventCols.append("event_").append(rs2.getString("event_order")).append(",").append("','").append(",");
					}
					String cols = eventCols.toString().substring(0, eventCols.toString().length()-5);
					
					String qry3 = "select concat("+cols+") optionIds from st_sl_game_tickets_"+gamePlayBean.getGameId()+"_"+gamePlayBean.getGameTypeId()+" where ticket_number = '"+gamePlayBean.getTicketNumber()+"'";
					rs5 =stmt.executeQuery(qry3);
					String optionIds = null;
					if(rs5.next()){
						optionIds = rs5.getString("optionIds");
					}
					String eventFetchQry = "select a.event_id, group_concat(evt_opt_id) option_id, event_description, c.team_name home_team,c.team_code home_team_code , d.team_name away_team,d.team_code away_team_code , e.league_display_name, f.venue_display_name, b.start_time eventDateTime from st_sl_event_option_mapping a, st_sl_event_master b,  st_sl_game_team_master c, st_sl_game_team_master d, st_sl_league_master e, st_sl_venue_master f where evt_opt_id in ("+optionIds+") and a.event_id = b.event_id and b.home_team_id = c.team_id and b.away_team_id = d.team_id and b.league_id = e.league_id and b.venue_id = f.venue_id and a.game_id="+gamePlayBean.getGameId()+" and a.game_type_id="+gamePlayBean.getGameTypeId()+" group by event_id ORDER BY evt_opt_id";
					rs3 = stmt.executeQuery(eventFetchQry);
					rs3.last();
					SportsLotteryGameEventDataBean[] eventDataArr = new SportsLotteryGameEventDataBean[rs3.getRow()];
					int count = 0;
					int noOfLines = 1;
					rs3.beforeFirst();
					while(rs3.next()){
						eventDataBean = new SportsLotteryGameEventDataBean();
						eventDataBean.setEventId(rs3.getInt("event_id"));
						tktBean.setEventTypeId(rs3.getInt("event_id"));
						eventDataBean.setEventDescription(rs3.getString("event_description"));
						eventDataBean.setHomeTeamName(rs3.getString("home_team"));
						eventDataBean.setHomeTeamCode(rs3.getString("home_team_code"));
						eventDataBean.setAwayTeamName(rs3.getString("away_team"));
						eventDataBean.setAwayTeamCode(rs3.getString("away_team_code"));
						eventDataBean.setLeagueName(rs3.getString("league_display_name"));
						eventDataBean.setVenueName(rs3.getString("venue_display_name"));
						eventDataBean.setEventDateTime(UtilityFunctions.timeFormat(rs3.getString("eventDateTime")));
						
							String optionsQry = "select group_concat(option_code) opts from st_sl_event_option_mapping where event_id = "+rs3.getInt("event_id")+" and evt_opt_id in ("+rs3.getString("option_id")+") and game_id="+gamePlayBean.getGameId()+" and game_type_id="+gamePlayBean.getGameTypeId()+"";
							stmt2 = con.createStatement();
							rs4 = stmt2.executeQuery(optionsQry);
							if(rs4.next()){
								eventDataBean.setSelectedOption(rs4.getString("opts").split(","));
							}
							noOfLines *= rs4.getString("opts").split(",").length;
							eventDataArr[count] = eventDataBean;
							count++;
							logger.debug(eventDataBean.toString());
					}
					
					drawDataBean.setGameEventDataBeanArray(eventDataArr);
					drawDataBean.setNoOfLines(noOfLines);
					drawDataBean.setBetAmountMultiple(rs.getInt("bet_amount_multiple"));
					drawDataBean.setBoardPurchaseAmount(noOfLines * gamePlayBean.getUnitPrice() * rs.getInt("bet_amount_multiple"));
					
					gameDrawArr[sCount] = drawDataBean;
					sCount++;
					String gameTypeDevName = SportsLotteryUtils.gameTypeInfoMerchantMap.get(merchantInfoBean.getMerchantDevName()).get(gamePlayBean.getGameTypeId()).getGameTypeDevName();
					if(!"soccer12".equalsIgnoreCase(gameTypeDevName)){
						if ("YES".equals(SportsLotteryUtils.gameTypeInfoMerchantMap.get(merchantInfoBean.getMerchantDevName()).get(gamePlayBean.getGameTypeId()).getJackPotMessageDisplay())) {
							Map<Integer, PrizeRankDrawWinningBean> drawPrizeRankMap = PurchaseTicketDaoImpl.getInstance().getPrizeRankDrawSaleDistriBution(merchantInfoBean.getMerchantId(), merchantInfoBean.getMerchantDevName(), gamePlayBean.getGameId(), gamePlayBean.getGameTypeId(), drawId, gamePlayBean.getTotalPurchaseAmt(), con);
							drawDataBean.setDrawPrizeRankMap(drawPrizeRankMap);
						}
					}
					
					logger.debug(drawDataBean.toString());
				}
				drawIdArr.deleteCharAt(drawIdArr.length()-1);
				gamePlayBean.setGameDrawDataBeanArray(gameDrawArr);
				int drawIdArrLen = drawIdArr.toString().split(",").length;
				Integer drawIdArrInt[] = new Integer[drawIdArrLen];
				for(int k = 0; k<drawIdArr.toString().split(",").length; k++){
					drawIdArrInt[k] = Integer.parseInt(drawIdArr.toString().split(",")[k]);
				}
				gamePlayBean.setDrawIdArray(drawIdArrInt);
				gamePlayBean.setNoOfBoard(noOfBords);
				
				
				logger.debug(gamePlayBean.toString());
				
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return gamePlayBean;
	}
	
	public ReprintTicketBean fetchLastSoldTicket(UserInfoBean userBean,String channelType,Connection con) throws SLEException {
		Statement stmt = null;
		ResultSet rs = null;
		ReprintTicketBean tktBean = null;
		try {
				String qry = "select game_id, game_type_id, ticket_nbr from st_sl_sale_txn_master where merchant_user_id = "+userBean.getMerchantUserId()+"  and merchant_id = "+userBean.getMerchantId()+" and status = 'DONE' and channel_type='"+channelType+"' order by trans_date desc limit 1";
				stmt = con.createStatement();
				rs = stmt.executeQuery(qry);
				tktBean = new ReprintTicketBean();
				if(rs.next()){
					tktBean.setGameId(rs.getInt("game_id"));
					tktBean.setGameTypeId(rs.getInt("game_type_id"));
					tktBean.setTicketNumber(rs.getString("ticket_nbr"));
				}				
		}catch (SQLException e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return tktBean;
	}
	
	public void updateTicketsTable(ReprintTicketBean tktBean,  Connection con) throws SLEException {
		Statement stmt = null;
		try {
				int totRpc = tktBean.getRpc()+1;
				String qry = "update st_sl_game_tickets_"+tktBean.getGameId()+"_"+tktBean.getGameTypeId()+" set rpc_total = "+totRpc+" , reprint_datetime = '"+Util.getCurrentTimeString()+"' where ticket_number = '"+tktBean.getTicketNumber()+"'";
				stmt = con.createStatement();
				stmt.executeUpdate(qry);
				tktBean.setRpc(totRpc);
		}catch (SQLException e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
	}
	
	

}
