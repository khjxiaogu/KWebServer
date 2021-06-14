package com.khjxiaogu.webserver.web;

import java.util.HashMap;
import java.util.Map;

import com.khjxiaogu.webserver.web.lowlayer.Request;
import com.khjxiaogu.webserver.web.lowlayer.Response;

/**
 * Class MethodContext. 按照请求类型
 *
 * @author khjxiaogu file: MethodContext.java time: 2020年5月8日
 */
public class MethodContext implements ContextHandler<MethodContext> {

	/**
	 * The ctxs.<br>
	 * 成员 ctxs.
	 */
	private Map<String, CallBack> ctxs = new HashMap<>();

	/**
	 * The default call back.<br>
	 * 成员 default call back.
	 */
	private CallBack defaultCallBack = null;

	/**
	 * Gets the default call back.<br>
	 * 获取默认回调.
	 *
	 * @return default call back<br>
	 *         默认回调
	 */
	public CallBack getDefaultCallBack() { return defaultCallBack; }

	/**
	 * Sets the default call back.<br>
	 * 设置默认回调
	 *
	 * @param defaultCallBack the default call back<br>
	 *                        默认回调
	 * @return return self<br>
	 *         返回自身
	 */
	public MethodContext setDefaultCallBack(CallBack defaultCallBack) {
		this.defaultCallBack = defaultCallBack;
		return this;
	}

	/**
	 * Instantiates a new MethodContext with a default callBack.<br>
	 * 使用一个默认回调CallBack新建一个MethodContext类<br>
	 *
	 * @param defaults the default callback<br>
	 *                 默认回调
	 */
	public MethodContext(CallBack defaults) { defaultCallBack = defaults; }

	/**
	 * Instantiates a new MethodContext.<br>
	 * 新建一个MethodContext类<br>
	 */
	public MethodContext() {

	}

	/**
	 * Instantiates a new MethodContext with a default ServerProvider.<br>
	 * 使用一个默认服务类新建一个MethodContext类<br>
	 *
	 * @param defaults the defaults<br>
	 */
	public MethodContext(ServerProvider defaults) { this(defaults.getListener()); }

	@Override
	public MethodContext createContext(String method, CallBack ctx) {
		ctxs.put(method, ctx);
		return this;
	}

	@Override
	public MethodContext removeContext(String rule) {
		if (rule != null) { ctxs.remove(rule); }
		return this;
	}

	@Override
	public void call(Request req, Response res) {
		CallBack ctx = ctxs.get(req.getMethod());
		if (ctx != null) { ctx.call(req, res); return; }
		if (defaultCallBack != null) { defaultCallBack.call(req, res); }
	}

}
