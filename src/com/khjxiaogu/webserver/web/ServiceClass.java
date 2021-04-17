package com.khjxiaogu.webserver.web;

import com.khjxiaogu.webserver.loging.SystemLogger;

public interface ServiceClass {
	SystemLogger getLogger();

	public String getName();
}
