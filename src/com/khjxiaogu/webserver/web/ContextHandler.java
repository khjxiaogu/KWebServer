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
 * Class ContextHandler.
 *
 * @author khjxiaogu
 * file: ContextHandler.java
 * time: 2020年5月8日
 * 提供规则匹配的调用器父类
 */
public interface ContextHandler<T extends ContextHandler<T>> extends CallBack {

	/**
	 * 添加调用规则.<br>
	 *
	 * @param rule 规则<br>
	 * @param ctx  调用<br>
	 * @return 返回自身 <br>
	 */
	public T createContext(String rule, CallBack ctx);

	/**
	 * 添加调用规则.<br>
	 *
	 * @param rule    规则<br>
	 * @param handler 处理类<br>
	 * @return 返回自身 <br>
	 */
	@SuppressWarnings("unchecked")
	public default T createContext(String rule, ServerProvider handler) {
		createContext(rule, handler.getListener());
		return (T) this;
	}

	public T removeContext(String rule);
}
