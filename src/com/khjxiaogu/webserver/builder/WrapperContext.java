/**
 * KWebserver
 * Copyright (C) 2021  khjxiaogu
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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