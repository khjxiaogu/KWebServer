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
 * Class CommandHolder. 指令控制器，用于同时提供指令执行和帮助信息
 *
 * @author khjxiaogu file: CommandHolder.java time: 2020年6月12日
 */
public class CommandHolder implements CommandExp, CommandHelper {

	/**
	 * The cexp.<br>
	 * 成员 cexp.
	 */
	private CommandExp cexp;

	/**
	 * The chm.<br>
	 * 成员 chm.
	 */
	private CommandHelper chm;

	/**
	 * Instantiates a new CommandHolder.<br>
	 * 新建一个CommandHolder类<br>
	 *
	 * @param cexp the original exp<br>
	 *             原始指令函数
	 * @param chm  the help provider<br>
	 *             帮助提供类
	 */
	public CommandHolder(CommandExp cexp, CommandHelper chm) {
		this.cexp = cexp;
		this.chm = chm;
	}

	@Override
	public boolean dispatchCommand(String msg, CommandSender sender) { return cexp.dispatchCommand(msg, sender); }

	@Override
	public String getHelp() { return chm.getHelp(); }

}
