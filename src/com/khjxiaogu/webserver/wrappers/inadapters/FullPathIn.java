package com.khjxiaogu.webserver.wrappers.inadapters;

import com.khjxiaogu.webserver.web.lowlayer.Request;
import com.khjxiaogu.webserver.wrappers.StaticInAdapter;

public class FullPathIn extends StaticInAdapter {

	@Override
	public Object handle(Request req) throws Exception { return req.getFullpath(); }

}
