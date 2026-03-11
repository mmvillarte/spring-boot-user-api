package org.exercise.user.application.usecase;

import org.exercise.user.application.command.UserCommand;
import org.exercise.user.application.command.UserCreator;
import org.exercise.user.application.command.UserDeletion;
import org.exercise.user.application.command.UserUpdate;
import org.exercise.user.application.command.service.UserCommandService;
import org.exercise.user.application.query.service.UserQueryService;
import org.exercise.user.infrastructure.persistence.model.UserEntity;

public class UserCommandHandler {
    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;

    public UserCommandHandler(UserCommandService userCommandService, UserQueryService userQueryService) {
        this.userCommandService = userCommandService;
        this.userQueryService = userQueryService;
    }

    public UserCommandResult<?> handle(UserCommand command) {
        switch (command) {
            case UserCreator c -> {
                UserEntity userEntity = userCommandService.create(c);
                if(userEntity == null) {
                    return new UserError<>(500, "Failed to create user");
                } else {
                    return new UserSuccess<>(201, "User successfully created", userEntity);
                }
            }
            case UserUpdate u -> {
                UserEntity userEntity = userCommandService.update(u);
                if(userEntity == null) {
                    return new UserError<>(500, "Failed to update user");
                } else {
                    return new UserSuccess<>(200, "User successfully updated", userEntity);
                }
            }
            case UserDeletion d -> {
                userCommandService.delete(d.id());

                if (userQueryService.findById(d.id()) != null) {
                    return new UserError<>(500, "Failed to delete user");
                } else {
                    return new UserSuccess<>(200, "User successfully deleted", null);
                }
            }
        }
    }
}
