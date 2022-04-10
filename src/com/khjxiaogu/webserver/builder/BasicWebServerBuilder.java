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

import java.io.File;
import java.util.Scanner;

import javax.net.ssl.SSLEngine;

import com.khjxiaogu.webserver.Utils;
import com.khjxiaogu.webserver.WebServerException;
import com.khjxiaogu.webserver.command.CommandDispatcher;
import com.khjxiaogu.webserver.command.CommandExp;
import com.khjxiaogu.webserver.command.CommandExpSplitter.SplittedExp;
import com.khjxiaogu.webserver.command.CommandHandler;
import com.khjxiaogu.webserver.command.CommandSender;
import com.khjxiaogu.webserver.command.Commands;
import com.khjxiaogu.webserver.loging.SimpleLogger;
import com.khjxiaogu.webserver.web.CallBack;
import com.khjxiaogu.webserver.web.ContextHandler;
import com.khjxiaogu.webserver.web.FilePageService;
import com.khjxiaogu.webserver.web.HostDispatchHandler;
import com.khjxiaogu.webserver.web.MethodContext;
import com.khjxiaogu.webserver.web.ServiceClass;
import com.khjxiaogu.webserver.web.URIMatchDispatchHandler;
import com.khjxiaogu.webserver.web.lowlayer.Handler;
import com.khjxiaogu.webserver.web.lowlayer.LowestCatcher;
import com.khjxiaogu.webserver.web.lowlayer.NettyHandlerBridge;
import com.khjxiaogu.webserver.wrappers.ServiceClassWrapper;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpServerKeepAliveHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.concurrent.UnorderedThreadPoolEventExecutor;

// TODO: Auto-generated Javadoc
/**
 * Class BasicWebServerBuilder.
 *
 * @author khjxiaogu file: BasicWebServerBuilder.java time: 2020年6月12日
 */
public class BasicWebServerBuilder implements CommandDispatcher, WebServerCreater {
	private CommandDispatcher cmds = new Commands();
	private CallBack root;
	private SslContext slc;
	private NettyHandlerBridge bridge;// main handler
	private EventLoopGroup bossGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 2 + 1);// the
																													// recommended
																													// best
																													// number
	private EventLoopGroup workerGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 10 + 1);
	private SimpleLogger logger = new SimpleLogger("服务器");
	private File NotFound;
	private Scanner scan;
	private ChannelGroup opened = new DefaultChannelGroup(new UnorderedThreadPoolEventExecutor(2));

	@Override
	public BasicWebServerBuilder add(CommandHandler ch) {
		cmds.add(ch);
		return this;
	}

	/**
	 * Gets the internal command dispatcher.<br>
	 * 获取指令处理内部类.
	 *
	 * @return command dispatch<br>
	 *         指令处理类
	 */
	@Override
	public CommandDispatcher getCommands() {
		return cmds;
	}

	/**
	 * Gets the logger.<br>
	 * 获取日志记录器.
	 *
	 * @return logger<br>
	 *         日志记录器
	 */
	@Override
	public SimpleLogger getLogger() {
		return logger;
	}

	private static class ConsoleCommandSender implements CommandSender {
		private SimpleLogger console;

		public ConsoleCommandSender(SimpleLogger logger) {
			this.console = logger;

		}

		@Override
		public void sendMessage(String msg) {
			console.info(msg);
		}

		@Override
		public String getUID() {
			return Utils.systemUID;
		}

	}

	public BasicWebServerBuilder() {
	}

	/**
	 * Builds a new BasicServerBuilder.<br>
	 * 创建这个类
	 *
	 * @return return new BasicServerBuilder <br>
	 *         返回这个类的实例
	 */
	public static BasicWebServerBuilder build() {
		return new BasicWebServerBuilder();
	}

	/**
	 * Creates a URI root dispatcher.<br>
	 * 创建根据URI匹配的根上下文
	 *
	 * @return return creates the URI root dispatcher<br>
	 *         返回根上下文
	 */
	public RootContext<URIMatchDispatchHandler, BasicWebServerBuilder> createURIRoot() {
		return createRoot(new URIMatchDispatchHandler());
	}

	/**
	 * Creates the host root dispatcher.<br>
	 * 创建根据域名匹配的根上下文
	 *
	 * @return return creates the host root dispatcher<br>
	 *         返回根上下文
	 */
	public RootContext<HostDispatchHandler, BasicWebServerBuilder> createHostRoot() {
		return createRoot(new HostDispatchHandler());
	}

	/**
	 * Creates the method root.<br>
	 * 创建根据请求方法匹配的根上下文
	 *
	 * @return return creates the method root dispatcher<br>
	 *         返回根上下文
	 */
	public RootContext<MethodContext, BasicWebServerBuilder> createMethodRoot() {
		return createRoot(new MethodContext());
	}

	/**
	 * Creates a file server.<br>
	 * 创建一个文件服务器
	 *
	 * @param root the root path<br>
	 *             根路径
	 * @return return self <br>
	 *         返回自身
	 */
	public BasicWebServerBuilder createFilePath(File root) {
		this.root = new FilePageService(root);
		return this;
	}

	/**
	 * Creates the root.<br>
	 * 创建自定义根节点
	 *
	 * @param <Y>        the argument type<br>
	 *                   参数类型
	 * @param dispatcher the dispatcher<br>
	 *                   自定义处理器
	 * @return return the created root <br>
	 *         返回根上下文
	 */
	public <Y extends ContextHandler<Y>> RootContext<Y, BasicWebServerBuilder> createRoot(Y dispatcher) {
		RootContext<Y, BasicWebServerBuilder> rt = new RootContext<>(dispatcher, this, cmds);
		root = rt;
		return rt;
	}

	public RootContext<URIMatchDispatchHandler, BasicWebServerBuilder> createWrapperRoot(ServiceClass obj) {
		RootContext<URIMatchDispatchHandler, BasicWebServerBuilder> rt = new RootContext<>(new ServiceClassWrapper(obj),
				this, cmds);
		root = rt;
		return rt;
	}

	/**
	 * Sets the SSL context.<br>
	 * 设置SSL上下文
	 *
	 * @param slc the SSL context<br>
	 *            SSL上下文
	 * @return return self <br>
	 *         返回自身
	 */
	@Override
	public BasicWebServerBuilder setSSL(SslContext slc) {
		this.slc = slc;
		return this;
	}

	/**
	 * Sets the not found page.<br>
	 * 设置404页面文件
	 *
	 * @param f the file<br>
	 *          页面文件
	 * @return return self <br>
	 *         返回自身
	 */
	public BasicWebServerBuilder setNotFound(File f) {
		NotFound = f;
		return this;
	}

	/**
	 * Compile the whole server and ready to open.<br>
	 * 创建服务器对象并且准备使用
	 *
	 * @return return self <br>
	 *         返回自身
	 */
	public WebServerCreater compile() {
		bridge = new NettyHandlerBridge().setHandler(new Handler(root)).setNotFound(NotFound);
		return this;
	}

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
	@Override
	public BasicWebServerBuilder serverHttps(int port) throws InterruptedException {
		return serverHttps("0.0.0.0", port);
	}

	/*
	 * private static final UpgradeCodecFactory upgradeCodecFactory = new
	 * UpgradeCodecFactory() {
	 *
	 * @SuppressWarnings("resource")
	 *
	 * @Override public UpgradeCodec newUpgradeCodec(CharSequence protocol) { if
	 * (AsciiString.contentEquals(Http2CodecUtil.HTTP_UPGRADE_PROTOCOL_NAME,
	 * protocol)) { Http2Connection h2c = new DefaultHttp2Connection(true);
	 * Http2ConnectionEncoder h2e = new DefaultHttp2ConnectionEncoder(h2c, new
	 * DefaultHttp2FrameWriter()); return new Http2ServerUpgradeCodec( new
	 * Http2ConnectionHandlerBuilder() .codec(new DefaultHttp2ConnectionDecoder(h2c,
	 * h2e, new DefaultHttp2FrameReader()), h2e) .frameListener(new
	 * InboundHttp2ToHttpAdapterBuilder(h2c) .maxContentLength(1024 * 1024) .build()
	 * ).build()); } return null; } };
	 */

	@Override
	public BasicWebServerBuilder serverHttps(String addr, int port) throws InterruptedException {
		if (port == 0)
			return this;
		if (slc != null) {
			NettyHandlerBridge cbr = new NettyHandlerBridge(bridge).setHttps(true);
			opened.add(new ServerBootstrap().group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						public void initChannel(SocketChannel ch) {
							SSLEngine engine = slc.newEngine(ch.alloc());
							// engine.setEnabledProtocols(new String[] { "TLSv1.2" });
							ch.pipeline().addLast(new SslHandler(engine)).addLast(new HttpServerCodec())
									.addLast("comp", new HttpContentCompressor())
									.addLast(new HttpServerKeepAliveHandler()).addLast(new LowestCatcher("HTTPS"))
									.addLast(new HttpObjectAggregator(1024 * 1024)).addLast("cw",new ChunkedWriteHandler())
									.addLast(new WebSocketServerCompressionHandler()).addLast(cbr);
						}
					}).bind(addr, port).sync().channel());
		}
		return this;
	}

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
	@Override
	public BasicWebServerBuilder serverHttp(int port) throws InterruptedException {
		return serverHttp("0.0.0.0", port);
	}

	@Override
	public BasicWebServerBuilder serverHttp(String addr, int port) throws InterruptedException {
		if (port == 0)
			return this;
		NettyHandlerBridge cbr = new NettyHandlerBridge(bridge).setHttps(false);
		opened.add(new ServerBootstrap().group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					public void initChannel(SocketChannel ch) {
						ch.pipeline().addLast("b", new HttpServerCodec()).addLast("comp", new HttpContentCompressor())
								.addLast(new HttpServerKeepAliveHandler()).addLast(new LowestCatcher("HTTP"))
								.addLast(new HttpObjectAggregator(1024 * 1024)).addLast("cw",new ChunkedWriteHandler())
								.addLast(new WebSocketServerCompressionHandler()).addLast(cbr);
					}
				}).bind(addr, port).sync().channel());
		return this;
	}

	@Override
	public WebServerCreater close() {
		opened.close();
		return this;
	}

	@Override
	public WebServerCreater closeSync() {
		opened.close();
		return this;
	}

	/**
	 * Await servers closed.<br>
	 * 等待服务器关闭
	 *
	 * @return return self <br>
	 *         返回自身
	 */
	@Override
	public BasicWebServerBuilder await() {
		try {
			opened.newCloseFuture().await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			throw new WebServerException(e, logger);
		}
		return this;
	}

	@Override
	public void shutdown() {
		if (scan != null) {
			scan.close();
		}
		bossGroup.shutdownGracefully();
		workerGroup.shutdownGracefully();
	}

	/**
	 * Log Info message.<br>
	 * 发送info信息
	 *
	 * @param s the s<br>
	 * @return return self <br>
	 *         返回自身
	 */
	@Override
	public BasicWebServerBuilder info(Object s) {
		logger.info(s);
		return this;
	}

	/**
	 * Read console commands.<br>
	 * 监听后台指令
	 *
	 * @return return self <br>
	 *         返回自身
	 */
	@Override
	public BasicWebServerBuilder readConsole(SimpleLogger logger) {
		ConsoleCommandSender ccs = new ConsoleCommandSender(logger);
		scan = new Scanner(System.in);
		scan.useDelimiter("[\r\n]");
		new Thread() {
			@Override
			public void run() {
				while (scan.hasNext()) {
					String input = scan.next();
					input = input.replaceAll("[\r\n]+", "");
					try {
						if (input.length() > 0)
							if (cmds.dispatchCommand(input, ccs)) {
								logger.info("执行成功");
							} else {
								logger.error("执行失败");
							}
					} catch (Throwable t) {
						logger.printStackTrace(t);
					}
				}
				scan.close();
			}
		}.start();
		return this;
	}

	@Override
	public boolean dispatchCommand(String msg, CommandSender sender) {
		return cmds.dispatchCommand(msg, sender);
	}

	@Override
	public BasicWebServerBuilder add(String label, CommandExp ch, String help) {
		cmds.add(label, ch, help);
		return this;
	}

	@Override
	public BasicWebServerBuilder add(String label, CommandExp ch) {
		cmds.add(label, ch);
		return this;
	}

	@Override
	public BasicWebServerBuilder add(String label, SplittedExp ch) {
		cmds.add(label, ch);
		return this;
	}

	@Override
	public BasicWebServerBuilder add(String label, SplittedExp ch, String help) {
		cmds.add(label, ch, help);
		return this;
	}
}
