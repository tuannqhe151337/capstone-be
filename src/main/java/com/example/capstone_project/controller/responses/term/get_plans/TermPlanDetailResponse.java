package com.example.capstone_project.controller.responses.term.get_plans;
import com.example.capstone_project.controller.responses.plan.detail.PlanDetailResponse;
import com.example.capstone_project.entity.BaseEntity;
import com.example.capstone_project.entity.PlanStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
