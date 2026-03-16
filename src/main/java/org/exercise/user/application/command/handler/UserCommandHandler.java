package org.exercise.user.application.command.handler;

import org.exercise.user.application.command.model.*;
import org.exercise.user.application.command.service.UserCommandService;
import org.exercise.user.infrastructure.persistence.model.UserEntity;
import org.springframework.http.HttpStatus;

import java.util.function.Supplier;

public class UserCommandHandler {
    private final UserCommandService userCommandService;

    public UserCommandHandler(UserCommandService userCommandService) {
        this.userCommandService = userCommandService;
    }

    public UserCommandResult<UserEntity> handle(CreateUser command) { return handleCreate(command); }
    public UserCommandResult<UserEntity> handle(UpdateUser command) { return handleUpdate(command); }
    public UserCommandResult<Void> handle(DeleteUser command) { return handleDelete(command); }

    private <T> UserCommandResult<T> executeUserCommand (
            Supplier<T> action,
            int successCode,
            String successMessage) {

        return new UserSuccess<>(successCode, successMessage, action.get());
    }

    private UserCommandResult<UserEntity> handleCreate(CreateUser creator) {
        return executeUserCommand(
                () -> userCommandService.create(creator),
                HttpStatus.CREATED.value(),
                "User successfully created"
        );
    }

    private UserCommandResult<UserEntity> handleUpdate(UpdateUser update) {
        return executeUserCommand(
                () -> userCommandService.update(update),
                HttpStatus.OK.value(),
                "User successfully updated"
        );
    }

    private UserCommandResult<Void> handleDelete(DeleteUser deletion) {
        return executeUserCommand(
                () -> {
                    userCommandService.delete(deletion.id());
                    // Potential improvement - Empty response instead of null
                    return null;
                },
                HttpStatus.NO_CONTENT.value(),
                "User successfully deleted"
        );
    }
}
