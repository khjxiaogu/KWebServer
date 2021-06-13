package com.khjxiaogu.webserver.web;

import java.util.HashSet;
import java.util.Set;

import com.khjxiaogu.webserver.web.lowlayer.Request;
import com.khjxiaogu.webserver.web.lowlayer.Response;

public class MethodRestrictHandler implements CallBack {
	Set<String> allow = new HashSet<>();
	CallBack orig;

	public MethodRestrictHandler(CallBack orig) { this.orig = orig; }

	public MethodRestrictHandler(ServerProvider crn) { orig = crn.getListener(); }

	public void addMethod(String method) { allow.add(method); }

	@Override
	public void call(Request req, Response res) { if (allow.contains(req.getMethod())) { orig.call(req, res); }else res.write(405); }

}
