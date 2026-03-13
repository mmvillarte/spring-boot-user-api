package org.exercise.user.application.query.service;

import org.exercise.user.application.query.model.UserDTO;
import org.exercise.user.infrastructure.persistence.mapper.UserMapper;
import org.exercise.user.infrastructure.persistence.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserQueryService {

    private final UserRepository userRepository;

    public UserQueryService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTO findById(UUID id) {
        return userRepository.findById(id)
                .map(UserMapper::toDomain)
                .orElse(null);
    }


}
