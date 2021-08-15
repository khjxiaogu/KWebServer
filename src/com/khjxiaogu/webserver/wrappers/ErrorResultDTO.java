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
