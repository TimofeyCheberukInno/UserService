package com.app.impl.service.impl;

import static com.app.impl.service.support.UserServiceSupport.findNotCachedUsersIds;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.impl.dto.userDtos.UserCreateDto;
import com.app.impl.dto.userDtos.UserDto;
import com.app.impl.dto.userDtos.UserUpdateDto;
import com.app.impl.entity.User;
import com.app.impl.exception.UserNotFoundException;
import com.app.impl.infrastructure.cache.UserCacheService;
import com.app.impl.mapper.UserMapper;
import com.app.impl.repository.UserRepository;
import com.app.impl.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;
	private final UserMapper userMapper;
	private final UserCacheService cacheService;
	private static final String USER_NOT_FOUND_BY_ID_MSG = "User with id %d not found";
	private static final String USER_NOT_FOUND_BY_EMAIL_MSG = "User with email %s not found";
	private static final String LIST_OF_USERS_NOT_FOUND_BY_IDS_MSG = "Users not found for ids: ";

	@Autowired
	public UserServiceImpl(
            UserRepository userRepository,
            UserMapper userMapper,
            UserCacheService cacheService
    ) {
		this.userRepository = userRepository;
		this.userMapper = userMapper;
		this.cacheService = cacheService;
	}

	@Override
	@Transactional
	public UserDto create(UserCreateDto userCreateDto) {
		User user = userMapper.toEntity(userCreateDto);
		User createdUser = userRepository.save(user);

		cacheService.save(createdUser);

		return userMapper.toDto(createdUser);
	}

	@Override
	@Transactional
	public int update(UserUpdateDto userUpdateDto) {
		userRepository.findById(userUpdateDto.id())
                .orElseThrow(() -> new UserNotFoundException(String.format(USER_NOT_FOUND_BY_ID_MSG, userUpdateDto.id())));

        User user = userMapper.toUpdateEntity(userUpdateDto);
		int cntOfUpdatedUsers = userRepository.updateUser(user);

		User updatedUser = userRepository.findById(user.getId()).get();
		cacheService.update(updatedUser);

		return cntOfUpdatedUsers;
	}

	@Override
	@Transactional
	public void delete(Long id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException(String.format(USER_NOT_FOUND_BY_ID_MSG, id)));

        userRepository.deleteById(id);
		cacheService.delete(user.getId(), user.getEmail());
	}

	@Override
	@Transactional(readOnly = true)
	public UserDto findById(Long id) {
		Optional<User> cachedUser = cacheService.getById(id);
        if (cachedUser.isPresent()) {
			return userMapper.toDto(cachedUser.get());
		}

		User user = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException(String.format(USER_NOT_FOUND_BY_ID_MSG, id)));

        return userMapper.toDto(user);
	}

	@Override
	@Transactional(readOnly = true)
	public UserDto findByEmail(String email) {
		Optional<User> cachedUser = cacheService.getByEmail(email);
        if (cachedUser.isPresent()) {
			return userMapper.toDto(cachedUser.get());
		}

		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new UserNotFoundException(String.format(USER_NOT_FOUND_BY_EMAIL_MSG, email)));

        return userMapper.toDto(user);
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserDto> findByIds(Collection<Long> ids) {
		List<User> cachedUsers = cacheService.getByIds(ids);

		List<Long> notCachedUsersIds = findNotCachedUsersIds(ids, cachedUsers);

		List<User> users = userRepository.findAllByIds(notCachedUsersIds);

		users.addAll(cachedUsers);

		if (users.size() < ids.size()) {
			Set<Long> existingIds = users.stream().map(User::getId).collect(Collectors.toSet());

			List<Long> notFoundIds = ids.stream().filter(id -> !existingIds.contains(id)).toList();

            throw new UserNotFoundException(LIST_OF_USERS_NOT_FOUND_BY_IDS_MSG + notFoundIds);
		}

		return userMapper.toDtoList(users);
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserDto> findAll() {
		return userMapper.toDtoList(userRepository.findAll());
	}
}
