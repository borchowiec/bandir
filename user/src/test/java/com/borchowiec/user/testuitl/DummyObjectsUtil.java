package com.borchowiec.user.testuitl;

import com.borchowiec.user.payload.CreateUserRequest;

public class DummyObjectsUtil {
    public static CreateUserRequest getCreateUserRequest() {
        CreateUserRequest request = new CreateUserRequest();
        request.setEmail("email@domain.com");
        request.setUsername("username");
        request.setPassword("password");
        request.setConfirmedPassword(request.getPassword());

        return request;
    }
}
