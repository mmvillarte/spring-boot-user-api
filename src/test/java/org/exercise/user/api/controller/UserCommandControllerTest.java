package org.exercise.user.api.controller;

import org.exercise.user.application.command.model.CreateUser;
import org.exercise.user.application.command.model.DeleteUser;
import org.exercise.user.application.command.model.UpdateUser;
import org.exercise.user.application.command.handler.UserCommandHandler;
import org.exercise.user.application.command.model.UserCommandResult;
import org.exercise.user.application.command.model.UserSuccess;
import org.exercise.user.application.query.model.UserDTO;
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
        MockitoAnnotations.openMocks(this);

        mockedDNI = mockStatic(DNI.class);
        mockedEmail = mockStatic(Email.class);

        mockedDNI.when(() -> DNI.of(anyString())).thenReturn(mock(DNI.class));
        mockedEmail.when(() -> Email.of(anyString())).thenReturn(mock(Email.class));
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
    class CreateUserTests {

        @Test
        @DisplayName("Should create user successfully with valid data")
        void createUserWithValidData() {
            UserDTO userDTO = new UserDTO("John", "Doe", "john.doe@example.com", "12345678T");
            UserSuccess<UUID> expectedResult = new UserSuccess<>(201, "User created", UUID.randomUUID());

            doReturn(expectedResult).when(handler).handle(any(CreateUser.class));
            UserCommandResult<?> result = controller.createUser(userDTO);

            assertNotNull(result);
            assertEquals(expectedResult, result);

            ArgumentCaptor<CreateUser> commandCaptor = ArgumentCaptor.forClass(CreateUser.class);
            verify(handler, times(1)).handle(commandCaptor.capture());

            CreateUser capturedCommand = commandCaptor.getValue();
            assertEquals("John", capturedCommand.firstName());
            assertEquals("Doe", capturedCommand.lastName());
        }

        @Test
        @DisplayName("Should delegate to handler with correct CreateUser command")
        void delegateToHandlerWithCorrectCommand() {
            UserDTO userDTO = new UserDTO("Jane", "Smith", "jane.smith@example.com", "87654321G");
            doReturn(new UserSuccess<>(201, "Created", null)).when(handler).handle(any(CreateUser.class));

            controller.createUser(userDTO);

            verify(handler, times(1)).handle(any(CreateUser.class));
        }

        @Test
        @DisplayName("Should pass email and DNI mocks to handler")
        void passEmailAndDniMocksToHandler() {
            UserDTO userDTO = new UserDTO("Test", "User", "test@example.com", "11111111G");
            doReturn(new UserSuccess<>(201, "Created", UUID.randomUUID())).when(handler).handle(any(CreateUser.class));

            controller.createUser(userDTO);

            ArgumentCaptor<CreateUser> commandCaptor = ArgumentCaptor.forClass(CreateUser.class);
            verify(handler).handle(commandCaptor.capture());

            CreateUser capturedCommand = commandCaptor.getValue();
            assertNotNull(capturedCommand.email());
            assertNotNull(capturedCommand.dni());
        }
    }

    @Nested
    @DisplayName("updateUser method")
    class UpdateUserTests {

        @Test
        @DisplayName("Should update user successfully with valid data")
        void updateUserWithValidData() {
            UUID userId = UUID.randomUUID();
            UserDTO userDTO = new UserDTO("Updated", "Name", "updated@example.com", "11111111G");
            UserSuccess<Void> expectedResult = new UserSuccess<>(200, "User updated", null);

            doReturn(expectedResult).when(handler).handle(any(UpdateUser.class));

            UserCommandResult<?> result = controller.updateUser(userId, userDTO);

            assertNotNull(result);
            assertEquals(expectedResult, result);

            ArgumentCaptor<UpdateUser> commandCaptor = ArgumentCaptor.forClass(UpdateUser.class);
            verify(handler, times(1)).handle(commandCaptor.capture());

            UpdateUser capturedCommand = commandCaptor.getValue();
            assertEquals(userId, capturedCommand.id());
            assertEquals("Updated", capturedCommand.firstName());
        }

        @Test
        @DisplayName("Should pass correct user ID to update command")
        void passCorrectUserIdToUpdateCommand() {
            UUID userId = UUID.randomUUID();
            UserDTO userDTO = new UserDTO("Test", "User", "test@example.com", "22222222N");
            doReturn(new UserSuccess<>(200, "Updated", null)).when(handler).handle(any(UpdateUser.class));

            controller.updateUser(userId, userDTO);

            ArgumentCaptor<UpdateUser> commandCaptor = ArgumentCaptor.forClass(UpdateUser.class);
            verify(handler).handle(commandCaptor.capture());
            assertEquals(userId, commandCaptor.getValue().id());
        }

        @Test
        @DisplayName("Should include all user data in update command")
        void includeAllUserDataInUpdateCommand() {
            UUID userId = UUID.randomUUID();
            UserDTO userDTO = new UserDTO("Alice", "Brown", "alice@example.com", "22222222N");
            doReturn(new UserSuccess<>(200, "Updated", null)).when(handler).handle(any(UpdateUser.class));

            controller.updateUser(userId, userDTO);

            ArgumentCaptor<UpdateUser> commandCaptor = ArgumentCaptor.forClass(UpdateUser.class);
            verify(handler).handle(commandCaptor.capture());

            UpdateUser capturedCommand = commandCaptor.getValue();
            assertEquals("Alice", capturedCommand.firstName());
            assertEquals("Brown", capturedCommand.lastName());
            assertNotNull(capturedCommand.email());
            assertNotNull(capturedCommand.dni());
        }
    }

    @Nested
    @DisplayName("deleteUser method")
    class DeleteUserTests {

        @Test
        @DisplayName("Should delete user successfully with valid ID")
        void deleteUserWithValidId() {
            UUID userId = UUID.randomUUID();
            UserSuccess<Void> expectedResult = new UserSuccess<>(204, "User deleted", null);

            doReturn(expectedResult).when(handler).handle(any(DeleteUser.class));

            UserCommandResult<?> result = controller.deleteUser(userId);

            assertNotNull(result);
            assertEquals(expectedResult, result);

            ArgumentCaptor<DeleteUser> commandCaptor = ArgumentCaptor.forClass(DeleteUser.class);
            verify(handler, times(1)).handle(commandCaptor.capture());

            DeleteUser capturedCommand = commandCaptor.getValue();
            assertEquals(userId, capturedCommand.id());
        }

        @Test
        @DisplayName("Should delegate to handler for user deletion")
        void delegateToHandlerForDeletion() {
            UUID userId = UUID.randomUUID();
            doReturn(new UserSuccess<>(204, "Deleted", null)).when(handler).handle(any(DeleteUser.class));

            controller.deleteUser(userId);

            verify(handler, times(1)).handle(any(DeleteUser.class));
        }

        @Test
        @DisplayName("Should pass correct user ID to delete command")
        void passCorrectUserIdToDeleteCommand() {
            UUID userId = UUID.randomUUID();
            doReturn(new UserSuccess<>(204, "Deleted", null)).when(handler).handle(any(DeleteUser.class));

            controller.deleteUser(userId);

            ArgumentCaptor<DeleteUser> commandCaptor = ArgumentCaptor.forClass(DeleteUser.class);
            verify(handler).handle(commandCaptor.capture());
            assertEquals(userId, commandCaptor.getValue().id());
        }
    }
}



