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
