package com.khjxiaogu.webserver.wrappers;

import com.khjxiaogu.webserver.web.lowlayer.Request;
import com.khjxiaogu.webserver.web.lowlayer.Response;

// TODO: Auto-generated Javadoc
/**
 * Interface IOWrapper.
 *
 * @author khjxiaogu file: IOWrapper.java time: 2020年8月15日
 */
public interface HttpFilter {

	/**
	 * Handle.<br>
	 *
	 * @param req the req<br>
	 * @param res the res<br>
	 * @param filters the filters<br>
	 * @return false, if no more handle needed<br>
	 *         如果不需要继续传递，返回false。
	 * @throws Exception if an exception occurred.<br>
	 *                   如果exception发生了
	 */
	public boolean handle(Request req, Response res, FilterChain filters) throws Exception;

}
