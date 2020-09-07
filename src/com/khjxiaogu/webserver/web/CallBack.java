package com.khjxiaogu.webserver.web;

import com.khjxiaogu.webserver.web.lowlayer.Request;
import com.khjxiaogu.webserver.web.lowlayer.Response;

// TODO: Auto-generated Javadoc
/**
 * Interface CallBack. 回调函数接口 抽象的http回调函数，输入了http的公有方法类。 若不对回复进行写入，将返回默认404页面
 * 
 * @author: khjxiaogu file: CallBack.java time: 2020年5月8日
 */
@FunctionalInterface
public interface CallBack {

	/**
	 * 呼叫回调，提供请求体，回复需要写入回复体。<br />
	 *
	 * @param req 请求体<br />
	 * @param res 回复体<br />
	 */
	public void call(Request req, Response res);
}