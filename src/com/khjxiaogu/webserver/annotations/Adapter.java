package com.khjxiaogu.webserver.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.khjxiaogu.webserver.wrappers.OutAdapter;
import com.khjxiaogu.webserver.wrappers.outadapters.ResultDTOOut;

@Retention(RUNTIME)
@Target(METHOD)
public @interface Adapter {
	public Class<? extends OutAdapter> value() default ResultDTOOut.class;
}
