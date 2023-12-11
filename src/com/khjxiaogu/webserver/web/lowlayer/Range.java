package com.khjxiaogu.webserver.web.lowlayer;

public class Range {
	long start;
	long end;
	public static final Range ALL=new Range(0,0);
	public Range(long start, long end) {
		super();
		this.start = start;
		this.end = end;
	}
	public boolean isEmpty() {
		return start==0&&end==0;
	}
}
