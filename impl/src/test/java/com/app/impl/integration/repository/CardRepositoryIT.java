package com.app.impl.integration.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;

import com.app.impl.repository.UserRepository;
import com.app.impl.entity.Card;
import com.app.impl.integration.support.CardITSupport;
import com.app.impl.repository.CardRepository;

@DataJpaTest
@Import({ CardITSupport.class })
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class CardRepositoryIT extends BaseDBTest {
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CardITSupport support;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
        cardRepository.deleteAll();
    }

    @Nested
    @DisplayName("Integration tests for findByIdWithUser(Long id)")
    class findByIdWithUserIT {
        @Test
        @DisplayName("returns card with user by id")
        void shouldReturnCardWithUserById() {
            Card expectedValue = support.createUserAndCard();

            Optional<Card> actualValue = cardRepository.findById(expectedValue.getId());

            assertThat(actualValue).isPresent()
                    .get()
                    .isEqualTo(expectedValue);
        }

        @Test
        @DisplayName("returns Optional.empty() because non-existent card")
        void shouldReturnEmptyOptional() {
            Optional<Card> actualValue = cardRepository.findById(1L);

            assertThat(actualValue).isEmpty();
        }
    }

    @Nested
    @DisplayName("Integration tests for updateCard(Card card)")
    class updateCardIT {
        @Test
        @DisplayName("returns amount of updated cards")
        void shouldReturnAmountOfUpdatedCards () {
            Card cardToUpdate = support.getCardToUpdate();

            int amountOfUpdatedCards = cardRepository.updateCard(cardToUpdate);

            assertThat(amountOfUpdatedCards).isEqualTo(1);
            Card updatedCard = cardRepository.findById(cardToUpdate.getId()).get();
            assertThat(updatedCard.getCardHolderName()).isEqualTo(cardToUpdate.getCardHolderName());
        }

        @Test
        @DisplayName("returns 0 updates while updating non-existent card")
        void shouldReturnExceptionWhileUpdatingNonExistentCard() {
            Card cardToUpdate = support.getCardToUpdateWithoutCreation();

            int amountOfUpdatedCards = cardRepository.updateCard(cardToUpdate);

            assertThat(amountOfUpdatedCards).isEqualTo(0);
            assertThat(userRepository.findById(cardToUpdate.getId())).isEmpty();
        }

    }

    @Nested
    @DisplayName("Integration tests for findAllByIds(Collection<Long> ids) and findAllByIdsWithUser(Collection<Long> ids)")
    class findAllByIdsIT {
        @Test
        @DisplayName("returns list of cards")
        void shouldReturnListOfCards() {
            List<Card> expectedValues = support.createListOfCards();

            List<Card> actualValues = cardRepository.findAllByIds(List.of(expectedValues.get(0).getId(), expectedValues.get(1).getId()));

            assertThat(actualValues).isEqualTo(expectedValues);
        }

        @Test
        @DisplayName("returns list of cards partially")
        void shouldReturnListOfCardsPartially() {
            List<Card> expectedValues = support.createListOfCards();

            List<Card> actualValues = cardRepository.findAllByIds(List.of(expectedValues.get(0).getId(), expectedValues.get(1).getId(), 3L));

            assertThat(actualValues.size()).isEqualTo(expectedValues.size());
            assertThat(actualValues).isEqualTo(expectedValues);
        }

        @Test
        @DisplayName("returns empty list of cards because receives empty list")
        void shouldReturnEmptyListBecauseReceivesEmptyList() {
            List<Card> actualValues = cardRepository.findAllByIds(List.of());

            assertThat(actualValues).isEmpty();
        }

        @Test
        @DisplayName("returns empty list of cards because receives list of non-existent ids")
        void shouldReturnEmptyListBecauseReceivesNonExistentIds() {
            List<Card> actualValues = cardRepository.findAllByIds(List.of(1L, 2L, 3L));

            assertThat(actualValues).isEmpty();
        }

        @Test
        @DisplayName("return list of cards where list of ids has duplicates")
        void shouldReturnListOfCardsWithDuplicates() {
            List<Card> expectedValues = support.createListOfCards();

            List<Card> actualValues = cardRepository.findAllByIds(List.of(expectedValues.get(0).getId(), expectedValues.get(1).getId(), expectedValues.get(1).getId()));

            assertThat(actualValues.size()).isEqualTo(expectedValues.size());
            assertThat(actualValues).isEqualTo(expectedValues);
        }

        @Test
        @DisplayName("returns list of cards with users")
        void shouldReturnListOfCardsWithUsers() {
            List<Card> expectedValues = support.createListOfCards();

            List<Card> actualValues = cardRepository.findAllByIdsWithUser(List.of(expectedValues.get(0).getId(), expectedValues.get(1).getId()));

            assertThat(actualValues).isEqualTo(expectedValues);
        }

        @Test
        @DisplayName("returns list of cards with users partially")
        void shouldReturnListOfCardsWithUsersPartially() {
            List<Card> expectedValues = support.createListOfCards();

            List<Card> actualValues = cardRepository.findAllByIdsWithUser(List.of(expectedValues.get(0).getId(), expectedValues.get(1).getId(), 3L));

            assertThat(actualValues.size()).isEqualTo(expectedValues.size());
            assertThat(actualValues).isEqualTo(expectedValues);
        }

        @Test
        @DisplayName("returns empty list of cards with users because receives empty list")
        void shouldReturnEmptyListWithUsersBecauseReceivesEmptyList() {
            List<Card> actualValues = cardRepository.findAllByIdsWithUser(List.of());

            assertThat(actualValues).isEmpty();
        }

        @Test
        @DisplayName("returns empty list of cards with users because receives list of non-existent ids")
        void shouldReturnEmptyListWithUsersBecauseReceivesNonExistentIds() {
            List<Card> actualValues = cardRepository.findAllByIdsWithUser(List.of(1L, 2L, 3L));

            assertThat(actualValues).isEmpty();
        }

        @Test
        @DisplayName("return list of cards with users where list of ids has duplicates")
        void shouldReturnListOfCardsWithUsersWithDuplicates() {
            List<Card> expectedValues = support.createListOfCards();

            List<Card> actualValues = cardRepository.findAllByIdsWithUser(List.of(expectedValues.get(0).getId(), expectedValues.get(1).getId(), expectedValues.get(1).getId()));

            assertThat(actualValues.size()).isEqualTo(expectedValues.size());
            assertThat(actualValues).isEqualTo(expectedValues);
        }
    }

    @Nested
    @DisplayName("Integration tests for findAllWithUser()")
    class findAllWithUserIT {
        @Test
        @DisplayName("returns list of cards")
        void shouldReturnListOfCards() {
            List<Card> expectedValues = support.createListOfCards();

            List<Card> actualValues = cardRepository.findAllWithUser();

            assertThat(actualValues).isEqualTo(expectedValues);
        }

        @Test
        @DisplayName("return empty list of cards")
        void shouldReturnEmptyListOfCards() {
            List<Card> actualValues = cardRepository.findAllWithUser();

            assertThat(actualValues).isEmpty();
        }
    }
}
