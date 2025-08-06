package com.app.impl.service;

import java.util.Collection;
import java.util.List;

import com.app.impl.dto.userDtos.UserCreateDto;
import com.app.impl.dto.userDtos.UserDto;
import com.app.impl.dto.userDtos.UserUpdateDto;

public interface UserService {
    UserDto create(UserCreateDto userCreateDto);
    int update(UserUpdateDto userUpdateDto);
    void delete(Long id);
    UserDto findById(Long id);
    UserDto findByEmail(String email);
    List<UserDto> findAllByIds(Collection<Long> ids);
    List<UserDto> findAll();
}
