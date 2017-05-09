package com.skilrock.sle.common;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeFormat {

	public static String reportDataDateFormatWeb(Timestamp dateTime){
		SimpleDateFormat sd=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return sd.format(dateTime);
	}
	
	public static String reportHeadingDateFormatWeb(Timestamp dateTime){
		SimpleDateFormat sd=new SimpleDateFormat("dd/MM/yyyy");
		return sd.format(dateTime);
	}
	
	public  String getCurrentTime(){
		SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy");
		String currTime=sdf.format(new Date());
		return currTime;
	}
	
	public static String getCurrentTimeString(){
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
	}
	
	public static Timestamp getCurrentTimeStamp(){
		return new Timestamp(Calendar.getInstance().getTimeInMillis());
	}
	
	public static String getDateTimeFormat(Timestamp dateTime){
		SimpleDateFormat sd=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return sd.format(dateTime);
	}

	public static String getDateTimeFormatForMobile(Timestamp dateTime){
		SimpleDateFormat sd=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return sd.format(dateTime);
	}
	
}
