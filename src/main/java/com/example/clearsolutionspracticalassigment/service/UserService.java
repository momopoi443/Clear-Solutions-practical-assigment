package com.example.clearsolutionspracticalassigment.service;

import com.example.clearsolutionspracticalassigment.controller.payload.CreateUserRequestPayload;
import com.example.clearsolutionspracticalassigment.controller.payload.PatchUserRequestPayload;
import com.example.clearsolutionspracticalassigment.dto.UserDTO;
import com.example.clearsolutionspracticalassigment.controller.payload.UpdateUserRequestPayload;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface UserService {

    UserDTO createUser(CreateUserRequestPayload creationData);

    void updateUser(UUID userId, UpdateUserRequestPayload updatedData);

    void patchUser(UUID userId, PatchUserRequestPayload updatedData);

    void deleteUser(UUID userId);

    UserDTO getUserById(UUID userId);

    List<UserDTO> findAllUsersByBirthDateBetween(
            LocalDate birthDateFrom,
            LocalDate birthDateTo
    );
}
