package com.example.userservice.dao;

import com.example.userservice.config.HibernateUtil;
import com.example.userservice.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class UserDaoImpl implements UserDao {

    private static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    @Override
    public void save(User user) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
            logger.info("Пользователь сохранён: {}", user);
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            logger.error("Ошибка при сохранении пользователя: {}", user, e);
            throw e;
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            User user = session.find(User.class, id);
            if (user != null) {
                logger.info("Пользователь найден: {}", user);
            } else {
                logger.warn("Пользователь с id = {} не найден", id);
            }
            return Optional.ofNullable(user);
        } catch (Exception e) {
            logger.error("Ошибка при поиске пользователя по id = {}", id, e);
            throw e;
        }
    }

    @Override
    public void update(User user) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.merge(user);
            transaction.commit();
            logger.info("Пользователь обновлён: {}", user);
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            logger.error("Ошибка при обновлении пользователя: {}", user, e);
            throw e;
        }
    }

    @Override
    public void deleteById(Long id) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            User user = session.find(User.class, id);
            if (user != null) {
                session.remove(user);
                logger.info("Пользователь с id = {} удалён", id);
            } else {
                logger.warn("Пользователь с id = {} не найден для удаления", id);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            logger.error("Ошибка при удалении пользователя с id = {}", id, e);
            throw e;
        }
    }

    @Override
    public List<User> getAll() {
        try (Session session = sessionFactory.openSession()) {
            List<User> users = session.createQuery("FROM User", User.class).getResultList();
            logger.info("Получено {} пользователей", users.size());
            return users;
        } catch (Exception e) {
            logger.error("Ошибка при получении списка пользователей", e);
            throw e;
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        try (Session session = sessionFactory.openSession()) {
            Long count = session.createQuery(
                            "SELECT COUNT(u) FROM User u WHERE u.email = :email", Long.class)
                    .setParameter("email", email)
                    .getSingleResult();
            logger.info("Проверка наличия пользователя с email {}: найдено {}", email, count);
            return count > 0;
        } catch (Exception e) {
            logger.error("Ошибка при проверке email: {}", email, e);
            throw e;
        }
    }
}

