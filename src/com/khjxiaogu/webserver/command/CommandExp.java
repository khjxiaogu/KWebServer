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
 * Interface CommandExp. 指令函数接口
 *
 * @author khjxiaogu file: CommandExp.java time: 2020年6月12日
 */
@FunctionalInterface
public interface CommandExp {

	/**
	 * Dispatch or execute command.<br>
	 * 分发或者执行指令
	 *
	 * @param msg    command arguments<br>
	 *               指令参数
	 * @param sender the command sender<br>
	 *               指令发送者
	 * @return true, if command found and executed successfully<br>
	 *         如果执行成功，返回true。
	 */
	public boolean dispatchCommand(String msg, CommandSender sender);
}
