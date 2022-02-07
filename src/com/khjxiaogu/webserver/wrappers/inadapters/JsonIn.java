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
package com.khjxiaogu.webserver.wrappers.inadapters;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.khjxiaogu.webserver.Utils;
import com.khjxiaogu.webserver.web.lowlayer.Request;
import com.khjxiaogu.webserver.wrappers.StaticInAdapter;

public class JsonIn extends StaticInAdapter {

	@Override
	public Object handle(Request req) throws JsonSyntaxException, IOException {
		return JsonParser.parseString(new String(Utils.readAll(req.getBody()), StandardCharsets.UTF_8));
	}
}
