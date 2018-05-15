package com.epam.test_generator.entities.api;

import org.springframework.dao.OptimisticLockingFailureException;

/**
 * Interface for entities which have version field (marked with Hibernate @Version annotation),
 * which is used for optimistic locking.
 * Interface provides some methods for manipulating versions.
 * */
public interface Versionable {
    Long getVersion();

    default void verifyVersion(long version) {
        if (getVersion() != version) {
            throw new OptimisticLockingFailureException(
                "Access denied! Entity was already modified");
        }
    }
}
