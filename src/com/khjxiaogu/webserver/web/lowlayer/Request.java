package com.khjxiaogu.webserver.web.lowlayer;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.URI;
import java.util.Map;

import com.khjxiaogu.webserver.Utils;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;

// TODO: Auto-generated Javadoc
/**
 * Class Request.
 *
 * @author khjxiaogu file: Request.java time: 2020年6月12日
 */
public class Request {

	/**
	 * The http headers.<br>
	 * 请求头.
	 */
	public final HttpHeaders headers;

	/**
	 * The http method.<br>
	 * 请求方法.
	 */
	public final String method;

	/**
	 * The http body.<br>
	 * 请求体.
	 */
	public final ByteBuf body;

	/**
	 * The http query(URL only).<br>
	 * 请求参数（仅包含URL中的）.
	 */
	private Map<String, String> query;

	/**
	 * The full request path.<br>
	 * 完整请求路径.
	 */
	public final String fullpath;
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

	/**
	 * The remote address.<br>
	 * 远程地址.
	 */
	public final InetSocketAddress remote;

	/**
	 * The query string.<br>
	 * 请求字符串.
	 */
	public final String queryString;

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
	Request(HttpHeaders httpHeaders, HttpMethod httpMethod, ByteBuf body, URI uri, SocketAddress socketAddress,
	        boolean isSecure) {
		super();
		headers = httpHeaders;
		method = httpMethod.toString();
		this.body = body;
		queryString = uri.getQuery();
		path = uri.getPath();
		fullpath = path;
		remote = (InetSocketAddress) socketAddress;
		this.isSecure = isSecure;
	}

	/**
	 * Skip path once to next /.<br>
	 * 跳过一个/
	 */
	public void SkipPathOnce() {
		int pos = path.indexOf('/', 1);
		if (pos == -1)
			path = "/";
		else
			path = path.substring(pos);
	}

	/**
	 * Skip path.<br>
	 *
	 * @param starts the starts<br>
	 */
	public void SkipPath(String starts) {
		if (path.length() <= starts.length())
			path = "/";
		else
			path = path.substring(starts.length());
	}

	public Map<String, String> getQuery() { 
		if(query!=null)
			return query;
		return query=Utils.queryToMap(queryString);
	}
}