package com.app.impl.dto.userDtos;

import static com.app.impl.dto.userDtos.UserConstraints.BLANK_EMAIL_MSG;
import static com.app.impl.dto.userDtos.UserConstraints.BLANK_NAME_MSG;
import static com.app.impl.dto.userDtos.UserConstraints.BLANK_SURNAME_MSG;
import static com.app.impl.dto.userDtos.UserConstraints.INVALID_BIRTH_DATE_MSG;
import static com.app.impl.dto.userDtos.UserConstraints.INVALID_EMAIL_MSG;
import static com.app.impl.dto.userDtos.UserConstraints.MAX_NAME_LENGTH;
import static com.app.impl.dto.userDtos.UserConstraints.MAX_NAME_LENGTH_MSG;
import static com.app.impl.dto.userDtos.UserConstraints.MAX_SURNAME_LENGTH;
import static com.app.impl.dto.userDtos.UserConstraints.MAX_SURNAME_LENGTH_MSG;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

public record UserCreateDto(
		@NotBlank(message = BLANK_NAME_MSG)
        @Size(max = MAX_NAME_LENGTH, message = MAX_NAME_LENGTH_MSG)
        String name,

		@NotBlank(message = BLANK_SURNAME_MSG)
        @Size(max = MAX_SURNAME_LENGTH, message = MAX_SURNAME_LENGTH_MSG)
        String surname,

		@Past(message = INVALID_BIRTH_DATE_MSG)
        LocalDate birthDate,

		@NotBlank(message = BLANK_EMAIL_MSG)
        @Email(message = INVALID_EMAIL_MSG)
        String email
) { }