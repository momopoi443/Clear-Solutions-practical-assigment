package com.example.clearsolutionspracticalassigment.service;

import com.example.clearsolutionspracticalassigment.controller.payload.CreateUserRequestPayload;
import com.example.clearsolutionspracticalassigment.controller.payload.PatchUserRequestPayload;
import com.example.clearsolutionspracticalassigment.controller.payload.UpdateUserRequestPayload;
import com.example.clearsolutionspracticalassigment.dto.UserDTO;
import com.example.clearsolutionspracticalassigment.entity.User;
import com.example.clearsolutionspracticalassigment.exception.InvalidArgumentException;
import com.example.clearsolutionspracticalassigment.exception.NotFoundException;
import com.example.clearsolutionspracticalassigment.mapper.UserMapperImpl;
import com.example.clearsolutionspracticalassigment.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    private final Long userMinimalAge = 18L;

    @Mock
    private UserRepository userRepository;

    @Spy
    private UserMapperImpl userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(userService, "minimalAge", userMinimalAge);
    }

    @Test
    public void createUser_CreatesUser_And_ReturnsUserDTO() {
        CreateUserRequestPayload creationPayload = CreateUserRequestPayload.builder()
                .email("test@gmail.com")
                .firstName("John")
                .lastName("Dou")
                .birthDate(LocalDate.of(2000, 7, 15))
                .build();
        User user = User.builder()
                .uuid(UUID.randomUUID())
                .email(creationPayload.getEmail())
                .firstName(creationPayload.getFirstName())
                .lastName(creationPayload.getLastName())
                .birthDate(creationPayload.getBirthDate())
                .build();

        when(userRepository.findByEmail(any()))
                .thenReturn(Optional.empty());
        when(userRepository.save(any()))
                .thenReturn(user);

        UserDTO savedUser = userService.createUser(creationPayload);

        verify(userRepository, times(1)).save(any());
        assertNotNull(savedUser);
        assertNotNull(savedUser.getUuid());
    }

    @Test
    public void createUser_ValidatesBirthDate() {
        CreateUserRequestPayload creationPayload = CreateUserRequestPayload.builder()
                .birthDate(LocalDate.now())
                .build();

        assertThrows(
                InvalidArgumentException.class,
                () -> userService.createUser(creationPayload)
        );
    }

    @Test
    public void createUser_ValidatesEmailUniqueness() {
        CreateUserRequestPayload creationPayload = CreateUserRequestPayload.builder()
                .email("test@gmail.com")
                .birthDate(LocalDate.of(2000, 7, 15))
                .build();

        when(userRepository.findByEmail(any()))
                .thenReturn(
                        Optional.of(new User(UUID.randomUUID()))
                );

        assertThrows(
                InvalidArgumentException.class,
                () -> userService.createUser(creationPayload)
        );
    }

    @Test
    public void updateUser_ValidatesBirthDate() {
        UUID userUuid = UUID.randomUUID();
        UpdateUserRequestPayload updatePayload = UpdateUserRequestPayload.builder()
                .birthDate(LocalDate.now())
                .build();

        assertThrows(
                InvalidArgumentException.class,
                () -> userService.updateUser(userUuid, updatePayload)
        );
    }

    @Test
    public void updateUser_ValidatesEmailUniqueness() {
        UUID userUuid = UUID.randomUUID();
        UpdateUserRequestPayload updatePayload = UpdateUserRequestPayload.builder()
                .email("test@gmail.com")
                .birthDate(LocalDate.of(2000, 7, 15))
                .build();

        when(userRepository.findByEmail(any()))
                .thenReturn(
                        Optional.of(new User(UUID.randomUUID()))
                );

        assertThrows(
                InvalidArgumentException.class,
                () -> userService.updateUser(userUuid, updatePayload)
        );
    }

    @Test
    public void updateUser_ThrowsNotFoundException() {
        UUID userUuid = UUID.randomUUID();
        UpdateUserRequestPayload updatePayload = UpdateUserRequestPayload.builder()
                .birthDate(LocalDate.of(2000, 7, 15))
                .build();

        when(userRepository.findByEmail(any()))
                .thenReturn(Optional.empty());
        when(userRepository.findByUuid(any()))
                .thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.class,
                () -> userService.updateUser(userUuid, updatePayload)
        );
    }

    @Test
    public void updateUser_UpdatesUser() {
        UUID userUuid = UUID.randomUUID();
        UpdateUserRequestPayload updatePayload = UpdateUserRequestPayload.builder()
                .email("newemail@gmail.com")
                .firstName("New one")
                .lastName("New one")
                .birthDate(LocalDate.of(2000, 7, 15))
                .address("New address")
                .phoneNumber("+380000000000")
                .build();
        User foundUser = new User();

        when(userRepository.findByEmail(any()))
                .thenReturn(Optional.empty());
        when(userRepository.findByUuid(any()))
                .thenReturn(Optional.of(foundUser));

        userService.updateUser(userUuid, updatePayload);

        verify(userRepository, times(1))
                .findByUuid(userUuid);
        verify(userRepository, times(1))
                .save(foundUser);
        assertEquals(updatePayload.getEmail(), foundUser.getEmail());
        assertEquals(updatePayload.getFirstName(), foundUser.getFirstName());
        assertEquals(updatePayload.getLastName(), foundUser.getLastName());
        assertEquals(updatePayload.getBirthDate(), foundUser.getBirthDate());
        assertEquals(updatePayload.getAddress(), foundUser.getAddress());
        assertEquals(updatePayload.getPhoneNumber(), foundUser.getPhoneNumber());
    }

    @Test
    public void patchUser_ValidatesBirthDate() {
        UUID userUuid = UUID.randomUUID();
        PatchUserRequestPayload patchPayload = PatchUserRequestPayload.builder()
                .birthDate(LocalDate.now())
                .build();

        assertThrows(
                InvalidArgumentException.class,
                () -> userService.patchUser(userUuid, patchPayload)
        );
    }

    @Test
    public void patchUser_ValidatesEmailUniqueness() {
        UUID userUuid = UUID.randomUUID();
        PatchUserRequestPayload patchPayload = PatchUserRequestPayload.builder()
                .email("test@gmail.com")
                .birthDate(LocalDate.of(2000, 7, 15))
                .build();

        when(userRepository.findByEmail(any()))
                .thenReturn(
                        Optional.of(new User(UUID.randomUUID()))
                );

        assertThrows(
                InvalidArgumentException.class,
                () -> userService.patchUser(userUuid, patchPayload)
        );
    }

    @Test
    public void patchUser_ThrowsNotFoundException() {
        UUID userUuid = UUID.randomUUID();
        PatchUserRequestPayload patchPayload = PatchUserRequestPayload.builder()
                .birthDate(LocalDate.of(2000, 7, 15))
                .build();

        when(userRepository.findByUuid(any()))
                .thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.class,
                () -> userService.patchUser(userUuid, patchPayload)
        );
    }

    @Test
    public void patchUser_PatchesUser() {
        UUID userUuid = UUID.randomUUID();
        PatchUserRequestPayload patchPayload = PatchUserRequestPayload.builder()
                .firstName(null)
                .lastName("new one")
                .build();
        String initialFirstName = "John";
        User foundUser = User.builder()
                .firstName(initialFirstName)
                .lastName("Some")
                .build();

        when(userRepository.findByUuid(any()))
                .thenReturn(Optional.of(foundUser));

        userService.patchUser(userUuid, patchPayload);

        verify(userRepository, times(1))
                .findByUuid(userUuid);
        verify(userRepository, times(1))
                .save(foundUser);
        assertEquals(initialFirstName, foundUser.getFirstName());
        assertEquals(patchPayload.getLastName(), foundUser.getLastName());
    }

    @Test
    public void deleteUser_ThrowsNotFoundException() {
        UUID userUuid = UUID.randomUUID();

        when(userRepository.existsByUuid(any()))
                .thenReturn(false);

        assertThrows(
                NotFoundException.class,
                () -> userService.deleteUser(userUuid)
        );
    }

    @Test
    public void deleteUser_DeletesUser() {
        UUID userUuid = UUID.randomUUID();

        when(userRepository.existsByUuid(any()))
                .thenReturn(true);

        userService.deleteUser(userUuid);

        verify(userRepository, times(1))
                .deleteByUuid(userUuid);
    }

    @Test
    public void getUserById_ThrowsNotFoundException() {
        UUID userUuid = UUID.randomUUID();

        when(userRepository.findByUuid(any()))
                .thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.class,
                () -> userService.getUserById(userUuid)
        );
    }

    @Test
    public void getUserById_ReturnsUserDTO() {
        UUID userUuid = UUID.randomUUID();
        User user = User.builder()
                .uuid(userUuid)
                .build();

        when(userRepository.findByUuid(any()))
                .thenReturn(Optional.of(user));

        UserDTO fetchedUserDTO = userService.getUserById(userUuid);

        verify(userRepository, times(1))
                .findByUuid(userUuid);
        assertNotNull(fetchedUserDTO);
        assertEquals(user.getUuid(), fetchedUserDTO.getUuid());
    }

    @Test
    public void findAllUsersByBirthDateBetween_ThrowsNotFoundException() {
        LocalDate birthDateFrom = LocalDate.now();
        LocalDate birthDateTo = LocalDate.now().minusYears(2);

        assertThrows(
                InvalidArgumentException.class,
                () -> userService.findAllUsersByBirthDateBetween(
                    birthDateFrom,
                    birthDateTo
                )
        );
    }

    @Test
    public void findAllUsersByBirthDateBetween_FindsUsers() {
        LocalDate birthDateFrom = LocalDate.now().minusYears(2);
        LocalDate birthDateTo = LocalDate.now();

        when(userRepository.findAllByBirthDateBetween(any(), any()))
                .thenReturn(List.of(
                        new User(), new User()
                ));

        List<UserDTO> fetchedUserDTOs = userService.findAllUsersByBirthDateBetween(
                        birthDateFrom,
                        birthDateTo
                );

        verify(userRepository, times(1))
                .findAllByBirthDateBetween(birthDateFrom, birthDateTo);
        assertEquals(2, fetchedUserDTOs.size());
    }
}