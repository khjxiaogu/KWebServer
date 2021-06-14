package com.khjxiaogu.webserver.wrappers;

import java.util.ArrayList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * HTTP结果传输对象.
 *
 * @author khjxiaogu file: ResultDTO.java time: 2020年9月7日
 */
public class ResultDTO {

	/**
	 * The code.<br>
	 * 成员 code.
	 */
	public int code = 200;

	/**
	 * The body.<br>
	 * 成员 body.
	 */
	private Object body = null;

	protected List<HHttpHeader> headers = null;

	public List<HHttpHeader> getHeaders() { return headers; }

	/**
	 * Class HHttpHeader.
	 *
	 * @author khjxiaogu file: ResultDTO.java time: 2020年9月7日
	 */
	public class HHttpHeader {

		/**
		 * The key.<br>
		 * 成员 key.
		 */
		public CharSequence key;

		/**
		 * The val.<br>
		 * 成员 val.
		 */
		public String val;

		/**
		 * Instantiates a new HHttpHeader.<br>
		 * 新建一个HHttpHeader类<br>
		 *
		 * @param key the key<br>
		 * @param val the val<br>
		 */
		public HHttpHeader(CharSequence key, String val) {
			this.key = key;
			this.val = val;
		}
	}

	/**
	 * Instantiates a new ResultDTO with a int object.<br>
	 * 使用一个int新建一个ResultDTO类<br>
	 *
	 * @param code the code<br>
	 */
	public ResultDTO(int code) { this.code = code; }

	/**
	 * Instantiates a new ResultDTO.<br>
	 * 新建一个ResultDTO类<br>
	 *
	 * @param code the code<br>
	 * @param body the body<br>
	 */
	public ResultDTO(int code, Object body) {
		this.code = code;
		setBody(body);
	}

	/**
	 * Write.<br>
	 *
	 * @param code the code<br>
	 * @param body the body<br>
	 */
	public void write(int code, Object body) {
		this.code = code;
		setBody(body);
	}

	/**
	 * Write.<br>
	 *
	 * @param code the code<br>
	 */
	public void write(int code) { this.code = code; }

	/**
	 * Instantiates a new ResultDTO.<br>
	 * 新建一个ResultDTO类<br>
	 */
	public ResultDTO() {}

	/**
	 * Adds the header.<br>
	 *
	 * @param k   the k<br>
	 * @param val the val<br>
	 */
	public void addHeader(CharSequence k, String val) {
		if (headers == null) { headers = new ArrayList<>(); }
		headers.add(new HHttpHeader(k, val));
	}

	public Object getBody() { return body; }

	public void setBody(Object body) { this.body = body; }

	public void setHeader(CharSequence k, String val) { addHeader(k, val); }
	public static ResultDTO redirect(String url) {
		ResultDTO res=new ResultDTO(302);
		res.addHeader("Location",url);
		return res;
	}
}
