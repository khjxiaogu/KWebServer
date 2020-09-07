package com.khjxiaogu.webserver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class WebServerPluginLoader {

	public WebServerPluginLoader() {}

	private final Pattern[] fileFilters = new Pattern[] { Pattern.compile("\\.jar$") };

	private final Map<String, Class<?>> classes = new ConcurrentHashMap<>();

	private final Map<String, ReentrantReadWriteLock> classLoadLock = new ConcurrentHashMap<>();

	private final Map<String, Integer> classLoadLockCount = new ConcurrentHashMap<>();

	private final List<WebServerPluginClassLoader> loaders = new CopyOnWriteArrayList<>();

	public WebServerPluginClassLoader loadPlugin(File file) throws FileNotFoundException {
		JsonElement description = getPluginDescription(file);
		WebServerPluginClassLoader loader = null;
		String name = description.getAsJsonObject().get("name").getAsString();
		if (!file.exists())
			throw new FileNotFoundException(file.getPath() + " does not exist");
		description = getPluginDescription(file);
		try {
			loader = new WebServerPluginClassLoader(this, getClass().getClassLoader(), description.getAsJsonObject(),
			        name, file);
		} catch (IOException ignored) {}
		this.loaders.add(loader);
		return loader;
	}

	public JsonElement getPluginDescription(File file) {
		JarFile jar = null;
		InputStream stream = null;
		try {
			jar = new JarFile(file);
			JarEntry entry = jar.getJarEntry("plugin.json");
			if (entry == null)
				throw new FileNotFoundException("Jar does not contain plugin.json");
			stream = jar.getInputStream(entry);
			return JsonParser.parseReader(new InputStreamReader(stream));
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		} finally {
			if (jar != null)
				try {
					jar.close();
				} catch (IOException iOException) {}
			if (stream != null)
				try {
					stream.close();
				} catch (IOException iOException) {}
		}
	}

	public Pattern[] getPluginFileFilters() { return this.fileFilters.clone(); }

	Class<?> getClassByName(String name) { return getClassByName(name, null); }

	Class<?> getClassByName(String name, WebServerPluginClassLoader requester) {
		ReentrantReadWriteLock lock;
		Class<?> cachedClass = this.classes.get(name);
		if (cachedClass != null)
			return cachedClass;
		synchronized (this.classLoadLock) {
			lock = this.classLoadLock.computeIfAbsent(name, x -> new ReentrantReadWriteLock());
			this.classLoadLockCount.compute(name, (x, prev) -> prev != null ? prev.intValue() + 1 : 1);
		}
		lock.writeLock().lock();
		try {
			if (requester != null) {
				try {
					cachedClass = requester.findClass(name, false);
				} catch (ClassNotFoundException classNotFoundException) {}
				if (cachedClass != null)
					return cachedClass;
			}
			cachedClass = this.classes.get(name);
			if (cachedClass != null)
				return cachedClass;
			for (WebServerPluginClassLoader loader : this.loaders) {
				try {
					cachedClass = loader.findClass(name, false);
				} catch (ClassNotFoundException classNotFoundException) {}
				if (cachedClass != null)
					return cachedClass;
			}
		} finally {
			synchronized (this.classLoadLock) {
				lock.writeLock().unlock();
				if (this.classLoadLockCount.get(name).intValue() == 1) {
					this.classLoadLock.remove(name);
					this.classLoadLockCount.remove(name);
				} else
					this.classLoadLockCount.compute(name, (x, prev) -> prev.intValue() - 1);
			}
		}
		return null;
	}

	void setClass(String name, Class<?> clazz) {
		if (!this.classes.containsKey(name))
			this.classes.put(name, clazz);
	}

	private void removeClass(String name) { this.classes.remove(name); }

	public void disablePlugin(WebServerPluginClassLoader plugin) { disablePlugin(plugin, false); }

	public void disablePlugin(WebServerPluginClassLoader plugin, boolean closeClassloader) {
		this.loaders.remove(plugin);
		Set<String> names = plugin.getClasses();
		for (String name : names)
			removeClass(name);
		try {
			if (closeClassloader)
				plugin.close();
		} catch (IOException e) {}
	}
}
