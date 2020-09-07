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
	 * @param forceSecure the force secure<br>
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
					res.setHeader(HttpHeaderNames.LOCATION, "https://" + req.headers.get(HttpHeaderNames.HOST)
					        + req.fullpath + (req.queryString != null ? "?" + req.queryString : ""));
					res.write(302);
					return;
				}
				wrapped.call(req, res);
			};
		else if (protocol.equals(Protocol.HTTP))
			return (req, res) -> {
				res.insecure();
				if (req.isSecure()) {
					res.setHeader(HttpHeaderNames.LOCATION, "http://" + req.headers.get(HttpHeaderNames.HOST)
					        + req.fullpath + (req.queryString != null ? "?" + req.queryString : ""));
					res.write(302);
					return;
				}
				wrapped.call(req, res);
			};
		return wrapped;
	}

}
