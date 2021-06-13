package com.khjxiaogu.webserver.wrappers.inadapters;

import com.khjxiaogu.webserver.web.lowlayer.Request;
import com.khjxiaogu.webserver.wrappers.StaticInStringAdapter;

public class HeaderValue extends StaticInStringAdapter {

	public HeaderValue(String key) { super(key); }


	@Override
	public Object handle(Request req) throws Exception { return req.getHeaders().get(key); }

}
