package com.khjxiaogu.webserver.wrappers;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.khjxiaogu.webserver.web.lowlayer.Request;
import com.khjxiaogu.webserver.web.lowlayer.Response;

public class FilterManager<T> {
	static class FilterWrapper implements HttpFilter{
		String name;
		HttpFilter intern;
		public FilterWrapper(String name, HttpFilter intern) {
			this.name = name;
			this.intern = intern;
		}
		@Override
		public boolean handle(Request req, Response res, FilterChain filters) throws Exception {
			if(intern==null)return true;
			return intern.handle(req, res, filters);
		}
	}
	static Map<String, HttpFilter> registry = new ConcurrentHashMap<>();
	static Map<String, FilterWrapper> created = new ConcurrentHashMap<>();
	public static void registerFilter(String name, HttpFilter httpFilter) {
		registry.put(name, httpFilter);
		FilterWrapper fw = created.get(name);
		if (fw != null) { fw.intern = httpFilter; }
	}

	public static void unregisterFilter(String name) {
		registry.remove(name);
		FilterWrapper fw = created.get(name);
		if (fw != null) { fw.intern = null; }
	}

	public static HttpFilter getFilter(String name) {
		FilterWrapper fw = created.get(name);
		if (fw == null) {
			fw = new FilterWrapper(name, registry.get(name));
			created.put(name, fw);
		}
		return fw;
	}
}
