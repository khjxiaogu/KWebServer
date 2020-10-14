package com.khjxiaogu.webserver.loging;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

public class SystemLogger extends PrintStream {
	private final static SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");

	public static enum Level {
		CONFIG("[%s][%s]" + new Ansi().fg(Ansi.Color.CYAN).toString() + "[配置]" + new Ansi().reset().toString() + "%s"),
		INFO("[%s][%s]" + new Ansi().fg(Ansi.Color.GREEN).toString() + "[信息]" + new Ansi().reset().toString() + "%s"),
		WARNING("[%s][%s]" + new Ansi().fg(Ansi.Color.YELLOW).toString() + "[警告]" + new Ansi().reset().toString()
		        + "%s"),
		ERROR("[%s][%s]" + new Ansi().fg(Ansi.Color.RED).toString() + "[错误]" + new Ansi().reset().toString() + "%s"),
		SEVERE("[%s][%s]" + new Ansi().fg(Ansi.Color.RED).toString() + "[致命]" + new Ansi().reset().toString() + "%s");

		private Level(String format) { this.format = format; }

		protected String format;
	}

	static {
		AnsiConsole.systemInstall();
	}

	public String getName() { return name; }

	private String name;
	private static PrintStream outStream = System.out;
	public static OutputStream logStream = new OutputStream() {
		private volatile boolean closed;

		private void ensureOpen() throws IOException {
			if (closed)
				throw new IOException("Stream closed");
		}

		@Override
		public void write(int b) throws IOException { ensureOpen(); }

		@Override
		public void write(byte b[], int off, int len) throws IOException {
			//Objects.checkFromIndexSize(off, len, b.length);
			ensureOpen();
		}

		@Override
		public void close() { closed = true; }
	};

	public void setOut(OutputStream out) { this.out = out; }

	public SystemLogger(String name) {
		super(SystemLogger.logStream);
		this.name = name;
		if (name == null)
			name = "未知";
	}

	@Override
	public void print(String s) {
		super.print(s);
		SystemLogger.outStream.print(s);
	}

	@Override
	public void print(boolean b) {
		super.print(b);
		SystemLogger.outStream.print(b);
	}

	@Override
	public void print(char c) {
		super.print(c);
		SystemLogger.outStream.print(c);
	}

	@Override
	public void print(int i) {
		super.print(i);
		SystemLogger.outStream.print(i);
	}

	@Override
	public void print(long l) {
		super.print(l);
		SystemLogger.outStream.print(l);
	}

	@Override
	public void print(float f) {
		super.print(f);
		SystemLogger.outStream.print(f);
	}

	@Override
	public void print(double d) {
		super.print(d);
		SystemLogger.outStream.print(d);
	}

	@Override
	public void print(char[] s) {
		super.print(s);
		SystemLogger.outStream.print(s);
	}

	@Override
	public void print(Object obj) {
		super.print(obj);
		SystemLogger.outStream.print(obj);
	}

	@Override
	public PrintStream printf(Locale l, String format, Object... args) {
		SystemLogger.outStream.printf(l, format, args);
		super.printf(l, format, args);
		return this;
	}

	@Override
	public PrintStream printf(String x, Object... objects) {
		String s = String.format(x, objects);
		SystemLogger.outStream.print(s);
		super.print(s.replaceAll("\u001B\\[[;\\d]*m", ""));
		return this;
	}

	@Override
	public void println() {
		super.println();
		SystemLogger.outStream.println();
	}

	@Override
	public void write(int b) { super.write(b); }

	@Override
	public void write(byte[] buf, int off, int len) { super.write(buf, off, len); }

	@Override
	public void println(boolean x) {
		super.println(x);
		SystemLogger.outStream.println();
	}

	@Override
	public void println(char x) {
		super.println(x);
		SystemLogger.outStream.println();
	}

	@Override
	public void println(int x) {
		super.println(x);
		SystemLogger.outStream.println();
	}

	@Override
	public void println(long x) {
		super.println(x);
		SystemLogger.outStream.println();
	}

	@Override
	public void println(float x) {
		super.println(x);
		SystemLogger.outStream.println();
	}

	@Override
	public void println(double x) {
		super.println(x);
		SystemLogger.outStream.println();
	}

	@Override
	public void println(char[] x) {
		super.println(x);
		SystemLogger.outStream.println();
	}

	@Override
	public void println(String x) {
		super.println(x);
		SystemLogger.outStream.println();
	}

	@Override
	public void println(Object x) {
		super.println(x);
		SystemLogger.outStream.println();
	}

	public void log(Level l, String s) {
		printf(l.format, SystemLogger.format.format(new Date()), name, s);
		println();
	}

	public void severe(Object s) { log(Level.SEVERE, String.valueOf(s)); }

	public void error(Object s) { log(Level.ERROR, String.valueOf(s)); }

	public void warning(Object s) { log(Level.WARNING, String.valueOf(s)); }

	public void info(Object s) { log(Level.INFO, String.valueOf(s)); }

	public void config(Object s) { log(Level.CONFIG, String.valueOf(s)); }
}
