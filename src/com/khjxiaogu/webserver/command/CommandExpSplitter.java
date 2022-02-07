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
 * Class CommandExpSplitter. 指令分割类
 *
 * @author khjxiaogu file: CommandExpSplitter.java time: 2020年6月12日
 */
public class CommandExpSplitter implements CommandExp {

	/**
	 * Interface SplittedExp.
	 *
	 * @author khjxiaogu file: CommandExpSplitter.java time: 2020年6月12日
	 */
	@FunctionalInterface
	public static interface SplittedExp {

		/**
		 * Execute.<br>
		 * 执行指令
		 *
		 * @param msg    the command arguments<br>
		 *               指令参数
		 * @param sender the command sender<br>
		 *               指令执行者
		 * @param c      reserved<br>
		 *               预留
		 * @return true, if <br>
		 *         如果，返回true。
		 */
		boolean execute(String[] msg, CommandSender sender, int c);
	}

	/**
	 * The exp.<br>
	 * 成员 exp.
	 */
	SplittedExp exp;

	/**
	 * Instantiates a new CommandExpSplitter with a SplittedExp object.<br>
	 * 使用一个SplittedExp新建一个CommandExpSplitter类<br>
	 *
	 * @param exp the exp<br>
	 */
	public CommandExpSplitter(SplittedExp exp) { this.exp = exp; }

	@Override
	public boolean dispatchCommand(String msg, CommandSender sender) { return exp.execute(msg.split(" "), sender, 0); }

}
