package com.app.impl.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.app.impl.dto.userDtos.UserCreateDto;
import com.app.impl.dto.userDtos.UserDto;
import com.app.impl.dto.userDtos.UserUpdateDto;
import com.app.impl.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
	UserDto toDto(User user);

	User toEntity(UserCreateDto userCreateDto);

	User toUpdateEntity(UserUpdateDto userUpdateDto);

	List<UserDto> toDtoList(List<User> users);
}
