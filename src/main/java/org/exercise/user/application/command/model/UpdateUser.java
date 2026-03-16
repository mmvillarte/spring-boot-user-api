package org.exercise.user.application.command.model;

import org.exercise.user.domain.model.DNI;
import org.exercise.user.domain.model.Email;

import java.util.UUID;

public record UpdateUser(UUID id, String firstName, String lastName, Email email, DNI dni)
        implements UserCommand {
}
