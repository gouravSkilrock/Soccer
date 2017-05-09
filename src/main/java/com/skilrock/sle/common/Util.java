package com.skilrock.sle.common;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.quartz.Scheduler;

import com.skilrock.sle.common.exception.SLEException;
import com.skilrock.sle.gamePlayMgmt.javaBeans.SportsLotteryGameDrawDataBean;
import com.skilrock.sle.gamePlayMgmt.javaBeans.SportsLotteryGameEventDataBean;
import com.skilrock.sle.gamePlayMgmt.javaBeans.SportsLotteryGamePlayBean;
import com.skilrock.sle.merchant.common.javaBeans.MerchantInfoBean;

public class Util {

	public static String serverURL;
	public static String serverPMSURL = null;
	public static String serverLMSURL = null;
	public static Scheduler scheduler;

	public static Map<String, String> sysPropertiesMap = null;
	public static Map<String, MerchantInfoBean> merchantInfoMap = null;
	public static Map<Integer, Map<Integer, Double>> jackpotMap = null;

	public static String getPropertyValue(String propertyDevName) {
		return sysPropertiesMap.get(propertyDevName);
	}

	public static String getCurrentTimeString() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar
				.getInstance().getTime());
	}

	public static Timestamp getCurrentTimeStamp() {
		return new Timestamp(Calendar.getInstance().getTimeInMillis());
	}

	public static String getDateTimeFormat(Timestamp dateTime) {
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sd.format(dateTime);
	}

	public static String getCurrentAmountFormatForMobile(double amount) {
		DecimalFormat df = new DecimalFormat("###0.00");
		// System.out.println(df.format(amount));
		return df.format(amount);
	}

	public static void main(String[] args) {
		SimpleDateFormat ft = new SimpleDateFormat("EEE,MMM-d");
		System.out.println(ft.format(Calendar.getInstance().getTime()));
	}

	public static double fmtToTwoDecimal(double number) {
		return Math.round((number * 100)) / 100.0;
	}
	
	public static MerchantInfoBean fetchMerchantInfoBean(int merchantId)
	{
		MerchantInfoBean merchantInfoBean = null;
		
		for(Map.Entry<String, MerchantInfoBean> entrySet: Util.merchantInfoMap.entrySet()) {
			if(merchantId == entrySet.getValue().getMerchantId()) {
				merchantInfoBean = entrySet.getValue();
				break;
			}
		}
		return merchantInfoBean;
	}

	public static long id = 0;

	public static synchronized long getAuditTrailId() {

		return id++;
	}
	
	public static boolean checkDuplicateValue(String[] array) {
		boolean isDuplicate = false;
		int arrayLength = 0;

		Set<String> s = new HashSet<String>();
		
		arrayLength = array.length;
        for (int i = 0; i < arrayLength; i++) {
                if (!s.add(array[i])) {
                	isDuplicate = true;
            }
        }
		return isDuplicate;
	}

	/**
	 * @param (dd-MM-yyyy HH:mm:ss)
	 */
	public static String convertDateTimeToResponseFormat(String dateTimeString){
		try{
			java.util.Date date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(dateTimeString);
			return  new SimpleDateFormat((String)getPropertyValue("DATE_FORMAT_INTEGRATION")).format(date);
		}catch(Exception e){
			e.printStackTrace();
		}
		return "";
	}
	

	/**
	 * @param  (yyyy-MM-dd HH:mm:ss)
	 */
	public static String convertDateTimeToResponseFormat2(String dateTimeString){
		try{
			java.util.Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateTimeString);
			return  new SimpleDateFormat((String)getPropertyValue("DATE_FORMAT_INTEGRATION")).format(date);
		}catch(Exception e){
			e.printStackTrace();
		}
		return "";
	}
	
	public static Date StringToDateConversion(String dateTimeString){
		Date date=null;
		try{
			date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateTimeString);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return  date;
	}
	
	public static Timestamp DateToTimeStampConversion(Date date){
		Timestamp ts=null;
		try{
			ts=new Timestamp(date.getTime());
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return  ts;
	}
	
	public static String prepareSMSData(SportsLotteryGamePlayBean gamePlayBeanResponse, String merchantDevname, String balance, long transId)
            throws SLEException {
		Map<Integer, String> drawDataMap = new LinkedHashMap<Integer, String>();
        StringBuilder smsData = new StringBuilder();
        String displayType = Util.getPropertyValue("SPORTS_LOTTERY_TICKET_TYPE");
        smsData.append("Ticket No : ").append(gamePlayBeanResponse.getTicketNumber()).append(gamePlayBeanResponse.getReprintCount()).append("\n")
        		.append("Ticket Cost : ").append(gamePlayBeanResponse.getTotalPurchaseAmt()).append("\n");
        try {
            if (displayType.equals("DRAW_WISE")) {
                for (int i = 0; i < gamePlayBeanResponse.getNoOfBoard(); i++) {
                    SportsLotteryGameDrawDataBean gameDrawDataBean = gamePlayBeanResponse.getGameDrawDataBeanArray()[i];
                    
                    if (drawDataMap.containsKey(gameDrawDataBean.getDrawId())) {
                        for (int j = 0; j < gameDrawDataBean.getGameEventDataBeanArray().length; j++) {
                            SportsLotteryGameEventDataBean eventDataBean = gameDrawDataBean.getGameEventDataBeanArray()[j];
                            smsData.append(eventDataBean.getEventDescription()).append(":");
                            for (int k = 0; k < eventDataBean.getSelectedOption().length; k++) {
                                smsData.append(URLEncoder.encode(eventDataBean.getSelectedOption()[k], "UTF-8")).append(",");
                            }
                            smsData.deleteCharAt(smsData.length()-1);
                            smsData.append("\n");
                        }
                    } else {
                        for (int j = 0; j < gameDrawDataBean.getGameEventDataBeanArray().length; j++) {
                        	smsData = new StringBuilder();
                            SportsLotteryGameEventDataBean eventDataBean = gameDrawDataBean.getGameEventDataBeanArray()[j];
                            smsData.append(eventDataBean.getEventDescription()).append(":");
                            for (int k = 0; k < eventDataBean.getSelectedOption().length; k++) {
                            	smsData.append(URLEncoder.encode(eventDataBean.getSelectedOption()[k], "UTF-8")).append(",");
                            }
                            smsData.deleteCharAt(smsData.length()-1);
                            smsData.append("\n");
                        }
                    }
                }
            } else {
                for (int i = 0; i < gamePlayBeanResponse.getNoOfBoard(); i++) {
                    SportsLotteryGameDrawDataBean gameDrawDataBean = gamePlayBeanResponse.getGameDrawDataBeanArray()[i];
                    smsData.append("Draw Time : " + gameDrawDataBean.getDrawDateTime()).append("\n");
                    for (int j = 0; j < gameDrawDataBean.getGameEventDataBeanArray().length; j++) {
                        SportsLotteryGameEventDataBean eventDataBean = gameDrawDataBean.getGameEventDataBeanArray()[j];
                        smsData.append(eventDataBean.getEventDescription()).append(":");
                        for (int k = 0; k < eventDataBean.getSelectedOption().length; k++) {
                        	smsData.append(URLEncoder.encode(eventDataBean.getSelectedOption()[k], "UTF-8")).append(",");
                        }
                        smsData.deleteCharAt(smsData.length()-1);
                        smsData.append("\n");
                    }
                }
            }
        }catch (Exception e) {
            System.out.println("Some Error In SMS : " + e.getMessage());
        }
        return smsData.toString() ;
    }
	
	public static void sendSMS(String smsData){
		if(!"".equalsIgnoreCase(Util.getPropertyValue("MOBILE_NO_WLS"))){
			String [] numbers = Util.getPropertyValue("MOBILE_NO_WLS").split(",");
			for(String num : numbers){
				CustomSendSMS smsSend = new CustomSendSMS(smsData,num);
				smsSend.setDaemon(true);
				smsSend.start();
				}
		}
		
	}
	public static String customSendSMS(String phoneNumber, String msg){

		try
		{ 
			URL url = new URL("http://www.unicel.in/SendSMS/sendmsg.php");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			OutputStreamWriter wr = new OutputStreamWriter(conn
					.getOutputStream());
			String urlStr ="uname=Serntytr&pass=Stpl2011&send=1234&dest="+phoneNumber+"&msg="+msg+"&concat=1"; 
			System.out.println("Arguments: "+urlStr);
			wr.write(urlStr);
			wr.flush();
			InputStream iStream =conn.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(iStream ));
			StringBuilder sb =new StringBuilder();
			String line =null;
					while ((line = reader.readLine()) != null) {
							sb.append(line);
							}
					
			  String deliveryMsg = sb.toString();
			  return  deliveryMsg;
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	
	}
	
	
}
