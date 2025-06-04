package com.example.userservice.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    @SuppressWarnings("unused")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private int age;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @SuppressWarnings("unused")
    public User(){

    }

    public Long getId() { return id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @SuppressWarnings("unused")
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public User(String name, String email, int age, LocalDateTime createdAt) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.createdAt = createdAt;
    }

    @Override
    public String toString () {
        return String.format(
                "id = %d\nимя = %s\nemail = %s\nвозраст = %d\nсоздан: %s",
                getId(), getName(), getEmail(), getAge(), getCreatedAt());
    }
}
