package org.exercise.user.application.command.model;

import org.exercise.user.domain.model.DNI;
import org.exercise.user.domain.model.Email;

public record CreateUser(String firstName, String lastName, Email email, DNI dni)
        implements UserCommand {
}
