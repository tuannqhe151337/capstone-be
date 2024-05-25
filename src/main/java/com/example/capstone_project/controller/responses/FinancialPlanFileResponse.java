package com.example.capstone_project.controller.responses;

import jakarta.persistence.Column;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

public class FinancialPlanFileResponse {
    private Long id;
    private String name;
    private String url;
    private String version;
    private LocalDate createdAt;
}
