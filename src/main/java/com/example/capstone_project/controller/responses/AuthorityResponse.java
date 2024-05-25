package com.example.capstone_project.controller.responses;

import jakarta.persistence.Column;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;

@Data
public class AuthorityResponse {
    private Long id;
    private String code;
    private String name;
}
