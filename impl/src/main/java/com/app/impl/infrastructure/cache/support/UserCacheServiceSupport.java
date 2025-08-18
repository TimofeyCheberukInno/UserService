package com.app.impl.infrastructure.cache.support;

public class UserCacheServiceSupport {
	public static String makeKey(String hashPrefix, String id) {
		return new StringBuilder(hashPrefix).append(":").append(id).toString();
	}
}
