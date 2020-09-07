package com.khjxiaogu.webserver;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.netty.buffer.ByteBuf;

public final class Utils {
	public final static String systemUID = "00000000-0000-0000-0000-000000000000";

	public static byte[] readAll(InputStream i) throws IOException {
		ByteArrayOutputStream ba = new ByteArrayOutputStream(16384);
		int nRead;
		byte[] data = new byte[4096];

		try {
			while ((nRead = i.read(data, 0, data.length)) != -1)
				ba.write(data, 0, nRead);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw e;
		}

		return ba.toByteArray();
	}

	public static byte[] readAll(File i) {
		try (FileInputStream fis = new FileInputStream(i)) {
			return Utils.readAll(fis);
		} catch (IOException ignored) {
			// TODO Auto-generated catch block
		}
		return new byte[0];
	}

	public static byte[] readAll(ByteBuf i) throws IOException {
		i.resetReaderIndex();
		byte[] ret = new byte[i.readableBytes()];
		i.readBytes(ret);

		return ret;
	}

	public static Map<String, String> PostToMap(ByteBuf i) {
		try {
			return Utils.queryToMap(URLDecoder.decode(new String(Utils.readAll(i), StandardCharsets.UTF_8), "UTF-8"));
		} catch (IOException e) {}
		return new HashMap<>();
	}

	public static Map<String, String> queryToMap(String query) {
		Map<String, String> result = new HashMap<>();
		if (query == null || query.length() <= 0)
			return result;

		for (String param : query.split("&")) {
			String[] entry = param.split("=");
			if (entry.length > 1)
				result.put(entry[0], entry[1]);
			else
				result.put(entry[0], "");
		}
		return result;
	}

	public static Map<String, String> cookieToMap(String cookie) {
		Map<String, String> result = new HashMap<>();
		if (cookie == null || cookie.length() <= 0)
			return result;

		for (String param : cookie.split(";")) {
			String[] entry = param.split("=");
			if (entry.length > 1)
				result.put(entry[0].trim(), entry[1]);
			else
				result.put(entry[0].trim(), "");
		}
		return result;
	}

	public static long getTime() { return new Date().getTime(); }

	public static String bytesToHex(byte[] hash) {
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < hash.length; i++) {
			String hex = Integer.toHexString(0xff & hash[i]);
			if (hex.length() == 1)
				hexString.append('0');
			hexString.append(hex);
		}
		return hexString.toString();
	}

	// private static FileNameMap fileNameMap = URLConnection.getFileNameMap();
	private static Map<String, String> extMap = new HashMap<>();
	static {
		Utils.extMap.put("tjs", "text/plain");
	}

	public static String SHA256(String data) {
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("SHA-256");

			return Utils.bytesToHex(digest.digest(data.getBytes(StandardCharsets.UTF_8)));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static String getMime(File f) {
		String fn = f.getName();
		String ext = fn.substring(fn.lastIndexOf('.') + 1);
		String mime = Utils.extMap.get(ext);
		if (mime == null)
			try {
				mime = Files.probeContentType(f.toPath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return mime;

	}
}
