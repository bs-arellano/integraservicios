package com.syntaxerror.seminario.controller;

import com.syntaxerror.seminario.dto.UserLoginRequest;
import com.syntaxerror.seminario.dto.UserUpdateRequest;
import com.syntaxerror.seminario.service.AuthenticationService;
import com.syntaxerror.seminario.dto.UserRegistrationRequest;
import com.syntaxerror.seminario.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    private final AuthenticationService authenticationService;
    private final UserService userService;

    public UserController(AuthenticationService authenticationService, UserService userService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    // Singup endpoint
    @PostMapping("/signup")
    public ResponseEntity<String> SignUp(@RequestBody UserRegistrationRequest request){
        try {
            userService.createUser(request.getUsername(), request.getEmail(), request.getPassword(), "usuario");
            return ResponseEntity.ok("User registered successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Login endpoint
    @PostMapping("/signin")
    public ResponseEntity<String> SignIn(@RequestBody UserLoginRequest request){
        try {
            String token = authenticationService.signIn(request.getUsername(), request.getPassword());
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
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
            userService.updateUser(id, request.getUsername(), request.getEmail(), request.getPassword());
            return ResponseEntity.ok("User updated successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}