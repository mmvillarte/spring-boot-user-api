package org.exercise.user.application.command.exception;

public class UserCommandException extends RuntimeException {
    public UserCommandException(String message) {
        super(message);
    }
}
