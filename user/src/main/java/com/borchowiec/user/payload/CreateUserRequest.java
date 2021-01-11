package com.borchowiec.user.payload;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class CreateUserRequest {
//    @NotNull
//    @Size(min = 4, max = 30)
    private String username;

//    @NotNull
//    @Size(min = 8, max = 50)
    private String password;

//    @NotNull
//    @Size(min = 8, max = 50)
    private String confirmedPassword;

//    @NotNull
//    @Email
    private String email;
}
