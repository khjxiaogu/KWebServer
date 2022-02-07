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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.khjxiaogu.webserver.web.ServiceClass;
import com.khjxiaogu.webserver.web.lowlayer.Request;
import com.khjxiaogu.webserver.web.lowlayer.Response;

public class FilterChainIterator implements FilterChain {
	ArrayList<HttpFilter> httpFilters;
	ServiceClass objthis;
	int i = 0;

	public FilterChainIterator(List<HttpFilter> httpFilters,ServiceClass objthis) { this.httpFilters = new ArrayList<>(httpFilters);this.objthis=objthis; }

	public FilterChainIterator(HttpFilter[] httpFilters,ServiceClass objthis) {
		this.httpFilters = new ArrayList<>(httpFilters.length);
		Collections.addAll(this.httpFilters, httpFilters);
		this.objthis=objthis;
	}

	@Override
	public boolean next(Request req, Response res) throws Exception {
		return httpFilters.get(i++).handle(req, res, this);
	}

	public boolean hasNext() { return httpFilters.size() > i; }

	@Override
	public void add(HttpFilter f) { httpFilters.add(i, f); }

	@Override
	public void addLast(HttpFilter f) { httpFilters.add(f); }

	@Override
	public void ignore(HttpFilter type) {
		for (int j = i; j < httpFilters.size(); i++) { if (httpFilters.get(j).equals(type)) { httpFilters.remove(j); } }
	}

	@Override
	public ServiceClass getCurrentContext() { return objthis; }

}
