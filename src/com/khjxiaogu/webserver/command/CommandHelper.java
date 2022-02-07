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
package com.khjxiaogu.webserver.command;

// TODO: Auto-generated Javadoc
/**
 * Interface CommandHelper. 可以提供指令帮助的接口
 *
 * @author khjxiaogu file: CommandHelper.java time: 2020年6月12日
 */
@FunctionalInterface
public interface CommandHelper {

	/**
	 * Gets the help string.<br>
	 * 获取帮助信息
	 *
	 * @return help string<br>
	 *         帮助信息
	 */
	public String getHelp();
}
