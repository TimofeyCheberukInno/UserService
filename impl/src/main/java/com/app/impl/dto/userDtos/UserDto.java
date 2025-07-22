package com.app.impl.dto.userDtos;

import java.time.LocalDate;

public record UserDto(
        Long id,
        String name,
        String surname,
        LocalDate birthDate,
        String email
) {
}
