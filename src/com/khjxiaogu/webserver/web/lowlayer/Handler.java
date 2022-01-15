package com.khjxiaogu.webserver.web.lowlayer;

import java.io.IOException;

import com.khjxiaogu.webserver.loging.SimpleLogger;
import com.khjxiaogu.webserver.web.CallBack;
import com.khjxiaogu.webserver.web.ServerProvider;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

public class Handler {
	CallBack cb = null;
	SimpleLogger logger = new SimpleLogger("包装层");

	public boolean handle(ChannelHandlerContext ctx, boolean isSecure, FullHttpRequest fhr) throws IOException {
		/*
		 * Request rq = new Request(fhr.headers(), fhr.method(), fhr.content(), requri,
		 * ctx.channel().remoteAddress(), isSecure);
		 */
		Request rq = new Request(ctx, isSecure, fhr);
		Response r = new Response(ctx, isSecure, fhr);
		cb.call(rq, r);
		return r.isWritten();
	}

	public Handler(CallBack cb) { this.cb = cb; }

	public Handler(ServerProvider provider) { this(provider.getListener()); }

}
