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
import com.khjxiaogu.webserver.web.HostDispatchHandler;
import com.khjxiaogu.webserver.web.lowlayer.Request;
import com.khjxiaogu.webserver.web.lowlayer.Response;

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
        implements CommandDispatcher, Context<T>, RulableContext<CallBackContext<T, S>>, CallBack {
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
	@Override
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
	@Override
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
	@Override
	public CallBackContext<T, S> rule(String rule) {
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

	@Override
	public void call(Request req, Response res) { sec.call(req, res); }

	public CallBackContext<T, S> registerCommand() {
		if (provider instanceof CommandHandler) { add((CommandHandler) provider); }
		return this;
	};
}