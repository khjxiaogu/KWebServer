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
package com.khjxiaogu.webserver.web;

import java.util.ArrayList;
import java.util.List;

import com.khjxiaogu.webserver.web.lowlayer.Request;
import com.khjxiaogu.webserver.web.lowlayer.Response;

import io.netty.handler.codec.http.HttpHeaderNames;

// TODO: Auto-generated Javadoc
/**
 * Class HostDispatchHandler. 根据域名决定调用回调的处理器
 *
 * @author khjxiaogu file: HostDispatchHandler.java time: 2020年5月8日
 */
public class HostDispatchHandler implements ContextHandler<HostDispatchHandler> {

	/**
	 * The ctxs.<br>
	 * 成员 ctxs.
	 */
	private List<CallBackContext> ctxs = new ArrayList<>();

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
	 */
	public CallBack getDefaultCallBack() { return defaultCallBack; }

	/**
	 * Sets the default call back.<br>
	 * 设置默认回调
	 *
	 * @param defaultCallBack the default call back<br>
	 *                        默认回调
	 * @return return self <br>
	 *         返回自身
	 */
	public HostDispatchHandler setDefaultCallBack(CallBack defaultCallBack) {
		this.defaultCallBack = defaultCallBack;
		return this;
	}

	/**
	 * Instantiates a new HostDispatchHandler with a default CallBack.<br>
	 * 使用一个默认回调新建一个HostDispatchHandler类<br>
	 *
	 * @param defaults the defaults<br>
	 */
	public HostDispatchHandler(CallBack defaults) { defaultCallBack = defaults; }

	/**
	 * Instantiates a new HostDispatchHandler.<br>
	 * 新建一个HostDispatchHandler类<br>
	 */
	public HostDispatchHandler() {

	}

	/**
	 * Instantiates a new HostDispatchHandler with a default ServerProvider.<br>
	 * 使用一个默认服务类新建一个HostDispatchHandler类<br>
	 *
	 * @param defaults the defaults<br>
	 */
	public HostDispatchHandler(ServerProvider defaults) { this(defaults.getListener()); }

	@Override
	public HostDispatchHandler createContext(String rule, CallBack ctx) {
		if (rule.length() == 0) {
			defaultCallBack = ctx;
		} else {
			ctxs.add(new CallBackContext(rule, ctx));
		}
		return this;
	}

	@Override
	public HostDispatchHandler removeContext(String rule) {
		if (rule != null) { ctxs.removeIf(T -> T.rule.equals(rule)); }
		return this;
	}

	@Override
	public void call(Request req, Response res) {
		String host = req.getHeaders().get(HttpHeaderNames.HOST);
		if (host != null) {
			for (CallBackContext hctx : ctxs)
				if (host.startsWith(hctx.rule)) { hctx.val.call(req, res); return; }
		}
		if (defaultCallBack != null) { defaultCallBack.call(req, res); }
	}

}
