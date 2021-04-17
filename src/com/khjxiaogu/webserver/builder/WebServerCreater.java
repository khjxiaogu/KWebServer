package com.khjxiaogu.webserver.builder;

import io.netty.handler.ssl.SslContext;

public interface WebServerCreater extends OpenedWebServerCreater {

	/**
	 * Sets the SSL context.<br>
	 * 设置SSL上下文
	 *
	 * @param slc the SSL context<br>
	 *            SSL上下文
	 * @return return self <br>
	 *         返回自身
	 */
	WebServerCreater setSSL(SslContext slc);

}