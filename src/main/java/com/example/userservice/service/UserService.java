package com.example.userservice.service;

import com.example.userservice.dao.UserDao;
import com.example.userservice.model.User;
import java.util.Optional;

public class UserService {

    private final UserDao dao;

    public UserService(UserDao dao) {
        this.dao = dao;
    }

    public void createUser(User user) {
        dao.save(user);
    }

    public Optional<User> findUserById(Long userId) {
        return dao.findById(userId);
    }

    public Optional<User> deleteUser(Long userId) {
        Optional<User> userOptional = dao.findById(userId);
        if (userOptional.isPresent()) {
            dao.deleteById(userId);
            return userOptional;
        }
        return Optional.empty();
    }

    public Optional<User> updateUser (Long userId) {
        Optional<User> userOptional = dao.findById(userId);
        if (userOptional.isPresent()) {
            dao.update(userOptional.get());
            return userOptional;
        }
        return Optional.empty();
    }
}

