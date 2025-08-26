package com.app.impl.integration.infrastructure.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import com.app.impl.config.RedisConfig;
import com.app.impl.integration.config.RedisTestContainersConfig;
import com.app.impl.entity.User;
import com.app.impl.infrastructure.cache.UserCacheService;

@SpringBootTest(classes = {
        UserCacheService.class,
        User.class,
})
@Import({RedisTestContainersConfig.class, RedisConfig.class})
@ActiveProfiles("redis")
@EnableAutoConfiguration(exclude = org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration.class)
public class UserCacheServiceIT {
    @Autowired
    private UserCacheService cacheService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    private static final String USERS_CACHE_PREFIX = "users" + ":";


    @BeforeEach
    void setUp() {
        redisTemplate.execute((RedisCallback<Object>) connection -> {
            connection.serverCommands().flushDb();
            return null;
        });
    }

    @Nested
    @DisplayName("Tests for save(User user) and update(User user)")
    class saveAndUpdateTests {
        @Test
        @DisplayName("saves user in cache")
        void shouldPutUserInCache() {
            User user = new User(
                    null,
                    "NAME_2",
                    "SURNAME_2",
                    LocalDate.of(2007, 10, 13),
                    "example_2@gmail.com"
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
