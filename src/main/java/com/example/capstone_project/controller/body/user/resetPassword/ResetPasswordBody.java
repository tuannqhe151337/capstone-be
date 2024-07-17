package com.example.capstone_project.controller.body.user.resetPassword;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordBody {
    private String newPassword;
}
