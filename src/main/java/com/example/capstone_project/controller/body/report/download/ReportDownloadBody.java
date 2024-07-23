package com.example.capstone_project.controller.body.report.download;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportDownloadBody {
    @NotNull(message = "Report Id can't be empty")
    private Long reportId;
}
