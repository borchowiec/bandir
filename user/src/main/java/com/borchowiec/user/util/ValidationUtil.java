package com.borchowiec.user.util;

import com.borchowiec.remote.client.NotificationClient;
import com.borchowiec.remote.model.WsMessage;
import com.borchowiec.user.payload.CreateUserRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

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


    public Mono<ResponseEntity<Void>> sendValidationErrorMessage(ConstraintViolation<CreateUserRequest> constraint,
                                                                 String wsSession) {
        String message = String.format("%s: %s", constraint.getPropertyPath(), constraint.getMessage());
        WsMessage wsMessage = new WsMessage(WsMessage.MessageType.ERROR_MESSAGE, message);

        return notificationClient.sendMessage(wsMessage, wsSession);
    }

    public void validate(CreateUserRequest request, String wsSession) {
        Set<ConstraintViolation<CreateUserRequest>> constraints = validator.validate(request);
        if (!constraints.isEmpty()) {
            constraints.forEach(constraint -> sendValidationErrorMessage(constraint, wsSession).subscribe());
            String message = constraints.stream().findFirst().get().getMessage();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }
    }
}
