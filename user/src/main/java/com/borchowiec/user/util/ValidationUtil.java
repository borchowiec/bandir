package com.borchowiec.user.util;

import com.borchowiec.user.client.NotificationClient;
import com.borchowiec.user.payload.CreateUserRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

@Component
public class ValidationUtil {
    private final Validator validator;
    private final NotificationClient notificationClient;

    public ValidationUtil(Validator validator, NotificationClient notificationClient) {
        this.validator = validator;
        this.notificationClient = notificationClient;
    }

    public void validate(CreateUserRequest request, String wsSession) {
        Set<ConstraintViolation<CreateUserRequest>> constraints = validator.validate(request);
        if (!constraints.isEmpty()) {
            constraints
                    .forEach(constraint -> notificationClient
                            .sendValidationErrorMessage(constraint, wsSession)
                            .subscribe());
            String message = constraints.stream().findFirst().get().getMessage();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }
    }
}
