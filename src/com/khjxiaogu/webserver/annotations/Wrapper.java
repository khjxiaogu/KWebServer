package com.khjxiaogu.webserver.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.khjxiaogu.webserver.wrappers.IOWrapper;

@Retention(RUNTIME)
@Target(METHOD)
@interface Wrappers {
	public Wrapper[] value();
}

@Retention(RUNTIME)
@Target(METHOD)
public @interface Wrapper {
	public Class<? extends IOWrapper> value();
}
