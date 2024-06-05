package com.syntaxerror.seminario.controller;

import com.syntaxerror.seminario.dto.UserLoginRequest;
import com.syntaxerror.seminario.dto.UserLoginResponse;
import com.syntaxerror.seminario.dto.UserUpdateRequest;
import com.syntaxerror.seminario.model.Empleado;
import com.syntaxerror.seminario.model.Usuario;
import com.syntaxerror.seminario.service.AuthenticationService;
import com.syntaxerror.seminario.dto.UserRegistrationRequest;
import com.syntaxerror.seminario.service.JwtUtil;
import com.syntaxerror.seminario.service.ServiceUnitManager;
import com.syntaxerror.seminario.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final ServiceUnitManager serviceUnitManager;

    public UserController(AuthenticationService authenticationService, UserService userService, JwtUtil jwtUtil, ServiceUnitManager serviceUnitManager) {
        this.authenticationService = authenticationService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.serviceUnitManager = serviceUnitManager;
    }

    // Singup endpoint
    @PostMapping("/signup")
    public ResponseEntity<String> SignUp(@RequestBody UserRegistrationRequest request){
        try {
            userService.createUser(request.getUsername(), request.getEmail(), request.getPassword(), "usuario");
            return ResponseEntity.ok("User registered successfully!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Login endpoint
    @PostMapping("/signin")
    public ResponseEntity<UserLoginResponse> SignIn(@RequestBody UserLoginRequest request){
        try {
            Usuario user = authenticationService.signIn(request.getUsername(), request.getPassword());
            Empleado employee = serviceUnitManager.getCurrentEmployer(user.getUsuarioId());
            String token = jwtUtil.generateToken(user.getUsuarioId().toString(), user.getRol());
            UserLoginResponse.UserLoginResponseBuilder builder = UserLoginResponse.builder()
                    .token(token)
                    .rol(user.getRol())
                    .username(user.getNombre());
            if(employee != null) {
                builder.serviceUnitId(employee.getUnidadId());
            }
            return ResponseEntity.ok(builder.build());
            //rol {user-admin}, username, service unit: id
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    //Update user endpoint
    @PutMapping("/users/{id}")
    public ResponseEntity<String> updateUser(@RequestBody UserUpdateRequest request, @PathVariable("id") Long id, @RequestHeader("Authorization") String authHeader){
        try {
            String jwt = authHeader.replace("Bearer ", "");
            //Validates request
            if(!authenticationService.validateRequest(jwt, id)){
                return ResponseEntity.badRequest().body("Usuario no autorizado para realizar esta acción");
            }
            userService.updateUser(id, request.getUsername(), request.getEmail(), request.getPassword(), request.getRol());
            return ResponseEntity.ok("User updated successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //Get all users
    @GetMapping("/users")
    public ResponseEntity<List<Usuario>> getAllUsers(){
        try {
            return ResponseEntity.ok(userService.getAllUsers());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    //Get user by id
    @GetMapping("/users/{id}")
    public ResponseEntity<Usuario> getUserById(@PathVariable("id") Long id){
        try {
            return ResponseEntity.ok(userService.findUserById(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}