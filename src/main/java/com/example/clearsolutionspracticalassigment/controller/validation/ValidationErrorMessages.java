package com.example.clearsolutionspracticalassigment.controller.validation;

public class ValidationErrorMessages {

    public static final String NOT_NULL_EMAIL_MESSAGE = "Request must include email";
    public static final String INVALID_EMAIL_MESSAGE = "Given invalid email";

    public static final String NOT_BLANC_FIRST_NAME_MESSAGE = "Given invalid first name: it can't be blank";

    public static final String NOT_BLANC_LAST_NAME_MESSAGE = "Given invalid last name: it can't be blank";

    public static final String NOT_NULL_BIRTH_DATE_MESSAGE = "Request must include birth date";
    public static final String PAST_BIRTH_DATE_MESSAGE = "Given invalid birth date: date can't be present or future";

    public static final String INVALID_PHONE_NUMBER_MESSAGE = "Given invalid phone number";

    public static final String INVALID_UUID_MESSAGE = "Given invalid uuid";
}
