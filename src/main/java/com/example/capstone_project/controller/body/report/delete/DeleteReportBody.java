package com.example.capstone_project.controller.body.report.delete;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeleteReportBody {
    @NotNull(message = "Report id can't be null")
    private Long reportId;
}
