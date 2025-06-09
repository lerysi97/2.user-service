package com.example.userservice.service.dao;

import com.example.userservice.dao.UserDaoImpl;
import com.example.userservice.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.time.temporal.ChronoUnit.MICROS;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Testcontainers
public class UserDaoImplTest {

    private static SessionFactory sessionFactory;

    private static int emailCounter = 0;
    private User createTestUser() {
        String name = "Lera";
        String email = "test" + (++emailCounter) + "@test.ru";
        int age = 100;
        LocalDateTime createdAt = LocalDateTime.now().truncatedTo(MICROS);
        return new User(name, email, age, createdAt);
    }

    @AfterEach
    void cleanUp() {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.createQuery("DELETE FROM User").executeUpdate();
            tx.commit();
        }
    }

    @SuppressWarnings("resource")
    @Container
    public static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:14.1-alpine")
                    .withDatabaseName("test-db")
                    .withUsername("tester")
                    .withPassword("test-pass");

    @BeforeAll
    public static void init() {
        String jdbcUrl = postgreSQLContainer.getJdbcUrl();
        String username = postgreSQLContainer.getUsername();
        String password = postgreSQLContainer.getPassword();

        Map<String, Object> settings = Stream.of(
                new AbstractMap.SimpleEntry<>("hibernate.connection.driver_class", "org.postgresql.Driver"),
                new AbstractMap.SimpleEntry<>("hibernate.connection.url", jdbcUrl),
                new AbstractMap.SimpleEntry<>("hibernate.connection.username", username),
                new AbstractMap.SimpleEntry<>("hibernate.connection.password", password),
                new AbstractMap.SimpleEntry<>("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect"),
                new AbstractMap.SimpleEntry<>("hibernate.hbm2ddl.auto", "update"),
                new AbstractMap.SimpleEntry<>("hibernate.show_sql", "true")
        ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .applySettings(settings)
                .build();

        sessionFactory = new MetadataSources(registry)
                .addAnnotatedClass(User.class)
                .buildMetadata()
                .buildSessionFactory();
    }

    @Test
    public void save() {
        User user = createTestUser();

        UserDaoImpl userDao = new UserDaoImpl(sessionFactory);
        userDao.save(user);

        Assertions.assertNotNull(user.getId());

        Optional<User> result = userDao.findById(user.getId());
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(user, result.get());
    }

    @Test
    public void findById() {
        User user = createTestUser();

        UserDaoImpl userDao = new UserDaoImpl(sessionFactory);
        userDao.save(user);

        Optional<User> result = userDao.findById(user.getId());
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(user, result.get());
    }

    @Test
    public void update() {
        UserDaoImpl userDao = new UserDaoImpl(sessionFactory);
        User user = createTestUser();
        userDao.save(user);

        user.setName("NewName");
        userDao.update(user);

        Optional<User> updated = userDao.findById(user.getId());
        Assertions.assertTrue(updated.isPresent());
        Assertions.assertEquals("NewName", updated.get().getName());
    }

    @Test
    public void deleteById() {
        UserDaoImpl userDao = new UserDaoImpl(sessionFactory);
        User user = createTestUser();
        userDao.save(user);

        userDao.deleteById(user.getId());

        Optional<User> result = userDao.findById(user.getId());
        Assertions.assertFalse(result.isPresent());
    }

    @Test
    public void getAll() {
        UserDaoImpl userDao = new UserDaoImpl(sessionFactory);
        User user1 = createTestUser();
        user1.setEmail("test1@test.ru");
        User user2 = createTestUser();
        user2.setEmail("test2@test.ru");

        userDao.save(user1);
        userDao.save(user2);

        var users = userDao.getAll();
        Assertions.assertTrue(users.contains(user1));
        Assertions.assertTrue(users.contains(user2));
    }

    @Test
    public void existsByEmail() {
        UserDaoImpl userDao = new UserDaoImpl(sessionFactory);
        User user = createTestUser();
        userDao.save(user);

        Assertions.assertTrue(userDao.existsByEmail(user.getEmail()));
        Assertions.assertFalse(userDao.existsByEmail("no-email@test.ru"));
    }

    @Test
    void save_catch () {
        SessionFactory mockSessionFactory = Mockito.mock(SessionFactory.class);
        Session mockSession = Mockito.mock(Session.class);
        Transaction mockTransaction = Mockito.mock(Transaction.class);

        Mockito.when(mockSessionFactory.openSession()).thenReturn(mockSession);
        Mockito.when(mockSession.beginTransaction()).thenReturn(mockTransaction);
        Mockito.doThrow(new RuntimeException("Ошибка при сохранении пользователя")).when(mockSession).persist(Mockito.any());

        UserDaoImpl userDao = new UserDaoImpl(mockSessionFactory);

        assertThrows(RuntimeException.class, () -> userDao.save(new User()));
        Mockito.verify(mockTransaction).rollback();
    }

    @Test
    void findById_catch () {
        SessionFactory sf = Mockito.mock(SessionFactory.class);
        Session s = Mockito.mock(Session.class);
        Mockito.when(sf.openSession()).thenReturn(s);
        Mockito.when(s.find(Mockito.eq(User.class), Mockito.any())).thenThrow(new RuntimeException("Ошибка при поиске пользователя по id"));
        UserDaoImpl dao = new UserDaoImpl(sf);

        assertThrows(RuntimeException.class, () -> dao.findById(1L));
    }

    @Test
    void update_catch () {
        SessionFactory sf = Mockito.mock(SessionFactory.class);
        Session session = Mockito.mock(Session.class);
        Transaction tx = Mockito.mock(Transaction.class);
        Mockito.when(sf.openSession()).thenReturn(session);
        Mockito.when(session.beginTransaction()).thenReturn(tx);
        Mockito.doThrow(new RuntimeException("Ошибка при обновлении пользователя")).when(session).merge(Mockito.any());
        UserDaoImpl dao = new UserDaoImpl(sf);

        assertThrows(RuntimeException.class, () -> dao.update(new User()));
        Mockito.verify(tx).rollback();
    }

    @Test
    void deleteById_catch () {
        SessionFactory sf = Mockito.mock(SessionFactory.class);
        Session session = Mockito.mock(Session.class);
        Transaction tx = Mockito.mock(Transaction.class);
        Mockito.when(sf.openSession()).thenReturn(session);
        Mockito.when(session.beginTransaction()).thenReturn(tx);
        Mockito.when(session.find(Mockito.eq(User.class), Mockito.any())).thenThrow(new RuntimeException("Ошибка при удалении пользователя с id"));
        UserDaoImpl dao = new UserDaoImpl(sf);

        assertThrows(RuntimeException.class, () -> dao.deleteById(1L));
        Mockito.verify(tx).rollback();
    }

    @Test
    void getAll_catch () {
        SessionFactory sf = Mockito.mock(SessionFactory.class);
        Session session = Mockito.mock(Session.class);
        Mockito.when(sf.openSession()).thenReturn(session);
        Mockito.when(session.createQuery(Mockito.anyString(), Mockito.eq(User.class)))
                .thenThrow(new RuntimeException("Ошибка при получении списка пользователей"));
        UserDaoImpl dao = new UserDaoImpl(sf);

        assertThrows(RuntimeException.class, dao::getAll);
    }

    @Test
    void existsByEmail_catch () {
        SessionFactory sf = Mockito.mock(SessionFactory.class);
        Session session = Mockito.mock(Session.class);
        Mockito.when(sf.openSession()).thenReturn(session);
        Mockito.when(session.createQuery(Mockito.anyString(), Mockito.eq(Long.class)))
                .thenThrow(new RuntimeException("Ошибка при проверке email"));
        UserDaoImpl dao = new UserDaoImpl(sf);

        assertThrows(RuntimeException.class, () -> dao.existsByEmail("test@test.ru"));
    }
}
