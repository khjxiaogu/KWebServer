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

// TODO: Auto-generated Javadoc
/**
 * Interface ServerProvider. 服务提供类
 *
 * @author khjxiaogu file: ServerProvider.java time: 2020年5月8日
 */
public interface ServerProvider {

	/**
	 * Gets the listener callback.<br>
	 * 获取回调函数.
	 *
	 * @return listener callback<br>
	 *         回调函数
	 */
	CallBack getListener();

}