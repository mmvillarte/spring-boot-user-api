package org.exercise.user.application.query.model;

public record UserDTO (
        String firstName,
        String lastName,
        String email,
        String dni
) {}
