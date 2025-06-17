package com.example.userservice.util;

import com.example.userservice.dto.UserVozvratDto;
import com.example.userservice.entity.User;

import java.time.LocalDateTime;

public class DataFactoryTest {
    public static UserVozvratDto createSampleUserVozvratDto() {
        return new UserVozvratDto(
                1L,
                "Lera",
                "test@test.ru",
                28,
                LocalDateTime.of(2025, 1, 1, 1, 1),
                null
        );
    }

    public static User createSampleUser() {
        User user = new User();
        user.setId(1L);
        user.setName("Lera");
        user.setEmail("test@test.ru");
        user.setAge(28);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return user;
    }
}
