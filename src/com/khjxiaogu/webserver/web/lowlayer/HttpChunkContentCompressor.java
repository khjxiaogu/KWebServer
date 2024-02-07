package com.khjxiaogu.webserver.web.lowlayer;

import java.lang.reflect.Field;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.compression.CompressionOptions;
import io.netty.handler.codec.compression.StandardCompressionOptions;
import io.netty.handler.codec.http.DefaultHttpContent;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpContentEncoder;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;

/**
 * @author <a href="mailto:nmaurer@redhat.com">Norman Maurer</a>
 */
final class HttpChunkContentCompressor extends HttpContentCompressor {

	public HttpChunkContentCompressor(CompressionOptions... compressionOptions) {
		super(0, compressionOptions);
	}

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		System.out.println(msg.getClass().getName());
		if (msg instanceof ByteBuf) {
			// convert ByteBuf to HttpContent to make it work with compression. This is
			// needed as we use the
			// ChunkedWriteHandler to send files when compression is enabled.
			ByteBuf buff = (ByteBuf) msg;
			if (buff.isReadable()) {
				// We only encode non empty buffers, as empty buffers can be used for
				// determining when
				// the content has been flushed and it confuses the HttpContentCompressor
				// if we let it go
				msg = new DefaultHttpContent(buff);
			}
		}
		super.write(ctx, msg, promise);
	}

	@Override
	protected Result beginEncode(HttpResponse httpResponse, String acceptEncoding) throws Exception {

		Result result = super.beginEncode(httpResponse, acceptEncoding);
		if (result == null && httpResponse.headers().contains(HttpHeaderNames.CONTENT_ENCODING, "identity", true)) {
			httpResponse.headers().remove(HttpHeaderNames.CONTENT_ENCODING);
		}
		return result;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, HttpRequest msg, List<Object> out) throws Exception {
		super.decode(ctx, msg, out);
	}
}