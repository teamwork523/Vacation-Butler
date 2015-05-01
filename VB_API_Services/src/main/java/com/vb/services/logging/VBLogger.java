package com.vb.services.logging;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;

/**
 * A uniform logging class for all the VB logging
 * 
 * @author Haokun Luo
 */
public class VBLogger {
	
	// TODO: Add MDC later on to collect client context information
	
	private Logger logger;

	///////////////////////////
	// factory methods
	///////////////////////////
	static public VBLogger getLogger(String name) {
		return new VBLogger(name);
	}
	
	static public VBLogger getLogger(Class<?> clazz) {
		return new VBLogger(clazz);
	}
	
	///////////////////////////
	// private construction
	///////////////////////////
	private VBLogger(String name) {
		logger = (Logger)LoggerFactory.getLogger(name);
	}

	private VBLogger(Class<?> clazz) {
		logger = (Logger)LoggerFactory.getLogger(clazz);
	}
	
	///////////////////////////
	// Event log methods
	///////////////////////////
	public void debug(String msg) {
		logger.debug(msg);
	}
	
	public void debug(String msg, Throwable throwable) {
		logger.debug(msg, throwable);
	}
	
	public void debug(String format, Object... objects) {
		logger.debug(format, objects);
	}
	
	public void error(String msg) {
		logger.error(msg);
	}
	
	public void error(String msg, Throwable throwable) {
		logger.error(msg, throwable);
	}
	
	public void error(String format, Object... objects) {
		logger.error(format, objects);
	}
	
	public void info(String msg) {
		logger.info(msg);
	}
	
	public void info(String msg, Throwable throwable) {
		logger.info(msg, throwable);
	}
	
	public void info(String format, Object... objects) {
		logger.info(format, objects);
	}
	
	public void trace(String msg) {
		logger.trace(msg);
	}
	
	public void trace(String msg, Throwable throwable) {
		logger.trace(msg, throwable);
	}
	
	public void trace(String format, Object... objects) {
		logger.trace(format, objects);
	}
	
	public void warn(String msg) {
		logger.warn(msg);
	}
	
	public void warn(String msg, Throwable throwable) {
		logger.warn(msg, throwable);
	}
	
	public void warn(String format, Object... objects) {
		logger.warn(format, objects);
	}
	
	///////////////////////////
	// Utility methods
	///////////////////////////
	public String getName() {
		return logger.getName();
	}
	
	public boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}
	
	public boolean isErrorEnabled() {
		return logger.isErrorEnabled();
	}
	
	public boolean isInfoEnabled() {
		return logger.isInfoEnabled();
	}
	
	public boolean isTraceEnabled() {
		return logger.isTraceEnabled();
	}
	
	public boolean isWarnEnabled() {
		return logger.isWarnEnabled();
	}
}
