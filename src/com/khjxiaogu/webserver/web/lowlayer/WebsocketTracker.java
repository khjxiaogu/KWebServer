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


import com.khjxiaogu.webserver.loging.SimpleLogger.Level;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;

public class WebsocketTracker extends SimpleChannelInboundHandler<WebSocketFrame> {
	WebsocketEvents ev;
	Channel conn;

	public WebsocketTracker(WebsocketEvents ev, ChannelHandlerContext ctx, FullHttpRequest request) {
		this.ev = ev;
		conn = ctx.channel();
		WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
		        "wss://" + request.headers().get(HttpHeaderNames.HOST) + request.uri(), null, true);
		WebSocketServerHandshaker handshaker = wsFactory.newHandshaker(request);
		if (handshaker == null) {
			WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
		} else {
			ChannelFuture channelFuture = handshaker.handshake(ctx.channel(), request);
			if (channelFuture.isSuccess()) { ev.onOpen(ctx.channel(), request); }
		}
	}

	@Override
	public void channelRead0(ChannelHandlerContext ctx, WebSocketFrame wsf) throws Exception {
		if (wsf instanceof PingWebSocketFrame) {
			ctx.channel().writeAndFlush(new PongWebSocketFrame(wsf.content().retain()));
		} else if (wsf instanceof TextWebSocketFrame) {
			ev.onMessage(ctx.channel(), ((TextWebSocketFrame) wsf).text());
		} else if (wsf instanceof CloseWebSocketFrame) { ctx.channel().close(); }
		return;
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		ev.onClose(ctx.channel());
		super.channelInactive(ctx);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.channel().close();
		NettyHandlerBridge.logger.warning(cause.getMessage());
		NettyHandlerBridge.logger.printStackTrace(Level.WARNING,cause);
	}
}
