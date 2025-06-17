package com.example.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistDto {

    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

    @Min(0)
    private int age;
}
