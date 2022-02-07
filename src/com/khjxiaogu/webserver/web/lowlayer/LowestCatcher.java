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

import com.khjxiaogu.webserver.loging.SimpleLogger;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;

public class LowestCatcher extends ChannelDuplexHandler {

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		// Uncaught exceptions from inbound handlers will propagate up to this handler
		ctx.close();
	}

	SimpleLogger logger;

	/*
	 * @Override public void connect(ChannelHandlerContext ctx, SocketAddress
	 * remoteAddress, SocketAddress localAddress, ChannelPromise promise) {
	 * ctx.connect(remoteAddress, localAddress, promise.addListener(new
	 * ChannelFutureListener() {
	 *
	 * @Override public void operationComplete(ChannelFuture future) { if
	 * (!future.isSuccess()) { // Handle connect exception here... } } })); }
	 *
	 * @Override public void write(ChannelHandlerContext ctx, Object msg,
	 * ChannelPromise promise) { ctx.write(msg, promise.addListener(new
	 * ChannelFutureListener() {
	 *
	 * @Override public void operationComplete(ChannelFuture future) { if
	 * (!future.isSuccess()) {
	 *
	 * // Handle write exception here... } } })); }
	 */
	public LowestCatcher(String logger) {
		super();
		this.logger = new SimpleLogger(logger);
	}

	// ... override more outbound methods to handle their exceptions as well
}
