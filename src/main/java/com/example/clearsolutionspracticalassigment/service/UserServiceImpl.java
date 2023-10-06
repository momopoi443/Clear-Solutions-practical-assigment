package com.example.clearsolutionspracticalassigment.service;

import com.example.clearsolutionspracticalassigment.controller.payload.PatchUserRequestPayload;
import com.example.clearsolutionspracticalassigment.exception.InvalidArgumentException;
import com.example.clearsolutionspracticalassigment.controller.payload.CreateUserRequestPayload;
import com.example.clearsolutionspracticalassigment.dto.UserDTO;
import com.example.clearsolutionspracticalassigment.controller.payload.UpdateUserRequestPayload;
import com.example.clearsolutionspracticalassigment.entity.User;
import com.example.clearsolutionspracticalassigment.exception.NotFoundException;
import com.example.clearsolutionspracticalassigment.mapper.UserMapper;
import com.example.clearsolutionspracticalassigment.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {

    @Value("${validation.user.minimalAge}")
    private Long minimalAge;

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDTO createUser(CreateUserRequestPayload creationData) {
        validateBirthDate(creationData.getBirthDate());
        validateUniquenessOfEmail(creationData.getEmail(), null);

        User user = new User(UUID.randomUUID());
        user.setEmail(creationData.getEmail());
        user.setFirstName(creationData.getFirstName());
        user.setLastName(creationData.getLastName());
        user.setBirthDate(creationData.getBirthDate());
        user.setAddress(creationData.getAddress());
        user.setPhoneNumber(creationData.getPhoneNumber());

        User savedUser = userRepository.save(user);
        log.info("Created user: " + savedUser.getUuid());

        return userMapper.mapUserToUserDTO(savedUser);
    }

    @Override
    public void updateUser(
            UUID userId,
            UpdateUserRequestPayload updatedData
    ) {
        validateBirthDate(updatedData.getBirthDate());
        validateUniquenessOfEmail(updatedData.getEmail(), userId);

        userRepository.findByUuid(userId).ifPresentOrElse(
                foundUser -> {
                    foundUser.setEmail(updatedData.getEmail());
                    foundUser.setFirstName(updatedData.getFirstName());
                    foundUser.setLastName(updatedData.getLastName());
                    foundUser.setBirthDate(updatedData.getBirthDate());
                    foundUser.setAddress(updatedData.getAddress());
                    foundUser.setPhoneNumber(updatedData.getPhoneNumber());

                    userRepository.save(foundUser);
                    log.info("Updated user: " + userId);
                },
                this::logAndThrowNotFoundException
        );
    }

    @Override
    public void patchUser(
            UUID userId,
            PatchUserRequestPayload updatedData
    ) {
        if (updatedData.getBirthDate() != null) {
            validateBirthDate(updatedData.getBirthDate());
        }
        if (updatedData.getEmail() != null) {
            validateUniquenessOfEmail(updatedData.getEmail(), userId);
        }

        userRepository.findByUuid(userId).ifPresentOrElse(
                foundUser -> {
                    if (updatedData.getEmail() != null) {
                        foundUser.setEmail(updatedData.getEmail());
                    }
                    if (updatedData.getFirstName() != null) {
                        foundUser.setFirstName(updatedData.getFirstName());
                    }
                    if (updatedData.getLastName() != null) {
                        foundUser.setLastName(updatedData.getLastName());
                    }
                    if (updatedData.getBirthDate() != null) {
                        foundUser.setBirthDate(updatedData.getBirthDate());
                    }
                    if (updatedData.getAddress() != null) {
                        foundUser.setAddress(updatedData.getAddress());
                    }
                    if (updatedData.getPhoneNumber() != null) {
                        foundUser.setPhoneNumber(updatedData.getPhoneNumber());
                    }

                    userRepository.save(foundUser);
                    log.info("Patched user: " + userId);
                },
                this::logAndThrowNotFoundException
        );
    }

    @Override
    public void deleteUser(UUID userId) {
        if (!userRepository.existsByUuid(userId)) {
            logAndThrowNotFoundException();
        }

        userRepository.deleteByUuid(userId);
        log.info("Deleted user: " + userId);
    }

    @Override
    public UserDTO getUserById(UUID userId) {
        AtomicReference<UserDTO> userDTO = new AtomicReference<>();

        userRepository.findByUuid(userId).ifPresentOrElse(
                foundUser -> userDTO.set(userMapper.mapUserToUserDTO(foundUser)),
                this::logAndThrowNotFoundException
        );

        return userDTO.get();
    }

    @Override
    public List<UserDTO> findAllUsersByBirthDateBetween(
            LocalDate birthDateFrom,
            LocalDate birthDateTo
    ) {
        if (birthDateFrom.isAfter(birthDateTo)) {
            throw new InvalidArgumentException(
                    "Invalid birth date range: birthDateFrom can't be newer than birthDateTo"
            );
        }

        return userRepository
                .findAllByBirthDateBetween(birthDateFrom, birthDateTo)
                .stream()
                .map(userMapper::mapUserToUserDTO)
                .toList();
    }

    private void validateBirthDate(LocalDate birthDate) {
        LocalDate minimalAcceptedBirthDate = LocalDate.now().minusYears(minimalAge);

        if (birthDate.isAfter(minimalAcceptedBirthDate)) {
            var error = new InvalidArgumentException(
                    "Invalid birth date: user must be at least " + minimalAge + " years old"
            );
            log.error(error.getMessage(), error);
            throw error;
        }
    }

    private void validateUniquenessOfEmail(
            String email,
            UUID userUuid
    ) {
        userRepository.findByEmail(email).ifPresent(
                foundUser -> {
                    if (!foundUser.getUuid().equals(userUuid)) {
                        var error = new InvalidArgumentException(
                                "Invalid email: given email is already taken"
                        );
                        log.error(error.getMessage(), error);
                        throw error;
                    }
                }
        );
    }

    private void logAndThrowNotFoundException() {
        var error = new NotFoundException("Given invalid user id: no users with such id");
        log.error(error.getMessage(), error);
        throw error;
    }
}
