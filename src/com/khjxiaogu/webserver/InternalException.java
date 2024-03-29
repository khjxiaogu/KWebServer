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
package com.khjxiaogu.webserver;

import com.khjxiaogu.webserver.loging.SimpleLogger;

public class InternalException extends WebServerException {

	public InternalException() { super(); }

	public InternalException(String message, Throwable cause) { super(message, cause); }

	public InternalException(String message) { super(message); }

	public InternalException(Throwable cause) { super(cause); }

	public InternalException(Throwable cause, SimpleLogger logger) { super(cause, logger); }

	public InternalException(Throwable cause, String message, SimpleLogger logger) {
		super(cause, message, logger);
	}
}
