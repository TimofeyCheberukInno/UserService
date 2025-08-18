package com.app.impl.infrastructure.cache.support;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.springframework.data.redis.core.RedisTemplate;

public class CardCacheServiceSupport {
	@SuppressWarnings("unchecked")
	public static <T> List<T> getAndValidateObjectsFromCache(
            RedisTemplate<String, Object> redisTemplate,
			String hashName, Collection<Long> ids,
            Class<T> type
    ) {
		List<String> keys = ids.stream()
                .map(id -> makeKey(hashName, id))
                .toList();

		List<Object> cardsWithNulls = Objects.requireNonNull(redisTemplate.opsForValue().multiGet(keys));

		List<Object> cards = cardsWithNulls.stream()
                .filter(Objects::nonNull)
                .toList();

		boolean allCorrectType = cards
                .stream()
                .allMatch(type::isInstance);

		if (!allCorrectType) {
			throw new ClassCastException("Object is not " + type.getName() + " type!");
		}

		return cards.stream()
                .map(object -> (T) object)
                .toList();
	}

	public static String makeKey(String hashPrefix, Long id) {
		return new StringBuilder(hashPrefix).append(":").append(id).toString();
	}
}
