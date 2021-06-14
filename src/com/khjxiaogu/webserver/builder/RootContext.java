package com.khjxiaogu.webserver.builder;

import com.khjxiaogu.webserver.command.CommandDispatcher;
import com.khjxiaogu.webserver.command.CommandExp;
import com.khjxiaogu.webserver.command.CommandExpSplitter.SplittedExp;
import com.khjxiaogu.webserver.command.CommandHandler;
import com.khjxiaogu.webserver.command.CommandSender;
import com.khjxiaogu.webserver.web.CallBack;
import com.khjxiaogu.webserver.web.ContextHandler;
import com.khjxiaogu.webserver.web.ServerProvider;
import com.khjxiaogu.webserver.web.lowlayer.Request;
import com.khjxiaogu.webserver.web.lowlayer.Response;

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
        implements ContextHandler<RootContext<T, S>>, XContext<T, S>, CommandDispatcher,
        ExtendableContext<RootContext<T, S>> {
	private T Intern;
	private S Owner;
	private CommandDispatcher command;

	RootContext(T Intern, S Owner, CommandDispatcher command) {
		this.Intern = Intern;
		this.command = command;
		this.Owner = Owner;
	}

	/**
	 * Register command.<br>
	 * 注册指令
	 *
	 * @return return self<br>
	 *         返回自身
	 */
	public RootContext<T, S> registerCommand() {
		if (Intern instanceof CommandHandler) { add((CommandHandler) Intern); }
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

	@Override
	public CommandDispatcher getDispatcher() { return command; }

	@Override
	public void call(Request req, Response res) { Intern.call(req, res); }
}