package com.app.impl.infrastructure.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CacheService {
    private CacheManager cacheManager;

    @Autowired
    public CacheService(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public<T> T getFromCache(String cacheName, Object key, Class<T> classType) {
        Cache cache = cacheManager.getCache(cacheName);
        if(cache == null){
            log.warn("Cache {} was not found!", cacheName);
            return null;
        }

        Cache.ValueWrapper cachedValue = cache.get(key);
        if(cachedValue == null){
            log.info("Cache miss for {} with id {}!", cacheName, key);
            return null;
        }
        else{
            log.info("Cache hit for {} with id {}!", cacheName, key);
            return classType.cast(cachedValue.get());
        }
    }

    public void putCache(String cacheName, Object key, Object value){
        Cache cache = cacheManager.getCache(cacheName);
        if(cache == null){
            log.warn("Cache {} was not found!", cacheName);
        }
        else{
            log.info("Putting object with id {} into cache {}!", key, cacheName);
            cache.put(key, value);
        }
    }

    public void evictFromCache(String cacheName, Object key){
        Cache cache = cacheManager.getCache(cacheName);
        if(cache == null){
            log.warn("Cache {} was not found!", cacheName);
        }
        else{
            log.info("Evicting object with id {} from cache {}!", key, cacheName);
            cache.evict(key);
        }
    }
}
