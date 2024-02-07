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

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.khjxiaogu.webserver.Utils;
import com.khjxiaogu.webserver.WebServerException;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultFileRegion;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.DefaultLastHttpContent;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpChunkedInput;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedFile;
import io.netty.handler.stream.ChunkedStream;

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
	private static final Pattern RANGE_HEADER = Pattern.compile("bytes=(\\d+)?-(\\d+)?");
	private final SimpleDateFormat format = new SimpleDateFormat("EEE', 'dd' 'MMM' 'yyyy' 'HH:mm:ss 'GMT'");
	{
		format.setTimeZone(TimeZone.getTimeZone("GMT"));
		format.setDateFormatSymbols(DateFormatSymbols.getInstance(Locale.ENGLISH));
	}
	private HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
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
		if (isSecure) { response.headers().set("Strict-Transport-Security", "max-age=15556000"); }
		response.headers().set(HttpHeaderNames.SERVER,"KWebServer");
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
		compressed();
		HttpUtil.setContentLength(response, content.length());
		ex.write(response);
		ex.writeAndFlush(new DefaultLastHttpContent(
		        ex.alloc().buffer(content.length()).writeBytes(content.getBytes(StandardCharsets.UTF_8))));
		written = true;
	}
	public void compressed() {
		if(ex.pipeline().get("gzip") == null) {
			HttpChunkContentCompressor hccc=new HttpChunkContentCompressor();
			try {//Hack compress to add dynamically
				hccc.decode(ex, cor, new ArrayList<>(1));
			} catch (Exception e) {
				e.printStackTrace();
			}
			ex.pipeline().addBefore("cw", "gzip", hccc);
			
		}
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
	 */
	public void write(int status) {
		response.setStatus(HttpResponseStatus.valueOf(status));
		ex.write(response);
		ex.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
		// ex.close();
		written = true;
	}
	public void redirect(String url) {
		response.setStatus(HttpResponseStatus.FOUND);
		response.headers().set(HttpHeaderNames.LOCATION,url);
		ex.write(response);
		ex.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
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

		if (!f.exists())
			return;
		Range r=parseRange();
		if(r==null) {
			write(416);
			return;
		}
		if (checkCacheValid(f)) {
			write(304);
			return;
		}
		response.setStatus(HttpResponseStatus.valueOf(status));
		response.headers().add(HttpHeaderNames.ACCEPT_RANGES,"bytes");
		writeFile(f,r);
	}
	private void writeFile(File f,Range r) {
		try{
			RandomAccessFile raf = new RandomAccessFile(f, "r");
			if(!r.isEmpty()) {
				response.setStatus(HttpResponseStatus.PARTIAL_CONTENT);
			}
			long start=r.start;
			if(start<0) {
				start=r.start+raf.length();
			}
			long len=0;
			if(!r.isEmpty())
				len=r.end-r.start+1;
			long maxlen=raf.length()-start;
			if(len<=0||len>maxlen)
				len=maxlen;
			
			if(!r.isEmpty()) {
				response.headers().add (HttpHeaderNames.CONTENT_RANGE,
	                    "bytes "
	                    + start
	                    + "-"
	                    + (len+start-1)
	                    + "/"
	                    + raf.length());
				response.setStatus(HttpResponseStatus.PARTIAL_CONTENT);
			}

			HttpUtil.setContentLength(response, len);
			if (response.headers().get(HttpHeaderNames.CONTENT_TYPE) == null) {
				String mime = Utils.getMime(f);
				if (mime != null) {
					if(mime.startsWith("text")||mime.startsWith("application/json"))
						compressed();
					response.headers().set(HttpHeaderNames.CONTENT_TYPE, mime);
				}else{
					compressed();
				}
				//compressed();
			}
			/*if (len > 102400||ex.pipeline().get(SslHandler.class) !=null||ex.pipeline().get(HttpContentCompressor.class)!=null)
				response.headers().set(HttpHeaderNames.TRANSFER_ENCODING, "chunked");*/
			if(cor.method()==HttpMethod.HEAD) {
				
				ex.write(response);
				ex.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
				raf.close();
			}else
			if (len > 102400||ex.pipeline().get(SslHandler.class) !=null||ex.pipeline().get(HttpContentCompressor.class)!=null) {
				System.out.println("chunked start:"+start+"len:"+len);
				response.headers().set(HttpHeaderNames.CONTENT_LENGTH, len);
				response.headers().set(HttpHeaderNames.TRANSFER_ENCODING, HttpHeaderValues.CHUNKED);
				/*for(Entry<String, String> iv:response.headers()) {
					System.out.println(iv.getKey()+":"+iv.getValue());
				}*/
				ex.write(response);
				
				ChannelFuture sendFileFuture=ex.writeAndFlush(new HttpChunkedInput(new ChunkedFile(raf,start,len,8192)), ex.newProgressivePromise());
				//sendFileFuture.await();
				//System.out.println("finished");
			   // if (!(cor.headers().contains("Connection") && cor.headers().get("Connection") == "close")) {
	           //     sendFileFuture.addListener(ChannelFutureListener.CLOSE);
	            //}
				
				//future.addListener(null)
			} else {
				//System.out.println("start:"+start+"len:"+len);
				response.headers().set(HttpHeaderNames.CONTENT_LENGTH, len);
				ex.write(response);
				ex.writeAndFlush(new DefaultFileRegion(raf.getChannel(), start, len));
				//ex.writeAndFlush(new DefaultLastHttpContent());
				//raf.close();
			}
			written = true;
		} catch (IOException e) {
			throw new WebServerException(e);
		}
	}
	 private Range parseRange() {
        String header = cor.headers().get(HttpHeaderNames.RANGE);
        if (header==null||header.isEmpty()) {
            return Range.ALL;
        }
        Matcher m = RANGE_HEADER.matcher(header);
        if (!m.matches()) {
            return null;
        }
        String start=m.group(1);
        String end=m.group(2);
        long starti=-1;
        long endi=0;
        if(start!=null)
	        try {
	        	starti=Long.parseLong(start.trim());
	        }catch(NumberFormatException ex) {return null;}
        if(end!=null)
	        try {
	        	endi=Long.parseLong(end.trim());
	        }catch(NumberFormatException ex) {return null;}
        if(starti==-1&&endi!=0) {
        	return new Range(-endi-1,0);
        }
        return new Range(starti,endi);
    }
	private String getETag(File file) {
		long lmf = file.lastModified();
		return "\""+lmf+"\"";
	}
	public boolean checkIdentical(File file) {
		String etag=getETag(file);
		if(cor.headers().get(HttpHeaderNames.IF_MATCH)!=null&&!cor.headers().get(HttpHeaderNames.IF_MATCH).equals(etag)) {
			return false;
		}
		return true;
	}
	public boolean checkETagValid(String tag) {
		String etag="\""+tag+"\"";
		setHeader(HttpHeaderNames.ETAG,etag);
		if (cor.headers().get(HttpHeaderNames.IF_NONE_MATCH)==null||!cor.headers().get(HttpHeaderNames.IF_NONE_MATCH).equals(etag)){
			return false;
		}
		return true;
	}
	public boolean checkLastModifiedValid(Date date) {
		setHeader(HttpHeaderNames.LAST_MODIFIED, format.format(date));
		try {
			if (cor.headers().get(HttpHeaderNames.IF_MODIFIED_SINCE) == null || format.parse(cor.headers().get(HttpHeaderNames.IF_MODIFIED_SINCE)).getTime() + 2000 <= date.getTime()){
				return false;
			}
		} catch (ParseException e) {
			return false;
		}
		return true;
	}
	/*
	 * check Does we need to send contents to client or modify on server?
	 * true: content unchanged
	 * false: content changed
	 * 
	 * */
	private  boolean checkCacheValid(File file){
		setHeader(HttpHeaderNames.CACHE_CONTROL, "public,max-age=10,must-revalidate");

		String etag=getETag(file);
		setHeader(HttpHeaderNames.ETAG,etag);
		
		long lmf = file.lastModified();
		Date lmd = new Date();
		if (lmf > 0) { lmd.setTime(lmf); }
		setHeader(HttpHeaderNames.LAST_MODIFIED, format.format(lmd));
		
		try {
			if ((cor.headers().get(HttpHeaderNames.IF_MODIFIED_SINCE) == null || format.parse(cor.headers().get(HttpHeaderNames.IF_MODIFIED_SINCE)).getTime() + 2000 <= lmf)
				&&(cor.headers().get(HttpHeaderNames.IF_NONE_MATCH)==null||!cor.headers().get(HttpHeaderNames.IF_NONE_MATCH).equals(etag))
				) {
					return false;
				}
		} catch (ParseException e) {
			return false;
		}

		return true;
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
	 * @return 如果写入过，返回true<br>
	 *         if is written,true.
	 */
	public boolean isWritten() { return written; }
}
