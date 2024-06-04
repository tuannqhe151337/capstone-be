package com.example.capstone_project.controller.body.term.create;

import com.example.capstone_project.entity.FinancialPlan;
import com.example.capstone_project.entity.FinancialReport;
import com.example.capstone_project.entity.TermStatus;
import com.example.capstone_project.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Table(schema = "capstone_v2", name = "terms")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CreateTermBody {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Name can not be empty")
    @Column(name = "name")
    private String name;

    @Column(name = "duration")
    private String duration;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", shape = JsonFormat.Shape.STRING)
    @Column(name = "start_date")
    private LocalDateTime startDate;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", shape = JsonFormat.Shape.STRING)
    @NotNull(message = "End date cannot be null")
    @Future(message = "End date must be in the future")
    @Column(name = "end_date")
    private LocalDateTime endDate;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", shape = JsonFormat.Shape.STRING)
    @Column(name = "plan_due_date")
    @NotNull(message = "Plan due date cannot be null")
    @Future(message = "Plan due date must be in the future")
    private LocalDateTime planDueDate;

}
