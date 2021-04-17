package com.khjxiaogu.webserver.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(METHOD)
@interface Filters {
	public Filter[] value();
}

@Retention(RUNTIME)
@Target(METHOD)
@Repeatable(Filters.class)
public @interface Filter {
	public String value();
}
