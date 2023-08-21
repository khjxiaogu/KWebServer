/**
 * KWebserver
 * Copyright (C) 2021  khjxiaogu
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
		public Object handle(Request req, Class<?> paramClass, ServiceClass ctx) throws Exception { return intern.handle(req, paramClass, ctx); }
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
