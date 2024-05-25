package com.syntaxerror.seminario.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserLoginResponse {
    private String token;
    private String rol;
    private String username;
    private Long serviceUnitId;
}
