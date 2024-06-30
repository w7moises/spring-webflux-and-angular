package com.example.usermicroservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserFoundException.class)
    public Mono<ResponseEntity<ErrorDetails>> handleUserFoundException(UserFoundException ex, ServerWebExchange exchange) {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getMessage(), exchange.getRequest().getPath().toString(), "ENTITY_FOUND");
        return Mono.just(ResponseEntity.status(HttpStatus.FOUND).body(errorDetails));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ErrorDetails>> handleGlobalException(Exception ex, ServerWebExchange exchange) {
        String errorMessage = ex.getMessage();
        if (errorMessage != null && errorMessage.contains("Validation failed")) {
            List<String> requiredMessages = findRequiredMessages(errorMessage);
            if (!requiredMessages.isEmpty()) {
                errorMessage = String.join("; ", requiredMessages);
            }
        }
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), errorMessage, exchange.getRequest().getPath().toString(), "INTERNAL_SERVER_ERROR");
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDetails));
    }

    private static List<String> findRequiredMessages(String errorMessage) {
        List<String> requiredMessages = new ArrayList<>();
        Pattern pattern = Pattern.compile("default message \\[([^\\]]*required[^\\]]*)\\]");
        Matcher matcher = pattern.matcher(errorMessage);

        while (matcher.find()) {
            requiredMessages.add(matcher.group(1));
        }

        return requiredMessages;
    }
}
