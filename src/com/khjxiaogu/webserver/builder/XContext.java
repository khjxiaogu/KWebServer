package com.khjxiaogu.webserver.builder;

import com.khjxiaogu.webserver.web.ContextHandler;

interface XContext<T extends ContextHandler<T>, S> extends Context<S> {
}