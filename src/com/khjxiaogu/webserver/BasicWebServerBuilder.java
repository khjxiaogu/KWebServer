/*
 *
 */
package com.khjxiaogu.webserver;

import java.io.File;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;

import javax.net.ssl.SSLEngine;

import com.khjxiaogu.webserver.command.CommandDispatcher;
import com.khjxiaogu.webserver.command.CommandExp;
import com.khjxiaogu.webserver.command.CommandExpSplitter.SplittedExp;
import com.khjxiaogu.webserver.command.CommandHandler;
import com.khjxiaogu.webserver.command.CommandSender;
import com.khjxiaogu.webserver.command.Commands;
import com.khjxiaogu.webserver.loging.SystemLogger;
import com.khjxiaogu.webserver.web.CallBack;
import com.khjxiaogu.webserver.web.ContextHandler;
import com.khjxiaogu.webserver.web.FilePageService;
import com.khjxiaogu.webserver.web.ForceSecureHandler;
import com.khjxiaogu.webserver.web.ForceSecureHandler.Protocol;
import com.khjxiaogu.webserver.web.HostDispatchHandler;
import com.khjxiaogu.webserver.web.MethodContext;
import com.khjxiaogu.webserver.web.ServerProvider;
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
public class BasicWebServerBuilder implements CommandDispatcher {

	/**
	 * Class ServerContext.
	 *
	 * @author khjxiaogu file: BasicWebServerBuilder.java time: 2020年6月12日
	 * @param <T> the holding type<br>
	 *            内部持有对象的类型
	 * @param <S> the parent type<br>
	 *            父对象的类型
	 */
	public class ServerContext<T extends ContextHandler<T>, S extends ContextHandler<S>>
	        implements ContextHandler<ServerContext<T, S>>, Context<S>, CommandDispatcher {
		private T Intern;
		private S sup;
		private CommandDispatcher command;
		private ServerProvider sec;

		private ServerContext(T CtxCls, S Super, CommandDispatcher command) {
			Intern = CtxCls;
			sec = CtxCls;
			sup = Super;
			this.command = command;
		}

		/**
		 * Force https.<br>
		 * 强制https访问
		 * 
		 * @return return self <br>
		 *         返回自身
		 */
		public ServerContext<T, S> forceHttps() {
			sec = new ForceSecureHandler(Intern.getListener(), Protocol.HTTPS);
			return this;
		}

		/**
		 * Force http.<br>
		 * 强制http访问
		 * 
		 * @return return self <br>
		 *         返回自身
		 */
		public ServerContext<T, S> forceHttp() {
			sec = new ForceSecureHandler(Intern.getListener(), Protocol.HTTP);
			return this;
		}

		/**
		 * Gets the listener,for internal use.<br>
		 * 获取http监听器，仅供内部使用.
		 *
		 * @return listener<br>
		 */
		@Override
		public CallBack getListener() { return Intern.getListener(); }

		/**
		 * Register command.<br>
		 * 注册命令
		 * 
		 * @return return self<br>
		 *         返回自身
		 */
		public ServerContext<T, S> registerCommand() {
			if (Intern instanceof CommandHandler)
				add((CommandHandler) Intern);
			return this;
		}

		/**
		 * Creates a child context with provided callback.<br>
		 * 用提供的回调创建子上下文
		 * 
		 * @param rule the rule<br>
		 *             匹配规则
		 * @param ctx  the callback<br>
		 *             回调
		 * @return return self <br>
		 *         返回自身
		 */
		@Override
		public ServerContext<T, S> createContext(String rule, CallBack ctx) {
			Intern.createContext(rule, ctx);
			return this;
		}

		/**
		 * Creates a child context with provided Sever Provider.<br>
		 * 用提供的服务提供者创建子上下文
		 * 
		 * @param rule the rule<br>
		 *             匹配规则
		 * @param ctx  the provider<br>
		 *             服务提供者
		 * @return return self <br>
		 *         返回自身
		 */
		@Override
		public ServerContext<T, S> createContext(String rule, ServerProvider ctx) {
			Intern.createContext(rule, ctx);
			return this;
		}

		/**
		 * Add rule for this context.<br>
		 * 为本上下文对象在父对象添加规则
		 * 
		 * @param rule the rule<br>
		 *             规则
		 * @return return self<br>
		 *         返回自身
		 */
		public ServerContext<T, S> rule(String rule) {
			sup.createContext(rule, sec);
			return this;
		}

		/**
		 * Patch by an patcher.<br>
		 * 使用带自定义规则的服务提供者提供的规则和回调
		 * 
		 * @param p the patcher<br>
		 *          服务提供者
		 * @return return self <br>
		 *         返回自身
		 */
		public ServerContext<T, S> patchBy(Patcher p) {
			p.patchSite(Intern);
			return this;
		}

		public ServerContext<T, S> patchBy(ServiceClass p) {
			try {
				new ServiceClassWrapper(p).patchSite(Intern);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
			        | InvocationTargetException | NoSuchMethodException | SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return this;
		}

		/**
		 * Creates the URI dispatch.<br>
		 * 创建根据URI匹配回调的子上下文对象
		 * 
		 * @return return created context<br>
		 *         返回创建的对象
		 */
		public ServerContext<URIMatchDispatchHandler, ServerContext<T, S>> createURIDispatch() {
			return createDispatch(new URIMatchDispatchHandler());
		}

		/**
		 * Creates the host dispatch.<br>
		 * 创建根据域名匹配回调的子上下文对象
		 * 
		 * @return return created context<br>
		 *         返回创建的对象
		 */
		public ServerContext<HostDispatchHandler, ServerContext<T, S>> createHostDispatch() {
			return createDispatch(new HostDispatchHandler());
		}

		/**
		 * Creates the method dispatch.<br>
		 * 创建根据请求方法匹配回调的子上下文对象
		 * 
		 * @return return created context<br>
		 *         返回创建的对象
		 */
		public ServerContext<MethodContext, ServerContext<T, S>> createMethodDispatch() {
			return createDispatch(new MethodContext());
		}

		/**
		 * Creates a file server.<br>
		 * 创建一个文件服务器
		 * 
		 * @param root the root path<br>
		 *             根路径
		 * @return return created context<br>
		 *         返回创建的对象
		 */
		public ProviderContext<FilePageService, ServerContext<T, S>> createFilePath(File root) {
			return createDispatch(new FilePageService(root));
		}

		/**
		 * add a dispatch with custom dispatch object.<br>
		 * 根据自定义的上下文处理对象创建一个子上下文
		 * 
		 * @param <Y>        the argument type<br>
		 *                   参数类型
		 * @param dispatcher the dispatch object<br>
		 *                   上下文对象
		 * @return return created dispatch <br>
		 *         返回新的上下文对象
		 */
		public <Y extends ContextHandler<Y>> ServerContext<Y, ServerContext<T, S>> createDispatch(Y dispatcher) {
			return new ServerContext<>(dispatcher, this, command);
		}

		/**
		 * Creates the dispatch.<br>
		 * 根据自定义的服务提供者创建一个子上下文
		 * 
		 * @param <Y>      the argument type<br>
		 *                 参数类型
		 * @param provider the server provider<br>
		 *                 服务提供者
		 * @return return created context <br>
		 *         返回子上下文
		 */
		public <Y extends ServerProvider> ProviderContext<Y, ServerContext<T, S>> createDispatch(Y provider) {
			return new ProviderContext<>(provider, this, command);
		}

		/**
		 * Creates the dispatch.<br>
		 * 根据自定义的回调创建一个子上下文
		 * 
		 * @param <Y>      the argument type<br>
		 *                 参数类型
		 * @param callback the callback<br>
		 *                 回调
		 * @return return created context <br>
		 *         返回子上下文
		 * @deprecated 应该使用ServerProvider
		 */
		@Deprecated
		public <Y extends CallBack> CallBackContext<ServerContext<T, S>, Y> createDispatch(Y callback) {
			return new CallBackContext<>(callback, this, command);
		}

		/**
		 * Complete definition of current context.<br>
		 * 完成此上下文的定义并返回父对象
		 * 
		 * @return return parent object <br>
		 *         返回父对象
		 */
		@Override
		public S complete() { return sup; }

		@Override
		public ServerContext<T, S> add(CommandHandler ch) {
			command.add(ch);
			return this;
		}

		@Override
		public ServerContext<T, S> add(String label, CommandExp ch, String help) {
			command.add(label, ch, help);
			return this;
		}

		@Override
		public ServerContext<T, S> add(String label, CommandExp ch) {
			command.add(label, ch);
			return this;
		}

		@Override
		public ServerContext<T, S> add(String label, SplittedExp ch) {
			command.add(label, ch);
			return this;
		}

		@Override
		public ServerContext<T, S> add(String label, SplittedExp ch, String help) {
			command.add(label, ch, help);
			return this;
		}

		/**
		 * Dispatch command.<br>
		 *
		 * @param msg  the msg<br>
		 * @param user the user<br>
		 * @return true, if <br>
		 *         如果，返回true。
		 */
		@Override
		public boolean dispatchCommand(String msg, CommandSender user) { return command.dispatchCommand(msg, user); }

		@Override
		public ServerContext<T, S> removeContext(String rule) {
			Intern.removeContext(rule);
			return this;
		}

		public ServerContext<URIMatchDispatchHandler, ServerContext<T, S>> createWrapper(ServiceClass obj) {
			try {
				return createDispatch(new ServiceClassWrapper(obj));
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
			        | InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
				return createURIDispatch();
			}
		}
	}

	/**
	 * Class RootContext. 根上下文
	 * 
	 * @author khjxiaogu file: BasicWebServerBuilder.java time: 2020年6月12日
	 * @param <T> the holding type<br>
	 *            内部对象类型
	 * @param <S> the parent type<br>
	 *            父对象类型
	 */
	public class RootContext<T extends ContextHandler<T>, S>
	        implements ContextHandler<RootContext<T, S>>, Context<S>, CommandDispatcher {
		private T Intern;
		private S Owner;
		private CommandDispatcher command;

		private RootContext(T Intern, S owner, CommandDispatcher command) {
			this.Intern = Intern;
			this.command = command;
			Owner = owner;
		}

		@Override
		public CallBack getListener() { return Intern.getListener(); }

		/**
		 * Register command.<br>
		 * 注册指令
		 * 
		 * @return return self<br>
		 *         返回自身
		 */
		public RootContext<T, S> registerCommand() {
			if (Intern instanceof CommandHandler)
				add((CommandHandler) Intern);
			return this;
		}

		/**
		 * Creates the URI dispatch.<br>
		 * 创建根据URI匹配回调的子上下文对象
		 * 
		 * @return return created context<br>
		 *         返回创建的对象
		 */
		public ServerContext<URIMatchDispatchHandler, RootContext<T, S>> createURIDispath() {
			return createDispatch(new URIMatchDispatchHandler());
		}

		/**
		 * Creates the host dispatch.<br>
		 * 创建根据域名匹配回调的子上下文对象
		 * 
		 * @return return created context<br>
		 *         返回创建的对象
		 */
		public ServerContext<HostDispatchHandler, RootContext<T, S>> createHostDispath() {
			return createDispatch(new HostDispatchHandler());
		}

		/**
		 * Creates the method dispatch.<br>
		 * 创建根据请求方法匹配回调的子上下文对象
		 * 
		 * @return return created context<br>
		 *         返回创建的对象
		 */
		public ServerContext<MethodContext, RootContext<T, S>> createMethodDispath() {
			return createDispatch(new MethodContext());
		}

		/**
		 * Creates a file server.<br>
		 * 创建一个文件服务器
		 * 
		 * @param root the root path<br>
		 *             根路径
		 * @return return created context<br>
		 *         返回创建的对象
		 */
		public ProviderContext<FilePageService, RootContext<T, S>> createFilePath(File root) {
			return createDispatch(new FilePageService(root));
		}

		/**
		 * add a dispatch with custom dispatch object.<br>
		 * 根据自定义的上下文处理对象创建一个子上下文
		 * 
		 * @param <Y>        the argument type<br>
		 *                   参数类型
		 * @param dispatcher the dispatch object<br>
		 *                   上下文对象
		 * @return return created dispatch <br>
		 *         返回新的上下文对象
		 */
		public <Y extends ContextHandler<Y>> ServerContext<Y, RootContext<T, S>> createDispatch(Y dispatcher) {
			return new ServerContext<>(dispatcher, this, command);
		}

		/**
		 * Creates the dispatch.<br>
		 * 根据自定义的服务提供者创建一个子上下文
		 * 
		 * @param <Y>      the argument type<br>
		 *                 参数类型
		 * @param provider the server provider<br>
		 *                 服务提供者
		 * @return return created context <br>
		 *         返回子上下文
		 */
		public <Y extends ServerProvider> ProviderContext<Y, RootContext<T, S>> createDispatch(Y provider) {
			return new ProviderContext<>(provider, this, command);
		}

		/**
		 * Creates the dispatch.<br>
		 * 根据自定义的回调创建一个子上下文
		 * 
		 * @param <Y>      the argument type<br>
		 *                 参数类型
		 * @param callback the callback<br>
		 *                 回调
		 * @return return created context <br>
		 *         返回子上下文
		 * @deprecated 应该使用ServerProvider
		 */
		@Deprecated
		public <Y extends CallBack> CallBackContext<RootContext<T, S>, Y> createDispatch(Y callback) {
			return new CallBackContext<>(callback, this, command);
		}

		/**
		 * Complete definition of current context.<br>
		 * 完成此上下文的定义并返回父对象
		 * 
		 * @return return parent object <br>
		 *         返回父对象
		 */
		@Override
		public S complete() { return Owner; }

		@Override
		public RootContext<T, S> add(CommandHandler ch) {
			command.add(ch);
			return this;
		}

		@Override
		public RootContext<T, S> add(String label, CommandExp ch, String help) {
			command.add(label, ch, help);
			return this;
		}

		@Override
		public RootContext<T, S> add(String label, CommandExp ch) {
			command.add(label, ch);
			return this;
		}

		@Override
		public RootContext<T, S> add(String label, SplittedExp ch) {
			command.add(label, ch);
			return this;
		}

		@Override
		public RootContext<T, S> add(String label, SplittedExp ch, String help) {
			command.add(label, ch, help);
			return this;
		}

		@Override
		public boolean dispatchCommand(String msg, CommandSender user) { return command.dispatchCommand(msg, user); }

		@Override
		public RootContext<T, S> createContext(String rule, CallBack ctx) {
			Intern.createContext(rule, ctx);
			return this;
		}

		@Override
		public RootContext<T, S> createContext(String rule, ServerProvider ctx) {
			Intern.createContext(rule, ctx);
			return this;
		}

		@Override
		public RootContext<T, S> removeContext(String rule) {
			Intern.removeContext(rule);
			return this;
		}
	}

	/**
	 * Class ProviderContext. 服务提供者上下文
	 * 
	 * @author khjxiaogu file: BasicWebServerBuilder.java time: 2020年6月12日
	 * @param <S> the generic type<br>
	 *            泛型参数
	 * @param <T> the generic type<br>
	 *            泛型参数
	 */
	public class ProviderContext<S extends ServerProvider, T extends ContextHandler<T>>
	        implements CommandDispatcher, Context<T> {
		private T sup;
		private S provider;
		private CommandDispatcher command;
		private ServerProvider sec;

		private ProviderContext(S provider, T sup, CommandDispatcher command) {
			this.sup = sup;
			this.provider = provider;
			sec = provider;
			this.command = command;
		}

		/**
		 * Add rule for this context.<br>
		 * 为本上下文对象在父对象添加规则
		 * 
		 * @param rule the rule<br>
		 *             规则
		 * @return return self<br>
		 *         返回自身
		 */
		public ProviderContext<S, T> rule(String rule) {
			sup.createContext(rule, sec);
			return this;
		}

		/**
		 * Complete definition of current context.<br>
		 * 完成此上下文的定义并返回父对象
		 * 
		 * @return return parent object <br>
		 *         返回父对象
		 */
		@Override
		public T complete() { return sup; }

		/**
		 * Force https.<br>
		 * 强制https访问
		 * 
		 * @return return self <br>
		 *         返回自身
		 */
		public ProviderContext<S, T> forceHttps() {
			sec = new ForceSecureHandler(provider.getListener(), Protocol.HTTPS);
			return this;
		}

		/**
		 * Force http.<br>
		 * 强制http访问
		 * 
		 * @return return self <br>
		 *         返回自身
		 */
		public ProviderContext<S, T> forceHttp() {
			sec = new ForceSecureHandler(provider.getListener(), Protocol.HTTP);
			return this;
		}

		/**
		 * Register command.<br>
		 * 注册命令
		 * 
		 * @return return self<br>
		 *         返回自身
		 */
		public ProviderContext<S, T> registerCommand() {
			if (provider instanceof CommandHandler)
				add((CommandHandler) provider);
			return this;
		}

		@Override
		public ProviderContext<S, T> add(CommandHandler ch) {
			command.add(ch);
			return this;
		}

		@Override
		public ProviderContext<S, T> add(String label, CommandExp ch, String help) {
			command.add(label, ch, help);
			return this;
		}

		@Override
		public ProviderContext<S, T> add(String label, CommandExp ch) {
			command.add(label, ch);
			return this;
		}

		@Override
		public ProviderContext<S, T> add(String label, SplittedExp ch) {
			command.add(label, ch);
			return this;
		}

		@Override
		public ProviderContext<S, T> add(String label, SplittedExp ch, String help) {
			command.add(label, ch, help);
			return this;
		}

		@Override
		public boolean dispatchCommand(String msg, CommandSender user) { return command.dispatchCommand(msg, user); }
	}

	/**
	 * Class CallBackContext. 回调上下文
	 * 
	 * @author khjxiaogu file: BasicWebServerBuilder.java time: 2020年6月12日
	 * @param <T> the generic type<br>
	 *            泛型参数
	 * @param <S> the generic type<br>
	 *            泛型参数
	 */
	public class CallBackContext<T extends ContextHandler<T>, S extends CallBack>
	        implements CommandDispatcher, Context<T> {
		private T sup;
		private S provider;
		private CommandDispatcher command;
		private CallBack sec;

		/**
		 * Instantiates a new CallBackContext.<br>
		 * 新建一个CallBackContext类<br>
		 *
		 * @param provider the provider<br>
		 * @param sup      the sup<br>
		 * @param command  the command<br>
		 */
		CallBackContext(S provider, T sup, CommandDispatcher command) {
			this.sup = sup;
			this.provider = provider;
			sec = provider;
			this.command = command;
		}

		/**
		 * Force https.<br>
		 * 强制https访问
		 * 
		 * @return return self <br>
		 *         返回自身
		 */
		public CallBackContext<T, S> forceHttps() {
			sec = new ForceSecureHandler(provider, Protocol.HTTPS).getListener();
			return this;
		}

		/**
		 * Force http.<br>
		 * 强制http访问
		 * 
		 * @return return self <br>
		 *         返回自身
		 */
		public CallBackContext<T, S> forceHttp() {
			sec = new ForceSecureHandler(provider, Protocol.HTTP).getListener();
			return this;
		}

		/**
		 * Add rule for this context.<br>
		 * 为本上下文对象在父对象添加规则
		 * 
		 * @param rule the rule<br>
		 *             规则
		 * @return return self<br>
		 *         返回自身
		 */
		public CallBackContext<T, S> rule(String rule) {
			sup.createContext(rule, sec);
			return this;
		}

		/**
		 * Complete definition of current context.<br>
		 * 完成此上下文的定义并返回父对象
		 * 
		 * @return return parent object <br>
		 *         返回父对象
		 */
		@Override
		public T complete() { return sup; }

		@Override
		public CallBackContext<T, S> add(CommandHandler ch) {
			command.add(ch);
			return this;
		}

		@Override
		public CallBackContext<T, S> add(String label, CommandExp ch, String help) {
			command.add(label, ch, help);
			return this;
		}

		@Override
		public CallBackContext<T, S> add(String label, CommandExp ch) {
			command.add(label, ch);
			return this;
		}

		@Override
		public CallBackContext<T, S> add(String label, SplittedExp ch) {
			command.add(label, ch);
			return this;
		}

		@Override
		public CallBackContext<T, S> add(String label, SplittedExp ch, String help) {
			command.add(label, ch, help);
			return this;
		}

		@Override
		public boolean dispatchCommand(String msg, CommandSender user) { return command.dispatchCommand(msg, user); }
	}

	private CommandDispatcher cmds = new Commands();
	private ServerProvider root;
	private SslContext slc;
	private NettyHandlerBridge bridge;// main handler
	private EventLoopGroup bossGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 2 + 1);// the
	                                                                                                             // recommended
	                                                                                                             // best
	                                                                                                             // number
	private EventLoopGroup workerGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 10 + 1);
	private SystemLogger logger = new SystemLogger("服务器");
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
	public CommandDispatcher getCommands() { return cmds; }

	/**
	 * Gets the logger.<br>
	 * 获取日志记录器.
	 *
	 * @return logger<br>
	 *         日志记录器
	 */
	public SystemLogger getLogger() { return logger; }

	private static class ConsoleCommandSender implements CommandSender {
		private PrintStream console;

		public ConsoleCommandSender(PrintStream console) {
			this.console = console;

		}

		@Override
		public void sendMessage(String msg) { console.println(msg); }

		@Override
		public String getUID() { return Utils.systemUID; }

	}

	public BasicWebServerBuilder() {}

	/**
	 * Builds a new BasicServerBuilder.<br>
	 * 创建这个类
	 * 
	 * @return return new BasicServerBuilder <br>
	 *         返回这个类的实例
	 */
	public static BasicWebServerBuilder build() { return new BasicWebServerBuilder(); }

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

	/**
	 * Sets the SSL context.<br>
	 * 设置SSL上下文
	 * 
	 * @param slc the SSL context<br>
	 *            SSL上下文
	 * @return return self <br>
	 *         返回自身
	 */
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
	public BasicWebServerBuilder compile() {
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
	public BasicWebServerBuilder serverHttps(int port) throws InterruptedException {
		if (port == 0)
			return this;
		if (slc != null) {
			NettyHandlerBridge cbr = new NettyHandlerBridge(bridge).setHttps(true);
			opened.add(new ServerBootstrap().group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
			        .childHandler(new ChannelInitializer<SocketChannel>() {
				        @Override
				        public void initChannel(SocketChannel ch) {
					        SSLEngine engine = slc.newEngine(ch.alloc());
					        ch.pipeline().addLast(new SslHandler(engine)).addLast(new HttpServerCodec())
					                .addLast(new HttpContentCompressor()).addLast(new HttpServerKeepAliveHandler())
					                .addLast(new LowestCatcher("HTTPS")).addLast(new HttpObjectAggregator(1024 * 1024))
					                .addLast(new ChunkedWriteHandler()).addLast(new WebSocketServerCompressionHandler())
					                .addLast(cbr);
				        }
			        }).bind("0.0.0.0", port).sync().channel());
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
	public BasicWebServerBuilder serverHttp(int port) throws InterruptedException {
		if (port == 0)
			return this;
		NettyHandlerBridge cbr = new NettyHandlerBridge(bridge).setHttps(false);
		opened.add(new ServerBootstrap().group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
		        .childHandler(new ChannelInitializer<SocketChannel>() {
			        @Override
			        public void initChannel(SocketChannel ch) {
				        ch.pipeline().addLast(new HttpServerCodec()).addLast(new HttpContentCompressor())
				                .addLast(new HttpServerKeepAliveHandler()).addLast(new LowestCatcher("HTTP"))
				                .addLast(new HttpObjectAggregator(1024 * 1024))// max request payload 1m
				                .addLast(new ChunkedWriteHandler()).addLast(new WebSocketServerCompressionHandler())
				                .addLast(cbr);
			        }
		        }).bind("0.0.0.0", port).sync().channel());
		return this;
	}

	/**
	 * Await servers closed.<br>
	 * 等待服务器关闭
	 * 
	 * @return return self <br>
	 *         返回自身
	 */
	public BasicWebServerBuilder await() {
		try {
			opened.newCloseFuture().await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (scan != null)
			scan.close();
		bossGroup.shutdownGracefully();
		workerGroup.shutdownGracefully();
		return this;
	}

	/**
	 * Log Info message.<br>
	 * 发送info信息
	 * 
	 * @param s the s<br>
	 * @return return self <br>
	 *         返回自身
	 */
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
	public BasicWebServerBuilder readConsole(SystemLogger logger) {
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
							if (cmds.dispatchCommand(input, ccs))
								logger.info("执行成功");
							else
								logger.error("执行失败");
					} catch (Throwable t) {
						t.printStackTrace(logger);
					}
				}
				scan.close();
			}
		}.start();
		return this;
	}

	@Override
	public boolean dispatchCommand(String msg, CommandSender sender) { return cmds.dispatchCommand(msg, sender); }

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
