package com.khjxiaogu.webserver.web;

import java.util.HashSet;
import java.util.Set;

public class MethodRestrictHandler implements ServerProvider {
	Set<String> allow = new HashSet<>();
	CallBack orig;

	public MethodRestrictHandler(CallBack orig) { this.orig = orig; }

	public MethodRestrictHandler(ServerProvider crn) { this.orig = crn.getListener(); }

	public void addMethod(String method) { allow.add(method); }

	@Override
	public CallBack getListener() {
		return (req, res) -> {
			if (allow.contains(req.method))
				orig.call(req, res);
		};
	}

}
