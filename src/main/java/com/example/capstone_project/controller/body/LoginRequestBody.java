package com.example.capstone_project.controller.body;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestBody {
    @NotBlank
    private String username;

    @NotBlank
    private String password;
}