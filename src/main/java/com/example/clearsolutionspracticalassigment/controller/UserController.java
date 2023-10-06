package com.example.clearsolutionspracticalassigment.controller;

import com.example.clearsolutionspracticalassigment.controller.payload.PatchUserRequestPayload;
import com.example.clearsolutionspracticalassigment.dto.UserDTO;
import com.example.clearsolutionspracticalassigment.controller.payload.CreateUserRequestPayload;
import com.example.clearsolutionspracticalassigment.controller.payload.UpdateUserRequestPayload;

import com.example.clearsolutionspracticalassigment.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Past;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static com.example.clearsolutionspracticalassigment.controller.validation.ValidationErrorMessages.*;

@RestController
@RequiredArgsConstructor
@Validated
public class UserController {

    public static final String USER_PATH = "v1/user";
    public static final String USER_PATH_ID = USER_PATH + "/{userId}";

    public final UserService userService;

    @PostMapping(USER_PATH)
    public ResponseEntity<Void> createUser(
            @RequestBody
            @Valid
            CreateUserRequestPayload creationData
    ) {
        UserDTO createdUser = userService.createUser(creationData);

        URI createdUserLocation = URI.create(USER_PATH + "/" + createdUser.getUuid());

        return ResponseEntity.created(createdUserLocation).build();
    }

    @PutMapping(USER_PATH_ID)
    public ResponseEntity<Void> updateUser(
            @PathVariable
            @UUID(message = INVALID_UUID_MESSAGE, allowNil = false)
            String userId,
            @RequestBody
            @Valid
            UpdateUserRequestPayload updatedData
    ) {
        userService.updateUser(
                java.util.UUID.fromString(userId),
                updatedData
        );

        return ResponseEntity.noContent().build();
    }

    @PatchMapping(USER_PATH_ID)
    public ResponseEntity<Void> patchUser(
            @PathVariable
            @UUID(message = INVALID_UUID_MESSAGE, allowNil = false)
            String userId,
            @RequestBody
            @Valid
            PatchUserRequestPayload updatedData
    ) {
        userService.patchUser(
                java.util.UUID.fromString(userId),
                updatedData
        );

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(USER_PATH_ID)
    public ResponseEntity<Void> deleteUser(
            @PathVariable
            @UUID(message = INVALID_UUID_MESSAGE, allowNil = false)
            String userId
    ) {
        userService.deleteUser(
                java.util.UUID.fromString(userId)
        );

        return ResponseEntity.noContent().build();
    }

    @GetMapping(USER_PATH_ID)
    public ResponseEntity<UserDTO> getUserById(
            @PathVariable
            @UUID(message = INVALID_UUID_MESSAGE, allowNil = false)
            String userId
    ) {
        UserDTO userResponsePayload = userService.getUserById(
                java.util.UUID.fromString(userId)
        );

        return ResponseEntity.ok(userResponsePayload);
    }

    @GetMapping(USER_PATH)
    public ResponseEntity<List<UserDTO>> listUsers(
            @RequestParam
            @Past(message = PAST_BIRTH_DATE_MESSAGE)
            LocalDate birthDateFrom,
            @RequestParam
            @Past(message = PAST_BIRTH_DATE_MESSAGE)
            LocalDate birthDateTo
    ) {
        List<UserDTO> foundUsers = userService
                .findAllUsersByBirthDateBetween(
                        birthDateFrom, birthDateTo
                );

        return ResponseEntity.ok(foundUsers);
    }
}
