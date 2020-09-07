package com.khjxiaogu.webserver.web.lowlayer;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpRequest;

public interface WebsocketEvents {

	void onOpen(Channel conn, FullHttpRequest handshake);

	void onClose(Channel conn);

	void onMessage(Channel conn, String message);

}