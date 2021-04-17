package com.khjxiaogu.webserver.builder;

public interface RulableContext<T> {

	/**
	 * Force https.<br>
	 * 强制https访问
	 *
	 * @return return self <br>
	 *         返回自身
	 */
	T forceHttps();

	/**
	 * Force http.<br>
	 * 强制http访问
	 *
	 * @return return self <br>
	 *         返回自身
	 */
	T forceHttp();
	
	/**
	 * Add rule for this context.<br>
	 * 为本上下文对象在父对象添加规则
	 *
	 * @param rule the rule<br>
	 *             规则
	 * @return return self<br>
	 *         返回自身
	 */
	T rule(String rule);

}