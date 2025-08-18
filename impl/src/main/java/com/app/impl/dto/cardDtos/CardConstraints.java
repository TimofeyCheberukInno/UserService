package com.app.impl.dto.cardDtos;

public class CardConstraints {
	static final String USER_ID_NULL_MSG = "Cards user id should not be null";
	static final String USER_ID_INVALID_VALUE_MSG = "Cards user id should be positive";

	static final String CARD_NUMBER_BLANK_MSG = "Card number should not be blank";
	static final String CARD_NUMBER_INVALID_MSG = "Card number is invalid";

	static final String CARD_HOLDER_NAME_BLANK_MSG = "Card holder name should not be blank";
	static final int CARD_HOLDER_NAME_MAX_LENGTH = 100;

	static final String CARD_EXPIRATION_DATE_NULL_MSG = "Card expiration date should not be null";
	static final String CARD_EXPIRATION_DATE_INVALID_MSG = "Card expiration date should be in future";
}