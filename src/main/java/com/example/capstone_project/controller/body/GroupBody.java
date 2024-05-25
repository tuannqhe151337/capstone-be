package com.example.capstone_project.controller.body;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GroupBody {
    @NotBlank
    private String name;
}
