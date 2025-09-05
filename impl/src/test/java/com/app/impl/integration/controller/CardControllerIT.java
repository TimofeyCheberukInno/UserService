package com.app.impl.integration.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.app.impl.integration.config.TestContainersConfig;
import com.app.impl.dto.cardDtos.CardWithUserDto;
import com.app.impl.entity.Card;
import com.app.impl.dto.cardDtos.CardUpdateDto;
import com.app.impl.dto.cardDtos.CardCreateDto;
import com.app.impl.dto.cardDtos.CardDto;
import com.app.impl.infrastructure.cache.CardCacheService;
import com.app.impl.integration.support.CardITSupport;
import com.app.impl.repository.CardRepository;
import com.app.impl.repository.UserRepository;

@Tag("controllers")
@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@Import({ TestContainersConfig.class })
@ActiveProfiles("test")
public class CardControllerIT {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    CardRepository cardRepository;
    @Autowired
    CardCacheService cacheService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CardITSupport support;
    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        cardRepository.deleteAll();
        userRepository.deleteAll();
        cacheService.deleteAll();
    }

    @Nested
    @DisplayName("Integration tests for createCard(CardCreateDto dto)")
    class createCardIT {
        @Test
        @DisplayName("returns 201 status")
        void shouldReturnCreatedCard() throws Exception {
            CardCreateDto dto = support.createUserAndCardCreateDto();

            MvcResult result = mockMvc.perform(post("/api/cards")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").exists())
                    .andExpect(jsonPath("$.userId").value(dto.userId()))
                    .andExpect(jsonPath("$.cardNumber").value(dto.cardNumber()))
                    .andExpect(jsonPath("$.cardHolderName").value(dto.cardHolderName()))
                    .andExpect(jsonPath("$.expirationDate").value(dto.expirationDate().toString()))
                    .andReturn();

            String json = result.getResponse().getContentAsString();
            CardDto cardDto = objectMapper.readValue(json, CardDto.class);

            Optional<CardDto> cardById = cacheService.getByIdWithoutUser(cardDto.id());
            assertThat(cardById).isPresent();
            assertThat(cardById.get().userId()).isEqualTo(dto.userId());
            assertThat(cardById.get().cardNumber()).isEqualTo(dto.cardNumber());
            assertThat(cardById.get().cardHolderName()).isEqualTo(dto.cardHolderName());
            assertThat(cardById.get().expirationDate()).isEqualTo(dto.expirationDate());
        }

        @Test
        @DisplayName("returns 400 status because dto is invalid")
        void shouldReturnBadRequestBecauseDtoIsInvalid() throws Exception {
            CardCreateDto dto = support.createInvalidCardCreateDto();

            mockMvc.perform(post("/api/cards")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").exists());
        }

        @Test
        @DisplayName("returns 400 status because dto is absent")
        void shouldReturnBadRequestBecauseDtoIsAbsent() throws Exception {
            mockMvc.perform(post("/api/cards"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").exists());
        }
    }

    @Nested
    @DisplayName("Integration tests for updateCard(UpdateCardDto dto)")
    class updateCardIT {
        @Test
        @DisplayName("returns 200 status")
        void shouldReturnAmountOfUpdatedCards() throws Exception {
            Card card = support.createUserAndCard();
            CardUpdateDto dto = support.createCardUpdateDto(card.getId());

            mockMvc.perform(put("/api/cards")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isOk())
                    .andExpect(content().string("1"));

            Optional<CardDto> cardById = cacheService.getByIdWithoutUser(card.getId());
            assertThat(cardById).isPresent();
            assertThat(cardById.get().cardHolderName()).isEqualTo(dto.cardHolderName());
        }

        @Test
        @DisplayName("returns 400 status because dto is invalid")
        void shouldReturnBadRequestBecauseDtoIsInvalid() throws Exception {
            CardUpdateDto dto = support.createInvalidCardUpdateDto();

            mockMvc.perform(put("/api/cards")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").exists());
        }

        @Test
        @DisplayName("returns 400 status because dto is absent")
        void shouldReturnBadRequestBecauseDtoIsAbsent() throws Exception {
            mockMvc.perform(put("/api/cards"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").exists());
        }
    }

    @Nested
    @DisplayName("Integration tests for deleteCard(Long id)")
    class deleteCardIT {
        @Test
        @DisplayName("returns 200 status")
        void shouldDeleteCard() throws Exception {
            Card card = support.createUserAndCard();

            mockMvc.perform(delete("/api/cards/" + card.getId()))
                    .andExpect(status().isNoContent());

            assertThat(cardRepository.findById(card.getId())).isEmpty();

            Optional<CardDto> cardById = cacheService.getByIdWithoutUser(card.getId());
            assertThat(cardById).isEmpty();
        }

        @Test
        @DisplayName("returns 404 status because card was not found")
        void shouldReturnNotFoundBecauseCardIsNotFound() throws Exception {
            mockMvc.perform(delete("/api/cards/1"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").exists());
        }

        @Test
        @DisplayName("returns 400 status because param is invalid")
        void shouldReturnBadRequestBecauseParamIsInvalid() throws Exception {
            mockMvc.perform(delete("/api/cards/-1"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").exists());
        }
    }

    @Nested
    @DisplayName("Integration tests for getCardWithUserById(Long id)")
    class getCardByIdIT {
        @Test
        @DisplayName("returns 200 status")
        void shouldReturnCardById() throws Exception {
            Card card = support.createUserAndCard();

            mockMvc.perform(get("/api/cards/" + card.getId().toString()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").exists())
                    .andExpect(jsonPath("$.userId").value(card.getUserId()))
                    .andExpect(jsonPath("$.cardNumber").value(card.getCardNumber()))
                    .andExpect(jsonPath("$.cardHolderName").value(card.getCardHolderName()))
                    .andExpect(jsonPath("$.expirationDate").value(card.getExpirationDate().toString()));

            Optional<CardWithUserDto> cardById = cacheService.getByIdWithUser(card.getId());
            assertThat(cardById).isPresent();
            assertThat(cardById.get().userId()).isEqualTo(card.getUserId());
            assertThat(cardById.get().cardNumber()).isEqualTo(card.getCardNumber());
            assertThat(cardById.get().cardHolderName()).isEqualTo(card.getCardHolderName());
            assertThat(cardById.get().expirationDate()).isEqualTo(card.getExpirationDate());

        }

        @Test
        @DisplayName("returns 404 status because card was not found")
        void shouldReturnNotFoundBecauseCardIsNotFound() throws Exception {
            mockMvc.perform(get("/api/cards/1"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").exists());
        }

        @Test
        @DisplayName("returns 400 status because param is invalid")
        void shouldReturnBadRequestBecauseParamIsInvalid() throws Exception {
            mockMvc.perform(get("/api/cards/-1"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").exists());
        }
    }

    @Nested
    @DisplayName("Integration tests for getListOfCardsByIds(List<Long> ids)")
    class getListOfCardsByIdsIT {
        @Test
        @DisplayName("returns 200 status")
        void shouldReturnCardsByIds() throws Exception {
            List<Card> cards = support.createListOfCards();

            mockMvc.perform(get("/api/cards/by-ids")
                            .param("ids", cards.get(0).getId().toString())
                            .param("ids", cards.get(1).getId().toString()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$.length()").value(2))

                    .andExpect(jsonPath("$[0].id").exists())
                    .andExpect(jsonPath("$[0].userId").value(cards.get(0).getUserId()))
                    .andExpect(jsonPath("$[0].cardNumber").value(cards.get(0).getCardNumber()))
                    .andExpect(jsonPath("$[0].cardHolderName").value(cards.get(0).getCardHolderName()))
                    .andExpect(jsonPath("$[0].expirationDate").value(cards.get(0).getExpirationDate().toString()))

                    .andExpect(jsonPath("$[0].id").exists())
                    .andExpect(jsonPath("$[0].userId").value(cards.get(0).getUserId()))
                    .andExpect(jsonPath("$[0].cardNumber").value(cards.get(0).getCardNumber()))
                    .andExpect(jsonPath("$[0].cardHolderName").value(cards.get(0).getCardHolderName()))
                    .andExpect(jsonPath("$[0].expirationDate").value(cards.get(0).getExpirationDate().toString()));

            List<CardDto> usersById = cacheService.getByIdsWithoutUser(List.of(cards.get(0).getId(), cards.get(1).getId()));
            assertThat(usersById).isNotNull();
            assertThat(usersById).hasSize(0);
        }

        @Test
        @DisplayName("returns 404 status because cards were not found")
        void shouldReturnNotFoundBecauseCardsAreNotFound() throws Exception {
            mockMvc.perform(get("/api/cards/by-ids")
                            .param("ids", "1")
                            .param("ids", "2"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").exists());
        }

        @Test
        @DisplayName("returns 400 status because params are invalid")
        void shouldReturnBadRequestBecauseParamsAreInvalid() throws Exception {
            mockMvc.perform(get("/api/cards/by-ids")
                            .param("ids", "one")
                            .param("ids", "two"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").exists());
        }
    }

    @Nested
    @DisplayName("Integration tests for getListOfCardsWithUserByIds(List<Long> ids)")
    class getListOfCardsWithUserByIdsIT {
        @Test
        @DisplayName("returns 200 status")
        void shouldReturnCardsByIds() throws Exception {
            List<Card> cards = support.createListOfCards();

            mockMvc.perform(get("/api/cards/by-ids/with-user")
                            .param("ids", cards.get(0).getId().toString())
                            .param("ids", cards.get(1).getId().toString()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$.length()").value(2))

                    .andExpect(jsonPath("$[0].id").exists())
                    .andExpect(jsonPath("$[0].userId").value(cards.get(0).getUserId()))
                    .andExpect(jsonPath("$[0].cardNumber").value(cards.get(0).getCardNumber()))
                    .andExpect(jsonPath("$[0].cardHolderName").value(cards.get(0).getCardHolderName()))
                    .andExpect(jsonPath("$[0].expirationDate").value(cards.get(0).getExpirationDate().toString()))

                    .andExpect(jsonPath("$[0].id").exists())
                    .andExpect(jsonPath("$[0].userId").value(cards.get(0).getUserId()))
                    .andExpect(jsonPath("$[0].cardNumber").value(cards.get(0).getCardNumber()))
                    .andExpect(jsonPath("$[0].cardHolderName").value(cards.get(0).getCardHolderName()))
                    .andExpect(jsonPath("$[0].expirationDate").value(cards.get(0).getExpirationDate().toString()));

            List<CardWithUserDto> usersById = cacheService.getByIdsWithUser(List.of(cards.get(0).getId(), cards.get(1).getId()));
            assertThat(usersById).isNotNull();
            assertThat(usersById).hasSize(0);
        }

        @Test
        @DisplayName("returns 404 status because cards were not found")
        void shouldReturnNotFoundBecauseCardsAreNotFound() throws Exception {
            mockMvc.perform(get("/api/cards/by-ids/with-user")
                            .param("ids", "1")
                            .param("ids", "2"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").exists());
        }

        @Test
        @DisplayName("returns 400 status because params are invalid")
        void shouldReturnBadRequestBecauseParamsAreInvalid() throws Exception {
            mockMvc.perform(get("/api/cards/by-ids/with-user")
                            .param("ids", "one")
                            .param("ids", "two"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").exists());
        }
    }

    @Nested
    @DisplayName("Integration tests for getAll() and getAllWithUser()")
    class getAllIT {
        @Test
        @DisplayName("returns 200 status")
        void shouldReturnAllCards() throws Exception {
            List<Card> cards = support.createListOfCards();

            mockMvc.perform(get("/api/cards"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$.length()").value(2))

                    .andExpect(jsonPath("$[0].id").exists())
                    .andExpect(jsonPath("$[0].userId").value(cards.get(0).getUserId()))
                    .andExpect(jsonPath("$[0].cardNumber").value(cards.get(0).getCardNumber()))
                    .andExpect(jsonPath("$[0].cardHolderName").value(cards.get(0).getCardHolderName()))
                    .andExpect(jsonPath("$[0].expirationDate").value(cards.get(0).getExpirationDate().toString()))

                    .andExpect(jsonPath("$[0].id").exists())
                    .andExpect(jsonPath("$[0].userId").value(cards.get(0).getUserId()))
                    .andExpect(jsonPath("$[0].cardNumber").value(cards.get(0).getCardNumber()))
                    .andExpect(jsonPath("$[0].cardHolderName").value(cards.get(0).getCardHolderName()))
                    .andExpect(jsonPath("$[0].expirationDate").value(cards.get(0).getExpirationDate().toString()));

            List<CardDto> usersById = cacheService.getByIdsWithoutUser(List.of(cards.get(0).getId(), cards.get(1).getId()));
            assertThat(usersById).isNotNull();
            assertThat(usersById).hasSize(0);
        }

        @Test
        @DisplayName("returns 404 status because cards were not found")
        void shouldReturnNotFoundBecauseCardsAreNotFound() throws Exception {
            mockMvc.perform(get("/api/cards"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$.length()").value(0));
        }

        @Test
        @DisplayName("returns 200 status")
        void shouldReturnAllCardsWithUsers() throws Exception {
            List<Card> cards = support.createListOfCards();

            mockMvc.perform(get("/api/cards/with-user"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$.length()").value(2))

                    .andExpect(jsonPath("$[0].id").exists())
                    .andExpect(jsonPath("$[0].userId").value(cards.get(0).getUserId()))
                    .andExpect(jsonPath("$[0].cardNumber").value(cards.get(0).getCardNumber()))
                    .andExpect(jsonPath("$[0].cardHolderName").value(cards.get(0).getCardHolderName()))
                    .andExpect(jsonPath("$[0].expirationDate").value(cards.get(0).getExpirationDate().toString()))

                    .andExpect(jsonPath("$[0].id").exists())
                    .andExpect(jsonPath("$[0].userId").value(cards.get(0).getUserId()))
                    .andExpect(jsonPath("$[0].cardNumber").value(cards.get(0).getCardNumber()))
                    .andExpect(jsonPath("$[0].cardHolderName").value(cards.get(0).getCardHolderName()))
                    .andExpect(jsonPath("$[0].expirationDate").value(cards.get(0).getExpirationDate().toString()));

            List<CardWithUserDto> usersById = cacheService.getByIdsWithUser(List.of(cards.get(0).getId(), cards.get(1).getId()));
            assertThat(usersById).isNotNull();
            assertThat(usersById).hasSize(0);
        }

        @Test
        @DisplayName("returns 404 status because cards were not found")
        void shouldReturnNotFoundBecauseCardsWithUsersAreNotFound() throws Exception {
            mockMvc.perform(get("/api/cards/with-user"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$.length()").value(0));
        }
    }
}

