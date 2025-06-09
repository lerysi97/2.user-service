package com.example.userservice;

import com.example.userservice.config.HibernateUtil;
import com.example.userservice.dao.UserDaoImpl;
import com.example.userservice.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        UserDaoImpl userDao = new UserDaoImpl(HibernateUtil.getSessionFactory());

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("""
                    Выберите действие:
                    1 — Создать пользователя
                    2 — Найти пользователя по id
                    3 — Обновить пользователя
                    4 — Удалить пользователя
                    5 — Показать всех пользователей
                    6 — Выйти
                    """);

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("Введите имя:");
                    String name = sc.nextLine();
                    System.out.println("Введите email:");
                    String email = sc.nextLine();
                    System.out.println("Введите возраст:");
                    int age = sc.nextInt();
                    sc.nextLine();
                    LocalDateTime createdAt = LocalDateTime.now();
                    User newUser = new User(name, email, age, createdAt);
                    try {
                        userDao.save(newUser);
                        System.out.println("Пользователь создан:\n" + newUser);
                    } catch (org.hibernate.exception.ConstraintViolationException e) {
                        System.out.println("Такой пользователь уже существует:\n" + newUser);
                    } if (userDao.existsByEmail(email)) {
                        System.out.println("Пользователь с таким email уже существует!");
                        break;
                    }
                case 2:
                    System.out.println("Введите id: ");
                    Long id = sc.nextLong();
                    Optional<User> optionalUser2 = userDao.findById(id);
                    if (optionalUser2.isPresent()) {
                        System.out.println("Пользователь найден:\n" + optionalUser2.get());
                    } else {
                        System.out.printf("Пользователь с id = %d не найден%n", id);
                    }
                    System.out.println();
                    break;
                case 3:
                    System.out.println("Введите id: ");
                    Long id1 = sc.nextLong();
                    sc.nextLine();
                    Optional<User> optionalUser3 = userDao.findById(id1);
                    if (optionalUser3.isPresent()) {
                        User user = optionalUser3.get();
                        System.out.println("Введите новое имя:");
                        String name1 = sc.nextLine();
                        System.out.println("Введите новый email:");
                        String email1 = sc.nextLine();
                        System.out.println("Введите новый возраст:");
                        int age1 = sc.nextInt();
                        sc.nextLine();

                        user.setName(name1);
                        user.setEmail(email1);
                        user.setAge(age1);
                        userDao.update(user);
                        System.out.println("Пользователь обновлен:\n" + user);
                    } else {
                        System.out.printf("Пользователь с id = %d не найден%n", id1);
                    }
                    System.out.println();
                    break;
                case 4:
                    System.out.println("Введите id: ");
                    Long id2 = sc.nextLong();
                    Optional<User> optionalUser4 = userDao.findById(id2);
                    if (optionalUser4.isPresent()) {
                        userDao.deleteById(id2);
                        System.out.println("Пользователь удалён:\n" + optionalUser4.get());
                    } else {
                        System.out.printf("Пользователь с id = %d не найден!%n", id2);
                    }
                    System.out.println();
                    break;
                case 5:
                    List<User> allUsers = userDao.getAll();
                    if (allUsers.isEmpty()) {
                        System.out.println("Пользователей не найдено.");
                    } else {
                        allUsers.forEach(System.out::println);
                    }
                    System.out.println();
                    break;
                case 6:
                    System.out.println("Выход из программы");
                    HibernateUtil.shutdown();
                    return;
                default:
                    System.out.println("Вы ввели неправильное значение.");
            }
        }
    }
}
