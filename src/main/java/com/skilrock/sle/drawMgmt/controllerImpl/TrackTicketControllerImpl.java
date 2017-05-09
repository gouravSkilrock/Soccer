package com.skilrock.sle.drawMgmt.controllerImpl;

import java.sql.Connection;
import java.sql.SQLException;

import com.skilrock.sle.common.ConfigurationVariables;
import com.skilrock.sle.common.DBConnect;
import com.skilrock.sle.common.SportsLotteryUtils;
import com.skilrock.sle.common.UtilityFunctions;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.common.javaBeans.ValidateTicketBean;
import com.skilrock.sle.drawMgmt.daoImpl.TrackTicketDaoImpl;
import com.skilrock.sle.drawMgmt.javaBeans.TrackSLETicketBean;

public class TrackTicketControllerImpl {
	private static TrackTicketControllerImpl singleInstance;

	private TrackTicketControllerImpl(){}

	public static TrackTicketControllerImpl getInstance() {
		if (singleInstance == null) {
			synchronized (TrackTicketControllerImpl.class) {
				if (singleInstance == null) {
					singleInstance = new TrackTicketControllerImpl();
				}
			}
		}

		return singleInstance;
	}

	public TrackSLETicketBean trackSLETicket(String merchantName,String ticketNumber) throws SLEException {
		Connection connection = null;
		TrackTicketDaoImpl daoImpl = null;
		TrackSLETicketBean trackTicketBean = null;
		int merchantId;
		int barCodeCount = 0;
		String tktNum = null;
		try {
			daoImpl = TrackTicketDaoImpl.getInstance();

			connection = DBConnect.getConnection();
			tktNum = ticketNumber;
			if("RMS".equalsIgnoreCase(merchantName)){
				tktNum = getTicketNumber(ticketNumber, 3);
			}
			ValidateTicketBean validateTktBean = new ValidateTicketBean(tktNum);
			SportsLotteryUtils.validateTkt(validateTktBean, merchantName);
			if("RMS".equalsIgnoreCase(merchantName)){
				if(ticketNumber.length() == ConfigurationVariables.barCodeCountRMS){
					barCodeCount = SportsLotteryUtils.getBarCodeCountFromTicketNumber(ticketNumber);
					if(barCodeCount==0){
						throw new SLEException(SLEErrors.INVALID_TICKET_ERROR_CODE, SLEErrors.INVALID_TICKET_ERROR_MESSAGE);
					}
				}
			}
			validateTktBean.setBarCodeCount(barCodeCount);
			if(validateTktBean.isValid()){
				merchantId=UtilityFunctions.getMerchantIdFromMerchantName(merchantName, connection);
				trackTicketBean = daoImpl.trackSLETicket(merchantId,validateTktBean.getTicketNumInDB(),ticketNumber, barCodeCount, validateTktBean, connection);
				trackTicketBean.setTransId(validateTktBean.getTransId());
				trackTicketBean.setMerchantUserId(validateTktBean.getMerchantUserId());
				trackTicketBean.setRpcCount(validateTktBean.getReprintCount());
				if("Y".equalsIgnoreCase(validateTktBean.getIsCancel())){
					trackTicketBean.setTktStatus("CANCELLED");
				}
				
			}
			else
				throw new SLEException(SLEErrors.INVALID_TICKET_ERROR_CODE, SLEErrors.INVALID_TICKET_ERROR_MESSAGE);	
		} catch (SLEException se) {
			throw se;
		} catch (SQLException se) {
			se.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(connection);
		}

		return trackTicketBean;
	}

	public static String getTicketNumber(String ticketNumber, int InpType) {
		String originalTicket = "";
		if (ticketNumber != null && ticketNumber != "") {
			int ticketLength = ticketNumber.length();
			switch (InpType) {
			case 1:
				if (ticketLength == ConfigurationVariables.barCodeCountRMS) {
					originalTicket = SportsLotteryUtils.getTicketNumberForBarCode(ticketNumber);
				}
				break;
			case 2:
				if (ticketLength == ConfigurationVariables.tktLenRMS) 
					originalTicket = ticketNumber;
				
				break;
			case 3:
				if (ticketLength == ConfigurationVariables.barCodeCountRMS) {
					originalTicket = SportsLotteryUtils.getTicketNumberForBarCode(ticketNumber);
				} else if (ticketLength == ConfigurationVariables.tktLenRMS) {
					originalTicket = ticketNumber;
				}
				break;
			default:
				originalTicket = "ERROR";
				break;
			}
		}
		return originalTicket;
	}
	/*public static void main(String[] args) throws SLEException, SQLException, ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection connection = DriverManager.getConnection("jdbc:mysql://192.168.124.80/SLE_DB_FINAL", "root", "root");
		TrackSLETicketBean trackTicketBean = TrackTicketDaoImpl.getInstance().trackSLETicket("64776233150486030", connection);
		System.out.println(trackTicketBean);
	}*/
}