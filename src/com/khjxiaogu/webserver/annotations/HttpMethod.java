package com.khjxiaogu.webserver.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(METHOD)
@interface HttpMethods {
	public HttpMethod[] value();
}

@Retention(RUNTIME)
@Target(METHOD)
@Repeatable(HttpMethods.class)
public @interface HttpMethod {
	public String value();
}
