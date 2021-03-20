package com.khjxiaogu.dao;

public interface InputStatement<T> extends StatementBuilder {

	T set(String key, Object val);

}
