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
package com.khjxiaogu.webserver.builder;

import com.khjxiaogu.webserver.command.CommandDispatcher;
import com.khjxiaogu.webserver.command.CommandExp;
import com.khjxiaogu.webserver.command.CommandExpSplitter.SplittedExp;
import com.khjxiaogu.webserver.command.CommandHandler;
import com.khjxiaogu.webserver.command.CommandSender;
import com.khjxiaogu.webserver.loging.SimpleLogger;

public interface OpenedWebServerCreater {

	OpenedWebServerCreater add(CommandHandler ch);

	/**
	 * Gets the internal command dispatcher.<br>
	 * 获取指令处理内部类.
	 *
	 * @return command dispatch<br>
	 *         指令处理类
	 */
	CommandDispatcher getCommands();

	/**
	 * Gets the logger.<br>
	 * 获取日志记录器.
	 *
	 * @return logger<br>
	 *         日志记录器
	 */
	SimpleLogger getLogger();

	/**
	 * Set up a https server.<br>
	 * 开启https服务器，如果没有SSL证书则不生效
	 *
	 * @param port the port<br>
	 *             端口
	 * @return return self <br>
	 *         返回自身
	 * @throws InterruptedException if an interrupted exception occurred.<br>
	 *                              如果interrupted exception发生了
	 */
	OpenedWebServerCreater serverHttps(int port) throws InterruptedException;

	OpenedWebServerCreater serverHttps(String addr, int port) throws InterruptedException;

	/**
	 * Set up a http server.<br>
	 * 开启http服务器
	 *
	 * @param port the port<br>
	 *             端口
	 * @return return self <br>
	 *         返回自身
	 * @throws InterruptedException if an interrupted exception occurred.<br>
	 *                              如果interrupted exception发生了
	 */
	OpenedWebServerCreater serverHttp(int port) throws InterruptedException;

	OpenedWebServerCreater serverHttp(String addr, int port) throws InterruptedException;

	/**
	 * Await servers closed.<br>
	 * 等待服务器关闭
	 *
	 * @return return self <br>
	 *         返回自身
	 */
	OpenedWebServerCreater await();

	/**
	 * Log Info message.<br>
	 * 发送info信息
	 *
	 * @param s the s<br>
	 * @return return self <br>
	 *         返回自身
	 */
	OpenedWebServerCreater info(Object s);

	/**
	 * Read console commands.<br>
	 * 监听后台指令
	 *
	 * @param logger the logger<br>
	 * @return return self <br>
	 *         返回自身
	 */
	OpenedWebServerCreater readConsole(SimpleLogger logger);

	boolean dispatchCommand(String msg, CommandSender sender);

	OpenedWebServerCreater add(String label, CommandExp ch, String help);

	OpenedWebServerCreater add(String label, CommandExp ch);

	OpenedWebServerCreater add(String label, SplittedExp ch);

	OpenedWebServerCreater add(String label, SplittedExp ch, String help);

	WebServerCreater close();

	void shutdown();

	WebServerCreater closeSync() throws InterruptedException;

}