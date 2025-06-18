package com.example.userservice.service;

import com.example.userservice.dto.UserRegistDto;
import com.example.userservice.dto.UserVozvratDto;
import com.example.userservice.entity.User;
import com.example.userservice.mapper.UserMapper;
import com.example.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserVozvratDto createUser(UserRegistDto userRegistDto) {
        log.info("Создание пользователя: {}", userRegistDto);

        if (userRepository.existsByEmail(userRegistDto.getEmail())) {
            log.warn("Такой пользователь уже существует: {}", userRegistDto.getEmail());
            throw new IllegalArgumentException("Пользователь с таким email уже существует!");
        }

        User user = UserMapper.mapToEntity(userRegistDto);
        User savedUser = userRepository.save(user);

        log.info("Пользователь создан: {}", savedUser);
        return UserMapper.mapToDto(savedUser);
    }

    @Override
    public UserVozvratDto findUserById(Long userId) {
        log.info("Поиск пользователя по id: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь с таким id не существует"));

        log.info("Пользователь найден: {}", user);
        return UserMapper.mapToDto(user);
    }

    @Override
    public UserVozvratDto deleteUser(Long userId) {
        log.info("Удаление пользователя с id: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь с таким id не существует"));

        UserVozvratDto dto = UserMapper.mapToDto(user);
        userRepository.delete(user);

        log.info("Пользователь удален: {}", user);
        return dto;
    }

    @Override
    public UserVozvratDto updateUser(Long userId, UserRegistDto userRegistDto) {
        log.info("Обновление пользователя с id: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь с таким id не существует"));

        UserMapper.updateEntityDto(userRegistDto, user);

        User updatedUser = userRepository.save(user);

        log.info("Пользователь обновлен: {}", user);
        return UserMapper.mapToDto(updatedUser);
    }

    @Override
    public List<UserVozvratDto> getAllUsers() {
        log.info("Запрошен список всех пользователей");

        List<UserVozvratDto> users = userRepository.findAll().stream()
                .map(UserMapper::mapToDto)
                .toList();

        log.info("Получены все пользователи: {}", users);
        return users;
    }
}
