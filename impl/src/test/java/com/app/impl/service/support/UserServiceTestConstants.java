package com.app.impl.service.support;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.app.impl.dto.userDtos.UserCreateDto;
import com.app.impl.dto.userDtos.UserDto;
import com.app.impl.dto.userDtos.UserUpdateDto;
import com.app.impl.entity.User;

import static com.app.impl.service.support.UserServiceTestData.USER_ID;
import static com.app.impl.service.support.UserServiceTestData.USER_NAME;
import static com.app.impl.service.support.UserServiceTestData.USER_SURNAME;
import static com.app.impl.service.support.UserServiceTestData.USER_BIRTHDATE;
import static com.app.impl.service.support.UserServiceTestData.USER_EMAIL;

public class UserServiceTestConstants {
    public static final Long USER_ID_VALUE = USER_ID;

    public static final String USER_EMAIL_VALUE = USER_EMAIL;

    public static final UserCreateDto USER_CREATE_DTO =
            new UserCreateDto(
                USER_NAME,
                USER_SURNAME,
                USER_BIRTHDATE,
                USER_EMAIL
        );

    public static final User MAPPED_USER_ENTITY =
            new User(
                null,
                USER_NAME,
                USER_SURNAME,
                USER_BIRTHDATE,
                USER_EMAIL
            );

    public static final UserDto CREATED_USER_DTO =
            new UserDto(
                USER_ID,
                USER_NAME,
                USER_SURNAME,
                USER_BIRTHDATE,
                USER_EMAIL
        );

    public static final User CREATED_USER_ENTITY =
            new User(
                USER_ID,
                USER_NAME,
                USER_SURNAME,
                USER_BIRTHDATE,
                USER_EMAIL
        );

    public static final UserUpdateDto USER_UPDATE_DTO =
        new UserUpdateDto(
                USER_ID,
                USER_NAME,
                USER_SURNAME,
                USER_EMAIL
        );

    public static final User USER_UPDATE_ENTITY =
            new User(
                    USER_ID,
                    USER_NAME,
                    USER_SURNAME,
                    null,
                    USER_EMAIL
            );

    public static final int AMOUNT_OF_UPDATED_USERS = 1;

    public static final String USER_NOT_FOUND_BY_ID_MSG = "User with id %d not found";

    public static final String USER_NOT_FOUND_BY_EMAIL_MSG = "User with email %s not found";

    public static final String LIST_OF_USERS_NOT_FOUND_BY_IDS_MSG = "Users not found for ids: ";

    public static Collection<Long> getInitialListOfIds() {
        return new ArrayList<>(Arrays.asList(1L, 2L, 3L));
    }

    public static Collection<Long> getFullListOfIds() {
        return new ArrayList<>(Arrays.asList(1L, 2L, 3L));
    }

    public static Collection<Long> getEmptyListOfIds() {
        return new ArrayList<>();
    }

    public static List<User> getEmptyListOfUsers(){
        return new ArrayList<>();
    }

    public static List<User> getFullListOfUsers() {
        return new ArrayList<>(Arrays.asList(user1, user2, user3));
    }

    public static List<UserDto> getFullListOfUserDtos() {
        return new ArrayList<>(Arrays.asList(userDto1, userDto2, userDto3));
    }

    public static List<User> getListOfCachedUsers() {
        return new ArrayList<>(Arrays.asList(user2, user3));
    }

    public static List<User> getListOfNotCachedUsers() {
        return new ArrayList<>(List.of(user1));
    }

    public static List<Long> getListOfNotCachedUserIds() {
        return new ArrayList<>(List.of(1L));
    }


    private static final User user1 =
            new User(
                    1L,
                    "NAME_1",
                    "SURNAME_1",
                    LocalDate.of(2000, 10, 10),
                    "email1@gmail.com"
            );

    private static final User user2 =
            new User(
                    2L,
                    "NAME_2",
                    "SURNAME_2",
                    LocalDate.of(2010, 5, 28),
                    "email2@mail.ru"
            );

    private static final User user3 =
            new User(
                    3L,
                    "NAME_3",
                    "SURNAME_3",
                    LocalDate.of(1999, 1, 29),
                    "email3@gmail.com"
            );

    private static final UserDto userDto1 =
            new UserDto(
                    1L,
                    "NAME_1",
                    "SURNAME_1",
                    LocalDate.of(2000, 10, 10),
                    "email1@gmail.com"
            );

    private static final UserDto userDto2 =
            new UserDto(
                    2L,
                    "NAME_2",
                    "SURNAME_2",
                    LocalDate.of(2010, 5, 28),
                    "email2@mail.ru"
            );

    private static final UserDto userDto3 =
            new UserDto(
                    3L,
                    "NAME_3",
                    "SURNAME_3",
                    LocalDate.of(1999, 1, 29),
                    "email3@gmail.com"
            );
}