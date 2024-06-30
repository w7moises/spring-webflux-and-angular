package com.example.productmicroservice.exception;

public class ErrorFoundException extends RuntimeException {
    public ErrorFoundException(String message) {
        super(message);
    }
}