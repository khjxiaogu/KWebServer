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
package com.khjxiaogu.webserver.web;

import java.util.HashSet;
import java.util.Set;

import com.khjxiaogu.webserver.web.lowlayer.Request;
import com.khjxiaogu.webserver.web.lowlayer.Response;

public class MethodRestrictHandler implements CallBack {
	Set<String> allow = new HashSet<>();
	CallBack orig;

	public MethodRestrictHandler(CallBack orig) { this.orig = orig; }

	public MethodRestrictHandler(ServerProvider crn) { orig = crn.getListener(); }

	public void addMethod(String method) { allow.add(method); }

	@Override
	public void call(Request req, Response res) { if (allow.contains(req.getMethod())) { orig.call(req, res); }else res.write(405); }

}
