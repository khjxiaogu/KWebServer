package com.khjxiaogu.webserver.loging;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * time 2020年3月28日 project:KChatServer
 * @author khjxiaogu
 * 
 */
public class LogStream extends PrintStream {

	private static OutputStream dout = System.out;
	PrintStream sout;

	public LogStream(OutputStream sout) { super(dout);this.sout = new PrintStream(sout); }

	@Override
	public synchronized void close() { sout.close(); }

	@Override
	public synchronized void flush() {
		super.flush();
		sout.flush();
	}
	@Override
	public synchronized void write(int val) {
		super.write(val);
		sout.write(val);
	}

	@Override
	public synchronized void write(byte[] ba) throws IOException {
		sout.write(ba);
		super.write(ba);
	}

	@Override
	public synchronized void write(byte[] ba, int str, int len) {
		sout.write(ba, str, len);
		super.write(ba, str, len);
	}

	@Override
	public void print(String s) { sout.print(s.replaceAll("\u001B\\[[;\\d]*m", ""));super.print(s); }

	@Override
	public void print(Object obj) { print(String.valueOf(obj)); }
	
}
