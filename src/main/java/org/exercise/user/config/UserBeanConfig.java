package org.exercise.user.config;

import org.exercise.user.application.query.service.UserQueryService;
import org.exercise.user.application.usecase.UserCommandHandler;
import org.exercise.user.application.command.service.UserCommandService;
import org.exercise.user.infrastructure.persistence.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserBeanConfig {
    @Bean
    public UserCommandHandler userCommandHandler(UserCommandService userCommandService, UserQueryService userQueryService) {
        return new UserCommandHandler(userCommandService, userQueryService);
    }

    @Bean
    public UserQueryService userQueryService(UserRepository userRepository) {
        return new UserQueryService(userRepository);
    }
}
