package com.example.capstone_project.controller.body.user.resetPassword;


import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordBody {
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\W).{8,}$",
            message = "New password must be at least 8 characters long," +
                    " contain at least one special character," +
                    " one uppercase letter, and one lowercase letter")
    private String newPassword;
}
