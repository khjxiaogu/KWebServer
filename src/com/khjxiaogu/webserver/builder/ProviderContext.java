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
import com.khjxiaogu.webserver.web.ContextHandler;
import com.khjxiaogu.webserver.web.ForceSecureHandler;
import com.khjxiaogu.webserver.web.ForceSecureHandler.Protocol;
import com.khjxiaogu.webserver.web.ServerProvider;

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
        implements CommandDispatcher, Context<T>, RulableContext<ProviderContext<S, T>> {
	private T sup;
	private S provider;
	private CommandDispatcher command;
	private ServerProvider sec;

	protected ProviderContext(S provider, T sup, CommandDispatcher command) {
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
	@Override
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
	@Override
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
	@Override
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
		if (provider instanceof CommandHandler) { add((CommandHandler) provider); }
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