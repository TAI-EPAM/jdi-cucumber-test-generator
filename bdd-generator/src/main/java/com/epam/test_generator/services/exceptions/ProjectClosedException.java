package com.epam.test_generator.services.exceptions;

import com.epam.test_generator.controllers.GlobalExceptionController;
import com.epam.test_generator.entities.Project;

/**
 * Exception that can be thrown when {@link Project} API user tries to modify project
 * that has already been closed (status readonly). {@link GlobalExceptionController} catches
 * this exception. To get more info about project projectId field in {@link ProjectClosedException}
 * instance can be used.
 */
public class ProjectClosedException extends RuntimeException {

    private Long projectId;

    public ProjectClosedException(String message, Long projectId) {
        super(message);
        this.projectId = projectId;
    }
}
