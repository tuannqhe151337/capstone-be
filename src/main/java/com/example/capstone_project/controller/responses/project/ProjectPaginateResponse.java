package com.example.capstone_project.controller.responses.project;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectPaginateResponse {
    private Long projectId;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
