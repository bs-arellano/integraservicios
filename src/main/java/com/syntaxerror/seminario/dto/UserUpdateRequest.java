package com.syntaxerror.seminario.dto;

import java.util.Optional;

public class UserUpdateRequest {
    private Optional<String> username;
    private Optional<String> password;
    private Optional<String> email;

    // Getters
    public Optional<String> getUsername() {
        return username;
    }
    public Optional<String> getPassword() {
        return password;
    }
    public Optional<String> getEmail() {
        return email;
    }
}