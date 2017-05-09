package com.skilrock.sle.common.exception;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SLEErrorProperty {

	private static Properties prop = null;
	static {
		prop = new Properties();
		InputStream input = null;

		try {
			input = SLEErrorProperty.class.getClassLoader().getResourceAsStream("SLE/error_code.properties");
			//input = SLEErrorProperty.class.getClassLoader().getResourceAsStream("com/skilrock/sle/common/exception/error_code.properties"); Before Externalization
			// input = new FileInputStream("resources/error_code.properties");

			// load a properties file
			prop.load(input);

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public static String getPropertyValue(String propertyKey) {
		return prop.getProperty(propertyKey);
	}

	public static String getPropertyValue(Integer propertyKey) {
		return prop.getProperty(String.valueOf(propertyKey));
	}

	public static void main(String[] args) {
		// LMSErrorProperty errorProperty=new LMSErrorProperty();
		System.out.println(getPropertyValue("10001"));
	}
}
