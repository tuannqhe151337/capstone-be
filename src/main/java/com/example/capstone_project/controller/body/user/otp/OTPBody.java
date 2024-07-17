package com.example.capstone_project.controller.body.user.otp;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OTPBody {
    @NotEmpty(message = "OTP cannot be empty")
    private String otp;
}
