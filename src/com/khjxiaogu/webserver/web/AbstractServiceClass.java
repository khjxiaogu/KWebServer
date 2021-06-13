package com.khjxiaogu.webserver.web;

import com.khjxiaogu.webserver.loging.SystemLogger;

public abstract class AbstractServiceClass implements ServiceClass {
	SystemLogger logger;

	@Override
	public SystemLogger getLogger() {
		if (logger == null) { logger = new SystemLogger(getName()); }
		return logger;
	}

}
