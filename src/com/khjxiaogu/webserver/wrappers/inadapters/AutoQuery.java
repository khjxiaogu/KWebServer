package com.khjxiaogu.webserver.wrappers.inadapters;

import com.khjxiaogu.webserver.web.lowlayer.Request;
import com.khjxiaogu.webserver.wrappers.StaticInStringAdapter;

public class AutoQuery extends StaticInStringAdapter {

	public AutoQuery(String key) {
		super(key);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object handle(Request req) throws Exception {
		// TODO Auto-generated method stub
		if("POST".equals(req.getMethod())) {
			return req.getPost().get(key);
		}
		return req.getQuery().get(key);
	}

}
