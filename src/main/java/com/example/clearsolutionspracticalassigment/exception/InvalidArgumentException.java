package com.example.clearsolutionspracticalassigment.exception;

public class InvalidArgumentException extends RuntimeException {

    public InvalidArgumentException(String message) {
        super(message);
    }

    public static String name() {
        return "Invalid argument exception";
    }
}
