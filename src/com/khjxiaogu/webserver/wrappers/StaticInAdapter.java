package com.khjxiaogu.webserver.wrappers;

import com.khjxiaogu.webserver.web.ServiceClass;
import com.khjxiaogu.webserver.web.lowlayer.Request;

public abstract class StaticInAdapter implements InAdapter {

	@Override
	public Object handle(Request req, ServiceClass context) throws Exception { return handle(req); }

	public abstract Object handle(Request req) throws Exception;
}
