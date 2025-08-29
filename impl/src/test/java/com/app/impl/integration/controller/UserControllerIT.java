package com.app.impl.integration.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.app.impl.dto.userDtos.UserDto;
import com.app.impl.entity.User;
import com.app.impl.integration.support.UserITSupport;
import com.app.impl.dto.userDtos.UserUpdateDto;
import com.app.impl.dto.userDtos.UserCreateDto;
import com.app.impl.infrastructure.cache.UserCacheService;
import com.app.impl.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class UserControllerIT extends BaseControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserCacheService cacheService;
    @Autowired
    private UserITSupport support;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        cacheService.deleteAll();
    }

    @Nested
    @DisplayName("Integration tests for createUser(UserCreateDto dto)")
    class createUserIT {
        @Test
        @DisplayName("returns 201 status")
        void shouldReturnCreatedUser() throws Exception {
            UserCreateDto dto = support.createUserCreateDto();

            MvcResult result = mockMvc.perform(post("/api/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").exists())
                    .andExpect(jsonPath("$.name").value(dto.name()))
                    .andExpect(jsonPath("$.surname").value(dto.surname()))
                    .andExpect(jsonPath("$.birthDate").value(dto.birthDate().toString()))
                    .andExpect(jsonPath("$.email").value(dto.email()))
                    .andReturn();

            String json = result.getResponse().getContentAsString();
            UserDto userDto = objectMapper.readValue(json, UserDto.class);

            Optional<User> userById = cacheService.getById(userDto.id());
            assertThat(userById).isPresent();

            Optional<User> userByEmail = cacheService.getByEmail(userDto.email());
            assertThat(userByEmail).isPresent();

            assertThat(userById.get()).isEqualTo(userByEmail.get());
            assertThat(userById.get().getName()).isEqualTo(dto.name());
            assertThat(userById.get().getSurname()).isEqualTo(dto.surname());
            assertThat(userById.get().getBirthDate()).isEqualTo(dto.birthDate());
            assertThat(userById.get().getEmail()).isEqualTo(dto.email());
        }

        @Test
        @DisplayName("returns 400 status because dto is invalid")
        void shouldReturnBadRequestBecauseDtoIsInvalid() throws Exception {
            UserCreateDto dto = support.createInvalidUserCreateDto();

            mockMvc.perform(post("/api/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").exists());
        }

        @Test
        @DisplayName("returns 400 status because dto is absent")
        void shouldReturnBadRequestBecauseDtoIsAbsent() throws Exception {
            mockMvc.perform(post("/api/users"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").exists());
        }
    }

    @Nested
    @DisplayName("Integration tests for updateUser(UpdateUserDto dto)")
    class updateUserIT {
        @Test
        @DisplayName("returns 200 status")
        void shouldReturnAmountOfUpdatedUsers() throws Exception {
            User user = support.createUser();
            UserUpdateDto dto = support.createUserUpdateDto(user.getId());

            mockMvc.perform(put("/api/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isOk())
                    .andExpect(content().string("1"));

            Optional<User> userById = cacheService.getById(user.getId());
            assertThat(userById).isPresent();

            Optional<User> userByEmail = cacheService.getByEmail(dto.email());
            assertThat(userByEmail).isPresent();

            assertThat(userById.get()).isEqualTo(userByEmail.get());
            assertThat(userById.get().getName()).isEqualTo(dto.name());
            assertThat(userById.get().getSurname()).isEqualTo(dto.surname());
            assertThat(userById.get().getEmail()).isEqualTo(dto.email());
        }

        @Test
        @DisplayName("returns 400 status because dto is invalid")
        void shouldReturnBadRequestBecauseDtoIsInvalid() throws Exception {
            UserUpdateDto dto = support.createInvalidUserUpdateDto();

            mockMvc.perform(put("/api/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").exists());
        }

        @Test
        @DisplayName("returns 400 status because dto is absent")
        void shouldReturnBadRequestBecauseDtoIsAbsent() throws Exception {
            mockMvc.perform(put("/api/users"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").exists());
        }
    }

    @Nested
    @DisplayName("Integration tests for deleteUser(Long id)")
    class deleteUserIT {
        @Test
        @DisplayName("returns 200 status")
        void shouldDeleteUser() throws Exception {
            User user = support.createUser();

            mockMvc.perform(delete("/api/users/" + user.getId()))
                    .andExpect(status().isNoContent());

            assertThat(userRepository.findById(user.getId())).isEmpty();

            Optional<User> userById = cacheService.getById(user.getId());
            assertThat(userById).isEmpty();

            Optional<User> userByEmail = cacheService.getByEmail(user.getEmail());
            assertThat(userByEmail).isEmpty();
        }

        @Test
        @DisplayName("returns 404 status because user was not found")
        void shouldReturnNotFoundBecauseUserIsNotFound() throws Exception {
            mockMvc.perform(delete("/api/users/1"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").exists());
        }

        @Test
        @DisplayName("returns 400 status because param is invalid")
        void shouldReturnBadRequestBecauseParamIsInvalid() throws Exception {
            mockMvc.perform(delete("/api/users/-1"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").exists());
        }
    }

    @Nested
    @DisplayName("Integration tests for getUserById(Long id)")
    class getUserByIdIT {
        @Test
        @DisplayName("returns 200 status")
        void shouldReturnUserById() throws Exception {
            User user = support.createUser();

            mockMvc.perform(get("/api/users/" + user.getId().toString()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").exists())
                    .andExpect(jsonPath("$.name").value(user.getName()))
                    .andExpect(jsonPath("$.surname").value(user.getSurname()))
                    .andExpect(jsonPath("$.birthDate").value(user.getBirthDate().toString()))
                    .andExpect(jsonPath("$.email").value(user.getEmail()));

            Optional<User> userById = cacheService.getById(user.getId());
            assertThat(userById).isEmpty();

            Optional<User> userByEmail = cacheService.getByEmail(user.getEmail());
            assertThat(userByEmail).isEmpty();
        }

        @Test
        @DisplayName("returns 404 status because user was not found")
        void shouldReturnNotFoundBecauseUserIsNotFound() throws Exception {
            mockMvc.perform(get("/api/users/1"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").exists());
        }

        @Test
        @DisplayName("returns 400 status because param is invalid")
        void shouldReturnBadRequestBecauseParamIsInvalid() throws Exception {
            mockMvc.perform(get("/api/users/-1"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").exists());
        }
    }

    @Nested
    @DisplayName("Integration tests for getUserByEmail(String email)")
    class getUserByEmailIT {
        @Test
        @DisplayName("returns 200 status")
        void shouldReturnUserByEmail() throws Exception {
            User user = support.createUser();

            mockMvc.perform(get("/api/users/by-email")
                            .param("email", user.getEmail()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").exists())
                    .andExpect(jsonPath("$.name").value(user.getName()))
                    .andExpect(jsonPath("$.surname").value(user.getSurname()))
                    .andExpect(jsonPath("$.birthDate").value(user.getBirthDate().toString()))
                    .andExpect(jsonPath("$.email").value(user.getEmail()));

            Optional<User> userById = cacheService.getById(user.getId());
            assertThat(userById).isEmpty();

            Optional<User> userByEmail = cacheService.getByEmail(user.getEmail());
            assertThat(userByEmail).isEmpty();
        }

        @Test
        @DisplayName("returns 404 status because user was not found")
        void shouldReturnNotFoundBecauseUserIsNotFound() throws Exception {
            mockMvc.perform(get("/api/users/by-email")
                            .param("email", "example@gmail.com"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").exists());
        }

        @Test
        @DisplayName("returns 400 status because param is invalid")
        void shouldReturnBadRequestBecauseParamIsInvalid() throws Exception {
            mockMvc.perform(get("/api/users/by-email")
                            .param("email", "invalid"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").exists());
        }
    }

    @Nested
    @DisplayName("Integration tests for getListOfUsersByIds(List<Long> ids)")
    class getListOfUsersByIdsIT {
        @Test
        @DisplayName("returns 200 status")
        void shouldReturnUsersByIds() throws Exception {
            List<User> users = support.createListOfUsers();

            mockMvc.perform(get("/api/users/by-ids")
                .param("ids", users.get(0).getId().toString())
                .param("ids", users.get(1).getId().toString()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$.length()").value(2))

            .andExpect(jsonPath("$[0].id").value(users.get(0).getId()))
            .andExpect(jsonPath("$[0].name").value(users.get(0).getName()))
            .andExpect(jsonPath("$[0].surname").value(users.get(0).getSurname()))
            .andExpect(jsonPath("$[0].birthDate").value(users.get(0).getBirthDate().toString()))
            .andExpect(jsonPath("$[0].email").value(users.get(0).getEmail()))

            .andExpect(jsonPath("$[1].id").value(users.get(1).getId()))
            .andExpect(jsonPath("$[1].name").value(users.get(1).getName()))
            .andExpect(jsonPath("$[1].surname").value(users.get(1).getSurname()))
            .andExpect(jsonPath("$[1].birthDate").value(users.get(1).getBirthDate().toString()))
            .andExpect(jsonPath("$[1].email").value(users.get(1).getEmail()));

            List<User> usersById = cacheService.getByIds(List.of(users.get(0).getId(), users.get(1).getId()));
            assertThat(usersById).isNotNull();
            assertThat(usersById).hasSize(0);
        }

        @Test
        @DisplayName("returns 404 status because users were not found")
        void shouldReturnNotFoundBecauseUsersAreNotFound() throws Exception {
            mockMvc.perform(get("/api/users/by-ids")
                            .param("ids", "1")
                            .param("ids", "2"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").exists());
        }

        @Test
        @DisplayName("returns 400 status because params are invalid")
        void shouldReturnBadRequestBecauseParamsAreInvalid() throws Exception {
            mockMvc.perform(get("/api/users/by-ids")
                            .param("ids", "one")
                            .param("ids", "two"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").exists());
        }
    }

    @Nested
    @DisplayName("Integration tests for getAll()")
    class getAllIT {
        @Test
        @DisplayName("returns 200 status")
        void shouldReturnAllUsers() throws Exception {
            List<User> users = support.createListOfUsers();

            mockMvc.perform(get("/api/users"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$.length()").value(2))

                    .andExpect(jsonPath("$[0].id").value(users.get(0).getId()))
                    .andExpect(jsonPath("$[0].name").value(users.get(0).getName()))
                    .andExpect(jsonPath("$[0].surname").value(users.get(0).getSurname()))
                    .andExpect(jsonPath("$[0].birthDate").value(users.get(0).getBirthDate().toString()))
                    .andExpect(jsonPath("$[0].email").value(users.get(0).getEmail()))

                    .andExpect(jsonPath("$[1].id").value(users.get(1).getId()))
                    .andExpect(jsonPath("$[1].name").value(users.get(1).getName()))
                    .andExpect(jsonPath("$[1].surname").value(users.get(1).getSurname()))
                    .andExpect(jsonPath("$[1].birthDate").value(users.get(1).getBirthDate().toString()))
                    .andExpect(jsonPath("$[1].email").value(users.get(1).getEmail()));

            List<User> usersById = cacheService.getByIds(List.of(users.get(0).getId(), users.get(1).getId()));
            assertThat(usersById).isNotNull();
            assertThat(usersById).hasSize(0);
        }

        @Test
        @DisplayName("returns empty list because there are no users")
        void shouldReturnEmptyListBecauseThereAreNoUsers() throws Exception {
            mockMvc.perform(get("/api/users"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$.length()").value(0));
        }
    }
}
