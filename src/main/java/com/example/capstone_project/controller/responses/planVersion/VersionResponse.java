package com.example.capstone_project.controller.responses.planVersion;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
@Data
@Builder
public class VersionResponse {
    private String version;
    private LocalDate publishedDate;
    private UserResponse uploadedBy;
}
