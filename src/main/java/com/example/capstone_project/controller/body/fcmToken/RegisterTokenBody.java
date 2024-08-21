package com.example.capstone_project.controller.body.fcmToken;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterTokenBody {
    @NotNull(message = "FCM token can't be empty")
    private String token;
}
