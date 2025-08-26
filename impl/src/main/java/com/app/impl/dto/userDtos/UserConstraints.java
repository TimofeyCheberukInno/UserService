package com.app.impl.dto.userDtos;

public class UserConstraints {
    static final String BLANK_NAME_MSG = "User name should not be blank";
    static final int MAX_NAME_LENGTH = 50;
    static final String MAX_NAME_LENGTH_MSG = "User name size should be less than or equal to 50";

    static final String BLANK_SURNAME_MSG = "User surname should not be blank";
    static final int MAX_SURNAME_LENGTH = 50;
    static final String MAX_SURNAME_LENGTH_MSG = "User surname size should be less than or equal to 50";

    static final String INVALID_BIRTH_DATE_MSG = "User date of birth should be in past";

    static final String BLANK_EMAIL_MSG = "User email should be blank";

    static final String INVALID_EMAIL_MSG = "User email should be valid";
}