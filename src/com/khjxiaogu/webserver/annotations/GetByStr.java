package com.khjxiaogu.webserver.annotations;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.khjxiaogu.webserver.wrappers.InAdapter;
import com.khjxiaogu.webserver.wrappers.InStringAdapter;
import com.khjxiaogu.webserver.wrappers.inadapters.NopIn;

@Retention(RUNTIME)
@Target(PARAMETER)
public @interface GetByStr {
	public Class<? extends InStringAdapter> value() default NopIn.class;

	public String param() default "";
}
