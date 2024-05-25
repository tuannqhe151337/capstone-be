package com.example.capstone_project.controller.responses;

import jakarta.persistence.Column;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;

public class FinancialReportExpenseResponse {
    private Long id;
    private String name;
    private BigDecimal unitPrice;
    private Integer amount;
    private String projectName;
    private String supplierName;
    private String pic;
    private String note;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
