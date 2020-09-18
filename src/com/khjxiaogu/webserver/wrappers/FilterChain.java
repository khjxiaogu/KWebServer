package com.khjxiaogu.webserver.wrappers;

import com.khjxiaogu.webserver.web.lowlayer.Request;
import com.khjxiaogu.webserver.web.lowlayer.Response;

public interface FilterChain{
	public void next(Request req,Response res);
	public void addFilter(Filter f);
	public void ignoreFilter(Filter type);
}
