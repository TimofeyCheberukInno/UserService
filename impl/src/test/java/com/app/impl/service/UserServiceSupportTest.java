package com.app.impl.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.app.impl.service.support.UserServiceSupport;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UserServiceSupportTest {

    @Test
    @DisplayName("returns full list of ids")
    public void shouldReturnFullInitialListOfIds() {
        List<Long> expectedValues = new ArrayList<>(Arrays.asList(1L, 2L, 3L));

        List<Long> actualValues = UserServiceSupport.findNotCachedUsersIds(
                new ArrayList<>(Arrays.asList(1L, 2L, 3L)),
                new ArrayList<>()
        );

        assertThat(actualValues).isEqualTo(expectedValues);
    }

    // FIXME: more tests
}