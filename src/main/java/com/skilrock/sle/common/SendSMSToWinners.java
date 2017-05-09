package com.skilrock.sle.common;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skilrock.sle.common.exception.SLEException;

public class SendSMSToWinners extends Thread{
	private static final Logger logger = LoggerFactory.getLogger(SendSMSToWinners.class.getName());
	
	private int gameId ;
	private int gameTypeId;
	private int drawId;
	private int purTbleName;
	private String merchantList;
	

	public SendSMSToWinners(int gameId, int gameTypeId, String merchantList, int drawId,int purTblName) {
		this.gameId = gameId;
		this.gameTypeId=gameTypeId;
		this.merchantList = merchantList;
		this.drawId = drawId;
		this.purTbleName=purTblName;
	}
	
	public static void sendVerificationCodeToWinners(int gameId,int gameTypeId, String merchantList,int drawId,int purTblName)throws SLEException{
		StringBuilder query=new StringBuilder();
		ResultSet rs=null;
		ResultSet rs1=null;
		String mobNbr=null;
		Statement stmt = null;
		String verificationCode=null;	
		String ticketNo=null;
		Connection con = null;
		try{
			con = DBConnect.getConnection();
			stmt = con.createStatement();
		
			query.append("SELECT mobile_nbr mob_nbr,ticket_number,verification_code FROM st_sl_draw_ticket_").append(gameId).append("_").append(gameTypeId).append("_").append(purTblName).append(" pTB inner join (SELECT um.user_id,um.merchant_id,mobile_nbr FROM  st_sl_merchant_player_master pm INNER JOIN st_sl_merchant_user_master um ON pm.merchant_user_id=um.merchant_user_id) player ON pTB.party_id=player.user_id INNER JOIN st_sl_merchant_master sm ON  player.merchant_id=sm.merchant_id WHERE rank_id > 0 AND draw_id=").append(drawId).append(" AND merchant_dev_name IN (").append(merchantList).append(") GROUP BY pTB.ticket_number"); 
			logger.info("***** Inside sendVerificationCodeToWinners with query {}", query);

			rs1=stmt.executeQuery(query.toString());
			while(rs1.next()){
				StringBuilder msg=new StringBuilder();
				mobNbr=rs1.getString("mob_nbr");
				verificationCode=rs1.getString("verification_code");
				ticketNo=String.valueOf(rs1.getLong("ticket_number"));
				msg.append("Dear Customer Your Ticket : ").append(ticketNo).append(" got winning. Kindly Visit Africa Lotto Back Office with verification code : ").append(verificationCode).append( " to claim your Winning");
				logger.info("Send SMS to winners : SMS Message"+msg);
				SendSMS smw=new SendSMS(mobNbr, msg.toString());
				smw.setDaemon(true);
				smw.start();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
		}finally{
			DBConnect.closeConnection(con, stmt, rs1);
			DBConnect.closeRs(rs);
		}
	}
	
	
	public void run() {
		try {
			logger.info(new StringBuilder("Send SMS to winners : gameId ").append(gameId).append(" ,draw ID ").append(drawId).append(" merchant List ").append(merchantList).toString());
			sendVerificationCodeToWinners(gameId, gameTypeId, merchantList, drawId,purTbleName);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
