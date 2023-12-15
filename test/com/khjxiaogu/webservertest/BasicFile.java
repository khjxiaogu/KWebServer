package com.khjxiaogu.webservertest;

import java.io.File;

import com.khjxiaogu.webserver.builder.BasicWebServerBuilder;
import com.khjxiaogu.webserver.loging.SimpleLogger;

public class BasicFile {
	static SimpleLogger sl=new SimpleLogger("主要");
	public static void main(String[] args) throws InterruptedException {
		BasicWebServerBuilder.build().createFilePath(new File("./")).readConsole(sl).compile()
		.serverHttp(9999).info("running").info(new File("./").getAbsolutePath())
		.await();
	}
}
