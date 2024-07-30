package com.example.capstone_project.controller.body.plan.download;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlanDownloadBody {
    @NotNull(message = "File Id can't be empty")
    private Long fileId;
}

