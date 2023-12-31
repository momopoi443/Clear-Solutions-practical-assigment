package com.example.clearsolutionspracticalassigment.controller.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.List;

@AllArgsConstructor
@Builder
@Setter
@Getter
public class ApiErrorInfo {

    private String title;

    private HttpStatus status;

    private List<String> messages;
}
