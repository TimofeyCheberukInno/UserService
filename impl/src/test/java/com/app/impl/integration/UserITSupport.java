package com.app.impl.integration;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.app.impl.entity.User;
import com.app.impl.repository.UserRepository;

@Component
public class UserITSupport {
    @Autowired
    UserRepository userRepository;

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

        User user = new User(
                createdUser.getId(),
                "UPDATED_NAME_1",
                "UPDATED_SURNAME_1",
                LocalDate.of(2005, 5, 13),
                "updated_example@gmail.com"
        );

        return user;
    }

    public User getInvalidUserWithNullFieldsToUpdate() {
        User createdUser = createUser();

        User user = new User(
                createdUser.getId(),
                null,
                null,
                LocalDate.of(2005, 5, 13),
                null
        );

        return user;
    }

    public User getInvalidUserWithBlankFieldsToUpdate() {
        User createdUser = createUser();

        User user = new User(
                createdUser.getId(),
                "",
                "",
                LocalDate.of(2005, 5, 13),
                ""
        );

        return user;
    }

    public User getUserToUpdateWithoutCreatingUser() {
        User user = new User(
                1L,
                "UPDATED_NAME_1",
                "UPDATED_SURNAME_1",
                LocalDate.of(2005, 5, 13),
                "updated_example@gmail.com"
        );

        return user;
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

        User userToUpdate = new User(
                createdUser.getId(),
                "UPDATED_NAME_2",
                "UPDATED_SURNAME_2",
                LocalDate.of(2007, 10, 13),
                "example@gmail.com"
        );

        return userToUpdate;
    }

    public User getUserToUpdateWithConstraintViolation() {
        User user = createUser();

        User userToUpdate = new User(
                user.getId(),
                "CONSTRAINT_VIOLATION_CONSTRAINT_VIOLATION_CONSTRAINT_VIOLATION_CONSTRAINT_VIOLATION_",
                "UPDATED_SURNAME_2",
                LocalDate.of(2007, 10, 13),
                "example@gmail.com"
        );

        return userToUpdate;
    }

    public List<User> createListOfUsers() {
        User user1 = createUser();
        User user2 = createSecondUser();
        User createdUser = userRepository.save(user2);

        return new ArrayList<>(Arrays.asList(user1, createdUser));
    }
}