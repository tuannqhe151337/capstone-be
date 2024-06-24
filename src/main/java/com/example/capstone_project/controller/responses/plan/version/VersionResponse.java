package com.example.capstone_project.controller.responses.plan.version;

import com.example.capstone_project.controller.responses.plan.UserResponse;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class VersionResponse {
    private LocalDateTime publishedDate;
    private UserResponse uploadedBy;
}
