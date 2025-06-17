package com.example.userservice.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVozvratDto {

    private Long id;
    private String name;
    private String email;
    private int age;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
