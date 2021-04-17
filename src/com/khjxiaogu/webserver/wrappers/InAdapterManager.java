package com.khjxiaogu.webserver.wrappers;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.khjxiaogu.webserver.web.lowlayer.Request;

public class InAdapterManager<T> {
	static class InAdapterWrapper implements InAdapter{
		String name;
		InAdapter intern;
		public InAdapterWrapper(String name, InAdapter intern) {
			this.name = name;
			this.intern = intern;
		}
		@Override
		public Object handle(Request req) throws Exception { return intern.handle(req); }
	}
	static Map<String, InAdapter> registry = new ConcurrentHashMap<>();
	static Map<String, InAdapterWrapper> created = new ConcurrentHashMap<>();
	public static void registerAdapter(String name,InAdapter adapter) {
		registry.put(name, adapter);
		InAdapterWrapper fw = created.get(name);
		if (fw != null) { fw.intern = adapter; }
	}

	public static void unregisterAdapter(String name) {
		registry.remove(name);
		InAdapterWrapper fw = created.get(name);
		if (fw != null) { fw.intern = null; }
	}

	public static InAdapter getAdapter(String name) {
		InAdapterWrapper fw = created.get(name);
		if (fw == null) {
			fw = new InAdapterWrapper(name, registry.get(name));
			created.put(name, fw);
		}
		return fw;
	}
}
