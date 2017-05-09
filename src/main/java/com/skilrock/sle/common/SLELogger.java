package com.skilrock.sle.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SLELogger {
	private Log logger;

	private SLELogger() {
	}

	private SLELogger(String name) {
		logger = LogFactory.getLog(name);
	}

	public static SLELogger getLogger(String name) {
		return new SLELogger(name);
	}

	public void trace(String message) {
		logger.trace(message);
	}

	public void debug(String message) {
		logger.debug(message);
	}

	public void info(String message) {
		logger.info(message);
	}

	public void warn(String message) {
		logger.warn(message);
	}

	public void fatal(String message) {
		logger.fatal(message);
	}

	public void error(String message) {
		logger.error(message);
	}
	public void error(String message,Exception e) {
		logger.error(message,e);
	}
}