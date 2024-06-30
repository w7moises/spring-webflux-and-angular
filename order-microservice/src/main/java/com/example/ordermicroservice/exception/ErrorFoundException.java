package com.example.ordermicroservice.exception;

public class ErrorFoundException extends RuntimeException {
    public ErrorFoundException(String message) {
        super(message);
    }
}