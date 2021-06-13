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
