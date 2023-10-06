package com.example.clearsolutionspracticalassigment.mapper;

import com.example.clearsolutionspracticalassigment.dto.UserDTO;
import com.example.clearsolutionspracticalassigment.entity.User;

public interface UserMapper {

    UserDTO mapUserToUserDTO(User user);
}
