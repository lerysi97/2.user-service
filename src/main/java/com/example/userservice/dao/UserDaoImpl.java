package com.example.userservice;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class UserDaoImpl {

    private final SessionFactory sessionFactory;

    public UserDaoImpl() {
        sessionFactory = new Configuration()
                .configure()
                .buildSessionFactory();
    }

    public void save(User user){
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        session.persist(user);

        transaction.commit();
        session.close();
    }

    public User findById(Long id) {
        Session session = sessionFactory.openSession();
        User user = session.find(User.class, id);
        session.close();

        return user;
    }

    public void update(User user) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        session.merge(user);

        transaction.commit();
        session.close();
    }

    public void deleteById(Long id) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        User user = session.find(User.class, id);
        if (user != null) {
            session.remove(user);
        } else {
            System.out.println("Пользователь с id" + id + "не найден.");
        }

        transaction.commit();
        session.close();
    }
}
