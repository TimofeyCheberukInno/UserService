package com.app.impl.infrastructure.cache;

import java.time.Duration;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    public UserCacheService(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void save(String hashPrefix, User user){
        redisTemplate.opsForValue().set(
                makeKey(hashPrefix, String.valueOf(user.getId())),
                user,
                Duration.ofMinutes(TIME_TO_LIVE_FOR_USER_IN_MINUTES));

        redisTemplate.opsForValue().set(
                makeKey(hashPrefix, user.getEmail()),
                user,
                Duration.ofMinutes(TIME_TO_LIVE_FOR_USER_IN_MINUTES));
    }

    public void update(String hashPrefix, User user){
        save(hashPrefix, user);
    }

    public Optional<User> getById(String hashPrefix, Long id){
        Object object = redisTemplate.opsForValue().get(makeKey(hashPrefix, String.valueOf(id)));
        if(object instanceof User){
            return Optional.of((User) object);
        }
        return Optional.empty();
    }

    public Optional<User> getByEmail(String hashPrefix, String email){
        Object object = redisTemplate.opsForValue().get(makeKey(hashPrefix, email));
        if(object instanceof User){
            return Optional.of((User) object);
        }
        return Optional.empty();
    }

    public List<User> getByIds(String hashPrefix, Collection<Long> ids){
        List<String> keys = ids.stream()
                .map(id -> makeKey(hashPrefix, String.valueOf(id)))
                .toList();

        List<Object> usersWithNulls = redisTemplate.opsForValue().multiGet(keys);

        List<Object> users = usersWithNulls.stream()
                .filter(Objects::nonNull)
                .toList();

        boolean allCorrectType = users != null
                && users.stream()
                        .allMatch(object -> object instanceof User);

        if(!allCorrectType){
            throw new ClassCastException("Object is not User type!");
        }

        return users.stream()
                .map(object -> (User) object)
                .toList();
    }

    public void delete(String hashPrefix, Long id, String email){
        redisTemplate.delete(makeKey(hashPrefix, String.valueOf(id)));
        redisTemplate.delete(makeKey(hashPrefix, email));
    }

    private String makeKey(String hashPrefix, String userId) {
        return hashPrefix + ":" + userId;
    }
}
