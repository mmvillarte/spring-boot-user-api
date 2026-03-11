package org.exercise.user.infrastructure.persistence.mapper;

import org.exercise.user.application.command.UserCreator;
import org.exercise.user.application.command.UserUpdate;
import org.exercise.user.domain.dto.UserDTO;
import org.exercise.user.infrastructure.persistence.model.UserEntity;

public class UserMapper {

    public static UserEntity toEntity(UserCreator userCreator) {
        return new UserEntity(
                userCreator.firstName(),
                userCreator.lastName(),
                userCreator.email().value(),
                userCreator.dni().value()
        );
    }

    public static UserEntity toEntity(UserUpdate userUpdate) {
        return new UserEntity(
                userUpdate.id(),
                userUpdate.firstName(),
                userUpdate.lastName(),
                userUpdate.email().value(),
                userUpdate.dni().value()
        );
    }

    public static UserDTO toDomain(UserEntity userEntity) {
        return new UserDTO(
                userEntity.getFirstName(),
                userEntity.getLastName(),
                userEntity.getEmail(),
                userEntity.getDni()
        );
    }
}
