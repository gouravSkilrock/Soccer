package com.skilrock.sle.common;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.common.javaBeans.TicketInfoBean;
import com.skilrock.sle.common.javaBeans.ValidateTicketBean;
import com.skilrock.sle.commonMethod.daoImpl.CommonMethodsDaoImpl;
import com.skilrock.sle.drawMgmt.javaBeans.SLPrizeRankDistributionBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.GameMasterBean;
import com.skilrock.sle.gameDataMgmt.javaBeans.GameTypeMasterBean;
import com.skilrock.sle.gamePlayMgmt.javaBeans.DrawDetailBean;


public class SportsLotteryUtils {
	private static final Logger logger = LoggerFactory.getLogger(SportsLotteryUtils.class);
	public static Map<String, Map<Integer,GameMasterBean>> gameInfoMerchantMap=null; 
	public static Map<String, Map<Integer,GameTypeMasterBean>> gameTypeInfoMerchantMap=null;
	public static Map<String, String> curretnUserLoggedInMap= null;
	public static Map<String, Integer> userSessionMap = new HashMap<String, Integer>();

	
	public static Map<String, Map<Integer, DrawDetailBean>> eventDataDrawWise = null;
	public static Map<String, Map<Integer, SLPrizeRankDistributionBean>> prizeRankDistributionMap = null;
	public static Map<String, Map<Integer, Double>> drawSaleMap = null;
	
	public static Map<String, Integer> getUserSessionMap() {
		return userSessionMap;
	}

	public static void validateTkt(ValidateTicketBean tktBean, String merchantDevname) throws SLEException {
		tktBean.setValid(false);
		
		char tktNumChar[] = tktBean.getTicketNo().toCharArray();
		int userId = 0;
		int day = 0;
		boolean isUserExists = false;
		Connection con = null;
		try {
		/*	Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/sle_blank_db", "root", "");
			con = connection;*/
			con = DBConnect.getConnection();
			if("RMS".equals(merchantDevname) || "OKPOS".equalsIgnoreCase(merchantDevname)) {
				if(tktBean.getTicketNo() != null && tktBean.getTicketNo().length() != ConfigurationVariables.tktLenRMS) {					
					tktBean.setValid(false);
					return;
				}

				//Checking For Service Id
				if(Character.getNumericValue(tktNumChar[4]) != Util.merchantInfoMap.get(merchantDevname).getServiceId()) {
					tktBean.setValid(false);
					return;
				}

				//Checking Merchant Id
				if(Character.getNumericValue(tktNumChar[5]) != Util.merchantInfoMap.get(merchantDevname).getMerchantId()) {
					tktBean.setValid(false);
					return;
				}

				//Checking if UserExists
				userId = Integer.parseInt(tktBean.getTicketNo().subSequence(9, 13).toString());
				isUserExists = CommonMethodsDaoImpl.getInstance().checkMerchantUserExistence(userId, merchantDevname, con);
				/*if(isUserExists == false) {
					tktBean.setValid(false);
					return;
				}*/

				//Validating Ticket Expire
//				day = Integer.parseInt(tktNum.subSequence(5, 8).toString());
//				if (day > 366) {
//					tktBean.setValid(false);
//					return;
//				} else {
//					Calendar cal = Calendar.getInstance();
//					int expiryPeriod = cal.get(Calendar.DAY_OF_YEAR) - day;
//
//					if (expiryPeriod < 0) {
//						cal.set(cal.get(Calendar.YEAR) - 1, 1, 1);
//						expiryPeriod = cal.getActualMaximum(cal.DAY_OF_YEAR)
//								+ expiryPeriod;
//					}
//					if (expiryPeriod > getTicketExpiryPeroid(gameNo)) {
//						tktBean.setValid(false);
//						tktBean.setStatus("TICKET_EXPIRED");
//						tktBean.setTicketExpired(true);
//					}
//				}
				
				//Checking Tkt Nbr in DB
				tktBean.setMerchantCode(merchantDevname);
				CommonMethodsDaoImpl.getInstance().fetchTktinfo(tktBean.getTicketNo().substring(0, 17).toString(), tktBean, con);
				if (Integer.valueOf(tktBean.getTicketNo().charAt(17) + "") == tktBean.getReprintCount()) {
					tktBean.setValid(true);
				} else{
					tktBean.setValid(false);
				}
			} else if("PMS".equals(merchantDevname) || "Asoft".equalsIgnoreCase(merchantDevname)) {
				if(tktBean.getTicketNo() != null && tktBean.getTicketNo().length() != ConfigurationVariables.tktLenPMS) {
					tktBean.setValid(false);
					return;
				}
				
				//Checking For Service Id
				if(Character.getNumericValue(tktNumChar[2]) != Util.merchantInfoMap.get(merchantDevname).getServiceId()) {
					tktBean.setValid(false);
					return;
				}
				
				//Checking Merchant Id
				if(Character.getNumericValue(tktNumChar[3]) != Util.merchantInfoMap.get(merchantDevname).getMerchantId()) {
					tktBean.setValid(false);
					return;
				}
				
				//Checking if UserExists
				userId = Integer.parseInt(tktBean.getTicketNo().subSequence(7, 14).toString());
				isUserExists = CommonMethodsDaoImpl.getInstance().checkMerchantUserExistence(userId, merchantDevname, con);
				if(isUserExists == false) {
					tktBean.setValid(false);
					return;
				}
				
				day = Integer.parseInt(tktBean.getTicketNo().subSequence(4, 6).toString());
				if (day > 366) {
					tktBean.setValid(false);
					return;
				}
				tktBean.setMerchantCode(merchantDevname);
				CommonMethodsDaoImpl.getInstance().fetchTktinfo(tktBean.getTicketNo().substring(0, 16).toString(), tktBean, con);
				tktBean.setValid(true);
			}
		} catch (SLEException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.INVALID_TICKET_NUMBER_ERROR_CODE, SLEErrors.INVALID_TICKET_NUMBER_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(con);
		}
	}

	public static void validateTicketPWT(ValidateTicketBean ticketBean, String merchantName, Connection connection) throws SLEException {
		ticketBean.setValid(false);
		char tktNumChar[] = ticketBean.getTicketNo().toCharArray();
		int userId = 0;
		int day = 0;
		boolean isUserExists = false;
		try {
			if("RMS".equals(merchantName) || "OKPOS".equalsIgnoreCase(merchantName)) {
				if(ticketBean.getTicketNo() != null && ticketBean.getTicketNo().length() != ConfigurationVariables.tktLenRMS) {
					ticketBean.setValid(false);
					return;
				}

				if(Character.getNumericValue(tktNumChar[4]) != Util.merchantInfoMap.get(merchantName).getServiceId()) {
					ticketBean.setValid(false);
					return;
				}

				if(Character.getNumericValue(tktNumChar[5]) != Util.merchantInfoMap.get(merchantName).getMerchantId()) {
					ticketBean.setValid(false);
					return;
				}

				userId = Integer.parseInt(ticketBean.getTicketNo().subSequence(9, 14).toString());
				isUserExists = CommonMethodsDaoImpl.getInstance().checkMerchantUserExistence(userId, merchantName, connection);
				if(isUserExists == false) {
					ticketBean.setValid(false);
					return;
				}

				ticketBean.setMerchantCode(merchantName);
				CommonMethodsDaoImpl.getInstance().fetchTktinfo(ticketBean.getTicketNo().substring(0, 17).toString(), ticketBean, connection);
				if (Integer.valueOf(ticketBean.getTicketNo().charAt(17) + "") == ticketBean.getReprintCount()) {
					ticketBean.setValid(true);
				} else{
					ticketBean.setValid(false);
				}
			} else if("PMS".equals(merchantName) || "Asoft".equalsIgnoreCase(merchantName)) {
				if(ticketBean.getTicketNo() != null && ticketBean.getTicketNo().length() != ConfigurationVariables.tktLenPMS) {
					ticketBean.setValid(false);
					return;
				}

				if(Character.getNumericValue(tktNumChar[2]) != Util.merchantInfoMap.get(merchantName).getServiceId()) {
					ticketBean.setValid(false);
					return;
				}

				if(Character.getNumericValue(tktNumChar[3]) != Util.merchantInfoMap.get(merchantName).getMerchantId()) {
					ticketBean.setValid(false);
					return;
				}

				userId = Integer.parseInt(ticketBean.getTicketNo().subSequence(7, 14).toString());
				isUserExists = CommonMethodsDaoImpl.getInstance().checkMerchantUserExistence(userId, merchantName, connection);
				/*if(isUserExists == false) {
					ticketBean.setValid(false);
					return;
				}*/
				
				day = Integer.parseInt(ticketBean.getTicketNo().subSequence(4, 6).toString());
				if (day > 366) {
					ticketBean.setValid(false);
					return;
				}
				ticketBean.setMerchantCode(merchantName);
				CommonMethodsDaoImpl.getInstance().fetchTktinfo(ticketBean.getTicketNo().substring(0, 16).toString(), ticketBean, connection);
				ticketBean.setValid(true);
			} else if("FPFCC".equals(merchantName)) {
				int saleMerchantId = 0;
				int merchantId = Util.merchantInfoMap.get(merchantName).getMerchantId();
				if(ticketBean.getTicketNo().trim().length() ==  ConfigurationVariables.tktLenRMS){
				  saleMerchantId = Character.getNumericValue(tktNumChar[5]);
				}else if(ticketBean.getTicketNo().trim().length() ==  ConfigurationVariables.tktLenPMS ){
				  saleMerchantId = Character.getNumericValue(tktNumChar[3]);
				}else {
				  ticketBean.setValid(false);
				  return;	
				}
				
				String saleMerchantName = Util.fetchMerchantInfoBean(saleMerchantId).getMerchantDevName();

				if("RMS".equals(saleMerchantName) && (ticketBean.getTicketNo().trim().length() != 18)) {
					ticketBean.setValid(false);
					return;
				} else if("PMS".equals(saleMerchantName) && (ticketBean.getTicketNo().trim().length() != 16)) {
					ticketBean.setValid(false);
					return;
				}

				if("PMS".equals(saleMerchantName) && (Character.getNumericValue(tktNumChar[2]) != Util.merchantInfoMap.get(saleMerchantName).getServiceId())) {
					ticketBean.setValid(false);
					return;
				}
				if("RMS".equals(saleMerchantName) && (Character.getNumericValue(tktNumChar[4]) != Util.merchantInfoMap.get(saleMerchantName).getServiceId())) {
					ticketBean.setValid(false);
					return;
				}

				if("PMS".equals(saleMerchantName) && Character.getNumericValue(tktNumChar[3]) != Util.merchantInfoMap.get(saleMerchantName).getMerchantId()) {
					ticketBean.setValid(false);
					return;
				}
				if("RMS".equals(saleMerchantName) && Character.getNumericValue(tktNumChar[5]) != Util.merchantInfoMap.get(saleMerchantName).getMerchantId()) {
					ticketBean.setValid(false);
					return;
				}

				/*userId = Integer.parseInt(ticketBean.getTicketNo().subSequence(9, 13).toString());
				isUserExists = CommonMethodsDaoImpl.getInstance().checkMerchantUserExistence(userId, saleMerchantName, connection);
				if(isUserExists == false) {
					ticketBean.setValid(false);
					return;
				}*/
                if("RMS".equals(saleMerchantName)){
				String isDateBypass = CommonMethodsDaoImpl.getInstance().checkSaleWinningClaimCondition(saleMerchantId, merchantId, connection);
					if(isDateBypass == null) {
						throw new SLEException(SLEErrors.TICKET_CLAIMING_NOT_AUTHORIZED_ERROR_CODE, SLEErrors.TICKET_CLAIMING_NOT_AUTHORIZED_ERROR_MESSAGE);
					} else {
						ticketBean.setDateBypass(("YES".equals(isDateBypass)) ? true : false);
					}
                }

				ticketBean.setMerchantCode(saleMerchantName);
				if("RMS".equals(saleMerchantName)) {
					CommonMethodsDaoImpl.getInstance().fetchTktinfo(ticketBean.getTicketNo().substring(0, 17).toString(), ticketBean, connection);
					if (Integer.valueOf(ticketBean.getTicketNo().charAt(17)+"") == ticketBean.getReprintCount()) {
						ticketBean.setValid(true);
					} else{
						ticketBean.setValid(false);
					}
				} else if("PMS".equals(saleMerchantName)) {
					CommonMethodsDaoImpl.getInstance().fetchTktinfo(ticketBean.getTicketNo().substring(0, 16).toString(), ticketBean, connection);
					if(Util.getCurrentTimeStamp().after(Timestamp.valueOf(ticketBean.getDrawClaimEndDate()))){
						throw new SLEException(SLEErrors.DRAW_EXPIRED_ERROR_CODE, SLEErrors.DRAW_EXPIRED_ERROR_MESSAGE);
					}
					ticketBean.setValid(true);
				  }

				//ticketBean.setMerchantCode(merchantName);
			}
		} catch (SLEException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.INVALID_TICKET_NUMBER_ERROR_CODE, SLEErrors.INVALID_TICKET_NUMBER_ERROR_MESSAGE);
		}
	}

	//Used During PWT
	/*public static ValidateTicketBean validateTkt(String tktNum, String merchantDevname) throws SLEException {
		int retId = 0;
		int retIdLen = 0;
		int gameTypeId = 0;
		int tktLen = 0;
		int day = 0;
		String date = null;
		String gameName = null;
		String tktBuf = null;
		String ticketNumInDB=null;
		//boolean isNewtktFormat=false;

		String reprintCount = null;
		ValidateTicketBean tktBean = new ValidateTicketBean();
		tktBean.setValid(false);
		try{
		if (tktNum != null) {
			
				tktLen = 18;
				retIdLen = 5;
				tktBuf = tktNum.substring(retIdLen, retIdLen
						+ 1);
				reprintCount = tktNum.substring(tktLen - 1, tktLen);
				ticketNumInDB=tktNum.substring(0, tktLen - 1);
			
			gameTypeId = Integer.parseInt(tktBuf);
			int gameId=gameTypeInfoMerchantMap.get(merchantDevname).get(gameTypeId).getGameId();
			day = Integer.parseInt(tktNum.substring(retIdLen + tktBuf.length(),
					retIdLen + tktBuf.length() + 3));
			
		
			//isNewtktFormat=isTktFormatChnged(day);
			//tktBean.setNewFormat(isNewtktFormat);
			if(isNewtktFormat)
				reprintCount = tktNum.substring(tktLen - 1, tktLen);
			else
			
			gameName=gameInfoMerchantMap.get(merchantDevname).get(gameId).getGameDispName();
			if (gameName != null) {
				tktBean.setGameName(gameName);
				tktBean.setGameNo(gameTypeId);
				tktBean.setReprintCount(reprintCount);
				tktBean.setTicketNumInDB(isNewtktFormat?tktNum.substring(0, tktLen - 1):ticketNumInDB);
				tktBean.setValid(true);
				tktBean.setGameid(gameId);
			}
			if (day > 366) {
				tktBean.setValid(false);
			} else {

				Calendar cal = Calendar.getInstance();
				int expiryPeriod = cal.get(Calendar.DAY_OF_YEAR) - day;

				if (expiryPeriod < 0) {
					cal.set(cal.get(Calendar.YEAR) - 1, 1, 1);
					expiryPeriod = cal.getActualMaximum(cal.DAY_OF_YEAR)
							+ expiryPeriod;

				}

				if (expiryPeriod > getTicketExpiryPeroid(gameNo)) {
					tktBean.setValid(false);
					tktBean.setStatus("TICKET_EXPIRED");
					tktBean.setTicketExpired(true);
				}

			}
		}
		}catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.INVALID_TICKET_NUMBER_ERROR_CODE, SLEErrors.INVALID_TICKET_NUMBER_ERROR_MESSAGE);

		}
		logger.debug(tktBean.getGameNo() + "**Validate"
				+ tktBean.getTicketNumInDB() + "**" + tktBean.getReprintCount()
				+ "**" + tktBean.isValid());
		return tktBean;
	}*/
	
	public static int[][] getAllCombinationsBoardWise(int[][] selectedEventArray){

		/*String s1[] = new String[3];
		s1[0] = "1";
		s1[1] = "2";
		s1[2] = "3";

		String s2[] = new String[3];
		s2[0] = "4";
		s2[1] = "5";
		s2[2] = "6";
		
		String s3[] = new String[3];
		s3[0] = "7";
		s3[1] = "8";
		s3[2] = "9";
		
		String s[][] = new String[3][];
		s[0] = s1;
		s[1]=s2;
		
		s[2] = s3
				;
		//s[3]=s1;
		//s[4]=s1;
		//s[5]=s1;
		//s[6]=s1;
*/		
		for (int i = 0; i < selectedEventArray.length; i++) {
			for (int j = 0; j < selectedEventArray[i].length; j++) {

				// plant = Arrays.toString(plants[i]);
				// color = Arrays.deepToString(plants[j]);
				// System.out.println(plant + " " + color);
//				System.out.print(selectedEventArray[i][j] + "\t");

			}
//			System.out.println();
		}
		// System.out.println(s);

		int validCombos = 1;

		for (int i = 0; i < selectedEventArray.length; i++)
			validCombos *= selectedEventArray[i].length;
//		System.out.println("Valid combinations:" + validCombos);

		int[][] combosArray = new int[validCombos][selectedEventArray.length];

		int combo;
		int offset = 1;

		for (int i=0; i < selectedEventArray.length; i++)
		{
		    combo = 0;
		    while (combo < validCombos)
		    {
		        for (int j=0; j < selectedEventArray[i].length; j++)
		        {
		            for (int k=0; k < offset; k++)
		            {
		                if (combo < validCombos)
		                {
		                    combosArray[combo][i] = selectedEventArray[i][j];
		                    combo++;
		                }
		            }
		        }
		    }
		    offset *= selectedEventArray[i].length;
		}

//		for (int i=0; i < combosArray.length; i++)
//		{
//		    System.out.print("[ ");
//		    for (int j=0; j < combosArray[i].length; j++)
//		        System.out.print(combosArray[i][j] + " ");
//		    System.out.println("]");
//		}
		
	
		return combosArray;
	}

	public static void loggedInUser(String merUserId, String sessionId){
		curretnUserLoggedInMap = new HashMap<String, String>();
		curretnUserLoggedInMap.put(merUserId, sessionId);
	}
	
	public static void updateLoggedInUserMap(String merUserId, String sessionId){
		curretnUserLoggedInMap.remove(merUserId);
	}
	
	public static Map<String, String> getCurretnUserLoggedInMap() {
		return curretnUserLoggedInMap;
	}
	
	public static boolean isUserExist(String merchantUserId){
		for(Entry<String, String> itr:curretnUserLoggedInMap.entrySet()){
			if(merchantUserId.equalsIgnoreCase(itr.getKey())){
				return true;
			}
		}
		return false;
	}
	
	public static String getTicketNumberForBarCode(String ticketNumber) {
		return ticketNumber.substring(0, ConfigurationVariables.barCodeCountRMS	- ConfigurationVariables.barCodeLengthRMS);
	}
	
	public static int getBarCodeCountFromTicketNumber(String ticketNumber) {
		return Integer.parseInt(ticketNumber.substring(ConfigurationVariables.barCodeCountRMS - ConfigurationVariables.barCodeLengthRMS,ConfigurationVariables.barCodeCountRMS));
	}
	
	public static boolean isBarCodeCorrectFotTicket(String ticketNum,int gameId,int gameTypeId,int barCode) throws SLEException{
		Connection con=null;
		Statement stmt=null;
		ResultSet rs=null;
		String query=null;
		boolean isValid=false;
		try {
			con=DBConnect.getConnection();
			query="SELECT stm.trans_id from st_sl_sale_txn_master stm INNER JOIN st_sl_game_tickets_"+gameId+"_"+gameTypeId+" gt on stm.trans_id=gt.trans_id and stm.ticket_nbr=gt.ticket_number WHERE ticket_number="+ticketNum+" AND barcode_count="+barCode;
			stmt=con.createStatement();
			rs=stmt.executeQuery(query);
			if(rs.next()){
				isValid=true;
			}
		} catch (Exception e) {
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}finally{
			DBConnect.closeConnection(con, stmt, rs);
		}
		return isValid;
	}
	
	public static String getLastArchDate(Connection con) {
		 PreparedStatement pstmt = null;
		 ResultSet rs = null;
		 String lastDate=null;
		 try {
			pstmt = con.prepareStatement("select alldate last_date from datestore order by alldate desc limit 1");
			rs=pstmt.executeQuery();
			if(rs.next()){
				lastDate=rs.getString("last_date");	
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return lastDate;
	}
	
	public static void updateVerificationCode(int gameId,int gameTypeId,int purTblName,Connection con,int drawId,String merchantList) throws SLEException{
		StringBuilder query=new StringBuilder();
		PreparedStatement pStmt = null;
		try {
			query.append("UPDATE st_sl_draw_ticket_").append(gameId).append("_").append(gameTypeId).append("_").append(purTblName).append(" A INNER JOIN (select ticket_number,floor( 1000000+RAND() * 100000) random from st_sl_draw_ticket_").append(gameId).append("_").append(gameTypeId).append("_").append(purTblName).append(" st INNER JOIN st_sl_merchant_master sm on st.merchant_id=sm.merchant_id  WHERE rank_id > 0 AND merchant_dev_name IN (").append(merchantList).append(") group by ticket_number ) As B on A.ticket_number=B.ticket_number SET verification_code= random");
			logger.info("***** Inside updateVerificationCode with query {}", query);
			pStmt = con.prepareStatement(query.toString());
			pStmt.executeUpdate();
		}catch (SQLException e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE,SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}finally{
			   DBConnect.closePstmt(pStmt);
		 }
	}
	
	public static void fetchTicketInfo(TicketInfoBean tktInfoBean, String tktNum ,Connection con){
		  Statement stmt = null;
		  String query = null;
		  ResultSet rs1 = null;
		  ResultSet rs2 = null;
		  ResultSet rs3 = null;
		  try{
			  query = "Select trans_id,game_type_id,game_id,ticket_nbr From st_sl_sale_txn_master Where ticket_nbr ="+tktNum;
			  stmt = con.createStatement();
			  rs1 = stmt.executeQuery(query);
			  if(rs1.next()){
				  tktInfoBean.setGameId(rs1.getInt("game_id"));
				  tktInfoBean.setGameTypeId(rs1.getInt("game_type_id"));
				  query = "Select ssdm.draw_status,ssdm.purchase_table_name,ssdm.draw_id,ssstm.amount From st_sl_sale_txn_master ssstm INNER JOIN st_sl_game_tickets_"+rs1.getString("game_id")+"_"+rs1.getString("game_type_id")+" ssgt ON ssstm.trans_id = ssgt.trans_id INNER JOIN st_sl_draw_master_"+rs1.getString("game_id")+" ssdm ON ssgt.draw_id = ssdm.draw_id WHERE ssstm.ticket_nbr ="+tktNum;
				  rs2 = stmt.executeQuery(query);
				  if(rs2.next()){
					  tktInfoBean.setPurchaseTable(rs2.getInt("purchase_table_name"));
					  query = "Select ticket_number,bet_amount_multiple,status,verification_code From st_sl_draw_ticket_"+tktInfoBean.getGameId()+"_"+tktInfoBean.getGameTypeId()+"_"+tktInfoBean.getPurchaseTable()+" Where ticket_number ="+tktNum;
					  rs3 = stmt.executeQuery(query);
					  if(rs3.next()){
						  tktInfoBean.setTktNbr(rs3.getLong("ticket_number"));
						  tktInfoBean.setStatus(rs3.getString("status"));
						  tktInfoBean.setAmount(rs3.getDouble("bet_amount_multiple"));
						  tktInfoBean.setVerificationCode(rs3.getString("verification_code"));
					  }
				  }
			  }
		  }
		  catch (Exception e) {
		   e.printStackTrace();
		  } finally{
		   DBConnect.closeStatement(stmt);
		   DBConnect.closeRs(rs1,rs2,rs3);
		  }
		  
		 }
	
}