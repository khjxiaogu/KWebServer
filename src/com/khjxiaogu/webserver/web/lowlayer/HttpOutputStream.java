package com.khjxiaogu.webserver.web.lowlayer;
import java.io.IOException;
import java.io.OutputStream;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.DefaultHttpContent;
import io.netty.handler.codec.http.LastHttpContent;


public class HttpOutputStream extends OutputStream {

    private final Channel channel;
    private final int chunkSize;
    private ByteBuf buffer;
    private boolean closed;

    public HttpOutputStream(Channel channel) {
        this(channel, 8192);
    }

    public HttpOutputStream(Channel channel, int chunkSize) {
        this.channel = channel;
        this.chunkSize = chunkSize;
        this.buffer = channel.alloc().buffer(chunkSize);
        this.closed = false;
    }

    private void ensureOpen() throws IOException {
        if (closed) {
            throw new IOException("Stream already closed");
        }
    }

    private void flushBuffer() throws IOException {
        if (buffer.readableBytes() > 0) {
            ByteBuf contentBuf = buffer;
            buffer = channel.alloc().buffer(chunkSize);
            DefaultHttpContent content = new DefaultHttpContent(contentBuf);
            channel.writeAndFlush(content);
        }
    }

    @Override
    public void write(int b) throws IOException {
        ensureOpen();
        if (!buffer.isWritable(1)) {
            flushBuffer();
        }
        buffer.writeByte(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        ensureOpen();
        if (len == 0) {
            return;
        }
        int remaining = len;
        int offset = off;
        while (remaining > 0) {
            int writable = buffer.writableBytes();
            if (writable == 0) {
                flushBuffer();
                writable = buffer.writableBytes();
            }
            int toWrite = Math.min(remaining, writable);
            buffer.writeBytes(b, offset, toWrite);
            remaining -= toWrite;
            offset += toWrite;
        }
    }

    @Override
    public void flush() throws IOException {
        ensureOpen();
        flushBuffer();
        channel.flush();
    }

    @Override
    public void close() throws IOException {
        if (closed) {
            return;
        }
        try {
            flush();
            channel.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
        } finally {
            closed = true;
            if (buffer != null && buffer.refCnt() > 0) {
                buffer.release();
            }
        }
    }
}