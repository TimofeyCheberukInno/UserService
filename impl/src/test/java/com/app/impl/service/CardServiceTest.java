package com.app.impl.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import static com.app.impl.service.support.CardServiceTestConstants.AMOUNT_OF_UPDATED_ENTITIES;
import static com.app.impl.service.support.CardServiceTestConstants.CARD_ENTITY_ID;
import static com.app.impl.service.support.CardServiceTestConstants.CARD_NOT_FOUND_BY_ID_MSG;
import static com.app.impl.service.support.CardServiceTestConstants.CARD_USER_ID;
import static com.app.impl.service.support.CardServiceTestConstants.LIST_OF_CARDS_NOT_FOUND_BY_IDS_MSG;
import static com.app.impl.service.support.CardServiceTestConstants.USER_NOT_FOUND_BY_ID_MSG;
import static com.app.impl.service.support.CardServiceTestConstants.getCard1;
import static com.app.impl.service.support.CardServiceTestConstants.getCard1WithUser;
import static com.app.impl.service.support.CardServiceTestConstants.getCard2;
import static com.app.impl.service.support.CardServiceTestConstants.getCard2WithUser;
import static com.app.impl.service.support.CardServiceTestConstants.getCard3;
import static com.app.impl.service.support.CardServiceTestConstants.getCard3WithUser;
import static com.app.impl.service.support.CardServiceTestConstants.getCardDto;
import static com.app.impl.service.support.CardServiceTestConstants.getCardDto1;
import static com.app.impl.service.support.CardServiceTestConstants.getCardDto1WithUser;
import static com.app.impl.service.support.CardServiceTestConstants.getCardDto2;
import static com.app.impl.service.support.CardServiceTestConstants.getCardDto2WithUser;
import static com.app.impl.service.support.CardServiceTestConstants.getCardDto3;
import static com.app.impl.service.support.CardServiceTestConstants.getCardDto3WithUser;
import static com.app.impl.service.support.CardServiceTestConstants.getCardEntityToUpdate;
import static com.app.impl.service.support.CardServiceTestConstants.getCardWithUserDto;
import static com.app.impl.service.support.CardServiceTestConstants.getCreateCardDto;
import static com.app.impl.service.support.CardServiceTestConstants.getCreatedCardEntity;
import static com.app.impl.service.support.CardServiceTestConstants.getEmptyListOfCardDtos;
import static com.app.impl.service.support.CardServiceTestConstants.getEmptyListOfCardDtosWithUsers;
import static com.app.impl.service.support.CardServiceTestConstants.getEmptyListOfCards;
import static com.app.impl.service.support.CardServiceTestConstants.getEmptyListOfIds;
import static com.app.impl.service.support.CardServiceTestConstants.getFullListOfCardDtos;
import static com.app.impl.service.support.CardServiceTestConstants.getFullListOfCardDtosWithUsers;
import static com.app.impl.service.support.CardServiceTestConstants.getFullListOfCardsWithUsers;
import static com.app.impl.service.support.CardServiceTestConstants.getFullListOfCardsWithoutUsers;
import static com.app.impl.service.support.CardServiceTestConstants.getFullListOfIds;
import static com.app.impl.service.support.CardServiceTestConstants.getMappedCardEntity;
import static com.app.impl.service.support.CardServiceTestConstants.getMappedCardEntityWithUser;
import static com.app.impl.service.support.CardServiceTestConstants.getUpdateCardDto;
import static com.app.impl.service.support.CardServiceTestConstants.getUserEntity;

import java.util.ArrayList;
import java.util.Arrays;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.app.impl.dto.cardDtos.CardDto;
import com.app.impl.dto.cardDtos.CardWithUserDto;
import com.app.impl.exception.CardNotFoundException;
import com.app.impl.exception.UserNotFoundException;
import com.app.impl.infrastructure.cache.CardCacheService;
import com.app.impl.mapper.CardMapper;
import com.app.impl.repository.CardRepository;
import com.app.impl.repository.UserRepository;
import com.app.impl.service.impl.CardServiceImpl;

@ExtendWith(SpringExtension.class)
public class CardServiceTest {
	@Mock
	CardRepository cardRepository;

	@Mock
	UserRepository userRepository;

	@Mock
	CardMapper cardMapper;

	@Mock
	CardCacheService cardCacheService;

	@InjectMocks
	CardServiceImpl cardService;

	@Nested
	@DisplayName("Tests for create(CardCreateDto dto)")
	class createCardTests {
		@Test
		@DisplayName("returns dto if card was successfully created")
		void shouldReturnDtoAfterCreatingCard() {
			Mockito.when(userRepository.findById(CARD_USER_ID))
                    .thenReturn(Optional.of(getUserEntity()));
			Mockito.when(cardMapper.toEntity(getCreateCardDto()))
                    .thenReturn(getMappedCardEntity());
			Mockito.when(cardRepository.save(getMappedCardEntityWithUser()))
                    .thenReturn(getCreatedCardEntity());
			Mockito.when(cardMapper.toDto(getCreatedCardEntity()))
                    .thenReturn(getCardDto());

			CardDto expectedValue = getCardDto();

			CardDto actualValue = cardService.create(getCreateCardDto());

			assertThat(actualValue).isEqualTo(expectedValue);

			Mockito.verify(userRepository, Mockito.times(1))
                    .findById(CARD_USER_ID);
			Mockito.verify(cardMapper, Mockito.times(1))
                    .toEntity(getCreateCardDto());
			Mockito.verify(cardRepository, Mockito.times(1))
                    .save(getMappedCardEntityWithUser());
			Mockito.verify(cardMapper, Mockito.times(1))
                    .toDto(getCreatedCardEntity());
			Mockito.verify(cardCacheService, Mockito.times(1))
                    .saveCardWithoutUser(getCardDto());
		}

		@Test
		@DisplayName("throws UserNotFoundException")
		void shouldThrowUserNotFoundException() {
			Mockito.when(userRepository.findById(CARD_USER_ID))
                    .thenReturn(Optional.empty());

			Assertions.assertThatExceptionOfType(UserNotFoundException.class)
                    .isThrownBy(() -> cardService.create(getCreateCardDto()))
                    .withMessage(USER_NOT_FOUND_BY_ID_MSG, CARD_USER_ID);

			Mockito.verify(userRepository, Mockito.times(1))
                    .findById(CARD_USER_ID);
			Mockito.verify(cardMapper, Mockito.never())
                    .toEntity(Mockito.any());
			Mockito.verify(cardRepository, Mockito.never())
                    .save(Mockito.any());
			Mockito.verify(cardMapper, Mockito.never())
                    .toDto(Mockito.any());
			Mockito.verify(cardCacheService, Mockito.never())
                    .saveCardWithoutUser(Mockito.any());
		}
	}

	@Nested
	@DisplayName("Tests for update(CardUpdatedDto dto)")
	class updateCardTests {
		@Test
		@DisplayName("returns amount of updated cards")
		void shouldReturnAmountOfUpdatedCards() {
			Mockito.when(cardRepository.findById(CARD_ENTITY_ID))
                    .thenReturn(Optional.of(getCreatedCardEntity()));
			Mockito.when(cardMapper.toUpdateEntity(getUpdateCardDto()))
                    .thenReturn(getCardEntityToUpdate());
			Mockito.when(cardRepository.updateCard(getCardEntityToUpdate()))
                    .thenReturn(AMOUNT_OF_UPDATED_ENTITIES);
			Mockito.when(cardMapper.toDto(getCreatedCardEntity()))
                    .thenReturn(getCardDto());
			Mockito.when(cardMapper.toDtoWithUser(getCreatedCardEntity()))
                    .thenReturn(getCardWithUserDto());

			int expectedValue = AMOUNT_OF_UPDATED_ENTITIES;

			int actualValue = cardService.update(getUpdateCardDto());

			assertThat(actualValue).isEqualTo(expectedValue);

			Mockito.verify(cardRepository, Mockito.times(2))
                    .findById(CARD_ENTITY_ID);
			Mockito.verify(cardMapper, Mockito.times(1))
                    .toUpdateEntity(getUpdateCardDto());
			Mockito.verify(cardRepository, Mockito.times(1))
                    .updateCard(getCardEntityToUpdate());
			Mockito.verify(cardCacheService, Mockito.times(1))
                    .updateCardWithoutUser(getCardDto());
			Mockito.verify(cardCacheService, Mockito.times(1))
                    .updateCardWithUser(getCardWithUserDto());
		}

		@Test
		@DisplayName("throws CardNotFoundException")
		void shouldThrowCardNotFoundException() {
			Mockito.when(cardRepository.findById(CARD_ENTITY_ID))
                    .thenReturn(Optional.empty());

			Assertions.assertThatExceptionOfType(CardNotFoundException.class)
                    .isThrownBy(() -> cardService.update(getUpdateCardDto()))
                    .withMessage(CARD_NOT_FOUND_BY_ID_MSG, CARD_ENTITY_ID);

			Mockito.verify(cardRepository, Mockito.times(1))
                    .findById(CARD_ENTITY_ID);
			Mockito.verify(cardMapper, Mockito.never())
                    .toUpdateEntity(Mockito.any());
			Mockito.verify(cardRepository, Mockito.never())
                    .updateCard(Mockito.any());
			Mockito.verify(cardCacheService, Mockito.never())
                    .updateCardWithoutUser(Mockito.any());
			Mockito.verify(cardCacheService, Mockito.never())
                    .updateCardWithUser(Mockito.any());
		}
	}

	@Nested
	@DisplayName("Tests for delete(Long id)")
	class deleteCardTests {
		@Test
		@DisplayName("successfully deletes card")
		void shouldDeleteCard() {
			Mockito.when(cardRepository.findById(CARD_ENTITY_ID))
                    .thenReturn(Optional.of(getCreatedCardEntity()));

			cardService.delete(CARD_ENTITY_ID);

			Mockito.verify(cardRepository, Mockito.times(1))
                    .findById(CARD_ENTITY_ID);
			Mockito.verify(cardRepository, Mockito.times(1))
                    .deleteById(CARD_ENTITY_ID);
			Mockito.verify(cardCacheService, Mockito.times(1))
                    .delete(CARD_ENTITY_ID);
		}

		@Test
		@DisplayName("throws CardNotFoundException")
		void shouldThrowCardNotFoundException() {
			Mockito.when(cardRepository.findById(CARD_ENTITY_ID))
                    .thenReturn(Optional.empty());

			Assertions.assertThatExceptionOfType(CardNotFoundException.class)
                    .isThrownBy(() -> cardService.delete(CARD_ENTITY_ID))
                    .withMessage(CARD_NOT_FOUND_BY_ID_MSG, CARD_ENTITY_ID);

			Mockito.verify(cardRepository, Mockito.times(1))
                    .findById(CARD_ENTITY_ID);
			Mockito.verify(cardRepository, Mockito.never())
                    .deleteById(Mockito.any());
			Mockito.verify(cardCacheService, Mockito.never())
                    .delete(Mockito.any());
		}
	}

	@Nested
	@DisplayName("Tests for findById(Long id) and findByIdWithUser(Long id)")
	class findByIdTests {
		@Test
		@DisplayName("returns card by id from cache")
		void shouldReturnCardByIdFromCache() {
			Mockito.when(cardCacheService.getByIdWithoutUser(CARD_ENTITY_ID))
                    .thenReturn(Optional.of(getCardDto()));

			CardDto expectedValue = getCardDto();

			CardDto actualValue = cardService.findById(CARD_ENTITY_ID);

			assertThat(actualValue).isEqualTo(expectedValue);

			Mockito.verify(cardCacheService, Mockito.times(1))
                    .getByIdWithoutUser(CARD_ENTITY_ID);
			Mockito.verify(cardRepository, Mockito.never())
                    .findById(Mockito.any());
			Mockito.verify(cardMapper, Mockito.never())
                    .toDto(Mockito.any());
		}

		@Test
		@DisplayName("returns card by id from DB")
		void shouldReturnCardByIdFromDB() {
			Mockito.when(cardCacheService.getByIdWithoutUser(CARD_ENTITY_ID))
                    .thenReturn(Optional.empty());
			Mockito.when(cardRepository.findById(CARD_ENTITY_ID))
                    .thenReturn(Optional.of(getCreatedCardEntity()));
			Mockito.when(cardMapper.toDto(getCreatedCardEntity()))
                    .thenReturn(getCardDto());

			CardDto expectedValue = getCardDto();

			CardDto actualValue = cardService.findById(CARD_ENTITY_ID);

			assertThat(actualValue).isEqualTo(expectedValue);

			Mockito.verify(cardCacheService, Mockito.times(1))
                    .getByIdWithoutUser(CARD_ENTITY_ID);
			Mockito.verify(cardRepository, Mockito.times(1))
                    .findById(CARD_ENTITY_ID);
			Mockito.verify(cardMapper, Mockito.times(1))
                    .toDto(getCreatedCardEntity());
		}

		@Test
		@DisplayName("throws CardNotFoundException")
		void shouldThrowCardNotFoundException() {
			Mockito.when(cardCacheService.getByIdWithoutUser(CARD_ENTITY_ID))
                    .thenReturn(Optional.empty());
			Mockito.when(cardRepository.findById(CARD_ENTITY_ID))
                    .thenReturn(Optional.empty());

			Assertions.assertThatExceptionOfType(CardNotFoundException.class)
                    .isThrownBy(() -> cardService.findById(CARD_ENTITY_ID))
                    .withMessage(CARD_NOT_FOUND_BY_ID_MSG, CARD_ENTITY_ID);

			Mockito.verify(cardCacheService, Mockito.times(1))
                    .getByIdWithoutUser(CARD_ENTITY_ID);
			Mockito.verify(cardRepository, Mockito.times(1))
                    .findById(CARD_ENTITY_ID);
			Mockito.verify(cardMapper, Mockito.never())
                    .toDto(Mockito.any());
		}

		@Test
		@DisplayName("returns card by id with user from cache")
		void shouldReturnCardByIdWithUserFromCache() {
			Mockito.when(cardCacheService.getByIdWithUser(CARD_ENTITY_ID))
					.thenReturn(Optional.of(getCardWithUserDto()));

			CardWithUserDto expectedValue = getCardWithUserDto();

			CardWithUserDto actualValue = cardService.findByIdWithUser(CARD_ENTITY_ID);

			assertThat(actualValue).isEqualTo(expectedValue);

			Mockito.verify(cardCacheService, Mockito.times(1))
                    .getByIdWithUser(CARD_ENTITY_ID);
			Mockito.verify(cardRepository, Mockito.never())
                    .findByIdWithUser(Mockito.any());
			Mockito.verify(cardMapper, Mockito.never())
                    .toDtoWithUser(Mockito.any());
		}

		@Test
		@DisplayName("returns card by id with user from DB")
		void shouldReturnCardByIdWithUserFromDB() {
			Mockito.when(cardCacheService.getByIdWithUser(CARD_ENTITY_ID))
                    .thenReturn(Optional.empty());
			Mockito.when(cardRepository.findByIdWithUser(CARD_ENTITY_ID))
					.thenReturn(Optional.of(getCreatedCardEntity()));
			Mockito.when(cardMapper.toDtoWithUser(getCreatedCardEntity()))
                    .thenReturn(getCardWithUserDto());

			CardWithUserDto expectedValue = getCardWithUserDto();

			CardWithUserDto actualValue = cardService.findByIdWithUser(CARD_ENTITY_ID);

			assertThat(actualValue).isEqualTo(expectedValue);

			Mockito.verify(cardCacheService, Mockito.times(1))
                    .getByIdWithUser(CARD_ENTITY_ID);
			Mockito.verify(cardRepository, Mockito.times(1))
                    .findByIdWithUser(CARD_ENTITY_ID);
			Mockito.verify(cardMapper, Mockito.times(1))
                    .toDtoWithUser(getCreatedCardEntity());
		}

		@Test
		@DisplayName("throws CardNotFoundException in findByIdWithUser(Long id)")
		void shouldThrowCardNotFoundExceptionWithUser() {
			Mockito.when(cardCacheService.getByIdWithUser(CARD_ENTITY_ID))
                    .thenReturn(Optional.empty());
			Mockito.when(cardRepository.findByIdWithUser(CARD_ENTITY_ID))
                    .thenReturn(Optional.empty());

			Assertions.assertThatExceptionOfType(CardNotFoundException.class)
                    .isThrownBy(() -> cardService.findByIdWithUser(CARD_ENTITY_ID))
                    .withMessage(CARD_NOT_FOUND_BY_ID_MSG, CARD_ENTITY_ID);

			Mockito.verify(cardCacheService, Mockito.times(1))
                    .getByIdWithUser(CARD_ENTITY_ID);
			Mockito.verify(cardRepository, Mockito.times(1))
                    .findByIdWithUser(CARD_ENTITY_ID);
			Mockito.verify(cardMapper, Mockito.never())
                    .toDtoWithUser(Mockito.any());
		}
	}

	@Nested
	@DisplayName("Tests for findByIds(Collection<Long> ids) and findByIdsWithUser(Collection<Long> ids)")
	class findByIdsTests {
		@Test
		@DisplayName("returns list of cards from cache")
		void shouldReturnListOfCardsFromCache() {
			Mockito.when(cardCacheService.getByIdsWithoutUser(getFullListOfIds()))
                    .thenReturn(getFullListOfCardDtos());
			Mockito.when(cardRepository.findAllByIds(getEmptyListOfIds()))
                    .thenReturn(getEmptyListOfCards());

			List<CardDto> expectedValues = getFullListOfCardDtos();

			List<CardDto> actualValues = cardService.findByIds(getFullListOfIds());

			Assertions.assertThat(actualValues).containsExactlyInAnyOrderElementsOf(expectedValues);

			Mockito.verify(cardCacheService, Mockito.times(1))
                    .getByIdsWithoutUser(getFullListOfIds());
			Mockito.verify(cardRepository, Mockito.times(1))
                    .findAllByIds(getEmptyListOfIds());
			Mockito.verify(cardMapper, Mockito.never())
                    .toDto(Mockito.any());
		}

		@Test
		@DisplayName("returns list of cards from DB")
		void shouldReturnListOfCardsFromDB() {
			Mockito.when(cardCacheService.getByIdsWithoutUser(getFullListOfIds()))
                    .thenReturn(getEmptyListOfCardDtos());
			Mockito.when(cardRepository.findAllByIds(getFullListOfIds()))
                    .thenReturn(getFullListOfCardsWithoutUsers());
			Mockito.when(cardMapper.toDto(getCard1()))
                    .thenReturn(getCardDto1());
			Mockito.when(cardMapper.toDto(getCard2()))
                    .thenReturn(getCardDto2());
			Mockito.when(cardMapper.toDto(getCard3()))
                    .thenReturn(getCardDto3());

			List<CardDto> expectedValues = getFullListOfCardDtos();

			List<CardDto> actualValues = cardService.findByIds(getFullListOfIds());

			Assertions.assertThat(actualValues).containsExactlyInAnyOrderElementsOf(expectedValues);

			Mockito.verify(cardCacheService, Mockito.times(1))
                    .getByIdsWithoutUser(getFullListOfIds());
			Mockito.verify(cardRepository, Mockito.times(1))
                    .findAllByIds(getFullListOfIds());
			Mockito.verify(cardMapper, Mockito.times(3))
                    .toDto(Mockito.any());
		}

		@Test
		@DisplayName("returns list of cards partially from cache and partially from DB")
		void shouldReturnListOfCardsPartiallyFromCacheAndDB() {
			Mockito.when(cardCacheService.getByIdsWithoutUser(getFullListOfIds()))
					.thenReturn(new ArrayList<>(Arrays.asList(getCardDto2(), getCardDto3())));
			Mockito.when(cardRepository.findAllByIds(new ArrayList<>(Arrays.asList(1L))))
					.thenReturn(new ArrayList<>(Arrays.asList(getCard1())));
			Mockito.when(cardMapper.toDto(getCard1()))
                    .thenReturn(getCardDto1());

			List<CardDto> expectedValues = getFullListOfCardDtos();

			List<CardDto> actualValues = cardService.findByIds(getFullListOfIds());

			Assertions.assertThat(actualValues).containsExactlyInAnyOrderElementsOf(expectedValues);

			Mockito.verify(cardCacheService, Mockito.times(1))
                    .getByIdsWithoutUser(getFullListOfIds());
			Mockito.verify(cardRepository, Mockito.times(1))
                    .findAllByIds(new ArrayList<>(Arrays.asList(1L)));
			Mockito.verify(cardMapper, Mockito.times(1))
                    .toDto(getCard1());
		}

		@Test
		@DisplayName("throws CardNotFoundException")
		void shouldThrowCardNotFoundException() {
			Mockito.when(cardCacheService.getByIdsWithoutUser(getFullListOfIds()))
					.thenReturn(new ArrayList<>(Arrays.asList(getCardDto1())));
			Mockito.when(cardRepository.findAllByIds(new ArrayList<>(Arrays.asList(2L, 3L))))
					.thenReturn(new ArrayList<>(Arrays.asList(getCard2())));
			Mockito.when(cardMapper.toDto(getCard2()))
                    .thenReturn(getCardDto2());

			Assertions.assertThatExceptionOfType(CardNotFoundException.class)
                    .isThrownBy(() -> cardService.findByIds(getFullListOfIds()))
            .withMessage(LIST_OF_CARDS_NOT_FOUND_BY_IDS_MSG + Arrays.asList(3L));

			Mockito.verify(cardCacheService, Mockito.times(1))
                    .getByIdsWithoutUser(getFullListOfIds());
			Mockito.verify(cardRepository, Mockito.times(1))
                    .findAllByIds(new ArrayList<>(Arrays.asList(2L, 3L)));
			Mockito.verify(cardMapper, Mockito.times(1))
                    .toDto(Mockito.any());
		}

		@Test
		@DisplayName("returns list of cards with users from cache")
		void shouldReturnListOfCardsWithUsersFromCache() {
			Mockito.when(cardCacheService.getByIdsWithUser(getFullListOfIds()))
					.thenReturn(getFullListOfCardDtosWithUsers());
			Mockito.when(cardRepository.findAllByIdsWithUser(getEmptyListOfIds()))
                    .thenReturn(getEmptyListOfCards());

			List<CardWithUserDto> expectedValues = getFullListOfCardDtosWithUsers();

			List<CardWithUserDto> actualValues = cardService.findByIdsWithUser(getFullListOfIds());

			Assertions.assertThat(actualValues).containsExactlyInAnyOrderElementsOf(expectedValues);

			Mockito.verify(cardCacheService, Mockito.times(1))
                    .getByIdsWithUser(getFullListOfIds());
			Mockito.verify(cardRepository, Mockito.times(1))
                    .findAllByIdsWithUser(getEmptyListOfIds());
			Mockito.verify(cardMapper, Mockito.never())
                    .toDtoWithUser(Mockito.any());
		}

		@Test
		@DisplayName("returns list of cards with users from DB")
		void shouldReturnListOfCardsWithUsersFromDB() {
			Mockito.when(cardCacheService.getByIdsWithUser(getFullListOfIds()))
					.thenReturn(getEmptyListOfCardDtosWithUsers());
			Mockito.when(cardRepository.findAllByIdsWithUser(getFullListOfIds()))
					.thenReturn(getFullListOfCardsWithUsers());
			Mockito.when(cardMapper.toDtoWithUser(getCard1WithUser()))
                    .thenReturn(getCardDto1WithUser());
			Mockito.when(cardMapper.toDtoWithUser(getCard2WithUser()))
                    .thenReturn(getCardDto2WithUser());
			Mockito.when(cardMapper.toDtoWithUser(getCard3WithUser()))
                    .thenReturn(getCardDto3WithUser());

			List<CardWithUserDto> expectedValues = getFullListOfCardDtosWithUsers();

			List<CardWithUserDto> actualValues = cardService.findByIdsWithUser(getFullListOfIds());

			Assertions.assertThat(actualValues).containsExactlyInAnyOrderElementsOf(expectedValues);

			Mockito.verify(cardCacheService, Mockito.times(1))
                    .getByIdsWithUser(getFullListOfIds());
			Mockito.verify(cardRepository, Mockito.times(1))
                    .findAllByIdsWithUser(getFullListOfIds());
			Mockito.verify(cardMapper, Mockito.times(3))
                    .toDtoWithUser(Mockito.any());
		}

		@Test
		@DisplayName("returns list of cards with users partially from cache and partially from DB")
		void shouldReturnListOfCardsWithUsersPartiallyFromCacheAndDB() {
			Mockito.when(cardCacheService.getByIdsWithUser(getFullListOfIds()))
					.thenReturn(new ArrayList<>(Arrays.asList(getCardDto2WithUser(), getCardDto3WithUser())));
			Mockito.when(cardRepository.findAllByIdsWithUser(new ArrayList<>(Arrays.asList(1L))))
					.thenReturn(new ArrayList<>(Arrays.asList(getCard1WithUser())));
			Mockito.when(cardMapper.toDtoWithUser(getCard1WithUser()))
                    .thenReturn(getCardDto1WithUser());

			List<CardWithUserDto> expectedValues = getFullListOfCardDtosWithUsers();

			List<CardWithUserDto> actualValues = cardService.findByIdsWithUser(getFullListOfIds());

			Assertions.assertThat(actualValues).containsExactlyInAnyOrderElementsOf(expectedValues);

			Mockito.verify(cardCacheService, Mockito.times(1))
                    .getByIdsWithUser(getFullListOfIds());
			Mockito.verify(cardRepository, Mockito.times(1))
                    .findAllByIdsWithUser(new ArrayList<>(Arrays.asList(1L)));
			Mockito.verify(cardMapper, Mockito.times(1))
                    .toDtoWithUser(getCard1WithUser());
		}

		@Test
		@DisplayName("throws CardNotFoundException in findByIdsWithUser(Collection<Long> ids)")
		void shouldThrowCardNotFoundExceptionWithUsers() {
			Mockito.when(cardCacheService.getByIdsWithUser(getFullListOfIds()))
					.thenReturn(new ArrayList<>(Arrays.asList(getCardDto1WithUser())));
			Mockito.when(cardRepository.findAllByIdsWithUser(new ArrayList<>(Arrays.asList(2L, 3L))))
					.thenReturn(new ArrayList<>(Arrays.asList(getCard2WithUser())));
			Mockito.when(cardMapper.toDtoWithUser(getCard2WithUser()))
                    .thenReturn(getCardDto2WithUser());

			Assertions.assertThatExceptionOfType(CardNotFoundException.class)
                    .isThrownBy(() -> cardService.findByIdsWithUser(getFullListOfIds()))
                    .withMessage(LIST_OF_CARDS_NOT_FOUND_BY_IDS_MSG + Arrays.asList(3L));

			Mockito.verify(cardCacheService, Mockito.times(1))
                    .getByIdsWithUser(getFullListOfIds());
			Mockito.verify(cardRepository, Mockito.times(1))
					.findAllByIdsWithUser(new ArrayList<>(Arrays.asList(2L, 3L)));
			Mockito.verify(cardMapper, Mockito.times(1))
                    .toDtoWithUser(Mockito.any());
		}
	}

	@Nested
	@DisplayName("Tests for findAll() and findAllWithUser()")
	class findAllTests {
		@Test
		@DisplayName("returns not empty list")
		void shouldReturnNotEmptyList() {
			Mockito.when(cardRepository.findAll())
                    .thenReturn(getFullListOfCardsWithoutUsers());
			Mockito.when(cardMapper.toDtoList(getFullListOfCardsWithoutUsers()))
                    .thenReturn(getFullListOfCardDtos());

			List<CardDto> expectedValues = getFullListOfCardDtos();

			List<CardDto> actualValues = cardService.findAll();

			Assertions.assertThat(actualValues).containsExactlyInAnyOrderElementsOf(expectedValues);

			Mockito.verify(cardRepository, Mockito.times(1))
                    .findAll();
			Mockito.verify(cardMapper, Mockito.times(1))
                    .toDtoList(getFullListOfCardsWithoutUsers());
		}

		@Test
		@DisplayName("returns empty list")
		void shouldReturnEmptyList() {
			Mockito.when(cardRepository.findAll()).thenReturn(getEmptyListOfCards());
			Mockito.when(cardMapper.toDtoList(getEmptyListOfCards()))
                    .thenReturn(getEmptyListOfCardDtos());

			List<CardDto> expectedValues = getEmptyListOfCardDtos();

			List<CardDto> actualValues = cardService.findAll();

			Assertions.assertThat(actualValues).containsExactlyInAnyOrderElementsOf(expectedValues);

			Mockito.verify(cardRepository, Mockito.times(1))
                    .findAll();
			Mockito.verify(cardMapper, Mockito.times(1))
                    .toDtoList(getEmptyListOfCards());
		}

		@Test
		@DisplayName("returns not empty list with users")
		void shouldReturnNotEmptyListWithUsers() {
			Mockito.when(cardRepository.findAllWithUser())
                    .thenReturn(getFullListOfCardsWithUsers());
			Mockito.when(cardMapper.toDtoWithUserList(getFullListOfCardsWithUsers()))
					.thenReturn(getFullListOfCardDtosWithUsers());

			List<CardWithUserDto> expectedValues = getFullListOfCardDtosWithUsers();

			List<CardWithUserDto> actualValues = cardService.findAllWithUser();

			Assertions.assertThat(actualValues).containsExactlyInAnyOrderElementsOf(expectedValues);

			Mockito.verify(cardRepository, Mockito.times(1))
                    .findAllWithUser();
			Mockito.verify(cardMapper, Mockito.times(1))
                    .toDtoWithUserList(getFullListOfCardsWithUsers());
		}

		@Test
		@DisplayName("returns empty list with users")
		void shouldReturnEmptyListWithUsers() {
			Mockito.when(cardRepository.findAllWithUser())
                    .thenReturn(getEmptyListOfCards());
			Mockito.when(cardMapper.toDtoWithUserList(getEmptyListOfCards()))
					.thenReturn(getEmptyListOfCardDtosWithUsers());

			List<CardWithUserDto> expectedValues = getEmptyListOfCardDtosWithUsers();

			List<CardWithUserDto> actualValues = cardService.findAllWithUser();

			Assertions.assertThat(actualValues).containsExactlyInAnyOrderElementsOf(expectedValues);

			Mockito.verify(cardRepository, Mockito.times(1))
                    .findAllWithUser();
			Mockito.verify(cardMapper, Mockito.times(1))
                    .toDtoWithUserList(getEmptyListOfCards());
		}
	}
}
