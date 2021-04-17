package com.khjxiaogu.webserver.wrappers;

import com.khjxiaogu.webserver.web.lowlayer.Request;
import com.khjxiaogu.webserver.web.lowlayer.Response;

public interface FilterChain {
	public boolean next(Request req, Response res) throws Exception;


	public void ignore(HttpFilter type);

	void addLast(HttpFilter f);

	void add(HttpFilter f);
}
