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
package com.khjxiaogu.webserver.web;

import com.khjxiaogu.webserver.web.lowlayer.Request;
import com.khjxiaogu.webserver.web.lowlayer.Response;

// TODO: Auto-generated Javadoc
/**
 * Interface CallBack. 回调函数接口 抽象的http回调函数，输入了http的公有方法类。 若不对回复进行写入，将返回默认404页面
 *
 * @author khjxiaogu
 * file: CallBack.java time: 2020年5月8日
 */
@FunctionalInterface
public interface CallBack {

	/**
	 * 呼叫回调，提供请求体，回复需要写入回复体。<br>
	 *
	 * @param req 请求体<br>
	 * @param res 回复体<br>
	 */
	public void call(Request req, Response res);
}