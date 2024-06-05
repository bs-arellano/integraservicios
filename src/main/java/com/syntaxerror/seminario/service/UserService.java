package com.syntaxerror.seminario.service;

import com.syntaxerror.seminario.model.Usuario;
import com.syntaxerror.seminario.model.UsuarioMongo;
import com.syntaxerror.seminario.repository.UsuarioMongoRepository;
import com.syntaxerror.seminario.repository.UsuarioRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMongoRepository usuarioMongoRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ValidationService validationService;
    private final TransactionTemplate transactionTemplate;


    public UserService(UsuarioRepository usuarioRepository, UsuarioMongoRepository usuarioMongoRepository, BCryptPasswordEncoder passwordEncoder, ValidationService validationService, TransactionTemplate transactionTemplate) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMongoRepository = usuarioMongoRepository;
        this.passwordEncoder = passwordEncoder;
        this.validationService = validationService;
        this.transactionTemplate = transactionTemplate;
    }

    @Transactional
    public void createUser(String name, String email, String password, String rol) {
        transactionTemplate.execute(status -> {
            try {
                //Checks if the email is valid
                validationService.validateEmail(email);

                //Checks if the email is already registered
                if (usuarioMongoRepository.findByEmail(email) != null) {
                    throw new RuntimeException("El correo ya esta registrado");
                }
                //Checks if the password is valid
                validationService.validatePassword(password);
                //Creates a new user on the relational database
                Usuario usuario = new Usuario();
                usuario.setNombre(name);
                usuario.setRol(rol);
                usuarioRepository.save(usuario);

                //Stores the user's email and password on the non-relational database
                String hashedPassword = passwordEncoder.encode(password);
                UsuarioMongo usuarioMongo = new UsuarioMongo();
                usuarioMongo.setEmail(email);
                usuarioMongo.setPassword(hashedPassword);
                usuarioMongo.setUsuarioId(usuario.getUsuarioId());
                usuarioMongoRepository.save(usuarioMongo);
                return null;
            } catch (Exception e) {
                status.setRollbackOnly();
                throw new RuntimeException("Error al crear el usuario");
            }
        });
    }

    public void updateUser(Long userID, Optional<String> username, Optional<String> email, Optional<String> password, Optional<String> rol) {
        // Retrieve the user
        Usuario usuario = usuarioRepository.findById(userID)
                .orElseThrow(() -> new RuntimeException("User not found"));
        // Update username if present
        username.ifPresent(usuario::setNombre);

        // Update email if present and valid
        email.ifPresent(e -> {
            validationService.validateEmail(e);
            UsuarioMongo usuarioMongo = usuarioMongoRepository.findByUsuarioId(userID)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            usuarioMongo.setEmail(e);
            usuarioMongoRepository.save(usuarioMongo);
        });
        // Update password if present and valid
        password.ifPresent(p -> {
            validationService.validatePassword(p);
            UsuarioMongo usuarioMongo = usuarioMongoRepository.findByUsuarioId(userID)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            String hashedPassword = passwordEncoder.encode(p);
            usuarioMongo.setPassword(hashedPassword);
            usuarioMongoRepository.save(usuarioMongo);
        });
        // Update rol if present
        rol.ifPresent(usuario::setRol);

        // Save the updated user
        usuarioRepository.save(usuario);
    }

    public Usuario findUserById(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public UsuarioMongo findUserByEmail(String email) {
        UsuarioMongo usuarioMongo = usuarioMongoRepository.findByEmail(email);
        if (usuarioMongo == null) {
            throw new RuntimeException("El correo no esta registrado");
        }
        return usuarioMongo;
    }

    public List<Usuario> getAllUsers() {
        return usuarioRepository.findAll();
    }
}