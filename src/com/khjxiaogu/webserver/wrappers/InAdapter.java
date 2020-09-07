package com.khjxiaogu.webserver.wrappers;

import com.khjxiaogu.webserver.web.lowlayer.Request;

public interface InAdapter {
	public Object handle(Request req) throws Exception;
}
