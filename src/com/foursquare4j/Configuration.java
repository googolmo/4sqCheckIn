/**
 * 
 */
package com.foursquare4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.AccessControlException;
import java.util.Properties;

/**
 * @author googolmo
 * 
 */
public class Configuration {

	private static Properties defaultProperty;

	static {
		init();
	}

	static void init() {
		defaultProperty = new Properties();
		//defaultProperty.setProperty("4sq.debug", "false");
		defaultProperty.setProperty("4sq.debug", "true");
		defaultProperty.setProperty("4sq.source", "4sq");
		// defaultProperty.setProperty("4sq.clientVersion","");
		defaultProperty.setProperty("4sq.clientURL",
				"http://sandin.tk/4sq.xml");
		defaultProperty.setProperty("4sq.http.userAgent",
				"4sq 2.0");
		// defaultProperty.setProperty("4sq.user","");
		// defaultProperty.setProperty("4sq.password","");
		defaultProperty.setProperty("4sq.http.useSSL", "false");
		// defaultProperty.setProperty("4sq.http.proxyHost","");
		defaultProperty.setProperty("4sq.http.proxyHost.fallback",
				"http.proxyHost");
		// defaultProperty.setProperty("4sq.http.proxyUser","");
		// defaultProperty.setProperty("4sq.http.proxyPassword","");
		// defaultProperty.setProperty("4sq.http.proxyPort","");
		defaultProperty.setProperty("4sq.http.proxyPort.fallback",
				"http.proxyPort");
		defaultProperty.setProperty("4sq.http.connectionTimeout",
				"20000");
		defaultProperty.setProperty("4sq.http.readTimeout", "120000");
		defaultProperty.setProperty("4sq.http.retryCount", "3");
		defaultProperty.setProperty("4sq.http.retryIntervalSecs", "10");
		defaultProperty.setProperty("4sq.oauth.consumerKey","f74f19ee9c0dabc98fcace2a9c32af98"); // your appKey
		defaultProperty.setProperty("4sq.oauth.consumerSecret","fb4fca4907cb04660f4b81cca3a71540"); // your secret
		defaultProperty.setProperty("4sq.oauth.baseUrl","https://api.foursquare.com/oauth");
		defaultProperty.setProperty("4sq.async.numThreads", "1");
		defaultProperty.setProperty("4sq.clientVersion", "1.0");
		try {
			// Android platform should have dalvik.system.VMRuntime in the
			// classpath.
			// @see
			// http://developer.android.com/reference/dalvik/system/VMRuntime.html
			Class.forName("dalvik.system.VMRuntime");
			defaultProperty.setProperty("4sq.dalvik", "true");
		} catch (ClassNotFoundException cnfe) {
			defaultProperty.setProperty("4sq.dalvik", "false");
		}
		DALVIK = getBoolean("4sq.dalvik");
		String t4jProps = "4sq.properties";
		boolean loaded = loadProperties(defaultProperty, "."
				+ File.separatorChar + t4jProps)
				|| loadProperties(
						defaultProperty,
						Configuration.class.getResourceAsStream("/WEB-INF/"
								+ t4jProps))
				|| loadProperties(defaultProperty,
						Configuration.class.getResourceAsStream("/" + t4jProps));
	}

	private static boolean loadProperties(Properties props, String path) {
		try {
			File file = new File(path);
			if (file.exists() && file.isFile()) {
				props.load(new FileInputStream(file));
				return true;
			}
		} catch (Exception ignore) {
		}
		return false;
	}

	private static boolean loadProperties(Properties props, InputStream is) {
		try {
			props.load(is);
			return true;
		} catch (Exception ignore) {
		}
		return false;
	}

	private static boolean DALVIK;

	public static boolean isDalvik() {
		return DALVIK;
	}

	public static boolean useSSL() {
		return getBoolean("4sq.http.useSSL");
	}

	public static String getScheme() {
		return useSSL() ? "https://" : "http://";
	}

	public static String getCilentVersion() {
		return getProperty("4sq.clientVersion");
	}

	public static String getCilentVersion(String clientVersion) {
		return getProperty("4sq.clientVersion", clientVersion);
	}

	public static String getSource() {
		return getProperty("4sq.source");
	}

	public static String getSource(String source) {
		return getProperty("4sq.source", source);
	}

	public static String getProxyHost() {
		return getProperty("4sq.http.proxyHost");
	}

	public static String getProxyHost(String proxyHost) {
		return getProperty("4sq.http.proxyHost", proxyHost);
	}

	public static String getProxyUser() {
		return getProperty("4sq.http.proxyUser");
	}

	public static String getProxyUser(String user) {
		return getProperty("4sq.http.proxyUser", user);
	}

	public static String getClientURL() {
		return getProperty("4sq.clientURL");
	}

	public static String getClientURL(String clientURL) {
		return getProperty("4sq.clientURL", clientURL);
	}

	public static String getProxyPassword() {
		return getProperty("4sq.http.proxyPassword");
	}

	public static String getProxyPassword(String password) {
		return getProperty("4sq.http.proxyPassword", password);
	}

	public static int getProxyPort() {
		return getIntProperty("4sq.http.proxyPort");
	}

	public static int getProxyPort(int port) {
		return getIntProperty("4sq.http.proxyPort", port);
	}

	public static int getConnectionTimeout() {
		return getIntProperty("4sq.http.connectionTimeout");
	}

	public static int getConnectionTimeout(int connectionTimeout) {
		return getIntProperty("4sq.http.connectionTimeout",
				connectionTimeout);
	}

	public static int getReadTimeout() {
		return getIntProperty("4sq.http.readTimeout");
	}

	public static int getReadTimeout(int readTimeout) {
		return getIntProperty("4sq.http.readTimeout", readTimeout);
	}

	public static int getRetryCount() {
		return getIntProperty("4sq.http.retryCount");
	}

	public static int getRetryCount(int retryCount) {
		return getIntProperty("4sq.http.retryCount", retryCount);
	}

	public static int getRetryIntervalSecs() {
		return getIntProperty("4sq.http.retryIntervalSecs");
	}

	public static int getRetryIntervalSecs(int retryIntervalSecs) {
		return getIntProperty("4sq.http.retryIntervalSecs",
				retryIntervalSecs);
	}

	public static String getUser() {
		return getProperty("4sq.user");
	}

	public static String getUser(String userId) {
		return getProperty("4sq.user", userId);
	}

	public static String getPassword() {
		return getProperty("4sq.password");
	}

	public static String getPassword(String password) {
		return getProperty("4sq.password", password);
	}

	public static String getUserAgent() {
		return getProperty("4sq.http.userAgent");
	}

	public static String getUserAgent(String userAgent) {
		return getProperty("4sq.http.userAgent", userAgent);
	}

	public static String getOAuthConsumerKey() {
		return getProperty("4sq.oauth.consumerKey");
	}

	public static String getOAuthConsumerKey(String consumerKey) {
		return getProperty("4sq.oauth.consumerKey", consumerKey);
	}

	public static String getOAuthConsumerSecret() {
		return getProperty("4sq.oauth.consumerSecret");
	}

	public static String getOAuthConsumerSecret(String consumerSecret) {
		return getProperty("4sq.oauth.consumerSecret", consumerSecret);
	}
	
	public static String getOAuthBaseUrl() {
		return getProperty("4sq.oauth.baseUrl");
	}

	public static boolean getBoolean(String name) {
		String value = getProperty(name);
		return Boolean.valueOf(value);
	}

	public static int getIntProperty(String name) {
		String value = getProperty(name);
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException nfe) {
			return -1;
		}
	}

	public static int getIntProperty(String name, int fallbackValue) {
		String value = getProperty(name, String.valueOf(fallbackValue));
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException nfe) {
			return -1;
		}
	}

	public static long getLongProperty(String name) {
		String value = getProperty(name);
		try {
			return Long.parseLong(value);
		} catch (NumberFormatException nfe) {
			return -1;
		}
	}

	public static String getProperty(String name) {
		return getProperty(name, null);
	}

	public static String getProperty(String name, String fallbackValue) {
		String value;
		try {
			value = System.getProperty(name, fallbackValue);
			if (null == value) {
				value = defaultProperty.getProperty(name);
			}
			if (null == value) {
				String fallback = defaultProperty.getProperty(name
						+ ".fallback");
				if (null != fallback) {
					value = System.getProperty(fallback);
				}
			}
		} catch (AccessControlException ace) {
			// Unsigned applet cannot access System properties
			value = fallbackValue;
		}
		return replace(value);
	}

	private static String replace(String value) {
		if (null == value) {
			return value;
		}
		String newValue = value;
		int openBrace = 0;
		if (-1 != (openBrace = value.indexOf("{", openBrace))) {
			int closeBrace = value.indexOf("}", openBrace);
			if (closeBrace > (openBrace + 1)) {
				String name = value.substring(openBrace + 1, closeBrace);
				if (name.length() > 0) {
					newValue = value.substring(0, openBrace)
							+ getProperty(name)
							+ value.substring(closeBrace + 1);

				}
			}
		}
		if (newValue.equals(value)) {
			return value;
		} else {
			return replace(newValue);
		}
	}

	public static int getNumberOfAsyncThreads() {
		return getIntProperty("4sq.async.numThreads");
	}

	public static boolean getDebug() {
		return getBoolean("4sq.debug");

	}

}
