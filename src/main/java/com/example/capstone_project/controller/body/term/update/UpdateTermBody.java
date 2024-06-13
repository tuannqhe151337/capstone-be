package com.example.capstone_project.controller.body.term.update;

import com.example.capstone_project.entity.TermDuration;
import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTermBody {
    @NotNull(message = "ID cannot be empty")
    private Long id;

    @NotEmpty(message = "Name cannot be empty")
    private String name;

    @NotNull(message = "Duration cannot be null")
    @Enumerated(EnumType.STRING)
    private TermDuration duration ;

    @NotNull(message = "Start date cannot be null")
    private LocalDateTime startDate;

    @NotNull(message = "End date cannot be null")
    @Future(message = "End due date must be in the future")
    private LocalDateTime endDate;

    @NotNull(message = "Plan due date cannot be null")
    @Future(message = "Plan due date must be in the future")
    private LocalDateTime planDueDate;


    @AssertTrue(message = "Plan due date must be before end date")
    private boolean isPlanDueDateBeforeEndDate() {
        if (planDueDate == null || endDate == null) {
            return true; // Để @NotNull xử lý null check
        }
        return planDueDate.isBefore(endDate);
    }
}
