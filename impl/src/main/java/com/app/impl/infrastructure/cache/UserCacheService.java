package com.app.impl.infrastructure.cache;

import java.time.Duration;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.app.impl.entity.User;

@Service
@Slf4j
public class UserCacheService {
    private final RedisTemplate<String, Object> redisTemplate;
    private static final int TIME_TO_LIVE_FOR_USER_IN_MINUTES = 60;
    private static final String USERS_CACHE_PREFIX = "users";

    @Autowired
    public UserCacheService(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void save(User user){
        redisTemplate.opsForValue().set(
                makeKey(USERS_CACHE_PREFIX, String.valueOf(user.getId())),
                user,
                Duration.ofMinutes(TIME_TO_LIVE_FOR_USER_IN_MINUTES));
        log.info("Putting cache for {} with id {}...", USERS_CACHE_PREFIX, user.getId());

        redisTemplate.opsForValue().set(
                makeKey(USERS_CACHE_PREFIX, user.getEmail()),
                user,
                Duration.ofMinutes(TIME_TO_LIVE_FOR_USER_IN_MINUTES));
        log.info("Putting cache for {} with id {}...", USERS_CACHE_PREFIX, user.getEmail());
    }

    public void update(User user){
        save(user);
        log.info("Updating cache for {} with id {}...", USERS_CACHE_PREFIX, user.getId());
    }

    public Optional<User> getById(Long id){
        Object object = redisTemplate.opsForValue().get(makeKey(USERS_CACHE_PREFIX, String.valueOf(id)));
        if(object instanceof User){
            log.info("Cache hit for {} with id {}!", USERS_CACHE_PREFIX, id);
            return Optional.of((User) object);
        }
        log.info("Cache miss for {} with id {}!", USERS_CACHE_PREFIX, id);
        return Optional.empty();
    }

    public Optional<User> getByEmail(String email){
        Object object = redisTemplate.opsForValue().get(makeKey(USERS_CACHE_PREFIX, email));
        if(object instanceof User){
            log.info("Cache hit for {} with email {}!", USERS_CACHE_PREFIX, email);
            return Optional.of((User) object);
        }
        log.info("Cache miss for {} with email {}!", USERS_CACHE_PREFIX, email);
        return Optional.empty();
    }

    public List<User> getByIds(Collection<Long> ids){
        List<String> keys = ids.stream()
                .map(id -> makeKey(USERS_CACHE_PREFIX, String.valueOf(id)))
                .toList();

        List<Object> usersWithNulls =
                Objects.requireNonNull(redisTemplate.opsForValue().multiGet(keys));

        List<Object> users = usersWithNulls.stream()
                .filter(Objects::nonNull)
                .toList();

        boolean allCorrectType = users.stream()
                .allMatch(object -> object instanceof User);

        if(!allCorrectType){
            throw new ClassCastException("Object is not User type!");
        }

        List<User> cachedUsers = users.stream()
                        .map(object -> (User) object)
                        .toList();

        List<Long> cachedUsersIds = cachedUsers.stream()
                        .map(User::getId)
                        .toList();
        log.info("Cache hit for {} with ids {}!", USERS_CACHE_PREFIX, cachedUsersIds);

        return cachedUsers;
    }

    public void delete(Long id, String email){
        redisTemplate.delete(makeKey(USERS_CACHE_PREFIX, String.valueOf(id)));
        log.info("Evicting cache for {} with id {}...", USERS_CACHE_PREFIX, id);
        redisTemplate.delete(makeKey(USERS_CACHE_PREFIX, email));
        log.info("Evicting cache for {} with email {}...", USERS_CACHE_PREFIX, email);
    }

    private String makeKey(String USERS_CACHE_PREFIX, String id) {
        return new StringBuilder(USERS_CACHE_PREFIX).append(":").append(id).toString();
    }
}
