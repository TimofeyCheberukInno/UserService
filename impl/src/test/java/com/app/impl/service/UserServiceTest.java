package com.app.impl.service;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.app.impl.exception.UserNotFoundException;
import com.app.impl.dto.userDtos.UserDto;
import com.app.impl.service.impl.UserServiceImpl;
import com.app.impl.infrastructure.cache.UserCacheService;
import com.app.impl.mapper.UserMapper;
import com.app.impl.repository.UserRepository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

import static com.app.impl.service.support.UserServiceTestConstants.USER_ID_VALUE;
import static com.app.impl.service.support.UserServiceTestConstants.USER_EMAIL_VALUE;
import static com.app.impl.service.support.UserServiceTestConstants.AMOUNT_OF_UPDATED_USERS;
import static com.app.impl.service.support.UserServiceTestConstants.USER_NOT_FOUND_BY_ID_MSG;
import static com.app.impl.service.support.UserServiceTestConstants.USER_NOT_FOUND_BY_EMAIL_MSG;
import static com.app.impl.service.support.UserServiceTestConstants.LIST_OF_USERS_NOT_FOUND_BY_IDS_MSG;
import static com.app.impl.service.support.UserServiceTestConstants.USER_CREATE_DTO;
import static com.app.impl.service.support.UserServiceTestConstants.MAPPED_USER_ENTITY;
import static com.app.impl.service.support.UserServiceTestConstants.CREATED_USER_DTO;
import static com.app.impl.service.support.UserServiceTestConstants.CREATED_USER_ENTITY;
import static com.app.impl.service.support.UserServiceTestConstants.USER_UPDATE_DTO;
import static com.app.impl.service.support.UserServiceTestConstants.USER_UPDATE_ENTITY;
import static com.app.impl.service.support.UserServiceTestConstants.getInitialListOfIds;
import static com.app.impl.service.support.UserServiceTestConstants.getFullListOfIds;
import static com.app.impl.service.support.UserServiceTestConstants.getEmptyListOfIds;
import static com.app.impl.service.support.UserServiceTestConstants.getEmptyListOfUsers;
import static com.app.impl.service.support.UserServiceTestConstants.getFullListOfUsers;
import static com.app.impl.service.support.UserServiceTestConstants.getFullListOfUserDtos;
import static com.app.impl.service.support.UserServiceTestConstants.getListOfCachedUsers;
import static com.app.impl.service.support.UserServiceTestConstants.getListOfNotCachedUsers;
import static com.app.impl.service.support.UserServiceTestConstants.getListOfNotCachedUserIds;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private UserCacheService userCacheService;

    @InjectMocks
    private UserServiceImpl userService;

    @Nested
    @DisplayName("Tests for create(UserCreateDto dto)")
    class createUserTests {
        @Test
        @DisplayName("returns userDto if user was created")
        void shouldSaveAndReturnUser(){
            Mockito.when(userMapper.toEntity(USER_CREATE_DTO)).thenReturn(MAPPED_USER_ENTITY);
            Mockito.when(userMapper.toDto(CREATED_USER_ENTITY)).thenReturn(CREATED_USER_DTO);
            Mockito.when(userRepository.save(MAPPED_USER_ENTITY)).thenReturn(CREATED_USER_ENTITY);

            UserDto expectedValue = CREATED_USER_DTO;

            UserDto actualValue = userService.create(USER_CREATE_DTO);

            assertThat(actualValue).isEqualTo(expectedValue);

            Mockito.verify(userRepository, Mockito.times(1)).save(MAPPED_USER_ENTITY);
            Mockito.verify(userMapper, Mockito.times(1)).toEntity(USER_CREATE_DTO);
            Mockito.verify(userMapper, Mockito.times(1)).toDto(CREATED_USER_ENTITY);
            Mockito.verify(userCacheService, Mockito.times(1)).save(CREATED_USER_ENTITY);
        }
    }

    @Nested
    @DisplayName("Tests for update(UserUpdateDto dto)")
    class updateUserTests {
        @Test
        @DisplayName("returns amount of updated entities")
        void shouldSaveAndReturnAmountUpdatedEntities(){
            Mockito.when(userRepository.findById(USER_ID_VALUE)).thenReturn(Optional.of(CREATED_USER_ENTITY));
            Mockito.when(userMapper.toUpdateEntity(USER_UPDATE_DTO)).thenReturn(USER_UPDATE_ENTITY);
            Mockito.when(userRepository.updateUser(USER_UPDATE_ENTITY)).thenReturn(AMOUNT_OF_UPDATED_USERS);

            int expectedValue = AMOUNT_OF_UPDATED_USERS;

            int actualValue = userService.update(USER_UPDATE_DTO);

            assertThat(actualValue).isEqualTo(expectedValue);

            Mockito.verify(userRepository, Mockito.times(2)).findById(USER_ID_VALUE);
            Mockito.verify(userRepository, Mockito.times(1)).updateUser(USER_UPDATE_ENTITY);
            Mockito.verify(userMapper, Mockito.times(1)).toUpdateEntity(USER_UPDATE_DTO);
            Mockito.verify(userCacheService, Mockito.times(1)).update(CREATED_USER_ENTITY);
        }

        @Test
        @DisplayName("throws UserNotFoundException")
        void shouldThrowUserNotFoundException(){
            Mockito.when(userRepository.findById(USER_ID_VALUE)).thenReturn(Optional.empty());

            assertThatExceptionOfType(UserNotFoundException.class).isThrownBy(() -> { userService.update(USER_UPDATE_DTO); })
                    .withMessage(USER_NOT_FOUND_BY_ID_MSG, USER_ID_VALUE);

            Mockito.verify(userRepository, Mockito.times(1)).findById(USER_ID_VALUE);
            Mockito.verify(userRepository, Mockito.never()).updateUser(Mockito.any());
            Mockito.verify(userMapper, Mockito.never()).toUpdateEntity(Mockito.any());
            Mockito.verify(userCacheService, Mockito.never()).update(Mockito.any());
        }
    }

    @Nested
    @DisplayName("Tests for delete(Long id)")
    class deleteUserTests {
        @Test
        @DisplayName("successfully deletes user")
        void shouldDeleteUser(){
            Mockito.when(userRepository.findById(USER_ID_VALUE)).thenReturn(Optional.of(CREATED_USER_ENTITY));

            userService.delete(USER_ID_VALUE);

            Mockito.verify(userRepository, Mockito.times(1)).findById(USER_ID_VALUE);
            Mockito.verify(userRepository, Mockito.times(1)).deleteById(USER_ID_VALUE);
            Mockito.verify(userCacheService, Mockito.times(1)).delete(USER_ID_VALUE, USER_EMAIL_VALUE);
        }

        @Test
        @DisplayName("throws UserNotFoundException")
        void shouldThrowUserNotFoundException(){
            Mockito.when(userRepository.findById(USER_ID_VALUE)).thenReturn(Optional.empty());

            assertThatExceptionOfType(UserNotFoundException.class).isThrownBy(() -> { userService.delete(USER_ID_VALUE); })
                    .withMessage(USER_NOT_FOUND_BY_ID_MSG, USER_ID_VALUE);

            Mockito.verify(userRepository, Mockito.times(1)).findById(USER_ID_VALUE);
            Mockito.verify(userRepository, Mockito.never()).deleteById(Mockito.any());
            Mockito.verify(userCacheService, Mockito.never()).delete(Mockito.any(), Mockito.any());
        }
    }

    @Nested
    @DisplayName("Tests for findById(Long id) and findByEmail(String email)")
    class findUserBy {
        @Test
        @DisplayName("returns user by id from cache")
        void shouldReturnUserByIdFromCache(){
            Mockito.when(userCacheService.getById(USER_ID_VALUE)).thenReturn(Optional.of(CREATED_USER_ENTITY));
            Mockito.when(userMapper.toDto(CREATED_USER_ENTITY)).thenReturn(CREATED_USER_DTO);

            UserDto expectedUserDto = CREATED_USER_DTO;

            UserDto actualUserDto = userService.findById(USER_ID_VALUE);

            assertThat(actualUserDto).isEqualTo(expectedUserDto);

            Mockito.verify(userCacheService, Mockito.times(1)).getById(USER_ID_VALUE);
            Mockito.verify(userMapper, Mockito.times(1)).toDto(CREATED_USER_ENTITY);
            Mockito.verify(userRepository, Mockito.never()).findById(Mockito.any());
        }

        @Test
        @DisplayName("returns user by id from db")
        void shouldReturnUserByIdFromDB(){
            Mockito.when(userCacheService.getById(USER_ID_VALUE)).thenReturn(Optional.empty());
            Mockito.when(userMapper.toDto(CREATED_USER_ENTITY)).thenReturn(CREATED_USER_DTO);
            Mockito.when(userRepository.findById(USER_ID_VALUE)).thenReturn(Optional.of(CREATED_USER_ENTITY));

            UserDto expectedUserDto = CREATED_USER_DTO;

            UserDto actualUserDto = userService.findById(USER_ID_VALUE);

            assertThat(actualUserDto).isEqualTo(expectedUserDto);

            Mockito.verify(userCacheService, Mockito.times(1)).getById(USER_ID_VALUE);
            Mockito.verify(userMapper, Mockito.times(1)).toDto(CREATED_USER_ENTITY);
            Mockito.verify(userRepository, Mockito.times(1)).findById(USER_ID_VALUE);
        }

        @Test
        @DisplayName("returns UserNotFoundException while searching by id")
        void shouldThrowUserNotFoundExceptionWhileFindingById(){
            Mockito.when(userCacheService.getById(USER_ID_VALUE)).thenReturn(Optional.empty());
            Mockito.when(userRepository.findById(USER_ID_VALUE)).thenReturn(Optional.empty());

            assertThatExceptionOfType(UserNotFoundException.class).isThrownBy(() -> { userService.findById(USER_ID_VALUE); })
                    .withMessage(USER_NOT_FOUND_BY_ID_MSG, USER_ID_VALUE);

            Mockito.verify(userCacheService, Mockito.times(1)).getById(USER_ID_VALUE);
            Mockito.verify(userRepository, Mockito.times(1)).findById(Mockito.any());
            Mockito.verify(userMapper, Mockito.never()).toDto(Mockito.any());
        }

        @Test
        @DisplayName("returns user by email from cache")
        void shouldReturnUserByEmailFromCache(){
            Mockito.when(userCacheService.getByEmail(USER_EMAIL_VALUE)).thenReturn(Optional.of(CREATED_USER_ENTITY));
            Mockito.when(userMapper.toDto(CREATED_USER_ENTITY)).thenReturn(CREATED_USER_DTO);

            UserDto expectedUserDto = CREATED_USER_DTO;

            UserDto actualUserDto = userService.findByEmail(USER_EMAIL_VALUE);

            assertThat(actualUserDto).isEqualTo(expectedUserDto);

            Mockito.verify(userCacheService, Mockito.times(1)).getByEmail(USER_EMAIL_VALUE);
            Mockito.verify(userMapper, Mockito.times(1)).toDto(CREATED_USER_ENTITY);
            Mockito.verify(userRepository, Mockito.never()).findByEmail(Mockito.any());
        }

        @Test
        @DisplayName("returns user by email from db")
        void shouldReturnUserByEmailFromDB(){
            Mockito.when(userCacheService.getByEmail(USER_EMAIL_VALUE)).thenReturn(Optional.empty());
            Mockito.when(userMapper.toDto(CREATED_USER_ENTITY)).thenReturn(CREATED_USER_DTO);
            Mockito.when(userRepository.findByEmail(USER_EMAIL_VALUE)).thenReturn(Optional.of(CREATED_USER_ENTITY));

            UserDto expectedUserDto = CREATED_USER_DTO;

            UserDto actualUserDto = userService.findByEmail(USER_EMAIL_VALUE);

            assertThat(actualUserDto).isEqualTo(expectedUserDto);

            Mockito.verify(userCacheService, Mockito.times(1)).getByEmail(USER_EMAIL_VALUE);
            Mockito.verify(userMapper, Mockito.times(1)).toDto(CREATED_USER_ENTITY);
            Mockito.verify(userRepository, Mockito.times(1)).findByEmail(USER_EMAIL_VALUE);
        }

        @Test
        @DisplayName("returns UserNotFoundException while searching by email")
        void shouldThrowUserNotFoundExceptionWhileFindingByEmail(){
            Mockito.when(userCacheService.getByEmail(USER_EMAIL_VALUE)).thenReturn(Optional.empty());
            Mockito.when(userRepository.findByEmail(USER_EMAIL_VALUE)).thenReturn(Optional.empty());

            assertThatExceptionOfType(UserNotFoundException.class).isThrownBy(() -> { userService.findByEmail(USER_EMAIL_VALUE); })
                    .withMessage(USER_NOT_FOUND_BY_EMAIL_MSG, USER_EMAIL_VALUE);

            Mockito.verify(userCacheService, Mockito.times(1)).getByEmail(USER_EMAIL_VALUE);
            Mockito.verify(userRepository, Mockito.times(1)).findByEmail(Mockito.any());
            Mockito.verify(userMapper, Mockito.never()).toDto(Mockito.any());
        }
    }

    @Nested
    @DisplayName("Tests for findByIds(Collection<Long> ids) and findAll()")
    class findListOfUsersBy {
        @Test
        @DisplayName("returns list of users by ids from cache")
        void shouldReturnListOfUsersByIdsFromCache(){
            Mockito.when(userCacheService.getByIds(getInitialListOfIds())).thenReturn(getFullListOfUsers());
            Mockito.when(userRepository.findAllByIds(getEmptyListOfIds())).thenReturn(getEmptyListOfUsers());
            Mockito.when(userMapper.toDtoList(getFullListOfUsers())).thenReturn(getFullListOfUserDtos());

            List<UserDto> expectedValues = getFullListOfUserDtos();

            List<UserDto> actualValues = userService.findByIds(getInitialListOfIds());

            Assertions.assertThat(actualValues).containsExactlyInAnyOrderElementsOf(expectedValues);

            Mockito.verify(userCacheService, Mockito.times(1)).getByIds(getInitialListOfIds());
            Mockito.verify(userRepository, Mockito.times(1)).findAllByIds(getEmptyListOfIds());
            Mockito.verify(userMapper, Mockito.times(1)).toDtoList(getFullListOfUsers());
        }

        @Test
        @DisplayName("returns list of users by ids from DB")
        void shouldReturnListOfUsersByIdsFromDB(){
            Mockito.when(userCacheService.getByIds(getInitialListOfIds())).thenReturn(getEmptyListOfUsers());
            Mockito.when(userRepository.findAllByIds(getFullListOfIds())).thenReturn(getFullListOfUsers());
            Mockito.when(userMapper.toDtoList(getFullListOfUsers())).thenReturn(getFullListOfUserDtos());

            List<UserDto> expectedValues = getFullListOfUserDtos();

            List<UserDto> actualValues = userService.findByIds(getInitialListOfIds());

            assertThat(actualValues).isEqualTo(expectedValues);

            Mockito.verify(userCacheService, Mockito.times(1)).getByIds(getInitialListOfIds());
            Mockito.verify(userRepository, Mockito.times(1)).findAllByIds(getFullListOfIds());
            Mockito.verify(userMapper, Mockito.times(1)).toDtoList(getFullListOfUsers());
        }

        @Test
        @DisplayName("return list of users by ids partially from cache and DB")
        void shouldReturnListOfUsersByIdsFromCacheAndDB(){
            Mockito.when(userCacheService.getByIds(getInitialListOfIds())).thenReturn(getListOfCachedUsers());
            Mockito.when(userRepository.findAllByIds(getListOfNotCachedUserIds())).thenReturn(getListOfNotCachedUsers());
            Mockito.when(userMapper.toDtoList(getFullListOfUsers())).thenReturn(getFullListOfUserDtos());

            List<UserDto> expectedValues = getFullListOfUserDtos();

            List<UserDto> actualValues = userService.findByIds(getInitialListOfIds());

            Assertions.assertThat(actualValues).containsExactlyInAnyOrderElementsOf(expectedValues);

            Mockito.verify(userCacheService, Mockito.times(1)).getByIds(getInitialListOfIds());
            Mockito.verify(userRepository, Mockito.times(1)).findAllByIds(getListOfNotCachedUserIds());
            Mockito.verify(userMapper, Mockito.times(1)).toDtoList(getFullListOfUsers());
        }

        @Test
        @DisplayName("throws UserNotFoundException")
        void shouldThrowUserNotFoundException(){
            Mockito.when(userCacheService.getByIds(getInitialListOfIds())).thenReturn(getListOfCachedUsers());
            Mockito.when(userRepository.findAllByIds(getListOfNotCachedUserIds())).thenReturn(getEmptyListOfUsers());

            assertThatExceptionOfType(UserNotFoundException.class).isThrownBy(() -> { userService.findByIds(getInitialListOfIds()); })
                    .withMessage(LIST_OF_USERS_NOT_FOUND_BY_IDS_MSG + getListOfNotCachedUserIds());

            Mockito.verify(userCacheService, Mockito.times(1)).getByIds(getInitialListOfIds());
            Mockito.verify(userRepository, Mockito.times(1)).findAllByIds(getListOfNotCachedUserIds());
            Mockito.verify(userMapper, Mockito.never()).toDtoList(Mockito.any());
        }
    }
}