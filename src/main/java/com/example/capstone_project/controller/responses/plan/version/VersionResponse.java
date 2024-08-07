package com.example.capstone_project.controller.responses.plan.version;

import com.example.capstone_project.controller.responses.plan.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VersionResponse {
    private Long planFileId;
    private Integer version;
    private LocalDateTime publishedDate;
    private UserResponse uploadedBy;
}
