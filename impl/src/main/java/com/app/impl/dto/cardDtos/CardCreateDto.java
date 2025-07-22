package com.app.impl.dto.cardDtos;

import java.time.LocalDate;

public record CardCreateDto(
        Long userId,
        String cardNumber,
        String cardHolderName,
        LocalDate expirationDate
) {
}
