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
 * Interface CommandSender. 指令发送者
 *
 * @author khjxiaogu file: CommandSender.java time: 2020年6月12日
 */
public interface CommandSender {

	/**
	 * Send message to this sender.<br>
	 * 为当前指令发送者发送信息
	 *
	 * @param msg the message<br>
	 *            信息
	 */
	public void sendMessage(String msg);

	/**
	 * Gets the User ID.<br>
	 * 获取用户ID.
	 *
	 * @return User ID<br>
	 *         用户ID
	 */
	public String getUID();

	/**
	 * Checks for permission.<br>
	 * 检查是否有权限
	 *
	 * @param tag the permission tag<br>
	 *            权限标签
	 * @return true, if has this permission<br>
	 *         如果有权限，返回true。
	 */
	public default boolean hasPermission(String tag) { return true; }
}
