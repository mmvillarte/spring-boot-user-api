package org.exercise.user.application.command.model;

import org.exercise.user.api.model.UserResult;

public sealed interface UserCommandResult<T> permits UserSuccess {

    UserResult<T> toUserResult();

}
