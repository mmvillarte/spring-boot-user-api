package org.exercise.user.application.command;

import org.exercise.user.domain.model.DNI;
import org.exercise.user.domain.model.Email;

public record UserCreator(String firstName, String lastName, Email email, DNI dni)
        implements UserCommand {
}
