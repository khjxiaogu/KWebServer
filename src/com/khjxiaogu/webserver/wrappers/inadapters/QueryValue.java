package com.khjxiaogu.webserver.wrappers.inadapters;

import com.khjxiaogu.webserver.web.lowlayer.Request;
import com.khjxiaogu.webserver.wrappers.InStringAdapter;

public class QueryValue implements InStringAdapter {
	String key;
	public QueryValue(String key) {this.key=key;}

	@Override
	public Object handle(Request req) throws Exception { return req.query.get(key); }

}
