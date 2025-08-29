package com.app.impl.integration.infrastructure.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;

import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.context.annotation.Import;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.annotation.DirtiesContext;

import com.app.impl.config.RedisConfig;
import com.app.impl.entity.User;
import com.app.impl.infrastructure.cache.UserCacheService;

@DataRedisTest
@Import({
        UserCacheService.class,
        RedisConfig.class
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class UserCacheServiceIT extends BaseRedisTest {
    @Autowired
    private UserCacheService cacheService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    private static final String USERS_CACHE_PREFIX = "users" + ":";

    @BeforeEach
    void setUp() {
        cacheService.deleteAll();
    }

    @Nested
    @DisplayName("Integration tests for save(User user) and update(User user)")
    class saveAndUpdateIT {
        @Test
        @DisplayName("saves user in cache")
        void shouldPutUserInCache() {
            User user = new User(
                    1L,
                    "NAME_1",
                    "SURNAME_1",
                    LocalDate.of(2007, 10, 13),
                    "example_1@gmail.com"
            );
            cacheService.save(user);

            String idKey = USERS_CACHE_PREFIX + user.getId();
            String emailKey = USERS_CACHE_PREFIX + user.getEmail();

            User cachedById = (User) redisTemplate.opsForValue().get(idKey);
            User cachedByEmail = (User) redisTemplate.opsForValue().get(emailKey);

            assertNotNull(cachedById);
            assertEquals(user.getEmail(), cachedById.getEmail());
            assertNotNull(cachedByEmail);
            assertEquals(user.getId(), cachedByEmail.getId());
        }

        @Test
        @DisplayName("throws NullPointerException")
        void shouldThrowNullPointerException() {
            Assertions.assertThatExceptionOfType(NullPointerException.class)
                    .isThrownBy(() -> cacheService.save(null));
        }
    }
}
