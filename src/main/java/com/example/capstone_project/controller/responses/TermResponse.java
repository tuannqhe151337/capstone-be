package com.example.capstone_project.controller.responses;

import jakarta.persistence.Column;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;

public class TermResponse {
    private Long id;
    private String name;
    private String duration;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate planDueDate;
    private LocalDate reportDueDate;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
