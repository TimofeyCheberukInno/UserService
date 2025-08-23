package com.app.impl.unit.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.app.impl.entity.User;
import com.app.impl.service.support.UserServiceSupport;

public class UserServiceSupportTest {

	@Nested
	@DisplayName("Tests for findNotCachedUsersIds(Collection<Long> ids, List<User> cachedUsers)")
	class FindNotCachedUsersIds {
		@Test
		@DisplayName("returns full list of ids")
		public void shouldReturnFullInitialListOfIds() {
			List<Long> expectedValues = new ArrayList<>(Arrays.asList(1L, 2L, 3L));

			List<Long> actualValues =
                    UserServiceSupport.findNotCachedUsersIds(new ArrayList<>(Arrays.asList(1L, 2L, 3L)), new ArrayList<>());

			assertThat(actualValues).isEqualTo(expectedValues);
		}

		@Test
		@DisplayName("returns empty list of ids")
		public void shouldReturnEmptyListOfIds() {
			List<Long> expectedValues = new ArrayList<>();

			List<Long> actualValues = UserServiceSupport.findNotCachedUsersIds(
					new ArrayList<>(Arrays.asList(1L, 2L, 3L)),
					new ArrayList<>(Arrays.asList(
							new User(1L, "NAME_1", "SURNAME_1", LocalDate.of(2000, 10, 10), "email1@gmail.com"),
							new User(2L, "NAME_2", "SURNAME_2", LocalDate.of(2010, 5, 28), "email2@mail.ru"),
							new User(3L, "NAME_3", "SURNAME_3", LocalDate.of(1999, 1, 29), "email3@gmail.com"))));

			assertThat(actualValues).isEqualTo(expectedValues);
		}

		@Test
		@DisplayName("returns part of list of ids")
		public void shouldReturnListOfIdsPartially() {
			List<Long> expectedValues = new ArrayList<>(Arrays.asList(2L, 3L));

			List<Long> actualValues =
                    UserServiceSupport.findNotCachedUsersIds(
                            new ArrayList<>(Arrays.asList(1L, 2L, 3L)),
                            new ArrayList<>(Arrays.asList(
                                    new User(1L, "NAME_1", "SURNAME_1", LocalDate.of(2000, 10, 10), "email1@gmail.com")
                            ))
                    );

			assertThat(actualValues).isEqualTo(expectedValues);
		}
	}
}