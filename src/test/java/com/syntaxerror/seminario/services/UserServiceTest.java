package com.syntaxerror.seminario.services;

import com.syntaxerror.seminario.model.Usuario;
import com.syntaxerror.seminario.model.UsuarioMongo;
import com.syntaxerror.seminario.repository.UsuarioMongoRepository;
import com.syntaxerror.seminario.repository.UsuarioRepository;
import com.syntaxerror.seminario.service.UserService;
import com.syntaxerror.seminario.service.ValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioMongoRepository usuarioMongoRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private ValidationService validationService;

    @Mock
    private TransactionTemplate transactionTemplate;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createUserSuccessfully() {
        when(usuarioMongoRepository.findByEmail(anyString())).thenReturn(null);
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(new Usuario());
        when(usuarioMongoRepository.save(any(UsuarioMongo.class))).thenReturn(new UsuarioMongo());
        when(transactionTemplate.execute(any(TransactionCallback.class))).thenAnswer(invocation -> {
            TransactionCallback<?> callback = invocation.getArgument(0);
            return callback.doInTransaction(null);
        });

        userService.createUser("name", "email", "password", "rol");

        verify(validationService, times(1)).validateEmail(anyString());
        verify(validationService, times(1)).validatePassword(anyString());
    }

    @Test
    public void createUserWithExistingEmail() {
        when(usuarioMongoRepository.findByEmail(anyString())).thenReturn(new UsuarioMongo());
        when(transactionTemplate.execute(any(TransactionCallback.class))).thenAnswer(invocation -> {
            TransactionCallback<?> callback = invocation.getArgument(0);
            return callback.doInTransaction(null);
        });
        assertThrows(RuntimeException.class, () -> userService.createUser("name", "email", "password", "rol"));
    }

    @Test
    public void updateUserSuccessfully() {
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.of(new Usuario()));
        when(usuarioMongoRepository.findById(anyString())).thenReturn(Optional.of(new UsuarioMongo()));

        userService.updateUser(1L, Optional.of("username"), Optional.of("email"), Optional.of("password"), Optional.of("rol"));

        verify(validationService, times(1)).validateEmail(anyString());
    }

    @Test
    public void updateUserWithNonExistingUser() {
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.updateUser(1L, Optional.of("username"), Optional.of("email"), Optional.of("password"), Optional.of("rol")));
    }

    @Test
    public void findUserByIdSuccessfully() {
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.of(new Usuario()));

        assertNotNull(userService.findUserById(1L));
    }

    @Test
    public void findUserByIdWithNonExistingUser() {
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.findUserById(1L));
    }

    @Test
    public void findUserByEmailSuccessfully() {
        when(usuarioMongoRepository.findByEmail(anyString())).thenReturn(new UsuarioMongo());

        assertNotNull(userService.findUserByEmail("email"));
    }

    @Test
    public void findUserByEmailWithNonExistingUser() {
        when(usuarioMongoRepository.findByEmail(anyString())).thenReturn(null);

        assertThrows(RuntimeException.class, () -> userService.findUserByEmail("email"));
    }
}