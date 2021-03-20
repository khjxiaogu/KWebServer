package com.khjxiaogu.webserver.wrappers.outadapters;

import java.io.File;

import com.khjxiaogu.webserver.web.lowlayer.Response;
import com.khjxiaogu.webserver.wrappers.ResultDTO;
import com.khjxiaogu.webserver.wrappers.ResultDTO.HHttpHeader;
import com.khjxiaogu.webserver.wrappers.OutAdapter;

public class ResultDTOOut implements OutAdapter {

	@Override
	public void handle(ResultDTO result, Response res) {
		if (result == null)
			return;
		if (result.getHeaders() != null)
			for (HHttpHeader hd : result.getHeaders())
				res.setHeader(hd.key, hd.val);
		if (result.getBody() == null)
			res.write(result.code);
		else if (result.getBody() instanceof File)
			res.write(result.code, (File) result.getBody());
		else if (result.getBody() instanceof byte[])
			res.write(result.code, (byte[]) result.getBody());
		else
			res.write(result.code, String.valueOf(result.getBody()));
	}

}
