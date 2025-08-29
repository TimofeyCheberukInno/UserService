package com.app.impl.dto.cardDtos;

import static com.app.impl.dto.cardDtos.CardConstraints.CARD_HOLDER_NAME_BLANK_MSG;
import static com.app.impl.dto.cardDtos.CardConstraints.CARD_HOLDER_NAME_MAX_LENGTH;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CardUpdateDto(
        @Positive Long id,

		@NotBlank(message = CARD_HOLDER_NAME_BLANK_MSG)
        @Size(max = CARD_HOLDER_NAME_MAX_LENGTH)
        String cardHolderName
) { }