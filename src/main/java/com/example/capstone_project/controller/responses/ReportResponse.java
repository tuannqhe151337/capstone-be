package com.example.capstone_project.controller.responses;

import jakarta.persistence.Column;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ReportResponse {
    private Long id;
    private BigDecimal totalExpense;
    private BigDecimal biggestExpenditure;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
