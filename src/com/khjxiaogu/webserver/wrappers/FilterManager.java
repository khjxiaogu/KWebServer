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
