package com.khjxiaogu.webserver.wrappers;

import com.khjxiaogu.webserver.web.lowlayer.Request;
import com.khjxiaogu.webserver.web.lowlayer.Response;

public interface IOAdapter {
	public void handle(Request req, Response res) throws Exception;
}
