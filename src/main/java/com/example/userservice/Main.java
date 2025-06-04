package com.example.userservice;

import com.example.userservice.dao.UserDao;
import com.example.userservice.dao.UserDaoImpl;
import com.example.userservice.model.User;

import java.time.LocalDateTime;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        UserDao userDao = new UserDaoImpl();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("""
                Выберите действие:
                1 — Создать пользователя
                2 — Найти пользователя по id
                3 — Обновить пользователя
                4 — Удалить пользователя
                5 — Выйти
            """);

            String input = sc.nextLine().trim();
            if (!input.matches("\\d")) {
                System.out.println("Введите номер действия от 1 до 5.");
                continue;
            }

            int choice = Integer.parseInt(input);

            switch (choice) {
                case 1 -> {
                    String name, email;
                    int age;

                    while (true) {
                        System.out.print("Введите имя: ");
                        name = sc.nextLine().trim();
                        if (!name.isBlank()) break;
                        System.out.println("Имя не может быть пустым.");
                    }

                    while (true) {
                        System.out.print("Введите email: ");
                        email = sc.nextLine().trim();
                        if (!email.isBlank()) break;
                        System.out.println("Email не может быть пустым.");
                    }

                    while (true) {
                        System.out.print("Введите возраст: ");
                        String ageInput = sc.nextLine().trim();
                        if (ageInput.matches("\\d+")) {
                            age = Integer.parseInt(ageInput);
                            if (age > 0) break;
                        }
                        System.out.println("Возраст должен быть положительным числом.");
                    }

                    User newUser = new User(name, email, age, LocalDateTime.now());
                    try {
                        userDao.save(newUser);
                        System.out.println("Пользователь создан:\n" + newUser);
                    } catch (Exception e) {
                        System.out.println("Ошибка при сохранении: " + e.getMessage());
                    }
                }

                case 2 -> {
                    System.out.print("Введите id: ");
                    String idInput = sc.nextLine().trim();
                    if (!idInput.matches("\\d+")) {
                        System.out.println("Неверный формат id.");
                        continue;
                    }
                    long id = Long.parseLong(idInput);
                    User user = userDao.getById(id);
                    if (user != null) {
                        System.out.println("Пользователь найден:\n" + user);
                    } else {
                        System.out.printf("Пользователь с id = %d не найден%n", id);
                    }
                }

                case 3 -> {
                    System.out.print("Введите id пользователя для обновления: ");
                    String idInput = sc.nextLine().trim();
                    if (!idInput.matches("\\d+")) {
                        System.out.println("Неверный формат id.");
                        continue;
                    }
                    long id = Long.parseLong(idInput);
                    User user = userDao.getById(id);
                    if (user == null) {
                        System.out.printf("Пользователь с id = %d не найден%n", id);
                        break;
                    }

                    String name, email;
                    int age;

                    while (true) {
                        System.out.print("Введите новое имя: ");
                        name = sc.nextLine().trim();
                        if (!name.isBlank()) break;
                        System.out.println("Имя не может быть пустым.");
                    }

                    while (true) {
                        System.out.print("Введите новый email: ");
                        email = sc.nextLine().trim();
                        if (!email.isBlank()) break;
                        System.out.println("Email не может быть пустым.");
                    }

                    while (true) {
                        System.out.print("Введите новый возраст: ");
                        String ageInput = sc.nextLine().trim();
                        if (ageInput.matches("\\d+")) {
                            age = Integer.parseInt(ageInput);
                            if (age > 0) break;
                        }
                        System.out.println("Возраст должен быть положительным числом.");
                    }

                    user.setName(name);
                    user.setEmail(email);
                    user.setAge(age);

                    try {
                        userDao.update(user);
                        System.out.println("Пользователь обновлен:\n" + user);
                    } catch (Exception e) {
                        System.out.println("Ошибка при обновлении: " + e.getMessage());
                    }
                }

                case 4 -> {
                    System.out.print("Введите id: ");
                    String idInput = sc.nextLine().trim();
                    if (!idInput.matches("\\d+")) {
                        System.out.println("Неверный формат id.");
                        continue;
                    }
                    long id = Long.parseLong(idInput);
                    User user = userDao.getById(id);
                    if (user != null) {
                        userDao.delete(id);
                        System.out.println("Пользователь удалён:\n" + user);
                    } else {
                        System.out.printf("Пользователь с id = %d не найден%n", id);
                    }
                }

                case 5 -> {
                    System.out.println("Выход из программы.");
                    return;
                }

                default -> System.out.println("Некорректный выбор.");
            }

            System.out.println();
        }
    }
}

