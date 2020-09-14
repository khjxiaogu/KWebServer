package com.khjxiaogu.webserver.wrappers.inadapters;

import com.khjxiaogu.webserver.web.lowlayer.Request;
import com.khjxiaogu.webserver.wrappers.InAdapter;

public class QueryIn implements InAdapter {

	public QueryIn() {}

	@Override
	public Object handle(Request req) throws Exception { return req.getQuery(); }

}
