package com.khjxiaogu.webserver.wrappers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.khjxiaogu.webserver.web.lowlayer.Request;
import com.khjxiaogu.webserver.web.lowlayer.Response;

public class FilterChainIterator implements FilterChain {
	Iterator<Filter> lsf;
	List<Filter> Ignores = new ArrayList<>(0);
	List<Filter> addons = new ArrayList<>(0);

	public FilterChainIterator() {}

	@Override
	public void next(Request req, Response res) {
		while (lsf.hasNext()) {
			Filter f = lsf.next();
			if (Ignores.indexOf(f) == -1)
				try {
					lsf.next().handle(req, res, this);
					break;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					break;
				}
		}
	}

	@Override
	public void addFilter(Filter f) {

	}

	@Override
	public void ignoreFilter(Filter type) { Ignores.add(type); }

}
