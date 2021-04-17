package com.khjxiaogu.webserver.wrappers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.khjxiaogu.webserver.web.lowlayer.Request;
import com.khjxiaogu.webserver.web.lowlayer.Response;

public class FilterChainIterator implements FilterChain {
	ArrayList<HttpFilter> httpFilters;
	int i=0;
	public FilterChainIterator(List<HttpFilter> httpFilters) {
		this.httpFilters=new ArrayList<>(httpFilters);
	}
	public FilterChainIterator(HttpFilter[] httpFilters) {
		this.httpFilters=new ArrayList<>(httpFilters.length);
		Collections.addAll(this.httpFilters,httpFilters);
	}
	@Override
	public boolean next(Request req, Response res) throws Exception {
		return httpFilters.get(i++).handle(req, res,this);
	}
	public boolean hasNext() {
		return httpFilters.size()<i;
	}
	@Override
	public void add(HttpFilter f) {
		httpFilters.add(i, f);
	}
	@Override
	public void addLast(HttpFilter f) {
		httpFilters.add(f);
	}
	@Override
	public void ignore(HttpFilter type) {
		for(int j=i;j<httpFilters.size();i++) {
			if(httpFilters.get(j).equals(type)) {
				httpFilters.remove(j);
			}
		}
	}

}
