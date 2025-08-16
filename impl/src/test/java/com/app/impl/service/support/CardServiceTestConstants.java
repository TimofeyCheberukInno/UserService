package com.app.impl.service.support;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.app.impl.dto.cardDtos.CardCreateDto;
import com.app.impl.dto.cardDtos.CardDto;
import com.app.impl.dto.cardDtos.CardUpdateDto;
import com.app.impl.dto.cardDtos.CardWithUserDto;
import com.app.impl.dto.userDtos.UserDto;
import com.app.impl.entity.Card;
import com.app.impl.entity.User;

import static com.app.impl.service.support.CardServiceTestData.CARD_ID;
import static com.app.impl.service.support.CardServiceTestData.USER_ID;
import static com.app.impl.service.support.CardServiceTestData.CARD_NUMBER;
import static com.app.impl.service.support.CardServiceTestData.CARD_HOLDER_NAME;
import static com.app.impl.service.support.CardServiceTestData.CARD_EXPIRATION_DATE;
import static com.app.impl.service.support.CardServiceTestData.USER_NAME;
import static com.app.impl.service.support.CardServiceTestData.USER_SURNAME;
import static com.app.impl.service.support.CardServiceTestData.USER_EMAIL;
import static com.app.impl.service.support.CardServiceTestData.USER_BIRTHDATE;

public class CardServiceTestConstants {
    public static final Long CARD_ENTITY_ID = CARD_ID;

    public static final Long CARD_USER_ID = USER_ID;

    public static final int AMOUNT_OF_UPDATED_ENTITIES = 1;

    public static final String CARD_NOT_FOUND_BY_ID_MSG = "Card with id %d was not found";

    public static final String LIST_OF_CARDS_NOT_FOUND_BY_IDS_MSG = "Cards not found for ids: ";

    public static final String USER_NOT_FOUND_BY_ID_MSG = "User with id %d not found";

    public static User getUserEntity() {
        return new User(
                USER_ID,
                USER_NAME,
                USER_SURNAME,
                USER_BIRTHDATE,
                USER_EMAIL
        );
    }

    public static UserDto getUserDto() {
        return new UserDto(
                USER_ID,
                USER_NAME,
                USER_SURNAME,
                USER_BIRTHDATE,
                USER_EMAIL
        );
    }

    public static CardCreateDto getCreateCardDto() {
            return new CardCreateDto(
                USER_ID,
                CARD_NUMBER,
                CARD_HOLDER_NAME,
                CARD_EXPIRATION_DATE
        );
    }

    public static CardDto getCardDto() {
            return new CardDto(
                    CARD_ID,
                    CARD_USER_ID,
                    CARD_NUMBER,
                    CARD_HOLDER_NAME,
                    CARD_EXPIRATION_DATE
        );
    }

    public static CardWithUserDto getCardWithUserDto() {
        return new CardWithUserDto(
                CARD_ID,
                CARD_USER_ID,
                CARD_NUMBER,
                CARD_HOLDER_NAME,
                CARD_EXPIRATION_DATE,
                getUserDto()
        );
    }

    public static Card getMappedCardEntity() {
        return new Card(
                null,
                USER_ID,
                null,
                CARD_NUMBER,
                CARD_HOLDER_NAME,
                CARD_EXPIRATION_DATE
        );
    }

    public static Card getMappedCardEntityWithUser() {
        return new Card(
                null,
                USER_ID,
                getUserEntity(),
                CARD_NUMBER,
                CARD_HOLDER_NAME,
                CARD_EXPIRATION_DATE
        );
    }

    public static Card getCreatedCardEntity() {
        return new Card(
                CARD_ID,
                USER_ID,
                getUserEntity(),
                CARD_NUMBER,
                CARD_HOLDER_NAME,
                CARD_EXPIRATION_DATE
        );
    }

    public static CardUpdateDto getUpdateCardDto() {
        return new CardUpdateDto(
                CARD_ID,
                CARD_HOLDER_NAME
        );
    }

    public static Card getCardEntityToUpdate() {
        return new Card(
                CARD_ID,
                null,
                null,
                null,
                CARD_HOLDER_NAME,
                null
        );
    }

    public static List<Long> getFullListOfIds() {
        return new ArrayList<>(Arrays.asList(1L, 2L, 3L));
    }

    public static List<Long> getEmptyListOfIds() {
        return new ArrayList<>();
    }

    public static List<CardDto> getFullListOfCardDtos() {
        return new ArrayList<>(Arrays.asList(
                getCardDto1(),
                getCardDto2(),
                getCardDto3()
        ));
    }

    public static CardDto getCardDto1() {
        return new CardDto(
                1L,
                1L,
                "4263986527583938",
                "NAME_1 SURNAME_1",
                LocalDate.of(2003, 10, 23)
        );
    }

    public static CardDto getCardDto2() {
        return new CardDto(
                2L,
                2L,
                "4532015219737381",
                "NAME_2 SURNAME_2",
                LocalDate.of(1990, 1, 23)
        );
    }

    public static CardDto getCardDto3() {
        return new CardDto(
                3L,
                3L,
                "4532012032321592",
                "NAME_3 SURNAME_3",
                LocalDate.of(1999, 11, 3)
        );
    }

    public static List<CardDto> getEmptyListOfCardDtos() {
        return new ArrayList<>();
    }

    public static List<Card> getFullListOfCardsWithoutUsers() {
        return new ArrayList<>(Arrays.asList(
                getCard1(),
                getCard2(),
                getCard3()
        ));
    }

    public static Card getCard1() {
        return new Card(
                1L,
                1L,
                null,
                "4263986527583938",
                "NAME_1 SURNAME_1",
                LocalDate.of(2003, 10, 23)
        );
    }

    public static Card getCard2() {
        return new Card(
                2L,
                2L,
                null,
                "4532015219737381",
                "NAME_2 SURNAME_2",
                LocalDate.of(1990, 1, 23)
        );
    }

    public static Card getCard3() {
        return new Card(
                3L,
                3L,
                null,
                "4532012032321592",
                "NAME_3 SURNAME_3",
                LocalDate.of(1999, 11, 3)
        );
    }

    public static Card getCard1WithUser() {
        return new Card(
                1L,
                1L,
                new User(
                    1L,
                    "NAME_1",
                    "SURNAME_1",
                    LocalDate.of(2000, 10, 10),
                    "email1@gmail.com"
                ),
                "4263986527583938",
                "NAME_1 SURNAME_1",
                LocalDate.of(2003, 10, 23)
        );
    }

    public static Card getCard2WithUser() {
        return new Card(
                2L,
                2L,
                new User(
                    2L,
                    "NAME_2",
                    "SURNAME_2",
                    LocalDate.of(2010, 5, 28),
                    "email2@mail.ru"
                ),
                "4532015219737381",
                "NAME_2 SURNAME_2",
                LocalDate.of(1990, 1, 23)
        );
    }

    public static Card getCard3WithUser() {
        return new Card(
                3L,
                3L,
                new User(
                    3L,
                    "NAME_3",
                    "SURNAME_3",
                    LocalDate.of(1999, 1, 29),
                    "email3@gmail.com"
                ),
                "4532012032321592",
                "NAME_3 SURNAME_3",
                LocalDate.of(1999, 11, 3)
        );
    }

    public static List<Card> getFullListOfCardsWithUsers() {
        return new ArrayList<>(Arrays.asList(
                getCard1WithUser(),
                getCard2WithUser(),
                getCard3WithUser()
        ));
    }

    public static CardWithUserDto getCardDto1WithUser() {
        return new CardWithUserDto(
                1L,
                1L,
                "4263986527583938",
                "NAME_1 SURNAME_1",
                LocalDate.of(2003, 10, 23),
                new UserDto(
                    1L,
                    "NAME_1",
                    "SURNAME_1",
                    LocalDate.of(2000, 10, 10),
                    "email1@gmail.com"
                )
        );
    }

    public static CardWithUserDto getCardDto2WithUser() {
        return new CardWithUserDto(
                2L,
                2L,
                "4532015219737381",
                "NAME_2 SURNAME_2",
                LocalDate.of(1990, 1, 23),
                new UserDto(
                    2L,
                    "NAME_2",
                    "SURNAME_2",
                    LocalDate.of(2010, 5, 28),
                    "email2@mail.ru"
                )
        );
    }

    public static CardWithUserDto getCardDto3WithUser() {
        return new CardWithUserDto(
                3L,
                3L,
                "4532012032321592",
                "NAME_3 SURNAME_3",
                LocalDate.of(1999, 11, 3),
                new UserDto(
                    3L,
                    "NAME_3",
                    "SURNAME_3",
                    LocalDate.of(1999, 1, 29),
                    "email3@gmail.com"
                )
        );
    }

    public static List<CardWithUserDto> getFullListOfCardDtosWithUsers() {
        return new ArrayList<>(Arrays.asList(
                getCardDto1WithUser(),
                getCardDto2WithUser(),
                getCardDto3WithUser()
        ));
    }

    public static List<CardWithUserDto> getEmptyListOfCardDtosWithUsers() {
        return new ArrayList<>();
    }

    public static List<Card> getEmptyListOfCards() {
        return new ArrayList<>();
    }
}