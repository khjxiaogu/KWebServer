package com.khjxiaogu.webserver.wrappers.inadapters;

import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;
import com.khjxiaogu.webserver.Utils;
import com.khjxiaogu.webserver.web.lowlayer.Request;
import com.khjxiaogu.webserver.wrappers.StaticInAdapter;

public class GsonIn extends StaticInAdapter {
	Gson gs=new Gson();
	@Override
	public Object handle(Request req, Class<?> paramClass) throws Exception {
		return gs.fromJson(new String(Utils.readAll(req.getBody()), StandardCharsets.UTF_8), paramClass);
	}

}
