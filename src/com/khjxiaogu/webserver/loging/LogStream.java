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
