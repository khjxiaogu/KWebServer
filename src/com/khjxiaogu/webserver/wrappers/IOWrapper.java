package com.khjxiaogu.webserver.wrappers;

import com.khjxiaogu.webserver.web.lowlayer.Request;
import com.khjxiaogu.webserver.web.lowlayer.Response;

// TODO: Auto-generated Javadoc
/**
 * Interface IOWrapper.
 *
 * @author khjxiaogu file: IOWrapper.java time: 2020年8月15日
 */
public interface IOWrapper {

	/**
	 * Handle.<br>
	 *
	 * @param req the req<br>
	 * @param res the res<br>
	 * @return true, if no more handle needed<br>
	 *         如果不需要继续传递，返回true。
	 * @throws Exception if an exception occurred.<br>
	 *                   如果exception发生了
	 */
	public boolean handle(Request req, Response res) throws Exception;
}
