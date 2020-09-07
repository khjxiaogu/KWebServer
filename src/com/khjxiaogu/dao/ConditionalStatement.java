package com.khjxiaogu.dao;

public interface ConditionalStatement<T extends ConditionalStatement<T>> {
	public T where();
}
