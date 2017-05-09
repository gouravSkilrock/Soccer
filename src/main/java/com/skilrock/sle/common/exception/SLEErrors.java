package com.skilrock.sle.common.exception;

public class SLEErrors {

	/*
	 * Starting Range of Custom Exceptions is 10000
	 */
	
	public static final int SUCCESS_CODE = 100;
	public static final String SUCCESS_MESSAGE = "SUCCESS";
	
	public static final int INVALID_DATA_ERROR_CODE = 10000;
	public static final String INVALID_DATA_ERROR_MESSAGE = "Invalid Data Submitted !";

	public static final int INVALID_MERCHANT_NAME_ERROR_CODE = 10001;
	public static final String INVALID_MERCHANT_NAME_ERROR_MESSAGE = "Invalid Merchant Name !";

	public static final int NO_GAME_AVAILABLE_ERROR_CODE = 10002;
	public static final String NO_GAME_AVAILABLE_ERROR_MESSAGE = "Games Not Available !";

	public static final int NO_ACTIVE_DRAW_AVAILABLE_ERROR_CODE = 10003;
	public static final String NO_ACTIVE_DRAW_AVAILABLE_ERROR_MESSAGE = "Draw Not Available !";

	public static final String INVALID_TICKET_NUMBER_ERROR_MESSAGE = "Invalid Ticket No";
	public static final int INVALID_TICKET_NUMBER_ERROR_CODE = 10004;

	public static final String SALE_FAILED_ERROR_MESSAGE = "Sale Failed!";
	public static final int SALE_FAILED_ERROR_CODE = 10005;

	public static final int INVALID_TICKET_ERROR_CODE = 10006;
	public static final String INVALID_TICKET_ERROR_MESSAGE = "Invalid Ticket!";

	public static final int CANCELLED_TICKET_ERROR_CODE = 10007;
	public static final String CANCELLED_TICKET_ERROR_MESSAGE = "Ticket is already Cancelled!";

	public static final int RPC_LIMIT_EXCEED_ERROR_CODE = 10008;
	public static final String RPC_LIMIT_EXCEED_ERROR_MESSAGE = "Limit Exceed !!";
	
	public static final int SQL_DATA_ERROR_CODE = 10009;
	public static final String SQL_DATA_ERROR_MESSAGE = "Data Error!";
	
	public static final int MERCHANT_USER_ERROR_CODE = 10010;
	public static final String MERCHANT_USER_ERROR = "User Of Merchant Doesn't Exists.";

	public static final int NO_RESULT_AVL_ERROR_CODE = 10011;
	public static final String NO_RESULT_AVL_ERROR_MESSGE = "No Draw/Result Available!!";
	
	public static final int INVALID_SESSION_ERROR_CODE = 10012;
	public static final String INVALID_SESSION_ERROR_MESSAGE = "Invalid Session or Session Expired !!";
	
	public static final int SESSION_MISMATCH_ERROR_CODE = 10017;
	public static final String SESSION_MISMATCH_ERROR_MESSAGE = "Session Mismatched / expired !!";
	
	public static final int NO_PRIV_ASSIGNED_ERROR_CODE = 10013;
	public static final String NO_PRIV_ASSIGNED_ERROR_MESSAGE = "No Option Available !!";
	
	public static final int MULTIPLE_LOGIN_FOR_SAME_SESSION_ERROR_CODE = 10014;
	public static final String MULTIPLE_LOGIN_FOR_SAME_SESSION_ERROR_MESSAGE = "Multiple users are logged in for same session !!";
	
	public static final int TICKET_CANCELLATION_ERROR_CODE = 10015;
	public static final String TICKET_CANCELLATION_ERROR_MESSAGE = "Can't be Cancelled,Draw Freezed or Cancelled!";
	
	public static final int CANCELLED_TICKET_INITIATE_ERROR_CODE = 10016;
	public static final String CANCELLED_TICKET_INITIATE_ERROR_MESSAGE = "Ticket Can Not be Cancelled!";
	
	public static final int TICKET_LIMIT_REACHED_ERROR_CODE = 10018;
	public static final String TICKET_LIMIT_REACHED_ERROR_MESSAGE = "Ticket Limit Reached!!";
	
	public static final int INVALID_TRANSACTIO_ID_ERROR_CODE = 10019;
	public static final String INVALID_TRANSACTIO_ID_ERROR_MESSAGE = "Invalid Transaction Id";
	
	public static final int TICKET_CLAIMING_NOT_AUTHORIZED_ERROR_CODE = 10020;
	public static final String TICKET_CLAIMING_NOT_AUTHORIZED_ERROR_MESSAGE = "Ticket Can Not Claimed Through This Channel!";
	
	public static final int TICKET_ALREADY_CLAIMED_ERROR_CODE = 10021;
	public static final String TICKET_ALREADY_CLAIMED_ERROR_MESSAGE = "Ticket Already Claimed!";
	
	public static final int TICKET_CANNOT_CLAIMED_ERROR_CODE = 10022;
	public static final String TICKET_CANNOT_CLAIMED_ERROR_MESSAGE = "Ticket Can Not Claimed!";
	
	public static final int DUPLICATE_TRANSACTION_ID_ERROR_CODE = 10023;
	public static final String DUPLICATE_TRANSACTION_ID_ERROR_MESSAGE = "Duplicate Transaction ID!";
	
	public static final int TICKET_CANCELLED_ERROR_CODE = 10024;
	public static final String TICKET_CANCELLED_ERROR_MESSAGE = "Ticket is Cancelled!";
	
	public static final int TICKET_REPRINT_FAILED_ERROR_CODE = 10025;
	public static final String  TICKET_REPRINT_FAILED_ERROR_MESSAGE = "Reprint failed !";
	
	public static final int FAILED_TRANSACTION_ERROR_CODE = 10026;
	public static final String FAILED_TRANSACTION_ERROR_MESSAGE = "Transaction Failed!";
	
	public static final int EVENT_MAPPING_FAILED_ERROR_CODE = 10027;
	public static final String EVENT_MAPPING_FAILED_ERROR_MESSAGE = "Event Already Mapped For The Draw !!";
	
	public static final int EVENT_ALREADY_EXIST_ERROR_CODE = 10028;
	public static final String EVENT_ALREADY_EXIST_ERROR_MESSAGE = "are already mapped for this game type !!";
	
	public static final int DRAW_FREEZED_ERROR_CODE = 10029;
	public static final String DRAW_FREEZED_ERROR_MESSAGE = "Draw Freezed !!";
	
	public static final int ARCH_DATE_LIMIT_ERROR_CODE = 10030;
	public static final String ARCH_DATE_LIMIT_ERROR_MESSAGE = "Please select date after archived date ";
	
	public static final int DRAW_EXPIRED_ERROR_CODE = 10031;
	public static final String DRAW_EXPIRED_ERROR_MESSAGE = "Draw Expired !!";

	public static final int SLE_TRANSACTION_MAP_EMPTY_ERROR_CODE = 10032;
	public static final String SLE_TRANSACTION_MAP_EMPTY_ERROR_MESSAGE = "SLE Transaction Map is Empty";
	
	public static final int USERINFOBEAN_NULL_ERROR_CODE = 10033;
	public static final String USERINFOBEAN_NULL_ERROR_MESSAGE = "UserInfoBean is NULL";
	/*
	 * Starting Range of Java Build-In Libraries Related Exceptions is 20000
	 */
	public static final int GENERAL_EXCEPTION_ERROR_CODE = 20000;
	public static final String GENERAL_EXCEPTION_ERROR_MESSAGE = "Internal System Error";

	public static final int SQL_EXCEPTION_ERROR_CODE = 20001;
	public static final String SQL_EXCEPTION_ERROR_MESSAGE = "SQL Exception !";
	
	public static final int CONNECTION_ERROR_CODE = 20002;
	public static final String CONNECTION_ERROR_MESSAGE = "Error in Connection !";

	/*
	 * Starting Range of Merchant Related Exceptions is 30000
	 */

	public static final int MERCHANT_NOT_AVAILABLE_ERROR_CODE = 30000;
	public static final String MERCHANT_NOT_AVAILABLE_ERROR_MESSAGE = "Merchant Not Reachable Error!";

	public static final int USER_AUTHORIZATION_FAILED_ERROR_CODE = 30001;
	public static final String USER_AUTHORIZATION_FAILED_ERROR_MESSAGE = "User not Authorized for Result Submission!";

	public static final int USER_ALREADY_SUBMITTED_RESULT_ERROR_CODE = 30002;
	public static final String USER_ALREADY_SUBMITTED_RESULT_ERROR_MESSAGE = "User Already Submitted Result!";
	
	public static final int INVALID_CREDENTIALS_ERROR_CODE = 30004;
	public static final String INVALID_CREDENTIALS_ERROR_MESSAGE = "CallerId or CallerPassword is incorrect!";
	
	public static final String NO_REQUEST_DATA_PROVIDED_ERROR_MESSAGE = "No request data provided";
	public static final int NO_REQUEST_DATA_PROVIDED_ERROR_CODE = 112;
	

	public static final int PROPER_REQUEST_DATA_INVALID_ERROR_CODE = 2005;
	public static final String PROPER_REQUEST_DATA_INVALID_ERROR_MESSAGE = "Proper request data not provided";
	
	public static final int NO_RECORD_FOUND_ERROR_CODE = 2006;
	public static final String NO_RECORD_FOUND_ERROR_MESSAGE = "No Record Found";
	
	public static final String Reprint_LIMIT_EXCEPTION_ERROR_MESSAGE = "Reprint Limit Reached!!";
	public static final String CANCEL_LIMIT_EXCEPTION_ERROR_MESSAGE = "Ticket Can't Cancelled, Due to cancel limit reached of retailer !!";
	public static final String CANCEL_LIMIT_TERMINAL_EXCEPTION_ERROR_MESSAGE="Cancel Limit Reached!!";
	public static final int RG_LIMIT_EXCEPTION_ERROR_CODE = 2016;
	
	
	public static final int INVALID_SESSION_MOBILE_ERROR_CODE = 118;
	public static final String INVALID_SESSION_MOBILE_ERROR_MESSAGE = "Time Out, Login Again!!";
	
	public static final int MATCH_NOT_AVAILABLE_ERROR_CODE = 10030;
	public static final String MATCH_NOT_AVAILABLE_ERROR_MESSAGE = "Match List not Available !!";
	
	public static final int MATCH_LIST_PRINT_LIMIT_EXCEEDED_ERROR_CODE = 10031;
	public static final String MATCH_LIST_PRINT_LIMIT_EXCEEDED_ERROR_MESSAGE = "Match List Print Limit Reached !!";
	
	public static final int INVALID_MERCHANT_FOR_PWT_ERROR_CODE = 30003;
	public static final String INVALID_MERCHANT_FOR_PWT_ERROR_MESSAGE = "Merchant is not authorized to claim this ticket!";
	
	public static final int MAXIMUM_BET_AMOUNT_MULTIPLE_LIMIT_EXCEED_ERROR_CODE = 10034;
	public static final String MAXIMUM_BET_AMOUNT_MULTIPLE_LIMIT_EXCEED_ERROR_MESSAGE = "Bet amoumt multiple limit exceed !! ";
	
	public static final int MAXIMUM_TICKET_PRICE_LIMIT_EXCEED_ERROR_CODE = 10035;
	public static final String MAXIMUM_TICKET_PRICE_LIMIT_EXCEED_ERROR_MESSAGE = "Ticket price limit exceed !!";
	
	public static final int INVALID_VERIFICATION_CODE_ERROR_CODE = 10032;
	public static final String INVALID_VERIFICATION_CODE_ERROR_MESSAGE = "Invalid Verification Code ";

	public static final int INVALID_DATE_RANGE_ERROR_CODE = 10033;
	public static final String INVALID_DATE_RANGE_ERROR_MESSAGE = "From date must be less than to date ";
	
	public static final String INVALID_RETAILERID_ERROR_MESSAGE = "Invalid Retailer Id provided!!";
	public static final int INVALID_RETAILERID_ERROR_CODE = 10034;
	
	public static final String SECOND_USER_RESULT_NOT_MATCHED_ERROR_MESSAGE = "Second User Result Not Matched, BO has to submit the result";
	public static final int SECOND_USER_RESULT_NOT_MATCHED_ERROR_CODE = 10037;
	
	public static final String BOTH_USER_RESULT_NOT_MATCHED_ERROR_MESSAGE = "Both Users Result Not Matched, BO has to submit the result";
	public static final int BOTH_USER_RESULT_NOT_MATCHED_ERROR_CODE = 10038;
	
	public static final String GAME_ID_NOT_PROVIDED_MESSAGE = "Game Id not provided";
	public static final String GAME_TYPE_ID_NOT_PROVIDED_MESSAGE = "Game Type Id not provided";
	public static final String DRAW_ID_NOT_PROVIDED_MESSAGE = "Draw Id not provided";
	
	public static final int INVALID_NUMBER_OF_EVENTS_ENTERED_ERROR_CODE = 10039;
	public static final String INVALID_NUMBER_OF_EVENTS_ENTERED_ERROR_MESSAGE = "Invalid number of events selected.";
	
	public static final int SAME_EVENT_SELECTED_ERROR_CODE = 10040;
	public static final String SAME_EVENT_SELECTED_ERROR_MESSAGE = "Same type of Events selected.";
}