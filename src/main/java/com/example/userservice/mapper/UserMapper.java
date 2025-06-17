package com.example.userservice.mapper;

import com.example.userservice.dto.UserRegistDto;
import com.example.userservice.dto.UserVozvratDto;
import com.example.userservice.entity.User;

import java.time.LocalDateTime;

public class UserMapper {

    public static UserVozvratDto mapToDto(User user) {
        return new UserVozvratDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getAge(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    public static User mapToEntity(UserRegistDto dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setAge(dto.getAge());
        user.setCreatedAt(LocalDateTime.now());
        return user;
    }

    public static void updateEntityDto(UserRegistDto dto, User user) {
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setAge(dto.getAge());
        user.setUpdatedAt(LocalDateTime.now());
    }
}
