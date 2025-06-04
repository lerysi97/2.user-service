package com.example.userservice.dao;

import com.example.userservice.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

public class UserDaoImplTest {
    private UserDaoImpl dao;

    @BeforeEach
    void setUp() {
        dao = new UserDaoImpl();
    }

    @AfterEach
    void tearDown() {
        // nothing to close - SessionFactory closes with JVM shutdown
    }

    @Test
    void testSaveAndGetById() {
        User user = new User("John", "john@example.com", 30, LocalDateTime.now());
        dao.save(user);
        assertNotNull(user.getId());

        User loaded = dao.findById(user.getId());
        assertNotNull(loaded);
        assertEquals(user.getName(), loaded.getName());
        assertEquals(user.getEmail(), loaded.getEmail());
        assertEquals(user.getAge(), loaded.getAge());
    }

    @Test
    void testGetAll() {
        User user1 = new User("Alice", "alice@example.com", 25, LocalDateTime.now());
        User user2 = new User("Bob", "bob@example.com", 35, LocalDateTime.now());
        dao.save(user1);
        dao.save(user2);

        // access internal SessionFactory to query all users
        org.hibernate.SessionFactory factory;
        try {
            Field field = UserDaoImpl.class.getDeclaredField("sessionFactory");
            field.setAccessible(true);
            factory = (org.hibernate.SessionFactory) field.get(dao);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try (org.hibernate.Session session = factory.openSession()) {
            List<User> users = session.createQuery("from User", User.class).list();
            assertEquals(2, users.size());
        }
    }

    @Test
    void testUpdate() {
        User user = new User("Carl", "carl@example.com", 40, LocalDateTime.now());
        dao.save(user);

        user.setName("Carl Updated");
        dao.update(user);

        User loaded = dao.findById(user.getId());
        assertEquals("Carl Updated", loaded.getName());
    }

    @Test
    void testDelete() {
        User user = new User("Dave", "dave@example.com", 22, LocalDateTime.now());
        dao.save(user);
        Long id = user.getId();

        dao.deleteById(id);
        User loaded = dao.findById(id);
        assertNull(loaded);
    }
}
