/*
 *
 */
package com.khjxiaogu.webserver.command;

// TODO: Auto-generated Javadoc
/**
 * Interface CommandHandler.
 *
 * @author khjxiaogu file: CommandHandler.java time: 2020年6月12日
 */
public interface CommandHandler extends CommandExp, CommandHelper {

	/**
	 * Gets the command label.<br>
	 * 获取指令标签.
	 *
	 * @return command label<br>
	 *         指令标签
	 */
	String getCommandLabel();
}
