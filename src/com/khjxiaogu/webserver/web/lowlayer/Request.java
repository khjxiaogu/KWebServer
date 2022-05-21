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
package com.khjxiaogu.webserver.web.lowlayer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.khjxiaogu.webserver.Utils;
import com.khjxiaogu.webserver.loging.SimpleLogger;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.InterfaceHttpData.HttpDataType;

// TODO: Auto-generated Javadoc
/**
 * Class Request.
 *
 * @author khjxiaogu file: Request.java time: 2020年6月12日
 */
public class Request {
	private final static SimpleLogger logger = new SimpleLogger("请求解析");
	/**
	 * The http query(URL only).<br>
	 * 请求参数（仅包含URL中的）.
	 */
	private Map<String, String> query;
	/**
	 * The http post.<br>
	 * 请求参数.
	 */
	private Map<String, String> post;

	/**
	 * The full request path.<br>
	 * 完整请求路径.
	 */
	private String fullpath;
	private String path;
	private boolean isSecure;

	/**
	 * Checks if is https secure connection.<br>
	 * 是否https安全链接.
	 *
	 * @return 如果是https安全链接，返回true<br>
	 *         if is https secure connection,true.
	 */
	public boolean isSecure() { return isSecure; }

	/**
	 * Gets the current request path.<br>
	 * 获取当前请求路径.
	 *
	 * @return current request path<br>
	 *         当前请求路径
	 */
	public String getCurrentPath() { return path; }

	private ChannelHandlerContext ctx;
	private FullHttpRequest fhr;
	private URI uri;

	/**
	 * Instantiates a new Request.<br>
	 * 新建一个Request类<br>
	 *
	 * @param httpHeaders   the http headers<br>
	 * @param httpMethod    the http method<br>
	 * @param body          the body<br>
	 * @param uri           the uri<br>
	 * @param socketAddress the socket address<br>
	 * @param isSecure      the is secure<br>
	 */
	Request(ChannelHandlerContext ctx, boolean isSecure, FullHttpRequest fhr) {
		this.ctx = ctx;
		this.fhr = fhr;
		/*
		 * headers = httpHeaders; method = httpMethod.toString(); this.body = body;
		 * queryString = uri.getQuery(); path = uri.getPath(); fullpath = path; remote =
		 * (InetSocketAddress) socketAddress;
		 */
		try {
			uri = new URI(fhr.uri()).normalize();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			Request.logger.warning(e.getMessage());
		}
		path = uri.getPath();
		fullpath = path;
		this.isSecure = isSecure;
	}

	/**
	 * Skip path once to next /.<br>
	 * 跳过一个/
	 */
	public void SkipPathOnce() {
		int pos = path.indexOf('/', 1);
		if (pos == -1) {
			path = "/";
		} else {
			path = path.substring(pos);
		}
	}

	/**
	 * Skip path.<br>
	 *
	 * @param starts the starts<br>
	 */
	public void SkipPath(String starts) {
		if (path.length() <= starts.length()) {
			path = "/";
		} else {
			path = path.substring(starts.length());
		}
	}

	public Map<String, String> getQuery() {
		if (query != null)
			return query;
		return query = Utils.queryToMap(getQueryString());
	}

	public HttpHeaders getHeaders() { return fhr.headers(); }

	public String getMethod() { return fhr.method().toString(); }

	public ByteBuf getBody() { return fhr.content(); }

	public String getFullpath() { return fullpath; }

	public InetSocketAddress getRemote() { return (InetSocketAddress) ctx.channel().remoteAddress(); }

	public String getQueryString() { return uri.getQuery(); }
	public String getRawQueryString() { return uri.getRawQuery(); }

	public HttpPostRequestDecoder getFullPost() { return new HttpPostRequestDecoder(new DefaultHttpDataFactory(false), fhr); }

	public Map<String, String> getPost() {
		if (post == null) {
			post = new HashMap<>();
			HttpPostRequestDecoder hpd = new HttpPostRequestDecoder(new DefaultHttpDataFactory(false), fhr);
			List<InterfaceHttpData> ihds=hpd.getBodyHttpDatas();
			for(InterfaceHttpData ihd:ihds){
				if (ihd.getHttpDataType() == HttpDataType.Attribute) {
					try {
						post.put(ihd.getName(), ((Attribute) ihd).getValue());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						Request.logger.warning(e.getMessage());
					}
				}
			}
		}
		return post;
	}

	public Map<String, InterfaceHttpData> getPostMap() {
		Map<String, InterfaceHttpData> postmap = new HashMap<>();
		HttpPostRequestDecoder hpd = new HttpPostRequestDecoder(new DefaultHttpDataFactory(false), fhr);
		List<InterfaceHttpData> ihds=hpd.getBodyHttpDatas();
		for(InterfaceHttpData ihd:ihds){
			if (ihd.getHttpDataType() == HttpDataType.Attribute) { postmap.put(ihd.getName(), ihd); }
		}
		return postmap;
	}
}