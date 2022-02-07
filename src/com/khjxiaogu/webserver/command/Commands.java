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

import java.util.HashMap;
import java.util.Map;

import com.khjxiaogu.webserver.command.CommandExpSplitter.SplittedExp;

// TODO: Auto-generated Javadoc
/**
 * Class Commands.
 *
 * @author khjxiaogu file: Commands.java time: 2020年6月12日
 */
public class Commands implements CommandHandler, CommandHelper, CommandDispatcher {
	protected Map<String, CommandExp> commands = new HashMap<>();
	protected String label;

	/**
	 * Instantiates a new Commands.<br>
	 * 新建一个Commands类<br>
	 */
	public Commands() { this(null); }

	/**
	 * Instantiates a new Commands with a label.<br>
	 * 使用一个指令标签新建一个Commands类<br>
	 *
	 * @param label the command label<br>
	 *              指令标签
	 */
	public Commands(String label) { this.label = label; }

	@Override
	public CommandDispatcher add(CommandHandler ch) {
		commands.put(ch.getCommandLabel(), ch);
		return this;
	}

	@Override
	public CommandDispatcher add(String label, CommandExp ch, String help) {
		commands.put(label, new CommandHolder(ch, () -> help));
		return this;
	}

	@Override
	public CommandDispatcher add(String label, CommandExp ch) {
		commands.put(label, ch);
		return this;
	}

	@Override
	public CommandDispatcher add(String label, SplittedExp ch) {
		commands.put(label, new CommandExpSplitter(ch));
		return this;
	}

	@Override
	public CommandDispatcher add(String label, SplittedExp ch, String help) {
		commands.put(label, new CommandHolder(new CommandExpSplitter(ch), () -> help));
		return this;
	}

	@Override
	public boolean dispatchCommand(String msg, CommandSender user) {
		int space = msg.indexOf(' ');
		String label = null;
		String next = "";
		if (space == -1) {
			label = msg;
		} else {
			label = msg.substring(0, space);
			next = msg.substring(space + 1);
		}
		CommandExp ch = commands.get(label);
		if (ch != null)
			return ch.dispatchCommand(next, user);
		else if (label.equals("help")) {
			for (Map.Entry<String, CommandExp> com : commands.entrySet())
				if (com.getValue() instanceof CommandHelper) {
					user.sendMessage(com.getKey() + " " + ((CommandHelper) com.getValue()).getHelp());
				} else {
					user.sendMessage(com.getKey() + " 无帮助信息");
				}
			return true;
		}
		return false;
	}

	/**
	 * List command, split with space.<br>
	 * 获取指令列表，用空格分隔
	 *
	 * @return return list command <br>
	 *         返回字符串指令列表
	 */
	public String listCommand() {
		StringBuilder sb = new StringBuilder();
		for (String s : commands.keySet()) { sb.append(s); sb.append(' '); }
		return sb.toString();
	}

	@Override
	public String getCommandLabel() { return label; }

	@Override
	public String getHelp() { return label + " 的指令，输入\"" + label + " help\"以获取帮助"; }

}