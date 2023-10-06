package com.example.clearsolutionspracticalassigment.mapper;

import com.example.clearsolutionspracticalassigment.dto.UserDTO;
import com.example.clearsolutionspracticalassigment.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDTO mapUserToUserDTO(User user) {
        return UserDTO.builder()
                .uuid(user.getUuid())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .birthDate(user.getBirthDate())
                .address(user.getAddress())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }
}
