package com.example.clearsolutionspracticalassigment.controller.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
/*
That class is almost similar to update ones,
but I consider write it due to:
 1) better readability
 2) single responsibility principle
*/
public class CreateUserRequestPayload {

    @Email(regexp = "${validation.email.regex}", message = "Given invalid email")
    private String email;

    @NotBlank(message = "Given invalid first name: it can't be blank")
    private String firstName;

    @NotBlank(message = "Given invalid last name: it can't be blank")
    private String lastName;

    @Past(message = "Given invalid birth date: date can't be present or future")
    private LocalDate birthDate;

    private String address;

    private String phoneNumber;
}