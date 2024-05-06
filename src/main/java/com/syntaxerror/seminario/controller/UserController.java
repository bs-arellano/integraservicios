package com.syntaxerror.seminario.controller;


import com.syntaxerror.seminario.service.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private final Authentication authenticationService;
    public UserController(Authentication authenticationService) {
        this.authenticationService = authenticationService;
    }

    // Singup endpoint
    @PostMapping("/signup")
    public ResponseEntity<String> SignUp(@RequestBody UserRegistrationRequest request){
        try {
            authenticationService.Signup(request.getUsername(), request.getEmail(), request.getPassword(), "usuario");
            return ResponseEntity.ok("User registered successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Login endpoint
    @PostMapping("/signin")
    public ResponseEntity<String> SignIn(@RequestBody UserLoginRequest request){
        try {
            String token = authenticationService.Signin(request.getUsername(), request.getPassword());
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public static class UserRegistrationRequest {
        private String username;
        private String password;
        private String email;
        // Getters
        public String getUsername() {
            return username;
        }
        public String getPassword() {
            return password;
        }
        public String getEmail() {
            return email;
        }
    }
    public static class UserLoginRequest {
        private String username;
        private String password;
        // Getters
        public String getUsername() {
            return username;
        }
        public String getPassword() {
            return password;
        }
    }
}
