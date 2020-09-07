package com.khjxiaogu.webserver.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.khjxiaogu.webserver.web.ForceSecureHandler.Protocol;

@Retention(RUNTIME)
@Target(METHOD)
public @interface ForceProtocol {
	public Protocol value();
}
