package com.example.capstone_project.controller.body.user.changePassword;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\W).{8,}$",
            message = "New password must be at least 8 characters long," +
                    " contain at least one special character," +
                    " one uppercase letter, and one lowercase letter"
    )
    private String newPassword;

}
