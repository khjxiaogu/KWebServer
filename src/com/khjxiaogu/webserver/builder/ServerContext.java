package com.khjxiaogu.webserver.builder;

import com.khjxiaogu.webserver.command.CommandDispatcher;
import com.khjxiaogu.webserver.command.CommandExp;
import com.khjxiaogu.webserver.command.CommandExpSplitter.SplittedExp;
import com.khjxiaogu.webserver.command.CommandHandler;
import com.khjxiaogu.webserver.command.CommandSender;
import com.khjxiaogu.webserver.web.CallBack;
import com.khjxiaogu.webserver.web.ContextHandler;
import com.khjxiaogu.webserver.web.ForceSecureHandler;
import com.khjxiaogu.webserver.web.ForceSecureHandler.Protocol;
import com.khjxiaogu.webserver.web.ServerProvider;
import com.khjxiaogu.webserver.web.lowlayer.Request;
import com.khjxiaogu.webserver.web.lowlayer.Response;

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
        implements XContext<T, S>, CommandDispatcher, RulableContext<ServerContext<T, S>>,
        ExtendableContext<ServerContext<T, S>>, CallBack {
	protected T Intern;
	protected S sup;
	protected CommandDispatcher command;
	protected CallBack sec;

	protected ServerContext(T CtxCls, S Super, CommandDispatcher command) {
		Intern = CtxCls;
		sec = CtxCls;
		sup = Super;
		this.command = command;
	}

	@Override
	public ServerContext<T, S> forceHttps() {
		sec = new ForceSecureHandler(Intern, Protocol.HTTPS).getListener();
		return this;
	}

	@Override
	public ServerContext<T, S> forceHttp() {
		sec = new ForceSecureHandler(Intern, Protocol.HTTP).getListener();
		return this;
	}

	/**
	 * Register command.<br>
	 * 注册命令
	 *
	 * @return return self<br>
	 *         返回自身
	 */
	public ServerContext<T, S> registerCommand() {
		if (Intern instanceof CommandHandler) { add((CommandHandler) Intern); }
		return this;
	}

	@Override
	public ServerContext<T, S> createContext(String rule, CallBack ctx) {
		Intern.createContext(rule, ctx);
		return this;
	}

	@Override
	public ServerContext<T, S> createContext(String rule, ServerProvider ctx) {
		Intern.createContext(rule, ctx);
		return this;
	}

	@Override
	public ServerContext<T, S> rule(String rule) {
		sup.createContext(rule, this);
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

	@Override
	public CommandDispatcher getDispatcher() { return command; }

	@Override
	public void call(Request req, Response res) { sec.call(req, res); }
}