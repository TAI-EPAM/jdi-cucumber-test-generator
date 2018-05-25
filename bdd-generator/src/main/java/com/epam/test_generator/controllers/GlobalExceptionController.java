package com.epam.test_generator.controllers;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.epam.test_generator.dto.ErrorDTO;
import com.epam.test_generator.dto.ValidationErrorsDTO;
import com.epam.test_generator.services.exceptions.BadRequestException;
import com.epam.test_generator.services.exceptions.BadRoleException;
import com.epam.test_generator.services.exceptions.IncorrectURI;
import com.epam.test_generator.services.exceptions.JiraRuntimeException;
import com.epam.test_generator.services.exceptions.NotFoundException;
import com.epam.test_generator.services.exceptions.ProjectClosedException;
import net.rcarz.jiraclient.JiraException;
import net.rcarz.jiraclient.RestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;


/**
 * This class handles all exceptions to make sure that in case of error server will not stop
 * and client will receive response with error code.
 */
@ControllerAdvice
public class GlobalExceptionController {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionController.class);


    @ExceptionHandler(JiraRuntimeException.class)
    public ResponseEntity<String> jiraException(JiraException ex) {
        logger.error("Catch Jira Exception", ex);
        if (ex.getCause() instanceof RestException) {
            RestException restException = (RestException) ex.getCause();
            if (restException.getHttpStatusCode() == 404) {
                return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MailSendException.class)
    public ResponseEntity<String> noInternetConnection(MailSendException ex) {
        logger.error("No connection to mail Server", ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handleRunTimeException(Exception ex) {
        logger.error("Internal Server Error occurred", ex);
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Void> handleNotFoundException(NotFoundException ex) {
        logger.warn("No data found for requested parameters", ex);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleMethodArgumentTypeMismatchException(
        MethodArgumentTypeMismatchException ex) {
        logger.warn("Invalid arguments entered", ex);
        return new ResponseEntity<>("Invalid argument: " + ex.getName(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorDTO> handleBadRequestException(BadRequestException ex) {
        return new ResponseEntity<>(new ErrorDTO(ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<ErrorDTO> handleOptimisticLockingFailureException(
        OptimisticLockingFailureException ex) {
        logger.warn("Error: failed to save entity: already modified", ex);
        return new ResponseEntity<>(new ErrorDTO(ex), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ProjectClosedException.class)
    public ResponseEntity<String> handleBadRequestException(ProjectClosedException ex) {
        logger.warn("Error: The try to change closed project", ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorsDTO> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException ex) {
        ValidationErrorsDTO validationErrorsDTO = new ValidationErrorsDTO(ex);
        logger.warn("Validation Error: invalid arguments entered", ex);
        return new ResponseEntity<>(validationErrorsDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = AuthenticationException.class)
    public ResponseEntity<ErrorDTO> loginFailed(AuthenticationException ex) {
        logger.warn("Authentication Error: login failed", ex);
        return new ResponseEntity<>(new ErrorDTO(ex), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {JWTVerificationException.class})
    public ResponseEntity<ErrorDTO> tokenInvalid(JWTVerificationException ex) {
        logger.warn("Authentication Error: invalid token entered", ex);
        return new ResponseEntity<>(new ErrorDTO(ex), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    public ResponseEntity<ErrorDTO> roleInvalid(AccessDeniedException ex) {
        logger.warn("Access denied", ex);
        return new ResponseEntity<>(new ErrorDTO(ex), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {BadRoleException.class})
    public ResponseEntity<ErrorDTO> roleUnexist(BadRoleException ex) {
        logger.warn("Access denied: role does not exist", ex);
        return new ResponseEntity<>(new ErrorDTO(ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {IncorrectURI.class})
    public ResponseEntity<ErrorDTO> incorrectURI(IncorrectURI ex) {
        logger.warn("Incorrect URI", ex);

        return new ResponseEntity<>(new ErrorDTO(ex), HttpStatus.I_AM_A_TEAPOT);
    }

}