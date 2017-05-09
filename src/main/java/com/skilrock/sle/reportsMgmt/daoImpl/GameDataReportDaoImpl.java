package com.skilrock.sle.reportsMgmt.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.skilrock.sle.common.DBConnect;
import com.skilrock.sle.common.SLELogger;
import com.skilrock.sle.common.SportsLotteryUtils;
import com.skilrock.sle.common.Util;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.reportsMgmt.javaBeans.DrawDataBean;
import com.skilrock.sle.reportsMgmt.javaBeans.GameDataReportBean;
import com.skilrock.sle.reportsMgmt.javaBeans.SaleDataBean;
import com.skilrock.sle.reportsMgmt.javaBeans.SaleTrendDataBean;
import com.skilrock.sle.reportsMgmt.javaBeans.WinningDataBean;

/**
 * @author Shobhit
 * @category Game data
 */
public class GameDataReportDaoImpl {
	
	private static final SLELogger logger = SLELogger.getLogger(GameDataReportDaoImpl.class.getName());
	
	private static volatile GameDataReportDaoImpl classInstance;
	public static GameDataReportDaoImpl getInstance() {
		if(classInstance == null){
			synchronized (GameDataReportDaoImpl.class) {
				if(classInstance == null){
					classInstance = new GameDataReportDaoImpl();					
				}
			}
		}
		return classInstance;
	}
	public int[][] getGameTypeList(int gameId, int merchantId, Connection connection) throws SLEException {
		Statement stmt = null;
		ResultSet rs = null;
		String gameIdWiseCheck = null;
		String query = null;
		int[][] gameTypeArray = null;
		try {
			stmt = connection.createStatement();
			gameIdWiseCheck = (gameId==-1)?"":" AND game_id="+gameId;
			query = "SELECT game_id, mas.game_type_id FROM st_sl_game_type_master mas " +
					"INNER JOIN st_sl_game_type_merchant_mapping map " +
					"ON mas.game_type_id=map.game_type_id"+gameIdWiseCheck+" AND merchant_id="+merchantId+";";
			logger.info("GetGameTypeId - "+query);
			rs = stmt.executeQuery(query);
			if (rs.last()) {
				gameTypeArray = new int[rs.getRow()][2];
				rs.beforeFirst();
			}
			int i=0;
			while(rs.next()) {
				gameTypeArray[i][0] = rs.getInt("game_id");
				gameTypeArray[i++][1] = rs.getInt("game_type_id");
			}
		} catch (SQLException se) {
			se.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}

		return gameTypeArray;
	}

	public Map<String, GameDataReportBean> getSaleDataReport(int gameId, int gameTypeId, Timestamp startDate, Timestamp endDate, String reportType, int merchantId, Connection connection) throws SLEException {
		Statement stmt = null;
		PreparedStatement pstmt = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		ResultSet archRs1 = null;
		ResultSet archRs2 = null;
		Map<String, GameDataReportBean> gameDataReportMap = new LinkedHashMap<String, GameDataReportBean>();
		GameDataReportBean gameDataReportBean = null;
		SimpleDateFormat simpleDateFormat = null;
		String lastArchDate=null;
		String dateCheckAppender=null;
		try {
			String merchantName = Util.fetchMerchantInfoBean(merchantId).getMerchantDevName();
			simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			lastArchDate=SportsLotteryUtils.getLastArchDate(connection);
			if(lastArchDate!=null && !startDate.after(Util.StringToDateConversion(lastArchDate+" 00:00:00"))){
				//Fetch archived data
				if(endDate.after(Util.StringToDateConversion(lastArchDate+" 00:00:00"))){
					dateCheckAppender="draw_datetime<='"+lastArchDate+" 23:59:59'";
				}else{
					dateCheckAppender="draw_datetime<='"+endDate+"'";
				}
				String archDrawQry="SELECT mas.draw_id, draw_name, year(draw_datetime) dYear, draw_datetime, draw_freeze_time, draw_status, purchase_table_name FROM st_sl_draw_master_"+gameId+" mas INNER JOIN st_sl_draw_merchant_mapping_"+gameId+" map ON mas.draw_id=map.draw_id WHERE game_type_id="+gameTypeId+" AND merchant_id="+merchantId+" AND draw_datetime>='"+startDate+"' AND "+dateCheckAppender+";";
				
				stmt = connection.createStatement();
				archRs1 = stmt.executeQuery(archDrawQry);
				pstmt = connection.prepareStatement("select  dd.draw_id ,total_sale_tickets-total_cancel_tickets noOfsale,total_sale_value-total_cancel_amount saleAmount from st_sl_draw_details_?_?_? dd INNER JOIN st_sl_draw_master_? dm on dd.draw_id=dm.draw_id and dd.merchant_id=? and dd.draw_id=?");
				while(archRs1.next()){
					int drawId = archRs1.getInt("draw_id");
					int dYear=archRs1.getInt("dYear");
					pstmt.setInt(1, gameId);
					pstmt.setInt(2, gameTypeId);
					pstmt.setInt(3, dYear);
					pstmt.setInt(4, gameId);
					pstmt.setInt(5, merchantId);
					pstmt.setInt(6, drawId);
					
					gameDataReportBean = new GameDataReportBean();
					gameDataReportBean.setDrawId(drawId);
					gameDataReportBean.setDrawName(archRs1.getString("draw_name"));
					gameDataReportBean.setDrawTime(simpleDateFormat.format(archRs1.getTimestamp("draw_datetime").getTime()));
					gameDataReportBean.setDrawFreezeTime(simpleDateFormat.format(archRs1.getTimestamp("draw_freeze_time").getTime()));
					gameDataReportBean.setDrawStatus(archRs1.getString("draw_status"));
					gameDataReportBean.setIsArchData("YES");
					
					archRs2=pstmt.executeQuery();
					if(archRs2.next()){
						gameDataReportBean.setGameId(gameId);
						gameDataReportBean.setGameName(SportsLotteryUtils.gameInfoMerchantMap.get(merchantName).get(gameId).getGameDispName());
						gameDataReportBean.setGameTypeId(gameTypeId);
						gameDataReportBean.setGameTypeName(SportsLotteryUtils.gameTypeInfoMerchantMap.get(merchantName).get(gameTypeId).getGameTypeDispName());
						
						gameDataReportBean.setNoOfSale(archRs2.getInt("noOfsale"));
						gameDataReportBean.setSaleAmount(archRs2.getDouble("saleAmount"));
						gameDataReportMap.put(gameId+"-"+gameTypeId+"-"+drawId, gameDataReportBean);
					}
					
				}
				
				//Fetch normal data
				String query = "SELECT mas.draw_id, draw_name, draw_datetime, draw_freeze_time, draw_status, purchase_table_name FROM st_sl_draw_master_"+gameId+" mas INNER JOIN st_sl_draw_merchant_mapping_"+gameId+" map ON mas.draw_id=map.draw_id WHERE game_type_id="+gameTypeId+" AND merchant_id="+merchantId+" AND draw_datetime>'"+lastArchDate+" 23:59:59' AND draw_datetime<='"+endDate+"';";
				stmt = connection.createStatement();
				rs1 = stmt.executeQuery(query);

				pstmt = connection.prepareStatement("SELECT COUNT(1) noOfsale,SUM(saleAmount) saleAmount ,SUM(avgSellerSale) avgSellerSale FROM (SELECT COUNT(1) noOfsale, SUM(unit_price*bet_amount_multiple) saleAmount, (SUM(unit_price*bet_amount_multiple))/(SELECT COUNT(DISTINCT(party_id)) FROM st_sl_draw_ticket_"+gameId+"_"+gameTypeId+"_? WHERE merchant_id="+merchantId+") avgSellerSale FROM st_sl_draw_ticket_"+gameId+"_"+gameTypeId+"_? dt INNER JOIN st_sl_sale_txn_master tt ON tt.ticket_nbr =  dt.ticket_number and tt.trans_id=dt.trans_id WHERE draw_id=? AND dt.merchant_id="+merchantId+" AND dt.STATUS NOT IN ('CANCELLED','FAILED') AND tt.status='DONE' group by ticket_number) mainTB;");
				while(rs1.next()) {
					int drawId = rs1.getInt("draw_id");
					int purchaseTableName = rs1.getInt("purchase_table_name");

					pstmt.setInt(1, purchaseTableName);
					pstmt.setInt(2, purchaseTableName);
					pstmt.setInt(3, drawId);

					gameDataReportBean = new GameDataReportBean();
					gameDataReportBean.setDrawId(drawId);
					gameDataReportBean.setDrawName(rs1.getString("draw_name"));
					gameDataReportBean.setDrawTime(simpleDateFormat.format(rs1.getTimestamp("draw_datetime").getTime()));
					gameDataReportBean.setDrawFreezeTime(simpleDateFormat.format(rs1.getTimestamp("draw_freeze_time").getTime()));
					gameDataReportBean.setDrawStatus(rs1.getString("draw_status"));

					//pstmt.seti
					rs2 = pstmt.executeQuery();
					if(rs2.next()) {
						gameDataReportBean.setGameId(gameId);
						gameDataReportBean.setGameName(SportsLotteryUtils.gameInfoMerchantMap.get(merchantName).get(gameId).getGameDispName());
						gameDataReportBean.setGameTypeId(gameTypeId);
						gameDataReportBean.setGameTypeName(SportsLotteryUtils.gameTypeInfoMerchantMap.get(merchantName).get(gameTypeId).getGameTypeDispName());
						
						gameDataReportBean.setNoOfSale(rs2.getInt("noOfsale"));
						gameDataReportBean.setSaleAmount(rs2.getDouble("saleAmount"));
						gameDataReportBean.setAvgSalePerSeller(rs2.getDouble("avgSellerSale"));
						gameDataReportMap.put(gameId+"-"+gameTypeId+"-"+drawId, gameDataReportBean);
					}
				}
			}else{
				String query = "SELECT mas.draw_id, draw_name, draw_datetime, draw_freeze_time, draw_status, purchase_table_name FROM st_sl_draw_master_"+gameId+" mas INNER JOIN st_sl_draw_merchant_mapping_"+gameId+" map ON mas.draw_id=map.draw_id WHERE game_type_id="+gameTypeId+" AND merchant_id="+merchantId+" AND draw_datetime>='"+startDate+"' AND draw_datetime<='"+endDate+"';";
				stmt = connection.createStatement();
				rs1 = stmt.executeQuery(query);

				pstmt = connection.prepareStatement("SELECT COUNT(1) noOfsale,SUM(saleAmount) saleAmount ,SUM(avgSellerSale) avgSellerSale FROM (SELECT COUNT(1) noOfsale, SUM(unit_price*bet_amount_multiple) saleAmount, (SUM(unit_price*bet_amount_multiple))/(SELECT COUNT(DISTINCT(party_id)) FROM st_sl_draw_ticket_"+gameId+"_"+gameTypeId+"_? WHERE merchant_id="+merchantId+") avgSellerSale FROM st_sl_draw_ticket_"+gameId+"_"+gameTypeId+"_? dt INNER JOIN st_sl_sale_txn_master tt ON tt.ticket_nbr =  dt.ticket_number and tt.trans_id=dt.trans_id WHERE draw_id=? AND dt.merchant_id="+merchantId+" AND dt.STATUS NOT IN ('CANCELLED','FAILED') AND tt.status='DONE' group by ticket_number) mainTB;");
				while(rs1.next()) {
					int drawId = rs1.getInt("draw_id");
					int purchaseTableName = rs1.getInt("purchase_table_name");

					pstmt.setInt(1, purchaseTableName);
					pstmt.setInt(2, purchaseTableName);
					pstmt.setInt(3, drawId);

					gameDataReportBean = new GameDataReportBean();
					gameDataReportBean.setDrawId(drawId);
					gameDataReportBean.setDrawName(rs1.getString("draw_name"));
					gameDataReportBean.setDrawTime(simpleDateFormat.format(rs1.getTimestamp("draw_datetime").getTime()));
					gameDataReportBean.setDrawFreezeTime(simpleDateFormat.format(rs1.getTimestamp("draw_freeze_time").getTime()));
					gameDataReportBean.setDrawStatus(rs1.getString("draw_status"));

					//pstmt.seti
					rs2 = pstmt.executeQuery();
					if(rs2.next()) {
						gameDataReportBean.setGameId(gameId);
						gameDataReportBean.setGameName(SportsLotteryUtils.gameInfoMerchantMap.get(merchantName).get(gameId).getGameDispName());
						gameDataReportBean.setGameTypeId(gameTypeId);
						gameDataReportBean.setGameTypeName(SportsLotteryUtils.gameTypeInfoMerchantMap.get(merchantName).get(gameTypeId).getGameTypeDispName());
						
						gameDataReportBean.setNoOfSale(rs2.getInt("noOfsale"));
						gameDataReportBean.setSaleAmount(rs2.getDouble("saleAmount"));
						gameDataReportBean.setAvgSalePerSeller(rs2.getDouble("avgSellerSale"));
						gameDataReportMap.put(gameId+"-"+gameTypeId+"-"+drawId, gameDataReportBean);
					}
				}
			}

		} catch (SQLException se) {
			se.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}finally{
			DBConnect.closeConnection(pstmt, rs1);
			DBConnect.closeConnection(stmt, rs2);
			DBConnect.closeRs(archRs1);
			DBConnect.closeRs(archRs2);
		}

		return gameDataReportMap;
	}

	public void getWinningDataReport(int gameId, int gameTypeId, Map<String, GameDataReportBean> salesDataMap, Timestamp startDate, Timestamp endDate, String reportType, int merchantId, Connection connection) throws SLEException {
		Statement stmtDraw = null;
		Statement stmtWinning = null;
		ResultSet rsDraw = null;
		ResultSet rsWinning = null;
		StringBuilder qry = null;
		GameDataReportBean gameDataReportBean = null;
		String lastArchDate=null;
		String dateCheckAppender=null;
		try {
			
			DecimalFormat df = new DecimalFormat("#0.00");
			lastArchDate=SportsLotteryUtils.getLastArchDate(connection);
			if(lastArchDate!=null && !startDate.after(Util.StringToDateConversion(lastArchDate+" 00:00:00"))){
				stmtDraw = connection.createStatement();
				stmtWinning=connection.createStatement();
				
				//fetch archived data
				if(endDate.after(Util.StringToDateConversion(lastArchDate+" 00:00:00"))){
					dateCheckAppender="draw_datetime<='"+lastArchDate+" 23:59:59'";
				}else{
					dateCheckAppender="draw_datetime<='"+endDate+"'";
				}
				rsDraw = stmtDraw.executeQuery("SELECT mas.draw_id, year(draw_datetime) dYear,purchase_table_name FROM st_sl_draw_master_"+gameId+" mas INNER JOIN st_sl_draw_merchant_mapping_"+gameId+" map ON mas.draw_id=map.draw_id WHERE game_type_id="+gameTypeId+" AND merchant_id="+merchantId+" AND draw_datetime>='"+startDate+"' AND "+dateCheckAppender+";");
				while(rsDraw.next()){
					int drawId = rsDraw.getInt("draw_id");
					int dYear=rsDraw.getInt("dYear");
					String archWinQry="select  dd.draw_id ,total_claimed_tickets totCLMtkts, total_winning_tickets-total_claimed_tickets TotUNCLMTkts, total_claimed_amount  TotCLMTktAmt, total_winning_tickets totWinTkts,total_winning_amount-total_claimed_amount  TotUNCLMTktAmt , total_winning_amount totWin from st_sl_draw_details_"+gameId+"_"+gameTypeId+"_"+dYear+" dd INNER JOIN st_sl_draw_master_"+gameId+" dm on dd.draw_id=dm.draw_id and dd.merchant_id="+merchantId+" and dd.draw_id="+drawId;
					rsWinning=stmtWinning.executeQuery(archWinQry);
					while(rsWinning.next()) {
						gameDataReportBean = salesDataMap.get(gameId+"-"+gameTypeId+"-"+drawId);
						if(gameDataReportBean != null) {
							gameDataReportBean.setNoOfWinning(rsWinning.getInt("totWinTkts"));
							gameDataReportBean.setWinningAmount(rsWinning.getDouble("totWin"));
							gameDataReportBean.setDiplayWinningAmt(df.format(rsWinning.getDouble("totWin")));
							gameDataReportBean.setTotalClaimedTkts(rsWinning.getInt("totCLMtkts"));
							gameDataReportBean.setTotalUnclaimedTkts(rsWinning.getInt("TotUNCLMTkts"));
							gameDataReportBean.setTotalClaimedAmt(rsWinning.getDouble("TotCLMTktAmt"));
							gameDataReportBean.setTotalUnclaimedAmt(rsWinning.getDouble("TotUNCLMTktAmt"));
						}
					}
				}
				
				//fetch normal data
				
				rsDraw = stmtDraw.executeQuery("SELECT mas.draw_id, purchase_table_name FROM st_sl_draw_master_"+gameId+" mas INNER JOIN st_sl_draw_merchant_mapping_"+gameId+" map ON mas.draw_id=map.draw_id WHERE game_type_id="+gameTypeId+" AND merchant_id="+merchantId+" AND draw_datetime>'"+lastArchDate+" 23:59:59' AND draw_datetime<='"+endDate+"';");
				while(rsDraw.next()) {
					int drawId = rsDraw.getInt("draw_id");
					int purTblName = rsDraw.getInt("purchase_table_name");
					
					qry = new StringBuilder();
					
					qry.append("select sum(totCLMtkts) as totCLMtkts, sum(clmTktAmt) as TotCLMTktAmt, sum(totUNCLMtkts) as TotUNCLMTkts, sum(unclmTktAmt) as TotUNCLMTktAmt, (sum(totCLMtkts)+sum(totUNCLMtkts)) as totWinTkts, (sum(clmTktAmt)+sum(unclmTktAmt))as totWin FROM ");
					qry.append("( SELECT IF(wintlb.status='CLAIMED', a, 0) as totCLMtkts, IF(wintlb.status='CLAIMED', winningAmount, 0) as clmTktAmt, IF(wintlb.status='UNCLAIMED', a, 0) as totUNCLMtkts, IF(wintlb.status='UNCLAIMED', winningAmount, 0) as unclmTktAmt FROM (  ");
					qry.append(" SELECT IF(draw_tlb.ticket_number>0,1,0) a, SUM((draw_tlb.bet_amount_multiple*draw_tlb.unit_price*prize_amount)/prize.unit_price) winningAmount, rank_id, draw_tlb.merchant_id, status  FROM st_sl_draw_ticket_").append(gameId).append("_").append(gameTypeId).append("_").append(purTblName).append(" draw_tlb ");
					qry.append(" INNER JOIN ");
					qry.append(" st_sl_prize_details_").append(gameId).append("_").append(gameTypeId).append(" prize  ON draw_tlb.merchant_id=prize.merchant_id  AND draw_tlb.draw_id=prize.draw_id  AND draw_tlb.draw_id=").append(drawId).append(" AND draw_tlb.rank_id=prize.prize_rank ");
					qry.append(" WHERE rank_id > 0  AND STATUS <> 'CANCELLED'   AND draw_tlb.merchant_id=").append(merchantId).append("  GROUP BY ticket_number)wintlb ) final ");
					
					logger.info("getWinningDataReport for(GameId, GameTypeId, DrawId) ("+gameId+", "+gameTypeId+", "+drawId+") - "+qry.toString());
					rsWinning = stmtWinning.executeQuery(qry.toString());
					while(rsWinning.next()) {
						gameDataReportBean = salesDataMap.get(gameId+"-"+gameTypeId+"-"+drawId);
						if(gameDataReportBean != null) {
							gameDataReportBean.setNoOfWinning(rsWinning.getInt("totWinTkts"));
							gameDataReportBean.setWinningAmount(rsWinning.getDouble("totWin"));
							gameDataReportBean.setDiplayWinningAmt(df.format(rsWinning.getDouble("totWin")));
							gameDataReportBean.setTotalClaimedTkts(rsWinning.getInt("totCLMtkts"));
							gameDataReportBean.setTotalUnclaimedTkts(rsWinning.getInt("TotUNCLMTkts"));
							gameDataReportBean.setTotalClaimedAmt(rsWinning.getDouble("TotCLMTktAmt"));
							gameDataReportBean.setTotalUnclaimedAmt(rsWinning.getDouble("TotUNCLMTktAmt"));
						}
					}
				}
				
				
				
			}else{
				stmtDraw = connection.createStatement();
				rsDraw = stmtDraw.executeQuery("SELECT mas.draw_id, purchase_table_name FROM st_sl_draw_master_"+gameId+" mas INNER JOIN st_sl_draw_merchant_mapping_"+gameId+" map ON mas.draw_id=map.draw_id WHERE game_type_id="+gameTypeId+" AND merchant_id="+merchantId+" AND draw_datetime>='"+startDate+"' AND draw_datetime<='"+endDate+"';");
				while(rsDraw.next()) {
					int drawId = rsDraw.getInt("draw_id");
					int purTblName = rsDraw.getInt("purchase_table_name");
					
					qry = new StringBuilder();
					
					qry.append("select sum(totCLMtkts) as totCLMtkts, sum(clmTktAmt) as TotCLMTktAmt, sum(totUNCLMtkts) as TotUNCLMTkts, sum(unclmTktAmt) as TotUNCLMTktAmt, (sum(totCLMtkts)+sum(totUNCLMtkts)) as totWinTkts, (sum(clmTktAmt)+sum(unclmTktAmt))as totWin FROM ");
					qry.append("( SELECT IF(wintlb.status='CLAIMED', a, 0) as totCLMtkts, IF(wintlb.status='CLAIMED', winningAmount, 0) as clmTktAmt, IF(wintlb.status='UNCLAIMED', a, 0) as totUNCLMtkts, IF(wintlb.status='UNCLAIMED', winningAmount, 0) as unclmTktAmt FROM (  ");
					qry.append(" SELECT IF(draw_tlb.ticket_number>0,1,0) a, SUM((draw_tlb.bet_amount_multiple*draw_tlb.unit_price*prize_amount)/prize.unit_price) winningAmount, rank_id, draw_tlb.merchant_id, status  FROM st_sl_draw_ticket_").append(gameId).append("_").append(gameTypeId).append("_").append(purTblName).append(" draw_tlb ");
					qry.append(" INNER JOIN ");
					qry.append(" st_sl_prize_details_").append(gameId).append("_").append(gameTypeId).append(" prize  ON draw_tlb.merchant_id=prize.merchant_id  AND draw_tlb.draw_id=prize.draw_id  AND draw_tlb.draw_id=").append(drawId).append(" AND draw_tlb.rank_id=prize.prize_rank ");
					qry.append(" WHERE rank_id > 0  AND STATUS <> 'CANCELLED'   AND draw_tlb.merchant_id=").append(merchantId).append("  GROUP BY ticket_number)wintlb ) final ");
					
					stmtWinning = connection.createStatement();
					logger.info("getWinningDataReport for(GameId, GameTypeId, DrawId) ("+gameId+", "+gameTypeId+", "+drawId+") - "+qry.toString());
					rsWinning = stmtWinning.executeQuery(qry.toString());
					while(rsWinning.next()) {
						gameDataReportBean = salesDataMap.get(gameId+"-"+gameTypeId+"-"+drawId);
						if(gameDataReportBean != null) {
							gameDataReportBean.setNoOfWinning(rsWinning.getInt("totWinTkts"));
							gameDataReportBean.setWinningAmount(rsWinning.getDouble("totWin"));
							gameDataReportBean.setDiplayWinningAmt(df.format(rsWinning.getDouble("totWin")));
							gameDataReportBean.setTotalClaimedTkts(rsWinning.getInt("totCLMtkts"));
							gameDataReportBean.setTotalUnclaimedTkts(rsWinning.getInt("TotUNCLMTkts"));
							gameDataReportBean.setTotalClaimedAmt(rsWinning.getDouble("TotCLMTktAmt"));
							gameDataReportBean.setTotalUnclaimedAmt(rsWinning.getDouble("TotUNCLMTktAmt"));
						}
					}
				}
			}
				
		} catch (SQLException se) {
			se.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
	}

	public List<GameDataReportBean> fetchDrawWiseTicketInfo(int merchantId, int gameId, int gameTypeId, int drawId, Connection connection) throws SLEException {
		logger.info("-- Inside fetchDrawWiseTicketInfo (Dao) --");

		Statement stmt = null;
		ResultSet rs = null;
		List<GameDataReportBean> reportList = new ArrayList<GameDataReportBean>();
		GameDataReportBean reportBean = null;
		SimpleDateFormat dateFormat = null;
		try {
			int purchaseTableName = 0;
			stmt = connection.createStatement();
			String query = "SELECT purchase_table_name FROM st_sl_draw_master_"+gameId+" WHERE game_type_id="+gameTypeId+" AND draw_id="+drawId+";";
			logger.info("purchaseTable Query - "+query);
			rs = stmt.executeQuery(query);
			if(rs.last()) {
				purchaseTableName = rs.getInt("purchase_table_name");
			} else {
				throw new SLEException(SLEErrors.NO_RESULT_AVL_ERROR_CODE, SLEErrors.NO_RESULT_AVL_ERROR_MESSGE);
			}

			dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			stmt = connection.createStatement();

			String merchantDevName = Util.fetchMerchantInfoBean(merchantId).getMerchantDevName();
			int index = ("RMS".equals(merchantDevName)) ? 4 : 2;
			String prefix = ("RMS".equals(merchantDevName)) ? "XXXX" : "XX";

			query = "SELECT CONCAT(ticket_number,rpc_total)ticket_number,first_name,last_name,purchase_date,saleAmount,winAmt,rpc_total,status,mobile_nbr from st_sl_merchant_user_master mum INNER JOIN  (SELECT ticket_number ,SUM(saleAmount) saleAmount ,SUM(winAmt) winAmt ,status,MAX(rpc_total) rpc_total,purchase_date ,user_id from (SELECT dt.ticket_number,0 As winAmt,SUM(unit_price*dt.bet_amount_multiple) saleAmount  ,dt.status ,rpc_total ,stm.trans_date purchase_date,stm.user_id  FROM st_sl_draw_ticket_"+gameId+"_"+gameTypeId+"_"+purchaseTableName+" dt INNER JOIN st_sl_game_tickets_"+gameId+"_"+gameTypeId+" gt INNER JOIN st_sl_sale_txn_master stm    ON stm.ticket_nbr =  dt.ticket_number AND stm.trans_id=dt.trans_id AND gt.trans_id=dt.trans_id AND gt.ticket_number = dt.ticket_number AND dt.merchant_id=stm.merchant_id WHERE stm.merchant_id = "+merchantId+" and dt.draw_id = "+drawId+" AND dt.STATUS NOT IN ('CANCELLED','FAILED') AND stm.status='DONE' and  rank_id>0  GROUP BY dt.ticket_number UNION ALL SELECT dt.ticket_number,SUM(dt.bet_amount_multiple*dt.unit_price*prize_amount)/pd.unit_price winAmt , 0 AS saleAmount ,dt.status,0 AS rpc_total ,0 As purchase_date,0 as user_id  FROM st_sl_draw_ticket_"+gameId+"_"+gameTypeId+"_"+purchaseTableName+"  dt INNER JOIN st_sl_prize_details_"+gameId+"_"+gameTypeId+"  pd ON dt.merchant_id=pd.merchant_id AND dt.rank_id=pd.prize_rank AND dt.draw_id=pd.draw_id WHERE dt.draw_id="+drawId+" AND rank_id >0 AND dt.merchant_id= "+merchantId+" group by ticket_number) mainTB group by ticket_number) saleWinTb on mum.user_id=saleWinTb.user_id where winAmt>0";
			
			logger.info("drawWiseTicketInfo Query - "+query);
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				reportBean = new GameDataReportBean();
				if("PMS".equals(merchantDevName)){
					if(rs.getString("first_name")==null || rs.getString("last_name")==null || rs.getString("first_name").equals("null") || rs.getString("last_name").equals("null")){
						reportBean.setMerchantOrgName(rs.getString("mobile_nbr"));
					}else{
						reportBean.setMerchantOrgName(rs.getString("first_name")+" "+rs.getString("last_name"));
						}
				}else {
					reportBean.setMerchantOrgName(rs.getString("first_name")+" "+rs.getString("last_name"));
				    }
				String ticketNum="RMS".equals(merchantDevName)?rs.getString("ticket_number"):rs.getString("ticket_number").substring(0, rs.getString("ticket_number").length()-1);
				
				if("UNCLAIMED".equals(rs.getString("status"))) {
					reportBean.setTicketNumber(prefix+ticketNum.substring(index, ticketNum.length()));
				} else {
					reportBean.setTicketNumber(ticketNum);
				}

				reportBean.setPurchaseTime(dateFormat.format(rs.getTimestamp("purchase_date")));
				reportBean.setTicketStatus(rs.getString("status"));
				reportBean.setSaleAmount(rs.getDouble("saleAmount"));
				reportBean.setWinningAmount(rs.getDouble("winAmt"));
				reportList.add(reportBean);
			}
		} catch (SQLException se) {
			se.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (SLEException se) {
			throw se;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}

		return reportList;
	}
	
	
	public List<SaleTrendDataBean> getSaleTrendDataDrawWise(int gameId, int gameTypeId, Timestamp startDate, Timestamp endDate, String reportType, int merchantId, Connection connection) throws SLEException {
		Statement stmt = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ResultSet saleResult = null;
		List<SaleTrendDataBean> dataMap = new ArrayList<SaleTrendDataBean>();
		SaleDataBean saleDataBean = null;
		WinningDataBean winningDataBean = null;
		DrawDataBean drawInfoBean = null;
		SaleTrendDataBean saleTrendBean = null;
		List<SaleDataBean> saleData = null;
		try {
				String query = "SELECT mas.draw_id, draw_name, draw_datetime, sale_start_time, draw_freeze_time, draw_status, purchase_table_name FROM st_sl_draw_master_"+gameId+" mas INNER JOIN st_sl_draw_merchant_mapping_"+gameId+" map ON mas.draw_id=map.draw_id WHERE game_type_id="+gameTypeId+" AND merchant_id="+merchantId+" AND draw_datetime>='"+startDate+"' AND draw_datetime<='"+endDate+"';";
				stmt = connection.createStatement();
				rs = stmt.executeQuery(query);
				
				String saleQuery = "SELECT day_name day, calendar_date date, ifnull(saleAmount,0.0) saleAmount, ifnull(noOfTickets,0) noOfTickets, round(ifnull(avgSalePerTicket,0.0) , 2) avgSalePerTicket FROM st_sl_calendar cal LEFT JOIN ( SELECT DATE_FORMAT(purchase_date,'%W') day, DATE(purchase_date) date, SUM(unit_price*bet_amount_multiple) saleAmount, count(ticket_number) noOfTickets, SUM(unit_price*bet_amount_multiple) / count(ticket_number) avgSalePerTicket FROM st_sl_draw_ticket_"+gameId+"_"+gameTypeId+"_? dt INNER JOIN st_sl_sale_txn_master tt ON tt.ticket_nbr =  dt.ticket_number WHERE tt.merchant_id = "+merchantId+" and draw_id = ?  AND dt.STATUS NOT IN ('CANCELLED','FAILED') AND tt.status='DONE' GROUP BY DATE(purchase_date) ) sale on calendar_date = sale.date where calendar_date >= date(?) and calendar_date <= date(?);" ;
				//String winDataQuery = "select prize_rank, prize_amount, no_of_winners as winners FROM st_sl_prize_details_"+gameId+"_"+gameTypeId+" WHERE draw_id = ? and merchant_id = "+merchantId+"; ";
				String winDataQuery = "SELECT prize_rank, prize_amount, count(distinct(ticket_number)) winners from st_sl_draw_ticket_"+gameId+"_"+gameTypeId+"_? a inner join  st_sl_prize_details_"+gameId+"_"+gameTypeId+" b on a.rank_id = b.prize_rank where b.merchant_id = "+merchantId+" and b.draw_id = ? and a.rank_id >0 group by a.rank_id;";
				
				while(rs.next()){
					
					int purchaseTableName = rs.getInt("purchase_table_name");
					int drawId = rs.getInt("draw_id");
					
					saleTrendBean  = new SaleTrendDataBean();				
					
					/* Prepare Draw Data */
					drawInfoBean = new DrawDataBean();
					drawInfoBean.setDrawId(rs.getInt("draw_id"));
					drawInfoBean.setDrawName(rs.getString("draw_name"));
					drawInfoBean.setDrawDateTime(rs.getString("draw_datetime"));
					drawInfoBean.setDrawStatus(rs.getString("draw_status"));
					drawInfoBean.setSaleStartTime(rs.getString("sale_start_time").substring(0,rs.getString("sale_start_time").lastIndexOf(".")));
					saleTrendBean.setDrawInfoBean(drawInfoBean);
					
					/* Prepare Sale Data */
					pstmt = connection.prepareStatement(saleQuery);
					pstmt.setInt(1, purchaseTableName);
					pstmt.setInt(2, drawId);
					pstmt.setString(3, rs.getString("sale_start_time"));
					pstmt.setString(4, rs.getString("draw_freeze_time"));
					saleResult = pstmt.executeQuery();
					saleData = new ArrayList<SaleDataBean>();
					while(saleResult.next()){
						saleDataBean = new SaleDataBean();
						saleDataBean.setDay(saleResult.getString("day"));
						saleDataBean.setDate(saleResult.getString("date"));
						saleDataBean.setSaleAmount(saleResult.getDouble("saleAmount"));
						saleDataBean.setTicketCount(saleResult.getInt("noOfTickets"));
						saleDataBean.setAvgSalePerTkt(saleResult.getDouble("avgSalePerTicket"));
						saleData.add(saleDataBean);
					}
					saleTrendBean.setSaleBeansDayWise(saleData);
					
					pstmt.clearParameters();
					
					/* Prepare Winning Data */
					pstmt = connection.prepareStatement(winDataQuery);
					pstmt.setInt(1, purchaseTableName);	
					pstmt.setInt(2, drawId);
					saleResult = pstmt.executeQuery();
					double totWinningAmt = 0.0;
					int totPlayers = 0;
					winningDataBean = new WinningDataBean();
					while(saleResult.next()){					
						if(saleResult.getInt("prize_rank") == 1){
							winningDataBean.setRankOneTotalPlayers(saleResult.getInt("winners"));
							winningDataBean.setRankOneWinningAmt(saleResult.getDouble("prize_amount"));
						}else if(saleResult.getInt("prize_rank") == 2){
							winningDataBean.setRankTwoTotalPlayers(saleResult.getInt("winners"));
							winningDataBean.setRankTwoWinningAmt(saleResult.getDouble("prize_amount"));
						}else {
							winningDataBean.setRankThreeTotalPlayers(saleResult.getInt("winners"));
							winningDataBean.setRankThreeWinningAmt(saleResult.getDouble("prize_amount"));
						}
						totWinningAmt+=saleResult.getDouble("prize_amount");
						totPlayers+=saleResult.getInt("winners");
						winningDataBean.setTotalWinningAmt(totWinningAmt);
						winningDataBean.setTotalPlayers(totPlayers);
						saleTrendBean.setWinningBean(winningDataBean);
					}										
					
					dataMap.add(saleTrendBean);					
				}
			
		} catch (SQLException se) {
			se.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return dataMap;
	}
	
	
	//sale report at BO
	public List<GameDataReportBean> getSaleReportData(int gameId,int gameTypeId,int merchantId, Timestamp startDate, Timestamp endDate,Connection con) throws SLEException{
		List<GameDataReportBean> drawWiseNetSaleList=new ArrayList<GameDataReportBean>();
		Statement stmt = null;
		Statement stmt1 = null;
		PreparedStatement pstmt = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		ResultSet archRs1 = null;
		ResultSet archRs2 = null;
		GameDataReportBean gameDataReportBean = null;
		SimpleDateFormat simpleDateFormat = null;
		String lastArchDate=null;
		String dateCheckAppender=null;
		try{
			
			String merchantName = Util.fetchMerchantInfoBean(merchantId).getMerchantDevName();
			simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			lastArchDate=SportsLotteryUtils.getLastArchDate(con);
			if(lastArchDate!=null && !startDate.after(Util.StringToDateConversion(lastArchDate+" 00:00:00"))){
				stmt1=con.createStatement();
				
				//Fetch archived data
				if(endDate.after(Util.StringToDateConversion(lastArchDate+" 00:00:00"))){
					dateCheckAppender="draw_datetime<='"+lastArchDate+" 23:59:59'";
				}else{
					dateCheckAppender="draw_datetime<='"+endDate+"'";
				}
				String archDrawQry="SELECT mas.draw_id, draw_name, year(draw_datetime) dYear, draw_datetime, draw_freeze_time, draw_status, purchase_table_name FROM st_sl_draw_master_"+gameId+" mas INNER JOIN st_sl_draw_merchant_mapping_"+gameId+" map ON mas.draw_id=map.draw_id WHERE game_type_id="+gameTypeId+" AND merchant_id="+merchantId+" AND draw_datetime>='"+startDate+"' AND "+dateCheckAppender+" AND draw_status='CLAIM ALLOW';";
				
				stmt = con.createStatement();
				archRs1 = stmt.executeQuery(archDrawQry);
				while(archRs1.next()){
					gameDataReportBean = new GameDataReportBean();
					int dYear=archRs1.getInt("dYear");
					int drawId = archRs1.getInt("draw_id");
					gameDataReportBean.setDrawId(drawId);
					gameDataReportBean.setGameId(gameId);
					gameDataReportBean.setGameTypeId(gameTypeId);
					gameDataReportBean.setDrawName(archRs1.getString("draw_name"));
					gameDataReportBean.setDrawTime(simpleDateFormat.format(archRs1.getTimestamp("draw_datetime").getTime()));
					gameDataReportBean.setDrawFreezeTime(simpleDateFormat.format(archRs1.getTimestamp("draw_freeze_time").getTime()));
					gameDataReportBean.setDrawStatus(archRs1.getString("draw_status"));
					gameDataReportBean.setGameId(gameId);
					gameDataReportBean.setGameName(SportsLotteryUtils.gameInfoMerchantMap.get(merchantName).get(gameId).getGameDispName());
					gameDataReportBean.setGameTypeId(gameTypeId);
					gameDataReportBean.setGameTypeName(SportsLotteryUtils.gameTypeInfoMerchantMap.get(merchantName).get(gameTypeId).getGameTypeDispName());
					
					String archDataQry="SELECT draw_id ,SUM(totSale-(totCancel+totWin)) netAmt FROM ( SELECT dd.draw_id,IFNULL(total_sale_value,0.00) totSale,IFNULL(total_cancel_amount,0.00) totCancel,IFNULL(total_winning_amount,0.00) totWin  FROM st_sl_draw_details_"+gameId+"_"+gameTypeId+"_"+dYear+" dd INNER JOIN st_sl_draw_master_"+gameId+" dm ON dd.draw_id=dm.draw_id  AND dd.draw_id="+drawId+") mainTB GROUP BY draw_id";
					
					archRs2 = stmt1.executeQuery(archDataQry);
					if(archRs2.next()){
						gameDataReportBean.setNetAmount(archRs2.getDouble("netAmt"));
						drawWiseNetSaleList.add(gameDataReportBean);
					}
					
					
				}
				
				//fetch normal data
				String query = "SELECT mas.draw_id, draw_name, draw_datetime, draw_freeze_time, draw_status, purchase_table_name FROM st_sl_draw_master_"+gameId+" mas INNER JOIN st_sl_draw_merchant_mapping_"+gameId+" map ON mas.draw_id=map.draw_id WHERE game_type_id="+gameTypeId+" AND merchant_id="+merchantId+" AND draw_datetime>'"+lastArchDate+" 23:59:59' AND draw_datetime<='"+endDate+"' AND draw_status='CLAIM ALLOW';";
				stmt = con.createStatement();
				rs1 = stmt.executeQuery(query);

				pstmt = con.prepareStatement("SELECT saleTB.draw_id, IFNULL(total_sale_amt-total_win_amt,0) netAmt FROM (SELECT  ? AS draw_id,sum(dt.unit_price *dt.bet_amount_multiple) total_sale_amt FROM st_sl_draw_ticket_"+gameId+"_"+gameTypeId+"_? dt INNER JOIN st_sl_sale_txn_master  stm ON dt.ticket_number= stm.ticket_nbr WHERE dt.draw_id = ?  AND  dt.status NOT IN ('CANCELLED','FAILED') AND stm.status ='DONE') saleTB INNER JOIN (SELECT ? as draw_id ,IFNULL(SUM(dt.bet_amount_multiple*dt.unit_price*prize_amount)/pd.unit_price,0) total_win_amt FROM st_sl_draw_ticket_"+gameId+"_"+gameTypeId+"_? dt INNER JOIN st_sl_prize_details_"+gameId+"_"+gameTypeId+" pd ON dt.merchant_id=pd.merchant_id AND dt.rank_id=pd.prize_rank AND dt.draw_id=pd.draw_id WHERE dt.draw_id=? AND rank_id >0 ) winTB ON saleTB.draw_id=winTB.draw_id;");
				while(rs1.next()) {
					int drawId = rs1.getInt("draw_id");
					int purchaseTableName = rs1.getInt("purchase_table_name");

					pstmt.setInt(1, drawId);
					pstmt.setInt(2, purchaseTableName);
					pstmt.setInt(3, drawId);
					pstmt.setInt(4, drawId);
					pstmt.setInt(5, purchaseTableName);
					pstmt.setInt(6, drawId);
					

					gameDataReportBean = new GameDataReportBean();
					gameDataReportBean.setDrawId(drawId);
					gameDataReportBean.setDrawName(rs1.getString("draw_name"));
					gameDataReportBean.setDrawTime(simpleDateFormat.format(rs1.getTimestamp("draw_datetime").getTime()));
					gameDataReportBean.setDrawFreezeTime(simpleDateFormat.format(rs1.getTimestamp("draw_freeze_time").getTime()));
					gameDataReportBean.setDrawStatus(rs1.getString("draw_status"));
					gameDataReportBean.setGameId(gameId);
					gameDataReportBean.setGameName(SportsLotteryUtils.gameInfoMerchantMap.get(merchantName).get(gameId).getGameDispName());
					gameDataReportBean.setGameTypeId(gameTypeId);
					gameDataReportBean.setGameTypeName(SportsLotteryUtils.gameTypeInfoMerchantMap.get(merchantName).get(gameTypeId).getGameTypeDispName());
					
					
					rs2 = pstmt.executeQuery();
					if(rs2.next()) {
						gameDataReportBean.setNetAmount(rs2.getDouble("netAmt"));
					}
					drawWiseNetSaleList.add(gameDataReportBean);
				}
			}else{
				String query = "SELECT mas.draw_id, draw_name, draw_datetime, draw_freeze_time, draw_status, purchase_table_name FROM st_sl_draw_master_"+gameId+" mas INNER JOIN st_sl_draw_merchant_mapping_"+gameId+" map ON mas.draw_id=map.draw_id WHERE game_type_id="+gameTypeId+" AND merchant_id="+merchantId+" AND draw_datetime>='"+startDate+"' AND draw_datetime<='"+endDate+"' AND draw_status='CLAIM ALLOW';";
				stmt = con.createStatement();
				rs1 = stmt.executeQuery(query);

				pstmt = con.prepareStatement("SELECT saleTB.draw_id, IFNULL(total_sale_amt-total_win_amt,0) netAmt FROM (SELECT  ? AS draw_id,sum(dt.unit_price *dt.bet_amount_multiple) total_sale_amt FROM st_sl_draw_ticket_"+gameId+"_"+gameTypeId+"_? dt INNER JOIN st_sl_sale_txn_master  stm ON dt.ticket_number= stm.ticket_nbr WHERE dt.draw_id = ?  AND  dt.status NOT IN ('CANCELLED','FAILED') AND stm.status ='DONE') saleTB INNER JOIN (SELECT ? as draw_id ,IFNULL(SUM(dt.bet_amount_multiple*dt.unit_price*prize_amount)/pd.unit_price,0) total_win_amt FROM st_sl_draw_ticket_"+gameId+"_"+gameTypeId+"_? dt INNER JOIN st_sl_prize_details_"+gameId+"_"+gameTypeId+" pd ON dt.merchant_id=pd.merchant_id AND dt.rank_id=pd.prize_rank AND dt.draw_id=pd.draw_id WHERE dt.draw_id=? AND rank_id >0 ) winTB ON saleTB.draw_id=winTB.draw_id;");
				while(rs1.next()) {
					int drawId = rs1.getInt("draw_id");
					int purchaseTableName = rs1.getInt("purchase_table_name");

					pstmt.setInt(1, drawId);
					pstmt.setInt(2, purchaseTableName);
					pstmt.setInt(3, drawId);
					pstmt.setInt(4, drawId);
					pstmt.setInt(5, purchaseTableName);
					pstmt.setInt(6, drawId);
					

					gameDataReportBean = new GameDataReportBean();
					gameDataReportBean.setDrawId(drawId);
					gameDataReportBean.setDrawName(rs1.getString("draw_name"));
					gameDataReportBean.setDrawTime(simpleDateFormat.format(rs1.getTimestamp("draw_datetime").getTime()));
					gameDataReportBean.setDrawFreezeTime(simpleDateFormat.format(rs1.getTimestamp("draw_freeze_time").getTime()));
					gameDataReportBean.setDrawStatus(rs1.getString("draw_status"));
					gameDataReportBean.setGameId(gameId);
					gameDataReportBean.setGameTypeId(gameTypeId);
					gameDataReportBean.setGameName(SportsLotteryUtils.gameInfoMerchantMap.get(merchantName).get(gameDataReportBean.getGameId()).getGameDispName());
					gameDataReportBean.setGameTypeName(SportsLotteryUtils.gameTypeInfoMerchantMap.get(merchantName).get(gameTypeId).getGameTypeDispName());
					
					rs2 = pstmt.executeQuery();
					if(rs2.next()) {
						gameDataReportBean.setNetAmount(rs2.getDouble("netAmt"));
					}
					drawWiseNetSaleList.add(gameDataReportBean);
				}
			}

		}catch (SQLException se) {
			se.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		}catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}finally{
			DBConnect.closeConnection(pstmt, rs1);
			DBConnect.closeConnection(stmt, rs2);
			DBConnect.closeRs(archRs1);
			DBConnect.closeConnection(stmt1, archRs2);
		}
		return drawWiseNetSaleList;
		
	}
}