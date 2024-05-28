package com.example.capstone_project.controller.responses.select_term;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
@Data
@Builder
public class TermResponse {
    private long termId;
    private String name;
    private String duration;
    private LocalDate startDate;
    private LocalDate endDate;
}
