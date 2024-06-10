package com.example.capstone_project.controller.responses.term.getPlans;
import lombok.*;

import java.time.LocalDateTime;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class TermPlanDetailResponse{

    private Long id;
    private String name;
    private PlanStatusResponse planStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt ;
}
