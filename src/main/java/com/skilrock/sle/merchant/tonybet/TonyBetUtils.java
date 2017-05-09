package com.skilrock.sle.merchant.tonybet;

public class TonyBetUtils {

	public enum ServiceMethod {
		account_details,withdraw,rollback,deposit;
	}
	
	public static final String BASE_SERVICE_API = "/api/v1/skilrock/";

	public static final String CALLER_ID = "demo";
	public static final String CALLER_PASSWORD = "test";
	public static final String   CURRENCY="usd";

}
