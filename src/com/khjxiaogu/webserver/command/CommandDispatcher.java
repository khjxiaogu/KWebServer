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

import com.khjxiaogu.webserver.command.CommandExpSplitter.SplittedExp;

// TODO: Auto-generated Javadoc
/**
 * Interface CommandDispatcher. 指令分发类接口
 *
 * @author khjxiaogu file: CommandDispatcher.java time: 2020年6月12日
 */
public interface CommandDispatcher extends CommandExp {

	/**
	 * Adds a command handler.<br>
	 * 添加一个指令处理器
	 *
	 * @param ch the handler<br>
	 * @return return self <br>
	 *         返回自身
	 */
	CommandDispatcher add(CommandHandler ch);

	/**
	 * Adds a command function.<br>
	 * 添加一个指令函数
	 *
	 * @param label the command label<br>
	 *              指令标签
	 * @param ch    the command expression<br>
	 *              指令函数
	 * @param help  the help string<br>
	 *              帮助字符串
	 * @return return self <br>
	 *         返回自身
	 */
	CommandDispatcher add(String label, CommandExp ch, String help);

	/**
	 * Adds a command function.<br>
	 * 添加一个指令函数
	 *
	 * @param label the command label<br>
	 *              指令标签
	 * @param ch    the command expression<br>
	 *              指令函数
	 * @return return self <br>
	 *         返回自身
	 */
	CommandDispatcher add(String label, CommandExp ch);

	/**
	 * Adds a command function.<br>
	 * 添加一个指令函数，在执行之前由系统自动分割参数
	 *
	 * @param label the command label<br>
	 *              指令标签
	 * @param ch    the command expression<br>
	 *              指令函数
	 * @return return self <br>
	 *         返回自身
	 */
	CommandDispatcher add(String label, SplittedExp ch);

	/**
	 * Adds a command function.<br>
	 * 添加一个指令函数，在执行之前由系统自动分割参数
	 *
	 * @param label the command label<br>
	 *              指令标签
	 * @param ch    the command expression<br>
	 *              指令函数
	 * @param help  the help string<br>
	 *              帮助字符串
	 * @return return self <br>
	 *         返回自身
	 */
	CommandDispatcher add(String label, SplittedExp ch, String help);

}