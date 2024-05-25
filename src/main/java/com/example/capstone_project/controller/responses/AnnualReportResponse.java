package com.example.capstone_project.controller.responses;

import jakarta.persistence.Column;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;

public class AnnualReportResponse {
    private Long id;
    private Integer year;
    private Integer totalTerm;
    private Integer totalDepartment;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
