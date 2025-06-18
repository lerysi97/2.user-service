package com.example.userservice.controller;

import com.example.userservice.dto.UserRegistDto;
import com.example.userservice.dto.UserVozvratDto;
import com.example.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserVozvratDto> createUser(@RequestBody @Valid UserRegistDto userRegistDto) {
        log.info("Создание пользователя: {}", userRegistDto);
        UserVozvratDto dto = userService.createUser(userRegistDto);
        log.info("Пользователь создан: {}", dto);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserVozvratDto> findUserById(@PathVariable Long id) {
        log.info("Поиск пользователя по id: {}", id);
        UserVozvratDto dto = userService.findUserById(id);
        log.info("Пользователь найден: {}", dto);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<UserVozvratDto> deleteUser(@PathVariable Long userId) {
        log.info("Удаление пользователя с id: {}", userId);
        UserVozvratDto dto = userService.deleteUser(userId);
        log.info("Пользователь удален: {}", userId);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserVozvratDto> updateUser(@PathVariable Long userId, @RequestBody @Valid UserRegistDto userRegistDto) {
        log.info("Обновление пользователя с id: {}", userId);
        UserVozvratDto dto = userService.updateUser(userId, userRegistDto);
        log.info("Пользователь обновлен: {}", userId);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<List<UserVozvratDto>> getAllUsers() {
        log.info("Запрошен список всех пользователей");
        List<UserVozvratDto> users = userService.getAllUsers();
        log.info("Получены все пользователи: {}", users);
        return ResponseEntity.ok(users);
    }
}

