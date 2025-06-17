package com.example.userservice.service;

import com.example.userservice.dto.UserRegistDto;
import com.example.userservice.dto.UserVozvratDto;
import com.example.userservice.entity.User;
import com.example.userservice.mapper.UserMapper;
import com.example.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserVozvratDto createUser(UserRegistDto userRegistDto) {
        if (userRepository.existsByEmail(userRegistDto.getEmail())) {
            throw new IllegalArgumentException("Пользователь с таким email уже существует");
        }

        User user = UserMapper.mapToEntity(userRegistDto);
        User savedUser = userRepository.save(user);
        return UserMapper.mapToDto(savedUser);
    }

    @Override
    public UserVozvratDto findUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь с таким id не существует"));

        return UserMapper.mapToDto(user);
    }

    @Override
    public UserVozvratDto deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь с таким id не существует"));

        UserVozvratDto dto = UserMapper.mapToDto(user);
        userRepository.delete(user);
        return dto;
    }

    @Override
    public UserVozvratDto updateUser(Long userId, UserRegistDto userRegistDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь с таким id не существует"));

        UserMapper.updateEntityDto(userRegistDto, user);

        User updatedUser = userRepository.save(user);
        return UserMapper.mapToDto(updatedUser);
    }

    @Override
    public List<UserVozvratDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::mapToDto)
                .toList();
    }
}
