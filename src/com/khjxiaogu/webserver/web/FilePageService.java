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

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;

import com.khjxiaogu.webserver.InternalException;
import com.khjxiaogu.webserver.WebServerException;
import com.khjxiaogu.webserver.loging.SimpleLogger;
import com.khjxiaogu.webserver.web.lowlayer.Request;
import com.khjxiaogu.webserver.web.lowlayer.Response;

// TODO: Auto-generated Javadoc
/**
 * Class FilePageService. 文件系统页面服务，根据文件路径发送网页 /将自动导向到当前目录下/index.html
 *
 * @author khjxiaogu file: FilePageService.java time: 2020年5月8日
 */
public class FilePageService implements CallBack {

	/**
	 * The dest.<br>
	 * 成员 dest.
	 */
	private File dest;

	/**
	 * Instantiates a new FilePageService with a root directory.<br>
	 * 新建一个FilePageService类，设置根目录。<br>
	 *
	 * @param root the root<br>
	 *             根目录
	 */
	public FilePageService(File root) {
		dest = root;

	}

	/**
	 * The logger.<br>
	 * 成员 logger.
	 */
	SimpleLogger logger = new SimpleLogger("页面");

	@Override
	public void call(Request req, Response res) {
		File f = new File(dest, FilePageService.sanitizeUri(req.getCurrentPath()));
		//logger.info(FilePageService.sanitizeUri(req.getCurrentPath()));
		//logger.info(f.getAbsolutePath());
		try {
			
			if (f.isDirectory()) {
				f=new File(f,"index.html");
				if(f.exists()) {
					if(!req.getCurrentPath().endsWith("/")) {
						res.redirect(FilePageService.sanitizeUri(req.getFullpath())+"/");
					}else {
						res.write(200, f);
					}
				}
			}else {
				res.write(200, f);
			}
		} catch (Exception ex) {
			try {
				res.write(500, ex.getMessage().getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
				res.write(500,"Internal Server Error");
				throw new InternalException(e,logger);
			}
			throw new WebServerException(ex,logger);
		}
	}


	/**
	 * Constant INSECURE_URI.<br>
	 * 常量 INSECURE_URI.
	 */
	private static final Pattern INSECURE_URI = Pattern.compile(".*[<>&\"].*");

	/**
	 * Sanitize uri.<br>
	 * 清理并安全化URI，转为操作系统相关URI
	 *
	 * @param uri the uri<br>
	 * @return return sanitized uri <br>
	 *         返回 string
	 */
	private static String sanitizeUri(String uri) {
		/*try {
			uri=Paths.get(new URI(uri)).normalize().toUri().toString();
		} catch (URISyntaxException e) {
		}*/
		if (uri == null || uri.isEmpty() || uri.charAt(0) != '/')
			return "";

		// Convert file separators.
		uri = uri.replace('/', File.separatorChar);

		// Simplistic dumb security check.
		// You will have to do something serious in the production environment.
		if (uri.contains(File.separator + '.') || uri.contains('.' + File.separator) || uri.charAt(0) == '.'
		        || uri.charAt(uri.length() - 1) == '.' || FilePageService.INSECURE_URI.matcher(uri).matches())
			return "";

		// Convert to absolute path.
		return uri.substring(1);
	}
}
