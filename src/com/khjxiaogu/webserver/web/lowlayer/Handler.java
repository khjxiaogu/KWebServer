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
