package com.syntaxerror.seminario.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRegistrationRequest {
    private String username;
    private String password;
    private String email;
}