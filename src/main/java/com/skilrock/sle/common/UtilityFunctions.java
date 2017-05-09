package com.skilrock.sle.common;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;

import com.google.gson.Gson;
import com.skilrock.sle.common.exception.SLEErrors;
import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.common.javaBeans.ValidateTicketBean;
import com.skilrock.sle.merchant.common.javaBeans.MerchantInfoBean;

public class UtilityFunctions {
	
	private static final SLELogger logger = SLELogger.getLogger(UtilityFunctions.class.getName());

	public static int getMerchantIdFromMerchantName(String merchantDevName, Connection connection) throws SLEException{
		Statement stmt = null;
		ResultSet rs = null;
		int merchantId = 0;
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery("SELECT merchant_id FROM st_sl_merchant_master WHERE merchant_dev_name='"+merchantDevName+"';");
			if(rs.next()) {
				merchantId = rs.getInt("merchant_id");
			} else {
				throw new SLEException(SLEErrors.INVALID_MERCHANT_NAME_ERROR_CODE, SLEErrors.INVALID_MERCHANT_NAME_ERROR_MESSAGE);
			}
		}catch (SLEException e) {
			throw e;
		}catch (Exception ex) {
			ex.printStackTrace();
		}
		return merchantId;
	}
	
	public static int getMerchantTicketLength(String merchantDevName){
		int ticketLen = 0;
		if("RMS".equalsIgnoreCase(merchantDevName)){
			ticketLen = ConfigurationVariables.tktLenRMS;
		}else if("PMS".equalsIgnoreCase(merchantDevName)){
			ticketLen = ConfigurationVariables.tktLenPMS;
		}
		return ticketLen;
	}
	
	public static int getMerchantUserIdLength(String merchantDevName){
		int userIdLen = 0;
		if("RMS".equalsIgnoreCase(merchantDevName)){
			userIdLen = ConfigurationVariables.userIdLenRMS;
		}else if("PMS".equalsIgnoreCase(merchantDevName)){
			userIdLen = ConfigurationVariables.userIdLenPMS;
		}
		return userIdLen;
	}
	
	public static int getMerchantServiceIdFromTktNum(String tktNum, String merchantDevName){
		int merServiceId = 0;
		if("RMS".equalsIgnoreCase(merchantDevName)){
			merServiceId = Integer.parseInt(tktNum.substring(ConfigurationVariables.randomNumberLenInTktRMS,ConfigurationVariables.randomNumberLenInTktRMS+1));
		}else if("PMS".equalsIgnoreCase(merchantDevName)){
			merServiceId = Integer.parseInt(tktNum.substring(ConfigurationVariables.randomNumberLenInTktPMS,ConfigurationVariables.randomNumberLenInTktPMS+1));
		}
		return merServiceId;
	}
	
	public static int getMerchantIdFromTktNum(String tktNum, String merchantDevName){
		int merId = 0;
		if("RMS".equalsIgnoreCase(merchantDevName)){
			merId = Integer.parseInt(tktNum.substring(ConfigurationVariables.randomNumberLenInTktRMS+1,ConfigurationVariables.randomNumberLenInTktRMS+2));
		}else if("PMS".equalsIgnoreCase(merchantDevName)){
			merId = Integer.parseInt(tktNum.substring(ConfigurationVariables.randomNumberLenInTktPMS+1,ConfigurationVariables.randomNumberLenInTktPMS+2));
		}
		return merId;
	}
	
	public static int getMerchantTktReprintCount(String tktNum, String merchantDevName){
		int reprintCount = 0;
		if("RMS".equalsIgnoreCase(merchantDevName)){
			reprintCount = Integer.parseInt(tktNum.substring(tktNum.length()-1, tktNum.length()));
		}else if("PMS".equalsIgnoreCase(merchantDevName)){
			reprintCount = 0;
		}
		return reprintCount;
	}
	
	public static String getDBTkt(String tktNum, String merchantDevName){
		String dbTicket = null;
		if("RMS".equalsIgnoreCase(merchantDevName)){
			dbTicket = tktNum.substring(0,tktNum.length()-1);
		}else if("PMS".equalsIgnoreCase(merchantDevName)){
			dbTicket = tktNum;
		}
		return dbTicket;
	}
	
	public static int getDaysFromTkt(String tktNum, String merchantDevName){
		int days = 0;
		if("RMS".equalsIgnoreCase(merchantDevName)){
			days = Integer.parseInt(tktNum.substring(ConfigurationVariables.randomNumberLenInTktRMS+2,ConfigurationVariables.randomNumberLenInTktRMS+5));
		}else if("PMS".equalsIgnoreCase(merchantDevName)){
			days = Integer.parseInt(tktNum.substring(ConfigurationVariables.randomNumberLenInTktPMS+2,ConfigurationVariables.randomNumberLenInTktPMS+5));
		}
		return days;
	}
	
	public static ValidateTicketBean validateTkt(String tktNum, String merchantDevname) throws SLEException {
		logger.debug("***Validating Ticket***");
		int tktLen = 0;
		//int days = 0;
		String ticketNumInDB = null;
		MerchantInfoBean merchantInfoBean = null;
		
		ValidateTicketBean tktBean = new ValidateTicketBean();
		tktBean.setValid(false);
		
		merchantInfoBean =Util.merchantInfoMap.get(merchantDevname);
		tktLen = getMerchantTicketLength(merchantInfoBean.getMerchantDevName());
		if (tktNum != null && (tktNum.length() == tktLen)){
			int merServiceId = getMerchantServiceIdFromTktNum(tktNum,merchantInfoBean.getMerchantDevName());
			int merId = getMerchantIdFromTktNum(tktNum,merchantInfoBean.getMerchantDevName());
			if(merServiceId == merchantInfoBean.getServiceId() && merId ==merchantInfoBean.getMerchantId()){
				ticketNumInDB = getDBTkt(tktNum, merchantInfoBean.getMerchantDevName());
				
				tktBean.setTicketNumInDB(ticketNumInDB);
				tktBean.setValid(true);
				
				/*confirm for ticket expire code*/
				//days = getDaysFromTkt(tktNum, merchantInfoBean.getMerchantDevName());			
			}		
		}else{
			throw new SLEException(SLEErrors.INVALID_TICKET_ERROR_CODE, SLEErrors.INVALID_DATA_ERROR_MESSAGE);
		}
		logger.debug("Ticket is Validated with Infomation (TktNumInDB:"+tktBean.getTicketNumInDB()+"|TktReprintCount:"+tktBean.getReprintCount()+"|TktValid:"+tktBean.isValid());
		return tktBean;
	}	
	
	public String convertJSON(Object object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    }
	
	public static String timeFormat(String time){
		return time.substring(0, time.lastIndexOf("."));
	}
	
	public synchronized static final String formatToTwoDecimal(Object number){
		return number == null ?"0.00":new DecimalFormat("#0.00").format(number);	
	}
	
	public static String getAmountFormatForMobile(Object amount){
        return amount == null ?"0.00":new DecimalFormat("#,##0.00").format(amount);
	}
}