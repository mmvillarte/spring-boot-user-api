package org.exercise.user.api.controller;

import org.exercise.user.api.model.UserResult;
import org.exercise.user.application.query.service.UserQueryService;
import org.exercise.user.application.query.model.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserQueryControllerTest {

    @Mock
    private UserQueryService userQueryService;

    @InjectMocks
    private UserQueryController controller;

    @BeforeEach
    void setUp() {
        //noinspection resource
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("findById method")
    class FindByIdTests {

        @Test
        @DisplayName("Should return user with 200 status when user exists")
        void returnUserWithOkStatusWhenUserExists() {
            UUID userId = UUID.randomUUID();
            UserDTO expectedUser = new UserDTO("John", "Doe", "john@example.com", "12345678T");

            when(userQueryService.findById(userId)).thenReturn(expectedUser);

            ResponseEntity<UserResult<UserDTO>> response = controller.findById(userId);

            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(200, response.getBody().code());
            assertEquals(expectedUser, response.getBody().data());
            assertEquals("User Found for provided id", response.getBody().message());
            verify(userQueryService, times(1)).findById(userId);
        }

        @Test
        @DisplayName("Should return 404 status when user does not exist")
        void return404StatusWhenUserNotExists() {
            UUID userId = UUID.randomUUID();

            when(userQueryService.findById(userId)).thenReturn(null);

            ResponseEntity<UserResult<UserDTO>> response = controller.findById(userId);

            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(404, response.getBody().code());
            assertNull(response.getBody().data());
            assertEquals("User Not Found for provided id", response.getBody().message());
            verify(userQueryService, times(1)).findById(userId);
        }

        @Test
        @DisplayName("Should delegate to service with correct user ID")
        void delegateToServiceWithCorrectUserId() {
            UUID userId = UUID.randomUUID();
            UserDTO expectedUser = new UserDTO("Jane", "Smith", "jane@example.com", "87654321G");

            when(userQueryService.findById(userId)).thenReturn(expectedUser);

            controller.findById(userId);

            verify(userQueryService, times(1)).findById(userId);
        }

        @Test
        @DisplayName("Should return correct user data in response body")
        void returnCorrectUserDataInResponseBody() {
            UUID userId = UUID.randomUUID();
            UserDTO expectedUser = new UserDTO("Alice", "Brown", "alice@example.com", "22222222N");

            when(userQueryService.findById(userId)).thenReturn(expectedUser);

            ResponseEntity<UserResult<UserDTO>> response = controller.findById(userId);

            UserResult<UserDTO> result = response.getBody();
            assertNotNull(result);
            assertEquals("Alice", result.data().firstName());
            assertEquals("Brown", result.data().lastName());
            assertEquals("alice@example.com", result.data().email());
            assertEquals("22222222N", result.data().dni());
        }

        @Test
        @DisplayName("Should always return 200 OK HTTP status code")
        void alwaysReturn200OkHttpStatus() {
            UUID userId = UUID.randomUUID();

            when(userQueryService.findById(userId)).thenReturn(null);

            ResponseEntity<UserResult<UserDTO>> response = controller.findById(userId);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(200, response.getStatusCode().value());
        }

        @Test
        @DisplayName("Should include correct message when user found")
        void includeCorrectMessageWhenUserFound() {
            UUID userId = UUID.randomUUID();
            UserDTO expectedUser = new UserDTO("Test", "User", "test@example.com", "11111111G");

            when(userQueryService.findById(userId)).thenReturn(expectedUser);

            ResponseEntity<UserResult<UserDTO>> response = controller.findById(userId);

            assertNotNull(response.getBody());
            assertEquals("User Found for provided id", response.getBody().message());
        }

        @Test
        @DisplayName("Should include correct message when user not found")
        void includeCorrectMessageWhenUserNotFound() {
            UUID userId = UUID.randomUUID();

            when(userQueryService.findById(userId)).thenReturn(null);

            ResponseEntity<UserResult<UserDTO>> response = controller.findById(userId);

            assertNotNull(response.getBody());
            assertEquals("User Not Found for provided id", response.getBody().message());
        }
    }
}




