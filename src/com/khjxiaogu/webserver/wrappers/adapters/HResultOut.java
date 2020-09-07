package com.khjxiaogu.webserver.wrappers.adapters;

import java.io.File;

import com.khjxiaogu.webserver.web.lowlayer.Response;
import com.khjxiaogu.webserver.wrappers.HResult;
import com.khjxiaogu.webserver.wrappers.HResult.HHttpHeader;
import com.khjxiaogu.webserver.wrappers.OutAdapter;

public class HResultOut implements OutAdapter {

	@Override
	public void handle(HResult result, Response res) {
		if(result==null)return;
		if (result.headers != null)
			for (HHttpHeader hd : result.headers)
				res.setHeader(hd.key, hd.val);
		if (result.body == null)
			res.write(result.code);
		else if (result.body instanceof File)
			res.write(result.code, (File) result.body);
		else if (result.body instanceof byte[])
			res.write(result.code, (byte[]) result.body);
		else
			res.write(result.code, String.valueOf(result.body));
	}

}
