package com.khjxiaogu.webserver.wrappers;

/**
 * Interface InStringAdapter. 必须有一个字符串类型作为参数的构造器
 *
 * @author khjxiaogu file: InStringAdapter.java time: 2020年9月7日
 */
public abstract class InStringAdapter implements InAdapter {
	protected String key;

	public InStringAdapter(String key) { this.key = key; }
}
