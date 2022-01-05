package com.khjxiaogu.webserver.loging;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class LogMessages {
	private static final String BUNDLE_NAME = "com.khjxiaogu.webserver.loging.messages"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	private LogMessages() {}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
