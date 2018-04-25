package com.epam.test_generator.services.utils;

import com.epam.test_generator.entities.Case;
import com.epam.test_generator.entities.Project;
import com.epam.test_generator.entities.Step;
import com.epam.test_generator.entities.StepSuggestion;
import com.epam.test_generator.entities.Suit;
import com.epam.test_generator.entities.Tag;
import com.epam.test_generator.entities.User;
import com.epam.test_generator.services.exceptions.BadRequestException;
import com.epam.test_generator.services.exceptions.NotFoundException;
import com.epam.test_generator.services.exceptions.ProjectClosedException;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.springframework.dao.OptimisticLockingFailureException;

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


    /**
     * Verifies that entity wasn't modified by checking it's version
     *
     * @param version expected version
     * @param stepSuggestion entity ti verify unmodified
     */
    public static void verifyVersion(long version, StepSuggestion stepSuggestion) {
        if (stepSuggestion.getVersion() != version) {
            throw new OptimisticLockingFailureException(
                "Access denied! Entity was already modified");
        }
    }
}
