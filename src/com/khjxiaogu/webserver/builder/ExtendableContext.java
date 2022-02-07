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
import java.lang.reflect.InvocationTargetException;

import com.khjxiaogu.webserver.command.CommandDispatcher;
import com.khjxiaogu.webserver.web.CallBack;
import com.khjxiaogu.webserver.web.ContextHandler;
import com.khjxiaogu.webserver.web.FilePageService;
import com.khjxiaogu.webserver.web.HostDispatchHandler;
import com.khjxiaogu.webserver.web.MethodContext;
import com.khjxiaogu.webserver.web.ServerProvider;
import com.khjxiaogu.webserver.web.ServiceClass;
import com.khjxiaogu.webserver.web.URIMatchDispatchHandler;
import com.khjxiaogu.webserver.wrappers.ServiceClassWrapper;

// TODO: Auto-generated Javadoc
/**
 * Interface ExtendableContext.
 *
 * @author khjxiaogu file: ExtendableContext.java time: 2021年4月17日
 * @param <T> the generic type<br>
 *            泛型参数
 */
public interface ExtendableContext<T extends ExtendableContext<T>> extends ContextHandler<T> {

	/**
	 * Gets the command dispatcher of current context.<br>
	 * 获取当前上下文指令处理器.
	 *
	 * @return dispatcher<br>
	 */
	CommandDispatcher getDispatcher();

	/**
	 * Creates the URI dispatch.<br>
	 * 创建根据URI匹配回调的子上下文对象
	 *
	 * @return return created context<br>
	 *         返回创建的对象
	 */
	default ServerContext<URIMatchDispatchHandler, T> createURIDispatch() {
		return createDispatch(new URIMatchDispatchHandler());
	};

	/**
	 * Creates the host dispatch.<br>
	 * 创建根据域名匹配回调的子上下文对象
	 *
	 * @return return created context<br>
	 *         返回创建的对象
	 */
	default ServerContext<HostDispatchHandler, T> createHostDispatch() {
		return createDispatch(new HostDispatchHandler());
	};

	/**
	 * Creates the method dispatch.<br>
	 * 创建根据请求方法匹配回调的子上下文对象
	 *
	 * @return return created context<br>
	 *         返回创建的对象
	 */
	default ServerContext<MethodContext, T> createMethodDispatch() { return createDispatch(new MethodContext()); };

	/**
	 * Creates a file server.<br>
	 * 创建一个文件服务器
	 *
	 * @param root the root path<br>
	 *             根路径
	 * @return return created context<br>
	 *         返回创建的对象
	 */
	default CallBackContext<T, FilePageService> createFilePath(File root) {
		return createDispatch(new FilePageService(root));
	};

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
	default <Y extends ContextHandler<Y>> ServerContext<Y, T> createDispatch(Y dispatcher) {
		return new ServerContext<>(dispatcher, (T) this, this.getDispatcher());
	};

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
	default <Y extends ServerProvider> ProviderContext<Y, T> createDispatch(Y provider) {
		return new ProviderContext<>(provider, (T) this, this.getDispatcher());
	};

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
	 */
	default <Y extends CallBack> CallBackContext<T, Y> createDispatch(Y callback) {
		return new CallBackContext<>(callback, (T) this, this.getDispatcher());
	};

	/**
	 * Creates the dispatch.<br>
	 * 根据服务提供类创建一个子上下文
	 *
	 * @param obj the obj<br>
	 * @return return created context <br>
	 *         返回子上下文
	 */
	default ServerContext<URIMatchDispatchHandler, T> createWrapper(ServiceClass obj) {
		return new WrapperContext<>(new ServiceClassWrapper(obj), (T) this, this.getDispatcher());
	};

	/**
	 * Patch by an patcher.<br>
	 * 使用带自定义规则的服务提供者提供的规则和回调
	 *
	 * @param p the patcher<br>
	 *          服务提供者
	 * @return return self <br>
	 *         返回自身
	 */
	@SuppressWarnings("unchecked")
	default T patchBy(Patcher p) {
		p.patchSite((T) this);
		return (T) this;
	}

	/**
	 * Patch by a service class.<br>
	 * 使用带自定义规则的服务类提供的规则和回调
	 *
	 * @param p the patcher<br>
	 *          服务提供者
	 * @return return self <br>
	 *         返回自身
	 */
	@SuppressWarnings("unchecked")
	default T patchBy(ServiceClass p) {
		new ServiceClassWrapper(p).patchSite((T) this);
		return (T) this;
	}

}