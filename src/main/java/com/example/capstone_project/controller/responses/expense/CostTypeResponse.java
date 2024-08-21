package com.example.capstone_project.controller.responses.expense;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CostTypeResponse {
    private Long costTypeId;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
