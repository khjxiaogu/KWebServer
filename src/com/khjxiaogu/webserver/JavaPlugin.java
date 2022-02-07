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
package com.khjxiaogu.webserver;

import com.khjxiaogu.webserver.command.CommandHandler;
import com.khjxiaogu.webserver.command.CommandSender;

public abstract class JavaPlugin implements CommandHandler {

	@Override
	public String getHelp() { return null; }

	@Override
	public boolean dispatchCommand(String msg, CommandSender sender) { return false; }

	@Override
	public String getCommandLabel() { return null; }

	public JavaPlugin() {}

	public void onLoad() {}

	public void onEnable() {}

	public void onClose() {}
}
