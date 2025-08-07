package com.app.impl.config;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

@Configuration
public class RedisConfig {
    private static final int USER_CACHE_TIME_TO_LIVE_IN_MINUTES = 60;
    private static final int CARD_CACHE_TIME_TO_LIVE_IN_MINUTES = 60;

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues();

        Map<String, RedisCacheConfiguration> cachesConfigs = new HashMap<>();
        cachesConfigs.put("users", config.entryTtl(Duration.ofMinutes(USER_CACHE_TIME_TO_LIVE_IN_MINUTES)));
        cachesConfigs.put("cards", config.entryTtl(Duration.ofMinutes(CARD_CACHE_TIME_TO_LIVE_IN_MINUTES)));

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(config)
                .withInitialCacheConfigurations(cachesConfigs)
                .build();
    }
}
