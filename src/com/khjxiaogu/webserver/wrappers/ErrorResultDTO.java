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
package com.khjxiaogu.webserver.wrappers;

import com.khjxiaogu.webserver.WebServerException;

public class ErrorResultDTO extends ResultDTO {
	private WebServerException wsex;
	public WebServerException getException() { return wsex; }

	private void Throw(WebServerException wsex) { this.wsex = wsex; }
	
	private void Throw(Throwable wsex) {
		if(wsex instanceof WebServerException)
			Throw((WebServerException) wsex);
		else
			Throw(new WebServerException(wsex));
	}

	public ErrorResultDTO(Throwable ex) {Throw(ex);}

	public ErrorResultDTO(int code, Object body,Throwable ex) { super(code, body);Throw(ex); }

	public ErrorResultDTO(int code,Throwable ex) { super(code);Throw(ex); }

}
