package com.example.capstone_project.controller.body.user.delete;


import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.NumberFormat;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteUserBody {
    @NotNull(message = "Id cannot be null")
   // @Pattern(regexp = "^[0-9]+$", message = "Field must contain natural numbers")
    private Long id;
}
