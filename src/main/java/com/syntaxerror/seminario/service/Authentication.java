package com.syntaxerror.seminario.service;
import com.syntaxerror.seminario.repository.UsuarioRepository;
import com.syntaxerror.seminario.repository.UsuarioMongoRepository;
import com.syntaxerror.seminario.model.Usuario;
import com.syntaxerror.seminario.model.UsuarioMongo;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

@Service
public class Authentication {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMongoRepository usuarioMongoRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public Authentication(UsuarioRepository usuarioRepository, UsuarioMongoRepository usuarioMongoRepository, BCryptPasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMongoRepository = usuarioMongoRepository;
        this.passwordEncoder = passwordEncoder;
    }
    //Creates a new user with the given name, email, password and role
    public void Signup(String name, String email, String password, String rol) {
        //Checks if the email is valid
        if (!validateEmail(email)) {
            throw new RuntimeException("El correo no es valido");
        }
        //Checks if the email is already registered
        if (usuarioMongoRepository.findByEmail(email) != null) {
            throw new RuntimeException("El correo ya esta registrado");
        }
        //Checks if the password is valid
        if (!validatePassword(password)) {
            throw new RuntimeException("La contraseña debe tener al menos 8 caracteres, una mayuscula, una minuscula y un numero");
        }
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
    }
    //User login
    public String Signin(String email, String password) {
        //Checks if the email is valid
        if (!validateEmail(email)) {
            throw new RuntimeException("El correo no es valido");
        }
        //Checks if the email is registered
        UsuarioMongo usuarioMongo = usuarioMongoRepository.findByEmail(email);
        if (usuarioMongo == null) {
            throw new RuntimeException("El correo no esta registrado");
        }
        //Checks if the password is correct
        if (!passwordEncoder.matches(password, usuarioMongo.getPassword())) {
            throw new RuntimeException("La contraseña es incorrecta");
        }
        Long usuario_id = usuarioMongo.getUsuarioId();
        Optional<Usuario> optUsuario = usuarioRepository.findById(usuario_id);
        Usuario usuario = optUsuario.orElseThrow(()->new RuntimeException("Usuario no encontrado"));
        //Returns a JWT with the user id and role
        return JwtUtil.generateToken(usuario.getUsuarioId().toString(), usuario.getRol());
    }

    //Checks if the password has at least 8 characters, one uppercase letter, one lowercase letter and one number
    public boolean validatePassword(String password) {
        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$");
    }
    //Checks if the email has the correct format
    public boolean validateEmail(String email) {
        return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    }
}
