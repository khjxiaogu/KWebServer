package com.khjxiaogu.webserver.annotations;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

@Retention(RUNTIME)
public @interface FilterKV {
	public String key();
	public String value();
}
