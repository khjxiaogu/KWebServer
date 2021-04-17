package com.khjxiaogu.webserver.builder;

import com.khjxiaogu.webserver.web.ContextHandler;

public interface Patcher {

	<T extends ContextHandler<T>> T patchSite(T site);

}