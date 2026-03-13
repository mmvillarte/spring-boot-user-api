package org.exercise.user.application.command.service;

import org.exercise.user.application.command.model.UserCreator;
import org.exercise.user.application.command.model.UserUpdate;
import org.exercise.user.application.command.exception.UserCommandException;
import org.exercise.user.infrastructure.persistence.mapper.UserMapper;
import org.exercise.user.infrastructure.persistence.model.UserEntity;
import org.exercise.user.infrastructure.persistence.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserCommandService {
    private final UserRepository userRepository;

    public UserCommandService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity create(UserCreator userCreator) {
        if(Boolean.TRUE.equals(userRepository.existsByDni(userCreator.dni().value()))) {
            throw new UserCommandException("Unable to create user - " +
                    "User with dni " + userCreator.dni().value() + " already exists");
        }

        UserEntity entityUser = UserMapper.toEntity(userCreator);
        userRepository.save(entityUser);

        return entityUser;
    }

    public UserEntity update(UserUpdate userUpdate) {
        if(!userRepository.existsById(userUpdate.id())) {
            throw new UserCommandException("Unable to update user - " +
                    "User with id " + userUpdate.id() + " does not exist");
        }

        UserEntity entityUser = UserMapper.toEntity(userUpdate);
        userRepository.save(entityUser);

        return entityUser;
    }

    public void delete(UUID id) {
        if(!userRepository.existsById(id)) {
            throw new UserCommandException("Unable to delete user - " +
                    "User with id " + id + " does not exist");
        }

        userRepository.deleteById(id);
    }
}
