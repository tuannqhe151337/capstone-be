package com.example.capstone_project.controller.responses.report;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TermResponse {
    private long termId;
    private String name;
}
