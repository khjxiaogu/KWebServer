package com.khjxiaogu.webserver.wrappers.outadapters;

import com.google.gson.Gson;
import com.khjxiaogu.webserver.web.lowlayer.Response;
import com.khjxiaogu.webserver.wrappers.OutAdapter;
import com.khjxiaogu.webserver.wrappers.ResultDTO;
import com.khjxiaogu.webserver.wrappers.ResultDTO.HHttpHeader;

public class JsonResultDTOOut implements OutAdapter {

	public JsonResultDTOOut() {}

	@Override
	public void handle(ResultDTO result, Response res) {
		if (result.getHeaders() != null)
			for (HHttpHeader hd : result.getHeaders())
				res.setHeader(hd.key, hd.val);
		res.write(result.code,new Gson().toJson(result.getBody()));
	}


}
