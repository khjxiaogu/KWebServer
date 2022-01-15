package com.khjxiaogu.webserver.web;

import com.khjxiaogu.webserver.loging.SimpleLogger;

public abstract class AbstractServiceClass implements ServiceClass {
	protected SimpleLogger logger;

	@Override
	public SimpleLogger getLogger() {
		if (logger == null) { logger = new SimpleLogger(getName()); }
		return logger;
	}

}
