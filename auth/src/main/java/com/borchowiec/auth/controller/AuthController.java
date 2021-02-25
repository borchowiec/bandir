package com.borchowiec.auth.controller;

import com.borchowiec.auth.dto.PasswordDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private final PasswordEncoder passwordEncoder;

    public AuthController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/hash-password")
    public PasswordDto hashPassword(@RequestBody PasswordDto passwordDto) {
        String encoded = passwordEncoder.encode(passwordDto.getPassword());
        PasswordDto result = new PasswordDto();
        result.setPassword(encoded);
        return result;
    }
}
