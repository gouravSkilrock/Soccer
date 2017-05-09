package com.skilrock.sle.reportsMgmt.daoImpl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.skilrock.sle.common.SLELogger;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.commonMethod.daoImpl.CommonMethodsDaoImpl;
import com.skilrock.sle.reportsMgmt.javaBeans.FixtureWiseWinningAnalysisReportBean;

public class FixtureWiseWinningAnalysisReportDaoImpl {
	private static final SLELogger logger = SLELogger.getLogger(FixtureWiseWinningAnalysisReportDaoImpl.class.getName());

	private static FixtureWiseWinningAnalysisReportDaoImpl instance;

	private FixtureWiseWinningAnalysisReportDaoImpl(){}

	public static FixtureWiseWinningAnalysisReportDaoImpl getInstance() {
		if (instance == null) {
			synchronized (FixtureWiseWinningAnalysisReportDaoImpl.class) {
				if (instance == null) {
					instance = new FixtureWiseWinningAnalysisReportDaoImpl();
				}
			}
		}

		return instance;
	}

	public Map<Integer, String> getDrawData(int merchantId, int gameId, int gameTypeId, String selectedDate, Connection connection) throws SLEException {
		logger.info("--- getDrawData (DAO) ---");

		Statement stmt = null;
		ResultSet rs = null;
		Map<Integer, String> drawMap = new TreeMap<Integer, String>();
		try {
			stmt = connection.createStatement();
			String query = "SELECT mas.draw_id draw_id, draw_name FROM st_sl_draw_master_"+gameId+" mas INNER JOIN st_sl_draw_merchant_mapping_"+gameId+" map ON mas.draw_id=map.draw_id WHERE merchant_id="+merchantId+" AND game_type_id="+gameTypeId+" AND DATE(draw_datetime)=DATE('"+selectedDate+"') AND draw_status IN ('CLAIM HOLD', 'CLAIM ALLOW');";
			logger.info("getDrawData Query - "+query);
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				drawMap.put(rs.getInt("draw_id"), rs.getString("draw_name"));
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}

		return drawMap;
	}

	public Map<Integer, FixtureWiseWinningAnalysisReportBean> getFixtureWinningData(int gameId, int gameTypeId, int drawId, Connection connection) throws SLEException {
		logger.info("--- getFixtureWinningData (DAO) ---");

		Statement stmt = null;
		ResultSet rs = null;
		Map<String, String> optionMap=null;
		Map<String, String> eventCountMap = null;
		Map<Integer, FixtureWiseWinningAnalysisReportBean> reportMap = new TreeMap<Integer, FixtureWiseWinningAnalysisReportBean>();
		FixtureWiseWinningAnalysisReportBean reportBean = null;
		try {
			stmt = connection.createStatement();
			optionMap = CommonMethodsDaoImpl.getInstance().fetchGameTypeOptionMap(gameId, gameTypeId, connection);
			int noOfEvents = 0;
			int purchaseTableName = 0;
			String query = "SELECT no_of_events, purchase_table_name FROM ((SELECT no_of_events FROM st_sl_game_type_master WHERE game_id="+gameId+" AND game_type_id="+gameTypeId+") aa, (SELECT purchase_table_name FROM st_sl_draw_master_"+gameId+" WHERE game_type_id="+gameTypeId+" AND draw_id="+drawId+") bb);";
			logger.info("noOfEvents, purchaseTableName Query - "+query);
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				noOfEvents = rs.getInt("no_of_events");
				purchaseTableName = rs.getInt("purchase_table_name");
			}

			query = "SELECT eom1.event_id, event_description, GROUP_CONCAT(CONCAT(eom1.evt_opt_id,'-',eom1.option_code) ORDER BY eom1.evt_opt_id ASC) option_id_code_list, dem.evt_opt_id, eom2.option_code FROM st_sl_event_option_mapping eom1 INNER JOIN st_sl_event_master em ON em.event_id=eom1.event_id INNER JOIN st_sl_draw_event_mapping_"+gameId+" dem ON eom1.event_id=dem.event_id AND draw_id="+drawId+" INNER JOIN st_sl_event_option_mapping eom2 ON dem.evt_opt_id=eom2.evt_opt_id WHERE eom1.option_code<>'C' and eom1.game_id="+gameId+" and eom1.game_type_id="+gameTypeId+" GROUP BY event_id ORDER BY dem.event_order;";
			logger.info("Event Option Details Query - "+query);
			rs = stmt.executeQuery(query);
			List<Integer> eventList = new ArrayList<Integer>();
			int i=1;
			while(rs.next()) {
				eventList.add(rs.getInt("event_id"));

				reportBean = new FixtureWiseWinningAnalysisReportBean();
				reportBean.setEventId(rs.getInt("event_id"));
				reportBean.setEventDescription(rs.getString("event_description"));
				reportBean.setOptionIdCodeList(rs.getString("option_id_code_list"));
				reportBean.setWinningOptionId(rs.getInt("evt_opt_id"));
				reportBean.setWinningOptionCode(rs.getString("option_code"));
				reportBean.setOptionCodeMap(optionMap);
				reportMap.put(i++, reportBean);
			}

			StringBuilder mainQueryBuilder = new StringBuilder("SELECT ");
			StringBuilder queryBuilder = new StringBuilder();
			i=1;
			for(Map.Entry<Integer, FixtureWiseWinningAnalysisReportBean> entry : reportMap.entrySet()) {
				
				queryBuilder.append("CONCAT(");
				FixtureWiseWinningAnalysisReportBean tempBean = entry.getValue();
				String[] optionIdCodeList = tempBean.getOptionIdCodeList().split(",");
				for(String option:optionIdCodeList){
					String optionName = option.split("-")[1].replace('+', 'b');
					queryBuilder.append("SUM(e").append(i).append(optionName).append("),'~").append(option.split("-")[1]).append(",',");
				}
				queryBuilder.replace(queryBuilder.toString().length()-3, queryBuilder.toString().length(), "'");
				queryBuilder.append(") event_").append(i).append(", ");
				i++;
			}
			queryBuilder.delete(queryBuilder.lastIndexOf(", "), queryBuilder.length());
			mainQueryBuilder.append(queryBuilder).append(" FROM (SELECT ");	

			i=1;
			queryBuilder = new StringBuilder();
			
			for(Map.Entry<Integer, FixtureWiseWinningAnalysisReportBean> entry : reportMap.entrySet()) {
				FixtureWiseWinningAnalysisReportBean tempBean = entry.getValue();
				String[] optionIdCodeList = tempBean.getOptionIdCodeList().split(",");
				for(String option:optionIdCodeList){
					String optionName = option.split("-")[1].replace('+', 'b');
					queryBuilder.append("IF(event_").append(i).append("=").append(option.split("-")[0]).append(",1,0) AS 'e").append(i).append(optionName).append("',");
				}
				i++;
			}
			queryBuilder.delete(queryBuilder.lastIndexOf(","), queryBuilder.length());
			mainQueryBuilder.append(queryBuilder).append(" FROM st_sl_draw_ticket_").append(gameId).append("_").append(gameTypeId).append("_").append(purchaseTableName).append(" WHERE draw_id=").append(drawId).append(" AND status NOT IN ('CANCELLED','FAILED'))aa;");

			query = mainQueryBuilder.toString();
			logger.info("Fixture Winning Data Query - "+query);
			rs = stmt.executeQuery(query);
			if(rs.next()) {
				for(i=1; i<=noOfEvents; i++) {
					int denominator = 0;
					String[] countString = rs.getString("event_"+(i)).split(",");
					reportBean = reportMap.get(i);
					eventCountMap = new HashMap<String, String>();
					for(int k=0; k<countString.length; k++){
						eventCountMap.put(optionMap.get(countString[k].split("~")[1].trim()), countString[k].split("~")[0]);
						denominator = denominator+ Integer.parseInt(countString[k].split("~")[0]);
						
					}
					reportBean.setEventCountMap(eventCountMap);
					String numeratorStr=eventCountMap.get(optionMap.get(reportBean.getWinningOptionCode()))==null?"0":eventCountMap.get(optionMap.get(reportBean.getWinningOptionCode()));
					int numerator = Integer.parseInt(numeratorStr); 
					reportBean.setWinningOptionCode(optionMap.get(reportBean.getWinningOptionCode()));
					double winPercentage=0.0;
					if(numerator==0){
						winPercentage = 100.00;
					}else{
						winPercentage = denominator!=0 ? ((double) numerator/denominator)*100 : 0.00;
					}
					
					reportBean.setWinPercentage(winPercentage);
				}
			}
			optionMap.remove("C");
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}

		return reportMap;
	}
}