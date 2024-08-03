package com.example.capstone_project.controller.body.user.update;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateUserBody {
        @NotNull(message = "Id cannot be blank")
        private Long id;

        @NotEmpty(message = "Fullname cannot be empty")
        @Size(max = 100, message = "Fullname must be less than 100 characters")
        @Pattern(regexp = "^[a-zA-ZÀ-ỹ]+(?: [a-zA-ZÀ-ỹ]+)*$", message = "Full name must contain only letters and spaces")
        private String fullName;


        @NotBlank(message = "Email cannot be blank")
        private String email;

        @NotNull(message = "Department cannot be blank")
        @NotNull
        private Long departmentId;

        @NotBlank(message = "Phone number cannot be blank")
        @Size(min = 10, max = 15, message = "Phone number must be between 10 and 15 digits")
        private String phoneNumber;

        @NotNull(message = "Role cannot be null")
        private Long roleId;

        @NotNull(message = "Position cannot be null")
        private Long positionId;

        @NotNull(message = "DOB cannot be null")
        private LocalDateTime dob;

        @NotNull(message = "Address cannot be null")
        @NotBlank
        private String address;
}


