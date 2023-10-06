package com.example.clearsolutionspracticalassigment.mapper;

import com.example.clearsolutionspracticalassigment.entity.User;
import com.example.clearsolutionspracticalassigment.dto.UserDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperImplTest {

    private final UserMapperImpl userMapper = new UserMapperImpl();

    @Test
    void mapUserToUserDTO_MapsCorrectly() {
        User user = User.builder()
                .uuid(UUID.randomUUID())
                .email("test@gmail.com")
                .firstName("John")
                .lastName("Dou")
                .birthDate(LocalDate.now())
                .address("Some address")
                .phoneNumber("+380000000000")
                .build();

        UserDTO userDTO = userMapper.mapUserToUserDTO(user);

        assertEquals(user.getUuid(), userDTO.getUuid());
        assertEquals(user.getEmail(), userDTO.getEmail());
        assertEquals(user.getFirstName(), userDTO.getFirstName());
        assertEquals(user.getLastName(), userDTO.getLastName());
        assertEquals(user.getBirthDate(), userDTO.getBirthDate());
        assertEquals(user.getAddress(), userDTO.getAddress());
        assertEquals(user.getPhoneNumber(), userDTO.getPhoneNumber());
    }
}