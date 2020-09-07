package com.khjxiaogu.webserver.wrappers;

import com.khjxiaogu.webserver.web.lowlayer.Response;

public interface OutAdapter {
	public void handle(HResult result, Response res);
}
