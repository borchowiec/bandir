package com.borchowiec.user.service;

import com.borchowiec.user.exception.AlreadyTakenException;
import com.borchowiec.user.model.User;
import com.borchowiec.user.payload.CreateUserRequest;
import com.borchowiec.user.repository.UserRepository;
import com.borchowiec.user.testuitl.DummyObjectsUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ApplicationEventPublisher publisher;

    @BeforeEach
    public void initMocks(){
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    class createUser {
        @Test
        void usernameAlreadyTaken_shouldThrowAlreadyTakenException() {
            // given
            CreateUserRequest request = DummyObjectsUtil.getCreateUserRequest();

            // when
            when(userRepository.existsByUsername(anyString())).thenReturn(Mono.just(Boolean.TRUE));
            when(userRepository.existsByEmail(anyString())).thenReturn(Mono.just(Boolean.FALSE));
            when(userRepository.save(any(User.class))).thenReturn(Mono.just(new User()));

            // then
            assertThrows(AlreadyTakenException.class, () -> userService.saveUser(request, "ws").block());
        }

        @Test
        void emailAlreadyTaken_shouldThrowAlreadyTakenException() {
            // given
            CreateUserRequest request = DummyObjectsUtil.getCreateUserRequest();

            // when
            when(userRepository.existsByUsername(anyString())).thenReturn(Mono.just(false));
            when(userRepository.existsByEmail(anyString())).thenReturn(Mono.just(true));
            when(userRepository.save(any(User.class))).thenReturn(Mono.just(new User()));

            // then
            assertThrows(AlreadyTakenException.class, () -> userService.saveUser(request, "ws").block());
        }

        @Test
        void usernameAndEmailIsAvailable_shouldHashPasswordAndCallSaveMethod() {
            // given
            CreateUserRequest request = DummyObjectsUtil.getCreateUserRequest();
            User user = new User();
            user.setId(UUID.randomUUID().toString());
            user.setUsername(request.getUsername());
            user.setPassword("encoded");
            user.setEmail(request.getEmail());

            // when
            when(userRepository.existsByUsername(anyString())).thenReturn(Mono.just(false));
            when(userRepository.existsByEmail(anyString())).thenReturn(Mono.just(false));
            ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
            when(userRepository.save(userCaptor.capture())).thenReturn(Mono.just(user));
            when(passwordEncoder.encode(anyString())).thenReturn("encoded");
            User actual = userService.saveUser(request, "ws").block();

            // then
            assertEquals("encoded", userCaptor.getValue().getPassword());
            assertEquals(user, actual);
        }
    }
}