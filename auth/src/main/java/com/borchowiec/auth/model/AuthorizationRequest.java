package com.borchowiec.auth.model;

import lombok.Data;

@Data
public class AuthorizationRequest {
    private String authToken;
    private String securityExpression;
}
