package com.example.clearsolutionspracticalassigment.controller;

import com.example.clearsolutionspracticalassigment.controller.payload.CreateUserRequestPayload;
import com.example.clearsolutionspracticalassigment.controller.payload.PatchUserRequestPayload;
import com.example.clearsolutionspracticalassigment.controller.payload.UpdateUserRequestPayload;
import com.example.clearsolutionspracticalassigment.dto.UserDTO;
import com.example.clearsolutionspracticalassigment.exception.InvalidArgumentException;
import com.example.clearsolutionspracticalassigment.service.UserService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private static CreateUserRequestPayload validCreatePayload;
    private static CreateUserRequestPayload invalidCreatePayload;
    private static UpdateUserRequestPayload validUpdatePayload;
    private static UpdateUserRequestPayload invalidUpdatePayload;
    private static PatchUserRequestPayload validPatchPayload;
    private static PatchUserRequestPayload invalidPatchPayload;

    @BeforeAll
    public static void setup() {
        validCreatePayload = CreateUserRequestPayload.builder()
                .email("test@gmail.com")
                .firstName("John")
                .lastName("Dou")
                .birthDate(LocalDate.now().minusYears(2))
                .phoneNumber("+380000000000")
                .build();
        invalidCreatePayload = CreateUserRequestPayload.builder()
                .email("invalid_email")
                .firstName("")
                .lastName("")
                .birthDate(LocalDate.now().plusYears(2))
                .phoneNumber("phone_number")
                .build();
        validUpdatePayload = convertCreatePayloadToUpdatePayload(validCreatePayload);
        invalidUpdatePayload = convertCreatePayloadToUpdatePayload(invalidCreatePayload);
        validPatchPayload = convertCreatePayloadToPatchPayload(validCreatePayload);
        invalidPatchPayload = convertCreatePayloadToPatchPayload(invalidCreatePayload);
    }


    @Test
    public void createUser_ValidatesPayload() throws Exception {
        ResultActions response = mockMvc.perform(
                post("/" + UserController.USER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidCreatePayload))
        );

        response.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title", is(InvalidArgumentException.name())))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.name())))
                .andExpect(jsonPath("$.messages.size()", is(5)));
    }

    @Test
    public void createUser_ReturnsLocation() throws Exception {
        UserDTO userDTO = UserDTO.builder()
                .uuid(UUID.randomUUID())
                .build();
        String locationHeaderVal = UserController.USER_PATH + "/" + userDTO.getUuid().toString();

        when(userService.createUser(any())).thenReturn(userDTO);

        ResultActions response = mockMvc.perform(
                post("/" + UserController.USER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validCreatePayload))
        );

        response.andExpect(status().isCreated())
                .andExpect(header().string("Location", is(locationHeaderVal)));
    }

    @Test
    public void updateUser_ValidatesPathVariable() throws Exception {
        ResultActions response = mockMvc.perform(
                put("/" + UserController.USER_PATH + "/" + "invalid_UUID")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUpdatePayload))
        );

        response.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title", is(InvalidArgumentException.name())))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.name())))
                .andExpect(jsonPath("$.messages.size()", is(1)));
    }

    @Test
    public void updateUser_ValidatesPayload() throws Exception {
        ResultActions response = mockMvc.perform(
                put("/" + UserController.USER_PATH + "/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUpdatePayload))
        );

        response.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title", is(InvalidArgumentException.name())))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.name())))
                .andExpect(jsonPath("$.messages.size()", is(5)));
    }

    @Test
    public void updateUser_ReturnsNoContent() throws Exception {
        UUID userUuid = UUID.randomUUID();

        ResultActions response = mockMvc.perform(
                put("/" + UserController.USER_PATH + "/" + userUuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUpdatePayload))
        );

        verify(userService, times(1)).updateUser(any(), any());
        response.andExpect(status().isNoContent());
    }

    @Test
    public void patchUser_ValidatesPathVariable() throws Exception {
        ResultActions response = mockMvc.perform(
                patch("/" + UserController.USER_PATH + "/" + "invalid_UUID")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validPatchPayload))
        );

        response.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title", is(InvalidArgumentException.name())))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.name())))
                .andExpect(jsonPath("$.messages.size()", is(1)));
    }

    @Test
    public void patchUser_ValidatesPayload() throws Exception {
        ResultActions response = mockMvc.perform(
                patch("/" + UserController.USER_PATH + "/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidPatchPayload))
        );

        response.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title", is(InvalidArgumentException.name())))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.name())))
                .andExpect(jsonPath("$.messages.size()", is(3)));
    }

    @Test
    public void patchUser_ReturnsNoContent() throws Exception {
        UUID userUuid = UUID.randomUUID();

        ResultActions response = mockMvc.perform(
                patch("/" + UserController.USER_PATH + "/" + userUuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validPatchPayload))
        );

        verify(userService, times(1)).patchUser(any(), any());
        response.andExpect(status().isNoContent());
    }

    @Test
    public void deleteUser_ValidatesPathVariable() throws Exception {
        ResultActions response = mockMvc.perform(
                delete("/" + UserController.USER_PATH + "/" + "invalid_UUID")
        );

        response.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title", is(InvalidArgumentException.name())))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.name())))
                .andExpect(jsonPath("$.messages.size()", is(1)));
    }

    @Test
    public void deleteUser_ReturnsNoContent() throws Exception {
        UUID userUuid = UUID.randomUUID();

        ResultActions response = mockMvc.perform(
                delete("/" + UserController.USER_PATH + "/" + userUuid)
        );

        verify(userService, times(1)).deleteUser(userUuid);
        response.andExpect(status().isNoContent());
    }

    @Test
    public void getUserById_ValidatesPathVariable() throws Exception {
        ResultActions response = mockMvc.perform(
                get("/" + UserController.USER_PATH + "/" + "invalid_UUID")
        );

        response.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title", is(InvalidArgumentException.name())))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.name())))
                .andExpect(jsonPath("$.messages.size()", is(1)));
    }

    @Test
    public void getUserById_ReturnsOk() throws Exception {
        UUID userUuid = UUID.randomUUID();

        ResultActions response = mockMvc.perform(
                get("/" + UserController.USER_PATH + "/" + userUuid)
        );

        verify(userService, times(1)).getUserById(userUuid);
        response.andExpect(status().isOk());
    }

    @Test
    public void listUsers_ValidatesLeftBorder() throws Exception {
        ResultActions response = mockMvc.perform(
                get("/" + UserController.USER_PATH)
                        .queryParam("birthDateFrom", LocalDate.now().toString())
                        .queryParam("birthDateTo", LocalDate.now().minusYears(2).toString())
        );

        response.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title", is(InvalidArgumentException.name())))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.name())))
                .andExpect(jsonPath("$.messages.size()", is(1)));
    }

    @Test
    public void listUsers_ValidatesRightBorder() throws Exception {
        ResultActions response = mockMvc.perform(
                get("/" + UserController.USER_PATH)
                        .queryParam("birthDateFrom", LocalDate.now().minusYears(2).toString())
                        .queryParam("birthDateTo", LocalDate.now().toString())
        );

        response.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title", is(InvalidArgumentException.name())))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.name())))
                .andExpect(jsonPath("$.messages.size()", is(1)));
    }

    @Test
    public void listUsers_ReturnsOk() throws Exception {
        LocalDate validBorder = LocalDate.now().minusYears(2);

        when(userService.findAllUsersByBirthDateBetween(any(), any()))
                .thenReturn(List.of(new UserDTO(), new UserDTO()));

        ResultActions response = mockMvc.perform(
                get("/" + UserController.USER_PATH)
                        .queryParam("birthDateFrom", validBorder.toString())
                        .queryParam("birthDateTo", validBorder.toString())
        );

        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", is(2)));
    }

    private static UpdateUserRequestPayload convertCreatePayloadToUpdatePayload(
            CreateUserRequestPayload creationPayload
    ) {
        return UpdateUserRequestPayload.builder()
                .email(creationPayload.getEmail())
                .firstName(creationPayload.getFirstName())
                .lastName(creationPayload.getLastName())
                .birthDate(creationPayload.getBirthDate())
                .phoneNumber(creationPayload.getPhoneNumber())
                .build();
    }

    private static PatchUserRequestPayload convertCreatePayloadToPatchPayload(
            CreateUserRequestPayload creationPayload
    ) {
        return PatchUserRequestPayload.builder()
                .email(creationPayload.getEmail())
                .birthDate(creationPayload.getBirthDate())
                .phoneNumber(creationPayload.getPhoneNumber())
                .build();
    }
}