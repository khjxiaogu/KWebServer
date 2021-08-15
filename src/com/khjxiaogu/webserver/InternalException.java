package com.khjxiaogu.webserver;

import com.khjxiaogu.webserver.loging.SystemLogger;

public class InternalException extends WebServerException {

	public InternalException() { super(); }

	public InternalException(String message, Throwable cause) { super(message, cause); }

	public InternalException(String message) { super(message); }

	public InternalException(Throwable cause) { super(cause); }

	public InternalException(Throwable cause, SystemLogger logger) { super(cause, logger); }
}
