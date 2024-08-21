package com.example.capstone_project.controller.responses.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TermResponse {
    private long termId;
    private String name;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean allowReupload;
    private LocalDateTime reuploadStartDate;
    private LocalDateTime reuploadEndDate;
    private LocalDateTime finalEndTermDate;
}
