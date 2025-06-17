package com.example.userservice.service;

import com.example.userservice.dto.UserRegistDto;
import com.example.userservice.dto.UserVozvratDto;

import java.util.List;

public interface UserService{

    UserVozvratDto createUser(UserRegistDto userRegistDto);

    UserVozvratDto findUserById(Long userId);

    UserVozvratDto deleteUser(Long userId);

    UserVozvratDto updateUser (Long userId, UserRegistDto userRegistDto);

    List<UserVozvratDto> getAllUsers();
}

