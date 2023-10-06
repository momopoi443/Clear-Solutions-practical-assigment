package com.example.clearsolutionspracticalassigment.controller.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

import static com.example.clearsolutionspracticalassigment.controller.validation.ValidationErrorMessages.*;
import static com.example.clearsolutionspracticalassigment.controller.validation.ValidationPatterns.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequestPayload {

    @NotNull(message = NOT_NULL_EMAIL_MESSAGE)
    @Email(
            regexp = EMAIL_PATTERN,
            message = INVALID_EMAIL_MESSAGE
    )
    private String email;

    @NotBlank(message = NOT_BLANC_FIRST_NAME_MESSAGE)
    private String firstName;

    @NotBlank(message = NOT_BLANC_LAST_NAME_MESSAGE)
    private String lastName;

    @NotNull(message = NOT_NULL_BIRTH_DATE_MESSAGE)
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
