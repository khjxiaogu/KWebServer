package com.khjxiaogu.webserver.web.lowlayer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.khjxiaogu.webserver.loging.SystemLogger;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;

@Sharable
public class NettyHandlerBridge extends SimpleChannelInboundHandler<FullHttpRequest> {

	public final static SystemLogger logger = new SystemLogger("网络");
	private File notFound;
	private Handler httpHandler;
	private boolean isHttps = true;

	public boolean isHttps() { return isHttps; }

	public NettyHandlerBridge setHttps(boolean isHttps) {
		this.isHttps = isHttps;
		return this;
	}

	public NettyHandlerBridge() { super(); }

	public NettyHandlerBridge(NettyHandlerBridge orig) {
		super();
		notFound = orig.notFound;
		httpHandler = orig.httpHandler;

	}

	public NettyHandlerBridge setNotFound(File pnf) {
		notFound = pnf;
		return this;
	}

	public NettyHandlerBridge setHandler(Handler handler) {
		httpHandler = handler;
		return this;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.channel().close();
		NettyHandlerBridge.logger.warning(cause.getMessage());
		cause.printStackTrace(NettyHandlerBridge.logger);
	}

	@Override
	public void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
		if (!request.decoderResult().isSuccess()) {
			FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
			        HttpResponseStatus.BAD_REQUEST);
			boolean keepAlive = HttpUtil.isKeepAlive(request);
			HttpUtil.setContentLength(response, response.content().readableBytes());
			if (!keepAlive) {
				response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
			} else if (request.protocolVersion().equals(HttpVersion.HTTP_1_0)) {
				response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
			}
			ChannelFuture flushPromise = ctx.writeAndFlush(response);
			if (!keepAlive) { flushPromise.addListener(ChannelFutureListener.CLOSE); }
			return;
		}
		try {
			if (httpHandler.handle(ctx, isHttps, request))
				return;
		} catch (Throwable t) {
			t.printStackTrace(NettyHandlerBridge.logger);
			FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
			        HttpResponseStatus.INTERNAL_SERVER_ERROR);
			boolean keepAlive = HttpUtil.isKeepAlive(request);
			HttpUtil.setContentLength(response, response.content().readableBytes());
			if (!keepAlive) {
				response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
			} else if (request.protocolVersion().equals(HttpVersion.HTTP_1_0)) {
				response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
			}
			ChannelFuture flushPromise = ctx.writeAndFlush(response);
			if (!keepAlive) { flushPromise.addListener(ChannelFutureListener.CLOSE); }
		}
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND);
		boolean keepAlive = HttpUtil.isKeepAlive(request);
		HttpUtil.setContentLength(response, response.content().readableBytes());
		if (!keepAlive) {
			response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
		} else if (request.protocolVersion().equals(HttpVersion.HTTP_1_0)) {
			response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
		}
		if (notFound != null) {
			try (InputStream raf = new FileInputStream(notFound)) {
				HttpUtil.setContentLength(response, raf.available());
				response.content().capacity(raf.available());
				response.content().writeBytes(raf, raf.available());
			} catch (IOException ignored) {}
		}
		ChannelFuture flushPromise = ctx.writeAndFlush(response);
		if (!keepAlive) { flushPromise.addListener(ChannelFutureListener.CLOSE); }
	}

}
