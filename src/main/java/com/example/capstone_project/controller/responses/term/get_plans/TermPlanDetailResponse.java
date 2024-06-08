package com.example.capstone_project.controller.responses.term.get_plans;
import com.example.capstone_project.controller.responses.plan.detail.PlanDetailResponse;
import com.example.capstone_project.entity.BaseEntity;
import com.example.capstone_project.entity.PlanStatus;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class TermPlanDetailResponse extends BaseEntity {
    @NotNull(message = "Id cannot be null")
    private Long id;

    @NotEmpty(message = "Name cannot be empty ")
    private String name;

    @NotNull(message = "Status cannot be empty ")
    private PlanStatusResponse planStatus;


    private LocalDateTime createdAt;
    private LocalDateTime updatedAt ;
}
