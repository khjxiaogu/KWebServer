package com.khjxiaogu.webserver.wrappers.inadapters;

import com.khjxiaogu.webserver.web.lowlayer.Request;
import com.khjxiaogu.webserver.wrappers.InAdapter;
import com.khjxiaogu.webserver.wrappers.InStringAdapter;

public class NopIn implements InStringAdapter {
	public NopIn() {}

	public NopIn(String str) {}

	@Override
	public Object handle(Request req) { return req; }

}
