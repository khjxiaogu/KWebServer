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
package com.khjxiaogu.webserver.wrappers.outadapters;

import com.google.gson.Gson;
import com.khjxiaogu.webserver.web.lowlayer.Response;
import com.khjxiaogu.webserver.wrappers.ErrorResultDTO;
import com.khjxiaogu.webserver.wrappers.OutAdapter;
import com.khjxiaogu.webserver.wrappers.ResultDTO;
import com.khjxiaogu.webserver.wrappers.ResultDTO.HHttpHeader;

public class JsonResultDTOOut implements OutAdapter {

	public JsonResultDTOOut() {}

	@Override
	public void handle(ResultDTO result, Response res) {
		if (result.getHeaders() != null) {
			for (HHttpHeader hd : result.getHeaders()) { res.setHeader(hd.key, hd.val); }
		}
		res.write(result.code, new Gson().toJson(result.getBody()));
		if(result instanceof ErrorResultDTO) {
			throw ((ErrorResultDTO) result).getException();
		}
	}

}
