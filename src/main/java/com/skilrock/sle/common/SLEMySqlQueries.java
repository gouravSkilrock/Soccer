package com.skilrock.sle.common;

public class SLEMySqlQueries {

	public static final String GET_PROPERTY_DETAILS = "SELECT id, property_code, property_dev_name, property_display_name, editable, VALUE, value_type, description FROM st_sl_property_master WHERE STATUS='ACTIVE';";
	
	public static final String GET_COUNTRY_DETAILS = "SELECT country_code, country_name FROM st_sl_country_master WHERE STATUS='ACTIVE' ORDER BY country_name;";
	public static final String GET_STATE_DETAILS = "SELECT country_code,state_code,state_name FROM st_sl_state_master a, st_sl_country_master b WHERE a.STATUS='ACTIVE' and a.country_id = b.country_id ORDER BY country_code, state_name;";
	public static final String GET_CITY_DETAILS = "SELECT city_code,city_name,state_code FROM st_sl_city_master a, st_sl_state_master b WHERE a.STATUS='ACTIVE'  and a.state_id = b.state_id ORDER BY state_code, city_name;";


}
