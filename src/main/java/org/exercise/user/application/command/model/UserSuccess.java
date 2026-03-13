package org.exercise.user.application.command.model;

import org.exercise.user.api.model.UserResult;
import org.springframework.lang.Nullable;

public record UserSuccess<T>(int code, String message, @Nullable T data) implements UserCommandResult<T> {

    @Override
    public UserResult<T> toUserResult() {
        return new UserResult<>(code, message, data);
    }

}
