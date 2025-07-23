package com.app.impl.dto.cardDtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import static com.app.impl.dto.cardDtos.CardConstraints.CARD_HOLDER_NAME_BLANK_MSG;
import static com.app.impl.dto.cardDtos.CardConstraints.CARD_HOLDER_NAME_MAX_LENGTH;

public record CardUpdateDto(
        @NotBlank(message = CARD_HOLDER_NAME_BLANK_MSG)
        @Size(max = CARD_HOLDER_NAME_MAX_LENGTH)
        String cardHolderName
) {
}
