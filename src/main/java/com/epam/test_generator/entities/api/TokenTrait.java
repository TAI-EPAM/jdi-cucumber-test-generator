package com.epam.test_generator.entities.api;

import java.util.Date;

public interface TokenTrait {

    Date getExpiryDate();

    default boolean isExpired() {
        return new Date().after(getExpiryDate());
    }

}
