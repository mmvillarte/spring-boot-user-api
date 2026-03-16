package org.exercise.user.application.command.model;

import java.util.UUID;

public record DeleteUser(UUID id)
        implements UserCommand {
}
