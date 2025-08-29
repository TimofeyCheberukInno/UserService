package com.app.impl.dto.cardDtos;

import java.time.LocalDate;

import com.app.impl.dto.userDtos.UserDto;

public record CardWithUserDto(
        Long id,
        Long userId,
        String cardNumber,
        String cardHolderName,
        LocalDate expirationDate,
		UserDto userDto
) { }