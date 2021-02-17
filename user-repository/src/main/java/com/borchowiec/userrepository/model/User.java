package com.borchowiec.userrepository.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class User {
    @Id
    private String id;

    @Indexed(unique=true)
    private String username;

    private String password;

    @Indexed(unique=true)
    private String email;
}
