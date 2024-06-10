package com.example.capstone_project.controller.body.term.create;

import com.example.capstone_project.entity.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CreateTermBody {
    @NotEmpty(message = "Name can not be empty")
    private String name;

    @NotNull(message = "Duration cannot be null")
    @Enumerated(EnumType.STRING)
    private TermDuration duration;

    @NotNull(message = "Start date cannot be null")
    private LocalDateTime startDate;

    @NotNull(message = "End date cannot be null")
    @Future(message = "End date must be in the future")
    private LocalDateTime endDate;

    @NotNull(message = "Plan due date cannot be null")
    @Future(message = "Plan due date must be in the future")
    private LocalDateTime planDueDate;
}
