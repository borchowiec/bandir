package com.borchowiec.auth.dto;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String usernameOrEmail;
    private String password;
}
