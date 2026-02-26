package com.khjxiaogu.webserver.web;

import com.khjxiaogu.webserver.web.lowlayer.Request;
import com.khjxiaogu.webserver.web.lowlayer.Response;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

public class RequestContext {
	private static class RequestData{
		ChannelHandlerContext ctx;
		Request req;
		Response res;
		FullHttpRequest fhr;
	}
	private static ThreadLocal<RequestData> context=ThreadLocal.withInitial(()->new RequestData());
	private RequestContext() {
	}
	public static void setRequestData(ChannelHandlerContext ctx,Request req,Response res,FullHttpRequest fhr) {
		RequestData data=context.get();
		data.ctx=ctx;
		data.req=req;
		data.res=res;
		data.fhr=fhr;
	}
	public static void clear() {
		RequestData data=context.get();
		data.ctx=null;
		data.req=null;
		data.res=null;
		data.fhr=null;
	}
	public static Response getResponse() {
		return context.get().res;
	}
	public static Request getRequest() {
		return context.get().req;
	}
	public static String getBasePath() {
		return context.get().req.getBasePath();
	}
	public static String getCurPath() {
		return context.get().req.getCurrentPath();
	}
	public static ChannelHandlerContext getChannelHandlerContext() {
		return context.get().ctx;
	}
	public static FullHttpRequest getFullHttpRequest() {
		return context.get().fhr;
	}
}
