package com.khjxiaogu.webserver.wrappers.inadapters;

import com.khjxiaogu.webserver.web.lowlayer.Request;
import com.khjxiaogu.webserver.wrappers.StaticInAdapter;

public class FullQueryIn extends StaticInAdapter {

	@Override
	public Object handle(Request req) throws Exception {
		return req.getRawQueryString();
	}

}
