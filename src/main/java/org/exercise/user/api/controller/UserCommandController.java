package org.exercise.user.api.controller;

import org.exercise.user.application.command.model.UserCreator;
import org.exercise.user.application.command.model.UserDeletion;
import org.exercise.user.application.command.model.UserUpdate;
import org.exercise.user.application.command.handler.UserCommandHandler;
import org.exercise.user.application.command.model.UserCommandResult;
import org.exercise.user.application.query.model.UserDTO;
import org.exercise.user.domain.model.DNI;
import org.exercise.user.domain.model.Email;
import org.exercise.user.infrastructure.persistence.model.UserEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserCommandController {
    private final UserCommandHandler handler;

    public UserCommandController(UserCommandHandler handler) {
        this.handler = handler;
    }

    @PostMapping
    public UserCommandResult<UserEntity> createUser(@RequestBody UserDTO userDTO) {
        Objects.requireNonNull(userDTO, "User cannot be null");

        var command = new UserCreator(
                userDTO.firstName(),
                userDTO.lastName(),
                Email.of(userDTO.email()),
                DNI.of(userDTO.dni())
        );

        return handler.handle(command);
    }

    @PutMapping
    public UserCommandResult<UserEntity> updateUser(@RequestParam UUID id,
                             @RequestBody UserDTO userDTO) {
        Objects.requireNonNull(id, "User Id cannot be null");
        Objects.requireNonNull(userDTO, "User cannot be null");

        var command = new UserUpdate(
                id,
                userDTO.firstName(),
                userDTO.lastName(),
                Email.of(userDTO.email()),
                DNI.of(userDTO.dni())
        );

        return handler.handle(command);
    }

    @DeleteMapping
    public UserCommandResult<Void> deleteUser(@RequestParam UUID id) {
        Objects.requireNonNull(id, "User Id cannot be null");

        var command = new UserDeletion(id);

        return handler.handle(command);
    }
}
