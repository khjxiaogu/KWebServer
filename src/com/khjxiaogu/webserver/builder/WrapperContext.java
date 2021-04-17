package com.khjxiaogu.webserver.builder;

import com.khjxiaogu.webserver.command.CommandDispatcher;
import com.khjxiaogu.webserver.command.CommandHandler;
import com.khjxiaogu.webserver.web.ContextHandler;
import com.khjxiaogu.webserver.web.ServiceClass;
import com.khjxiaogu.webserver.web.URIMatchDispatchHandler;
import com.khjxiaogu.webserver.wrappers.ServiceClassWrapper;

public class WrapperContext<T extends ContextHandler<T>, S extends ContextHandler<S>>
        extends ServerContext<URIMatchDispatchHandler, S> {

	protected WrapperContext(ServiceClassWrapper CtxCls, S Super, CommandDispatcher command) {
		super(CtxCls, Super, command);
	}

	@Override
	public ServerContext<URIMatchDispatchHandler, S> registerCommand() {
		ServiceClass sc = ((ServiceClassWrapper) Intern).getObject();
		if (sc instanceof CommandHandler) { add((CommandHandler) sc); }
		return this;
	}

}