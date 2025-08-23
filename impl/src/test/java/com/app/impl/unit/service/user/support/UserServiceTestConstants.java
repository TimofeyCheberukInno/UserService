package com.app.impl.unit.service.user.support;

import static com.app.impl.unit.service.user.UserServiceTestData.USER_BIRTHDATE;
import static com.app.impl.unit.service.user.UserServiceTestData.USER_EMAIL;
import static com.app.impl.unit.service.user.UserServiceTestData.USER_ID;
import static com.app.impl.unit.service.user.UserServiceTestData.USER_NAME;
import static com.app.impl.unit.service.user.UserServiceTestData.USER_SURNAME;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.app.impl.dto.userDtos.UserCreateDto;
import com.app.impl.dto.userDtos.UserDto;
import com.app.impl.dto.userDtos.UserUpdateDto;
import com.app.impl.entity.User;

public class UserServiceTestConstants {
    public static final Long USER_ID_VALUE = USER_ID;
    public static final String USER_EMAIL_VALUE = USER_EMAIL;
    public static final int AMOUNT_OF_UPDATED_USERS = 1;

    public static final String USER_NOT_FOUND_BY_ID_MSG = "User with id %d not found";
    public static final String USER_NOT_FOUND_BY_EMAIL_MSG = "User with email %s not found";
    public static final String LIST_OF_USERS_NOT_FOUND_BY_IDS_MSG = "Users not found for ids: ";


	public static UserCreateDto getUserCreateDto() {
		return new UserCreateDto(
                USER_NAME,
                USER_SURNAME,
                USER_BIRTHDATE,
                USER_EMAIL
        );
	}

	public static User getMapperUserEntity() {
		return new User(
                null,
                USER_NAME,
                USER_SURNAME,
                USER_BIRTHDATE,
                USER_EMAIL
        );
	}

	public static UserDto getCreatedUserDto() {
		return new UserDto(
                USER_ID,
                USER_NAME,
                USER_SURNAME,
                USER_BIRTHDATE,
                USER_EMAIL
        );
	}

	public static User getCreatedUserEntity() {
		return new User(
                USER_ID,
                USER_NAME,
                USER_SURNAME,
                USER_BIRTHDATE,
                USER_EMAIL
        );
	}

	public static UserUpdateDto getUserUpdateDto() {
		return new UserUpdateDto(
                USER_ID, USER_NAME,
                USER_SURNAME,
                USER_EMAIL
        );
	}

	public static User getUserUpdateEntity() {
		return new User(
                USER_ID,
                USER_NAME,
                USER_SURNAME,
                null,
                USER_EMAIL
        );
	}

	public static Collection<Long> getInitialListOfIds() {
		return new ArrayList<>(Arrays.asList(1L, 2L, 3L));
	}

	public static Collection<Long> getFullListOfIds() {
		return new ArrayList<>(Arrays.asList(1L, 2L, 3L));
	}

	public static Collection<Long> getEmptyListOfIds() {
		return new ArrayList<>();
	}

	public static List<User> getEmptyListOfUsers() {
		return new ArrayList<>();
	}

	public static List<User> getFullListOfUsers() {
		return new ArrayList<>(Arrays.asList(getUser1(), getUser2(), getUser3()));
	}

	public static List<UserDto> getFullListOfUserDtos() {
		return new ArrayList<>(Arrays.asList(getUserDto1(), getUserDto2(), getUserDto3()));
	}

	public static List<UserDto> getEmptyListOfUserDtos() {
		return new ArrayList<>();
	}

	public static List<User> getListOfCachedUsers() {
		return new ArrayList<>(Arrays.asList(getUser2(), getUser3()));
	}

	public static List<User> getListOfNotCachedUsers() {
		return new ArrayList<>(List.of(getUser1()));
	}

	public static List<Long> getListOfNotCachedUserIds() {
		return new ArrayList<>(List.of(1L));
	}

	private static User getUser1() {
        return new User(1L,
                "NAME_1",
                "SURNAME_1",
                LocalDate.of(2000, 10, 10),
                "email1@gmail.com"
        );
    }

	private static User getUser2() {
        return new User(
                2L,
                "NAME_2",
                "SURNAME_2",
                LocalDate.of(2010, 5, 28),
                "email2@mail.ru"
        );
    }

	private static User getUser3() {
        return new User(
                3L,
                "NAME_3",
                "SURNAME_3",
                LocalDate.of(1999, 1, 29),
                "email3@gmail.com"
        );
    }

	private static UserDto getUserDto1() {
        return new UserDto(
                1L,
                "NAME_1",
                "SURNAME_1",
                LocalDate.of(2000, 10, 10),
                "email1@gmail.com"
        );
    }

	private static UserDto getUserDto2() {
        return new UserDto(
                2L,
                "NAME_2",
                "SURNAME_2",
                LocalDate.of(2010, 5, 28),
                "email2@mail.ru"
        );
    }

	private static UserDto getUserDto3() {
        return new UserDto(
                3L,
                "NAME_3",
                "SURNAME_3",
                LocalDate.of(1999, 1, 29),
                "email3@gmail.com"
        );
    }
}