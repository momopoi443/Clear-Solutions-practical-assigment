package com.example.clearsolutionspracticalassigment.controller;

import com.example.clearsolutionspracticalassigment.controller.payload.ApiErrorInfo;
import com.example.clearsolutionspracticalassigment.exception.InvalidArgumentException;

import com.example.clearsolutionspracticalassigment.exception.NotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class UserControllerAdvice {

    @ExceptionHandler(value = {
            MethodArgumentNotValidException.class
    })
    public ResponseEntity<ApiErrorInfo> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception
    ) {
        log.error(exception.getMessage(), exception);
        ApiErrorInfo errorInfo = ApiErrorInfo.builder()
                .title(InvalidArgumentException.name())
                .status(HttpStatus.BAD_REQUEST)
                .messages(MethodArgumentNotValidException.errorsToStringList(exception
                        .getBindingResult()
                        .getAllErrors())
                )
                .build();

        return new ResponseEntity<>(errorInfo, errorInfo.getStatus());
    }

    @ExceptionHandler(value = {
            ConstraintViolationException.class,
    })
    public ResponseEntity<ApiErrorInfo> handleConstraintViolationException(
            ConstraintViolationException exception
    ) {
        log.error(exception.getMessage(), exception);
        ApiErrorInfo errorInfo = ApiErrorInfo.builder()
                .title(InvalidArgumentException.name())
                .status(HttpStatus.BAD_REQUEST)
                .messages(List.of(exception.getMessage()))
                .build();

        return new ResponseEntity<>(errorInfo, errorInfo.getStatus());
    }

    @ExceptionHandler(value = {
            InvalidArgumentException.class,
    })
    public ResponseEntity<ApiErrorInfo> handleInvalidArgumentException(
            InvalidArgumentException exception
    ) {
        ApiErrorInfo errorInfo = ApiErrorInfo.builder()
                .title(InvalidArgumentException.name())
                .status(HttpStatus.BAD_REQUEST)
                .messages(List.of(exception.getMessage()))
                .build();

        return new ResponseEntity<>(errorInfo, errorInfo.getStatus());
    }

    @ExceptionHandler(value = {
            NotFoundException.class
    })
    public ResponseEntity<ApiErrorInfo> handleNotFoundException(
            NotFoundException exception
    ) {
        ApiErrorInfo errorInfo = ApiErrorInfo.builder()
                .title(NotFoundException.name())
                .status(HttpStatus.BAD_REQUEST)
                .messages(List.of(exception.getMessage()))
                .build();

        return new ResponseEntity<>(errorInfo, errorInfo.getStatus());
    }
}
