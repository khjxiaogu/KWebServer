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

import io.netty.handler.codec.http.HttpHeaderNames;

// TODO: Auto-generated Javadoc
/**
 * Class ForceSecureHandler. Force http or https connection 强制http或者https链接
 *
 * @author khjxiaogu file: ForceSecureHandler.java time: 2020年6月12日
 */
public class ForceSecureHandler implements ServerProvider {
	public enum Protocol {
		HTTP, HTTPS;
	}

	private CallBack wrapped;
	private Protocol protocol;

	/**
	 * Instantiates a new ForceSecureHandler.<br>
	 * 新建一个ForceSecureHandler类<br>
	 *
	 * @param wrapped     the wrapped callback<br>
	 *                    修饰的回调
	 * @param protocol the forced protocol<br>
	 *                    是否强制https
	 */
	public ForceSecureHandler(CallBack wrapped, Protocol protocol) {
		this.wrapped = wrapped;
		this.protocol = protocol;
	}

	/**
	 * Gets the listener.<br>
	 * 获取 listener.
	 *
	 * @return listener<br>
	 */
	@Override
	public CallBack getListener() {
		if (protocol.equals(Protocol.HTTPS))
			return (req, res) -> {
				if (!req.isSecure()) {
					res.setHeader(HttpHeaderNames.LOCATION, "https://" + req.getHeaders().get(HttpHeaderNames.HOST)
					        + req.getFullpath() + (req.getQueryString() != null ? "?" + req.getQueryString() : ""));
					res.write(302);
					return;
				}
				wrapped.call(req, res);
			};
		else if (protocol.equals(Protocol.HTTP))
			return (req, res) -> {
				if (req.isSecure()) {
					res.setHeader(HttpHeaderNames.LOCATION, "http://" + req.getHeaders().get(HttpHeaderNames.HOST)
					        + req.getFullpath() + (req.getQueryString() != null ? "?" + req.getQueryString() : ""));
					res.write(302);
					return;
				}
				wrapped.call(req, res);
			};
		return wrapped;
	}

}
