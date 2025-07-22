package com.app.impl.dto.cardDtos;

public record CardDto(
        Long id,
        Long userId,
        String cardNumber,
        String cardHolderName,
        String expirationDate
) {
}
