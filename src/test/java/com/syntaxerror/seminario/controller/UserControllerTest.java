package com.syntaxerror.seminario.controller;

import com.syntaxerror.seminario.dto.UserLoginRequest;
import com.syntaxerror.seminario.dto.UserRegistrationRequest;
import com.syntaxerror.seminario.dto.UserUpdateRequest;
import com.syntaxerror.seminario.service.AuthenticationService;
import com.syntaxerror.seminario.service.UserService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private UserService userService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void signUpSuccessfully() {
        UserRegistrationRequest request = UserRegistrationRequest.builder()
                .email("email")
                .password("password")
                .username("username").build();

        userController.SignUp(request);

        verify(userService, times(1)).createUser(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    public void signUpWithException() {
        UserRegistrationRequest request = UserRegistrationRequest.builder()
            .username("username")
            .email("email")
            .password("password")
            .build();
        doThrow(new RuntimeException("Error")).when(userService).createUser(anyString(), anyString(), anyString(), anyString());

        ResponseEntity<String> response = userController.SignUp(request);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Error", response.getBody());
    }

    @Test
    public void signInSuccessfully() {
        UserLoginRequest request = UserLoginRequest.builder()
            .username("username")
            .password("password")
            .build();
        when(authenticationService.signIn(anyString(), anyString())).thenReturn("Token");

        ResponseEntity<String> response = userController.SignIn(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Token", response.getBody());
    }

    @Test
    public void signInWithException() {
        UserLoginRequest request = UserLoginRequest.builder()
            .username("username")
            .password("password")
            .build();
        when(authenticationService.signIn(anyString(), anyString())).thenThrow(new RuntimeException("Error"));

        ResponseEntity<String> response = userController.SignIn(request);

        assertEquals(400, response.getStatusCode().value());
        assertEquals("Error", response.getBody());
    }

    @Test
    public void updateUserSuccessfully() {
        UserUpdateRequest request = UserUpdateRequest.builder()
            .username(Optional.of("username"))
            .email(Optional.of("email"))
            .password(Optional.of("password"))
            .build();
        when(authenticationService.validateRequest(anyString(), anyLong())).thenReturn(true);
        doNothing().when(userService).updateUser(any(Long.class), any(Optional.class), any(Optional.class), any(Optional.class));

        ResponseEntity<String> response = userController.updateUser(request, 1L, "Bearer Token");

        assertEquals(200, response.getStatusCode().value());
        assertEquals("User updated successfully!", response.getBody());
    }

    @Test
    public void updateUserUnauthorized() {
        UserUpdateRequest request = UserUpdateRequest.builder()
                .username(Optional.of("username"))
                .email(Optional.of("email"))
                .password(Optional.of("password"))
                .build();
        when(authenticationService.validateRequest(anyString(), anyLong())).thenReturn(false);

        ResponseEntity<String> response = userController.updateUser(request, 1L, "Bearer Token");

        assertEquals(400, response.getStatusCode().value());
        assertEquals("Usuario no autorizado para realizar esta acción", response.getBody());
    }

    @Test
    public void updateUserWithException() {
        UserUpdateRequest request = UserUpdateRequest.builder()
                .username(Optional.of("username"))
                .email(Optional.of("email"))
                .password(Optional.of("password"))
                .build();
        when(authenticationService.validateRequest(anyString(), anyLong())).thenReturn(true);
        doThrow(new RuntimeException("Error")).when(userService).updateUser(any(Long.class), any(Optional.class), any(Optional.class), any(Optional.class));

        ResponseEntity<String> response = userController.updateUser(request, 1L, "Bearer Token");

        assertEquals(400, response.getStatusCode().value());
        assertEquals("Error", response.getBody());
    }
}