package com.app.impl.dto.cardDtos;

import java.time.LocalDate;

public record CardDto(
        Long id,
        Long userId,
        String cardNumber,
        String cardHolderName,
        LocalDate expirationDate
) {
}
