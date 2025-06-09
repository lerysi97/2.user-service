package com.example.userservice.service;

import com.example.userservice.dao.UserDao;
import com.example.userservice.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Optional;

public class UserServiceTest {
    String name = "Lera";
    String email = "test@test.ru";
    int age = 100;
    LocalDateTime createdAt = LocalDateTime.now();

    @Test
    public void createUser() {
        UserDao mockUserDao = Mockito.mock(UserDao.class);
        UserService userService = new UserService(mockUserDao);
        User mockUser = new User(name, email, age, createdAt);
        userService.createUser(mockUser);
        Mockito.verify(mockUserDao).save(mockUser);
    }

    @Test
    public void findUserById_userFound() {
        UserDao mockUserDao = Mockito.mock(UserDao.class);
        UserService userService = new UserService(mockUserDao);
        User mockUser = new User(name, email, age, createdAt);
        Long userId = 1L;
        Mockito.when(mockUserDao.findById(userId)).thenReturn(Optional.of(mockUser));
        Optional<User> result = userService.findUserById(userId);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(mockUser, result.get());
        Mockito.verify(mockUserDao).findById(userId);
    }

    @Test
    public void findUserById_userNotFound() {
        UserDao mockUserDao = Mockito.mock(UserDao.class);
        UserService userService = new UserService(mockUserDao);
        Long userId = 1L;
        Mockito.when(mockUserDao.findById(userId)).thenReturn(Optional.empty());
        Optional<User> result = userService.findUserById(userId);
        Assertions.assertTrue(result.isEmpty());
        Mockito.verify(mockUserDao).findById(userId);
    }

    @Test
    public void deleteUser_userFound() {
        UserDao mockUserDao = Mockito.mock(UserDao.class);
        UserService userService = new UserService(mockUserDao);
        User mockUser = new User(name, email, age, createdAt);
        Long userId = 1L;
        Mockito.when(mockUserDao.findById(userId)).thenReturn(Optional.of(mockUser));
        Optional<User> result = userService.deleteUser(userId);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(mockUser, result.get());
        Mockito.verify(mockUserDao).findById(userId);
        Mockito.verify(mockUserDao).deleteById(userId);
    }

    @Test
    public void deleteUser_userNotFound() {
        UserDao mockUserDao = Mockito.mock(UserDao.class);
        UserService userService = new UserService(mockUserDao);
        Long userId = 1L;
        Mockito.when(mockUserDao.findById(userId)).thenReturn(Optional.empty());
        Optional<User> result = userService.deleteUser(userId);
        Assertions.assertTrue(result.isEmpty());
        Mockito.verify(mockUserDao, Mockito.never()).deleteById(userId);
    }

    @Test
    public void updateUser () {
        UserDao mockUserDao = Mockito.mock(UserDao.class);
        UserService userService = new UserService(mockUserDao);
        User mockUser = new User(name, email, age, createdAt);
        Long userId = 1L;
        Mockito.when(mockUserDao.findById(userId)).thenReturn(Optional.of(mockUser));
        Optional<User> result = userService.updateUser(userId);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(mockUser, result.get());
        Mockito.verify(mockUserDao).update(mockUser);
        Mockito.verify(mockUserDao).findById(userId);
    }

    @Test
    public void updateUser_userNotFound() {
        UserDao mockUserDao = Mockito.mock(UserDao.class);
        UserService userService = new UserService(mockUserDao);
        Long userId = 1L;
        Mockito.when(mockUserDao.findById(userId)).thenReturn(Optional.empty());
        Optional<User> result = userService.updateUser(userId);
        Assertions.assertTrue(result.isEmpty());
        Mockito.verify(mockUserDao, Mockito.never()).update(Mockito.any());
    }
}

