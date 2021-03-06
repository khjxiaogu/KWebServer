package com.khjxiaogu.webserver.wrappers;

import com.khjxiaogu.webserver.web.ServiceClass;
import com.khjxiaogu.webserver.web.lowlayer.Request;

/**
 * Interface InStringAdapter. 必须有一个字符串类型作为参数的构造器
 *
 * @author khjxiaogu file: InStringAdapter.java time: 2020年9月7日
 */
public abstract class StaticInStringAdapter extends InStringAdapter {

	public StaticInStringAdapter(String key) { super(key); }

	@Override
	public Object handle(Request req, ServiceClass context) throws Exception { return handle(req); }

	public abstract Object handle(Request req) throws Exception;
}
