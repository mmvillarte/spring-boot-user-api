package org.exercise.user.application.command.service;

import org.exercise.user.application.command.model.CreateUser;
import org.exercise.user.application.command.model.UpdateUser;
import org.exercise.user.domain.model.DNI;
import org.exercise.user.domain.model.Email;
import org.exercise.user.infrastructure.persistence.model.UserEntity;
import org.exercise.user.infrastructure.persistence.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserCommandServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserCommandService userCommandService;

    private Email mockEmail;
    private DNI mockDni;

    @BeforeEach
    void setUp() {
        //noinspection resource
        MockitoAnnotations.openMocks(this);
        mockEmail = mock(Email.class);
        mockDni = mock(DNI.class);
    }

    @Test
    @DisplayName("Should create a user and save it to repository")
    void createUserAndSaveToRepository() {
        CreateUser createUser = new CreateUser("John", "Doe", mockEmail, mockDni);
        UUID userId = UUID.randomUUID();
        UserEntity expectedEntity = new UserEntity(userId, "John", "Doe", "john.doe@example.com", "12345678T");

        when(userRepository.existsByDni(expectedEntity.getDni())).thenReturn(false);
        when(userRepository.save(any(UserEntity.class))).thenReturn(expectedEntity);

        UserEntity result = userCommandService.create(createUser);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    @DisplayName("Should update a user and save it to repository")
    void updateUserAndSaveToRepository() {
        UUID userId = UUID.randomUUID();
        UpdateUser updateUser = new UpdateUser(userId, "Jane", "Smith", mockEmail, mockDni);
        UserEntity expectedEntity = new UserEntity(userId, "Jane", "Smith", "jane.smith@example.com", "87654321G");

        when(userRepository.existsById(userId)).thenReturn(true);
        when(userRepository.save(any(UserEntity.class))).thenReturn(expectedEntity);

        UserEntity result = userCommandService.update(updateUser);

        assertNotNull(result);
        assertEquals("Jane", result.getFirstName());
        assertEquals("Smith", result.getLastName());
        assertEquals(userId, result.getId());
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    @DisplayName("Should delete a user by ID")
    void deleteUserById() {
        UUID userId = UUID.randomUUID();

        when(userRepository.existsById(userId)).thenReturn(true);
        userCommandService.delete(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    @DisplayName("Should handle create user with null values in mocks")
    void createUserWithMockedNullValues() {
        CreateUser createUser = new CreateUser("Test", "User", mockEmail, mockDni);
        UserEntity expectedEntity = new UserEntity(UUID.randomUUID(), "Test", "User", null, null);

        when(userRepository.existsByDni(expectedEntity.getDni())).thenReturn(false);
        when(userRepository.save(any(UserEntity.class))).thenReturn(expectedEntity);

        UserEntity result = userCommandService.create(createUser);

        assertNotNull(result);
        assertEquals("Test", result.getFirstName());
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }
}
