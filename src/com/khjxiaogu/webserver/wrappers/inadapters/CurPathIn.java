package com.khjxiaogu.webserver.wrappers.inadapters;

import com.khjxiaogu.webserver.web.lowlayer.Request;
import com.khjxiaogu.webserver.wrappers.InAdapter;

public class CurPathIn implements InAdapter {

	public CurPathIn() {}

	@Override
	public Object handle(Request req) throws Exception { return req.getCurrentPath(); }

}
