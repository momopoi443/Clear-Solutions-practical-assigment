package com.example.clearsolutionspracticalassigment.controller;

import com.example.clearsolutionspracticalassigment.controller.payload.GetUserResponsePayload;
import com.example.clearsolutionspracticalassigment.controller.payload.CreateUserRequestPayload;
import com.example.clearsolutionspracticalassigment.controller.payload.UpdateUserRequestPayload;

import com.example.clearsolutionspracticalassigment.repository.UserRepository;
import com.example.clearsolutionspracticalassigment.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Past;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    public static final String USER_PATH = "v1/user";
    public static final String USER_PATH_ID = USER_PATH + "/{userId}";

    public final UserService userService;
    public final UserRepository repository;

    @PostMapping(USER_PATH)
    public ResponseEntity<Void> createUser(
            @RequestBody
            @Valid
            CreateUserRequestPayload creationData
    ) {
        GetUserResponsePayload createdUser = userService.createUser(creationData);

        URI createdUserLocation = URI.create(USER_PATH + "/" + createdUser.getUuid());

        return ResponseEntity.created(createdUserLocation).build();
    }

    @PutMapping(USER_PATH_ID)
    public ResponseEntity<Void> fullyUpdateUser(
            @PathVariable
            @UUID(message = "Given invalid uuid", allowNil = false)
            String userId,
            @RequestBody
            @Valid
            UpdateUserRequestPayload updatedData
    ) {
        userService.fullyUpdateUser(
                java.util.UUID.fromString(userId),
                updatedData
        );

        return ResponseEntity.noContent().build();
    }

    @PatchMapping(USER_PATH_ID)
    public ResponseEntity<Void> partiallyUpdateUser(
            @PathVariable
            @UUID(message = "Given invalid uuid", allowNil = false)
            String userId,
            @RequestBody
            @Valid
            UpdateUserRequestPayload updatedData
    ) {
        userService.partiallyUpdateUser(
                java.util.UUID.fromString(userId),
                updatedData
        );

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(USER_PATH_ID)
    public ResponseEntity<Void> deleteUser(
            @PathVariable
            @UUID(message = "Given invalid uuid", allowNil = false)
            String userId
    ) {
        userService.deleteUser(
                java.util.UUID.fromString(userId)
        );

        return ResponseEntity.noContent().build();
    }

    @GetMapping(USER_PATH_ID)
    public ResponseEntity<GetUserResponsePayload> getUserById(
            @PathVariable
            @UUID(message = "Given invalid uuid", allowNil = false)
            String userId
    ) {
        GetUserResponsePayload userResponsePayload = userService.getUserById(
                java.util.UUID.fromString(userId)
        );

        return ResponseEntity.ok(userResponsePayload);
    }

    @GetMapping(USER_PATH)
    public ResponseEntity<List<GetUserResponsePayload>> listUsers(
            @RequestParam
            @Past(message = "Invalid left birth date: it can't be present of future")
            LocalDate birthDateFrom,
            @RequestParam
            @Past(message = "Invalid right birth date: it can't be present of future")
            LocalDate birthDateTo
    ) {
        List<GetUserResponsePayload> foundUsers = userService
                .findAllUsersByBirthDateBetween(
                        birthDateFrom, birthDateTo
                );

        return ResponseEntity.ok(foundUsers);
    }
}
