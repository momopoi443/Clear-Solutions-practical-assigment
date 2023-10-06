package com.example.clearsolutionspracticalassigment.controller.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;

import static com.example.clearsolutionspracticalassigment.controller.validation.ValidationErrorMessages.*;
import static com.example.clearsolutionspracticalassigment.controller.validation.ValidationPatterns.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatchUserRequestPayload {

    @Email(
            regexp = EMAIL_PATTERN,
            message = INVALID_EMAIL_MESSAGE
    )    private String email;

    private String firstName;

    private String lastName;

    @Past(message = PAST_BIRTH_DATE_MESSAGE)
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd"
    )
    private LocalDate birthDate;

    private String address;

    @Pattern(
            regexp = PHONE_NUMBER_PATTERN,
            message = INVALID_PHONE_NUMBER_MESSAGE
    )
    private String phoneNumber;
}
