package com.khjxiaogu.webserver.wrappers;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.khjxiaogu.webserver.web.ServiceClass;
import com.khjxiaogu.webserver.web.lowlayer.Request;

public final class InAdapterManager {
	static class InAdapterWrapper implements InAdapter {
		String name;
		InAdapter intern;

		public InAdapterWrapper(String name, InAdapter intern) {
			this.name = name;
			this.intern = intern;
		}

		@Override
		public Object handle(Request req, ServiceClass ctx) throws Exception { return intern.handle(req, ctx); }
	}

	static Map<String, InAdapter> registry = new ConcurrentHashMap<>();
	static Map<String, InAdapterWrapper> created = new ConcurrentHashMap<>();

	public static void registerAdapter(String name, InAdapter adapter) {
		InAdapterManager.registry.put(name, adapter);
		InAdapterWrapper fw = InAdapterManager.created.get(name);
		if (fw != null) { fw.intern = adapter; }
	}

	public static void unregisterAdapter(String name) {
		InAdapterManager.registry.remove(name);
		InAdapterWrapper fw = InAdapterManager.created.get(name);
		if (fw != null) { fw.intern = null; }
	}

	public static InAdapter getAdapter(String name) {
		InAdapterWrapper fw = InAdapterManager.created.get(name);
		if (fw == null) {
			fw = new InAdapterWrapper(name, InAdapterManager.registry.get(name));
			InAdapterManager.created.put(name, fw);
		}
		return fw;
	}
}
