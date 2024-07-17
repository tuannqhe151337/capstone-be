package com.example.capstone_project.controller.body.user.changePassword;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordBody {
    @NotEmpty(message = "Password cannot be null")
    private String oldPassword;
    @NotEmpty(message = "New password cannot be null")
    private String newPassword;

}
