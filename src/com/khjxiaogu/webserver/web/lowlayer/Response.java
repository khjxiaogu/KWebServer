package com.khjxiaogu.webserver.web.lowlayer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import com.khjxiaogu.webserver.Utils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.DefaultLastHttpContent;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpChunkedInput;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.stream.ChunkedFile;

// TODO: Auto-generated Javadoc
/**
 * Class Response. 回复体
 *
 * @author khjxiaogu file: Response.java time: 2020年6月12日
 */
public class Response {
	private boolean written = false;
	private final FullHttpRequest cor;
	private final ChannelHandlerContext ex;
	private HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
	private boolean isSecure;

	/**
	 * Instantiates a new Response with a ChannelHandlerContext object.<br>
	 * 使用一个ChannelHandlerContext新建一个Response类<br>
	 *
	 * @param t        the t<br>
	 * @param isSecure
	 */
	Response(ChannelHandlerContext t, boolean isSecure, FullHttpRequest req) {
		cor = req;
		ex = t;
		this.isSecure = isSecure;
		if (isSecure) { response.headers().set("Strict-Transport-Security", "max-age=15556000"); }
		response.headers().set("Access-Control-Allow-Origin", "*");
	}

	/**
	 * Write to response.<br>
	 * 回复
	 *
	 * @param status  the http status code<br>
	 *                http回复码
	 * @param queries the response headers, can be null<br>
	 *                回复头，可以为null
	 * @param content the response content<br>
	 *                回复体
	 */
	public void write(int status, Map<String, String> queries, byte[] content) {
		if (queries != null) {
			for (Map.Entry<String, String> query : queries.entrySet()) {
				response.headers().set(query.getKey(), query.getValue());
			}
		}
		write(status, content);
	}

	/**
	 * Write to response.<br>
	 * 回复
	 *
	 * @param status  the http status code<br>
	 *                http回复码
	 * @param content the response content<br>
	 *                回复体
	 */
	public void write(int status, String content) {
		if (response.headers().get(HttpHeaderNames.CONTENT_TYPE) == null) {
			response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");
		}
		response.setStatus(HttpResponseStatus.valueOf(status));

		HttpUtil.setContentLength(response, content.length());
		ex.write(response);
		ex.writeAndFlush(new DefaultLastHttpContent(
		        ex.alloc().buffer(content.length()).writeBytes(content.getBytes(StandardCharsets.UTF_8))));
		written = true;
	}

	/**
	 * Write to response.<br>
	 * 回复
	 *
	 * @param status  the http status code<br>
	 *                http回复码
	 * @param content the response content<br>
	 *                回复体
	 */
	public void write(int status, byte[] content) {
		if (response.headers().get(HttpHeaderNames.CONTENT_TYPE) == null) {
			response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");
		}
		response.setStatus(HttpResponseStatus.valueOf(status));
		HttpUtil.setContentLength(response, content.length);
		ex.write(response);
		ex.writeAndFlush(new DefaultLastHttpContent(ex.alloc().buffer(content.length).writeBytes(content)));
		written = true;
	}

	/**
	 * Write to response.<br>
	 * 回复
	 *
	 * @param status  the http status code<br>
	 *                http回复码
	 * @param queries the response headers, can be null<br>
	 *                回复头，可以为null
	 */
	public void write(int status, Map<String, String> queries) {
		if (queries != null) {
			for (Map.Entry<String, String> query : queries.entrySet()) {
				response.headers().set(query.getKey(), query.getValue());
			}
		}
		write(status);
	}

	/**
	 * Write to response.<br>
	 * 回复
	 *
	 * @param status  the http status code<br>
	 *                http回复码
	 * @param queries the response headers, can be null<br>
	 *                回复头，可以为null
	 */
	public void write(int status) {
		response.setStatus(HttpResponseStatus.valueOf(status));
		ex.write(response);
		ex.writeAndFlush(new DefaultLastHttpContent());
		// ex.close();
		written = true;
	}

	/**
	 * Set header.<br>
	 * 设置回复头值
	 *
	 * @param key the key<br>
	 *            回复头名称
	 * @param val the value<br>
	 *            回复头值
	 */
	public void setHeader(CharSequence key, String val) { response.headers().set(key, val); }

	/**
	 * Write a file to response.<br>
	 * 回复一个文件
	 *
	 * @param status  the status<br>
	 *                http回复码
	 * @param queries the response headers, can be null<br>
	 *                回复头，可以为null
	 * @param f       the file<br>
	 *                文件
	 */
	public void write(int status, Map<String, String> queries, File f) {
		if (queries != null) {
			for (Map.Entry<String, String> query : queries.entrySet()) {
				response.headers().set(query.getKey(), query.getValue());
			}
		}
		write(status, f);

	}

	/**
	 * Write a file to response.<br>
	 * 回复一个文件
	 *
	 * @param status the status<br>
	 *               http回复码
	 * @param f      the file<br>
	 *               文件
	 */
	public void write(int status, File f) {
		// response.headers().set(HttpHeaderNames.ACCEPT_RANGES, "bytes");
		/*
		 * if (response.headers().get(HttpHeaderNames.CONTENT_TYPE) == null) { String
		 * mime = Utils.getMime(f); if (mime != null) {
		 * response.headers().set(HttpHeaderNames.CONTENT_TYPE, mime); } }
		 * response.setStatus(HttpResponseStatus.valueOf(status)); try (InputStream raf
		 * = new FileInputStream(f)) { HttpUtil.setContentLength(response,
		 * raf.available()); int min = 0; int max = raf.available(); if (status == 200)
		 * { String range = request.headers.get(HttpHeaderNames.RANGE); if (range !=
		 * null) { int eq = range.indexOf('='); if (eq != -1) { status = 206; int to =
		 * range.indexOf('-'); min = Integer.parseInt(range.substring(eq + 1,
		 * to).trim()); int fin = range.indexOf(','); if (range.length() > to + 1) { if
		 * (fin != -1) { max = Integer.parseInt(range.substring(to + 1, fin).trim()); }
		 * else { max = Integer.parseInt(range.substring(to + 1).trim()); } }
		 * StringBuilder sb = new StringBuilder("bytes "); sb.append(min);
		 * sb.append("-"); sb.append(max); sb.append("/"); sb.append(max - min);
		 * response.headers().set(HttpHeaderNames.CONTENT_RANGE, sb.toString()); } } }
		 * response.content().capacity(max - min); raf.skip(min);
		 * response.content().writeBytes(raf, max); ex.writeAndFlush(response); written
		 * = true; } catch (IOException ignore) {
		 *
		 * }
		 */
		if (!f.exists())
			return;
		try {
			RandomAccessFile raf = new RandomAccessFile(f, "r");
			response.setStatus(HttpResponseStatus.valueOf(status));
			HttpUtil.setContentLength(response, raf.length());
			if (response.headers().get(HttpHeaderNames.CONTENT_TYPE) == null) {
				String mime = Utils.getMime(f);
				if (mime != null) { response.headers().set(HttpHeaderNames.CONTENT_TYPE, mime); }
			}
			if (raf.length() > 102400) {
				response.headers().set(HttpHeaderNames.CONTENT_ENCODING, "chunked");
				ex.write(response);
				ex.writeAndFlush(new HttpChunkedInput(new ChunkedFile(raf)));
			} else {
				ByteBuf buf = ex.alloc().buffer((int) raf.length());
				FileInputStream fis = new FileInputStream(f);
				buf.writeBytes(fis, (int) raf.length());
				raf.close();
				ex.write(response);
				ex.writeAndFlush(new DefaultLastHttpContent(buf)).await();
				fis.close();
			}

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

	}

	public boolean suscribeWebsocketEvents(WebsocketEvents handler) {
		String s = cor.headers().get(HttpHeaderNames.UPGRADE);
		if (s != null && s.equals("websocket")) {
			ex.pipeline().addLast(new WebsocketTracker(handler, ex, cor));
			written = true;
			return true;
		}
		return false;
	}

	/**
	 * Checks if is written.<br>
	 * 是否曾经写入过.
	 *
	 * @return 如果写入1过，返回true<br>
	 *         if is written,true.
	 */
	public boolean isWritten() { return written; }
}
