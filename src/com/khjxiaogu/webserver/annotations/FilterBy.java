package com.khjxiaogu.webserver.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.khjxiaogu.webserver.wrappers.Filter;

@Retention(RUNTIME)
@Target(METHOD)
@interface Wrappers {
	public FilterBy[] value();
}

@Retention(RUNTIME)
@Target(METHOD)
public @interface FilterBy {
	public Class<? extends Filter> value();
}
