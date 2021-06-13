package com.khjxiaogu.webserver;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSigner;
import java.security.CodeSource;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import com.google.gson.JsonObject;

class WebServerPluginClassLoader extends URLClassLoader {
	private final Map<String, Class<?>> classes = new ConcurrentHashMap<>();

	public final JsonObject description;

	private final File file;

	private final JarFile jar;

	private final Manifest manifest;

	private final URL url;

	private final WebServerPluginLoader loader;

	public final String name;
	static {
		ClassLoader.registerAsParallelCapable();
	}

	public WebServerPluginClassLoader(WebServerPluginLoader loader, ClassLoader cparent, JsonObject desc, String name,
	        File f) throws IOException {
		super(new URL[] { f.toURI().toURL() }, cparent);
		this.name = name;
		this.loader = loader;
		description = desc;
		file = f;
		jar = new JarFile(f);
		manifest = jar.getManifest();
		url = file.toURI().toURL();
	}

	@Override
	public URL getResource(String name) { return findResource(name); }

	@Override
	public Enumeration<URL> getResources(String name) throws IOException { return findResources(name); }

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException { return findClass(name, true); }

	Class<?> findClass(String name, boolean checkGlobal) throws ClassNotFoundException {
		Class<?> result = classes.get(name);
		if (result == null) {
			if (checkGlobal) { result = loader.getClassByName(name, this); }
			if (result == null) {
				byte[] classBytes;
				String path = name.replace('.', '/').concat(".class");
				JarEntry entry = jar.getJarEntry(path);
				if (entry != null) {
					try (InputStream is = jar.getInputStream(entry)) {
						classBytes = Utils.readAll(is);
					} catch (IOException ex) {
						throw new ClassNotFoundException(name, ex);
					}
					int dot = name.lastIndexOf('.');
					if (dot != -1) {
						String pkgName = name.substring(0, dot);
						if (getPackage(pkgName) == null) {
							try {
								if (manifest != null) {
									definePackage(pkgName, manifest, url);
								} else {
									definePackage(pkgName, null, null, null, null, null, null, null);
								}
							} catch (IllegalArgumentException ex) {
								if (getPackage(pkgName) == null)
									throw new IllegalStateException("Cannot find package " + pkgName);
							}
						}
					}
					CodeSigner[] signers = entry.getCodeSigners();
					CodeSource source = new CodeSource(url, signers);
					result = defineClass(name, classBytes, 0, classBytes.length, source);
				}
				if (result == null) { result = super.findClass(name); }
				if (result != null) { loader.setClass(name, result); }
				classes.put(name, result);
			}
		}
		return result;
	}

	@Override
	public void close() throws IOException {
		try {
			super.close();
		} finally {
			jar.close();
		}
	}

	Set<String> getClasses() { return classes.keySet(); }

}
