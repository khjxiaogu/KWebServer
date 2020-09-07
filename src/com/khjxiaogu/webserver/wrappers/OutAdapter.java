package com.khjxiaogu.webserver.wrappers;

import com.khjxiaogu.webserver.web.lowlayer.Response;

public interface OutAdapter {
	public void handle(ResultDTO result, Response res);
}
