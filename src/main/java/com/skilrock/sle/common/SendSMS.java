package com.skilrock.sle.common;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SendSMS extends Thread {
	private static final Logger logger = LoggerFactory.getLogger(SendSMS.class.getName());

	private String message ;
	private String phoneNumber;

	public SendSMS(String plrPhoneNumber, String msg) {
		this.message = msg;
		this.phoneNumber = plrPhoneNumber;
	}

	public static String sendSMSToUsers(String phnNo, String msg, String serviceProvider, String mobileCountryCode){
		String smsURL = null;
		String urlStr = null;
		InputStream iStream = null;
		//Properties smsProperties = null;
		try {
			//smsProperties = PropertyLoader.loadProperties("config/send_sms.properties");
			if("maccesssmspush".equalsIgnoreCase(serviceProvider)) {
				smsURL = "http://push1.maccesssmspush.com/servlet/com.aclwireless.pushconnectivity.listeners.TextListener?";
				urlStr = "userId=wgrlint&pass=wgrlint&appid=wgrlint&subappid=wgrlint&msgtype=1&contenttype=1&selfid=true&to=234"+phnNo+"&from=56767&dlrreq=true&text="+msg+"&alert=1&intflag=true";
			} else if("infobip".equalsIgnoreCase(serviceProvider)) {
				smsURL = "http://api2.infobip.com/api/sendsms/plain?";
				urlStr = "user=AfricaLotto&password=tvPswgWZ&sender=Friend&SMSText="+msg+"&GSM=263"+phnNo;
			} else if("alertsindia".equalsIgnoreCase(serviceProvider)) {
				smsURL = "http://transapi.alertsindia.com/Desk2web/sendsms.aspx?";
				urlStr = "UserName=payworld&password=pay1world&MobileNo=91"+phnNo+"&SenderID=ALERTS&CDMAHeader=ALERTS&Message="+msg+"&isFlash=False";
			}else if("smsmobile24".equalsIgnoreCase(serviceProvider)) {							
				smsURL = "http://smsmobile24.com/components/com_spc/smsapi.php?";
				urlStr = "username=winlotlotto&password=resources&sender=WGRL&recipient=234"+phnNo+"&message="+msg;
			} /*else if("cytech".equalsIgnoreCase(serviceProvider)) {		
				phnNo = mobileCountryCode.substring(1)+phnNo;
				String originator = (String) smsProperties.get("cytech_originator");
				smsURL = (String) smsProperties.get("cytech_url");
				urlStr = "mobile_number="+phnNo+"&originator="+originator+"&text="+msg+"&concatenated=true&request_delivery=true";
			} */else {
				return null;
			}

			URL url = new URL(smsURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
		/*	if("cytech".equalsIgnoreCase(serviceProvider)) {				
				String loginPassword = (String) smsProperties.get("cytech_username")+ ":" + (String) smsProperties.get("cytech_password");
				String encoded = new sun.misc.BASE64Encoder().encode (loginPassword.getBytes());
				conn.setRequestProperty ("Authorization", "Basic " + encoded);
			}*/
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(urlStr);
			wr.flush();
			if(conn.getResponseCode() == 200){
				iStream = conn.getInputStream();
			}else{
				iStream = conn.getErrorStream();
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(iStream));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			logger.info("Sending SMS Response : " + sb);
			return  sb.toString();
		} catch(Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public void run() {
		String serviceProvider = null;
		String mobileCountryCode = null;
		String deliveryMsg = null;
		try {
			logger.info("Phone Number-"+phoneNumber+" : Message-"+message+" : Message Length-"+message.length());
			if ("YES".equals(Util.getPropertyValue("IS_SMS_TURN_ON"))) {
				serviceProvider = (String)Util.getPropertyValue("SMS_SERVICE_PROVIDER");
				mobileCountryCode = "+"+(String)Util.getPropertyValue("MOBILE_COUNTRY_CODE");
				deliveryMsg = sendSMSToUsers(phoneNumber,message,serviceProvider,mobileCountryCode);
				logger.debug("Delivery Msg - "+deliveryMsg);
			} else {
				logger.debug("Messaging Service Disabled.");
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}