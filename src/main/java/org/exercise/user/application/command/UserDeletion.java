package org.exercise.user.application.command;

import java.util.UUID;

public record UserDeletion(UUID id)
        implements UserCommand {
}
