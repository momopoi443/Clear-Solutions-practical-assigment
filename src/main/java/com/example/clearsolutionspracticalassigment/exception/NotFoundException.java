package com.example.clearsolutionspracticalassigment.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }

    public static String name() {
        return "Not found exception";
    }
}
