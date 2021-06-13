package com.khjxiaogu.webserver.wrappers;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.khjxiaogu.webserver.web.lowlayer.Request;
import com.khjxiaogu.webserver.web.lowlayer.Response;

public final class FilterManager {
	static class FilterWrapper implements HttpFilter {
		String name;
		HttpFilter intern;

		public FilterWrapper(String name, HttpFilter intern) {
			this.name = name;
			this.intern = intern;
		}

		@Override
		public boolean handle(Request req, Response res, FilterChain filters) throws Exception {
			if (intern == null)
				return true;
			return intern.handle(req, res, filters);
		}
	}

	static Map<String, HttpFilter> registry = new ConcurrentHashMap<>();
	static Map<String, FilterWrapper> created = new ConcurrentHashMap<>();

	public static void registerFilter(String name, HttpFilter httpFilter) {
		FilterManager.registry.put(name, httpFilter);
		FilterWrapper fw = FilterManager.created.get(name);
		if (fw != null) { fw.intern = httpFilter; }
	}

	public static void unregisterFilter(String name) {
		FilterManager.registry.remove(name);
		FilterWrapper fw = FilterManager.created.get(name);
		if (fw != null) { fw.intern = null; }
	}

	public static HttpFilter getFilter(String name) {
		FilterWrapper fw = FilterManager.created.get(name);
		if (fw == null) {
			fw = new FilterWrapper(name, FilterManager.registry.get(name));
			FilterManager.created.put(name, fw);
		}
		return fw;
	}
}
