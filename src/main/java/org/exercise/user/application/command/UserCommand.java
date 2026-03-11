package org.exercise.user.application.command;

public sealed interface UserCommand permits UserCreator, UserUpdate, UserDeletion {
}
