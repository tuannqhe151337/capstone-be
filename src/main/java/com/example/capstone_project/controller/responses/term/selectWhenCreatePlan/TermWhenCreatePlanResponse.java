package com.example.capstone_project.controller.responses.term.selectWhenCreatePlan;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TermWhenCreatePlanResponse {
    private Long termId;
    private String name;
    private String duration;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
