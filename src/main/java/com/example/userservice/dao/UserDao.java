package com.example.userservice;

import java.util.List;

public interface UserDao {
    void save(User user);
    User getById (Long id);
    List<User> getAll();
    void update(User user);
    void delete(Long id);
}
