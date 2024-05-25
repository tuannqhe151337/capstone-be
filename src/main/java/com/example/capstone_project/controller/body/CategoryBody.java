package com.example.capstone_project.controller.body;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryBody {
    @NotBlank
    private String name;
    @NotBlank
    private String iconCode;
}
