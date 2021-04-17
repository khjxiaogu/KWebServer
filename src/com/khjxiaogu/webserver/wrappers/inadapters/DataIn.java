package com.khjxiaogu.webserver.wrappers.inadapters;

import com.khjxiaogu.webserver.Utils;
import com.khjxiaogu.webserver.web.lowlayer.Request;
import com.khjxiaogu.webserver.wrappers.InAdapter;

public class DataIn implements InAdapter {

	@Override
	public Object handle(Request req) throws Exception { return Utils.readAll(req.getBody()); }

}
