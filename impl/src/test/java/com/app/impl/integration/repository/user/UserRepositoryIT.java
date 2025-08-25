package com.app.impl.integration.repository.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import com.app.impl.integration.UserITSupport;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import com.app.impl.entity.User;
import com.app.impl.repository.UserRepository;

@SpringBootTest
@Import(UserITSupport.class)
@ActiveProfiles("jpa")
public class UserRepositoryIT {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserITSupport support;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
    }

    @Nested
    @DisplayName("Tests for findByEmail(String email)")
    class findByEmailTests {
        @Test
        @DisplayName("return user by email")
        void shouldReturnUserByEmail() {
            User expectedUser = support.createUser();

            Optional<User> actualUser = userRepository.findByEmail(expectedUser.getEmail());

            assertThat(actualUser).isPresent()
                    .get()
                    .isEqualTo(expectedUser);
        }

        @Test
        @DisplayName("return Optional.empty() because no existing user with email")
        void shouldReturnEmptyOptional() {
            Optional<User> actualUser = userRepository.findByEmail("mamama@gmail.com");

            assertThat(actualUser).isEmpty();
        }

        // TODO: excessive?
        @Test
        @DisplayName("returns Optional.empty() because email is null")
        void shouldReturnEmptyOptionalBecauseEmailIsNull() {
            Optional<User> actualUser = userRepository.findByEmail(null);

            assertThat(actualUser).isEmpty();
        }

        // TODO: excessive?
        @Test
        @DisplayName("returns Optional.empty() because email is blank")
        void shouldReturnEmptyOptionalBecauseEmailIsBlank() {
            Optional<User> actualUser = userRepository.findByEmail("");

            assertThat(actualUser).isEmpty();
        }

        // TODO: excessive?
        @Test
        @DisplayName("returns Optional.empty() because email is invalid")
        void shouldReturnEmptyOptionalBecauseEmailIsInvalid() {
            Optional<User> actualUser = userRepository.findByEmail("invalid-email");

            assertThat(actualUser).isEmpty();
        }

        @Test
        @DisplayName("returns Optional.empty() because email is in uppercase")
        void shouldReturnEmptyOptionalBecauseEmailIsInUppercase() {
            User expectedUser = support.createUser();

            String[] parts = expectedUser.getEmail().split("@");
            Optional<User> actualUser = userRepository.findByEmail(parts[0].toUpperCase() + "@" + parts[1]);

            assertThat(actualUser).isEmpty();
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
        }

        // TODO: excessive?
        @Test
        @DisplayName("returns exception because updates user with null value fields")
        void shouldReturnExceptionBecauseUpdatesUserWithNullValueFields() {
            User userToUpdate = support.getInvalidUserWithNullFieldsToUpdate();

            Assertions.assertThatExceptionOfType(DataIntegrityViolationException.class)
                    .isThrownBy(() -> userRepository.updateUser(userToUpdate));
        }

        // TODO: excessive?
        @Test
        @DisplayName("returns 0 updated users because updates user with blank fields")
        void shouldReturnExceptionBecauseUpdatesUserWithBlankFields() {
            User userToUpdate = support.getInvalidUserWithBlankFieldsToUpdate();

            int amountOfUpdatedUsers = userRepository.updateUser(userToUpdate);

            assertThat(amountOfUpdatedUsers).isEqualTo(1);
        }

        @Test
        @DisplayName("returns 0 updated users while updation non-existent user")
        void shouldReturnExceptionWhileUpdationNonExistentUser() {
            User userToUpdate = support.getUserToUpdateWithoutCreatingUser();

            int amountOfUpdatedUsers = userRepository.updateUser(userToUpdate);

            assertThat(amountOfUpdatedUsers).isEqualTo(0);
        }

        @Test
        @DisplayName("returns exception while updating email to occupied value")
        void shouldReturnExceptionWhileUpdatingEmailToOccupiedValue() {
            User userToUpdate = support.getUserToUpdateWithExistingEmail();

            Assertions.assertThatExceptionOfType(DataIntegrityViolationException.class)
                    .isThrownBy(() -> userRepository.updateUser(userToUpdate));
        }

        // TODO: excessive?
        @Test
        @DisplayName("returns exception while updation with constraint violation")
        void shouldReturnExceptionWhileUpdatingEmailToConstraintViolation() {
            User userToUpdate = support.getUserToUpdateWithConstraintViolation();

            Assertions.assertThatExceptionOfType(DataIntegrityViolationException.class)
                    .isThrownBy(() -> userRepository.updateUser(userToUpdate));
        }

        @Test
        @DisplayName("check if operation was transactional")
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
            List<User> expectedUsers = support.createListOfUsers();

            List<User> actualUsers = userRepository.findAllByIds(List.of(expectedUsers.get(0).getId(), expectedUsers.get(1).getId()));

            assertThat(actualUsers).isEqualTo(expectedUsers);
        }

        @Test
        @DisplayName("returns list of users partially")
        void shouldReturnListOfUserPartially() {
            List<User> expectedUsers = support.createListOfUsers();

            List<User> actualUsers = userRepository.findAllByIds(List.of(expectedUsers.get(0).getId(), expectedUsers.get(1).getId(), 3L));

            assertThat(actualUsers.size()).isEqualTo(expectedUsers.size());
            assertThat(actualUsers).isEqualTo(expectedUsers);
        }

        @Test
        @DisplayName("returns empty list because receives empty list")
        void shouldReturnEmptyListBecauseReceivesEmptyList() {
            List<User> actualUsers = userRepository.findAllByIds(List.of());

            assertThat(actualUsers).isEmpty();
        }

        @Test
        @DisplayName("returns empty list because receives list of non-existent ids")
        void shouldReturnEmptyListBecauseReceivesNonExistentIds() {
            List<User> actualUsers = userRepository.findAllByIds(List.of(1L, 2L, 3L));

            assertThat(actualUsers).isEmpty();
        }

        @Test
        @DisplayName("return list of users where list of ids has duplicates")
        void shouldReturnListOfUsersWithDuplicates() {
            List<User> expectedUsers = support.createListOfUsers();

            List<User> actualUsers = userRepository.findAllByIds(List.of(expectedUsers.get(0).getId(), expectedUsers.get(1).getId(), expectedUsers.get(1).getId()));

            assertThat(actualUsers.size()).isEqualTo(expectedUsers.size());
            assertThat(actualUsers).isEqualTo(expectedUsers);
        }
    }
}
