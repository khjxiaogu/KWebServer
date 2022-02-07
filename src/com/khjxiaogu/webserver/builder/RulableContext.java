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
package com.khjxiaogu.webserver.builder;

public interface RulableContext<T> {

	/**
	 * Force https.<br>
	 * 强制https访问
	 *
	 * @return return self <br>
	 *         返回自身
	 */
	T forceHttps();

	/**
	 * Force http.<br>
	 * 强制http访问
	 *
	 * @return return self <br>
	 *         返回自身
	 */
	T forceHttp();

	/**
	 * Add rule for this context.<br>
	 * 为本上下文对象在父对象添加规则
	 *
	 * @param rule the rule<br>
	 *             规则
	 * @return return self<br>
	 *         返回自身
	 */
	T rule(String rule);

}