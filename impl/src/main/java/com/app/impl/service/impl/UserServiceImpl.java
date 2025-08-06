package com.app.impl.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.impl.dto.userDtos.UserCreateDto;
import com.app.impl.dto.userDtos.UserDto;
import com.app.impl.dto.userDtos.UserUpdateDto;
import com.app.impl.entity.User;
import com.app.impl.mapper.UserMapper;
import com.app.impl.repository.UserRepository;
import com.app.impl.service.UserService;
import com.app.impl.exception.UserNotFoundException;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private static final String USER_NOT_FOUND_BY_ID_MSG = "User with id %d not found";
    private static final String USER_NOT_FOUND_BY_EMAIL_MSG = "User with email %s not found";
    private static final String LIST_OF_USERS_NOT_FOUND_BY_IDS_MSG = "Users not found for ids: ";

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional
    public UserDto create(UserCreateDto userCreateDto) {
        User user = userMapper.toEntity(userCreateDto);
        User createdUser = userRepository.save(user);
        return userMapper.toDto(createdUser);
    }

    @Override
    @Transactional
    public int update(UserUpdateDto userUpdateDto) {
        userRepository.findById(userUpdateDto.id()).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_BY_ID_MSG));

        User user = userMapper.toUpdateEntity(userUpdateDto);
        return userRepository.updateUser(user);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException(String.format(USER_NOT_FOUND_BY_ID_MSG, id))
        );
        return userMapper.toDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto findByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new UserNotFoundException(String.format(USER_NOT_FOUND_BY_EMAIL_MSG, email))
        );
        return userMapper.toDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> findAllByIds(Collection<Long> ids) {
        List<User> users = userRepository.findAllById(ids);

        if(users.size() < ids.size()){
            Set<Long> existingIds = users.stream()
                    .map(User::getId)
                    .collect(Collectors.toSet());

            List<Long> notFoundIds = ids.stream()
                    .filter(id -> !existingIds.contains(id))
                    .toList();
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
