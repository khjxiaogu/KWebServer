package com.khjxiaogu.webserver.wrappers;

import java.util.ArrayList;
import java.util.List;

public class HResult {
	public int code = 200;
	public Object body = null;
	public List<HHttpHeader> headers = null;

	public class HHttpHeader {
		public CharSequence key;
		public String val;

		public HHttpHeader(CharSequence key, String val) {
			this.key = key;
			this.val = val;
		}
	}

	public HResult(int code) { this.code = code; }

	public HResult(int code, Object body) {
		this.code = code;
		this.body = body;
	}

	public void write(int code, Object body) {
		this.code = code;
		this.body = body;
	}

	public HResult() {}

	public void addHeader(CharSequence k, String val) {
		if (headers == null)
			headers = new ArrayList<>();
		headers.add(new HHttpHeader(k, val));
	}
}
