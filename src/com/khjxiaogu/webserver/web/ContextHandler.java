package com.khjxiaogu.webserver.web;

// TODO: Auto-generated Javadoc
/**
 * Class ContextHandler.
 *
 * @author: khjxiaogu file: ContextHandler.java time: 2020年5月8日 提供规则匹配的调用器父类
 */
public interface ContextHandler<T extends ContextHandler<T>> extends CallBack {

	/**
	 * 添加调用规则.<br />
	 *
	 * @param rule 规则<br />
	 * @param ctx  调用<br />
	 * @return 返回自身 <br />
	 */
	public T createContext(String rule, CallBack ctx);

	/**
	 * 添加调用规则.<br />
	 *
	 * @param rule    规则<br />
	 * @param handler 处理类<br />
	 * @return 返回自身 <br />
	 */
	@SuppressWarnings("unchecked")
	public default T createContext(String rule, ServerProvider handler) {
		createContext(rule, handler.getListener());
		return (T) this;
	}

	public T removeContext(String rule);
}
