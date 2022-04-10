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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.khjxiaogu.webserver.loging.SimpleLogger;

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

	public final static SimpleLogger logger = new SimpleLogger("网络");
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
		NettyHandlerBridge.logger.printStackTrace(cause);
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
			NettyHandlerBridge.logger.printStackTrace(t);
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
