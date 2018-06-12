package com.plank;

public class DatabaseContextHolder {
	private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

	public static void setDatabaseType(String type) {
		contextHolder.set(type);
	}

	public static String getDatabaseType() {
		return contextHolder.get();
	}
}