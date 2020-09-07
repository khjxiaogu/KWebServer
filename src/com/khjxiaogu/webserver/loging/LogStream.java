package com.khjxiaogu.webserver.loging;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author khjxiaogu
 * @time 2020年3月28日 project:KChatServer
 */
public class LogStream extends OutputStream {

	private static OutputStream dout = System.out;
	OutputStream sout;

	public LogStream(OutputStream sout) { this.sout = sout; }

	@Override
	public synchronized void close() throws IOException { sout.close(); }

	@Override
	public synchronized void flush() throws IOException {
		sout.flush();
		LogStream.dout.flush();
	}

	@Override
	public synchronized void write(int val) throws IOException {
		sout.write(val);
		LogStream.dout.write(val);
	}

	@Override
	public synchronized void write(byte[] ba) throws IOException {
		sout.write(ba);
		LogStream.dout.write(ba);
	}

	@Override
	public synchronized void write(byte[] ba, int str, int len) throws IOException {
		sout.write(ba, str, len);
		LogStream.dout.write(ba, str, len);
	}

}
