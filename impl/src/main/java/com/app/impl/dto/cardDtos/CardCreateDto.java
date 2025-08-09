package com.app.impl.dto.cardDtos;

import java.time.LocalDate;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.CreditCardNumber;

import static com.app.impl.dto.cardDtos.CardConstraints.USER_ID_NULL_MSG;
import static com.app.impl.dto.cardDtos.CardConstraints.USER_ID_INVALID_VALUE_MSG;
import static com.app.impl.dto.cardDtos.CardConstraints.CARD_NUMBER_BLANK_MSG;
import static com.app.impl.dto.cardDtos.CardConstraints.CARD_NUMBER_INVALID_MSG;
import static com.app.impl.dto.cardDtos.CardConstraints.CARD_HOLDER_NAME_BLANK_MSG;
import static com.app.impl.dto.cardDtos.CardConstraints.CARD_HOLDER_NAME_MAX_LENGTH;
import static com.app.impl.dto.cardDtos.CardConstraints.CARD_EXPIRATION_DATE_NULL_MSG;
import static com.app.impl.dto.cardDtos.CardConstraints.CARD_EXPIRATION_DATE_INVALID_MSG;

public record CardCreateDto(
        @NotNull(message = USER_ID_NULL_MSG)
        @Positive(message = USER_ID_INVALID_VALUE_MSG)
        Long userId,

        @NotBlank(message = CARD_NUMBER_BLANK_MSG)
        @CreditCardNumber(message = CARD_NUMBER_INVALID_MSG)
        String cardNumber,

        @NotBlank(message = CARD_HOLDER_NAME_BLANK_MSG)
        @Size(max = CARD_HOLDER_NAME_MAX_LENGTH)
        String cardHolderName,

        @NotNull(message = CARD_EXPIRATION_DATE_NULL_MSG)
        @Future(message = CARD_EXPIRATION_DATE_INVALID_MSG)
        LocalDate expirationDate
) {
}
