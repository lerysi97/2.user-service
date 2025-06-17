package com.example.userservice.controller;

import com.example.userservice.dto.UserRegistDto;
import com.example.userservice.dto.UserVozvratDto;
import com.example.userservice.service.UserService;
import com.example.userservice.util.DataFactoryTest;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserController.class)
class UserControllerTest {

    @MockBean
    UserService userService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;


    @Test
    void createUser() throws Exception {
        UserRegistDto userRegistDto = new UserRegistDto("Lera", "test@test.ru", 28);
        UserVozvratDto dto = DataFactoryTest.createSampleUserVozvratDto();
        Mockito.when(userService.createUser(userRegistDto)).thenReturn(dto);
        postJson("/api/users", userRegistDto)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Lera"))
                .andExpect(jsonPath("$.email").value("test@test.ru"))
                .andExpect(jsonPath("$.age").value(28));
        System.out.println(objectMapper.writeValueAsString(dto));
    }

    @Test
    void findUserById() throws Exception {
        UserVozvratDto dto = DataFactoryTest.createSampleUserVozvratDto();
        Mockito.when(userService.findUserById(1L)).thenReturn(dto);
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Lera"))
                .andExpect(jsonPath("$.email").value("test@test.ru"))
                .andExpect(jsonPath("$.age").value(28));
        System.out.println(objectMapper.writeValueAsString(dto));

    }

    @Test
    void findUserById_notFound() throws Exception {
        Mockito.when(userService.findUserById(1L))
                .thenThrow(new IllegalArgumentException("Пользователь с таким id не существует"));

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Пользователь с таким id не существует"));
    }

    @Test
    void deleteUser() throws Exception {
        UserVozvratDto dto = DataFactoryTest.createSampleUserVozvratDto();
        Mockito.when(userService.deleteUser(1L)).thenReturn(dto);
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Lera"))
                .andExpect(jsonPath("$.email").value("test@test.ru"))
                .andExpect(jsonPath("$.age").value(28));
        System.out.println(objectMapper.writeValueAsString(dto));
    }

    @Test
    void updateUser() throws Exception {
        UserVozvratDto dto = DataFactoryTest.createSampleUserVozvratDto();
        UserRegistDto userRegistDto = new UserRegistDto("Lera", "test@test.ru", 28);
        String json = objectMapper.writeValueAsString(userRegistDto);
        Mockito.when(userService.updateUser(1L, userRegistDto)).thenReturn(dto);
        mockMvc.perform(put("/api/users/1").content(json).contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Lera"))
                .andExpect(jsonPath("$.email").value("test@test.ru"))
                .andExpect(jsonPath("$.age").value(28));
        System.out.println(objectMapper.writeValueAsString(dto));
    }

    @Test
    void getAllUsers() throws Exception {
        UserVozvratDto dto = DataFactoryTest.createSampleUserVozvratDto();
        Mockito.when(userService.getAllUsers()).thenReturn(List.of(dto));
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Lera"))
                .andExpect(jsonPath("$[0].email").value("test@test.ru"))
                .andExpect(jsonPath("$[0].age").value(28));
        System.out.println(objectMapper.writeValueAsString(dto));
    }

    @Test
    void createUser_withInvalidEmail_returns400() throws Exception {
        UserRegistDto invalidDto = new UserRegistDto("Lera", "не email", 25);
        postJson("/api/users", invalidDto)
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUser_notNameEmail() throws Exception {
        UserRegistDto invalidDto = new UserRegistDto("", "не email", -1);

        postJson("/api/users", invalidDto)
                .andExpect(status().isBadRequest());
    }

    private @NotNull ResultActions postJson(String url, Object body) throws Exception {
        return mockMvc.perform(post(url)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)));
    }
}