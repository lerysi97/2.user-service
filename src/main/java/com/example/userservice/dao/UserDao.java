package com.example.userservice.dao;

import com.example.userservice.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    void save(User user);
    Optional<User> findById(Long id);
    List<User> getAll();
    void update(User user);
    void deleteById (Long id);
    boolean existsByEmail(String email);
}
