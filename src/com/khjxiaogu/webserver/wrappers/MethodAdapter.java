package com.khjxiaogu.webserver.wrappers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.khjxiaogu.webserver.web.lowlayer.Request;
import com.khjxiaogu.webserver.web.lowlayer.Response;

public class MethodAdapter implements IOAdapter {

	protected Method method;
	protected Object objthis;
	protected InAdapter iadp;
	protected OutAdapter oadp;

	protected MethodAdapter() {}

	protected void setMethod(Method method, Object objthis) {
		this.method = method;
		this.objthis = objthis;
	}

	protected final HResult callMethod(Object... args)
	        throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return (HResult) method.invoke(objthis, args);
	}

	@Override
	public void handle(Request req, Response res) {
		try {
			oadp.handle(callMethod(iadp.handle(req)), res);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			res.write(500, e.getMessage());
			e.printStackTrace();
		}
	}

	public final static MethodAdapter createAdapter(Class<? extends InAdapter> icls, Class<? extends OutAdapter> ocls,
	        Method method, Object objthis) {
		try {
			MethodAdapter ret = new MethodAdapter();
			ret.iadp = icls.getConstructor().newInstance();
			ret.oadp = ocls.getConstructor().newInstance();
			method.setAccessible(true);
			ret.setMethod(method, objthis);
			return ret;
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
		        | NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
