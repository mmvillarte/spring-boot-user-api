package org.exercise.user.api.controller;

import org.exercise.user.application.command.UserCreator;
import org.exercise.user.application.command.UserDeletion;
import org.exercise.user.application.command.UserUpdate;
import org.exercise.user.application.usecase.UserCommandHandler;
import org.exercise.user.application.usecase.UserCommandResult;
import org.exercise.user.application.usecase.UserSuccess;
import org.exercise.user.domain.dto.UserDTO;
import org.exercise.user.domain.model.DNI;
import org.exercise.user.domain.model.Email;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UserCommandControllerTest {

    @Mock
    private UserCommandHandler handler;

    @InjectMocks
    private UserCommandController controller;

    private MockedStatic<DNI> mockedDNI;
    private MockedStatic<Email> mockedEmail;

    @BeforeEach
    void setUp() {
        //noinspection resource
        MockitoAnnotations.openMocks(this);

        mockedDNI = mockStatic(DNI.class);
        mockedEmail = mockStatic(Email.class);

        mockedDNI.when(() -> DNI.of(anyString())).thenAnswer(invocation -> {
            DNI mock = mock(DNI.class);
            return mock;
        });

        mockedEmail.when(() -> Email.of(anyString())).thenAnswer(invocation -> {
            Email mock = mock(Email.class);
            return mock;
        });
    }

    void tearDown() {
        mockedDNI.close();
        mockedEmail.close();
    }

    @AfterEach
    void cleanup() {
        if (mockedDNI != null) {
            mockedDNI.close();
        }
        if (mockedEmail != null) {
            mockedEmail.close();
        }
    }

    @Nested
    @DisplayName("createUser method")
    class UserCreatorTests {

        @Test
        @DisplayName("Should create user successfully with valid data")
        void createUserWithValidData() {
            UserDTO userDTO = new UserDTO("John", "Doe", "john.doe@example.com", "12345678T");
            UserSuccess<UUID> expectedResult = new UserSuccess<>(201, "User created", UUID.randomUUID());

            doReturn(expectedResult).when(handler).handle(any(UserCreator.class));
            UserCommandResult<?> result = controller.createUser(userDTO);

            assertNotNull(result);
            assertEquals(expectedResult, result);

            ArgumentCaptor<UserCreator> commandCaptor = ArgumentCaptor.forClass(UserCreator.class);
            verify(handler, times(1)).handle(commandCaptor.capture());

            UserCreator capturedCommand = commandCaptor.getValue();
            assertEquals("John", capturedCommand.firstName());
            assertEquals("Doe", capturedCommand.lastName());
        }

        @Test
        @DisplayName("Should delegate to handler with correct CreateUser command")
        void delegateToHandlerWithCorrectCommand() {
            UserDTO userDTO = new UserDTO("Jane", "Smith", "jane.smith@example.com", "87654321G");
            doReturn(new UserSuccess<>(201, "Created", null)).when(handler).handle(any(UserCreator.class));

            controller.createUser(userDTO);

            verify(handler, times(1)).handle(any(UserCreator.class));
        }

        @Test
        @DisplayName("Should pass email and DNI mocks to handler")
        void passEmailAndDniMocksToHandler() {
            UserDTO userDTO = new UserDTO("Test", "User", "test@example.com", "11111111G");
            doReturn(new UserSuccess<>(201, "Created", UUID.randomUUID())).when(handler).handle(any(UserCreator.class));

            controller.createUser(userDTO);

            ArgumentCaptor<UserCreator> commandCaptor = ArgumentCaptor.forClass(UserCreator.class);
            verify(handler).handle(commandCaptor.capture());

            UserCreator capturedCommand = commandCaptor.getValue();
            assertNotNull(capturedCommand.email());
            assertNotNull(capturedCommand.dni());
        }
    }

    @Nested
    @DisplayName("updateUser method")
    class UserUpdateTests {

        @Test
        @DisplayName("Should update user successfully with valid data")
        void updateUserWithValidData() {
            UUID userId = UUID.randomUUID();
            UserDTO userDTO = new UserDTO("Updated", "Name", "updated@example.com", "11111111G");
            UserSuccess<Void> expectedResult = new UserSuccess<>(200, "User updated", null);

            doReturn(expectedResult).when(handler).handle(any(UserUpdate.class));

            UserCommandResult<?> result = controller.updateUser(userId, userDTO);

            assertNotNull(result);
            assertEquals(expectedResult, result);

            ArgumentCaptor<UserUpdate> commandCaptor = ArgumentCaptor.forClass(UserUpdate.class);
            verify(handler, times(1)).handle(commandCaptor.capture());

            UserUpdate capturedCommand = commandCaptor.getValue();
            assertEquals(userId, capturedCommand.id());
            assertEquals("Updated", capturedCommand.firstName());
        }

        @Test
        @DisplayName("Should pass correct user ID to update command")
        void passCorrectUserIdToUpdateCommand() {
            UUID userId = UUID.randomUUID();
            UserDTO userDTO = new UserDTO("Test", "User", "test@example.com", "22222222N");
            doReturn(new UserSuccess<>(200, "Updated", null)).when(handler).handle(any(UserUpdate.class));

            controller.updateUser(userId, userDTO);

            ArgumentCaptor<UserUpdate> commandCaptor = ArgumentCaptor.forClass(UserUpdate.class);
            verify(handler).handle(commandCaptor.capture());
            assertEquals(userId, commandCaptor.getValue().id());
        }

        @Test
        @DisplayName("Should include all user data in update command")
        void includeAllUserDataInUpdateCommand() {
            UUID userId = UUID.randomUUID();
            UserDTO userDTO = new UserDTO("Alice", "Brown", "alice@example.com", "22222222N");
            doReturn(new UserSuccess<>(200, "Updated", null)).when(handler).handle(any(UserUpdate.class));

            controller.updateUser(userId, userDTO);

            ArgumentCaptor<UserUpdate> commandCaptor = ArgumentCaptor.forClass(UserUpdate.class);
            verify(handler).handle(commandCaptor.capture());

            UserUpdate capturedCommand = commandCaptor.getValue();
            assertEquals("Alice", capturedCommand.firstName());
            assertEquals("Brown", capturedCommand.lastName());
            assertNotNull(capturedCommand.email());
            assertNotNull(capturedCommand.dni());
        }
    }

    @Nested
    @DisplayName("deleteUser method")
    class UserDeletionTests {

        @Test
        @DisplayName("Should delete user successfully with valid ID")
        void deleteUserWithValidId() {
            UUID userId = UUID.randomUUID();
            UserSuccess<Void> expectedResult = new UserSuccess<>(204, "User deleted", null);

            doReturn(expectedResult).when(handler).handle(any(UserDeletion.class));

            UserCommandResult<?> result = controller.deleteUser(userId);

            assertNotNull(result);
            assertEquals(expectedResult, result);

            ArgumentCaptor<UserDeletion> commandCaptor = ArgumentCaptor.forClass(UserDeletion.class);
            verify(handler, times(1)).handle(commandCaptor.capture());

            UserDeletion capturedCommand = commandCaptor.getValue();
            assertEquals(userId, capturedCommand.id());
        }

        @Test
        @DisplayName("Should delegate to handler for user deletion")
        void delegateToHandlerForDeletion() {
            UUID userId = UUID.randomUUID();
            doReturn(new UserSuccess<>(204, "Deleted", null)).when(handler).handle(any(UserDeletion.class));

            controller.deleteUser(userId);

            verify(handler, times(1)).handle(any(UserDeletion.class));
        }

        @Test
        @DisplayName("Should pass correct user ID to delete command")
        void passCorrectUserIdToDeleteCommand() {
            UUID userId = UUID.randomUUID();
            doReturn(new UserSuccess<>(204, "Deleted", null)).when(handler).handle(any(UserDeletion.class));

            controller.deleteUser(userId);

            ArgumentCaptor<UserDeletion> commandCaptor = ArgumentCaptor.forClass(UserDeletion.class);
            verify(handler).handle(commandCaptor.capture());
            assertEquals(userId, commandCaptor.getValue().id());
        }
    }
}



