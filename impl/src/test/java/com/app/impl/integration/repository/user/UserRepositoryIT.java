package com.app.impl.integration.repository.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import com.app.impl.integration.UserITSupport;
import com.app.impl.entity.User;
import com.app.impl.repository.UserRepository;

@SpringBootTest
@ActiveProfiles("jpa")
public class UserRepositoryIT {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserITSupport support;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Nested
    @DisplayName("Tests for findByEmail(String email)")
    class findByEmailTests {
        @Test
        @DisplayName("return user by email")
        void shouldReturnUserByEmail() {
            User expectedValue = support.createUser();

            Optional<User> actualValue = userRepository.findByEmail(expectedValue.getEmail());

            assertThat(actualValue).isPresent()
                    .get()
                    .isEqualTo(expectedValue);
        }

        @Test
        @DisplayName("return Optional.empty() because no existing user with email")
        void shouldReturnEmptyOptional() {
            Optional<User> actualValue = userRepository.findByEmail("mamama@gmail.com");

            assertThat(actualValue).isEmpty();
        }

        @Test
        @DisplayName("returns Optional.empty() because user email is in uppercase")
        void shouldReturnEmptyOptionalBecauseEmailIsInUppercase() {
            User expectedValue = support.createUser();

            String[] parts = expectedValue.getEmail().split("@");
            Optional<User> actualValue = userRepository.findByEmail(parts[0].toUpperCase() + "@" + parts[1]);

            assertThat(actualValue).isEmpty();
        }
    }

    @Nested
    @DisplayName("Tests for updateUser(User user)")
    class updateUserTests {
        @Test
        @DisplayName("returns amount of updated users")
        void shouldReturnUpdatedUser() {
            User userToUpdate = support.getUserToUpdate();

            int amountOfUpdatedUsers = userRepository.updateUser(userToUpdate);

            assertThat(amountOfUpdatedUsers).isEqualTo(1);
            User updatedUser = userRepository.findById(userToUpdate.getId()).get();
            assertThat(updatedUser.getName()).isEqualTo(userToUpdate.getName());
            assertThat(updatedUser.getSurname()).isEqualTo(userToUpdate.getSurname());
            assertThat(updatedUser.getEmail()).isEqualTo(userToUpdate.getEmail());
        }

        @Test
        @DisplayName("returns 0 updated users while updating non-existent user")
        void shouldReturnExceptionWhileUpdatingNonExistentUser() {
            User userToUpdate = support.getUserToUpdateWithoutCreatingUser();

            int amountOfUpdatedUsers = userRepository.updateUser(userToUpdate);

            assertThat(amountOfUpdatedUsers).isEqualTo(0);
            assertThat(userRepository.findById(userToUpdate.getId())).isEmpty();
        }

        @Test
        @DisplayName("returns exception while updating user email to occupied value")
        void shouldReturnExceptionWhileUpdatingEmailToOccupiedValue() {
            User userToUpdate = support.getUserToUpdateWithExistingEmail();

            Assertions.assertThatExceptionOfType(DataIntegrityViolationException.class)
                    .isThrownBy(() -> userRepository.updateUser(userToUpdate));
        }

        @Test
        @DisplayName("check if operation with user was transactional")
        void shouldCheckIfOperationWasTransactional() {
            User userToUpdate = support.getUserToUpdateWithExistingEmail();

            Assertions.assertThatExceptionOfType(DataIntegrityViolationException.class)
                    .isThrownBy(() -> userRepository.updateUser(userToUpdate));

            Optional<User> user = userRepository.findById(userToUpdate.getId());

            assertThat(user).isPresent()
                    .get()
                    .isEqualTo(support.createSecondUser());
        }
    }

    @Nested
    @DisplayName("Tests for findAllByIds(Collection<Long> ids")
    class findAllByIdsTests {
        @Test
        @DisplayName("returns list of users")
        void shouldReturnListOfUsers() {
            List<User> expectedValues = support.createListOfUsers();

            List<User> actualValues = userRepository.findAllByIds(List.of(expectedValues.get(0).getId(), expectedValues.get(1).getId()));

            assertThat(actualValues).isEqualTo(expectedValues);
        }

        @Test
        @DisplayName("returns list of users partially")
        void shouldReturnListOfUserPartially() {
            List<User> expectedValues = support.createListOfUsers();

            List<User> actualValues = userRepository.findAllByIds(List.of(expectedValues.get(0).getId(), expectedValues.get(1).getId(), 3L));

            assertThat(actualValues.size()).isEqualTo(expectedValues.size());
            assertThat(actualValues).isEqualTo(expectedValues);
        }

        @Test
        @DisplayName("returns empty list of users because receives empty list")
        void shouldReturnEmptyListBecauseReceivesEmptyList() {
            List<User> actualValues = userRepository.findAllByIds(List.of());

            assertThat(actualValues).isEmpty();
        }

        @Test
        @DisplayName("returns empty list of users because receives list of non-existent ids")
        void shouldReturnEmptyListBecauseReceivesNonExistentIds() {
            List<User> actualValues = userRepository.findAllByIds(List.of(1L, 2L, 3L));

            assertThat(actualValues).isEmpty();
        }

        @Test
        @DisplayName("return list of users where list of ids has duplicates")
        void shouldReturnListOfUsersWithDuplicates() {
            List<User> expectedValues = support.createListOfUsers();

            List<User> actualValues = userRepository.findAllByIds(List.of(expectedValues.get(0).getId(), expectedValues.get(1).getId(), expectedValues.get(1).getId()));

            assertThat(actualValues.size()).isEqualTo(expectedValues.size());
            assertThat(actualValues).isEqualTo(expectedValues);
        }
    }
}
