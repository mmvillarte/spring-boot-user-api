package org.exercise.user.application.command.model;

public sealed interface UserCommand permits CreateUser, UpdateUser, DeleteUser {
}
