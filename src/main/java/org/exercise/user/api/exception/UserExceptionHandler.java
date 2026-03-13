package org.exercise.user.api.exception;

import org.exercise.user.application.command.exception.UserCommandException;
import org.exercise.user.application.command.exception.UserNotFoundException;
import org.exercise.user.application.command.model.UserError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserExceptionHandler {
    @ExceptionHandler(UserCommandException.class)
    public UserError handleUserCommandException (
            UserCommandException ex) {

        return new UserError(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }

    @ExceptionHandler(NullPointerException.class)
    public UserError handleNullPointerException (
            NullPointerException ex) {

        return new UserError(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public UserError handleIllegalArgumentException (
            IllegalArgumentException ex) {

        return new UserError(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public UserError handleGenericException(Exception ex) {
        return new UserError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Unexpected error occurred");
    }
}
