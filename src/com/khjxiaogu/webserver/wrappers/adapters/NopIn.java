package com.khjxiaogu.webserver.wrappers.adapters;

import com.khjxiaogu.webserver.web.lowlayer.Request;
import com.khjxiaogu.webserver.wrappers.InAdapter;

public class NopIn implements InAdapter {

	@Override
	public Object handle(Request req) { return req; }

}
