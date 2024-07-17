package com.example.capstone_project.controller.body.user.deactive;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeactiveUserBody {
    @NotNull(message = "Id cannot be null")
    private Long id;
}
