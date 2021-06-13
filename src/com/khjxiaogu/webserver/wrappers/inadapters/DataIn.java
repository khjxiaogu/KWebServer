package com.khjxiaogu.webserver.wrappers.inadapters;

import com.khjxiaogu.webserver.Utils;
import com.khjxiaogu.webserver.web.lowlayer.Request;
import com.khjxiaogu.webserver.wrappers.StaticInAdapter;

public class DataIn extends StaticInAdapter {

	@Override
	public Object handle(Request req) throws Exception { return Utils.readAll(req.getBody()); }

}
