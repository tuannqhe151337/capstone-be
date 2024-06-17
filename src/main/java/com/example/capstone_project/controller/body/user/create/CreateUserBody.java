package com.example.capstone_project.controller.body.user.create;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserBody {
    @NotEmpty(message = "Fullname cannot be empty")
    @Size(max = 100, message = "Fullname must be less than 100 characters")
    private String fullName;

    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Email is invalid")
    private String email;

    @NotEmpty(message = "Phone number cannot be empty")
    @Pattern(regexp = "\\d{10,15}", message = "Phone number must be between 10 and 15 digits")
    private String phoneNumber;

    @NotNull(message = "Department Id cannot be null")
    private Long departmentId;

    @NotNull(message = "Role Id cannot be null")
    private Long roleId;

    @NotNull(message = "Position id cannot be null")
    private Long positionId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", shape = JsonFormat.Shape.STRING)
    @Past(message = "DOB must be in the past")
    private LocalDateTime dob;

    @NotEmpty(message = "Address cannot be empty")
    @Size(max = 200, message = "Address must be less than 200 characters")
    private String address;
    @Size(max = 200, message = "Note must be less than 200 characters")
    private String note;

}

