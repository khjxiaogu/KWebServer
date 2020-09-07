package com.khjxiaogu.webserver.web.lowlayer;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import com.khjxiaogu.webserver.loging.SystemLogger;
import com.khjxiaogu.webserver.web.CallBack;
import com.khjxiaogu.webserver.web.ServerProvider;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

public class Handler {
	CallBack cb = null;
	SystemLogger logger = new SystemLogger("包装层");

	public boolean handle(ChannelHandlerContext ctx, boolean isSecure, FullHttpRequest fhr) throws IOException {
		try {
			URI requri = new URI(fhr.uri());

			Request rq = new Request(fhr.headers(), fhr.method(), fhr.content(), requri, ctx.channel().remoteAddress(),
			        isSecure);
			Response r = new Response(ctx, fhr);
			cb.call(rq, r);
			return r.isWritten();
		} catch (URISyntaxException e) {
			logger.warning(e.getMessage());
			return false;
		}
	}

	public Handler(CallBack cb) { this.cb = cb; }

	public Handler(ServerProvider provider) { this(provider.getListener()); }

}
