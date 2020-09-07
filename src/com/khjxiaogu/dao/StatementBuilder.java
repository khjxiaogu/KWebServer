package com.khjxiaogu.dao;

public interface StatementBuilder {
	boolean execute();
	String getSQL();
}
