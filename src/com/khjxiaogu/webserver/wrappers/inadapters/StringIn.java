package com.khjxiaogu.webserver.wrappers.inadapters;

import java.nio.charset.StandardCharsets;

import com.khjxiaogu.webserver.Utils;
import com.khjxiaogu.webserver.web.lowlayer.Request;
import com.khjxiaogu.webserver.wrappers.InAdapter;

public class StringIn implements InAdapter {

	public StringIn() {}

	@Override
	public Object handle(Request req) throws Exception {
		return new String(Utils.readAll(req.body), StandardCharsets.UTF_8);
	}

}
