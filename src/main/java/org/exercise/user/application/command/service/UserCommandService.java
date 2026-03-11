package org.exercise.user.application.command.service;

import org.exercise.user.application.command.UserCreator;
import org.exercise.user.application.command.UserUpdate;
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
        UserEntity entityUser = UserMapper.toEntity(userCreator);
        userRepository.save(entityUser);

        return entityUser;
    }

    public UserEntity update(UserUpdate userUpdate) {
        UserEntity entityUser = UserMapper.toEntity(userUpdate);
        userRepository.save(entityUser);

        return entityUser;
    }

    public void delete(UUID id) {
        userRepository.deleteById(id);
    }
}
