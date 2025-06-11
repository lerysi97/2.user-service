package com.example.userservice.model;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    void EqualsHashCode() throws Exception {
        User u1 = createUser(1L);
        User u2 = createUser(1L);
        User u3 = createUser(2L);
        User u4 = createUser(null);

        assertEquals(u1, u2);
        assertNotEquals(u1, u3);
        assertNotEquals(u1, u4);
        //noinspection ConstantConditions,SimplifiableAssertion
        assertFalse(u1.equals(null));
        // noinspection SimplifiableAssertion
        assertFalse(u1.equals(new Object()));

        assertEquals(u1.hashCode(), u2.hashCode());
        assertEquals(0, u4.hashCode());
    }

    @Test
    void nullObject() throws Exception {
        User user = createUser(1L);
        //noinspection ConstantConditions,SimplifiableAssertion
        assertFalse(user.equals(null));
    }


    @Test
    void ReturnFalse() throws Exception {
        User user = createUser(1L);
        // noinspection SimplifiableAssertion
        assertFalse(user.equals(new Object()));
    }

    @Test
    void ToString() {
        LocalDateTime time = LocalDateTime.of(2024, 1, 1, 10, 0);
        User user = new User("Test", "test@test.ru", 20, time);
        user.setCreatedAt(time);

        String result = user.toString();
        assertTrue(result.contains("имя = Test"));
        assertTrue(result.contains("email = test@test.ru"));
        assertTrue(result.contains("создан: 2024-01-01T10:00"));
    }

    private User createUser(Long id) throws Exception {
        User user = new User("Test", "test@test.ru", 20, LocalDateTime.now());
        Field idField = User.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(user, id);
        return user;
    }
}
