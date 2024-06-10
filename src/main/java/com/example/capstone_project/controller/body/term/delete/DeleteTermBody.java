package com.example.capstone_project.controller.body.term.delete;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeleteTermBody {
    @NotNull(message = "Id cannot be null")
    // @Pattern(regexp = "^[0-9]+$", message = "Field must contain natural numbers")
    private Long id;
}
