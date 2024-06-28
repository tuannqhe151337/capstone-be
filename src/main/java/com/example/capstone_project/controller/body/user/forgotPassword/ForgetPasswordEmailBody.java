package com.example.capstone_project.controller.body.user.forgotPassword;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForgetPasswordEmailBody {
    @NotEmpty(message = "Email cannot be empty")
    private String email;
}
