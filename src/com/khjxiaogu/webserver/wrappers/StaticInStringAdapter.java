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

import com.khjxiaogu.webserver.web.ServiceClass;
import com.khjxiaogu.webserver.web.lowlayer.Request;

/**
 * Interface InStringAdapter. 必须有一个字符串类型作为参数的构造器
 *
 * @author khjxiaogu file: InStringAdapter.java time: 2020年9月7日
 */
public abstract class StaticInStringAdapter extends InStringAdapter {

	public StaticInStringAdapter(String key) { super(key); }

	@Override
	public Object handle(Request req, ServiceClass context) throws Exception { return handle(req); }

	public abstract Object handle(Request req) throws Exception;
}
