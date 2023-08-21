package com.khjxiaogu.webserver.wrappers;

import java.lang.reflect.Parameter;

import com.khjxiaogu.webserver.web.ServiceClass;
import com.khjxiaogu.webserver.web.lowlayer.Request;

public class ParamAdapterWrapper {
	String paramName;
	Class<?> paramClass;
	InAdapter adapter;
	public ParamAdapterWrapper(Parameter param,InAdapter adapter) {
		super();
		this.adapter = adapter;
		this.paramName=param.getName();
		this.paramClass=param.getType();
	}
	public ParamAdapterWrapper(String paramName, Class<?> paramClass, InAdapter adapter) {
		super();
		this.paramName = paramName;
		this.paramClass = paramClass;
		this.adapter = adapter;
	}
	public Object handle(Request req,ServiceClass cls) throws Exception {
		return adapter.handle(req, paramClass,cls);
	}
}
