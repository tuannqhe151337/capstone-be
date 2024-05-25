package com.example.capstone_project.controller.responses;

import jakarta.persistence.Column;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;

public class TermStatusResponse {
    private Long id;
    private String name;
    private String iconCode;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
