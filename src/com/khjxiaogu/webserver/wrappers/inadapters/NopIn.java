package com.khjxiaogu.webserver.wrappers.inadapters;

import com.khjxiaogu.webserver.web.lowlayer.Request;
import com.khjxiaogu.webserver.wrappers.StaticInStringAdapter;

public class NopIn extends StaticInStringAdapter {
	public NopIn() { super(null); }

	public NopIn(String str) { super(str); }

	@Override
	public Object handle(Request req) { return req; }

}
