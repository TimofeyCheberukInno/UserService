package com.app.impl.integration.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.app.impl.entity.User;
import com.app.impl.integration.support.UserITSupport;
import com.app.impl.dto.userDtos.UserUpdateDto;
import com.app.impl.dto.userDtos.UserCreateDto;
import com.app.impl.infrastructure.cache.UserCacheService;
import com.app.impl.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
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
    @DisplayName("Tests for createUser(UserCreateDto dto)")
    class createUserTests {
        @Test
        @DisplayName("returns 201 status")
        void shouldReturnCreatedUser() throws Exception {
            UserCreateDto dto = support.createUserCreateDto();

            mockMvc.perform(post("/api/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").exists())
                    .andExpect(jsonPath("$.name").value(dto.name()))
                    .andExpect(jsonPath("$.surname").value(dto.surname()))
                    .andExpect(jsonPath("$.birthDate").value(dto.birthDate().toString()))
                    .andExpect(jsonPath("$.email").value(dto.email()));
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
    @DisplayName("Tests for updateUser(UpdateUserDto dto)")
    class updateUserTests {
        @Test
        @DisplayName("returns 200 status")
        void shouldReturnAmountOfUpdatedUsers() throws Exception {
            User user = userRepository.save(support.createUser());
            UserUpdateDto dto = support.createUserUpdateDto(user.getId());

            mockMvc.perform(put("/api/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isOk())
                    .andExpect(content().string("1"));
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
    @DisplayName("Tests for deleteUser(Long id)")
    class deleteUserTests {
        @Test
        @DisplayName("returns 200 status")
        void shouldReturnCreatedUser() throws Exception {
            User user = userRepository.save(support.createUser());

            mockMvc.perform(delete("/api/users/" + user.getId()))
                    .andExpect(status().isNoContent());

            assertThat(userRepository.findById(user.getId())).isEmpty();
        }

        @Test
        @DisplayName("returns 404 status because user was not found")
        void shouldReturnNotFoundBecauseUserIsNotFound() throws Exception {
            mockMvc.perform(delete("/api/users/" + 1))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("returns 400 status because param is invalid")
        void shouldReturnBadRequestBecauseParamIsInvalid() throws Exception {
            mockMvc.perform(delete("/api/users/" + -1))
                    .andExpect(status().isBadRequest());
        }
    }
}
