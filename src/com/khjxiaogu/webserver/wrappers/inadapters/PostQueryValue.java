package com.khjxiaogu.webserver.wrappers.inadapters;

import com.khjxiaogu.webserver.web.lowlayer.Request;
import com.khjxiaogu.webserver.wrappers.StaticInStringAdapter;

public class PostQueryValue extends StaticInStringAdapter {

	public PostQueryValue(String key) { super(key); }

	@Override
	public Object handle(Request req) throws Exception { return req.getPost().get(key); }

}
