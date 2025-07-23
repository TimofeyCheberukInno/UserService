package com.app.impl.dto.cardDtos;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.CreditCardNumber;

import static com.app.impl.dto.cardDtos.CardConstraints.CARD_NUMBER_BLANK_MSG;
import static com.app.impl.dto.cardDtos.CardConstraints.CARD_NUMBER_INVALID_MSG;

public record CardUpdateDto(
        @NotBlank(message = CARD_NUMBER_BLANK_MSG)
        @CreditCardNumber(message = CARD_NUMBER_INVALID_MSG)
        String cardNumber
) {
}
