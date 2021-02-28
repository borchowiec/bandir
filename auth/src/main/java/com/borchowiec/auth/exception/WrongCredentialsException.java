package com.borchowiec.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class WrongCredentialsException extends RuntimeException {
    public WrongCredentialsException() {
        super("Wrong credentials");
    }

    public WrongCredentialsException(String message) {
        super(message);
    }

    public WrongCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }
}
