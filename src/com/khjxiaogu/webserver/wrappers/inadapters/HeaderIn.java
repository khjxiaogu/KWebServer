package com.khjxiaogu.webserver.wrappers.inadapters;

import com.khjxiaogu.webserver.web.ServiceClass;
import com.khjxiaogu.webserver.web.lowlayer.Request;
import com.khjxiaogu.webserver.wrappers.InAdapter;
import com.khjxiaogu.webserver.wrappers.StaticInAdapter;

public class HeaderIn extends StaticInAdapter {

	@Override
	public Object handle(Request req) throws Exception { return req.getHeaders(); }

}
