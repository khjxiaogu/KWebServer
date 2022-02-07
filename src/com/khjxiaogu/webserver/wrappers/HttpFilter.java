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
