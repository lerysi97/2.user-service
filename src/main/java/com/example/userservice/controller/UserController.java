package com.example.userservice.controller;

import com.example.userservice.dto.UserRegistDto;
import com.example.userservice.dto.UserVozvratDto;
import com.example.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserVozvratDto> createUser(@RequestBody @Valid UserRegistDto userRegistDto) {
        UserVozvratDto dto = userService.createUser(userRegistDto);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserVozvratDto> findUserById(@PathVariable Long id) {
        UserVozvratDto dto = userService.findUserById(id);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<UserVozvratDto> deleteUser(@PathVariable Long userId) {
        UserVozvratDto dto = userService.deleteUser(userId);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserVozvratDto> updateUser(@PathVariable Long userId, @RequestBody @Valid UserRegistDto userRegistDto) {
        UserVozvratDto dto = userService.updateUser(userId, userRegistDto);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<List<UserVozvratDto>> getAllUsers() {
        List<UserVozvratDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
}

