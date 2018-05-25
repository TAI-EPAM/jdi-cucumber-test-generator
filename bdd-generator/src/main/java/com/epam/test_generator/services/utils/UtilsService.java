package com.epam.test_generator.services.utils;

import com.epam.test_generator.entities.Case;
import com.epam.test_generator.entities.Project;
import com.epam.test_generator.entities.Step;
import com.epam.test_generator.entities.Suit;
import com.epam.test_generator.entities.Tag;
import com.epam.test_generator.entities.User;
import com.epam.test_generator.services.exceptions.NotFoundException;

/**
 * Class which provides different check methods for
 * {@link Project}, {@link User}, {@link Suit}, {@link Case}, {@link Step} and {@link Tag} objects.
 */
public class UtilsService {

    public static <T> T checkNotNull(T obj) {
        if (obj == null) {
            throw new NotFoundException();
        } else {
            return obj;
        }
    }
}
