package com.example.capstone_project.controller.body.term.create;

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

    @NotNull(message = "ID can not be empty")
    private Long id;

    @NotEmpty(message = "Name can not be empty")
    private String name;

    @NotEmpty(message = "Duration cannot be empty")
    private String duration;

    @NotNull(message = "Start date cannot be null")
    private LocalDateTime startDate;

    @NotNull(message = "End date cannot be null")
    @Future(message = "End date must be in the future")
    private LocalDateTime endDate;

    @NotNull(message = "Plan due date cannot be null")
    @Future(message = "Plan due date must be in the future")
    private LocalDateTime planDueDate;

}
