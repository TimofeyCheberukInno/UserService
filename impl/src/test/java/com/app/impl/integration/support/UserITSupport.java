package com.app.impl.integration.support;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.app.impl.dto.userDtos.UserCreateDto;
import com.app.impl.dto.userDtos.UserUpdateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.app.impl.entity.User;
import com.app.impl.repository.UserRepository;

@Component
public class UserITSupport {
    @Autowired
    private UserRepository userRepository;

    public User createUser() {
        User user = new User(
                null,
                "NAME_1",
                "SURNAME_1",
                LocalDate.of(2005, 5, 13),
                "example@gmail.com"
        );

        return userRepository.save(user);
    }

    public User getUserToUpdate() {
        User createdUser = createUser();

        return new User(
                createdUser.getId(),
                "UPDATED_NAME_1",
                "UPDATED_SURNAME_1",
                null,
                "updated_example@gmail.com"
        );
    }

    public User getInvalidUserWithNullFieldsToUpdate() {
        User createdUser = createUser();

        return new User(
                createdUser.getId(),
                null,
                null,
                null,
                null
        );
    }

    public User getInvalidUserWithBlankFieldsToUpdate() {
        User createdUser = createUser();

        return new User(
                createdUser.getId(),
                "",
                "",
                null,
                ""
        );
    }

    public User getUserToUpdateWithoutCreatingUser() {
        return new User(
                1L,
                "UPDATED_NAME_1",
                "UPDATED_SURNAME_1",
                null,
                "updated_example@gmail.com"
        );
    }

    public User createSecondUser() {
        return new User(
                null,
                "NAME_2",
                "SURNAME_2",
                LocalDate.of(2007, 10, 13),
                "example_2@gmail.com"
        );
    }

    public User getUserToUpdateWithExistingEmail() {
        createUser();
        User user = createSecondUser();
        User createdUser = userRepository.save(user);

        return new User(
                createdUser.getId(),
                "UPDATED_NAME_2",
                "UPDATED_SURNAME_2",
                null,
                "example@gmail.com"
        );
    }

    public User getUserToUpdateWithConstraintViolation() {
        User user = createUser();

        return new User(
                user.getId(),
                "CONSTRAINT_VIOLATION_CONSTRAINT_VIOLATION_CONSTRAINT_VIOLATION_CONSTRAINT_VIOLATION_",
                "UPDATED_SURNAME_2",
                null,
                "example@gmail.com"
        );
    }

    public List<User> createListOfUsers() {
        User user1 = createUser();
        User user2 = createSecondUser();
        User createdUser = userRepository.save(user2);

        return new ArrayList<>(Arrays.asList(user1, createdUser));
    }

    public UserCreateDto createUserCreateDto() {
        return new UserCreateDto(
                "NAME_1",
                "SURNAME_1",
                LocalDate.of(2005, 5, 13),
                "example@gmail.com"
        );
    }

    public UserCreateDto createInvalidUserCreateDto() {
        return new UserCreateDto(
                "NAME_1",
                "SURNAME_1",
                LocalDate.of(2030, 5, 13),
                "example@gmail.com"
        );
    }

    public UserUpdateDto createUserUpdateDto(Long id) {
        return new UserUpdateDto(
                id,
                "UPDATED_NAME_1",
                "UPDATED_SURNAME_1",
                "updated_example@gmail.com"
        );
    }

    public UserUpdateDto createInvalidUserUpdateDto() {
        return new UserUpdateDto(
                1L,
                "UPDATED_NAME_1",
                "UPDATED_SURNAME_1",
                "updated"
        );
    }
}