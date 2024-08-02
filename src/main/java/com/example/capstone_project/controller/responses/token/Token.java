package com.example.capstone_project.controller.responses.token;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data//toString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Token {
    @NotEmpty(message = "Token cannot be empty")
    private String token;
}
