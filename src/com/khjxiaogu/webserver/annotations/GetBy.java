package com.khjxiaogu.webserver.annotations;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.khjxiaogu.webserver.wrappers.InAdapter;
import com.khjxiaogu.webserver.wrappers.adapters.NopIn;

@Retention(RUNTIME)
@Target(PARAMETER)
public @interface GetBy {
	public Class<? extends InAdapter> value() default NopIn.class;
}
