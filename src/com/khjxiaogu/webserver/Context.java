package com.khjxiaogu.webserver;

public interface Context<T> {

	/**
	 * Complete definition of current context.<br>
	 * 完成此上下文的定义并返回父对象
	 * 
	 * @return return parent object <br>
	 *         返回父对象
	 */
	T complete();

}