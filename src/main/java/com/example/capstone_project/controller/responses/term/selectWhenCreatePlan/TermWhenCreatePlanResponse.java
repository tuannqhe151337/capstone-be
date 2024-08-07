package com.example.capstone_project.controller.responses.term.selectWhenCreatePlan;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TermWhenCreatePlanResponse {
    private Long termId;
    private String name;
    private String duration;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
