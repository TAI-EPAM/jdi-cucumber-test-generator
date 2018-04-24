package com.epam.test_generator.entities.api;

import java.time.ZonedDateTime;

public interface TokenTrait {

    ZonedDateTime getExpiryDate();

    default boolean isExpired() {
        return ZonedDateTime.now().isAfter(getExpiryDate());
    }

}
