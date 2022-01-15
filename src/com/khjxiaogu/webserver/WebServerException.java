package com.khjxiaogu.webserver;

import java.lang.reflect.InvocationTargetException;

import com.khjxiaogu.webserver.loging.SimpleLogger;

public class WebServerException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String loggers = "";

	public String getLoggers() { return loggers; }

	public void setLogger(SimpleLogger logger) { loggers = logger.getQuoteName() + loggers; }

	public void setSource(String source) { loggers = source + loggers; }

	public WebServerException() { super(); }


	public WebServerException(String message, Throwable cause) {
		super(message);
		while (true) {
			if (cause instanceof InvocationTargetException) {
				cause = cause.getCause();
			} else if (cause instanceof WebServerException) {
				this.setSource(((WebServerException) cause).getLoggers());
			} else
				break;
		}
		this.initCause(cause);
	}

	public WebServerException(String message) { super(message); }

	public WebServerException(Throwable cause) {
		super();
		while (true) {
			if (cause instanceof InvocationTargetException) {
				cause = cause.getCause();
			} else if (cause instanceof WebServerException) {
				this.setSource(((WebServerException) cause).getLoggers());
			} else
				break;
		}
		this.initCause(cause);
	}

	public WebServerException(Throwable cause, SimpleLogger logger) {
		super();
		while (true) {
			if (cause instanceof InvocationTargetException) {
				cause = cause.getCause();
			} else if (cause instanceof WebServerException) {
				this.setSource(((WebServerException) cause).getLoggers());
			} else
				break;
		}
		this.initCause(cause);
		setLogger(logger);
	}
}
