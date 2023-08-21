/**
 * KWebserver
 * Copyright (C) 2021  khjxiaogu
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
		this.initCause(cause);
	}

	public WebServerException(String message) { super(message); }

	public WebServerException(Throwable cause) {
		super();
		this.initCause(cause);
	}

	public WebServerException(Throwable cause,String message, SimpleLogger logger) {
		super(message);
		this.initCause(cause);
		setLogger(logger);
	}
	public WebServerException(Throwable cause, SimpleLogger logger) {
		super();
		this.initCause(cause);
		setLogger(logger);
	}

	@Override
	public synchronized Throwable initCause(Throwable cause) {
		while (true) {
			if (cause instanceof InvocationTargetException) {
				cause = cause.getCause();
			} else if (cause instanceof WebServerException) {
				this.setSource(((WebServerException) cause).getLoggers());
			} else
				break;
		}
		return super.initCause(cause);
	}

}
