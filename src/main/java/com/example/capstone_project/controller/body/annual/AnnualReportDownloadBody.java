package com.example.capstone_project.controller.body.annual;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnnualReportDownloadBody {
    @NotNull(message = "Annual report id can't be empty")
    private Long annualReportId;
}
