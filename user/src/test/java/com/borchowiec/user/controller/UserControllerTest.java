package com.borchowiec.user.controller;

import com.borchowiec.user.model.User;
import com.borchowiec.user.payload.CreateUserRequest;
import com.borchowiec.user.service.UserService;
import com.borchowiec.user.testuitl.DummyObjectsUtil;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@WebFluxTest(controllers = UserController.class)
class UserControllerTest {
    @Autowired
    private WebTestClient webClient;

    @MockBean
    private UserService userService;

    @Nested
    class addUser {
        void shouldReturnBadRequest(CreateUserRequest request) {
            webClient.post()
                    .uri("/user")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("user-ws-session-id", "ws")
                    .body(BodyInserters.fromValue(request))
                    .exchange()
                    .expectStatus().isBadRequest();

            verify(userService, times(0)).saveUser(any(CreateUserRequest.class), anyString());
        }

        @Test
        void wrongUsername_shouldReturnBadRequest() {
            // null
            CreateUserRequest request = DummyObjectsUtil.getCreateUserRequest();
            request.setUsername(null);
            shouldReturnBadRequest(request);

            // shorter than 4 chars
            request = DummyObjectsUtil.getCreateUserRequest();
            request.setUsername("");
            shouldReturnBadRequest(request);

            // longer than 30 chars
            request = DummyObjectsUtil.getCreateUserRequest();
            request.setUsername("1234512345123451234512345123451234512345");
            shouldReturnBadRequest(request);
        }

        @Test
        void wrongPassword_shouldReturnBadRequest() {
            // null
            CreateUserRequest request = DummyObjectsUtil.getCreateUserRequest();
            request.setPassword(null);
            shouldReturnBadRequest(request);

            // shorter than 8 chars
            request = DummyObjectsUtil.getCreateUserRequest();
            request.setPassword("");
            shouldReturnBadRequest(request);

            // longer than 50 chars
            request = DummyObjectsUtil.getCreateUserRequest();
            request.setPassword("12345123451234512345123451234512345123451234512345123451234512345123451234512345");
            shouldReturnBadRequest(request);
        }

        @Test
        void wrongConfirmedPassword_shouldReturnBadRequest() {
            // null
            CreateUserRequest request = DummyObjectsUtil.getCreateUserRequest();
            request.setConfirmedPassword(null);
            shouldReturnBadRequest(request);

            // shorter than 8 chars
            request = DummyObjectsUtil.getCreateUserRequest();
            request.setConfirmedPassword("");
            shouldReturnBadRequest(request);

            // longer than 50 chars
            request = DummyObjectsUtil.getCreateUserRequest();
            request.setConfirmedPassword("123451234512345123451234512345123451234512345123451234512345123451234512345");
            shouldReturnBadRequest(request);
        }

        @Test
        void wrongEmail_shouldReturnBadRequest() {
            // null
            CreateUserRequest request = DummyObjectsUtil.getCreateUserRequest();
            request.setEmail(null);
            shouldReturnBadRequest(request);

            // wrong format
            request = DummyObjectsUtil.getCreateUserRequest();
            request.setEmail("wrongformat");
            shouldReturnBadRequest(request);
        }

        @Test
        void passwordAndConfirmedPasswordAreNotMatching_shouldReturnBadRequest() {
            CreateUserRequest request = DummyObjectsUtil.getCreateUserRequest();
            request.setPassword("password1");
            request.setConfirmedPassword("password2");
            shouldReturnBadRequest(request);
        }

        @Test
        void properData_shouldReturn200() {
            CreateUserRequest request = DummyObjectsUtil.getCreateUserRequest();
            when(userService.saveUser(any(request.getClass()), anyString())).thenReturn(Mono.just(new User()));

            webClient.post()
                    .uri("/user")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("user-ws-session-id", "ws")
                    .body(BodyInserters.fromValue(request))
                    .exchange()
                    .expectStatus().isOk();

            verify(userService, times(1)).saveUser(any(CreateUserRequest.class), anyString());
        }
    }
}