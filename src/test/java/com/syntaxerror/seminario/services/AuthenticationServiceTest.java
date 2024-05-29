package com.syntaxerror.seminario.services;

import com.syntaxerror.seminario.model.Usuario;
import com.syntaxerror.seminario.model.UsuarioMongo;
import com.syntaxerror.seminario.service.AuthenticationService;
import com.syntaxerror.seminario.service.JwtUtil;
import com.syntaxerror.seminario.service.UserService;
import com.syntaxerror.seminario.service.ValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Map;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthenticationServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private ValidationService validationService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void signInSuccessfully() {
        doNothing().when(validationService).validateEmail(anyString());
        UsuarioMongo usuarioMongo = UsuarioMongo.builder().email("ud@gmail.com")
                .usuario_id(1245L).password("password").build();
        Usuario usuario = Usuario.builder().usuarioId(1245L).rol("admin").build();

        when(userService.findUserByEmail("ud@gmail.com")).thenReturn(usuarioMongo);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(userService.findUserById(1245L)).thenReturn(usuario);
        when(jwtUtil.generateToken(anyString(), anyString())).thenReturn("mockToken");

        // Call the method and assert the result
        Usuario result = authenticationService.signIn("ud@gmail.com", "password");
        assertEquals(usuario.getUsuarioId(), result.getUsuarioId());
    }

    @Test
    public void signInWithInvalidEmail() {
        doThrow(new RuntimeException("Invalid email")).when(validationService).validateEmail(anyString());

        assertThrows(RuntimeException.class, () -> authenticationService.signIn("invalidEmail", "password"));
    }

    @Test
    public void signInWithNonExistingEmail() {
        doNothing().when(validationService).validateEmail(anyString());
        when(userService.findUserByEmail(anyString())).thenReturn(null);

        assertThrows(RuntimeException.class, () -> authenticationService.signIn("nonExistingEmail", "password"));
    }

    @Test
    public void signInWithIncorrectPassword() {
        doNothing().when(validationService).validateEmail(anyString());
        when(userService.findUserByEmail(anyString())).thenReturn(new UsuarioMongo());
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        assertThrows(RuntimeException.class, () -> authenticationService.signIn("email", "incorrectPassword"));
    }

    @Test
    public void validateRequestSuccessfully() {
        Map<String, String> decodedToken = new HashMap<>();
        decodedToken.put("rol", "admin");
        decodedToken.put("id", "1");

        when(jwtUtil.decodeToken(anyString())).thenReturn(decodedToken);

        assertTrue(authenticationService.validateRequest("jwt", 1L));
    }

    @Test
    public void validateRequestWithNonAdminRoleAndDifferentId() {
        Map<String, String> decodedToken = new HashMap<>();
        decodedToken.put("rol", "user");
        decodedToken.put("id", "2");

        when(jwtUtil.decodeToken(anyString())).thenReturn(decodedToken);

        assertFalse(authenticationService.validateRequest("jwt", 1L));
    }
}