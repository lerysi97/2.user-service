package com.example.userservice.dao;

import com.example.userservice.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UserDaoImpl implements UserDao {

    private static final Logger log = LoggerFactory.getLogger(UserDaoImpl.class);

    private final SessionFactory sessionFactory;

    public UserDaoImpl() {
        sessionFactory = new Configuration()
                .configure()
                .buildSessionFactory();
    }

    @Override
    public void save(User user){
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            try {
                if (user == null || user.getName() == null || user.getName().isBlank() || user.getEmail() == null || user.getEmail().isBlank() || user.getAge() <= 0) {
                    throw new IllegalArgumentException("Не может быть null.");
                }

                session.persist(user);
                transaction.commit();
            } catch (Exception e) {
                if (transaction != null && transaction.isActive()) {
                    transaction.rollback();
                }
                log.error("Не удалось сохранить пользователя.", e);
            }
        }
    }

    @Override
    public User getById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.find(User.class, id);
        } catch (Exception e) {
            log.error("Не удалось найти пользователя по id.", e);
            return null;
        }
    }

    @Override
    public List<User> getAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM User", User.class).list();
        } catch (Exception e) {
            log.error("Не удалось получить список пользователей.", e);
            return List.of();
        }
    }

    @Override
    public void update(User user) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            try {
                if (user == null || user.getId() == null) {
                    throw new IllegalArgumentException("Не может быть null");
                }

                session.merge(user);
                transaction.commit();
            } catch (Exception e) {
                if (transaction != null && transaction.isActive()) {
                    transaction.rollback();
                }
                log.error("Не удалось обновить данные пользователя.", e);
            }
        }
    }

    @Override
    public void delete(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            try {
                User user = session.find(User.class, id);
                if (user != null) {
                    session.remove(user);
                } else {
                    System.out.println("Пользователь не найден.");
                }

                transaction.commit();
            } catch (Exception e) {
                if (transaction != null && transaction.isActive()) {
                    transaction.rollback();
                }
                log.error("Не удалось удалить пользователя.", e);
            }
        }
    }
}

