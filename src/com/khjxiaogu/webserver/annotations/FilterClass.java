package com.khjxiaogu.webserver.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.khjxiaogu.webserver.wrappers.HttpFilter;

@Retention(RUNTIME)
@Target(METHOD)
@interface FilterClasses {
	public FilterClass[] value();
}

@Retention(RUNTIME)
@Target(METHOD)
@Repeatable(FilterClasses.class)
public @interface FilterClass {
	public Class<? extends HttpFilter> value();
}
