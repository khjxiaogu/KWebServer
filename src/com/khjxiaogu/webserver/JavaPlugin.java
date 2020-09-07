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
