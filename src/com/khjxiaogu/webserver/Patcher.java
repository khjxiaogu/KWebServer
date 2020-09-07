package com.khjxiaogu.webserver;

import com.khjxiaogu.webserver.web.ContextHandler;

public interface Patcher {

	<T extends ContextHandler<T>> T patchSite(T site);

}