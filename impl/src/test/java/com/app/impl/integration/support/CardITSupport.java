package com.app.impl.integration.support;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.app.impl.repository.UserRepository;
import com.app.impl.entity.User;
import com.app.impl.entity.Card;
import com.app.impl.repository.CardRepository;

@Component
public class CardITSupport {
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private UserRepository userRepository;

    public Card createUserAndCard() {
        User user = new User(
                null,
                "NAME_1",
                "SURNAME_1",
                LocalDate.of(2005, 5, 13),
                "example@gmail.com"
        );
        User createdUser = userRepository.save(user);

        Card card = new Card(
                null,
                createdUser.getId(),
                createdUser,
                "4485908119420396",
                "NAME_1 SURNAME_1",
                LocalDate.of(2035, 5, 13)
        );

        return cardRepository.save(card);
    }

    public Card createSecondPairOfUserAndCard() {
        User user = new User(
                null,
                "NAME_2",
                "SURNAME_2",
                LocalDate.of(2015, 5, 3),
                "example_2@gmail.com"
        );
        User createdUser = userRepository.save(user);

        Card card = new Card(
                null,
                createdUser.getId(),
                createdUser,
                "4263 9896 7641 8534",
                "NAME_2 SURNAME_2",
                LocalDate.of(2027, 2, 1)
        );

        return cardRepository.save(card);
    }

    public Card getCardToUpdate() {
        Card card = createUserAndCard();

        Card cardToUpdate = new Card(
                card.getId(),
                null,
                null,
                null,
                "UPDATE_NAME_1 UPDATED_SURNAME_1",
                null
        );

        return cardToUpdate;
    }

    public Card getCardToUpdateWithoutCreation() {
        return new Card(
                1L,
                null,
                null,
                null,
                "UPDATE_NAME_1 UPDATED_SURNAME_1",
                null
        );
    }

    public List<Card> createListOfCards() {
        Card card1 = createUserAndCard();
        Card card2 = createSecondPairOfUserAndCard();

        return new ArrayList<Card>(Arrays.asList(card1, card2));
    }
}