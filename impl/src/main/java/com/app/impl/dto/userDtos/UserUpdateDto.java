package com.app.impl.dto.userDtos;

import static com.app.impl.dto.userDtos.UserConstraints.BLANK_EMAIL_MSG;
import static com.app.impl.dto.userDtos.UserConstraints.BLANK_NAME_MSG;
import static com.app.impl.dto.userDtos.UserConstraints.BLANK_SURNAME_MSG;
import static com.app.impl.dto.userDtos.UserConstraints.INVALID_EMAIL_MSG;
import static com.app.impl.dto.userDtos.UserConstraints.MAX_NAME_LENGTH;
import static com.app.impl.dto.userDtos.UserConstraints.MAX_NAME_LENGTH_MSG;
import static com.app.impl.dto.userDtos.UserConstraints.MAX_SURNAME_LENGTH;
import static com.app.impl.dto.userDtos.UserConstraints.MAX_SURNAME_LENGTH_MSG;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;

public record UserUpdateDto(
        @Positive
        Long id,

        @NotBlank(message = BLANK_NAME_MSG)
        @Size(max = MAX_NAME_LENGTH, message = MAX_NAME_LENGTH_MSG)
        String name,

		@NotBlank(message = BLANK_SURNAME_MSG)
        @Size(max = MAX_SURNAME_LENGTH, message = MAX_SURNAME_LENGTH_MSG)
        String surname,

		@NotBlank(message = BLANK_EMAIL_MSG)
        @Email(message = INVALID_EMAIL_MSG)
        String email
) { }